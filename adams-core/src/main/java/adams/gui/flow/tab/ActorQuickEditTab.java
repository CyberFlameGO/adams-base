/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * ActorQuickEditTab.java
 * Copyright (C) 2015-2018 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.flow.tab;

import adams.core.CloneHandler;
import adams.core.Utils;
import adams.core.option.OptionHandler;
import adams.core.option.OptionUtils;
import adams.flow.core.Actor;
import adams.gui.core.BaseButton;
import adams.gui.core.GUIHelper;
import adams.gui.event.ActorChangeEvent;
import adams.gui.event.ActorChangeEvent.Type;
import adams.gui.flow.tree.Node;
import adams.gui.flow.tree.Tree;
import adams.gui.flow.tree.postprocessor.AbstractEditPostProcessor;
import adams.gui.goe.GenericObjectEditor;
import adams.gui.goe.GenericObjectEditorPopupMenu;
import adams.gui.goe.PropertySheetPanel;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

/**
 * Tab for displaying the GOE for the currently selected actor for quickly
 * editing options.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class ActorQuickEditTab
  extends AbstractEditorTab
  implements SelectionAwareEditorTab {

  /** for serialization. */
  private static final long serialVersionUID = 3860012648562358118L;

  /** the text when no actor selected. */
  public final static String NO_SELECTION_TEXT = "<html><center><b>Please select an actor</b></center></html>";

  /**
   * Handles the GUI side of editing values.
   *
   * @version $Revision$
   */
  public static class OptionsPanel
    extends JPanel {

    /** for serialization. */
    static final long serialVersionUID = 3656028520876011335L;

    /** the panel itself. */
    protected OptionsPanel m_Self;

    /** the owner. */
    protected ActorQuickEditTab m_Owner;

    /** the editor this panel is for. */
    protected GenericObjectEditor m_Editor;

    /** the current object. */
    protected Object m_Object;

    /** the backup of the object. */
    protected Object m_Backup;

    /** the current tree path. */
    protected TreePath m_TreePath;

    /** The component that performs classifier customization. */
    protected PropertySheetPanel m_PanelProperties;

    /** The name of the current class. */
    protected JLabel m_LabelClassname;

    /** apply button. */
    protected BaseButton m_ButtonApply;

    /** revert button. */
    protected BaseButton m_ButtonRevert;

    /** the button for copy/paste menu. */
    protected BaseButton m_ButtonCopyPaste;

    /** the top panel with the classname and choose button. */
    protected JPanel m_TopPanel;

    /** whether to ignore selection changes to the combobox. */
    protected boolean m_IgnoreChanges;

    /**
     * Creates the GUI editor component.
     *
     * @param owner	the tab this panel belongs to
     * @param editor	the GOE editor to use
     */
    public OptionsPanel(ActorQuickEditTab owner, GenericObjectEditor editor) {
      m_Owner  = owner;
      m_Editor = editor;
      m_Editor.addPropertyChangeListener((PropertyChangeEvent evt) -> {
	m_Object = m_Editor.getValue();
	m_Backup = copyObject(m_Object);
	updateProperties();
      });

      m_Object = m_Editor.getValue();
      m_Backup = copyObject(m_Object);

      m_LabelClassname = new JLabel("None");

      m_PanelProperties = new PropertySheetPanel();

      m_ButtonApply = new BaseButton("Apply");
      m_ButtonApply.setEnabled(true);
      m_ButtonApply.setToolTipText("Apply changes");
      m_ButtonApply.setMnemonic('A');
      m_ButtonApply.addActionListener((ActionEvent e) -> {
	m_Object = m_PanelProperties.getTarget();
	if (m_TreePath == null) {
	  System.err.println("No path linking back to actor tree, cannot apply settings!");
	  return;
	}
	Tree tree = m_Owner.getCurrentPanel().getTree();
	Node node = (Node) m_TreePath.getLastPathComponent();
	Node parent = (Node) node.getParent();
	node.setActor((Actor) m_Object);
	tree.updateActorName(node);
	tree.setModified(true);
	tree.nodeStructureChanged(node);
	tree.notifyActorChangeListeners(new ActorChangeEvent(tree, node, Type.MODIFY));
	if (!tree.getIgnoreNameChanges())
	  AbstractEditPostProcessor.apply(tree, ((parent != null) ? parent.getActor() : null), (Actor) m_Backup, node.getActor());
	m_Backup = copyObject(m_Object);
      });

      m_ButtonRevert = new BaseButton(GUIHelper.getIcon("undo.gif"));
      m_ButtonRevert.setEnabled(true);
      m_ButtonRevert.setToolTipText("Revert changes");
      m_ButtonRevert.addActionListener((ActionEvent e) -> {
	if (m_Backup != null) {
	  m_Editor.setValue(copyObject(m_Backup));
	  m_Editor.firePropertyChange();
	}
      });

      setLayout(new BorderLayout());

      m_ButtonCopyPaste = new BaseButton("...");
      m_ButtonCopyPaste.setToolTipText("Displays copy/paste/favorites action menu");
      m_ButtonCopyPaste.addActionListener((ActionEvent e) -> {
	GenericObjectEditorPopupMenu menu = new GenericObjectEditorPopupMenu(m_Editor, m_ButtonCopyPaste);
	menu.show(m_ButtonCopyPaste, 0, m_ButtonCopyPaste.getHeight());
      });
      m_TopPanel = new JPanel(new BorderLayout());
      m_TopPanel.add(m_LabelClassname, BorderLayout.CENTER);
      JPanel chooseButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      chooseButtonPanel.add(m_ButtonCopyPaste);
      m_TopPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      m_TopPanel.add(chooseButtonPanel, BorderLayout.EAST);
      add(m_TopPanel, BorderLayout.NORTH);

      JPanel childPanel = new JPanel(new BorderLayout());
      childPanel.add(m_PanelProperties, BorderLayout.CENTER);
      add(childPanel, BorderLayout.CENTER);

      JPanel allButs = new JPanel(new GridLayout(1, 2));
      JPanel rightButs = new JPanel();
      allButs.add(rightButs, BorderLayout.EAST);
      allButs.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      rightButs.setLayout(new FlowLayout(FlowLayout.RIGHT));
      rightButs.add(m_ButtonApply);
      rightButs.add(m_ButtonRevert);
      add(allButs, BorderLayout.SOUTH);

      if (m_Editor.getClassType() != null) {
	if (m_Editor.getValue() != null) {
	  updateProperties();
	}
      }
    }

    /**
     * Makes a copy of an object using serialization.
     *
     * @param source 	the object to copy
     * @return 		a copy of the source object
     */
    protected Object copyObject(Object source) {
      Object 	result;

      if (source instanceof OptionHandler)
	result = OptionUtils.shallowCopy(source);
      else if (source instanceof CloneHandler)
	result = ((CloneHandler) source).getClone();
      else
	result = Utils.deepCopy(source);

      return result;
    }

    /**
     * Updates the property sheet.
     */
    public void updateProperties() {
      String 		classname;

      m_IgnoreChanges = true;

      classname = "None";
      if (m_Object != null)
	classname = m_Object.getClass().getName();
      m_LabelClassname.setText(classname);
      m_PanelProperties.setTarget(m_Object);

      m_IgnoreChanges = false;
    }

    /**
     * Sets the current tree path.
     *
     * @param value	the current path, can be null
     */
    public void setTreePath(TreePath value) {
      m_TreePath = value;
      m_ButtonApply.setEnabled(value != null);
    }

    /**
     * Returns the current tree path.
     *
     * @return		the current path, null if none set
     */
    public TreePath getTreePath() {
      return m_TreePath;
    }
  }

  /** the GOE editor. */
  protected GenericObjectEditor m_Editor;

  /** for displaying the options. */
  protected OptionsPanel m_PanelEditor;

  /** for displaying "cannot display". */
  protected JPanel m_PanelError;

  /**
   * Initializes the widgets.
   */
  protected void initGUI() {
    super.initGUI();

    setLayout(new BorderLayout());

    m_Editor = new GenericObjectEditor();
    m_Editor.setClassType(Actor.class);
    m_Editor.setCanChangeClassInDialog(false);
    m_PanelEditor = new OptionsPanel(this, m_Editor);
    m_PanelError = new JPanel(new FlowLayout(FlowLayout.CENTER));
    m_PanelError.add(new JLabel(NO_SELECTION_TEXT));

    add(m_PanelError, BorderLayout.CENTER);
  }

  /**
   * Returns the title of the tab.
   *
   * @return		the title
   */
  public String getTitle() {
    return "Quick Edit";
  }

  /**
   * Returns whether the tab is enabled by default.
   *
   * @return		true if enabled by default
   */
  public boolean enabledByDefault() {
    return false;
  }

  /**
   * Notifies the tab of the currently selected actors.
   *
   *
   * @param paths	the selected paths
   * @param actors	the currently selected actors
   */
  public void actorSelectionChanged(TreePath[] paths, Actor[] actors) {
    if (actors.length != 1) {
      remove(m_PanelEditor);
      add(m_PanelError, BorderLayout.CENTER);
      return;
    }

    m_Editor.setValue(actors[0]);
    m_PanelEditor.setTreePath(paths[0]);
    remove(m_PanelError);
    add(m_PanelEditor, BorderLayout.CENTER);
  }
}

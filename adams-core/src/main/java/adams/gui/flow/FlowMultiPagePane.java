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
 * FlowTabbedPane.java
 * Copyright (C) 2011-2018 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.flow;

import adams.core.CleanUpHandler;
import adams.core.Properties;
import adams.flow.core.Actor;
import adams.gui.core.ConsolePanel;
import adams.gui.core.MultiPagePane;
import adams.gui.flow.tab.RegisteredDisplaysTab;
import adams.gui.flow.tree.Tree;

import javax.swing.event.ChangeEvent;
import javax.swing.tree.TreePath;
import java.io.File;
import java.lang.reflect.Constructor;

/**
 * Specialized tabbed pane for Flow panels.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class FlowMultiPagePane
  extends MultiPagePane
  implements CleanUpHandler {

  /** for serialization. */
  private static final long serialVersionUID = -1675887825433207074L;

  /** the owning editor. */
  protected FlowEditorPanel m_Owner;

  /** the flowpanel class to use. */
  protected Class m_FlowPanelClass;

  /**
   * Initializes the tabbed pane.
   *
   * @param owner	the owning editor
   */
  public FlowMultiPagePane(FlowEditorPanel owner) {
    super();

    m_Owner = owner;

    setPageCloseApprover((MultiPagePane source, int index) -> {
      FlowPanel panel = getPanelAt(index);
      boolean result = checkForModified(panel);
      // to avoid second popup from checkModified() in removeTab method
      if (result && panel.isModified())
	panel.setModified(false);
      return result;
    });

    addChangeListener((ChangeEvent e) -> pageSelected(e));
  }

  /**
   * Returns the owning editor.
   *
   * @return		the owning editor
   */
  public FlowEditorPanel getOwner() {
    return m_Owner;
  }

  /**
   * Adds a new tab with an empty panel.
   *
   * @return		the new flow panel
   */
  public FlowPanel newPanel() {
    FlowPanel	result;
    Properties	props;
    String	clsname;
    Constructor	constr;

    if (m_FlowPanelClass == null) {
      props   = FlowEditorPanel.getPropertiesEditor();
      clsname = props.getProperty("FlowPanelClass", FlowPanel.class.getName());
      try {
        m_FlowPanelClass = Class.forName(clsname);
      }
      catch (Exception e) {
	m_FlowPanelClass = FlowPanel.class;
	ConsolePanel.getSingleton().append("Failed to instantiate flow panel class: " + clsname, e);
      }
    }

    try {
      constr = m_FlowPanelClass.getConstructor(FlowMultiPagePane.class);
      result = (FlowPanel) constr.newInstance(this);
    }
    catch (Exception e) {
      ConsolePanel.getSingleton().append("Failed to instantiate flow panel class: " + m_FlowPanelClass.getClass().getName(), e);
      result = new FlowPanel(this);
    }

    addPage(result.getTitle(), result);
    setSelectedPage(result);

    return result;
  }

  /**
   * Returns the number of panels in the tabbed pane.
   *
   * @return		the number of panels
   */
  public int getPanelCount() {
    return getPageCount();
  }

  /**
   * Returns the panel at the specified position.
   *
   * @param index	the tab index
   * @return		the requested panel
   */
  public FlowPanel getPanelAt(int index) {
    return (FlowPanel) getPageAt(index);
  }

  /**
   * Returns the index of the panel with the specified title.
   *
   * @param title	the title to check
   * @return		the panel, -1 if not found
   */
  public int indexOfPanel(String title) {
    int		result;
    int		i;
    FlowPanel	panel;

    result = -1;

    for (i = 0; i < getPanelCount(); i++) {
      panel = getPanelAt(i);
      if (panel.getTitle().equals(title)) {
	result = i;
	break;
      }
    }

    return result;
  }

  /**
   * Returns whether a panel with the specified title exists.
   *
   * @param title	the title to look for
   * @return		true if it exists
   */
  public boolean hasPanel(String title) {
    return (indexOfPanel(title) > -1);
  }

  /**
   * Returns if a panel is available.
   *
   * @return		true if a panel available
   */
  public boolean hasCurrentPanel() {
    return (getCurrentPanel() != null);
  }

  /**
   * Returns the currently selected panel.
   *
   * @return		the current panel, null if not available
   */
  public FlowPanel getCurrentPanel() {
    if (getSelectedIndex() != -1)
      return (FlowPanel) getPageAt(getSelectedIndex());
    else
      return null;
  }

  /**
   * Returns the tree at the specified position.
   *
   * @param index	the tab index
   * @return		the requested tree
   */
  public Tree getTreeAt(int index) {
    return getPanelAt(index).getTree();
  }

  /**
   * Returns whether a tree is currently selected.
   *
   * @return		true if a tree available
   */
  public boolean hasCurrentTree() {
    return (getCurrentTree() != null);
  }

  /**
   * Returns the currently selected tree.
   *
   * @return		the currently selected tree, null if not available
   */
  public Tree getCurrentTree() {
    if (getSelectedIndex() != -1)
      return getPanelAt(getSelectedIndex()).getTree();
    else
      return null;
  }

  /**
   * Cleans up data structures, frees up memory.
   */
  public void cleanUp() {
    int		i;

    for (i = 0; i < getPanelCount(); i++)
      getPanelAt(i).cleanUp();
  }

  /**
   * Returns whether we can proceed with the operation or not, depending on
   * whether the user saved the flow or discarded the changes.
   *
   * @return		true if safe to proceed
   */
  protected boolean checkForModified() {
    if (m_Owner == null)
      return true;
    return m_Owner.checkForModified();
  }

  /**
   * Returns whether we can proceed with the operation or not, depending on
   * whether the user saved the flow or discarded the changes.
   *
   * @param panel	the panel to check
   * @return		true if safe to proceed
   */
  protected boolean checkForModified(FlowPanel panel) {
    if (m_Owner == null)
      return true;
    return m_Owner.checkForModified(panel);
  }

  /**
   * Gets called when a tab gets selected.
   * 
   * @param e		the event that triggered the action
   */
  protected void pageSelected(ChangeEvent e) {
    // actor tabs
    if ((getPanelCount() == 0) || (getSelectedIndex() == -1))
      m_Owner.getTabs().notifyTabs(
	  new TreePath[0],
	  new Actor[0]);
    else
      m_Owner.getTabs().notifyTabs(
	  m_Owner.getCurrentPanel().getTree().getSelectionPaths(),
	  m_Owner.getCurrentPanel().getTree().getSelectedActors());
    
    // title
    updateOwnerTitle();

    // statusbar
    if (hasCurrentPanel() && getCurrentPanel().isRunning())
      getOwner().showStatus("Running");
    else
      getOwner().showStatus("");

    // ensure that tabs are visible
    if (hasCurrentPanel()) {
      getOwner().getTabs().setVisible(RegisteredDisplaysTab.class, getCurrentPanel().hasRegisteredDisplays(), false);
    }
    
    // current directory
    updateCurrentDirectory();
    
    // update in general
    m_Owner.update();
  }

  /**
   * Updates the owner's title.
   */
  protected void updateOwnerTitle() {
    String	title;

    if (m_Owner == null)
      return;

    if (getPanelCount() == 0) {
      title = FlowEditorPanel.DEFAULT_TITLE;
      m_Owner.setParentTitle(title);
    }
    else if (getCurrentPanel() != null) {
      title = getCurrentPanel().generateTitle();
      m_Owner.setParentTitle(title);
    }
  }

  /**
   * Updates the title.
   *
   * @param panel	the panel to update the title for
   * @param title	the new title
   */
  public void updateTitle(FlowPanel panel, String title) {
    int		index;

    index = indexOfPage(panel);
    if (index == -1)
      return;

    setTitleAt(index, title);
    updateOwnerTitle();
  }

  /**
   * Updates the current working directory of the owner's file chooser to
   * the one represented by this flow.
   */
  public void updateCurrentDirectory() {
    File	file;
    
    if (!hasCurrentPanel())
      return;
    
    file = getCurrentPanel().getCurrentFile();
    if (file == null)
      return;
    
    getOwner().setCurrentDirectory(file.getParentFile());
  }

  /**
   * Removes the tab at <code>index</code>.
   * After the component associated with <code>index</code> is removed,
   * its visibility is reset to true to ensure it will be visible
   * if added to other containers.
   *
   * @param index the index of the tab to be removed
   */
  @Override
  public PageContainer removePageAt(int index) {
    PageContainer	result;
    FlowPanel		panel;

    if (index < 0)
      return null;

    panel = getPanelAt(index);
    if (panel.isRunning())
      panel.stop(true);
    else
      panel.cleanUp();

    result = super.removePageAt(index);

    updateOwnerTitle();

    return result;
  }
}

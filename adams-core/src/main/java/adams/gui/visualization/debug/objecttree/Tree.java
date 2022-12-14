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
 * Tree.java
 * Copyright (C) 2011-2022 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.visualization.debug.objecttree;

import adams.core.Utils;
import adams.core.option.OptionHandler;
import adams.data.textrenderer.AbstractTextRenderer;
import adams.gui.chooser.ObjectExporterFileChooser;
import adams.gui.core.BasePopupMenu;
import adams.gui.core.BaseTree;
import adams.gui.core.GUIHelper;
import adams.gui.core.ImageManager;
import adams.gui.core.MouseUtils;
import adams.gui.dialog.ApprovalDialog;
import adams.gui.visualization.debug.InspectionPanel;
import adams.gui.visualization.debug.inspectionhandler.AbstractInspectionHandler;
import adams.gui.visualization.debug.objectexport.AbstractObjectExporter;
import adams.gui.visualization.debug.propertyextractor.AbstractPropertyExtractor;
import com.github.fracpete.jclipboardhelper.ClipboardHelper;

import javax.swing.JMenuItem;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyEditorManager;
import java.io.File;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Specialized tree that displays the properties of an object.
 * <br><br>
 * In order to avoid loops, a HashSet is used for keeping track of the processed
 * objects. Of course, custom equals(Object)/compareTo(Object) methods will
 * interfere with this mechanism.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class Tree
  extends BaseTree {

  /** for serialization. */
  private static final long serialVersionUID = -127345486742553561L;

  /** the label for the hashcode. */
  public final static String LABEL_HASHCODE = "hashCode";

  /** the maximum number of array/list elements to show. */
  public final static int MAX_ITEMS = 100;

  /** the maximum depth of the tree. */
  public final static int MAX_DEPTH = 10;

  /** the current object. */
  protected transient Object m_Object;

  /** the search string. */
  protected String m_SearchString;

  /** the search pattern. */
  protected Pattern m_SearchPattern;

  /** whether the search is using a regular expression. */
  protected boolean m_IsRegExp;

  /** filechooser for exporting objects. */
  protected ObjectExporterFileChooser m_FileChooser;

  /** the maximum depth to use. */
  protected int m_MaxDepth;

  /** caching the class / extractors relation. */
  protected static Map<Class,List<AbstractPropertyExtractor>> m_ExtractorCache;

  /** caching the class / inspection handler relation. */
  protected static Map<Class,List<AbstractInspectionHandler>> m_InspectionHandlerCache;

  /**
   * Initializes the tree.
   */
  public Tree() {
    super();

    if (m_ExtractorCache == null) {
      m_ExtractorCache         = new HashMap<>();
      m_InspectionHandlerCache = new HashMap<>();
    }

    m_SearchString  = null;
    m_SearchPattern = null;
    m_IsRegExp      = false;
    m_FileChooser   = null;
    m_MaxDepth      = MAX_DEPTH;
    setShowsRootHandles(true);
    setRootVisible(true);
    setCellRenderer(new Renderer());
    buildTree(null);

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
	TreePath path = getPathForLocation(e.getX(), e.getY());
        if ((path != null) && MouseUtils.isRightClick(e)) {
	  showPopup(e);
	  e.consume();
	}

	if (!e.isConsumed())
	  super.mouseClicked(e);
      }
    });
  }

  /**
   * Sets the maximum depth to use.
   *
   * @param value	the depth
   */
  public void setMaxDepth(int value) {
    m_MaxDepth = value;
  }

  /**
   * Returns the current maximum depth.
   *
   * @return		the depth
   */
  public int getMaxDepth() {
    return m_MaxDepth;
  }

  /**
   * Builds the tree from the given object.
   *
   * @param root	the object to build the tree from, null for empty tree
   */
  protected void buildTree(Object root) {
    DefaultTreeModel		model;
    Node		rootNode;

    if (root == null) {
      model = new DefaultTreeModel(null);
    }
    else {
      rootNode = buildTree(null, null, root, NodeType.NORMAL, 0);
      model    = new DefaultTreeModel(rootNode);
    }

    setModel(model);
  }

  /**
   * Adds the array below the parent.
   *
   * @param parent	the parent to add the array to
   * @param obj		the array to add
   * @param depth	the current depth
   */
  protected void addArray(Node parent, Object obj, int depth) {
    int		len;
    int		i;
    Object	value;
    Node	child;

    len = Array.getLength(obj);
    for (i = 0; (i < len) && (i < MAX_ITEMS); i++) {
      value = Array.get(obj, i);
      if (value != null) {
	buildTree(parent, "[" + (i+1) + "]", value, NodeType.ARRAY_ELEMENT, depth + 1);
      }
      else {
	child = new Node("[" + (i+1) + "]", null, NodeType.ARRAY_ELEMENT);
	parent.add(child);
      }
    }
    if (len > MAX_ITEMS) {
      child = new Node("[" + MAX_ITEMS + "-" + len + "]", "skipped", NodeType.ARRAY_ELEMENT);
      parent.add(child);
    }
  }

  /**
   * Checks the label against the current search setup.
   *
   * @param label	the label to check
   * @return		true if a match and should be added
   */
  protected boolean matches(String label) {
    boolean	result;

    result = true;

    if (m_SearchString != null) {
      if (m_SearchPattern != null)
	result = m_SearchPattern.matcher(label).matches();
      else
	result = label.contains(m_SearchString);
    }

    return result;
  }

  /**
   * Builds the tree recursively.
   *
   * @param parent	the parent to add the object to (null == root)
   * @param property	the name of the property the object belongs to (null == root)
   * @param obj		the object to add
   * @param type	the type of node
   * @param depth	the current depth
   * @return		the generated node
   */
  protected Node buildTree(Node parent, String property, Object obj, NodeType type, int depth) {
    Node				result;
    List<AbstractPropertyExtractor>	extractors;
    List<AbstractInspectionHandler>	handlers;
    Hashtable<String,Object>		additional;
    Object				current;
    String				label;
    HashSet<String>			labels;
    int					i;
    boolean				add;

    // too deep?
    if (depth >= m_MaxDepth) {
      result = new Node(property, "...", type);
      if (parent != null)
	parent.add(result);
      return result;
    }

    result = new Node(property, obj, type);
    if (parent != null)
      parent.add(result);

    // Object's hashcode
    if (!Utils.isPrimitive(obj) && matches(LABEL_HASHCODE))
      result.add(new Node(LABEL_HASHCODE, obj.hashCode(), NodeType.HASHCODE));

    // array?
    if (obj.getClass().isArray())
      addArray(result, obj, depth);

    labels = new HashSet<>();

    // child properties
    try {
      if (m_ExtractorCache.containsKey(obj.getClass())) {
	extractors = m_ExtractorCache.get(obj.getClass());
      }
      else {
	extractors = AbstractPropertyExtractor.getExtractors(obj);
	m_ExtractorCache.put(obj.getClass(), extractors);
      }
      for (AbstractPropertyExtractor extractor: extractors) {
	extractor.setCurrent(obj);
	for (i = 0; i < extractor.size(); i++) {
	  current = extractor.getValue(i);
	  if (current != null) {
	    label = extractor.getLabel(i);
	    add = matches(label)
	      || (current instanceof OptionHandler)
	      || (current.getClass().isArray());
	    add = add && !labels.contains(label);
	    if (add) {
	      labels.add(label);
	      buildTree(result, label, current, NodeType.NORMAL, depth + 1);
	    }
	  }
	}
      }
    }
    catch (Exception e) {
      System.err.println("Failed to obtain property descriptors for: " + obj);
      e.printStackTrace();
    }

    // additional values obtained through inspection handlers
    if (m_InspectionHandlerCache.containsKey(obj.getClass())) {
      handlers = m_InspectionHandlerCache.get(obj.getClass());
    }
    else {
      handlers = AbstractInspectionHandler.getHandler(obj);
      m_InspectionHandlerCache.put(obj.getClass(), handlers);
    }
    for (AbstractInspectionHandler handler: handlers) {
      additional = handler.inspect(obj);
      for (String key: additional.keySet()) {
	if (matches(key) && !labels.contains(key)) {
	  labels.add(key);
	  buildTree(result, key, additional.get(key), NodeType.NORMAL, depth + 1);
	}
      }
    }

    return result;
  }

  /**
   * Sets the object to display.
   *
   * @param value	the object
   */
  public void setObject(Object value) {
    m_Object = value;
    buildTree(m_Object);
  }

  /**
   * Returns the object currently displayed.
   *
   * @return		the object
   */
  public Object getObject() {
    return m_Object;
  }

  /**
   * Attempts to select the specified property path.
   *
   * @param parent	the parent node
   * @param path	the path to select (and open in the tree)
   * @param index	the index in the path array
   * @return		true if successfully selected
   */
  protected boolean selectPropertyPath(Node parent, String[] path, int index) {
    boolean	result;
    Node	child;
    int		i;

    if (parent == null)
      return false;

    result = false;

    for (i = 0; i < parent.getChildCount(); i++) {
      child = (Node) parent.getChildAt(i);
      if (child.getProperty().equals(path[index])) {
	if (index < path.length - 1) {
	  result = selectPropertyPath(child, path, index + 1);
	}
	else {
	  result = true;
	  setSelectionPath(new TreePath(child.getPath()));
	}
      }
    }

    return result;
  }

  /**
   * Attempts to select the specified property path.
   *
   * @param path	the path to select (and open in the tree)
   * @return		true if successfully selected
   */
  public boolean selectPropertyPath(String[] path) {
    return selectPropertyPath((Node) getModel().getRoot(), path, 0);
  }

  /**
   * Initiates the search.
   *
   * @param search	the search string
   * @param isRegExp	whether the search is using a regular expression
   */
  public void search(String search, boolean isRegExp) {
    if (search.trim().length() == 0) {
      search   = null;
      isRegExp = false;
    }

    m_SearchString = search;
    m_IsRegExp     = isRegExp;

    if ((m_SearchString != null) && m_IsRegExp) {
      try {
	m_SearchPattern = Pattern.compile(m_SearchString);
      }
      catch (Exception e) {
	m_SearchPattern = null;
      }
    }
    else {
      m_SearchPattern = null;
    }

    buildTree(m_Object);
  }

  /**
   * Returns the file chooser to use.
   *
   * @param cls		the class that the exporters must be able to handle, null for all
   * @return		the file chooser
   */
  protected ObjectExporterFileChooser getFileChooser(Class cls) {
    if (m_FileChooser == null)
      m_FileChooser = new ObjectExporterFileChooser();
    m_FileChooser.setCurrentClass(cls);
    return m_FileChooser;
  }

  /**
   * Returns whether the object can be edited.
   *
   * @param obj		the object to check
   * @return		true if editable
   */
  protected boolean canEdit(Object obj) {
    if (obj == null)
      return false;
    if (obj.getClass().isArray())
      return true;
    return (PropertyEditorManager.findEditor(obj.getClass()) != null);
  }

  /**
   * Brings up a popup menu.
   *
   * @param e		the mouse event that triggered the popup menu
   */
  protected void showPopup(MouseEvent e) {
    BasePopupMenu menu;
    JMenuItem				menuitem;
    TreePath 				path;
    final Node 				node;
    final Object			obj;

    path = getPathForLocation(e.getX(), e.getY());
    if (path == null)
      return;

    node = (Node) path.getLastPathComponent();
    obj  = node.getUserObject();

    menu = new BasePopupMenu();

    menuitem = new JMenuItem("Copy", ImageManager.getIcon("copy.gif"));
    menuitem.setEnabled(obj != null);
    menuitem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        copyToClipboard(obj);
      }
    });
    menu.add(menuitem);

    menuitem = new JMenuItem("Export...", ImageManager.getIcon("save.gif"));
    menuitem.setEnabled(obj != null);
    menuitem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	export(obj);
      }
    });
    menu.add(menuitem);

    menuitem = new JMenuItem("Inspect...", ImageManager.getIcon("object.gif"));
    menuitem.setEnabled(obj != null);
    menuitem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	inspect(node.getPropertyPath(), obj);
      }
    });
    menu.add(menuitem);

    menu.showAbsolute(this, e);
  }

  /**
   * Copies the object's string plain text rendering to the clipboard.
   *
   * @param obj		the object to copy
   */
  protected void copyToClipboard(Object obj) {
    ClipboardHelper.copyToClipboard(AbstractTextRenderer.renderObject(obj));
  }

  /**
   * Exports the object to a file.
   *
   * @param obj		the object to export
   */
  protected void export(Object obj) {
    ObjectExporterFileChooser	fileChooser;
    int 			retVal;
    File 			file;
    AbstractObjectExporter 	exporter;
    String 			msg;

    fileChooser = getFileChooser(obj.getClass());
    retVal = fileChooser.showSaveDialog(this);
    if (retVal != ObjectExporterFileChooser.APPROVE_OPTION)
      return;

    file     = fileChooser.getSelectedFile();
    exporter = fileChooser.getWriter();
    msg      = exporter.export(obj, file);
    if (msg != null)
      GUIHelper.showErrorMessage(
	Tree.this, "Failed to export object to '" + file + "'!\n" + msg);
  }

  /**
   * Inspects the object in a separate dialog.
   *
   * @param path 	the property path
   * @param obj		the object to inspect
   */
  protected void inspect(String[] path, Object obj) {
    ApprovalDialog	dialog;
    InspectionPanel	panel;

    panel = new InspectionPanel();
    panel.setCurrent(obj);

    if (getParentDialog() != null)
      dialog = new ApprovalDialog(getParentDialog(), ModalityType.MODELESS);
    else
      dialog = new ApprovalDialog(getParentFrame(), false);
    dialog.setDefaultCloseOperation(ApprovalDialog.DISPOSE_ON_CLOSE);
    dialog.setTitle("Inspecting: " + Utils.flatten(path, "."));
    dialog.setDiscardVisible(false);
    dialog.setCancelVisible(false);
    dialog.setApproveVisible(true);
    dialog.setApproveCaption("Close");
    dialog.setApproveMnemonic('l');
    dialog.getContentPane().add(panel, BorderLayout.CENTER);
    dialog.setSize(GUIHelper.makeWider(GUIHelper.getDefaultDialogDimension()));
    dialog.setLocationRelativeTo(dialog.getParent());
    dialog.setVisible(true);
  }
}
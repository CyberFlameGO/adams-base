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
 * SpreadSheetExplorer.java
 * Copyright (C) 2016-2017 University of Waikato, Hamilton, New Zealand
 */

package adams.gui.visualization.spreadsheet;

import adams.core.CleanUpHandler;
import adams.core.Properties;
import adams.core.Range;
import adams.core.StatusMessageHandler;
import adams.core.io.PlaceholderFile;
import adams.data.io.output.CsvSpreadSheetWriter;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import adams.env.Environment;
import adams.env.SpreadSheetExplorerDefinition;
import adams.gui.core.BasePanel;
import adams.gui.core.BaseStatusBar;
import adams.gui.core.BaseTabbedPane;
import adams.gui.core.BaseTable;
import adams.gui.core.GUIHelper;
import adams.gui.core.MenuBarProvider;
import adams.gui.core.RecentFilesHandler;
import adams.gui.core.SearchPanel;
import adams.gui.core.SearchPanel.LayoutType;
import adams.gui.event.DataChangeEvent;
import adams.gui.event.DataChangeListener;
import adams.gui.event.RecentItemEvent;
import adams.gui.event.RecentItemListener;
import adams.gui.event.SearchEvent;
import adams.gui.goe.GenericObjectEditorDialog;
import adams.gui.sendto.SendToActionSupporter;
import adams.gui.sendto.SendToActionUtils;
import adams.gui.visualization.container.ContainerListManager;
import adams.gui.visualization.container.ContainerTable;
import adams.gui.visualization.core.ColorProvider;
import adams.gui.visualization.core.Paintlet;
import adams.gui.visualization.report.ReportContainerList;
import adams.gui.visualization.report.ReportFactory;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A panel for exploring Instances visually.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 13451 $
 */
public class SpreadSheetExplorer
  extends BasePanel
  implements MenuBarProvider, StatusMessageHandler,
             ContainerListManager<SpreadSheetRowContainerManager>,
             DataChangeListener, CleanUpHandler, SendToActionSupporter {

  /** for serialization. */
  private static final long serialVersionUID = 4478483903068117980L;

  /** the name of the props file. */
  public final static String FILENAME = "SpreadSheetExplorer.props";

  /** the file to store the recent files in. */
  public final static String SESSION_FILE = "SpreadSheetExplorerSession.props";

  /** the properties. */
  protected static Properties m_Properties;

  /** the panel for displaying. */
  protected SpreadSheetRowPanel m_PanelInstance;

  /** the status bar. */
  protected BaseStatusBar m_StatusBar;

  /** the menu bar, if used. */
  protected JMenuBar m_MenuBar;

  /** the "load recent" submenu. */
  protected JMenu m_MenuItemLoadRecent;

  /** the menu item for view related stuff. */
  protected JMenu m_MenuView;

  /** the toggle zoom overview menu item. */
  protected JMenuItem m_MenuItemViewZoomOverview;

  /** the toggle anti-aliasing menu item. */
  protected JMenuItem m_MenuItemViewAntiAliasing;

  /** the color provider menu item. */
  protected JMenuItem m_MenuItemViewColorProvider;

  /** the paintlet  menu item. */
  protected JMenuItem m_MenuItemViewPaintlet;

  /** the clear data menu item. */
  protected JMenuItem m_MenuItemClearData;

  /** the tabbed pane for the data to display. */
  protected BaseTabbedPane m_TabbedPane;

  /** the reports. */
  protected ReportFactory.Panel m_Reports;

  /** for searching the fields in the reports. */
  protected SearchPanel m_SearchPanel;

  /** the dialog for loading datasets. */
  protected LoadSpreadSheetDialog m_LoadFromDiskDialog;

  /** the recent files handler. */
  protected RecentFilesHandler<JMenu> m_RecentFilesHandler;

  /** the dialog for the histogram setup. */
  protected HistogramFactory.SetupDialog m_HistogramSetup;
  
  /** the dialog for selecting the color provider. */
  protected GenericObjectEditorDialog m_DialogColorProvider;

  /** the dialog for selecting the paintlet. */
  protected GenericObjectEditorDialog m_DialogPaintlet;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_LoadFromDiskDialog     = null;
    m_RecentFilesHandler     = null;
    m_HistogramSetup         = null;
    m_DialogColorProvider    = null;
    m_DialogPaintlet         = null;
  }

  /**
   * Initializes the widgets.
   */
  @Override
  protected void initGUI() {
    JPanel	panel;
    JPanel	panelData;
    JPanel	panelReports;

    super.initGUI();

    setLayout(new BorderLayout());

    m_TabbedPane = new BaseTabbedPane();
    add(m_TabbedPane, BorderLayout.CENTER);

    // 1. page: graph
    panelData = new JPanel(new BorderLayout());
    m_TabbedPane.addTab("Data", panelData);
    m_TabbedPane.addChangeListener((ChangeEvent e) -> {
      ContainerTable dtable = getSpreadSheetRowContainerList().getTable();
      // data
      if (m_TabbedPane.getSelectedIndex() == 0) {
        BaseTable rtable = m_Reports.getReportContainerList().getTable();
        if ((rtable == null) || (rtable.getSelectedRowCount() != 1))
          return;
        int row = rtable.getSelectedRow();
        dtable.getSelectionModel().clearSelection();
        dtable.getSelectionModel().setSelectionInterval(row, row);
      }
      // reports
      else if (m_TabbedPane.getSelectedIndex() == 1) {
        if (dtable.getSelectedRowCount() != 1)
          return;
        m_Reports.setCurrentTable(dtable.getSelectedRow());
      }
    });

    // the instances
    m_PanelInstance = new SpreadSheetRowPanel();
    m_PanelInstance.getContainerManager().addDataChangeListener(this);
    m_PanelInstance.setStatusMessageHandler(this);
    panelData.add(m_PanelInstance, BorderLayout.CENTER);

    // 2. page: information
    panelReports = new JPanel(new BorderLayout());
    m_TabbedPane.addTab("Information", panelReports);
    m_Reports = newReportPanel();
    m_Reports.setDataContainerPanel(m_PanelInstance);
    panelReports.add(m_Reports, BorderLayout.CENTER);

    m_SearchPanel = new SearchPanel(LayoutType.HORIZONTAL, true);
    m_SearchPanel.addSearchListener((SearchEvent e) -> {
      m_Reports.search(
        m_SearchPanel.getSearchText(), m_SearchPanel.isRegularExpression());
      m_SearchPanel.grabFocus();
    });
    panel = new JPanel(new BorderLayout());
    panel.add(m_SearchPanel, BorderLayout.WEST);
    panelReports.add(panel, BorderLayout.SOUTH);

    // the status bar
    m_StatusBar = new BaseStatusBar();
    add(m_StatusBar, BorderLayout.SOUTH);
  }

  /**
   * Returns the panel for painting the rows.
   *
   * @return		the panel
   */
  public SpreadSheetRowPanel getSpreadSheetRowPanel() {
    return m_PanelInstance;
  }

  /**
   * Returns the panel listing the rows.
   *
   * @return		the panel
   */
  public SpreadSheetRowContainerList getSpreadSheetRowContainerList() {
    return (SpreadSheetRowContainerList) m_PanelInstance.getContainerList();
  }

  /**
   * Displays a message.
   *
   * @param msg		the message to display
   */
  public void showStatus(String msg) {
    m_StatusBar.showStatus(msg);
  }

  /**
   * Creates a new tabbed pane for the reports.
   *
   * @return		the tabbed pane
   */
  protected SpreadSheetRowReportFactory.Panel newReportPanel() {
    return new SpreadSheetRowReportFactory.Panel();
  }

  /**
   * Gets called if the data of the instance panel has changed.
   *
   * @param e		the event that the instance panel sent
   */
  public void dataChanged(DataChangeEvent e) {
    updateMenu();
  }

  /**
   * Returns the current container manager.
   *
   * @return		the manager
   */
  public SpreadSheetRowContainerManager getContainerManager() {
    return m_PanelInstance.getContainerManager();
  }

  /**
   * Sets the manager for handling the containers.
   *
   * @param value	the manager
   */
  public void setContainerManager(SpreadSheetRowContainerManager value) {
    m_PanelInstance.setContainerManager(value);
  }

  /**
   * updates the enabled state of the menu items.
   */
  protected void updateMenu() {
    boolean	dataLoaded;

    if (m_MenuBar == null)
      return;

    dataLoaded = (getContainerManager().count() > 0);

    m_MenuItemClearData.setEnabled(dataLoaded);
    m_MenuItemLoadRecent.setEnabled(m_RecentFilesHandler.size() > 0);
    m_MenuItemViewAntiAliasing.setSelected(getSpreadSheetRowPanel().isAntiAliasingEnabled());
  }

  /**
   * Creates a menu bar (singleton per panel object). Can be used in frames.
   *
   * @return		the menu bar
   */
  public JMenuBar getMenuBar() {
    JMenuBar		result;
    JMenu		menu;
    JMenu		submenu;
    JMenuItem		menuitem;

    if (m_MenuBar == null) {
      result = new JMenuBar();

      // File
      menu = new JMenu("File");
      result.add(menu);
      menu.setMnemonic('F');
      menu.addChangeListener((ChangeEvent e) -> updateMenu());

      // File/Clear
      menuitem = new JMenuItem("Clear");
      menu.add(menuitem);
      menuitem.setMnemonic('C');
      menuitem.setAccelerator(GUIHelper.getKeyStroke("ctrl pressed N"));
      menuitem.setIcon(GUIHelper.getIcon("new.gif"));
      menuitem.addActionListener((ActionEvent e) -> clearData());
      m_MenuItemClearData = menuitem;

      menu.addSeparator();

      // File/Open
      menuitem = new JMenuItem("Open...");
      menu.add(menuitem);
      menuitem.setMnemonic('o');
      menuitem.setAccelerator(GUIHelper.getKeyStroke("ctrl pressed O"));
      menuitem.setIcon(GUIHelper.getIcon("open.gif"));
      menuitem.addActionListener((ActionEvent e) -> loadData());

      // File/Recent files
      submenu = new JMenu("Open recent");
      menu.add(submenu);
      m_RecentFilesHandler = new RecentFilesHandler<>(
	  SESSION_FILE, getProperties().getInteger("MaxRecentFiles", 5), submenu);
      m_RecentFilesHandler.addRecentItemListener(new RecentItemListener<JMenu,File>() {
	public void recentItemAdded(RecentItemEvent<JMenu,File> e) {
	  // ignored
	}
	public void recentItemSelected(RecentItemEvent<JMenu,File> e) {
	  loadData(e.getItem());
	}
      });
      m_MenuItemLoadRecent = submenu;

      // File/Send to
      menu.addSeparator();
      if (SendToActionUtils.addSendToSubmenu(this, menu))
	menu.addSeparator();

      // File/Close
      menuitem = new JMenuItem("Close");
      menu.add(menuitem);
      menuitem.setMnemonic('C');
      menuitem.setAccelerator(GUIHelper.getKeyStroke("ctrl pressed Q"));
      menuitem.setIcon(GUIHelper.getIcon("exit.png"));
      menuitem.addActionListener((ActionEvent e) -> close());

      // View
      menu = new JMenu("View");
      result.add(menu);
      menu.setMnemonic('V');
      menu.addChangeListener((ChangeEvent e) -> updateMenu());
      m_MenuView = menu;

      // View/Display zoom overview
      menuitem = new JCheckBoxMenuItem("Display zoom overview");
      menu.add(menuitem);
      menuitem.setMnemonic('Z');
      menuitem.setSelected(isZoomOverviewPanelVisible());
      menuitem.addActionListener((ActionEvent e) -> setZoomOverviewPanelVisible(m_MenuItemViewZoomOverview.isSelected()));
      m_MenuItemViewZoomOverview = menuitem;

      // View/Anti-aliasing
      menuitem = new JCheckBoxMenuItem("Anti-aliasing");
      menu.add(menuitem);
      menuitem.setMnemonic('A');
      menuitem.setSelected(getSpreadSheetRowPanel().isAntiAliasingEnabled());
      menuitem.addActionListener((ActionEvent e) ->
	  getSpreadSheetRowPanel().setAntiAliasingEnabled(m_MenuItemViewAntiAliasing.isSelected())
      );
      m_MenuItemViewAntiAliasing = menuitem;

      // View/Color provider
      menuitem = new JMenuItem("Color provider...");
      menu.add(menuitem);
      menuitem.setMnemonic('d');
      menuitem.addActionListener((ActionEvent e) -> selectColorProvider());
      m_MenuItemViewColorProvider = menuitem;

      // View/Paintlet
      menuitem = new JMenuItem("Paintlet...");
      menu.add(menuitem);
      menuitem.setMnemonic('P');
      menuitem.addActionListener((ActionEvent e) -> selectPaintlet());
      m_MenuItemViewPaintlet = menuitem;

      // update menu
      m_MenuBar = result;
      updateMenu();
    }
    else {
      result = m_MenuBar;
    }

    return result;
  }

  /**
   * Removes all the data.
   */
  public void clearData() {
    getContainerManager().clear();
  }

  /**
   * pops up file dialog for loading dataset from disk.
   */
  public void loadData() {
    loadData(null);
  }

  /**
   * pops up file dialog for loading dataset from disk.
   *
   * @param file	an optional file, use null to ignore
   */
  public void loadData(File file) {
    if (m_LoadFromDiskDialog == null) {
      if (getParentDialog() != null)
	m_LoadFromDiskDialog = new LoadSpreadSheetDialog(getParentDialog());
      else
	m_LoadFromDiskDialog = new LoadSpreadSheetDialog(getParentFrame());
      m_LoadFromDiskDialog.setCurrent(new File(getProperties().getPath("InitialDir", "%h")));
      m_LoadFromDiskDialog.setDefaultAttributeRange(getProperties().getPath("AttributeRange", "first-last"));
      m_LoadFromDiskDialog.setDefaultSortIndex(getProperties().getPath("SortIndex", ""));
      m_LoadFromDiskDialog.setAcceptListener((ChangeEvent e) -> {
	int[] indices = m_LoadFromDiskDialog.getIndices();
	if (indices == null)
	  return;
	if (m_RecentFilesHandler != null)
	  m_RecentFilesHandler.addRecentItem(m_LoadFromDiskDialog.getCurrent());

	showStatus("Loading data...");
	List<SpreadSheetRowContainer> data = new ArrayList<>();
	SpreadSheet sheet = m_LoadFromDiskDialog.getSpreadSheet();
	int[] additional = m_LoadFromDiskDialog.getAdditionalAttributes();
	Range range = m_LoadFromDiskDialog.getCurrentAttributeRange();
	int id = m_LoadFromDiskDialog.getCurrentIDIndex();
	HashSet<Integer> attTypes = null;
	for (int i = 0; i < indices.length; i++) {
	  Row srow = sheet.getRow(indices[i]);
	  SpreadSheetRow row = new SpreadSheetRow();
	  row.set(srow, i, additional, range, attTypes);
	  if (id == -1) {
	    row.setID((indices[i] + 1) + "." + (sheet.getName() != null ? sheet.getName() : "sheet"));
	  }
	  else {
	    row.setID(srow.getCell(id).getContent());
	  }
	  data.add(getContainerManager().newContainer(row));
	  showStatus("Loading data " + (i+1) + "/" + sheet.getRowCount());
	}
	loadData(sheet, data);

	showStatus("");
      });
    }

    if (file != null)
      m_LoadFromDiskDialog.setCurrent(file);
    m_LoadFromDiskDialog.setVisible(true);
  }

  /**
   * Loads the given data into the container manager.
   *
   * @param sheet	the dataset
   * @param data	the data to add to the manager
   */
  protected void loadData(SpreadSheet sheet, List<SpreadSheetRowContainer> data) {
    SpreadSheetRowContainerList	listInst;
    ReportContainerList		listReport;

    // turn off anti-aliasing to speed up display
    if (getContainerManager().count() + data.size() > getProperties().getInteger("MaxNumContainersWithAntiAliasing", 50)) {
      if (getSpreadSheetRowPanel().isAntiAliasingEnabled())
	getSpreadSheetRowPanel().setAntiAliasingEnabled(false);
    }

    listInst   = (SpreadSheetRowContainerList) m_PanelInstance.getContainerList();
    listReport = m_Reports.getReportContainerList();

    listInst.setDisplayDatabaseID(false);
    listReport.setDisplayDatabaseID(false);
    getContainerManager().addAll(data);
  }

  /**
   * closes the dialog/frame.
   */
  public void close() {
    cleanUp();
    if (getParentFrame() != null) {
      getParentFrame().setVisible(false);
      getParentFrame().dispose();
    }
    else if (getParentDialog() != null) {
      getParentDialog().setVisible(false);
      getParentDialog().dispose();
    }
  }

  /**
   * Whether to display the side panel or not.
   *
   * @param visible	if true, then the side panel will be displayed
   */
  public void setSidePanelVisible(boolean visible) {
    m_PanelInstance.setSidePanelVisible(visible);
  }

  /**
   * Returns whether the side panel is visible or not.
   *
   * @return		true if the side panel is visible
   */
  public boolean isSidePanelVisible() {
    return m_PanelInstance.isSidePanelVisible();
  }

  /**
   * Returns the side panel.
   *
   * @return		the side panel
   */
  public JPanel getSidePanel() {
    return m_PanelInstance.getSidePanel();
  }

  /**
   * Sets the zoom overview panel visible or not.
   * 
   * @param value	if true then the panel is visible
   */
  public void setZoomOverviewPanelVisible(boolean value) {
    m_PanelInstance.setZoomOverviewPanelVisible(value);
  }
  
  /**
   * Returns whether the zoom overview panel is visible or not.
   * 
   * @return		true if visible
   */
  public boolean isZoomOverviewPanelVisible() {
    return m_PanelInstance.isZoomOverviewPanelVisible();
  }

  /**
   * Lets the user select a new color provider.
   */
  protected void selectColorProvider() {
    if (m_DialogColorProvider == null) {
      if (getParentDialog() != null)
	m_DialogColorProvider = new GenericObjectEditorDialog(getParentDialog(), ModalityType.DOCUMENT_MODAL);
      else
	m_DialogColorProvider = new GenericObjectEditorDialog(getParentFrame(), true);
      m_DialogColorProvider.setTitle("Select color provider");
      m_DialogColorProvider.getGOEEditor().setClassType(ColorProvider.class);
      m_DialogColorProvider.getGOEEditor().setCanChangeClassInDialog(true);
      m_DialogColorProvider.setLocationRelativeTo(this);
    }
    
    m_DialogColorProvider.setCurrent(getContainerManager().getColorProvider().shallowCopy());
    m_DialogColorProvider.setVisible(true);
    if (m_DialogColorProvider.getResult() != GenericObjectEditorDialog.APPROVE_OPTION)
      return;
    getContainerManager().setColorProvider(((ColorProvider) m_DialogColorProvider.getCurrent()).shallowCopy());
  }

  /**
   * Lets the user select a new paintlet.
   */
  protected void selectPaintlet() {
    Paintlet 	paintlet;
    boolean	zoomVisible;

    if (m_DialogPaintlet == null) {
      if (getParentDialog() != null)
	m_DialogPaintlet = new GenericObjectEditorDialog(getParentDialog(), ModalityType.DOCUMENT_MODAL);
      else
	m_DialogPaintlet = new GenericObjectEditorDialog(getParentFrame(), true);
      m_DialogPaintlet.setTitle("Select paintlet");
      m_DialogPaintlet.getGOEEditor().setClassType(AbstractSpreadSheetRowPaintlet.class);
      m_DialogPaintlet.getGOEEditor().setCanChangeClassInDialog(true);
      m_DialogPaintlet.setLocationRelativeTo(this);
    }
    
    m_DialogPaintlet.setCurrent(getSpreadSheetRowPanel().getDataPaintlet().shallowCopy());
    m_DialogPaintlet.setVisible(true);
    if (m_DialogPaintlet.getResult() != GenericObjectEditorDialog.APPROVE_OPTION)
      return;
    paintlet = (Paintlet) m_DialogPaintlet.getCurrent();
    paintlet.setPanel(getSpreadSheetRowPanel());
    getSpreadSheetRowPanel().removePaintlet(getSpreadSheetRowPanel().getDataPaintlet());
    getSpreadSheetRowPanel().addPaintlet(paintlet);
    zoomVisible = getSpreadSheetRowPanel().isZoomOverviewPanelVisible();
    getSpreadSheetRowPanel().getZoomOverviewPanel().setDataContainerPanel(getSpreadSheetRowPanel());
    getSpreadSheetRowPanel().setZoomOverviewPanelVisible(zoomVisible);
  }
  
  /**
   * Returns the classes that the supporter generates.
   *
   * @return		the classes
   */
  public Class[] getSendToClasses() {
    return new Class[]{PlaceholderFile.class, JComponent.class};
  }

  /**
   * Checks whether something to send is available.
   *
   * @param cls		the classes to retrieve an item for
   * @return		true if an object is available for sending
   */
  public boolean hasSendToItem(Class[] cls) {
    return (getContainerManager().countVisible() > 0);
  }

  /**
   * Returns the object to send.
   *
   * @param cls		the classes to retrieve the item for
   * @return		the item to send
   */
  public Object getSendToItem(Class[] cls) {
    Object			result;
    SpreadSheet 		sheet;
    CsvSpreadSheetWriter	writer;

    result = null;

    if (SendToActionUtils.isAvailable(PlaceholderFile.class, cls)) {
      if (getContainerManager().countVisible() > 0) {
	result = SendToActionUtils.nextTmpFile("spreadsheetexplorer", "csv");
	sheet  = m_PanelInstance.getSpreadSheet();
	writer = new CsvSpreadSheetWriter();
	if (!writer.write(sheet, (PlaceholderFile) result)) {
	  GUIHelper.showErrorMessage(this, "Failed to write spreadsheet to '" + result + "'!");
          result = null;
        }
      }
    }
    else if (SendToActionUtils.isAvailable(JComponent.class, cls)) {
      if (getContainerManager().countVisible() > 0) {
	result = this;
      }
    }

    return result;
  }

  /**
   * Cleans up data structures, frees up memory.
   */
  public void cleanUp() {
    m_PanelInstance.cleanUp();
    if (m_DialogColorProvider != null) {
      m_DialogColorProvider.dispose();
      m_DialogColorProvider = null;
    }
    if (m_DialogPaintlet != null) {
      m_DialogPaintlet.dispose();
      m_DialogPaintlet = null;
    }
  }

  /**
   * Returns the properties that define the editor.
   *
   * @return		the properties
   */
  public static synchronized Properties getProperties() {
    if (m_Properties == null)
      m_Properties = Environment.getInstance().read(SpreadSheetExplorerDefinition.KEY);

    return m_Properties;
  }
}

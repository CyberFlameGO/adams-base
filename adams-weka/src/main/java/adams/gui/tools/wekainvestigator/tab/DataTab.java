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

/**
 * DataTab.java
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */

package adams.gui.tools.wekainvestigator.tab;

import adams.core.logging.LoggingLevel;
import adams.gui.core.ConsolePanel;
import adams.gui.core.GUIHelper;
import adams.gui.core.SearchPanel;
import adams.gui.core.SearchPanel.LayoutType;
import adams.gui.event.SearchEvent;
import adams.gui.tools.wekainvestigator.tab.datatab.AbstractDataTabAction;
import adams.gui.tools.wekainvestigator.tab.datatab.Export;
import adams.gui.tools.wekainvestigator.viewer.InstancesTable;
import adams.gui.tools.wekainvestigator.viewer.InstancesTableModel;
import com.googlecode.jfilechooserbookmarks.gui.BaseScrollPane;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideSplitButton;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Lists the currently loaded datasets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class DataTab
  extends AbstractInvestigatorTabWithDataTable {

  private static final long serialVersionUID = -94945456385486233L;

  /** the button for removing a dataset. */
  protected JideButton m_ButtonRemove;

  /** the action button. */
  protected JideSplitButton m_ButtonAction;

  /** the available actions. */
  protected List<AbstractDataTabAction> m_Actions;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    Class[]			classes;
    AbstractDataTabAction 	action;

    super.initialize();

    m_Owner          = null;
    m_Actions        = new ArrayList<>();
    classes          = AbstractDataTabAction.getActions();
    for (Class cls: classes) {
      try {
	action = (AbstractDataTabAction) cls.newInstance();
	action.setOwner(this);
	m_Actions.add(action);
      }
      catch (Exception e) {
	ConsolePanel.getSingleton().append(LoggingLevel.SEVERE, "Failed to instantiate action: " + cls.getName(), e);
      }
    }
  }

  /**
   * Initializes the widgets.
   */
  @Override
  protected void initGUI() {
    super.initGUI();

    m_ButtonRemove = new JideButton("Remove", GUIHelper.getIcon("delete.gif"));
    m_ButtonRemove.setButtonStyle(JideButton.TOOLBOX_STYLE);
    m_ButtonRemove.addActionListener((ActionEvent e) -> removeData(m_Table.getSelectedRows()));
    m_Table.addToButtonsPanel(m_ButtonRemove);

    m_ButtonAction = new JideSplitButton();
    m_ButtonAction.setAlwaysDropdown(false);
    m_ButtonAction.setButtonEnabled(true);
    m_ButtonAction.setButtonStyle(JideSplitButton.TOOLBOX_STYLE);
    for (AbstractDataTabAction action: m_Actions) {
      if (action instanceof Export)
	m_ButtonAction.setAction(action);
      else
	m_ButtonAction.add(action);
    }
    m_Table.addToButtonsPanel(m_ButtonAction);
  }

  /**
   * Returns the title of this table.
   *
   * @return		the title
   */
  @Override
  public String getTitle() {
    return "Data";
  }

  /**
   * Returns the icon name for the tab icon.
   *
   * @return		the icon name, null if not available
   */
  public String getTabIcon() {
    return "spreadsheet.png";
  }

  /**
   * Returns whether a readonly table is used.
   *
   * @return		true if readonly
   */
  protected boolean hasReadOnlyTable() {
    return false;
  }

  /**
   * Returns the list selection mode to use.
   *
   * @return		the mode
   * @see                ListSelectionModel
   */
  protected int getDataTableListSelectionMode() {
    return ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
  }

  /**
   * Gets called when the used changes the selection.
   */
  protected void dataTableSelectionChanged() {
    updateButtons();
    displayData();
  }

  /**
   * Updates the state of the buttons.
   */
  protected void updateButtons() {
    m_ButtonRemove.setEnabled(m_Table.getSelectedRowCount() > 0);
    for (AbstractDataTabAction action: m_Actions)
      action.update();
  }

  /**
   * Displays the data.
   */
  protected void displayData() {
    JPanel			panel;
    SearchPanel			search;
    InstancesTable		table;
    int				index;
    InstancesTableModel 	model;

    if (m_Table.getSelectedRow() > -1) {
      panel = new JPanel(new BorderLayout());
      // table
      // TODO cache tables?
      index = m_Table.getActualRow(m_Table.getSelectedRow());
      model = new InstancesTableModel(getData().get(index).getData());
      model.setShowAttributeIndex(true);
      table = new InstancesTable(model);
      table.setUndoEnabled(true);
      panel.add(new BaseScrollPane(table), BorderLayout.CENTER);
      // search
      search = new SearchPanel(LayoutType.HORIZONTAL, true);
      search.addSearchListener((SearchEvent e) -> {
	if (e.getParameters().getSearchString().isEmpty())
	  table.search(null, false);
	else
	  table.search(e.getParameters().getSearchString(), e.getParameters().isRegExp());
      });
      panel.add(search, BorderLayout.SOUTH);
      m_PanelData.removeAll();
      m_PanelData.add(panel, BorderLayout.CENTER);
      if (m_SplitPane.isBottomComponentHidden()) {
	m_SplitPane.setDividerLocation(m_DefaultDataTableHeight);
	m_SplitPane.setBottomComponentHidden(false);
      }
      table.setOptimalColumnWidth();
    }
    else {
      m_PanelData.removeAll();
      m_SplitPane.setBottomComponentHidden(true);
    }
    invalidate();
    revalidate();
    doLayout();
  }
}

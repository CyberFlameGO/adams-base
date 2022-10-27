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
 * DataFilterRows.java
 * Copyright (C) 2014-2022 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.tools.spreadsheetviewer.menu;

import adams.data.spreadsheet.SpreadSheet;
import adams.data.spreadsheet.rowfinder.ByValue;
import adams.data.spreadsheet.rowfinder.RowFinder;
import adams.flow.transformer.SpreadSheetRowFilter;
import adams.gui.goe.GenericObjectEditorDialog;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;

/**
 * Filters the rows.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class DataFilterRows
  extends AbstractSpreadSheetViewerMenuItemAction {

  /** for serialization. */
  private static final long serialVersionUID = 5235570137451285010L;

  /** the last finder. */
  protected RowFinder m_LastFinder;

  /**
   * Returns the caption of this action.
   * 
   * @return		the caption, null if not applicable
   */
  @Override
  protected String getTitle() {
    return "Filter rows...";
  }

  /**
   * Creates a new dialog.
   * 
   * @return		the dialog
   */
  @Override
  protected GenericObjectEditorDialog createDialog() {
    GenericObjectEditorDialog	result;

    if (getParentDialog() != null)
      result = new GenericObjectEditorDialog(getParentDialog(), ModalityType.DOCUMENT_MODAL);
    else
      result = new GenericObjectEditorDialog(getParentFrame(), true);
    result.setTitle("Row finder");
    result.setUISettingsPrefix(RowFinder.class);
    result.getGOEEditor().setClassType(RowFinder.class);
    result.getGOEEditor().setCanChangeClassInDialog(true);
    result.setCurrent(m_LastFinder);
    result.setLocationRelativeTo(m_State);
    
    return result;
  }
  
  /**
   * Invoked when an action occurs.
   */
  @Override
  protected void doActionPerformed(ActionEvent e) {
    SpreadSheet			sheet;
    RowFinder			finder;
    SpreadSheetRowFilter	filter;

    sheet = getTabbedPane().getCurrentSheet();
    if (sheet == null)
      return;

    if (m_LastFinder == null)
      m_LastFinder = new ByValue();

    getDialog().setVisible(true);
    if (getDialog().getResult() != GenericObjectEditorDialog.APPROVE_OPTION)
      return;

    finder = (RowFinder) getDialog().getGOEEditor().getValue();
    filter = new SpreadSheetRowFilter();
    filter.setFinder(finder);

    m_State.filterData(getTabbedPane().getTitleAt(getTabbedPane().getSelectedIndex()), sheet, filter);
  }

  /**
   * Performs the actual update of the state of the action.
   */
  @Override
  protected void doUpdate() {
    setEnabled(isSheetSelected());
  }
}

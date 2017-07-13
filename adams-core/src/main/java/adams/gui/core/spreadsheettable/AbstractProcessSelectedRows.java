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
 * AbstractProcessSelectedRows.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package adams.gui.core.spreadsheettable;

import adams.core.Utils;
import adams.core.option.AbstractOptionHandler;
import adams.data.spreadsheet.SpreadSheet;
import adams.gui.core.GUIHelper;
import adams.gui.core.SpreadSheetTable;

/**
 * Ancestor for plugins that process a row.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public abstract class AbstractProcessSelectedRows
  extends AbstractOptionHandler
  implements ProcessSelectedRows {

  private static final long serialVersionUID = 7979833588446267882L;

  /**
   * For sorting the menu items.
   *
   * @param o       the other item
   * @return        -1 if less than, 0 if equal, +1 if larger than this
   *                menu item name
   */
  @Override
  public int compareTo(SpreadSheetTablePopupMenuItem o) {
    return getMenuItem().compareTo(o.getMenuItem());
  }

  /**
   * Returns the name of the icon.
   *
   * @return            the name, null if none available
   */
  public String getIconName() {
    return null;
  }

  /**
   * Hook method for checks before attempting processing.
   *
   * @param table	the source table
   * @param sheet	the spreadsheet to use as basis
   * @param actRows	the rows in the spreadsheet
   * @param selRows 	the selected rows in the table
   * @return		null if passed, otherwise error message
   */
  protected String check(SpreadSheetTable table, SpreadSheet sheet, int[] actRows, int[] selRows) {
    if (table == null)
      return "No source table available!";
    if (sheet == null)
      return "No spreadsheet available!";
    if (actRows.length == 0)
      return "No rows selected!";
    return null;
  }

  /**
   * Processes the specified rows.
   *
   * @param table	the source table
   * @param sheet	the spreadsheet to use as basis
   * @param actRows	the actual rows in the spreadsheet
   * @param selRows	the selected rows in the table
   * @return		true if successful
   */
  protected abstract boolean doProcessSelectedRows(SpreadSheetTable table, SpreadSheet sheet, int[] actRows, int[] selRows);

  /**
   * Processes the specified rows.
   *
   * @param table	the source table
   * @param sheet	the spreadsheet to use as basis
   * @param actRows	the actual rows in the spreadsheet
   * @param selRows	the selected rows in the table
   * @return		true if successful
   */
  public boolean processSelectedRows(SpreadSheetTable table, SpreadSheet sheet, int[] actRows, int[] selRows) {
    boolean	result;
    String	error;

    error = check(table, sheet, actRows, selRows);
    result = (error == null);
    if (result)
      result = doProcessSelectedRows(table, sheet, actRows, selRows);
    else
      GUIHelper.showErrorMessage(table, "Failed to process rows (0-based indices): " + Utils.arrayToString(actRows) + "\n" + error);

    return result;
  }
}

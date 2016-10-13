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
 * JTableHelper.java
 * Copyright (C) 2005-2016 University of Waikato, Hamilton, New Zealand
 * Copyright http://fopps.sourceforge.net/
 */

package adams.gui.core;

import adams.core.License;
import adams.core.annotation.MixedCopyright;
import adams.core.logging.Logger;
import adams.core.logging.LoggingHelper;
import adams.data.spreadsheet.DefaultSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;

import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.logging.Level;

/**
 * A helper class for JTable, e.g. calculating the optimal colwidth.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 * @see weka.gui.JTableHelper
 */
@MixedCopyright(
    copyright = "2002, 2003 Oliver Wieland",
    license = License.GPL2,
    url = "http://fopps.sourceforge.net/",
    note = "org.fopps.ui.EnhancedTable"
)
public class JTableHelper {

  /** for logging. */
  protected static Logger LOGGER = LoggingHelper.getLogger(JTableHelper.class);

  /** the maximum number of rows to use for calculation. */
  public final static int MAX_ROWS = 100;

  /** the table to work with. */
  protected JTable m_Table;

  /**
   * initializes the object.
   *
   * @param table	the table to work on
   */
  public JTableHelper(JTable table) {
    this.m_Table = table;
  }

  /**
   * returns the JTable.
   *
   * @return		the table to work on
   */
  public JTable getJTable() {
    return m_Table;
  }

  /**
   * calcs the optimal column width of the given column.
   *
   * @param col		the column index
   * @return		the optimal width
   */
  public int calcColumnWidth(int col) {
    return calcColumnWidth(col, -1);
  }

  /**
   * calcs the optimal column width of the given column.
   *
   * @param col		the column index
   * @param max      the limit for the column width, -1 for unlimited
   * @return		the optimal width
   */
  public int calcColumnWidth(int col, int max) {
    return calcColumnWidth(getJTable(), col, max);
  }

  /**
   * Calculates the optimal width for the column of the given table. The
   * calculation is based on the preferred width of the header and cell
   * renderer.
   * <br>
   * Taken from the newsgroup de.comp.lang.java with some modifications.<br>
   * Taken from FOPPS/EnhancedTable - http://fopps.sourceforge.net/<br>
   *
   * @param table    the table to calculate the column width
   * @param col      the column to calculate the widths
   * @return         the width, -1 if error
   */
  public static int calcColumnWidth(JTable table, int col) {
    return calcColumnWidth(table, col, - 1);
  }

  /**
   * Calculates the optimal width for the column of the given table. The
   * calculation is based on the preferred width of the header and cell
   * renderer.
   * <br>
   * Taken from the newsgroup de.comp.lang.java with some modifications.<br>
   * Taken from FOPPS/EnhancedTable - http://fopps.sourceforge.net/<br>
   *
   * @param table    the table to calculate the column width
   * @param col      the column to calculate the widths
   * @param max      the limit for the column width, -1 for unlimited
   * @return         the width, -1 if error
   */
  public static int calcColumnWidth(JTable table, int col, int max) {
    int 	result;
    TableModel 	data;
    int 	rowCount;
    int		row;
    int		dec;
    Component 	c;

    result = calcHeaderWidth(table, col, max);
    if (result == -1)
      return result;

    data     = table.getModel();
    rowCount = data.getRowCount();
    dec      = (int) Math.ceil((double) rowCount / (double) MAX_ROWS);
    try {
      for (row = rowCount - 1; row >= 0; row -= dec) {
        c = table.prepareRenderer(
            table.getCellRenderer(row, col),
            row, col);
        result = Math.max(result, c.getPreferredSize().width + 10);
	if (result >= max) {
	  result = max;
	  break;
	}
      }
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to calculate column width!", e);
    }

    return result;
  }

  /**
   * calcs the optimal header width of the given column.
   *
   * @param col		the column index
   * @return		the optimal width
   */
  public int calcHeaderWidth(int col) {
    return calcHeaderWidth(getJTable(), col, -1);
  }

  /**
   * calcs the optimal header width of the given column.
   *
   * @param col		the column index
   * @param max      the limit for the column width, -1 for unlimited
   * @return		the optimal width
   */
  public int calcHeaderWidth(int col, int max) {
    return calcHeaderWidth(getJTable(), col, max);
  }

  /**
   * Calculates the optimal width for the header of the given table. The
   * calculation is based on the preferred width of the header renderer.
   *
   * @param table    the table to calculate the column width
   * @param col      the column to calculate the widths
   * @return         the width, -1 if error
   */
  public static int calcHeaderWidth(JTable table, int col) {
    return calcHeaderWidth(table, col, -1);
  }

  /**
   * Calculates the optimal width for the header of the given table. The
   * calculation is based on the preferred width of the header renderer.
   *
   * @param table    the table to calculate the column width
   * @param col      the column to calculate the widths
   * @param max      the limit for the column width, -1 for unlimited
   * @return         the width, -1 if error
   */
  public static int calcHeaderWidth(JTable table, int col, int max) {
    if (table == null)
      return -1;

    if (col < 0 || col > table.getColumnCount()) {
      LOGGER.severe("calcHeaderWidth: invalid col " + col);
      return -1;
    }

    JTableHeader header = table.getTableHeader();
    TableCellRenderer defaultHeaderRenderer = null;
    if (header != null) defaultHeaderRenderer = header.getDefaultRenderer();
    TableColumnModel columns = table.getColumnModel();
    TableColumn column = columns.getColumn(col);
    int width = -1;
    TableCellRenderer h = column.getHeaderRenderer();
    if (h == null) h = defaultHeaderRenderer;
    if (h != null) {
      // Not explicitly impossible
      Component c = h.getTableCellRendererComponent(
          table,
          column.getHeaderValue(),
          false, false, -1, col);
      width = c.getPreferredSize().width + 5;
    }

    if (max > -1)
      width = Math.min(width, max);

    return width;
  }

  /**
   * sets the optimal column width for the given column.
   *
   * @param col		the column index
   */
  public void setOptimalColumnWidth(int col) {
    setOptimalColumnWidth(getJTable(), col);
  }

  /**
   * sets the optimal column width for the given column.
   *
   * @param table	the table to work on
   * @param col		the column index
   */
  public static void setOptimalColumnWidth(final JTable table, final int col) {
    final int	  width;

    if ( (col >= 0) && (col < table.getColumnModel().getColumnCount()) ) {
      width = calcColumnWidth(table, col);

      if (width >= 0) {
        SwingUtilities.invokeLater(() -> {
          JTableHeader header = table.getTableHeader();
          TableColumn column = table.getColumnModel().getColumn(col);
          column.setPreferredWidth(width);
          table.doLayout();
          header.repaint();
        });
      }
    }
  }

  /**
   * sets the optimal column width for all columns.
   */
  public void setOptimalColumnWidth() {
    setOptimalColumnWidth(getJTable());
  }

  /**
   * sets the optimal column width for alls column if the given table.
   *
   * @param table	the table to work on
   */
  public static void setOptimalColumnWidth(JTable table) {
    int		i;

    for (i = 0; i < table.getColumnModel().getColumnCount(); i++)
      setOptimalColumnWidth(table, i);
  }

  /**
   * sets the optimal header width for the given column.
   *
   * @param col		the column index
   */
  public void setOptimalHeaderWidth(int col) {
    setOptimalHeaderWidth(getJTable(), col);
  }

  /**
   * sets the optimal header width for the given column.
   *
   * @param table	the table to work on
   * @param col		the column index
   */
  public static void setOptimalHeaderWidth(final JTable table, final int col) {
    final int   width;

    if ( (col >= 0) && (col < table.getColumnModel().getColumnCount()) ) {
      width = calcHeaderWidth(table, col);

      if (width >= 0) {
        SwingUtilities.invokeLater(() -> {
          JTableHeader header = table.getTableHeader();
          TableColumn column = table.getColumnModel().getColumn(col);
          column.setPreferredWidth(width);
          table.doLayout();
          header.repaint();
        });
      }
    }
  }

  /**
   * sets the optimal header width for all columns.
   */
  public void setOptimalHeaderWidth() {
    setOptimalHeaderWidth(getJTable());
  }

  /**
   * sets the optimal header width for alls column if the given table.
   *
   * @param table	the table to work with
   */
  public static void setOptimalHeaderWidth(JTable table) {
    int		i;

    for (i = 0; i < table.getColumnModel().getColumnCount(); i++)
      setOptimalHeaderWidth(table, i);
  }

  /**
   * Assumes table is contained in a JScrollPane.
   * Scrolls the cell (rowIndex, vColIndex) so that it is visible
   * within the viewport.
   *
   * @param row		the row index
   * @param col		the column index
   */
  public void scrollToVisible(int row, int col) {
    scrollToVisible(getJTable(), row, col);
  }

  /**
   * Assumes table is contained in a JScrollPane.
   * Scrolls the cell (rowIndex, vColIndex) so that it is visible
   * within the viewport.
   *
   * @param table	the table to work with
   * @param row		the row index
   * @param col		the column index
   */
  public static void scrollToVisible(JTable table, int row, int col) {
    if (!(table.getParent() instanceof JViewport))
      return;

    JViewport viewport = (JViewport) table.getParent();

    // This rectangle is relative to the table where the
    // northwest corner of cell (0,0) is always (0,0).
    Rectangle rect = table.getCellRect(row, col, true);

    // The location of the viewport relative to the table
    Point pt = viewport.getViewPosition();

    // Translate the cell location so that it is relative
    // to the view, assuming the northwest corner of the
    // view is (0,0)
    rect.setLocation(rect.x - pt.x, rect.y - pt.y);

    // Scroll the area into view
    viewport.scrollRectToVisible(rect);
  }

  /**
   * Turns a table into a spreadsheet.
   *
   * @param table	the table to convert
   * @return		the generated spreadsheet
   */
  public static SpreadSheet toSpreadSheet(JTable table) {
    return toSpreadSheet(table.getModel());
  }

  /**
   * Turns a table model into a spreadsheet.
   *
   * @param model	the table model to convert
   * @return		the generated spreadsheet
   */
  public static SpreadSheet toSpreadSheet(TableModel model) {
    SpreadSheet 	result;
    Row			row;
    int			i;
    int			n;
    Object		value;

    result = new DefaultSpreadSheet();
    // header
    row = result.getHeaderRow();
    for (i = 0; i < model.getColumnCount(); i++)
      row.addCell("" + i).setContent(model.getColumnName(i));
    for (n = 0; n < model.getRowCount(); n++) {
      row = result.addRow("" + n);
      for (i = 0; i < model.getColumnCount(); i++) {
        value = model.getValueAt(n, i);
        if (value != null)
          row.addCell("" + i).setContent(value.toString());
      }
    }

    return result;
  }
}

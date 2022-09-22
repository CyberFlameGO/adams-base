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
 * ExcelSpreadSheetWriter.java
 * Copyright (C) 2010-2022 University of Waikato, Hamilton, New Zealand
 */
package adams.data.io.output;

import adams.core.Constants;
import adams.core.ExcelHelper;
import adams.core.Utils;
import adams.data.DateFormatString;
import adams.data.io.input.ExcelSpreadSheetReader;
import adams.data.io.input.SpreadSheetReader;
import adams.data.spreadsheet.SpreadSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.util.HashSet;
import java.util.logging.Level;

/**
 <!-- globalinfo-start -->
 * Writes MS Excel files.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 *
 * <pre>-sheet-prefix &lt;java.lang.String&gt; (property: sheetPrefix)
 * &nbsp;&nbsp;&nbsp;The prefix for sheet names.
 * &nbsp;&nbsp;&nbsp;default: Sheet
 * </pre>
 *
 * <pre>-missing &lt;java.lang.String&gt; (property: missingValue)
 * &nbsp;&nbsp;&nbsp;The placeholder for missing values.
 * &nbsp;&nbsp;&nbsp;default:
 * </pre>
 *
 * <pre>-time-format &lt;adams.data.DateFormatString&gt; (property: timeFormat)
 * &nbsp;&nbsp;&nbsp;The format for times.
 * &nbsp;&nbsp;&nbsp;default: HH:mm:ss
 * &nbsp;&nbsp;&nbsp;more: https:&#47;&#47;docs.oracle.com&#47;javase&#47;8&#47;docs&#47;api&#47;java&#47;text&#47;SimpleDateFormat.html
 * </pre>
 *
 * <pre>-date-format &lt;adams.data.DateFormatString&gt; (property: dateFormat)
 * &nbsp;&nbsp;&nbsp;The format for dates.
 * &nbsp;&nbsp;&nbsp;default: yyyy-MM-dd
 * &nbsp;&nbsp;&nbsp;more: https:&#47;&#47;docs.oracle.com&#47;javase&#47;8&#47;docs&#47;api&#47;java&#47;text&#47;SimpleDateFormat.html
 * </pre>
 *
 * <pre>-datetime-format &lt;adams.data.DateFormatString&gt; (property: dateTimeFormat)
 * &nbsp;&nbsp;&nbsp;The format for date&#47;times.
 * &nbsp;&nbsp;&nbsp;default: yyyy-MM-dd HH:mm:ss
 * &nbsp;&nbsp;&nbsp;more: https:&#47;&#47;docs.oracle.com&#47;javase&#47;8&#47;docs&#47;api&#47;java&#47;text&#47;SimpleDateFormat.html
 * </pre>
 *
 * <pre>-output-as-displayed &lt;boolean&gt; (property: outputAsDisplayed)
 * &nbsp;&nbsp;&nbsp;If enabled, cells are output as displayed, ie, results of formulas instead
 * &nbsp;&nbsp;&nbsp;of the formulas.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class ExcelSpreadSheetWriter
    extends AbstractMultiSheetSpreadSheetWriterWithMissingValueSupport
    implements SpreadSheetWriterWithFormulaSupport {

  /** for serialization. */
  private static final long serialVersionUID = -3549185519778801930L;

  /** the binary file extension. */
  public static String FILE_EXTENSION = ".xls";

  /** the OOXML file extension. */
  public static String FILE_EXTENSION_OOXML = ".xlsx";

  /** whether to write OOXML instead of binary Excel files (latter is default). */
  protected boolean m_WriteOOXML;

  /** the format string for the times. */
  protected DateFormatString m_TimeFormat;

  /** the format string for the dates. */
  protected DateFormatString m_DateFormat;

  /** the format string for the date/times. */
  protected DateFormatString m_DateTimeFormat;

  /** whether to output the cells as displayed (disable to output formulas). */
  protected boolean m_OutputAsDisplayed;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Writes MS Excel files.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
	"time-format", "timeFormat",
	new DateFormatString(Constants.TIME_FORMAT));

    m_OptionManager.add(
	"date-format", "dateFormat",
	new DateFormatString(Constants.DATE_FORMAT));

    m_OptionManager.add(
	"datetime-format", "dateTimeFormat",
	new DateFormatString(Constants.TIMESTAMP_FORMAT));

    m_OptionManager.add(
	"output-as-displayed", "outputAsDisplayed",
	false);
  }

  /**
   * Returns a string describing the format (used in the file chooser).
   *
   * @return 			a description suitable for displaying in the
   * 				file chooser
   */
  @Override
  public String getFormatDescription() {
    return "MS Excel spreadsheets";
  }

  /**
   * Returns the extension(s) of the format.
   *
   * @return 			the extension (without the dot!)
   */
  @Override
  public String[] getFormatExtensions() {
    return new String[]{"xls", "xlsx"};
  }

  /**
   * Returns, if available, the corresponding reader.
   *
   * @return		the reader, null if none available
   */
  public SpreadSheetReader getCorrespondingReader() {
    return new ExcelSpreadSheetReader();
  }

  /**
   * Returns how the data is written.
   *
   * @return		the type
   */
  @Override
  protected OutputType getOutputType() {
    return OutputType.STREAM;
  }

  /**
   * Sets whether to use OOXML instead of binary Excel files.
   *
   * @param value	if true OOXML files are generated
   */
  public void setWriteOOXML(boolean value) {
    m_WriteOOXML = value;
  }

  /**
   * Returns whether OOXML files are generated instead of binary Excel files.
   *
   * @return		true if OOXML files are generated
   */
  public boolean getWriteOOXML() {
    return m_WriteOOXML;
  }

  /**
   * Sets the format for time columns.
   *
   * @param value	the format
   */
  public void setTimeFormat(DateFormatString value) {
    m_TimeFormat = value;
    reset();
  }

  /**
   * Returns the format for time columns.
   *
   * @return		the format
   */
  public DateFormatString getTimeFormat() {
    return m_TimeFormat;
  }

  /**
   * Returns the tip time for this property.
   *
   * @return 		tip time for this property suitable for
   * 			displaying in the gui
   */
  public String timeFormatTipText() {
    return "The format for times.";
  }

  /**
   * Sets the format for date columns.
   *
   * @param value	the format
   */
  public void setDateFormat(DateFormatString value) {
    m_DateFormat = value;
    reset();
  }

  /**
   * Returns the format for date columns.
   *
   * @return		the format
   */
  public DateFormatString getDateFormat() {
    return m_DateFormat;
  }

  /**
   * Returns the tip date for this property.
   *
   * @return 		tip date for this property suitable for
   * 			displaying in the gui
   */
  public String dateFormatTipText() {
    return "The format for dates.";
  }

  /**
   * Sets the format for date/time columns.
   *
   * @param value	the format
   */
  public void setDateTimeFormat(DateFormatString value) {
    m_DateTimeFormat = value;
    reset();
  }

  /**
   * Returns the format for date/time columns.
   *
   * @return		the format
   */
  public DateFormatString getDateTimeFormat() {
    return m_DateTimeFormat;
  }

  /**
   * Returns the tip date/time for this property.
   *
   * @return 		tip date for this property suitable for
   * 			displaying in the gui
   */
  public String dateTimeFormatTipText() {
    return "The format for date/times.";
  }

  /**
   * Sets whether to output the cell content as displayed, ie, no formulas
   * but the result of formulas.
   *
   * @param value	true if to output as displayed
   */
  @Override
  public void setOutputAsDisplayed(boolean value) {
    m_OutputAsDisplayed = value;
    reset();
  }

  /**
   * Returns whether to output the cell content as displayed, ie, no formulas
   * but the result of formulas.
   *
   * @return		true if to output as displayed
   */
  @Override
  public boolean getOutputAsDisplayed() {
    return m_OutputAsDisplayed;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   *         		displaying in the explorer/experimenter gui
   */
  @Override
  public String outputAsDisplayedTipText() {
    return "If enabled, cells are output as displayed, ie, results of formulas instead of the formulas.";
  }

  /**
   * Hook method before writing to a file.
   *
   * @param filename	the filename to check
   */
  protected void preWriteFile(String filename) {
    super.preWriteFile(filename);
    setWriteOOXML(filename.endsWith(FILE_EXTENSION_OOXML));
  }

  /**
   * Performs the actual writing. The caller must ensure that the writer gets
   * closed.
   *
   * @param content	the spreadsheet to write
   * @param out		the writer to write the spreadsheet to
   * @return		true if successfully written
   */
  @Override
  protected boolean doWrite(SpreadSheet[] content, OutputStream out) {
    boolean			result;
    Workbook 			workbook;
    Sheet 			sheet;
    Row 			row;
    adams.data.spreadsheet.Row	spRow;
    adams.data.spreadsheet.Cell	spCell;
    Cell 			cell;
    int 			i;
    int 			n;
    int				count;
    CellStyle			styleDate;
    CellStyle			styleDateTime;
    CellStyle			styleTime;
    HashSet<String>		names;
    String			name;

    result = true;

    try {
      if (getWriteOOXML())
	workbook = new XSSFWorkbook();
      else
	workbook = new HSSFWorkbook();
      styleTime     = ExcelHelper.getDateCellStyle(workbook, m_TimeFormat.getValue());
      styleDate     = ExcelHelper.getDateCellStyle(workbook, m_DateFormat.getValue());
      styleDateTime = ExcelHelper.getDateCellStyle(workbook, m_DateTimeFormat.getValue());

      count = 0;
      names = new HashSet<>();
      for (SpreadSheet cont: content) {
	if (m_Stopped)
	  return false;

	sheet = workbook.createSheet();
	if (cont.getName() != null) {
	  name = cont.getName().replace("'", "");
	  if (names.contains(name))
	    name += (count + 1);
	}
	else {
	  name = m_SheetPrefix + (count + 1);
	}
	names.add(name);
	workbook.setSheetName(count, name);

	// header
	row = sheet.createRow(0);
	for (i = 0; i < cont.getColumnCount(); i++) {
	  cell = row.createCell(i);
	  cell.setCellValue(cont.getHeaderRow().getCell(i).getContent());
	}

	// data
	for (n = 0; n < cont.getRowCount(); n++) {
	  if (m_Stopped)
	    return false;
	  row   = sheet.createRow(n + 1);
	  spRow = cont.getRow(n);
	  for (i = 0; i < cont.getColumnCount(); i++) {
	    cell   = row.createCell(i);
	    spCell = spRow.getCell(i);
	    if ((spCell == null) || spCell.isMissing()) {
	      if (m_MissingValue.length() > 0)
		cell.setCellValue(m_MissingValue);
	      else
		cell.setBlank();
	      continue;
	    }

	    if (spCell.isFormula() && !m_OutputAsDisplayed) {
	      cell.setCellFormula(spCell.getFormula().substring(1));
	    }
	    else {
	      if (spCell.isDate()) {
		cell.setCellValue(spCell.toDate());
		cell.setCellStyle(styleDate);
	      }
	      else if (spCell.isTime()) {
		cell.setCellValue(spCell.toTime());
		cell.setCellStyle(styleTime);
	      }
	      else if (spCell.isDateTime()) {
		cell.setCellValue(spCell.toDateTime());
		cell.setCellStyle(styleDateTime);
	      }
	      else if (spCell.isNumeric()) {
		cell.setCellValue(Utils.toDouble(spCell.getContent()));
	      }
	      else if (spCell.isBoolean()) {
	        cell.setCellValue(spCell.toBoolean());
	      }
	      else {
		cell.setCellValue(spCell.getContent());
	      }
	    }
	  }
	}

	// next sheet
	count++;
      }

      // save
      workbook.write(out);
    }
    catch (Exception e) {
      result = false;
      getLogger().log(Level.SEVERE, "Failed writing spreadsheet data", e);
    }

    return result;
  }
}

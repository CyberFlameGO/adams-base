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
 * SpreadSheetStatistic.java
 * Copyright (C) 2010-2015 University of Waikato, Hamilton, New Zealand
 */

package adams.flow.transformer;

import adams.core.Index;
import adams.core.QuickInfoHelper;
import adams.core.base.BaseString;
import adams.data.spreadsheet.Cell;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import adams.data.statistics.AbstractArrayStatistic;
import adams.data.statistics.ArrayMean;
import adams.flow.core.Token;

/**
 <!-- globalinfo-start -->
 * Generates statistics from a SpreadSheet object.<br>
 * If cells aren't numeric or missing, a default value of zero is used.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
 * Input&#47;output:<br>
 * - accepts:<br>
 * &nbsp;&nbsp;&nbsp;adams.core.io.SpreadSheet<br>
 * - generates:<br>
 * &nbsp;&nbsp;&nbsp;adams.core.io.SpreadSheet<br>
 * <br><br>
 <!-- flow-summary-end -->
 *
 <!-- options-start -->
 * Valid options are: <br><br>
 *
 * <pre>-D &lt;int&gt; (property: debugLevel)
 * &nbsp;&nbsp;&nbsp;The greater the number the more additional info the scheme may output to
 * &nbsp;&nbsp;&nbsp;the console (0 = off).
 * &nbsp;&nbsp;&nbsp;default: 0
 * &nbsp;&nbsp;&nbsp;minimum: 0
 * </pre>
 *
 * <pre>-name &lt;java.lang.String&gt; (property: name)
 * &nbsp;&nbsp;&nbsp;The name of the actor.
 * &nbsp;&nbsp;&nbsp;default: SpreadSheetStatistic
 * </pre>
 *
 * <pre>-annotation &lt;adams.core.base.BaseText&gt; (property: annotations)
 * &nbsp;&nbsp;&nbsp;The annotations to attach to this actor.
 * &nbsp;&nbsp;&nbsp;default:
 * </pre>
 *
 * <pre>-skip (property: skip)
 * &nbsp;&nbsp;&nbsp;If set to true, transformation is skipped and the input token is just forwarded
 * &nbsp;&nbsp;&nbsp;as it is.
 * </pre>
 *
 * <pre>-stop-flow-on-error (property: stopFlowOnError)
 * &nbsp;&nbsp;&nbsp;If set to true, the flow gets stopped in case this actor encounters an error;
 * &nbsp;&nbsp;&nbsp; useful for critical actors.
 * </pre>
 *
 * <pre>-type &lt;ROW_BY_INDEX|COLUMN_BY_INDEX|COLUMN_BY_REGEXP&gt; (property: dataType)
 * &nbsp;&nbsp;&nbsp;Whether to retrieve rows or columns from the Instances object.
 * &nbsp;&nbsp;&nbsp;default: COLUMN_BY_INDEX
 * </pre>
 *
 * <pre>-location &lt;adams.core.base.BaseString&gt; [-location ...] (property: locations)
 * &nbsp;&nbsp;&nbsp;The locations of the data, depending on the chosen data type that can be
 * &nbsp;&nbsp;&nbsp;either indices or regular expressions on the attribute names.
 * &nbsp;&nbsp;&nbsp;default:
 * </pre>
 *
 * <pre>-statistic &lt;adams.data.statistics.AbstractArrayStatistic [options]&gt; (property: statistic)
 * &nbsp;&nbsp;&nbsp;The statistic to generate from the data.
 * &nbsp;&nbsp;&nbsp;default: adams.data.statistics.ArrayMean
 * </pre>
 *
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class SpreadSheetStatistic
  extends AbstractSpreadSheetTransformer {

  /** for serialization. */
  private static final long serialVersionUID = -540187402790189753L;

  /** the type of data to get from the Instances object (rows or columns). */
  protected SpreadSheetStatisticDataType m_DataType;

  /** the array of indices/regular expressions. */
  protected BaseString[] m_Locations;

  /** the statistic to generate. */
  protected AbstractArrayStatistic m_Statistic;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return
        "Generates statistics from a SpreadSheet object.\n"
      + "If cells aren't numeric or missing, a default value of zero is used.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
	    "type", "dataType",
	    SpreadSheetStatisticDataType.COLUMN_BY_INDEX);

    m_OptionManager.add(
	    "location", "locations",
	    new BaseString[0]);

    m_OptionManager.add(
	    "statistic", "statistic",
	    new ArrayMean());
  }

  /**
   * Sets what type of data to retrieve from the Instances object.
   *
   * @param value	the type of conversion
   */
  public void setDataType(SpreadSheetStatisticDataType value) {
    m_DataType = value;
    reset();
  }

  /**
   * Returns what type of data to retrieve from the Instances object.
   *
   * @return		the type of conversion
   */
  public SpreadSheetStatisticDataType getDataType() {
    return m_DataType;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String dataTypeTipText() {
    return "Whether to retrieve rows or columns from the Instances object.";
  }

  /**
   * Sets the locations of the data (indices/regular expressions on attribute name).
   *
   * @param value	the locations of the data
   */
  public void setLocations(BaseString[] value) {
    m_Locations = value;
    reset();
  }

  /**
   * Returns the locations of the data (indices/regular expressions on attribute name).
   *
   * @return		the locations of the data
   */
  public BaseString[] getLocations() {
    return m_Locations;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String locationsTipText() {
    return
        "The locations of the data, depending on the chosen data type that "
      + "can be either indices or regular expressions on the attribute names.";
  }

  /**
   * Sets the statistic to use.
   *
   * @param value	the statistic
   */
  public void setStatistic(AbstractArrayStatistic value) {
    m_Statistic = value;
    reset();
  }

  /**
   * Returns the statistic in use.
   *
   * @return		the statistic
   */
  public AbstractArrayStatistic getStatistic() {
    return m_Statistic;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String statisticTipText() {
    return "The statistic to generate from the data.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    return QuickInfoHelper.toString(this, "statistic", m_Statistic);
  }

  /**
   * Returns the row as Double array. Non-numeric/missing values are defaulted to 0.
   *
   * @param sheet	the sheet to work on
   * @param index	the 0-based row index
   * @return		the numeric values
   */
  protected Double[] getRow(SpreadSheet sheet, int index) {
    Double[]	result;
    Row		row;
    Cell	cell;
    int		i;

    row    = sheet.getRow(index);
    result = new Double[sheet.getColumnCount()];

    for (i = 0; i < result.length; i++) {
      cell = row.getCell(i);
      if ((cell == null) || cell.isMissing() || !cell.isNumeric())
	result[i] = new Double(0.0);
      else
	result[i] = new Double(cell.getContent());
    }

    return result;
  }

  /**
   * Returns the column as Double array. Non-numeric/missing values are defaulted to 0.
   *
   * @param sheet	the sheet to work on
   * @param index	the 0-based column index
   * @return		the numeric values
   */
  protected Double[] getColumn(SpreadSheet sheet, int index) {
    Double[]	result;
    Row		row;
    Cell	cell;
    int		i;

    result = new Double[sheet.getRowCount()];

    for (i = 0; i < result.length; i++) {
      row  = sheet.getRow(i);
      cell = row.getCell(index);
      if ((cell == null) || cell.isMissing() || !cell.isNumeric())
	result[i] = new Double(0.0);
      else
	result[i] = new Double(cell.getContent());
    }

    return result;
  }

  /**
   * Executes the flow item.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  protected String doExecute() {
    String			result;
    SpreadSheet			sheet;
    SpreadSheet			data;
    int				i;
    int				n;
    Index			index;
    AbstractArrayStatistic 	stat;

    result = null;

    try {
      sheet = null;
      data  = (SpreadSheet) m_InputToken.getPayload();
      stat  = m_Statistic.shallowCopy(true);

      for (i = 0; i < m_Locations.length; i++) {
	switch (m_DataType) {
	  case ROW_BY_INDEX:
	    index = new Index(m_Locations[i].stringValue());
	    index.setMax(data.getRowCount());
	    stat.add(getRow(data, index.getIntIndex()));
	    break;

	  case COLUMN_BY_INDEX:
	    index = new Index(m_Locations[i].stringValue());
	    index.setMax(data.getColumnCount());
	    stat.add(getColumn(data, index.getIntIndex()));
	    break;

	  case COLUMN_BY_REGEXP:
	    for (n = 0; n < data.getColumnCount(); n++) {
	      if (data.getHeaderRow().getCell(n).getContent().matches(m_Locations[i].stringValue())) {
		stat.add(getColumn(data, n));
		break;
	      }
	    }
	    break;

	  default:
	    throw new IllegalStateException("Unhandlded data type: " + m_DataType);
	}
      }

      sheet = stat.calculate().toSpreadSheet();
    }
    catch (Exception e) {
      result = handleException("Error generating the statistic: ", e);
      sheet = null;
    }

    if (sheet != null)
      m_OutputToken = new Token(sheet);

    return result;
  }
}

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
 * SpreadSheetDbReader.java
 * Copyright (C) 2012-2017 University of Waikato, Hamilton, New Zealand
 */
package adams.flow.source;

import adams.core.QuickInfoHelper;
import adams.core.Shortening;
import adams.data.spreadsheet.DataRow;
import adams.data.spreadsheet.DataRowTypeHandler;
import adams.data.spreadsheet.DenseDataRow;
import adams.data.spreadsheet.SpreadSheet;
import adams.data.spreadsheet.sql.AbstractTypeMapper;
import adams.data.spreadsheet.sql.DefaultTypeMapper;
import adams.data.spreadsheet.sql.Reader;
import adams.db.AbstractDatabaseConnection;
import adams.db.SQL;
import adams.db.SQLStatement;
import adams.flow.core.Token;

import java.sql.ResultSet;

/**
 * Ancestor for spreadsheet database readers.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractSpreadSheetDbReader
  extends AbstractDbSource
  implements DataRowTypeHandler {

  /** for serialization. */
  private static final long serialVersionUID = 494594301273926225L;

  /** the type mapper to use. */
  protected AbstractTypeMapper m_TypeMapper;

  /** the SQL query to execute. */
  protected SQLStatement m_Query;

  /** the data row type to use. */
  protected DataRow m_DataRowType;

  /** the chunk size to use. */
  protected int m_ChunkSize;
  
  /** the generated output token. */
  protected Token m_OutputToken;

  /** for reading the data. */
  protected Reader m_Reader;
  
  /** the current result set. */
  protected transient ResultSet m_ResultSet;
  
  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return 
	"Returns a spreadsheet object generated from an SQL query.\n"
	+ "To optimize memory consumption, you can return the result of the query "
	+ "in chunks of a specified size.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "type-mapper", "typeMapper",
      new DefaultTypeMapper());

    m_OptionManager.add(
      "query", "query",
      new SQLStatement("select * from blah"));

    m_OptionManager.add(
      "data-row-type", "dataRowType",
      new DenseDataRow());

    m_OptionManager.add(
      "chunk-size", "chunkSize",
      -1, -1, null);
  }

  /**
   * Resets the scheme.
   */
  @Override
  protected void reset() {
    super.reset();
    
    m_OutputToken = null;
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;
    String	value;

    result  = QuickInfoHelper.toString(this, "query", Shortening.shortenEnd(m_Query.stringValue(), 40), "query: ");
    result += QuickInfoHelper.toString(this, "dataRowType", m_DataRowType, ", row type: ");
    value   = QuickInfoHelper.toString(this, "chunkSize", (m_ChunkSize > 0 ? m_ChunkSize : null), ", chunk: ");
    if (value != null)
      result += value;

    return result;
  }

  /**
   * Sets the type mapper to use.
   *
   * @param value	the mapper
   */
  public void setTypeMapper(AbstractTypeMapper value) {
    m_TypeMapper = value;
    reset();
  }

  /**
   * Returns the type mapper in use.
   *
   * @return		the mapper
   */
  public AbstractTypeMapper  getTypeMapper() {
    return m_TypeMapper;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String typeMapperTipText() {
    return "The type mapper to use for mapping spreadsheet and SQL types.";
  }

  /**
   * Sets the SQL query.
   *
   * @param value	the query
   */
  public void setQuery(SQLStatement value) {
    m_Query = value;
    reset();
  }

  /**
   * Returns the SQL query.
   *
   * @return		the query
   */
  public SQLStatement getQuery() {
    return m_Query;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String queryTipText() {
    return "The SQL query to use for generating the spreadsheet.";
  }

  /**
   * Sets the type of data row to use.
   *
   * @param value	the type
   */
  public void setDataRowType(DataRow value) {
    m_DataRowType = value;
    reset();
  }

  /**
   * Returns the type of data row to use.
   *
   * @return		the type
   */
  public DataRow getDataRowType() {
    return m_DataRowType;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String dataRowTypeTipText() {
    return "The type of row to use for the data.";
  }

  /**
   * Sets the maximum chunk size.
   * 
   * @param value	the size of the chunks, &lt; 1 denotes infinity
   */
  public void setChunkSize(int value) {
    if (value < 1)
      value = -1;
    m_ChunkSize = value;
    reset();
  }
  
  /**
   * Returns the current chunk size.
   * 
   * @return	the size of the chunks, &lt; 1 denotes infinity
   */
  public int getChunkSize() {
    return m_ChunkSize;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the gui
   */
  public String chunkSizeTipText() {
    return "The maximum number of rows per chunk; using -1 will read put all data into a single spreadsheet object.";
  }

  /**
   * Returns the default database connection.
   *
   * @return 		the default database connection
   */
  protected abstract AbstractDatabaseConnection getDefaultDatabaseConnection();

  /**
   * Determines the database connection in the flow.
   *
   * @return		the database connection to use
   */
  protected abstract adams.db.AbstractDatabaseConnection getDatabaseConnection();

  /**
   * Returns the class of objects that it generates.
   *
   * @return		the Class of the generated tokens
   */
  @Override
  public Class[] generates() {
    return new Class[]{SpreadSheet.class};
  }

  /**
   * Performs the actual database query.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  protected String queryDatabase() {
    String		result;
    SpreadSheet		sheet;
    SQL			sql;
    String		query;
    
    result = null;

    sheet = null;
    query = m_Query.getValue();
    query = getVariables().expand(query);
    try {
      sql      = new SQL(m_DatabaseConnection);
      sql.setDebug(isLoggingEnabled());
      m_Reader = new Reader(m_TypeMapper, m_DataRowType.getClass());
      m_Reader.setLoggingLevel(getLoggingLevel());
      if (isLoggingEnabled())
	getLogger().info("Query: " + query);
      m_ResultSet = sql.getResultSet(query);
      sheet       = m_Reader.read(m_ResultSet, m_ChunkSize);
      if (m_Reader.isFinished() || m_Reader.isStopped()) {
	m_Reader    = null;
	m_ResultSet = null;
      }
    }
    catch (Exception e) {
      result = handleException("Failed to retrieve data from database!\n" + query, e);
    }
    
    if (isStopped())
      result = null;
    
    if (result == null)
      m_OutputToken = new Token(sheet);
    
    return result;
  }

  /**
   * Checks whether there is pending output to be collected after
   * executing the flow item.
   *
   * @return		true if there is pending output
   */
  @Override
  public boolean hasPendingOutput() {
    if (m_OutputToken != null)
      return true;
    if (m_Reader == null)
      return false;
    if (m_ResultSet == null)
      return false;
    try {
      return !m_Reader.isFinished();
    }
    catch (Exception e) {
      handleException("Failed to query result set's closed state", e);
      return false;
    }
  }
  
  /**
   * Returns the generated token.
   *
   * @return		the generated token
   */
  @Override
  public Token output() {
    Token	result;

    result = null;
    
    if (m_OutputToken != null) {
      result        = m_OutputToken;
      m_OutputToken = null;
    }
    else {
      try {
	if ((m_ResultSet != null) && !m_Reader.isFinished())
	  result = new Token(m_Reader.read(m_ResultSet, m_ChunkSize));
	if (m_Reader.isFinished() || m_Reader.isStopped()) {
	  m_Reader    = null;
	  m_ResultSet = null;
	}
      }
      catch (Exception e) {
	handleException("Failed to read the next chunk", e);
      }
    }
    
    return result;
  }

  /**
   * Stops the execution. No message set.
   */
  @Override
  public void stopExecution() {
    if (m_Reader != null)
      m_Reader.stopExecution();
    super.stopExecution();
  }
  
  /**
   * Cleans up after the execution has finished. Graphical output is left
   * untouched.
   */
  @Override
  public void wrapUp() {
    m_DatabaseConnection = null;
    m_Reader             = null;
    m_ResultSet          = null;

    super.wrapUp();
  }
}

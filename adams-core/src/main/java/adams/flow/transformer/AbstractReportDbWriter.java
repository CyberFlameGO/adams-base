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
 * AbstractReportDbWriter.java
 * Copyright (C) 2009-2015 University of Waikato, Hamilton, New Zealand
 */

package adams.flow.transformer;

import adams.core.QuickInfoHelper;
import adams.data.report.Field;
import adams.data.report.Report;
import adams.data.report.ReportHandler;
import adams.db.ReportProvider;
import adams.flow.core.Token;
import adams.flow.transformer.report.AbstractReportPreProcessor;
import adams.flow.transformer.report.NoPreProcessing;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract ancestor for actors that write reports to the database.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 * @param <T> the type of report to handle
 * @param <I> the type of ID used
 */
public abstract class AbstractReportDbWriter<T extends Report, I>
  extends AbstractDbTransformer {

  /** for serialization. */
  private static final long serialVersionUID = -5253006932367969870L;

  /** whether to remove existing reports (i.e., completely remove them from the DB). */
  protected boolean m_RemoveExisting;

  /** whether to merge the current and existing report. */
  protected boolean m_Merge;

  /** the fields to overwrite in "merge" mode. */
  protected Field[] m_OverwriteFields;

  /** whether to allow reports that are not associated with a data container. */
  protected boolean m_StandaloneReports;

  /** the pre-processor to apply to the data. */
  protected AbstractReportPreProcessor m_PreProcessor;

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
	    "remove", "removeExisting",
	    false);

    m_OptionManager.add(
	    "merge", "merge",
	    false);

    m_OptionManager.add(
	    "overwrite", "overwriteFields",
	    new Field[]{});

    m_OptionManager.add(
	    "standalone-reports", "standaloneReports",
	    false);
    
    m_OptionManager.add(
	"pre-processor", "preProcessor",
	new NoPreProcessing());
  }

  /**
   * Sets whether existing reports are removed first.
   *
   * @param value 	if true then existing reports will be removed first
   */
  public void setRemoveExisting(boolean value) {
    m_RemoveExisting = value;
    reset();
  }

  /**
   * Returns whether existing reports are removed first.
   *
   * @return 		true if are removed first
   */
  public boolean getRemoveExisting() {
    return m_RemoveExisting;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String removeExistingTipText() {
    return
        "If true then existing reports will be removed first completely "
      + "from the database before the current one is saved.";
  }

  /**
   * Sets whether the information of the current report is added to an existing
   * one.
   *
   * @param value 	if true then information is added
   */
  public void setMerge(boolean value) {
    m_Merge = value;
    reset();
  }

  /**
   * Returns whether the information of current report is added to an existing
   * one.
   *
   * @return 		true if information is added
   */
  public boolean getMerge() {
    return m_Merge;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String mergeTipText() {
    return
        "If true then the information in the current report is only added to "
      + "the existing one (but '" + Report.FIELD_DUMMYREPORT + "' "
      + "is always set to 'false').";
  }

  /**
   * Sets the fields to overwrite in "merge" mode.
   *
   * @param value 	the fields
   */
  public void setOverwriteFields(Field[] value) {
    m_OverwriteFields = value;
    reset();
  }

  /**
   * Returns the fields to overwrite in "merge" mode.
   *
   * @return 		the fields
   */
  public Field[] getOverwriteFields() {
    return m_OverwriteFields;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String overwriteFieldsTipText() {
    return "The fields to overwrite with the new data when in 'merge' mode.";
  }

  /**
   * Sets whether reports are allowed that are not associated with a
   * data container.
   *
   * @param value 	if true then reports don't have to be associated with
   * 			a data container
   */
  public void setStandaloneReports(boolean value) {
    m_StandaloneReports = value;
    reset();
  }

  /**
   * Returns whether reports don't have to be associated with a data container.
   *
   * @return 		true if reports don't have to be associated with a
   * 			data container
   */
  public boolean getStandaloneReports() {
    return m_StandaloneReports;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String standaloneReportsTipText() {
    return
        "If true then reports don't have to be associated with a data container (= 'standalone').";
  }

  /**
   * Sets the pre-processor to apply to the data.
   *
   * @param value 	the pre-processor
   */
  public void setPreProcessor(AbstractReportPreProcessor value) {
    m_PreProcessor = value;
    m_PreProcessor.setOwner(this);
    reset();
  }

  /**
   * Returns the pre-processor in use.
   *
   * @return 		the pre-processor
   */
  public AbstractReportPreProcessor getPreProcessor() {
    return m_PreProcessor;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String preProcessorTipText() {
    return "The pre-processor to apply to the data.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String 	result;
    List<String> options;

    result = QuickInfoHelper.toString(this, "preProcessor", m_PreProcessor, "pre-processor: ");
    result += QuickInfoHelper.toString(this, "overwriteFields", m_OverwriteFields, ", overwrite fields: ");
    options = new ArrayList<>();
    QuickInfoHelper.add(options, QuickInfoHelper.toString(this, "merge", m_Merge, "merge"));
    QuickInfoHelper.add(options, QuickInfoHelper.toString(this, "removeExisting", m_RemoveExisting, "remove-existing"));
    QuickInfoHelper.add(options, QuickInfoHelper.toString(this, "standaloneReports", m_StandaloneReports, "standalone-reports"));
    result += QuickInfoHelper.flatten(options);

    return result;
  }

  /**
   * Returns the class that the consumer accepts.
   *
   * @return		the report class
   */
  @Override
  public abstract Class[] accepts();

  /**
   * Returns the report provider to use for writing the reports to the database.
   *
   * @return		the provider to use
   */
  protected abstract ReportProvider<T,I> getReportProvider();

  /**
   * Extracts the ID from the report.
   *
   * @param report	the report to extract the ID from
   * @return		the ID
   */
  protected abstract I extractID(T report);

  /**
   * Checks whether the ID is valid.
   *
   * @param id		the ID to check
   * @return		true if valid
   */
  protected abstract boolean checkID(I id);

  /**
   * Executes the flow item.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  protected String queryDatabase() {
    String			result;
    ReportProvider<T,I> 	provider;
    boolean			stored;
    T 				report;
    I 				id;

    result = null;

    if (m_InputToken.getPayload() instanceof ReportHandler)
      report = (T) ((ReportHandler) m_InputToken.getPayload()).getReport();
    else
      report = (T) m_InputToken.getPayload();

    if (report == null) {
      result = "No report attached: " + m_InputToken.getPayload();
    }
    else {
      report = (T) m_PreProcessor.preProcess(report);
      id     = extractID(report);
      stored = false;

      if (m_StandaloneReports || checkID(id)) {
	provider = getReportProvider();
	stored   = provider.store(id, report, m_RemoveExisting, m_Merge, m_OverwriteFields);
      }
      else {
	result = "No container with ID: " + id;
      }

      if (checkID(id) && stored)
	m_OutputToken = new Token(id);
    }

    return result;
  }
}

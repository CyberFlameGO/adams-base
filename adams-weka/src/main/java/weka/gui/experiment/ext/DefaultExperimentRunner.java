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
 * DefaultExperimentRunner.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.experiment.ext;

import java.io.File;
import java.util.Date;

import adams.core.DateUtils;

/**
 * A class that handles running a copy of the experiment
 * in a separate thread.
 * 
 * @see weka.gui.experiment.RunPanel.ExperimentRunner
 */
public class DefaultExperimentRunner
  extends AbstractExperimentRunner {
  
  /** for serialization. */
  private static final long serialVersionUID = -5499408120296699079L;

  /**
   * Initializes the thread.
   * 
   * @param owner		the experimenter this runner belongs to
   * @throws Exception	if experiment is null or cannot be copied via serialization
   */
  public DefaultExperimentRunner(ExperimenterPanel owner) throws Exception {
    super(owner);
  }

  /**
   * Performs the actual running of the experiment.
   * 
   * @throws Exception	fails due to some error
   */
  @Override
  protected void doRun() throws Exception {
    int errors = 0;
    showStatus("Iterating...");
    while (m_Running && m_ExpCopy.hasMoreIterations()) {
      try {
	String current = "Iteration:";
	if (m_ExpCopy.getUsePropertyIterator()) {
	  int cnum = m_ExpCopy.getCurrentPropertyNumber();
	  String ctype = m_ExpCopy.getPropertyArray().getClass().getComponentType().getName();
	  int lastDot = ctype.lastIndexOf('.');
	  if (lastDot != -1)
	    ctype = ctype.substring(lastDot + 1);
	  String cname = " " + ctype + "=" + (cnum + 1) + ":" + m_ExpCopy.getPropertyArrayValue(cnum).getClass().getName();
	  current += cname;
	}
	String dname = ((File) m_ExpCopy.getDatasets().elementAt(m_ExpCopy.getCurrentDatasetNumber())).getName();
	current += " Dataset=" + dname + " Run=" + (m_ExpCopy.getCurrentRunNumber());
	showStatus(current);
	m_ExpCopy.nextIteration();
      } 
      catch (Exception ex) {
	errors++;
	logMessage(ex);
	ex.printStackTrace();
	boolean continueAfterError = false;
	if (continueAfterError)
	  m_ExpCopy.advanceCounters(); // Try to keep plowing through
	else
	  m_Running = false;
      }
    }
    showStatus("Postprocessing...");
    m_ExpCopy.postProcess();
    if (!m_Running)
      logMessage("Interrupted");
    else
      logMessage("Finished");
    if (errors == 1)
      logMessage("There was " + errors + " error");
    else
      logMessage("There were " + errors + " errors");
    showStatus(NOT_RUNNING);
  }
  
  /**
   * Hook method that gets executed after the experiment has finished
   * (successfully or not).
   */
  @Override
  protected void postRun() {
    super.postRun();
    m_Owner.finishExecution();
    update();
    m_Running = false;
    logMessage("Done!");
    logMessage("--> END: " + DateUtils.getTimestampFormatter().format(new Date()));
  }
}
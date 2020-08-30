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
 * ExternalFlow.java
 * Copyright (C) 2014-2020 University of Waikato, Hamilton, New Zealand
 */
package adams.flow.standalone;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.Variables;
import adams.core.VariablesHandler;
import adams.core.io.FlowFile;
import adams.event.VariableChangeEvent;
import adams.event.VariableChangeEvent.Type;
import adams.flow.control.Flow;
import adams.flow.core.AbstractActor;
import adams.flow.core.Actor;
import adams.flow.core.ActorUtils;
import adams.flow.core.AutomatableInteraction;
import adams.flow.core.ExternalActorFileHandler;
import adams.flow.core.RunnableWithLogging;
import adams.flow.processor.ManageInteractiveActors;

import java.util.ArrayList;
import java.util.List;

/**
 <!-- globalinfo-start -->
 * Allows to execute a complete external Flow rather than just an external actor.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
 <!-- flow-summary-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 * 
 * <pre>-name &lt;java.lang.String&gt; (property: name)
 * &nbsp;&nbsp;&nbsp;The name of the actor.
 * &nbsp;&nbsp;&nbsp;default: ExternalFlow
 * </pre>
 * 
 * <pre>-annotation &lt;adams.core.base.BaseAnnotation&gt; (property: annotations)
 * &nbsp;&nbsp;&nbsp;The annotations to attach to this actor.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 * <pre>-skip &lt;boolean&gt; (property: skip)
 * &nbsp;&nbsp;&nbsp;If set to true, transformation is skipped and the input token is just forwarded 
 * &nbsp;&nbsp;&nbsp;as it is.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-stop-flow-on-error &lt;boolean&gt; (property: stopFlowOnError)
 * &nbsp;&nbsp;&nbsp;If set to true, the flow execution at this level gets stopped in case this
 * &nbsp;&nbsp;&nbsp;actor encounters an error; the error gets propagated; useful for critical
 * &nbsp;&nbsp;&nbsp;actors.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-silent &lt;boolean&gt; (property: silent)
 * &nbsp;&nbsp;&nbsp;If enabled, then no errors are output in the console; Note: the enclosing
 * &nbsp;&nbsp;&nbsp;actor handler must have this enabled as well.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-file &lt;adams.core.io.FlowFile&gt; (property: flowFile)
 * &nbsp;&nbsp;&nbsp;The file containing the external flow.
 * &nbsp;&nbsp;&nbsp;default: ${CWD}
 * </pre>
 *
 * <pre>-execution-type &lt;SYNCHRONOUS|SYNCHRONOUS_IMMEDIATE_CLEANUP|ASYNCHRONOUS&gt; (property: executionType)
 * &nbsp;&nbsp;&nbsp;Determines how the flow is executed.
 * &nbsp;&nbsp;&nbsp;default: SYNCHRONOUS
 * </pre>
 *
 * <pre>-headless-mode &lt;boolean&gt; (property: headlessMode)
 * &nbsp;&nbsp;&nbsp;Whether to execute the flow in headless mode.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-non-interactive-mode &lt;boolean&gt; (property: nonInteractiveMode)
 * &nbsp;&nbsp;&nbsp;Whether to run flow in non-interactive mode (disable interactivity of actors
 * &nbsp;&nbsp;&nbsp;implementing adams.flow.core.AutomatableInteraction).
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class ExternalFlow
  extends AbstractActor
  implements ExternalActorFileHandler {

  /** for serialization. */
  private static final long serialVersionUID = 6212392783858480058L;

  /**
   * Determines how the flow is executed.
   * 
   * @author  fracpete (fracpete at waikato dot ac dot nz)
   */
  public enum ExecutionType {
    /** wait for flow to finish. */
    SYNCHRONOUS,
    /** wait for flow to finish and cleanup. */
    SYNCHRONOUS_IMMEDIATE_CLEANUP,
    /** launch flow and don't wait to finish. */
    ASYNCHRONOUS
  }
  
  /** the file the external flow is stored in. */
  protected FlowFile m_ActorFile;

  /** how to execute the flow. */
  protected ExecutionType m_ExecutionType;

  /** whether to run in headless mode. */
  protected boolean m_HeadlessMode;

  /** whether to run in non-interactive mode. */
  protected boolean m_NonInteractiveMode;

  /** the external flow itself. */
  protected Actor m_ExternalFlow;

  /** indicates whether a variable is attached to the external file. */
  protected Boolean m_FlowFileIsVariable;

  /** the variable attached to the external file. */
  protected String m_FlowFileVariable;

  /** whether the external flow file has changed. */
  protected boolean m_FlowFileChanged;
  
  /** the background processes launched. */
  protected List<RunnableWithLogging> m_Asynchronous;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Allows to execute a complete external Flow rather than just an external actor.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "file", "actorFile",
      new FlowFile("."));

    m_OptionManager.add(
      "execution-type", "executionType",
      ExecutionType.SYNCHRONOUS);

    m_OptionManager.add(
      "headless-mode", "headlessMode",
      false);

    m_OptionManager.add(
      "non-interactive-mode", "nonInteractiveMode",
      false);
  }

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();
    
    m_Asynchronous = new ArrayList<>();
  }
  
  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;
    
    result  = QuickInfoHelper.toString(this, "actorFile", m_ActorFile, "file: ");
    result += QuickInfoHelper.toString(this, "executionType", m_ExecutionType, ", execution: ");
    result += QuickInfoHelper.toString(this, "headlessMode", m_HeadlessMode, "headless", ", ");
    result += QuickInfoHelper.toString(this, "nonInteractiveMode", m_NonInteractiveMode, "non-interactive", ", ");

    return result;
  }

  /**
   * Sets the file containing the external actor.
   *
   * @param value	the flow file
   */
  public void setActorFile(FlowFile value) {
    m_ActorFile = value;
    reset();
  }

  /**
   * Returns the file containing the external flow.
   *
   * @return		the flow file
   */
  public FlowFile getActorFile() {
    return m_ActorFile;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String actorFileTipText() {
    return "The file containing the external flow.";
  }

  /**
   * Sets how to execute the flow.
   *
   * @param value	the type
   */
  public void setExecutionType(ExecutionType value) {
    m_ExecutionType = value;
    reset();
  }

  /**
   * Returns how to execute the flow.
   *
   * @return		the type
   */
  public ExecutionType getExecutionType() {
    return m_ExecutionType;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String executionTypeTipText() {
    return "Determines how the flow is executed.";
  }

  /**
   * Sets whether to execute the flow in headless mode.
   *
   * @param value	true if headless
   */
  public void setHeadlessMode(boolean value) {
    m_HeadlessMode = value;
    reset();
  }

  /**
   * Returns whether to execute the flow in headless mode.
   *
   * @return		true if headless
   */
  public boolean getHeadlessMode() {
    return m_HeadlessMode;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String headlessModeTipText() {
    return "Whether to execute the flow in headless mode.";
  }

  /**
   * Sets whether to run the flow in non-interactive mode.
   *
   * @param value	true if non-interactive
   */
  public void setNonInteractiveMode(boolean value) {
    m_NonInteractiveMode = value;
    reset();
  }

  /**
   * Returns whether to run the flow in non-interactive mode.
   *
   * @return		true if non-interactive
   */
  public boolean getNonInteractiveMode() {
    return m_NonInteractiveMode;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String nonInteractiveModeTipText() {
    return "Whether to run flow in non-interactive mode (disable interactivity "
      + "of actors implementing " + AutomatableInteraction.class.getName() + ").";
  }

  /**
   * Gets triggered when a variable changed (added, modified, removed).
   *
   * @param e		the event
   */
  @Override
  public void variableChanged(VariableChangeEvent e) {
    super.variableChanged(e);

    if (m_FlowFileIsVariable == null) {
      m_FlowFileVariable   = getOptionManager().getVariableForProperty("flowFile");
      m_FlowFileIsVariable = (m_FlowFileVariable != null);
      if (m_FlowFileIsVariable)
	m_FlowFileVariable = Variables.extractName(m_FlowFileVariable);
    }

    if ((m_FlowFileIsVariable) && (e.getName().equals(m_FlowFileVariable)))
      m_FlowFileChanged = (e.getType() != Type.REMOVED);
  }

  /**
   * Sets up the external actor.
   *
   * @return		null if everything is fine, otherwise error message
   */
  public String setUpExternalActor() {
    String			result;
    MessageCollection 		errors;
    ManageInteractiveActors	interactive;

    result = null;

    if (!m_ActorFile.isFile()) {
      result = "'" + m_ActorFile.getAbsolutePath() + "' does not point to a file!";
    }
    else {
      errors = new MessageCollection();
      m_ExternalFlow = ActorUtils.read(m_ActorFile.getAbsolutePath(), errors);
      if (!errors.isEmpty()) {
	result = "Error loading external flow '" + m_ActorFile.getAbsolutePath() + "':\n" + errors;
      }
      else if (m_ExternalFlow == null) {
	result = "Error loading external flow '" + m_ActorFile.getAbsolutePath() + "'!";
      }
      else {
	m_ExternalFlow = ActorUtils.removeDisabledActors(m_ExternalFlow);
	if (m_NonInteractiveMode) {
	  interactive = new ManageInteractiveActors();
	  interactive.setEnable(false);
	  interactive.process(m_ExternalFlow);
	  if (interactive.isModified()) {
	    if (isLoggingEnabled())
	      getLogger().info("Disabled interactive actors for: " + m_ActorFile);
	    m_ExternalFlow = interactive.getModifiedActor();
	  }
	}
	if (m_ExternalFlow instanceof Flow)
	  ((Flow) m_ExternalFlow).setHeadless(m_HeadlessMode);
	ActorUtils.updateProgrammaticVariables((VariablesHandler & Actor) m_ExternalFlow, m_ActorFile);
	result = m_ExternalFlow.setUp();
	ActorUtils.updateProgrammaticVariables((VariablesHandler & Actor) m_ExternalFlow, m_ActorFile);
      }
    }

    m_FlowFileChanged = false;

    return result;
  }

  /**
   * Cleans up the external actor.
   */
  public void cleanUpExternalActor() {
    if (m_FlowFileChanged && (m_ExternalFlow != null)) {
      m_ExternalFlow.wrapUp();
      m_ExternalFlow.cleanUp();
      m_ExternalFlow = null;
    }
  }

  @Override
  public String setUp() {
    String	result;
    
    result = super.setUp();
    
    if (result == null) {
      // due to change in variable, we might need to clean up external actor
      cleanUpExternalActor();

      if (getOptionManager().getVariableForProperty("flowFile") == null) {
	if (m_ExternalFlow == null)
	  result = setUpExternalActor();
      }
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
    String		result;
    RunnableWithLogging	run;

    result = null;

    // not setup yet due to variable?
    cleanUpExternalActor();
    if (m_ExternalFlow == null)
      result = setUpExternalActor();

    if (result == null) {
      switch (m_ExecutionType) {
	case SYNCHRONOUS:
	  result = m_ExternalFlow.execute();
	  break;
	case SYNCHRONOUS_IMMEDIATE_CLEANUP:
	  result = m_ExternalFlow.execute();
	  m_ExternalFlow.wrapUp();
	  m_ExternalFlow.destroy();
	  m_ExternalFlow = null;
	  break;
	case ASYNCHRONOUS:
	  run = new RunnableWithLogging() {
	    private static final long serialVersionUID = -3439650903854980640L;
	    @Override
	    protected void doRun() {
	      m_ExternalFlow.execute();
	      m_Asynchronous.remove(this);
	    }
	    @Override
	    public void stopExecution() {
	      m_ExternalFlow.stopExecution();
	      super.stopExecution();
	    }
	  };
	  m_Asynchronous.add(run);
	  new Thread(run).start();
	  break;
	default:
	  throw new IllegalStateException("Unhandled execution type: " + m_ExecutionType);
      }
    }

    return result;
  }

  /**
   * Stops the execution.
   */
  @Override
  public void stopExecution() {
    super.stopExecution();

    if (m_ExternalFlow != null)
      m_ExternalFlow.stopExecution();
  }

  /**
   * Cleans up after the execution has finished. Graphical output is left
   * untouched.
   */
  @Override
  public void wrapUp() {
    if (m_ExternalFlow != null) {
      switch (m_ExecutionType) {
	case SYNCHRONOUS:
	  m_ExternalFlow.wrapUp();
	  break;
	case SYNCHRONOUS_IMMEDIATE_CLEANUP:
	  // nothing to do
	  break;
	case ASYNCHRONOUS:
	  while (m_Asynchronous.size() > 0) {
	    try {
	      synchronized(this) {
		wait(100);
	      }
	    }
	    catch (Exception e) {
	      // ignored
	    }
	  }
	  break;
	default:
	  throw new IllegalStateException("Unhandled execution type: " + m_ExecutionType);
      }
    }

    m_FlowFileIsVariable = null;
    m_FlowFileVariable   = null;
    m_FlowFileChanged    = false;

    super.wrapUp();
  }

  /**
   * Cleans up after the execution has finished.
   */
  @Override
  public void cleanUp() {
    if (m_ExternalFlow != null) {
      m_ExternalFlow.destroy();
      m_ExternalFlow = null;
    }

    super.cleanUp();
  }
}

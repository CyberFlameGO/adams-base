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
 * Stop.java
 * Copyright (C) 2010-2017 University of Waikato, Hamilton, New Zealand
 */
package adams.flow.control;

import adams.core.QuickInfoHelper;
import adams.flow.core.AbstractActor;
import adams.flow.core.Actor;
import adams.flow.core.ControlActor;
import adams.flow.core.InputConsumer;
import adams.flow.core.StopHelper;
import adams.flow.core.StopMode;
import adams.flow.core.StopModeSupporter;
import adams.flow.core.StopRestrictor;
import adams.flow.core.Token;
import adams.flow.core.Unknown;

/**
 <!-- globalinfo-start -->
 * Stops the execution of the flow when triggered.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
 * Input&#47;output:<br>
 * - accepts:<br>
 * &nbsp;&nbsp;&nbsp;adams.flow.core.Unknown<br>
 * <br><br>
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
 * &nbsp;&nbsp;&nbsp;default: Stop
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
 * &nbsp;&nbsp;&nbsp;If set to true, the flow gets stopped in case this actor encounters an error;
 * &nbsp;&nbsp;&nbsp; useful for critical actors.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-silent &lt;boolean&gt; (property: silent)
 * &nbsp;&nbsp;&nbsp;If enabled, then no errors are output in the console.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-stop-msg &lt;java.lang.String&gt; (property: stopMessage)
 * &nbsp;&nbsp;&nbsp;The optional stop message to send when stopping the flow; variables get 
 * &nbsp;&nbsp;&nbsp;automatically expanded.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class Stop
  extends AbstractActor
  implements ControlActor, InputConsumer, StopModeSupporter {

  /** for serialization. */
  private static final long serialVersionUID = -6615127883045169960L;

  /** the stop message to fail with. */
  protected String m_StopMessage;

  /** how to perform the stop. */
  protected StopMode m_StopMode;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return
      "Stops the execution of the flow when triggered.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "stop-msg", "stopMessage",
      "");

    m_OptionManager.add(
      "stop-mode", "stopMode",
      StopMode.GLOBAL);
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result  = QuickInfoHelper.toString(this, "stopMessage", (m_StopMessage.length() > 0 ? m_StopMessage : null));
    result += QuickInfoHelper.toString(this, "stopMode", m_StopMode, ", mode: ");

    return result;
  }

  /**
   * Sets the stop message.
   *
   * @param value	the message
   */
  public void setStopMessage(String value) {
    m_StopMessage = value;
    reset();
  }

  /**
   * Returns the stop message.
   *
   * @return		the message
   */
  @Override
  public String getStopMessage() {
    return m_StopMessage;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String stopMessageTipText() {
    return "The optional stop message to send when stopping the flow; variables get automatically expanded.";
  }

  /**
   * Sets the stop mode.
   *
   * @param value	the mode
   */
  @Override
  public void setStopMode(StopMode value) {
    m_StopMode = value;
    reset();
  }

  /**
   * Returns the stop mode.
   *
   * @return		the mode
   */
  @Override
  public StopMode getStopMode() {
    return m_StopMode;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  @Override
  public String stopModeTipText() {
    return "The stop mode to use.";
  }

  /**
   * Returns the class that the consumer accepts.
   *
   * @return		<!-- flow-accepts-start -->adams.flow.core.Unknown.class<!-- flow-accepts-end -->
   */
  public Class[] accepts() {
    return new Class[]{Unknown.class};
  }

  /**
   * Does nothing.
   *
   * @param token	the token to accept and process
   */
  public void input(Token token) {
  }

  /**
   * Returns whether an input token is currently present.
   *
   * @return		always false
   */
  public boolean hasInput() {
    return false;
  }

  /**
   * Returns the current input token, if any.
   *
   * @return		always null
   */
  public Token currentInput() {
    return null;
  }

  /**
   * Returns the enclosing {@link StopRestrictor}.
   *
   * @return		the restrictor
   */
  protected Actor getStopRestrictor() {
    Actor	result;
    Actor	parent;

    result = null;
    parent = getParent();
    do {
      if (parent instanceof StopRestrictor)
	result = parent;
    }
    while ((parent != null) && (result == null));

    if (result == null)
      result = getRoot();

    return result;
  }

  /**
   * Executes the flow item.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  protected String doExecute() {
    return StopHelper.stop(this, m_StopMode, (m_StopMessage.length() == 0) ? null : m_StopMessage);
  }
}

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
 * PathBreakpoint.java
 * Copyright (C) 2013-2015 University of Waikato, Hamilton, New Zealand
 */
package adams.flow.execution.debug;

import adams.flow.core.Actor;
import adams.flow.core.Token;
import adams.flow.execution.debug.AbstractBreakpoint;

/**
 <!-- globalinfo-start -->
 * Triggers with any actor encountered during listening.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 * 
 * <pre>-disabled &lt;boolean&gt; (property: disabled)
 * &nbsp;&nbsp;&nbsp;If set to true, the breakpoint is completely disabled.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-on-pre-input &lt;boolean&gt; (property: onPreInput)
 * &nbsp;&nbsp;&nbsp;If set to true, the breakpoint gets evaluated at pre-input (of token) time;
 * &nbsp;&nbsp;&nbsp; token available.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-on-post-input &lt;boolean&gt; (property: onPostInput)
 * &nbsp;&nbsp;&nbsp;If set to true, the breakpoint gets evaluated at post-input (of token) time.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-on-pre-execute &lt;boolean&gt; (property: onPreExecute)
 * &nbsp;&nbsp;&nbsp;If set to true, the breakpoint gets evaluated at pre-execute time.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-on-post-execute &lt;boolean&gt; (property: onPostExecute)
 * &nbsp;&nbsp;&nbsp;If set to true, the breakpoint gets evaluated at post-execute time.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-on-pre-output &lt;boolean&gt; (property: onPreOutput)
 * &nbsp;&nbsp;&nbsp;If set to true, the breakpoint gets evaluated at pre-output (of token) time;
 * &nbsp;&nbsp;&nbsp; token available.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-on-post-output &lt;boolean&gt; (property: onPostOutput)
 * &nbsp;&nbsp;&nbsp;If set to true, the breakpoint gets evaluated at post-output (of token) 
 * &nbsp;&nbsp;&nbsp;time.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-watch &lt;adams.core.base.BaseString&gt; [-watch ...] (property: watches)
 * &nbsp;&nbsp;&nbsp;The expression to display initially in the watch dialog; the type of the 
 * &nbsp;&nbsp;&nbsp;watch needs to be specified as well.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 * <pre>-watch-type &lt;VARIABLE|BOOLEAN|NUMERIC|STRING&gt; [-watch-type ...] (property: watchTypes)
 * &nbsp;&nbsp;&nbsp;The types of the watch expressions; determines how the expressions get evaluated 
 * &nbsp;&nbsp;&nbsp;and displayed.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 * <pre>-view &lt;SOURCE|EXPRESSIONS|VARIABLES|STORAGE|INSPECT_TOKEN|BREAKPOINTS&gt; [-view ...] (property: views)
 * &nbsp;&nbsp;&nbsp;The views to display automatically when the breakpoint is reached.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class AnyActorBreakpoint
  extends AbstractBreakpoint {

  /** for serialization. */
  private static final long serialVersionUID = 3782327753485131754L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Triggers with any actor encountered during listening.";
  }

  /**
   * Evaluates the breakpoint at pre-input.
   * 
   * @param actor	the current actor
   * @param token	the token available for input
   * @return		true if breakpoint triggers
   */
  @Override
  protected boolean evaluatePreInput(Actor actor, Token token) {
    return true;
  }
  
  /**
   * Evaluates the breakpoint at post-input.
   * 
   * @param actor	the current actor
   * @return		true if breakpoint triggers
   */
  @Override
  protected boolean evaluatePostInput(Actor actor) {
    return true;
  }
  
  /**
   * Evaluates the breakpoint at pre-execute.
   * 
   * @param actor	the current actor
   * @return		true if breakpoint triggers
   */
  @Override
  protected boolean evaluatePreExecute(Actor actor) {
    return true;
  }
  
  /**
   * Evaluates the breakpoint at post-execute.
   * 
   * @param actor	the current actor
   * @return		true if breakpoint triggers
   */
  @Override
  protected boolean evaluatePostExecute(Actor actor) {
    return true;
  }
  
  /**
   * Evaluates the breakpoint at pre-output.
   * 
   * @param actor	the current actor
   * @return		true if breakpoint triggers
   */
  @Override
  protected boolean evaluatePreOutput(Actor actor) {
    return true;
  }
  
  /**
   * Evaluates the breakpoint at post-output.
   * 
   * @param actor	the current actor
   * @param token	the token available for output
   * @return		true if breakpoint triggers
   */
  @Override
  protected boolean evaluatePostOutput(Actor actor, Token token) {
    return true;
  }
}

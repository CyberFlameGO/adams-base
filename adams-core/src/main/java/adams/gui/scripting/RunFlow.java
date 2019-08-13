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
 * RunFlow.java
 * Copyright (C) 2009-2019 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.scripting;

import adams.flow.control.SubProcess;

/**
 * Abstract ancestor for scriptlets that run flows and replace the data
 * containers.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class RunFlow
  extends AbstractFlowScriptlet {

  /** for serialization. */
  private static final long serialVersionUID = -2210176790250818043L;

  /** the action to execute. */
  public final static String ACTION = "run-flow";

  /**
   * Returns the action string used in the command processor.
   *
   * @return		the action string
   */
  public String getAction() {
    return ACTION;
  }

  /**
   * Returns the full description of the action.
   *
   * @return		the full description
   */
  public String getDescription() {
    return
        "Executes the flow stored in the given file.\n"
      + "The base actor has to be '" + SubProcess.class.getName() + "'.\n"
      + "The processed " + getOwner().getRequiredFlowClass().getName().replaceAll(".*\\.", "") + "s "
      + "replace the currently loaded ones.";
  }

  /**
   * Processes the options.
   *
   * @param options	additional/optional options for the action
   * @return		null if no error, otherwise error message
   * @throws Exception 	if something goes wrong
   */
  protected String doProcess(String options) throws Exception {
    return process(options, false);
  }
}

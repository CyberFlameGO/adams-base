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
 * LogTextBoxRequestHandler.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package adams.scripting.requesthandler;

import adams.core.logging.LoggingLevel;
import adams.core.option.OptionUtils;
import adams.scripting.command.RemoteCommand;
import adams.terminal.core.LogTextBox;

/**
 * For logging requests.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class LogTextBoxRequestHandler
  extends AbstractRequestHandler {

  private static final long serialVersionUID = -1354303964630420400L;

  /** the log to use. */
  protected LogTextBox m_Log;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Logs requests.";
  }

  /**
   * Sets the log to use.
   *
   * @param value	the log
   */
  public void setLog(LogTextBox value) {
    m_Log = value;
  }

  /**
   * Returns the log in use.
   *
   * @return		the log
   */
  public LogTextBox getLog() {
    return m_Log;
  }

  /**
   * Handles successfuly requests.
   *
   * @param cmd		the command with the request
   */
  @Override
  public void requestSuccessful(RemoteCommand cmd) {
    if (m_Enabled && (m_Log != null))
      m_Log.append(LoggingLevel.INFO, "Successful request: " + OptionUtils.getCommandLine(cmd) + "\n" + cmd);
  }

  /**
   * Handles failed requests.
   *
   * @param cmd		the command with the request
   * @param msg		the optional error message, can be null
   */
  @Override
  public void requestFailed(RemoteCommand cmd, String msg) {
    if (m_Enabled && (m_Log != null))
      m_Log.append(LoggingLevel.SEVERE, "Failed request: " + OptionUtils.getCommandLine(cmd) + "\nMessage: " + msg + "\n" + cmd);
  }

  /**
   * Handles rejected requests.
   *
   * @param cmd		the command with the request
   * @param msg		the optional error message, can be null
   */
  @Override
  public void requestRejected(RemoteCommand cmd, String msg) {
    if (m_Enabled && (m_Log != null))
      m_Log.append(LoggingLevel.WARNING, "Rejected request: " + OptionUtils.getCommandLine(cmd) + "\nMessage: " + msg + "\n" + cmd);
  }
}

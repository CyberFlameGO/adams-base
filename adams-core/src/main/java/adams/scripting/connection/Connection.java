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
 * Connection.java
 * Copyright (C) 2016-2017 University of Waikato, Hamilton, NZ
 */

package adams.scripting.connection;

import adams.core.CleanUpHandler;
import adams.core.QuickInfoSupporter;
import adams.core.option.OptionHandler;
import adams.scripting.command.RemoteCommand;
import adams.scripting.processor.RemoteCommandProcessor;

/**
 * Interface for class that handle connections for remote commands.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public interface Connection
  extends OptionHandler, CleanUpHandler, Comparable<Connection>, QuickInfoSupporter {

  /**
   * Sends the request command.
   *
   * @param cmd		the command to send
   * @param processor	the processor for formatting/parsing
   * @return		null if successful, otherwise error message
   */
  public String sendRequest(RemoteCommand cmd, RemoteCommandProcessor processor);

  /**
   * Sends the response command.
   *
   * @param cmd		the command to send
   * @param processor	the processor for formatting/parsing
   * @return		null if successful, otherwise error message
   */
  public String sendResponse(RemoteCommand cmd, RemoteCommandProcessor processor);
}

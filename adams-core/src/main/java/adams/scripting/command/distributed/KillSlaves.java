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
 * KillSlaves.java
 * Copyright (C) 2016-2017 University of Waikato, Hamilton, NZ
 */

package adams.scripting.command.distributed;

import adams.scripting.command.AbstractCommand;
import adams.scripting.engine.RemoteScriptingEngine;
import adams.scripting.engine.SlaveScriptingEngine;
import adams.scripting.processor.RemoteCommandProcessor;

/**
 * Kills all {@link SlaveScriptingEngine} that are registered with the master
 * receiving this message.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class KillSlaves
  extends AbstractCommand {

  private static final long serialVersionUID = -1657908444959620122L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return
      "Kills all the " + SlaveScriptingEngine.class.getName() + " registered "
	+ "with the master receiving this message.";
  }

  /**
   * Sets the payload for the request.
   *
   * @param value	the payload
   */
  @Override
  public void setRequestPayload(byte[] value) {
  }

  /**
   * Returns the payload of the request, if any.
   *
   * @return		the payload
   */
  @Override
  public byte[] getRequestPayload() {
    return new byte[0];
  }

  /**
   * Returns the objects that represent the request payload.
   *
   * @return		the objects
   */
  public Object[] getRequestPayloadObjects() {
    return new Object[0];
  }

  /**
   * Handles the request.
   *
   * @param engine	the remote engine handling the request
   * @param processor 	the processor for formatting/parsing
   * @return		null if successful, otherwise error message
   */
  protected String doHandleRequest(RemoteScriptingEngine engine, RemoteCommandProcessor processor) {
    return null;
  }
}

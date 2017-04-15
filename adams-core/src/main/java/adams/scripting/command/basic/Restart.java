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
 * Restart.java
 * Copyright (C) 2016-2017 University of Waikato, Hamilton, NZ
 */

package adams.scripting.command.basic;

import adams.core.ClassCrossReference;
import adams.core.management.Launcher;
import adams.scripting.engine.RemoteScriptingEngine;

/**
 * Attempts to restart the remote ADAMS instance.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class Restart
  extends AbstractCommandWithFlowStopping
  implements ClassCrossReference {

  private static final long serialVersionUID = -1657908444959620122L;

  /** whether to restart with more memory. */
  protected boolean m_MoreMemory;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return
      "Restarts the ADAMS instance. This only works if the ADAMS instance "
	+ "was started through the " + Launcher.class.getName() + ".";
  }

  /**
   * Returns the cross-referenced classes.
   *
   * @return		the classes
   */
  public Class[] getClassCrossReferences() {
    return new Class[]{Kill.class, Stop.class};
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "more-memory", "moreMemory",
      false);
  }

  /**
   * Sets whether to restart with more memory.
   *
   * @param value	true if to restart with more memory
   */
  public void setMoreMemory(boolean value) {
    m_MoreMemory = value;
    reset();
  }

  /**
   * Returns whether to restart with more memory.
   *
   * @return		true if to restart with more memory
   */
  public boolean getMoreMemory() {
    return m_MoreMemory;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the gui
   */
  public String moreMemoryTipText() {
    return "If enabled, the ADAMS instance gets restarted with more memory.";
  }

  /**
   * Ignored.
   *
   * @param value	the payload
   */
  @Override
  public void setRequestPayload(byte[] value) {
  }

  /**
   * Always zero-length array.
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
   * @return		null if successful, otherwise error message
   */
  protected String doHandleRequest(RemoteScriptingEngine engine) {
    if (m_StopFlows)
      stopFlows();

    if (getRemoteScriptingEngineHandler() != null) {
      if (getRemoteScriptingEngineHandler().getRemoteScriptingEngine() != null) {
        getLogger().info("Stopping scripting engine");
        getRemoteScriptingEngineHandler().getRemoteScriptingEngine().stopExecution();
      }
    }

    getLogger().info("Restarting" + (m_MoreMemory ? " using more memory" : ""));
    if (m_MoreMemory)
      System.exit(Launcher.CODE_RESTART_MORE_HEAP);
    else
      System.exit(Launcher.CODE_RESTART);
    return null;
  }
}

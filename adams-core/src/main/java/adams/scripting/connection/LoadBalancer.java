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
 * LoadBalancer.java
 * Copyright (C) 2016-2017 University of Waikato, Hamilton, NZ
 */

package adams.scripting.connection;

import adams.scripting.command.RemoteCommand;
import adams.scripting.processor.RemoteCommandProcessor;

/**
 * Balances the handling of commands among several connections.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class LoadBalancer
  extends AbstractMultiConnection {

  private static final long serialVersionUID = 6581951716043112610L;

  /** the current connection for requests. */
  protected int m_CurrentRequest;

  /** the current connection for responses. */
  protected int m_CurrentResponse;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Balances the handling of commands among several connections.";
  }

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_CurrentRequest  = 0;
    m_CurrentResponse = 0;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the gui
   */
  public String connectionsTipText() {
    return "The connections to use for balancing.";
  }

  /**
   * Hook method that checks the request command and setup for sending it.
   *
   * @param cmd		the command to check
   * @return		null if successful, otherwise error message
   */
  protected String checkRequest(RemoteCommand cmd) {
    String	result;

    result = super.checkRequest(cmd);

    if (result == null) {
      if (m_Connections.size() == 0)
	result = "No connections defined for balancing!";
    }

    return result;
  }

  /**
   * Sends the request command.
   *
   * @param cmd		the command to send
   * @param processor 	the processor for formatting/parsing
   * @return		null if successful, otherwise error message
   */
  @Override
  protected String doSendRequest(RemoteCommand cmd, RemoteCommandProcessor processor) {
    String	result;

    result = m_Connections.get(m_CurrentRequest).sendRequest(cmd, processor);
    m_CurrentRequest++;
    if (m_CurrentRequest >= m_Connections.size())
      m_CurrentRequest = 0;

    return result;
  }

  /**
   * Hook method that checks the response command and setup for sending it.
   *
   * @param cmd		the command to check
   * @return		null if successful, otherwise error message
   */
  protected String checkResponse(RemoteCommand cmd) {
    String	result;

    result = super.checkResponse(cmd);

    if (result == null) {
      if (m_Connections.size() == 0)
	result = "No connections defined for balancing!";
    }

    return result;
  }

  /**
   * Sends the response command.
   *
   * @param cmd		the command to send
   * @param processor 	the processor for formatting/parsing
   * @return		null if successful, otherwise error message
   */
  @Override
  protected String doSendResponse(RemoteCommand cmd, RemoteCommandProcessor processor) {
    String	result;

    result = m_Connections.get(m_CurrentResponse).sendResponse(cmd, processor);
    m_CurrentResponse++;
    if (m_CurrentResponse >= m_Connections.size())
      m_CurrentResponse = 0;

    return result;
  }
}

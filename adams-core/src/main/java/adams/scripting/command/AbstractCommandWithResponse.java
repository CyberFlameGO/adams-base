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
 * AbstractCommandWithResponse.java
 * Copyright (C) 2016-2017 University of Waikato, Hamilton, NZ
 */

package adams.scripting.command;

import adams.core.Properties;
import adams.core.Utils;
import adams.core.io.GzipUtils;
import adams.core.option.OptionUtils;
import adams.scripting.ScriptingHelper;
import adams.scripting.connection.Connection;
import adams.scripting.engine.RemoteScriptingEngine;
import adams.scripting.processor.RemoteCommandProcessor;
import adams.scripting.responsehandler.ResponseHandler;

/**
 * Ancestor for commands that send a response.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public abstract class AbstractCommandWithResponse
  extends AbstractCommand
  implements RemoteCommandWithResponse {

  private static final long serialVersionUID = -2803551461382517312L;

  /** the key for the error message (optional). */
  String KEY_ERRORMESSAGE = "ErrorMessage";

  /** the response connection. */
  protected Connection m_ResponseConnection;

  /** the error message. */
  protected String m_ErrorMessage;

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "response-connection", "responseConnection",
      getDefaultResponseConnection());
  }

  /**
   * Returns the default connection to use.
   *
   * @return		the connection
   */
  protected Connection getDefaultResponseConnection() {
    return ScriptingHelper.getSingleton().getDefaultConnection();
  }

  /**
   * Sets the connection to send the response to.
   *
   * @param value	the connection
   */
  public void setResponseConnection(Connection value) {
    m_ResponseConnection = value;
    reset();
  }

  /**
   * Returns the connection to send the response to.
   *
   * @return		the connection
   */
  public Connection getResponseConnection() {
    return m_ResponseConnection;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the gui
   */
  public String responseConnectionTipText() {
    return "The connection to send the response to.";
  }

  /**
   * Returns whether an error message is available.
   *
   * @return		true if an error message available
   */
  public boolean hasErrorMessage() {
    return (m_ErrorMessage != null);
  }

  /**
   * Returns the error message (if any).
   *
   * @return		the error message, null if none availabe
   */
  public String getErrorMessage() {
    return m_ErrorMessage;
  }

  /**
   * Parses the header information.
   *
   * @param header	the header
   * @return		null if successfully parsed, otherwise error message
   */
  public String parse(Properties header) {
    String	result;

    result = super.parse(header);
    if (result == null) {
      if (header.hasKey(KEY_ERRORMESSAGE))
	m_ErrorMessage = header.getProperty(KEY_ERRORMESSAGE);
    }

    return null;
  }

  /**
   * Assembles the response header.
   *
   * @return		the response header
   */
  protected Properties assembleResponseHeader() {
    Properties		result;

    result = new Properties();
    result.setProperty(KEY_COMMAND, OptionUtils.getCommandLine(this));
    result.setProperty(KEY_TYPE, VALUE_RESPONSE);

    return result;
  }

  /**
   * Hook method for preparing the response payload,
   * <br>
   * Default implementation does nothing.
   */
  protected void prepareResponsePayload() {
  }

  /**
   * Assembles the command into a string, including any payload.
   *
   * @param processor 	for formatting/parsing
   * @return		the generated string, null if failed to assemble
   */
  public String assembleResponse(RemoteCommandProcessor processor) {
    Properties		header;
    byte[]		payload;

    m_ErrorMessage = null;

    // header
    header = assembleResponseHeader();

    // payload
    prepareResponsePayload();
    if (hasErrorMessage()) {
      header.setProperty(KEY_ERRORMESSAGE, getErrorMessage());
      getLogger().severe(getErrorMessage());
    }
    payload = getResponsePayload();
    if (payload.length > 0)
      payload = GzipUtils.compress(payload);

    return processor.format(header, payload);
  }

  /**
   * Handles the request.
   *
   * @param engine	the remote engine handling the request
   * @return		null if successful, otherwise error message
   */
  @Override
  protected String doHandleRequest(RemoteScriptingEngine engine, RemoteCommandProcessor processor) {
    setRequest(false);  // to avoid checks to fail
    return m_ResponseConnection.sendResponse(this, processor);
  }

  /**
   * Handles the response.
   *
   * @param engine	the remote engine handling the response
   * @param handler	for handling the response
   */
  @Override
  public void handleResponse(RemoteScriptingEngine engine, ResponseHandler handler) {
    handler.responseSuccessful(this);
  }

  /**
   * Hook method before sending the response.
   * <br>
   * Default implementation does nothing.
   */
  public void beforeSendResponse() {
  }

  /**
   * Hook method after sending the response.
   * <br>
   * Default implementation does nothing.
   *
   * @param error	null if successful, otherwise error message
   */
  public void afterSendResponse(String error) {
  }

  /**
   * Returns a short description of the command.
   *
   * @return		the description
   */
  public String toString() {
    return getClass().getName() + ": " + Utils.arrayToString(isRequest() ? getRequestPayloadObjects() : getResponsePayloadObjects());
  }
}

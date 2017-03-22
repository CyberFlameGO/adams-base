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
 * GetFlow.java
 * Copyright (C) 2016-2017 University of Waikato, Hamilton, NZ
 */

package adams.scripting.command.flow;

import adams.core.io.FileWriter;
import adams.core.io.PlaceholderFile;
import adams.data.io.input.DefaultFlowReader;
import adams.data.io.output.DefaultFlowWriter;
import adams.flow.core.Actor;
import adams.flow.core.ActorUtils;
import adams.scripting.command.AbstractRemoteCommandOnFlowWithResponse;
import adams.scripting.engine.RemoteScriptingEngine;
import adams.scripting.responsehandler.ResponseHandler;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * Retrieves a running/registered flow using its ID.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class GetFlow
  extends AbstractRemoteCommandOnFlowWithResponse
  implements FileWriter {

  private static final long serialVersionUID = -3350680106789169314L;

  /** whether to load the flow from disk. */
  protected boolean m_LoadFromDisk;

  /** the file to save the flow to. */
  protected PlaceholderFile m_OutputFile;

  /** the flow. */
  protected Actor m_Flow;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Retrieves a running/registered flow using its ID.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "load-from-disk", "loadFromDisk",
      true);

    m_OptionManager.add(
      "output-file", "outputFile",
      new PlaceholderFile());
  }

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_Flow = null;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the gui
   */
  public String IDTipText() {
    return "The ID of the flow to get; -1 if to retrieve the only one.";
  }

  /**
   * Sets whether to load the flow from disk rather than retrieve it from memory.
   *
   * @param value	true if to load from disk
   */
  public void setLoadFromDisk(boolean value) {
    m_LoadFromDisk = value;
    reset();
  }

  /**
   * Returns whether to load the flow from disk rather than retrieve it from memory.
   *
   * @return		true if to load from disk
   */
  public boolean getLoadFromDisk() {
    return m_LoadFromDisk;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the gui
   */
  public String loadFromDiskTipText() {
    return "If enabled, the flow gets loaded from disk rather than retrieving the one currently in memory.";
  }

  /**
   * Set output file.
   *
   * @param value	file
   */
  public void setOutputFile(PlaceholderFile value) {
    m_OutputFile = value;
    reset();
  }

  /**
   * Get output file.
   *
   * @return	file
   */
  public PlaceholderFile getOutputFile() {
    return m_OutputFile;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String outputFileTipText() {
    return "The file to save the retrieved flow to.";
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
   * Sets the payload for the response.
   *
   * @param value	the payload
   */
  @Override
  public void setResponsePayload(byte[] value) {
    Actor		flow;
    String		cmd;
    StringReader	sreader;
    DefaultFlowReader	freader;

    if (value.length == 0) {
      m_Flow = null;
      return;
    }

    flow = null;
    cmd  = new String(value);
    try {
      sreader = new StringReader(cmd);
      freader = new DefaultFlowReader();
      flow    = freader.readActor(sreader);
    }
    catch (Exception e) {
      getLogger().severe("Failed to instantiate actor from:\n" + cmd);
    }

    m_Flow = flow;
  }

  /**
   * Returns the payload of the response, if any.
   *
   * @return		the payload
   */
  @Override
  public byte[] getResponsePayload() {
    DefaultFlowWriter 	fwriter;
    StringWriter	swriter;

    if (m_Flow == null)
      return new byte[0];

    swriter = new StringWriter();
    fwriter = new DefaultFlowWriter();
    fwriter.setUseCompact(true);
    fwriter.write(m_Flow, swriter);

    return swriter.toString().getBytes();
  }

  /**
   * Hook method for preparing the response payload,
   */
  @Override
  protected void prepareResponsePayload() {
    super.prepareResponsePayload();
    m_Flow = retrieveFlow(m_LoadFromDisk);
  }

  /**
   * Returns the objects that represent the response payload.
   *
   * @return		the objects
   */
  public Object[] getResponsePayloadObjects() {
    return new Object[]{m_Flow};
  }

  /**
   * Handles the response.
   *
   * @param engine	the remote engine handling the response
   * @param handler	for handling the response
   */
  @Override
  public void handleResponse(RemoteScriptingEngine engine, ResponseHandler handler) {
    String	msg;

    if (!m_OutputFile.isDirectory()) {
      msg = null;
      if (m_Flow != null) {
	if (!ActorUtils.write(m_OutputFile.getAbsolutePath(), m_Flow))
	  msg = "Failed to save flow to: " + m_OutputFile;
      }
      if (msg != null)
	handler.responseFailed(this, msg);
      else
	super.handleResponse(engine, handler);
    }
    else {
      if (m_Flow != null)
	getLogger().info(m_Flow.toCommandLine());
      super.handleResponse(engine, handler);
    }
  }
}

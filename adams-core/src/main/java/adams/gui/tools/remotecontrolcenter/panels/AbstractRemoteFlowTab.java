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
 * AbstractRemoteFlowTab.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package adams.gui.tools.remotecontrolcenter.panels;

import adams.core.base.BaseHostname;
import adams.core.net.PortManager;
import adams.data.spreadsheet.SpreadSheet;
import adams.gui.core.BaseObjectTextField;
import adams.gui.core.BaseSplitPane;
import adams.gui.core.GUIHelper;
import adams.gui.core.SpreadSheetTable;
import adams.gui.core.SpreadSheetTableModel;
import adams.gui.event.PopupMenuListener;
import adams.scripting.command.RemoteCommand;
import adams.scripting.command.RemoteCommandWithResponse;
import adams.scripting.command.basic.StopEngine;
import adams.scripting.command.basic.StopEngine.EngineType;
import adams.scripting.command.flow.ListFlows;
import adams.scripting.connection.DefaultConnection;
import adams.scripting.engine.DefaultScriptingEngine;
import adams.scripting.requesthandler.RequestHandler;
import adams.scripting.requesthandler.SimpleLogPanelRequestHandler;
import adams.scripting.responsehandler.AbstractResponseHandler;
import adams.scripting.responsehandler.ResponseHandler;
import adams.scripting.responsehandler.SimpleLogPanelResponseHandler;
import com.googlecode.jfilechooserbookmarks.gui.BaseScrollPane;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * Ancestor for tabs that get applied to remote flows using their ID(s).
 * For simplicity, only uses {@link DefaultConnection} which communicates
 * via sockets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public abstract class AbstractRemoteFlowTab
  extends AbstractRemoteControlCenterTab
  implements PopupMenuListener, ListSelectionListener {

  private static final long serialVersionUID = 321058606982723480L;

  /**
   * Custom handler for intercepting the responses from the {@link ListFlows}
   * remote command.
   *
   * @author FracPete (fracpete at waikato dot ac dot nz)
   * @version $Revision$
   */
  public static class FlowListResponseHandler
    extends AbstractResponseHandler {

    private static final long serialVersionUID = 6205405220037007365L;

    /** the owner. */
    protected AbstractRemoteFlowTab m_Tab;

    /**
     * Initializes the handler.
     *
     * @param tab	the tab this handler belongs to
     */
    public FlowListResponseHandler(AbstractRemoteFlowTab tab) {
      super();
      m_Tab = tab;
    }

    /**
     * Returns a string describing the object.
     *
     * @return 			a description suitable for displaying in the gui
     */
    @Override
    public String globalInfo() {
      return "Ties into " + AbstractRemoteFlowTab.class.getName() + " derived tabs.";
    }

    /**
     * Handles successful responses.
     *
     * @param cmd		the command with the response
     */
    @Override
    public void responseSuccessful(RemoteCommand cmd) {
      ListFlows		list;

      if (cmd instanceof ListFlows) {
	list = (ListFlows) cmd;
	if (list.getResponsePayloadObjects().length > 0) {
	  m_Tab.getFlowsTable().setModel(
	    new SpreadSheetTableModel(
	      (SpreadSheet) list.getResponsePayloadObjects()[0]));
	  m_Tab.getFlowsTable().setShowRowColumn(false);
	  m_Tab.getFlowsTable().setUseSimpleHeader(true);
	}
      }
    }

    /**
     * Handles failed responses.
     *
     * @param cmd		the command with the response
     * @param msg		message, can be null
     */
    @Override
    public void responseFailed(RemoteCommand cmd, String msg) {
      if (cmd instanceof ListFlows) {
	m_Tab.getFlowsTable().setModel(new SpreadSheetTableModel());
	m_Tab.getOwner().logError("Failed to retrieve remote flows:\n" + msg, "List remote flows");
      }
    }
  }

  /** the default port to use for refreshing flows. */
  public final static int DEFAULT_PORT = 21345;

  /** the split pane. */
  protected BaseSplitPane m_SplitPane;

  /** the panel for the connection/table. */
  protected JPanel m_PanelFlows;

  /** the remote machine. */
  protected BaseObjectTextField<BaseHostname> m_TextRemote;

  /** the local machine. */
  protected BaseObjectTextField<BaseHostname> m_TextLocal;

  /** the button for refreshing the flows. */
  protected JButton m_ButtonRefresh;

  /** the table with the remote flows. */
  protected SpreadSheetTable m_TableFlows;

  /**
   * Initializes the widgets.
   */
  @Override
  protected void initGUI() {
    JPanel			panelConn;
    JPanel			panelButton;
    JLabel 			label;

    super.initGUI();

    setLayout(new BorderLayout());

    m_SplitPane = new BaseSplitPane(BaseSplitPane.VERTICAL_SPLIT);
    m_SplitPane.setDividerLocation(200);
    m_SplitPane.setResizeWeight(0.5);
    add(m_SplitPane, BorderLayout.CENTER);

    m_PanelFlows = new JPanel(new BorderLayout());
    m_SplitPane.setTopComponent(m_PanelFlows);

    panelConn = new JPanel(new FlowLayout(FlowLayout.LEFT));
    m_PanelFlows.add(panelConn, BorderLayout.NORTH);
    
    m_TextRemote = new BaseObjectTextField<>(new BaseHostname(), "127.0.0.1:12345");
    m_TextRemote.setColumns(20);
    label = new JLabel("Remote");
    label.setDisplayedMnemonic('R');
    label.setLabelFor(m_TextRemote);
    panelConn.add(label);
    panelConn.add(m_TextRemote);
    
    m_TextLocal = new BaseObjectTextField<>(new BaseHostname(), "127.0.0.1:" + DEFAULT_PORT);
    m_TextLocal.setColumns(20);
    label = new JLabel("Local");
    label.setDisplayedMnemonic('L');
    label.setLabelFor(m_TextLocal);
    panelConn.add(label);
    panelConn.add(m_TextLocal);

    m_ButtonRefresh = new JButton(GUIHelper.getIcon("refresh.gif"));
    m_ButtonRefresh.addActionListener((ActionEvent e) -> refreshFlows());
    panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panelButton.add(m_ButtonRefresh);
    panelConn.add(panelButton);

    m_TableFlows = new SpreadSheetTable(new SpreadSheetTableModel());
    m_TableFlows.addCellPopupMenuListener(this);
    m_TableFlows.setShowRowColumn(false);
    m_TableFlows.setUseSimpleHeader(true);
    m_TableFlows.getSelectionModel().addListSelectionListener(this);
    m_PanelFlows.add(new BaseScrollPane(m_TableFlows), BorderLayout.CENTER);
  }

  /**
   * Finalizes the initialization.
   */
  @Override
  protected void finishInit() {
    super.finishInit();
    updateButtons();
  }

  /**
   * Returns the underlying table for the flows.
   *
   * @return		the table
   */
  public SpreadSheetTable getFlowsTable() {
    return m_TableFlows;
  }

  /**
   * Returns new instance of a configured scripting engine.
   *
   * @param responseHandler	the handler to use for intercepting the result, can be null
   * @return			the engine
   */
  protected DefaultScriptingEngine configureEngine(ResponseHandler responseHandler) {
    DefaultScriptingEngine 				result;
    adams.scripting.requesthandler.MultiHandler		multiRequest;
    adams.scripting.responsehandler.MultiHandler	multiResponse;
    SimpleLogPanelRequestHandler			simpleRequest;
    SimpleLogPanelResponseHandler 			simpleResponse;

    result = new DefaultScriptingEngine();
    result.setPort(PortManager.getSingleton().next(result.getClass(), DEFAULT_PORT));

    // request
    simpleRequest = new SimpleLogPanelRequestHandler();
    simpleRequest.setLog(getLogPanel().getRequestLog());
    multiRequest = new adams.scripting.requesthandler.MultiHandler();
    multiRequest.setHandlers(new RequestHandler[]{
      new adams.scripting.requesthandler.LoggingHandler(),
      simpleRequest,
    });
    result.setRequestHandler(multiRequest);

    // response
    simpleResponse = new SimpleLogPanelResponseHandler();
    simpleResponse.setLog(getLogPanel().getRequestLog());
    multiResponse = new adams.scripting.responsehandler.MultiHandler();
    multiResponse.setHandlers(new ResponseHandler[]{
      new adams.scripting.responsehandler.LoggingHandler(),
      simpleResponse,
    });
    if (responseHandler != null)
      multiResponse.addHandler(responseHandler);
    result.setResponseHandler(multiResponse);

    return result;
  }

  /**
   * Sends the specified command and the response handler for intercepting
   * the result.
   *
   * @param cmd			the command to send
   * @param responseHandler 	the response handler for intercepting the result
   */
  public void sendCommand(RemoteCommandWithResponse cmd, ResponseHandler responseHandler) {
    StopEngine			stop;
    DefaultConnection		conn;
    DefaultScriptingEngine	engine;
    DefaultConnection		connResp;
    BaseHostname		local;
    BaseHostname		remote;
    String			msg;

    local  = m_TextLocal.getObject();
    remote = m_TextRemote.getObject();

    // engine
    engine = configureEngine(responseHandler);
    new Thread(() -> engine.execute()).start();

    // command
    connResp = new DefaultConnection();
    connResp.setHost(local.hostnameValue());
    connResp.setPort(engine.getPort());
    cmd.setResponseConnection(connResp);

    // send command
    conn = new DefaultConnection();
    conn.setHost(remote.hostnameValue());
    conn.setPort(remote.portValue());
    msg  = conn.sendRequest(cmd);
    if (msg != null) {
      engine.stopExecution();
      getOwner().logError("Failed to refresh flow list:\n" + msg, "Scripting error");
    }
    else {
      // send stop signal
      stop = new StopEngine();
      stop.setType(EngineType.RESPONSE);
      stop.setResponseConnection(connResp);
      conn.sendRequest(stop);
    }
  }

  /**
   * Refreshes the list of flows.
   */
  protected void refreshFlows() {
    sendCommand(new ListFlows(), new FlowListResponseHandler(this));
  }

  /**
   * Returns an ID array of the the currently selected flows.
   *
   * @return		the flow IDs
   */
  protected int[] getSelectedFlowIDs() {
    int[]	result;
    int[]	sel;
    int		i;

    sel    = m_TableFlows.getSelectedRows();
    result = new int[sel.length];
    for (i = 0; i < sel.length; i++)
      result[i] = ((Number) m_TableFlows.getValueAt(i, 0)).intValue();

    return result;
  }

  /**
   * Gets called when a cell in the flow table is right-clicked.
   * <br>
   * Default implementation does nothing.
   *
   * @param e		the mouse event that triggered the display of popup menu
   */
  @Override
  public void showPopupMenu(MouseEvent e) {
  }

  /**
   * Gets called when the selection in the flow table changes.
   * <br>
   * Default implementation does nothing.
   *
   * @param e		the mouse event
   */
  @Override
  public void valueChanged(ListSelectionEvent e) {
    updateButtons();
  }

  /**
   * Updates the state of the buttons.
   * <br>
   * Default implementation does nothing.
   */
  protected void updateButtons() {
  }
}

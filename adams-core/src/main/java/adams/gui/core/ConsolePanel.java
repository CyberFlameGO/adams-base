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
 * ConsolePanel.java
 * Copyright (C) 2011-2019 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.core;

import adams.core.Properties;
import adams.core.io.PlaceholderFile;
import adams.core.logging.LoggingHelper;
import adams.core.logging.LoggingLevel;
import adams.core.logging.RotatingFileHandler;
import adams.gui.event.ConsolePanelEvent;
import adams.gui.event.ConsolePanelListener;
import adams.gui.sendto.SendToActionSupporter;
import adams.gui.sendto.SendToActionUtils;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

/**
 * Global panel for capturing output via PrintObject instances.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class ConsolePanel
  extends BasePanel
  implements MenuBarProvider, SendToActionSupporter {

  /** for serialization. */
  private static final long serialVersionUID = -2339480199106797838L;

  /**
   * Represents a single panel for a specific type of output.
   *
   * @author  fracpete (fracpete at waikato dot ac dot nz)
   */
  public static class OutputPanel
    extends BasePanel
    implements LogPanel {

    /** for serialization. */
    private static final long serialVersionUID = 8547336176163250862L;

    /** the title of the panel. */
    protected String m_Title;

    /** whether output is enabled. */
    protected boolean m_OutputEnabled;

    /** the text area for the output. */
    protected StyledTextEditorPanel m_TextArea;

    /** the button for enabling/disabling the output. */
    protected BaseButton m_ButtonEnabledDisable;

    /** the spinner for the maximum number of lines. */
    protected JSpinner m_SpinnerMaxLines;

    /** the button for clearing the output. */
    protected BaseButton m_ButtonClear;

    /** the level/color association. */
    protected HashMap<LoggingLevel,AttributeSet> m_LevelAttributeSets;

    /**
     * Initializes the panel.
     *
     * @param title	the title of the panel
     */
    public OutputPanel(String title) {
      super();

      m_Title         = title;
      m_OutputEnabled = true;
      m_LevelAttributeSets = new HashMap<>();
      for (LoggingLevel l: LoggingLevel.values()) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, levelToColor(l));
        StyleConstants.setFontFamily(set, "monospaced");
        m_LevelAttributeSets.put(l, set);
      }
    }

    /**
     * Initializes the widgets.
     */
    @Override
    protected void initGUI() {
      JPanel			panel;
      SpinnerNumberModel	model;
      Properties		props;

      super.initGUI();

      props = getProperties();

      setLayout(new BorderLayout());

      m_TextArea = new StyledTextEditorPanel();
      add(m_TextArea, BorderLayout.CENTER);

      panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
      panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
      add(panel, BorderLayout.SOUTH);

      m_ButtonEnabledDisable = new BaseButton("Disable");
      m_ButtonEnabledDisable.setMnemonic('a');
      m_ButtonEnabledDisable.addActionListener((ActionEvent e) -> {
        m_OutputEnabled = !m_OutputEnabled;
        if (m_OutputEnabled)
          m_ButtonEnabledDisable.setText("Disable");
        else
          m_ButtonEnabledDisable.setText("Enable");
      });
      panel.add(m_ButtonEnabledDisable);

      m_SpinnerMaxLines = new JSpinner();
      m_SpinnerMaxLines.addChangeListener((ChangeEvent e) -> trimOutput());
      model = (SpinnerNumberModel) m_SpinnerMaxLines.getModel();
      model.setMinimum(props.getInteger("LinesMinimum", 1));
      model.setMaximum(props.getInteger("LinesMaximum", 10000000));
      model.setStepSize(props.getInteger("LinesStep", 10000));
      model.setValue(props.getInteger("LinesDefault", 10000));
      panel.add(m_SpinnerMaxLines);

      m_ButtonClear = new BaseButton("Clear", GUIHelper.getIcon("new.gif"));
      m_ButtonClear.setMnemonic('l');
      m_ButtonClear.addActionListener((ActionEvent e) -> m_TextArea.clear());
      panel.add(m_ButtonClear);
    }

    /**
     * Returns the title of the panel.
     *
     * @return		the title of the panel
     */
    public String getTitle() {
      return m_Title;
    }

    /**
     * Sets whether the output is enabled.
     *
     * @param value	if true the output will get enabled
     */
    public void setOutputEnabled(boolean value) {
      m_OutputEnabled = value;
    }

    /**
     * Clears the text.
     */
    public void clear() {
      synchronized (m_TextArea) {
        m_TextArea.clear();
      }
    }

    /**
     * Copies the text to the clipboard.
     */
    public void copy() {
      m_TextArea.copy();
    }

    /**
     * Saves the current content to a file.
     */
    public void saveAs() {
      m_TextArea.saveAs();
    }

    /**
     * For finding a string.
     */
    public void find() {
      m_TextArea.find();
    }

    /**
     * Finds the next occurrence.
     */
    public void findNext() {
      m_TextArea.findNext();
    }

    /**
     * Sets the line wrap flag.
     *
     * @param value	if true line wrap is enabled
     */
    public void setLineWrap(boolean value) {
      m_TextArea.setWordWrap(value);
    }

    /**
     * Returns the current line wrap setting.
     *
     * @return		true if line wrap is enabled
     */
    public boolean getLineWrap() {
      return m_TextArea.getWordWrap();
    }

    /**
     * Sets the current text.
     *
     * @param value	the text
     */
    public void setText(String value) {
      m_TextArea.setText(value);
    }

    /**
     * Returns the current text.
     *
     * @return		the tex
     */
    public String getText() {
      return m_TextArea.getText();
    }

    /**
     * Trims the output of the text area if necessary.
     */
    protected void trimOutput() {
      StringBuilder	buf;
      int		index;

      synchronized(m_TextArea) {
        if (m_TextArea.getTextPane().getLineCount() > ((Number) m_SpinnerMaxLines.getValue()).intValue()) {
          buf   = new StringBuilder(m_TextArea.getContent());
          index = buf.indexOf("\n", (int) (buf.length() * 0.2));
          if (index == -1)
            buf = new StringBuilder();
          else
            buf.delete(0, index);
          m_TextArea.setContent(buf.toString());
        }
      }
    }

    /**
     * Appends the given string.
     *
     * @param level	the logging level
     * @param msg	the message to append
     */
    public void append(final LoggingLevel level, final String msg) {
      boolean 	caretIsLast;

      if (!m_OutputEnabled)
        return;

      synchronized(m_TextArea) {
        caretIsLast = (m_TextArea.getCaretPosition() == m_TextArea.getDocument().getLength());

        m_TextArea.getTextPane().append(msg, m_LevelAttributeSets.get(level));
        trimOutput();

        // only move cursor if at end of document
        if (caretIsLast)
          m_TextArea.setCaretPositionLast();
      }
    }

    /**
     * Returns the content of the text area.
     *
     * @return		the currently displayed text
     */
    public String getContent() {
      return m_TextArea.getContent();
    }
  }

  /**
   * The type of panel.
   *
   * @author  fracpete (fracpete at waikato dot ac dot nz)
   */
  public enum PanelType {
    /** contains all the output. */
    ALL,
    /** only error output. */
    ERRORS,
  }

  /**
   * For letting {@link PrintStream} objects print to the {@link ConsolePanel}.
   *
   * @author  fracpete (fracpete at waikato dot ac dot nz)
   */
  public static class ConsolePanelOutputStream
    extends OutputStream {

    /** the level to use. */
    protected LoggingLevel m_Level;

    /** the current buffer. */
    protected StringBuilder m_Buffer;

    /**
     * Initializes the output stream.
     *
     * @param level 	the logging level
     */
    public ConsolePanelOutputStream(LoggingLevel level) {
      super();

      m_Level  = level;
      m_Buffer = new StringBuilder();
    }

    /**
     * Writes the specified byte to this output stream. The general
     * contract for <code>write</code> is that one byte is written
     * to the output stream. The byte to be written is the eight
     * low-order bits of the argument <code>b</code>. The 24
     * high-order bits of <code>b</code> are ignored.
     * <p>
     * Subclasses of <code>OutputStream</code> must provide an
     * implementation for this method.
     *
     * @param      b   the <code>byte</code>.
     */
    @Override
    public void write(int b) throws IOException {
      char	c;

      c = (char) b;
      m_Buffer.append(c);

      if (c == '\n') {
        getSingleton().append(m_Level, m_Buffer.toString());
        m_Buffer.delete(0, m_Buffer.length());
      }
    }
  }

  /** the props file with the style definitions. */
  public final static String FILENAME = "adams/gui/core/ConsolePanel.props";

  /** the properties. */
  protected static Properties m_Properties;

  /** the singleton. */
  protected static ConsolePanel m_Singleton;

  /** the tabbed pane for the various outputs. */
  protected BaseTabbedPane m_TabbedPane;

  /** the ALL panel. */
  protected OutputPanel m_PanelAll;

  /** the error panel. */
  protected OutputPanel m_PanelError;

  /** the menu bar. */
  protected JMenuBar m_MenuBar;

  /** the save as item. */
  protected JMenuItem m_MenuItemSaveAs;

  /** the close item. */
  protected JMenuItem m_MenuItemExit;

  /** the copy item. */
  protected JMenuItem m_MenuItemCopy;

  /** the clear item. */
  protected JMenuItem m_MenuItemClear;

  /** the clear all item. */
  protected JMenuItem m_MenuItemClearAll;

  /** the find item. */
  protected JMenuItem m_MenuItemFind;

  /** the find next item. */
  protected JMenuItem m_MenuItemFindNext;

  /** the listeners. */
  protected HashSet<ConsolePanelListener> m_Listeners;

  /** the name of the log file. */
  protected PlaceholderFile m_Log;

  /**
   * Initializes the panel.
   */
  protected ConsolePanel() {
    super();
  }

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_Listeners = new HashSet<>();
    initializeLogging();
  }

  /**
   * Initializes the logging.
   * <br>
   * See {@link RotatingFileHandler#ADAMS_LOGFILE_PREFIX} for injecting
   * prefix to log file via environment variable.
   */
  protected void initializeLogging() {
    LoggingHelper.addToDefaultHandler(new RotatingFileHandler());
  }

  /**
   * Initializes the GUI.
   */
  @Override
  protected void initGUI() {
    super.initGUI();

    setLayout(new BorderLayout());

    m_TabbedPane = new BaseTabbedPane();
    add(m_TabbedPane, BorderLayout.CENTER);

    m_PanelAll = new OutputPanel("All");
    m_TabbedPane.addTab(m_PanelAll.getTitle(), m_PanelAll);

    m_PanelError = new OutputPanel("Error");
    m_TabbedPane.addTab(m_PanelError.getTitle(), m_PanelError);
  }

  /**
   * Creates a menu bar (singleton per panel object). Can be used in frames.
   *
   * @return		the menu bar
   */
  @Override
  public JMenuBar getMenuBar() {
    JMenuBar	result;
    JMenu	menu;
    JMenuItem	menuitem;

    if (m_MenuBar == null) {
      result = new JMenuBar();

      // File
      menu = new JMenu("File");
      result.add(menu);
      menu.setMnemonic('F');

      // File/Clear
      menuitem = new JMenuItem("Clear");
      menu.add(menuitem);
      menuitem.setMnemonic('l');
      menuitem.setAccelerator(GUIHelper.getKeyStroke("ctrl pressed N"));
      menuitem.setIcon(GUIHelper.getIcon("new.gif"));
      menuitem.addActionListener((ActionEvent e) -> {
        if (getCurrentPanel() != null)
          getCurrentPanel().clear();
      });
      m_MenuItemClear = menuitem;

      // File/Clear all
      menuitem = new JMenuItem("Clear all");
      menu.add(menuitem);
      menuitem.setMnemonic('a');
      menuitem.setAccelerator(GUIHelper.getKeyStroke("ctrl shift pressed N"));
      menuitem.setIcon(GUIHelper.getEmptyIcon());
      menuitem.addActionListener((ActionEvent e) -> {
        for (int i = 0; i < m_TabbedPane.getTabCount(); i++)
          ((OutputPanel) m_TabbedPane.getComponentAt(i)).clear();
      });
      m_MenuItemClearAll = menuitem;

      // File/Save as...
      menuitem = new JMenuItem("Save as...");
      menu.add(menuitem);
      menuitem.setMnemonic('S');
      menuitem.setAccelerator(GUIHelper.getKeyStroke("ctrl shift pressed S"));
      menuitem.setIcon(GUIHelper.getIcon("save.gif"));
      menuitem.addActionListener((ActionEvent e) -> {
        if (getCurrentPanel() != null)
          getCurrentPanel().saveAs();
      });
      m_MenuItemSaveAs = menuitem;

      // File/Send to
      menu.addSeparator();
      if (SendToActionUtils.addSendToSubmenu(this, menu))
        menu.addSeparator();

      // File/Exit
      menuitem = new JMenuItem("Close");
      menu.add(menuitem);
      menuitem.setMnemonic('C');
      menuitem.setAccelerator(GUIHelper.getKeyStroke("ctrl pressed Q"));
      menuitem.setIcon(GUIHelper.getIcon("exit.png"));
      menuitem.addActionListener((ActionEvent e) -> closeParent());
      m_MenuItemExit = menuitem;

      // Edit
      menu = new JMenu("Edit");
      result.add(menu);
      menu.setMnemonic('E');

      // Edit/Copy
      menuitem = new JMenuItem("Copy");
      menu.add(menuitem);
      menuitem.setMnemonic('C');
      menuitem.setAccelerator(GUIHelper.getKeyStroke("ctrl pressed C"));
      menuitem.setIcon(GUIHelper.getIcon("copy.gif"));
      menuitem.addActionListener((ActionEvent e) -> {
        if (getCurrentPanel() != null)
          getCurrentPanel().copy();
      });
      m_MenuItemCopy = menuitem;

      // Edit/Line wrap
      menuitem = new JMenuItem("Line wrap");
      menu.addSeparator();
      menu.add(menuitem);
      menuitem.setMnemonic('L');
      menuitem.setAccelerator(GUIHelper.getKeyStroke("ctrl pressed L"));
      menuitem.setIcon(GUIHelper.getEmptyIcon());
      menuitem.addActionListener((ActionEvent e) -> {
        if (getCurrentPanel() != null)
          getCurrentPanel().setLineWrap(!getCurrentPanel().getLineWrap());
      });
      m_MenuItemFind = menuitem;

      // Edit/Find
      menuitem = new JMenuItem("Find");
      menu.addSeparator();
      menu.add(menuitem);
      menuitem.setMnemonic('F');
      menuitem.setAccelerator(GUIHelper.getKeyStroke("ctrl pressed F"));
      menuitem.setIcon(GUIHelper.getIcon("find.gif"));
      menuitem.addActionListener((ActionEvent e) -> {
        if (getCurrentPanel() != null)
          getCurrentPanel().find();
      });
      m_MenuItemFind = menuitem;

      // Edit/Find
      menuitem = new JMenuItem("Find next");
      menu.add(menuitem);
      menuitem.setMnemonic('N');
      menuitem.setAccelerator(GUIHelper.getKeyStroke("ctrl shift pressed F"));
      menuitem.setIcon(GUIHelper.getEmptyIcon());
      menuitem.addActionListener((ActionEvent e) -> {
        if (getCurrentPanel() != null)
          getCurrentPanel().findNext();
      });
      m_MenuItemFindNext = menuitem;

      m_MenuBar = result;
    }

    return m_MenuBar;
  }

  /**
   * Returns the current output panel.
   *
   * @return		the panel, null if none available
   */
  public OutputPanel getCurrentPanel() {
    OutputPanel	result;

    result = null;
    if (m_TabbedPane.getSelectedComponent() instanceof OutputPanel)
      result = (OutputPanel) m_TabbedPane.getSelectedComponent();

    return result;
  }

  /**
   * Returns the specified output panel.
   *
   * @param type	the panel to retrieve
   * @return		the panel
   */
  public OutputPanel getPanel(PanelType type) {
    switch (type) {
      case ALL:
        return m_PanelAll;
      case ERRORS:
        return m_PanelError;
      default:
        throw new IllegalArgumentException("Unhandled panel type: " + type);
    }
  }

  /**
   * Adds the listener.
   *
   * @param l		the listener to add
   */
  public void addListener(ConsolePanelListener l) {
    synchronized(m_Listeners) {
      m_Listeners.add(l);
    }
  }

  /**
   * Removes the listener.
   *
   * @param l		the listener to remove
   */
  public void removeListener(ConsolePanelListener l) {
    synchronized(m_Listeners) {
      m_Listeners.remove(l);
    }
  }

  /**
   * Notifies the listeners.
   *
   * @param level	the logging level of output the string represents
   * @param msg		the message to append
   */
  protected void notifyListeners(LoggingLevel level, String msg) {
    ConsolePanelEvent	e;

    if (m_Listeners.size() == 0)
      return;

    e = new ConsolePanelEvent(this, level, msg);
    try {
      synchronized(m_Listeners) {
        for (ConsolePanelListener l: m_Listeners)
          l.consolePanelMessageReceived(e);
      }
    }
    catch (Throwable t) {
      // ignored
    }
  }

  /**
   * Convenience method for outputting exceptions.
   *
   * @param msg		the message to precede the stacktrace
   * @param t           the exception (stacktrace will be output)
   */
  public void append(String msg, Throwable t) {
    append(LoggingLevel.SEVERE, msg + "\n" + LoggingHelper.throwableToString(t));
  }

  /**
   * Convenience method for outputting exceptions.
   *
   * @param source	the source object
   * @param msg		the message to precede the stacktrace
   * @param t           the exception (stacktrace will be output)
   */
  public void append(Object source, String msg, Throwable t) {
    append(LoggingLevel.SEVERE, source.getClass().getName() + ": " + msg + "\n" + LoggingHelper.throwableToString(t));
  }

  /**
   * Convenience method for outputting exceptions.
   *
   * @param source	the source object
   * @param level	the logging level
   * @param msg		the message to precede the stacktrace
   */
  public void append(Object source, LoggingLevel level, String msg) {
    append(level, source.getClass().getName() + ": " + msg);
  }

  /**
   * Appends the given string to the according panels.
   *
   * @param level	the logging level
   * @param msg		the message to append
   */
  public void append(LoggingLevel level, String msg) {
    m_PanelAll.append(level, msg);
    if (LoggingHelper.isAtMost(level.getLevel(), Level.WARNING))
      m_PanelError.append(level, msg);

    notifyListeners(level, msg);
  }

  /**
   * Returns the classes that the supporter generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] getSendToClasses() {
    return new Class[]{String.class, JTextComponent.class};
  }

  /**
   * Checks whether something to send is available.
   *
   * @param cls		the requested classes
   * @return		true if an object is available for sending
   */
  @Override
  public boolean hasSendToItem(Class[] cls) {
    return    (SendToActionUtils.isAvailable(new Class[]{String.class, JTextComponent.class}, cls))
      && (getCurrentPanel().getContent().length() > 0);
  }

  /**
   * Returns the object to send.
   *
   * @param cls		the requested classes
   * @return		the item to send
   */
  @Override
  public Object getSendToItem(Class[] cls) {
    Object	result;

    result = null;

    if ((SendToActionUtils.isAvailable(String.class, cls))) {
      result = getCurrentPanel().getContent();
      if (((String) result).length() == 0)
        result = null;
    }
    else if (SendToActionUtils.isAvailable(JTextComponent.class, cls)) {
      if (getCurrentPanel().getContent().length() > 0)
        result = getCurrentPanel();
    }

    return result;
  }

  /**
   * Returns the syntax style definition.
   *
   * @return		the props file with the definitions
   */
  public static synchronized Properties getProperties() {
    if (m_Properties == null) {
      try {
        m_Properties = Properties.read(FILENAME);
      }
      catch (Exception e) {
        System.err.println("Failed to load properties '" + FILENAME + "': ");
        e.printStackTrace();
        m_Properties = new Properties();
      }
    }
    return m_Properties;
  }

  /**
   * Turns the logging level into a color.
   *
   * @param level	the level
   * @return		the associated color
   */
  public static Color levelToColor(LoggingLevel level) {
    return levelToColor(level.getLevel());
  }

  /**
   * Turns the logging level into a color.
   *
   * @param level	the level
   * @return		the associated color
   */
  public static Color levelToColor(Level level) {
    if (level == Level.SEVERE)
      return Color.RED.darker();
    else if (level == Level.WARNING)
      return Color.RED.brighter();
    else if (level == Level.CONFIG)
      return Color.DARK_GRAY;
    else if (level == Level.FINE)
      return Color.MAGENTA.darker().darker();
    else if (level == Level.FINER)
      return Color.MAGENTA.darker();
    else if (level == Level.FINEST)
      return Color.MAGENTA;
    else
      return Color.BLACK;
  }

  /**
   * Returns the singleton instance.
   *
   * @return		the singleton
   */
  public static synchronized ConsolePanel getSingleton() {
    if (m_Singleton == null)
      m_Singleton = new ConsolePanel();

    return m_Singleton;
  }
}

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
 * TesseractOCR.java
 * Copyright (C) 2011-2017 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.visualization.image.plugins;

import adams.core.Utils;
import adams.core.io.FileUtils;
import adams.core.io.PlaceholderFile;
import adams.core.io.TempUtils;
import adams.core.logging.LoggingHelper;
import adams.core.management.ProcessUtils;
import adams.core.option.OptionUtils;
import adams.data.tesseract.TesseractHelper;
import adams.gui.core.GUIHelper;
import adams.gui.dialog.TextDialog;
import adams.gui.visualization.image.ImagePanel;
import com.github.fracpete.processoutput4j.output.CollectingProcessOutput;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.List;


/**
 * Allows the user to apply a JAI transformer to an image in the ImageViewer.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class TesseractOCR
  extends AbstractImageViewerPlugin {

  /** for serialization. */
  private static final long serialVersionUID = -3146372359577147914L;

  /** the last command that was executed. */
  protected String m_LastCommand;

  /**
   * Returns the text for the menu to place the plugin beneath.
   *
   * @return		the menu
   */
  @Override
  public String getMenu() {
    return "Process";
  }

  /**
   * Returns the text for the menu item to create.
   *
   * @return		the text
   */
  @Override
  public String getCaption() {
    return "Tesseract OCR";
  }

  /**
   * Returns the icon name.
   *
   * @return		the name, null if none available
   */
  @Override
  public String getIconName() {
    return "tesseract.png";
  }

  /**
   * Checks whether the plugin can be executed given the specified image panel.
   *
   * @param panel	the panel to use as basis for decision
   * @return		true if plugin can be executed
   */
  @Override
  public boolean canExecute(ImagePanel panel) {
    return (panel != null) && (panel.getCurrentImage() != null);
  }

  /**
   * Creates the log message.
   * 
   * @return		the message, null if none available
   */
  @Override
  protected String createLogEntry() {
    return getClass().getSimpleName() + ": " + m_LastCommand;
  }

  /**
   * Executes the plugin.
   *
   * @return		null if OK, otherwise error message
   */
  @Override
  protected String doExecute() {
    String		result;
    String[]		cmd;
    File		tmp;
    File		tmp2;
    CollectingProcessOutput proc;
    List<String>	lines;
    TextDialog		dialog;

    result = null;

    m_LastCommand = "";
    tmp = null;
    try {
      tmp = TempUtils.createTempFile("ocr", ".png");
      if (!ImageIO.write(m_CurrentPanel.getCurrentImage(), "PNG", tmp))
	result = "Failed to save current image as temp file: " + tmp;
    }
    catch (Exception e) {
      result = LoggingHelper.handleException(this, "Failed to save current image as temp file!", e);
    }
    
    // create tesseract command
    cmd = null;
    if (result == null) {
      cmd = TesseractHelper.getSingleton().getCommand(
	  tmp.getAbsolutePath(), 
	  FileUtils.replaceExtension(tmp.getAbsolutePath(), ""), 
	  new PlaceholderFile("."));
    }
    
    // apply tesseract
    if (cmd != null) {
      m_LastCommand = OptionUtils.joinOptions(cmd);
      try {
	proc = ProcessUtils.execute(cmd);
	if (proc.getExitCode() != 0)
	  result = "OCR failed with exit code: " + proc.getExitCode() + "\n" + proc.getStdErr();
      }
      catch (Exception e) {
	result = LoggingHelper.handleException(this, "Failed to apply OCR: " + m_LastCommand, e);
      }
    }
    
    // gather text
    lines = null;
    tmp2  = null;
    if (result == null) {
      tmp2  = FileUtils.replaceExtension(tmp, ".txt");
      lines = FileUtils.loadFromFile(tmp2);
      if (lines == null)
	result = "Failed to load OCR output: " + tmp2;
    }
    
    // display text
    if (lines != null) {
      if (m_CurrentPanel.getParentDialog() != null)
	dialog = new TextDialog(m_CurrentPanel.getParentDialog());
      else
	dialog = new TextDialog(m_CurrentPanel.getParentFrame());
      dialog.setDialogTitle("OCR Result");
      dialog.setContent(Utils.flatten(lines, "\n"));
      dialog.setEditable(false);
      dialog.setSize(
        GUIHelper.getInteger("DefaultSmallDialog.Width", 600),
        GUIHelper.getInteger("DefaultSmallDialog.Width", 600));
      dialog.setLocationRelativeTo(m_CurrentPanel);
      dialog.setVisible(true);
    }

    if (tmp != null)
      FileUtils.delete(tmp);
    if (tmp2 != null)
      FileUtils.delete(tmp2);
    
    return result;
  }
}

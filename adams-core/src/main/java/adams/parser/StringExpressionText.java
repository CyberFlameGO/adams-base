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
 * StringExpressionText.java
 * Copyright (C) 2017 University of Waikato, Hamilton, New Zealand
 */

package adams.parser;

import adams.gui.core.AbstractSimpleScript;
import adams.gui.core.AbstractTextEditorPanelWithSimpleSyntaxHighlighting;
import adams.gui.core.StringExpressionEditorPanel;

/**
 * Wrapper for a String object to be editable in the GOE. Basically the same
 * as BaseString, but used for longer, multi-line strings. Uses a different
 * GOE editor.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class StringExpressionText
  extends AbstractSimpleScript {

  /** for serialization. */
  private static final long serialVersionUID = -7223597009565454854L;

  /**
   * Initializes the string with length 0.
   */
  public StringExpressionText() {
    this("");
  }

  /**
   * Initializes the object with the string to parse.
   *
   * @param s		the string to parse
   */
  public StringExpressionText(String s) {
    super(s);
  }

  /**
   * Returns the tip text for the script.
   *
   * @return		the tool tip
   */
  protected String getScriptTipText() {
    return "String expression";
  }

  /**
   * Returns the configured text editor panel to use in the GOE.
   *
   * @return		the text editor panel
   */
  public AbstractTextEditorPanelWithSimpleSyntaxHighlighting getTextEditorPanel() {
    return new StringExpressionEditorPanel();
  }
  
  /**
   * Returns whether inline editing in the GOE is allowed.
   * 
   * @return		true if inline editing is allowed
   */
  public boolean allowsInlineEditing() {
    return true;
  }
}

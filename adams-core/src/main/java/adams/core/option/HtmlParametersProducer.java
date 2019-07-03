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
 * HtmlParametersProducer.java
 * Copyright (C) 2011-2019 University of Waikato, Hamilton, New Zealand
 */
package adams.core.option;

import adams.core.Utils;
import adams.core.base.AbstractBaseString;
import adams.core.io.FileFormatHandler;
import adams.core.net.HtmlUtils;
import adams.gui.core.AbstractAdvancedScript;
import adams.gui.core.AbstractSimpleScript;
import nz.ac.waikato.cms.locator.ClassLocator;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * Generates HTML output of the parameters of an object (non-recursive).
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class HtmlParametersProducer
  extends AbstractOptionProducer<String,StringBuilder>
  implements FileFormatHandler {

  /** for serialization. */
  private static final long serialVersionUID = 7520567844837662391L;

  /** the buffer for assembling the help. */
  protected StringBuilder m_OutputBuffer;

  /** whether to generate "fake" class cross reference links. */
  protected boolean m_ClassCrossRefLinks;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Generates HTML 3 help output, which is used in the GUI.";
  }

  /**
   * Sets whether to generate class cross-reference links for the help user interface.
   *
   * @param value	true if to generate
   */
  public void setClassCrossRefLinks(boolean value) {
    m_ClassCrossRefLinks = value;
    reset();
  }

  /**
   * Returns whether to generate class cross-reference links for the help user interface.
   *
   * @return		true if to generate
   */
  public boolean getClassCrossRefLinks() {
    return m_ClassCrossRefLinks;
  }

  /**
   * Initializes the output data structure.
   *
   * @return		the created data structure
   */
  @Override
  protected String initOutput() {
    return "";
  }

  /**
   * Initializes the visitor.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_OutputBuffer = new StringBuilder();
  }

  /**
   * Returns the output generated from the visit.
   *
   * @return		the output
   */
  @Override
  public String getOutput() {
    if (m_Output == null)
      m_Output = m_OutputBuffer.toString();

    return m_Output;
  }

  /**
   * Turns the string into HTML. Line feeds are automatically converted
   * into &lt;br&gt;.
   *
   * @param s		the string to convert to HTML
   * @return		the HTML string
   * @see		HtmlUtils#markUpURLs(String, boolean)
   */
  protected String toHTML(String s) {
    return toHTML(s, false);
  }

  /**
   * Turns the string into HTML. Line feeds are automatically converted
   * into &lt;br&gt;.
   *
   * @param s		the string to convert to HTML
   * @param nbsp	whether to convert leading blanks to non-breaking spaces
   * @return		the HTML string
   * @see		HtmlUtils#markUpURLs(String, boolean)
   */
  protected String toHTML(String s, boolean nbsp) {
    String	result;
    
    result = HtmlUtils.markUpURLs(s, true);
    result = HtmlUtils.convertLines(result, nbsp);
    result = HtmlUtils.hyperlinkClassnames(result, m_ClassCrossRefLinks);

    return result;
  }

  /**
   * Breaks up the tool tip and adds it to the StringBuilder.
   *
   * @param option	the current option to obtain the data from
   * @param buffer	the buffer to add the tool tip to
   */
  protected void addToolTip(AbstractOption option, StringBuilder buffer) {
    String	text;

    // obtain and add text
    if (option.getToolTipMethod() != null) {
      try {
	text = (String) option.getToolTipMethod().invoke(option.getOptionHandler(), new Object[]{});
	buffer.append("<p>");
	buffer.append(toHTML(text));
	buffer.append("</p>\n");
      }
      catch (Exception e) {
	// this should never happen!
	e.printStackTrace();
      }
    }
  }

  /**
   * Adds additional information about the argument, e.g., the class.
   *
   * @param option	the current option to obtain the data from
   * @param buffer	the buffer to add the information to
   */
  protected void addArgumentInfo(AbstractArgumentOption option, StringBuilder buffer) {
    String	text;
    Method	method;
    Object[]	vals;

    if (option instanceof EnumOption) {
      text = "";
      try {
	method = option.getBaseClass().getMethod("values", new Class[0]);
	vals   = (Object[]) method.invoke(null, new Object[0]);
	text   = Utils.arrayToString(vals).replaceAll(",", "|");
      }
      catch (Exception e) {
	e.printStackTrace();
	text = "Error retrieving enum values";
      }
    }
    else {
      text = option.getBaseClass().getName();
    }

    buffer.append(" &lt;" + toHTML(text) + "&gt;");
  }

  /**
   * Visits a boolean option.
   *
   * @param option	the boolean option
   * @return		the last internal data structure that was generated
   */
  @Override
  public StringBuilder processOption(BooleanOption option) {
    StringBuilder	result;
    Boolean		value;

    result = new StringBuilder();

    value = (Boolean) option.getCurrentValue();
    if (value.booleanValue() != ((Boolean) option.getDefaultValue()).booleanValue()) {
      result.append("<li>\n");
      result.append("<b>" + toHTML(option.getProperty()) + "</b>\n");
      result.append("<br>\n");

      if (value)
	result.append("enabled");
      else
	result.append("disabled");

      result.append("<br>\n");
      result.append("</li>\n");

      m_OutputBuffer.append(result);
    }

    return result;
  }

  /**
   * Visits a class option.
   *
   * @param option	the class option
   * @return		the last internal data structure that was generated
   */
  @Override
  public StringBuilder processOption(ClassOption option) {
    return processOption((AbstractArgumentOption) option);
  }

  /**
   * Visits an argument option.
   *
   * @param option	the argument option
   * @return		the last internal data structure that was generated
   */
  @Override
  public StringBuilder processOption(AbstractArgumentOption option) {
    StringBuilder		result;
    String			text;
    int				n;
    Object			value;
    boolean			script;
    AbstractBaseString		bstring;

    result = new StringBuilder();

    value = option.getCurrentValue();
    if (!option.isDefaultValue(value)) {
      result.append("<li>\n");
      result.append("<b>" + toHTML(option.getProperty()) + "</b>\n");
      result.append("<br>\n");

      script = (ClassLocator.isSubclass(AbstractSimpleScript.class, option.getBaseClass()))
	|| (ClassLocator.isSubclass(AbstractAdvancedScript.class, option.getBaseClass()));

      if (script) {
	if (value == null) {
	  text = "null";
	}
	else if (option.isMultiple()) {
	  text = "<ol>";
	  for (n = 0; n < Array.getLength(value); n++) {
	    bstring = (AbstractBaseString) Array.get(value, n);
	    text += "<li>" + toHTML(Utils.backQuoteChars(bstring.getValue()), false) + "</li>\n";
          }
          text += "</ol>";
        }
	else {
	  bstring = (AbstractBaseString) value;
	  text = toHTML(bstring.getValue());
	}
	result.append(text + "\n");
      }
      else {
	if (value == null) {
	  text = "null";
	}
	else if (option.isMultiple()) {
	  text = "<ol>";
	  for (n = 0; n < Array.getLength(value); n++) {
	    text += "<li>" + toHTML(Utils.backQuoteChars(option.toString(Array.get(value, n))), false) + "</li>\n";
	  }
          text += "</ol>";
	}
	else {
	  text = toHTML(option.toString(value), false);
	}
	result.append(text + "\n");
      }
      result.append("<br>\n");
      result.append("</li>\n");

      m_OutputBuffer.append(result);
    }

    return result;
  }

  /**
   * Hook method that gets called just before an option gets produced.
   * <br><br>
   * Default implementation does nothing
   *
   * @param manager	the option manager
   * @param index	the index of the option
   */
  @Override
  protected void preProduce(OptionManager manager, int index) {
    super.preProduce(manager, index);

    m_Output = null;
  }

  /**
   * Hook-method before starting visiting options. Adds header and global
   * info to the output buffer.
   */
  @Override
  protected void preProduce() {
    m_OutputBuffer = new StringBuilder();
    m_OutputBuffer.append("<html>" + "\n");
    m_OutputBuffer.append("<head>\n");
    m_OutputBuffer.append("<title>" + getInput().getClass().getName() + "<title>\n");
    m_OutputBuffer.append("</head>\n");
    m_OutputBuffer.append("\n");
    m_OutputBuffer.append("<body>\n");
    m_OutputBuffer.append("<h3>Name</h3>\n");
    m_OutputBuffer.append("<p><code>" + getInput().getClass().getName() + "</code></p>\n");
    m_OutputBuffer.append("<br>\n");
    m_OutputBuffer.append("\n");

    m_OutputBuffer.append("<h3>Parameters</h3>\n");
    m_OutputBuffer.append("<ul>\n");
  }

  /**
   * Hook-method after visiting options.
   * <br><br>
   * Default implementation does nothing.
   */
  @Override
  protected void postProduce() {
    m_OutputBuffer.append("</ul>\n");
    m_OutputBuffer.append("</body>\n");
    m_OutputBuffer.append("</html>\n");
  }

  /**
   * Returns the output generated from the visit.
   *
   * @return		the output, null in case of an error
   */
  @Override
  public String toString() {
    return getOutput();
  }

  /**
   * Returns the description of the file format.
   *
   * @return		the description
   */
  public String getFormatDescription() {
    return "HTML Parameters";
  }

  /**
   * Returns the default file extension (without the dot).
   *
   * @return		the default extension
   */
  public String getDefaultFormatExtension() {
    return "html";
  }

  /**
   * Returns the file extensions (without the dot).
   *
   * @return		the extensions
   */
  public String[] getFormatExtensions() {
    return new String[]{"html", "htm"};
  }
  
  /**
   * Executes the producer from commandline.
   * 
   * @param args	the commandline arguments, use -help for help
   */
  public static void main(String[] args) {
    runProducer(HtmlParametersProducer.class, args);
  }
}

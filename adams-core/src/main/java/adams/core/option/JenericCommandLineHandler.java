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
 * JenericCommandLineHandler.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, New Zealand
 */
package adams.core.option;

import adams.core.classmanager.ClassManager;
import nz.ac.waikato.cms.jenericcmdline.AbstractProcessor;
import nz.ac.waikato.cms.jenericcmdline.DefaultProcessor;
import nz.ac.waikato.cms.jenericcmdline.core.OptionUtils;
import nz.ac.waikato.cms.jenericcmdline.traversal.SpecificClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * For classes that get enhanced with a generic commandline using the
 * jeneric-cmdline library.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class JenericCommandLineHandler
  extends AbstractCommandLineHandler {

  /** for serialization. */
  private static final long serialVersionUID = -5233496867185402778L;

  /** the processor to use. */
  protected AbstractProcessor m_Processor;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    SpecificClasses	specific;
    List<Class>		managed;

    super.initialize();

    m_Processor = new DefaultProcessor();
    specific    = new SpecificClasses();
    managed     = JenericCmdline.getSingleton().getManaged();
    specific.setAllowed(managed.toArray(new Class[managed.size()]));
    m_Processor.setTraverser(specific);
    ((DefaultProcessor) m_Processor).setSkipDeprecated(true);
  }

  /**
   * Generates an object from the specified commandline.
   *
   * @param cmd		the commandline to create the object from
   * @return		the created object, null in case of error
   */
  @Override
  public Object fromCommandLine(String cmd) {
    return fromArray(splitOptions(cmd));
  }

  /**
   * Generates an object from the commandline options.
   *
   * @param args	the commandline options to create the object from
   * @return		the created object, null in case of error
   */
  @Override
  public Object fromArray(String[] args) {
    Object	result;

    result = null;

    if (args.length > 0) {
      try {
	result  = ClassManager.getSingleton().forName(Conversion.getSingleton().rename(args[0])).getDeclaredConstructor().newInstance();
	args[0] = "";
	setOptions(result, args);
      }
      catch (Exception e) {
        getLogger().log(Level.SEVERE, "Failed to instantiate object from array (fromArray):", e);
      }
    }

    return result;
  }

  /**
   * Generates a commandline from the specified object.
   *
   * @param obj		the object to create the commandline for
   * @return		the generated commandline
   */
  @Override
  public String toCommandLine(Object obj) {
    List<String>	parts;
    String[]		options;

    options = getOptions(obj);
    if (options.length == 0)
      return obj.getClass().getName();

    parts = new ArrayList<>();
    parts.add(obj.getClass().getName());
    parts.addAll(Arrays.asList(options));

    return joinOptions(parts.toArray(new String[parts.size()]));
  }

  /**
   * Generates a commandline from the specified object. Uses a shortened
   * format, e.g., removing the package from the class.
   *
   * @param obj		the object to create the commandline for
   * @return		the generated commandline
   */
  @Override
  public String toShortCommandLine(Object obj) {
    List<String>	parts;
    String[]		options;

    options = getOptions(obj);
    if (options.length == 0)
      return obj.getClass().getSimpleName();

    parts = new ArrayList<>();
    parts.add(obj.getClass().getSimpleName());
    parts.addAll(Arrays.asList(options));

    return joinOptions(parts.toArray(new String[parts.size()]));
  }

  /**
   * Generates an options array from the specified object.
   *
   * @param obj		the object to create the array for
   * @return		the generated array
   */
  @Override
  public String[] toArray(Object obj) {
    return splitOptions(toCommandLine(obj));
  }

  /**
   * Returns the commandline options (without classname) of the specified object.
   *
   * @param obj		the object to get the options from
   * @return		always array with length 0
   */
  @Override
  public String[] getOptions(Object obj) {
    try {
      return m_Processor.getOptions(obj);
    }
    catch (Exception e) {
      getLogger().log(Level.SEVERE, "Failed to get options from " + obj.getClass().getName() + " (getOptions)!", e);
      return new String[0];
    }
  }

  /**
   * Sets the options of the specified object.
   *
   * @param obj		the object to set the options for
   * @param args	the options
   * @return		always true, does nothing
   */
  @Override
  public boolean setOptions(Object obj, String[] args) {
    List<String>	newArgs;

    // always expects pairs: flag/value
    // can't handle leading empty strings in array
    newArgs = new ArrayList<>(Arrays.asList(args));
    while ((newArgs.size() > 0) && newArgs.get(0).isEmpty())
      newArgs.remove(0);
    args = newArgs.toArray(new String[newArgs.size()]);

    try {
      m_Processor.setOptions(obj, args);
      return true;
    }
    catch (Exception e) {
      getLogger().log(Level.SEVERE, "Failed to set options for " + obj.getClass().getName() + " (setOptions)!", e);
      return false;
    }
  }

  /**
   * Splits the commandline into an array.
   *
   * @param cmdline	the commandline to split
   * @return		the generated array of options
   */
  @Override
  public String[] splitOptions(String cmdline) {
    String[]	result;

    try {
      result = OptionUtils.splitOptions(cmdline);
    }
    catch (Exception e) {
      getLogger().log(Level.SEVERE, "Failed to split options (splitOptions): " + cmdline, e);
      result = new String[0];
    }

    return result;
  }

  /**
   * Turns the option array back into a commandline.
   *
   * @param args	the options to turn into a commandline
   * @return		the generated commandline
   */
  @Override
  public String joinOptions(String[] args) {
    return OptionUtils.joinOptions(args);
  }

  /**
   * Checks whether the given class can be processed.
   *
   * @param cls		the class to inspect
   * @return		true if the handler can process the class
   */
  @Override
  public boolean handles(Class cls) {
    return JenericCmdline.getSingleton().isHandled(cls);
  }
}

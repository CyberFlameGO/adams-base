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
 * BaseObjectCommandLineHandler.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, New Zealand
 */
package adams.core.option;

import adams.core.base.BaseObject;
import adams.core.classmanager.ClassManager;
import nz.ac.waikato.cms.jenericcmdline.core.OptionUtils;
import nz.ac.waikato.cms.locator.ClassLocator;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * For BaseObject derived classes (mainly for favorites management).
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class BaseObjectCommandLineHandler
  extends AbstractCommandLineHandler {

  /** for serialization. */
  private static final long serialVersionUID = -5233496867185402778L;

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

    if (args.length == 2) {
      try {
	result  = ClassManager.getSingleton().forName(Conversion.getSingleton().rename(args[0])).getDeclaredConstructor().newInstance();
	setOptions(result, new String[]{args[1]});
      }
      catch (Exception e) {
        getLogger().log(Level.SEVERE, "Failed to instantiate object from array (fromArray):", e);
      }
    }
    else if (args.length == 1) {
      try {
	result = ClassManager.getSingleton().forName(Conversion.getSingleton().rename(args[0])).getDeclaredConstructor().newInstance();
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
    parts   = new ArrayList<>();
    parts.add(obj.getClass().getName());
    parts.add(options[0]);

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
    parts   = new ArrayList<>();
    parts.add(obj.getClass().getSimpleName());
    parts.add(options[0]);

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
   * @return		the base object value
   */
  @Override
  public String[] getOptions(Object obj) {
    return new String[]{((BaseObject) obj).getValue()};
  }

  /**
   * Sets the options of the specified object.
   *
   * @param obj		the object to set the options for
   * @param args	the options
   * @return		true if successfully set
   */
  @Override
  public boolean setOptions(Object obj, String[] args) {
    boolean	result;
    int		i;
    String	value;

    result = false;

    if (args.length > 0) {
      value = "";
      for (i = 0; i < args.length; i++) {
	if (!args[i].isEmpty()) {
	  value = args[i];
	  break;
	}
      }
      if (((BaseObject) obj).isValid(value)) {
	((BaseObject) obj).setValue(value);
	result = true;
      }
    }

    return result;
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
    return ClassLocator.isSubclass(BaseObject.class, cls);
  }
}

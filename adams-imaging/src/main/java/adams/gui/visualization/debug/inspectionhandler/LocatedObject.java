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
 * LocatedObject.java
 * Copyright (C) 2018 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.visualization.debug.inspectionhandler;

import nz.ac.waikato.cms.locator.ClassLocator;

import java.util.Hashtable;

/**
 * Provides further insight into LocatedObject instances.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class LocatedObject
  extends AbstractInspectionHandler {

  /**
   * Checks whether the handler can handle the specified class.
   *
   * @param cls		the class to check
   * @return		true if the handler can handle this type of object
   */
  @Override
  public boolean handles(Class cls) {
    return ClassLocator.isSubclass(adams.flow.transformer.locateobjects.LocatedObject.class, cls);
  }

  /**
   * Returns further inspection values.
   *
   * @param obj		the object to further inspect
   * @return		the named inspected values
   */
  @Override
  public Hashtable<String,Object> inspect(Object obj) {
    Hashtable<String,Object>				result;
    adams.flow.transformer.locateobjects.LocatedObject	located;

    result = new Hashtable<>();

    located = (adams.flow.transformer.locateobjects.LocatedObject) obj;
    result.put("Index", located.getIndex());
    result.put("X", located.getX());
    result.put("Y", located.getY());
    result.put("Width", located.getWidth());
    result.put("Height", located.getHeight());
    result.put("Meta", located.getMetaData());

    return result;
  }
}

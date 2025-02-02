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
 * PassThrough.java
 * Copyright (C) 2021 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.multimapoperation;

import adams.core.MessageCollection;

import java.util.Map;

/**
 * Dummy, just passes through the data.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class PassThrough
  extends AbstractMultiMapOperation<Map[]> {

  private static final long serialVersionUID = 5831884654010979232L;

  /**
   * Returns a string describing the object.
   *
   * @return a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Dummy, just passes through the data.";
  }

  /**
   * Returns the minimum number of maps that are required for the operation.
   *
   * @return the number of maps that are required, <= 0 means no lower limit
   */
  @Override
  public int minNumMapsRequired() {
    return -1;
  }

  /**
   * Returns the maximum number of maps that are required for the operation.
   *
   * @return the number of maps that are required, <= 0 means no upper limit
   */
  @Override
  public int maxNumMapsRequired() {
    return -1;
  }

  /**
   * The type of data that is generated.
   *
   * @return the class
   */
  @Override
  public Class generates() {
    return Map[].class;
  }

  /**
   * Performs the actual processing of the maps.
   *
   * @param maps 	the containers to process
   * @param errors	for collecting errors
   * @return 		the generated data
   */
  @Override
  protected Map[] doProcess(Map[] maps, MessageCollection errors) {
    return maps;
  }
}

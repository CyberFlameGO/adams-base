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
 * SavitzkyGolay2NumPoints.java
 * Copyright (C) 2015 University of Waikato, Hamilton, NZ
 */

package adams.core.discovery;

import adams.core.discovery.PropertyPath.PropertyContainer;
import weka.filters.unsupervised.attribute.SavitzkyGolay2;

/**
 * SavitzkyGolay numPoints handler.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class SavitzkyGolay2NumPoints
  extends AbstractGeneticIntegerDiscoveryHandler {

  private static final long serialVersionUID = 9168998412950337023L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Handles the numPoints parameter of the SavitzkyGolay2 filter.";
  }

  /**
   * Returns the default minimum.
   *
   * @return		the default
   */
  @Override
  protected int getDefaultMinimum() {
    return 1;
  }

  /**
   * Returns the default maximum.
   *
   * @return		the default
   */
  @Override
  protected int getDefaultMaximum() {
    return 7;
  }

  /**
   * Returns the packed bits for the genetic algorithm.
   *
   * @param cont	the container to obtain the value from to turn into a string
   * @return		the bits
   */
  @Override
  protected String doPack(PropertyContainer cont) {
    SavitzkyGolay2 sg2 = (SavitzkyGolay2) cont.getObject();
    int val = sg2.getNumPoints();
    return intToBits(val);
  }

  /**
   * Unpacks and applies the bits from the genetic algorithm.
   *
   * @param cont	the container to set the value for created from the string
   * @param bits	the bits to use
   */
  @Override
  protected void doUnpack(PropertyContainer cont, String bits) {
    int val = bitsToInt(bits);
    SavitzkyGolay2 sg2 = (SavitzkyGolay2) cont.getObject();
    sg2.setNumPoints(val);
  }

  /**
   * Checks whether this object is handled by this discovery handler.
   *
   * @param obj		the object to check
   * @return		true if handled
   */
  @Override
  protected boolean handles(Object obj) {
    return (obj instanceof SavitzkyGolay2);
  }
}

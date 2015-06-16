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
 * ListCallableActorUsage.java
 * Copyright (C) 2015 University of Waikato, Hamilton, NZ
 */

package adams.flow.processor;

import adams.core.option.OptionHandler;
import adams.flow.core.Actor;
import adams.flow.core.CallableActorReference;

/**
 <!-- globalinfo-start -->
 * Lists all the actors where the specified callable actor name is used.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 * 
 * <pre>-name &lt;java.lang.String&gt; (property: name)
 * &nbsp;&nbsp;&nbsp;The callable actor name to look for.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class ListCallableActorUsage
  extends AbstractListNameUsage<CallableActorReference> {

  private static final long serialVersionUID = -6340700367008421185L;

  /**
   * Returns a string describing the object.
   *
   * @return 		a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Lists all the actors where the specified callable actor name is used.";
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  @Override
  public String nameTipText() {
    return "The callable actor name to look for.";
  }

  /**
   * Returns the title for the dialog.
   *
   * @return		the title
   */
  public String getTitle() {
    return "Callable actor '" + m_Name + "'";
  }

  /**
   * Checks whether the located object matches the name that we are looking for.
   *
   * @param obj		the object to check
   * @return		true if a match
   */
  @Override
  protected boolean isNameMatch(Object obj) {
    return (obj instanceof CallableActorReference)
      && ((CallableActorReference) obj).getValue().equals(m_Name);
  }

  /**
   * Creates a location string used in the list.
   *
   * @param owner	the option handler
   * @param obj		the object where the name was located
   * @return		the generated location string
   */
  @Override
  protected String createLocation(OptionHandler owner, Object obj) {
    if (owner instanceof Actor)
      return ((Actor) owner).getFullName();
    else
      return owner.getClass().getName();
  }

  @Override
  protected String getHeader() {
    return "Locations referencing callable actor '" + m_Name + "':";
  }
}

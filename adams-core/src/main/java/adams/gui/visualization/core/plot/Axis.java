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
 * Axis.java
 * Copyright (C) 2008-2019 University of Waikato, Hamilton, New Zealand
 */

package adams.gui.visualization.core.plot;

/**
 * An enumeration of the axes.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public enum Axis {
  /** left of the content panel. */
  LEFT("Left", "L"),
  /** right of the content panel. */
  RIGHT("Right", "R"),
  /** over of the content panel. */
  TOP("Top", "T"),
  /** below of the content panel. */
  BOTTOM("Bottom", "B");

  /** the raw display. */
  private String m_Raw;

  /** the display string of the axis. */
  private String m_Display;

  /** the short display string of the axis. */
  private String m_DisplayShort;

  /**
   * Initializes the type.
   *
   * @param display		the string used for displaying
   * @param displayShort	the short display string
   */
  private Axis(String display, String displayShort) {
    m_Raw          = super.toString();
    m_Display      = display;
    m_DisplayShort = displayShort;
  }

  /**
   * Returns the raw string, used in super.toString().
   *
   * @return		the raw string
   */
  public String getRaw() {
    return m_Raw;
  }

  /**
   * Returns the display string, used in toString().
   *
   * @return		the display string
   * @see		#toString()
   */
  public String getDisplay() {
    return m_Display;
  }

  /**
   * Returns the short display string.
   *
   * @return		the display string
   */
  public String getDisplayShort() {
    return m_DisplayShort;
  }

  /**
   * Returns the display string.
   *
   * @return		the display string
   */
  public String toString() {
    return m_Display;
  }

  /**
   * Returns an enum generated from the string.
   *
   * @param str		the string to convert to an enum
   * @return		the generated enum or null in case of error
   */
  public static Axis parse(String str) {
    Axis	result;

    result = null;

    // default parsing
    try {
      result = valueOf(str);
    }
    catch (Exception e) {
      // ignored
    }

    // try display
    if (result == null) {
      for (Axis axis : values()) {
	if (axis.getDisplay().equals(str)) {
	  result = axis;
	  break;
	}
	if (axis.getDisplayShort().equals(str)) {
	  result = axis;
	  break;
	}
      }
    }

    return result;
  }
}
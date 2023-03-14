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
 * SelectionUtils.java
 * Copyright (C) 2018 University of Waikato, Hamilton, NZ
 */

package adams.gui.visualization.image;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Helper class for selections.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class RectangleUtils {

  /**
   * Defines the corners of a rectangle.
   */
  public enum RectangleCorner {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
  }

  /**
   * Computes the specified corner of the rectangle using two arbitrary
   * corner points of the rectangle.
   *
   * @param corner1   	the first corner
   * @param corner2	the second corner
   * @param corner	the type of corner to compute
   * @return		the computed corner
   */
  public static Point rectangleCorner(Point corner1, Point corner2, RectangleCorner corner) {
    int	x0;
    int	y0;
    int	x1;
    int	y1;
    int	xTmp;
    int	yTmp;

    x0 = corner1.x;
    y0 = corner1.y;
    x1 = corner2.x;
    y1 = corner2.y;

    // turn into top-left, bottom-right corners
    if (x0 > x1) {
      xTmp = x0;
      x0   = x1;
      x1   = xTmp;
    }
    if (y0 > y1) {
      yTmp = y0;
      y0   = y1;
      y1   = yTmp;
    }

    switch (corner) {
      case TOP_LEFT:
	return new Point(x0, y0);
      case TOP_RIGHT:
	return new Point(x1, y0);
      case BOTTOM_LEFT:
	return new Point(x0, y1);
      case BOTTOM_RIGHT:
	return new Point(x1, y1);
      default:
	throw new IllegalStateException("Unhandled rectangle corner: " + corner);
    }
  }

  /**
   * Updates the corner of a rectangle and returns the new rectangle.
   *
   * @param rect	the rectangle to update
   * @param cornerOld	the old corner
   * @param cornerNew	the new corner
   * @return		the updated rectangle
   */
  public static Rectangle updateCorner(Rectangle rect, Point cornerOld, Point cornerNew) {
    RectangleCorner	corner;
    int			minX;
    int			maxX;
    int			minY;
    int			maxY;

    corner = null;

    minX = rect.x;
    maxX = rect.x + rect.width - 1;
    minY = rect.y;
    maxY = rect.y + rect.height - 1;

    // which corner was updated?
    if (cornerOld.x == minX) {
      if (cornerOld.y == minY)
        corner = RectangleCorner.TOP_LEFT;
      else if (cornerOld.y == maxY)
	corner = RectangleCorner.BOTTOM_LEFT;
    }
    else if (cornerOld.x == maxX) {
      if (cornerOld.y == minY)
	corner = RectangleCorner.TOP_RIGHT;
      else if (cornerOld.y == maxY)
	corner = RectangleCorner.BOTTOM_RIGHT;
    }

    if (corner == null)
      return rect;

    // update boundaries
    switch (corner) {
      case TOP_LEFT:
        minX = cornerNew.x;
        minY = cornerNew.y;
        break;
      case TOP_RIGHT:
        maxX = cornerNew.x;
        minY = cornerNew.y;
        break;
      case BOTTOM_LEFT:
	minX = cornerNew.x;
	maxY = cornerNew.y;
        break;
      case BOTTOM_RIGHT:
	maxX = cornerNew.x;
	maxY = cornerNew.y;
	break;
      default:
        throw new IllegalStateException("Unhandled corner: " + corner);
    }

    return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
  }
}

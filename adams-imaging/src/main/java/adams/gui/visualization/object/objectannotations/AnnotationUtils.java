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
 * AnnotationUtils.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package adams.gui.visualization.object.objectannotations;

import adams.core.Utils;
import adams.data.image.ImageAnchor;
import adams.flow.transformer.locateobjects.LocatedObject;
import adams.flow.transformer.locateobjects.LocatedObjects;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Helper methods for annotations.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class AnnotationUtils {

  /**
   * Applies further format options. Does nothing if format or value are empty.
   *
   * @param value	the value to format
   * @param format	the format to apply
   * @return		the processed value
   */
  public static String applyFormatOptions(String value, String format) {
    int		decimals;
    boolean	isDouble;
    String	tmp;

    if (format.isEmpty() || value.isEmpty())
      return value;

    isDouble = Utils.isDouble(value);

    // max # of decimals?
    if (format.startsWith(".") && isDouble) {
      tmp = format.substring(1);
      if (Utils.isInteger(tmp)) {
	decimals = Integer.parseInt(tmp);
	value    = Utils.doubleToString(Double.parseDouble(value), decimals);
      }
    }

    return value;
  }

  /**
   * Applies the label format to the object to generate a display string.
   *
   * @param object 	the object to use as basis
   * @param typeSuffix 	the meta-data key for the type
   * @param labelFormat	the label format to use
   * @return		the generated label
   */
  public static String applyLabelFormat(LocatedObject object, String typeSuffix, String labelFormat) {
    String 	result;
    String	type;
    String	key;
    String	value;
    int		start;
    int		end;
    String	format;

    type = "";
    if (object.getMetaData().containsKey(typeSuffix))
      type = "" + object.getMetaData().get(typeSuffix);
    result = labelFormat
	.replace("#", "" + object.getMetaData().get(LocatedObjects.KEY_INDEX))
	.replace("@", type)
	.replace("$", type.replaceAll(".*\\.", ""));

    // other meta-data keys?
    while (((start = result.indexOf("{")) > -1) && ((end = result.indexOf("}", start)) > -1)) {
      key    = result.substring(start + 1, end);
      format = "";
      if (key.contains("|")) {
	format = key.substring(key.indexOf("|") + 1);
	key    = key.substring(0, key.indexOf("|"));
      }
      if (object.getMetaData().containsKey(key))
	value = "" + object.getMetaData().get(key);
      else
	value = "";
      value  = applyFormatOptions(value, format);
      result = result.substring(0, start) + value + result.substring(end + 1);
    }

    return result;
  }

  /**
   * Calculates the string dimensions in pixels.
   *
   * @param g		the graphics context
   * @param f		the font to use
   * @param s		the string to measure
   * @return		the dimensions in pixels
   */
  public static Dimension calcStringDimenions(Graphics g, Font f, String s) {
    FontMetrics metrics;

    metrics = g.getFontMetrics(f);
    return new Dimension(metrics.stringWidth(s), metrics.getHeight());
  }

  /**
   * Draws the string at the specified position.
   *
   * @param g		the graphics context
   * @param rect	the reference for the anchor
   * @param anchor 	the anchor to use
   * @param label	the label to draw
   * @param offsetX 	the X offset from the anchor
   * @param offsetY 	the Y offset from the anchor
   * @param font 	the font to use
   */
  public static void drawString(Graphics g, Rectangle rect, ImageAnchor anchor, String label, int offsetX, int offsetY, Font font) {
    Dimension dims;

    if (label.isEmpty())
      return;

    dims = calcStringDimenions(g, font, label);

    switch (offsetX) {
      case -1:
	offsetX = 0;
	break;
      case -2:
	offsetX = -dims.width / 2;
	break;
      case -3:
	offsetX = -dims.width;
	break;
      default:
	if (offsetX < 0)
	  offsetX = 0;
    }

    switch (offsetY) {
      case -1:
	offsetY = 0;
	break;
      case -2:
	offsetY = dims.height / 2;
	break;
      case -3:
	offsetY = dims.height;
	break;
      default:
	if (offsetY < 0)
	  offsetY = 0;
    }

    g.setFont(font);
    switch (anchor) {
      case TOP_LEFT:
	g.drawString(
	    label,
	    (int) (rect.getX() + offsetX),
	    (int) (rect.getY() + offsetY));
	break;
      case TOP_CENTER:
	g.drawString(
	    label,
	    (int) (rect.getX() + rect.width / 2 - dims.width / 2 + offsetX),
	    (int) (rect.getY() + offsetY));
	break;
      case TOP_RIGHT:
	g.drawString(
	    label,
	    (int) (rect.getX() + rect.width + offsetX),
	    (int) (rect.getY() + offsetY));
	break;
      case MIDDLE_LEFT:
	g.drawString(
	    label,
	    (int) (rect.getX() + offsetX),
	    (int) (rect.getY() + rect.height / 2 - dims.height / 2 + offsetY));
	break;
      case MIDDLE_CENTER:
	g.drawString(
	    label,
	    (int) (rect.getX() + rect.width / 2 - dims.width / 2 + offsetX),
	    (int) (rect.getY() + rect.height / 2 - dims.height / 2 + offsetY));
	break;
      case MIDDLE_RIGHT:
	g.drawString(
	    label,
	    (int) (rect.getX() + rect.width + offsetX),
	    (int) (rect.getY() + rect.height / 2 - dims.height / 2 + offsetY));
	break;
      case BOTTOM_LEFT:
	g.drawString(
	    label,
	    (int) (rect.getX() + offsetX),
	    (int) (rect.getY() + rect.height + offsetY));
	break;
      case BOTTOM_CENTER:
	g.drawString(
	    label,
	    (int) (rect.getX() + rect.width / 2 - dims.width / 2 + offsetX),
	    (int) (rect.getY() + rect.height + offsetY));
	break;
      case BOTTOM_RIGHT:
	g.drawString(
	    label,
	    (int) (rect.getX() + rect.width + offsetX),
	    (int) (rect.getY() + rect.height + offsetY));
	break;
      default:
	throw new IllegalStateException("Unhandled label anchor: " + anchor);
    }
  }
}

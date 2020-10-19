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
 * Cursors.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.gui.core;

import adams.data.image.BufferedImageHelper;

import javax.swing.ImageIcon;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * For creating cursors.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Cursors {

  /** the cursor cache (name - cursor). */
  protected static Map<String,Cursor> m_Cache;
  static {
    m_Cache = new HashMap<>();
  }

  /**
   * Returns the cursor cache.
   *
   * @return		the cache (name - cursor)
   */
  public static Map<String,Cursor> getCache() {
    return m_Cache;
  }

  /**
   * Creates a square cursor with the specified width.
   *
   * @param width	the width of the square
   * @return		the cursor
   */
  public static synchronized Cursor square(int width) {
    return square(width, 1.0);
  }

  /**
   * Creates a square cursor with the specified width.
   *
   * @param width	the width of the square
   * @param scale 	for scaling the cursor (1.0 = 100%)
   * @return		the cursor
   */
  public static synchronized Cursor square(int width, double scale) {
    String		name;
    BufferedImage	img;
    Graphics2D 		g2d;
    Point		p;

    name = "square-" + width;
    if (!m_Cache.containsKey(name)) {
      img = new BufferedImage((int) (width*scale), (int) (width*scale), BufferedImage.TYPE_INT_ARGB);
      g2d = img.createGraphics();
      p = new Point((int) (width*scale) / 2, (int) (width*scale) / 2);

      // clear
      g2d.scale(scale, scale);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
      g2d.fillRect(0, 0, width, width);

      // reset composite
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

      // draw
      width= Math.max(4, width);
      g2d.setPaint(Color.BLACK);
      g2d.drawRect(0, 0, width - 1, width - 1);
      g2d.setColor(Color.WHITE);
      g2d.drawRect(1, 1, width - 3, width - 3);
      m_Cache.put(name, Toolkit.getDefaultToolkit().createCustomCursor(img, p, name));
    }
    return m_Cache.get(name);
  }

  /**
   * Creates a circle cursor with the specified diameter.
   *
   * @param diameter	the diameter of the circle
   * @return		the cursor
   */
  public static synchronized Cursor circle(int diameter) {
    return circle(diameter, 1.0);
  }

  /**
   * Creates a circle cursor with the specified diameter.
   *
   * @param diameter	the diameter of the circle
   * @param scale 	for scaling the cursor (1.0 = 100%)
   * @return		the cursor
   */
  public static synchronized Cursor circle(int diameter, double scale) {
    String		name;
    BufferedImage	img;
    Graphics2D 		g2d;
    Point		p;

    name = "circle-" + diameter;
    if (!m_Cache.containsKey(name)) {
      img = new BufferedImage((int) (diameter*scale), (int) (diameter*scale), BufferedImage.TYPE_INT_ARGB);
      g2d = img.createGraphics();
      p = new Point((int) (diameter*scale) / 2, (int) (diameter*scale) / 2);

      // clear
      g2d.scale(scale, scale);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
      g2d.fillRect(0, 0, diameter, diameter);

      // reset composite
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

      // draw
      diameter = Math.max(4, diameter);
      g2d.setPaint(Color.BLACK);
      g2d.drawOval(0, 0, diameter - 1, diameter - 1);
      g2d.setColor(Color.WHITE);
      g2d.drawOval(1, 1, diameter - 3, diameter - 3);
      m_Cache.put(name, Toolkit.getDefaultToolkit().createCustomCursor(img, p, name));
    }
    return m_Cache.get(name);
  }

  /**
   * Creates a cross-hair cursor with the specified diameter.
   *
   * @param diameter	the diameter of the cross-hair
   * @return		the cursor
   */
  public static synchronized Cursor crosshair(int diameter) {
    return crosshair(diameter, 1.0);
  }

  /**
   * Creates a cross-hair cursor with the specified diameter.
   *
   * @param diameter	the diameter of the cross-hair
   * @param scale 	for scaling the cursor (1.0 = 100%)
   * @return		the cursor
   */
  public static synchronized Cursor crosshair(int diameter, double scale) {
    String		name;
    BufferedImage	img;
    Graphics2D 		g2d;
    Point		p;
    int 		l;

    name = "crosshair-" + diameter + "-" + scale;
    if (!m_Cache.containsKey(name)) {
      img = new BufferedImage((int) (diameter*scale), (int) (diameter*scale), BufferedImage.TYPE_INT_ARGB);
      g2d = img.createGraphics();
      if (diameter % 2 == 0)
        diameter++;
      p = new Point((int) (diameter*scale) / 2, (int) (diameter*scale) / 2);

      // white background
      g2d.scale(scale, scale);
      g2d.fillRect(0, 0, diameter, diameter);

      // clear quadrants
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
      l = diameter / 2 - 1;
      g2d.fillRect(0,                0,                l, l);
      g2d.fillRect(diameter / 2 + 2, 0,                l, l);
      g2d.fillRect(0,                diameter / 2 + 2, l, l);
      g2d.fillRect(diameter / 2 + 2, diameter / 2 + 2, l, l);
      g2d.fillRect(diameter / 2 - 1, diameter / 2 - 1, 3, 3);

      // reset composite
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

      // draw cross-hair
      g2d.setPaint(Color.BLACK);
      g2d.fillRect(1,                diameter / 2,     diameter / 2 - 2, 1);
      g2d.fillRect(diameter / 2 + 2, diameter / 2,     diameter / 2 - 2,     1);
      g2d.fillRect(diameter / 2, 1,                1, diameter / 2 - 2);
      g2d.fillRect(diameter / 2, diameter / 2 + 2, 1, diameter / 2 - 2);
      m_Cache.put(name, Toolkit.getDefaultToolkit().createCustomCursor(img, p, name));
    }
    return m_Cache.get(name);
  }

  /**
   * Scales the image according to the scale factor.
   *
   * @param img		the image to scale
   * @param scale	the scale factor
   * @return		the scaled image
   */
  protected static BufferedImage scale(BufferedImage img, double scale) {
    BufferedImage 	result;
    Graphics2D		g2d;

    if (scale == 1.0)
      return img;

    result = new BufferedImage((int) (img.getWidth()*scale), (int) (img.getHeight()*scale), BufferedImage.TYPE_INT_ARGB);
    g2d = result.createGraphics();
    // clear
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
    g2d.fillRect(0, 0, result.getWidth(), result.getHeight());
    // paint
    g2d.scale(scale, scale);
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
    g2d.drawImage(img, 0, 0, null);
    g2d.dispose();

    return result;
  }

  /**
   * Scales the image icon according to the scale factor.
   *
   * @param icon		the image to scale
   * @param scale	the scale factor
   * @return		the scaled image
   */
  protected static BufferedImage scale(ImageIcon icon, double scale) {
    BufferedImage 	result;
    Graphics2D		g2d;

    result = new BufferedImage((int) (icon.getIconWidth()*scale), (int) (icon.getIconHeight()*scale), BufferedImage.TYPE_INT_ARGB);
    g2d = result.createGraphics();
    // clear
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
    g2d.fillRect(0, 0, result.getWidth(), result.getHeight());
    // paint
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
    g2d.scale(scale, scale);
    icon.paintIcon(null, g2d, 0, 0);
    g2d.dispose();

    return result;
  }

  /**
   * Uses the ADAMS image (located adams.gui.images) as icon.
   *
   * @param iconName	the name of the image (no path)
   * @param x		the x coordinate for the cursor hotspot
   * @param y		the y coordinate for the cursor hotspot
   * @return		the cursor
   * @throws IllegalStateException	if the icon doesn't exist
   */
  public static Cursor fromIcon(String iconName, int x, int y) {
    return fromIcon(iconName, x, y, 1.0);
  }

  /**
   * Uses the ADAMS image (located adams.gui.images) as icon.
   *
   * @param iconName	the name of the image (no path)
   * @param x		the x coordinate for the cursor hotspot
   * @param y		the y coordinate for the cursor hotspot
   * @param scale 	for scaling the cursor (1.0 = 100%)
   * @return		the cursor
   * @throws IllegalStateException	if the icon doesn't exist
   */
  public static Cursor fromIcon(String iconName, int x, int y, double scale) {
    String	name;
    ImageIcon	icon;

    name = iconName + "-" + x + "-" + y + "-" + scale;
    if (!m_Cache.containsKey(name)) {
      icon = GUIHelper.getIcon(iconName);
      if (icon == null)
        throw new IllegalStateException("Failed to load icon: " + iconName);
      m_Cache.put(name, Toolkit.getDefaultToolkit().createCustomCursor(scale(icon, scale), new Point(x, y), name));
    }
    return m_Cache.get(name);
  }

  /**
   * Uses the image as icon.
   *
   * @param fileName	the name of the image (incl path)
   * @param x		the x coordinate for the cursor hotspot
   * @param y		the y coordinate for the cursor hotspot
   * @return		the cursor
   * @throws IllegalStateException	if the icon doesn't exist
   */
  public static Cursor fromFile(String fileName, int x, int y) {
    return fromFile(new File(fileName), x, y);
  }

  /**
   * Uses the image as icon.
   *
   * @param fileName	the name of the image (incl path)
   * @param x		the x coordinate for the cursor hotspot
   * @param y		the y coordinate for the cursor hotspot
   * @param scale 	for scaling the cursor (1.0 = 100%)
   * @return		the cursor
   * @throws IllegalStateException	if the icon doesn't exist
   */
  public static Cursor fromFile(String fileName, int x, int y, double scale) {
    return fromFile(new File(fileName), x, y, scale);
  }

  /**
   * Uses the image as icon.
   *
   * @param file	the name of the image (incl path)
   * @param x		the x coordinate for the cursor hotspot
   * @param y		the y coordinate for the cursor hotspot
   * @return		the cursor
   * @throws IllegalStateException	if the icon doesn't exist
   */
  public static Cursor fromFile(File file, int x, int y) {
    return fromFile(file, x, y, 1.0);
  }

  /**
   * Uses the image as icon.
   *
   * @param file	the name of the image (incl path)
   * @param x		the x coordinate for the cursor hotspot
   * @param y		the y coordinate for the cursor hotspot
   * @param scale 	for scaling the cursor (1.0 = 100%)
   * @return		the cursor
   * @throws IllegalStateException	if the icon doesn't exist
   */
  public static Cursor fromFile(File file, int x, int y, double scale) {
    String		name;
    BufferedImage 	img;

    name = file.getAbsolutePath() + "-" + x + "-" + y + "-" + scale;
    if (!m_Cache.containsKey(name)) {
      img = BufferedImageHelper.read(file).toBufferedImage();
      if (img == null)
        throw new IllegalStateException("Failed to load image: " + file);
      m_Cache.put(name, Toolkit.getDefaultToolkit().createCustomCursor(scale(img, scale), new Point(x, y), name));
    }
    return m_Cache.get(name);
  }
}

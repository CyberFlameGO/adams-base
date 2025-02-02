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
 * Scale.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, NZ
 */

package adams.data.objectfilter;

import adams.core.QuickInfoHelper;
import adams.data.RoundingType;
import adams.data.RoundingUtils;
import adams.flow.transformer.locateobjects.LocatedObject;
import adams.flow.transformer.locateobjects.LocatedObjects;

import java.awt.Polygon;

/**
 <!-- globalinfo-start -->
 * Scales the objects.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 *
 * <pre>-scale-x &lt;double&gt; (property: scaleX)
 * &nbsp;&nbsp;&nbsp;The factor for scaling x&#47;width.
 * &nbsp;&nbsp;&nbsp;default: 1.0
 * &nbsp;&nbsp;&nbsp;minimum: 0.0
 * </pre>
 *
 * <pre>-scale-y &lt;double&gt; (property: scaleY)
 * &nbsp;&nbsp;&nbsp;The factor for scaling y&#47;width.
 * &nbsp;&nbsp;&nbsp;default: 1.0
 * &nbsp;&nbsp;&nbsp;minimum: 0.0
 * </pre>
 *
 * <pre>-round &lt;boolean&gt; (property: round)
 * &nbsp;&nbsp;&nbsp;If enabled, the scaled values get round.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-rounding-type &lt;ROUND|CEILING|FLOOR&gt; (property: roundingType)
 * &nbsp;&nbsp;&nbsp;The type of rounding to perform.
 * &nbsp;&nbsp;&nbsp;default: ROUND
 * </pre>
 *
 <!-- options-end -->
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Scale
  extends AbstractObjectFilter {

  private static final long serialVersionUID = -2181381799680316619L;

  /** the scale factor for x/width. */
  protected double m_ScaleX;

  /** the scale factor for y/height. */
  protected double m_ScaleY;

  /** whether to round the scaled values. */
  protected boolean m_Round;

  /** the rounding type. */
  protected RoundingType m_RoundingType;

  /** the number of decimals. */
  protected int m_NumDecimals;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Scales the objects.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "scale-x", "scaleX",
      1.0, 0.0, null);

    m_OptionManager.add(
      "scale-y", "scaleY",
      1.0, 0.0, null);

    m_OptionManager.add(
      "round", "round",
      false);

    m_OptionManager.add(
      "rounding-type", "roundingType",
      RoundingType.ROUND);

    m_OptionManager.add(
      "num-decimals", "numDecimals",
      0, 0, null);
  }

  /**
   * Sets the scale factor for x/width.
   *
   * @param value	the factor
   */
  public void setScaleX(double value) {
    if (getOptionManager().isValid("scaleX", value)) {
      m_ScaleX = value;
      reset();
    }
  }

  /**
   * Returns the scale factor for x/width.
   *
   * @return		the factor
   */
  public double getScaleX() {
    return m_ScaleX;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String scaleXTipText() {
    return "The factor for scaling x/width.";
  }

  /**
   * Sets the scale factor for y/width.
   *
   * @param value	the factor
   */
  public void setScaleY(double value) {
    if (getOptionManager().isValid("scaleY", value)) {
      m_ScaleY = value;
      reset();
    }
  }

  /**
   * Returns the scale factor for y/width.
   *
   * @return		the factor
   */
  public double getScaleY() {
    return m_ScaleY;
  }

  /**
   * Returns the tip teyt for this property.
   *
   * @return 		tip teyt for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String scaleYTipText() {
    return "The factor for scaling y/width.";
  }

  /**
   * Sets whether to round the scaled values.
   *
   * @param value	true if to round
   */
  public void setRound(boolean value) {
    m_Round = value;
    reset();
  }

  /**
   * Returns whether to round the scaled values.
   *
   * @return		true if to round
   */
  public boolean getRound() {
    return m_Round;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String roundTipText() {
    return "If enabled, the scaled values get round.";
  }

  /**
   * Sets the type of rounding to perform.
   *
   * @param value	the type
   */
  public void setRoundingType(RoundingType value) {
    m_RoundingType = value;
    reset();
  }

  /**
   * Returns the type of rounding to perform.
   *
   * @return		the type
   */
  public RoundingType getRoundingType() {
    return m_RoundingType;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String roundingTypeTipText() {
    return "The type of rounding to perform.";
  }

  /**
   * Sets the number of decimals after the decimal point to use.
   *
   * @param value	the number of decimals
   */
  public void setNumDecimals(int value) {
    if (getOptionManager().isValid("numDecimals", value)) {
      m_NumDecimals = value;
      reset();
    }
  }

  /**
   * Returns the number of decimals after the decimal point to use.
   *
   * @return		the number of decimals
   */
  public int getNumDecimals() {
    return m_NumDecimals;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String numDecimalsTipText() {
    return "The number of decimals after the decimal point to use.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result  = QuickInfoHelper.toString(this, "scaleX", m_ScaleX, "x: ");
    result += QuickInfoHelper.toString(this, "scaleY", m_ScaleY, ", y: ");
    if (m_Round) {
      result += QuickInfoHelper.toString(this, "roundingType", m_RoundingType, ", rounding: ");
      result += QuickInfoHelper.toString(this, "numDecimals", m_NumDecimals, ", decimals: ");
    }

    return result;
  }

  /**
   * Rounds the value according to the parameters.
   *
   * @param value	the value to round
   * @return		the potentially rounded value
   * @see		#m_Round
   * @see		#m_RoundingType
   */
  protected double round(double value) {
    if (!m_Round)
      return value;
    else
      return RoundingUtils.apply(m_RoundingType, value, m_NumDecimals);
  }

  /**
   * Filters the image objects.
   *
   * @param objects	the located objects
   * @return		the updated list of objects
   */
  @Override
  protected LocatedObjects doFilter(LocatedObjects objects) {
    LocatedObjects	result;
    LocatedObject	newObj;
    boolean		hasPoly;
    int[]		xpoints;
    int[]		ypoints;
    int			i;

    result = new LocatedObjects();
    for (LocatedObject obj: objects) {
      hasPoly = obj.hasPolygon();
      xpoints = new int[0];
      ypoints = new int[0];
      if (hasPoly) {
        xpoints = obj.getPolygon().xpoints.clone();
        ypoints = obj.getPolygon().ypoints.clone();
        for (i = 0; i < xpoints.length; i++) {
          xpoints[i] *= m_ScaleX;
          ypoints[i] *= m_ScaleY;
	}
      }
      newObj = new LocatedObject(
        obj.getImage(),
	(int) round(obj.getX() * m_ScaleX),
	(int) round(obj.getY() * m_ScaleY),
	(int) round(obj.getWidth() * m_ScaleX),
	(int) round(obj.getHeight() * m_ScaleY),
	obj.getMetaData(true));
      if (hasPoly)
        newObj.setPolygon(new Polygon(xpoints, ypoints, xpoints.length));
      result.add(newObj);
    }

    return result;
  }
}

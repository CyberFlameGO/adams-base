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
 * CircleHitDetector.java
 * Copyright (C) 2013-2017 University of Waikato, Hamilton, New Zealand
 */

package adams.gui.visualization.sequence;

import adams.data.sequence.XYSequence;
import adams.data.sequence.XYSequencePoint;
import adams.data.sequence.XYSequenceUtils;
import adams.gui.visualization.core.AxisPanel;
import adams.gui.visualization.core.plot.Axis;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Detects selections of sequence points in the sequence panel.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class CircleHitDetector
  extends AbstractXYSequencePointHitDetector {

  /** for serialization. */
  private static final long serialVersionUID = -3363546923840405674L;

  /**
   * Initializes the hit detector (constructor only for GOE) with no owner.
   */
  public CircleHitDetector() {
    this(null);
  }

  /**
   * Initializes the hit detector.
   *
   * @param owner	the paintlet that uses this detector
   */
  public CircleHitDetector(XYSequencePaintlet owner) {
    super(owner);
  }

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Detects selections of timestamps in the total ion count panel.";
  }

  /**
   * Returns the default minimum pixel difference.
   *
   * @return		the minimum
   */
  protected int getDefaultMinimumPixelDifference() {
    return 1;
  }

  /**
   * Checks for a hit.
   *
   * @param e		the MouseEvent (for coordinates)
   * @return		the associated object with the hit, otherwise null
   */
  @Override
  protected List<XYSequencePoint> isHit(MouseEvent e) {
    List<XYSequencePoint>	result;
    double			y;
    double			x;
    double			diffY;
    double			diffX;
    double			diffPixel;
    int				i;
    XYSequence			s;
    XYSequencePoint		sp;
    AxisPanel			axisBottom;
    AxisPanel			axisLeft;
    int[]			indices;
    List<XYSequencePoint>	points;
    int				diameter;
    double			wobble;
    boolean			logging;

    if (m_Owner == null)
      return null;

    result     = new ArrayList<>();
    axisBottom = m_Owner.getPlot().getAxis(Axis.BOTTOM);
    axisLeft   = m_Owner.getPlot().getAxis(Axis.LEFT);
    y          = axisLeft.posToValue(e.getY());
    x          = axisBottom.posToValue(e.getX());
    logging    = isLoggingEnabled();
    diameter   = 1;
    if (m_Owner instanceof DiameterBasedPaintlet) {
      diameter = ((DiameterBasedPaintlet) m_Owner).getDiameter();
    }
    else if (m_Owner instanceof MetaXYSequencePaintlet) {
      if (((MetaXYSequencePaintlet) m_Owner).getPaintlet() instanceof DiameterBasedPaintlet)
        diameter = ((DiameterBasedPaintlet) ((MetaXYSequencePaintlet) m_Owner).getPaintlet()).getDiameter();
    }
    wobble = Math.abs(axisBottom.posToValue(0) - axisBottom.posToValue(diameter));

    for (i = 0; i < m_Owner.getSequencePanel().getContainerManager().count(); i++) {
      if (!m_Owner.getSequencePanel().getContainerManager().get(i).isVisible())
	continue;

      // check for hit
      s      = m_Owner.getSequencePanel().getContainerManager().get(i).getData();
      points = s.toList();

      if (logging)
	getLogger().info("\n" + s.getID() + ":");

      indices = XYSequenceUtils.findClosestXs(points, x, wobble);
      for (int index: indices) {
	sp = points.get(index);

	diffX = sp.getX() - x;
	diffPixel = Math.abs(axisBottom.valueToPos(diffX) - axisBottom.valueToPos(0));
	if (logging)
	  getLogger().info("diff x=" + diffPixel);
	if (diffPixel > m_MinimumPixelDifference + (diameter / 2))
	  continue;
	diffY = sp.getY() - y;
	diffPixel = Math.abs(axisLeft.valueToPos(diffY) - axisLeft.valueToPos(0));
	if (logging)
	  getLogger().info("diff y=" + diffPixel);
	if (diffPixel > m_MinimumPixelDifference + (diameter / 2))
	  continue;

	// add hit
	if (logging)
	  getLogger().info("hit!");
	result.add(sp);
      }
    }

    if (result.size() > 0)
      return result;
    else
      return null;
  }
}

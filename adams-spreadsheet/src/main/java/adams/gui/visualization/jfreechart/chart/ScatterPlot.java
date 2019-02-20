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
 * ScatterPlot.java
 * Copyright (C) 2016-2019 University of Waikato, Hamilton, NZ
 */

package adams.gui.visualization.jfreechart.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;

/**
 * Generates a scatter plot from XY data.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ScatterPlot
  extends AbstractChartGeneratorWithAxisLabels<XYDataset> {

  private static final long serialVersionUID = -4759011723765395176L;

  /** the plot orientation. */
  protected Orientation m_Orientation;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Creates a simple scatter plot from X-Y data.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "orientation", "orientation",
      Orientation.VERTICAL);
  }

  /**
   * Sets the orientation for the plot.
   *
   * @param value	the orientation
   */
  public void setOrientation(Orientation value) {
    m_Orientation = value;
    reset();
  }

  /**
   * Returns the orientation for the plot.
   *
   * @return		the orientation
   */
  public Orientation getOrientation() {
    return m_Orientation;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String orientationTipText() {
    return "The orientation of the plot.";
  }

  /**
   * Performs the actual generation of the chart.
   *
   * @param data	the data to use
   * @return		the chart
   */
  @Override
  protected JFreeChart doGenerate(XYDataset data) {
    JFreeChart result = ChartFactory.createScatterPlot(m_Title, m_LabelX, m_LabelY, data, m_Orientation.getOrientation(), m_Legend, m_ToolTips, false);
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    if (result.getXYPlot().getSeriesCount() == 2) {
      renderer.setSeriesLinesVisible(0, false);
      renderer.setSeriesLinesVisible(1, true);
      renderer.setSeriesShapesVisible(0, true);
      renderer.setSeriesShapesVisible(1, false);
      result.getXYPlot().setRenderer(renderer);
    }
    return result;
  }
}

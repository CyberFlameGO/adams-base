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
 * Adjust.java
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */

package adams.gui.visualization.timeseries.plotpopup;

import adams.data.timeseries.Timeseries;
import adams.gui.core.ImageManager;
import adams.gui.visualization.container.DataContainerPanelWithContainerList;
import adams.gui.visualization.container.datacontainerpanel.plotpopup.AbstractPlotPopupCustomizer;
import adams.gui.visualization.timeseries.TimeseriesContainer;
import adams.gui.visualization.timeseries.TimeseriesContainerManager;
import adams.gui.visualization.timeseries.TimeseriesPanel;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * Whether to adjust the plot to the loaded or visible data.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class Adjust
  extends AbstractPlotPopupCustomizer<Timeseries, TimeseriesContainerManager, TimeseriesContainer> {

  private static final long serialVersionUID = 3295471324320509106L;

  /**
   * The name.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Adjust";
  }

  /**
   * The group this customizer belongs to.
   *
   * @return		the group
   */
  @Override
  public String getGroup() {
    return "graphics";
  }

  /**
   * Checks whether this action can handle the panel.
   *
   * @param panel	the panel to check
   * @return		true if handled
   */
  @Override
  public boolean handles(DataContainerPanelWithContainerList<Timeseries, TimeseriesContainerManager, TimeseriesContainer> panel) {
    return (panel instanceof TimeseriesPanel);
  }

  /**
   * Returns a popup menu for the table of the container list.
   *
   * @param panel	the affected panel
   * @param e		the mouse event
   * @param menu	the popup menu to customize
   */
  @Override
  public void customize(DataContainerPanelWithContainerList<Timeseries, TimeseriesContainerManager, TimeseriesContainer> panel, MouseEvent e, JPopupMenu menu) {
    JMenuItem		item;

    item = new JMenuItem();
    item.setIcon(ImageManager.getEmptyIcon());
    if (((TimeseriesPanel) panel).getAdjustToVisibleData())
      item.setText("Adjust to loaded data");
    else
      item.setText("Adjust to visible data");
    item.addActionListener((ActionEvent ae) -> {
      ((TimeseriesPanel) panel).setAdjustToVisibleData(!((TimeseriesPanel) panel).getAdjustToVisibleData());
      panel.update();
    });
    menu.add(item);
  }
}

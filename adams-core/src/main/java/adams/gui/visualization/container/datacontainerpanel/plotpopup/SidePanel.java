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
 * SidePanel.java
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */

package adams.gui.visualization.container.datacontainerpanel.plotpopup;

import adams.data.container.DataContainer;
import adams.gui.core.ImageManager;
import adams.gui.visualization.container.AbstractContainer;
import adams.gui.visualization.container.AbstractContainerManager;
import adams.gui.visualization.container.DataContainerPanelWithContainerList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * For showing/hiding the side panel.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class SidePanel<T extends DataContainer, M extends AbstractContainerManager, C extends AbstractContainer>
  extends AbstractPlotPopupCustomizer<T,M,C> {

  private static final long serialVersionUID = 4374831204149159338L;

  /**
   * The name.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Side panel";
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
  public boolean handles(DataContainerPanelWithContainerList<T, M, C> panel) {
    return true;
  }

  /**
   * Returns a popup menu for the table of the container list.
   *
   * @param panel	the affected panel
   * @param e		the mouse event
   * @param menu	the popup menu to customize
   */
  @Override
  public void customize(final DataContainerPanelWithContainerList<T, M, C> panel, MouseEvent e, JPopupMenu menu) {
    JMenuItem 		item;

    item = new JMenuItem();
    item.setIcon(ImageManager.getEmptyIcon());
    if (panel.isSidePanelVisible())
      item.setText("Hide side panel");
    else
      item.setText("Show side panel");
    item.addActionListener((ActionEvent ae) -> panel.setSidePanelVisible(!panel.isSidePanelVisible()));
    menu.add(item);
  }
}

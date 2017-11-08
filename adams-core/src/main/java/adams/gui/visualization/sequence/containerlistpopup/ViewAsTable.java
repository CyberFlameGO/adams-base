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
 * ViewAsTable.java
 * Copyright (C) 2016-2017 University of Waikato, Hamilton, NZ
 */

package adams.gui.visualization.sequence.containerlistpopup;

import adams.data.sequence.XYSequence;
import adams.gui.visualization.container.DataContainerPanelWithContainerList;
import adams.gui.visualization.container.datacontainerpanel.containerlistpopup.AbstractContainerListPopupCustomizer;
import adams.gui.visualization.sequence.XYSequenceContainer;
import adams.gui.visualization.sequence.XYSequenceContainerManager;
import adams.gui.visualization.sequence.XYSequencePanel;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.ActionEvent;

/**
 * Views the selected sequence as table.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ViewAsTable
  extends AbstractContainerListPopupCustomizer<XYSequence, XYSequenceContainerManager, XYSequenceContainer>{

  private static final long serialVersionUID = -7796583803269239174L;

  /**
   * The name.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "View as table";
  }

  /**
   * The group this customizer belongs to.
   *
   * @return		the group
   */
  @Override
  public String getGroup() {
    return "view";
  }

   /**
   * Checks whether this action can handle the panel.
   *
   * @param panel	the panel to check
   * @return		true if handled
   */
 @Override
  public boolean handles(DataContainerPanelWithContainerList<XYSequence, XYSequenceContainerManager, XYSequenceContainer> panel) {
    return (panel instanceof XYSequencePanel);
  }

  /**
   * Returns a popup menu for the table of the container list.
   *
   * @param context	the context
   * @param menu	the popup menu to customize
   */
  @Override
  public void customize(final Context<XYSequence,XYSequenceContainerManager,XYSequenceContainer> context, JPopupMenu menu) {
    JMenuItem		item;
    final int[]		indices;

    indices = context.actualSelectedContainerIndices;
    item = new JMenuItem("View as table");
    item.setEnabled(indices.length == 1);
    item.addActionListener((ActionEvent e) -> ((XYSequencePanel) context.panel).viewSequence(context.panel.getContainerManager().get(indices[0])));
  }
}

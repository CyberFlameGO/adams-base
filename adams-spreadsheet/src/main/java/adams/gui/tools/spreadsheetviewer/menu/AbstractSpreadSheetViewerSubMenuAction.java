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
 * AbstractSpreadSheetViewerSubMenuAction.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.tools.spreadsheetviewer.menu;

import adams.core.Properties;
import adams.gui.action.AbstractPropertiesSubMenuAction;
import adams.gui.tools.SpreadSheetViewerPanel;
import adams.gui.tools.spreadsheetviewer.TabbedPane;

/**
 * Ancestor for actions in the spreadsheet viewer that generate a submenu.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public abstract class AbstractSpreadSheetViewerSubMenuAction
  extends AbstractPropertiesSubMenuAction<SpreadSheetViewerPanel>
  implements SpreadSheetViewerAction {

  /** for serialization. */
  private static final long serialVersionUID = 1168747259624542350L;
  
  /**
   * Returns the underlying properties.
   * 
   * @return		the properties
   */
  @Override
  protected Properties getProperties() {
    return SpreadSheetViewerPanel.getPropertiesMenu();
  }
  
  /**
   * Returns the tabbed pane of the viewer.
   * 
   * @return		the tabbed pane
   */
  protected TabbedPane getTabbedPane() {
    return m_State.getTabbedPane();
  }

  /**
   * Returns whether a sheet is selected.
   * 
   * @return		true if selected
   */
  protected boolean isSheetSelected() {
    return (getTabbedPane().getTabCount() > 0) && (getTabbedPane().getSelectedIndex() != -1);
  }
}

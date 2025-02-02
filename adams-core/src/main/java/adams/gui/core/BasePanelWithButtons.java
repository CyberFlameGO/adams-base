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
 * BasePanelWithButtons.java
 * Copyright (C) 2010-2015 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.core;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

/**
 * Panel that offers associated buttons on the right-hand side.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class BasePanelWithButtons
  extends BasePanel {

  /** for serialization. */
  private static final long serialVersionUID = 2480939317042703826L;

  /** the panel containing the buttons. */
  protected JPanel m_PanelButtons;

  /** the layout for the buttons. */
  protected GridLayout m_LayoutButtons;

  /**
   * Initializes the widgets.
   */
  protected void initGUI() {
    JPanel	panel;

    super.initGUI();

    setLayout(new BorderLayout(1, 0));
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    // the buttons
    m_LayoutButtons = new GridLayout(0, 1, 0, 1);
    m_PanelButtons  = new JPanel(m_LayoutButtons);
    m_PanelButtons.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
    panel           = new JPanel(new BorderLayout());
    panel.add(m_PanelButtons, BorderLayout.NORTH);
    add(panel, BorderLayout.EAST);
  }

  /**
   * Removes all components from the panel holding the buttons.
   */
  public void clearButtonsPanel() {
    m_PanelButtons.removeAll();
  }

  /**
   * Adds the component to the panel with the buttons.
   *
   * @param comp	the component to add
   */
  public void addToButtonsPanel(Component comp) {
    removeFromButtonsPanel(comp);
    m_LayoutButtons.setRows(m_LayoutButtons.getRows() + 1);
    m_PanelButtons.add(comp);
  }

  /**
   * Removes the component from the panel with the buttons.
   *
   * @param comp	the component to remove
   */
  public void removeFromButtonsPanel(Component comp) {
    m_LayoutButtons.setRows(m_LayoutButtons.getRows() - 1);
    m_PanelButtons.remove(comp);
  }
}

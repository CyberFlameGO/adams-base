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
 * ProgramUserMode.java
 * Copyright (C) 2012-2023 University of Waikato, Hamilton, New Zealand
 *
 */

package adams.gui.menu;

import adams.core.option.UserMode;
import adams.gui.application.AbstractApplicationFrame;
import adams.gui.application.AbstractBasicMenuItemDefinition;
import adams.gui.core.ImageManager;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import java.awt.event.ActionEvent;

/**
 * Allows the user to switch the user-mode from the GUI.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class ProgramUserMode
  extends AbstractBasicMenuItemDefinition {

  /** for serialization. */
  private static final long serialVersionUID = 2322866186840295800L;

  /**
   * Initializes the menu item with no owner.
   */
  public ProgramUserMode() {
    this(null);
  }

  /**
   * Initializes the menu item.
   *
   * @param owner	the owning application
   */
  public ProgramUserMode(AbstractApplicationFrame owner) {
    super(owner);
  }

  /**
   * Returns the file name of the icon.
   *
   * @return		the filename or null if no icon available
   */
  @Override
  public String getIconName() {
    return "person.png";
  }

  /**
   * Does nothing.
   */
  @Override
  public void launch() {
  }

  /**
   * Returns the title of the window (and text of menuitem).
   *
   * @return 		the title
   */
  @Override
  public String getTitle() {
    return "User mode";
  }

  /**
   * Whether the panel can only be displayed once.
   *
   * @return		true if the panel can only be displayed once
   */
  @Override
  public boolean isSingleton() {
    return false;
  }

  /**
   * Returns the user mode, which determines visibility as well.
   *
   * @return		the user mode
   */
  @Override
  public UserMode getUserMode() {
    return UserMode.BASIC;
  }

  /**
   * Returns the category of the menu item in which it should appear, i.e.,
   * the name of the menu.
   *
   * @return		the category/menu name
   */
  @Override
  public String getCategory() {
    return CATEGORY_PROGRAM;
  }

  /**
   * Returns the JMenuItem to use.
   *
   * @return		the menu item
   * @see		#launch()
   */
  @Override
  public JMenuItem getMenuItem() {
    JMenu			result;
    JRadioButtonMenuItem 	menuitem;
    ButtonGroup			group;

    result = new JMenu(getTitle());
    result.setIcon(ImageManager.getIcon(getIconName()));
    group = new ButtonGroup();
    for (final UserMode um: UserMode.values()) {
      menuitem = new JRadioButtonMenuItem(um.toDisplay());
      group.add(menuitem);
      if (m_Owner.getUserMode() == um)
	menuitem.setSelected(true);
      menuitem.addActionListener((ActionEvent e) -> m_Owner.setUserMode(um));
      result.add(menuitem);
    }

    return result;
  }
}
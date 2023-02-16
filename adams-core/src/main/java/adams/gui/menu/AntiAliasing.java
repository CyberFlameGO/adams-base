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
 * AntiAliasing.java
 * Copyright (C) 2016 University of Waikato, Hamilton, New Zealand
 *
 */

package adams.gui.menu;

import adams.core.option.UserMode;
import adams.gui.application.AbstractApplicationFrame;
import adams.gui.application.AbstractMenuItemDefinition;
import adams.gui.core.GUIHelper;
import adams.gui.core.ImageManager;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import java.awt.event.ActionEvent;

/**
 * Global switch for anti-aliasing.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class AntiAliasing
  extends AbstractMenuItemDefinition {

  /** for serialization. */
  private static final long serialVersionUID = 7586443345167287461L;

  /**
   * Initializes the menu item with no owner.
   */
  public AntiAliasing() {
    this(null);
  }

  /**
   * Initializes the menu item.
   *
   * @param owner	the owning application
   */
  public AntiAliasing(AbstractApplicationFrame owner) {
    super(owner);
  }

  /**
   * Returns the JMenuItem to use.
   *
   * @return		the menu item
   */
  @Override
  public JMenuItem getMenuItem() {
    JMenu			result;
    ButtonGroup 		group;
    JRadioButtonMenuItem	menuitem;

    result = new JMenu(getTitle());
    result.setIcon(ImageManager.getIcon(getIconName()));

    group    = new ButtonGroup();
    menuitem = new JRadioButtonMenuItem("Enable");
    menuitem.setSelected(GUIHelper.AntiAliasingEnabled);
    menuitem.addActionListener((ActionEvent e) -> GUIHelper.AntiAliasingEnabled = true);
    group.add(menuitem);
    result.add(menuitem);

    menuitem = new JRadioButtonMenuItem("Disable");
    menuitem.setSelected(!GUIHelper.AntiAliasingEnabled);
    menuitem.addActionListener((ActionEvent e) -> GUIHelper.AntiAliasingEnabled = false);
    group.add(menuitem);
    result.add(menuitem);

    return result;
  }

  /**
   * Returns the title of the window (and text of menuitem).
   *
   * @return 		the title
   */
  @Override
  public String getTitle() {
    return "Anti-Aliasing";
  }

  /**
   * Returns the file name of the icon.
   *
   * @return		the filename or null if no icon available
   */
  @Override
  public String getIconName() {
    return "plot.gif";
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
    return CATEGORY_VISUALIZATION;
  }
}
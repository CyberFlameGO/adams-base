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
 * AbstractToolPluginManager.java
 * Copyright (C) 2015-2020 University of Waikato, Hamilton, NZ
 */

package adams.gui.plugin;

import adams.core.classmanager.ClassManager;
import adams.gui.core.BaseMenu;
import adams.gui.core.GUIHelper;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Ancestor for classes that manage tool plugins.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @param <T> the owning tool
 * @param <P> the type of plugin
 */
public abstract class AbstractToolPluginManager<T extends ToolPluginSupporter, P extends AbstractToolPlugin> {

  /** the owner. */
  protected T m_Owner;

  /** the plugins. */
  protected List<P> m_Plugins;

  /** the plugin menu items. */
  protected List<JMenuItem> m_MenuItems;

  /** the change listener to use for triggering menu updates. */
  protected ChangeListener m_MenuUpdateListener;

  /**
   * Initializes the manager.
   *
   * @param owner	the owning tool
   */
  protected AbstractToolPluginManager(T owner) {
    m_Owner              = owner;
    m_Plugins            = new ArrayList<>();
    m_MenuItems          = new ArrayList<>();
    m_MenuUpdateListener = null;
  }

  /**
   * Sets the listener for menu updates.
   *
   * @param l		the listener, null to unset
   */
  public void setMenuUpdateListener(ChangeListener l) {
    m_MenuUpdateListener = l;
  }

  /**
   * Returns the listener for menu updates.
   *
   * @return		the listener, null if not available
   */
  public ChangeListener getMenuUpdateListener() {
    return m_MenuUpdateListener;
  }

  /**
   * Returns a list of plugin classnames.
   *
   * @return		all the available plugins
   */
  public abstract String[] getPlugins();

  /**
   * Adds the plugins to the menu bar.
   *
   * @param menubar	the menu bar
   */
  public void addToMenuBar(JMenuBar menubar) {
    JMenu			menu;
    JMenuItem			menuitem;
    int				i;
    int				n;
    String[]			plugins;
    List<String> 		menuNames;
    HashMap<String,JMenu> 	menus;
    List<JMenu> 		newMenus;

    m_MenuItems.clear();
    m_Plugins.clear();
    plugins = getPlugins();

    // gather menus
    menuNames = new ArrayList<>();
    for (i = 0; i < plugins.length; i++) {
      try {
	P plugin = (P) ClassManager.getSingleton().forName(plugins[i]).getDeclaredConstructor().newInstance();
	m_Plugins.add(plugin);
	if (!menuNames.contains(plugin.getMenu()))
	  menuNames.add(plugin.getMenu());
      }
      catch (Exception e) {
	System.err.println("Failed to install plugin '" + plugins[i] + "':");
	e.printStackTrace();
      }
    }
    Collections.sort(menuNames);

    // add menu
    newMenus = new ArrayList<>();
    menus    = new HashMap<>();
    for (String menuName: menuNames) {
      // does menu already exist?
      menu = null;
      for (n = 0; n < menubar.getMenuCount(); n++) {
	if (menubar.getMenu(n).getText().equals(menuName)) {
	  menu = menubar.getMenu(n);
	  menu.addSeparator();
	  break;
	}
      }
      // add new menu
      if (menu == null) {
	menu = new BaseMenu(menuName);
	menubar.add(menu);
	menu.setVisible(plugins.length > 0);
	menu.addChangeListener(new ChangeListener() {
	  public void stateChanged(ChangeEvent e) {
	    if (m_MenuUpdateListener != null)
	      m_MenuUpdateListener.stateChanged(e);
	    updateMenu();
	  }
	});
	newMenus.add(menu);
      }
      menus.put(menuName, menu);
    }

    // add items
    for (i = 0; i < m_Plugins.size(); i++) {
      try {
	final P plugin = m_Plugins.get(i);
	menuitem = new JMenuItem(plugin.getCaption());
	menuitem.setIcon(plugin.getIcon());
	menus.get(plugin.getMenu()).add(menuitem);
	menuitem.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent e) {
	    String error = plugin.execute(m_Owner.getCurrentPanel());
	    if ((error != null) && !error.isEmpty())
	      GUIHelper.showErrorMessage(
		m_Owner.getCurrentPanel(),
		"Error occurred executing plugin '" + plugin.getCaption() + "':\n" + error);
	    updateMenu();
	  }
	});
	m_MenuItems.add(menuitem);
      }
      catch (Exception e) {
	System.err.println("Failed to install plugin '" + plugins[i] + "':");
	e.printStackTrace();
      }
    }

    // sort
    for (JMenu m: newMenus) {
      if (m instanceof BaseMenu)
	((BaseMenu) m).sort();
    }
  }

  /**
   * updates the enabled state of the menu items.
   */
  public void updateMenu() {
    int		i;
    boolean	enabled;

    for (i = 0; i < m_Plugins.size(); i++) {
      try {
	enabled = m_Plugins.get(i).canExecute(m_Owner.getCurrentPanel());
	m_MenuItems.get(i).setEnabled(enabled);
      }
      catch (Exception e) {
	System.err.println("Failed to update plugin: " + m_Plugins.get(i).getClass().getName());
	e.printStackTrace();
      }
    }
  }
}

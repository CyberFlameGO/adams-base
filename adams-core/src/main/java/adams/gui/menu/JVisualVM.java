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
 * JVisualVM.java
 * Copyright (C) 2010-2019 University of Waikato, Hamilton, New Zealand
 *
 */

package adams.gui.menu;

import adams.core.management.ProcessUtils;
import adams.gui.application.AbstractApplicationFrame;
import adams.gui.core.GUIHelper;

/**
 * Starts up jvisualvm.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @see adams.core.management.JVisualVM
 */
public class JVisualVM
  extends AbstractJDKMenuItemDefinition {

  /** for serialization. */
  private static final long serialVersionUID = 5160350971358567707L;

  /**
   * Initializes the menu item with no owner.
   */
  public JVisualVM() {
    this(null);
  }

  /**
   * Initializes the menu item.
   *
   * @param owner	the owning application
   */
  public JVisualVM(AbstractApplicationFrame owner) {
    super(owner);
  }

  /**
   * Launches the functionality of the menu item.
   */
  @Override
  protected void doLaunch() {
    if (!adams.core.management.JVisualVM.isAvailable()) {
      GUIHelper.showErrorMessage(
	null,
	"JVisualVM binary '" + adams.core.management.JVisualVM.getExecutablePath() + "' is not available!\n"
	  + "\n"
	  + adams.core.management.JVisualVM.getPropertiesHelp());
      return;
    }

    // query for options
    String options = GUIHelper.showInputDialog(
	null, "Enter the options for " + adams.core.management.JVisualVM.EXECUTABLE + ":",
	adams.core.management.JVisualVM.getDefaultOptions());
    if (options == null)
      return;

    final long fPid = ProcessUtils.getVirtualMachinePID();
    final String fOptions = options;
    Thread thread = new Thread(() -> adams.core.management.JVisualVM.execute(fOptions, fPid));
    thread.start();
  }

  /**
   * Returns the title of the window (and text of menuitem).
   *
   * @return 		the title
   */
  @Override
  public String getTitle() {
    return "JVisualVM";
  }

  /**
   * Whether the panel can only be displayed once.
   *
   * @return		true if the panel can only be displayed once
   */
  @Override
  public boolean isSingleton() {
    return true;
  }
}
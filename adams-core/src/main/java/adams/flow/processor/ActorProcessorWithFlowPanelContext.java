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
 * ActorProcessorWithFlowPanelContext.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package adams.flow.processor;

import adams.gui.flow.FlowPanel;

/**
 * Interface for actor processors that can have the FlowPanel context set.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface ActorProcessorWithFlowPanelContext
  extends ActorProcessor {

  /**
   * Sets the FlowPanel context.
   *
   * @param value	the context, null if none
   */
  public void setContext(FlowPanel value);

  /**
   * Returns the FlowPanel context.
   *
   * @return		the context, null if not set
   */
  public FlowPanel getContext();
}

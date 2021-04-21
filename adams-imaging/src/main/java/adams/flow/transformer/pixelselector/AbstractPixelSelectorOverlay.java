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
 * AbstractPixelSelectorOverlay.java
 * Copyright (C) 2012-2021 University of Waikato, Hamilton, New Zealand
 */
package adams.flow.transformer.pixelselector;

import adams.core.ShallowCopySupporter;
import adams.core.option.AbstractOptionHandler;
import adams.core.option.OptionUtils;
import adams.data.image.AbstractImageContainer;
import adams.gui.visualization.image.ImageOverlay;
import adams.gui.visualization.image.ImagePanel.PaintPanel;

import java.awt.Graphics;

/**
 * Ancestor for overlays in the pixel selector GUI, making use of the data
 * stored in the report.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractPixelSelectorOverlay
  extends AbstractOptionHandler 
  implements ImageOverlay, ShallowCopySupporter<AbstractPixelSelectorOverlay> {

  /** for serialization. */
  private static final long serialVersionUID = -3880315824020638532L;

  /** the underlying image. */
  protected AbstractImageContainer m_Image;
  
  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    StringBuilder	result;
    Class[]		actions;
    
    result  = new StringBuilder(getGlobalInfo());
    actions = getSuggestedActions();
    if (actions.length > 0) {
      result.append("\n\nSome actions that generate data for this overlay:");
      for (Class action: actions) {
	result.append("\n");
	result.append(action.getName());
      }
    }
    
    return result.toString();
  }

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  protected abstract String getGlobalInfo();

  /**
   * Returns some actions that generate data for this overlay.
   * 
   * @return		the actions
   */
  public abstract Class[] getSuggestedActions();
  
  /**
   * Sets the underlying image.
   * 
   * @param value	the image
   */
  public void setImage(AbstractImageContainer value) {
    m_Image = value;
    reset();
  }
  
  /**
   * Returns the underlying image.
   * 
   * @return		the image
   */
  public AbstractImageContainer getImage() {
    return m_Image;
  }
  
  /**
   * Paints the actual overlay over the image.
   *
   * @param panel	the panel this overlay is for
   * @param g		the graphics context
   */
  protected abstract void doPaintOverlay(PaintPanel panel, Graphics g);
  
  /**
   * Paints the overlay over the image.
   *
   * @param panel	the panel this overlay is for
   * @param g		the graphics context
   */
  public void paintOverlay(PaintPanel panel, Graphics g) {
    if (m_Image == null)
      return;
    if (!m_Image.hasReport())
      return;
    doPaintOverlay(panel, g);
  }

  /**
   * Gets called when the image overlay got added to a paintable panel.
   * <br>
   * Default implementation does nothing.
   *
   * @param panel	the panel it got added to
   */
  public void overlayAdded(PaintPanel panel) {
  }

  /**
   * Gets called when the image overlay got removed from a paintable panel.
   * <br>
   * Default implementation does nothing.
   *
   * @param panel	the panel it got removed from
   */
  public void overlayRemoved(PaintPanel panel) {
  }

  /**
   * Returns a shallow copy of itself.
   *
   * @return		the shallow copy
   */
  public AbstractPixelSelectorOverlay shallowCopy() {
    return shallowCopy(false);
  }

  /**
   * Returns a shallow copy of itself.
   *
   * @param expand	whether to expand variables to their current values
   * @return		the shallow copy
   */
  public AbstractPixelSelectorOverlay shallowCopy(boolean expand) {
    return (AbstractPixelSelectorOverlay) OptionUtils.shallowCopy(this, expand);
  }
  
  /**
   * Cleans up data structures, frees up memory.
   */
  @Override
  public void cleanUp() {
  }
}

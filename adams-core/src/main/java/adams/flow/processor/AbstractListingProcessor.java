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
 * AbstractListingProcessor.java
 * Copyright (C) 2012-2017 University of Waikato, Hamilton, New Zealand
 */
package adams.flow.processor;

import adams.core.Utils;
import adams.core.option.AbstractArgumentOption;
import adams.core.option.AbstractOption;
import adams.core.option.BooleanOption;
import adams.core.option.ClassOption;
import adams.core.option.OptionTraversalPath;
import adams.core.option.OptionTraverser;
import adams.flow.core.Actor;
import adams.gui.dialog.TextPanel;

import java.awt.Component;
import java.awt.Dimension;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Ancestor for processors that list stuff.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public abstract class AbstractListingProcessor
  extends AbstractActorProcessor 
  implements GraphicalOutputProducingProcessor {

  /** for serialization. */
  private static final long serialVersionUID = 5159872442351040225L;
  
  /** for storing the list items. */
  protected List<String> m_List;
  
  /**
   * Checks whether the object is valid and should be added to the list.
   * 
   * @param option	the current option
   * @param obj		the object to check
   * @param path	the traversal path of properties
   * @return		true if valid
   */
  protected abstract boolean isValid(AbstractOption option, Object obj, OptionTraversalPath path);

  /**
   * Returns the string representation of the object that is to be added to the list.
   * <br><br>
   * Default implementation only calls the <code>toString()</code> method.
   * 
   * @param option	the current option
   * @param obj		the object to turn into a string
   * @param path	the traversal path of properties
   * @return		the string representation, null if to ignore the item
   */
  protected String objectToString(AbstractOption option, Object obj, OptionTraversalPath path) {
    return obj.toString();
  }

  /**
   * Returns the string array representation of the object that is to be added to the list.
   * <br><br>
   * Default implementation only calls the <code>toString()</code> method.
   *
   * @param option	the current option
   * @param obj		the object to turn into a string
   * @param path	the traversal path of properties
   * @return		the string representation, null if to ignore the item
   */
  protected String[] objectToStrings(AbstractOption option, Object obj, OptionTraversalPath path) {
    return new String[]{obj.toString()};
  }

  /**
   * Processes the object.
   *
   * @param option	the current option
   * @param obj		the object 
   * @param path	the traversal path of properties
   */
  protected void process(AbstractOption option, Object obj, OptionTraversalPath path) {
    String[]	items;

    if (isValid(option, obj, path)) {
      if (generatesMultipleItems())
	items = objectToStrings(option, obj, path);
      else
	items = new String[]{objectToString(option, obj, path)};
      if ((items == null) || (items.length == 0) || ((items.length == 1) && (items[0] == null)))
	return;
      for (String item: items) {
	if (isUniqueList() && m_List.contains(item))
	  continue;
	m_List.add(item);
      }
    }
  }

  /**
   * Returns whether the list generates a string array per object or not.
   *
   * @return		true if multiple items get generated
   * @see		#objectToString(AbstractOption, Object, OptionTraversalPath)
   * @see		#objectToStrings(AbstractOption, Object, OptionTraversalPath)
   */
  protected boolean generatesMultipleItems() {
    return false;
  }

  /**
   * Returns whether the list should be sorted.
   * 
   * @return		true if the list should get sorted
   */
  protected abstract boolean isSortedList();

  /**
   * Returns whether the list should not contain any duplicates.
   * 
   * @return		true if the list contains no duplicates
   */
  protected abstract boolean isUniqueList();
  
  /**
   * Initializes the list.
   * <br><br>
   * Default implementation creates an empty vector.
   */
  protected void initializeList() {
    m_List = new ArrayList<String>();
  }
  
  /**
   * Performs the actual processing.
   *
   * @param actor	the actor to process (is a copy of original for
   * 			processors implementing ModifyingProcessor)
   * @see		ModifyingProcessor
   */
  @Override
  protected void processActor(Actor actor) {
    initializeList();
    
    actor.getOptionManager().traverse(new OptionTraverser() {
      public void handleClassOption(ClassOption option, OptionTraversalPath path) {
	handleArgumentOption(option, path);
      }
      public void handleBooleanOption(BooleanOption option, OptionTraversalPath path) {
	// ignored
      }
      public void handleArgumentOption(AbstractArgumentOption option, OptionTraversalPath path) {
	Object current = option.getCurrentValue();
	if (option.isMultiple()) {
	  for (int i = 0; i < Array.getLength(current); i++)
	    process(option, Array.get(current, i), path);
	}
	else {
	  process(option, current, path);
	}
      }
      public boolean canHandle(AbstractOption option) {
	return true;
      }
      public boolean canRecurse(Class cls) {
	return true;
      }
      public boolean canRecurse(Object obj) {
	return canRecurse(obj.getClass());
      }
    });
    
    finalizeList();
  }

  /**
   * Finishes up the list, e.g., sorts it.
   * 
   * @see		#isSortedList()
   */
  protected void finalizeList() {
    if (isSortedList())
      Collections.sort(m_List);
  }
  
  /**
   * Returns whether graphical output was generated.
   *
   * @return		true if graphical output was generated
   */
  @Override
  public boolean hasGraphicalOutput() {
    return (m_List.size() > 0);
  }

  /**
   * Returns the header to use in the dialog, i.e., the one-liner that
   * explains the output.
   * 
   * @return		the header, null if no header available
   */
  protected abstract String getHeader();
  
  /**
   * Returns the default sie of the dialog.
   * <br><br>
   * The default is 400x300.
   * 
   * @return		the size
   */
  protected Dimension getDefaultSize() {
    return new Dimension(400, 300);
  }
  
  /**
   * Returns the graphical output that was generated.
   *
   * @return		the graphical output
   */
  @Override
  public Component getGraphicalOutput() {
    TextPanel	result;
    
    result = new TextPanel();
    result.setPreferredSize(getDefaultSize());
    result.setEditable(false);
    result.setUpdateParentTitle(false);
    result.setContent(Utils.flatten(m_List, "\n"));
    result.setInfoText(getHeader());
    
    return result;
  }

  /**
   * Returns the list.
   *
   * @return		the list
   */
  public List<String> getList() {
    return m_List;
  }
}

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
 * AbstractPropertyExtractor.java
 * Copyright (C) 2012-2020 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.visualization.debug.propertyextractor;

import adams.core.ClassLister;
import adams.core.CleanUpHandler;
import adams.core.classmanager.ClassManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Ancestor for property extractors, used for populating the object tree.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractPropertyExtractor
  implements CleanUpHandler {

  /** the cache for object class / extractor relation. */
  protected static Hashtable<Class,List<Class>> m_Cache;

  /** the extractors (classnames) currently available. */
  protected static String[] m_Extractors;

  /** the extractors (classes) currently available. */
  protected static Class[] m_ExtractorClasses;

  /** the current object to inspect. */
  protected Object m_Current;

  static {
    m_Cache            = new Hashtable<>();
    m_Extractors       = null;
    m_ExtractorClasses = null;
  }

  /**
   * Initializes the extractors.
   */
  protected static synchronized void initExtractors() {
    int		i;

    if (m_Extractors != null)
      return;

    m_Extractors       = ClassLister.getSingleton().getClassnames(AbstractPropertyExtractor.class);
    m_ExtractorClasses = new Class[m_Extractors.length];
    for (i = 0; i < m_Extractors.length; i++) {
      try {
	m_ExtractorClasses[i] = ClassManager.getSingleton().forName(m_Extractors[i]);
      }
      catch (Exception e) {
	System.err.println("Failed to instantiate inspection extractor '" + m_Extractors[i] + "': ");
	e.printStackTrace();
      }
    }
  }

  /**
   * Returns the first extractor for the specified object.
   *
   * @param obj		the object to get an extractor for
   * @return		the extractor
   */
  public static synchronized AbstractPropertyExtractor getExtractor(Object obj) {
    return getExtractor(obj.getClass());
  }

  /**
   * Returns the first extractor for the specified class.
   *
   * @param cls		the class to get an extractor for
   * @return		the extractor
   */
  public static synchronized AbstractPropertyExtractor getExtractor(Class cls) {
    return getExtractors(cls).get(0);
  }

  /**
   * Returns all the extractors for the specified object.
   *
   * @param obj		the object to get the extractors for
   * @return		the extractors
   */
  public static synchronized List<AbstractPropertyExtractor> getExtractors(Object obj) {
    return getExtractors(obj.getClass());
  }

  /**
   * Returns all the extractors for the specified class.
   *
   * @param cls		the class to get the extractors for
   * @return		the extractors
   */
  public static synchronized List<AbstractPropertyExtractor> getExtractors(Class cls) {
    List<AbstractPropertyExtractor>	result;
    AbstractPropertyExtractor		extractor;
    int					i;

    result = new ArrayList<>();

    initExtractors();

    // already cached?
    if (m_Cache.containsKey(cls)) {
      try {
	for (Class extrCls: m_Cache.get(cls))
	  result.add((AbstractPropertyExtractor) extrCls.getDeclaredConstructor().newInstance());
	return result;
      }
      catch (Exception e) {
	// ignored
	result = null;
      }
    }

    // find suitable extractor
    for (i = 0; i < m_ExtractorClasses.length; i++) {
      if (m_ExtractorClasses[i] == DefaultPropertyExtractor.class)
	continue;
      try {
	extractor = (AbstractPropertyExtractor) m_ExtractorClasses[i].getDeclaredConstructor().newInstance();
	if (extractor.handles(cls))
	  result.add(extractor);
      }
      catch (Exception e) {
	// ignored
      }
    }

    extractor = new DefaultPropertyExtractor();
    if (!result.contains(extractor))
      result.add(extractor);

    // store in cache
    m_Cache.put(cls, new ArrayList<>());
    for (AbstractPropertyExtractor extr: result)
      m_Cache.get(cls).add(extr.getClass());

    return result;
  }

  /**
   * Checks whether this extractor actually handles this type of class.
   * 
   * @param cls		the class to check
   * @return		true if the extractor handles the object/class
   */
  public abstract boolean handles(Class cls);
  
  /**
   * Sets the current object to inspect.
   * 
   * @param value	the object to inspect
   * @see		#initialize()
   */
  public void setCurrent(Object value) {
    m_Current = value;
    initialize();
  }
  
  /**
   * Returns the current object that is inspected.
   * 
   * @return		the current object
   */
  public Object getCurrent() {
    return m_Current;
  }

  /**
   * Initializes the extractor.
   * <br><br>
   * Default implementation does nothing.
   */
  protected void initialize() {
  }  
  
  /**
   * The number of properties that are available.
   * 
   * @return		the number of properties
   */
  public abstract int size();
  
  /**
   * Checks whether the specified property has a value.
   * 
   * @param index	the index of the property to check
   * @return		true if a value exists
   */
  public boolean hasValue(int index) {
    return (getValue(index) != null);
  }
  
  /**
   * Returns the current value of the specified property.
   * 
   * @param index	the index of the property to retrieve
   * @return		the current value of the property
   */
  public abstract Object getValue(int index);
  
  /**
   * Returns the label for the specified property.
   * 
   * @param index	the index of the property to get the label for
   * @return		the label for the property
   */
  public abstract String getLabel(int index);

  /**
   * Cleans up data structures, frees up memory.
   */
  public void cleanUp() {
    m_Current = null;
  }
}

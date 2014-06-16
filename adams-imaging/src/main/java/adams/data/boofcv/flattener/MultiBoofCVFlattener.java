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
 * MultiBoofCVFlattener.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package adams.data.boofcv.flattener;

import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import adams.data.boofcv.BoofCVImageContainer;

/**
 <!-- globalinfo-start -->
 * Applies multiple flatteners to the same image and merges the generate Instance objects side-by-side. If one of the flatteners should create fewer Instance objects, missing values are used in that case.
 * <p/>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 * 
 * <pre>-field &lt;adams.data.report.Field&gt; [-field ...] (property: fields)
 * &nbsp;&nbsp;&nbsp;The fields to add to the output.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 * <pre>-notes &lt;adams.core.base.BaseString&gt; [-notes ...] (property: notes)
 * &nbsp;&nbsp;&nbsp;The notes to add as attributes to the generated data, eg 'PROCESS INFORMATION'
 * &nbsp;&nbsp;&nbsp;.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 * <pre>-sub-flattener &lt;adams.data.jai.flattener.AbstractBoofCVFlattener&gt; [-sub-flattener ...] (property: subFlatteners)
 * &nbsp;&nbsp;&nbsp;The flatteners to apply to the image.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 * <pre>-prefix &lt;java.lang.String&gt; (property: prefix)
 * &nbsp;&nbsp;&nbsp;The prefix to use for disambiguating the attribute names of the datasets 
 * &nbsp;&nbsp;&nbsp;generated by the flatteners; use '&#64;' for the relation name and '#' for the 
 * &nbsp;&nbsp;&nbsp;1-based index of the flattener; examples are: '#-' and '#-&#64;-'
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class MultiBoofCVFlattener
  extends AbstractBoofCVFlattener {

  /** for serialization. */
  private static final long serialVersionUID = -4136037171201268286L;
  
  /** the flatteners to use. */
  protected AbstractBoofCVFlattener[] m_SubFlatteners;
  
  /** the prefix to use to disambiguate the attributes. */
  protected String m_Prefix;
  
  /** the sub-headers. */
  protected Instances[] m_SubHeaders;
  
  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return 
	"Applies multiple flatteners to the same image and merges the "
	+ "generate Instance objects side-by-side. If one of the "
	+ "flatteners should create fewer Instance objects, missing "
	+ "values are used in that case.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
	    "sub-flattener", "subFlatteners",
	    new AbstractBoofCVFlattener[0]);

    m_OptionManager.add(
	    "prefix", "prefix",
	    "");
  }

  /**
   * Sets the flatteners to use.
   *
   * @param value 	the flatteners
   */
  public void setSubFlatteners(AbstractBoofCVFlattener[] value) {
    m_SubFlatteners = value;
    reset();
  }

  /**
   * Returns the flatteners to use.
   *
   * @return 		the flatteners
   */
  public AbstractBoofCVFlattener[] getSubFlatteners() {
    return m_SubFlatteners;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String subFlattenersTipText() {
    return "The flatteners to apply to the image.";
  }

  /**
   * Sets the prefix to user for disambiguating the attributes.
   *
   * @param value 	the prefix
   */
  public void setPrefix(String value) {
    m_Prefix = value;
    reset();
  }

  /**
   * Returns the prefix to use for disambiguating the attributes.
   *
   * @return 		the prefix
   */
  public String getPrefix() {
    return m_Prefix;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String prefixTipText() {
    return 
	"The prefix to use for disambiguating the attribute names of the "
	+ "datasets generated by the flatteners; use '@' for the relation "
	+ "name and '#' for the 1-based index of the flattener; examples "
	+ "are: '#-' and '#-@-'";
  }

  /**
   * Generates the prefix for the specified flattener.
   * 
   * @param index	the index of the flattener
   * @return		the prefix
   */
  protected String createPrefix(int index) {
    String	result;
    
    result = m_Prefix;
    result = result.replace("@", m_SubHeaders[index].relationName());
    result = result.replace("#", "" + (index + 1));
    
    return result;
  }
  
  /**
   * Creates the header from a template image.
   *
   * @param img		the image to act as a template
   * @return		the generated header
   */
  @Override
  public Instances createHeader(BoofCVImageContainer img) {
    Instances	result;
    int		i;
    int		n;
    String	name;
    
    m_SubHeaders = new Instances[m_SubFlatteners.length];
    for (i = 0; i < m_SubHeaders.length; i++)
      m_SubHeaders[i] = m_SubFlatteners[i].createHeader(img);

    // disambiguate the attribute names
    if (!m_Prefix.isEmpty()) {
      for (i = 0; i < m_SubHeaders.length; i++) {
	for (n = 0; n < m_SubHeaders[i].numAttributes(); n++) {
	  name = createPrefix(i) + m_SubHeaders[i].attribute(n).name();
	  m_SubHeaders[i].renameAttribute(n, name);
	}
      }
    }
    
    if (m_SubHeaders.length > 0) {
      result = new Instances(m_SubHeaders[0], 0);
      for (i = 1; i < m_SubHeaders.length; i++)
	result = Instances.mergeInstances(result, m_SubHeaders[i]);
    }
    else {
      result = new Instances(getClass().getSimpleName(), new ArrayList<Attribute>(), 0);
    }

    return result;
  }

  /**
   * Performs the actual flattening of the image. Will use the previously
   * generated header.
   *
   * @param img		the image to process
   * @return		the generated array
   * @see		#m_Header
   */
  @Override
  public Instance[] doFlatten(BoofCVImageContainer img) {
    Instance[]	result;
    Instances	merged;
    Instances[]	sub;
    Instance[]	flat;
    int		i;
    int		n;
    int		max;
    
    // flatten image
    sub = new Instances[m_SubFlatteners.length];
    for (i = 0; i < m_SubFlatteners.length; i++) {
      flat   = m_SubFlatteners[i].flatten(img);
      sub[i] = new Instances(m_SubHeaders[i]);
      for (n = 0; n < flat.length; n++)
	sub[i].add(flat[n]);
    }
    
    // fill in Instance objects with missing values, if necessary
    max = 0;
    for (i = 0; i < sub.length; i++)
      max = Math.max(max, sub[i].numInstances());
    for (i = 0; i < sub.length; i++) {
      while (sub[i].numInstances() < max)
	sub[i].add(new DenseInstance(m_SubHeaders[i].numAttributes()));
    }

    // merge datasets
    if (sub.length > 0) {
      merged = sub[0];
      for (i = 1; i < sub.length; i++)
	merged = Instances.mergeInstances(merged, sub[i]);
    }
    else {
      flat    = new Instance[1];
      flat[0] = new DenseInstance(m_Header.numAttributes());
      merged  = new Instances(m_Header, 0);
      merged.add(flat[0]);
    }

    // create output array
    result = new Instance[merged.numInstances()];
    for (i = 0; i < merged.numInstances(); i++)
      result[i] = merged.instance(i);
    
    return result;
  }
}

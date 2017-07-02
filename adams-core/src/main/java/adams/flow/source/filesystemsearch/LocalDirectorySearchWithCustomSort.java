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
 * LocalDirectorySearchWithCustomSort.java
 * Copyright (C) 2015-2017 University of Waikato, Hamilton, New Zealand
 */
package adams.flow.source.filesystemsearch;

import adams.core.QuickInfoHelper;
import adams.core.base.BaseRegExp;
import adams.core.io.PlaceholderDirectory;
import adams.core.io.lister.Sorting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 <!-- globalinfo-start -->
 * Searches only for directories, but uses a regular expression to reassemble the name and perform the sorting.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 * 
 * <pre>-directory &lt;adams.core.io.PlaceholderDirectory&gt; (property: directory)
 * &nbsp;&nbsp;&nbsp;The directory to search for directories.
 * &nbsp;&nbsp;&nbsp;default: ${CWD}
 * </pre>
 * 
 * <pre>-max-items &lt;int&gt; (property: maxItems)
 * &nbsp;&nbsp;&nbsp;The maximum number of dirs to return (&lt;= 0 is unlimited).
 * &nbsp;&nbsp;&nbsp;default: -1
 * </pre>
 * 
 * <pre>-regexp &lt;adams.core.base.BaseRegExp&gt; (property: regExp)
 * &nbsp;&nbsp;&nbsp;The regular expression that the dirs must match (empty string matches all
 * &nbsp;&nbsp;&nbsp;).
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 * <pre>-sort-find &lt;adams.core.base.BaseRegExp&gt; (property: sortFind)
 * &nbsp;&nbsp;&nbsp;The regular expression that extracts groups to be used in reassembling the 
 * &nbsp;&nbsp;&nbsp;string for sorting.
 * &nbsp;&nbsp;&nbsp;default: ([\\\\s\\\\S]+)
 * </pre>
 * 
 * <pre>-sort-replace &lt;java.lang.String&gt; (property: sortReplace)
 * &nbsp;&nbsp;&nbsp;The reassmbly string making use of the groups extracted with the regular 
 * &nbsp;&nbsp;&nbsp;expression.
 * &nbsp;&nbsp;&nbsp;default: $0
 * </pre>
 * 
 * <pre>-descending &lt;boolean&gt; (property: sortDescending)
 * &nbsp;&nbsp;&nbsp;If set to true, the directories are sorted in descending manner.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-recursive &lt;boolean&gt; (property: recursive)
 * &nbsp;&nbsp;&nbsp;Whether to search recursively or not.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-max-depth &lt;int&gt; (property: maxDepth)
 * &nbsp;&nbsp;&nbsp;The maximum depth to search in recursive mode (1 = only search directory,
 * &nbsp;&nbsp;&nbsp; -1 = infinite).
 * &nbsp;&nbsp;&nbsp;default: -1
 * </pre>
 * 
 * <pre>-use-relative-paths &lt;boolean&gt; (property: useRelativePaths)
 * &nbsp;&nbsp;&nbsp;If enabled, paths relative to the specified directory.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class LocalDirectorySearchWithCustomSort
  extends AbstractLocalDirectoryListerBasedSearchlet {

  /** for serialization. */
  private static final long serialVersionUID = 3229293554987103145L;

  /**
   * Custom container for sorting the filenames.
   *
   * @author  fracpete (fracpete at waikato dot ac dot nz)
   * @version $Revision$
   */
  public static class SortContainer
    implements Serializable, Comparable<SortContainer> {

    private static final long serialVersionUID = 8905572097502057181L;

    /** the original filename. */
    protected String m_Original;

    /** the reassembled filename used for comparison. */
    protected String m_Comparison;

    /**
     * Initializes the container.
     *
     * @param original		the original filename
     * @param comparison 	the reassembled filename used for comparison
     */
    public SortContainer(String original, String comparison) {
      m_Original   = original;
      m_Comparison = comparison;
    }

    /**
     * Returns the original filename.
     *
     * @return		the filename
     */
    public String getOriginal() {
      return m_Original;
    }

    /**
     * Returns the filename used for comparison.
     *
     * @return		the filename
     */
    public String getComparison() {
      return m_Comparison;
    }

    /**
     * Compares this filename with the other one.
     *
     * @param o		the other container to compare with
     * @return		less than zero is smaller, 0 if equal or greater than
     * 			zero if larger
     */
    @Override
    public int compareTo(SortContainer o) {
      return getComparison().compareTo(o.getComparison());
    }

    /**
     * Returns a short representation of the container.
     *
     * @return		the representation
     */
    @Override
    public String toString() {
      return "comp=" + m_Comparison + ", orig=" + m_Original;
    }
  }

  /** the regular expression for finding the data to use for sorting (groups). */
  protected BaseRegExp m_SortFind;

  /** the string used for reassembling the groups extracted with the regular expression. */
  protected String m_SortReplace;

  /** whether to sort ascending or descending. */
  protected boolean m_Descending;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Searches only for directories, but uses a regular expression to reassemble the name and perform the sorting.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "directory", "directory",
      new PlaceholderDirectory("."));

    m_OptionManager.add(
	    "max-items", "maxItems",
	    -1);

    m_OptionManager.add(
	    "regexp", "regExp",
	    new BaseRegExp(""));

    m_OptionManager.add(
	    "sort-find", "sortFind",
	    new BaseRegExp("([\\s\\S]+)"));

    m_OptionManager.add(
	    "sort-replace", "sortReplace",
	    "$0");

    m_OptionManager.add(
	    "descending", "sortDescending",
	    false);

    m_OptionManager.add(
	    "recursive", "recursive",
	    false);

    m_OptionManager.add(
	    "max-depth", "maxDepth",
	    -1);

    m_OptionManager.add(
	    "use-relative-paths", "useRelativePaths",
	    false);
  }

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_Lister.setListDirs(true);
    m_Lister.setListFiles(false);
  }

  /**
   * Sets the directory to search.
   *
   * @param value	the directory
   */
  public void setDirectory(PlaceholderDirectory value) {
    m_Lister.setWatchDir(value.getAbsolutePath());
  }

  /**
   * Returns the directory to search.
   *
   * @return		the directory.
   */
  public PlaceholderDirectory getDirectory() {
    return new PlaceholderDirectory(m_Lister.getWatchDir());
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String directoryTipText() {
    return "The directory to search for directories.";
  }

  /**
   * Sets the maximum number of items to return.
   *
   * @param value	the maximum number
   */
  public void setMaxItems(int value) {
    m_Lister.setMaxItems(value);
    reset();
  }

  /**
   * Returns the maximum number of items to return.
   *
   * @return		the maximum number
   */
  public int getMaxItems() {
    return m_Lister.getMaxItems();
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String maxItemsTipText() {
    return "The maximum number of dirs to return (<= 0 is unlimited).";
  }

  /**
   * Sets the regular expression for the files/dirs.
   *
   * @param value	the regular expression
   */
  public void setRegExp(BaseRegExp value) {
    m_Lister.setRegExp(value);
    reset();
  }

  /**
   * Returns the regular expression for the files/dirs.
   *
   * @return		the regular expression
   */
  public BaseRegExp getRegExp() {
    return m_Lister.getRegExp();
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String regExpTipText() {
    return "The regular expression that the dirs must match (empty string matches all).";
  }

  /**
   * Sets the regular expression for extracting the groups.
   *
   * @param value	the regular expression
   */
  public void setSortFind(BaseRegExp value) {
    m_SortFind = value;
    reset();
  }

  /**
   * Returns the regular expression for extracting the groups.
   *
   * @return		the regular expression
   */
  public BaseRegExp getSortFind() {
    return m_SortFind;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String sortFindTipText() {
    return "The regular expression that extracts groups to be used in reassembling the string for sorting.";
  }

  /**
   * Sets the reassembly string for generating the sort string.
   *
   * @param value	the string
   */
  public void setSortReplace(String value) {
    m_SortReplace = value;
    reset();
  }

  /**
   * Returns the reassembly string for generating the sort string.
   *
   * @return		the string
   */
  public String getSortReplace() {
    return m_SortReplace;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String sortReplaceTipText() {
    return "The reassmbly string making use of the groups extracted with the regular expression.";
  }

  /**
   * Sets the type of sorting to perform.
   *
   * @param value	the type of sorting
   */
  public void setSorting(Sorting value) {
    m_Lister.setSorting(value);
    reset();
  }

  /**
   * Returns the type of sorting to perform.
   *
   * @return		the type of sorting
   */
  public Sorting getSorting() {
    return m_Lister.getSorting();
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String sortingTipText() {
    return "The type of sorting to perform.";
  }

  /**
   * Sets whether to sort descendingly.
   *
   * @param value	true if sorting in descending order
   */
  public void setSortDescending(boolean value) {
    m_Descending = value;
    reset();
  }

  /**
   * Returns whether to sort descendingly.
   *
   * @return		true if sorting in descending order
   */
  public boolean getSortDescending() {
    return m_Descending;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String sortDescendingTipText() {
    return "If set to true, the directories are sorted in descending manner.";
  }

  /**
   * Sets whether to search recursively.
   *
   * @param value	true if search is recursively
   */
  public void setRecursive(boolean value) {
    m_Lister.setRecursive(value);
    reset();
  }

  /**
   * Returns whether search is recursively.
   *
   * @return		true if search is recursively
   */
  public boolean getRecursive() {
    return m_Lister.getRecursive();
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String recursiveTipText() {
    return "Whether to search recursively or not.";
  }

  /**
   * Sets the maximum depth to search (in recursive mode). 1 = only watch
   * directory, -1 = infinite.
   *
   * @param value	the maximum number of directory levels to traverse
   */
  public void setMaxDepth(int value) {
    m_Lister.setMaxDepth(value);
    reset();
  }

  /**
   * Returns the maximum depth to search (in recursive mode). 1 = only watch
   * directory, -1 = infinite.
   *
   * @return		the maximum number of directory levels to traverse
   */
  public int getMaxDepth() {
    return m_Lister.getMaxDepth();
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String maxDepthTipText() {
    return
        "The maximum depth to search in recursive mode (1 = only search "
       + "directory, -1 = infinite).";
  }

  /**
   * Sets whether to output relative paths.
   *
   * @param value 	true if to output relative paths
   */
  public void setUseRelativePaths(boolean value) {
    m_Lister.setUseRelativePaths(value);
    reset();
  }

  /**
   * Returns whether to output relative paths.
   *
   * @return 		true if to output relative paths
   */
  public boolean getUseRelativePaths() {
    return m_Lister.getUseRelativePaths();
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String useRelativePathsTipText() {
    return "If enabled, paths relative to the specified directory.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String		result;
    List<String>	options;

    result  = QuickInfoHelper.toString(this, "directory", getDirectory());
    result += QuickInfoHelper.toString(this, "sortFind", getSortFind(), ", find: ");
    result += QuickInfoHelper.toString(this, "sortReplace", getSortReplace(), ", replace: ");

    // further options
    options = new ArrayList<>();
    QuickInfoHelper.add(options, QuickInfoHelper.toString(this, "sortDescending", getSortDescending(), "descending"));
    QuickInfoHelper.add(options, QuickInfoHelper.toString(this, "recursive", getRecursive(), "recursive"));
    QuickInfoHelper.add(options, QuickInfoHelper.toString(this, "maxItems", (getMaxItems() > 0 ? getMaxItems() : null), "max="));
    QuickInfoHelper.add(options, QuickInfoHelper.toString(this, "useRelativePaths", getUseRelativePaths(), "relative"));
    result += QuickInfoHelper.flatten(options);

    return result;
  }

  /**
   * Performs the actual search.
   *
   * @return		the search result
   * @throws Exception	if search failed
   */
  @Override
  protected List<String> doSearch() throws Exception {
    List<String>	result;
    List<SortContainer>	sort;

    result = super.doSearch();

    // assemble sort containers
    sort = new ArrayList<>();
    for (String file: result)
      sort.add(new SortContainer(file, file.replaceAll(m_SortFind.getValue(), m_SortReplace)));

    // sort
    Collections.sort(sort);
    if (m_Descending)
      Collections.reverse(sort);

    // assemble result
    result = new ArrayList<>();
    for (SortContainer cont: sort)
      result.add(cont.getOriginal());

    return result;
  }
}

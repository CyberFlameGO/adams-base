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
 * ColumnFinderRangeTest.java
 * Copyright (C) 2013 University of Waikato, Hamilton, New Zealand
 */
package adams.data.spreadsheet.cellfinder;

import adams.core.Range;
import adams.core.base.BaseRegExp;
import adams.data.spreadsheet.SpreadSheetColumnRange;
import adams.data.spreadsheet.columnfinder.ByIndex;
import adams.data.spreadsheet.columnfinder.ByName;
import adams.data.spreadsheet.columnfinder.ColumnFinder;

/**
 * Tests the ColumnFinderRange cell locator.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class ColumnFinderRangeTest
  extends AbstractCellFinderTestCase {

  /**
   * Initializes the test.
   * 
   * @param name	the name of the test
   */
  public ColumnFinderRangeTest(String name) {
    super(name);
  }
  
  /**
   * Returns the filenames (without path) of the input data files to use
   * in the regression test.
   *
   * @return		the filenames
   */
  @Override
  protected String[] getRegressionInputFiles() {
    return new String[]{
	"bolts.csv",
	"bolts.csv",
	"bolts.csv",
	"bolts.csv",
    };
  }

  /**
   * Returns the setups to use in the regression test.
   *
   * @return		the setups
   */
  @Override
  protected CellFinder[] getRegressionSetups() {
    ColumnFinderRange[]	result;
    ColumnFinder	finder;
    
    result    = new ColumnFinderRange[4];
    
    result[0] = new ColumnFinderRange();
    
    result[1] = new ColumnFinderRange();
    finder = new ByName();
    ((ByName) finder).setRegExp(new BaseRegExp("T20BOLT"));
    result[1].setColumnFinder(finder);
    result[1].setRows(new Range("2"));

    result[2] = new ColumnFinderRange();
    finder = new ByIndex();
    ((ByIndex) finder).setColumns(new SpreadSheetColumnRange("3-4"));
    result[2].setColumnFinder(finder);
    result[2].setRows(new Range(Range.ALL));
    
    result[3] = new ColumnFinderRange();
    finder = new ByIndex();
    ((ByIndex) finder).setColumns(new SpreadSheetColumnRange("3-4"));
    result[3].setColumnFinder(finder);
    result[3].setRows(new Range("4-6"));
    
    return result;
  }

}

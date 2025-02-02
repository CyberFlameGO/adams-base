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
 * CommonIDsTest.java
 * Copyright (C) 2021 University of Waikato, Hamilton, New Zealand
 */
package adams.flow.transformer.multispreadsheetoperation;

import adams.env.Environment;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the ArrayStatistic statistic generator.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class CommonIDsTest
  extends AbstractMultiSpreadSheetOperationTestCase {

  /**
   * Initializes the test.
   *
   * @param name	the name of the test
   */
  public CommonIDsTest(String name) {
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
	"iris_with_id-subset1.csv",
	"iris_with_id-subset2.csv",
    };
  }

  /**
   * Returns the setups to use in the regression test.
   *
   * @return the setups
   */
  @Override
  protected AbstractMultiSpreadSheetOperation[] getRegressionSetups() {
    CommonIDs[] result;

    result    = new CommonIDs[2];
    result[0] = new CommonIDs();
    result[1] = new CommonIDs();
    result[1].setInvert(true);

    return result;
  }

  /**
   * Returns a test suite.
   *
   * @return		the test suite
   */
  public static Test suite() {
    return new TestSuite(CommonIDsTest.class);
  }

  /**
   * Runs the test from commandline.
   *
   * @param args	ignored
   */
  public static void main(String[] args) {
    Environment.setEnvironmentClass(Environment.class);
    runTest(suite());
  }
}

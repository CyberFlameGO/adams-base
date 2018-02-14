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
 * StringExpressionTextTest.java
 * Copyright (C) 2018 University of Waikato, Hamilton, New Zealand
 */

package adams.parser;

import adams.core.base.AbstractBaseObjectTestCase;
import adams.env.Environment;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the adams.parser.StringExpressionText class. Run from commandline with: <br><br>
 * java adams.parser.StringExpressionTextTest
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class StringExpressionTextTest
  extends AbstractBaseObjectTestCase<StringExpressionText> {

  /**
   * Initializes the test.
   *
   * @param name	the name of the test
   */
  public StringExpressionTextTest(String name) {
    super(name);
  }

  /**
   * Returns a default base object.
   *
   * @return		the default object
   */
  protected StringExpressionText getDefault() {
    return new StringExpressionText();
  }

  /**
   * Returns a base object initialized with the given string.
   *
   * @param s		the string to initialize the object with
   * @return		the custom object
   */
  protected StringExpressionText getCustom(String s) {
    return new StringExpressionText(s);
  }

  /**
   * Returns the string representing a typical value to parse that doesn't
   * fail.
   *
   * @return		the value
   */
  protected String getTypicalValue() {
    return "find(\"ll\", \"hello world\")";
  }

  /**
   * Returns a test suite.
   *
   * @return		the test suite
   */
  public static Test suite() {
    return new TestSuite(StringExpressionTextTest.class);
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

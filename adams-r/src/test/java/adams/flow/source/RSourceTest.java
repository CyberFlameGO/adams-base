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
 * RSourceTest.java
 * Copyright (C) 2013 University of Waikato, Hamilton, New Zealand
 */

package adams.flow.source;

import junit.framework.Test;
import junit.framework.TestSuite;
import adams.core.option.AbstractArgumentOption;
import adams.env.Environment;
import adams.flow.AbstractRFlowTest;
import adams.flow.control.Flow;
import adams.flow.core.Actor;
import adams.test.TmpFile;

/**
 * Test for RSource actor.
 *
 * @author fracpete
 * @author adams.core.option.FlowJUnitTestProducer (code generator)
 * @version $Revision$
 */
public class RSourceTest
  extends AbstractRFlowTest {

  /**
   * Initializes the test.
   *
   * @param name	the name of the test
   */
  public RSourceTest(String name) {
    super(name);
  }

  /**
   * Called by JUnit before each test method.
   *
   * @throws Exception 	if an error occurs.
   */
  @Override
  protected void setUp() throws Exception {
    super.setUp();

    m_TestHelper.deleteFileFromTmp("dumpfile.txt");
  }

  /**
   * Called by JUnit after each test method.
   *
   * @throws Exception	if tear-down fails
   */
  @Override
  protected void tearDown() throws Exception {
    m_TestHelper.deleteFileFromTmp("dumpfile.txt");

    super.tearDown();
  }

  /**
   * Performs a regression test, comparing against previously generated output.
   */
  public void testRegression() {
    performRegressionTest(
        new TmpFile[]{
          new TmpFile("dumpfile.txt")
        });
  }

  /**
   *
   * Returns a test suite.
   *
   * @return		the test suite
   */
  public static Test suite() {
    return new TestSuite(RSourceTest.class);
  }

  /**
   * Used to create an instance of a specific actor.
   *
   * @return a suitably configured <code>Actor</code> value
   */
  @Override
  public Actor getActor() {
    AbstractArgumentOption    argOption;

    Flow flow = new Flow();

    try {
      argOption = (AbstractArgumentOption) flow.getOptionManager().findByProperty("actors");
      adams.flow.core.Actor[] abstractactor1 = new adams.flow.core.Actor[4];

      // Flow.Rserve
      adams.flow.standalone.Rserve rserve2 = new adams.flow.standalone.Rserve();
      abstractactor1[0] = rserve2;

      // Flow.RSource
      adams.flow.source.RSource rsource3 = new adams.flow.source.RSource();
      argOption = (AbstractArgumentOption) rsource3.getOptionManager().findByProperty("script");
      rsource3.setScript((adams.core.scripting.RScript) argOption.valueOf("set.seed(1)\nx <- sample(1:100, 200, replace=T)\nx2 <- log2(x)"));

      argOption = (AbstractArgumentOption) rsource3.getOptionManager().findByProperty("returnType");
      rsource3.setReturnType((adams.core.RDataType) argOption.valueOf("DoubleArray"));

      abstractactor1[1] = rsource3;

      // Flow.ArrayToSequence
      adams.flow.transformer.ArrayToSequence arraytosequence6 = new adams.flow.transformer.ArrayToSequence();
      abstractactor1[2] = arraytosequence6;

      // Flow.DumpFile
      adams.flow.sink.DumpFile dumpfile7 = new adams.flow.sink.DumpFile();
      argOption = (AbstractArgumentOption) dumpfile7.getOptionManager().findByProperty("outputFile");
      dumpfile7.setOutputFile((adams.core.io.PlaceholderFile) argOption.valueOf("${TMP}/dumpfile.txt"));

      dumpfile7.setAppend(true);

      abstractactor1[3] = dumpfile7;
      flow.setActors(abstractactor1);

      argOption = (AbstractArgumentOption) flow.getOptionManager().findByProperty("flowExecutionListener");
      adams.flow.execution.NullListener nulllistener10 = new adams.flow.execution.NullListener();
      flow.setFlowExecutionListener(nulllistener10);

    }
    catch (Exception e) {
      fail("Failed to set up actor: " + e);
    }

    return flow;
  }

  /**
   * Runs the test from commandline.
   *
   * @param args	ignored
   */
  public static void main(String[] args) {
    Environment.setEnvironmentClass(adams.env.Environment.class);
    runTest(suite());
  }
}


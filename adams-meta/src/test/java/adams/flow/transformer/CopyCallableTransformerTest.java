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
 * CopyCallableTransformerTest.java
 * Copyright (C) 2012-2014 University of Waikato, Hamilton, New Zealand
 */

package adams.flow.transformer;

import junit.framework.Test;
import junit.framework.TestSuite;
import adams.core.option.AbstractArgumentOption;
import adams.env.Environment;
import adams.flow.AbstractFlowTest;
import adams.flow.control.Flow;
import adams.flow.core.Actor;
import adams.test.TmpFile;

/**
 * Test for CopyCallableTransformer actor.
 *
 * @author fracpete
 * @author adams.core.option.FlowJUnitTestProducer (code generator)
 * @version $Revision$
 */
public class CopyCallableTransformerTest
  extends AbstractFlowTest {

  /**
   * Initializes the test.
   *
   * @param name	the name of the test
   */
  public CopyCallableTransformerTest(String name) {
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
    return new TestSuite(CopyCallableTransformerTest.class);
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
      adams.flow.core.Actor[] tmp1 = new adams.flow.core.Actor[5];
      adams.flow.standalone.DeleteFile tmp2 = new adams.flow.standalone.DeleteFile();
      argOption = (AbstractArgumentOption) tmp2.getOptionManager().findByProperty("directory");
      tmp2.setDirectory((adams.core.io.PlaceholderDirectory) argOption.valueOf("${TMP}"));

      argOption = (AbstractArgumentOption) tmp2.getOptionManager().findByProperty("regExp");
      tmp2.setRegExp((adams.core.base.BaseRegExp) argOption.valueOf("dumpfile.txt"));

      tmp1[0] = tmp2;
      adams.flow.standalone.CallableActors tmp5 = new adams.flow.standalone.CallableActors();
      argOption = (AbstractArgumentOption) tmp5.getOptionManager().findByProperty("actors");
      adams.flow.core.Actor[] tmp6 = new adams.flow.core.Actor[2];
      adams.flow.sink.DumpFile tmp7 = new adams.flow.sink.DumpFile();
      argOption = (AbstractArgumentOption) tmp7.getOptionManager().findByProperty("outputFile");
      tmp7.setOutputFile((adams.core.io.PlaceholderFile) argOption.valueOf("${TMP}/dumpfile.txt"));

      tmp7.setAppend(true);

      tmp6[0] = tmp7;
      adams.flow.transformer.MathExpression tmp9 = new adams.flow.transformer.MathExpression();
      argOption = (AbstractArgumentOption) tmp9.getOptionManager().findByProperty("expression");
      tmp9.setExpression((adams.parser.MathematicalExpressionText) argOption.valueOf("X/10"));

      tmp6[1] = tmp9;
      tmp5.setActors(tmp6);

      tmp1[1] = tmp5;
      adams.flow.source.Start tmp11 = new adams.flow.source.Start();
      tmp1[2] = tmp11;
      adams.flow.control.Trigger tmp12 = new adams.flow.control.Trigger();
      argOption = (AbstractArgumentOption) tmp12.getOptionManager().findByProperty("actors");
      adams.flow.core.Actor[] tmp13 = new adams.flow.core.Actor[4];
      adams.flow.source.StringConstants tmp14 = new adams.flow.source.StringConstants();
      argOption = (AbstractArgumentOption) tmp14.getOptionManager().findByProperty("strings");
      adams.core.base.BaseString[] tmp15 = new adams.core.base.BaseString[5];
      tmp15[0] = (adams.core.base.BaseString) argOption.valueOf("1");
      tmp15[1] = (adams.core.base.BaseString) argOption.valueOf("2");
      tmp15[2] = (adams.core.base.BaseString) argOption.valueOf("3");
      tmp15[3] = (adams.core.base.BaseString) argOption.valueOf("4");
      tmp15[4] = (adams.core.base.BaseString) argOption.valueOf("5");
      tmp14.setStrings(tmp15);

      tmp13[0] = tmp14;
      adams.flow.transformer.Convert tmp16 = new adams.flow.transformer.Convert();
      argOption = (AbstractArgumentOption) tmp16.getOptionManager().findByProperty("conversion");
      adams.data.conversion.StringToInt tmp18 = new adams.data.conversion.StringToInt();
      tmp16.setConversion(tmp18);

      tmp13[1] = tmp16;
      adams.flow.transformer.CopyCallableTransformer tmp19 = new adams.flow.transformer.CopyCallableTransformer();
      argOption = (AbstractArgumentOption) tmp19.getOptionManager().findByProperty("callableName");
      tmp19.setCallableName((adams.flow.core.CallableActorReference) argOption.valueOf("MathExpression"));

      tmp13[2] = tmp19;
      adams.flow.sink.CallableSink tmp21 = new adams.flow.sink.CallableSink();
      argOption = (AbstractArgumentOption) tmp21.getOptionManager().findByProperty("callableName");
      tmp21.setCallableName((adams.flow.core.CallableActorReference) argOption.valueOf("DumpFile"));

      tmp13[3] = tmp21;
      tmp12.setActors(tmp13);

      tmp1[3] = tmp12;
      adams.flow.control.Trigger tmp23 = new adams.flow.control.Trigger();
      argOption = (AbstractArgumentOption) tmp23.getOptionManager().findByProperty("name");
      tmp23.setName((java.lang.String) argOption.valueOf("Trigger-1"));

      argOption = (AbstractArgumentOption) tmp23.getOptionManager().findByProperty("actors");
      adams.flow.core.Actor[] tmp25 = new adams.flow.core.Actor[4];
      adams.flow.source.StringConstants tmp26 = new adams.flow.source.StringConstants();
      argOption = (AbstractArgumentOption) tmp26.getOptionManager().findByProperty("strings");
      adams.core.base.BaseString[] tmp27 = new adams.core.base.BaseString[5];
      tmp27[0] = (adams.core.base.BaseString) argOption.valueOf("11");
      tmp27[1] = (adams.core.base.BaseString) argOption.valueOf("21");
      tmp27[2] = (adams.core.base.BaseString) argOption.valueOf("31");
      tmp27[3] = (adams.core.base.BaseString) argOption.valueOf("41");
      tmp27[4] = (adams.core.base.BaseString) argOption.valueOf("51");
      tmp26.setStrings(tmp27);

      tmp25[0] = tmp26;
      adams.flow.transformer.Convert tmp28 = new adams.flow.transformer.Convert();
      argOption = (AbstractArgumentOption) tmp28.getOptionManager().findByProperty("conversion");
      adams.data.conversion.StringToInt tmp30 = new adams.data.conversion.StringToInt();
      tmp28.setConversion(tmp30);

      tmp25[1] = tmp28;
      adams.flow.transformer.CopyCallableTransformer tmp31 = new adams.flow.transformer.CopyCallableTransformer();
      argOption = (AbstractArgumentOption) tmp31.getOptionManager().findByProperty("callableName");
      tmp31.setCallableName((adams.flow.core.CallableActorReference) argOption.valueOf("MathExpression"));

      tmp25[2] = tmp31;
      adams.flow.sink.CallableSink tmp33 = new adams.flow.sink.CallableSink();
      argOption = (AbstractArgumentOption) tmp33.getOptionManager().findByProperty("callableName");
      tmp33.setCallableName((adams.flow.core.CallableActorReference) argOption.valueOf("DumpFile"));

      tmp25[3] = tmp33;
      tmp23.setActors(tmp25);

      tmp1[4] = tmp23;
      flow.setActors(tmp1);

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


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
 * StandalonesTest.java
 * Copyright (C) 2010-2021 University of Waikato, Hamilton, New Zealand
 */

package adams.flow.standalone;

import adams.core.base.BaseRegExp;
import adams.db.JdbcUrl;
import adams.env.Environment;
import adams.flow.AbstractFlowTest;
import adams.flow.control.Flow;
import adams.flow.core.Actor;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the Standalones actor.
 * <br><br>
 * Just uses the WhileLoopTest since it uses a Standalones actor.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class StandalonesTest
  extends AbstractFlowTest {

  /**
   * Initializes the test.
   *
   * @param name	the name of the test
   */
  public StandalonesTest(String name) {
    super(name);
  }

  /**
   * Used to create an instance of a specific actor.
   *
   * @return a suitably configured <code>Actor</code> value
   */
  public Actor getActor() {
    DatabaseConnection dbcon = new DatabaseConnection();
    dbcon.setURL(new JdbcUrl(getDatabaseURL()));
    dbcon.setUser(getDatabaseUser());
    dbcon.setPassword(getDatabasePassword());

    DatabaseCheck dbc = new DatabaseCheck();
    dbc.setRegExp(new BaseRegExp(".*" + getDatabaseURL().replaceAll("\\?.*", "").replaceAll(".*\\/", "") + ".*"));

    Tool tl = new Tool();

    Standalones sg = new Standalones();
    sg.setActors(new Actor[]{
	dbcon,
	dbc,
	tl
    });

    Flow flow = new Flow();
    flow.setActors(new Actor[]{sg});

    return flow;
  }

  /**
   * Returns a test suite.
   *
   * @return		the test suite
   */
  public static Test suite() {
    return new TestSuite(StandalonesTest.class);
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

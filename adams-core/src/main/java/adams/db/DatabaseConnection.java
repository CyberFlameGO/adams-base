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
 * DatabaseConnection.java
 * Copyright (C) 2008-2021 University of Waikato, Hamilton, New Zealand
 *
 */

package adams.db;

import adams.core.base.BasePassword;
import adams.core.option.OptionUtils;
import adams.env.AbstractEnvironment;
import adams.env.DatabaseConnectionDefinition;
import adams.env.Environment;

/**
 * DatabaseConnection manages the interface to the database back-end.
 * Currently set up for MYSQL.
 *
 *  @author  dale (dale at waikato dot ac dot nz)
 */
public class DatabaseConnection
    extends AbstractDatabaseConnection {

  /** for serialization. */
  private static final long serialVersionUID = -3625820307854172417L;

  /** the props file. */
  public final static String FILENAME = "DatabaseConnection.props";

  /** for managing the database connections. */
  private static DatabaseManager<DatabaseConnection> m_DatabaseManager;

  /**
   * Local Database Constructor.
   */
  public DatabaseConnection() {
    super();
  }

  /**
   * Local Database Constructor. Initialise the JDBC driver, and attempt
   * connection to the database specified in the URL, with the given username
   * and password.
   *
   * @param url         the JDBC URL
   * @param user        the user to connect with
   * @param password    the password for the user
   */
  public DatabaseConnection(String url, String user, BasePassword password) {
    super(url, user, password);
  }

  /**
   * Creates a new instance of the environment object that we require.
   *
   * @return		the instance
   */
  protected AbstractEnvironment createEnvironment() {
    return new Environment();
  }

  /**
   * Returns the properties key to use for retrieving the properties.
   *
   * @return		the key
   */
  @Override
  protected String getDefinitionKey() {
    return DatabaseConnectionDefinition.KEY;
  }

  /**
   * Returns the database manager, instantiates it if necessary.
   *
   * @return		the manager
   */
  protected static synchronized DatabaseManager<DatabaseConnection> getDatabaseManager() {
    if (m_DatabaseManager == null) {
      m_DatabaseManager = new DatabaseManager<>("adams");
      DatabaseConnection dbcon = new DatabaseConnection();
      m_DatabaseManager.setDefault(DatabaseConnection.getSingleton(dbcon.getURL(), dbcon.getUser(), dbcon.getPassword()));
    }
    return m_DatabaseManager;
  }

  /**
   * Returns the global database connection object. If not instantiated yet, it
   * will automatically try to connect to the database server.
   *
   * @param url		the database URL
   * @param user	the database user
   * @param password	the database password
   * @return		the singleton
   */
  public static synchronized DatabaseConnection getSingleton(String url, String user, BasePassword password) {
    if (!getDatabaseManager().has(url, user, password)) {
      getDatabaseManager().add(new DatabaseConnection(url, user, password));
    }
    else {
      if (!getDatabaseManager().get(url, user, password).isConnected()) {
	try {
	  getDatabaseManager().get(url, user, password).connect();
	}
	catch (Exception e) {
	  e.printStackTrace();
	}
      }
    }

    return getDatabaseManager().get(url, user, password);
  }

  /**
   * Returns the global database connection object. If not instantiated yet, it
   * can automatically try to connect to the database server, depending on the
   * default in the props file (SUFFIX_CONNECTONSTARTUP).
   *
   * @return		the singleton
   * @see		#getConnectOnStartUp()
   */
  public static synchronized DatabaseConnection getSingleton() {
    return getDatabaseManager().getDefault();
  }

  /**
   * Returns the commandline string.
   *
   * @return		 the commandline
   */
  @Override
  public String toCommandLine() {
    return OptionUtils.getCommandLine(this);
  }
}

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
 * HTMLRequestResult.java
 * Copyright (C) 2015-2017 University of Waikato, Hamilton, NZ
 */

package adams.flow.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Container for storing the results of HTML requests.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class HTMLRequestResult
  extends AbstractContainer {

  private static final long serialVersionUID = -2153844706336105272L;

  /** the identifier for the numeric status code. */
  public final static String VALUE_STATUSCODE = "Status code";

  /** the identifier for the status message. */
  public final static String VALUE_STATUSMESSAGE = "Status message";

  /** the identifier for the result body (HTML). */
  public final static String VALUE_BODY = "Body";

  /** the identifier for the cookies (if any; Map). */
  public final static String VALUE_COOKIES = "Cookies";

  /**
   * Default constructor.
   */
  public HTMLRequestResult() {
    super();
    store(VALUE_STATUSCODE, 200);
    store(VALUE_STATUSMESSAGE, "OK");
    store(VALUE_BODY, "");
  }

  /**
   * Initializes the container with the status code and body.
   *
   * @param statusCode		the status code
   * @param statusMessage	the status message
   * @param body		the body (HTML)
   */
  public HTMLRequestResult(int statusCode, String statusMessage, String body) {
    this(statusCode, statusMessage, body, null);
  }

  /**
   * Initializes the container with the status code, body and cookies.
   *
   * @param statusCode	the status code
   * @param statusMessage	the status message
   * @param body	the body (HTML)
   * @param cookies	the cookies, can be null
   */
  public HTMLRequestResult(int statusCode, String statusMessage, String body, Map<String,String> cookies) {
    this();
    store(VALUE_STATUSCODE, statusCode);
    store(VALUE_STATUSMESSAGE, statusMessage);
    store(VALUE_BODY, body);
    store(VALUE_COOKIES, cookies);
  }

  /**
   * Initializes the help strings.
   */
  protected void initHelp() {
    super.initHelp();

    addHelp(VALUE_STATUSCODE, "HTTP status code", Integer.class);
    addHelp(VALUE_STATUSMESSAGE, "HTTP status message", String.class);
    addHelp(VALUE_BODY, "HTML of the request", String.class);
    addHelp(VALUE_COOKIES, "cookie mappings (String/String)", Map.class);
  }

  /**
   * Returns all value names that can be used (theoretically).
   *
   * @return		enumeration over all possible value names
   */
  @Override
  public Iterator<String> names() {
    List<String> result;

    result = new ArrayList<>();

    result.add(VALUE_STATUSCODE);
    result.add(VALUE_STATUSMESSAGE);
    result.add(VALUE_BODY);
    result.add(VALUE_COOKIES);

    return result.iterator();
  }

  /**
   * Checks whether the setup of the container is valid.
   *
   * @return		true if all the necessary values are available
   */
  @Override
  public boolean isValid() {
    return (hasValue(VALUE_STATUSCODE) && hasValue(VALUE_BODY));
  }
}

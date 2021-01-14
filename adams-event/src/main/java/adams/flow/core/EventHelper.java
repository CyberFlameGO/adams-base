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
 * EventHelper.java
 * Copyright (C) 2012-2018 University of Waikato, Hamilton, New Zealand
 */

package adams.flow.core;

import adams.core.Properties;
import adams.core.logging.LoggingObject;
import adams.flow.control.AbstractDirectedControlActor;
import adams.flow.standalone.Events;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for events.
 *
 * You can place "quartz.properties" or "quartz.props" on the classpath to
 * customize the settings of the scheduler.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class EventHelper
  extends LoggingObject {

  /** for serialization. */
  private static final long serialVersionUID = -763479272812116920L;

  /** the scheduler factory. */
  protected static transient Map<Integer,StdSchedulerFactory> m_SchedulerFactory;

  /**
   * Checks a control actor's children whether they contain the event
   * that we're looking for.
   *
   * @param group	the group to check
   * @param name	the name of the event
   * @return		the event or null if not found
   */
  public Actor findEvent(ActorHandler group, EventReference name) {
    Actor			result;
    int				i;
    Events			global;
    int				index;
    ExternalActorHandler	external;

    result = null;

    for (i = 0; i < group.size(); i++) {
      if (group.get(i) instanceof Events) {
	global = (Events) group.get(i);
	index  = global.indexOf(name.toString());
	if (index > -1) {
	  result = global.get(index);
	  break;
	}
      }
      else if (group.get(i) instanceof ExternalActorHandler) {
	external = (ExternalActorHandler) group.get(i);
	if (external.getExternalActor() instanceof ActorHandler) {
	  result = findEvent((ActorHandler) external.getExternalActor(), name);
	  if (result != null)
	    break;
	}
      }
    }

    return result;
  }

  /**
   * Tries to find the event referenced by its global name.
   *
   * @param root	the root to search in
   * @param name	the name of the event
   * @return		the event or null if not found
   */
  public Actor findEvent(Actor root, EventReference name) {
    Actor	result;

    result = null;

    if (root == null) {
      getLogger().severe("No root container found!");
    }
    else if (!(root instanceof AbstractDirectedControlActor)) {
      getLogger().severe(
	  "Root is not a container ('" + root.getFullName() + "'/"
	  + root.getClass().getName() + ")!");
      root = null;
    }

    if (root != null)
      result = findEvent((ActorHandler) root, name);

    return result;
  }

  /**
   * Tries to find the referenced event. First all possible actor
   * handlers are located recursively (up to the root) that allow also
   * singletons. This list of actors is then searched for the event.
   *
   * @param actor	the actor to start from
   * @param name	the name of the event
   * @return		the event or null if not found
   * @see		ActorUtils#findActorHandlers(Actor, boolean)
   */
  public Actor findEventRecursive(Actor actor, EventReference name) {
    Actor		result;
    List<ActorHandler>	handlers;
    int			i;

    result   = null;
    handlers = ActorUtils.findActorHandlers(actor, true);
    for (i = 0; i < handlers.size(); i++) {
      result = findEvent(handlers.get(i), name);
      if (result != null)
	break;
    }

    return result;
  }

  /**
   * Synchronized access for the default scheduler.
   *
   * @param flowID 		the flow ID is the ID of the scheduler
   * @return			the default scheduler
   * @throws SchedulerException	if scheduler cannot be initialized
   */
  public static synchronized Scheduler getDefaultScheduler(int flowID) throws SchedulerException {
    Properties		props;
    Properties		defaultProps;
    Properties		userProps1;
    Properties		userProps2;

    if (m_SchedulerFactory == null)
      m_SchedulerFactory = new HashMap<>();

    if (!m_SchedulerFactory.containsKey(flowID)) {
      defaultProps = new Properties();
      Properties.loadFromResource(defaultProps, "org/quartz/quartz.properties");
      userProps1 = new Properties(defaultProps);
      Properties.loadFromResource(userProps1, "quartz.properties");
      userProps2 = new Properties(userProps1);
      Properties.loadFromResource(userProps2, "quartz.props");
      props = userProps2;

      // disable update check
      props.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
      props.setProperty("org.quartz.scheduler.instanceName", "QuartzScheduler-" + flowID);

      m_SchedulerFactory.put(flowID, new StdSchedulerFactory(props));
    }

    return m_SchedulerFactory.get(flowID).getScheduler();
  }
}

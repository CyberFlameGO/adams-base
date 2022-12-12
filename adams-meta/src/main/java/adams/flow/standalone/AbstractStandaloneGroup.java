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
 * AbstractStandaloneGroup.java
 * Copyright (C) 2014-2022 University of Waikato, Hamilton, New Zealand
 */
package adams.flow.standalone;

import adams.core.Utils;
import adams.core.Variables;
import adams.flow.core.Actor;
import adams.flow.core.ActorExecution;
import adams.flow.core.ActorHandler;
import adams.flow.core.ActorHandlerInfo;
import adams.flow.core.ActorUtils;
import nz.ac.waikato.cms.locator.ClassLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * Ancestor for fixed-sized groups.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @param <T> the type of sub-actor
 */
public abstract class AbstractStandaloneGroup<T extends Actor>
  extends AbstractStandalone
  implements StandaloneGroup<T> {

  /** for serialization. */
  private static final long serialVersionUID = -739244942139022557L;

  /** the actors of the fixed group. */
  protected List<T> m_Actors;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_Actors = getDefaultActors();
    updateParent();
  }

  /**
   * Returns the list of default actors.
   *
   * @return		the default actors
   */
  protected abstract List<T> getDefaultActors();

  /**
   * Returns the classes that the flow editor uses for filtering.
   *
   * @return		the classes
   */
  protected Class[] getActorFilter() {
    return new Class[]{StandaloneGroupItem.class};
  }

  /**
   * Returns some information about the actor handler, e.g., whether it can
   * contain standalones and the actor execution.
   *
   * @return		the info
   */
  @Override
  public ActorHandlerInfo getActorHandlerInfo() {
    return new ActorHandlerInfo()
      .allowStandalones(true)
      .allowSource(false)
      .actorExecution(ActorExecution.UNDEFINED)
      .forwardsInput(false)
      .restrictions(getActorFilter());
  }

  /**
   * Sets the parent of this actor, e.g., the group it belongs to.
   *
   * @param value	the new parent
   */
  @Override
  public void setParent(Actor value) {
    super.setParent(value);
    updateParent();
  }

  /**
   * Updates the parent of all actors in this group.
   */
  protected void updateParent() {
    int		i;

    for (i = 0; i < size(); i++) {
      get(i).setParent(null);
      get(i).setParent(this);
    }
  }

  /**
   * Checks the actors whether they are of the correct type.
   *
   * @param actors	the actor to check
   * @return		null if OK, otherwise the error message
   */
  protected String checkActors(Actor[] actors) {
    int		i;
    String	msg;

    for (i = 0; i < actors.length; i++) {
      msg = checkActor(actors[i], i);
      if (msg != null)
	return msg;
    }

    return null;
  }

  /**
   * Checks the actor whether it is of the correct type.
   *
   * @param actor	the actor to check
   * @return		null if OK, otherwise the error message
   */
  protected String checkActor(Actor actor) {
    return checkActor(actor, -1);
  }

  /**
   * Checks the actor whether it is of the correct type.
   *
   * @param actor	the actor to check
   * @param index	the index of actor, ignored if -1
   * @return		null if OK, otherwise the error message
   */
  protected String checkActor(Actor actor, int index) {
    String	result;
    Class[]	accepted;
    boolean	found;

    result   = null;
    accepted = getActorFilter();
    found    = false;

    for (Class cls: accepted) {
      if (ClassLocator.matches(cls, actor.getClass())) {
	found = true;
	break;
      }
    }

    if (!found)
      result = "Actor #" + (index+1) + " (" + Utils.classToString(actor) + ") is not of type: " + Utils.classesToString(accepted);

    return result;
  }

  /**
   * Returns the size of the group.
   *
   * @return		the size, always 2
   */
  @Override
  public int size() {
    return m_Actors.size();
  }

  /**
   * Sets the actors to use.
   *
   * @param value	the actors
   */
  protected void setActors(Actor[] value) {
    String	msg;

    msg = checkActors(value);
    if (msg != null) {
      getLogger().warning(msg);
      return;
    }

    m_Actors.clear();
    for (Actor actor: value) {
      m_Actors.add((T) actor);
      ActorUtils.uniqueName(actor, this, m_Actors.size() - 1);
    }
    reset();
    updateParent();
  }

  /**
   * Returns the actors to use.
   *
   * @return		the actors
   */
  protected Actor[] getActors() {
    return m_Actors.toArray(new Actor[0]);
  }

  /**
   * Returns the actor at the given position.
   *
   * @param index	the position
   * @return		the actor
   */
  @Override
  public Actor get(int index) {
    return m_Actors.get(index);
  }

  /**
   * Sets the actor at the given position.
   *
   * @param index	the position
   * @param actor	the actor to set at this position
   * @return		null if everything is fine, otherwise the error
   */
  @Override
  public String set(int index, Actor actor) {
    String 	result;

    result = checkActor(actor, index);
    if (result == null) {
      ActorUtils.uniqueName(actor, this, index);
      m_Actors.set(index, (T) actor);
      reset();
      updateParent();
    }
    else {
      getLogger().severe(result);
    }

    return result;
  }

  /**
   * Returns the index of the actor.
   *
   * @param actor	the name of the actor to look for
   * @return		the index of -1 if not found
   */
  @Override
  public int indexOf(String actor) {
    int		result;
    int		i;

    result = -1;

    for (i = 0; i < m_Actors.size(); i++) {
      if (m_Actors.get(i).getName().equals(actor)) {
	result = i;
	break;
      }
    }

    return result;
  }

  /**
   * Performs checks on the "sub-actors".
   *
   * @return		null if everything is fine, otherwise the error
   */
  @Override
  public abstract String check();

  /**
   * Returns the first non-skipped actor.
   *
   * @return		the first 'active' actor, null if none available
   */
  @Override
  public Actor firstActive() {
    Actor	result;
    int		i;

    result = null;

    for (i = 0; i < m_Actors.size(); i++) {
      if (!m_Actors.get(i).getSkip()) {
	result = m_Actors.get(i);
	break;
      }
    }

    return result;
  }

  /**
   * Returns the last non-skipped actor.
   *
   * @return		the last 'active' actor, null if none available
   */
  @Override
  public Actor lastActive() {
    Actor	result;
    int		i;

    result = null;

    for (i = m_Actors.size() - 1; i >= 0; i--) {
      if (!m_Actors.get(i).getSkip()) {
	result = m_Actors.get(i);
	break;
      }
    }

    return result;
  }

  /**
   * Finds the actor with the specified name recursively.
   *
   * @param name	the name of the actor
   * @return		the actor, null if not found
   */
  public Actor find(String name) {
    Actor	result;

    result = null;

    for (Actor actor: m_Actors) {
      if (actor.getSkip())
	continue;
      if (actor.getName().equals(name)) {
	result = actor;
	break;
      }
      if (actor instanceof AbstractStandaloneGroup) {
	result = ((AbstractStandaloneGroup) actor).find(name);
	if (result != null)
	  break;
      }
    }

    return result;
  }

  /**
   * Checks whether an actor of this type is present as a default (exact match).
   *
   * @param actor	the actor type to check
   * @return		true if such an actor is present
   */
  public boolean hasActor(Class actor) {
    boolean	result;
    int		i;

    result = false;
    for (i = 0; i < size(); i++) {
      if (get(i).getClass().equals(actor) && !get(i).getSkip()) {
	result = true;
	break;
      }
    }

    return result;
  }

  /**
   * Returns a list of actors comprising of the requested actor type
   * (exact match).
   *
   * @param actor	the actor type too look here
   * @param <A>		the type
   * @return		the list of actors that were located
   */
  public <A extends Actor> List<A> getActors(Class<A> actor) {
    List<A>	result;
    int		i;

    result = new ArrayList<>();
    for (i = 0; i < size(); i++) {
      if (get(i).getClass().equals(actor) && !get(i).getSkip()) {
        result.add((A) get(i));
        // TODO was: break;
      }
    }

    return result;
  }

  /**
   * Checks whether an actor of this type is present as a default (exact match).
   *
   * @param parent 	the parent the actor needs to be below (assumed to be ActorHandler, uses first occurrence)
   * @param actor	the actor type to check
   * @return		true if such an actor is present
   */
  public boolean hasActorBelow(Class parent, Class actor) {
    boolean		result;
    int			i;
    List<Actor>		parents;
    ActorHandler	p;

    result = false;
    if (hasActor(parent)) {
      parents = getActors(parent);
      if (parents.get(0) instanceof ActorHandler) {
        p = (ActorHandler) parents.get(0);
	for (i = 0; i < p.size(); i++) {
	  if (p.get(i).getClass().equals(actor) && !p.get(i).getSkip()) {
	    result = true;
	    break;
	  }
	}
      }
    }

    return result;
  }

  /**
   * Returns a list of actors comprising of the requested actor type
   * (exact match).
   *
   * @param parent	the parent the actors need to be below (assumed to be ActorHandler, uses first occurrence)
   * @param actor	the actor type too look here
   * @param <A>		the type
   * @return		the list of actors that were located
   */
  public <A extends Actor> List<A> getActorsBelow(Class parent, Class<A> actor) {
    List<A>		result;
    int			i;
    List<Actor>		parents;
    ActorHandler	p;

    result  = new ArrayList<>();
    parents = getActors(parent);
    if ((parents.size() > 0) && (parents.get(0) instanceof ActorHandler)) {
      p = (ActorHandler) parents.get(0);
      for (i = 0; i < p.size(); i++) {
	if (p.get(i).getClass().equals(actor) && !p.get(i).getSkip())
	  result.add((A) p.get(i));
      }
    }

    return result;
  }

  /**
   * Updates the Variables instance in use.
   * <br><br>
   * Use with caution!
   *
   * @param value	the instance to use
   */
  @Override
  protected void forceVariables(Variables value) {
    int		i;

    super.forceVariables(value);

    for (i = 0; i < size(); i++) {
      if (!get(i).getSkip())
	get(i).setVariables(value);
    }
  }

  /**
   * Initializes the item for flow execution.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  public String setUp() {
    String	result;
    int		i;

    result = super.setUp();

    if (result == null)
      result = check();

    if (result == null) {
      for (i = 0; i < m_Actors.size(); i++) {
	if (m_Actors.get(i).getSkip())
	  continue;
	result = checkActor(m_Actors.get(i), i);
	if (result == null)
	  result = m_Actors.get(i).setUp();
	if (result != null) {
	  result = m_Actors.get(i).getFullName() + ": " + result;
	  break;
	}
      }
    }

    return result;
  }

  /**
   * Executes the flow item.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  protected abstract String doExecute();

  /**
   * Stops the processing of tokens without stopping the flow.
   */
  public void flushExecution() {
    int		i;

    for (i = 0; i < m_Actors.size(); i++) {
      if (m_Actors.get(i).getSkip())
	continue;
      if (m_Actors.get(i) instanceof ActorHandler)
	((ActorHandler) m_Actors.get(i)).flushExecution();
    }
  }

  /**
   * Stops the execution. No message set.
   */
  @Override
  public void stopExecution() {
    int		i;

    for (i = 0; i < m_Actors.size(); i++) {
      if (isLoggingEnabled())
	getLogger().info("Stopping " + (i+1) + "/" + m_Actors.size());
      if (m_Actors.get(i).getSkip()) {
	if (isLoggingEnabled())
	  getLogger().info("Skipped " + (i+1) + "/" + m_Actors.size());
	continue;
      }
      m_Actors.get(i).stopExecution();
      if (isLoggingEnabled())
	getLogger().info("Stopped " + (i+1) + "/" + m_Actors.size());
    }

    super.stopExecution();
  }

  /**
   * Cleans up after the execution has finished. Graphical output is left
   * untouched.
   */
  @Override
  public void wrapUp() {
    int		i;

    for (i = 0; i < m_Actors.size(); i++) {
      if (isLoggingEnabled())
	getLogger().info("Wrapping up " + (i+1) + "/" + m_Actors.size());
      if (m_Actors.get(i).getSkip()) {
	if (isLoggingEnabled())
	  getLogger().info("Skipped " + (i+1) + "/" + m_Actors.size());
	continue;
      }
      m_Actors.get(i).wrapUp();
      if (isLoggingEnabled())
	getLogger().info("Wrapped up " + (i+1) + "/" + m_Actors.size());
    }

    super.wrapUp();
  }

  /**
   * Cleans up after the execution has finished. Also removes graphical
   * components.
   */
  @Override
  public void cleanUp() {
    int		i;

    for (i = 0; i < m_Actors.size(); i++) {
      if (isLoggingEnabled())
	getLogger().info("Cleaning up " + (i+1) + "/" + m_Actors.size());
      if (m_Actors.get(i).getSkip()) {
	if (isLoggingEnabled())
	  getLogger().info("Skipped " + (i+1) + "/" + m_Actors.size());
	continue;
      }
      m_Actors.get(i).cleanUp();
      if (isLoggingEnabled())
	getLogger().info("Cleaned up " + (i+1) + "/" + m_Actors.size());
    }

    super.cleanUp();
  }
}

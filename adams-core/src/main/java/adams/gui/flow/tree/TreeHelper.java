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
 * TreeHelper.java
 * Copyright (C) 2014-2016 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.flow.tree;

import adams.core.MessageCollection;
import adams.core.Utils;
import adams.core.option.OptionUtils;
import adams.flow.core.Actor;
import adams.flow.core.ActorHandler;
import adams.flow.core.ActorPath;

import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for flow tree related stuff.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class TreeHelper {

  /**
   * Converts the path to a node (the last component in a path).
   *
   * @param path	the path to the actor
   * @return		the node
   */
  public static Node pathToNode(TreePath path) {
    if (path == null)
      return null;

    return (Node) path.getLastPathComponent();
  }

  /**
   * Converts the paths to actors (the last component in a path).
   *
   * @param paths	the paths to the actors
   * @return		the actors
   */
  public static Node[] pathsToNodes(TreePath[] paths) {
    Node[]	result;
    int		i;

    if (paths == null)
      return null;
    
    result = new Node[paths.length];
    for (i = 0; i < paths.length; i++)
      result[i] = (Node) paths[i].getLastPathComponent();

    return result;
  }

  /**
   * Converts the path to an actor (the last component in a path).
   * Does not return the "full" actor.
   *
   * @param path	the path to the actor
   * @return		the actor
   */
  public static Actor pathToActor(TreePath path) {
    return pathToActor(path, false);
  }

  /**
   * Converts the path to an actor (the last component in a path).
   * "Full" actor means including (potential) sub-tree.
   *
   * @param path	the path to the actor
   * @param full	whether to return the full actor
   * @return		the actor
   */
  public static Actor pathToActor(TreePath path, boolean full) {
    Actor	result;
    Node	node;
    
    if (path == null)
      return null;
    
    node = pathToNode(path);
    if (full)
      result = node.getFullActor();
    else
      result = node.getActor();
    
    return result;
  }

  /**
   * Converts the paths to actors (the last component in a path).
   * Does not return the "full" actor.
   *
   * @param paths	the paths to the actors
   * @return		the actors
   */
  public static Actor[] pathsToActors(TreePath[] paths) {
    return pathsToActors(paths, false);
  }

  /**
   * Converts the paths to actors (the last component in a path).
   * "Full" actor means including (potential) sub-tree.
   *
   * @param paths	the paths to the actors
   * @param full	whether to return the full actor
   * @return		the actors
   */
  public static Actor[] pathsToActors(TreePath[] paths, boolean full) {
    Actor[]	result;
    Node[]	nodes;
    int		i;

    if (paths == null)
      return null;
    
    result = new Actor[paths.length];
    nodes  = pathsToNodes(paths);
    for (i = 0; i < nodes.length; i++) {
      if (full)
	result[i] = nodes[i].getFullActor();
      else
	result[i] = nodes[i].getActor();
    }

    return result;
  }

  /**
   * Turns a {@link TreePath} into a {@link ActorPath}.
   *
   * @param path	the path to convert
   * @return		the generated path
   */
  public static ActorPath treePathToActorPath(TreePath path) {
    Object[]	parts;
    String[]	names;
    int		i;

    parts = path.getPath();
    names = new String[parts.length];
    for (i = 0; i < parts.length; i++)
      names[i] = ((Node) parts[i]).getActor().getName();

    return new ActorPath(names);
  }

  /**
   * Builds the tree from the actor commandlines.
   *
   * @param actors	the commandlines with indentation
   * @param root	the root node
   * @param errors	for storing any errors
   */
  protected static void buildTree(List<String> actors, Node root, MessageCollection errors) {
    int		level;
    int		index;
    String	cmdline;
    Actor	actor;
    Node 	previous;
    Node	node;
    Node	parent;

    previous = root;

    for (index = 1; index < actors.size(); index++) {
      cmdline = actors.get(index);

      // determine level
      level = 0;
      while (level < cmdline.length() && cmdline.charAt(level) == ' ')
	level++;

      try {
	actor = (Actor) OptionUtils.forCommandLine(Actor.class, actors.get(index).trim());
	node = new Node(previous.getOwner(), actor);
      }
      catch (Exception e) {
	errors.add("Failed to parse actor: " + actors.get(index) + "\n" + Utils.throwableToString(e));
	return;
      }

      // sibling
      if (level == previous.getLevel()) {
	((Node) previous.getParent()).add(node);
      }
      // child of some parent node
      else if (level < previous.getLevel()) {
	parent = previous;
	while (level < parent.getLevel())
	  parent = (Node) parent.getParent();
	((Node) parent.getParent()).add(node);
      }
      // child
      else {
	previous.add(node);
      }

      previous = node;
    }
  }

  /**
   * Builds the tree from the nested commandlines.
   *
   * @param actors	the nested commandlines
   * @return		the root node, null if failed to build
   */
  public static Node buildTree(List<String> actors) {
    return buildTree(actors, new MessageCollection());
  }

  /**
   * Builds the tree from the nested commandlines.
   *
   * @param actors	the nested commandlines
   * @param errors	for storing any errors
   * @return		the root node, null if failed to build
   */
  public static Node buildTree(List<String> actors, MessageCollection errors) {
    Actor	actor;
    Node	root;

    if (actors.size() == 0)
      return null;

    try {
      actor = (Actor) OptionUtils.forCommandLine(Actor.class, actors.get(0).trim());
      root  = new Node(null, actor);
      buildTree(actors, root, errors);
      return root;
    }
    catch (Exception e) {
      errors.add("Failed to parse actor: " + actors.get(0) + "\n" + Utils.throwableToString(e));
      return null;
    }
  }

  /**
   * Builds the tree with the given root.
   *
   * @param root	the root actor, can be null
   * @return		the root node
   */
  public static Node buildTree(Actor root) {
    return buildTree(null, root, true);
  }

  /**
   * Builds the tree recursively.
   *
   * @param parent	the parent to add the actor to
   * @param actor	the actor to add
   * @param append	whether to append the sub-tree to the parent or just
   * 			return it (recursive calls always append the sub-tree!)
   * @return		the generated node
   */
  public static Node buildTree(Node parent, Actor actor, boolean append) {
    return buildTree(parent, new Actor[]{actor}, append)[0];
  }

  /**
   * Builds the tree recursively.
   *
   * @param parent	the parent to add the actor to
   * @param actors	the actors to add
   * @param append	whether to append the sub-tree to the parent or just
   * 			return it (recursive calls always append the sub-tree!)
   * @return		the generated nodes
   */
  protected static Node[] buildTree(final Node parent, Actor[] actors, boolean append) {
    final Node[]	result;
    int			n;
    int			i;

    result = new Node[actors.length];
    for (n = 0; n < actors.length; n++) {
      result[n] = new Node((parent != null) ? parent.getOwner() : null, actors[n]);

      if (actors[n] instanceof ActorHandler) {
	for (i = 0; i < ((ActorHandler) actors[n]).size(); i++)
	  buildTree(result[n], ((ActorHandler) actors[n]).get(i), true);
      }
    }

    if ((parent != null) && append) {
      for (Node node : result)
	parent.add(node);
    }

    return result;
  }

  /**
   * Inserts blanks at the start of the string.
   *
   * @param s		the string to process
   * @param numBlanks	the number of blanks to insert
   * @return		the processed string
   */
  protected static String indent(String s, int numBlanks) {
    StringBuilder 	result;
    int			i;

    result = new StringBuilder();
    for (i = 0; i < numBlanks; i++)
      result.append(" ");

    result.append(s);

    return result.toString();
  }

  /**
   * Adds the node and its children to the list of commandlines.
   *
   * @param node      	the node to add
   * @param cmdlines	the command lines to add to
   */
  protected static void getCommandLines(Node node, List<String> cmdlines) {
    int		i;

    cmdlines.add(indent(node.getCommandLine(), node.getLevel()));
    for (i = 0; i < node.getChildCount(); i++)
      getCommandLines((Node) node.getChildAt(i), cmdlines);
  }

  /**
   * Returns the nested commandlines. Indentation in blanks represents
   * nesting level.
   *
   * @param root	the root node
   * @return		the tree as nested commandlines
   */
  public static List<String> getCommandLines(Node root) {
    List<String>	result;

    result = new ArrayList<>();
    getCommandLines(root, result);

    return result;
  }
}

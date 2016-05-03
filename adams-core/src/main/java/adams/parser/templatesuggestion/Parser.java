
//----------------------------------------------------
// The following code was generated by CUP v0.11a beta 20060608
// Tue May 03 13:11:58 NZST 2016
//----------------------------------------------------

package adams.parser.templatesuggestion;

import adams.core.ClassLocator;
import adams.flow.core.AbstractActor;
import adams.flow.core.Actor;
import adams.flow.core.ActorUtils;
import adams.flow.template.AbstractActorTemplate;
import adams.parser.ParserHelper;
import java_cup.runtime.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;

/** CUP v0.11a beta 20060608 generated parser.
  * @version Tue May 03 13:11:58 NZST 2016
  */
public class Parser extends java_cup.runtime.lr_parser {

  /** Default constructor. */
  public Parser() {super();}

  /** Constructor which sets the default scanner. */
  public Parser(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which sets the default scanner. */
  public Parser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\030\000\002\002\004\000\002\002\004\000\002\002" +
    "\003\000\002\004\006\000\002\005\005\000\002\005\003" +
    "\000\002\005\003\000\002\005\003\000\002\005\005\000" +
    "\002\005\005\000\002\005\004\000\002\005\003\000\002" +
    "\005\003\000\002\005\005\000\002\005\004\000\002\005" +
    "\004\000\002\005\004\000\002\005\004\000\002\005\004" +
    "\000\002\005\004\000\002\005\004\000\002\005\004\000" +
    "\002\005\004\000\002\005\004" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\046\000\004\021\006\001\002\000\004\002\050\001" +
    "\002\000\004\002\uffff\001\002\000\026\005\016\006\010" +
    "\007\007\014\011\015\012\016\014\017\020\025\013\026" +
    "\015\030\017\001\002\000\012\020\ufff5\022\ufff5\023\ufff5" +
    "\024\ufff5\001\002\000\012\020\ufff6\022\ufff6\023\ufff6\024" +
    "\ufff6\001\002\000\012\020\ufffc\022\ufffc\023\ufffc\024\ufffc" +
    "\001\002\000\012\020\ufffb\022\ufffb\023\ufffb\024\ufffb\001" +
    "\002\000\014\004\044\010\043\011\045\012\046\013\042" +
    "\001\002\000\012\020\ufffa\022\ufffa\023\ufffa\024\ufffa\001" +
    "\002\000\014\004\040\010\036\011\037\012\041\013\035" +
    "\001\002\000\004\027\033\001\002\000\026\005\016\006" +
    "\010\007\007\014\011\015\012\016\014\017\020\025\013" +
    "\026\015\030\017\001\002\000\026\005\016\006\010\007" +
    "\007\014\011\015\012\016\014\017\020\025\013\026\015" +
    "\030\017\001\002\000\010\022\023\023\022\024\024\001" +
    "\002\000\026\005\016\006\010\007\007\014\011\015\012" +
    "\016\014\017\020\025\013\026\015\030\017\001\002\000" +
    "\004\004\026\001\002\000\026\005\016\006\010\007\007" +
    "\014\011\015\012\016\014\017\020\025\013\026\015\030" +
    "\017\001\002\000\012\020\ufff8\022\ufff8\023\ufff8\024\ufff8" +
    "\001\002\000\004\002\ufffe\001\002\000\012\020\ufff9\022" +
    "\ufff9\023\ufff9\024\ufff9\001\002\000\010\020\031\023\022" +
    "\024\024\001\002\000\012\020\ufffd\022\ufffd\023\ufffd\024" +
    "\ufffd\001\002\000\012\020\ufff7\022\ufff7\023\022\024\024" +
    "\001\002\000\004\004\034\001\002\000\012\020\ufff4\022" +
    "\ufff4\023\ufff4\024\ufff4\001\002\000\012\020\uffec\022\uffec" +
    "\023\uffec\024\uffec\001\002\000\012\020\ufff2\022\ufff2\023" +
    "\ufff2\024\ufff2\001\002\000\012\020\ufff0\022\ufff0\023\ufff0" +
    "\024\ufff0\001\002\000\012\020\uffea\022\uffea\023\uffea\024" +
    "\uffea\001\002\000\012\020\uffee\022\uffee\023\uffee\024\uffee" +
    "\001\002\000\012\020\uffed\022\uffed\023\uffed\024\uffed\001" +
    "\002\000\012\020\ufff3\022\ufff3\023\ufff3\024\ufff3\001\002" +
    "\000\012\020\uffeb\022\uffeb\023\uffeb\024\uffeb\001\002\000" +
    "\012\020\ufff1\022\ufff1\023\ufff1\024\ufff1\001\002\000\012" +
    "\020\uffef\022\uffef\023\uffef\024\uffef\001\002\000\004\002" +
    "\001\001\002\000\004\002\000\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\046\000\006\002\003\004\004\001\001\000\002\001" +
    "\001\000\004\003\046\001\001\000\004\005\020\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\004\005\031\001\001\000" +
    "\004\005\027\001\001\000\002\001\001\000\004\005\026" +
    "\001\001\000\002\001\001\000\004\005\024\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$Parser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$Parser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$Parser$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 1;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}



  /** the helper. */
  protected ParserHelper m_Helper = new ParserHelper();

  /** for storing the result of the expression. */
  protected AbstractActorTemplate m_Result = null;

  /** the parent of the where to suggest the template. */
  protected Actor m_Parent = null;

  /** the position to add the proposed actor template at. */
  protected Integer m_Position = null;

  /** all the current nodes in which to insert/add to the proposed actor. */
  protected Actor[] m_Actors = null;

  /**
   * Returns the parser helper.
   *
   * @return the helper
   */
  public ParserHelper getHelper() {
    return m_Helper;
  }

  /**
   * Sets the result of the evaluation.
   *
   * @param value the result
   */
  public void setResult(AbstractActorTemplate value) {
    m_Result = value;
  }

  /**
   * Returns the result of the evaluation.
   *
   * @return the result
   */
  public AbstractActorTemplate getResult() {
    return m_Result;
  }

  /**
   * Sets the parent of the proposed template.
   *
   * @param value the parent
   */
  public void setParent(Actor value) {
    m_Parent = value;
  }

  /**
   * Returns the parent of the proposed template.
   *
   * @return the parent
   */
  public Actor getParent() {
    return m_Parent;
  }

  /**
   * Sets the position of the proposed template.
   *
   * @param value the position
   */
  public void setPosition(Integer value) {
    m_Position = value;
  }

  /**
   * Returns the position of the proposed template.
   *
   * @return the position
   */
  public Integer getPosition() {
    return m_Position;
  }

  /**
   * Sets the actors in which to insert the proposed template.
   *
   * @param value the actors
   */
  public void setActors(Actor[] value) {
    m_Actors = value;
  }

  /**
   * Returns the actors in which to insert the proposed template.
   *
   * @return the actors
   */
  public Actor[] getActors() {
    return m_Actors;
  }

  /**
   * Returns whether the proposed actor will get added at the first position.
   *
   * @return true if the actor gets added at the first position
   */
  public Boolean isFirst() {
    return (m_Position == 0);
  }

  /**
   * Returns whether the proposed actor will get added at the last position.
   *
   * @return true if the actor gets added at the last position
   */
  public Boolean isLast() {
    return (m_Position >= m_Actors.length);
  }

  /**
   * Checks whether the actor will get added before or after a standalone
   * actor.
   *
   * @param before whether the actor gets added before or after
   * @return true if the actor gets added before/after a standalone one
   */
  public Boolean isStandalone(boolean before) {
    boolean 	result;

    result = false;

    if (before) {
      if (m_Position + 1 < m_Actors.length) {
        result = (ActorUtils.isStandalone(m_Actors[m_Position + 1]));
      }
    }
    else {
      if (m_Position > 0) {
        result = (ActorUtils.isStandalone(m_Actors[m_Position - 1]));
      }
    }

    return result;
  }

  /**
   * Checks whether the actor will get added before or after a source
   * actor.
   *
   * @param before whether the actor gets added before or after
   * @return true if the actor gets added before/after a source one
   */
  public Boolean isSource(boolean before) {
    boolean 	result;

    result = false;

    if (before) {
      if (m_Position + 1 < m_Actors.length) {
        result = (ActorUtils.isSource(m_Actors[m_Position + 1]));
      }
    }
    else {
      if (m_Position > 0) {
        result = (ActorUtils.isSource(m_Actors[m_Position - 1]));
      }
    }

    return result;
  }

  /**
   * Checks whether the actor will get added before or after a transformer
   * actor.
   *
   * @param before whether the actor gets added before or after
   * @return true if the actor gets added before/after a transformer one
   */
  public Boolean isTransformer(boolean before) {
    boolean 	result;

    result = false;

    if (before) {
      if (m_Position + 1 < m_Actors.length) {
        result = (ActorUtils.isTransformer(m_Actors[m_Position + 1]));
      }
    }
    else {
      if (m_Position > 0) {
        result = (ActorUtils.isTransformer(m_Actors[m_Position - 1]));
      }
    }

    return result;
  }

  /**
   * Checks whether the actor will get added before or after a sink
   * actor.
   *
   * @param before whether the actor gets added before or after
   * @return true if the actor gets added before/after a sink one
   */
  public Boolean isSink(boolean before) {
    boolean 	result;

    result = false;

    if (before) {
      if (m_Position + 1 < m_Actors.length) {
        result = (ActorUtils.isSink(m_Actors[m_Position + 1]));
      }
    }
    else {
      if (m_Position > 0) {
        result = (ActorUtils.isSink(m_Actors[m_Position - 1]));
      }
    }

    return result;
  }

  /**
   * Checks whether the actor will get added before or after a specific
   * actor (exact classname or interface classname).
   *
   * @param before whether the actor gets added before or after
   * @param classname the name of the actor to check against
   * @return true if the actor gets added before/after a specific one
   */
  public Boolean isClassname(boolean before, String classname) {
    boolean 	result;

    result = false;

    if (before) {
      if (m_Position + 1 < m_Actors.length) {
        result =    m_Actors[m_Position + 1].getClass().getName().equals(classname)
                 || ClassLocator.hasInterface(classname, m_Actors[m_Position + 1].getClass().getName());
      }
    }
    else {
      if (m_Position > 0) {
        result =    m_Actors[m_Position - 1].getClass().getName().equals(classname)
                 || ClassLocator.hasInterface(classname, m_Actors[m_Position - 1].getClass().getName());
      }
    }

    return result;
  }

}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$Parser$actions {
  private final Parser parser;

  /** Constructor */
  CUP$Parser$actions(Parser parser) {
    this.parser = parser;
  }

  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$Parser$do_action(
    int                        CUP$Parser$act_num,
    java_cup.runtime.lr_parser CUP$Parser$parser,
    java.util.Stack            CUP$Parser$stack,
    int                        CUP$Parser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$Parser$result;

      /* select the action based on the action number */
      switch (CUP$Parser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 23: // boolexpr ::= AFTER CLASSNAME 
            {
              Boolean RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int cright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = parser.isClassname(false, c); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 22: // boolexpr ::= BEFORE CLASSNAME 
            {
              Boolean RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int cright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = parser.isClassname(true, c); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 21: // boolexpr ::= AFTER SINK 
            {
              Boolean RESULT =null;
		 RESULT = parser.isSink(false); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 20: // boolexpr ::= BEFORE SINK 
            {
              Boolean RESULT =null;
		 RESULT = parser.isSink(true); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 19: // boolexpr ::= AFTER TRANSFORMER 
            {
              Boolean RESULT =null;
		 RESULT = parser.isTransformer(false); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 18: // boolexpr ::= BEFORE TRANSFORMER 
            {
              Boolean RESULT =null;
		 RESULT = parser.isTransformer(true); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 17: // boolexpr ::= AFTER SOURCE 
            {
              Boolean RESULT =null;
		 RESULT = parser.isSource(false); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 16: // boolexpr ::= BEFORE SOURCE 
            {
              Boolean RESULT =null;
		 RESULT = parser.isSource(true); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 15: // boolexpr ::= AFTER STANDALONE 
            {
              Boolean RESULT =null;
		 RESULT = parser.isStandalone(false); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 14: // boolexpr ::= BEFORE STANDALONE 
            {
              Boolean RESULT =null;
		 RESULT = parser.isStandalone(true); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // boolexpr ::= PARENT IS CLASSNAME 
            {
              Boolean RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int cright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new Boolean(parser.getParent().getClass().getName().equals(c)); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // boolexpr ::= ISLAST 
            {
              Boolean RESULT =null;
		 RESULT = parser.isLast(); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // boolexpr ::= ISFIRST 
            {
              Boolean RESULT =null;
		 RESULT = parser.isFirst(); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // boolexpr ::= NOT boolexpr 
            {
              Boolean RESULT =null;
		int bleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int bright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Boolean b = (Boolean)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = !b; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // boolexpr ::= boolexpr OR boolexpr 
            {
              Boolean RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		Boolean l = (Boolean)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Boolean r = (Boolean)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = l || r; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // boolexpr ::= boolexpr AND boolexpr 
            {
              Boolean RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		Boolean l = (Boolean)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Boolean r = (Boolean)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = l && r; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // boolexpr ::= FALSE 
            {
              Boolean RESULT =null;
		 RESULT = new Boolean(false); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // boolexpr ::= TRUE 
            {
              Boolean RESULT =null;
		 RESULT = new Boolean(true); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // boolexpr ::= BOOLEAN 
            {
              Boolean RESULT =null;
		int bleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int bright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Boolean b = (Boolean)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = b; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // boolexpr ::= LPAREN boolexpr RPAREN 
            {
              Boolean RESULT =null;
		int bleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int bright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		Boolean b = (Boolean)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = b; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("boolexpr",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // expr ::= IF boolexpr THEN CLASSNAME 
            {
              Object RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		Boolean e = (Boolean)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int cleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int cright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		
                  if (e)
                    parser.setResult(AbstractActorTemplate.forCommandLine(c));
                  else
                    parser.setResult(null);
                
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",2, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // rule ::= expr 
            {
              Object RESULT =null;

              CUP$Parser$result = parser.getSymbolFactory().newSymbol("rule",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // $START ::= rule EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		Object start_val = (Object)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		RESULT = start_val;
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$Parser$parser.done_parsing();
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // rule ::= expr expr_list 
            {
              Object RESULT =null;

              CUP$Parser$result = parser.getSymbolFactory().newSymbol("rule",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}


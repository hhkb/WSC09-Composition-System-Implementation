/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-05
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.ll.programBuilder.ProgramBuilder.java
 * Last modification: 2007-03-05
 *                by: Thomas Weise
 *
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to theFree Software
 *                    Foundation, Inc. 51 Franklin Street, Fifth Floor,
 *                    Boston, MA 02110-1301, USA or download the license
 *                    under http://www.gnu.org/licenses/lgpl.html or
 *                    http://www.gnu.org/copyleft/lesser.html.
 *
 * Warranty         : This software is provided "as is" without any
 *                    warranty; without even the implied warranty of
 *                    merchantability or fitness for a particular purpose.
 *                    See the Gnu Lesser General Public License for more
 *                    details.
 */

package org.dgpf.lgp.base.programBuilder;

import org.dgpf.lgp.base.ECondition;
import org.dgpf.lgp.base.EIndirection;
import org.dgpf.lgp.base.ILGPParameters;
import org.dgpf.lgp.base.Instruction;
import org.dgpf.lgp.base.InstructionSet;
import org.dgpf.lgp.base.LGPProgram;
import org.dgpf.lgp.base.instructions.arith.Add;
import org.dgpf.lgp.base.instructions.arith.And;
import org.dgpf.lgp.base.instructions.arith.Compare;
import org.dgpf.lgp.base.instructions.arith.Div;
import org.dgpf.lgp.base.instructions.arith.Exchange;
import org.dgpf.lgp.base.instructions.arith.Mod;
import org.dgpf.lgp.base.instructions.arith.Move;
import org.dgpf.lgp.base.instructions.arith.Mul;
import org.dgpf.lgp.base.instructions.arith.Not;
import org.dgpf.lgp.base.instructions.arith.Or;
import org.dgpf.lgp.base.instructions.arith.Sub;
import org.dgpf.lgp.base.instructions.arith.Xor;
import org.dgpf.lgp.base.instructions.ctrl.Call;
import org.dgpf.lgp.base.instructions.ctrl.JumpRelative;
import org.dgpf.lgp.base.instructions.ctrl.Terminate;
import org.sfc.collections.buffers.DirectBufferable;

/**
 * The program builder
 * 
 * @param <BT>
 *          the builder type
 * @author Thomas Weise
 */
public class ProgramBuilder<BT extends ProgramBuilder<BT>> extends
    DirectBufferable<BT> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the internal lock
   */
  static final Object LOCK = new Object();

  /**
   * the builder cache
   */
  static BuilderCache s_cache;

  /**
   * the instance count
   */
  private static long s_instCnt;

  /**
   * The instruction set
   */
  private final InstructionSet m_is;

  /**
   * the lgp parameters
   */
  private final ILGPParameters m_parameters;

  /**
   * the variables
   */
  private Variable[] m_variables;

  /**
   * the variable count
   */
  private int m_vc;

  /**
   * the intermediates
   */
  private Variable m_intermed;

  /**
   * the constants
   */
  private Variable m_const;

  /**
   * the global variable count
   */
  private int m_gc;

  /**
   * the local variable count
   */
  private int m_lc;

  /**
   * the current construct
   */
  private Construct m_constr;

  /**
   * the functions;
   */
  private Function[] m_funcs;

  /**
   * the function count
   */
  private int m_fc;

  /**
   * the current function
   */
  private Function m_curF;

  /**
   * the pending pop instruction
   */
  private boolean m_pendingPop;

  /** the addition */
  private final Add m_add;

  /** the and */
  private final And m_and;

  /** the comparison */
  private final Compare m_cmp;

  /** the div */
  private final Div m_div;

  /** the exchange */
  private final Exchange m_xchg;

  /** the modulo division */
  private final Mod m_mod;

  /** the multiplication */
  private final Mul m_mul;

  /** the move */
  private final Move m_mov;

  /** the not */
  private final Not m_not;

  /** the or */
  private final Or m_or;

  /** the subtraction */
  private final Sub m_sub;

  /** the xor */
  private final Xor m_xor;

  /** the jump */
  private final JumpRelative<?> m_jmp;

  /** the call */
  private final Call<?> m_call;

  /**
   * the termination command
   */
  private final Terminate<?> m_terminate;

  /**
   * Create a new program builder
   * 
   * @param parameters
   *          the lgp parameters
   */
  public ProgramBuilder(final ILGPParameters parameters) {
    super();
    newInst();

    InstructionSet is;

    this.m_parameters = parameters;
    is = parameters.getInstructions();

    this.m_is = is;
    this.m_variables = new Variable[16];
    this.m_funcs = new Function[16];
    this.m_pendingPop = false;

    this.m_add = is.getInstance(Add.class, 0);
    this.m_and = is.getInstance(And.class, 0);
    this.m_cmp = is.getInstance(Compare.class, 0);
    this.m_div = is.getInstance(Div.class, 0);
    this.m_xchg = is.getInstance(Exchange.class, 0);
    this.m_mod = is.getInstance(Mod.class, 0);
    this.m_mov = is.getInstance(Move.class, 0);
    this.m_mul = is.getInstance(Mul.class, 0);
    this.m_not = is.getInstance(Not.class, 0);
    this.m_or = is.getInstance(Or.class, 0);
    this.m_sub = is.getInstance(Sub.class, 0);
    this.m_xor = is.getInstance(Xor.class, 0);
    this.m_jmp = is.getInstance(JumpRelative.class, 0);
    this.m_call = is.getInstance(Call.class, 0);
    this.m_terminate = is.getInstance(Terminate.class, 0);
  }

  /**
   * Allocate a variable.
   * 
   * @param indir
   *          the indirection
   * @param index
   *          the index
   * @param intermed
   *          <code>true</code> for intermediate variables,
   *          <code>false</code> otherwise
   * @return the new variable
   */
  public Variable allocateVariable(final EIndirection indir,
      final int index, final boolean intermed) {
    Variable v;
    Variable[] q;
    int idx, l;
    EIndirection ind;

    if (indir == EIndirection.STACK)
      return Variable.TOP_OF_STACK;

    v = Variable.allocate();

    ind = indir;
    idx = index;

    if (indir == EIndirection.LOCAL) {
      if (this.m_curF == null) {
        ind = EIndirection.GLOBAL;
        idx = (this.m_gc++);
        // return Variable.TOP_OF_STACK;
      } // if no function is active, no local
      // variables can be declared
      idx = (this.m_lc++);
    } else if (indir == EIndirection.GLOBAL) {
      idx = (this.m_gc++);
    } else if ((indir == EIndirection.LOCAL_LOCAL)
        || (indir == EIndirection.GLOBAL_LOCAL)
        || (indir == EIndirection.LOCAL_GLOBAL)) {
      if (this.m_curF == null) {// if no function is active, no local
        ind = EIndirection.GLOBAL;
        idx = (this.m_gc++);
      }
    }

    v.m_indirection = ind;
    v.m_index = idx;

    if (indir == EIndirection.CONSTANT) {
      v.m_next = this.m_const;
      this.m_const = v;
    } else if (intermed) {
      v.m_next = this.m_intermed;
      this.m_intermed = v;
    } else {
      q = this.m_variables;
      l = this.m_vc;

      if (l >= q.length) {
        q = new Variable[l << 1];
        System.arraycopy(this.m_variables, 0, q, 0, l);
        this.m_variables = q;
      }
      q[l] = v;
      this.m_vc = (l + 1);
    }

    return v;
  }

  /**
   * Clear this program builder
   */
  public void clear() {
    Variable[] v;
    int i;
    Variable c, d;
    Function[] f;
    Construct cc, dd;

    this.m_gc = 0;
    this.m_lc = 0;

    v = this.m_variables;

    for (i = (this.m_vc - 1); i >= 0; i--) {
      Variable.enqueue(v[i]);
    }

    for (c = this.m_intermed; c != null; c = d) {
      d = c.m_next;
      Variable.enqueue(c);
    }

    for (c = this.m_const; c != null; c = d) {
      d = c.m_next;
      Variable.enqueue(c);
    }

    this.m_const = null;
    this.m_intermed = null;
    this.m_vc = 0;

    f = this.m_funcs;
    for (i = (this.m_fc - 1); i >= 0; i--) {
      f[i].clear();
    }
    this.m_fc = 0;

    for (cc = this.m_constr; (!(cc instanceof Function)) && (cc != null); cc = dd) {
      dd = cc.m_next;
      cc.clear();
    }
    this.m_pendingPop = false;

    this.m_curF = null;

  }

  /**
   * create a pending pop
   */
  public void pendingPop() {
    this.m_pendingPop = true;
  }

  /**
   * fix the pending pop
   */
  private final void fixPendingPop() {
    if (this.m_pendingPop) {
      this.m_pendingPop = false;
      if (this.m_curF != null) {
        this.m_curF.addInstruction(this.m_mov.getId(), EIndirection
            .encode(EIndirection.CONSTANT, 0), EIndirection.encode(
            EIndirection.STACK, 0), 1);
      }
    }
  }

  /**
   * Obtain the instruction set of this program builder
   * 
   * @return the instruction set of this program builder
   */
  public InstructionSet getInstructionSet() {
    return this.m_is;
  }

  /**
   * Begin a new construct
   */
  public void beginConstruct() {
    Construct c;

    c = Construct.allocate();
    this.beginConstruct(c);
  }

  /**
   * begin a new construct
   * 
   * @param c
   *          the construct
   */
  private final void beginConstruct(final Construct c) {
    c.m_next = this.m_constr;
    this.m_constr = c;
    c.m_vc = this.m_vc;
    c.m_gc = this.m_gc;
    c.m_lc = this.m_lc;
    c.m_intermed = this.m_intermed;
  }

  /**
   * End a construct
   */
  public void endConstruct() {
    Construct c;
    Variable[] vs;
    int i;
    Variable im, cc, dd;

    c = this.m_constr;
    if (c != null) {
      this.m_constr = c.m_next;

      vs = this.m_variables;
      for (i = (this.m_vc - 1); i >= c.m_vc; i--) {
        Variable.enqueue(vs[i]);
      }

      im = c.m_intermed;
      for (cc = this.m_intermed; cc != im; cc = dd) {
        dd = cc.m_next;
        Variable.enqueue(cc);
      }
      this.m_intermed = im;

      this.m_vc = (i + 1);
      this.m_gc = c.m_gc;
      this.m_lc = c.m_lc;

      c.clear();
    }
  }

  /**
   * end the current function
   */
  public void endFunction() {
    Function f;
    Construct c, d;

    this.m_pendingPop = false;
    f = this.m_curF;

    for (c = this.m_constr; (c != f) && (f != null); c = d) {
      d = c.m_next;
      c.clear();
    }

    this.m_curF = null;
    this.m_constr = null;
  }

  /**
   * Clear the caches of the program builder.
   */
  public static final void clearCaches() {
    BuilderCache b;

    for (b = s_cache; b != null; b = b.m_next) {
      b.clear();
      System.gc();
    }

    System.gc();
  }

  /**
   * Finalize this program builder instance
   * 
   * @throws Throwable
   *           if the program builder is not used anymore
   */
  @Override
  protected void finalize() throws Throwable {
    instKilled();
    super.finalize();
  }

  /**
   * This method is called if a new program builder was created
   */
  private static final void newInst() {
    synchronized (LOCK) {
      if ((++s_instCnt) <= 0)
        s_instCnt = 1;
    }
  }

  /**
   * This method is called when an instance is finalized
   */
  private static final void instKilled() {
    synchronized (LOCK) {
      if ((--s_instCnt) <= 0) {
        s_instCnt = 0;
        clearCaches();
      }
    }
  }

  /**
   * Begin a function
   * 
   * @return the function
   */
  public Function declareFunction() {
    Function f;
    Function[] fs;
    int fc;

    f = Function.allocateF();
    fc = this.m_fc;
    fs = this.m_funcs;

    if (fc >= fs.length) {
      fs = new Function[fc << 1];
      System.arraycopy(this.m_funcs, 0, fs, 0, fc);
      this.m_funcs = fs;
    }

    fs[fc] = f;
    f.m_index = fc;
    this.m_fc = (fc + 1);
    return f;
  }

  /**
   * Begin the specified function
   * 
   * @param f
   *          the function to begin
   */
  public void beginFunction(final Function f) {
    int i;
    this.beginConstruct(f);
    this.m_curF = f;

    for (i = f.m_paramC; i > 0; i--) {
      this.allocateVariable(EIndirection.LOCAL, 0, false);
    }
  }

  /**
   * Create an add instruction
   * 
   * @param v1
   *          the first variable
   * @param v2
   *          the second variable
   * @param v3
   *          the third variable
   * @return the index of the newly added instruction
   */
  public int add(final Variable v1, final Variable v2, final Variable v3) {
    if (this.m_add == null) {
      if (this.m_curF != null)
        return this.m_curF.m_pos;
      return 0;
    }
    return this.addInstruction(this.m_add, v1.encode(), v2.encode(), v3
        .encode());
  }

  /**
   * Create an sub instruction
   * 
   * @param v1
   *          the first variable
   * @param v2
   *          the second variable
   * @param v3
   *          the third variable
   * @return the index of the newly added instruction
   */
  public int sub(final Variable v1, final Variable v2, final Variable v3) {
    if (this.m_sub == null) {
      if (this.m_curF != null)
        return this.m_curF.m_pos;
      return 0;
    }
    return this.addInstruction(this.m_sub, v1.encode(), v2.encode(), v3
        .encode());
  }

  /**
   * Create an mul instruction
   * 
   * @param v1
   *          the first variable
   * @param v2
   *          the second variable
   * @param v3
   *          the third variable
   * @return the index of the newly added instruction
   */
  public int mul(final Variable v1, final Variable v2, final Variable v3) {
    if (this.m_mul == null) {
      if (this.m_curF != null)
        return this.m_curF.m_pos;
      return 0;
    }
    return this.addInstruction(this.m_mul, v1.encode(), v2.encode(), v3
        .encode());
  }

  /**
   * Create an div instruction
   * 
   * @param v1
   *          the first variable
   * @param v2
   *          the second variable
   * @param v3
   *          the third variable
   * @return the index of the newly added instruction
   */
  public int div(final Variable v1, final Variable v2, final Variable v3) {
    if (this.m_div == null) {
      if (this.m_curF != null)
        return this.m_curF.m_pos;
      return 0;
    }
    return this.addInstruction(this.m_div, v1.encode(), v2.encode(), v3
        .encode());
  }

  /**
   * Create an mod instruction
   * 
   * @param v1
   *          the first variable
   * @param v2
   *          the second variable
   * @param v3
   *          the third variable
   * @return the index of the newly added instruction
   */
  public int mod(final Variable v1, final Variable v2, final Variable v3) {
    if (this.m_mod == null) {
      if (this.m_curF != null)
        return this.m_curF.m_pos;
      return 0;
    }
    return this.addInstruction(this.m_mod, v1.encode(), v2.encode(), v3
        .encode());
  }

  /**
   * Create an and instruction
   * 
   * @param v1
   *          the first variable
   * @param v2
   *          the second variable
   * @param v3
   *          the third variable
   * @return the index of the newly added instruction
   */
  public int and(final Variable v1, final Variable v2, final Variable v3) {
    if (this.m_and == null) {
      if (this.m_curF != null)
        return this.m_curF.m_pos;
      return 0;
    }
    return this.addInstruction(this.m_add, v1.encode(), v2.encode(), v3
        .encode());
  }

  /**
   * Create an or instruction
   * 
   * @param v1
   *          the first variable
   * @param v2
   *          the second variable
   * @param v3
   *          the third variable
   * @return the index of the newly added instruction
   */
  public int or(final Variable v1, final Variable v2, final Variable v3) {
    if (this.m_or == null) {
      if (this.m_curF != null)
        return this.m_curF.m_pos;
      return 0;
    }
    return this.addInstruction(this.m_or, v1.encode(), v2.encode(), v3
        .encode());
  }

  /**
   * Create an xor instruction
   * 
   * @param v1
   *          the first variable
   * @param v2
   *          the second variable
   * @param v3
   *          the third variable
   * @return the index of the newly added instruction
   */
  public int xor(final Variable v1, final Variable v2, final Variable v3) {
    if (this.m_xor == null) {
      if (this.m_curF != null)
        return this.m_curF.m_pos;
      return 0;
    }
    return this.addInstruction(this.m_xor, v1.encode(), v2.encode(), v3
        .encode());
  }

  /**
   * Create an not instruction
   * 
   * @param v1
   *          the first variable
   * @param v2
   *          the second variable
   * @return the index of the newly added instruction
   */
  public int not(final Variable v1, final Variable v2) {
    if (this.m_not == null) {
      if (this.m_curF != null)
        return this.m_curF.m_pos;
      return 0;
    }
    return this.addInstruction(this.m_not, v1.encode(), v2.encode(), 0);
  }

  /**
   * Create an compare instruction
   * 
   * @param v1
   *          the first variable
   * @param v2
   *          the second variable
   * @param v3
   *          the third variable
   * @return the index of the newly added instruction
   */
  public int compare(final Variable v1, final Variable v2,
      final Variable v3) {
    if (this.m_cmp == null) {
      if (this.m_curF != null)
        return this.m_curF.m_pos;
      return 0;
    }
    return this.addInstruction(this.m_cmp, v1.encode(), v2.encode(), v3
        .encode());
  }

  /**
   * Create an move instruction
   * 
   * @param v1
   *          the first variable
   * @param v2
   *          the second variable
   * @return the index of the newly added instruction
   */
  public int move(final Variable v1, final Variable v2) {
    if (this.m_mov == null) {
      if (this.m_curF != null)
        return this.m_curF.m_pos;
      return 0;
    }
    return this.addInstruction(this.m_mov, v1.encode(), v2.encode(), 0);
  }

  /**
   * Create an exchange instruction
   * 
   * @param v1
   *          the first variable
   * @param v2
   *          the second variable
   * @return the index of the newly added instruction
   */
  public int exchange(final Variable v1, final Variable v2) {
    if (this.m_xchg == null) {
      if (this.m_curF != null)
        return this.m_curF.m_pos;
      return 0;
    }
    return this.addInstruction(this.m_xchg, v1.encode(), v2.encode(), 0);
  }

  /**
   * Create a jump instruction
   * 
   * @param cond
   *          the condition
   * @param v
   *          the variable
   * @param index
   *          the target
   * @return the index of the newly added instruction
   */
  public int jump(final ECondition cond, final Variable v, final int index) {
    if (this.m_jmp == null) {
      if (this.m_curF != null)
        return this.m_curF.m_pos;
      return 0;
    }
    return this.addInstruction(this.m_jmp, ECondition.encode(cond), v
        .encode(), index);
  }

  /**
   * Replace a jump instruction
   * 
   * @param pos
   *          the jump instruction's index
   * @param cond
   *          the condition
   * @param v
   *          the variable
   * @param index
   *          the target
   */
  public void replaceJump(final int pos, final ECondition cond,
      final Variable v, final int index) {
    this.replaceJump(pos, cond, v.encode(), index);
  }

  /**
   * Replace a jump instruction
   * 
   * @param pos
   *          the jump instruction's index
   * @param cond
   *          the condition
   * @param v
   *          the variable
   * @param index
   *          the target
   */
  public void replaceJump(final int pos, final ECondition cond,
      final int v, final int index) {
    this.replaceInstruction(pos, this.m_jmp.getId(), ECondition
        .encode(cond), v, index);
  }

  /**
   * Create a function call instruction
   * 
   * @param cond
   *          the condition
   * @param v
   *          the variable
   * @param func
   *          the target function
   * @return the index of the newly added instruction
   */
  public int call(final ECondition cond, final Variable v,
      final Function func) {
    return this.call(cond, v, func.m_index);
  }

  /**
   * Create a function call instruction
   * 
   * @param cond
   *          the condition
   * @param v
   *          the variable
   * @param func
   *          the target function index
   * @return the index of the newly added instruction
   */
  public int call(final ECondition cond, final Variable v, final int func) {
    if (this.m_call == null) {
      if (this.m_curF != null)
        return this.m_curF.m_pos;
      return 0;
    }
    return this.addInstruction(this.m_call, ECondition.encode(cond), v
        .encode(), func);
  }

  /**
   * Create a termination instruction
   * 
   * @param cond
   *          the condition
   * @param v
   *          the variable
   * @return the index of the newly added instruction
   */
  public int terminate(final ECondition cond, final Variable v) {
    if (this.m_call == null) {
      if (this.m_curF != null)
        return this.m_curF.m_pos;
      return 0;
    }
    return this.addInstruction(this.m_terminate, ECondition.encode(cond),
        v.encode(), 0);
  }

  /**
   * Replaces an instruction in the current function
   * 
   * @param index
   *          the index of the instruction to replace
   * @param opCode
   *          the op code
   * @param param1
   *          the first parameter
   * @param param2
   *          the first parameter
   * @param param3
   *          the third parameter
   */
  private void replaceInstruction(final int index, final int opCode,
      final int param1, final int param2, final int param3) {
    this.m_curF.replaceInstruction(index, opCode, param1, param2, param3);
  }

  /**
   * Obtain the function currently processed
   * 
   * @return the function currently processed
   */
  public Function getCurrentFunction() {
    return this.m_curF;
  }

  /**
   * Obtain the current position
   * 
   * @return the function index
   */
  public int getPosition() {
    return this.m_curF.m_pos;
  }

  /**
   * Declare and begin a new function and return it.
   * 
   * @param paramCount
   *          the count of parameters of the new function
   * @return the new function
   */
  public Function beginFunction(final int paramCount) {
    Function f;

    f = this.declareFunction();
    f.m_paramC = paramCount;
    this.beginFunction(f);
    return f;
  }

  /**
   * Obtain the function count
   * 
   * @return the function count
   */
  public int getFunctionCount() {
    return this.m_fc;
  }

  /**
   * Obtain the variable count
   * 
   * @return the variable count
   */
  public int getVariableCount() {
    return this.m_vc;
  }

  /**
   * Obtain the variable at position <code>index</code>
   * 
   * @param index
   *          the index
   * @return the variable at that position
   */
  public Variable getVariable(final int index) {
    return this.m_variables[index];
  }

  /**
   * Obtain a certain function
   * 
   * @param index
   *          the function index
   * @return the function wanted
   */
  public Function getFunction(final int index) {
    return this.m_funcs[index];
  }

  /**
   * Add a return instruction
   * 
   * @return the index of the newly added instruction
   */
  public int addReturn() {
    return this.addInstruction(Integer.MAX_VALUE, Integer.MAX_VALUE,
        Integer.MAX_VALUE, Integer.MAX_VALUE);
  }

  /**
   * Obtain the program built
   * 
   * @return the program built
   */
  @SuppressWarnings("unchecked")
  public LGPProgram getProgram() {
    int[][] code;
    int i, s;
    Function[] d;
    InstructionSet is;

    s = this.m_fc;

    if (s <= 0)
      return LGPProgram.EMPTY_PROGRAM;

    is = this.m_is;
    d = this.m_funcs;
    code = new int[s][];
    for (i = 0; i < s; i++) {
      code[i] = d[i].getCode(is);
    }

    return new LGPProgram(this.m_is, code).postProcess(this.m_parameters);
  }

  /**
   * Add a new instruction to this function
   * 
   * @param instr
   *          the instruction to add
   * @param param1
   *          the first parameter
   * @param param2
   *          the first parameter
   * @param param3
   *          the third parameter
   * @return the index of the newly added instruction
   */
  public int addInstruction(final Instruction<?> instr, final int param1,
      final int param2, final int param3) {
    return this.addInstruction(instr.getId(), param1, param2, param3);
  }

  /**
   * Add a new instruction to this function
   * 
   * @param clazz
   *          the instruction class to add
   * @param param1
   *          the first parameter
   * @param param2
   *          the first parameter
   * @param param3
   *          the third parameter
   * @return the index of the newly added instruction
   */
  public int addInstruction(final Class<? extends Instruction<?>> clazz,
      final int param1, final int param2, final int param3) {

    Instruction<?> c;

    c = this.m_is.getInstance(clazz, 0);
    if (c != null)
      return this.addInstruction(c, param1, param2, param3);
    return this.m_curF.m_pos;
  }

  /**
   * Add a new instruction to this function
   * 
   * @param opCode
   *          the op code
   * @param param1
   *          the first parameter
   * @param param2
   *          the first parameter
   * @param param3
   *          the third parameter
   * @return the index of the newly added instruction
   */
  private final int addInstruction(final int opCode, final int param1,
      final int param2, final int param3) {
    this.fixPendingPop();
    return this.m_curF.addInstruction(opCode, param1, param2, param3);
  }

  /**
   * add a dummy instruction
   * 
   * @return the dummy instruction's position
   */
  public int addDummy() {
    return this.addInstruction(0, 0, 0, 0);
  }

  /**
   * The index that the next instruction added to this function will have.
   * 
   * @return the index that the next instruction added to this function
   *         will have
   */
  public int getNextInstructionIndex() {
    return this.m_curF.m_pos;
  }

  /**
   * This abstract class helps us when clearing caches
   * 
   * @author Thomas Weise
   */
  public abstract static class BuilderCache {
    /**
     * the next builder cache.
     */
    BuilderCache m_next;

    /**
     * instantiate the builder cache
     */
    protected BuilderCache() {
      super();
      synchronized (LOCK) {
        this.m_next = s_cache;
        s_cache = this;
      }
    }

    /**
     * Clear this cache
     */
    protected abstract void clear();

  }
}

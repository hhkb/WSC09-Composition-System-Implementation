/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-05
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.ll.programBuilder.Function.java
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
import org.dgpf.lgp.base.InstructionSet;
import org.dgpf.lgp.base.programBuilder.ProgramBuilder.BuilderCache;

/**
 * the function class
 * 
 * @author Thomas Weise
 */
public final class Function extends Construct {
  /**
   * the lock object
   */
  private static final Object LOCK = new Object();

  /**
   * the empty code
   */
  private static final int[] EMPTY_CODE = new int[0];

  /**
   * the construct cache
   */
  static Construct s_fcache;

  static {
    new BuilderCache() {
      @Override
      protected void clear() {
        s_fcache = null;
      }
    };
  }

  /**
   * the function index
   */
  int m_index;

  /**
   * the parameter count
   */
  int m_paramC;

  /**
   * the function code
   */
  private int[] m_code;

  /**
   * the current code position
   */
  int m_pos;

  /**
   * the code len
   */
  private int m_len;

  /**
   * create a new function
   */
  Function() {
    super();
    this.m_code = new int[128];
  }

  /**
   * Obtain the function index
   * 
   * @return the function index
   */
  public int getIndex() {
    return this.m_index;
  }

  /**
   * Obtain the parameter count
   * 
   * @return the parameter count
   */
  public int getParameterCount() {
    return this.m_paramC;
  }

  /**
   * Set the parameter count of this function
   * 
   * @param paramC
   *          the parameter count
   */
  public void setParameterCount(final int paramC) {
    this.m_paramC = paramC;
  }

  /**
   * Replaces an instruction in this function
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
  final void replaceInstruction(final int index, final int opCode,
      final int param1, final int param2, final int param3) {
    int i;
    int[] code;

    i = (index << 2);
    code = this.m_code;

    code[i] = opCode;
    code[i + 1] = param1;
    code[i + 2] = param2;
    code[i + 3] = param3;
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
  final int addInstruction(final int opCode, final int param1,
      final int param2, final int param3) {
    int[] c;
    int l;

    c = this.m_code;
    l = (this.m_len + 4);
    if (l > c.length) {
      c = new int[l << 1];
      System.arraycopy(this.m_code, 0, c, 0, l - 4);
      this.m_code = c;
    }

    c[l - 4] = opCode;
    c[l - 3] = param1;
    c[l - 2] = param2;
    c[l - 1] = param3;
    this.m_len = l;
    return (this.m_pos++);
  }

  /**
   * Obtain the code of this function
   * 
   * @param is
   *          the instruction set
   * @return the code of this function
   */
  final int[] getCode(final InstructionSet is) {
    int[] c;
    int l;

    this.resolveReturns(is);

    l = this.m_len;
    if (l <= 0)
      return EMPTY_CODE;

    c = new int[l];
    System.arraycopy(this.m_code, 0, c, 0, l);
    if (l > 16384)
      this.m_code = new int[128];
    return c;
  }

  /**
   * Clear this construct. This method clears this construct and
   * subsequently invokes {{@link #dispose()}.
   */
  @Override
  protected void clear() {
    this.m_index = 0;
    this.m_paramC = 0;
    this.m_len = 0;
    this.m_pos = 0;
    super.clear();
  }

  /**
   * Dispose this construct
   */
  @Override
  protected void dispose() {
    synchronized (LOCK) {
      this.m_next = s_fcache;
      s_fcache = this;
    }
  }

  /**
   * Resolve all return instructions and add a fake return to the end if
   * there is none. Per default, all functions return the value of their
   * first local variable.
   * 
   * @param is
   *          the instruction set
   */
  final void resolveReturns(final InstructionSet is) {
    int[] code;
    int len, i, p, ep, mc, jc;

    code = this.m_code;
    len = this.m_len;

    mc = is
        .getInstance(org.dgpf.lgp.base.instructions.arith.Move.class, 0)
        .getId();
    jc = is.getInstance(
        org.dgpf.lgp.base.instructions.ctrl.JumpRelative.class, 0).getId();

    if (len <= 0) {
      code[0] = mc; // DefaultInstructionSet.MOVE.getOpCode();
      code[1] = EIndirection.encode(EIndirection.STACK, 0);
      code[2] = EIndirection.encode(EIndirection.CONSTANT, 0);
      this.m_len = 4;
      this.m_pos = 1;
      return;
    }

    if ((code[len - 1] == Integer.MAX_VALUE)
        && (code[len - 2] == Integer.MAX_VALUE)
        && (code[len - 3] == Integer.MAX_VALUE)
        && (code[len - 4] == Integer.MAX_VALUE)) {
      this.m_pos--;
      len -= 4;
      this.m_len = len;
    }

    if (!(is.get(code[len - 4]).pushesOnStack(code[len - 3],
        code[len - 2], code[len - 1]))) {
      if (code.length >= len) {
        code = new int[len << 1];
        System.arraycopy(this.m_code, 0, code, 0, len);
        this.m_code = code;
      }

      code[len] = mc;
      code[len + 1] = EIndirection.encode(EIndirection.STACK, 0);
      code[len + 2] = EIndirection.encode(EIndirection.LOCAL, 0);
      len += 4;
      this.m_pos++;
      this.m_len = len;
    }

    ep = this.m_pos;

    for (i = 0, p = 0; i < len; i += 4, p++) {
      if ((code[i] == Integer.MAX_VALUE)
          && (code[i + 1] == Integer.MAX_VALUE)
          && (code[i + 2] == Integer.MAX_VALUE)
          && (code[i + 3] == Integer.MAX_VALUE)) {
        code[i] = jc;
        code[i + 1] = ECondition.encode(ECondition.TRUE);
        code[i + 2] = 0;
        code[i + 3] = (ep - p);
      }
    }
  }

  // /**
  // * Add a return instruction
  // *
  // * @return the index of the newly added instruction
  // */
  // final int addReturn() {
  // return this.addInstruction(Integer.MAX_VALUE, Integer.MAX_VALUE,
  // Integer.MAX_VALUE, Integer.MAX_VALUE);
  // }

  /**
   * Allocate a new construct
   * 
   * @return the new construct
   */
  static final Function allocateF() {
    Construct c;

    synchronized (LOCK) {
      c = s_fcache;
      if (c != null)
        s_fcache = c.m_next;
    }
    if (c == null)
      return new Function();
    return ((Function) c);
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-05
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.ll.programBuilder.Variable.java
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

import org.dgpf.lgp.base.EIndirection;
import org.dgpf.lgp.base.programBuilder.ProgramBuilder.BuilderCache;

/**
 * The variable descriptor
 * 
 * @author Thomas Weise
 */
public final class Variable {
  /**
   * the lock object
   */
  private static final Object LOCK = new Object();

  /**
   * the variable cache
   */
  static Variable s_cache;

  /**
   * this variable performs the push and pop
   */
  public static final Variable TOP_OF_STACK;

  /**
   * the constant null variable
   */
  public static final Variable NULL;

  /**
   * the constant -1 variable
   */
  public static final Variable FFFF;

  /**
   * the constant 1 variable
   */
  public static final Variable ONE;

  static {
    TOP_OF_STACK = new Variable();
    TOP_OF_STACK.m_indirection = EIndirection.STACK;
    NULL = new Variable();
    NULL.m_indirection = EIndirection.CONSTANT;
    FFFF = new Variable();
    FFFF.m_indirection = EIndirection.CONSTANT;
    FFFF.m_index = -1;
    ONE = new Variable();
    ONE.m_indirection = EIndirection.CONSTANT;
    ONE.m_index = 1;

    new BuilderCache() {

      /**
       * Clear this cache
       */
      @Override
      protected void clear() {
        s_cache = null;
      }

    };
  }

  /**
   * the indirection
   */
  EIndirection m_indirection;

  /**
   * the index
   */
  int m_index;

  /**
   * the next variable
   */
  Variable m_next;

  /**
   * create a new variable desc
   */
  Variable() {
    super();
  }

  /**
   * Obtain the indirection of the variable
   * 
   * @return the indirection of the variable
   */
  public EIndirection getIndirection() {
    return this.m_indirection;
  }

  /**
   * Obtain the index of the variable
   * 
   * @return the index of the variable
   */
  public int getIndex() {
    return this.m_index;
  }

  /**
   * Encode this variable
   * 
   * @return the result of the encoding - the encoded variable
   */
  public int encode() {
    return EIndirection.encode(this.m_indirection, this.m_index);
  }

  /**
   * enqueue a variable into the cache
   * 
   * @param v
   *          the variable to enqueue
   */
  static final void enqueue(final Variable v) {
    synchronized (LOCK) {
      v.m_next = s_cache;
      s_cache = v;
    }
  }

  /**
   * allocate a new variable
   * 
   * @return the variable
   */
  static final Variable allocate() {
    Variable v;
    synchronized (LOCK) {
      v = s_cache;
      if (v != null) {
        s_cache = v.m_next;
      }
    }

    if (v != null) {
      v.m_next = null;
      return v;
    }
    return new Variable();
  }

  /**
   * Obtain the string representation of this variable
   * 
   * @return the string representation of this variable
   */
  @Override
  public String toString() {
    return (this.m_indirection.name() + ':' + (this.m_index));
  }

}

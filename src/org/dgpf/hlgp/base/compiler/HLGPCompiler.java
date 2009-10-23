/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-04-03
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.compiler.Compiler.java
 * Last modification: 2007-04-03
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

package org.dgpf.hlgp.base.compiler;

import org.dgpf.lgp.base.EIndirection;
import org.dgpf.lgp.base.ILGPParameters;
import org.dgpf.lgp.base.programBuilder.ProgramBuilder;
import org.dgpf.lgp.base.programBuilder.Variable;
import org.sfc.math.Mathematics;

/**
 * With this class you can compile high-level language programs to
 * low-level language programs.
 * 
 * @author Thomas Weise
 */
public class HLGPCompiler extends ProgramBuilder<HLGPCompiler> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the target variables
   */
  private Variable[] m_target;

  /**
   * the target count
   */
  private int m_tc;

  /**
   * Create a new compiler.
   * 
   * @param parameters
   *          the lgp parameters
   */
  public HLGPCompiler(final ILGPParameters parameters) {
    super(parameters);
    this.m_target = new Variable[16];
  }

  /**
   * Clear this program builder
   */
  @Override
  public void clear() {
    this.m_tc = 0;
    super.clear();
  }

  /**
   * resolve the given variable
   * 
   * @param var
   *          the variable to be resolved
   * @return the resolved variable
   */
  public Variable resolveVariable(final int var) {
    return this.getVariable(Resolver.resolveVariable(var, this
        .getVariableCount()));
  }

  /**
   * Resolve a function
   * 
   * @param func
   *          the function to be resolved
   * @return the index of the resolved function
   */
  public int resolveFunction(final int func) {
    return Resolver.resolveFunction(func, this.getFunctionCount());
  }

  /**
   * Allocate a new target for an expression
   * 
   * @return the new target for an expression
   */
  public Variable allocateTarget() {
    Variable v;

    v = this.allocateVariable(EIndirection.LOCAL, -1, true);
    this.pushTarget(v);
    return v;
  }

  /**
   * push a new target variable
   * 
   * @param v
   *          the variable to push
   */
  public void pushTarget(final Variable v) {
    Variable[] t;
    int tc;

    t = this.m_target;
    tc = this.m_tc;
    if (tc >= t.length) {
      t = new Variable[tc << 1];
      System.arraycopy(this.m_target, 0, t, 0, tc);
      this.m_target = t;
    }
    t[tc] = v;
    this.m_tc = (tc + 1);
  }

  /**
   * Obtain the current target variable
   * 
   * @return the current target variable
   */
  public Variable getTarget() {
    int tc;

    tc = this.m_tc;
    if (tc <= 0)
      return Variable.TOP_OF_STACK;
    return this.m_target[tc - 1];
  }

  /**
   * pop the current target
   * 
   * @return the current target
   */
  public Variable popTarget() {
    return this.m_target[--this.m_tc];
  }

  /**
   * Resolve a local variable
   * 
   * @param index
   *          the index
   * @param indir
   *          the indirection type
   * @return the local variable
   */
  public Variable resolveTypedVariable(final int index,
      final EIndirection indir) {
    int i, c, d, e, oe;
    Variable v;

    c = this.getVariableCount();
    if (c <= 0)
      return null;
    i = Mathematics.modulo(index, c);
    e = i;
    do {
      oe = e;
      for (d = c; d > 0; d--) {
        v = this.getVariable(i);
        if (v.getIndirection() == indir) {
          if ((--e) <= 0)
            return v;
        }
        if (i == 0)
          i = (c - 1);
        else
          i = (i - 1);
      }
    } while (e != oe);

    return this.getVariable(c - 1);
  }

}

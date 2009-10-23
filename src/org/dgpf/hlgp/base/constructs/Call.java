/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.Call.java
 * Last modification: 2007-03-01
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

package org.dgpf.hlgp.base.constructs;

import org.dgpf.hlgp.base.Construct;
import org.dgpf.hlgp.base.Expression;
import org.dgpf.hlgp.base.compiler.HLGPCompiler;
import org.dgpf.lgp.base.ECondition;
import org.dgpf.lgp.base.EIndirection;
import org.dgpf.lgp.base.LGPProgram;
import org.dgpf.lgp.base.programBuilder.Function;
import org.dgpf.lgp.base.programBuilder.Variable;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * The call construct
 * 
 * @author Thomas Weise
 */
public class Call extends Construct {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * the index
   */
  private final int m_index;

  /**
   * Create a new function call
   * 
   * @param index
   *          the index of the function to be called
   * @param parameters
   *          the function parameters
   */
  public Call(final int index, final Node[] parameters) {
    super(parameters);
    this.m_index = index;
  }

  /**
   * Serializes the parameters of the constructor of this object.
   * 
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  @Override
  protected void javaParametersToStringBuilder(final StringBuilder sb,
      final int indent) {
    sb.append(this.m_index);
    sb.append(',');
    sb.append(' ');
    super.javaParametersToStringBuilder(sb, indent);
  }

  /**
   * Obtain the factory which deals with nodes of the same type as this
   * node.
   * 
   * @return the factory which deals with nodes of the same type as this
   *         node
   */
  @Override
  public INodeFactory getFactory() {
    return CallFactory.CALL_FACTORY;
  }

  /**
   * Transform this program into its human readable representation.
   * 
   * @param sb
   *          the string builder to write to
   * @param indent
   *          the indent
   */
  @Override
  protected void toStringBuilder(final StringBuilder sb, final int indent) {
    int i, s;

    LGPProgram.appendFunctionName(this.m_index, sb);
    sb.append('(');
    s = this.size();
    if (s > 0) {
      this.get(0).toStringBuilder(sb);
      for (i = 1; i < s; i++) {
        sb.append(',');
        sb.append(' ');
        this.get(i).toStringBuilder(sb);
      }
    }
    sb.append(')');
  }

  /**
   * Check whether this node equals another object.
   * 
   * @param o
   *          the other object
   * @return <code>true</code> if the other object equals this node
   */
  @Override
  public boolean equals(final Object o) {
    return super.equals(o) && (((Call) o).m_index == this.m_index);
  }

  /**
   * Obtain the count of parameters of a function
   * 
   * @param func
   *          the function
   * @param c
   *          the compiler
   * @return the parameter count
   */
  @Override
  protected int getParameterCount(final int func, final HLGPCompiler c) {
    if (c.resolveFunction(this.m_index) == func) {
      return this.size();
    }

    return 0;
  }

  /**
   * Compile this construct.
   * 
   * @param compiler
   *          the compiler to use
   */
  @Override
  public void compile(final HLGPCompiler compiler) {
    int q, c, i, j;
    Function f;
    Construct cc;
    Variable x, r;

    q = compiler.resolveFunction(this.m_index);
    f = compiler.getFunction(q);

    c = f.getParameterCount();
    j = Math.min(this.size(), c);
    compiler.beginConstruct();

    r = null;
    for (i = 0; i < j; i++) {
      cc = ((Construct) (this.get(i)));

      if (cc instanceof Expression)
        x = ((Expression) cc).provideResultVariable(compiler);
      else
        x = null;

      if (x == null) {
        if (r == null)
          r = compiler.allocateTarget();
        x = r;
      }

      cc.compile(compiler);
      if (x.getIndirection() != EIndirection.STACK)
        compiler.move(Variable.TOP_OF_STACK, x);
    }

    if (r != null)
      compiler.popTarget();

    for (; i < c; i++) {
      compiler.move(Variable.TOP_OF_STACK, Variable.NULL);
    }

    compiler.endConstruct();
    compiler.call(ECondition.TRUE, compiler.getTarget(), q);

    compiler.move(compiler.getTarget(), Variable.TOP_OF_STACK);
  }
}

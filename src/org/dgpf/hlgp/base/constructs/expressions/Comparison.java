/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.expressions.Comparison.java
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

package org.dgpf.hlgp.base.constructs.expressions;

import org.dgpf.hlgp.base.compiler.HLGPCompiler;
import org.dgpf.lgp.base.ECondition;
import org.dgpf.lgp.base.EIndirection;
import org.dgpf.lgp.base.programBuilder.Variable;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * This instruction is an comparison expression
 * 
 * @author Thomas Weise
 */
public class Comparison extends ArithExpression {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the comparison to be performed
   */
  EComparison m_comp;

  /**
   * Create a new add expression
   * 
   * @param comp
   *          the comparison to be performed
   * @param operands
   *          the operands
   */
  public Comparison(final EComparison comp, final Node[] operands) {
    super(operands);
    this.m_comp = comp;
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
    if (this.m_comp != null)
      this.m_comp.appendQualifiedName(sb);
    else
      sb.append((Object) null);
    sb.append(',');
    sb.append(' ');
    super.javaParametersToStringBuilder(sb, indent);
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
    if (this.size() > 1) {
      sb.append('(');
      nodeToStringBuilder(this.get(0), sb, indent);
      sb.append(this.m_comp.m_txt);
      nodeToStringBuilder(this.get(1), sb, indent);
      sb.append(')');
    }
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
    return ComparisonFactory.COMPARISON_FACTORY;
  }

  /**
   * Perform the compilation
   * 
   * @param dest
   *          the destination variable
   * @param src1
   *          the first source variable
   * @param src2
   *          the second source variable
   * @param c
   *          the compiler
   */
  @Override
  protected void doCompile(final Variable dest, final Variable src1,
      final Variable src2, final HLGPCompiler c) {
    EIndirection ei;
    Variable tv;

    ei = dest.getIndirection();
    if ((ei == EIndirection.LOCAL) || (ei == EIndirection.GLOBAL)) {
      tv = dest;
    } else {
      c.beginConstruct();
      tv = c.allocateVariable(EIndirection.LOCAL, 0, true);
    }

    c.compare(tv, src1, src2);
    c.jump(this.m_comp.m_cond, tv, +3);
    c.move(dest, Variable.NULL);
    c.jump(ECondition.TRUE, Variable.NULL, +2);
    c.move(dest, Variable.FFFF);

    if (tv != dest)
      c.endConstruct();
  }
}

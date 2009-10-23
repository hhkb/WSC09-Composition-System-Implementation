/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.expressions.And.java
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
import org.dgpf.lgp.base.programBuilder.Variable;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * This instruction is an and expression
 * 
 * @author Thomas Weise
 */
public class And extends ArithExpression {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the text
   */
  private static final char[] TEXT = " & ".toCharArray(); //$NON-NLS-1$

  /**
   * Create a new add expression
   * 
   * @param operands
   *          the operands
   */
  public And(final Node[] operands) {
    super(operands);
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
    int i, j;
    i = this.size();
    if (i >= 2) {
      sb.append('(');
      nodeToStringBuilder(this.get(0), sb, 0);
      for (j = 1; j < i; j++) {
        sb.append(TEXT);
        nodeToStringBuilder(this.get(j), sb, 0);
      }
      sb.append(')');
    } else
      sb.append('0');
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
    return ArithExpressionFactories.AND_FACTORY;
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
    c.and(dest, src1, src2);
  }
}

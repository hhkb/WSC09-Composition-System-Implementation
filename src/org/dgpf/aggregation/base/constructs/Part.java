/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.base.constructs.Part.java
 * Last modification: 2007-03-27
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

package org.dgpf.aggregation.base.constructs;

import org.dgpf.aggregation.base.Command;
import org.dgpf.symbolicRegression.ComputationContext;
import org.dgpf.symbolicRegression.vector.real.RealExpression;
import org.sfc.text.TextUtils;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * PartA contain the information
 * 
 * @author Thomas Weise
 */
public class Part extends Command {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * Create a new part
   * 
   * @param sub
   *          the sub-expressions
   */
  Part(final Node[] sub) {
    super(sub);
  }

  /**
   * compute the value of this expression
   * 
   * @param x
   *          the input data
   * @param c
   *          the context
   * @return the value of the expression
   */
  @Override
  public double compute(final double[] x, final ComputationContext<?> c) {
    int i, j;

    j = this.size();
    for (i = 0; i < j; i++) {
      ((RealExpression) (this.get(i))).compute(x, c);
    }

    return 0d;
  }

  /**
   * Transform this node into its human readable representation.
   * 
   * @param sb
   *          the string builder to write to
   * @param indent
   *          the indent
   */
  @Override
  protected void toStringBuilder(final StringBuilder sb, final int indent) {
    sb.append('[');
    sb.append(TextUtils.LINE_SEPARATOR);
    TextUtils.appendSpaces(sb, indent + 2);
    this.childrenToStringBuilder(sb, indent);
    sb.append(TextUtils.LINE_SEPARATOR);
    TextUtils.appendSpaces(sb, indent);
    sb.append(']');
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
    return PartFactory.PART_FACTORY;
  }

  /**
   * Write the text of this node to the string builder.
   * 
   * @param sb
   *          the string builder to write to
   */
  @Override
  protected void textToStringBuilder(final StringBuilder sb) {//
  }
}

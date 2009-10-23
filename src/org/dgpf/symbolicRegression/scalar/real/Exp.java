/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-05-10
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.symbolicRegression.scalar.real.Exp.java
 * Last modification: 2008-05-10
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

package org.dgpf.symbolicRegression.scalar.real;

import org.dgpf.symbolicRegression.ComputationContext;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.refimpl.genomes.tree.NodeFactory;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * the exp operation
 * 
 * @author Thomas Weise
 */
public class Exp extends RealExpression {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the and factory
   */
  public static final NodeFactory EXP_FACTORY = new RealFactory(Exp.class,
      1) {
    /**
     * the serial version uid
     */
    private static final long serialVersionUID = 1;

    /**
     * Create a new node using the specified children.
     * 
     * @param children
     *          the children of the node to be, or <code>null</code> if
     *          no children are wanted
     * @param random
     *          the randomizer to be used for the node's creation
     * @return the new node
     */
    public Node create(final Node[] children, final IRandomizer random) {
      return new Exp(children);
    }

  };

  /**
   * Create a new program
   * 
   * @param children
   *          the child nodes
   */
  public Exp(final Node[] children) {
    super(children);
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
  public double compute(final double x, final ComputationContext<?> c) {
    double a;

    a = ((RealExpression) (this.get(0))).compute(x, c);

    return this.doUnaryCheck(a, Math.exp(a), c);
  }

  /**
   * Transform this node into its human readable representation.
   * 
   * @param sb
   *          the string builder to write to
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {

    sb.append("e^(");//$NON-NLS-1$
    this.get(0).toStringBuilder(sb);

    sb.append(')');
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
    return EXP_FACTORY;
  }
}

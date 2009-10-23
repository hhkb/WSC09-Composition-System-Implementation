/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-05-10
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.symbolicRegression.scalar.real.Constant.java
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
import org.sfc.math.Mathematics;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.refimpl.genomes.tree.NodeFactory;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * a constant
 * 
 * @author Thomas Weise
 */
public class Constant extends RealExpression {

  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the and factory
   */
  public static final NodeFactory CONSTANT_FACTORY = new NodeFactory(
      Constant.class, 0, 0) {
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
      return CONSTANTS[random.nextInt(CONSTANTS.length)];
    }

    /**
     * This method performs one single mutation on the nodes values. It
     * must not change its children or any other the members of
     * <code>Node</code>.
     * 
     * @param source
     *          The source node.
     * @param random
     *          The randomizer to be used.
     * @return The resulting node or the source node if no mutation was
     *         available.
     * @throws NullPointerException
     *           if <code>source==null||random==null</code>.
     */
    @Override
    public Node mutate(final Node source, final IRandomizer random) {
      Constant c;

      for (;;) {
        c = CONSTANTS[random.nextInt(CONSTANTS.length)];
        if (c != source)
          return c;
      }
    }

  };

  /**
   * the constants
   */
  static final Constant[] CONSTANTS;

  static {
    double[] v;
    int s, l;

    v = Mathematics.listConstants();
    s = v.length;
    l = (s + 21);
    CONSTANTS = new Constant[l];

    for (--l; l >= s; l--) {
      CONSTANTS[l] = new Constant(1 << (l - s));
    }

    for (--s; s >= 0; s--) {
      CONSTANTS[s] = new Constant(v[s]);
    }
  }

  /**
   * the constant value
   */
  double m_value;

  /**
   * Create a new program
   * 
   * @param value
   *          the constant value
   */
  public Constant(double value) {
    super(null);
    this.m_value = value;
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
    sb.append(this.m_value);
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
    return this.m_value;
  }

  /**
   * Transform this node into its human readable representation.
   * 
   * @param sb
   *          the string builder to write to
   */
  @Override
  public final void toStringBuilder(final StringBuilder sb) {
    sb.append(this.m_value);
  }

  /**
   * Obtain the factory which deals with nodes of the same type as this
   * node.
   * 
   * @return the factory which deals with nodes of the same type as this
   *         node
   */
  @Override
  public final INodeFactory getFactory() {
    return CONSTANT_FACTORY;
  }
}

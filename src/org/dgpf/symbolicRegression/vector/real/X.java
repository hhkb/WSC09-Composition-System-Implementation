/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-05-10
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.symbolicRegression.vector.real.X.java
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

package org.dgpf.symbolicRegression.vector.real;

import org.dgpf.symbolicRegression.ComputationContext;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.NodeFactory;

/**
 * a constant
 * 
 * @author Thomas Weise
 */
public class X extends RealExpression {

  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the index
   */
  private final int m_index;

  /**
   * the node factory
   */
  private final NodeFactory m_factory;

  /**
   * Create a new program
   * 
   * @param index
   *          the index
   * @param factory
   *          the node factory
   */
  public X(final int index, final NodeFactory factory) {
    super(null);
    this.m_index = index;
    this.m_factory = factory;
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
    return x[this.m_index];
  }

  /**
   * Transform this node into its human readable representation.
   * 
   * @param sb
   *          the string builder to write to
   */
  @Override
  public final void toStringBuilder(final StringBuilder sb) {
    sb.append('[');
    sb.append(this.m_index);
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
  public final INodeFactory getFactory() {
    return this.m_factory;
  }
}

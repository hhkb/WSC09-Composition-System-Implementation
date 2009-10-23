/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-05-10
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.symbolicRegression.vector.real.RealExpression.java
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
import org.dgpf.symbolicRegression.RealExpressionBase;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * The base class for real expressions
 * 
 * @author Thomas Weise
 */
public abstract class RealExpression extends RealExpressionBase {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new boolean expression
   */
  protected RealExpression() {
    super();
  }

  /**
   * Create a new program
   * 
   * @param children
   *          the child nodes
   */
  protected RealExpression(final Node[] children) {
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
  public abstract double compute(final double[] x, final ComputationContext<?> c);
}

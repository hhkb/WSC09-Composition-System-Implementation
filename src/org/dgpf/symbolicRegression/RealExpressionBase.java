/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-05-10
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.symbolicRegression.scalar.real.RealExpression.java
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

package org.dgpf.symbolicRegression;

import org.sfc.math.Mathematics;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * The base class for real expressions
 * 
 * @author Thomas Weise
 */
public abstract class RealExpressionBase extends Node {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new boolean expression
   */
  protected RealExpressionBase() {
    super();
  }

  /**
   * Create a new program
   * 
   * @param children
   *          the child nodes
   */
  protected RealExpressionBase(final Node[] children) {
    super(children);
  }

  /**
   * perform a binary check
   * 
   * @param a
   *          the first parameter
   * @param b
   *          the second parameter
   * @param r
   *          the result
   * @param c
   *          the context
   * @return the result
   */
  protected final double doBinaryCheck(final double a, final double b,
      final double r, final ComputationContext<?> c) {

    if (Mathematics.isNumber(r)) {
      if (checkEqual(a, r) || checkEqual(b, r)) {
        c.negligible(this);
      }
      return r;
    }
    if (Mathematics.isNumber(a) && Mathematics.isNumber(b)) {
      c.setError(this);
    }
    return Double.NaN;
  }

  /**
   * perform a binary check
   * 
   * @param a
   *          the parameter
   * @param r
   *          the result
   * @param c
   *          the context
   * @return the result
   */
  protected final double doUnaryCheck(final double a, final double r,
      final ComputationContext<?> c) {

    if (Mathematics.isNumber(r)) {
      if (checkEqual(a, r)) {
        c.negligible(this);
      }
      return r;
    }
    if (Mathematics.isNumber(a)) {
      c.setError(this);
    }
    return Double.NaN;
  }

  /**
   * Check whether two numbers are equal
   * 
   * @param a
   *          the first number
   * @param b
   *          the second number
   * @return the return value
   */
  private static final boolean checkEqual(final double a, final double b) {
    if (a == b)
      return true;

    if (a == 0d)
      return (Math.abs(b) <= Mathematics.EPS);
    if (b == 0d)
      return (Math.abs(a) <= Mathematics.EPS);

    return ((Math.abs(a - b) / Math.min(Math.abs(a), Math.abs(b))) < 1e-5d);
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
    this.toStringBuilder(sb);
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    super.toStringBuilder(sb, 0);
  }
}

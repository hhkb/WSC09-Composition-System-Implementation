/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.oldAggregation.constructs.Formula.java
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
import org.dgpf.vm.base.IVirtualMachineParameters;
import org.sfc.math.Mathematics;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * Formulas are the active part of a program.
 * 
 * @author Thomas Weise
 */
public class Formula extends Command {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * the variable index
   */
  int m_index;

  /**
   * Create a new formula
   * 
   * @param index
   *          the variable index
   * @param sub
   *          the sub-expressions
   */
  public Formula(final int index, final Node[] sub) {
    super(sub);
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
    super.javaParametersToStringBuilder(sb, indent);
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
    if (this.size() > 0) {
      return x[this.m_index] = ((RealExpression) (this.get(0))).compute(x,
          c);
    }
    return 0d;
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
    return FormulaFactory.FORMULA_FACTORY;
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
    if (this.size() > 0) {
      // sb.append('(');
      // if ((this.m_index < 26) && (this.m_index >= 0))
      // sb.append((char) ('a' + this.m_index));
      // else {
      sb.append('[');
      sb.append(this.m_index);
      sb.append(']');
      // }
      sb.append(' ');
      sb.append('=');
      sb.append(' ');
      nodeToStringBuilder(this.get(0), sb, indent);
      sb.append(')');
    }
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
    return super.equals(o) && (((Formula) o).m_index == this.m_index);
  }

  /**
   * Postprocess this command using the virtual machine parameters.
   * 
   * @param p
   *          the virtual machine parameters
   */
  @Override
  public void postProcess(final IVirtualMachineParameters<double[]> p) {
    this.m_index = Mathematics.modulo(this.m_index, p.getMemorySize());
    super.postProcess(p);
  }
}

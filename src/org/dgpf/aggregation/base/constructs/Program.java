/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.base.constructs.Program.java
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
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * HLGPProgramCompiler contain the information
 * 
 * @author Thomas Weise
 */
public class Program extends Command {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * the empty program
   */
  public static final Program EMPTY_PROGRAM = new Program(null);

  /**
   * Create a new program
   * 
   * @param sub
   *          the sub-expressions
   */
  public Program(final Node[] sub) {
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
    if (this.size() > 1) {
      return ((RealExpression) (this.get(1))).compute(x, c);
    }
    if (this.size() > 0) {
      return ((RealExpression) (this.get(0))).compute(x, c);
    }
    return 0d;
  }

  /**
   * Compute the value of this formula.
   * 
   * @param values
   *          the variables
   * @param c
   *          the context
   */
  public void init(final double[] values, final ComputationContext<?> c) {
    if (this.size() > 1) {
      ((RealExpression) (this.get(0))).compute(values, c);
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
    return ProgramFactory.PROGRAM_FACTORY;
  }

  /**
   * Determine whether the given class can arbitrarily be nested.
   * 
   * @param clazz
   *          the class to possibly be nested
   * @return <code>true</code> if and only if this class can arbitrarily
   *         be nested.
   */
  public static final boolean canBeArbitrarilyNested(final Class<?> clazz) {
    return (!((Program.class.isAssignableFrom(clazz) || Part.class
        .isAssignableFrom(clazz))));
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

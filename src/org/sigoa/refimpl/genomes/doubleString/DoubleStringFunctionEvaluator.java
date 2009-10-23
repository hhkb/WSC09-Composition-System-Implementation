/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-01-06
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.doubleString.DoubleStringFunctionEvaluator.java
 * Last modification: 2007-01-06
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

package org.sigoa.refimpl.genomes.doubleString;

import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.evaluation.IEvaluator;

/**
 * This evaluator is specialized to evaluate sets of mathematical
 * functions.
 *
 * @see IDoubleStringFunction
 * @author Thomas Weise
 */
public class DoubleStringFunctionEvaluator implements IEvaluator<double[]> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the functions.
   */
  private final IDoubleStringFunction[] m_functions;

  /**
   * Create a new double vector function evaluator.
   *
   * @param functions
   *          the functions to be evaluated.
   * @throws NullPointerException
   *           if <code>functions==null</code>
   * @throws IllegalArgumentException
   *           if <code>functions.length &lt;= 0</code> or if the
   *           function array contains any <code>null</code> field.
   */
  public DoubleStringFunctionEvaluator(
      final IDoubleStringFunction[] functions) {
    int i;

    i = functions.length;
    if (i <= 0)
      throw new IllegalArgumentException();
    for (--i; i >= 0; i--) {
      if (functions[i] == null)
        throw new IllegalArgumentException();
    }
    this.m_functions = functions.clone();
  }

  /**
   * Evaluate an individual by computing all its objective values.
   *
   * @param individual
   *          The individual to be evaluated.
   * @throws NullPointerException
   *           if the individual or its phenotype or the host is
   *           <code>null</code> or if no simulation manager is
   *           available.
   */
  public void evaluate(final IIndividual<?, double[]> individual) {
    int i;
    IDoubleStringFunction[] f;
    double[] vals;

    vals = individual.getPhenotype();
    f = this.m_functions;
    for (i = (f.length - 1); i >= 0; i--) {
      individual.setObjectiveValue(i, f[i].compute(vals));
    }
  }

  /**
   * Obtain the count of the objective values which are set by this
   * evaluator.
   *
   * @return The count of objective functions applied.
   */
  public int getObjectiveValueCount() {
    return this.m_functions.length;
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-29
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.refimpl.utils.testseries.successFilters.FirstObjectiveZeroFilter.java
 * Last modification: 2007-11-29
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

package org.sigoa.refimpl.utils.testSeries.successFilters;

import java.io.Serializable;

import org.sigoa.refimpl.utils.testSeries.ISuccessFilter;
import org.sigoa.refimpl.utils.testSeries.IndividualEvaluator;
import org.sigoa.spec.go.IIndividual;

/**
 * A success filter for the gcd problem
 * 
 * @author Thomas Weise
 */
public class FirstObjectiveZeroFilter implements
    ISuccessFilter<Serializable> {

  /**
   * the evaluator
   */
  private final IndividualEvaluator m_eval;

  /**
   * create a new gcd success filter
   * 
   * @param eval
   *          the evaluator
   */
  public FirstObjectiveZeroFilter(final IndividualEvaluator eval) {
    super();

    this.m_eval = eval;
  }

  /**
   * Determine whether an output of a run would be considered as solution
   * judging by its objective values.
   * 
   * @param objectives
   *          an array containing the objective values of the individual
   * @return <code>true</code> if and only if the objective values
   *         indicate success, <code>false</code> otherwise
   */
  public boolean isSuccess(final double[] objectives) {
    return (objectives[0] <= 0d);
  }

  /**
   * Determine whether an output of a run would be considered as solution
   * judging by its objective values.
   * 
   * @param individual
   *          the individual record
   * @return <code>true</code> if and only if the objective values
   *         indicate success, <code>false</code> otherwise
   */
  public boolean isSuccess(final IIndividual<?, Serializable> individual) {
    return (individual.getObjectiveValue(0) <= 0d);
  }

  /**
   * Determine whether a successful individual is overfitted or not.
   * 
   * @param phenotype
   *          the phenotype to check
   * @return <code>true</code> if the phenotype does not represent a
   *         general solution to the problem, <code>false</code>
   *         otherwise
   */
  @SuppressWarnings("unchecked")
  public boolean isOverfitted(final Serializable phenotype) {
    return !(this.isSuccess((IIndividual) (this.m_eval.evaluate(null,
        phenotype))));
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-08
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.evaluation.ICompetitionEvaluator.java
 * Last modification: 2007-11-08
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

package org.sigoa.spec.go.evaluation;

import java.io.Serializable;

import org.sigoa.spec.go.IIndividual;

/**
 * The competition-based evaluator.
 * 
 * @param <PP>
 *          The phenotype of the individuals to be evaluated.
 * @author Thomas Weise
 */
public interface ICompetitionEvaluator<PP extends Serializable> extends
    IEvaluator<PP> {
  /**
   * Evaluate an individual by letting it compete against itself.
   * 
   * @param individual
   *          The individual to be evaluated.
   * @throws NullPointerException
   *           if the individual or its phenotype or the host is
   *           <code>null</code> or if no simulation manager is
   *           available.
   */
  public abstract void evaluate(final IIndividual<?, PP> individual);

  /**
   * At least 2: the first objective value is always the number of losses
   * and the second one the number of draws.
   * 
   * @return The count of objective functions applied.
   */
  public abstract int getObjectiveValueCount();

  /**
   * Let two contestants compete with each other. In the result, the first
   * two objective values of the contestants may be increased.
   * 
   * @param contestant1
   *          the first contestant
   * @param contestant2
   *          the second contestant
   */
  public abstract void compete(final IIndividual<?, PP> contestant1,
      final IIndividual<?, PP> contestant2);
}

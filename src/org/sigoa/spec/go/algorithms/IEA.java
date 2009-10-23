/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-02
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.algorithms.ea.IEA.java
 * Last modification: 2006-12-02
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

package org.sigoa.spec.go.algorithms;

import java.io.Serializable;

import org.sigoa.spec.go.IOptimizer;
import org.sigoa.spec.go.reproduction.ICrossoverParameters;
import org.sigoa.spec.go.reproduction.IMutatorParameters;

/**
 * This interface is common to all evolutionary algorithms.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public interface IEA<G extends Serializable, PP extends Serializable>
    extends IOptimizer<G, PP>, IMutatorParameters, ICrossoverParameters,
    IIterativeAlgorithm {

  /**
   * Obtain the current population size.
   *
   * @return the current population size of the evolutionary algorithm
   */
  public abstract int getPopulationSize();

  /**
   * Obtain the population size that will be set in the next generation.
   *
   * @return the next population size of the evolutionary algorithm
   */
  public abstract int getNextPopulationSize();

  /**
   * Set the population size for the next generation. Setting this property
   * will take effect in the next generation.
   *
   * @param size
   *          The next population size.
   * @throws IllegalArgumentException
   *           if <code>size&lt;0</code>
   */
  public abstract void setNextPopulationSize(final int size);

}

/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 1006-11-22 06:18:01
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.IOptimizationInfo.java
 * Version          : 1.0.0
 * Last modification: 1006-11-22
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

package org.sigoa.spec.go;

import java.io.Serializable;

import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.evaluation.IEvaluator;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;

/**
 * The optimization information record.
 *
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 * @version 1.0.0
 */
public interface IOptimizationInfo<G extends Serializable, PP extends Serializable>
    extends Serializable {
  /**
   * Obtain the default comparator used in order to compare two
   * individuals.
   *
   * @return The default comparator used in order to compare two
   *         individuals.
   */
  public abstract IComparator getComparator();

  /**
   * Obtain the default embryogeny to be used for the optimization job.
   *
   * @return the default embryogeny to be used for the optimization job
   */
  public abstract IEmbryogeny<G, PP> getEmbryogeny();

  /**
   * Obtain the default evaluator used in order to determine the objective
   * values of an individual.
   *
   * @return The evaluator used in order to determine the objective values
   *         of an individual.
   */
  public abstract IEvaluator<PP> getEvaluator();

  /**
   * Obtain the default creator which should be used to create new elements
   * in this optimization job.
   *
   * @return the default creator to be used for this optimization process
   */
  public abstract ICreator<G> getCreator();

  /**
   * Obtain the default mutator which should be used to create new elements
   * in this optimization job.
   *
   * @return the default mutator to be used for this optimization process
   */
  public abstract IMutator<G> getMutator();

  /**
   * Obtain the default recombiner which should be used to create new
   * elements in this optimization job.
   *
   * @return the default recombiner to be used for this optimization
   *         process
   */
  public abstract ICrossover<G> getCrossover();

  /**
   * Obtain the individual factory to be used in this optimization process.
   *
   * @return the individual factory to be used in this optimization process
   */
  public abstract IIndividualFactory<G, PP> getIndividualFactory();
}

/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 ${time}
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.IIndividual.java
 * Last modification: 2006-11-22
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

/**
 * An individual records holds the genotype, the phenotype, the objective
 * function values and the assigned fitnesses of an individual.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 * @version 1.0.0
 */
public interface IIndividual<G extends Serializable, PP extends Serializable>
    extends Serializable, Cloneable {

  /**
   * Clear all the fitness and objective values which means setting them to
   * <code>OptimizationUtils.WORST</code>.
   *
   * @see #getFitness()
   * @see #getObjectiveValue(int)
   * @see #clearFitness()
   * @see #clear()
   */
  public abstract void clearEvaluation();

  /**
   * Clear this individual record. Calling this method resets all fields of
   * this individual record.
   *
   * @see #clearEvaluation()
   */
  public abstract void clear();

  /**
   * Clear the fitness of this individual.
   * @see #getFitness()
   * @see #clearEvaluation()
   */
  public abstract void clearFitness();

  /**
   * Obtain the genotype stored in the individual record.
   *
   * @return The genotype stored in the individual record or
   *         <code>null</code> if no genotype is currently stored.
   * @see #setGenotype(Serializable)
   */
  public abstract G getGenotype();

  /**
   * Store a genotype in this individual record.
   *
   * @param genotype
   *          The genotype to be stored.
   * @see #getGenotype()
   */
  public abstract void setGenotype(final G genotype);

  /**
   * Obtain the phenotype stored in the individual record.
   *
   * @return The phenotype stored in the individual record or
   *         <code>null</code> if no phenotype has been stored or
   *         produced yet.
   * @see #setPhenotype(Serializable)
   */
  public abstract PP getPhenotype();

  /**
   * Store a phenotype in this individual record.
   *
   * @param phenotype
   *          The phenotype to be stored.
   * @see #getPhenotype()
   */
  public abstract void setPhenotype(final PP phenotype);

  /**
   * Obtain the count of the objective values rating this individual which
   * is also the count of objective functions applied.
   *
   * @return The count of objective functions applied.
   * @see #getObjectiveValue(int)
   */
  public abstract int getObjectiveValueCount();

  /**
   * Obtain the objective value produced by the objective function at the
   * specified index.
   *
   * @param index
   *          The index of the objective function providing this objective
   *          value. This value must be >= 0 and <
   *          {@link #getObjectiveValueCount()}.
   * @return The objective value stored at the specified index, the smaller
   *         it is, the fitter is the individual according to the specified
   *         objective function.
   * @throws ArrayIndexOutOfBoundsException
   *           if <code>index</code> &lt; 0 or &gt;=
   *           <code>getObjectiveValueCount</code>.
   * @see #getObjectiveValueCount()
   * @see #setObjectiveValue(int, double)
   */
  public abstract double getObjectiveValue(final int index);

  /**
   * Set the objective value produced by the objective function at the
   * specified index.
   *
   * @param index
   *          The index of the objective function providing this objective
   *          value. This value must be >= 0 and <
   *          {@link #getObjectiveValueCount()}.
   * @param value
   *          The objective value stored at the specified index, the
   *          smaller it is, the fitter is the individual according to the
   *          specified objective function.
   * @throws ArrayIndexOutOfBoundsException
   *           if <code>index</code> &lt; 0 or &gt;=
   *           <code>getObjectiveValueCount</code>.
   * @throws IllegalArgumentException
   *           if <code>value</code> is NaN.
   * @see #getObjectiveValueCount()
   * @see #getObjectiveValue(int)
   */
  public abstract void setObjectiveValue(final int index,
      final double value);

  /**
   * Obtain the fitness of this individual. This value will be computed
   * from the objective values by a fitness assignment process.
   *
   * @return The fitness of this individual, computed from the objective
   *         values by a fitness assignment process.
   * @see #setFitness(double)
   */
  public abstract double getFitness();

  /**
   * Set the fitness value of this individual as result of a computation
   * from the objective values by a fitness assignment process.
   *
   * @param fitness
   *          The fitness value to be assigned to this individual.
   * @throws IllegalArgumentException
   *           if <code>fitness</code> is NaN or infinite.
   * @see #getFitness()
   */
  public abstract void setFitness(final double fitness);

}

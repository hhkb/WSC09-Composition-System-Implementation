/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.GlobalOptimizationInfo.java
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

package org.sigoa.refimpl.go;

import java.io.Serializable;

import org.sigoa.refimpl.go.comparators.FitnessComparator;
import org.sigoa.refimpl.go.comparators.ParetoComparator;
import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.IIndividualFactory;
import org.sigoa.spec.go.IOptimizationInfo;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.evaluation.IEvaluator;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;

/**
 * The default global optimization record implementation.
 *
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class OptimizationInfo<G extends Serializable, PP extends Serializable>
    extends ImplementationBase<G,PP> implements IOptimizationInfo<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The comparator used by the optimization info record.
   */
  private final IComparator m_comparator;

  /**
   * The breeder used.
   */
  private final IEmbryogeny<G, PP> m_embryogeny;

  /**
   * The evaluator used.
   */
  private final IEvaluator<PP> m_evaluator;

  /**
   * The creator used.
   */
  private final ICreator<G> m_creator;

  /**
   * The mutator used.
   */
  private final IMutator<G> m_mutator;

  /**
   * The crossover instance used.
   */
  private final ICrossover<G> m_crossover;

  /**
   * the individual factory
   */
  private final IIndividualFactory<G, PP> m_individualFactory;

  /**
   * Create a new optimization information record.
   *
   * @param evaluator
   *          The evaluator to be used in order to determine the objective
   *          function values of the individuals. This parameter must not
   *          be <code>null</code>
   * @param embryogeny
   *          The breeder instance to be used in order to create a
   *          phenotype from a genotype.
   * @param creator
   *          The creator instance to be used in order to create new
   *          genotype instances. This parameter must not be
   *          <code>null</code>
   * @param mutator
   *          The mutator which creates one new genotype instance from an
   *          existing one. This parameter must not be <code>null</code>
   * @param crossover
   *          The crossover instance used to create one new genotype
   *          instance from two existing ones. This parameter must not be
   *          <code>null</code>
   * @throws NullPointerException
   *           if the
   *           <code>evaluator==null || breeder==null || creator==null || mutator==null || crossover==null</code>.
   * @throws IllegalArgumentException
   *           if
   *           <code>(comparator == FitnessComparator.FITNESS_COMPARATOR)</code>
   */
  public OptimizationInfo(final IEvaluator<PP> evaluator,
      final IEmbryogeny<G, PP> embryogeny, final ICreator<G> creator,
      final IMutator<G> mutator, final ICrossover<G> crossover) {
    this(evaluator, null, embryogeny, creator, mutator, crossover, null);
  }

  /**
   * Create a new optimization information record.
   *
   * @param comparator
   *          The comparator to be used for the optimization. If this is
   *          <code>null</code>, an instance of the
   *          <code>ParetoComparator</code> is used.
   * @param evaluator
   *          The evaluator to be used in order to determine the objective
   *          function values of the individuals. This parameter must not
   *          be <code>null</code>
   * @param embryogeny
   *          The breeder instance to be used in order to create a
   *          phenotype from a genotype.
   * @param creator
   *          The creator instance to be used in order to create new
   *          genotype instances. This parameter must not be
   *          <code>null</code>
   * @param mutator
   *          The mutator which creates one new genotype instance from an
   *          existing one. This parameter must not be <code>null</code>
   * @param crossover
   *          The crossover instance used to create one new genotype
   *          instance from two existing ones. This parameter must not be
   *          <code>null</code>
   * @param individualFactory
   *          the individual factory to be used, if <code>null</code>, a
   *          default factory is used
   * @throws NullPointerException
   *           if the
   *           <code>evaluator==null || breeder==null || creator==null || mutator==null || crossover==null</code>.
   * @throws IllegalArgumentException
   *           if
   *           <code>(comparator == FitnessComparator.FITNESS_COMPARATOR)</code>
   */
  @SuppressWarnings("unchecked")
  public OptimizationInfo(final IEvaluator<PP> evaluator,
      final IComparator comparator, final IEmbryogeny<G, PP> embryogeny,
      final ICreator<G> creator, final IMutator<G> mutator,
      final ICrossover<G> crossover,
      final IIndividualFactory<G, PP> individualFactory) {
    super();

    IIndividualFactory<G, PP> x;

    if ((evaluator == null) || (embryogeny == null) || (creator == null)
        || (mutator == null) || (crossover == null))
      throw new NullPointerException();
    if (comparator == FitnessComparator.FITNESS_COMPARATOR)
      throw new IllegalArgumentException();

    this.m_evaluator = evaluator;

    this.m_comparator = ((comparator != null) ? comparator
        : ParetoComparator.PARETO_COMPARATOR);

    this.m_embryogeny = embryogeny;
    this.m_creator = creator;
    this.m_mutator = mutator;
    this.m_crossover = crossover;

    if (individualFactory != null)
      this.m_individualFactory = individualFactory;
    else {
      x = ((IIndividualFactory<G, PP>) (IndividualFactory.DEFAULT_INDIVIDUAL_FACTORY));
      this.m_individualFactory = x;
    }
  }

  /**
   * Obtain the comparator used in order to compare two individuals.
   *
   * @return The comparator used in order to compare two individuals.
   */
  @Override
  public IComparator getComparator() {
    return this.m_comparator;
  }

  /**
   * Obtain the default embryogeny to be used for the optimization job.
   *
   * @return the default embryogeny to be used for the optimization job
   */
  @Override
  public IEmbryogeny<G, PP> getEmbryogeny() {
    return this.m_embryogeny;
  }

  /**
   * Obtain the evaluator used in order to determine the objective values
   * of an individual.
   *
   * @return The evaluator used in order to determine the objective values
   *         of an individual.
   */
  @Override
  public IEvaluator<PP> getEvaluator() {
    return this.m_evaluator;
  }

  /**
   * Obtain the default creator which should be used to create new elements
   * in this optimization job.
   *
   * @return the default creator to be used for this optimization process
   */
  @Override
  public ICreator<G> getCreator() {
    return this.m_creator;
  }

  /**
   * Obtain the default mutator which should be used to create new elements
   * in this optimization job.
   *
   * @return the default mutator to be used for this optimization process
   */
  @Override
  public IMutator<G> getMutator() {
    return this.m_mutator;
  }

  /**
   * Obtain the default recombiner which should be used to create new
   * elements in this optimization job.
   *
   * @return the default recombiner to be used for this optimization
   *         process
   */
  @Override
  public ICrossover<G> getCrossover() {
    return this.m_crossover;
  }

  /**
   * Obtain the individual factory to be used in this optimization process.
   *
   * @return the individual factory to be used in this optimization process
   */
  @Override
  public IIndividualFactory<G, PP> getIndividualFactory() {
    return this.m_individualFactory;
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.benchmark.GPBenchmarkTestSeries.java
 * Last modification: 2007-10-22
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

package test.org.dgpf.benchmark;

import java.util.Iterator;
import java.util.List;

import org.dgpf.benchmark.GPBenchmarkEvaluator;
import org.dgpf.benchmark.GPBenchmarkSuccessFilter;
import org.dgpf.benchmark.TunableModel;
import org.sfc.collections.CollectionUtils;
import org.sigoa.refimpl.genomes.bitString.ExactBitString;
import org.sigoa.refimpl.genomes.bitString.ExactBitStringEditor;
import org.sigoa.refimpl.genomes.string.StringCreator;
import org.sigoa.refimpl.genomes.string.StringEditor;
import org.sigoa.refimpl.genomes.string.StringOneElementMutator;
import org.sigoa.refimpl.genomes.string.VariableLengthStringOnePointCrossover;
import org.sigoa.refimpl.go.comparators.ParetoComparator;
import org.sigoa.refimpl.utils.testSeries.EDataGrouping;
import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.evaluation.IEvaluator;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.simulation.ISimulationProvider;

import test.org.dgpf.DGPFTestSeries;

/**
 * the base class for the genetic programming benchmark test series
 * 
 * @author Thomas Weise
 */
public class GPBenchmarkTestSeries extends DGPFTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the maximum bytes per genotype
   */
  public static final int MAX_GENOTYPE_LEN = 2000;

  /**
   * the string editor used by the reproduction operations
   */
  public static final StringEditor<ExactBitString> BIT_STRING_EDITOR = new ExactBitStringEditor(
  // 8, 1, MAX_GENOTYPE_LEN);

      1, 8 * 1, 8 * MAX_GENOTYPE_LEN);

  /**
   * the model
   */
  private final TunableModel m_model;

  /**
   * the evaluator
   */
  private final GPBenchmarkEvaluator m_eval;

  /**
   * Create a new test series.
   * 
   * @param dir
   *          the directory to store the test data in
   * @param grouping
   *          how the data should be grouped
   * @param titles
   *          the titles of the test series parameters
   * @param iterator
   *          the parameter iterator
   */
  protected GPBenchmarkTestSeries(final Object dir,
      final EDataGrouping grouping, final String[] titles,
      final Iterator<Object[]> iterator) {
    super(dir, grouping, -1, titles, iterator);

    this.m_model = new TunableModel();
    this.m_eval = new GPBenchmarkEvaluator(this.m_model);

  }

  /**
   * Create the objective functions
   * 
   * @return the list of objective functions
   */
  @Override
  protected List<?> createObjectives() {
    return CollectionUtils.EMPTY_LIST;
  }

  /**
   * the empty provicer
   */
  private static final ISimulationProvider[] EMPTY_PROV = new ISimulationProvider[] {};

  /**
   * Obtain the simulation provider needed.
   * 
   * @return the simulation provider needed
   */
  @Override
  protected final ISimulationProvider[] createSimulationProviders() {
    return EMPTY_PROV;
  }

  /**
   * Create the comparator*
   * 
   * @return the comparator
   */
  @Override
  protected IComparator createComparator() {
    return ParetoComparator.PARETO_COMPARATOR;
    // return new TieredParetoComparator(
    // new int[] { 1, 100 });
  }

  /**
   * create the creator
   * 
   * @return the creator
   */
  @Override
  protected ICreator<?> createCreator() {
    return new StringCreator<ExactBitString>(BIT_STRING_EDITOR);
  }

  /**
   * This method is called for setting up the parameters
   * 
   * @param parameters
   *          the parameters
   */
  @Override
  protected void setupParameters(final Object[] parameters) {
    this.m_model.recalculateInternalValues();
    super.setupParameters(parameters);
  }

  /**
   * create the mutator
   * 
   * @return the mutator
   */
  @Override
  protected IMutator<?> createMutator() {
    return new StringOneElementMutator<ExactBitString>(BIT_STRING_EDITOR);
  }

  /**
   * create the crossover
   * 
   * @return the crossover
   */
  @Override
  protected ICrossover<?> createCrossover() {
    return new VariableLengthStringOnePointCrossover<ExactBitString>(
        BIT_STRING_EDITOR);
    //
    // return new Crossover();
  }

  /**
   * Internally perform one iteration
   * 
   * @param iteration
   *          the iteration number
   */
  @Override
  protected void iteration(final int iteration) {
    this.setSuccessFilter(new GPBenchmarkSuccessFilter(this.m_model));
    super.iteration(iteration);
  }

  /**
   * Create the evaluator.
   * 
   * @return the evaluator
   */
  @Override
  protected IEvaluator<?> createEvaluator() {
    return this.m_eval;
  }

  /**
   * Returns the tunable modle.
   * 
   * @return Returns the tunable modle.
   */
  protected final TunableModel getTunableModel() {
    return this.m_model;
  }

  /**
   * Obtain the number of available processors.
   * 
   * @return the number of available processors.
   */
  @Override
  protected int getProcessorCount() {
    return Math.min(1, super.getProcessorCount());
  }

}

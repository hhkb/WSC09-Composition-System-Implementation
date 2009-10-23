/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-23
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.aggregation.AggregationTestSeries.java
 * Last modification: 2007-12-23
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

package test.org.dgpf.aggregation;

import java.util.List;

import org.dgpf.aggregation.base.DefaultAggregationLanguage;
import org.dgpf.aggregation.net.AggregationNetwork;
import org.dgpf.aggregation.net.AggregationParameters;
import org.dgpf.aggregation.net.AggregationProvider;
import org.dgpf.aggregation.net.AggregationVMFactory;
import org.dgpf.aggregation.net.genetics.AggregationNetProgramReproduction;
import org.dgpf.vm.objectives.MinimizeSizeObjective;
import org.dgpf.vm.objectives.MinimizeWeightObjective;
import org.dgpf.vm.objectives.error.MinimizeAbsErrorObjective;
import org.dgpf.vm.objectives.error.MinimizeSqrErrorObjective;
import org.sfc.collections.CollectionUtils;
import org.sigoa.refimpl.go.comparators.TieredParetoComparator;
import org.sigoa.refimpl.go.embryogeny.CopyEmbryogeny;
import org.sigoa.refimpl.utils.testSeries.EDataGrouping;
import org.sigoa.refimpl.utils.testSeries.LoggingSelection;
import org.sigoa.refimpl.utils.testSeries.successFilters.FirstObjectiveZeroFilter;
import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.simulation.ISimulationProvider;

import test.org.dgpf.DGPFTestSeries;

/**
 * The aggregation test series
 * 
 * @author Thomas Weise
 */
public class AggregationTestSeries extends DGPFTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * the minimum count of vms
   */
  public static final int MIN_VM_COUNT = 10;

  /**
   * the maximum count of vms
   */
  public static final int MAX_VM_COUNT = 15;

  /**
   * the required steps
   */
  public static final int REQUIRED_STEPS = (MAX_VM_COUNT * 30);

  /**
   * the maximum message count
   */
  public static final int MAX_MESSAGES = (REQUIRED_STEPS * 5 * MAX_VM_COUNT);

  /**
   * the parameters
   */
  public static final AggregationParameters PARAMETERS = new AggregationParameters(
      AggregationFunctions.AVG,//
      new DefaultAggregationLanguage(4),// memory size
      MIN_VM_COUNT,//
      MAX_VM_COUNT,//
      AggregationVMFactory.AGGREGATION_VM_FACTORY,//
      MAX_MESSAGES, //
      0,// the min delay
      4// the max delay
  );

  /**
   * the embryogeny
   */
  public static final IEmbryogeny<?, ?> EMBRYOGENY = CopyEmbryogeny.COPY_EMBRYOGENY;

  /**
   * the success filter
   */
  public static FirstObjectiveZeroFilter s_filter = null;

  /**
   * Create a new test series.
   * 
   * @param dir
   *          the directory to store the test data in
   */
  protected AggregationTestSeries(final Object dir) {
    super(dir, EDataGrouping.RUN,
        -1,// 
        AggregationParameterIterator.TITLES,
        AggregationParameterIterator.AGGREGATION_PARAMETER_ITERATOR);

    this.selectForLogging(LoggingSelection.RESULT_INDIVIDUALS);
    this.selectForLogging(RUN_PARAMETERS);
    this.selectForLogging(BEST_OBJECTIVES);
    this.selectForLogging(BEST_INDIVIDUALS);
    this.selectForLogging(LoggingSelection.RESULT_OBJECTIVES);
    this.selectForLogging(LoggingSelection.OBSERVE_SUCCESS);

    if (s_filter == null) {
      s_filter = new FirstObjectiveZeroFilter(this
          .createIndividualEvaluator());
    }

    this.setSuccessFilter(s_filter);
  }

  /**
   * This method is called for setting up the parameters
   * 
   * @param parameters
   *          the parameters
   */
  @Override
  protected void setupParameters(final Object[] parameters) {

    super.setupParameters(parameters);

    this.setPopulationSize(((Number) (parameters[0])).intValue());
    // this.setTestCaseCount(((Number) (parameters[1])).intValue());
    // this.setTestCasesChanging(((Boolean)
    // (parameters[2])).booleanValue());

    this.setTestCaseCount(10);
    this.setTestCasesChanging(true);

    this.setSteadyState(((Boolean) (parameters[1])).booleanValue());

    this
        .setConvergencePrevention(((Number) (parameters[2])).doubleValue());

  }

  /**
   * Obtain the simulation provider needed.
   * 
   * @return the simulation provider needed
   */
  @Override
  protected final ISimulationProvider[] createSimulationProviders() {
    return new ISimulationProvider[] { new AggregationProvider(PARAMETERS,
        this.areTestCasesChanging(), this.getTestCaseCount()) };
  }

  /**
   * Create the objective functions
   * 
   * @return the list of objective functions
   */
  @Override
  protected List<?> createObjectives() {
    List<Object> l;

    l = CollectionUtils.createList();

    // l.add(this.createFeasibilityObjective());
    l.add(new MinimizeSqrErrorObjective(REQUIRED_STEPS, false,
        AggregationNetwork.class));
    l.add(new MinimizeAbsErrorObjective(REQUIRED_STEPS, false,
        AggregationNetwork.class));
    l.add(MinimizeSizeObjective.MINIMIZE_SIZE_OBJECTIVE);
    l.add(MinimizeWeightObjective.MINIMIZE_WEIGHT_OBJECTIVE);

    return l;
  }

  /**
   * the shared comparator
   */
  public static final IComparator COMPARATOR = new TieredParetoComparator(
      new int[] { 2, 100 });

  /**
   * Create the comparator
   * 
   * @return the comparator
   */
  @Override
  protected IComparator createComparator() {
    return COMPARATOR;
  }

  /**
   * Setup the individual evaluator parameters
   */
  @Override
  protected void setupIndividualEvaluatorParameters() {
    super.setupIndividualEvaluatorParameters();
    this.setTestCaseCount(100);
    this.setTestCasesChanging(true);
  }

  /**
   * create the embryogeny
   * 
   * @return the embryogeny
   */
  @Override
  protected IEmbryogeny<?, ?> createEmbryogeny() {
    return EMBRYOGENY;
  }

  /**
   * create the creator
   * 
   * @return the creator
   */
  @Override
  protected ICreator<?> createCreator() {
    return new AggregationNetProgramReproduction(PARAMETERS);
  }

  /**
   * create the mutator
   * 
   * @return the mutator
   */
  @Override
  protected IMutator<?> createMutator() {
    return new AggregationNetProgramReproduction(PARAMETERS);
  }

  /**
   * create the crossover
   * 
   * @return the crossover
   */
  @Override
  protected ICrossover<?> createCrossover() {
    return new AggregationNetProgramReproduction(PARAMETERS);
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    new AggregationTestSeries("").start(); //$NON-NLS-1$
    // new ElectionFragletTestSeries("e:\\temp\\k\\").start(); //
    // //$NON-NLS-1$
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.CSTestSeries.java
 * Last modification: 2007-09-22
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

package test.org.dgpf.cs;

import java.util.List;

import org.dgpf.vm.net.NetworkProvider;
import org.dgpf.vm.objectives.MinimizeWeightObjective;
import org.sfc.collections.CollectionUtils;
import org.sigoa.refimpl.utils.testSeries.EDataGrouping;
import org.sigoa.refimpl.utils.testSeries.LoggingSelection;
import org.sigoa.spec.simulation.ISimulationProvider;

import test.org.dgpf.DGPFTestSeries;
import test.org.dgpf.election.ElectionTestSeries;

/**
 * The standard settings for the election problem
 * 
 * @author Thomas Weise
 */
public abstract class CSTestSeries extends DGPFTestSeries {

  /**
   * the minimum count of vms
   */
  public static final int MIN_VM_COUNT = 4;

  /**
   * the maximum count of vms
   */
  public static final int MAX_VM_COUNT = 23;

  /**
   * the maximum message count
   */
  public static final int MAX_MESSAGES = (int) (MAX_VM_COUNT
      * MAX_VM_COUNT * Math.sqrt(MAX_VM_COUNT));

  /**
   * the minimum delay
   */
  public static final int MIN_DELAY = 1;

  /**
   * the number of required steps
   */
  private final int m_requiredSteps;

  // /**
  // * The factory for simple eas
  // */
  // public static final IEAFactory EA_FACTORY =
  // ElectionTestSeries.EA_FACTORY;

  /**
   * the basic cs count
   */
  public static final int BASE_CS_COUNT = (MAX_VM_COUNT * 10);

  /**
   * the basic cs time
   */
  public static final int BASE_CS_TIME = (MAX_VM_COUNT * 3);

  /**
   * the default number of test cases
   */
  public static final int DEFAULT_TEST_CASE_COUNT = 35;

  /**
   * Create a new test series.
   * 
   * @param dir
   *          the directory to store the test data in
   * @param requiredSteps
   *          the number of required steps
   */
  protected CSTestSeries(final Object dir, final int requiredSteps) {
    super(dir, EDataGrouping.RUN, -1,// 
        null,// CSParameterIterator.TITLES,
        null// CSParameterIterator.CS_PARAMETER_ITERATOR
    );

    this.m_requiredSteps = requiredSteps;

    // this.selectForLogging(BEST_OBJECTIVES);
    this.selectForLogging(LoggingSelection.RESULT_OBJECTIVES);
    this.selectForLogging(BEST_INDIVIDUALS);
    this.selectForLogging(LoggingSelection.OBSERVE_SUCCESS);
    this.selectForLogging(LoggingSelection.OBSERVE_SUCCESS_INDIVIDUALS);
  }

  /**
   * This method is called for setting up the parameters
   * 
   * @param parameters
   *          the parameters
   */
  @Override
  protected void setupParameters(final Object[] parameters) {
    this.setMaxGenerations(1000);
    this.setSteadyState(true);
    this.setConvergencePrevention(0.3d);
    this.setEAFactory(ElectionTestSeries.EA_FACTORY);
    this.setPopulationSize(2000);
    this.setTestCaseCount(DEFAULT_TEST_CASE_COUNT);
    this.setTestCasesChanging(true);

    this.selectForLogging(LoggingSelection.RESULT_INDIVIDUALS);
    this.selectForLogging(RUN_PARAMETERS);
    this.setBILimit(10);
  }

  // /**
  // * This method is called for setting up the parameters
  // *
  // * @param parameters
  // * the parameters
  // */
  // @Override
  // protected void setupParameters(final Object[] parameters) {
  // // int i;
  // //
  // // i = 0;
  //
  // this.setEAFactory(new IEAFactory() {
  // /**
  // * Create the evolutionary algorithm
  // *
  // * @return the new evolutionary algorithm, ready to use
  // */
  // public EA<?, ?> createEA() {
  // EA<?, ?> ea;
  // ea = new EA<Serializable, Serializable>() {
  // private static final long serialVersionUID = 1;
  //
  // @Override
  // protected IFitnessAssigner<Serializable, Serializable>
  // createFitnessAssigner() {
  // return new VarietyFitnessAssigner<Serializable, Serializable>();
  // }
  // };
  // // AutoObjectives.prepareEA(ea, 4);
  // DecisiveParetoComparator.prepareEA(ea,
  // AgreementTestSeries.this.m_comparator);
  // return ea;
  // }
  //
  // /**
  // * Obtain the name of this ea factory
  // *
  // * @return the name of this ea factory
  // */
  // @Override
  // public String toString() {
  // return "variety+tiered+rw ea"; //$NON-NLS-1$
  // }
  // });
  // // this.setPopulationSize(((Number) (parameters[i++])).intValue());
  // this.setTestCaseCount(DEFAULT_TEST_CASE_COUNT);
  // this.setTestCasesChanging(true);
  //
  // // this.setSteadyState(((Boolean) (parameters[i++])).booleanValue());
  // this.setConvergencePrevention(0.3d);
  //
  // super.setupParameters(parameters);
  // }

  /**
   * Obtain the simulation provider needed.
   * 
   * @param sc
   *          the scenario count
   * @param randomized
   *          <code>true</code> if and only if the scenarios should be
   *          ranomized
   * @return the simulation provider needed
   */
  protected abstract NetworkProvider<?> doCreateSimulationProvider(
      final int sc, final boolean randomized);

  /**
   * Obtain the simulation provider needed.
   * 
   * @return the simulation provider needed
   */
  @Override
  protected final ISimulationProvider[] createSimulationProviders() {
    return new ISimulationProvider[] { this.doCreateSimulationProvider(
        this.getTestCaseCount(), this.areTestCasesChanging()) };
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

    l.add(new CollisionObjective(this.m_requiredSteps));
    l.add(new UsageObjective2(this.m_requiredSteps));
    // l.add(new StdDevObjective(this.m_requiredSteps));

    l.add(new MinimizeWeightObjective(this.getMinWeight(), this
        .getMaxWeight()));

    return l;
  }

  /**
   * Obtain the maximum weight of a program
   * 
   * @return the maximum weight of a program
   */
  protected int getMaxWeight() {
    return Integer.MAX_VALUE;
  }

  /**
   * Obtain the minimum weight of a program
   * 
   * @return the minimum weight of a program
   */
  protected int getMinWeight() {
    return 1;
  }

  // /**
  // * the internally used comparator
  // */
  // AdaptiveTieredComparator m_comparator;
  //
  // /**
  // * Internally perform one iteration
  // *
  // * @param iteration
  // * the iteration number
  // */
  // @Override
  // protected void iteration(final int iteration) {
  // this.m_comparator = new AdaptiveTieredComparator(//
  // new int[][] { { 1 }, { 1, 1 }, },//
  // new int[] { 10, 10, 10, 10 });
  // super.iteration(iteration);
  // }
  //
  // /**
  // * Create the comparator
  // *
  // * @return the comparator
  // */
  // @Override
  // protected IComparator createComparator() {
  // return this.m_comparator;
  // }

  /**
   * Setup the individual evaluator parameters
   */
  @Override
  protected void setupIndividualEvaluatorParameters() {
    super.setupIndividualEvaluatorParameters();
    this.setTestCaseCount(200);
    this.setTestCasesChanging(true);
  }

}

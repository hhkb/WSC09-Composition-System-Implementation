/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.ElectionTestSeries.java
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

package test.org.dgpf.election;

import java.io.Serializable;
import java.util.List;

import org.dgpf.vm.net.NetworkProvider;
import org.dgpf.vm.objectives.MinimizeWeightObjective;
import org.sfc.collections.CollectionUtils;
import org.sigoa.refimpl.go.algorithms.ea.EA;
import org.sigoa.refimpl.go.fitnessAssignment.variety.VarietyFitnessAssigner;
import org.sigoa.refimpl.utils.testSeries.EDataGrouping;
import org.sigoa.refimpl.utils.testSeries.LoggingSelection;
import org.sigoa.refimpl.utils.testSeries.ea.IEAFactory;
import org.sigoa.spec.go.fitnessAssignment.IFitnessAssigner;
import org.sigoa.spec.simulation.ISimulationProvider;

import test.org.dgpf.DGPFTestSeries;

/**
 * The standard settings for the election problem
 * 
 * @author Thomas Weise
 */
public abstract class ElectionTestSeries extends DGPFTestSeries {

  /**
   * the minimum count of vms
   */
  public static final int MIN_VM_COUNT = 4;

  /**
   * the maximum count of vms
   */
  public static final int MAX_VM_COUNT = 20;

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

  /**
   * The factory for simple eas
   */
  public static final IEAFactory EA_FACTORY = new IEAFactory() {
    /**
     * Create the evolutionary algorithm
     * 
     * @return the new evolutionary algorithm, ready to use
     */
    public EA<?, ?> createEA() {
      return new EA<Serializable, Serializable>() {
        private static final long serialVersionUID = 1;

        @Override
        protected IFitnessAssigner<Serializable, Serializable> createFitnessAssigner() {
          return new VarietyFitnessAssigner<Serializable, Serializable>();
        }
      };
    }

    /**
     * Obtain the name of this ea factory
     * 
     * @return the name of this ea factory
     */
    @Override
    public String toString() {
      return "variety ea"; //$NON-NLS-1$
    }
  };

  /**
   * the objective
   */
  private boolean m_objective;

  /**
   * Create a new test series.
   * 
   * @param dir
   *          the directory to store the test data in
   * @param requiredSteps
   *          the number of required steps
   */
  protected ElectionTestSeries(final Object dir, final int requiredSteps) {
    super(dir, EDataGrouping.RUN,
        -1,// 
        ElectionParameterIterator.TITLES,
        ElectionParameterIterator.ELECTION_PARAMETER_ITERATOR);

    this.m_requiredSteps = requiredSteps;

    this.selectForLogging(LoggingSelection.RESULT_INDIVIDUALS);
//    this.selectForLogging(RUN_PARAMETERS);
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
    this.setTestCaseCount(20);
    this.setTestCasesChanging(true);
    this.setPopulationSize(512);
    this.setConvergencePrevention(0.3d);
    this.setEAFactory(EA_FACTORY);
    this.setMutationRate(0.4d);
    this.setCrossoverRate(0.7d);
    this.setSteadyState(true);
    this.setBILimit(10);
    this.m_objective = ((Boolean) (parameters[0])).booleanValue();
  }

  // /**
  // * This method is called for setting up the parameters
  // *
  // * @param parameters
  // * the parameters
  // */
  // @Override
  // protected void setupParameters(final Object[] parameters) {
  // int i;
  //
  // i = 0;
  //
  // this.setEAFactory(EA_FACTORY);
  // this.setPopulationSize(((Number) (parameters[i++])).intValue());
  // this.setTestCaseCount(25);
  // this.setTestCasesChanging(true);
  // this.setBILimit(10);
  //
  // // this.setTestCaseCount(((Number) (parameters[i++])).intValue());
  // // this.setTestCasesChanging(((Boolean)
  // // (parameters[i++])).booleanValue());
  // this.setSteadyState(((Boolean) (parameters[i++])).booleanValue());
  // //
  // // this.setConvergencePrevention(((Number) (parameters[i++]))
  // // .doubleValue());
  //
  // this.setSteadyState(false);
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
    return new ISimulationProvider[] {// 
    this.doCreateSimulationProvider(//
        this.getTestCaseCount(),//
        this.areTestCasesChanging()) // 
    };
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

    if (this.m_objective) {
      l.add(new ElectionObjective(this.m_requiredSteps));
    } else {
      l.add(new ElectionObjectiveB(this.m_requiredSteps));
    }

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
  // * the shared comparator
  // */
  // public static final IComparator COMPARATOR = new
  // TieredParetoComparator(
  // new int[] { 1, 100 });
  //
  // /**
  // * Create the comparator
  // *
  // * @return the comparator
  // */
  // @Override
  // protected IComparator createComparator() {
  // return COMPARATOR;
  // }

  /**
   * Setup the individual evaluator parameters
   */
  @Override
  protected void setupIndividualEvaluatorParameters() {
    super.setupIndividualEvaluatorParameters();
    this.setTestCaseCount(100);
    this.setTestCasesChanging(true);
  }

}

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

package test.org.dgpf.byzantine.agreement;

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
public abstract class AgreementTestSeries extends DGPFTestSeries {

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
   * the maximum delay
   */
  public static final int MAX_DELAY = (2 * MAX_VM_COUNT);

  /**
   * the number of required steps
   */
  private final int m_requiredSteps;

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
  protected AgreementTestSeries(final Object dir, final int requiredSteps) {
    super(dir, EDataGrouping.RUN, -1,// 
        null,//
        null// 
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
    this.setPopulationSize(1024);
    this.setTestCaseCount(DEFAULT_TEST_CASE_COUNT);
    this.setTestCasesChanging(true);

    this.selectForLogging(LoggingSelection.RESULT_INDIVIDUALS);
    this.selectForLogging(RUN_PARAMETERS);
    this.setBILimit(10);
  }

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
   * create an agreement objective
   * 
   * @param requiredSteps
   *          the required steps
   * @return the agreement objective
   */
  protected abstract AgreementObjective<?> createAgreementObjective(
      final int requiredSteps);

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

    l.add(this.createAgreementObjective(this.m_requiredSteps));
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

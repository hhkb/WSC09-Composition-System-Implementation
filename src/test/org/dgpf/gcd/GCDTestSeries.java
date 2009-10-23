/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.GCDTestSeries.java
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

package test.org.dgpf.gcd;

import java.util.List;

import org.dgpf.vm.objectives.MinimizeSizeObjective;
import org.dgpf.vm.objectives.MinimizeStepsObjective;
import org.dgpf.vm.objectives.MinimizeWeightObjective;
import org.dgpf.vm.objectives.StateObjective;
import org.sfc.collections.CollectionUtils;
import org.sigoa.refimpl.go.comparators.TieredParetoComparator;
import org.sigoa.refimpl.utils.testSeries.EDataGrouping;
import org.sigoa.refimpl.utils.testSeries.LoggingSelection;
import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.simulation.ISimulationProvider;

import test.org.dgpf.DGPFTestSeries;

/**
 * The standard settings for the gcd problem
 * 
 * @author Thomas Weise
 */
public abstract class GCDTestSeries extends DGPFTestSeries {
  /**
   * the number of required steps
   */
  private final int m_requiredSteps;

  /**
   * Create a new test series.
   * 
   * @param dir
   *          the directory to store the test data in
   * @param requiredSteps
   *          the number of required steps
   */
  protected GCDTestSeries(final Object dir, final int requiredSteps) {
    super(dir, EDataGrouping.RUN,
        -1,//
        GCDParameterIterator.TITLES,
        GCDParameterIterator.GCD_PARAMETER_ITERATOR);

    this.m_requiredSteps = requiredSteps;

    // this.selectForLogging(BEST_INDIVIDUALS);

    this.selectForLogging(LoggingSelection.RESULT_INDIVIDUALS);
    this.selectForLogging(RUN_PARAMETERS);
    // this.selectForLogging(BEST_OBJECTIVES);
    this.selectForLogging(LoggingSelection.RESULT_OBJECTIVES);
    this.selectForLogging(LoggingSelection.OBSERVE_SUCCESS);
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
  protected abstract GCDProvider<?, ?> doCreateSimulationProvider(
      final int sc, final boolean randomized);

  /**
   * Obtain the simulation provider needed.
   * 
   * @return the simulation provider needed
   */
  @Override
  protected final synchronized ISimulationProvider[] createSimulationProviders() {
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

    // l.add(this.createFeasibilityObjective());
    l.add(new GCDObjective(this.m_requiredSteps));
    l.add(StateObjective.STATE_OBJECTIVE);
    // l.add(new MinimizeAbsErrorObjective(this.m_requiredSteps, false));
    // l.add(new MinimizeSqrErrorObjective(this.m_requiredSteps, false));
    l.add(MinimizeStepsObjective.MINIMIZE_STEPS_OBJECTIVE);
    l.add(MinimizeSizeObjective.MINIMIZE_SIZE_OBJECTIVE);
    l.add(MinimizeWeightObjective.MINIMIZE_WEIGHT_OBJECTIVE);

    return l;
  }

  /**
   * the shared comparator
   */
  public static final IComparator COMPARATOR = new TieredParetoComparator(
      new int[] { 1, 100 });

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
   * This method is called for setting up the parameters
   * 
   * @param parameters
   *          the parameters
   */
  @Override
  protected void setupParameters(final Object[] parameters) {
    int i;

    i = 0;

    this.setPopulationSize(((Number) (parameters[i++])).intValue());
    this.setTestCaseCount(((Number) (parameters[i++])).intValue());
    this.setTestCasesChanging(//
        ((Boolean) (parameters[i++])).booleanValue());
    this.setSteadyState(((Boolean) (parameters[i++])).booleanValue());

    this.setConvergencePrevention(//
        ((Number) (parameters[i++])).doubleValue());

    // if (((Boolean) (parameters[i++])).booleanValue())
    // this.setEAFactory(EAFactories.RANDOM_WALK);
    // else
    // this.setEAFactory(EAFactories.PLAIN_EA);

    this.setMutationRate(//
        ((Number) (parameters[i++])).doubleValue());

    this.setCrossoverRate(//
        ((Number) (parameters[i++])).doubleValue());

    super.setupParameters(parameters);
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

}

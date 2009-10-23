/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-09
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.simulation.RandomizedScenarioSimulationProvider.java
 * Last modification: 2007-10-09
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

package org.sigoa.refimpl.simulation;

import org.sigoa.refimpl.stoch.Randomizer;
import org.sigoa.spec.go.algorithms.IIterationHook;
import org.sigoa.spec.simulation.ISimulation;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * A randomized scenario simulation provider is able to a set generate
 * random scenarios.
 * 
 * @author Thomas Weise
 */
public class RandomizedScenarioSimulationProvider extends
    SimulationProvider implements IIterationHook {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * <code>true</code> if and only if the scenarios should be changed
   * before each generation
   */
  private final boolean m_changingScenarios;

  /**
   * the number of scenarios
   */
  private final int m_scenarioCount;

  /**
   * the internal randomizer
   */
  private final IRandomizer m_random;

  /**
   * Create a new simulation provider for the specified class of
   * simulations. The class must provide a parameterless constructor.
   * 
   * @param clazz
   *          The class of simulations.
   * @param changingScenarios
   *          <code>true</code> if and only if the scenarios should be
   *          changed before each generation
   * @param scenarioCount
   *          the number of scenarios
   */
  public RandomizedScenarioSimulationProvider(
      final Class<? extends ISimulation<?>> clazz,
      final boolean changingScenarios, final int scenarioCount) {
    super(clazz);
    this.m_changingScenarios = changingScenarios;
    this.m_scenarioCount = scenarioCount;
    this.m_random = new Randomizer();
  }

  /**
   * Check whether the scenarios used by this scenario-based provider are
   * changing, i.e. will be computed newly/randomized before each
   * generation.
   * 
   * @return <code>true</code> if and only if the scenarios should be
   *         changed before
   */
  public boolean areScenariosChanging() {
    return this.m_changingScenarios;
  }

  /**
   * Obtain the number of different scenarios held by this provider
   * 
   * @return the number of scenarios
   */
  public int getScenarioCount() {
    return this.m_scenarioCount;
  }

  /**
   * Create the scenarios
   * 
   * @param random
   *          the randomizer to be used for scenario creation
   * @param iteration
   *          the index of the iteration
   */
  protected void createScenarios(final IRandomizer random,
      final long iteration) {
    //
  }

  /**
   * This method is invoked before the iteration with index
   * <code>iteration</code>.
   * 
   * @param iteration
   *          the index of the iteration
   */
  public void beforeIteration(final long iteration) {
    boolean b;

    b = (iteration <= 0l);
    if (b) {
      this.m_random.setDefaultSeed();
    }

    if (this.m_changingScenarios || b)
      this.createScenarios(this.m_random, iteration);
  }

  /**
   * This method is invoked after the iteration with index
   * <code>iteration</code>.
   * 
   * @param iteration
   *          the index of the iteration
   */
  public void afterIteration(final long iteration) {
    //
  }
}

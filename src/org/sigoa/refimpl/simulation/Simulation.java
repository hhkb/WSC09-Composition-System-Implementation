/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.Simulation.java
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

package org.sigoa.refimpl.simulation;

import java.io.Serializable;

import org.sfc.parallel.simulation.IStepable;
import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.simulation.ISimulation;

/**
 * The base class of all simulations (implements <code>ISimulation</code>)
 *
 * @param <PP>
 *          The phenotype that is simulated by this simulator..
 * @author Thomas Weise
 */
public class Simulation<PP extends Serializable> extends
    ImplementationBase<Serializable, PP> implements ISimulation<PP>,
    IStepable {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The individual currently simulated.
   */
  private PP m_what;

  /**
   * <code>true</code> if and only if we're inside a simulation
   */
  private boolean m_inSim;

  /**
   * Instantiate the simulation.
   */
  public Simulation() {
    super();
  }

  /**
   * Begin the simulation of the specified individual.
   *
   * @param what
   *          the individual
   * @throws NullPointerException
   *           if <code>what</code> is <code>null</code>.
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  public void beginIndividual(final PP what) {
    if (what == null)
      throw new NullPointerException();
    if ((this.m_what != null) || (this.m_inSim))
      throw new IllegalStateException();
    this.m_what = what;
  }

  /**
   * End the individual.
   */
  public void endIndividual() {
    if ((this.m_what == null) || (this.m_inSim))
      throw new IllegalStateException();
    this.m_what = null;

  }

  /**
   * This method is called right before the simulation begins.
   *
   * @throws IllegalStateException
   *           if this simulation is already running or the
   *           {@link #beginIndividual(Serializable)} method has not yet
   *           been called or {@link #endIndividual()} has already been
   *           called.
   */
  public void beginSimulation() {
    if ((this.m_what == null) || (this.m_inSim))
      throw new IllegalStateException();
    this.m_inSim = true;
  }

  /**
   * Obtain the simulated phenotype.
   *
   * @return The simulated phenotype.
   */
  public PP getSimulated() {
    return this.m_what;
  }

  /**
   * This method is called when the simulation has ended.
   *
   * @throws IllegalStateException
   *           If this simulation is not yet running.
   */
  public void endSimulation() {
    if ((this.m_what == null) || (!(this.m_inSim)))
      throw new IllegalStateException();
    this.m_inSim = false;
  }

  /**
   * Perform <code>steps</code> simulation steps. This method returns
   * <code>true</code> per default.
   *
   * @param steps
   *          The count of simulation steps to be performed.
   * @return <code>true</code> if and only if further simulating would
   *         possible change the state of the simulation,
   *         <code>false</code> if the simulation has come to a final,
   *         terminal state which cannot change anymore.
   * @throws IllegalStateException
   *           If this simulation is not yet running.
   * @throws IllegalArgumentException
   *           if <code>steps <= 0</code>.
   */
  public boolean simulate(final long steps) {
    if (this.m_what == null)
      throw new IllegalStateException();
    if (steps <= 0)
      throw new IllegalArgumentException();
    return true;
  }

  /**
   * Obtain an unique identifier of the simulation type performed by this
   * simulator. This could be the fully qualified class name of the
   * simulation provider, for example.
   *
   * @return The unique identifier of the type of this simulation. Here,
   *         the class is returned.
   */
  public Serializable getSimulationId() {
    return this.getClass();
  }

  /**
   * Perform a step
   */
  public void step() {
    this.simulate(1L);
  }
}

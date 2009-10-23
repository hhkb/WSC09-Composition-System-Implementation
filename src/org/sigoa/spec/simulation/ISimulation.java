/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 ${time}
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.simulation.ISimulation.java
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

package org.sigoa.spec.simulation;

import java.io.Serializable;

/**
 * This interface is common to all simulators.
 *
 * @author Thomas Weise
 * @version 1.0.0
 * @param <PP>
 *          The phenotype used as input for the simulation.
 */
public interface ISimulation<PP extends Serializable> extends Serializable {

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
  public abstract void beginIndividual(final PP what);

  /**
   * End the individual.
   */
  public abstract void endIndividual();

  /**
   * This method is called right before the simulation begins.
   *
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  public abstract void beginSimulation();

  /**
   * This method is called when the simulation has ended
   *
   * @throws IllegalStateException
   *           If this simulation is not yet running.
   */
  public abstract void endSimulation();

  /**
   * Perform <code>steps</code> simulation steps.
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
  public abstract boolean simulate(final long steps);

  /**
   * Obtain an unique identifier of the simulation type performed by this
   * simulator. This could be the fully qualified class name of the
   * simulation provider, for example.
   *
   * @return The unique identifier of the type of this simulation.
   */
  public abstract Serializable getSimulationId();

}

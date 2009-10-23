/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 ${time}
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.simulation.ISimulationManager.java
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
import java.lang.reflect.Method;

/**
 * A simulation manager manages the reuse of simulator instances.
 */
public interface ISimulationManager extends Serializable {
  /**
   * Add a simulator provider to the set of available simulators
   *
   * @param provider
   *          the simulation provider to be added to the management.
   * @throws NullPointerException
   *           If <code>provider==null</code> or if its id is
   *           <code>null</code>.
   * @throws RuntimeException
   *           If the provider is already stored.
   */
  public abstract void addProvider(final ISimulationProvider provider);

  /**
   * Remove a simulator provider to the set of available simulators
   *
   * @param id
   *          the if of simulation provider to be removed from the
   *          management.
   * @throws NullPointerException
   *           If <code>id==null</code>
   * @throws RuntimeException
   *           If the provider is not known.
   * @throws IllegalStateException
   *           If there are still simulations in use which belong to this
   *           provider.
   */
  public abstract void removeProvider(final Serializable id);

  /**
   * Obtain a simulator of the specified id to run in the specified host.
   * This method may block if all instances of the specified simulator are
   * currently in use.
   *
   * @param id
   *          The id of the required simulation.
   * @return The simulation.
   * @throws NullPointerException
   *           If <code>id==null</code>.
   * @throws RuntimeException
   *           If the provider is not available.
   */
  public abstract ISimulation<?> getSimulation(final Serializable id);

  /**
   * Obtain a set of simulations. This method must be used if an evaluation
   * process needs more than one simulation in order to prevent deadlocks.
   *
   * @param ids
   *          The ids of the required simulation. If a field of this array
   *          is <code>null</code>, it is ignored.
   * @param sims
   *          The array to store the simulations in.
   * @throws NullPointerException
   *           If <code>ids==null</code> or <code>sims==null</code>.
   * @throws RuntimeException
   *           If a provider is not available.
   * @throws IllegalArgumentException
   *           if <code>sims.length &lt; ids.length</code>
   */
  public abstract void getSimulations(final Serializable[] ids,
      final ISimulation<?>[] sims);

  /**
   * Return a simulator no longer used to the simulator pool.
   *
   * @param simulator
   *          The simulator no longer needed.
   * @throws NullPointerException
   *           If <code>simulator==null</code> or if its id is
   *           <code>null</code>.
   * @throws RuntimeException
   *           If the provider cannot be found.
   */
  public abstract void returnSimulation(final ISimulation<?> simulator);

  /**
   * Invoke a hook of the simulation manager.
   *
   * @param method
   *          the hook method to invoke
   * @param parameters
   *          the hook parameters
   */
  public abstract void invokeHook(final Method method,
      final Serializable[] parameters);
}

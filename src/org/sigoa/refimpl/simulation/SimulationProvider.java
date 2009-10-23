/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.simulation.SimulationProvider.java
 * Last modification: 2007-08-04
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

import org.sfc.utils.ErrorUtils;
import org.sfc.utils.Utils;
import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.simulation.ISimulation;
import org.sigoa.spec.simulation.ISimulationProvider;

/**
 * The default <code>ISimulationProvider</code>-implementation.
 *
 * @author Thomas Weise
 */
public class SimulationProvider extends
    ImplementationBase<Serializable, Serializable> implements
    ISimulationProvider {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The simulator class.
   */
  private final Class<? extends ISimulation<?>> m_clazz;

  /**
   * Create a new simulation provider for the specified class of
   * simulations. The class must provide a parameterless constructor.
   *
   * @param clazz
   *          The class of simulations.
   */
  public SimulationProvider(final Class<? extends ISimulation<?>> clazz) {
    super();
    this.m_clazz = clazz;
  }

  /**
   * Create a new instance of this simulator.
   *
   * @return The newly created simulator instance.
   */
  public ISimulation<?> createSimulation() {
    try {
      return this.m_clazz.newInstance();
    } catch (Throwable t) {
      ErrorUtils.onError(t);
      return null; // this code cannot be reached
    }
  }

  /**
   * Destroy a simulator instance. This method is called whenever a
   * simulation instance is not needed anymore and should be cleaned up.
   *
   * @param instance
   *          The simulator instance that is to be destroyed - no further
   *          calls will be allowed to that instance.
   * @throws NullPointerException
   *           if <code>instance==null</code>.
   */
  public void destroySimulation(final ISimulation<?> instance) {
    if (instance == null)
      throw new NullPointerException();
  }

  /**
   * This method returns the maximum count of instances of this simulator
   * that may be created on this local machine. Normally, it should be
   * useful to create one instance per processor. If one simulation however
   * needs multiple threads to be performed, this count could be less in
   * order to preserve performance.
   *
   * @return The maximum count of simulator instances recommended for this
   *         machine, usually one per processor. (values <= 0 are not
   *         allowed here)
   */
  public int getMaxSimulations() {
    return Integer.MAX_VALUE;
  }

  /**
   * Obtain the unique id of this simulator provider.This could be the
   * fully qualified class name of the simulation provider, for example.
   * This method returns the class of the simulations.
   *
   * @return The unique identifier of the type of this simulation.
   */
  public Serializable getSimulationId() {
    return this.m_clazz;
  }

  /**
   * Check whether this simulation provider equals another object.
   *
   * @param o
   *          The object to compare with.
   * @return <code>true</code> if and only if this simulation provider
   *         equals the other object-
   */
  @Override
  public boolean equals(final Object o) {
    if (o == this)
      return true;
    if (o == null)
      return false;
    return ((o instanceof ISimulationProvider) && Utils.testEqual(
        ((ISimulationProvider) o).getSimulationId(), this
            .getSimulationId()));
  }
}

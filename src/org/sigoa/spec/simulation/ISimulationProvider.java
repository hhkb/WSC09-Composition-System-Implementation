/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 ${time}
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.simulation.ISimulationProvider.java
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
 * A simulation provider is able to connect the optimization algorithms
 * with simulators. A simulator may run in the same virtual machine or
 * somewhere else in the outside.
 *
 * @author Thomas Weise
 * @version 1.0.0
 */
public interface ISimulationProvider extends Serializable {
  /**
   * Create a new instance of this simulator.
   *
   * @return The newly created simulator instance.
   */
  public abstract ISimulation<?> createSimulation();

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
  public abstract void destroySimulation(final ISimulation<?> instance);

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
  public abstract int getMaxSimulations();

  /**
   * Obtain the unique id of this simulator provider.This could be the
   * fully qualified class name of the simulation provider, for example.
   *
   * @return The unique identifier of the type of this simulation.
   */
  public abstract Serializable getSimulationId();

}

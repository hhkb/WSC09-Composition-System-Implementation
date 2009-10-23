/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 ${time}
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.simulation.ProviderState.java
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

import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sigoa.spec.simulation.ISimulation;
import org.sigoa.spec.simulation.ISimulationProvider;

/**
 * This internal class keeps track on simulation providers.
 *
 * @author Thomas Weise
 */
public class ProviderState {

  /**
   * the class.
   */
  final Class<?> m_cls;

  /**
   * the id list.
   */
  final List<Object> m_ids;

  /**
   * The provider stored in this provider state record.
   */
  ISimulationProvider m_provider;

  /**
   * The count of simulations already created.
   */
  private int m_created;

  /**
   * The internal list of simulations.
   */
  private final List<ISimulation<?>> m_simulations;

  /**
   * Create a new simulation provider state.
   *
   * @param id
   *          the id
   * @param provider
   *          The simulation provider.
   */
  public ProviderState(final Object id, final ISimulationProvider provider) {
    super();
    this.m_provider = provider;
    this.m_simulations = CollectionUtils.createList();

    if (id instanceof Class) {
      this.m_cls = ((Class<?>) id);
      this.m_ids = CollectionUtils.createList();
    } else {
      this.m_cls = null;
      this.m_ids = null;
    }
  }

  /**
   * Obtain a simulation, wait if none is available.
   *
   * @return A simulation.
   */
  protected ISimulation<?> getSimulation() {
    List<ISimulation<?>> s;
    int i;

    s = this.m_simulations;

    for (;;) {
      synchronized (s) {
        i = s.size();
        if (i > 0)
          return s.remove(i - 1);
        i = this.m_created;
        if (i <= this.m_provider.getMaxSimulations()) {
          this.m_created = (i + 1);
          return this.m_provider.createSimulation();
        }
        try {
          s.wait();
        } catch (Throwable t) {
          //
        }
      }
    }
  }

  /**
   * Return a simulator no longer used to the simulator pool.
   *
   * @param simulator
   *          The simulator no longer needed.
   */
  protected void returnSimulation(final ISimulation<?> simulator) {
    List<ISimulation<?>> s;
    s = this.m_simulations;
    synchronized (s) {
      s.add(simulator);
    }
  }

  /**
   * Destroy this provider state.
   *
   * @throws IllegalStateException
   *           If there are still simulations in use.
   */
  protected void destroy() {
    List<ISimulation<?>> s;
    int i;

    s = this.m_simulations;
    synchronized (s) {
      if (s.size() != this.m_created)
        throw new IllegalStateException();
      for (i = (s.size() - 1); i >= 0; i--) {
        this.m_provider.destroySimulation(s.remove(i));
      }
      this.m_provider = null;
      s.notifyAll();
    }
  }
}

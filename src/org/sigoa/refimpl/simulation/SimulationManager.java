/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 ${time}
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.simulation.SimulationManager.java
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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.sfc.collections.CollectionUtils;
import org.sfc.collections.lists.DefaultList;
import org.sfc.collections.lists.ListBase;
import org.sfc.utils.ErrorUtils;
import org.sigoa.refimpl.events.ErrorEvent;
import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.jobsystem.IHost;
import org.sigoa.spec.simulation.ISimulation;
import org.sigoa.spec.simulation.ISimulationManager;
import org.sigoa.spec.simulation.ISimulationProvider;

/**
 * The default implementation of the interface
 * <code>ISimulationManager</code>.
 *
 * @author Thomas Weise
 */
public class SimulationManager extends
    ImplementationBase<Serializable, Serializable> implements
    ISimulationManager {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * The internal map with the simulation providers.
   */
  private final Map<Object, ProviderState> m_providers;

  /**
   * the provider list.
   */
  private final ListBase<ProviderState> m_provList;

  /**
   * Create a new simulation manager.
   */
  public SimulationManager() {
    super();
    this.m_providers = CollectionUtils.createMap();
    this.m_provList = new DefaultList<ProviderState>();
  }

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
  public void addProvider(final ISimulationProvider provider) {
    Map<Object, ProviderState> m;
    Object id;
    ProviderState ps;

    if (provider == null)
      throw new NullPointerException();

    m = this.m_providers;
    id = provider.getSimulationId();
    if (id == null)
      throw new NullPointerException();

    synchronized (m) {
      if (m.get(id) != null) {
        ErrorUtils.onError("Provider " + provider + //$NON-NLS-1$
            " already installed."); //$NON-NLS-1$
        return;// cannot be reached
      }
      // throw new RuntimeException();
      ps = new ProviderState(id, provider);
      m.put(id, ps);
      if (ps.m_cls != null)
        this.m_provList.add(ps);
    }
  }

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
  public ISimulation<?> getSimulation(final Serializable id) {
    ProviderState ps;

    if (id == null)
      throw new NullPointerException();

    ps = this.getProviderState(id);

    if (ps == null) {
      ErrorUtils.onError("Simulation provider " + id + //$NON-NLS-1$
          " not available.");//$NON-NLS-1$
      return null;// will never happen
    }
    // throw new RuntimeException();
    return ps.getSimulation();
  }

  /**
   * Obtain a provider state.
   *
   * @param id
   *          the id
   * @return the provider state
   */
  private final ProviderState getProviderState(final Object id) {
    Map<Object, ProviderState> m;
    List<ProviderState> l;
    ProviderState ps;
    Class<?> idc;
    int i;

    m = this.m_providers;
    synchronized (m) {
      ps = m.get(id);
      if (ps != null)
        return ps;
      if (id instanceof Class) {
        idc = ((Class<?>) (id));

        l = this.m_provList;
        for (i = (l.size() - 1); i >= 0; i--) {
          ps = l.get(i);
          if (idc.isAssignableFrom(ps.m_cls)) {
            m.put(id, ps);
            ps.m_ids.add(id);
            return ps;
          }
        }

        return null;
      }
    }

    return null;
  }

  /**
   * An internal hash-code wrapper.
   *
   * @param o
   *          The object.
   * @return The hashcode.
   */
  private static final int hc(final Object o) {
    if ((o instanceof String) || (o instanceof Number))
      return o.hashCode();
    return System.identityHashCode(o);
  }

  /**
   * Obtain a set of simulations. This method must be used if an evaluation
   * process needs more than one simulation in order to prevent deadlocks.
   * This implementation prevents possible deadlocks by requiring the
   * simulations in the order of the hashcodes of their ids. Therefore,
   * provider ids managed by this simulation manager must not have the same
   * hashcode.
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
  public void getSimulations(final Serializable[] ids,
      final ISimulation<?>[] sims) {

    ProviderState ps;
    int i, t, v, x;

    if (sims.length < ids.length)
      throw new IllegalArgumentException();

    Arrays.fill(sims, null);
    for (;;) {
      x = -1;
      t = Integer.MAX_VALUE;
      for (i = (ids.length - 1); i >= 0; i--) {
        if (ids[i] != null) {
          v = hc(ids[i]);
          if ((sims[i] == null) && (v < t)) {
            x = i;
            t = v;
          }
        }
      }

      if (x < 0)
        break;

      ps = this.getProviderState(ids[x]);

      if (ps == null) {
        for (i = (sims.length - 1); i >= 0; i--) {
          if (sims[i] != null)
            this.returnSimulation(sims[i]);
        }
        // throw new RuntimeException();

        ErrorUtils.onError("Simulation provider " + ids[x] + //$NON-NLS-1$
            " not available.");//$NON-NLS-1$
        return;// cannot be reached
      }

      sims[x] = ps.getSimulation();
    }

  }

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
  public void returnSimulation(final ISimulation<?> simulator) {
    Map<Object, ProviderState> m;
    Object id;
    ProviderState s;

    id = simulator.getSimulationId();
    if (id == null)
      throw new NullPointerException();

    m = this.m_providers;

    synchronized (m) {
      s = m.get(id);
    }

    if (s == null) {
      ErrorUtils.onError("Simulation provider " + id + //$NON-NLS-1$
          " not available.");//$NON-NLS-1$
      return;// cannot be reached
      // throw new RuntimeException();
    }
    s.returnSimulation(simulator);
  }

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
  public void removeProvider(final Serializable id) {
    Map<Object, ProviderState> m;
    ProviderState s;
    List<Object> o;
    int i;

    if (id == null)
      throw new NullPointerException();

    m = this.m_providers;

    synchronized (m) {
      s = m.get(id);
      if (s == null) {
        ErrorUtils.onError("Simulation provider " + id + //$NON-NLS-1$
            " not available.");//$NON-NLS-1$
        return;// cannot be reached
        // throw new RuntimeException();
      }
      s.destroy();
      m.remove(id);
      o = s.m_ids;
      if (o != null) {
        for (i = (o.size() - 1); i >= 0; i--) {
          m.remove(o.get(i));
        }
        this.m_provList.removeFast(s);
      }
    }

  }

  /**
   * This method cleans up the simulation manager. Call it only when there
   * are no future needs to work with the simulations stored here.
   *
   * @throws IllegalStateException
   *           If there are still simulations in use.
   */
  public void destroy() {
    Map<Object, ProviderState> m;

    m = this.m_providers;
    synchronized (m) {
      for (ProviderState e : m.values()) {
        e.destroy();
      }
    }
  }

  /**
   * Invoke a hook of the simulation manager.
   *
   * @param method
   *          the hook method to invoke
   * @param parameters
   *          the hook parameters
   * @throws IllegalArgumentException
   *           if <code>method==null</code>
   */
  public void invokeHook(final Method method,
      final Serializable[] parameters) throws IllegalArgumentException {
    Class<?> c;
    ListBase<ProviderState> l;
    int i;
    ISimulationProvider p;
    IHost h;

    if (method == null)
      throw new IllegalArgumentException();

    c = method.getDeclaringClass();
    synchronized (this.m_providers) {
      l = this.m_provList;
      for (i = (l.size() - 1); i >= 0; i--) {
        p = l.get(i).m_provider;
        if (c.isInstance(p)) {
          try {
            method.invoke(p, (Object[]) parameters);
          } catch (Throwable t) {
            h = this.getHost();
            if (h != null)
              h.receiveEvent(new ErrorEvent(t));
            else
              ErrorUtils.onError(t);
          }
        }
      }
    }
  }
}

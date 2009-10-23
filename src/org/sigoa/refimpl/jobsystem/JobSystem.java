/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-06
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.jobsystem.JobSystem.java
 * Last modification: 2006-12-06
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

package org.sigoa.refimpl.jobsystem;

import java.io.Serializable;

import org.sigoa.refimpl.events.ErrorEvent;
import org.sigoa.refimpl.events.EventPropagator;
import org.sigoa.refimpl.simulation.SimulationManager;
import org.sigoa.refimpl.stoch.Randomizer;
import org.sigoa.spec.events.IEvent;
import org.sigoa.spec.events.IEventListener;
import org.sigoa.spec.go.IOptimizer;
import org.sigoa.spec.jobsystem.IJobInfo;
import org.sigoa.spec.jobsystem.IJobSystem;
import org.sigoa.spec.simulation.ISimulationManager;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The base class of all job systems
 *
 * @author Thomas Weise
 */
public abstract class JobSystem extends ThreadActivity implements IJobSystem {
  /**
   * The internal event listener used to propagate events.
   */
  private final EventPropagator m_events;


  /**
   * The simulation manager.
   */
  private final ISimulationManager m_simulationManager;

  /**
   * Create a new job system with a default simulation manager.
   */
  public JobSystem() {
    this(null);
  }

  /**
   * Create a new job system.
   *
   * @param simulationManager
   *          the simulation manager to be used if this is
   *          <code>null</code>, a default simulation manager will be
   *          created
   */
  public JobSystem(final ISimulationManager simulationManager) {
    super(null);
    this.m_events = this.createEventPropgator();
    if (this.m_events == null)
      throw new NullPointerException();


    this.m_simulationManager = ((simulationManager != null) ? simulationManager
        : new SimulationManager());
  }

  /**
   * Create the event propagator to write the events to. All internal
   * events will be written to it.
   *
   * @return the new <code>EventPropagator</code>
   */
  protected EventPropagator createEventPropgator() {
    return new EventPropagator();
  }

  /**
   * Propagate an event to the internal event propagator.
   *
   * @param id
   *          the id of the
   * @param e
   *          the event to be propagated
   */
  protected void propagateEvent(final Serializable id, final IEvent e) {
    if (e.getEventSource() != id) {
      this.m_events.receiveEvent(new ErrorEvent(id, new SecurityException(
          "wrong event source")));//$NON-NLS-1$
    } else
      this.m_events.receiveEvent(e);
  }

  /**
   * Create a new randomizer that can be provided by host to its jobs.
   *
   * @return a new randomizer that can be provided by host to its jobs
   */
  protected IRandomizer createRandomizer() {
    return new Randomizer();
  }

  /**
   * Obtain the event listener interface of the internally used event
   * propagator.
   *
   * @return the internal event propagator
   */
  protected EventPropagator getEventPropagator() {
    return this.m_events;
  }

  /**
   * Register an event lister that should receive events from this source.
   *
   * @param listener
   *          The event listener that should now be able to receive events
   *          from this source.
   * @throws NullPointerException
   *           if <code>listener</code> is <code>null</code>.
   */
  public void addEventListener(final IEventListener listener) {
    this.m_events.addEventListener(listener);
  }

  /**
   * Detach an event listener from this event source.
   *
   * @param listener
   *          The event listener to be detached from this event source.
   * @throws NullPointerException
   *           if <code>listener</code> is <code>null</code>.
   */
  public void removeEventListener(final IEventListener listener) {
    this.m_events.removeEventListener(listener);
  }



  /**
   * Obtain the simulation manager used by the job system.
   *
   * @return the simulation manager used by the job system
   */
  public ISimulationManager getSimulationManager() {
    return this.m_simulationManager;
  }

  /**
   * This method performs the shutdown. It is called by
   * <code>abort()</code> at most once.
   *
   * @see #abort()
   */
  @Override
  protected void doAbort() {
    this.getThreadGroup().interrupt();
    super.doAbort();
  }

  /**
   * Check whether an optimization job can be executed.
   *
   * @param job
   *          the job to execute
   * @param info
   *          the job information record
   * @param <G>
   *          the genotype of the optimizer job
   * @param <PP>
   *          the phenotype of the optimizer job
   * @throws IllegalStateException
   *           If the executor is neither in the state
   *           <code>INITIALIZED</code> nor <code>RUNNING</code> or if
   *           the job has a processor count of <= 0 assigned.
   * @throws NullPointerException
   *           if the job or any required field of the info record is
   *           <code>null</code>.
   */
  protected <G extends Serializable, PP extends Serializable> void checkExecuteOptimization(
      final IOptimizer<G, PP> job, final IJobInfo<G, PP> info) {

    if ((job == null) || (info == null)
        || (info.getSecurityInfo() == null)
        || (info.getOptimizationInfo() == null))
      throw new NullPointerException();

    if (info.getExecutionInfo().getMaxProcessorCount() <= 0)
      throw new IllegalArgumentException();

    if(this.isFinal())
      throw new IllegalStateException();
  }
}

/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-25
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.jobsystem.singleprocessor.SingleProcessorJobSystem.java
 * Last modification: 2008-05-12
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

package org.sigoa.refimpl.jobsystem.singleprocessor;

import java.io.Serializable;

import org.sigoa.refimpl.jobsystem.JobId;
import org.sigoa.refimpl.jobsystem.JobSystem;
import org.sigoa.spec.events.IEvent;
import org.sigoa.spec.go.IOptimizer;
import org.sigoa.spec.jobsystem.IJobInfo;
import org.sigoa.spec.jobsystem.IOptimizationHandle;
import org.sigoa.spec.simulation.ISimulationManager;

/**
 * An implementation of the <code>IJobSystem</code>-interface which uses
 * only one processor.
 *
 * @author Thomas Weise
 */
public class SingleProcessorJobSystem extends JobSystem {

  /**
   * The next job.
   */
  private SPJSJob<?, ?> m_job;


  /**
   * The internal synchronization object.
   */
  private final Object m_sync;

  /**
   * Create a new single processor executor using a default simulation
   * manager.
   */
  public SingleProcessorJobSystem() {
    this(null);
  }

  /**
   * Create a new single processor executor.
   *
   * @param simulationManager
   *          the simulation manager to be used if this is
   *          <code>null</code>, a default simulation manager will be
   *          created
   */
  public SingleProcessorJobSystem(
      final ISimulationManager simulationManager) {
    super(simulationManager);
    //this.m_thread = this.createThread();
    this.addThread(this.createThread());
    this.m_sync = new Object();
  }

  /**
   * Create a new single processor executor thread.
   *
   * @return The new thread.
   */
  protected SPJSThread createThread() {
    return new SPJSThread(this, this.getThreadGroup());
  }

  /**
   * Executes an optimization job using the information of a given job info
   * record.
   *
   * @param job
   *          the job to execute
   * @param info
   *          the job information record
   * @param <G>
   *          the genotype of the optimizer job
   * @param <PP>
   *          the phenotype of the optimizer job
   * @return an instance of <code>IWaitable</code> allowing you to wait
   *         for the job to complete
   * @throws IllegalStateException
   *           If the executor is neither in the state
   *           <code>INITIALIZED</code> nor <code>RUNNING</code> or if
   *           the job has a processor count of <= 0 assigned.
   * @throws NullPointerException
   *           if the job or any required field of the info record is
   *           <code>null</code>.
   */
  public <G extends Serializable, PP extends Serializable> IOptimizationHandle executeOptimization(
      final IOptimizer<G, PP> job, final IJobInfo<G, PP> info) {
    SPJSJob<G, PP> ss;
    JobId j;

    this.checkExecuteOptimization(job, info);
    j = new JobId(this);
    synchronized (this.m_sync) {
      this.m_job = ss = new SPJSJob<G, PP>(job, info, this.m_job, j);
      this.m_sync.notifyAll();
    }
    return ss;
  }

  /**
   * Obtain the next job to be executed.
   *
   * @return The new job or <code>null</code> if the executor terminates.
   */
  final SPJSJob<?, ?> getJob() {
    SPJSJob<?, ?> job;
    for (;;) {
      synchronized (this.m_sync) {
        if (!(this.isRunning()))
          return null;
        job = this.m_job;
        if (job != null) {
          this.m_job = job.m_next;
          return job;
        }

        try {
          this.m_sync.wait();
        } catch (InterruptedException ie) {
          //
        }
      }
    }
  }

  /**
   * This method performs the shutdown. It is called by
   * <code>abort()</code> at most once.
   *
   * @see #abort()
   */
  @Override
  protected void doAbort() {
    SPJSJob<?, ?> s;

    this.abortThreads();
    synchronized (this.m_sync) {
      for (s = this.m_job; s != null; s = s.m_next) {
        s.finished();
      }
      this.m_job = null;
      this.m_sync.notifyAll();
    }
    super.doAbort();
    this.finished();
  }


  /**
   * Propagate an event to the internal event propagator.
   *
   * @param id
   *          the id of the
   * @param e
   *          the event to be propagated
   */
  final void receiveEvent(final Serializable id, final IEvent e) {
    super.propagateEvent(id, e);
  }

  /**
   * It is recommended that derived classes use this method for
   * error-processing.
   * @param t the error object
   */
  @Override
  protected void onError(final Throwable t) {
    super.onError(t);
  }

  // /**
  // * Create a new randomizer that can be provided by host to its jobs.
  // *
  // * @return a new randomizer that can be provided by host to its jobs
  // */
  // final IRandomizer doCreateRandomizer() {
  // return this.createRandomizer();
  //  }
}

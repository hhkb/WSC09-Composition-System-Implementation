/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 ${time}
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.jobsystem.multiprocessor.MultiProcessorJobSystem.java
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

package org.sigoa.refimpl.jobsystem.multiprocessor;

import java.io.Serializable;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sigoa.refimpl.jobsystem.JobId;
import org.sigoa.refimpl.jobsystem.JobSystem;
import org.sigoa.spec.events.IEvent;
import org.sigoa.spec.go.IOptimizer;
import org.sigoa.spec.jobsystem.IJobInfo;
import org.sigoa.spec.jobsystem.IJobSystem;
import org.sigoa.spec.jobsystem.IOptimizationHandle;
import org.sigoa.spec.simulation.ISimulationManager;

/**
 * The default implementation of the <code>IJobSystem</code>-interface.
 *
 * @see IJobSystem
 */
public class MultiProcessorJobSystem extends JobSystem {
  /**
   * The internal worker threads.
   */
//  final List<MPJSThread> m_threads;

  /**
   * The job state list.
   */
  final List<MPJSJob> m_jobs;

  /**
   * The index of the next job to assign a worker to.
   */
  private int m_nextJob;

  /**
   * The next thread id.
   */
  private int m_nextThreadId;

  /**
   * this flag is set if a job may have become available
   */
  int m_flag;

  /**
   * the thread synchronizer
   */
  private final Object m_threadSync;

  /**
   * Create a new multi-threaded job system.
   */
  public MultiProcessorJobSystem() {
    this(null);
  }

  /**
   * Create a new multi-threaded job system.
   *
   * @param simulationManager
   *          the simulation manager to be used if this is
   *          <code>null</code>, a default simulation manager will be
   *          created
   */
  public MultiProcessorJobSystem(final ISimulationManager simulationManager) {
    super(simulationManager);

    this.m_threadSync = new Object();

   this.createThreads();

    this.m_jobs = CollectionUtils.createList();
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
   *
   * @param t
   *          the error object
   */
  @Override
  protected void onError(final Throwable t) {
//    t.printStackTrace();
    super.onError(t);
  }

  /**
   * Obtain the next thread id.
   *
   * @return The id to assign to the thread created next.
   */
  protected int getNextThreadId() {
    synchronized (this.m_threadSync) {
      return (++this.m_nextThreadId);
    }
  }

  /**
   * Create a new default executor thread by incrementing the internal id
   * and passing it to the <code>createThread(int)</code> method.
   *
   * @return The new internal executor thread.
   * @throws NullPointerException
   *           if <code>createThread(int)</code> returns
   *           <code>null</code>.
   */
  protected MPJSThread createThread() {
    return new MPJSThread("executorThread[" + //$NON-NLS-1$
        this.getNextThreadId() + ']', this, this.getThreadGroup());
  }

  /**
   * Obtain the optimal count of worker threads to be used. Normally, this
   * is the count of available processors.
   *
   * @return The optimal count of worker threads to be used by this
   *         executor.
   */
  protected int getOptimalThreadCount() {
    return Runtime.getRuntime().availableProcessors();
  }

  /**
   * Create the optimal count of worker
   * threads as specified by <code>getOptimalThreadCount</code>.
   */
  protected void createThreads() {
    int i;
    MPJSThread t;

    i = this.getOptimalThreadCount();
    if (i <= 0)
      throw new IllegalArgumentException();
    for (--i; i >= 0; i--) {
      t = this.createThread();
      if (t == null)
        throw new NullPointerException();
      this.addThread(t);
    }
  }




  /**
   * Perform a job.
   *
   * @param t
   *          the thread which wants to do some work
   * @return <code>true</code> if the thread should go on working,
   *         <code>false</code> otherwise
   */
  final boolean doJob(final MPJSThread t) {

    if (!(this.isRunning()))
      return false;

    if (t.m_job != null) {
      if (t.m_job.m_state.doJob(t))
        return true;
    }

    return this.findJob(t);
  }

  /**
   * Find a job.
   *
   * @param t
   *          the thread which wants to do some work
   * @return <code>true</code> if the thread should go on working,
   *         <code>false</code> otherwise
   */
  final boolean findJob(final MPJSThread t) {
    List<MPJSJob> jobs;
    int i, n, s;
    MPJSJob c;

    jobs = this.m_jobs;
    i = this.m_nextJob;
    n = 0;
    main: for (;;) {
      if (!(this.isRunning()))
        return false;

      synchronized (jobs) {
        s = jobs.size();
        if (n >= s) {
          n = 0;
          s = this.m_flag;
          if (s > 0) {
            this.m_flag = (s - 1);
          } else {
            try {
              jobs.wait();
            } catch (InterruptedException iee) {//
            }
          }
          continue main;
        }

        s = ((i + n) % s);
        c = jobs.get(s);
        n++;
      }

      if (c.doJob(t)) {
        this.m_nextJob = s;
        return true;
      }
    }
  }

  /**
   * Perform the work of aborting this executor.
   */
  @Override
  protected void doAbort() {
    int i;
    List<MPJSJob> j;

    this.abortThreads();

    j = this.m_jobs;
    synchronized (j) {
      this.m_flag = 1000000;
      for (i = (j.size() - 1); i >= 0; i--) {
        j.get(i).abort();
      }
      j.clear();
      j.notifyAll();
    }

    this.abortThreads();

    synchronized (j) {
      j.notifyAll();
    }

    super.doAbort();
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
    MPJSJob m;

    this.checkExecuteOptimization(job, info);
    m = new MPJSJob(this, info, job, new JobId(this));
    synchronized (this.m_jobs) {
      this.m_jobs.add(m);
      this.m_flag++;
      this.m_jobs.notify();
    }

    return m;
  }

//  /**
//   * Create a new randomizer that can be provided by host to its jobs.
//   *
//   * @return a new randomizer that can be provided by host to its jobs
//   */
//  final IRandomizer doCreateRandomizer() {
//    return this.createRandomizer();
//  }
}

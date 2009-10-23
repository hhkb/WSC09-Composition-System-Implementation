/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.jobsystem.multiprocessor.MPJSThread.java
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

import org.sigoa.refimpl.events.ErrorEvent;
import org.sigoa.refimpl.jobsystem.JobSystemThread;
import org.sigoa.spec.events.IEvent;
import org.sigoa.spec.jobsystem.IHost;
import org.sigoa.spec.jobsystem.IJobInfo;
import org.sigoa.spec.jobsystem.IWaitable;

/**
 * The job system worker thread.
 * 
 * @author Thomas Weise
 */
public class MPJSThread extends JobSystemThread<MultiProcessorJobSystem>
    implements IHost {
  //
  // /**
  // * The job system that owns this thread.
  // */
  // final MultiProcessorJobSystem m_js;

  /**
   * The currently running sub-job.
   */
  MPJSJobBase m_job;

  //
  // /**
  // * The internal randomizer.
  // */
  // private final IRandomizer m_randomizer;

  /**
   * Create a new job system thread.
   * 
   * @param name
   *          The name of the new job system thread.
   * @param multiProcessorJobSystem
   *          The job system instance using this thread.
   * @param tg
   *          the threadgroup to use
   */
  public MPJSThread(final String name,
      final MultiProcessorJobSystem multiProcessorJobSystem,
      final ThreadGroup tg) {
    super(name, multiProcessorJobSystem, tg);

    if (name == null)
      throw new NullPointerException();

    // this.m_js = multiProcessorJobSystem;
    // this.m_randomizer = multiProcessorJobSystem.doCreateRandomizer();
  }

  /**
   * Execute a job. This method can only be called from a job that is
   * already running within an <code>IJobSystem</code>-instance. With
   * it, the job may enqueue additional tasks. If the job added is neither
   * in the state <code>INITIALIZED</code> or <code>RUNNING</code> it
   * is discarted.
   * 
   * @param job
   *          the job to execute
   * @return an instance of <code>IRunning</code> allowing you to wait
   *         for the job to complete
   * @throws NullPointerException
   *           if <code>job==null</code>.
   */
  public IWaitable executeJob(final Runnable job) {
    return this.m_job.m_state.addSubJob(job, this.m_job);
  }

  /**
   * Wait until all tasks of the current job are finished.
   */
  public void flushJobs() {
    this.m_job.m_state.flush(this);
  }

  /**
   * Perform the work of this thread.
   */
  @Override
  protected void doRun() {
    while (this.isRunning()) {
      this.getJobSystem().doJob(this);
    }
  }

  /**
   * Obtain the job information interface provided at job creation.
   * 
   * @param <G>
   *          the genotype of the optimizer job
   * @param <PP>
   *          the phenotype of the optimizer job
   * @return The job information interface.
   */
  @SuppressWarnings("unchecked")
  public <G extends Serializable, PP extends Serializable> IJobInfo<G, PP> getJobInfo() {
    return ((IJobInfo<G, PP>) (this.m_job.m_state.m_info));
  }

  // /**
  // * Obtain the simulation manager of this job system.
  // *
  // * @return The simulation manager of the job system running this job.
  // */
  // public ISimulationManager getSimulationManager() {
  // return this.m_js.getSimulationManager();
  // }

  /**
   * does nothing
   * 
   * @param eh
   *          ignored
   * @throws SecurityException
   *           always
   */
  @Override
  public void setUncaughtExceptionHandler(final UncaughtExceptionHandler eh) {
    throw new SecurityException();
  }

  /**
   * Forward an event to the job system owning this host.
   * 
   * @param event
   *          The event to be processed.
   */
  @Override
  public void doReceiveEvent(final IEvent event) {
    this.getJobSystem().receiveEvent(this.m_job.m_state.m_id, event);
  }

  /**
   * This method is called whenever an error was caught. It effectively
   * replaces the <code>UncaughtExceptionHandler</code>
   * 
   * @param t
   *          the error caught
   */
  @Override
  protected void onError(final Throwable t) {
    Serializable s;
    MultiProcessorJobSystem js;

    super.onError(t);

    js = this.getJobSystem();
    js.onError(t);
    s = this.m_job.m_state.m_id;
    js.receiveEvent(s, new ErrorEvent(s, t));
  }

  /**
   * Obtain the unique identifier of the optimization process this task is
   * part of.
   * 
   * @return the unique identifier of the optimization process this task is
   *         part of
   */
  public Serializable getOptimizationId() {
    return this.m_job.m_state.m_id;
  }

  /**
   * Obtain the multi-processor job system
   * 
   * @return the multi-processor job system
   */
  final MultiProcessorJobSystem doGetJS() {
    return this.getJobSystem();
  }

  // /**
  // * Returns the randomizer for the jobs running in this host.
  // *
  // * @return the randomizer for the jobs running in this host
  // */
  // public IRandomizer getRandomizer() {
  // return this.m_randomizer;
  // }
}

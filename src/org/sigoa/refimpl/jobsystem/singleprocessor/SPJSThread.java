/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-25
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.jobsystem.singleprocessor.SPJSThread.java
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

import org.sigoa.refimpl.events.ErrorEvent;
import org.sigoa.refimpl.jobsystem.JobSystemThread;
import org.sigoa.spec.events.IEvent;
import org.sigoa.spec.jobsystem.IJobInfo;
import org.sigoa.spec.jobsystem.IWaitable;
import org.sigoa.spec.jobsystem.JobSystemUtils;

/**
 * The single processor executor thread.
 * 
 * @author Thomas Weise
 */
public class SPJSThread extends JobSystemThread<SingleProcessorJobSystem> {
  // /**
  // * The executor.
  // */
  // private final SingleProcessorJobSystem m_js;

  /**
   * The job info record.
   */
  private IJobInfo<?, ?> m_info;

  /**
   * The internal job.
   */
  private SPJSJob<?, ?> m_job;

  // /**
  // * The internal randomizer.
  // */
  // private final IRandomizer m_randomizer;

  /**
   * Create a new single processor executor thread.
   * 
   * @param executor
   *          The owning executor.
   * @param t
   *          the thread group
   */
  public SPJSThread(final SingleProcessorJobSystem executor,
      final ThreadGroup t) {
    super("executorThread", executor, t);//$NON-NLS-1$
    // this.m_js = executor;
    // this.m_randomizer = executor.doCreateRandomizer();
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
    job.run();
    return JobSystemUtils.NO_WAIT_WAITABLE;
  }

  /**
   * Wait until all tasks of the current job are finished.
   */
  public void flushJobs() {
    //
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
    return ((IJobInfo<G, PP>) (this.m_info));
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
    SingleProcessorJobSystem j;

    super.onError(t);
    j = this.getJobSystem();

    j.onError(t);
    s = this.m_job.m_id;
    j.receiveEvent(s, new ErrorEvent(s, t));
  }

  /**
   * Perform the work of this thread.
   */
  @Override
  protected void doRun() {
    SPJSJob<?, ?> s;

    while (this.isRunning()) {
      s = this.getJobSystem().getJob();
      if (s == null)
        return;
      try {
        this.m_info = s.m_info;
        this.m_job = s;
        s.m_job.run();
      } catch (Throwable t) {
        // t.printStackTrace();
        this.onError(t);
      } finally {
        s.finished();
        this.m_job = null;
        this.m_info = null;
      }

    }
  }

  /**
   * Forward an event to the executor owning this host.
   * 
   * @param event
   *          The event to be processed.
   */
  @Override
  public void doReceiveEvent(final IEvent event) {
    this.getJobSystem().receiveEvent(this.m_job.m_id, event);
  }

  /**
   * Obtain the unique identifier of the optimization process this task is
   * part of.
   * 
   * @return the unique identifier of the optimization process this task is
   *         part of
   */
  public Serializable getOptimizationId() {
    return this.m_job.m_id;
  }
}

/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-23
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.jobsystem.multiprocessor.MPJSJob.java
 * Last modification: 2006-11-23
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

import org.sfc.utils.ErrorUtils;
import org.sigoa.refimpl.events.ErrorEvent;
import org.sigoa.spec.jobsystem.IJobInfo;
import org.sigoa.spec.jobsystem.IOptimizationHandle;
import org.sigoa.spec.jobsystem.IWaitable;

/**
 * This class holds the information on a certain job.
 *
 * @author Thomas Weise
 */
final class MPJSJob extends MPJSJobBase implements IOptimizationHandle {
  /**
   * the job system.
   */
  final MultiProcessorJobSystem m_js;

  /**
   * the job info
   */
  final IJobInfo<?, ?> m_info;

  /**
   * the job synchronizer
   */
  private final Object m_jobSync;

  /**
   * the count of currently assigned processors.
   */
  private int m_processorCount;

  /**
   * the job id
   */
  final Serializable m_id;

  /**
   * Create a new multi-processor job system job state.
   *
   * @param js
   *          the job system
   * @param info
   *          the information
   * @param opt
   *          the runnable
   * @param id
   *          the job id
   */
  MPJSJob(final MultiProcessorJobSystem js, final IJobInfo<?, ?> info,
      final Runnable opt, final Serializable id) {
    super(opt, null, null);
    this.m_state = this;
    this.m_info = info;
    this.m_js = js;
    this.m_next = this;
    this.m_jobSync = new Object();
    this.m_id = id;
  }

  /**
   * add a sub job to this job.
   *
   * @param job
   *          the job
   * @param owner
   *          the owner
   * @return a waitable instance
   * @throws NullPointerException
   *           if <code>job==null</code>
   */
  final IWaitable addSubJob(final Runnable job, final MPJSJobBase owner) {
    MPJSJobBase sj;
    if (job == null)
      throw new NullPointerException();

    sj = new MPJSJobBase(job, owner, this);
    synchronized (this.m_jobSync) {
      sj.m_next = this.m_next;
      this.m_next = sj;
      this.m_jobSync.notify();
    }

    synchronized (this.m_js.m_jobs) {
      this.m_js.m_flag++;
      this.m_js.m_jobs.notify();
    }

    return sj;
  }

  /**
   * Perform a job.
   *
   * @param t
   *          the thread which wants to do some work
   * @return <code>true</code> if a job was available and has been
   *         executed <code>false</code> otherwise
   */
  final boolean doJob(final MPJSThread t) {
    MPJSJobBase sj, oj;
    boolean newt;

    oj = t.m_job;
    newt = ((oj == null) || (t.m_job.m_state != this));

    synchronized (this.m_jobSync) {
      if (newt
          && (this.m_processorCount >= this.m_info.getExecutionInfo()
              .getMaxProcessorCount()))
        return false;
      sj = this.m_next;
      if (sj == null)
        return false;
      if (sj.m_next != this)
        this.m_next = sj.m_next;
      else
        this.m_next = null;
      if (newt)
        this.m_processorCount++;
    }

    if (newt && (oj != null)) {
      synchronized (oj.m_state.m_jobSync) {
        oj.m_state.m_processorCount--;
      }
    }

    t.m_job = sj;
    try {
      sj.m_runnable.run();
    } catch (Throwable tt) {
//      this.m_js.onError(tt);
      ErrorUtils.onError(tt);
      this.m_js.receiveEvent(this.m_id, new ErrorEvent(this.m_id, tt));
    }

    t.m_job = oj;
    sj.oneFinished();

    if (newt) {
      synchronized (this.m_jobSync) {
        this.m_processorCount--;
      }
      if (oj != null) {
        synchronized (oj.m_state.m_jobSync) {
          oj.m_state.m_processorCount++;
        }
      }
    }

    return true;
  }

  /**
   * flush all jobs
   *
   * @param t
   *          the thread which wants to do some work
   */
  final void flush(final MPJSThread t) {
    MPJSJobBase j;

    j = t.m_job;

    while (!(j.isFlushed())) {
      if (!(this.doJob(t)))
        Thread.yield();
    }
  }

  /**
   * abort this job.
   */
  @Override
  final void abort() {
    MPJSJobBase b;
    main: for (;;) {
      synchronized (this.m_jobSync) {
        b = this.m_next;
        if (b != null) {
          b.abort();
          if(b.m_next == b) break main;
          this.m_next = b.m_next;
        } else
          break main;
      }
    }
    super.abort();
  }

  /**
   * Obtain the unique identifier of the running optimization process.
   *
   * @return the unique identifier of the running optimization process.
   */
  public final Serializable getId() {
    return this.m_id;
  }
}

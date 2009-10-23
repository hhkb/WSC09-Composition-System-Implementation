/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-24
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.jobsystem.multiprocessor.MPJSJobBase.java
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

import org.sigoa.spec.jobsystem.IWaitable;

/**
 * The internal sub-job base class.
 * 
 * @author Thomas Weise
 */
class MPJSJobBase implements IWaitable {
  /**
   * the runnable to execute.
   */
  final Runnable m_runnable;

  /**
   * the pending sub jobs.
   */
  private int m_pending;

  /**
   * the owning job
   */
  private MPJSJobBase m_owner;

  /**
   * the next job in the queue.
   */
  MPJSJobBase m_next;

  /**
   * the owning job state
   */
  MPJSJob m_state;

  /**
   * Create a new job base.
   * 
   * @param exec
   *          the runnable to execute
   * @param owner
   *          the owning job base
   * @param state
   *          the owning job state
   */
  MPJSJobBase(final Runnable exec, final MPJSJobBase owner,
      final MPJSJob state) {
    super();
    this.m_runnable = exec;
    this.m_pending = 1;
    this.m_owner = owner;
    this.m_state = state;
    if (owner != null) {
      synchronized (owner) {
        owner.m_pending++;
      }
    }
  }

  /**
   * one of the sub-jobs or the main jobs is finished.
   */
  final void oneFinished() {
    boolean b;
    b = false;
    MultiProcessorJobSystem j;
    synchronized (this) {
      if ((--this.m_pending) == 0) {
        b = true;
        this.notifyAll();
      }
    }
    if (b) {
      if (this.m_owner != null)
        this.m_owner.oneFinished();
      else {
        j = ((MPJSJob) this).m_js;
        synchronized (j.m_jobs) {
          j.m_jobs.remove(this);
        }
      }
    }
  }

  /**
   * abort this job.
   */
  void abort() {
    synchronized (this) {
      this.m_pending = 0;
      this.notifyAll();
    }
    if (this.m_owner != null) {
      this.m_owner.oneFinished();
    }
  }

  /**
   * Wait until the event occures.
   * 
   * @param interruptible
   *          <code>true</code> if the waiting should be aborted if the
   *          current thread is interrupted - <code>false</code> if
   *          interrupting should be ignored (in this case this method may
   *          return prematurely).
   * @return <code>true</code> if the wait operation was interrupted,
   *         <code>false</code> otherwise
   * @throws IllegalStateException
   *           if a main job waits for itself
   */
  public final boolean waitFor(final boolean interruptible) {
    Thread t;
    MPJSThread m;
    MPJSJob s;
    MPJSJobBase b;

    t = Thread.currentThread();

    if (t instanceof MPJSThread) {
      m = ((MPJSThread) t);
      if (m.doGetJS() == this.m_state.m_js) {
        s = this.m_state;
        b = m.m_job;
        if (b.m_state != s)
          throw new IllegalStateException();

        while (!(this.isDone())) {
          s.doJob(m);
        }

        return false;
      }
    }

    for (;;) {
      synchronized (this) {
        if (this.m_pending <= 0)
          return false;
        try {
          this.wait();
        } catch (InterruptedException ie) {
          if (interruptible)
            return true;
        }
      }
    }
  }

  /**
   * check if this job is done
   * 
   * @return <code>true</code> if it is, <code>false</code> otherwise
   */
  final boolean isDone() {
    synchronized (this) {
      return (this.m_pending <= 0);
    }
  }

  /**
   * check if this job is flushed
   * 
   * @return <code>true</code> if it is, <code>false</code> otherwise
   */
  final boolean isFlushed() {
    synchronized (this) {
      return (this.m_pending <= 1);
    }
  }
}

/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-25
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.jobsystem.singleprocessor.SPJSJob.java
 * Last modification: 2006-11-25
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

import org.sigoa.spec.jobsystem.IJobInfo;
import org.sigoa.spec.jobsystem.IOptimizationHandle;

/**
 * The internal job holder class.
 *
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
final class SPJSJob<G extends Serializable, PP extends Serializable>
    implements IOptimizationHandle {

  /**
   * The job.
   */
  final Runnable m_job;

  /**
   * The job info.
   */
  IJobInfo<G, PP> m_info;

  /**
   * The next executor job.
   */
  SPJSJob<?, ?> m_next;

  /**
   * the unique identifier.
   */
  final Serializable m_id;

  /**
   * Create a new single processor executor job.
   *
   * @param job
   *          The job.
   * @param jobInfo
   *          The job info.
   * @param next
   *          The next job.
   * @param id
   *          the unique identifier
   */
  SPJSJob(final Runnable job, final IJobInfo<G, PP> jobInfo,
      final SPJSJob<?, ?> next, final Serializable id) {
    super();
    this.m_job = job;
    this.m_info = jobInfo;
    this.m_next = next;
    this.m_id = id;
  }

  /**
   * set the job to finished
   */
  final void finished() {
    synchronized (this) {
      this.m_info = null;
      this.notifyAll();
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
   */
  public final boolean waitFor(final boolean interruptible) {
    for (;;) {
      synchronized (this) {
        if (this.m_info == null)
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
   * Obtain the unique identifier of the running optimization process.
   *
   * @return the unique identifier of the running optimization process.
   */
  public final Serializable getId() {
    return this.m_id;
  }
}

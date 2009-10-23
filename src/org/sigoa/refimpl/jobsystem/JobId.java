/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-08
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.jobsystem.JobId.java
 * Last modification: 2006-12-08
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

import org.sfc.text.Textable;
import org.sigoa.spec.jobsystem.IJobSystem;

/**
 * A basic id class which can be used to uniquely identify running
 * optimization jobs in a job system.
 *
 * @author Thomas Weise
 */
public class JobId extends Textable implements Serializable {
  /**
   * the serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the internal synchronizer.
   */
  private static final Object SYNC = new Object();

  /**
   * the internal id counter.
   */
  private static long s_idCnt = (System.nanoTime() ^ System
      .identityHashCode(SYNC));

  /**
   * 64 additional bits
   */
  private final long m_rest;

  /**
   * the unique number assigned
   */
  private final long m_uniqueNumber;

  /**
   * Generate a new job id.
   *
   * @param jobSystem
   *          the job system for which the id should be generated.
   */
  public JobId(final IJobSystem jobSystem) {
    super();
    synchronized (SYNC) {
      this.m_uniqueNumber = (s_idCnt++);
    }
    this.m_rest = (System.currentTimeMillis() ^ ((((long) (System
        .identityHashCode(jobSystem))) << 32L) | System
        .identityHashCode(this)));
  }

  /**
   * Check whether this job id equals a given object or not.
   *
   * @param o
   *          the object to compare with
   * @return <code>true</code> if and only if the object is also a job id
   *         and identifies the same job, <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object o) {
    JobId j;
    if (o == this)
      return true;
    if (o instanceof JobId) {
      j = ((JobId) o);
      return ((j.m_rest == this.m_rest) && (j.m_uniqueNumber == this.m_uniqueNumber));
    }
    return false;
  }

  /**
   * Compute the hash code of this job id.
   *
   * @return the hash code of this job id
   */
  @Override
  public int hashCode() {
    return (int) (this.m_uniqueNumber + this.m_rest);
  }

  /**
   * Append this object's textual representation to a string builder.
   *
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    sb.append(Long.toHexString(this.m_rest));
    sb.append(':');
    sb.append(Long.toHexString(this.m_uniqueNumber));
  }
}

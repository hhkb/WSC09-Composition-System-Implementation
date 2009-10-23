/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-10
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.jobsystem.IActivity.java
 * Last modification: 2007-02-21
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

package org.sigoa.spec.jobsystem;

/**
 * A basic interface common to jobs and such and such. It is the foundation
 * for the <code>IActivity2</code>-interface.
 *
 * @author Thomas Weise
 */
public interface IActivity extends IWaitable {

  /**
   * Returns <code>true</code> if and only if the activity is in the
   * state RUNNING.
   *
   * @return <code>true</code> if the activity is running,
   *         <code>false</code> otherwise
   */
  public abstract boolean isRunning();

  /**
   * Returns <code>true</code> if and only if the activity is in the
   * state TERMINATED.
   *
   * @return <code>true</code> if the activity is terminated,
   *         <code>false</code> otherwise
   */
  public abstract boolean isTerminated();

  /**
   * An activity becomes final if it is either TERMINATING or TERMINATED.
   *
   * @return <code>true</code> if and only if this activity is final,
   *         <code>false</code> otherwise.
   */
  public abstract boolean isFinal();

  /**
   * Abort an activity. Aborting an activity means telling it to gently
   * stop all its doing. A call to this method must not block and just
   * trigger asynchronously some action leading to the activity's shutdow.
   * If the activity is not yet in the states <code>TERMINATING</code> or
   * <code>TERMINATED</code>, calling this method leads to an immediate
   * transition to <code>TERMINATING</code>.
   */
  public abstract void abort();

}

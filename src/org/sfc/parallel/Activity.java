/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 *
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-02-23
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.parallel.Activity.java
 * Last modification: 2007-02-23
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

package org.sfc.parallel;

import org.sfc.utils.ErrorUtils;

/**
 * This class can be used as base to implement the behavior of an activity,
 * that is something that runs in parallel.
 * 
 * @author Thomas Weise
 */
public class Activity extends ErrorHandler {

  /**
   * The internal activity state.
   */
  private EActivityState m_state;

  /**
   * The internal synchronizer object.
   */
  private final Object m_sync;

  /**
   * Create a new instance of <code>IActivity2</code> .
   */
  protected Activity() {
    this.m_state = EActivityState.INITIALIZED;
    this.m_sync = new Object();
  }

  /**
   * Returns <code>true</code> if and only if the activity is in the
   * state RUNNING.
   * 
   * @return <code>true</code> if the activity is running,
   *         <code>false</code> otherwise
   */
  public boolean isRunning() {
    return (this.m_state == EActivityState.RUNNING);
  }

  /**
   * Sleep a fixed time in a safe manner.
   * 
   * @param time
   *          the time to sleep
   */
  protected void safeSleep(final long time) {
    try {
      synchronized (this.m_sync) {
        this.m_sync.wait(time);
      }
    } catch (Throwable t) {
      ErrorUtils.onError(t);
    }
  }

  /**
   * Returns <code>true</code> if and only if the activity is in the
   * state TERMINATED.
   * 
   * @return <code>true</code> if the activity is terminated,
   *         <code>false</code> otherwise
   */
  public boolean isTerminated() {
    return (this.m_state == EActivityState.TERMINATED);
  }

  /**
   * An activity becomes final if it is either TERMINATING or TERMINATED.
   * 
   * @return <code>true</code> if and only if this activity is final,
   *         <code>false</code> otherwise.
   */
  public boolean isFinal() {
    EActivityState s;
    s = this.m_state;
    return ((s == EActivityState.TERMINATED) || (s == EActivityState.TERMINATING));
  }

  /**
   * Abort an activity. Aborting an activity means telling it to gently
   * stop all its doing. A call to this method must not block and just
   * trigger asynchronously some action leading to the activity's shutdow.
   * If the activity is not yet in the states <code>TERMINATING</code> or
   * <code>TERMINATED</code>, calling this method leads to an immediate
   * transition to <code>TERMINATING</code>.
   * 
   * @see #doAbort()
   */
  public final void abort() {
    EActivityState s;
    synchronized (this.m_sync) {
      s = this.m_state;
      switch (s) {
      case INITIALIZED:
      case RUNNING: {
        this.m_state = EActivityState.TERMINATING;
        this.m_sync.notifyAll();
        break;
      }
        // case INITIALIZED: {
        // this.m_state = EActivityState.TERMINATED;
        // break;
        // }
      default:
        return;
      }
    }
    this.doAbort();
  }

  /**
   * This method performs the shutdown. It is called by
   * <code>abort()</code> at most once.
   * 
   * @see #abort()
   */
  protected void doAbort() {
    //
  }

  /**
   * Set that this activity is finished. This leads to a transition into
   * the state <code>TERMINATED</code>.
   */
  protected void finished() {
    synchronized (this.m_sync) {
      this.m_state = EActivityState.TERMINATED;
      this.m_sync.notifyAll();
    }
  }

  /**
   * <p>
   * Start the activity. Calling this method twice or on an activity that
   * has already finished its work will result in an
   * <code>IllegalStateException</code>. Therefore, this method must
   * only be called as long as the activity is in the state
   * <code>INITIALIZED</code>.
   * </p>
   * <p>
   * This method is optional, at least in the context of the
   * <code>IRunning</code>-interface.
   * </p>
   * 
   * @throws IllegalStateException
   *           if the executor has already finished or was already started.
   * @see #doStart()
   */
  public final void start() {
    synchronized (this.m_sync) {
      if (this.m_state != EActivityState.INITIALIZED)
        throw new IllegalStateException();
      this.m_state = EActivityState.RUNNING;
    }
    this.doStart();
  }

  /**
   * Perform the work of the startup. This method is called by
   * <code>start()</code> at most once.
   * 
   * @see #start()
   */
  protected void doStart() {
    //
  }

  /**
   * Wait until the activity has finished its doing.
   * 
   * @param interruptible
   *          <code>true</code> if the waiting should be aborted if the
   *          current thread is interrupted - <code>false</code> if
   *          interrupting should be ignored (in this case this method may
   *          return prematurely).
   * @return <code>true</code> if and only if the wait operation was
   *         interrupted and <code>interruptible==true</code>,
   *         <code>false</code> otherwise
   */
  public boolean waitFor(final boolean interruptible) {
    for (;;) {
      synchronized (this.m_sync) {
        if (this.m_state == EActivityState.TERMINATED)
          return false;
        try {
          this.m_sync.wait();
        } catch (InterruptedException ie) {
          if (interruptible)
            return true;
        }
      }

    }
  }
}

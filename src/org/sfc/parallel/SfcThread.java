/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-02-23
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.parallel.SfcThread.java
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

/**
 * The base class for all sfc threads.
 * 
 * @author Thomas Weise
 */
public abstract class SfcThread extends Thread {

  /**
   * The internal activity.
   */
  private InternalActivity m_activity;

  /**
   * Allocates a new <code>SfcThread</code> object.
   */
  protected SfcThread() {
    this("nameless"); //$NON-NLS-1$
  }

  /**
   * Allocates a new <code>Thread</code> object.
   * 
   * @param name
   *          the name of the new thread.
   */
  protected SfcThread(final String name) {
    this((ThreadGroup) null, name);
  }

  /**
   * Allocates a new <code>Thread</code> object.
   * 
   * @param group
   *          the thread group.
   * @param name
   *          the name of the new thread.
   * @exception SecurityException
   *              if the current thread cannot create a thread in the
   *              specified thread group.
   */
  protected SfcThread(final ThreadGroup group, final String name) {
    super(group, name);
    this.m_activity = new InternalActivity();
  }

  /**
   * Returns <code>true</code> if and only if the thread is in the state
   * RUNNING.
   * 
   * @return <code>true</code> if the thread is running,
   *         <code>false</code> otherwise
   */
  public boolean isRunning() {
    return this.m_activity.isRunning();
  }

  /**
   * Returns <code>true</code> if and only if the thread is in the state
   * TERMINATED.
   * 
   * @return <code>true</code> if the thread is terminated,
   *         <code>false</code> otherwise
   */
  public boolean isTerminated() {
    return this.m_activity.isTerminated();
  }

  /**
   * An activity becomes final if it is either TERMINATING or TERMINATED.
   * 
   * @return <code>true</code> if and only if this activity is final,
   *         <code>false</code> otherwise.
   */
  public boolean isFinal() {
    return this.m_activity.isFinal();
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
  @Override
  public final void start() {
    this.m_activity.start();
  }

  /**
   * Start this thread.
   */
  protected void doStart() {
    super.start();
  }

  /**
   * Abort an activity. Aborting an activity means telling it to gently
   * stop all its doing. A call to this method must not block and just
   * trigger asynchronously some action leading to the activity's shutdow.
   * If the activity is not yet in the states <code>TERMINATING</code> or
   * <code>TERMINATED</code>, calling this method leads to an immediate
   * transition to <code>TERMINATING</code>.
   */
  public final void abort() {
    checkAccess();
    this.m_activity.abort();
  }

  /**
   * Abort this thread.
   */
  protected void doAbort() {
    super.interrupt();
  }

  /**
   * Interrupt == abort this thread.
   */
  @Override
  public void interrupt() {
    if (this != Thread.currentThread()) {
      checkAccess();
      this.abort();
    } else
      super.interrupt();
  }

  /**
   * Perform the work of this thread.
   */
  protected abstract void doRun();

  /**
   * This method is called whenever an error was caught. It effectively
   * replaces the <code>UncaughtExceptionHandler</code>
   * 
   * @param t
   *          the error caught
   */
  protected void onError(final Throwable t) {
    ErrorHandler.checkError(t);
  }

  /**
   * Wait until the thread has finished running.
   * 
   * @param interruptible
   *          <code>true</code> if the waiting should be aborted if the
   *          current thread is interrupted - <code>false</code> if
   *          interrupting should be ignored (in this case this method may
   *          return prematurely).
   * @return <code>true</code> if the wait operation was interrupted,
   *         <code>false</code> otherwise
   */
  public boolean waitFor(final boolean interruptible) {
    for (;;) {
      if ((this.m_activity.isTerminated()) && (!(this.isAlive())))
        return false;
      try {
        super.join();
      } catch (InterruptedException ie) {
        if (interruptible)
          return true;
      }
    }
  }

  /**
   * Sleep a fixed time in a safe manner.
   * 
   * @param time
   *          the time to sleep
   */
  protected void safeSleep(final long time) {
    this.m_activity.safeSleep(time);
  }

  /**
   * Run this thread. This method defers to <code>doRun()</code>.
   * 
   * @see #doRun()
   */
  @Override
  public final void run() {
    if (this != Thread.currentThread())
      throw new IllegalStateException();
    try {
      this.doRun();
    } catch (Throwable t) {
      this.onError(t);
    } finally {
      this.finished();
    }
  }

  /**
   * Set that this activity is finished. This leads to a transition into
   * the state <code>TERMINATED</code>.
   */
  protected void finished() {
    this.m_activity.finished();
  }

  /**
   * The internal activity used to manage the thread's state.
   * 
   * @author Thomas Weise
   */
  private final class InternalActivity extends Activity {
    /**
     * Create the internal activity.
     */
    InternalActivity() {
      super();
    }

    /**
     * Start the owning thread.
     */
    @Override
    protected void doStart() {
      SfcThread.this.doStart();
    }

    /**
     * Abort the owning thread.
     */
    @Override
    protected void doAbort() {
      SfcThread.this.doAbort();
    }

    /**
     * Called by the owning thread when its finished.
     */
    @Override
    protected void finished() {
      super.finished();
    }
  }

  /**
   * Delay for the given time.
   * 
   * @param time
   *          the time to sleep
   */
  public static final void pause(final long time) {
    Thread t;

    t = Thread.currentThread();
    if (t instanceof SfcThread) {
      ((SfcThread) t).safeSleep(time);
    } else {
      try {
        Thread.sleep(time);
      } catch (InterruptedException ie) {
        return;
      }
    }
  }
}

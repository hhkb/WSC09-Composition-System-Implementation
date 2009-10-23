/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-02-23
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.parallel.ThreadActivity.java
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

import org.sfc.collections.lists.SimpleList;

/**
 * This activity hosts threads.
 * 
 * @author Thomas Weise
 */
public class ThreadActivity extends Activity {

  /**
   * the thread group of the threads
   */
  private final ThreadGroup m_threadGroup;

  /**
   * the internal array of the threads
   */
  private SimpleList<SfcThread> m_threads;

  /**
   * Create a new activity the uses own threads.
   * 
   * @param threadGroup
   *          the thread group to be used or <code>null</code> if none is
   *          required
   */
  protected ThreadActivity(final ThreadGroup threadGroup) {
    super();
    this.m_threadGroup = ((threadGroup != null) ? threadGroup : this
        .createThreadGroup(this.toString() + ".threads")); //$NON-NLS-1$
    this.m_threads = new SimpleList<SfcThread>();
  }

  /**
   * Obtain the thread group of this activity.
   * 
   * @return the thread group of this activity
   */
  protected ThreadGroup getThreadGroup() {
    return this.m_threadGroup;
  }

  /**
   * add a thread to this activity
   * 
   * @param thread
   *          the thread to be added
   * @throws IllegalStateException
   *           if this activity is already final
   */
  protected void addThread(final SfcThread thread) {
    if (this.isFinal())
      throw new IllegalStateException();
    if (thread != null)
      synchronized (this.m_threads) {
        this.m_threads.add(thread);
      }
  }

  /**
   * remove a thread from the internal thread list.
   * 
   * @param thread
   *          the thread to be removed
   */
  protected void removeThread(final SfcThread thread) {
    synchronized (this.m_threads) {
      this.m_threads.removeFast(thread);
    }
  }

  /**
   * Perform the work of the startup. This method is called by
   * <code>start()</code> at most once.
   * 
   * @see #start()
   */
  @Override
  protected void doStart() {
    SimpleList<SfcThread> l;
    SfcThread t;
    int i;

    super.doStart();

    l = this.m_threads;
    synchronized (l) {
      for (i = (l.size()- 1); i >= 0; i--) {
        t = l.get(i);
        if (!(t.isRunning() || t.isFinal()))
          t.start();
      }
    }
  }

  /**
   * abort all the threads of this activity
   */
  protected void abortThreads() {
    SimpleList<SfcThread> l;
    int i;

    l = this.m_threads;

    synchronized (l) {
      for (i = (l.size() - 1); i >= 0; i--) {
        l.get(i).abort();
      }
    }

  }

  /**
   * This method performs the shutdown. It is called by
   * <code>abort()</code> at most once.
   * 
   * @see #abort()
   */
  @Override
  protected void doAbort() {
    this.abortThreads();
    super.doAbort();
    this.finished();
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
  @Override
  public boolean waitFor(final boolean interruptible) {
    SimpleList<SfcThread> l;
    SfcThread t;
    boolean b;

    l = this.m_threads;
    b = true;
    while (b) {
      synchronized (l) {
        if (l.size() <= 0)
          break;
        t = l.removeLast();
      }

      if (t.waitFor(interruptible)) {
        if (interruptible) {
          b = false;
          synchronized (l) {
            l.add(t);
          }
        }

      }
    }

    if (b)
      return super.waitFor(interruptible);
    return false;
  }

  /**
   * The internal method called in order to create the thread group used by
   * the thread activity.
   * 
   * @param name
   *          the name that the new thread group should have
   * @return the new thread group
   */
  protected ThreadGroup createThreadGroup(final String name) {
    return new ThreadGroup(name);
  }
}

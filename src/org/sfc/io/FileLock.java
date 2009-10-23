/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-18
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.FileLock.java
 * Last modification: 2007-10-18
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

package org.sfc.io;

import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import org.sfc.scoped.DefaultReferenceCounted;
import org.sfc.utils.ErrorUtils;

/**
 * The class file lock allows you to perform global, file-system based
 * synchronization. You can use it to synchronize different processes that
 * cooperatively use file system output / lockfiles. 
 * 
 * @author Thomas Weise
 */
public class FileLock extends DefaultReferenceCounted {
  /**
   * the internal file channel
   */
  private FileChannel m_channel;

  /**
   * the lock counter
   */
  private volatile int m_lockCnt;

  /**
   * the lock
   */
  private volatile java.nio.channels.FileLock m_lock;

  /**
   * Create a new glocal lock
   * 
   * @param file
   *          the file on which the lock is based
   */
  public FileLock(final Object file) {
    super();

    CanonicalFile f;
    FileChannel c;

    f = IO.getFile(file);
    c = null;

    if (f != null) {
      f.deleteOnExit();

      try {
        c = new FileOutputStream(f, true).getChannel();
      } catch (Throwable t) {
        ErrorUtils.onError(t);
      }
    }
    this.m_channel = c;
  }

  /**
   * This method is automatically called when the object gets disposed. You
   * should perform your cleanup in this method. If you override this
   * method, don't forget to call the super method.
   */
  @Override
  protected synchronized void dispose() {
    java.nio.channels.FileLock l;

    l = this.m_lock;
    if (l != null)
      try {
        l.release();
      } catch (Throwable t) {
        ErrorUtils.onError(t);
      } finally {
        this.m_lock = null;
      }

    if (this.m_channel != null) {
      try {
        this.m_channel.close();
      } catch (Throwable t) {//
      } finally {
        this.m_channel = null;
      }
    }

    super.dispose();
  }

  /**
   * lock this global lock
   */
  public synchronized void lock() {
    if (((this.m_lockCnt++) == 0) && (this.m_channel != null)) {
      try {
        this.m_lock = this.m_channel.lock();
      } catch (Throwable t) {
        ErrorUtils.onError(t);
      }
    }
  }

  /**
   * unlock this global lock
   */
  public synchronized void unlock() {
    if (((--this.m_lockCnt) == 0) && (this.m_lock != null)) {
      try {
        this.m_lock.release();
      } catch (Throwable t) {
        ErrorUtils.onError(t);
      } finally {
        this.m_lock = null;
      }
    }
  }
}

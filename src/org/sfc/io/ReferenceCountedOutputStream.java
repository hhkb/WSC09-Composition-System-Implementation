/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 *
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.ReferenceCountedOutputStream.java
 * Last modification: 2006-11-26
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

import java.io.IOException;
import java.io.OutputStream;

import org.sfc.scoped.AlreadyDisposedException;
import org.sfc.scoped.IReferenceCounted;
import org.sfc.utils.ErrorUtils;

/**
 * This class is a wrapper for <code>OutputStream</code>s that makes
 * them reference counted.
 * 
 * @author Thomas Weise
 */
public abstract class ReferenceCountedOutputStream extends OutputStream
    implements IReferenceCounted {
  /**
   * The internal reference counter.
   */
  private int m_refCount;

  /**
   * <code>true</code> only as long as this object hasn't been disposed.
   */
  private boolean m_living;

  /**
   * Create a reference counted <code>OutputStream</code>.
   */
  protected ReferenceCountedOutputStream() {
    super();
    this.m_refCount = 1;
    this.m_living = true;
  }

  /**
   * Increase the reference counter of this object by one. You must call
   * <code>addRef</code> before you assign a variable to point on this
   * object (except when calling the constructor and storing the result to
   * exactly one variable).
   * 
   * @throws AlreadyDisposedException
   *           If the object has already been disposed.
   */
  public void addRef() throws AlreadyDisposedException {
    synchronized (this) {
      if (this.m_living) {
        this.m_refCount++;
      } else {
        throw new AlreadyDisposedException();
      }
    }
  }

  /**
   * Decrease the reference counter of this object by one. If the reference
   * count reaches zero, the object will automatically perform cleanup
   * operations. You must call <code>release</code> whenever you don't
   * need a variable pointing to this object anymore.
   * 
   * @throws AlreadyDisposedException
   *           If the object has already been disposed.
   */
  public void release() throws AlreadyDisposedException {
    try {
      this.doRelease();
    } catch (IOException ioe) {
      ErrorUtils.onError(ioe);
    }
  }

  /**
   * Decrease the reference counter of this object by one. If the reference
   * count reaches zero, the object will automatically perform cleanup
   * operations. You must call <code>release</code> whenever you don't
   * need a variable pointing to this object anymore.
   * 
   * @throws IOException
   *           If the underlying was closed and caused an IOException.
   * @throws AlreadyDisposedException
   *           If the object has already been disposed.
   */
  private final void doRelease() throws IOException,
      AlreadyDisposedException {
    synchronized (this) {
      if (this.m_living) {
        if ((--this.m_refCount) <= 0) {
          this.m_living = false;
          this.dispose();
        }
      } else {
        throw new AlreadyDisposedException();
      }
    }
  }

  /**
   * The internal dispose method.
   * 
   * @throws IOException
   *           If the underlying was closed and caused an IOException.
   */
  protected void dispose() throws IOException {
    super.close();
  }

  /**
   * Close the stream. This method supersets the normal
   * <code>close()</code>- method and delegates to
   * <code>release()</code>, so you can easily pass objects of this
   * class to methods that use normal semantics.
   * 
   * @throws IOException
   *           if an I/O error occurs.
   */
  @Override
  public void close() throws IOException {
    doRelease();
  }

  /**
   * This method ensures the <code>close()</code> is called if it was not
   * yet called due to a programming mistake.
   * 
   * @throws Throwable
   *           Maybe thrown by the super method.
   */
  @Override
  protected void finalize() throws Throwable {

    synchronized (this) {
      if (this.m_living) {
        this.m_living = false;
        super.close();
      }
      this.m_refCount = 0;
    }

    super.finalize();
  }

  /**
   * Use this method to check if the object is still alive. It will throw
   * an error if not.
   */
  protected void checkLiving() {
    if (!(this.m_living))
      throw new AlreadyDisposedException();
  }
}

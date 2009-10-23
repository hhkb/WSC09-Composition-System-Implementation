/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.scoped.DefaultReferenceCounted.java
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

package org.sfc.scoped;

/**
 * This class implements an default reference counting mechanism.
 * 
 * @author Thomas Weise
 */
public class DefaultReferenceCounted extends Scoped implements
    IReferenceCounted {
  /**
   * The internal reference counter.
   */
  private int m_refCount;

  /**
   * Create a new instance of the default base of reference counted
   * objects.
   */
  public DefaultReferenceCounted() {
    super();
    this.m_refCount = 1;
  }

  /**
   * Increase the reference counter of this object by one. You must call
   * <code>add_ref</code> before you assign a variable to point on this
   * object (except when calling the constructor and storing the result to
   * exactly one variable).
   * 
   * @throws AlreadyDisposedException
   *           If the object has already been disposed.
   */
  public synchronized void addRef() throws AlreadyDisposedException {
    if (this.m_living) {
      this.m_refCount++;
    } else {
      throw new AlreadyDisposedException();
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
  public synchronized void release() throws AlreadyDisposedException {
    if (this.m_living) {
      if ((--this.m_refCount) <= 0) {
        this.m_living = false;
        dispose();
      }
    } else {
      throw new AlreadyDisposedException();
    }
  }

  /**
   * Creates and returns a copy of this object. This copy will have a
   * reference-count of one. You must call <code>release</code> on that
   * copy when you're finished with it. You cannot assume the copy's
   * reference count being linked in any way with this object's one.
   * 
   * @return An exact copy of this object.
   * @throws CloneNotSupportedException
   *           If the object doesn't implement the <code>Cloneable</code>
   *           interface.
   */
  @Override
  protected synchronized Object clone() throws CloneNotSupportedException {
    DefaultReferenceCounted l_o;

    this.checkLiving();

    l_o = ((DefaultReferenceCounted) (super.clone()));
    l_o.m_refCount = 1;
    return l_o;
  }
}

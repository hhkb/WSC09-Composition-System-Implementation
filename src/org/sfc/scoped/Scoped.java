/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.scoped.Scoped.java
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
 * This internal class is the base for all scoped classes.
 * 
 * @author Thomas Weise
 */
class Scoped {
  /**
   * <code>true</code> as long as this object is alive,
   * <code>false</code> if it died.
   */
  volatile boolean m_living;

  /**
   * Create a new instance of the default base of scoped classes.
   */
  public Scoped() {
    super();
    this.m_living = true;
  }

  /**
   * Use this method to check if the object is still alive. It will throw
   * an error if not.
   */
  protected void checkLiving() {
    if (!(this.m_living))
      throw new AlreadyDisposedException();
  }

  /**
   * This method is automatically called when the object gets disposed. You
   * should perform your cleanup in this method. If you override this
   * method, don't forget to call the super method.
   */
  protected void dispose() {
    //
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
        this.dispose();
      }
    }

    super.finalize();
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
    if (this.m_living) {
      return super.clone();
    }

    throw new AlreadyDisposedException();
  }
}

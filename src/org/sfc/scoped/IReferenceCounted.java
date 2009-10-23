/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.scoped.IReferenceCounted.java
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
 * <p>
 * This interface is common to all objects that are reference counted. Each
 * such object manages an internal reference counter. When the object is
 * created, the counter is set to 1. Whenever you create a new reference to
 * this object, i.e. let a variable point to it, you must increase its
 * reference counter by calling <code>add_ref()</code> <i>before</i> you
 * do so. Whenever one variable pointing to such an object is no longer
 * needed, you must decrease the object's reference counter by calling
 * <code>release</code>. Furthermore, you should set the variable to
 * <code>null</code> afterwards. If the internal reference counter of the
 * object reaches zero, there are no more references to it. Thus it will
 * not be used in future anymore. Then, it may perform some special
 * operations like closing input or output connections.
 * </p>
 * <p>
 * This interface enables objects to perform something like it was purposed
 * with the <code>finalize</code> method, but in a more reliable way.
 * When its reference counter reaches zero, it may actively perform cleanup
 * instead of waiting for the garbage collector.
 * </p>
 * <p>
 * <strong>NOTE</strong> If you hand over an instance of
 * <code>IReferenceCounted</code> as a parameter to a method, you must
 * call <code>add_ref</code> before calling the method and
 * <code>release</code> after the method returned to ensure that the
 * parameter stays valid during the method's execution.
 * </p>
 * <strong>NOTE</strong> If return an instance of
 * <code>IReferenceCounted</code> as the result of a function call, you
 * must call <code>add_ref</code> before returning it. Calling
 * <code>release</code> must be done by the code receiving your result.
 * </p>
 * 
 * @author Thomas Weise
 */
public interface IReferenceCounted {
  /**
   * Increase the reference counter of this object by one. You must call
   * <code>add_ref</code> before you assign a variable to point on this
   * object (except when calling the constructor and storing the result to
   * exactly one variable). Calls to <code>add_ref</code> must be
   * balanced with calls to <code>release</code>.
   * 
   * @throws AlreadyDisposedException
   *           If the object has already been disposed.
   * @see #release()
   */
  public abstract void addRef() throws AlreadyDisposedException;

  /**
   * Decrease the reference counter of this object by one. If the reference
   * count reaches zero, the object will automatically perform cleanup
   * operations. You must call <code>release</code> whenever you don't
   * need a variable pointing to this object anymore. Calls to
   * <code>add_ref</code> must be balanced with calls to
   * <code>release</code>.
   * 
   * @throws AlreadyDisposedException
   *           If the object has already been disposed.
   * @see #addRef()
   */
  public abstract void release() throws AlreadyDisposedException;
}

/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.security.SfcSecurityManager.java
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

package org.sfc.security;

/**
 * The sfc version of a security manager.
 * 
 * @author Thomas Weise
 */
public class SfcSecurityManager extends SecurityManager {

  /**
   * the internal sfc security manager instance.
   */
  private static final SfcSecurityManager INTERNAL_INSTANCE = new SfcSecurityManager(
      false);

  /**
   * Create a new sfc security manager.
   * 
   * @param set
   *          <code>true</code> if the security manager should be set,
   *          <code>false</code> otherwise.
   */
  public SfcSecurityManager(final boolean set) {
    super();
    if (set)
      System.setSecurityManager(this);
  }

  /**
   * <p>
   * Returns the current execution stack as list of classes.
   * </p>
   * <p>
   * The length of the list is the number of methods on the execution
   * stack. The element at index <code>0</code> is the class of the
   * currently executing method, the element at index <code>1</code> is
   * the class of that method's caller, and so on.
   * </p>
   * 
   * @return the execution stack.
   * @see #getCallStack(int)
   */
  protected CallStack getCallStack() {
    return this.getCallStack(1);
  }

  /**
   * <p>
   * Returns the current execution stack as list of classes.
   * </p>
   * <p>
   * The length of the list is the number of methods on the execution
   * stack. The element at index <code>0</code> is the class of the
   * currently executing method, the element at index <code>1</code> is
   * the class of that method's caller, and so on.
   * </p>
   * 
   * @param offset
   *          An additional offset of classes to be skipped.
   * @return the execution stack.
   * @see #getCallStack()
   */
  protected CallStack getCallStack(final int offset) {
    int c, d;
    Class<?>[] cc;

    cc = super.getClassContext();
    c = (offset + 1);
    d = (cc.length - c);
    System.arraycopy(cc, c, cc, 0, d);

    return new CallStack(cc, d);
  }

  /**
   * <p>
   * Returns the current execution stack as list of classes.
   * </p>
   * <p>
   * The length of the list is the number of methods on the execution
   * stack. The element at index <code>0</code> is the class of the
   * currently executing method, the element at index <code>1</code> is
   * the class of that method's caller, and so on.
   * </p>
   * 
   * @return the execution stack.
   * @see #getCallStack(int)
   */
  public static final CallStack getCurrentCallStack() {
    SecurityManager x;
    x = System.getSecurityManager();
    if (x instanceof SfcSecurityManager) {
      return ((SfcSecurityManager) x).getCallStack(1);
    }
    return INTERNAL_INSTANCE.getCallStack(1);
  }

  /**
   * <p>
   * Returns the current execution stack as list of classes.
   * </p>
   * <p>
   * The length of the list is the number of methods on the execution
   * stack. The element at index <code>0</code> is the class of the
   * currently executing method, the element at index <code>1</code> is
   * the class of that method's caller, and so on.
   * </p>
   * 
   * @param offset
   *          An additional offset of classes to be skipped.
   * @return the execution stack.
   * @see #getCallStack()
   */
  public static final CallStack getCurrentCallStack(final int offset) {
    SecurityManager x;
    x = System.getSecurityManager();
    if (x instanceof SfcSecurityManager) {
      return ((SfcSecurityManager) x).getCallStack(offset + 1);
    }
    return INTERNAL_INSTANCE.getCallStack(offset + 1);
  }
}

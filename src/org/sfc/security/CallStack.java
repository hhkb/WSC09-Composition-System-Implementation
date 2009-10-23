/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.security.CallStack.java
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

import org.sfc.collections.lists.ImmutableArrayList;

/**
 * The class call stack which can be obtained from the security manager.
 * 
 * @author Thomas Weise
 */
public final class CallStack extends ImmutableArrayList<Class<?>> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Create a call stack.
   * 
   * @param initialData
   *          The call stack data.
   * @param count
   *          The count of classes in the call stack.
   */
  CallStack(final Class<?>[] initialData, final int count) {
    super(initialData, count);
  }
}

/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.lists.EmptyList.java
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

package org.sfc.collections.lists;

/**
 * The empty list implementation. This class provides an immutable, empty
 * list.
 * 
 * @author Thomas Weise
 */
public class EmptyList extends ListBase<Object> {
  /**
   * The serial version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The globally shared singleton instance of the empty list.
   */
  public static final ListBase<Object> INSTANCE = new EmptyList();

  /**
   * No one can call this method, use the globally shared singleton
   * instance instead.
   */
  private EmptyList() {
    super();
  }

  /**
   * This method does nothing.
   */
  @Override
  public void clearEmptyFields() {
    //
  }

  /**
   * This method does nothing.
   * 
   * @param minCapacity
   *          ignored
   */
  @Override
  public void ensureCapacity(int minCapacity) {
    //
  }

  /**
   * This method does nothing, since the empty list is empty, allways an
   * exception will be thrown.
   * 
   * @param index
   *          useless.
   * @return never
   * @throws IndexOutOfBoundsException
   *           always
   */
  @Override
  public Object get(int index) {
    throw new IndexOutOfBoundsException();
  }

  /**
   * Obtain the size of the empty list, which is always 0.
   * 
   * @return always 0.
   */
  @Override
  public int size() {
    return 0;
  }

  /**
   * Perform a write replace of this object.
   * 
   * @return The object to store.
   */
  private final Object writeReplace() {
    return INSTANCE;
  }

  /**
   * Deserialize to the proper instance.
   * 
   * @return The globally shared instance.
   */
  private final Object readResolve() {
    return INSTANCE;
  }
}

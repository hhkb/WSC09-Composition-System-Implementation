/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.lists.ImmutableArrayList.java
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

import java.util.Collection;
import java.util.Comparator;

/**
 * A list that can not be altered in any way and just represents a list
 * interface to an array,
 * 
 * @param <T>
 *          The type of the list items.
 * @author Thomas Weise
 */
public class ImmutableArrayList<T> extends DefaultList<T> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Create a new immutable linear list accessing an array of data
   * directly.
   * 
   * @param initialData
   *          The data for the list.
   * @param <T2>
   *          The type of the initial data.
   */
  public <T2 extends T> ImmutableArrayList(final T2[] initialData) {
    super(initialData, true);
  }

  /**
   * Create a new immutable linear list accessing an array of data
   * directly.
   * 
   * @param initialData
   *          The data for the list.
   * @param count
   *          The count of valid array items.
   * @param <T2>
   *          The type of the initial data.
   */
  public <T2 extends T> ImmutableArrayList(final T2[] initialData, final int count) {
    super(initialData, true, count);
  }

  // Modification Operations

  /**
   * not available for immutable lists.
   * 
   * @param o
   *          Useless.
   * @return Never.
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final boolean add(T o) {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param o
   *          Useless.
   * @return Never
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final boolean remove(Object o) {
    throw new UnsupportedOperationException();
  }

  // Bulk Modification Operations

  /**
   * not available for immutable lists.
   * 
   * @param c
   *          Useless.
   * @return Never
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final boolean addAll(Collection<? extends T> c) {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param index
   *          Useless.
   * @param c
   *          Useless.
   * @return Never
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final boolean addAll(int index, Collection<? extends T> c) {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param c
   *          Useless.
   * @return Never
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param c
   *          Useless.
   * @return Never
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final void clear() {
    throw new UnsupportedOperationException();
  }

  // Positional Access Operations

  /**
   * not available for immutable lists.
   * 
   * @param index
   *          Useless.
   * @param element
   *          Useless
   * @return Never
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final T set(int index, T element)

  {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param index
   *          Useless.
   * @param element
   *          Useless
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final void add(int index, T element) {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param index
   *          Useless.
   * @return Never
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final T remove(int index) {
    throw new UnsupportedOperationException();
  }

  // Search Operations

  /**
   * not available for immutable lists.
   * 
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final void sort() {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param comparator
   *          Useless.
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final void sort(Comparator<T> comparator) {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param c
   *          Useless.
   * @return never
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final boolean removeAllFast(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param index
   *          Useless.
   * @return never
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final T removeFast(int index) {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param o
   *          Useless.
   * @return never
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final boolean removeFast(Object o) {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param c
   *          Useless.
   * @return never
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final boolean retainAllFast(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param item
   *          Useless.
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final void push(T item) {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @return never
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final T pop() {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param fromIndex
   *          useless
   * @param toIndex
   *          useless
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final void removeRange(int fromIndex, int toIndex) {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param array
   *          useless
   * @param index
   *          useless
   * @param count
   *          useless
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final void addArray(T[] array, int index, int count) {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param minCapacity
   *          Useless.
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final void ensureCapacity(int minCapacity) {
    throw new UnsupportedOperationException();
  }

  /**
   * not available for immutable lists.
   * 
   * @param o
   *          Useless.
   * @return never.
   * @throws UnsupportedOperationException
   *           always.
   */
  @Override
  public final boolean removeCompletely(Object o) {
    throw new UnsupportedOperationException();
  }
}

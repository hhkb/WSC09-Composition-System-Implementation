/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.lists.SubList.java
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

import java.util.List;

/**
 * This class allows to create a view on a list.
 * 
 * @param <T>
 *          The type of the elements in the list.
 * @author Thomas Weise
 */
public class SubList<T> extends ListBase<T> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The internal list.
   */
  private List<T> m_list;

  /**
   * The internal offset.
   */
  private int m_offset;

  /**
   * The count of items in the array.
   */
  private int m_count;

  /**
   * Create a new sub list.
   * 
   * @param list
   *          The backing list.
   * @param offset
   *          The offset into the backing list.
   * @param size
   *          The size of the backing list.
   */
  @SuppressWarnings("unchecked")
  public SubList(final List<T> list, final int offset, final int size) {
    super();

    int s, o;
    SubList<T> l_sl;

    if (offset < 0) {
      s = (size + offset);
      o = 0;
    } else {
      s = size;
      o = offset;
    }

    if (list.getClass() == SubList.class) {
      l_sl = (SubList<T>) (list);
      this.m_list = l_sl.m_list;
      o += l_sl.m_offset;
    } else {
      this.m_list = list;
    }

    this.m_offset = o;
    this.m_count = ((s > 0) ? s : 0);
  }

  /**
   * Returns the number of elements in this list. If this list contains
   * more than <tt>Integer.MAX_VALUE</tt> elements, returns
   * <tt>Integer.MAX_VALUE</tt>.
   * 
   * @return the number of elements in this list.
   */
  @Override
  public int size() {
    return this.m_count;
  }

  /**
   * Returns <tt>true</tt> if this list contains no elements.
   * 
   * @return <tt>true</tt> if this list contains no elements.
   */
  @Override
  public boolean isEmpty() {
    return (this.m_count > 0);
  }

  /**
   * Returns the element at the specified position in this list.
   * 
   * @param index
   *          index of element to return.
   * @return the element at the specified position in this list.
   * @throws IndexOutOfBoundsException
   *           if the index is out of range (index &lt; 0 || index &gt;=
   *           size()).
   */
  @Override
  public T get(final int index) {
    if ((index < 0) || (index >= this.m_count)) {
      throw new IndexOutOfBoundsException();
    }

    return this.m_list.get(this.m_offset + index);
  }

  /**
   * Replaces the element at the specified position in this list with the
   * specified element (optional operation).
   * <p>
   * This implementation always throws an
   * <tt>UnsupportedOperationException</tt>.
   * 
   * @param index
   *          index of element to replace.
   * @param element
   *          element to be stored at the specified position.
   * @return the element previously at the specified position.
   * @throws UnsupportedOperationException
   *           if the <tt>set</tt> method is not supported by this List.
   * @throws ClassCastException
   *           if the class of the specified element prevents it from being
   *           added to this list.
   * @throws IllegalArgumentException
   *           if some aspect of the specified element prevents it from
   *           being added to this list.
   * @throws IndexOutOfBoundsException
   *           if the specified index is out of range (<tt>index &lt; 0 || index &gt;= size()</tt>).
   */
  @Override
  public T set(final int index, final T element) {
    if ((index < 0) || (index >= this.m_count)) {
      throw new IndexOutOfBoundsException();
    }

    return this.m_list.set(index + this.m_offset, element);
  }

  /**
   * Inserts the specified element at the specified position in this list
   * (optional operation). Shifts the element currently at that position
   * (if any) and any subsequent elements to the right (adds one to their
   * indices).
   * <p>
   * This implementation always throws an UnsupportedOperationException.
   * 
   * @param index
   *          index at which the specified element is to be inserted.
   * @param element
   *          element to be inserted.
   * @throws UnsupportedOperationException
   *           if the <tt>add</tt> method is not supported by this list.
   * @throws ClassCastException
   *           if the class of the specified element prevents it from being
   *           added to this list.
   * @throws IllegalArgumentException
   *           if some aspect of the specified element prevents it from
   *           being added to this list.
   * @throws IndexOutOfBoundsException
   *           index is out of range (<tt>index &lt;
   *      0 || index &gt; size()</tt>).
   */
  @Override
  public void add(final int index, final T element) {
    int s, i;

    s = this.m_count;

    if (index < 0)
      i = 0;
    else if (index > s)
      i = s;
    else
      i = index;

    this.m_list.add(i + this.m_offset, element);
    this.m_count = (s + 1);
  }

  /**
   * Removes the element at the specified position in this list (optional
   * operation). Shifts any subsequent elements to the left (subtracts one
   * from their indices). Returns the element that was removed from the
   * list.
   * <p>
   * This implementation always throws an
   * <tt>UnsupportedOperationException</tt>.
   * 
   * @param index
   *          the index of the element to remove.
   * @return the element previously at the specified position.
   * @throws UnsupportedOperationException
   *           if the <tt>remove</tt> method is not supported by this
   *           list.
   * @throws IndexOutOfBoundsException
   *           if the specified index is out of range (<tt>index &lt; 0 || index &gt;= size()</tt>).
   */
  @Override
  public T remove(final int index) {
    int s;
    T t;

    s = this.m_count;
    if ((index < 0) || (index >= s)) {
      throw new IndexOutOfBoundsException();
    }

    t = this.m_list.remove(index + this.m_offset);
    this.m_count = (s - 1);

    return t;
  }

  /**
   * This method overwrites internal storage fields that are currently not
   * used with <code>null</code>. This helps the garbage collector to
   * work efficient, but is skipped in normal list operations to increase
   * speed. This method has no effect on this list's data.
   */
  @Override
  public void clearEmptyFields() {
    if (this.m_list instanceof ListBase) {
      ((ListBase<?>) (this.m_list)).clearEmptyFields();
    }
  }

  /**
   * Increases the capacity of this <tt>FastList</tt> instance, if
   * necessary, to ensure that it can hold at least the number of elements
   * specified by the minimum capacity argument.
   * 
   * @param minCapacity
   *          the desired minimum capacity.
   */
  @Override
  public void ensureCapacity(final int minCapacity) {
    if (this.m_list instanceof ListBase) {
      ((ListBase<?>) (this.m_list)).ensureCapacity(minCapacity);
    }
  }
}

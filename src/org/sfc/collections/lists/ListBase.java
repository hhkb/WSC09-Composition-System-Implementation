/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.lists.ListBase.java
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

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

import org.sfc.utils.ICloneable;

/**
 * This is the abstract base class for all sfc lists.
 * 
 * @param <T>
 *          The type of elements stored in this list.
 * @author Thomas Weise
 */
public abstract class ListBase<T> extends AbstractList<T> implements
    ICloneable, Serializable, RandomAccess {

  /**
   * Returns an iterator over a set of elements of type T2.
   * 
   * @return an Iterator.
   */
  @Override
  public Iterator<T> iterator() {
    return this.listIterator(0);
  }

  /**
   * Returns a list iterator of the elements in this list (in proper
   * sequence).
   * 
   * @return a list iterator of the elements in this list (in proper
   *         sequence).
   */
  @Override
  public java.util.ListIterator<T> listIterator() {
    return this.listIterator(0);
  }

  /**
   * Returns a list iterator of the elements in this list (in proper
   * sequence), starting at the specified position in the list. The
   * specified index indicates the first element that would be returned by
   * an initial call to the <tt>next</tt> method. An initial call to the
   * <tt>previous</tt> method would return the element with the specified
   * index minus one.
   * <p>
   * This implementation returns a straightforward implementation of the
   * <tt>ListIterator</tt> interface that extends the implementation of
   * the <tt>Iterator</tt> interface returned by the <tt>iterator()</tt>
   * method. The <tt>ListIterator</tt> implementation relies on the
   * backing list's <tt>get(int)</tt>, <tt>set(int, Object)</tt>,
   * <tt>add(int, Object)</tt> and <tt>remove(int)</tt> methods.
   * <p>
   * Note that the list iterator returned by this implementation will throw
   * an <tt>UnsupportedOperationException</tt> in response to its
   * <tt>remove</tt>, <tt>set</tt> and <tt>add</tt> methods unless
   * the list's <tt>remove(int)</tt>, <tt>set(int, Object)</tt>, and
   * <tt>add(int, Object)</tt> methods are overridden.
   * <p>
   * This implementation can be made to throw runtime exceptions in the
   * face of concurrent modification, as described in the specification for
   * the (protected) <tt>modCount</tt> field.
   * 
   * @param index
   *          index of the first element to be returned from the list
   *          iterator (by a call to the <tt>next</tt> method).
   * @return a list iterator of the elements in this list (in proper
   *         sequence), starting at the specified position in the list.
   * @throws IndexOutOfBoundsException
   *           if the specified index is out of range (<tt>index &lt; 0 || index &gt; size()</tt>).
   * @see #modCount
   */
  @Override
  public java.util.ListIterator<T> listIterator(final int index) {
    return new org.sfc.collections.iterators.ListIterator<T>(this, index);
  }

  /**
   * Create a perfect shallow copy of this list.
   * 
   * @return a clone of this instance.
   * @see java.lang.Cloneable
   */
  @Override
  public Object clone() {
    try {
      return super.clone();
    } catch (Throwable t) {
      return this;
    }
  }

  /**
   * Returns a view of the portion of this list between <tt>fromIndex</tt>,
   * inclusive, and <tt>toIndex</tt>, exclusive. (If <tt>fromIndex</tt>
   * and <tt>toIndex</tt> are equal, the returned list is empty.) The
   * returned list is backed by this list, so changes in the returned list
   * are reflected in this list, and vice-versa. The returned list supports
   * all of the optional list operations supported by this list.
   * <p>
   * This method eliminates the need for explicit range operations (of the
   * sort that commonly exist for arrays). Any operation that expects a
   * list can be used as a range operation by operating on a subList view
   * instead of a whole list. For example, the following idiom removes a
   * range of elements from a list:
   * 
   * <pre>
   * list.subList(from, to).clear();
   * </pre>
   * 
   * Similar idioms may be constructed for <tt>indexOf</tt> and
   * <tt>lastIndexOf</tt>, and all of the algorithms in the
   * <tt>Collections</tt> class can be applied to a subList.
   * <p>
   * The semantics of the list returned by this method become undefined if
   * the backing list (i.e., this list) is <i>structurally modified</i> in
   * any way other than via the returned list. (Structural modifications
   * are those that change the size of the list, or otherwise perturb it in
   * such a fashion that iterations in progress may yield incorrect
   * results.)
   * <p>
   * This implementation returns a list that subclasses
   * <tt>AbstractList</tt>. The subclass stores, in private fields, the
   * offset of the subList within the backing list, the size of the subList
   * (which can change over its lifetime), and the expected
   * <tt>modCount</tt> value of the backing list. There are two variants
   * of the subclass, one of which implements <tt>RandomAccess</tt>. If
   * this list implements <tt>RandomAccess</tt> the returned list will be
   * an instance of the subclass that implements <tt>RandomAccess</tt>.
   * <p>
   * The subclass's <tt>set(int, Object)</tt>, <tt>get(int)</tt>,
   * <tt>add(int, Object)</tt>, <tt>remove(int)</tt>, <tt>addAll(int,
   * Collection)</tt>
   * and <tt>removeRange(int, int)</tt> methods all delegate to the
   * corresponding methods on the backing abstract list, after
   * bounds-checking the index and adjusting for the offset. The
   * <tt>addAll(Collection c)</tt> method merely returns <tt>addAll(size,
   * c)</tt>.
   * <p>
   * The <tt>listIterator(int)</tt> method returns a "wrapper object"
   * over a list iterator on the backing list, which is created with the
   * corresponding method on the backing list. The <tt>iterator</tt>
   * method merely returns <tt>listIterator()</tt>, and the
   * <tt>size</tt> method merely returns the subclass's <tt>size</tt>
   * field.
   * <p>
   * All methods first check to see if the actual <tt>modCount</tt> of
   * the backing list is equal to its expected value, and throw a
   * <tt>ConcurrentModificationException</tt> if it is not.
   * 
   * @param fromIndex
   *          low endpoint (inclusive) of the subList.
   * @param toIndex
   *          high endpoint (exclusive) of the subList.
   * @return a view of the specified range within this list.
   * @throws IndexOutOfBoundsException
   *           endpoint index value out of range
   *           <tt>(fromIndex &lt; 0 || toIndex &gt; size)</tt>
   * @throws IllegalArgumentException
   *           endpoint indices out of order
   *           <tt>(fromIndex &gt; toIndex)</tt>
   */
  @Override
  public List<T> subList(final int fromIndex, final int toIndex) {
    return new SubList<T>(this, fromIndex, toIndex - fromIndex);
  }

  /**
   * Push an item into this list.
   * 
   * @param item
   *          The item to be pushed.
   */
  public void push(final T item) {
    this.add(item);
  }

  /**
   * Pop an item from this list.
   * 
   * @return The item popped, or <code>null</code> if the list was empty.
   */
  public T pop() {
    int i;

    i = this.size();
    if (i > 0) {
      return this.remove(i - 1);
    }
    return null;
  }

  /**
   * This method overwrites internal storage fields that are currently not
   * used with <code>null</code>. This helps the garbage collector to
   * work efficient, but is skipped in normal list operations to increase
   * speed. This method has no effect on this list's data.
   */
  public abstract void clearEmptyFields();

  /**
   * Removes from this list all of the elements whose index is between
   * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive.
   * Shifts any succeeding elements to the left (reduces their index). This
   * call shortens the ArrayList by <tt>(toIndex - fromIndex)</tt>
   * elements. (If <tt>toIndex==fromIndex</tt>, this operation has no
   * effect.)
   * <p>
   * 
   * @param fromIndex
   *          index of first element to be removed.
   * @param toIndex
   *          index after last element to be removed.
   */
  @Override
  public void removeRange(final int fromIndex, final int toIndex) {
    super.removeRange(fromIndex, toIndex);
  }

  /**
   * Add the elements of an array to this list.
   * 
   * @param array
   *          The array to be added.
   * @param index
   *          The index in <code>array</code> from where to start adding.
   * @param count
   *          The count of elements to add, starting from
   *          <code>index</code>.
   */
  public void addArray(final T[] array, final int index, final int count) {
    int i, j;

    i = index;
    for (j = count; j > 0; j--, i++) {
      this.add(array[i]);
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
  public abstract void ensureCapacity(final int minCapacity);

  /**
   * <p>
   * Remove all occurences of the object <code>o</code> from this list.
   * </p>
   * <p>
   * Warning: This operation does not preserve the order of the elements in
   * this list.
   * </p>
   * 
   * @param o
   *          The item to be removed.
   * @return <code>true</code> if <code>o</code> could be removed at
   *         least once.
   */
  public boolean removeCompletely(final Object o) {
    boolean b;

    b = this.remove(o);
    if (b) {
      while (this.remove(o)) { /* */
      }
    }

    return b;
  }

  /**
   * Sort this list.
   */
  public void sort() {
    sort(this);
  }

  /**
   * <p>
   * Removes the element at the specified position in this list (optional
   * operation). Shifts any subsequent elements to the left (subtracts one
   * from their indices). Returns the element that was removed from the
   * list.
   * </p>
   * <p>
   * This is the fast version of the remove method, it does not preserve
   * the order of the elements in the list.
   * </p>
   * 
   * @param index
   *          the index of the element to removed.
   * @return the element previously at the specified position.
   */
  public T removeFast(final int index) {
    return this.remove(index);
  }

  /**
   * <p>
   * Removes the first occurrence in this list of the specified element
   * (optional operation). If this list does not contain the element, it is
   * unchanged. More formally, removes the element with the lowest index i
   * such that <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt> (if
   * such an element exists).
   * </p>
   * <p>
   * This is the fast version of the remove method, it does not preserve
   * the order of the elements in the list.
   * </p>
   * 
   * @param o
   *          element to be removed from this list, if present.
   * @return <tt>true</tt> if this list contained the specified element.
   * @throws ClassCastException
   *           if the type of the specified element is incompatible with
   *           this list (optional).
   * @see #remove(Object)
   */
  public boolean removeFast(final Object o) {
    return this.remove(o);
  }

  /**
   * <p>
   * Removes from this list all the elements that are contained in the
   * specified collection (optional operation).
   * </p>
   * <p>
   * This is the fact version of removeAll, it does not preserve the
   * ordering of the elements.
   * </p>
   * 
   * @param c
   *          collection that defines which elements will be removed from
   *          this list.
   * @return <tt>true</tt> if this list changed as a result of the call.
   * @throws ClassCastException
   *           if the types of one or more elements in this list are
   *           incompatible with the specified collection (optional).
   * @see #removeAll(Collection)
   * @see #remove(Object)
   * @see #contains(Object)
   */
  public boolean removeAllFast(final Collection<?> c) {
    return this.removeAll(c);
  }

  /**
   * <p>
   * Retains only the elements in this list that are contained in the
   * specified collection (optional operation). In other words, removes
   * from this list all the elements that are not contained in the
   * specified collection.
   * </p>
   * <p>
   * This is the fast version of the retainAll method, it does not preserve
   * the order of the elements.
   * </p>
   * 
   * @param c
   *          collection that defines which elements this set will retain.
   * @return <tt>true</tt> if this list changed as a result of the call.
   * @throws ClassCastException
   *           if the types of one or more elements in this list are
   *           incompatible with the specified collection (optional).
   * @see #remove(Object)
   * @see #contains(Object)
   */
  public boolean retainAllFast(final Collection<?> c) {
    return this.retainAll(c);
  }

  /**
   * Internal sort wrapper.
   * 
   * @param list
   *          The list to be sorted.
   * @param <T2>
   *          A dummy parameter.
   */
  @SuppressWarnings("unchecked")
  private static final <T2 extends Comparable<? super T2>> void sort(
      final List<?> list) {
    List<T2> l;

    l = (List<T2>) (list);
    java.util.Collections.sort(l);
  }

  /**
   * Sort this list using the specified comparator.
   * 
   * @param comparator
   *          The comparator to be used when sorting this list.
   */
  public void sort(final Comparator<T> comparator) {
    java.util.Collections.sort(this, comparator);
  }
}

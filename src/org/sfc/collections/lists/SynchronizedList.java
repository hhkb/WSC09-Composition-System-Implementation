/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.lists.SynchronizedList.java
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
 * This list provides the same operations as <code>DefaultList</code> but
 * in a synchronized manner.
 * 
 * @param <T>
 *          The type of the list items.
 * @author Thomas Weise
 */
public class SynchronizedList<T> extends DefaultList<T> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Create a new linear list.
   * 
   * @param initialCapacity
   *          The initial capacity of the new list.
   */
  public SynchronizedList(final int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * Create a new linear list.
   * 
   * @param initialData
   *          The initial data for the list.
   * @param useDirectly
   *          <code>true</code> if this list should work directly on the
   *          array provided (<code>initialData</code>),
   *          <code>false</code> if it should work on a copy of
   *          <code>initialData</code>.
   * @param <T2>
   *          The type of the initial data.
   */
  public <T2 extends T> SynchronizedList(final T2[] initialData,
      final boolean useDirectly) {
    super(initialData, useDirectly);
  }

  /**
   * Returns <tt>true</tt> if this list contains the specified element.
   * More formally, returns <tt>true</tt> if and only if this list
   * contains at least one element <tt>e</tt> such that
   * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
   * 
   * @param o
   *          element whose presence in this list is to be tested.
   * @return <tt>true</tt> if this list contains the specified element.
   * @throws ClassCastException
   *           if the type of the specified element is incompatible with
   *           this list (optional).
   * @throws NullPointerException
   *           if the specified element is null and this list does not
   *           support null elements (optional).
   */
  @Override
  public synchronized boolean contains(final Object o) {
    return super.contains(o);
  }

  /**
   * Returns an array containing all of the elements in this list in proper
   * sequence. Obeys the general contract of the
   * <tt>Collection.toArray</tt> method.
   * 
   * @return an array containing all of the elements in this list in proper
   *         sequence.
   * @see java.util.Arrays#asList(Object[])
   */
  @Override
  public synchronized Object[] toArray() {
    return super.toArray();
  }

  /**
   * Returns an array containing all of the elements in this list in proper
   * sequence; the runtime type of the returned array is that of the
   * specified array. Obeys the general contract of the
   * <tt>Collection.toArray(Object[])</tt> method.
   * 
   * @param a
   *          the array into which the elements of this list are to be
   *          stored, if it is big enough; otherwise, a new array of the
   *          same runtime type is allocated for this purpose.
   * @param <T2>
   *          The type of the array's items-
   * @return an array containing the elements of this list.
   * @throws ArrayStoreException
   *           if the runtime type of the specified array is not a
   *           supertype of the runtime type of every element in this list.
   * @throws NullPointerException
   *           if the specified array is <tt>null</tt>.
   */
  @Override
  public synchronized <T2> T2[] toArray(final T2[] a) {
    return super.toArray(a);
  }

  // Modification Operations

  /**
   * Appends the specified element to the end of this list (optional
   * operation).
   * <p>
   * Lists that support this operation may place limitations on what
   * elements may be added to this list. In particular, some lists will
   * refuse to add null elements, and others will impose restrictions on
   * the type of elements that may be added. List classes should clearly
   * specify in their documentation any restrictions on what elements may
   * be added.
   * 
   * @param o
   *          element to be appended to this list.
   * @return <tt>true</tt> (as per the general contract of the
   *         <tt>Collection.add</tt> method).
   * @throws ClassCastException
   *           if the class of the specified element prevents it from being
   *           added to this list.
   * @throws IllegalArgumentException
   *           if some aspect of this element prevents it from being added
   *           to this list.
   */
  @Override
  public synchronized boolean add(final T o) {
    return super.add(o);
  }

  /**
   * Removes the first occurrence in this list of the specified element
   * (optional operation). If this list does not contain the element, it is
   * unchanged. More formally, removes the element with the lowest index i
   * such that <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt> (if
   * such an element exists).
   * 
   * @param o
   *          element to be removed from this list, if present.
   * @return <tt>true</tt> if this list contained the specified element.
   * @throws ClassCastException
   *           if the type of the specified element is incompatible with
   *           this list (optional).
   */
  @Override
  public synchronized boolean remove(final Object o) {
    return super.remove(o);
  }

  // Bulk Modification Operations

  /**
   * Returns <tt>true</tt> if this list contains all of the elements of
   * the specified collection.
   * 
   * @param c
   *          collection to be checked for containment in this list.
   * @return <tt>true</tt> if this list contains all of the elements of
   *         the specified collection.
   * @throws ClassCastException
   *           if the types of one or more elements in the specified
   *           collection are incompatible with this list (optional).
   * @throws NullPointerException
   *           if the specified collection contains one or more null
   *           elements and this list does not support null elements
   *           (optional).
   * @throws NullPointerException
   *           if the specified collection is <tt>null</tt>.
   * @see #contains(Object)
   */
  @Override
  public synchronized boolean containsAll(final Collection<?> c) {
    return super.containsAll(c);
  }

  /**
   * Appends all of the elements in the specified collection to the end of
   * this list, in the order that they are returned by the specified
   * collection's iterator (optional operation). The behavior of this
   * operation is unspecified if the specified collection is modified while
   * the operation is in progress. (Note that this will occur if the
   * specified collection is this list, and it's nonempty.)
   * 
   * @param c
   *          collection whose elements are to be added to this list.
   * @return <tt>true</tt> if this list changed as a result of the call.
   * @throws ClassCastException
   *           if the class of an element in the specified collection
   *           prevents it from being added to this list.
   * @throws IllegalArgumentException
   *           if some aspect of an element in the specified collection
   *           prevents it from being added to this list.
   * @see #add(Object)
   */
  @Override
  public synchronized boolean addAll(Collection<? extends T> c) {
    return super.addAll(c);
  }

  /**
   * Inserts all of the elements in the specified collection into this list
   * at the specified position (optional operation). Shifts the element
   * currently at that position (if any) and any subsequent elements to the
   * right (increases their indices). The new elements will appear in this
   * list in the order that they are returned by the specified collection's
   * iterator. The behavior of this operation is unspecified if the
   * specified collection is modified while the operation is in progress.
   * (Note that this will occur if the specified collection is this list,
   * and it's nonempty.)
   * 
   * @param index
   *          index at which to insert first element from the specified
   *          collection.
   * @param c
   *          elements to be inserted into this list.
   * @return <tt>true</tt> if this list changed as a result of the call.
   * @throws ClassCastException
   *           if the class of one of elements of the specified collection
   *           prevents it from being added to this list.
   * @throws IllegalArgumentException
   *           if some aspect of one of elements of the specified
   *           collection prevents it from being added to this list.
   * @throws IndexOutOfBoundsException
   *           if the index is out of range (index &lt; 0 || index &gt;
   *           size()).
   */
  @Override
  public synchronized boolean addAll(final int index,
      final Collection<? extends T> c) {
    return super.addAll(index, c);
  }

  /**
   * Removes from this list all the elements that are contained in the
   * specified collection (optional operation).
   * 
   * @param c
   *          collection that defines which elements will be removed from
   *          this list.
   * @return <tt>true</tt> if this list changed as a result of the call.
   * @throws ClassCastException
   *           if the types of one or more elements in this list are
   *           incompatible with the specified collection (optional).
   * @see #remove(Object)
   * @see #contains(Object)
   */
  @Override
  public synchronized boolean removeAll(Collection<?> c) {
    return super.removeAll(c);
  }

  /**
   * Retains only the elements in this list that are contained in the
   * specified collection (optional operation). In other words, removes
   * from this list all the elements that are not contained in the
   * specified collection.
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
  @Override
  public synchronized boolean retainAll(Collection<?> c) {
    return super.retainAll(c);
  }

  /**
   * Removes all of the elements from this list (optional operation). This
   * list will be empty after this call returns (unless it throws an
   * exception).
   */
  @Override
  public synchronized void clear() {
    super.clear();
  }

  // Positional Access Operations

  /**
   * Replaces the element at the specified position in this list with the
   * specified element (optional operation).
   * 
   * @param index
   *          index of element to replace.
   * @param element
   *          element to be stored at the specified position.
   * @return the element previously at the specified position.
   * @throws ClassCastException
   *           if the class of the specified element prevents it from being
   *           added to this list.
   * @throws IllegalArgumentException
   *           if some aspect of the specified element prevents it from
   *           being added to this list.
   * @throws IndexOutOfBoundsException
   *           if the index is out of range (index &lt; 0 || index &gt;=
   *           size()).
   */
  @Override
  public synchronized T set(final int index, final T element)

  {
    return super.set(index, element);
  }

  /**
   * Inserts the specified element at the specified position in this list
   * (optional operation). This operation alters the order of the elements
   * in the list.
   * 
   * @param index
   *          index at which the specified element is to be inserted.
   * @param element
   *          element to be inserted.
   * @throws ClassCastException
   *           if the class of the specified element prevents it from being
   *           added to this list.
   * @throws NullPointerException
   *           if the specified element is null and this list does not
   *           support null elements.
   * @throws IllegalArgumentException
   *           if some aspect of the specified element prevents it from
   *           being added to this list.
   * @throws IndexOutOfBoundsException
   *           if the index is out of range (index &lt; 0 || index &gt;
   *           size()).
   */
  @Override
  public synchronized void add(final int index, final T element) {
    super.add(index, element);
  }

  /**
   * Removes the element at the specified position in this list (optional
   * operation). Shifts any subsequent elements to the left (subtracts one
   * from their indices). Returns the element that was removed from the
   * list.
   * 
   * @param index
   *          the index of the element to removed.
   * @return the element previously at the specified position.
   */
  @Override
  public synchronized T remove(final int index) {
    return super.remove(index);
  }

  // Search Operations

  /**
   * Returns the index in this list of the first occurrence of the
   * specified element, or -1 if this list does not contain this element.
   * More formally, returns the lowest index <tt>i</tt> such that
   * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>, or -1 if
   * there is no such index.
   * 
   * @param o
   *          element to search for.
   * @return the index in this list of the first occurrence of the
   *         specified element, or -1 if this list does not contain this
   *         element.
   * @throws ClassCastException
   *           if the type of the specified element is incompatible with
   *           this list (optional).
   * @throws NullPointerException
   *           if the specified element is null and this list does not
   *           support null elements (optional).
   */
  @Override
  public synchronized int indexOf(final Object o) {
    return super.indexOf(o);
  }

  /**
   * Returns the index in this list of the last occurrence of the specified
   * element, or -1 if this list does not contain this element. More
   * formally, returns the highest index <tt>i</tt> such that
   * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>, or -1 if
   * there is no such index.
   * 
   * @param o
   *          element to search for.
   * @return the index in this list of the last occurrence of the specified
   *         element, or -1 if this list does not contain this element.
   * @throws ClassCastException
   *           if the type of the specified element is incompatible with
   *           this list (optional).
   * @throws NullPointerException
   *           if the specified element is null and this list does not
   *           support null elements (optional).
   */
  @Override
  public synchronized int lastIndexOf(final Object o) {
    return super.lastIndexOf(o);
  }

  /**
   * Sort this list.
   */

  @Override
  public synchronized void sort() {
    super.sort();
  }

  /**
   * Sort this list using the specified comparator.
   * 
   * @param comparator
   *          The comparator to be used when sorting this list.
   */
  @Override
  public synchronized void sort(final Comparator<T> comparator) {
    super.sort(comparator);
  }

  /**
   * Creates and returns a copy of this object. The precise meaning of
   * "copy" may depend on the class of the object. The general intent is
   * that, for any object <code>x</code>, the expression:
   * 
   * <pre>
   * x.clone() != x
   * </pre>
   * 
   * will be true, and that the expression:
   * 
   * <pre>
   * x.clone().getClass() == x.getClass()
   * </pre>
   * 
   * will be <code>true</code>, but these are not absolute requirements.
   * While it is typically the case that:
   * 
   * <pre>
   * x.clone().equals(x)
   * </pre>
   * 
   * </blockquote> will be <code>true</code>, this is not an absolute
   * requirement.
   * <p>
   * By convention, the returned object should be obtained by calling
   * <code>super.clone</code>. If a class and all of its superclasses
   * (except <code>Object</code>) obey this convention, it will be the
   * case that <code>x.clone().getClass() == x.getClass()</code>.
   * </p>
   * <p>
   * By convention, the object returned by this method should be
   * independent of this object (which is being cloned). To achieve this
   * independence, it may be necessary to modify one or more fields of the
   * object returned by <code>super.clone</code> before returning it.
   * Typically, this means copying any mutable objects that comprise the
   * internal "deep structure" of the object being cloned and replacing the
   * references to these objects with references to the copies. If a class
   * contains only primitive fields or references to immutable objects,
   * then it is usually the case that no fields in the object returned by
   * <code>super.clone</code> need to be modified.
   * </p>
   * 
   * @return a clone of this instance.
   * @see java.lang.Cloneable
   */
  @Override
  public synchronized Object clone() {
    return super.clone();
  }

  /**
   * Returns the hash code value for this list.
   * <p>
   * This implementation uses exactly the code that is used to define the
   * list hash function in the documentation for the <tt>List.hashCode</tt>
   * method.
   * 
   * @return the hash code value for this list.
   */
  @Override
  public synchronized int hashCode() {
    return super.hashCode();
  }

  /**
   * Compares the specified object with this list for equality. Returns
   * <tt>true</tt> if and only if the specified object is also a list,
   * both lists have the same size, and all corresponding pairs of elements
   * in the two lists are <i>equal</i>. (Two elements <tt>e1</tt> and
   * <tt>e2</tt> are <i>equal</i> if <tt>(e1==null ? e2==null :
   * e1.equals(e2))</tt>.)
   * In other words, two lists are defined to be equal if they contain the
   * same elements in the same order.
   * <p>
   * This implementation first checks if the specified object is this list.
   * If so, it returns <tt>true</tt>; if not, it checks if the specified
   * object is a list. If not, it returns <tt>false</tt>; if so, it
   * iterates over both lists, comparing corresponding pairs of elements.
   * If any comparison returns <tt>false</tt>, this method returns
   * <tt>false</tt>. If either iterator runs out of elements before the
   * other it returns <tt>false</tt> (as the lists are of unequal
   * length); otherwise it returns <tt>true</tt> when the iterations
   * complete.
   * 
   * @param o
   *          the object to be compared for equality with this list.
   * @return <tt>true</tt> if the specified object is equal to this list.
   */
  @Override
  public synchronized boolean equals(final Object o) {
    return super.equals(o);
  }

  /**
   * Returns a string representation of this collection. The string
   * representation consists of a list of the collection's elements in the
   * order they are returned by its iterator, enclosed in square brackets (<tt>"[]"</tt>).
   * Adjacent elements are separated by the characters <tt>", "</tt>
   * (comma and space). Elements are converted to strings as by
   * <tt>String.valueOf(Object)</tt>.
   * <p>
   * This implementation creates an empty string buffer, appends a left
   * square bracket, and iterates over the collection appending the string
   * representation of each element in turn. After appending each element
   * except the last, the string <tt>", "</tt> is appended. Finally a
   * right bracket is appended. A string is obtained from the string
   * buffer, and returned.
   * 
   * @return a string representation of this collection.
   */
  @Override
  public synchronized String toString() {
    return super.toString();
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
  @Override
  public synchronized boolean removeAllFast(Collection<?> c) {
    return super.removeAllFast(c);
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
  @Override
  public synchronized T removeFast(final int index) {
    return super.removeFast(index);
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
  @Override
  public synchronized boolean removeFast(final Object o) {
    return super.removeFast(o);
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
  @Override
  public synchronized boolean retainAllFast(Collection<?> c) {
    return super.retainAllFast(c);
  }

  /**
   * Pop an item from this list.
   * 
   * @return The item popped, or <code>null</code> if the list was empty.
   */
  @Override
  public synchronized T pop() {
    return super.pop();
  }

  /**
   * This method overwrites internal storage fields that are currently not
   * used with <code>null</code>. This helps the garbage collector to
   * work efficient, but is skipped in normal list operations to increase
   * speed. This method has no effect on this list's data.
   */
  @Override
  public synchronized void clearEmptyFields() {
    super.clearEmptyFields();
  }

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
  public synchronized void removeRange(final int fromIndex,
      final int toIndex) {
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
  @Override
  public synchronized void addArray(final T[] array, final int index,
      final int count) {
    super.addArray(array, index, count);
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
  public synchronized void ensureCapacity(final int minCapacity) {
    super.ensureCapacity(minCapacity);
  }

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
  @Override
  public synchronized boolean removeCompletely(final Object o) {
    return super.removeCompletely(o);
  }
}

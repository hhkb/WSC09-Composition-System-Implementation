/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.lists.DefaultList.java
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

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;

/**
 * A fast list implementation based on an internal array storage.
 * 
 * @param <T>
 *          The type of the list items.
 * @author Thomas Weise
 */
public class DefaultList<T> extends ListBase<T> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The internal data array.
   */
  private transient T[] m_data;

  /**
   * The count of valid items in the data array.
   */
  private int m_count;

  /**
   * Create a new linear list.
   */
  public DefaultList() {
    this(-1);
  }

  /**
   * Create a new linear list.
   * 
   * @param initialCapacity
   *          The initial capacity of the new list.
   */
  @SuppressWarnings("unchecked")
  public DefaultList(final int initialCapacity) {
    super();
    this.m_data = ((T[]) (new Object[(initialCapacity > 0) ? initialCapacity
        : 16]));
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
  public <T2 extends T> DefaultList(final T2[] initialData,
      final boolean useDirectly) {
    super();
    this.m_data = (useDirectly ? initialData : initialData.clone());
    this.m_count = initialData.length;
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
   * @param count
   *          The count of valid items inside the initial data array.
   * @param <T2>
   *          The type of the initial data.
   */
  @SuppressWarnings("unchecked")
  public <T2 extends T> DefaultList(final T2[] initialData,
      final boolean useDirectly, final int count) {
    super();

    if (useDirectly) {
      this.m_data = initialData;
    } else {
      this.m_data = ((T2[]) (new Object[count]));
      System.arraycopy(initialData, 0, this.m_data, 0, count);
    }

    this.m_count = count;
  }

  /**
   * Increases the capacity of this <tt>FastList</tt> instance, if
   * necessary, to ensure that it can hold at least the number of elements
   * specified by the minimum capacity argument.
   * 
   * @param minCapacity
   *          the desired minimum capacity.
   */
  @SuppressWarnings("unchecked")
  private final void doEnsureCapacity(final int minCapacity) {
    T[] d;
    int oldCapacity, newCapacity;

    d = this.m_data;
    oldCapacity = d.length;

    if (minCapacity > oldCapacity) {
      newCapacity = (minCapacity << 1);

      d = (T[]) (new Object[newCapacity]);

      System.arraycopy(this.m_data, 0, d, 0, this.m_count);
      this.m_data = d;
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
    this.doEnsureCapacity(minCapacity);
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
    return (this.m_count <= 0);
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
  public boolean contains(final Object o) {
    T[] d;
    int i;

    d = this.m_data;
    if (o == null) {
      for (i = (this.m_count - 1); i >= 0; i--) {
        if (d[i] == null)
          return true;
      }
    } else {
      for (i = (this.m_count - 1); i >= 0; i--) {
        if (o.equals(d[i]))
          return true;
      }
    }

    return false;
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
  public Object[] toArray() {
    Object[] d;
    int c;

    c = this.m_count;
    if (c <= 0)
      return new Object[0];

    d = new Object[c];
    System.arraycopy(this.m_data, 0, d, 0, c);
    return d;
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
  @SuppressWarnings("unchecked")
  public <T2> T2[] toArray(final T2[] a) {
    int c;
    T2[] d;

    c = this.m_count;

    if (a.length < c) {
      d = (T2[]) (java.lang.reflect.Array.newInstance(a.getClass()
          .getComponentType(), c));
    } else {
      d = a;
    }

    System.arraycopy(this.m_data, 0, d, 0, c);

    if (d.length > c)
      d[c] = null;
    return d;
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
  public boolean add(final T o) {
    int c;

    c = this.m_count;

    this.doEnsureCapacity(c + 1);

    this.m_data[c] = o;
    this.m_count = (c + 1);
    return true;
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
   * @see #removeFast(Object)
   */
  @Override
  public boolean remove(final Object o) {
    int i;

    i = this.indexOf(o);
    if (i >= 0) {
      this.remove(i);
      return true;
    }

    return false;
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
  public boolean removeFast(final Object o) {
    int i, c;

    i = this.indexOf(o);
    if (i < 0)
      return false;

    c = (this.m_count - 1);
    this.m_count = c;
    this.m_data[i] = this.m_data[c];

    return true;
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
  public boolean containsAll(final Collection<?> c) {
    for (Object o : c) {
      if (!(this.contains(o)))
        return false;
    }

    return true;
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
  @SuppressWarnings("unchecked")
  public boolean addAll(final Collection<? extends T> c) {
    DefaultList<T> x;
    int c1, c2;
    Object[] e;

    c1 = this.m_count;

    if (c instanceof DefaultList) {
      x = ((DefaultList<T>) c);
    } else
      x = null;

    if (x != null) {
      synchronized (x) {
        c2 = x.m_count;
        if (c2 <= 0)
          return false;

        this.doEnsureCapacity(c1 + c2);

        System.arraycopy(x.m_data, 0, this.m_data, c1, c2);
        this.m_count = (c1 + c2);

        return true;
      }
    }

    e = c.toArray();
    c2 = e.length;

    if (c2 <= 0)
      return false;

    this.doEnsureCapacity(c1 + c2);
    System.arraycopy(e, 0, this.m_data, c1, c2);
    this.m_count = (c1 + c2);

    return true;
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
  @SuppressWarnings("unchecked")
  public boolean addAll(final int index, final Collection<? extends T> c) {
    DefaultList<T> x;
    int c1, c2;
    T[] d;
    Object[] e;

    c1 = this.m_count;

    if (c instanceof DefaultList) {
      x = ((DefaultList<T>) c);
    } else
      x = null;

    if ((x != null) && (x != this)) {

      synchronized (x) {
        c2 = x.m_count;
        if (c2 <= 0)
          return false;

        this.doEnsureCapacity(c1 + c2);
        d = this.m_data;

        System.arraycopy(d, index, d, index + c2, c1 - index);
        System.arraycopy(x.m_data, 0, d, index, c2);
        this.m_count = (c1 + c2);

        return true;
      }
    }

    e = c.toArray();
    c2 = e.length;

    if (c2 <= 0)
      return false;

    this.doEnsureCapacity(c1 + c2);
    d = this.m_data;
    System.arraycopy(d, index, d, index + c2, c1 - index);
    System.arraycopy(e, 0, d, index, c2);
    this.m_count = (c1 + c2);

    return true;
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
   * @see #removeAllFast(Collection)
   */
  @Override
  public boolean removeAll(final Collection<?> c) {
    T[] d;
    int l, i;

    d = this.m_data;
    l = this.m_count;

    for (i = (l - 1); i >= 0; i--) {
      if (c.contains(d[i])) {
        --l;
        System.arraycopy(d, i + 1, d, i, l - i);
      }
    }

    if (l < this.m_count) {
      this.m_count = l;
      return true;
    }

    return false;
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
  public boolean removeAllFast(final Collection<?> c) {
    T[] d;
    int l, i;

    d = this.m_data;
    l = this.m_count;

    for (i = (l - 1); i >= 0; i--) {
      if (c.contains(d[i])) {
        d[i] = d[--l];
      }
    }

    if (l < this.m_count) {
      this.m_count = l;
      return true;
    }

    return false;
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
  public boolean retainAll(final Collection<?> c) {
    T[] d;
    int l, i;

    d = this.m_data;
    l = this.m_count;

    for (i = (l - 1); i >= 0; i--) {
      if (!(c.contains(d[i]))) {
        --l;
        System.arraycopy(d, i + 1, d, i, l - i);
      }
    }

    if (l < this.m_count) {
      this.m_count = l;
      return true;
    }

    return false;
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
  public boolean retainAllFast(final Collection<?> c) {
    T[] d;
    int l, i;

    d = this.m_data;
    l = this.m_count;

    for (i = (l - 1); i >= 0; i--) {
      if (!(c.contains(d[i]))) {
        d[i] = d[--l];
      }
    }

    if (l < this.m_count) {
      this.m_count = l;
      return true;
    }

    return false;
  }

  /**
   * Removes all of the elements from this list (optional operation). This
   * list will be empty after this call returns (unless it throws an
   * exception).
   */
  @Override
  public void clear() {
    this.m_count = 0;
  }

  // Positional Access Operations

  /**
   * Returns the element at the specified position in this list.
   * 
   * @param index
   *          index of element to return.
   * @return the element at the specified position in this list.
   */
  @Override
  public T get(final int index) {
    return this.m_data[index];
  }

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
   */
  @Override
  public T set(final int index, final T element)

  {
    T x;

    x = this.m_data[index];
    this.m_data[index] = element;

    return x;
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
  public void add(final int index, final T element) {
    int c, i;
    T[] d;

    c = this.m_count;
    this.doEnsureCapacity(c + 1);

    d = this.m_data;
    i = ((c > index) ? index : ((c < 0) ? 0 : c));

    System.arraycopy(d, i, d, index + 1, c - i);

    d[i] = element;
    this.m_count = (c + 1);
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
   * @see #removeFast(int)
   */
  @Override
  public T remove(final int index) {
    T[] d;
    int c;
    T l;

    if (index < 0)
      return null;

    c = this.m_count;
    d = this.m_data;

    if (index >= c)
      return null;

    l = d[index];
    this.m_count = (--c);
    System.arraycopy(d, index + 1, d, index, c - index);

    return l;
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
  public T removeFast(final int index) {
    T[] d;
    int c;
    T l;

    if (index < 0)
      return null;

    c = this.m_count;
    d = this.m_data;

    if (index >= c)
      return null;

    l = d[index];
    d[index] = d[--c];
    this.m_count = c;

    return l;
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
  public int indexOf(final Object o) {
    T[] d;
    int i, c;

    d = this.m_data;
    c = this.m_count;

    if (o == null) {
      for (i = 0; i < c; i++) {
        if (d[i] == null)
          return i;
      }
      return -1;
    }

    for (i = 0; i < c; i++) {
      if (o.equals(d[i]))
        return i;
    }
    return -1;
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
  public int lastIndexOf(final Object o) {

    T[] d;
    int c;

    d = this.m_data;
    c = this.m_count;

    if (o == null) {
      for (c = (this.m_count - 1); c >= 0; c--) {
        if (d[c] == null)
          return c;
      }
      return -1;
    }

    for (c = (this.m_count - 1); c >= 0; c--) {
      if (o.equals(d[c]))
        return c;
    }
    return -1;
  }

  /**
   * Sort this list.
   */
  @Override
  public void sort() {
    java.util.Arrays.sort(this.m_data, 0, this.m_count);
  }

  /**
   * Sort this list using the specified comparator.
   * 
   * @param comparator
   *          The comparator to be used when sorting this list.
   */
  @Override
  public void sort(final Comparator<T> comparator) {
    java.util.Arrays.sort(this.m_data, 0, this.m_count, comparator);
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
  @SuppressWarnings("unchecked")
  public Object clone() {
    DefaultList<T> x;
    T[] t;
    int i;

    x = (DefaultList<T>) (super.clone());
    i = x.m_count;

    t = (T[]) (new Object[i]);
    System.arraycopy(x.m_data, 0, t, 0, i);
    x.m_data = t;

    return x;
  }

  /**
   * Pop an item from this list.
   * 
   * @return The item popped, or <code>null</code> if the list was empty.
   */
  @Override
  public T pop() {
    int i;

    i = this.m_count;
    if (i > 0) {
      this.m_count = (--i);
      return this.m_data[i];
    }

    return null;
  }

  /**
   * This method overwrites internal storage fields that are currently not
   * used with <code>null</code>. This helps the garbage collector to
   * work efficient, but is skipped in normal list operations to increase
   * speed. This method has no effect on this list's data.
   */
  @Override
  public void clearEmptyFields() {
    T[] d;
    int i, j;

    d = this.m_data;
    j = this.m_count;

    for (i = (d.length - 1); i >= j; i--) {
      d[i] = null;
    }
  }

  /**
   * Save the state of the <tt>DefaultList</tt> instance to a stream
   * (that is, serialize it).
   * 
   * @param s
   *          The output stream.
   * @serialData The length of the array backing the <tt>ArrayList</tt>
   *             instance is emitted (int), followed by all of its elements
   *             (each an <tt>Object</tt>) in the inverse order.
   * @throws IOException
   *           If io fails.
   */
  private synchronized final void writeObject(java.io.ObjectOutputStream s)
      throws java.io.IOException {
    int i;
    T[] d;
    s.defaultWriteObject();
    d = this.m_data;
    for (i = (this.m_count - 1); i >= 0; i--) {
      s.writeObject(d[i]);
    }
  }

  /**
   * Reconstitute the <tt>DefaultList</tt> instance from a stream (that
   * is, deserialize it).
   * 
   * @param s
   *          The input stream.
   * @throws IOException
   *           If the io fails.
   * @throws ClassNotFoundException
   *           If any required class could not be found.
   */
  @SuppressWarnings("unchecked")
  private final void readObject(java.io.ObjectInputStream s)
      throws java.io.IOException, ClassNotFoundException {
    Object[] d;
    int i;
    s.defaultReadObject();
    i = this.m_count;
    this.m_data = (T[]) (d = new Object[Math.max(2, i)]);

    for (--i; i >= 0; i--) {
      d[i] = s.readObject();
    }
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
  public void removeRange(final int fromIndex, final int toIndex) {
    T[] d;
    int c;

    d = this.m_data;
    c = this.m_count;
    System.arraycopy(d, toIndex, d, fromIndex, c - toIndex);
    this.m_count = (c - toIndex + fromIndex);
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
  public void addArray(final T[] array, final int index, final int count) {
    int c;
    T[] d;

    c = this.m_count;
    this.doEnsureCapacity(c + count);
    d = this.m_data;
    System.arraycopy(array, index, d, c, count);
    this.m_count = (c + count);
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
  public boolean removeCompletely(final Object o) {
    T[] d;
    int l, i;

    d = this.m_data;
    l = this.m_count;

    if (o == null) {
      for (i = (l - 1); i >= 0; i--) {
        if (d[i] == null) {
          d[i] = d[--l];
        }
      }
    } else {
      for (i = (l - 1); i >= 0; i--) {
        if (o.equals(d[i])) {
          d[i] = d[--l];
        }
      }
    }

    if (l < this.m_count) {
      this.m_count = l;
      return true;
    }

    return false;
  }
}

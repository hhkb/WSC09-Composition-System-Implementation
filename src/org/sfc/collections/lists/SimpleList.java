/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-02-06
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.lists.SimpleList.java
 * Last modification: 2007-02-06
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

import java.util.AbstractList;

import org.sfc.collections.CollectionUtils;
import org.sfc.collections.IArrayConstructor;
import org.sfc.utils.ICloneable;

/**
 * <p>
 * This is a good base class if you just need a simple and fast list.
 * </p>
 * <p>
 * If you set {@link IArrayConstructor} field in the constructor properly,
 * you can access the data field from a variable.
 * </p>
 * 
 * @param <T>
 *          The item type.
 * @author Thomas Weise
 */
public class SimpleList<T> extends AbstractList<T> implements ICloneable {
  /**
   * The count of items stored.
   */
  public transient int m_count;

  /**
   * The list data.
   */
  public transient T[] m_data;

  /**
   * the array constructor
   */
  private final IArrayConstructor<T> m_constr;

  /**
   * Create a new simple list.
   */
  public SimpleList() {
    this(-1);
  }

  /**
   * Create a new simple list.
   * 
   * @param copy
   *          the list to copy from
   */
  @SuppressWarnings("unchecked")
  public SimpleList(final SimpleList<T> copy) {
    super();
    int i;
    T[] x;
    this.m_count = i = copy.m_count;
    this.m_constr = copy.m_constr;
    x = this.m_constr.createArray(i);// ((T[]) (new Object[i]));
    System.arraycopy(copy.m_data, 0, x, 0, i);
    this.m_data = x;
  }

  /**
   * Create a new simple list.
   * 
   * @param size
   *          the initial count of slots, use <code>-1</code> for don't
   *          care
   */
  @SuppressWarnings("unchecked")
  public SimpleList(final int size) {
    this(size, null);
  }

  /**
   * Create a new simple list.
   * 
   * @param size
   *          the initial count of slots, use <code>-1</code> for don't
   *          care
   * @param constr
   *          the array constructor
   */
  @SuppressWarnings("unchecked")
  public SimpleList(final int size, final IArrayConstructor<T> constr) {
    super();
    this.m_constr = ((constr == null) ? ((IArrayConstructor<T>) (CollectionUtils.OBJECT_ARRAY_CONSTRUCTOR))
        : constr);
    this.m_data = this.m_constr.createArray((size > 0) ? size : 8);
    // ((T[]) (new Object[(size > 0) ? size : 8]));
  }

  /**
   * Create a new simple list.
   * 
   * @param size
   *          the initial count of slots, use <code>-1</code> for don't
   *          care
   * @param constr
   *          the array constructor
   */
  public SimpleList(final IArrayConstructor<T> constr) {
    this(-1, constr);
  }

  /**
   * Obtain the list's length.
   * 
   * @return The list's length.
   */
  @Override
  public final int size() {
    return this.m_count;
  }

  /**
   * Obtain the item at index <code>index</code>.
   * 
   * @param index
   *          The index we want to get the item at.
   * @return The item at index <code>index</code>.
   */
  @Override
  public final T get(final int index) {
    return this.m_data[index];
  }

  /**
   * Set the item at index <code>index</code>.
   * 
   * @param index
   *          The index we want to get the item at.
   * @param item
   *          The item to be set at index <code>index</code>.
   */
  @Override
  public final T set(final int index, final T item) {
    T x;
    x = this.m_data[index];
    this.m_data[index] = item;
    return x;
  }

  /**
   * Check wether the given item is stored in the list.
   * 
   * @param item
   *          The item to find.
   * @return <code>true</code> if and only if the item is stored in this
   *         list.
   */
  @Override
  public final boolean contains(final Object item) {
    int i;
    T[] d;

    d = this.m_data;

    for (i = (this.m_count - 1); i >= 0; i--) {
      if (item == d[i])
        return true;
    }

    return false;
  }

  /**
   * Remove an element from the list.
   * 
   * @param index
   *          The index in the list.
   * @return The removed element.
   */
  @Override
  public final T remove(final int index) {
    T[] o;
    T v;
    int x;

    o = this.m_data;
    v = o[index];
    x = (this.m_count - 1);
    this.m_count = x;
    System.arraycopy(o, index + 1, o, index, x - index);

    return v;
  }

  /**
   * Remove an element from the list not keeping the list's order. This
   * method is faster than the normal remove.
   * 
   * @param index
   *          The index in the list.
   * @return The removed element.
   */
  public final T removeFast(final int index) {
    T[] o;
    T v;
    int x;

    o = this.m_data;
    v = o[index];
    x = (this.m_count - 1);
    this.m_count = x;
    o[index] = o[x];

    return v;
  }

  /**
   * Remove an element from the list not keeping the list's order. This
   * method is faster than the normal remove.
   * 
   * @param item
   *          the item to be removed
   * @return <code>true</code> if it worked
   */
  public final boolean removeFast(final T item) {
    T[] o;
    int x, index;

    o = this.m_data;
    x = (this.m_count - 1);

    for (index = x; index >= 0; index--) {
      if (o[index] == item) {
        this.m_count = x;
        o[index] = o[x];
        return true;
      }
    }

    return false;
  }

  /**
   * Remove the last service from the simple list.
   * 
   * @return the item removed
   */
  public final T removeLast() {
    return this.m_data[--this.m_count];
  }

  /**
   * Clear the simple list.
   */
  @Override
  public final void clear() {
    this.m_count = 0;
  }

  /**
   * Check an object for equality with this list.
   * 
   * @param o
   *          The object to check.
   * @return <code>true</code> if the object is also a list and has the
   *         same contents as this one.
   */
  @Override
  @SuppressWarnings("unchecked")
  public final boolean equals(final Object o) {
    SimpleList<?> x;
    Object[] q;
    T[] t;
    int c;

    if (o == this)
      return true;

    if (o instanceof SimpleList) {
      x = (SimpleList<T>) (o);
      c = x.m_count;
      if (c == this.m_count) {
        q = x.m_data;
        t = this.m_data;
        for (--c; c >= 0; c--) {
          if (q[c] != t[c])
            return false;
        }
        return true;
      }
    }

    return false;
  }

  /**
   * Add an item to the list.
   * 
   * @param item
   *          the new item
   */
  @SuppressWarnings("unchecked")
  @Override
  public final boolean add(final T item) {
    T[] data;
    int c;

    data = this.m_data;
    c = this.m_count;

    if (c >= data.length) {
      data = this.m_constr.createArray(c << 1);// ((T[]) (new Object[c <<
      // 1]));
      System.arraycopy(this.m_data, 0, data, 0, c);
      this.m_data = data;
    }

    data[c] = item;
    this.m_count = (c + 1);
    return true;
  }

  /**
   * Add an item to the list.
   * 
   * @param item
   *          the new item
   */
  @SuppressWarnings("unchecked")
  public final void addIfNew(final T item) {
    T[] data;
    int c, i;

    data = this.m_data;
    c = this.m_count;

    for (i = (c - 1); i >= 0; i--) {
      if (data[i].equals(item))
        return;
    }

    if (c >= data.length) {
      data = this.m_constr.createArray(c << 1);// ((T[]) (new Object[c <<
      // 1]));
      System.arraycopy(this.m_data, 0, data, 0, c);
      this.m_data = data;
    }

    data[c] = item;
    this.m_count = (c + 1);
  }

  /**
   * Obtain the string representation of this simple list.
   * 
   * @return The string representation of this simple list.
   */
  @Override
  public final String toString() {
    T[] s;
    int c, i;
    StringBuilder b;

    s = this.m_data;
    c = this.m_count;
    b = new StringBuilder(25 * c);
    for (i = 0; i < c; i++) {// --c; c >= 0; c--) {
      if (i > 0)
        b.append(' ');
      b.append(s[i].toString());
    }

    return b.toString();
  }

  /**
   * Add a specified set of items to this list.
   * 
   * @param items
   *          The list of items to be added.
   * @param count
   *          The count of items to add.
   */
  @SuppressWarnings("unchecked")
  public final void addArray(final T[] items, final int count) {
    this.addArray(items, 0, count);
  }

  /**
   * Add a specified set of items to this list.
   * 
   * @param items
   *          The list of items to be added.
   * @param start
   *          the start index
   * @param count
   *          The count of items to add.
   */
  @SuppressWarnings("unchecked")
  public final void addArray(final T[] items, final int start,
      final int count) {
    T[] c;
    int cc, x;

    if (count > 0) {
      c = this.m_data;
      cc = this.m_count;
      x = (cc + count);
      if (c.length < x) {
        c = this.m_constr.createArray(x << 1);// ((T[]) (new Object[x <<
        // 1]));
        System.arraycopy(this.m_data, 0, c, 0, cc);
        this.m_data = c;
      }
      this.m_count = x;

      System.arraycopy(items, start, c, cc, count);
    }
  }

  /**
   * Add a specified set of items to this list.
   * 
   * @param items
   *          The list of items to be added.
   */
  public final void addSimpleList(final SimpleList<T> items) {
    this.addArray(items.m_data, 0, items.m_count);
  }
  

  /**
   * Add a specified set of items to this list.
   * 
   * @param items
   *          The list of items to be added.
   */
  public final void addSimpleListIfNew(final SimpleList<T> items) {
    int i;
    for(i=(items.m_count-1);i>=0;i--){
      this.addIfNew(items.m_data[i]);
    }
  }

  // /**
  // * Stores the <code>SfcEvent</code> into the stream.
  // *
  // * @param s
  // * The output stream.
  // * @throws IOException
  // * If something io-like went wrong.
  // */
  // private final void writeObject(final ObjectOutputStream s)
  // throws IOException {
  // s.defaultWriteObject();
  //
  // int c;
  // T[] b;
  //
  // c = this.m_count;
  // s.writeInt(c);
  //
  // b = this.m_data;
  // for (--c; c >= 0; c--) {
  // s.writeObject(b[c]);
  // }
  // }
  //
  // /**
  // * Reconstitute the <code>SfcEvent</code> instance from a stream (that
  // * is, deserialize it).
  // *
  // * @param s
  // * The input stream.
  // * @throws IOException
  // * If something io-like went wrong.
  // * @throws ClassNotFoundException
  // * If a needed class could not be found.
  // */
  // @SuppressWarnings("unchecked")
  // private final void readObject(final ObjectInputStream s)
  // throws IOException, ClassNotFoundException {
  // int c;
  // Object[] ss;
  //
  // s.defaultReadObject();
  // this.m_count = c = s.readInt();
  // ss = new Object[c];
  //
  // for (--c; c >= 0; c--) {
  // ss[c] = s.readObject();
  // }
  //
  // this.m_data = ((T[]) ss);
  // }

  /**
   * This method deletes all individuals stored but the one refered by
   * <code>index</code>.
   * 
   * @param index
   *          The index of the only individual to retain.
   */
  public final void deleteAllButOne(final int index) {
    this.m_data[0] = this.m_data[index];
    this.m_count = 1;
  }

  /**
   * Copy the contents of the specified list into this one.
   * 
   * @param list
   *          The list to copy from.
   */
  @SuppressWarnings("unchecked")
  public final void assign(final SimpleList<T> list) {
    T[] c;
    int x;

    c = this.m_data;
    x = list.m_count;
    if (c.length < x) {
      this.m_data = c = this.m_constr.createArray(x);// ((T[]) (new
      // Object[x]));
    }
    this.m_count = x;
    System.arraycopy(list.m_data, 0, c, 0, x);
  }

  /**
   * Copy the <code>count</code> first items to the array
   * <code>array</code>.
   * 
   * @param array
   *          the array to copy to
   * @param count
   *          the count of items to be copied
   */
  public final void copyToArray(final Object[] array, final int count) {
    System.arraycopy(this.m_data, 0, array, 0, count);
  }

  /**
   * Clone this list
   */
  @Override
  @SuppressWarnings("unchecked")
  public Object clone() {
    SimpleList<T> t;

    try {
      t = ((SimpleList<T>) (super.clone()));
    } catch (Throwable qq) {
      return null;
    }

    t.m_data = t.m_data.clone();
    return t;
  }

}
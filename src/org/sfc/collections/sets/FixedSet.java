/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-12
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.sets.FixedSet.java
 * Last modification: 2007-11-12
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

package org.sfc.collections.sets;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

import org.sfc.collections.iterators.ArrayIterator;
import org.sfc.text.ITextable;
import org.sfc.utils.Utils;

/**
 * The base class for fixed set.
 * 
 * @param <FSET>
 *          the fixed set element type
 * @author Thomas Weise
 */
public class FixedSet<FSET extends FixedSetElement<?>> extends
    AbstractSet<FSET> implements ITextable, Serializable, Set<FSET> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the id counter
   */
  private int m_count;

  /**
   * the elements
   */
  private FSET[] m_elements;

  /**
   * Create a new element set
   */
  public FixedSet() {
    super();
    this.m_elements = this.createArray(10);
  }

  /**
   * Create the internal array
   * 
   * @param len
   *          the length of the wanted array
   * @return the array
   */
  @SuppressWarnings("unchecked")
  protected FSET[] createArray(final int len) {
    return ((FSET[]) (new FixedSetElement[len]));
  }

  /**
   * Check whether the given element can be registered or not.
   * 
   * @param element
   *          the element to be registered
   * @return <code>true</code> if this is possible, <code>false</code>
   *         otherwise
   */
  protected boolean checkRegister(final FSET element) {
    return (element != null);
  }

  /**
   * Register an element in this element set.
   * 
   * @param element
   *          the element to be registered
   * @return the id for this element (-1 on error)
   */
  @SuppressWarnings("unchecked")
  synchronized final int registerElement(final FSET element) {
    int c;
    FSET[] ac;

    if (this.checkRegister(element)) {

      c = this.m_count;
      ac = this.m_elements;

      if (c >= ac.length) {
        ac = this.createArray(c << 1);
        System.arraycopy(this.m_elements, 0, ac, 0, c);
        this.m_elements = ac;
      }

      ac[c] = element;
      this.m_count = (c + 1);

      return c;
    }

    return -1;
  }

  /**
   * Obtain the number of elements in this element set
   * 
   * @return the number of elements in this element set
   */
  @Override
  public final int size() {
    return this.m_count;
  }

  /**
   * Obtain the element of the given id
   * 
   * @param id
   *          the id of the wanted element
   * @return the element of the given id
   */
  @SuppressWarnings("unchecked")
  public final FSET get(final int id) {
    return this.m_elements[id];
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  public void toStringBuilder(final StringBuilder sb) {
    FixedSetElement<?>[] u;
    int i;

    u = this.m_elements;
    for (i = 0; i < this.m_count; i++) {
      if (i > 0) {
        sb.append(',');
        sb.append(' ');
      }
      u[i].toStringBuilder(sb);
    }
  }

  /**
   * Obtain the string representation of this set
   * 
   * @return the string representation of this set
   */
  @Override
  public final String toString() {
    StringBuilder sb;

    sb = new StringBuilder();
    this.toStringBuilder(sb);
    return sb.toString();
  }

  /**
   * Obtain the elements of this set.
   * 
   * @return the elements of this set.
   */
  public final FSET[] getElements() {
    return this.m_elements;
  }

  /**
   * Returns <tt>true</tt> if this set contains no elements.
   * 
   * @return <tt>true</tt> if this set contains no elements.
   */
  @Override
  public final boolean isEmpty() {
    return (this.m_count <= 0);
  }

  /**
   * Returns <tt>true</tt> if this set contains the specified element.
   * More formally, returns <tt>true</tt> if and only if this set
   * contains an element <code>e</code> such that
   * <code>(o==null ? e==null :
   * o.equals(e))</code>.
   * 
   * @param o
   *          element whose presence in this set is to be tested.
   * @return <tt>true</tt> if this set contains the specified element.
   * @throws ClassCastException
   *           if the type of the specified element is incompatible with
   *           this set (optional).
   * @throws NullPointerException
   *           if the specified element is null and this set does not
   *           support null elements (optional).
   */
  @Override
  public final boolean contains(final Object o) {
    FixedSetElement<?>[] fs;
    int i;

    fs = this.m_elements;
    for (i = (this.m_count - 1); i >= 0; i--) {
      if (Utils.testEqual(o, fs[i]))
        return true;
    }

    return false;
  }

  /**
   * Returns an iterator over the elements in this set. The elements are
   * returned in no particular order (unless this set is an instance of
   * some class that provides a guarantee).
   * 
   * @return an iterator over the elements in this set.
   */
  @Override
  public final Iterator<FSET> iterator() {
    return new ArrayIterator<FSET>(this.m_elements, 0, this.m_count);
  }

  /**
   * Obtain the <code>index</code>th instance of class
   * <code>clazz</code>.
   * 
   * @param clazz
   *          the class
   * @param index
   *          the index of the instance
   * @param <T>
   *          the type
   * @return the <code>index</code>th instance of class
   *         <code>clazz</code> or <code>null</code> if no such
   *         instance can be found
   */
  @SuppressWarnings("unchecked")
  public final <T extends FSET> T getInstance(
      final Class<? extends T> clazz, final int index) {
    int i, l, c;
    FixedSetElement<?>[] s;
    FixedSetElement<?> e;

    l = this.m_count;
    s = this.m_elements;
    c = index;
    for (i = 0; i < l; i++) {
      e = s[i];
      if (clazz.isInstance(e)) {
        if ((--c) <= 0)
          return (T) e;
      }
    }

    return null;
  }
}

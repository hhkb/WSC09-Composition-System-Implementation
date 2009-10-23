/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-14
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.arrays.DynamicArray.java
 * Last modification: 2007-11-14
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

package org.sfc.collections.arrays;

import java.io.Serializable;
import java.lang.reflect.Array;

import org.sfc.text.TextUtils;
import org.sfc.text.Textable;

/**
 * A dynamic array
 * 
 * @param <AT>
 *          the internal array type
 * @author Thomas Weise
 */
abstract class DynamicArray<AT> extends Textable implements Serializable {
  /**
   * The serial version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * the data
   */
  AT m_data;

  /**
   * the size
   */
  int m_size;

  /**
   * the array length
   */
  int m_length;

  /**
   * Create a new dynamic array
   */
  DynamicArray() {
    this(128);
  }

  /**
   * Create a new dynamic array of at list the given initial size
   * 
   * @param initialSize
   *          the initial size of the array
   */
  DynamicArray(final int initialSize) {
    super();
    this.m_data = this.createArray(initialSize);
    this.m_length = initialSize;
  }

  /**
   * Create a new array of the given length
   * 
   * @param length
   *          the length
   * @return the new array
   */
  abstract AT createArray(final int length);

  /**
   * Obtain the new size
   * 
   * @return the size of this array
   */
  public int size() {
    return this.m_size;
  }

  /**
   * Add the given array to this one.
   * 
   * @param array
   *          the array
   * @param start
   *          the start
   * @param length
   *          the length
   * @param <ATT>
   *          the type of the array
   */
  public <ATT extends AT> void add(final ATT array, final int start,
      final int length) {

    int s, nl;
    AT a;

    s = this.m_size;
    nl = (s + length);
    a = this.m_data;

    if (nl > this.m_length) {
      a = this.createArray(this.m_length = (nl << 1));
      System.arraycopy(this.m_data, 0, a, 0, s);
      this.m_data = a;
    }

    System.arraycopy(array, start, a, s, length);
    this.m_size = nl;
  }

  /**
   * Obtain a copy of this dynamic array's data
   * 
   * @return a copy of this dynamic array's data
   */
  public AT getCopy() {
    int s;
    AT a;
    s = this.m_size;
    a = this.createArray(s);
    System.arraycopy(this.m_data, 0, a, 0, s);
    return a;
  }

  /**
   * Set the size of this dynamic array
   * 
   * @param size
   *          the new size
   */
  public void setSize(final int size) {
    AT a;

    if (size > 0) {
      if (size > this.m_length) {
        a = this.createArray(this.m_length = (size << 1));
        System.arraycopy(this.m_data, 0, a, 0, this.m_size);
        this.m_data = a;
      }

      this.m_size = size;
    } else
      this.m_size = 0;
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    AT a;
    int i, s;

    a = this.m_data;
    TextUtils.appendClass(a.getClass(), sb);
    sb.append(' ');
    sb.append('{');
    s = this.m_size;
    for (i = 0; i < s; i++) {
      if (i > 0) {
        sb.append(',');
        sb.append(' ');
      }
      sb.append(Array.get(a, i));
    }
    sb.append('}');
  }
}

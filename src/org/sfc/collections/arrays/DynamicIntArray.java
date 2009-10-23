/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-14
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.arrays.DynamicIntArray.java
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

/**
 * A dynamic integer array.
 * 
 * @author Thomas Weise
 */
public class DynamicIntArray extends DynamicArray<int[]> {
  /**
   * The serial version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Create a new dynamic array
   */
  public DynamicIntArray() {
    this(128);
  }

  /**
   * Create a new dynamic array of at list the given initial size
   * 
   * @param initialSize
   *          the initial size of the array
   */
  public DynamicIntArray(final int initialSize) {
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
  @Override
  final int[] createArray(final int length) {
    return new int[length];
  }

  /**
   * Add a given integer value to this list
   * 
   * @param value
   *          the value
   */
  public void add(final int value) {
    int s;
    int[] a;

    a = this.m_data;
    s = this.m_size;
    if (s >= this.m_length) {
      a = new int[this.m_length = ((s + 1) << 1)];
      System.arraycopy(this.m_data, 0, a, 0, s);
      this.m_data = a;
    }
    a[s] = value;
    this.m_size = (s + 1);
  }

  /**
   * Obtain the item at index <code>index</code>.
   * 
   * @param index
   *          the index
   * @return the value at that position
   */
  public int get(final int index) {
    return this.m_data[index];
  }

}

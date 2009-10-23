/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.iterators.ArrayIterator.java
 * Last modification: 2008-04-22
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

package org.sfc.collections.iterators;

import java.util.NoSuchElementException;

/**
 * This class iterates over a passed in array.
 * 
 * @param <T>
 *          The sort of items to iterate over.
 * @author Thomas Weise
 */
public final class ArrayIterator<T> extends IteratorBase<T>  {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The data.
   */
  private final T[] m_data;

  /**
   * The current index.
   */
  private int m_index;

  /**
   * The counter.
   */
  private int m_cnt;

  /**
   * Create a new array iterator.
   * 
   * @param data
   *          The array to iterate over.
   */
  public ArrayIterator(final T[] data) {
    this(data, 0, data.length);
  }

  /**
   * Obtain the number of maximum possible combinations
   * 
   * @return the number of maximum possible combinations
   */
  @Override
  public int getCombinationCount() {
    return this.m_cnt;
  }

  /**
   * Create a new array iterator.
   * 
   * @param data
   *          The data to iterate over.
   * @param index
   *          The start index.
   * @param count
   *          The count of items in the iteration.
   */
  public ArrayIterator(final T[] data, final int index, final int count) {
    super();
    this.m_data = data;
    this.m_index = index;
    this.m_cnt = count;
  }

  /**
   * Returns <tt>true</tt> if the iteration has more elements. (In other
   * words, returns <tt>true</tt> if <tt>next</tt> would return an
   * element rather than throwing an exception.)
   * 
   * @return <tt>true</tt> if the iterator has more elements.
   */
  public final boolean hasNext() {
    return (this.m_cnt > 0);
  }

  /**
   * Returns the next element in the iteration. Calling this method
   * repeatedly until the {@link #hasNext()} method returns false will
   * return each element in the underlying collection exactly once.
   * 
   * @return the next element in the iteration.
   * @exception NoSuchElementException
   *              iteration has no more elements.
   */
  public final T next() {
    if ((this.m_cnt--) > 0) {
      return this.m_data[this.m_index++];
    }
    throw new NoSuchElementException();
  }
}

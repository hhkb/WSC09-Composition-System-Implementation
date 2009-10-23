/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.iterators.ListIterator.java
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

import java.util.List;
import java.util.NoSuchElementException;

/**
 * A list iterator prototype.
 * 
 * @param <T>
 *          The element type.
 * @author Thomas Weise
 */
public class ListIterator<T> extends IteratorBase<T> implements
    java.util.ListIterator<T> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The list backing this iterator.
   */
  private final List<T> m_list;

  /**
   * The current iteration index.
   */
  private int m_index;

  /**
   * Create a new list iterator.
   * 
   * @param list
   *          The list to work on.
   * @param index
   *          The index to start at.
   */
  public ListIterator(final List<T> list, final int index) {
    super();
    this.m_list = list;
    this.m_index = (index - 1);
  }

  /**
   * Obtain the number of maximum possible combinations
   * 
   * @return the number of maximum possible combinations
   */
  @Override
  public int getCombinationCount() {
    return (this.m_list.size() - this.m_index);
  }

  /**
   * Returns <tt>true</tt> if this list iterator has more elements when
   * traversing the list in the forward direction. (In other words, returns
   * <tt>true</tt> if <tt>next</tt> would return an element rather than
   * throwing an exception.)
   * 
   * @return <tt>true</tt> if the list iterator has more elements when
   *         traversing the list in the forward direction.
   */
  public boolean hasNext() {
    return (this.m_list.size() > (1 + this.m_index));
  }

  /**
   * Returns the next element in the list. This method may be called
   * repeatedly to iterate through the list, or intermixed with calls to
   * <tt>previous</tt> to go back and forth. (Note that alternating calls
   * to <tt>next</tt> and <tt>previous</tt> will return the same
   * element repeatedly.)
   * 
   * @return the next element in the list.
   * @exception NoSuchElementException
   *              if the iteration has no next element.
   */
  public T next() {
    int c;
    List<T> l;

    c = ++this.m_index;
    l = this.m_list;
    if (c >= l.size())
      throw new NoSuchElementException();
    // this.m_index = (c + 1);
    return l.get(c);
  }

  /**
   * Returns <tt>true</tt> if this list iterator has more elements when
   * traversing the list in the reverse direction. (In other words, returns
   * <tt>true</tt> if <tt>previous</tt> would return an element rather
   * than throwing an exception.)
   * 
   * @return <tt>true</tt> if the list iterator has more elements when
   *         traversing the list in the reverse direction.
   */
  public boolean hasPrevious() {
    return (this.m_index >= 0);
  }

  /**
   * Returns the previous element in the list. This method may be called
   * repeatedly to iterate through the list backwards, or intermixed with
   * calls to <tt>next</tt> to go back and forth. (Note that alternating
   * calls to <tt>next</tt> and <tt>previous</tt> will return the same
   * element repeatedly.)
   * 
   * @return the previous element in the list.
   * @exception NoSuchElementException
   *              if the iteration has no previous element.
   */
  public T previous() {
    int c;
    List<T> l;

    c = (this.m_index - 1);
    if (c < -1)
      throw new NoSuchElementException();
    l = this.m_list;
    this.m_index = c;
    return l.get(c);
  }

  /**
   * Returns the index of the element that would be returned by a
   * subsequent call to <tt>next</tt>. (Returns list size if the list
   * iterator is at the end of the list.)
   * 
   * @return the index of the element that would be returned by a
   *         subsequent call to <tt>next</tt>, or list size if list
   *         iterator is at end of list.
   */
  public int nextIndex() {
    return (this.m_index + 1);
  }

  /**
   * Returns the index of the element that would be returned by a
   * subsequent call to <tt>previous</tt>. (Returns -1 if the list
   * iterator is at the beginning of the list.)
   * 
   * @return the index of the element that would be returned by a
   *         subsequent call to <tt>previous</tt>, or -1 if list
   *         iterator is at beginning of list.
   */
  public int previousIndex() {
    return (this.m_index);
  }

  // Modification Operations

  /**
   * Removes from the list the last element that was returned by
   * <tt>next</tt> or <tt>previous</tt> (optional operation). This call
   * can only be made once per call to <tt>next</tt> or <tt>previous</tt>.
   * It can be made only if <tt>ListIterator.add</tt> has not been called
   * after the last call to <tt>next</tt> or <tt>previous</tt>.
   * 
   * @exception UnsupportedOperationException
   *              if the <tt>remove</tt> operation is not supported by
   *              this list iterator.
   * @exception IllegalStateException
   *              neither <tt>next</tt> nor <tt>previous</tt> have been
   *              called, or <tt>remove</tt> or <tt>add</tt> have been
   *              called after the last call to * <tt>next</tt> or
   *              <tt>previous</tt>.
   */
  @Override
  public void remove() {
    this.m_list.remove(this.m_index);
  }

  /**
   * Replaces the last element returned by <tt>next</tt> or
   * <tt>previous</tt> with the specified element (optional operation).
   * This call can be made only if neither <tt>ListIterator.remove</tt>
   * nor <tt>ListIterator.add</tt> have been called after the last call
   * to <tt>next</tt> or <tt>previous</tt>.
   * 
   * @param p_o
   *          the element with which to replace the last element returned
   *          by <tt>next</tt> or <tt>previous</tt>.
   * @exception UnsupportedOperationException
   *              if the <tt>set</tt> operation is not supported by this
   *              list iterator.
   * @exception ClassCastException
   *              if the class of the specified element prevents it from
   *              being added to this list.
   * @exception IllegalArgumentException
   *              if some aspect of the specified element prevents it from
   *              being added to this list.
   * @exception IllegalStateException
   *              if neither <tt>next</tt> nor <tt>previous</tt> have
   *              been called, or <tt>remove</tt> or <tt>add</tt> have
   *              been called after the last call to <tt>next</tt> or
   *              <tt>previous</tt>.
   */
  public void set(final T p_o) {
    this.m_list.set(this.m_index, p_o);
  }

  /**
   * Inserts the specified element into the list (optional operation). The
   * element is inserted immediately before the next element that would be
   * returned by <tt>next</tt>, if any, and after the next element that
   * would be returned by <tt>previous</tt>, if any. (If the list
   * contains no elements, the new element becomes the sole element on the
   * list.) The new element is inserted before the implicit cursor: a
   * subsequent call to <tt>next</tt> would be unaffected, and a
   * subsequent call to <tt>previous</tt> would return the new element.
   * (This call increases by one the value that would be returned by a call
   * to <tt>nextIndex</tt> or <tt>previousIndex</tt>.)
   * 
   * @param p_o
   *          the element to insert.
   * @exception UnsupportedOperationException
   *              if the <tt>add</tt> method is not supported by this
   *              list iterator.
   * @exception ClassCastException
   *              if the class of the specified element prevents it from
   *              being added to this list.
   * @exception IllegalArgumentException
   *              if some aspect of this element prevents it from being
   *              added to this list.
   */
  public void add(final T p_o) {
    this.m_list.add(++this.m_index, p_o);
  }
}

/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.maps.DefaultMapKeySet.java
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

package org.sfc.collections.maps;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.sfc.collections.iterators.IteratorBase;

/**
 * The internal key set for hash maps.
 * 
 * @param <K>
 *          The type of the map's keys.
 * @param <V>
 *          The type of the map's values.
 * @author Thomas Weise
 */
final class DefaultMapKeySet<K, V> extends AbstractSet<K> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The hash-map.
   */
  private final MapBase<K, V> m_map;

  /**
   * Create a new hash map key set.
   * 
   * @param map
   *          The map.
   */
  DefaultMapKeySet(final MapBase<K, V> map) {
    super();
    this.m_map = map;
  }

  /**
   * Returns an iterator over the elements contained in this collection.
   * 
   * @return an iterator over the elements contained in this collection.
   */
  @Override
  public final Iterator<K> iterator() {
    return new KeySetIterator<K, V>(this.m_map.iterator());
  }

  /**
   * Returns <tt>true</tt> if this collection contains the specified
   * element. More formally, returns <tt>true</tt> if and only if this
   * collection contains at least one element <tt>e</tt> such that
   * <tt>(o==null ? e==null : o.equals(e))</tt>.
   * <p>
   * This implementation iterates over the elements in the collection,
   * checking each element in turn for equality with the specified element.
   * 
   * @param o
   *          object to be checked for containment in this collection.
   * @return <tt>true</tt> if this collection contains the specified
   *         element.
   */
  @Override
  public final boolean contains(final Object o) {
    return this.m_map.containsKey(o);
  }

  /**
   * Removes a single instance of the specified element from this
   * collection, if it is present (optional operation). More formally,
   * removes an element <tt>e</tt> such that <tt>(o==null ? e==null :
   * o.equals(e))</tt>,
   * if the collection contains one or more such elements. Returns
   * <tt>true</tt> if the collection contained the specified element (or
   * equivalently, if the collection changed as a result of the call).
   * <p>
   * This implementation iterates over the collection looking for the
   * specified element. If it finds the element, it removes the element
   * from the collection using the iterator's remove method.
   * <p>
   * Note that this implementation throws an
   * <tt>UnsupportedOperationException</tt> if the iterator returned by
   * this collection's iterator method does not implement the
   * <tt>remove</tt> method and this collection contains the specified
   * object.
   * 
   * @param o
   *          element to be removed from this collection, if present.
   * @return <tt>true</tt> if the collection contained the specified
   *         element.
   * @throws UnsupportedOperationException
   *           if the <tt>remove</tt> method is not supported by this
   *           collection.
   */
  @Override
  public final boolean remove(final Object o) {
    return (this.m_map.removeEntry(o) != null);
  }

  /**
   * Returns the number of elements in this collection. If the collection
   * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
   * <tt>Integer.MAX_VALUE</tt>.
   * 
   * @return the number of elements in this collection.
   */
  @Override
  public final int size() {
    return this.m_map.size();
  }

  /**
   * Removes all of the elements from this collection (optional operation).
   * The collection will be empty after this call returns (unless it throws
   * an exception).
   * <p>
   * This implementation iterates over this collection, removing each
   * element using the <tt>Iterator.remove</tt> operation. Most
   * implementations will probably choose to override this method for
   * efficiency.
   * <p>
   * Note that this implementation will throw an
   * <tt>UnsupportedOperationException</tt> if the iterator returned by
   * this collection's <tt>iterator</tt> method does not implement the
   * <tt>remove</tt> method and this collection is non-empty.
   * 
   * @throws UnsupportedOperationException
   *           if the <tt>clear</tt> method is not supported by this
   *           collection.
   */
  @Override
  public final void clear() {
    this.m_map.clear();
  }

  /**
   * The internal iterator class.
   * 
   * @param <K>
   *          The type of the map's keys.
   * @param <V>
   *          The type of the map's values.
   * @author Thomas Weise
   */
  private static final class KeySetIterator<K, V> extends IteratorBase<K> {
    /**
     * the serial version uid
     */
    private static final long serialVersionUID = 1;

    /**
     * The internal iterator.
     */
    private final Iterator<Entry<K, V>> m_iterator;

    /**
     * Create a new hash map iterator.
     * 
     * @param iterator
     *          The underlying iterator.
     */
    KeySetIterator(final Iterator<Entry<K, V>> iterator) {
      super();
      this.m_iterator = iterator;
    }

    /**
     * Obtain the next Key in the Key set.
     * 
     * @return The next Key in the Key set.
     */
    public final K next() {
      return this.m_iterator.next().getKey();
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In
     * other words, returns <tt>true</tt> if <tt>next</tt> would return
     * an element rather than throwing an exception.)
     * 
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public final boolean hasNext() {
      return this.m_iterator.hasNext();
    }

    /**
     * Remove the current element in the iteration.
     */
    @Override
    public final void remove() {
      this.m_iterator.remove();
    }
  }
}

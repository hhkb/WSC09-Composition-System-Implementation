/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.maps.DefaultMapEntrySet.java
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

/**
 * The internal entry set for hash maps.
 * 
 * @param <K>
 *          The type of the map's keys.
 * @param <V>
 *          The type of the map's values.
 * @author Thomas Weise
 */
final class DefaultMapEntrySet<K, V> extends
    AbstractSet<Entry<K, V>> {
  /**
   * The hash-map.
   */
  private final MapBase<K, V> m_map;

  /**
   * Create a new hash map entry set.
   * 
   * @param map
   *          The map.
   */
  DefaultMapEntrySet(final MapBase<K, V> map) {
    super();
    this.m_map = map;
  }

  /**
   * Returns an iterator over the elements contained in this collection.
   * 
   * @return an iterator over the elements contained in this collection.
   */
  @Override
  public final Iterator<Entry<K, V>> iterator() {
    return this.m_map.iterator();
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
  @SuppressWarnings("unchecked")
  public final boolean contains(final Object o) {
    V e;
    Entry<K, V> candidate;

    if (!(o instanceof Entry))
      return false;

    e = (V) (((Entry) o).getKey());
    candidate = this.m_map.getEntry(e);

    return ((candidate != null) && candidate.equals(e));
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

}

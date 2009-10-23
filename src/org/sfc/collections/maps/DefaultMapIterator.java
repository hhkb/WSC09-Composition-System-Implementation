/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.maps.DefaultMapIterator.java
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

import java.util.NoSuchElementException;
import java.util.Map.Entry;

import org.sfc.collections.iterators.IteratorBase;

/**
 * An iterator based on a hash map.
 * 
 * @param <K>
 *          The type of the map's keys.
 * @param <V>
 *          The type of the map's values.
 * @author Thomas Weise
 */
class DefaultMapIterator<K, V> extends IteratorBase<Entry<K, V>> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The hash map.
   */
  final DefaultMap<K, V> m_map;

  /**
   * The index.
   */
  private int m_index;

  /**
   * The current entry.
   */
  private DefaultMapEntry<K, V> m_current;

  /**
   * The next entry.
   */
  private DefaultMapEntry<K, V> m_next;

  /**
   * Create a new hash map iterator.
   * 
   * @param map
   *          The map the new iterator is intended for.
   */
  DefaultMapIterator(final DefaultMap<K, V> map) {
    super();

    DefaultMapEntry<K, V>[] t;
    int i;
    DefaultMapEntry<K, V> n;

    this.m_map = map;

    t = map.m_table;
    i = t.length;
    n = null;

    if (map.m_count > 0) {
      while ((i > 0) && ((n = t[--i]) == null)) { /* */
      }

      this.m_next = n;
      this.m_index = i;
    } else {
      this.m_index = -1;
    }
  }

  /**
   * Check whether there is another element in the iteration.
   * 
   * @return <code>true</code> if and only there is another element in
   *         the iteration.
   */
  public boolean hasNext() {
    return (this.m_next != null);
  }

  /**
   * Obtain the next entry in the entry set.
   * 
   * @return The next entry in the entry set.
   */
  public Entry<K, V> next() {
    DefaultMapEntry<K, V> e, n;
    DefaultMapEntry<K, V>[] t;
    int i;

    e = this.m_next;

    if (e == null)
      throw new NoSuchElementException();

    n = e.m_next;

    if (n == null) {
      t = this.m_map.m_table;
      i = this.m_index;

      while ((i > 0) && ((n = t[--i]) == null)) { /* */
      }

      this.m_index = i;
    }

    this.m_next = n;

    return (this.m_current = e);
  }

  /**
   * Remove the current element in the iteration.
   */
  @Override
  public void remove() {
    DefaultMapEntry<K, V> e;

    e = this.m_current;
    if (e == null)
      throw new IllegalStateException();
    this.m_current = null;
    this.m_map.removeEntry(e.m_key);
  }
}

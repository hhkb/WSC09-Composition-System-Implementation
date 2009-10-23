/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.maps.MapBase.java
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

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.sfc.utils.ICloneable;

/**
 * This is the base class for all sfc maps.
 * 
 * @param <K>
 *          The type of the map's keys.
 * @param <V>
 *          The type of the map's values.
 * @author Thomas Weise
 */
public abstract class MapBase<K, V> extends
    AbstractMap<K, V> implements
    Iterable<Map.Entry<K, V>>, ICloneable, Serializable {
  /**
   * The entry set used.
   */
  private volatile transient DefaultMapEntrySet<K, V> m_entrySet;

  /**
   * The key set.
   */
  private volatile transient DefaultMapKeySet<K, V> m_keySet;

  /**
   * The value collection.
   */
  private volatile transient DefaultMapValueCollection<K, V> m_valueCollection;

  /**
   * Returns an iterator over a set of elements of type T.
   * 
   * @return an Iterator.
   */
  public Iterator<Map.Entry<K, V>> iterator() {
    return this.entrySet().iterator();
  }

  /**
   * Returns the entry associated with the specified key in the DefaultMap.
   * Returns null if the DefaultMap contains no mapping for this key.
   * 
   * @param key
   *          The key to find the entry for.
   * @return The entry matching to <code>key</code>, <code>null</code>
   *         if no such entry is included.
   */
  public abstract Entry<K, V> getEntry(final Object key);

  /**
   * Removes the entry associated with the specified key from the
   * DefaultMap. Returns null if the DefaultMap contains no mapping for
   * this key.
   * 
   * @param key
   *          The key to find the entry for.
   * @return The entry matching to <code>key</code>, <code>null</code>
   *         if no such entry is included. This entry is removed from the
   *         map.
   */
  public abstract Entry<K, V> removeEntry(final Object key);

  /**
   * Returns a set view of the mappings contained in this map. Each element
   * in this set is a Map.Entry. The set is backed by the map, so changes
   * to the map are reflected in the set, and vice-versa. (If the map is
   * modified while an iteration over the set is in progress, the results
   * of the iteration are undefined.) The set supports element removal,
   * which removes the corresponding entry from the map, via the
   * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
   * <tt>removeAll</tt>, <tt>retainAll</tt> and <tt>clear</tt>
   * operations. It does not support the <tt>add</tt> or <tt>addAll</tt>
   * operations.
   * 
   * @return a set view of the mappings contained in this map.
   */
  @Override
  public Set<java.util.Map.Entry<K, V>> entrySet() {
    if (this.m_entrySet != null)
      return this.m_entrySet;
    return (this.m_entrySet = new DefaultMapEntrySet<K, V>(
        this));
  }

  /**
   * Returns a set view of the keys contained in this map. The set is
   * backed by the map, so changes to the map are reflected in the set, and
   * vice-versa. The set supports element removal, which removes the
   * corresponding mapping from this map, via the <tt>Iterator.remove</tt>,
   * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt>,
   * and <tt>clear</tt> operations. It does not support the <tt>add</tt>
   * or <tt>addAll</tt> operations.
   * 
   * @return a set view of the keys contained in this map.
   */
  @Override
  public Set<K> keySet() {
    if (this.m_keySet != null)
      return this.m_keySet;
    return (this.m_keySet = new DefaultMapKeySet<K, V>(this));
  }

  /**
   * Returns a collection view of the values contained in this map. The
   * collection is backed by the map, so changes to the map are reflected
   * in the collection, and vice-versa. (If the map is modified while an
   * iteration over the collection is in progress, the results of the
   * iteration are undefined.) The collection supports element removal,
   * which removes the corresponding entry from the map, via the
   * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
   * <tt>removeAll</tt>, <tt>retainAll</tt> and <tt>clear</tt>
   * operations. It does not support the <tt>add</tt> or <tt>addAll</tt>
   * operations.
   * <p>
   * The collection is created the first time this method is called, and
   * returned in response to all subsequent calls. No synchronization is
   * performed, so there is a slight chance that multiple calls to this
   * method will not all return the same Collection.
   * 
   * @return a collection view of the values contained in this map.
   */
  @Override
  public Collection<V> values() {
    if (this.m_valueCollection != null)
      return this.m_valueCollection;
    return (this.m_valueCollection = new DefaultMapValueCollection<K, V>(
        this));
  }

  /**
   * Put the specified key-value pair into this map only if there is not
   * yet a mapping for the key defined.
   * 
   * @param key
   *          The key.
   * @param value
   *          The value that should be stored under the key if the key is
   *          not yet known to the map.
   * @return <code>true</code> if and only if this operation was
   *         successful, meaning that there didn't exist a mapping with the
   *         key <code>key</code> yet. <code>false</code> if there
   *         already was a key-value mapping with <code>key</code> as
   *         key-component.
   */
  public abstract boolean putIfNew(final K key, final V value);

  /**
   * Create a perfect copy of this map.
   * 
   * @return A perfect copy of this map.
   */
  @Override
  @SuppressWarnings("unchecked")
  public Object clone() {
    MapBase<K, V> t;
    try {
      t = (MapBase<K, V>) (super.clone());
      t.m_entrySet = null;
      t.m_keySet = null;
      t.m_valueCollection = null;
      return t;
    } catch (Throwable x) {
      return this;
    }
  }
}

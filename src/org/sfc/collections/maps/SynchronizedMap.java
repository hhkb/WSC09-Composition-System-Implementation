/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.maps.SynchronizedMap.java
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

import java.util.Iterator;
import java.util.Map;

/**
 * A synchronized map ensures that all access operations are performed in a
 * thread safe manner. This can, of course, not be guaranteed for
 * iterator-based actions.
 * 
 * @param <K>
 *          The type of the map's keys.
 * @param <V>
 *          The type of the map's values.
 * @author Thomas Weise
 */
public class SynchronizedMap<K, V> extends DefaultMap<K, V> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new synchronized map.
   * 
   * @param initialCapacity
   *          The initial capacity wanted for this map, set this parameter
   *          to <code>-1</code> to use a reasonable default.
   */
  public SynchronizedMap(final int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * Returns the value to which the specified key is mapped in this
   * identity hash map, or <tt>null</tt> if the map contains no mapping
   * for this key. A return value of <tt>null</tt> does not
   * <i>necessarily</i> indicate that the map contains no mapping for the
   * key; it is also possible that the map explicitly maps the key to
   * <tt>null</tt>. The <tt>containsKey</tt> method may be used to
   * distinguish these two cases.
   * 
   * @param key
   *          the key whose associated value is to be returned.
   * @return the value to which this map maps the specified key, or
   *         <tt>null</tt> if the map contains no mapping for this key.
   * @see #put(Object, Object)
   */
  @Override
  public synchronized V get(final Object key) {
    return super.get(key);
  }

  /**
   * Returns <tt>true</tt> if this map contains a mapping for the
   * specified key.
   * 
   * @param key
   *          The key whose presence in this map is to be tested
   * @return <tt>true</tt> if this map contains a mapping for the
   *         specified key.
   */
  @Override
  public synchronized boolean containsKey(final Object key) {
    return super.containsKey(key);
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
  @Override
  public synchronized Entry<K, V> getEntry(final Object key) {
    return super.getEntry(key);
  }

  /**
   * Associates the specified value with the specified key in this map. If
   * the map previously contained a mapping for this key, the old value is
   * replaced.
   * 
   * @param key
   *          key with which the specified value is to be associated.
   * @param value
   *          value to be associated with the specified key.
   * @return previous value associated with specified key, or <tt>null</tt>
   *         if there was no mapping for key. A <tt>null</tt> return can
   *         also indicate that the DefaultMap previously associated
   *         <tt>null</tt> with the specified key.
   */
  @Override
  public synchronized V put(final K key, final V value) {
    return super.put(key, value);
  }

  /**
   * Copies all of the mappings from the specified map to this map These
   * mappings will replace any mappings that this map had for any of the
   * keys currently in the specified map.
   * 
   * @param map
   *          mappings to be stored in this map.
   * @throws NullPointerException
   *           if the specified map is null.
   */
  @Override
  public synchronized void putAll(final Map<? extends K, ? extends V> map) {
    super.putAll(map);
  }

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
  @Override
  public synchronized Entry<K, V> removeEntry(final Object key) {
    return super.removeEntry(key);
  }

  /**
   * Removes all mappings from this map.
   */
  @Override
  public synchronized void clear() {
    super.clear();
  }

  /**
   * Returns <tt>true</tt> if this map maps one or more keys to the
   * specified value.
   * 
   * @param value
   *          value whose presence in this map is to be tested.
   * @return <tt>true</tt> if this map maps one or more keys to the
   *         specified value.
   */
  @Override
  public synchronized boolean containsValue(final Object value) {
    return super.containsValue(value);
  }

  /**
   * Returns an iterator over a set of elements of type T.
   * 
   * @return an Iterator.
   */
  @Override
  public synchronized Iterator<Map.Entry<K, V>> iterator() {
    return new SyncIterator<K, V>(this);
  }

  /**
   * Create a new hash map entry.
   * 
   * @param key
   *          The key of the entry.
   * @param value
   *          The value of the entry.
   * @param next
   *          The next entry.
   * @param hash
   *          The key's hash value.
   * @return The new entry.
   */
  @Override
  DefaultMapEntry<K, V> createEntry(final K key, final V value,
      final DefaultMapEntry<K, V> next, final int hash) {
    return new SyncEntry(key, value, next, hash);
  }

  /**
   * Create a perfect copy of this map.
   * 
   * @return A perfect copy of this map.
   */
  @Override
  public synchronized Object clone() {
    return super.clone();
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
  @Override
  public synchronized boolean putIfNew(final K key, final V value) {
    return super.putIfNew(key, value);
  }

  /**
   * A synchronized iterator.
   * 
   * @param <K>
   *          The type of the map's keys.
   * @param <V>
   *          The type of the map's values.
   */
  static final class SyncIterator<K, V> extends DefaultMapIterator<K, V> {
    /**
     * the serial version uid
     */
    private static final long serialVersionUID = 1;

    /**
     * Create a new hash map iterator.
     * 
     * @param map
     *          The map the new iterator is intended for.
     */
    SyncIterator(final DefaultMap<K, V> map) {
      super(map);
    }

    /**
     * Obtain the next entry in the entry set.
     * 
     * @return The next entry in the entry set.
     */
    @Override
    public Entry<K, V> next() {
      synchronized (this.m_map) {
        return super.next();
      }
    }
  }

  /**
   * The synchronized form of a hash map entry.
   * 
   * @author Thomas Weise
   */
  private final class SyncEntry extends DefaultMapEntry<K, V> {
    /**
     * The serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new hash map entry.
     * 
     * @param key
     *          The key of the entry.
     * @param value
     *          The value of the entry.
     * @param next
     *          The next entry.
     * @param hash
     *          The key's hash value.
     */
    SyncEntry(final K key, final V value,
        final DefaultMapEntry<K, V> next, final int hash) {
      super(key, value, next, hash);
    }

    /**
     * Replaces the value corresponding to this entry with the specified
     * value (optional operation). (Writes through to the map.) The
     * behavior of this call is undefined if the mapping has already been
     * removed from the map (by the iterator's <tt>remove</tt>
     * operation).
     * 
     * @param value
     *          new value to be stored in this entry.
     * @return old value corresponding to the entry.
     * @throws UnsupportedOperationException
     *           if the <tt>put</tt> operation is not supported by the
     *           backing map.
     * @throws ClassCastException
     *           if the class of the specified value prevents it from being
     *           stored in the backing map.
     * @throws IllegalArgumentException
     *           if some aspect of this value prevents it from being stored
     *           in the backing map.
     * @throws NullPointerException
     *           if the backing map does not permit <tt>null</tt> values,
     *           and the specified value is <tt>null</tt>.
     * @throws IllegalStateException
     *           implementations may, but are not required to, throw this
     *           exception if the entry has been removed from the backing
     *           map
     */
    @Override
    public V setValue(final V value) {
      synchronized (SynchronizedMap.this) {
        return super.setValue(value);
      }
    }
  }
}

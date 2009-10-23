/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.maps.DefaultMap.java
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

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import org.sfc.utils.HashUtils;
import org.sfc.utils.Utils;

/**
 * The fast, hash-based implementation of maps in sfc.
 * 
 * @param <K>
 *          The type of the map's keys.
 * @param <V>
 *          The type of the map's values.
 * @author Thomas Weise
 */
public class DefaultMap<K, V> extends MapBase<K, V> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The count of elements currently stored.
   */
  transient int m_count;

  /**
   * The internal hash table.
   */
  transient DefaultMapEntry<K, V>[] m_table;

  
  /**
   * Create a new hash map.
   */
  public DefaultMap() {
    this(-1);
  }
  
  /**
   * Create a new hash map.
   * 
   * @param initialCapacity
   *          The initial capacity wanted for this map, set this parameter
   *          to <code>-1</code> to use a reasonable default.
   */
  @SuppressWarnings("unchecked")
  public DefaultMap(final int initialCapacity) {
    super();

    int c;

    c = 16;
    while (c < initialCapacity)
      c <<= 1;

    this.m_table = new DefaultMapEntry[c];
  }

  // /**
  // * Returns a hash value for the specified object. In addition to
  // * the object's own hashCode, this method applies a "supplemental
  // * hash function," which defends against poor quality hash functions.
  // * This is critical because DefaultMap uses power-of two length
  // * hash tables.<p>
  // *
  // * The shift distances in this function were chosen as the result
  // * of an automated search over the entire four-dimensional search
  // space.
  // *
  // * @param x The object.
  // * @return The refined hash code.
  // */
  // private static final int calcHash(final Object x)
  // {
  // int h;
  //    
  // if(x == null) return 1657484237;
  //    
  // h = x.hashCode();
  //
  // h += (~(h << 9));
  // h ^= (h >>> 14);
  // h += (h << 4);
  // h ^= (h >>> 10);
  // return h;
  // }

  // /**
  // * Check for equality of non-null reference x and possibly-null y.
  // *
  // * @param x
  // * The first object.
  // * @param y
  // * The second object.
  // * @return <code>true</code> if and only if the first object equals the
  // * second one.
  // */
  // private static final boolean eq(final Object x, final Object y) {
  // return ((x == y) || ((x != null) && (x.equals(y))));
  // }

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
  DefaultMapEntry<K, V> createEntry(final K key, final V value,
      final DefaultMapEntry<K, V> next, final int hash) {
    return new DefaultMapEntry<K, V>(key, value, next, hash);
  }

  /**
   * Returns the number of key-value mappings in this map.
   * 
   * @return the number of key-value mappings in this map.
   */
  @Override
  public int size() {
    return this.m_count;
  }

  /**
   * Returns <tt>true</tt> if this map contains no key-value mappings.
   * 
   * @return <tt>true</tt> if this map contains no key-value mappings.
   */
  @Override
  public boolean isEmpty() {
    return (this.m_count <= 0);
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
  public V get(final Object key) {
    int hash;
    DefaultMapEntry<K, V> t;

    hash = HashUtils.objectHashCode(key);

    for (t = this.m_table[hash & (this.m_table.length - 1)]; t != null; t = t.m_next) {
      if ((t.m_hash == hash) && Utils.testEqual(key, t.m_key)) {
        return t.m_value;
      }
    }

    return null;
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
  public boolean containsKey(final Object key) {
    int hash;
    DefaultMapEntry<K, V> t;

    hash = HashUtils.objectHashCode(key);

    for (t = this.m_table[hash & (this.m_table.length - 1)]; t != null; t = t.m_next) {
      if ((t.m_hash == hash) && Utils.testEqual(key, t.m_key)) {
        return true;
      }
    }

    return false;
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
  public Entry<K, V> getEntry(final Object key) {
    int hash;
    DefaultMapEntry<K, V> t;

    hash = HashUtils.objectHashCode(key);

    for (t = this.m_table[hash & (this.m_table.length - 1)]; t != null; t = t.m_next) {
      if ((t.m_hash == hash) && Utils.testEqual(key, t.m_key)) {
        return t;
      }
    }

    return null;
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
  public V put(final K key, final V value) {
    V old;
    DefaultMapEntry<K, V> t;
    DefaultMapEntry<K, V>[] tb;
    int hash, p;

    hash = HashUtils.objectHashCode(key);
    tb = this.m_table;
    p = (hash & (tb.length - 1));

    for (t = tb[p]; t != null; t = t.m_next) {
      if ((t.m_hash == hash) && Utils.testEqual(key, t.m_key)) {
        old = t.m_value;
        t.m_value = value;
        return old;
      }
    }

    tb[p] = this.createEntry(key, value, tb[p], hash);
    this.rehash(++this.m_count);

    return null;
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
  public boolean putIfNew(final K key, final V value) {
    DefaultMapEntry<K, V> t;
    DefaultMapEntry<K, V>[] tb;
    int hash, p;

    hash = HashUtils.objectHashCode(key);
    tb = this.m_table;
    p = (hash & (tb.length - 1));

    for (t = tb[p]; t != null; t = t.m_next) {
      if ((t.m_hash == hash) && Utils.testEqual(key, t.m_key)) {
        return false;
      }
    }

    tb[p] = this.createEntry(key, value, tb[p], hash);
    this.rehash(++this.m_count);

    return true;
  }

  /**
   * Rehash this hash map.
   * 
   * @param minSize
   *          The minimum size needed for our hash.
   */
  @SuppressWarnings("unchecked")
  private final void rehash(final int minSize) {
    int s, i, j;
    DefaultMapEntry<K, V>[] tb, nb;
    DefaultMapEntry<K, V> e, n;

    tb = this.m_table;
    s = tb.length;
    i = (minSize * 3);

    if ((s <<= 1) > i)
      return;

    while (s <= i)
      s <<= 1;

    nb = new DefaultMapEntry[s];
    s--;
    for (i = (tb.length - 1); i >= 0; i--) {
      for (e = tb[i]; e != null; e = n) {
        n = e.m_next;
        j = (e.m_hash & s);
        e.m_next = nb[j];
        nb[j] = e;
      }
    }

    this.m_table = nb;
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
  public void putAll(final Map<? extends K, ? extends V> map) {
    K key;
    V value;
    DefaultMapEntry<K, V> t;
    DefaultMapEntry<K, V>[] tb;
    int hash, p, c;

    this.rehash(map.size());

    tb = this.m_table;
    c = this.m_count;

    for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
      key = entry.getKey();
      value = entry.getValue();
      hash = HashUtils.objectHashCode(key);
      p = (hash & (tb.length - 1));

      place: {
        for (t = tb[p]; t != null; t = t.m_next) {
          if ((t.m_hash == hash) && Utils.testEqual(key, t.m_key)) {
            t.m_value = value;
            break place;
          }
        }

        tb[p] = this.createEntry(key, value, tb[p], hash);
        c++;
      }
    }

    this.rehash(this.m_count = c);
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
  public Entry<K, V> removeEntry(final Object key) {
    int hash, p;
    DefaultMapEntry<K, V> t, t0;

    hash = HashUtils.objectHashCode(key);
    t0 = null;

    for (t = this.m_table[p = (hash & (this.m_table.length - 1))]; t != null; t = t.m_next) {
      if ((t.m_hash == hash) && Utils.testEqual(key, t.m_key)) {
        if (t0 != null)
          t0.m_next = t.m_next;
        else
          this.m_table[p] = t.m_next;
        this.m_count--;
        return t;
      }

      t0 = t;
    }

    return null;
  }

  /**
   * Removes the mapping for this key from this map if present.
   * 
   * @param key
   *          key whose mapping is to be removed from the map.
   * @return previous value associated with specified key, or <tt>null</tt>
   *         if there was no mapping for key. A <tt>null</tt> return can
   *         also indicate that the map previously associated <tt>null</tt>
   *         with the specified key.
   */
  @Override
  public V remove(final Object key) {
    Entry<K, V> t;

    t = this.removeEntry(key);
    return ((t != null) ? (t.getValue()) : null);
  }

  /**
   * Removes all mappings from this map.
   */
  @Override
  public void clear() {
    this.m_count = 0;
    Arrays.fill(this.m_table, null);
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
  public boolean containsValue(final Object value) {
    DefaultMapEntry<K, V> t;
    DefaultMapEntry<K, V>[] tb;
    int i;

    tb = this.m_table;
    i = tb.length;

    if (value == null) {
      for (--i; i >= 0; i--) {
        for (t = tb[i]; t != null; t = t.m_next) {
          if (t.m_value == null)
            return true;
        }
      }

      return false;
    }

    for (--i; i >= 0; i--) {
      for (t = tb[i]; t != null; t = t.m_next) {
        if (value.equals(t.m_value))
          return true;
      }
    }

    return false;
  }

  /**
   * Returns an iterator over a set of elements of type T.
   * 
   * @return an Iterator.
   */
  @Override
  public Iterator<Map.Entry<K, V>> iterator() {
    return new DefaultMapIterator<K, V>(this);
  }

  /**
   * Create a perfect copy of this map.
   * 
   * @return A perfect copy of this map.
   */
  @Override
  @SuppressWarnings("unchecked")
  public Object clone() {
    DefaultMap<K, V> t;
    DefaultMapEntry<K, V> e, e2, eo;
    DefaultMapEntry<K, V>[] tb;
    int i;

    t = (DefaultMap<K, V>) (super.clone());

    tb = t.m_table.clone();

    for (i = (tb.length - 1); i >= 0; i--) {
      eo = null;

      for (e = tb[i]; e != null; e = e2) {
        e2 = e.m_next;
        e = (DefaultMapEntry<K, V>) (e.clone());

        if (eo != null)
          eo.m_next = e;
        else
          tb[i] = e;

        eo = e;
      }
    }

    return t;
  }

  /**
   * Save the state of the <tt>DefaultMap</tt> instance to a stream
   * (i.e., serialize it).
   * 
   * @param s
   *          The destination output stream.
   * @throws IOException
   *           If something goes wrong concerning io.
   * @serialData The <i>capacity</i> of the DefaultMap (the length of the
   *             bucket array) is emitted (int), followed by the <i>size</i>
   *             of the DefaultMap (the number of key-value mappings),
   *             followed by the key (Object) and value (Object) for each
   *             key-value mapping represented by the DefaultMap The
   *             key-value mappings are emitted in the order that they are
   *             returned by <tt>entrySet().iterator()</tt>.
   */
  private synchronized final void writeObject(
      final java.io.ObjectOutputStream s) throws IOException {
    DefaultMapEntry<K, V> t;
    DefaultMapEntry<K, V>[] tb;
    int i;

    s.defaultWriteObject();

    tb = this.m_table;
    i = tb.length;

    s.writeInt(this.m_count);

    for (--i; i >= 0; i--) {
      for (t = tb[i]; t != null; t = t.m_next) {
        s.writeObject(t.m_key);
        s.writeObject(t.m_value);
      }
    }
  }

  /**
   * Reconstitute the <tt>DefaultMap</tt> instance from a stream (i.e.,
   * deserialize it).
   * 
   * @param s
   *          The stream source.
   * @throws IOException
   *           If something goes wrong concerning io.
   * @throws ClassNotFoundException
   *           If a class could not be found.
   */
  @SuppressWarnings("unchecked")
  private final void readObject(java.io.ObjectInputStream s)
      throws IOException, ClassNotFoundException {
    DefaultMapEntry<K, V>[] tb;
    int i, x, h;
    K k;
    V v;

    s.defaultReadObject();

    this.m_count = i = s.readInt();

    x = 16;
    while (x <= i)
      x <<= 1;
    this.m_table = tb = new DefaultMapEntry[x];
    x--;

    for (; i > 0; i--) {
      k = (K) (s.readObject());
      v = (V) (s.readObject());
      h = HashUtils.objectHashCode(k);

      tb[h & x] = this.createEntry(k, v, tb[h & x], h);
    }
  }
}

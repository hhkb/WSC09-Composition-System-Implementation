/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.maps.DefaultMapEntry.java
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
import java.util.Map.Entry;

import org.sfc.utils.ICloneable;

/**
 * An entry of the hash map.
 * 
 * @param <K>
 *          The type of the map's keys.
 * @param <V>
 *          The type of the map's values.
 * @author Thomas Weise
 */
class DefaultMapEntry<K, V> implements Entry<K, V>, Serializable,
    ICloneable {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The key.
   */
  K m_key;

  /**
   * The value.
   */
  V m_value;

  /**
   * The hash code.
   */
  final int m_hash;

  /**
   * The next entry in the entry chain.
   */
  transient DefaultMapEntry<K, V> m_next;

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
  DefaultMapEntry(final K key, final V value,
      final DefaultMapEntry<K, V> next, final int hash) {
    super();
    this.m_key = key;
    this.m_value = value;
    this.m_next = next;
    this.m_hash = hash;
  }

  /**
   * Returns the key corresponding to this entry.
   * 
   * @return the key corresponding to this entry.
   * @throws IllegalStateException
   *           implementations may, but are not required to, throw this
   *           exception if the entry has been removed from the backing map
   */
  public K getKey() {
    return this.m_key;
  }

  /**
   * Returns the value corresponding to this entry. If the mapping has been
   * removed from the backing map (by the iterator's <tt>remove</tt>
   * operation), the results of this call are undefined.
   * 
   * @return the value corresponding to this entry.
   * @throws IllegalStateException
   *           implementations may, but are not required to, throw this
   *           exception if the entry has been removed from the backing map
   */
  public V getValue() {
    return this.m_value;
  }

  /**
   * Replaces the value corresponding to this entry with the specified
   * value (optional operation). (Writes through to the map.) The behavior
   * of this call is undefined if the mapping has already been removed from
   * the map (by the iterator's <tt>remove</tt> operation).
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
   *           exception if the entry has been removed from the backing map
   */
  public V setValue(final V value) {
    V t;

    t = this.m_value;
    this.m_value = value;

    return t;
  }

  /**
   * Compares the specified object with this entry for equality. Returns
   * <tt>true</tt> if the given object is also a map entry and the two
   * entries represent the same mapping. More formally, two entries
   * <tt>e1</tt> and <tt>e2</tt> represent the same mapping if
   * 
   * <pre>
   * (e1.getKey() == null ? e2.getKey() == null : e1.getKey().equals(
   *     e2.getKey()))
   *     &amp;&amp; (e1.getValue() == null ? e2.getValue() == null : e1.getValue()
   *         .equals(e2.getValue()))
   * </pre>
   * 
   * This ensures that the <tt>equals</tt> method works properly across
   * different implementations of the <tt>Map.Entry</tt> interface.
   * 
   * @param o
   *          object to be compared for equality with this map entry.
   * @return <tt>true</tt> if the specified object is equal to this map
   *         entry.
   */
  @Override
  public boolean equals(final Object o) {
    Entry<?, ?> e;
    Object a;

    if (o == this)
      return true;

    if (o instanceof Entry) {
      e = ((Entry<?, ?>) o);

      a = e.getKey();
      if (a == null) {
        if (this.m_key != null)
          return false;
      } else if (a.equals(this.m_key)) {
        a = e.getValue();
        if (a == null) {
          return (this.m_value != null);
        }
        return a.equals(this.m_value);
      }
    }

    return false;
  }

  /**
   * Returns the hash code value for this map entry. The hash code of a map
   * entry <tt>e</tt> is defined to be:
   * 
   * <pre>
   * (e.getKey() == null ? 0 : e.getKey().hashCode())
   *     &circ; (e.getValue() == null ? 0 : e.getValue().hashCode())
   * </pre>
   * 
   * This ensures that <tt>e1.equals(e2)</tt> implies that
   * <tt>e1.hashCode()==e2.hashCode()</tt> for any two Entries
   * <tt>e1</tt> and <tt>e2</tt>, as required by the general contract
   * of <tt>Object.hashCode</tt>.
   * 
   * @return the hash code value for this map entry.
   * @see Object#hashCode()
   * @see Object#equals(Object)
   * @see #equals(Object)
   */
  @Override
  public int hashCode() {
    return (((this.m_key == null) ? 0 : this.m_key.hashCode()) ^ ((this.m_value == null) ? 0
        : this.m_value.hashCode()));
  }

  /**
   * Create a perfect copy of this map entry.
   * 
   * @return A perfect copy of this map entry.
   */
  @Override
  public Object clone() {
    DefaultMapEntry<?, ?> e;

    try {
      e = ((DefaultMapEntry<?, ?>) (super.clone()));
      e.m_next = null;
      return e;
    } catch (Throwable t) {
      return this;
    }
  }
}

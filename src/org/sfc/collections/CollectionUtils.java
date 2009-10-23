/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.CollectionUtils.java
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

package org.sfc.collections;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.sfc.collections.iterators.IteratorBase;
import org.sfc.collections.lists.DefaultList;
import org.sfc.collections.maps.DefaultMap;
import org.sfc.utils.ErrorUtils;

/**
 * This helper class is used to create and manage collections.
 * 
 * @author Thomas Weise
 */
public final class CollectionUtils {

  /**
   * The the empty list.
   */
  public static final List<?> EMPTY_LIST = java.util.Collections.EMPTY_LIST;

  /**
   * The the empty set.
   */
  public static final Set<?> EMPTY_SET = java.util.Collections.EMPTY_SET;

  /**
   * The the empty map.
   */
  public static final Map<?, ?> EMPTY_MAP = java.util.Collections.EMPTY_MAP;

  /**
   * The internal instance of the empty iterator.
   */
  public static final IteratorBase<?> EMPTY_ITERATOR = new EI();

  /**
   * The object array constructor
   */
  public static final IArrayConstructor<?> OBJECT_ARRAY_CONSTRUCTOR = new IArrayConstructor<Object>() {
    public static final long serialVersionUID = 1;

    public Object[] createArray(final int length) {
      return new Object[length];
    }

    private final Object readResolve() {
      return OBJECT_ARRAY_CONSTRUCTOR;
    }

    private final Object writeReplace() {
      return OBJECT_ARRAY_CONSTRUCTOR;
    }
  };

  /**
   * Create a new list.
   * 
   * @param <T>
   *          The list item type.
   * @return The new list.
   */
  public static final <T> List<T> createList() {
    return new DefaultList<T>();
    // new ArrayList<T>();
  }

  /**
   * Create a new list.
   * 
   * @param size
   *          The preallocated list size.
   * @param <T>
   *          The list item type.
   * @return The new list.
   */
  public static final <T> List<T> createList(final int size) {
    return new DefaultList<T>(size);
  }

  /**
   * Create a new set.
   * 
   * @param <T>
   *          the element type
   * @return the new set
   */
  public static final <T> Set<T> createSet() {
    return new HashSet<T>();
  }

  /**
   * Create a new hash map.
   * 
   * @param <K>
   *          The key type.
   * @param <V>
   *          The value type.
   * @return The new list.
   */
  public static final <K, V> Map<K, V> createMap() {
    return new DefaultMap<K, V>(-1);
  }

  /**
   * Instances of this class cannot be constructed.
   */
  private CollectionUtils() {
    ErrorUtils.doNotCall();
  }

  /**
   * The hidden empty iterator class.
   */
  private static final class EI extends IteratorBase<Object> implements
      Serializable {
    /**
     * the serial version uid
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create the empty iterator.
     */
    EI() {
      super();
    }

    /**
     * Returns <tt>false</tt>.
     * 
     * @return <tt>false</tt>
     */
    public final boolean hasNext() {
      return false;
    }

    /**
     * Throws a NoSuchElementException.
     * 
     * @return never
     * @exception NoSuchElementException
     *              iteration has no more elements.
     */
    public final Object next() {
      throw new NoSuchElementException();
    }

    /**
     * Resolve this comparator at deserialization.
     * 
     * @return The right comparator instance.
     */
    private final Object readResolve() {
      return EMPTY_ITERATOR;
    }
  }

}

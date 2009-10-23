/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.stoch.IValueExtractor.java
 * Last modification: 2006-11-28
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

package org.sigoa.refimpl.stoch;

/**
 * This interface allows us to extract values from any kind of
 * random-access source.
 *
 * @param <CollectionType>
 *          The collection type to use the concrete value extractor
 *          implementation for.
 * @author Thomas Weise
 */
public interface IValueExtractor<CollectionType> {

  /**
   * Obtain a double value from the item at position <code>p_index</code>
   * in the collection.
   *
   * @param collection
   *          The collection object to obtain the value from.
   * @param index
   *          The index of the item to return the value of.
   * @return The <code>double</code> representation of the item at this
   *         position.
   */
  public abstract double getValue(final CollectionType collection,
      final int index);
}

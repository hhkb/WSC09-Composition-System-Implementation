/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.iterators.IteratorBase.java
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

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class should be used as base class for all iterators.
 * 
 * @param <T>
 *          The type of items to iterate over.
 * @author Thomas Weise
 */
public abstract class IteratorBase<T> implements Iterator<T>,
    Enumeration<T>, Serializable, ICombinatorics {

  /**
   * Tests if this enumeration contains more elements.
   * 
   * @return <code>true</code> if and only if this enumeration object
   *         contains at least one more element to provide;
   *         <code>false</code> otherwise.
   */
  public final boolean hasMoreElements() {
    return hasNext();
  }

  /**
   * Returns the next element of this enumeration if this enumeration
   * object has at least one more element to provide.
   * 
   * @return the next element of this enumeration.
   * @exception NoSuchElementException
   *              if no more elements exist.
   */
  public final T nextElement() {
    return next();
  }

  /**
   * This default implementation throws an exception.
   * 
   * @exception UnsupportedOperationException
   *              if the <tt>remove</tt> operation is not supported by
   *              this Iterator.
   */
  public void remove() {
    throw new UnsupportedOperationException();
  }

  /**
   * Obtain the number of maximum possible combinations
   * 
   * @return the number of maximum possible combinations or <code>-1</code>
   *         if it could not be determined
   */
  public int getCombinationCount() {
    return -1;
  }
}

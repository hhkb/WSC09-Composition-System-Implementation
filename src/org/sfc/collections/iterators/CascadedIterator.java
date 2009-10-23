/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-01-11
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.iterators.CascadedIterator.java
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

import org.sfc.parallel.simulation.IStepable;

/**
 * <p>
 * The cascaded iterator allows you to enumerate something cyclic, giving
 * forward feedback to some other iterator whenever a cycle is completed.
 * </p>
 * <p>
 * The <code>next</code>-method of this iterator type always returns the
 * iterator itself and it is assumed that the iterator somehow makes its
 * state available to the outside world via some specific methods.
 * <p>
 * 
 * @param <T>
 *          The type of items to iterate over.
 * @author Thomas Weise
 */
public abstract class CascadedIterator<T extends CascadedIterator<T>>
    extends IteratorBase<T> implements IStepable {

  /**
   * the next element
   */
  final CascadedIterator<?> m_next;

  /**
   * Create a new cascaded iterator
   * 
   * @param next
   *          the next iterator
   */
  public CascadedIterator(final CascadedIterator<?> next) {
    super();
    this.m_next = next;
  }

  /**
   * Create a new cascaded iterator
   * 
   * @param next
   *          the next iterator
   */
  public CascadedIterator(final NumericIterator next) {
    this((next == null) ? null : (next.m_internal));
  }

  /**
   * Returns <code>true</code> if the iteration has more elements. (In
   * other words, returns <code>true</code> if <code>next</code> would
   * return an element rather than throwing an exception.)
   * 
   * @return Since this iterator type is cyclic, it will always return
   *         <code>true</code> here
   */
  public boolean hasNext() {
    return true;
  }

  /**
   * Returns the next element in the iteration.
   * 
   * @return always this object itself
   */
  @SuppressWarnings("unchecked")
  public T next() {
    step();
    return (T) this;
  }

  /**
   * Perform one iteration step.
   */
  public abstract void step();

  /**
   * This method is called by <code>step</code> when a cycle has been
   * completet and the iterator starts again.
   */
  protected void onReset() {
    if (this.m_next != null)
      this.m_next.step();
  }

}

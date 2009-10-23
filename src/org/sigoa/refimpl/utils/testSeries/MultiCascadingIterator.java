/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-01-11
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.MultiNumericIterator.java
 * Last modification: 2008-01-11
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

package org.sigoa.refimpl.utils.testSeries;

import java.util.NoSuchElementException;

import org.sfc.collections.iterators.ICombinatorics;
import org.sfc.collections.iterators.IteratorBase;
import org.sfc.parallel.simulation.IStepable;

/**
 * The multi-cascading iterator
 * 
 * @author Thomas Weise
 */
public class MultiCascadingIterator extends IteratorBase<Object[]> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the interators
   */
  private final IStepable[] m_iterators;

  /**
   * Create a new parameter iterator.
   * 
   * @param iterators
   *          the stepable iterators; <code>iterators[0]</code> is used
   *          for stepping
   */
  public MultiCascadingIterator(final IStepable[] iterators) {
    super();
    this.m_iterators = iterators;
  }

  /**
   * Returns <tt>true</tt> if the iteration has more elements. (In other
   * words, returns <tt>true</tt> if <tt>next</tt> would return an
   * element rather than throwing an exception.)
   * 
   * @return <tt>true</tt> if the iterator has more elements.
   */
  public final boolean hasNext() {
    return true;
  }

  /**
   * Returns the next element in the iteration. Calling this method
   * repeatedly until the {@link #hasNext()} method returns false will
   * return each element in the underlying collection exactly once.
   * 
   * @return the next element in the iteration.
   * @exception NoSuchElementException
   *              iteration has no more elements.
   */
  public Object[] next() {
    this.m_iterators[0].step();
    return this.m_iterators;
  }

  /**
   * Obtain the number of maximum possible combinations
   * 
   * @return the number of maximum possible combinations or <code>-1</code>
   *         if it could not be determined
   */
  @Override
  public int getCombinationCount() {
    final IStepable t;
    t = this.m_iterators[0];
    if (t instanceof ICombinatorics)
      return ((ICombinatorics) t).getCombinationCount();
    return -1;
  }
}

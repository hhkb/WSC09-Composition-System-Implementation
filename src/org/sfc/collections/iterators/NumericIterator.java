/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.benchmark.populationSize.IntegerIterator.java
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

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.sfc.parallel.simulation.IStepable;
import org.sfc.text.ITextable;

/**
 * the integer iterator which can be used for example for iterating
 * population sizes
 * 
 * @author Thomas Weise
 */
public class NumericIterator extends Number implements Iterator<Number>,
    ITextable, IStepable, ICombinatorics {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the minimum value
   */
  final double m_min;

  /**
   * the maximum value
   */
  final double m_max;

  /**
   * the step size
   */
  final double m_stepSize;

  /**
   * the current value
   */
  double m_current;

  /**
   * the internal cascaded iterator
   */
  final Casc m_internal;

  /**
   * should the values be rounded?
   */
  final boolean m_rounding;

  /**
   * Create a new integer iterator
   * 
   * @param min
   *          the minimum value
   * @param max
   *          the maximum value
   * @param stepSize
   *          the step size
   * @param next
   *          the next iterator
   * @param rounding
   *          should the values be rounded?
   */
  public NumericIterator(final double min, final double max,
      final double stepSize, final boolean rounding,
      final CascadedIterator<?> next) {
    super();

    this.m_min = Math.min(min, max);
    this.m_max = Math.max(min, max);
    this.m_stepSize = stepSize;

    this.m_current = this.m_min
        + (stepSize * (int) (Math.random() * this
            .getLocalCombinationCount()));
    this.m_internal = new Casc(next);
    this.m_rounding = rounding;
  }

  /**
   * Obtain the number of maximum possible combinations
   * 
   * @return the number of maximum possible combinations
   */
  public int getLocalCombinationCount() {
    return (((int) ((this.m_max - this.m_min) / this.m_stepSize)) + 1);
  }

  /**
   * Obtain the number of maximum possible combinations
   * 
   * @return the number of maximum possible combinations
   */
  public int getCombinationCount() {
    return this.m_internal.getCombinationCount();
  }

  /**
   * Create a new integer iterator
   * 
   * @param min
   *          the minimum value
   * @param max
   *          the maximum value
   * @param stepSize
   *          the step size
   * @param next
   *          the next iterator
   * @param rounding
   *          should the values be rounded?
   */
  public NumericIterator(final double min, final double max,
      final double stepSize, final boolean rounding,
      final NumericIterator next) {
    this(min, max, stepSize, rounding, (next != null) ? next.m_internal
        : null);
  }

  /**
   * Create a new integer iterator
   * 
   * @param min
   *          the minimum value
   * @param max
   *          the maximum value
   * @param stepSize
   *          the step size
   * @param rounding
   *          should the values be rounded?
   */
  public NumericIterator(final double min, final double max,
      final double stepSize, final boolean rounding) {
    this(min, max, stepSize, rounding, (CascadedIterator<?>) null);
  }

  /**
   * Returns the value of the specified number as an <code>int</code>.
   * This may involve rounding or truncation.
   * 
   * @return the numeric value represented by this object after conversion
   *         to type <code>int</code>.
   */
  @Override
  public int intValue() {
    return (int) (this.longValue());
  }

  /**
   * Returns the value of the specified number as a <code>long</code>.
   * This may involve rounding or truncation.
   * 
   * @return the numeric value represented by this object after conversion
   *         to type <code>long</code>.
   */
  @Override
  public long longValue() {
    return Math.round(this.m_current);
  }

  /**
   * Returns the value of the specified number as a <code>float</code>.
   * This may involve rounding.
   * 
   * @return the numeric value represented by this object after conversion
   *         to type <code>float</code>.
   */
  @Override
  public float floatValue() {
    return (float) (this.doubleValue());
  }

  /**
   * Returns the value of the specified number as a <code>double</code>.
   * This may involve rounding.
   * 
   * @return the numeric value represented by this object after conversion
   *         to type <code>double</code>.
   */
  @Override
  public double doubleValue() {
    return (this.m_rounding ? Math.rint(this.m_current) : this.m_current);
  }

  /**
   * Returns <tt>true</tt> if the iteration has more elements. (In other
   * words, returns <tt>true</tt> if <tt>next</tt> would return an
   * element rather than throwing an exception.)
   * 
   * @return <tt>true</tt> if the iterator has more elements.
   */
  public boolean hasNext() {
    return true;
  }

  /**
   * Perform a step of the activity, for example a single simulation step
   */
  public void step() {
    this.m_internal.step();
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
  public Number next() {
    this.step();
    return this;
  }

  /**
   * Removes from the underlying collection the last element returned by
   * the iterator (optional operation). This method can be called only once
   * per call to <tt>next</tt>. The behavior of an iterator is
   * unspecified if the underlying collection is modified while the
   * iteration is in progress in any way other than by calling this method.
   * 
   * @exception UnsupportedOperationException
   *              if the <tt>remove</tt> operation is not supported by
   *              this Iterator.
   * @exception IllegalStateException
   *              if the <tt>next</tt> method has not yet been called, or
   *              the <tt>remove</tt> method has already been called
   *              after the last call to the <tt>next</tt> method.
   */
  public void remove() {
    this.m_internal.remove();
  }

  /**
   * Obtain the string representation of this iterator
   * 
   * @return the string representation of this iterator
   */
  @Override
  public String toString() {
    return String.valueOf(this.m_current);
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  public void toStringBuilder(final StringBuilder sb) {
    if (this.m_rounding)
      sb.append(Math.round(this.m_current));
    else
      sb.append(this.m_current);
  }

  /**
   * the internal cascaded iterator class
   * 
   * @author Thomas Weise
   */
  private final class Casc extends CascadedIterator<Casc> {
    /**
     * the serial version uid
     */
    private static final long serialVersionUID = 1;

    /**
     * Create a new cascaded iterator
     * 
     * @param next
     *          the next iterator
     */
    public Casc(final CascadedIterator<?> next) {
      super(next);
    }

    /**
     * Create a new cascaded iterator
     * 
     * @param next
     *          the next iterator
     */
    public Casc(final NumericIterator next) {
      this((next == null) ? null : (next.m_internal));
    }

    /**
     * Obtain the number of maximum possible combinations
     * 
     * @return the number of maximum possible combinations or
     *         <code>-1</code> if it could not be determined
     */
    @Override
    public final int getCombinationCount() {
      int i, j;

      j = NumericIterator.this.getLocalCombinationCount();
      if (this.m_next == null)
        return j;

      i = this.m_next.getCombinationCount();
      if (i < 0)
        return -1;

      // check for overflow
      i = (i * j);
      if (i < j)
        return -1;
      return i;
    }

    /**
     * Perform one iteration step.
     */
    @Override
    public void step() {
      NumericIterator.this.m_current += NumericIterator.this.m_stepSize;
      if (NumericIterator.this.m_current > NumericIterator.this.m_max) {
        NumericIterator.this.m_current = NumericIterator.this.m_min;
        this.onReset();
      }
    }
  }
}

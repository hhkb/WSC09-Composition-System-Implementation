/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.IterativeSelector.java
 * Last modification: 2007-02-28
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

package org.sigoa.refimpl.utils;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.sfc.collections.iterators.IteratorBase;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * An iterative selector allows to iterate over a the sequence in a
 * randomized manner.
 * 
 * @param <T>
 *          the object type
 * @author Thomas Weise
 */
public class IterativeSelector<T> extends Selector<T> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the sessions
   */
  Session m_sessions;

  /**
   * the synchronizer
   */
  final Object m_sync;

  /**
   * Create a new selector.
   * 
   * @param data
   *          the objects to select from
   * @param weights
   *          their weights
   * @throws NullPointerException
   *           if <code>data==null || weights==null</code>
   * @throws IllegalArgumentException
   *           <code>data.length != weights.length || weights.length&lt;=0 </code>
   */
  public IterativeSelector(final T[] data, final double[] weights) {
    this(null, data, weights);
  }

  /**
   * Create a new iterative selector by adding additional selectables to an
   * existing selector.
   * 
   * @param inherit
   *          a selector to inherite from
   * @param data
   *          the objects to select from
   * @param weights
   *          their weights
   * @throws NullPointerException
   *           if <code>data==null || weights==null</code>
   * @throws IllegalArgumentException
   *           <code>data.length != weights.length || weights.length&lt;=0 </code>
   */
  public IterativeSelector(final Selector<T> inherit, final T[] data,
      final double[] weights) {
    super(inherit, data, weights);
    this.m_sync = new Object();
  }

  /**
   * Open a new session. A session allows you to step through the items of
   * this set in a randomized order according to their weight.
   * 
   * @param r
   *          The randomizer to be used for this session.
   * @return The new Session object. You must close it using
   *         <code>close</code> when its no longer needed.
   * @see Session#close()
   */
  public final Session openSession(final IRandomizer r) {
    Session s;

    main: {
      synchronized (this.m_sync) {
        s = this.m_sessions;
        if (s == null)
          break main;
        this.m_sessions = s.m_se;
      }
      s.m_c = this.size();
      Arrays.fill(s.m_done, false);
      s.m_r = r;
      return s;
    }

    return new Session(r);
  }

  /**
   * The session class.
   * 
   * @author Thomas Weise
   */
  public final class Session extends IteratorBase<T> {
    /**
     * the serial version uid
     */
    private static final long serialVersionUID = 1;

    /**
     * The items already visited.
     */
    boolean[] m_done;

    /**
     * The remaining individual count.
     */
    int m_c;

    /**
     * The next session.
     */
    Session m_se;

    /**
     * The randomizer to use.
     */
    IRandomizer m_r;

    /**
     * Create a new session.
     * 
     * @param r
     *          The initial randomizer.
     */
    Session(final IRandomizer r) {
      super();

      this.m_c = IterativeSelector.this.size();
      this.m_done = new boolean[this.m_c];
      this.m_r = r;
    }

    /**
     * Close a randomized session.
     */
    public final void close() {
      IterativeSelector<T> w;

      this.m_r = null;
      w = IterativeSelector.this;
      synchronized (w.m_sync) {
        this.m_se = w.m_sessions;
        w.m_sessions = this;
      }
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In
     * other words, returns <tt>true</tt> if <tt>next</tt> would return
     * an element rather than throwing an exception.)
     * 
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public final boolean hasNext() {
      return (this.m_c > 0);
    }

    /**
     * Obtain the count of remaining items that can be obtained from this
     * session.
     * 
     * @return The count of remaining items that can be obtained from this
     *         session.
     */
    public final int getRemaining() {
      return this.m_c;
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
    public final T next() {
      int c, i;
      double[] d;
      double x;
      boolean[] b;
      IRandomizer r;

      c = this.m_c;
      if (c <= 0)
        throw new NoSuchElementException();

      r = this.m_r;

      b = this.m_done;
      d = IterativeSelector.this.m_weights;
      x = d[d.length - 1];

      do {
        i = Arrays.binarySearch(d, r.nextDouble() * x);
        if (i < 0)
          i = (-i - 1);
      } while (b[i]);

      this.m_c = (c - 1);
      b[i] = true;

      return IterativeSelector.this.get(i);
    }

  }
}

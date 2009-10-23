/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-19
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.Selector.java
 * Last modification: 2006-12-19
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

import java.lang.reflect.Array;
import java.util.Arrays;

import org.sfc.collections.lists.ImmutableArrayList;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The selector helper class choses one object of many with a given
 * relative probability.
 *
 * @param <T>
 *          the object type
 * @author Thomas Weise
 */
public class Selector<T> extends ImmutableArrayList<T> {
  // extends ImplementationBase<G, PP> implements Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the weights.
   */
  final double[] m_weights;

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
  public Selector(final T[] data, final double[] weights) {
    this(null, data, weights);
  }

  /**
   * Create a new selector by adding additional selectables to an existing
   * selector.
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
  @SuppressWarnings("unchecked")
  public Selector(final Selector<T> inherit, final T[] data,
      final double[] weights) {
    super((inherit != null) ? appendT(((T[]) (inherit.toArray())), data)
        : data.clone());

    int l, i;
    double[] w, w2;
    double d, ww;

    if (inherit == null) {
      d = 0d;
      i = 0;
      l = weights.length;
      w = new double[l];
    } else {
      w2 = inherit.m_weights;
      i = w2.length;
      l = (i + weights.length);
      w = new double[l];
      System.arraycopy(w2, 0, w, 0, l);
      d = w[i - 1];
    }

    for (; i < l; i++) {
      ww = weights[i];
      if ((ww < 0.0d) || Double.isInfinite(ww) || Double.isNaN(ww))
        throw new IllegalArgumentException();
      d += ww;
      w[i] = d;
    }

    this.m_weights = w;
  }

  /**
   * append two arrays of T
   *
   * @param t1
   *          the first array
   * @param t2
   *          the second array
   * @param <X>
   *          the type
   * @return the result array
   */
  @SuppressWarnings("unchecked")
  private static final <X> X[] appendT(final X[] t1, final X[] t2) {
    X[] a;
    int c1, c2;
    c1 = t1.length;
    c2 = t2.length;
    a = ((X[]) (Array.newInstance(t2.getClass().getComponentType(), c1
        + c2)));
    System.arraycopy(t1, 0, a, 0, c1);
    System.arraycopy(t2, 0, a, c1, c2);
    return a;
  }

  /**
   * Randomly select an object from the stored ones according to their
   * weight.
   *
   * @param r
   *          the randomizer
   * @return the selected object
   */
  public T select(final IRandomizer r) {
    int i;
    double[] x;
    x = this.m_weights;
    i = Arrays.binarySearch(x, r.nextDouble() * x[x.length - 1]);
    if (i < 0)
      i = (-(i + 1));
    return this.get(i);
  }

  /**
   * Obtain the weights used by this selector.
   *
   * @return the weights used by this selector
   */
  public double[] getWeights() {
    double[] d;
    int i;

    d = this.m_weights.clone();
    for (i = (d.length - 1); i > 0; i--) {
      d[i] -= d[i - 1];
    }

    return d;
  }
}

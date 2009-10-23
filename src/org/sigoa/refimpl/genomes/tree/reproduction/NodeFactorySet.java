/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.tree.reproduction.NodeFactorySet.java
 * Last modification: 2007-02-16
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

package org.sigoa.refimpl.genomes.tree.reproduction;

import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.utils.IterativeSelector;
import org.sigoa.refimpl.utils.Selector;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The node factory set.
 *
 * @author Thomas Weise
 */
public class NodeFactorySet extends IterativeSelector<INodeFactory> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the selector for leaf factories.
   */
  final IterativeSelector<INodeFactory> m_leafFactories;

  /**
   * the selector for node factories.
   */
  final IterativeSelector<INodeFactory> m_nodeFactories;

  /**
   * the root factory
   */
  final INodeFactory m_rootFactory;

  /**
   * Create a new node factory set.
   *
   * @param factories
   *          the factories to select from
   * @param weights
   *          their weights
   * @param rootFactory
   *          the factory that creates the roots, leave <code>null</code>
   *          no special root factory is defined
   * @throws NullPointerException
   *           if <code>data==null || weights==null</code>
   * @throws IllegalArgumentException
   *           <code>data.length != weights.length || weights.length&lt;=0 </code>
   */
  public NodeFactorySet(final INodeFactory[] factories,
      final double[] weights, final INodeFactory rootFactory) {
    super(factories, weights);

    INodeFactory[] x, r;
    double[] d;
    int i, l;

    this.m_rootFactory = rootFactory;

    x = factories.clone();
    l = x.length;
    for (i = (l - 1); i >= 0; i--) {
      if (x[i].getMinChildren() > 0) {
        x[i] = x[--l];
      }
    }

    r = new INodeFactory[l];
    System.arraycopy(x, 0, r, 0, l);
    d = new double[l];
    for (--l; l >= 0; l--) {
      i = factories.length;
      do {
        i--;
      } while (factories[i] != r[l]);
      d[l] = weights[i];
    }
    this.m_leafFactories = new IterativeSelector<INodeFactory>(r, d);

    x = factories.clone();
    l = x.length;
    for (i = (l - 1); i >= 0; i--) {
      if (x[i].getMaxChildren() <= 0) {
        x[i] = x[--l];
      }
    }

    r = new INodeFactory[l];
    System.arraycopy(x, 0, r, 0, l);
    d = new double[l];
    for (--l; l >= 0; l--) {
      i = factories.length;
      do {
        i--;
      } while (factories[i] != r[l]);
      d[l] = weights[i];
    }
    this.m_nodeFactories = new IterativeSelector<INodeFactory>(r, d);
  }

  /**
   * Create a new node factory set.
   *
   * @param inherit
   *          a selector to inherite from
   * @param factories
   *          the factories to select from
   * @param weights
   *          their weights
   * @param rootFactory
   *          the factory that creates the roots, leave <code>null</code>
   *          no special root factory is defined
   * @throws NullPointerException
   *           if <code>data==null || weights==null</code>
   * @throws IllegalArgumentException
   *           <code>data.length != weights.length || weights.length&lt;=0 </code>
   */
  public NodeFactorySet(final Selector<INodeFactory> inherit,
      final INodeFactory[] factories, final double[] weights,
      final INodeFactory rootFactory) {
    this((inherit != null) ? appendT(inherit.toArray(), factories)
        : factories, (inherit != null) ? appendD(inherit.getWeights(),
        weights) : weights,
        (rootFactory == null) ? ((inherit instanceof NodeFactorySet) ? //
        ((NodeFactorySet) inherit).m_rootFactory
            : rootFactory) : rootFactory);
  }

  /**
   * Create a new node factory set.
   *
   * @param inherit
   *          a selector to inherite from
   * @param factories
   *          the factories to select from
   * @param weights
   *          their weights
   * @throws NullPointerException
   *           if <code>data==null || weights==null</code>
   * @throws IllegalArgumentException
   *           <code>data.length != weights.length || weights.length&lt;=0 </code>
   */
  public NodeFactorySet(final NodeFactorySet inherit,
      final INodeFactory[] factories, final double[] weights) {
    this((inherit != null) ? appendT(inherit.toArray(), factories)
        : factories, (inherit != null) ? appendD(inherit.getWeights(),
        weights) : weights, ((inherit != null) ? inherit.getRootFactory()
        : null));
  }

  /**
   * append two arrays of T
   *
   * @param t1
   *          the first array
   * @param t2
   *          the second array
   * @return the result array
   */
  @SuppressWarnings("unchecked")
  private static final INodeFactory[] appendT(final Object[] t1,
      final INodeFactory[] t2) {
    INodeFactory[] a;
    int c1, c2;
    c1 = t1.length;
    c2 = t2.length;
    a = new INodeFactory[c1 + c2];
    System.arraycopy(t1, 0, a, 0, c1);
    System.arraycopy(t2, 0, a, c1, c2);
    return a;
  }

  /**
   * append two double arrays
   *
   * @param d1
   *          the first array
   * @param d2
   *          the second array
   * @return the result array
   */
  private static final double[] appendD(final double[] d1,
      final double[] d2) {
    double d[];
    int a, b;
    a = d1.length;
    b = d2.length;
    d = new double[a + b];
    System.arraycopy(d1, 0, d, 0, a);
    System.arraycopy(d2, 0, d, a, b);
    return d;
  }

  /**
   * Obtain the factory that creates the roots
   *
   * @return the factory that creates the roots, or <code>null</code> no
   *         special root factory is defined
   */
  public INodeFactory getRootFactory() {
    return this.m_rootFactory;
  }

  /**
   * Select a leaf factory.
   *
   * @param random
   *          the randomizer to be used
   * @return the leaf factory selected
   */
  public INodeFactory selectLeafFactory(final IRandomizer random) {
    return this.m_leafFactories.select(random);
  }

  /**
   * Select a node factory for nodes that may have children.
   *
   * @param random
   *          the randomizer to be used
   * @return the leaf factory selected
   */
  public INodeFactory selectInnerNodeFactory(final IRandomizer random) {
    return this.m_nodeFactories.select(random);
  }

  /**
   * Open a new leaf node factory session. A session allows you to step
   * through the items of this set in a randomized order according to their
   * weight.
   *
   * @param r
   *          The randomizer to be used for this session.
   * @return The new Session object. You must close it using
   *         <code>close</code> when its no longer needed.
   * @see Session#close()
   */
  public final Session openLeafSession(final IRandomizer r) {
    return this.m_leafFactories.openSession(r);
  }

  /**
   * Open a new inner node factory session. A session allows you to step
   * through the items of this set in a randomized order according to their
   * weight.
   *
   * @param r
   *          The randomizer to be used for this session.
   * @return The new Session object. You must close it using
   *         <code>close</code> when its no longer needed.
   * @see Session#close()
   */
  public final Session openInnerNodeSession(final IRandomizer r) {
    return this.m_nodeFactories.openSession(r);
  }

  /**
   * Find a suitable node factory
   *
   * @param sel
   *          the selector to select from
   * @param parent
   *          the parent node factory
   * @param pos
   *          the position
   * @param random
   *          the randomizer
   * @return the new node factory
   */
  static final INodeFactory findFactory(
      final IterativeSelector<INodeFactory> sel,
      final INodeFactory parent, final int pos, final IRandomizer random) {
    int q;
    IterativeSelector<INodeFactory>.Session s;
    INodeFactory f;

    if (parent == null)
      return sel.select(random);

    s = sel.openSession(random);

    for (q = s.getRemaining(); q > 0; q--) {
      f = s.next();
      if (parent.isChildAllowed(f.getNodeClass(), pos)) {
        s.close();
        return f;
      }
    }

    s.close();
    return null;
  }
}

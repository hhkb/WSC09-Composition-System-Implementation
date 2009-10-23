/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.tree.reproduction.TreeCreator.java
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
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.refimpl.go.reproduction.Creator;
import org.sigoa.refimpl.stoch.Randomizer;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * This creator can be used to create trees. It applies the ramped-half-
 * and-half scheme.
 * 
 * @author Thomas Weise
 */
public class TreeCreator extends Creator<Node> {

  /**
   * The node factories.
   */
  final NodeFactorySet m_factories;

  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the max depth of the new trees created
   */
  private final int m_maxDepth;

  /**
   * the probability of "full"
   */
  private final double m_fullProb;

  /**
   * the default result
   */
  private final Node m_default;

  /**
   * Instantiate the tree creator.
   * 
   * @param factories
   *          the node factories to be used
   * @param maxDepth
   *          the maximum depth of new trees
   * @param fullProb
   *          the probability of the "full" algorithm. for large maximum
   *          depths, full may result in memory overflow and can be
   *          deactivated by setting this value here to 0
   * @param defaultNode
   *          the default node
   * @throws IllegalArgumentException
   *           if <code>factories==null</code>
   */
  public TreeCreator(final NodeFactorySet factories, final int maxDepth,
      final double fullProb, final Node defaultNode) {
    super();
    if (factories == null)
      throw new IllegalArgumentException();
    this.m_factories = factories;
    this.m_maxDepth = ((2 > maxDepth) ? 2 : maxDepth);
    this.m_fullProb = fullProb;
    this.m_default = defaultNode;
  }

  /**
   * Instantiate the tree creator.
   * 
   * @param factories
   *          the node factories to be used
   * @param defaultNode
   *          the default node
   * @throws IllegalArgumentException
   *           if <code>factories==null</code>
   */
  public TreeCreator(final NodeFactorySet factories, final Node defaultNode) {
    this(factories, 7, 0.25d, (defaultNode != null) ? defaultNode
        : factories.m_rootFactory.create(null, new Randomizer()));
  }

  /**
   * Instantiate the tree creator.
   * 
   * @param factories
   *          the node factories to be used
   * @throws IllegalArgumentException
   *           if <code>factories==null</code>
   */
  public TreeCreator(final NodeFactorySet factories) {
    this(factories, 7, 0.25d, null);
  }

  /**
   * Obtain the depth value for this tree.
   * 
   * @param random
   *          the randomizer to be used
   * @return the depth of the tree
   */
  protected int getTreeDepth(final IRandomizer random) {
    return (2 + random.nextInt(this.m_maxDepth - 1));
  }

  /**
   * Build a new node according to the "full" scheme.
   * 
   * @param parent
   *          the parent node factory
   * @param pos
   *          the position
   * @param remainingDepth
   *          the remaining depth
   * @param random
   *          the randomizer
   * @return the new node
   */
  protected Node buildNodeFull(final INodeFactory parent, final int pos,
      final int remainingDepth, final IRandomizer random) {
    INodeFactory f;
    Node[] n;
    int ma, mi, x;

    if (remainingDepth < 0)
      return null;

    if ((parent != null) || ((f = this.m_factories.m_rootFactory) == null)) {
      if (remainingDepth <= 1) {
        f = NodeFactorySet.findFactory(this.m_factories.m_leafFactories,
            parent, pos, random);
        if (f != null)
          return f.create(null, random);
      }

      f = NodeFactorySet.findFactory(this.m_factories.m_nodeFactories,
          parent, pos, random);
      if (f == null) {
        f = NodeFactorySet.findFactory(this.m_factories.m_leafFactories,
            parent, pos, random);
        if (f != null)
          return f.create(null, random);
        return null;
      }
    }

    mi = f.getMinChildren();
    ma = f.getMaxChildren();
    if (mi <= 0)
      mi = 1;
    if (mi < ma) {
      if (ma > 10) {
        do {
          x = mi + (int) (random.nextExponential(1));
        } while (x > ma);
      } else
        x = random.nextInt(ma - mi + 1);
      mi += x;
    }
    n = new Node[mi];
    for (--mi; mi >= 0; mi--) {
      if ((n[mi] = this.buildNodeFull(f, mi, remainingDepth - 1, random)) == null)
        return null;
    }
    return f.create(n, random);
  }

  /**
   * Build a new node according to the "grow" method.
   * 
   * @param parent
   *          the parent node factory
   * @param pos
   *          the position
   * @param remainingDepth
   *          the remaining depth
   * @param random
   *          the randomizer
   * @return the new node
   */
  protected Node buildNodeGrow(final INodeFactory parent, final int pos,
      final int remainingDepth, final IRandomizer random) {
    INodeFactory f;
    Node[] n;
    int ma, mi, x;

    if (remainingDepth < 0)
      return null;

    if ((parent != null) || ((f = this.m_factories.m_rootFactory) == null)) {
      if (remainingDepth <= 1) {
        f = NodeFactorySet.findFactory(this.m_factories.m_leafFactories,
            parent, pos, random);
        if (f != null)
          return f.create(null, random);
      }

      f = NodeFactorySet
          .findFactory(this.m_factories, parent, pos, random);
      if (f == null) {
        return null;
      }
    }

    mi = f.getMinChildren();
    ma = f.getMaxChildren();

    if (mi < ma) {
      if (ma > 10) {
        do {
          x = mi + (int) (random.nextExponential(1));
        } while (x > ma);
      } else
        x = random.nextInt(ma - mi + 1);
      mi += x;
    }

    if (mi <= 0)
      n = null;
    else {
      n = new Node[mi];
      for (--mi; mi >= 0; mi--) {
        if ((n[mi] = this.buildNodeGrow(f, mi, remainingDepth - 1, random)) == null)
          return null;
      }
    }
    return f.create(n, random);
  }

  /**
   * Create a single new random genotype
   * 
   * @param parent
   *          the parent node factory
   * @param pos
   *          the position
   * @param random
   *          The randomizer to be used.
   * @return The resulting genotype.
   * @throws NullPointerException
   *           if <code>random==null</code>.
   */
  final Node create(final INodeFactory parent, final int pos,
      final IRandomizer random) {
    int d, i;
    boolean b;
    Node n;

    for (i = 1000; i > 0; i--) {
      d = this.getTreeDepth(random);
      b = (random.nextDouble() < this.m_fullProb);

      if (b)
        n = this.buildNodeFull(parent, pos, d, random);
      else
        n = this.buildNodeGrow(parent, pos, d, random);

      if (n == null) {
        if (b)
          n = this.buildNodeGrow(parent, pos, d, random);
        else
          n = this.buildNodeFull(parent, pos, d, random);
      }

      if (n != null)
        return n;
    }
    return this.m_default;
  }

  /**
   * Create a single new random genotype
   * 
   * @param random
   *          The randomizer to be used.
   * @return The resulting genotype.
   * @throws NullPointerException
   *           if <code>random==null</code>.
   */
  public Node create(final IRandomizer random) {
    return this.create(null, 0, random);
  }
}

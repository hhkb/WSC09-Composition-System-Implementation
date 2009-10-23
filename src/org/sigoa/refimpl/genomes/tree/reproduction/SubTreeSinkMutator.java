/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.tree.reproduction.SubTreeSinkMutator.java
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
import org.sigoa.refimpl.genomes.tree.Path;
import org.sigoa.refimpl.go.reproduction.Mutator;
import org.sigoa.refimpl.utils.IterativeSelector;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * A mutator that sinks a sub-tree.
 * 
 * @author Thomas Weise
 */
public class SubTreeSinkMutator extends Mutator<Node> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The node factories.
   */
  private final NodeFactorySet m_factories;

  /**
   * create a new sub tree replace mutator
   * 
   * @param factories
   *          the node factories to be used
   * @throws IllegalArgumentException
   *           if <code>subTreeCreator==null</code>
   */
  public SubTreeSinkMutator(final NodeFactorySet factories) {
    super();
    if (factories == null)
      throw new IllegalArgumentException();
    this.m_factories = factories;
  }

  /**
   * Build a new node according to the "full" scheme.
   * 
   * @param parent
   *          the parent node factory
   * @param pos
   *          the position
   * @param random
   *          the randomizer
   * @return the new node
   */
  protected final Node buildNewNode(final INodeFactory parent,
      final int pos, final IRandomizer random) {
    return this.buildNewNode(parent, pos, random, 10);
  }

  /**
   * Build a new node according to the "full" scheme.
   * 
   * @param parent
   *          the parent node factory
   * @param pos
   *          the position
   * @param random
   *          the randomizer
   * @param rd
   *          the remaining depth
   * @return the new node
   */
  private final Node buildNewNode(final INodeFactory parent,
      final int pos, final IRandomizer random, final int rd) {
    INodeFactory f;
    Node[] n;
    int ma, mi, x;

    if (rd < 0)
      return null;

    f = NodeFactorySet.findFactory(this.m_factories.m_leafFactories,
        parent, pos, random);
    if (f != null)
      return f.create(null, random);

    f = NodeFactorySet.findFactory(this.m_factories.m_nodeFactories,
        parent, pos, random);
    if (f == null) {
      f = NodeFactorySet.findFactory(this.m_factories.m_leafFactories,
          parent, pos, random);
      if (f != null)
        return f.create(null, random);
      return null;
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
      if ((n[mi] = this.buildNewNode(f, mi, random, rd - 1)) == null)
        return null;
    }
    return f.create(n, random);
  }

  /**
   * Perform one single mutation.
   * 
   * @param source
   *          The source genotype.
   * @param random
   *          The randomizer to be used.
   * @return The resulting genotype.
   * @throws NullPointerException
   *           if <code>source==null||random==null</code>.
   */
  @Override
  public Node mutate(final Node source, final IRandomizer random) {
    Path p1;
    Node end, res;
    Node[] n;
    INodeFactory pf, nf;
    int w, l, idx, p, i, j;
    NodeFactorySet nfs;
    IterativeSelector<INodeFactory>.Session ses;
    Class<? extends Node> cn;

    w = source.getWeight();
    if (w <= 1) {
      if (random == null)
        throw new NullPointerException();
      return source;
    }

    p1 = Path.allocate();
    res = null;
    nfs = this.m_factories;

    for (; (w >= 0) && (res == null); w--) {
      do {//
      } while ((end = p1.findRandomPath(source, random)) == source);

      l = p1.size() - 1;
      pf = p1.get(l).getFactory();
      idx = p1.getIndex(l);
      cn = end.getClass();

      ses = nfs.m_nodeFactories.openSession(random);

      inner: for (l = ses.getRemaining(); l > 0; l--) {
        nf = ses.next();
        if (pf.isChildAllowed(nf.getNodeClass(), idx)) {
          p = nf.getMinChildren();
          if (p < nf.getMaxChildren())
            p++;
          if (p <= 0)
            continue inner;
          fp: {
            for (j = p; j >= 0; j--) {
              i = random.nextInt(p);
              if (nf.isChildAllowed(cn, i))
                break fp;
            }
            continue inner;
          }

          j = Math.max(i + 1, nf.getMinChildren());
          n = new Node[j];
          n[i] = end;

          for (--j; j >= 0; j--) {
            if (n[j] == null) {
              if ((n[j] = this.buildNewNode(nf, j, random)) == null)
                continue inner;
            }
          }

          res = p1.replaceEnd(nf.create(n, random));
          break inner;
        }
      }

      ses.close();
    }

    p1.dispose();

    return ((res != null) ? res : source);
  }

}

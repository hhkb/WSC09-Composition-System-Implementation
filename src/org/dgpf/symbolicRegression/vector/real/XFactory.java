/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-05-26
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.symbolicRegression.vector.real.XFactory.java
 * Last modification: 2008-05-26
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

package org.dgpf.symbolicRegression.vector.real;

import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.refimpl.genomes.tree.NodeFactory;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The x-node factory
 * 
 * @author Thomas Weise
 */
public class XFactory extends NodeFactory {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the cache
   */
  private final X[] m_cache;

  /**
   * Create a new x-factory
   * 
   * @param num
   *          the number
   */
  public XFactory(final int num) {
    super(X.class, 0, 0);

    X[] x;
    int i;

    this.m_cache = x = new X[num];

    for (i = (num - 1); i >= 0; i--) {
      x[i] = new X(i, this);
    }
  }

  /**
   * Create a new node using the specified children.
   * 
   * @param children
   *          the children of the node to be, or <code>null</code> if no
   *          children are wanted
   * @param random
   *          the randomizer to be used for the node's creation
   * @return the new node
   */
  public Node create(final Node[] children, final IRandomizer random) {
    return this.m_cache[random.nextInt(this.m_cache.length)];
  }

  /**
   * This method performs one single mutation on the nodes values. It must
   * not change its children or any other the members of <code>Node</code>.
   * 
   * @param source
   *          The source node.
   * @param random
   *          The randomizer to be used.
   * @return The resulting node or the source node if no mutation was
   *         available.
   * @throws NullPointerException
   *           if <code>source==null||random==null</code>.
   */
  @Override
  public Node mutate(final Node source, final IRandomizer random) {
    final X[] x;
    X z;

    x = this.m_cache;
    if (x.length <= 1)
      return source;

    do {
      z = x[random.nextInt(x.length)];
    } while (z == source);

    return z;
  }

}

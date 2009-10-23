/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.tree.Path.java
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

package org.sigoa.refimpl.genomes.tree;

import java.util.Arrays;

import org.sigoa.spec.stoch.IRandomizer;

/**
 * A path to a node.
 *
 * @author Thomas Weise
 */
public final class Path {

  /**
   * the synchronizer
   */
  private static final Object PATH_SYNC = new Object();

  /**
   * the paths
   */
  private static Path s_paths = new Path();

  /**
   * the path.
   */
  private int[] m_path;

  /**
   * the path length.
   */
  private int m_length;

  /**
   * the next path item
   */
  private Path m_next;

  /**
   * the end node of the path
   */
  private Node m_end;

  /**
   * the nodes.
   */
  private Node[] m_nodes;

  /**
   * create a new path
   */
  private Path() {
    super();
    this.m_path = new int[64];
    this.m_nodes = new Node[64];
  }

  /**
   * dispose this path.
   */
  public final void dispose() {
    Arrays.fill(this.m_nodes, 0, this.m_length, null);
    this.m_length = 0;
    this.m_end = null;
    synchronized (PATH_SYNC) {
      this.m_next = s_paths;
      s_paths = this;
    }
  }

  /**
   * Obtain the end node of this path.
   *
   * @return the end node of this path.
   */
  public final Node getEnd() {
    return this.m_end;
  }

  /**
   * Find a random path through the tree denoted by <code>node</code>.
   *
   * @param node
   *          the root of the path to walk along
   * @param random
   *          the randomizer to be used to create the random numbers needed
   * @return the end node of the path, which can be any node in the tree,
   *         not just one of its leafs
   */
  public final Node findRandomPath(final Node node,
      final IRandomizer random) {
    int[] path, y;
    int len, cl, r;
    Node pos, v;
    Node[] children;
    Node[] x1, x2;
    Node p;

    path = this.m_path;
    len = 0;
    pos = node;
    x1 = this.m_nodes;

    for (;;) {
      p = pos;
      children = p.m_children;
      cl = children.length;
      if (cl <= 0)
        break;

      r = random.nextInt(p.m_weight);
      for (--cl; cl >= 0; cl--) {
        v = children[cl];
        r -= v.m_weight;
        if (r < 0) {
          pos = v;
          break;
        }
      }
      if (cl < 0)
        break;
      len++;
      if (len > path.length) {
        y = new int[len << 2];
        System.arraycopy(path, 0, y, 0, len - 1);
        path = y;
        x2 = new Node[len << 2];
        System.arraycopy(x1, 0, x2, 0, len - 1);
        x1 = x2;
      }
      path[len - 1] = cl;
      x1[len - 1] = p;
    }

    this.m_path = path;
    this.m_nodes = x1;
    this.m_length = len;
    return (this.m_end = pos);
  }

  /**
   * Replace or delete the end node of this path with the node
   * <code>t</code>. If the node <code>t</code> is <code>null</code>,
   * the path end is deleted.
   *
   * @param t
   *          the node used to replace the end node, or <code>null</code>
   *          to delete the path end
   * @return the new start node of this path
   */
  public final Node replaceEnd(final Node t) {
    int[] path;
    int len, wd, z;
    Node repl;
    Node[] ch, ch2;
    Node o;
    Node[] x;

    wd = ((t != null) ? t.m_weight : 0) - this.m_end.m_weight;

    repl = t;
    path = this.m_path;
    x = this.m_nodes;
    for (len = (this.m_length - 1); len >= 0; len--) {
      o = x[len].copy();
      ch2 = o.m_children;
      z = path[len];
      if (repl != null) {
        ch = ch2.clone();
        ch[z] = repl;
      } else {
        if (ch2.length <= 1)
          ch = Node.EMPTY;
        else {
          ch = new Node[ch2.length - 1];
          System.arraycopy(ch2, 0, ch, 0, z);
          System.arraycopy(ch2, z + 1, ch, z, ch2.length - z - 1);
        }
      }
      o.m_children =ch;
      o.m_weight += wd;
      repl = o;
    }

    this.m_end = t;
    return repl;
  }

  /**
   * Obtain the start node of this path.
   *
   * @return the start node of this path
   */
  public final Node getStart() {
    return ((this.m_length > 0) ? this.m_nodes[0] : this.m_end);
  }

  /**
   * Allocate a new path instance.
   *
   * @return the new path
   */
  public static final Path allocate() {
    Path p;
    synchronized (PATH_SYNC) {
      p = s_paths;
      if (p == null)
        return new Path();
      s_paths = p.m_next;
      return p;
    }
  }

  /**
   * Insert a new child node into an existing node.
   *
   * @param node
   *          the parent node
   * @param newChild
   *          the new child node
   * @param index
   *          the index to insert at
   * @return the new parent node
   */
  public static final Node insert(final Node node, final Node newChild,
      final int index) {
    Node p;
    Node[] c1, c2;
    int l;

    p = node.copy();
    c1 = p.m_children;
    l = c1.length;
    c2 = new Node[l + 1];
    System.arraycopy(c1, 0, c2, 0, index);
    c2[index] = newChild;
    System.arraycopy(c1, index, c2, index + 1, l - index);
    p.m_children = c2;
    p.m_weight += newChild.m_weight;

    return p;
  }



  /**
   * Returns the count of nodes before the end node.
   *
   * @return the count of nodes before the end node
   */
  public final int size() {
    return this.m_length;
  }

  /**
   * Obtain the node at the specified index in the path.
   *
   * @param index
   *          the index
   * @return the node at this position
   */
  public final Node get(final int index) {
    return this.m_nodes[index];
  }

  /**
   * Obtain the index at the specified index in the path.
   *
   * @param index
   *          the index
   * @return the node index at this position
   */
  public final int getIndex(final int index) {
    return this.m_path[index];
  }
}

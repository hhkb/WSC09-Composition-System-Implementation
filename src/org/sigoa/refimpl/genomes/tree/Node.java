/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.tree.Node.java
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

import java.io.Serializable;

import org.sfc.text.JavaTextable;
import org.sfc.text.TextUtils;
import org.sfc.utils.ErrorUtils;

/**
 * Instances of this node may be inner nodes of a tree.
 * 
 * @author Thomas Weise
 */
public abstract class Node extends JavaTextable implements Serializable,
    Cloneable {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the empty list.
   */
  static final Node[] EMPTY = new Node[0];

  // /**
  // * the new node text
  // */
  // private static final char[] NEW_NODES = ("new " + //$NON-NLS-1$
  // Node.class.getCanonicalName() + '[').toCharArray();
  //
  // /**
  // * the new node text
  // */
  // private static final char[] NEW_NODES2 = new char[] { ']', ' ', '{' };

  //
  // /**
  // * the new node text
  // */
  // private static final char[] NEW_NODES3 = new char[] { '0', ']' };

  /**
   * The child nodes
   */
  Node[] m_children;

  /**
   * the nodes weight.
   */
  int m_weight;

  /**
   * Create a new node.
   * 
   * @param children
   *          the child nodes
   * @param weight
   *          the weight
   */
  private Node(final Node[] children, final int weight) {
    super();
    this.m_children = children;
    this.m_weight = weight;
  }

  /**
   * Create a new node without any children.
   */
  public Node() {
    this(EMPTY, 1);
  }

  /**
   * Create a new node
   * 
   * @param children
   *          the child nodes
   */
  public Node(final Node[] children) {
    this(
        (((children != null) && (children.length > 0)) ? children : EMPTY),
        computeWeight(children) + 1);
  }

  /**
   * compute the weight of children
   * 
   * @param children
   *          the children
   * @return their weight
   */
  private static final int computeWeight(final Node[] children) {
    int i, w;
    if (children == null)
      return 0;
    w = 0;
    for (i = (children.length - 1); i >= 0; i--)
      w += children[i].m_weight;
    return w;
  }

  /**
   * Returns whether this node is terminal or not.
   * 
   * @return <code>true</code> if and only if this node is terminal,
   *         <code>false</code> otherwise
   */
  public boolean isTerminal() {
    return (this.m_children == EMPTY);
  }

  /**
   * Obtain the count of child nodes.
   * 
   * @return the count of child nodes
   */
  public int size() {
    return this.m_children.length;
  }

  /**
   * Obtain the child node at the specified index.
   * 
   * @param index
   *          the index
   * @return the child node at the specified index
   * @throws ArrayIndexOutOfBoundsException
   *           if the index is invalid
   */
  public Node get(final int index) {
    return this.m_children[index];
  }

  /**
   * Write the text of this node to the string builder.
   * 
   * @param sb
   *          the string builder to write to
   */
  protected void textToStringBuilder(final StringBuilder sb) {
    super.toStringBuilder(sb);
  }

  /**
   * Transform this node into its human readable representation.
   * 
   * @param sb
   *          the string builder to write to
   * @param indent
   *          the indent
   */
  protected void childrenToStringBuilder(final StringBuilder sb,
      final int indent) {
    Node[] nodes;
    int i, k;
    Node x;

    nodes = this.m_children;
    if ((nodes != EMPTY) && (nodes != null)) {

      k = (indent + 2);
      // TextUtils.appendSpaces(sb, indent);
      // nodes[0].toStringBuilder(sb, k);
      x = nodes[0];
      x.toStringBuilder(sb, k);// (x instanceof Block) ? indent : k);

      for (i = 1; i < nodes.length; i++) {
        sb.append(TextUtils.LINE_SEPARATOR);
        TextUtils.appendSpaces(sb, k);
        x = nodes[i];
        // nodes[i].toStringBuilder(sb, k);
        x.toStringBuilder(sb, k);
      }

    }
  }

  /**
   * Transform this node into its human readable representation.
   * 
   * @param sb
   *          the string builder to write to
   * @param indent
   *          the indent
   */
  protected void toStringBuilder(final StringBuilder sb, final int indent) {
    Node[] nodes;
    int i;

    i = sb.length();
    this.textToStringBuilder(sb);

    nodes = this.m_children;

    if (nodes != EMPTY) {
      if (sb.length() != i)
        sb.append(' ');
      sb.append('{');
      sb.append(TextUtils.LINE_SEPARATOR);
      TextUtils.appendSpaces(sb, indent + 2);
      this.childrenToStringBuilder(sb, indent);
      sb.append(TextUtils.LINE_SEPARATOR);
      TextUtils.appendSpaces(sb, indent);
      sb.append('}');
    }

  }

  /**
   * Append a node to a string builder.
   * 
   * @param n
   *          the node
   * @param sb
   *          the string builder
   * @param indent
   *          the indent
   */
  protected static final void nodeToStringBuilder(final Node n,
      final StringBuilder sb, final int indent) {
    n.toStringBuilder(sb, indent);
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    this.toStringBuilder(sb, 0);
  }

  /**
   * Create a copy of this node. This method is needed by the reproduction
   * paths.
   * 
   * @return a copy of this node
   */
  final Node copy() {
    try {
      return ((Node) (this.clone()));
    } catch (CloneNotSupportedException cnse) {
      ErrorUtils.onError(cnse);
      return null;
    }
  }

  /**
   * Obtain the factory which deals with nodes of the same type as this
   * node.
   * 
   * @return the factory which deals with nodes of the same type as this
   *         node
   */
  public abstract INodeFactory getFactory();

  /**
   * Check whether this node equals another object.
   * 
   * @param o
   *          the other object
   * @return <code>true</code> if the other object equals this node
   */
  @Override
  public boolean equals(final Object o) {
    Node n;
    Node[] n1, n2;
    int i;

    if (o == null)
      return false;
    if (o == this)
      return true;
    // if (!(o instanceof Node))
    // return false;
    if (o.getClass() != this.getClass())
      return false;
    n = ((Node) o);
    if (n.m_weight != this.m_weight)
      return false;
    n1 = this.m_children;
    n2 = n.m_children;
    if (n1 == n2)
      return true;
    i = n1.length;
    if (i != n2.length)
      return false;
    for (--i; i >= 0; i--) {
      if (!(n1[i].equals(n2[i])))
        return false;
    }
    return true;
  }

  /**
   * Obtain the weight of this node which is the total count of nodes in
   * the sub-tree below it + 1.
   * 
   * @return this nodes weight
   */
  public int getWeight() {
    return this.m_weight;
  }

  /**
   * Get the depth of the subtree with this node as root.
   * 
   * @return the depth of the subtree with this node as root
   */
  public int getDepth() {
    int c, i, j;
    Node[] ch;

    ch = this.m_children;
    if ((c = ch.length) <= 0)
      return 1;

    i = 0;
    for (--c; c >= 0; c--) {
      j = ch[c].getDepth();
      if (j > i)
        i = j;
    }
    return (i + 1);
  }

  /**
   * Serializes the parameters of the constructor of this object.
   * 
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  @Override
  protected void javaParametersToStringBuilder(final StringBuilder sb,
      final int indent) {
    JavaTextable.appendJavaObjects(this.m_children,
        this.m_children.length, true, sb, indent);
    // Node[] ch;
    // int i, j;
    //
    // ch = this.m_children;
    // j = (ch.length - 1);
    // if (j >= 0) {
    // sb.append(TextUtils.LINE_SEPARATOR);
    // TextUtils.appendSpaces(sb, indent);
    // sb.append(NEW_NODES);
    // sb.append(NEW_NODES2);
    //
    // for (i = 0; i <= j; i++) {
    // sb.append(TextUtils.LINE_SEPARATOR);
    // TextUtils.appendSpaces(sb, indent + 2);
    // ch[i].javaToStringBuilder(sb, indent + 4);
    // if (i < j) {
    // sb.append(',');
    // sb.append(' ');
    // }
    // }
    //
    // sb.append(TextUtils.LINE_SEPARATOR);
    // TextUtils.appendSpaces(sb, indent);
    // sb.append('}');
    // } else {
    // sb.append((Object) null);
    // // sb.append(NEW_NODES);
    // // sb.append(NEW_NODES3);
    // }

  }
}

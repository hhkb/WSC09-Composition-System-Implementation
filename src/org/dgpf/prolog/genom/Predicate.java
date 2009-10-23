/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-02-19
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.prolog.genom.Program.java
 * Last modification: 2008-02-19
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

package org.dgpf.prolog.genom;

import org.sfc.math.Mathematics;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * The predicate class
 * 
 * @author Thomas Weise
 */
public class Predicate extends Node {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the predicate index
   */
  private final int m_index;

  /**
   * Create a new program without any children.
   * 
   * @param index
   *          the index
   */
  public Predicate(final int index) {
    this(null, index);
  }

  /**
   * Create a new program
   * 
   * @param children
   *          the child nodes
   * @param index
   *          the index
   */
  public Predicate(final Node[] children, final int index) {
    super(children);
    this.m_index = index;
  }

  /**
   * Obtain the factory which deals with nodes of the same type as this
   * node.
   * 
   * @return the factory which deals with nodes of the same type as this
   *         node
   */
  @Override
  public INodeFactory getFactory() {
    return PredicateFactory.PREDICATE_FACTORIES[this.size()];
  }

  /**
   * Write the text of this node to the string builder.
   * 
   * @param sb
   *          the string builder to write to
   */
  @Override
  protected void textToStringBuilder(final StringBuilder sb) {
    String[] s;
    s = Predicates.PREDICATES[this.size()];
    sb.append(s[Mathematics.modulo(this.m_index, s.length)]);
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.hlgp.base.Function.java
 * Last modification: 2007-03-01
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

package org.dgpf.hlgp.base;


import org.dgpf.hlgp.base.constructs.instructions.Block;
import org.dgpf.lgp.base.LGPProgram;
import org.sfc.text.TextUtils;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * The base class for function
 *
 * @author Thomas Weise
 */
public final class Function extends Block {
  /**
   * the function text 1
   */
  private static final char[] TEXT1 = (" {" + TextUtils.LINE_SEPARATOR).toCharArray(); //$NON-NLS-1$
  /**
   * the function text 2
   */
  private static final char[] TEXT2 = (TextUtils.LINE_SEPARATOR+"  }").toCharArray(); //$NON-NLS-1$


  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new node
   *
   * @param children
   *          the child nodes
   */
  public Function(final Node[] children) {
    super(children);
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
    return FunctionFactory.FUNCTION_FACTORY;
  }


    /**
   * Transform this program into its human readable representation.
   *
   * @param sb
   *          the string builder to write to
   * @param indent
   *          the indent
   */
  @Override
  protected void toStringBuilder(final StringBuilder sb,
      final int indent) {
    this.doToStringBuilder(sb, 0);
  }

  /**
   * Write this program's human readable representation to a string
   * builder.
   *
   * @param add
   *          the string builder to write to
   *          @param index the function index
   */
  final void doToStringBuilder(final StringBuilder add, final int index) {
    LGPProgram.appendFunctionName(index, add);
    add.append(TEXT1);
    TextUtils.appendSpaces(add, 2);
    this.childrenToStringBuilder(add, 2);
    add.append(TEXT2);
  }
}

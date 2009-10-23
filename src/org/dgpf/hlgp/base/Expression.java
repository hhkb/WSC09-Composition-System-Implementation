/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.hlgp.base.Expression.java
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

import org.dgpf.hlgp.base.compiler.HLGPCompiler;
import org.dgpf.lgp.base.programBuilder.Variable;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * The base class for expressions
 * 
 * @author Thomas Weise
 */
public abstract class Expression extends Construct {
  /**
   * Create a new expression
   * 
   * @param children
   *          the child nodes
   */
  public Expression(final Node[] children) {
    super(children);
  }

  /**
   * An expression may be able to provide a result variable.
   * 
   * @param c
   *          The compiler
   * @return the result variable or<code>null</code> if none is known
   */
  public Variable provideResultVariable(final HLGPCompiler c) {
    return null;
  }
}

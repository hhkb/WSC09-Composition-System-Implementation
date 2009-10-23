/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-28
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.single.Variable.java
 * Last modification: 2007-05-28
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

package org.dgpf.rbgp.base;

/**
 * The variable symbol.
 * 
 * @author Thomas Weise
 */
public class Variable extends NumberedSymbol {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new variable.
   * 
   * @param symbolSet
   *          The symbol set
   * @param index
   *          the index of the variable
   */
  public Variable(final SymbolSet symbolSet, final int index) {
    super(symbolSet, (index < 26) ? // 
    Character.toString((char) ('a' + index))
        : ("var[" + (index + 1) + "]"),//$NON-NLS-1$//$NON-NLS-2$        
        index, 0, false, true, "getVariable(");//$NON-NLS-1$
  }

}

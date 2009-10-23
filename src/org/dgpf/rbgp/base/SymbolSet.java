/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.base.SymbolSet.java
 * Last modification: 2007-05-27
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

import java.io.Serializable;

import org.dgpf.utils.FixedSet;

/**
 * A set of symbols.
 * 
 * @author Thomas Weise
 */
public class SymbolSet extends FixedSet<Symbol> implements Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new symbol set
   */
  public SymbolSet() {
    super();
  }

  /**
   * Create the internal array
   * 
   * @param len
   *          the length of the wanted array
   * @return the array
   */
  @Override
  @SuppressWarnings("unchecked")
  protected final Symbol[] createArray(final int len) {
    return new Symbol[len];
  }

  /**
   * obtain a variable
   * 
   * @param index
   *          the index of the wanted variable
   * @return the variable, or <code>null</code> if none exists at that
   *         index
   */
  public final Variable getVariable(final int index) {
    // int i, j,k;
    //
    //    
    // k= this.size();
    // j = index;
    // for (i = 0; i < k; i++) {
    // if (this.get(i) instanceof Variable) {
    // if ((j--) <= 0)
    // return ((Variable<?>) (this.get(i)));
    // }
    // }
    //
    // return null;
    return this.getInstance(Variable.class, index);
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.single.DefaultSymbolSet.java
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

/**
 * A default symbol set to inherit from
 * 
 * @author Thomas Weise
 */
public class DefaultSymbolSet extends SymbolSet {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared default symbol set
   */
  public static final DefaultSymbolSet DEFAULT_SYMBOL_SET = new DefaultSymbolSet();

  /**
   * the zero symbol
   */
  private final Symbol m_zeroSymbol;

  /**
   * the one symbol
   */
  private final Symbol m_oneSymbol;

  /**
   * the start symbol
   */
  private final Symbol m_startSymbol;

  /**
   * Create a new default symbol set
   */
  protected DefaultSymbolSet() {
    this(0, true);
  }

  /**
   * Create a new default symbol set
   * 
   * @param vc
   *          the number of variables to create
   * @param needsZO
   *          <code>true</code> if and only if the zero and one symbols
   *          are needed
   */
  public DefaultSymbolSet(final int vc, final boolean needsZO) {
    super();
    int i;

    for (i = 0; i < vc; i++) {
      new Variable(this, i);
    }

    if (needsZO) {
      this.m_zeroSymbol = new Symbol(this, "0", 0, true, true); //$NON-NLS-1$ symbol 0
      this.m_oneSymbol = new Symbol(this, "1", 1, true, true);//$NON-NLS-1$ symbol 1
    } else {
      this.m_zeroSymbol = null;
      this.m_oneSymbol = null;
    }

    this.m_startSymbol = new Symbol(this, "start", 1, false, false);//$NON-NLS-1$ symbol start
  }

  /**
   * Obtain the zero symbol.
   * 
   * @return the zero symbol
   */
  public final Symbol getZeroSymbol() {
    return this.m_zeroSymbol;
  }

  /**
   * Obtain the one symbol.
   * 
   * @return the one symbol
   */
  public final Symbol getOneSymbol() {
    return this.m_oneSymbol;
  }

  /**
   * Obtain the start symbol.
   * 
   * @return the start symbol
   */
  public final Symbol getStartSymbol() {
    return this.m_startSymbol;
  }

}

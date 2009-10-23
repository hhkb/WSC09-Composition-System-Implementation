/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-13
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.rbgp.CSSymbolSet.java
 * Last modification: 2008-03-13
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

package test.org.dgpf.cs.rbgp;

import org.dgpf.rbgp.base.Symbol;
import org.dgpf.rbgp.net.DefaultNetSymbolSet;
import org.dgpf.rbgp.net.symbols.NetSizeSymbol;

/**
 * The critical section symbol set
 * 
 * @author Thomas Weise
 */
public class CSSymbolSet extends DefaultNetSymbolSet {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the default symbol set
   */
  public static final CSSymbolSet DEFAULT_CS_SYMBOL_SET = new CSSymbolSet(
      true);

  /**
   * the leave symbol
   */
  private final Symbol m_leave;

  /**
   * the net size symbol
   */
  private final Symbol m_size;

  /**
   * Create a new cs symbol set
   * 
   * @param needsZO
   *          <code>true</code> if and only if the zero and one symbols
   *          are needed
   */
  public CSSymbolSet(final boolean needsZO) {
    super(5, needsZO, 2);
    this.m_leave = new LeaveCSSymbol(this);
    this.m_size = new NetSizeSymbol(this);
  }

  /**
   * Obtain the leave symbol
   * 
   * @return the leave symbol
   */
  public final Symbol getLeaveCSSymbol() {
    return this.m_leave;
  }

  /**
   * Obtain the size symbol
   * 
   * @return the size symbol
   */
  public final Symbol getNetSizeSymbol() {
    return this.m_size;
  }
}

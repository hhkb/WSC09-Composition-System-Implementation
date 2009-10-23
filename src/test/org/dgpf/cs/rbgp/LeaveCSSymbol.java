/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-09
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.rbgp.LeaveCSSymbol.java
 * Last modification: 2007-10-09
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

/**
 * The leave cs symbol is only set when a node leaves the cs
 * 
 * @author Thomas Weise
 */
public class LeaveCSSymbol extends Symbol {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new symbol.
   * 
   * @param symbolSet
   *          The symbol set
   */
  public LeaveCSSymbol(final DefaultNetSymbolSet symbolSet) {
    super(symbolSet, "leaveCS", 0, false, false); //$NON-NLS-1$
  }
}

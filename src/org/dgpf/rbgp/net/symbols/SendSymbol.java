/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-08
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.net.SendSymbol.java
 * Last modification: 2007-10-08
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

package org.dgpf.rbgp.net.symbols;

import org.dgpf.rbgp.base.NumberedSymbol;
import org.dgpf.rbgp.net.DefaultNetSymbolSet;

/**
 * The symbol for sending data.
 * 
 * @author Thomas Weise
 */
public class SendSymbol extends NumberedSymbol {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the send name
   */
  private static final String SEND_NAME = "out"; //$NON-NLS-1$

  /**
   * Create a new send symbol.
   * 
   * @param symbolSet
   *          The symbol set
   * @param index
   *          the index of the send symbol
   */
  public SendSymbol(final DefaultNetSymbolSet symbolSet, int index) {
    super(symbolSet,// 
        ((index <= 0) ? SEND_NAME : (SEND_NAME + index)),//
        Math.max(0, index - 1),//
        0, false, false, "getSendSymbol("); //$NON-NLS-1$
  }

}

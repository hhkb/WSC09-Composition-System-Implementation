/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-08
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.net.DefaultNetSymbolSet.java
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

package org.dgpf.rbgp.net;

import org.dgpf.rbgp.base.DefaultSymbolSet;
import org.dgpf.rbgp.base.Symbol;
import org.dgpf.rbgp.net.symbols.IdSymbol;
import org.dgpf.rbgp.net.symbols.ReceiveSymbol;
import org.dgpf.rbgp.net.symbols.SendSymbol;

/**
 * The network symbolset.
 * 
 * @author Thomas Weise
 */
public class DefaultNetSymbolSet extends DefaultSymbolSet {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The default instance of the net symbol set
   */
  public static final DefaultNetSymbolSet DEFAULT_NET_SYMBOL_SET = new DefaultNetSymbolSet(
      2, true, 1);

  /**
   * the send/receive symbol index
   */
  final int m_srIndex;

  /**
   * the number of send/receive symbols
   */
  final int m_srCount;

  /**
   * the symbol signaling incoming messages
   */
  final Symbol m_incomingMsgSymbol;

  /**
   * the id symbol
   */
  private final Symbol m_idSymbol;

  /**
   * Create a new default symbol set
   * 
   * @param vc
   *          the number of variables to create
   * @param needsZO
   *          <code>true</code> if and only if the zero and one symbols
   *          are needed
   * @param sr
   *          the number of send and receive symbols to create
   */
  public DefaultNetSymbolSet(final int vc, final boolean needsZO,
      final int sr) {
    super(vc, needsZO);

    int c;

    this.m_srIndex = this.size();

    if (sr <= 1) {
      new SendSymbol(this, -1);
      new ReceiveSymbol(this, -1);
      this.m_srCount = 1;
    } else {
      this.m_srCount = sr;
      for (c = sr; c > 0; c--) {
        new SendSymbol(this, c);
      }
      for (c = sr; c > 0; c--) {
        new ReceiveSymbol(this, c);
      }
    }

    this.m_incomingMsgSymbol = new Symbol(this, "incomingMsg", //$NON-NLS-1$
        0, true, false);
    this.m_idSymbol = new IdSymbol(this);

  }

  /**
   * Obtain the size of the messages that are sent/received by this symbol
   * set.
   * 
   * @return the size of the messages that are sent/received by this symbol
   *         set
   */
  public final int getMessageSize() {
    return this.m_srCount;
  }

  /**
   * Obtain the send symbol at the given index
   * 
   * @param index
   *          the index of the send symbol
   * @return the send symbol at that index
   */
  public final Symbol getSendSymbol(final int index) {
    return this.get(this.m_srIndex + index);
  }

  /**
   * Obtain the receive symbol at the given index
   * 
   * @param index
   *          the index of the send symbol
   * @return the receive symbol at that index
   */
  @SuppressWarnings("unchecked")
  public final Symbol getReceiveSymbol(final int index) {
    return this.get(this.m_srIndex + this.m_srCount + index);
  }

  /**
   * Obtain the incoming message symbol
   * 
   * @return the incoming message symbol
   */
  public final Symbol getIncomingMsgSymbol() {
    return this.m_incomingMsgSymbol;
  }

  /**
   * Obtain the id symbol
   * 
   * @return the id symbol
   */
  public final Symbol getIdSymbol() {
    return this.m_idSymbol;
  }
}

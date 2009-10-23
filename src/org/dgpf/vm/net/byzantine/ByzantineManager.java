/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-06-02
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.net.byzantine.ByzantineManager.java
 * Last modification: 2008-06-02
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

package org.dgpf.vm.net.byzantine;

import java.io.Serializable;
import java.util.Arrays;

import org.dgpf.vm.net.Message;
import org.dgpf.vm.net.NetVirtualMachine;

/**
 * this class aids with byzantine processing
 * 
 * @param <MT>
 *          the memory type
 * @author Thomas Weise
 */
final class ByzantineManager<MT extends Serializable> implements
    Serializable {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the errors
   */
  private final int[][] m_errors;

  /**
   * the current errors
   */
  private int[] m_err;

  /**
   * the error index
   */
  private int m_errIdx;

  /**
   * the values seen
   */
  private final int[] m_seen;

  /**
   * the values seen
   */
  private final boolean[] m_seenU;

  /**
   * Create a new network.
   * 
   * @param provider
   *          the network provider
   */
  @SuppressWarnings("unchecked")
  protected ByzantineManager(
      final ByzantineIntBroadcastNetworkProvider<MT> provider) {
    super();
    this.m_errors = provider.m_errors;
    this.m_seen = new int[16384];
    this.m_seenU = new boolean[16384];
  }

  /**
   * obtain the next random int
   * 
   * @return the next random int
   */
  private final int nextInt() {
    return this.m_err[this.m_errIdx = ((this.m_errIdx + 1) % this.m_err.length)];
  }

  /**
   * modify the given message
   * 
   * @param message
   *          the message
   * @param chg
   *          should the message be changed
   * @param drop
   *          should the message dropped
   * @return <code>true</code> if the message should be used,
   *         <code>false</code> if message processing stops here
   */
  final boolean modify(final Message<int[]> message, final boolean chg,
      final boolean drop) {
    int j, v, k;
    final int[] d, s;
    final boolean[] b;

    d = message.m_data;
    s = this.m_seen;
    b = this.m_seenU;
    for (j = (message.m_size - 1); j >= 0; j--) {
      v = d[j];
      k = (v & 16383);
      if (b[k] || ((this.nextInt() & 1) != 0)) {
        b[k] = false;
        s[k] = v;
      }
    }

    if (drop) {
      if ((this.nextInt() & 1) == 0) {
        NetVirtualMachine.disposeMessage(message);
        return false;
      }
    }

    if (chg) {
      for (j = (message.m_size - 1); j >= 0; j--) {

        do {

          if ((this.nextInt() & 1) != 0) {

            k = this.nextInt() & 16383;
            for (; b[k]; k = ((k + 1) & 16383)) {
              // 
            }
            v = s[k];
          } else {
            v = ((this.nextInt() & 31) - 15);
            if (v == 0)
              v = -16;
            v += d[j];
          }

        } while (v == d[j]);

        d[j] = v;
      }

    }

    return true;
  }

  /**
   * reset
   * 
   * @param simIdx
   *          the simulation index
   */
  final void reset(final int simIdx) {
    this.m_err = this.m_errors[simIdx];
    this.m_errIdx = 0;
    Arrays.fill(this.m_seenU, true);
  }

}

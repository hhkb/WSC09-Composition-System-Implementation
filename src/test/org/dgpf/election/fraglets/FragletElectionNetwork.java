/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-16
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.fraglets.FragletElectionNetwork.java
 * Last modification: 2007-12-16
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

package test.org.dgpf.election.fraglets;

import org.dgpf.fraglets.base.Fraglet;
import org.dgpf.fraglets.base.FragletProgram;
import org.dgpf.fraglets.base.FragletStore;
import org.dgpf.fraglets.base.IFragletParameters;
import org.dgpf.fraglets.base.IFragletReactionPerformance;
import org.dgpf.fraglets.base.InstructionSet;
import org.dgpf.fraglets.base.InstructionUtils;
import org.dgpf.fraglets.net.FragletNetVM;

import test.org.dgpf.election.ElectionNetwork;
import test.org.dgpf.election.IElectionInformation;

/**
 * the fraglet-based election network
 * 
 * @author Thomas Weise
 */
public class FragletElectionNetwork extends
    ElectionNetwork<FragletStore, FragletProgram, int[]> implements
    IElectionInformation, IFragletReactionPerformance {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the number buffer
   */
  private final int[] m_sels;

  /**
   * the number buffer 2
   */
  private final int[] m_sels2;

  /**
   * Create a new network.
   * 
   * @param provider
   *          the network provider
   */
  public FragletElectionNetwork(final FragletElectionProvider provider) {
    super(provider);

    int i;
    i = provider.getParameters().getMaxVirtualMachines();
    this.m_sels = new int[i];
    this.m_sels2 = new int[i];
  }

  /**
   * Obtain the result of the <code>index</code>th virtual machine.
   * 
   * @param index
   *          the index of the vm
   * @return the result of the <code>index</code>th virtual machine
   */
  @Override
  protected int doGetResult(final int index) {
    FragletStore m;
    int i, r, k, l, ri, s;
    Fraglet f;
    final int[] i1, i2;
    InstructionSet is;

    m = ((FragletNetVM) (this.getVirtualMachine(index))).m_memory;
    is = ((IFragletParameters) (this.m_parameters)).getInstructionSet();
    s = is.size();
    ri = ((ElectionInstructionSet) is).m_idSymbol.m_id;

    i1 = this.m_sels;
    i2 = this.m_sels2;
    r = 0;

    outer: for (i = (m.size() - 1); i >= 0; i--) {
      f = m.peekFraglet(i);

      if (f.m_len >= 2) {
        if (InstructionUtils.isInstruction(f.m_code[0])) {
          if ((InstructionUtils.decodeInstruction(f.m_code[0], s)) == ri) {

            if (!(InstructionUtils.isInstruction(f.m_code[1]))) {

              k = InstructionUtils.decodeData(f.m_code[1]);

              if (!(this.isValidId(k)))
                return -1;

              for (l = (r - 1); l >= 0; l--) {
                if (i1[l] == k) {
                  i2[l] += Math.max(1, f.m_count);
                  continue outer;
                }
              }

              i1[r] = k;
              i2[r] = Math.max(1, f.m_count);
              r++;

              continue outer;
            }

            return -1;
          }
        }
      }
    }

    if ((r <= 0) || (r > 3))
      return -1;

    boolean d;
    k = i1[0];
    l = i2[0];
    d = false;
    s = 0;
    for (--r; r > 0; r--) {
      ri = i2[r];
      s += ri;
      if (ri > l) {
        l = ri;
        k = i1[r];
        d = false;
      } else if (ri == l)
        d = true;
    }

    if ((d) || (s > 10))
      return -1;
    return k;

    // // if(r > 4) return -1;
    //
    // k = i1[0];
    // l = i2[0];
    // ss = l;
    // for (--r; r > 0; r--) {
    // if (i2[r] > l) {
    // l = i2[r];
    // k = i1[r];
    // }
    // ss += i2[r];
    // }
    //
    // if ((ss <= 1) || (l <= (ss >>> 1)))
    // return -1;
    // return k;
  }

  /**
   * Obtain the number of reactions that took place.
   * 
   * @return the number of reactions that took place
   */
  public int getReactionCount() {
    long l;
    int i;

    l = 0l;
    for (i = (this.getVirtualMachineCount() - 1); i >= 0; i--) {
      l += ((IFragletReactionPerformance) (this.getVirtualMachine(i)))
          .getReactionCount();
    }

    return ((int) (Math.min(Integer.MAX_VALUE, l)));
  }
}

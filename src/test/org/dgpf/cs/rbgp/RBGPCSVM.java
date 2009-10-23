/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-11
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.rbgp.RBGPCSVM.java
 * Last modification: 2008-03-11
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

import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.RBGPProgramBase;
import org.dgpf.rbgp.base.Symbol;
import org.dgpf.rbgp.base.SymbolSet;
import org.dgpf.rbgp.base.Variable;
import org.dgpf.rbgp.net.RBGPNetVM;
import org.dgpf.vm.base.EVirtualMachineState;

import test.org.dgpf.cs.CSNetwork;
import test.org.dgpf.cs.ICSVM;

/**
 * A virtual machine driven by rbgp-programs and enabled for mutual
 * exclusion at the critical section
 * 
 * @author Thomas Weise
 */
public class RBGPCSVM extends RBGPNetVM implements ICSVM {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the cs network
   */
  private final CSNetwork<RBGPMemory, RBGPProgramBase, int[]> m_network;

  /**
   * the time to spent in the cs
   */
  private int m_inCS;

  /**
   * how often did we enter the cs?
   */
  private int m_csTimes;

  /**
   * the cs flag
   */
  private boolean m_shouldEnter;

  /**
   * the leave cs symbol
   */
  private final Symbol m_leaveCSSymbol;

  /**
   * Create a new virtual machine with the default symbol set.
   * 
   * @param network
   *          the network
   * @param index
   *          the index
   */
  @SuppressWarnings("unchecked")
  public RBGPCSVM(
      final CSNetwork<RBGPMemory, RBGPProgramBase, int[]> network,
      final int index) {
    super(network, index);

    Symbol s;
    final SymbolSet x;

    this.m_network = network;
    x = this.m_symbols;
    s = x.getInstance(LeaveCSSymbol.class, 0);
    if (s == null)
      s = x.getInstance(Variable.class, 0);
    this.m_leaveCSSymbol = ((s != null) ? s : x.get(x.size() - 1));
  }

  /**
   * Obtain the number of times this vm has been inside the critical
   * section.
   * 
   * @return the number of times this vm has been inside the critical
   *         section
   */
  public final int getCSTimes() {
    return this.m_csTimes;
  }

  /**
   * Set the cs flag
   */
  public final void setCSFlag() {
    this.m_shouldEnter = true;
  }

  /**
   * Initialize this vm
   */
  @Override
  public void beginSimulation() {
    super.beginSimulation();
    this.m_csTimes = 0;
    this.m_inCS = 0;
    this.m_shouldEnter = false;
  }

  /**
   * Perform a single simulation step.
   * 
   * @return a new virtual machine state, see {@link EVirtualMachineState}
   */
  @Override
  protected final EVirtualMachineState doStep() {
    EVirtualMachineState s;
    int i;

    i = (--this.m_inCS);
    if (i == 0) {
      this.m_network.leaveCS();
      this.setValue(this.m_leaveCSSymbol, 1);
    } else if (i < 0) {
      s = super.doStep();
      if (this.m_shouldEnter) {
        this.m_shouldEnter = false;

        if (s == EVirtualMachineState.TERMINATED) {
          return EVirtualMachineState.TERMINATED;
        }

        if (this.m_inCS <= 0) {
          this.m_inCS = this.m_network.enterCS();
          this.m_csTimes++;
        }

        return EVirtualMachineState.CHANGED;
      }
      return s;
    }

    return EVirtualMachineState.CHANGED;
  }
}

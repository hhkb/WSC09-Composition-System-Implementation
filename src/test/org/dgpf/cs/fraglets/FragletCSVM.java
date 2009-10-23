/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-27
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.fraglets.FragletCSVM.java
 * Last modification: 2008-03-27
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

package test.org.dgpf.cs.fraglets;

import org.dgpf.fraglets.base.Fraglet;
import org.dgpf.fraglets.base.FragletProgram;
import org.dgpf.fraglets.base.FragletStore;
import org.dgpf.fraglets.base.InstructionUtils;
import org.dgpf.fraglets.net.FragletNetVM;
import org.dgpf.vm.base.EVirtualMachineState;

import test.org.dgpf.cs.CSNetwork;
import test.org.dgpf.cs.ICSVM;

/**
 * The fraglet based critical section virtual machine.
 * 
 * @author Thomas Weise
 */
public class FragletCSVM extends FragletNetVM implements ICSVM {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the time to spent in the cs
   */
  private int m_inCS;

  /**
   * how often did we enter the cs?
   */
  private int m_csTimes;

  /**
   * the network
   */
  private final CSNetwork<FragletStore, FragletProgram, int[]> m_network;

  /**
   * the leave value.
   */
  private final int m_lv;

  /**
   * Create a new virtual machine with the default symbol set.
   * 
   * @param network
   *          the network
   * @param index
   *          the index
   */
  @SuppressWarnings("unchecked")
  public FragletCSVM(
      final CSNetwork<FragletStore, FragletProgram, int[]> network,
      final int index) {
    super(network, index);
    this.m_network = network;
    this.m_lv = InstructionUtils
        .encodeInstruction(((CSInstructionSet) (this.m_memory.m_instructions)).m_leaveSymbol);
  }

  /**
   * Initialize this vm
   */
  @Override
  public void beginSimulation() {
    super.beginSimulation();
    this.m_inCS = 0;
    this.m_csTimes = 0;
  }

  /**
   * enter the critical section.
   */
  public final void enterCS() {
    if (this.m_inCS < 0) {
      this.m_inCS = this.m_network.enterCS();
      this.m_csTimes++;
    }
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
   * Perform a single simulation step.
   * 
   * @return a new virtual machine state, see {@link EVirtualMachineState}
   */
  @Override
  protected final EVirtualMachineState doStep() {
    Fraglet f;
    int i;

    i = (--this.m_inCS);
    if (i == 0) {
      this.m_network.leaveCS();

      f = this.m_memory.allocate();
      if (f != null) {
        f.m_code[0] = this.m_lv;
        f.m_len = 1;
        this.m_memory.enterFraglet(f, this);
      }
    } else if (i < 0) {
      return super.doStep();
    }

    return EVirtualMachineState.CHANGED;
  }

}
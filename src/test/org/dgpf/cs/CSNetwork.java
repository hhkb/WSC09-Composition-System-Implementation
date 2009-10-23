/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-07
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.CSNetwork.java
 * Last modification: 2008-03-07
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

package test.org.dgpf.cs;

import java.io.Serializable;

import org.dgpf.vm.base.VirtualMachineProgram;
import org.dgpf.vm.net.BroadcastNetwork;

/**
 * The critical section network
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the virtual machine program type
 * @param <MDT>
 *          the message data type
 * @author Thomas Weise
 */
public class CSNetwork<MT extends Serializable, PT extends VirtualMachineProgram<MT>, MDT extends Serializable>
    extends BroadcastNetwork<MT, PT, MDT> {//BroadcastNetwork<MT, PT, MDT> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the number of vms that have entered the critical section
   */
  private int m_inCS;

  /**
   * the number of collisions
   */
  long m_collisions;

  /**
   * the cs lengths
   */
  private final int[][] m_csLens2;

  /**
   * the cs lengths
   */
  private int[] m_csLens;

  /**
   * the cs index
   */
  private int m_csIdx;

  /**
   * <code>true</code> if the critical section was used
   */
  boolean m_cs_used;

  /**
   * Create a new network.
   * 
   * @param provider
   *          the network provider
   */
  @SuppressWarnings("unchecked")
  public CSNetwork(final CSNetworkProvider<MT> provider) {
    super(provider);
    this.m_csLens2 = provider.m_csLengths;
  }

  /**
   * This method is called right before the simulation begins.
   * 
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  @Override
  public final void beginSimulation() {
    super.beginSimulation();

    this.m_csLens = this.m_csLens2[this.getSimulationIndex()];
    this.m_inCS = 0;
    this.m_collisions = 0l;
    this.m_csIdx = 0;
    this.m_cs_used = false;
  }

  /**
   * Perform a single step.
   * 
   * @return <code>true</code> if additional steps are useful,
   *         <code>false</code> otherwise
   */
  @Override
  protected final boolean doStep() {
    boolean b;
    int i;
    b = super.doStep();

    if ((i = this.m_inCS) > 1) {
      this.m_collisions += (i * (i - 1));
    }

    return b;
  }

  /**
   * a vm has entered the cs
   * 
   * @return the number of ticks it will remain in the cs
   */
  public final int enterCS() {
    int[] x;

    this.m_inCS++;
    this.m_cs_used = true;

    x = this.m_csLens;
    return x[this.m_csIdx = ((this.m_csIdx + 1) % x.length)];
  }

  /**
   * Leave the critical section.
   */
  public final void leaveCS() {
    this.m_inCS = Math.max(0, this.m_inCS - 1);
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-16
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.net.FragletNetVM.java
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

package org.dgpf.fraglets.net;

import org.dgpf.fraglets.base.Fraglet;
import org.dgpf.fraglets.base.FragletProgram;
import org.dgpf.fraglets.base.FragletStore;
import org.dgpf.fraglets.base.IFragletParameters;
import org.dgpf.fraglets.base.IFragletReactionPerformance;
import org.dgpf.vm.base.EVirtualMachineState;
import org.dgpf.vm.net.Message;
import org.dgpf.vm.net.NetVirtualMachine;
import org.dgpf.vm.net.Network;

/**
 * The virtual machine for networked fraglets
 * 
 * @author Thomas Weise
 */
public class FragletNetVM extends
    NetVirtualMachine<FragletStore, FragletProgram, int[]> implements
    IFragletReactionPerformance {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the idle steps
   */
  private int m_idleSteps;

  /**
   * Create a new networked hosted virtual machine.
   * 
   * @param network
   *          the network
   * @param index
   *          the index
   */
  public FragletNetVM(
      final Network<FragletStore, FragletProgram, int[]> network,
      final int index) {
    super(network, index);
  }

  /**
   * Create a memory block
   * 
   * @return the vm memory
   */
  @Override
  protected final FragletStore createMemory() {
    return new FragletStore(((IFragletParameters) (this.m_parameters)));
  }

  /**
   * Create a message data record
   * 
   * @return the new message data record
   */
  @Override
  protected int[] createMessageData() {
    return new int[this.m_memory.m_maxLength];
  }

  /**
   * Process an incoming message.
   * 
   * @param message
   *          the message received
   */
  @Override
  protected void handleMessage(final Message<int[]> message) {
    Fraglet f;

    f = this.m_memory.allocate();
    if (f == null)
      this.setError();
    else {
      System.arraycopy(message.m_data, 0, f.m_code, 0,
          f.m_len = message.m_size);
      this.m_memory.enterFraglet(f, this);
    }

    disposeMessage(message);
  }

  /**
   * This method is called right before the simulation begins.
   * 
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  @Override
  public void beginSimulation() {
    super.beginSimulation();
    this.m_memory.initialize(this.m_program, this);
    this.m_memory.m_randomizer.setSeed(this.getId());
    this.m_idleSteps = 0;
  }

  /**
   * Obtain the number of steps where this vm was idle.
   * 
   * @return the number of steps where this vm was idle
   */
  @Override
  public final int getIdleSteps() {

    // <br/> This method
    // * represents a dirty hack: the number of idle steps is normally
    // * maximized in many cases of networking. However, in terms of
    // fraglets,
    // * we may be more interested in maximizing the number of reactions
    // that
    // * took place. So therefore, this method here returns their number
    // * instead of the number of idle steps.
    // return this.getReactionCount();

    return this.m_idleSteps;
  }

  /**
   * Begin the simulation of the specified individual.
   * 
   * @param what
   *          the individual
   * @throws NullPointerException
   *           if <code>what</code> is <code>null</code>.
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  @Override
  public void beginIndividual(final FragletProgram what) {
    super.beginIndividual(what);
    this.m_memory.m_randomizer.setDefaultSeed();
  }

  /**
   * Transmit the given fraglet to all hosts in reach.
   * 
   * @param fraglet
   *          the given fraglet
   */
  public void broadcastFraglet(final Fraglet fraglet) {
    Message<int[]> i;

    i = this.allocateMessage();
    if (i == null)
      return;

    System.arraycopy(fraglet.m_code, 1, i.m_data, 0,
        i.m_size = (fraglet.m_len - 1));

    this.send(i);
  }

  /**
   * Perform a single simulation step.
   * 
   * @return a new virtual machine state, see {@link EVirtualMachineState}
   */
  @Override
  protected EVirtualMachineState doStep() {
    if (this.m_memory.execute(this) == EVirtualMachineState.NOTHING)
      this.m_idleSteps++;

    return EVirtualMachineState.CHANGED;
  }

  /**
   * Obtain the number of reactions that took place.
   * 
   * @return the number of reactions that took place
   */
  public final int getReactionCount() {
    return this.m_memory.getReactionCount();
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-22
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.net.AggregationNetVM.java
 * Last modification: 2007-12-22
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

package org.dgpf.aggregation.net;

import java.util.Arrays;

import org.dgpf.symbolicRegression.ComputationContext;
import org.dgpf.vm.base.EVirtualMachineState;
import org.dgpf.vm.net.Message;
import org.dgpf.vm.net.NetVirtualMachine;
import org.dgpf.vm.net.Network;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * The net virtual machine for aggregation programs.
 * 
 * @author Thomas Weise
 */
public class AggregationNetVM extends
    NetVirtualMachine<double[], AggregationNetProgram, double[]> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * indicate the first computational step
   */
  private boolean m_first;

  /**
   * <code>true</code> if and only if something has changed
   */
  private boolean m_changed;

  /**
   * the context
   */
  private final ComputationContext<AggregationNetProgram> m_context;

  /**
   * Create an new aggregation network virtual machine for the given
   * network.
   * 
   * @param network
   *          the network for the virtul machine
   * @param index
   *          the index
   */
  protected AggregationNetVM(
      final Network<double[], AggregationNetProgram, double[]> network,
      final int index) {
    super(network, index);

    this.m_context = new ComputationContext<AggregationNetProgram>() {
      /**
       * the serial version uid
       */
      private static final long serialVersionUID = 1;

      /**
       * increase the number of erraneous terms
       * 
       * @param node
       *          the node detecting the error
       */
      @Override
      public void setError(final Node node) {
        super.setError(node);
        AggregationNetVM.this.setError();
      }
    };
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
  public void beginIndividual(final AggregationNetProgram what) {
    super.beginIndividual(what);
    this.m_context.beginIndividual(what);
  }

  /**
   * End the individual.
   */
  @Override
  public void endIndividual() {
    this.m_context.endIndividual();
    super.endIndividual();
  }

  /**
   * Create a memory block
   * 
   * @return the vm memory
   */
  @Override
  protected final double[] createMemory() {
    return new double[this.m_parameters.getMemorySize()];
  }

  /**
   * Create a message data record
   * 
   * @return the new message data record
   */
  @Override
  protected final double[] createMessageData() {
    return new double[this.m_memory.length];
  }

  /**
   * Process an incoming message.
   * 
   * @param message
   *          the message received
   */
  @Override
  protected void handleMessage(final Message<double[]> message) {
    final int[] in;
    final double[] mem, msg;
    int i;

    mem = this.m_memory;
    in = this.m_program.m_in;
    msg = message.m_data;

    for (i = 0; i < in.length; i++) {
      mem[in[i]] = msg[i];
    }

    this.m_program.m_root.compute(mem, this.m_context);
    this.m_changed = true;
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
    this.m_context.beginSimulation();
    Arrays.fill(this.m_memory, 0d);
    this.m_first = true;
  }

  /**
   * This method is called when the simulation has ended.
   * 
   * @throws IllegalStateException
   *           If this simulation is not yet running.
   */
  @Override
  public void endSimulation() {
    this.m_context.endSimulation();
    super.endSimulation();
  }

  /**
   * Perform a single simulation step.
   * 
   * @return a new virtual machine state, see {@link EVirtualMachineState}
   */
  @Override
  protected final EVirtualMachineState doStep() {
    final double[] mem, msg;
    final int[] out;
    final Message<double[]> m;
    int i;

    mem = this.m_memory;

    if (this.m_first) {
      this.m_first = false;
      this.m_changed = true;
      this.m_program.m_root.init(mem, this.m_context);
    }

    if (!(this.m_changed))
      return EVirtualMachineState.NOTHING;
    this.m_changed = false;

    m = this.allocateMessage();
    if (m == null)
      return EVirtualMachineState.ERROR;

    msg = m.m_data;
    out = this.m_program.m_out;
    m.m_size = out.length;

    for (i = 0; i < out.length; i++) {
      msg[i] = mem[out[i]];
    }

    this.send(m);
    return EVirtualMachineState.CHANGED;
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-20
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.net.Network.java
 * Last modification: 2007-09-20
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

package org.dgpf.vm.net;

import java.io.Serializable;

import org.dgpf.vm.base.EVirtualMachineState;
import org.dgpf.vm.base.VirtualMachineProgram;
import org.dgpf.vm.net.objectives.INetworkSimulationInformation;
import org.sfc.parallel.simulation.IStepable;
import org.sfc.text.JavaTextable;
import org.sigoa.spec.simulation.ISimulation;

/**
 * The base class for all networks.
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the virtual machine program type
 * @param <MDT>
 *          the message data type
 * @author Thomas Weise
 */
public class Network<MT extends Serializable, PT extends VirtualMachineProgram<MT>, MDT extends Serializable>
    extends JavaTextable implements Serializable, ISimulation<PT>,
    IStepable, IVirtualMachineNetworkParameters<MT>,
    INetworkSimulationInformation {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the virtual machine network parameters
   */
  protected final IVirtualMachineNetworkParameters<MT> m_parameters;

  /**
   * the network provider
   */
  protected final NetworkProvider<MT> m_provider;

  /**
   * the internal array of virtual machines
   */
  final NetVirtualMachine<MT, PT, MDT>[] m_vms;

  /**
   * the internal array of active virtual machines
   */
  private final NetVirtualMachine<MT, PT, MDT>[] m_active;

  /**
   * the number of active vms
   */
  private int m_activeCnt;

  /**
   * the index from where on only terminated vms will be situated
   */
  int m_terminatedIdx;

  /**
   * are we in a simulation right now
   */
  private boolean m_inSim;

  /**
   * the messages
   */
  private Message<MDT> m_messages;

  /**
   * the delays
   */
  private int[] m_delays;

  /**
   * the delay index
   */
  private int m_delayIdx;

  /**
   * the steps array of the provider
   */
  private int[] m_steps;

  /**
   * the current steps index
   */
  private int m_stepsIdx;

  /**
   * the virtual machine count
   */
  int m_vmCount;

  /**
   * the simulation index
   */
  int m_simIndex;

  /**
   * <code>true</code> if and only if the execution was terminated
   * because of an error
   */
  private boolean m_error;

  /**
   * Create a new network.
   * 
   * @param provider
   *          the network provider
   */
  @SuppressWarnings("unchecked")
  protected Network(final NetworkProvider<MT> provider) {
    super();

    int i;
    INetVirtualMachineFactory<MT, PT, MDT> factory;
    NetVirtualMachine<MT, PT, MDT>[] vms;

    this.m_parameters = provider.m_parameters;
    this.m_provider = provider;

    factory = ((INetVirtualMachineFactory) (this.m_parameters
        .getNetVirtualMachineFactory()));
    i = this.m_parameters.getMaxVirtualMachines();

    this.m_active = new NetVirtualMachine[i];
    this.m_vms = vms = new NetVirtualMachine[i];

    for (--i; i >= 0; i--) {
      vms[i] = factory.createNetVirtualMachine(this, i);
    }

  }

  /**
   * Obtain the virtual machine network parameters specifying the behavior
   * of this network.
   * 
   * @return the parameters of this network
   */
  public final IVirtualMachineNetworkParameters<MT> getParameters() {
    return this.m_parameters;
  }

  /**
   * Obtain the network provider of this network.
   * 
   * @return the network provider
   */
  public final NetworkProvider<MT> getProvider() {
    return this.m_provider;
  }

  /**
   * Obtain the number of virtual machines in the current simulation
   * 
   * @return the number of virtual machines in the current simulation
   */
  public final int getVirtualMachineCount() {
    return this.m_vmCount;
  }

  /**
   * Obtain the virtual machine at the given <code>index</code>.
   * 
   * @param index
   *          the index of the vm to be returned
   * @return the virtual machine at the given <code>index</code>
   */
  public final NetVirtualMachine<MT, PT, MDT> getVirtualMachine(
      final int index) {
    return this.m_vms[index];
  }

  /**
   * obtain the index of the current simulation
   * 
   * @return the index of the current simulation
   */
  protected final int getSimulationIndex() {
    return this.m_simIndex;
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
  public void beginIndividual(final PT what) {
    NetVirtualMachine<MT, PT, MDT>[] vms;
    int i;

    this.m_simIndex = 0;
    vms = this.m_vms;

    for (i = (vms.length - 1); i >= 0; i--) {
      vms[i].beginIndividual(what);
    }
  }

  /**
   * End the individual.
   */
  public void endIndividual() {
    NetVirtualMachine<MT, PT, MDT>[] vms;
    int i;

    vms = this.m_vms;
    for (i = (vms.length - 1); i >= 0; i--) {
      vms[i].endIndividual();
    }

  }

  /**
   * Check whether the given id is valid or not.
   * 
   * @param id
   *          the id to be checked
   * @return <code>true</code> if and only if <code>id</code> is a
   *         valid id
   */
  public final boolean isValidId(final int id) {
    int[] ids;
    int i;

    i = this.m_simIndex;
    if (!(this.m_inSim))
      i--;
    ids = this.m_provider.m_ids[i];
    for (i = (this.m_vmCount - 1); i >= 0; i--) {
      if (id == ids[i])
        return true;
    }

    return false;
  }

  /**
   * Initialize the network
   */
  protected void initNetwork() {
    //
  }

  /**
   * This method is called right before the simulation begins.
   * 
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  public void beginSimulation() {
    NetVirtualMachine<MT, PT, MDT>[] vms;
    // Message<MT> m1, m2;
    int i, z;
    int[] ids;

    this.m_inSim = true;
    z = this.m_simIndex;

    this.m_error = false;

    this.m_terminatedIdx = this.m_activeCnt = this.m_vmCount = i = this.m_provider.m_vmCount[z];

    vms = this.m_vms;

    System.arraycopy(vms, 0, this.m_active, 0, i);
    ids = this.m_provider.m_ids[z];

    this.m_steps = this.m_provider.m_steps[z];
    this.m_delays = this.m_provider.m_delays[z];

    // for (m1 = this.m_messages; m1 != null; m1 = m2) {
    // m2 = m1.m_next;
    // NetVirtualMachine.disposeMessage(m1);
    // }
    NetVirtualMachine.disposeMessages(this.m_messages);
    this.m_messages = null;

    this.m_stepsIdx = this.m_steps.length;
    this.m_delayIdx = this.m_delays.length;

    this.initNetwork();

    for (--i; i >= 0; i--) {
      vms[i].m_id = ids[i];
      vms[i].beginSimulation();
    }
  }

  /**
   * This method is called when the simulation has ended
   * 
   * @throws IllegalStateException
   *           If this simulation is not yet running.
   */
  public void endSimulation() {
    NetVirtualMachine<MT, PT, MDT>[] vms;
    int i;

    vms = this.m_vms;
    i = this.m_vmCount;
    for (--i; i >= 0; i--) {
      vms[i].endSimulation();
    }
    this.m_simIndex++;

    this.m_inSim = false;
  }

  /**
   * Deliver a certain message.
   * 
   * @param message
   *          the message to be delivered
   */
  @SuppressWarnings("unchecked")
  protected void send(final Message<MDT> message) {
    int i;
    Message<MDT> m;
    final NetVirtualMachine<MT, PT, MDT>[] vms;
    final NetVirtualMachine<?, ?, MDT> v1;
    NetVirtualMachine<MT, PT, MDT> v2;

    v1 = message.m_owner;
    vms = ((NetVirtualMachine[]) (v1.m_neighbors));
    m = null;

    for (i = (v1.m_neighborCount - 1); i >= 0; i--) {
      v2 = vms[i];
      if (v2.m_alive) {
        if (i > 0) {
          if ((m = copyMessage(message)) == null) {
            this.m_error = true;
            return;
          }
        } else
          m = message;
        this.deliver(m, v2);
      }
    }

    if (m != message)
      NetVirtualMachine.disposeMessage(message);
  }

  /**
   * deliver the message <code>message</code> to the node at index
   * <code>to</code>.
   * 
   * @param message
   *          the message to be delivered
   * @param to
   *          the node to deliver to
   */
  @SuppressWarnings("unchecked")
  protected  void deliver(final Message<MDT> message,
      final NetVirtualMachine<MT, PT, MDT> to) {
    int del, d;
    Message<MDT> m, p;

    del = this.m_delays[--this.m_delayIdx];
    if (this.m_delayIdx <= 0)
      this.m_delayIdx = this.m_delays.length;

    p = null;
    for (m = this.m_messages; m != null; m = m.m_next) {
      d = m.m_delay;
      if (d > del) {
        m.m_delay = (d - del);
        break;
      }
      del -= d;
      p = m;
    }
    message.m_delay = del;
    message.m_next = m;
    if (p != null)
      p.m_next = message;
    else
      this.m_messages = message;

    message.m_to = to;
  }

  /**
   * copy a message
   * 
   * @param msg
   *          the message to be copied
   * @param <MTX>
   *          the memory type
   * @return the new message
   */
  protected static final <MTX extends Serializable> Message<MTX> copyMessage(
      final Message<MTX> msg) {
    Message<MTX> x;

    x = msg.m_owner.allocateMessage();
    if (x == null)
      return null;

    System.arraycopy(msg.m_data, 0, x.m_data, 0, (x.m_size = msg.m_size));
    return x;
  }

  /**
   * Perform a single step.
   * 
   * @return <code>true</code> if additional steps are useful,
   *         <code>false</code> otherwise
   */
  protected boolean doStep() {
    Message<MDT> m, n;
    int i, a;
    NetVirtualMachine<MT, PT, MDT> x;
    NetVirtualMachine<MT, PT, MDT>[] z;
    boolean b;

    b = false;

    // <-- messages
    m = this.m_messages;
    if (m != null) {
      if ((--m.m_delay) <= 0) {

        for (; m != null; m = n) {
          if (m.m_delay <= 0) {
            n = m.m_next;
            if (m.m_to.m_alive) {
              m.m_to.handleMessage(m);
              b = true;
            } else
              NetVirtualMachine.disposeMessage(m);
          } else
            break;
        }
        this.m_messages = m;

        if (b) {
          // activate !
          this.m_activeCnt = this.m_terminatedIdx;// this.m_vmCount;
          //
        }
      } else
        b = true;
    }

    // -->messages

    a = this.m_activeCnt;
    if (a <= 0)
      return b;

    z = this.m_active;

    i = ((this.m_steps[--this.m_stepsIdx]) % a);
    if (this.m_stepsIdx <= 0)
      this.m_stepsIdx = this.m_steps.length;

    x = z[i];
    switch (x.singleStep()) {
    case ERROR: {
      this.m_activeCnt = 0;
      this.m_error = true;
      return false;
    }

    case NOTHING: {
      this.m_activeCnt = (--a);
      z[i] = z[a];
      z[a] = x;
      break;
    }

    case TERMINATED: {
      this.m_activeCnt = (--a);
      z[i] = z[a];
      i = (--this.m_terminatedIdx);
      System.arraycopy(z, a + 1, z, a, i - a);
      z[i] = x;
      x.m_alive = false;
      break;
    }

    case CHANGED: {
      b = true;
      break;
    }
    }

    return ((a > 0) || b);
  }

  /**
   * Perform <code>steps</code> simulation steps.
   * 
   * @param steps
   *          The count of simulation steps to be performed.
   * @return <code>true</code> if and only if further simulating would
   *         possible change the state of the simulation,
   *         <code>false</code> if the simulation has come to a final,
   *         terminal state which cannot change anymore.
   * @throws IllegalStateException
   *           If this simulation is not yet running.
   * @throws IllegalArgumentException
   *           if <code>steps <= 0</code>.
   */
  public final boolean simulate(final long steps) {
    int i;

    for (i = ((int) steps); i > 0; i--) {
      if (!(this.doStep()))
        return false;
    }
    return true;
  }

  /**
   * Obtain an unique identifier of the simulation type performed by this
   * simulator. This could be the fully qualified class name of the
   * simulation provider, for example.
   * 
   * @return The unique identifier of the type of this simulation.
   */
  public Serializable getSimulationId() {
    return this.getClass();
  }

  /**
   * Perform a step of the activity, for example a single simulation step
   */
  public final void step() {
    this.doStep();
  }

  /**
   * The minimum number of virtual machines to be in a simulation.
   * 
   * @return minimum number of virtual machines to be in a simulation
   */
  public final int getMinVirtualMachines() {
    return this.m_parameters.getMinVirtualMachines();
  }

  /**
   * The maximum number of virtual machines to be in a simulation.
   * 
   * @return maximum number of virtual machines to be in a simulation
   */
  public final int getMaxVirtualMachines() {
    return this.m_vms.length;
  }

  /**
   * Obtain the virtual machine factory for the vms that will be running
   * inside the network.
   * 
   * @return the virtual machine factory of this parameter set
   */
  public final INetVirtualMachineFactory<MT, ?, ?> getNetVirtualMachineFactory() {
    return this.m_parameters.getNetVirtualMachineFactory();
  }

  /**
   * The maximum messages a node is allowed to send.
   * 
   * @return The maximum messages a node is allowed to send.
   */
  public final int getMaxMessages() {
    return this.m_parameters.getMaxMessages();
  }

  /**
   * The maximum size of a message
   * 
   * @return the maximum size of a message
   */
  public final int getMaxMessageSize() {
    return this.m_parameters.getMaxMessageDelay();
  }

  /**
   * Obtain the minimum message delay.
   * 
   * @return the minimum message delay
   */
  public final int getMinMessageDelay() {
    return this.m_parameters.getMinMessageDelay();
  }

  /**
   * Obtain the maximum message delay.
   * 
   * @return the maximum message delay
   */
  public final int getMaxMessageDelay() {
    return this.m_parameters.getMaxMessageDelay();
  }

  /**
   * Obtain the memory size defined in this parameter set.
   * 
   * @return the memory size defined in this parameter set
   */
  public final int getMemorySize() {
    return this.m_parameters.getMemorySize();
  }

  /**
   * Obtain the number of performed steps.
   * 
   * @return the number of steps performed
   */
  public final int getPerformedSteps() {
    int s, i;
    NetVirtualMachine<MT, PT, ?>[] x;

    x = this.m_vms;
    s = 0;
    for (i = (this.m_vmCount - 1); i >= 0; i--) {
      s += x[i].getPerformedSteps();
    }
    return s;
  }

  /**
   * Obtain the virtual machine state.
   * 
   * @return the virtual machine state
   */
  public EVirtualMachineState getVirtualMachineState() {
    int i;
    NetVirtualMachine<MT, PT, ?>[] x;
    EVirtualMachineState e, n;

    x = this.m_vms;
    e = EVirtualMachineState.NOTHING;
    for (i = (this.m_vmCount - 1); i >= 0; i--) {
      n = x[i].getVirtualMachineState();
      if (n.ordinal() > e.ordinal())
        e = n;
    }
    return e;
  }

  /**
   * Obtain the number of neighbors.
   * 
   * @return the number of neighbors
   */
  public final int getNeighborCount() {
    int s, i, k;
    NetVirtualMachine<MT, PT, ?>[] x;

    x = this.m_vms;
    s = 0;
    for (i = ((k = this.m_vmCount) - 1); i >= 0; i--) {
      s += x[i].getNeighborCount();
    }
    return ((k > 0) ? ((int) (0.5d + (s / ((double) k)))) : 0);
  }

  /**
   * Obtain the number of steps where this vm was idle.
   * 
   * @return the number of steps where this vm was idle
   */
  public final int getIdleSteps() {
    int s, i;
    NetVirtualMachine<MT, PT, ?>[] x;

    x = this.m_vms;
    s = 0;
    for (i = (this.m_vmCount - 1); i >= 0; i--) {
      s += x[i].getIdleSteps();
    }
    return s;
  }

  /**
   * Obtain the number of messages sent.
   * 
   * @return the number of messages sent
   */
  public final int getMessageCount() {
    int s, i;
    NetVirtualMachine<MT, PT, ?>[] x;

    x = this.m_vms;
    s = 0;
    for (i = (this.m_vmCount - 1); i >= 0; i--) {
      s += x[i].getMessageCount();
    }
    return s;
  }

  /**
   * Check whether an error occurd in this network
   * 
   * @return <code>true</code> if and only if an error occured,
   *         <code>false</code> if everything went ok
   */
  public final boolean wasErroneous() {
    return this.m_error;
  }
}

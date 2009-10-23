/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-08-29
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.VirtualMachine.java
 * Last modification: 2007-09-13
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

package org.dgpf.vm.base;

import java.io.Serializable;

import org.dgpf.vm.objectives.IVirtualMachineSimulationInformation;
import org.sfc.parallel.simulation.IStepable;
import org.sfc.text.ITextable;
import org.sfc.text.TextUtils;
import org.sigoa.spec.simulation.ISimulation;

/**
 * This is a common base class for virtual machines of any kind.
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the program type
 * @author Thomas Weise
 */
public abstract class VirtualMachine<MT extends Serializable, PT extends VirtualMachineProgram<MT>>
    extends VirtualMachineParameters<MT> implements Serializable,
    ISimulation<PT>, IStepable, IVirtualMachineSimulationInformation {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the program running on this vm
   */
  protected PT m_program;

  /**
   * the step counter
   */
  private int m_stepCnt;

  /**
   * the current memory.
   */
  public final MT m_memory;

  /**
   * The virtual machine parameters
   */
  protected final IVirtualMachineParameters<MT> m_parameters;

  /**
   * the state of this vm
   */
  private EVirtualMachineState m_state;

  /**
   * Create a new virtual machine.
   * 
   * @param parameters
   *          the virtual machine parameters
   */
  @SuppressWarnings("unchecked")
  protected VirtualMachine(final IVirtualMachineParameters<MT> parameters) {
    super(parameters);
    this.m_parameters = parameters;
    this.m_memory = this.createMemory();
  }

  /**
   * Create a memory block
   * 
   * @return the vm memory
   */
  protected abstract MT createMemory();

  /**
   * Obtain the state of this virtual machine.
   * 
   * @return the state of this virtual machine
   */
  public final EVirtualMachineState getVirtualMachineState() {
    return this.m_state;
  }

  /**
   * An error occured.
   */
  public final void setError() {
    this.m_state = EVirtualMachineState.ERROR;
  }

  /**
   * Terminate this vm's course of execution.
   */
  public final void setTerminate() {
    if (this.m_state.ordinal() < 2)
      this.m_state = EVirtualMachineState.TERMINATED;
  }

  /**
   * Perform a single simulation step
   */
  public final void step() {
    this.singleStep();
  }

  /**
   * Perform a single simulation step.
   * 
   * @return a new virtual machine state, see {@link EVirtualMachineState}
   */
  protected EVirtualMachineState doStep() {
    return this.m_state;
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
    this.m_program = what;
  }

  /**
   * End the individual.
   */
  public void endIndividual() {
    this.m_program = null;
  }

  /**
   * This method is called right before the simulation begins.
   * 
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  @SuppressWarnings("unchecked")
  public void beginSimulation() {
    this.m_state = EVirtualMachineState.NOTHING;
    this.m_stepCnt = 0;
  }

  /**
   * This method is called when the simulation has ended
   * 
   * @throws IllegalStateException
   *           If this simulation is not yet running.
   */
  public void endSimulation() {
    //
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
    EVirtualMachineState s;

    for (i = ((int) (steps)); i > 0; i--) {
      this.m_stepCnt++;
      s = this.doStep();
      if ((this.m_state = EVirtualMachineState
          .updateState(this.m_state, s)).ordinal() > 1)
        return false;
    }
    return true;
  }

  /**
   * Perform a single step of this virtual machine.
   * 
   * @return the new virtual machine state, see
   *         {@link EVirtualMachineState}
   */
  public final EVirtualMachineState singleStep() {
    EVirtualMachineState s;

    this.m_stepCnt++;

    if (this.m_state.ordinal() > 1)
      return this.m_state;

    s = this.doStep();
    return (this.m_state = EVirtualMachineState.updateState(this.m_state,
        s));
  }

  /**
   * Obtain the number of performed steps. Warning: the value provided by
   * this method is only updated <i>after</i> calls to {@link #step()} and
   * {@link #simulate(long)}.
   * 
   * @return the number of steps performed
   */
  public final int getPerformedSteps() {
    return this.m_stepCnt;
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
   * Obtain the program running on this vm
   * 
   * @return the program running on this vm
   */
  public final PT getProgram() {
    return this.m_program;
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    super.toStringBuilder(sb);
    sb.append(TextUtils.LINE_SEPARATOR);
    TextUtils.append(this.m_program, sb);
    sb.append(TextUtils.LINE_SEPARATOR);
    sb.append("State         : "); //$NON-NLS-1$
    this.m_state.toStringBuilder(sb);
    sb.append(TextUtils.LINE_SEPARATOR);
    sb.append("Steps         : "); //$NON-NLS-1$
    sb.append(this.m_stepCnt);
    if (this.m_memory instanceof ITextable) {
      sb.append(TextUtils.LINE_SEPARATOR);
      sb.append("Memory        : "); //$NON-NLS-1$
      ((ITextable) (this.m_memory)).toStringBuilder(sb);
    }
  }

  /**
   * Serializes the parameters of the constructor of this object.
   * 
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  @Override
  protected void javaParametersToStringBuilder(final StringBuilder sb,
      final int indent) {
    appendJavaObject(this.m_parameters, sb, indent);
  }

  /**
   * Obtain the virtual machine parameters that were used to create this vm
   * 
   * @return the parameters the provided the information to create this vm
   */
  public final IVirtualMachineParameters<MT> getParameters() {
    return this.m_parameters;
  }
}

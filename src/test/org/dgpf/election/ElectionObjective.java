/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-09
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.ElectionObjective.java
 * Last modification: 2007-10-09
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

package test.org.dgpf.election;

import java.io.Serializable;

import org.dgpf.vm.base.EVirtualMachineState;
import org.dgpf.vm.base.VirtualMachineProgram;
import org.dgpf.vm.net.NetVirtualMachine;
import org.sfc.collections.buffers.IntArrayBuffer;
import org.sigoa.refimpl.go.objectives.HysteresisObjectiveFunction;
import org.sigoa.refimpl.go.objectives.ObjectiveState;
import org.sigoa.spec.simulation.ISimulation;

/**
 * An objective function for election algorithms.
 * 
 * @author Thomas Weise
 */
public class ElectionObjective
    extends
    HysteresisObjectiveFunction<VirtualMachineProgram<Serializable>, ObjectiveState, Serializable, //
    ISimulation<VirtualMachineProgram<Serializable>>> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the required steps
   */
  private final int m_requiredSteps;

  /**
   * the int array buffer
   */
  private final IntArrayBuffer m_buffer;

  /**
   * Create the objective function
   * 
   * @param requiredSteps
   *          the steps required
   */
  public ElectionObjective(final int requiredSteps) {
    super();
    this.m_requiredSteps = requiredSteps;
    this.m_buffer = new IntArrayBuffer(ElectionTestSeries.MAX_VM_COUNT);
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
    sb.append("election"); //$NON-NLS-1$
  }

  /**
   * Obtain the id of the required simulator. If <code>null</code> is
   * returned, no simulation will be needed/performed for objective
   * function.
   * 
   * @return The id of the simulator required for the evaluation of this
   *         objective function.
   */
  @Override
  public Serializable getRequiredSimulationId() {
    return IElectionInformation.class;
  }

  /**
   * This method is called after any simulation/evaluation is performed.
   * After this method returns, an objective value must have been stored in
   * the state record.
   * 
   * @param individual
   *          The individual that should be evaluated next.
   * @param state
   *          The state record.
   * @param staticData
   *          not used, <code>null</code>
   * @param simulation
   *          The simulation (<code>null</code> if no simulation is
   *          required as indivicated by
   *          <code>getRequiredSimulationSteps</code>).
   * @throws NullPointerException
   *           if <code>individual==null</code> or
   *           <code>state==null</code> or if
   *           <code>simulator==null</code> but a simulation is required.
   */
  @Override
  public void endEvaluation(
      final VirtualMachineProgram<Serializable> individual,
      final ObjectiveState state, final Serializable staticData,
      final ISimulation<VirtualMachineProgram<Serializable>> simulation) {

    IElectionInformation f;
    int i, j, id, valueCount, errorCount;
    final int totalCount;
    final int[] values;
    IntArrayBuffer buf;
    NetVirtualMachine<?, ?, ?> m;
    double r;
    int terminated;
    EVirtualMachineState ss;

    if (individual == null)
      return;

    f = ((IElectionInformation) simulation);

    buf = this.m_buffer;
    values = buf.allocate();

    totalCount = f.getVirtualMachineCount();
    valueCount = 0;
    errorCount = 0;
    terminated = 0;

    outer: for (i = (totalCount - 1); i >= 0; i--) {

      m = f.getVirtualMachine(i);
      id = m.getId();
      ss = m.getVirtualMachineState();
      if (ss == EVirtualMachineState.TERMINATED)
        terminated++;

      id = f.getResult(i);
      if (f.isValidId(id) && (ss != EVirtualMachineState.ERROR)) {
        for (j = (valueCount - 1); j >= 0; j--) {
          if (values[j] == id) {
            continue outer;
          }
        }
        values[valueCount] = id;
        valueCount++;
      } else
        errorCount++;
    }

    buf.dispose(values);

    r = 0d;

    if (valueCount > 0) {
      r += (((double) (valueCount - 1)) / ((double) (totalCount - 1)));
    }

    if (valueCount >= (totalCount - errorCount))
      r++;

    r += (((double) (errorCount) / ((double) (totalCount))));

    // add termination offset
    // r += (0.5d * ((totalCount - terminated) / ((double) totalCount)));

    state.setObjectiveValue(0.5d * r);
  }

  /**
   * Obtain the count of simulation steps that will be needed in order to
   * evaluate the specified individual.
   * 
   * @param individual
   *          The individual to be evaluated.
   * @param state
   *          The state container.
   * @param staticState
   *          the static state record
   * @return The count of simulation steps needed. Values 0 indidicate that
   *         no simulation needs to be performed for this objective
   *         function.
   * @throws NullPointerException
   *           if <code>individual==null</code> or
   *           <code>state==null</code>
   */
  @Override
  public long getRequiredSimulationSteps(
      final VirtualMachineProgram<Serializable> individual,
      final ObjectiveState state, final Serializable staticState) {
    return this.m_requiredSteps;
  }
}

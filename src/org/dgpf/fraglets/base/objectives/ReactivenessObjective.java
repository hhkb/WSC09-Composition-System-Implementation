/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-16
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.base.objectives.ReactivenessObjective.java
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

package org.dgpf.fraglets.base.objectives;

import java.io.Serializable;

import org.dgpf.fraglets.base.FragletProgram;
import org.dgpf.fraglets.base.IFragletReactionPerformance;
import org.sigoa.refimpl.go.objectives.ObjectiveFunction;
import org.sigoa.refimpl.go.objectives.ObjectiveState;
import org.sigoa.spec.go.objectives.IObjectiveFunction;
import org.sigoa.spec.simulation.ISimulation;

/**
 * An objective function favoring reactive processes.
 * 
 * @author Thomas Weise
 */
public class ReactivenessObjective extends
    ObjectiveFunction<FragletProgram, ObjectiveState, Serializable, //
    ISimulation<FragletProgram>> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared instance of this objective function
   */
  public static final IObjectiveFunction<FragletProgram, ObjectiveState, Serializable, //
  ISimulation<FragletProgram>> REACTIVENESS_OBJECTIVE = new ReactivenessObjective();

  /**
   * the performance class
   */
  private final Class<? extends IFragletReactionPerformance> m_class;

  /**
   * Create the objective function
   */
  protected ReactivenessObjective() {
    this(null);
  }

  /**
   * Create a new fraglet performance measure
   * 
   * @param clazz
   *          the class of the required simulator
   */
  public ReactivenessObjective(
      final Class<? extends IFragletReactionPerformance> clazz) {
    super();
    this.m_class = ((clazz != null) ? clazz
        : IFragletReactionPerformance.class);
  }

  /**
   * read resolve
   * 
   * @return the read resolution
   */
  private final Object readResolve() {
    if (this.getClass() == ReactivenessObjective.class)
      return REACTIVENESS_OBJECTIVE;
    return this;
  }

  /**
   * write replace
   * 
   * @return the write replacement
   */
  private final Object writeReplace() {
    return readResolve();
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
    sb.append("reactiveness"); //$NON-NLS-1$
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
    return this.m_class;
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
  public void endEvaluation(final FragletProgram individual,
      final ObjectiveState state, final Serializable staticData,
      final ISimulation<FragletProgram> simulation) {
    int i;

    i = ((IFragletReactionPerformance) simulation).getReactionCount();

    state.setObjectiveValue((i > 0) ? (1.0d / i) : 1.0d);
  }
}
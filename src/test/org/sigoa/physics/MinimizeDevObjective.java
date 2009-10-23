/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-21
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.symbolicRegression.objectives.MinimizeErrorObjective.java
 * Last modification: 2007-09-21
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

package test.org.sigoa.physics;

import java.io.Serializable;

import org.dgpf.symbolicRegression.scalar.real.RealExpression;
import org.sigoa.refimpl.go.objectives.ObjectiveFunction;
import org.sigoa.refimpl.go.objectives.ObjectiveState;

/**
 * This (binary) objective function separates those programs that produce
 * errors from those which don't.
 * 
 * @author Thomas Weise
 */
public class MinimizeDevObjective extends
    ObjectiveFunction<RealExpression, ObjectiveState, Serializable, //
    PhysicsContext> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the index
   */
  private final int m_idx;

  /**
   * the name
   */
  private final char[] m_name;

  /**
   * Create the objective function
   * 
   * @param idx
   *          the index
   * @param name
   *          the name
   */
  protected MinimizeDevObjective(final int idx, final String name) {
    super();
    this.m_idx = idx;
    this.m_name = name.toCharArray();
  }

  /**
   * <p>
   * This method is called before any simulation or optimization will be
   * performed. It is used to check whether an individual is worth being
   * processed further. Normally, this method will return <code>true</code>
   * or performs only a very fast and simple check.
   * </p>
   * <p>
   * This method is only called once per individual.
   * </p>
   * <p>
   * If this method returns
   * <code>false/<code>, all objective values of the individual will be set
   * to positive infinity and also no other objective function is invoked
   * on the individual
   * </p>
   *
   * @param individual
   *          The individual to be checked.
   * @param staticState
   *          the static state record
   * @return <code>true</code> if and only if the individual is worth
   *         being examined. <code>false</code> if it can be thrown away.
   *
   * @throws NullPointerException if <code>individual==null</code>
   */
  @Override
  public boolean sanityCheck(final RealExpression individual,
      final Serializable staticState) {
    int i;

    if (individual == null)
      return false;
    i = individual.getWeight();
    return ((i >= 6) && (i <= 100));
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
    sb.append(this.m_name);
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
    return PhysicsContext.class;
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
  public void endEvaluation(final RealExpression individual,
      final ObjectiveState state, final Serializable staticData,
      final PhysicsContext simulation) {
    state.setObjectiveValue(simulation.m_result[this.m_idx]);
  }
}

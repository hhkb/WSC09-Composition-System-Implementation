/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-27
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.objectives.ObjectiveFunction.java
 * Last modification: 2006-11-27
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

package org.sigoa.refimpl.go.objectives;

import java.io.Serializable;

import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.go.objectives.IObjectiveFunction;
import org.sigoa.spec.go.objectives.IObjectiveState;
import org.sigoa.spec.go.objectives.IObjectiveValueComputer;
import org.sigoa.spec.simulation.ISimulation;

/**
 * The default implementation of the <code>IObjectiveFunction</code>-interface.
 *
 * @param <PP>
 *          The phenotype of the individuals to evaluate.
 * @param <D>
 *          The type of state object used by this objective function.
 * @param <SI>
 *          The simulator type involved in the individual evaluation.
 * @param <SS>
 *          the type of static state used, if any needed
 * @author Thomas Weise
 * @version 1.0.0
 */
public class ObjectiveFunction<PP extends Serializable, D extends IObjectiveState, SS extends Serializable, SI extends ISimulation<PP>>
    extends ImplementationBase<Serializable, PP> implements
    IObjectiveFunction<PP, D, SS, SI> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The objective value computer used by this objective function.
   */
  private final IObjectiveValueComputer m_ovc;

  /**
   * Create a new objective function.
   */
  protected ObjectiveFunction() {
    this(null);
  }

  /**
   * Create a new objective function.
   *
   * @param ovc
   *          The objective value computer to be used. If this is
   *          <code>null</code>, the <code>ObjectiveUtils.AVG_OVC</code>
   *          is used as default.
   */
  protected ObjectiveFunction(final IObjectiveValueComputer ovc) {
    super();
    this.m_ovc = ((ovc != null) ? ovc : ObjectiveUtils.AVG_OVC);
  }

  /**
   * Obtain the objective value computer used by this objective function.
   *
   * @return The objective value computer used by this objective function.
   */
  public IObjectiveValueComputer getObjectiveValueComputer() {
    return this.m_ovc;
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
  public boolean sanityCheck(final PP individual, final SS staticState) {
    if (individual == null)
      throw new NullPointerException();
    return true;
  }

  /**
   * Create a new static state record.
   *
   * @return the new static state record
   */
  public SS createStaticState() {
    return null;
  }

  /**
   * Destroy a static state record no longer needed.
   *
   * @param staticState
   *          the static state record no longer needed
   */
  public void destroyStaticState(final SS staticState) {
    //
  }

  /**
   * Create a new state object for this objective function. This default
   * implementation creates a new <code>AggregationState</code>-object.
   *
   * @param staticState
   *          the static state record
   * @return The new state container.
   */
  @SuppressWarnings("unchecked")
  public D createState(final SS staticState) {
    return (D) (new ObjectiveState());
  }

  /**
   * Destroy a state object no longer needed.
   *
   * @param state
   *          The state container to be destroyed.
   * @param staticState
   *          the static state record
   * @throws NullPointerException
   *           if <code>state==null</code>.
   */
  public void destroyState(final D state, final SS staticState) {
    if (state == null)
      throw new NullPointerException();
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
   * @return The count of simulation steps needed. This default
   *         implementation returns 0.
   * @throws NullPointerException
   *           if <code>individual==null</code> or
   *           <code>state==null</code>
   */
  public long getRequiredSimulationSteps(final PP individual,
      final D state, final SS staticState) {
    if ((individual == null) || (state == null))
      throw new NullPointerException();
    return 0;
  }

  /**
   * This method is called before any evaluation/simulation is performed.
   * If this objective function requires a simulation and needs to inspect
   * the simulation's state, the return value is the simulation step where
   * the first inspection should be scheduled. If this is not the case or
   * the return value is 0 or exceeds the total count of simulation steps,
   * no inspection will occure.
   *
   * @param individual
   *          The individual that should be evaluated next.
   * @param state
   *          The state record.
   * @param simulation
   *          The simulation (<code>null</code> if no simulation is
   *          required as indivicated by
   *          <code>getRequiredSimulationSteps</code>).
   * @param staticState
   *          the static state record
   * @return This default method returns 0.
   * @throws NullPointerException
   *           if <code>individual==null</code> or
   *           <code>state==null</code> or if
   *           <code>simulator==null</code> but a simulation is required.
   */
  public long beginEvaluation(final PP individual, final D state,
      final SS staticState, final SI simulation) {
    if ((individual == null) || (state == null))
      throw new NullPointerException();
    return 0;
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
   * @param staticState
   *          the static state record
   * @param simulation
   *          The simulation (<code>null</code> if no simulation is
   *          required as indivicated by
   *          <code>getRequiredSimulationSteps</code>).
   * @throws NullPointerException
   *           if <code>individual==null</code> or
   *           <code>state==null</code> or if
   *           <code>simulator==null</code> but a simulation is required.
   */
  public void endEvaluation(final PP individual, final D state,
      final SS staticState, final SI simulation) {
    if ((individual == null) || (state == null))
      throw new NullPointerException();
  }

  /**
   * Inspect the state of a simulation.
   *
   * @param individual
   *          The individual that should be evaluated next.
   * @param state
   *          The state record.
   * @param staticState
   *          the static state record
   * @param simulation
   *          The simulation (<code>null</code> if no simulation is
   *          required as indivicated by
   *          <code>getRequiredSimulationSteps</code>).
   * @return This default implementation returns 0.
   * @throws NullPointerException
   *           optionally: if <code>individual==null</code> or
   *           <code>state==null</code>
   */
  public long inspect(final PP individual, final D state,
      final SS staticState, final SI simulation) {
    return 0;
  }

  /**
   * Obtain the id of the required simulator. If <code>null</code> is
   * returned, no simulation will be needed/performed for objective
   * function.
   *
   * @return The id of the simulator required for the evaluation of this
   *         objective function.
   */
  public Serializable getRequiredSimulationId() {
    return null;
  }

  /**
   * This method is called by the evaluator in order to determine the final
   * objective value for this objective. The evaluator may perform multiple
   * simulations, where the objective function stores an objective value
   * into its state record for each single one. These values are now put
   * into an array of double, so the objective function may decide on a
   * final value based on these results of single simulations. The
   * <code>results</code>-array is sorted in ascending order.
   *
   * @param results
   *          The results of the single simulations. (Sorted in ascending
   *          order)
   * @param staticState
   *          the static state record
   * @return A final objective value based on the results passed in.
   */
  public double computeObjectiveValue(final double[] results,
      final SS staticState) {
    return this.m_ovc.computeObjectiveValue(results);
  }

  /**
   * After all objective functions have decided that the individual is
   * sane, this function is called for of them.
   *
   * @param individual
   *          The individual to be evaluated.
   * @param staticState
   *          the static state record
   * @throws NullPointerException
   *           if <code>individual==null</code>
   */
  public void beginIndividual(final PP individual, final SS staticState) {
    if (individual == null)
      throw new NullPointerException();
  }

  /**
   * This function is called at the end of an individual's inspection
   * before {@link #computeObjectiveValue(double[], Serializable)}.
   *
   * @param individual
   *          The individual to be evaluated.
   * @param staticState
   *          the static state record
   * @throws NullPointerException
   *           if <code>individual==null</code>
   */
  public void endIndividual(final PP individual, final SS staticState) {
    if (individual == null)
      throw new NullPointerException();
  }
}

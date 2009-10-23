/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 ${time}
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.objectives.IObjectiveFunction.java
 * Last modification: 2006-11-22
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

package org.sigoa.spec.go.objectives;

import java.io.Serializable;

import org.sigoa.spec.simulation.ISimulation;

/**
 * An objective function is used in order to evaluate individuals according
 * to their fitness in one certain aspect.
 *
 * @param <PP>
 *          The phenotype of the individuals to evaluate.
 * @param <ST>
 *          The type of state object used by this objective function.
 * @param <SI>
 *          The simulator type involved in the individual evaluation.
 * @param <SS>
 *          the type of static state used, if any needed
 * @author Thomas Weise
 * @version 1.0.0
 */
public interface IObjectiveFunction<PP extends Serializable, ST extends IObjectiveState, SS extends Serializable, SI extends ISimulation<PP>>
    extends Serializable {
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
   *  @param staticState the static state record
   * @return <code>true</code> if and only if the individual is worth
   *         being examined. <code>false</code> if it can be thrown away.
   *
   * @throws NullPointerException if <code>individual==null</code>
   */
  public abstract boolean sanityCheck(final PP individual,
      final SS staticState);

  /**
   * Create a new static state record.
   *
   * @return the new static state record
   */
  public abstract SS createStaticState();

  /**
   * Destroy a static state record no longer needed.
   *
   * @param staticState
   *          the static state record no longer needed
   */
  public abstract void destroyStaticState(final SS staticState);

  /**
   * Create a new state object for this objective function.
   *
   * @param staticState
   *          the static state record
   * @return The new state container.
   */
  public abstract ST createState(final SS staticState);

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
  public abstract void destroyState(final ST state, final SS staticState);

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
  public abstract void beginIndividual(final PP individual,
      final SS staticState);

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
  public abstract void endIndividual(final PP individual,
      final SS staticState);

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
  public abstract long getRequiredSimulationSteps(final PP individual,
      final ST state, final SS staticState);

  /**
   * This method is called before any evaluation/simulation is performed.
   * If this objective function requires a simulation and needs to inspect
   * the simulation's state, the return value is the simulation step where
   * the first inspection should be scheduled. If this is not the case or
   * the return value is = 0 or exceeds the total count of simulation
   * steps, no inspection will occure.
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
   * @return The time index of the first inspection of the simulation.
   * @throws NullPointerException
   *           if <code>individual==null</code> or
   *           <code>state==null</code> or if
   *           <code>simulator==null</code> but a simulation is required.
   */
  public abstract long beginEvaluation(final PP individual, final ST state,
      final SS staticState, final SI simulation);

  /**
   * This method is called after any simulation/evaluation is performed.
   * After this method returns, an objective value must have been stored in
   * the state record.
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
   * @throws NullPointerException
   *           if <code>individual==null</code> or
   *           <code>state==null</code>
   */
  public abstract void endEvaluation(final PP individual, final ST state,
      final SS staticState, final SI simulation);

  /**
   * Inspect the state of a simulation.
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
   * @return The count of simulation steps until the next inspection should
   *         be scheduled. If this value is either <= 0 or exceeds the
   *         maximum count of simulation steps defined, no further
   *         inspection will be performed in this evaluation.
   * @throws NullPointerException
   *           optionally: if <code>individual==null</code> or
   *           <code>state==null</code>
   */
  public abstract long inspect(final PP individual, final ST state,
      final SS staticState, final SI simulation);

  /**
   * Obtain the id of the required simulator. If <code>null</code> is
   * returned, no simulation will be needed/performed for objective
   * function.
   *
   * @return The id of the simulator required for the evaluation of this
   *         objective function.
   */
  public abstract Serializable getRequiredSimulationId();

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
  public abstract double computeObjectiveValue(final double[] results,
      final SS staticState);
}

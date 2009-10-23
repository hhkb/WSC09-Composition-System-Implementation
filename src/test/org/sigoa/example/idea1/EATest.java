/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : TODO
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.example.idea1.EATest.java
 * Last modification: TODO
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

package test.org.sigoa.example.idea1;

import java.io.Serializable;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sigoa.refimpl.go.comparators.ParetoComparator;
import org.sigoa.refimpl.go.embryogeny.CopyEmbryogeny;
import org.sigoa.refimpl.go.objectives.ObjectiveState;
import org.sigoa.refimpl.utils.testSeries.EDataGrouping;
import org.sigoa.refimpl.utils.testSeries.LoggingSelection;
import org.sigoa.refimpl.utils.testSeries.ea.EATestSeries;
import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.objectives.IObjectiveFunction;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.simulation.ISimulation;

/**
 * A program that runs an evolutionary algorithm on the problem.
 * 
 * @author Thomas Weise
 */
public class EATest extends EATestSeries {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the tsp creator
   */
  public static final ICreator<int[]> TSP_CREATOR = new Creation();

  /**
   * the tsp mutator
   */
  public static final IMutator<int[]> TSP_MUTATOR = new Mutation();

  /**
   * the tsp mutator
   */
  public static final ICrossover<int[]> TSP_CROSSOVER = new Crossover();

  /**
   * the objective function
   */
  public static final IObjectiveFunction<int[], ObjectiveState, Serializable, ISimulation<int[]>> DIST_OBJECTIVE = new DistObjective();

  /**
   * the comparator
   */
  public static final IComparator COMPARATOR = ParetoComparator.PARETO_COMPARATOR;

  /**
   * the genotype-phenotype-mapping
   */
  @SuppressWarnings("unchecked")
  public static final IEmbryogeny<int[], int[]> EMBRYOGENY = ((IEmbryogeny) (CopyEmbryogeny.COPY_EMBRYOGENY));

  /**
   * Create a new test series.
   * 
   * @param dir
   *          the directory to store the test data in
   * @param requiredSteps
   *          the number of required steps
   */
  protected EATest(final Object dir) {
    super(dir, EDataGrouping.RUN, -1, null, null);

    this.selectForLogging(BEST_INDIVIDUALS);
    this.selectForLogging(BEST_OBJECTIVES);
    this.selectForLogging(RUN_PARAMETERS);
    this.selectForLogging(LoggingSelection.RESULT_INDIVIDUALS);
    this.selectForLogging(LoggingSelection.RESULT_OBJECTIVES);
    this.selectForLogging(LoggingSelection.ALL_OBJECTIVES);
    this.selectForLogging(LoggingSelection.ALL_INDIVIDUALS);
    this.selectForLogging(LoggingSelection.OBSERVE_SUCCESS);
    this.selectForLogging(LoggingSelection.OBSERVE_SUCCESS_INDIVIDUALS);

    this.setMutationRate(0.7d);
    this.setCrossoverRate(0.4d);
    this.setPopulationSize(100);
    this.setSteadyState(true);
    this.setMaxGenerations(20);
  }

  /**
   * Create the objective functions
   * 
   * @return the list of objective functions
   */
  @Override
  protected List<?> createObjectives() {
    List<Object> o;

    o = CollectionUtils.createList();

    o.add(DIST_OBJECTIVE);

    return o;
  }

  /**
   * Create the comparator
   * 
   * @return the comparator
   */
  @Override
  protected IComparator createComparator() {
    return COMPARATOR;
  }

  /**
   * create the creator
   * 
   * @return the creator
   */
  @Override
  protected ICreator<?> createCreator() {
    return TSP_CREATOR;
  }

  /**
   * create the mutator
   * 
   * @return the mutator
   */
  @Override
  protected IMutator<?> createMutator() {
    return TSP_MUTATOR;
  }

  /**
   * create the crossover
   * 
   * @return the crossover
   */
  @Override
  protected ICrossover<?> createCrossover() {
    return TSP_CROSSOVER;
  }

  /**
   * create the embryogeny
   * 
   * @return the embryogeny
   */
  @Override
  protected IEmbryogeny<?, ?> createEmbryogeny() {
    return EMBRYOGENY;
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    new EATest("E:\\temp\\results").start(); //$NON-NLS-1$
  }
}
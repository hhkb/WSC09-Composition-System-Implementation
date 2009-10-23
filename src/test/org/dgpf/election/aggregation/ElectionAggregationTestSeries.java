/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.aggregation.ElectionAggregationTestSeries.java
 * Last modification: 2007-12-22
 *                by: Thomas Weise
 * 
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to the Free Software
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

package test.org.dgpf.election.aggregation;

import org.dgpf.aggregation.base.DefaultAggregationLanguage;
import org.dgpf.aggregation.net.AggregationNetParameters;
import org.dgpf.aggregation.net.genetics.AggregationNetProgramReproduction;
import org.dgpf.vm.net.NetworkProvider;
import org.sigoa.refimpl.go.embryogeny.CopyEmbryogeny;
import org.sigoa.refimpl.utils.testSeries.successFilters.FirstObjectiveZeroFilter;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;

import test.org.dgpf.election.ElectionTestSeries;
import test.org.dgpf.election.rbgp.ElectionRBGPTestSeries;

/**
 * the test series for aggregation election
 * 
 * @author Thomas Weise
 */
public class ElectionAggregationTestSeries extends ElectionTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * the symbol set to use
   */
  public static final DefaultAggregationLanguage LANGUAGE = new DefaultAggregationLanguage(
      4);

  /**
   * the maximum delay
   */
  public static final int MAX_DELAY = (2 * MAX_VM_COUNT);

  /**
   * the number of steps
   */
  public static final int STEPS = ElectionRBGPTestSeries.STEPS;

  /**
   * the shared classifier parameters
   */
  public static final AggregationNetParameters PARAMETERS = new AggregationNetParameters(
      LANGUAGE,// 
      MIN_VM_COUNT, MAX_VM_COUNT,
      AggregationElectionVMFactory.AGGREGATION_ELECTION_VM_FACTORY,
      MAX_MESSAGES,// max messages
      MIN_DELAY, MAX_DELAY);

  /**
   * the embryogeny
   */
  public static final IEmbryogeny<?, ?> EMBRYOGENY = CopyEmbryogeny.COPY_EMBRYOGENY;

  /**
   * the success filter
   */
  public static FirstObjectiveZeroFilter s_filter = null;

  /**
   * Create a new test series.
   * 
   * @param dir
   *          the directory to store the test data in
   */
  public ElectionAggregationTestSeries(final Object dir) {
    this(dir, STEPS);
  }

  /**
   * Create a new test series.
   * 
   * @param dir
   *          the directory to store the test data in
   * @param steps
   *          the number of steps allowed
   */
  public ElectionAggregationTestSeries(final Object dir, final int steps) {
    super(dir, steps);

    if (s_filter == null) {
      s_filter = new FirstObjectiveZeroFilter(this
          .createIndividualEvaluator());
    }

    this.setSuccessFilter(s_filter);
//    this.selectForLogging(LoggingSelection.ALL_INDIVIDUALS);
  }

  /**
   * Obtain the simulation provider needed.
   * 
   * @param sc
   *          the scenario count
   * @param randomized
   *          <code>true</code> if and only if the scenarios should be
   *          ranomized
   * @return the simulation provider needed
   */
  @Override
  protected NetworkProvider<?> doCreateSimulationProvider(final int sc,
      final boolean randomized) {
    return new AggregationElectionProvider(PARAMETERS, randomized, sc);
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
   * create the creator
   * 
   * @return the creator
   */
  @Override
  protected ICreator<?> createCreator() {
    return new AggregationNetProgramReproduction(PARAMETERS);
  }

  /**
   * create the mutator
   * 
   * @return the mutator
   */
  @Override
  protected IMutator<?> createMutator() {
    return new AggregationNetProgramReproduction(PARAMETERS);
  }

  /**
   * create the crossover
   * 
   * @return the crossover
   */
  @Override
  protected ICrossover<?> createCrossover() {
    return new AggregationNetProgramReproduction(PARAMETERS);
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    new ElectionAggregationTestSeries("e:\\temp\\q").start(); //$NON-NLS-1$
    // new ElectionFragletTestSeries("e:\\temp\\k\\").start(); //
    // //$NON-NLS-1$
  }

  /**
   * Obtain the maximum weight of a program
   * 
   * @return the maximum weight of a program
   */
  @Override
  protected int getMaxWeight() {
    return 50;
  }
}

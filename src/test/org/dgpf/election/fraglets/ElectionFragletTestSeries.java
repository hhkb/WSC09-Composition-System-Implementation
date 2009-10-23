/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-16
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.fraglets.ElectionFragletTestSeries.java
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

package test.org.dgpf.election.fraglets;

import org.dgpf.fraglets.base.genetics.FragletInt2StringEmbryogeny;
import org.dgpf.fraglets.net.FragletNetParameters;
import org.dgpf.lgp.base.genome.plain.Int2StringEditor;
import org.dgpf.lgp.base.genome.plain.Int2StringProcCrossover;
import org.dgpf.vm.net.NetworkProvider;
import org.sigoa.refimpl.genomes.string.DefaultStringCrossover;
import org.sigoa.refimpl.genomes.string.StringCreator;
import org.sigoa.refimpl.genomes.string.VariableLengthStringMutator;
import org.sigoa.refimpl.go.reproduction.IterativeMultiCrossover;
import org.sigoa.refimpl.utils.testSeries.successFilters.FirstObjectiveZeroFilter;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;

import test.org.dgpf.election.ElectionTestSeries;
import test.org.dgpf.election.rbgp.ElectionRBGPTestSeries;

/**
 * The fraglet-based election test series
 * 
 * @author Thomas Weise
 */
public class ElectionFragletTestSeries extends ElectionTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * the symbol set to use
   */
  public static final ElectionInstructionSet INSTRUCTION_SET = ElectionInstructionSet.DEFAULT_ELECTION_INSTRUCTION_SET;

  /**
   * the maximum delay
   */
  public static final int MAX_DELAY = (2 * MAX_VM_COUNT);

  /**
   * the number of steps
   */
  public static final int STEPS = 2 * ElectionRBGPTestSeries.STEPS;// 4

  /**
   * the maximum length of a fraglet
   */
  public static final int MAX_FRAGLET_LENGTH = 15;

  /**
   * the shared classifier parameters
   */
  public static final FragletNetParameters PARAMETERS = new FragletNetParameters(
      INSTRUCTION_SET,// 
      1000,// max total fraglets
      100,// max differnt fraglets
      MIN_VM_COUNT, MAX_VM_COUNT,// 
      null,// ElectionFragletNetVMFactory.DEFAULT_FRAGLET_ELECTION_VM_FACTORY,
      MAX_MESSAGES,// max messages
      MAX_FRAGLET_LENGTH,// max fraglet size in symbols
      MIN_DELAY, MAX_DELAY);

  /**
   * the embryogeny
   */
  public static final IEmbryogeny<?, ?> EMBRYOGENY = new FragletInt2StringEmbryogeny(
      INSTRUCTION_SET);

  /**
   * the editor
   */
  public static final Int2StringEditor EDITOR = new Int2StringEditor(2,
      10, 1, MAX_FRAGLET_LENGTH, 1);

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
  public ElectionFragletTestSeries(final Object dir) {
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
  public ElectionFragletTestSeries(final Object dir, final int steps) {
    super(dir, steps);

    if (s_filter == null) {
      s_filter = new FirstObjectiveZeroFilter(this
          .createIndividualEvaluator());
    }

    this.setSuccessFilter(s_filter);

    // this.selectForLogging(LoggingSelection.ALL_INDIVIDUALS);
    // this.selectForLogging(LoggingSelection.ALL_OBJECTIVES);
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
    return new FragletElectionProvider(PARAMETERS, randomized, sc);
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
    return new StringCreator<int[][]>(EDITOR);
  }

  /**
   * create the mutator
   * 
   * @return the mutator
   */
  @Override
  protected IMutator<?> createMutator() {
    return new VariableLengthStringMutator<int[][]>(EDITOR);
  }

  /**
   * create the crossover
   * 
   * @return the crossover
   */
  @Override
  @SuppressWarnings("unchecked")
  protected ICrossover<?> createCrossover() {
    return new IterativeMultiCrossover<int[][]>(//
        new ICrossover[] {//
        new DefaultStringCrossover<int[][]>(EDITOR, true),//
            new Int2StringProcCrossover(EDITOR) },// 
        new double[] { 1, 1 });
    // new VariableLengthStringNPointCrossover<int[][]>(EDITOR);
  }

  // /**
  // * Obtain the minimum weight of a program
  // *
  // * @return the minimum weight of a program
  // */
  // @Override
  // protected int getMinWeight() {
  // return 12;
  // }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    new ElectionFragletTestSeries("").start(); //$NON-NLS-1$
    // new ElectionFragletTestSeries("e:\\temp\\k\\").start(); //
    // //$NON-NLS-1$
  }

  // /**
  // * Create the objective functions
  // *
  // * @return the list of objective functions
  // */
  // @Override
  // @SuppressWarnings("unchecked")
  // protected List<?> createObjectives() {
  // List<Object> l1, l2;
  // int i, j, k;
  // Object o;
  //
  // l1 = ((List) (super.createObjectives()));
  // l2 = CollectionUtils.createList();
  //
  // i = l1.size();
  // k = -1;
  // for (j = 0; j < i; j++) {
  // o = l1.get(j);
  // if ((o instanceof MaximizeIdleTimeObjective)) {
  // k = j;
  // continue;
  // }
  // l2.add(o);
  // }
  //
  // if (k < 0)
  // k = l2.size();
  // l2.add(k, ReactivenessObjective.REACTIVENESS_OBJECTIVE);
  //
  // return l2;
  // }
}

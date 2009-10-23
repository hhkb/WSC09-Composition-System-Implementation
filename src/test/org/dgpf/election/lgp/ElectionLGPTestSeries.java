/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-23
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.lgp.ElectionLGPTestSeries.java
 * Last modification: 2007-09-23
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

package test.org.dgpf.election.lgp;

import org.dgpf.lgp.base.genome.LGPInt2StringEmbryogeny;
import org.dgpf.lgp.base.genome.plain.Int2StringLGPCreator;
import org.dgpf.lgp.base.genome.plain.Int2StringLGPCrossover;
import org.dgpf.lgp.base.genome.plain.Int2StringLGPEditor;
import org.dgpf.lgp.base.genome.plain.Int2StringLGPMutator;
import org.dgpf.lgp.net.LGPNetParameters;
import org.dgpf.lgp.net.LGPNetVMInstructionSet;
import org.dgpf.vm.net.NetworkProvider;
import org.sigoa.refimpl.utils.testSeries.successFilters.FirstObjectiveZeroFilter;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;

import test.org.dgpf.election.ElectionTestSeries;
import test.org.dgpf.election.rbgp.ElectionRBGPTestSeries;

/**
 * The test series for the election rbgp algorithm.
 * 
 * @author Thomas Weise
 */
public class ElectionLGPTestSeries extends ElectionTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * the symbol set to use
   */
  public static final LGPNetVMInstructionSet INSTRUCTION_SET = new LGPNetVMInstructionSet(
      true);

  /**
   * the maximum delay
   */
  public static final int MAX_DELAY = (2 * MAX_VM_COUNT);

  /**
   * the number of steps
   */
  public static final int STEPS = 2 * ElectionRBGPTestSeries.STEPS;

  /**
   * the shared classifier parameters
   */
  public static final LGPNetParameters PARAMETERS = new LGPNetParameters(
      INSTRUCTION_SET, 2, 2,
      2,
      10,//
      MIN_VM_COUNT, MAX_VM_COUNT,
      ElectionLGPNetVMFactory.DEFAULT_LGP_ELECTION_VM_FACTORY,
      MAX_MESSAGES, MIN_DELAY, MAX_DELAY);

  /**
   * the embryogeny
   */
  public static final IEmbryogeny<?, ?> EMBRYOGENY = new LGPInt2StringEmbryogeny(
      INSTRUCTION_SET);

  /**
   * the editor
   */
  public static final Int2StringLGPEditor EDITOR = new Int2StringLGPEditor(
      2, 4);

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
  public ElectionLGPTestSeries(final Object dir) {
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
  public ElectionLGPTestSeries(final Object dir, final int steps) {
    super(dir, steps);

    if (s_filter == null) {
      s_filter = new FirstObjectiveZeroFilter(this
          .createIndividualEvaluator());
    }

    this.setSuccessFilter(s_filter);
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
    return new LGPElectionProvider(PARAMETERS, randomized, sc);
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
  @SuppressWarnings("unchecked")
  protected ICreator<?> createCreator() {
    return new Int2StringLGPCreator(EDITOR, PARAMETERS);
  }

  /**
   * create the mutator
   * 
   * @return the mutator
   */
  @Override
  @SuppressWarnings("unchecked")
  protected IMutator<?> createMutator() {
    return new Int2StringLGPMutator(EDITOR, PARAMETERS);
  }

  /**
   * create the crossover
   * 
   * @return the crossover
   */
  @Override
  @SuppressWarnings("unchecked")
  protected ICrossover<?> createCrossover() {
    return new Int2StringLGPCrossover(EDITOR, PARAMETERS);
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    new ElectionLGPTestSeries("").start(); //$NON-NLS-1$
//     new ElectionHLGPTestSeries("e:\\temp\\y").start(); //$NON-NLS-1$
  }

  /**
   * Obtain the minimum weight of a program
   * 
   * @return the minimum weight of a program
   */
  @Override
  protected int getMinWeight() {
    return 10;
  }

  /**
   * Obtain the minimum weight of a program
   * 
   * @return the minimum weight of a program
   */
  @Override
  protected int getMaxWeight() {
    return 100;
  }
}

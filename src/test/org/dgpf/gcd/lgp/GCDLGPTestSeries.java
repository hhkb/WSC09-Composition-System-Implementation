/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-18
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.lgp.GCDLGPTestSeries.java
 * Last modification: 2007-11-18
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

package test.org.dgpf.gcd.lgp;

import org.dgpf.lgp.base.genome.LGPInt2StringEmbryogeny;
import org.dgpf.lgp.base.genome.plain.Int2StringLGPCreator;
import org.dgpf.lgp.base.genome.plain.Int2StringLGPCrossover;
import org.dgpf.lgp.base.genome.plain.Int2StringLGPEditor;
import org.dgpf.lgp.base.genome.plain.Int2StringLGPMutator;
import org.dgpf.lgp.single.LGPParameters;
import org.dgpf.lgp.single.LGPVMInstructionSet;
import org.sigoa.refimpl.utils.testSeries.successFilters.FirstObjectiveZeroFilter;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;

import test.org.dgpf.gcd.GCDProvider;
import test.org.dgpf.gcd.GCDTestSeries;
import test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries;

/**
 * the linear genetic programming test series for the gcd problem
 * 
 * @author Thomas Weise
 */
public class GCDLGPTestSeries extends GCDTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the instruction set to use
   */
  public static final LGPVMInstructionSet INSTRUCTION_SET = LGPVMInstructionSet.DEFAULT_SINGLE_VM_INSTRUCTION_SET;

  /**
   * the shared classifier parameters
   */
  public static final LGPParameters PARAMETERS = new LGPParameters(
      INSTRUCTION_SET, 4, 4, 4, 10);

  /**
   * the embryogeny
   */
  public static final LGPInt2StringEmbryogeny EMBRYOGENY = new LGPInt2StringEmbryogeny(
      INSTRUCTION_SET);

  /**
   * the editor
   */
  public static final Int2StringLGPEditor EDITOR = new Int2StringLGPEditor(
      1, 3);

  /**
   * the number of steps
   */
  public static final int STEPS = 2 * GCDRBGPTestSeries.STEPS;

  /**
   * the success filter
   */
  private static FirstObjectiveZeroFilter s_filter = null;

  /**
   * Create a new test series.
   * 
   * @param dir
   *          the directory to store the test data in
   */
  public GCDLGPTestSeries(final Object dir) {
    this(dir, STEPS);
  }

  /**
   * Create a new test series.
   * 
   * @param dir
   *          the directory to store the test data in
   * @param steps
   *          the number of steps
   */
  public GCDLGPTestSeries(final Object dir, final int steps) {
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
  protected GCDProvider<?, ?> doCreateSimulationProvider(final int sc,
      final boolean randomized) {
    return new GCDLGPVMProvider(PARAMETERS, this.getCurrentDirectory(),
        randomized, sc);
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
    return new Int2StringLGPCreator(EDITOR, PARAMETERS);
  }

  /**
   * create the mutator
   * 
   * @return the mutator
   */
  @Override
  protected IMutator<?> createMutator() {
    return new Int2StringLGPMutator(EDITOR, PARAMETERS);
  }

  /**
   * create the crossover
   * 
   * @return the crossover
   */
  @Override
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
    new GCDLGPTestSeries("").start(); //$NON-NLS-1$
    // new GCDHLGPTestSeries("e:\\temp\\ssd").start(); //$NON-NLS-1$
  }

}

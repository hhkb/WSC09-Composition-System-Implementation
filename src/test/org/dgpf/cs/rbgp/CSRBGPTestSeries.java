/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-23
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.rbgp.CSRBGPTestSeries.java
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

package test.org.dgpf.cs.rbgp;

import org.dgpf.rbgp.base.genome.RBGPBitStringEmbryogeny;
import org.dgpf.rbgp.net.DefaultNetActionSet;
import org.dgpf.rbgp.net.RBGPNetParameters;
import org.dgpf.vm.net.NetworkProvider;
import org.sigoa.refimpl.genomes.bitString.ByteBitStringEditor;
import org.sigoa.refimpl.genomes.string.StringCreator;
import org.sigoa.refimpl.genomes.string.StringEditor;
import org.sigoa.refimpl.genomes.string.VariableLengthStringMutator;
import org.sigoa.refimpl.genomes.string.VariableLengthStringNPointCrossover;
import org.sigoa.refimpl.utils.testSeries.successFilters.FirstObjectiveZeroFilter;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;

import test.org.dgpf.cs.CSNetworkProvider;
import test.org.dgpf.cs.CSTestSeries;

/**
 * The test series for the election rbgp algorithm.
 * 
 * @author Thomas Weise
 */
public class CSRBGPTestSeries extends CSTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * the symbol set to use
   */
  public static final CSSymbolSet SYMBOL_SET = CSSymbolSet.DEFAULT_CS_SYMBOL_SET;

  /**
   * the symbol set to use
   */
  public static final DefaultNetActionSet ACTION_SET = CSActionSet.DEFAULT_CS_ACTIONS;

  /**
   * the maximum delay
   */
  public static final int MAX_DELAY = (2 * MAX_VM_COUNT);

  /**
   * the number of steps
   */
  public static final int STEPS = // 
  (((MAX_DELAY + 10) * MAX_VM_COUNT * MAX_VM_COUNT) + //
      BASE_CS_COUNT * (BASE_CS_TIME << 1))//
      >>> 1;

  /**
   * the shared classifier parameters
   */
  public static final RBGPNetParameters PARAMETERS = new RBGPNetParameters(
      ACTION_SET, SYMBOL_SET,
      true,//
      MIN_VM_COUNT, MAX_VM_COUNT, 
      RBGPCSVMFactory.DEFAULT_CS_VM_FACTORY,
      MAX_MESSAGES, MIN_DELAY, MAX_DELAY);

  /**
   * the embryogeny
   */
  public static final RBGPBitStringEmbryogeny EMBRYOGENY = new RBGPBitStringEmbryogeny(
      ACTION_SET, SYMBOL_SET);

  /**
   * the editor
   */
  public static final StringEditor<byte[]> EDITOR = new ByteBitStringEditor(
      EMBRYOGENY.getGranularity(), 4, 100);

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
  public CSRBGPTestSeries(final Object dir) {
    super(dir, STEPS);

    // this.selectForLogging(LoggingSelection.ALL_OBJECTIVES);

    // if (s_filter == null) {
    // s_filter = new FirstObjectiveZeroFilter(this
    // .createIndividualEvaluator());
    // }
    //
    // this.setSuccessFilter(s_filter);
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
  @SuppressWarnings("unchecked")
  protected NetworkProvider<?> doCreateSimulationProvider(final int sc,
      final boolean randomized) {
    return new CSNetworkProvider(PARAMETERS, randomized, sc, BASE_CS_TIME);
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
    return new StringCreator<byte[]>(EDITOR);
  }

  /**
   * create the mutator
   * 
   * @return the mutator
   */
  @Override
  @SuppressWarnings("unchecked")
  protected IMutator<?> createMutator() {
    return new VariableLengthStringMutator<byte[]>(EDITOR);
  }

  /**
   * create the crossover
   * 
   * @return the crossover
   */
  @Override
  @SuppressWarnings("unchecked")
  protected ICrossover<?> createCrossover() {
    return new VariableLengthStringNPointCrossover<byte[]>(EDITOR);
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    new CSRBGPTestSeries("").start(); //$NON-NLS-1$
  }
}

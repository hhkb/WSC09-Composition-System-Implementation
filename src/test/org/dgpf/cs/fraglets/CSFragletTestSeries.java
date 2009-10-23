/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-27
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.fraglets.CSFragletTestSeries.java
 * Last modification: 2008-03-27
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

package test.org.dgpf.cs.fraglets;

import java.io.Serializable;

import org.dgpf.fraglets.base.genetics.FragletInt2StringEmbryogeny;
import org.dgpf.fraglets.net.FragletNetParameters;
import org.dgpf.fraglets.net.NetInstructionSet;
import org.dgpf.lgp.base.genome.plain.Int2StringEditor;
import org.dgpf.vm.net.NetworkProvider;
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
import test.org.dgpf.cs.rbgp.CSRBGPTestSeries;

/**
 * the fraglet cs test series
 * 
 * @author Thomas Weise
 */
public class CSFragletTestSeries extends CSTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * the symbol set to use
   */
  public static final NetInstructionSet INSTRUCTIONS = CSInstructionSet.DEFAULT_CS_INSTRUCTION_SET;

  /**
   * the maximum delay
   */
  public static final int MAX_DELAY = CSRBGPTestSeries.MAX_DELAY;

  /**
   * the maximum length of a fraglet
   */
  public static final int MAX_FRAGLET_LENGTH = 15;

  /**
   * the number of steps
   */
  public static final int STEPS = // 
  (((MAX_DELAY + 10) * MAX_VM_COUNT * MAX_VM_COUNT) + //
      BASE_CS_COUNT * (BASE_CS_TIME << 1))//
      >>> 2;

  /**
   * the shared classifier parameters
   */
  public static final FragletNetParameters PARAMETERS = new FragletNetParameters(
      INSTRUCTIONS,// 
      1000,// max total fraglets
      100,// max differnt fraglets
      MIN_VM_COUNT, MAX_VM_COUNT,// 
      CSFragletVMFactory.DEFAULT_CS_VM_FACTORY,//
      MAX_MESSAGES,// max messages
      MAX_FRAGLET_LENGTH,// max fraglet size in symbols
      MIN_DELAY, MAX_DELAY);

  /**
   * the embryogeny
   */
  @SuppressWarnings("unchecked")
  public static final IEmbryogeny<Serializable, Serializable> EMBRYOGENY = (IEmbryogeny) new FragletInt2StringEmbryogeny(
      INSTRUCTIONS);

  /**
   * the editor
   */
  public static final StringEditor<int[][]> EDITOR = new Int2StringEditor(
      4, 10, 1, MAX_FRAGLET_LENGTH, 1);

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
  public CSFragletTestSeries(final Object dir) {
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
  protected ICrossover<?> createCrossover() {
    return new VariableLengthStringNPointCrossover<int[][]>(EDITOR);
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    new CSFragletTestSeries("").start(); //$NON-NLS-1$
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-23
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.rbgp.ElectionRBGPTestSeries.java
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

package test.org.dgpf.election.rbgp;

import org.dgpf.rbgp.base.RBGPProgram;
import org.dgpf.vm.net.NetworkProvider;
import org.sigoa.refimpl.genomes.bitString.ByteBitStringEditor;
import org.sigoa.refimpl.genomes.bitString.redundancy.XorNetworks;
import org.sigoa.refimpl.genomes.string.DefaultStringCrossover;
import org.sigoa.refimpl.genomes.string.StringCreator;
import org.sigoa.refimpl.genomes.string.StringEditor;
import org.sigoa.refimpl.genomes.string.VariableLengthStringMutator;
import org.sigoa.refimpl.utils.testSeries.successFilters.FirstObjectiveZeroFilter;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;

/**
 * The test series for the election rbgp algorithm.
 * 
 * @author Thomas Weise
 */
public class ElectionRBGPTestSeries2 extends ElectionRBGPTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * the embryogeny
   */
  @SuppressWarnings("unchecked")
  public static final XorNetworks<byte[], RBGPProgram> EMBRYOGENY2 = new XorNetworks(
      ElectionRBGPTestSeries.EMBRYOGENY, ElectionRBGPTestSeries.EMBRYOGENY
          .getGranularity());

  /**
   * the editor
   */
  public static final StringEditor<byte[]> EDITOR2 = new ByteBitStringEditor(
      EMBRYOGENY2.getInputGeneSize(), 2, 100);

  /**
   * Create a new test series.
   * 
   * @param dir
   *          the directory to store the test data in
   */
  public ElectionRBGPTestSeries2(final Object dir) {
    super(dir);

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
    return new RBGPElectionProvider(PARAMETERS, randomized, sc);
  }

  /**
   * create the embryogeny
   * 
   * @return the embryogeny
   */
  @Override
  protected IEmbryogeny<?, ?> createEmbryogeny() {
    return EMBRYOGENY2;
  }

  /**
   * create the creator
   * 
   * @return the creator
   */
  @Override
  @SuppressWarnings("unchecked")
  protected ICreator<?> createCreator() {
    return new StringCreator<byte[]>(EDITOR2);
  }

  /**
   * create the mutator
   * 
   * @return the mutator
   */
  @Override
  @SuppressWarnings("unchecked")
  protected IMutator<?> createMutator() {
    return new VariableLengthStringMutator<byte[]>(EDITOR2);
  }

  /**
   * create the crossover
   * 
   * @return the crossover
   */
  @Override
  @SuppressWarnings("unchecked")
  protected ICrossover<?> createCrossover() {
    return new DefaultStringCrossover<byte[]>(EDITOR, true);
    //new VariableLengthStringNPointCrossover<byte[]>(EDITOR2);
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static final void main(String[] args) {
    new ElectionRBGPTestSeries2("").start(); //$NON-NLS-1$
  }
}

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

package test.org.dgpf.election.eRBGP;

import org.dgpf.eRbgp.base.TreeRBGPProgram;
import org.dgpf.eRbgp.base.genetics.ERBGPCreator;
import org.dgpf.eRbgp.base.genetics.ERBGPCrossover;
import org.dgpf.eRbgp.base.genetics.ERBGPMutator;
import org.dgpf.eRbgp.net.DefaultRBGPNetLanguage;
import org.dgpf.rbgp.net.DefaultNetSymbolSet;
import org.dgpf.rbgp.net.RBGPNetParameters;
import org.dgpf.vm.net.NetworkProvider;
import org.sigoa.refimpl.go.embryogeny.CopyEmbryogeny;
import org.sigoa.refimpl.utils.testSeries.successFilters.FirstObjectiveZeroFilter;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;

import test.org.dgpf.election.ElectionTestSeries;
import test.org.dgpf.election.rbgp.ElectionRBGPTestSeries;
import test.org.dgpf.election.rbgp.RBGPElectionProvider;

/**
 * The test series for the election rbgp algorithm.
 * 
 * @author Thomas Weise
 */
public class ElectionERBGPTestSeries extends ElectionTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * the symbol set to use
   */
  public static final DefaultNetSymbolSet SYMBOL_SET = new DefaultNetSymbolSet(
      2, false, 1);

  /**
   * the language to be used
   */
  public static final DefaultRBGPNetLanguage LANGUAGE = new DefaultRBGPNetLanguage(
      SYMBOL_SET);

  /**
   * the shared classifier parameters
   */

  /**
   * the shared classifier parameters
   */
  public static final RBGPNetParameters PARAMETERS = new RBGPNetParameters(
      null, SYMBOL_SET, true,//
      MIN_VM_COUNT, MAX_VM_COUNT, null, MAX_MESSAGES, MIN_DELAY,// 
      ElectionRBGPTestSeries.MAX_DELAY);

  /**
   * the embryogeny
   */
  @SuppressWarnings("unchecked")
  public static final IEmbryogeny<TreeRBGPProgram, TreeRBGPProgram> EMBRYOGENY = ((IEmbryogeny) (CopyEmbryogeny.COPY_EMBRYOGENY));

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
  public ElectionERBGPTestSeries(final Object dir) {
    super(dir, ElectionRBGPTestSeries.STEPS);

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
    return new ERBGPCreator(LANGUAGE);
  }

  /**
   * create the mutator
   * 
   * @return the mutator
   */
  @Override
  @SuppressWarnings("unchecked")
  protected IMutator<?> createMutator() {
    return new ERBGPMutator(LANGUAGE);
  }

  /**
   * create the crossover
   * 
   * @return the crossover
   */
  @Override
  @SuppressWarnings("unchecked")
  protected ICrossover<?> createCrossover() {
    return ERBGPCrossover.ERBGP_CROSSOVER;
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    new ElectionERBGPTestSeries("").start(); //$NON-NLS-1$
  }
}

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

package test.org.dgpf.gcd.hlgp;

import org.dgpf.hlgp.base.DefaultHLGPLanguage;
import org.dgpf.hlgp.base.HLGPProgram;
import org.dgpf.hlgp.base.genetics.HLGPLGPEmbryogeny;
import org.sigoa.refimpl.genomes.tree.reproduction.DefaultTreeMutator;
import org.sigoa.refimpl.genomes.tree.reproduction.NodeFactorySet;
import org.sigoa.refimpl.genomes.tree.reproduction.SubTreeExchangeCrossover;
import org.sigoa.refimpl.genomes.tree.reproduction.TreeCreator;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;

import test.org.dgpf.gcd.lgp.GCDLGPTestSeries;

/**
 * the linear genetic programming test series for the gcd problem
 * 
 * @author Thomas Weise
 */
public class GCDHLGPTestSeries extends GCDLGPTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the instruction set to use
   */
  public static final NodeFactorySet NODE_FACTORIES = DefaultHLGPLanguage.DEFAULT_LANGUAGE;

  /**
   * the embryogeny
   */
  public static final HLGPLGPEmbryogeny HLGP_EMBRYOGENY = new HLGPLGPEmbryogeny(
      GCDLGPTestSeries.PARAMETERS);

  /**
   * the number of steps
   */
  public static final int HLGP_STEPS = 3 * GCDLGPTestSeries.STEPS;

  /**
   * Create a new test series.
   * 
   * @param dir
   *          the directory to store the test data in
   */
  public GCDHLGPTestSeries(final Object dir) {
    super(dir, HLGP_STEPS);
  }

  /**
   * create the embryogeny
   * 
   * @return the embryogeny
   */
  @Override
  protected IEmbryogeny<?, ?> createEmbryogeny() {
    return HLGP_EMBRYOGENY;
  }

  /**
   * create the creator
   * 
   * @return the creator
   */
  @Override
  protected ICreator<?> createCreator() {
    return new TreeCreator(NODE_FACTORIES, 7, 0.45,
        HLGPProgram.EMPTY_PROGRAM);
  }

  /**
   * create the mutator
   * 
   * @return the mutator
   */
  @Override
  protected IMutator<?> createMutator() {
    return new DefaultTreeMutator(((TreeCreator) (this.createCreator())));
  }

  /**
   * create the crossover
   * 
   * @return the crossover
   */
  @Override
  protected ICrossover<?> createCrossover() {
    return SubTreeExchangeCrossover.SUB_TREE_EXCHANGE_CROSSOVER;
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    // new GCDHLGPTestSeries("").start(); //$NON-NLS-1$
    new GCDHLGPTestSeries("e:\\temp\\gcdx").start(); //$NON-NLS-1$
  }

}

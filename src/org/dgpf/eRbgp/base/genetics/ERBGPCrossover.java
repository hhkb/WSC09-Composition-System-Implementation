/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.eRbgp.base.genetics.ERBGPCrossover.java
 * Last modification: 2008-04-18
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

package org.dgpf.eRbgp.base.genetics;

import org.dgpf.eRbgp.base.InternalProgram;
import org.dgpf.eRbgp.base.TreeRBGPProgram;
import org.sigoa.refimpl.genomes.tree.reproduction.SubTreeExchangeCrossover;
import org.sigoa.refimpl.go.reproduction.Crossover;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * Create a new tree rbgp crossover
 * 
 * @author Thomas Weise
 */
public class ERBGPCrossover extends Crossover<TreeRBGPProgram> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the shared instance of the crossover
   */
  public static final ICrossover<TreeRBGPProgram> ERBGP_CROSSOVER = new ERBGPCrossover();

  /**
   * Create the crossover
   * 
   * @param language
   *          the default language
   */
  private ERBGPCrossover() {
    super();
  }

  /**
   * Perform one single recombination/crossover. This simple default
   * implementation just returns one of the two source genotypes chosen
   * randomly.
   * 
   * @param source1
   *          The first source genotype.
   * @param source2
   *          The second source genotype.
   * @param random
   *          The randomizer to be used.
   * @return The resulting genotype.
   * @throws NullPointerException
   *           if <code>source1==null||source2==null||random==null</code>.
   */
  @Override
  public TreeRBGPProgram crossover(final TreeRBGPProgram source1,
      final TreeRBGPProgram source2, final IRandomizer random) {
    InternalProgram in1, in2, r;

    in1 = source1.m_program;
    in2 = source2.m_program;

    r = ((InternalProgram) (SubTreeExchangeCrossover.SUB_TREE_EXCHANGE_CROSSOVER
        .crossover(in1, in2, random)));
    if ((r != null) && (r != in1) && (r != in2)) {
      return new TreeRBGPProgram(r);
    }

    return random.nextBoolean() ? source1 : source2;
  }
}

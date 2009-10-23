/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-19
 * Crossover          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.reproduction.PostProcessingCrossover.java
 * Last modification: 2007-11-19
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

package org.sigoa.refimpl.go.reproduction;

import java.io.Serializable;

import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * A crossover that postprocesses the results of another crossover,
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public class PostProcessingCrossover<G extends Serializable> extends
    Crossover<G> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the real crossover
   */
  private final ICrossover<G> m_crossover;

  /**
   * Create a new postprocessing crossover.
   * 
   * @param crossover
   *          the true crossover
   */
  public PostProcessingCrossover(final ICrossover<G> crossover) {
    super();
    this.m_crossover = crossover;
  }

  /**
   * The postprocessing routing.
   * 
   * @param genotype
   *          the genotype
   * @return the postprocessed genotype.
   */
  protected G postprocess(final G genotype) {
    return genotype;
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
  public G crossover(final G source1, final G source2,
      final IRandomizer random) {
    return this.postprocess(this.m_crossover.crossover(source1, source2,
        random));
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-19
 * Mutator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.reproduction.PostProcessingMutator.java
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

import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * A mutator that postprocesses the results of another mutator,
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public class PostProcessingMutator<G extends Serializable> extends
    Mutator<G> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the real mutator
   */
  private final IMutator<G> m_mutator;

  /**
   * Create a new postprocessing mutator.
   * 
   * @param mutator
   *          the true mutator
   */
  public PostProcessingMutator(final IMutator<G> mutator) {
    super();
    this.m_mutator = mutator;
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
   * Perform one single mutation.
   * 
   * @param source
   *          The source genotype.
   * @param random
   *          The randomizer to be used.
   * @return The resulting genotype.
   * @throws NullPointerException
   *           if <code>source==null||random==null</code>.
   */
  @Override
  public G mutate(final G source, final IRandomizer random) {
    return this.postprocess(this.m_mutator.mutate(source, random));
  }
}

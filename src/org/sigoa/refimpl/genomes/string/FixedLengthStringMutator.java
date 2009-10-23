/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-17
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.string.FixedLengthStringMutator.java
 * Last modification: 2007-11-17
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

package org.sigoa.refimpl.genomes.string;

import java.io.Serializable;

import org.sigoa.refimpl.go.reproduction.IterativeMultiMutator;
import org.sigoa.spec.go.reproduction.IMutator;

/**
 * The fixed length string mutator class unites a good mix of the different
 * mutator types.
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public class FixedLengthStringMutator<G extends Serializable> extends
    IterativeMultiMutator<G> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new fixed length string crossover mix
   * 
   * @param e
   *          the string editor to be used
   * @param usePermutation
   *          defines whether permutation should be used or not
   */
  @SuppressWarnings("unchecked")
  public FixedLengthStringMutator(final StringEditor<G> e,
      final boolean usePermutation) {
    super(usePermutation ? //        
    new IMutator[] { new StringConsecutiveElementMutator<G>(e),
        new StringRandomElementMutator<G>(e),
        new StringRandomGenePermutator<G>(e) }
        : //
        new IMutator[] { new StringConsecutiveElementMutator<G>(e),
            new StringRandomElementMutator<G>(e) },//
        usePermutation ? //
        new double[] { 0.3d, 0.3d, 0.4d }
            : //
            new double[] { 0.5d, 0.5d }

    );
  }

  /**
   * Create a new fixed length string crossover mix
   * 
   * @param e
   *          the string editor to be used
   */
  @SuppressWarnings("unchecked")
  public FixedLengthStringMutator(final StringEditor<G> e) {
    this(e, true);
  }
}

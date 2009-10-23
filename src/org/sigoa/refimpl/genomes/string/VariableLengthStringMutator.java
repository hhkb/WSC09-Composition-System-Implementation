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
 * The variable length string mutator class unites a good mix of the
 * different mutator types.
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public class VariableLengthStringMutator<G extends Serializable> extends
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
   */
  @SuppressWarnings("unchecked")
  public VariableLengthStringMutator(final StringEditor<G> e) {
    super(new IMutator[] { new StringConsecutiveElementMutator<G>(e),
        new StringRandomElementMutator<G>(e),
        new StringInsertionMutator<G>(e), new StringDeletionMutator<G>(e),
        new StringRandomGenePermutator<G>(e), }, //
        new double[] { 0.225d, 0.225d, 0.05d, 0.15d, 0.35d });
  }
}

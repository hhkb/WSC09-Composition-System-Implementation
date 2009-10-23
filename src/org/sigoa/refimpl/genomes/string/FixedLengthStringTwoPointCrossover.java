/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.string.FixedLengthTwoPointStringOnePointCrossover.java
 * Last modification: 2007-11-16
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

import org.sigoa.spec.stoch.IRandomizer;

/**
 * This crossover operation performes a crossover
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public class FixedLengthStringTwoPointCrossover<G extends Serializable>
    extends FixedLengthStringNPointCrossover<G> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Instantiate a new string reproduction operation
   * 
   * @param editor
   *          the string editor
   * @throws IllegalArgumentException
   *           if <code>maxPoints&lt;1</code>
   */
  public FixedLengthStringTwoPointCrossover(final StringEditor<G> editor) {
    super(editor, 1);
  }

  /**
   * Obtain the number of split points to be used
   * 
   * @param random
   *          the randomizer
   * @param max
   *          the maximum number of split points available
   * @return the count of split points to be used
   */
  @Override
  protected int getSplitCount(final IRandomizer random, final int max) {
    return Math.min(2, max);
  }
}

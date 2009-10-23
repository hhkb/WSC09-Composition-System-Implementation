/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-01
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.reproduction.Crossover.java
 * Last modification: 2006-12-01
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

import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The basic <code>ICrossover</code>-implementation.
 * 
 * @param <G>
 *          The genotype.
 * @author Thomas Weise
 */
public class Crossover<G extends Serializable> extends
    ImplementationBase<G, Serializable> implements ICrossover<G> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new crossover.
   */
  public Crossover() {
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
  public G crossover(final G source1, final G source2,
      final IRandomizer random) {
    if ((source1 == null) || (source2 == null))
      throw new NullPointerException();
    return (random.nextBoolean() ? source1 : source2);
  }

}

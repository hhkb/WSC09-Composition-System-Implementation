/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : TODO
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.example.idea1.Mutation.java
 * Last modification: TODO
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

package test.org.sigoa.example.idea1;

import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.stoch.IRandomizer;

import test.org.sigoa.example.TSPData;

/**
 * the mutator
 * 
 * @author Thomas Weise
 */
public class Mutation implements IMutator<int[]> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the exchange rate
   */
  private static final double EXCHG = Math.max(1e-3, (1.0d / Math
      .log(TSPData.CITY_COORD_X.length)));

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
  public int[] mutate(final int[] source, final IRandomizer random) {
    int[] d;
    int c, i, j, v;

    d = source.clone();

    // exchange a random number of cities in the sequence
    for (c = (int) (random.nextExponential(EXCHG)); c >= 0; c--) {

      // pick two tour positions
      i = random.nextInt(d.length);
      do {
        j = random.nextInt(d.length);
      } while (j == i);

      // and exchange them
      v = d[i];
      d[i] = d[j];
      d[j] = v;
    }

    return d;
  }
}

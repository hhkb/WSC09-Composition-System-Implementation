/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : TODO
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.example.idea1.Crossover.java
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

import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * the crossover operation for the tsp problem first selects a crossover
 * point and copies the first parent up to this point. the child is then
 * filled up with the missing cities in the sequence with which they occur
 * in the second parent
 * 
 * @author Thomas Weise
 */
public class Crossover implements ICrossover<int[]> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Perform one single recombination/crossover.
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
  public int[] crossover(final int[] source1, final int[] source2,
      final IRandomizer random) {
    int[] d, p2;
    int i, j, v, l, cp;

    l = source1.length;
    d = new int[l];
    p2 = source2.clone();

    // pick a number between 1 and source parent length-1
    cp = random.nextInt(l - 1) + 1;

    /**
     * copy the first parent
     */
    for (i = 0; i < cp; i++) {
      v = source1[i];
      d[i] = v;

      // delete city from copy of second parent
      for (j = (l - 1); j >= 0; j--) {
        if (v == p2[j]) {
          p2[j] = -1;
          break;
        }
      }
    }

    // copy the rest from the second parent
    j = 0;
    while (i < l) {
      v = p2[j];
      if (v >= 0) {
        d[i++] = v;
      }
      j++;
    }

    return d;
  }
}

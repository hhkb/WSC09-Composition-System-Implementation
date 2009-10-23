/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : TODO
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.example.idea1.Creation.java
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

import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.stoch.IRandomizer;

import test.org.sigoa.example.TSPData;

/**
 * The creation operator creates the initial random population
 * 
 * @author Thomas Weise
 */
public class Creation implements ICreator<int[]> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a single new random genotype
   * 
   * @param random
   *          The randomizer to be used.
   * @return The resulting genotype.
   * @throws NullPointerException
   *           if <code>random==null</code>.
   */
  public int[] create(final IRandomizer random) {
    int[] d;
    int l, v, i, j, a;

    // length of phenotype/genotype = number of cities
    l = TSPData.CITY_COORD_X.length;
    d = new int[l];

    // create initial sequence
    for (i = (l - 1); i >= 0; i--) {
      d[i] = i;
    }

    // mix up the initial cities
    for (v = (int)(Math.sqrt(l) * l); v >= 0; v--) {
      i = random.nextInt(l);
      j = random.nextInt(l);
      a = d[i];
      d[i] = d[j];
      d[j] = a;
    }

    return d;
  }

}

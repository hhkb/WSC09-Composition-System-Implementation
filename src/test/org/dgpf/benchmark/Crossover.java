/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : TODO
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.benchmark.Crossover.java
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

package test.org.dgpf.benchmark;

import org.sfc.math.BinaryMath;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * the alternative crossover
 * 
 * @author Thomas Weise
 */
public class Crossover implements ICrossover<byte[]> {
  /**
   * the serial version uid
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
  public byte[] crossover(final byte[] source1, final byte[] source2,
      final IRandomizer random) {
    int l1, l2, p1, p2, newlen, nbl;
    byte[] res;

    if (source1 == null) {
      if (source2 == null)
        return new byte[0];
      return source2;
    } else if (source2 == null)
      return source1;

    l1 = source1.length << 3;
    l2 = source2.length << 3;

    p1 = random.nextInt(l1) + 1;
    p2 = random.nextInt(l2);

    newlen = p1 + (l2 - p2);
    nbl = (newlen >>> 3);
    if ((nbl << 3) < newlen)
      nbl++;
    res = new byte[nbl];

    BinaryMath.moveBits(source1, 0, res, 0, p1);
    BinaryMath.moveBits(source2, p2, res, p1, l2 - p2);

    return res;

  }
}

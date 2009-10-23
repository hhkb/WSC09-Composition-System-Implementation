/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-10
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.doubleString.reproduction.FixedLengthDoubleStringCrossover.java
 * Last modification: 2006-12-10
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

package org.sigoa.refimpl.genomes.doubleString.reproduction;

import java.util.Arrays;

import org.sigoa.refimpl.genomes.string.FixedLengthStringNPointCrossover;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * A double vector crossover.
 * 
 * @author Thomas Weise
 */
public class FixedLengthDoubleStringCrossover extends
    FixedLengthStringNPointCrossover<double[]> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new double vector crossover.
   * 
   * @param editor
   *          the string editor
   * @throws IllegalArgumentException
   *           if <code>maxPoints&lt;1</code>
   */
  public FixedLengthDoubleStringCrossover(final DoubleStringEditor editor) {
    super(editor);
  }

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
  @Override
  public double[] crossover(final double[] source1,
      final double[] source2, final IRandomizer random) {

    double[] r, min, max;
    int i, j, k, m;
    double n, v;
    double mi, ma;
    final DoubleStringEditor ed;

    if (source1 == null)
      return source2;
    if (source2 == null)
      return source1;

    if (random.nextInt(3) <= 0) {
      r = super.crossover(source1, source2, random);

      if ((r != null) && (!(Arrays.equals(r, source1)))
          && (!(Arrays.equals(r, source1))))
        return r;
    }

    i = source1.length;
    r = new double[i];
    ed = ((DoubleStringEditor) (this.getEditor()));
    min = ed.m_minValues;
    max = ed.m_maxValues;

    for (--i; i >= 0; i--) {
      mi = (i < min.length) ? min[i] : DoubleStringEditor.MIN;
      ma = (i < max.length) ? max[i] : DoubleStringEditor.MAX;
      m = 10000;
      do {

        switch (random.nextInt(10)) {
        case 0: {
          n = source1[i];
          break;
        }

        case 1: {
          n = source2[i];
          break;
        }

        default: {
          if ((--m) <= 0) {
            n = (random.nextBoolean() ? mi : ma);
          } else {

            // approximate a normal distribution
            k = random.nextInt(10) + 1;
            v = 0d;
            for (j = k; j > 0; j--) {
              v += random.nextDouble();
            }
            v /= k;
            if (v < 0d)
              v = 0d;
            else if (v > 1d)
              v = 1d;

            n = ((v * source1[i]) + ((1.0d - v) * source2[i]));
          }
          break;
        }
        }
      } while ((n < mi) || (n > ma));
      r[i] = n;
    }
  
    return r;
  }
}

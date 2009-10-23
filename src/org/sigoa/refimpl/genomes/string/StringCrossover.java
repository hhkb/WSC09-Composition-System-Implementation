/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.string.StringCrossover.java
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * This crossover operation performes a crossover
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public abstract class StringCrossover<G extends Serializable> extends
    StringReproductionOperator<G> implements ICrossover<G> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the crossover point coordinates
   */
  transient int[] m_points;

  /**
   * Instantiate a new string reproduction operation
   * 
   * @param editor
   *          the string editor
   * @param maxPoints
   *          the maximum number of crossover points
   * @throws IllegalArgumentException
   *           if <code>maxPoints&lt;1</code>
   */
  protected StringCrossover(final StringEditor<G> editor,
      final int maxPoints) {
    super(editor);
    if (maxPoints < 1)
      throw new IllegalArgumentException();
    this.init(maxPoints);
  }

  /**
   * initialize this object
   * 
   * @param maxPoints
   *          the count of coordinates needed
   */
  void init(final int maxPoints) {
    this.m_points = new int[maxPoints + 1];
  }

  /**
   * Serialize this crossover.
   * 
   * @param out
   *          the output stream
   * @throws IOException
   *           maybe
   */
  private final void writeObject(final ObjectOutputStream out)
      throws IOException {
    out.defaultWriteObject();
    out.writeInt(this.m_points.length - 1);
  }

  /**
   * Deserialize the crossover.
   * 
   * @param stream
   *          The stream to deserialize from.
   * @throws IOException
   *           if io fucks up.
   * @throws ClassNotFoundException
   *           if the class could not be found.
   */
  private final void readObject(final ObjectInputStream stream)
      throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.init(stream.readInt());
  }

  /**
   * Obtain the maximum number of crossover points
   * 
   * @return the maximum number of crossover points
   */
  public int getMaxCrossoverPoints() {
    return this.m_points.length-1;
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
  protected int getSplitCount(final IRandomizer random, final int max) {
    int i;

    do {
      i = (1 + (int) (random.nextExponential(0.9d)));
    } while (i > max);

    return i;
  }

  /**
   * Obtain the crossover points where to crossover the parents.
   * 
   * @param length
   *          the length of the parent in genes
   * @param random
   *          the randomizer
   * @param points
   *          an integer array to fill in the crossover points of the
   *          parent
   * @param count
   *          the count of split points to generate
   */
  protected void getCrossoverPoints(final int length,
      final IRandomizer random, final int[] points, final int count) {
    int i, sp, j;

    if (count <= 0)
      return;

    for (i = count; i > 0; i--) {
      main: for (;;) {
        sp = (1 + random.nextInt(length - 1));
        for (j = (i + 1); j <= count; j++) {
          if (points[j] == sp)
            continue main;
        }
        break main;
      }

      points[i] = sp;
    }

    Arrays.sort(points, 1, count+1);
  }

}

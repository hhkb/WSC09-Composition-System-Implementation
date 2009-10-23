/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-29
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.fitnessAssignment.RankBasedFitnessAssigner.java
 * Last modification: 2006-11-29
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

package org.sigoa.refimpl.go.fitnessAssignment;

import java.io.Serializable;
import java.util.Arrays;

import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.IIndividual;

/**
 * The rank-based fitness assignment process. Individuals get a fitness
 * which is proportional to their rank in the population. .
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class RankBasedFitnessAssigner<G extends Serializable, PP extends Serializable>
    extends FitnessAssigner<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Assign the fitness.
   *
   * @param source
   *          The buffered source population.
   * @param size
   *          The count of individuals in the population array.
   */
  @Override
  protected void process(final IIndividual<G, PP>[] source, final int size) {
    IComparator c;
    int i, r;
    IIndividual<G,PP> x, y;

    c = this.getComparator();
    Arrays.sort(source, 0, size, c);
    r = 1;

    x = source[0];
    x.setFitness(1);
    this.output(x);

    for (i = 1; i < size; i++) {
      y = x;
      x = source[i];
      if (c.compare(x, y) > 0)
        r++;
      x.setFitness(r);
      this.output(x);
    }
  }
}

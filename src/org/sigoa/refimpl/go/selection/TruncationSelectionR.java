/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.selection.TruncationSelectionR.java
 * Last modification: 2007-06-07
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

package org.sigoa.refimpl.go.selection;

import java.io.Serializable;
import java.util.Arrays;

import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.IIndividual;

/**
 * The truncation selection with replacement.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class TruncationSelectionR<G extends Serializable, PP extends Serializable>
    extends SelectionAlgorithm<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new truncation selection algorithm with replacement.
   *
   * @param fitnessBased
   *          <code>true</code> if and only if this selection algorithm
   *          is fitness based, <code>false</code> if it is comparator
   *          based.
   * @param selectionSize
   *          The initial selection size.
   * @throws IllegalArgumentException
   *           if <code>selectionSize&lt;=0</code>.
   */
  public TruncationSelectionR(final boolean fitnessBased,
      final int selectionSize) {
    super(fitnessBased, selectionSize);
  }

/**
   * Process the buffered population.
   *
   * @param source
   *          The buffered source population.
   * @param size
   *          The count of individuals in the population array.
   */
  @Override
  protected void process(final IIndividual<G, PP>[] source,
      final int size) {
    IComparator c;
    int i, m, dc;
    IIndividual<G, PP> x, y;

    c = this.getComparator();
    Arrays.sort(source, 0, size, c);
    dc = this.getPassThroughCount();

    x = source[0];
    this.output(x);
    if ((--dc) <= 0)
      return;

    for (m = 1; m < size; m++) {
      y = x;
      x = source[m];
      if (c.compare(x, y) > 0)
        break;
      output(x);
      if ((--dc) <= 0)
        return;
    }

    for (;;) {
      for (i = 0; i < m; i++) {
        output(source[i]);
        if ((--dc) <= 0)
          return;
      }

    }
  }

}

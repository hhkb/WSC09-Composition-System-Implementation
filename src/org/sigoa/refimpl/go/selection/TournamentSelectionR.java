/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.selection.TournamentSelectionR.java
 * Last modification: 2006-11-28
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

import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The random selection with replacement.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class TournamentSelectionR<G extends Serializable, PP extends Serializable>
    extends SelectionAlgorithm<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The tournament size.
   */
  private int m_tournamentSize;

  /**
   * Create a new random selection algorithm with replacement.
   *
   * @param fitnessBased
   *          <code>true</code> if and only if this selection algorithm
   *          is fitness based, <code>false</code> if it is comparator
   *          based.
   * @param selectionSize
   *          The initial selection size.
   * @param tournamentSize
   *          The tournament size.
   * @throws IllegalArgumentException
   *           if <code>selectionSize&lt;=0 || tournamentSize&lt;=0 </code>.
   */
  public TournamentSelectionR(final boolean fitnessBased,
      final int selectionSize, final int tournamentSize) {
    super(fitnessBased, selectionSize);
    if (tournamentSize <= 0)
      throw new IllegalArgumentException();
    this.m_tournamentSize = tournamentSize;
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
  protected void process(final IIndividual<G, PP>[] source, final int size) {
    int i, j, ts;
    IRandomizer r;
    IComparator c;
    IIndividual<G, PP> i1, i2;

    c = this.getComparator();
    r = this.getRandomizer();
    ts = this.m_tournamentSize;

    for (i = this.getPassThroughCount(); i > 0; i--) {
      i1 = source[r.nextInt(size)];
      for (j = (ts - 1); j > 0; j--) {
        i2 = source[r.nextInt(size)];
        if (c.compare(i1, i2) > 0)
          i1 = i2;
      }

      this.output(i1);
    }
  }

  /**
   * Set the tournament size.
   *
   * @param size
   *          The new tournament size.
   * @throws IllegalArgumentException
   *           if <code>size&lt;=0 </code>.
   */
  protected void setTournamentSize(final int size) {
    if (size <= 0)
      throw new IllegalArgumentException();
    this.m_tournamentSize = size;
  }

  /**
   * Obtain the tournament size.
   *
   * @return The tournament size.
   */
  protected int getTournamentSize() {
    return this.m_tournamentSize;
  }
}

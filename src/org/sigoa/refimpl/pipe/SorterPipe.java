/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-07-03
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.pipe.SorterPipe.java
 * Last modification: 2007-07-03
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

package org.sigoa.refimpl.pipe;

import java.io.Serializable;
import java.util.Arrays;

import org.sigoa.refimpl.go.comparators.HierarchicalComparator;
import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.IIndividual;

/**
 * This pipe sorts all individuals according to a comparator.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class SorterPipe<G extends Serializable, PP extends Serializable>
    extends BufferedPipe<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The comparator to be used for sorting
   */
  private IComparator m_comparator;

  /**
   * Create a new sorter pipe.
   */
  public SorterPipe() {
    this(null);
  }

  /**
   * Create a new sorter pipe.
   *
   * @param comparator
   *          The comparator to be used for sorting
   */
  public SorterPipe(final IComparator comparator) {
    this(false, comparator);
  }

  /**
   * Create a new sorter pipe.
   *
   * @param removeDuplicates
   *          <code>true</code> if all duplicates should be removed from
   *          the buffer before processing is done.
   * @param comparator
   *          The comparator to be used for sorting
   */
  public SorterPipe(final boolean removeDuplicates,
      final IComparator comparator) {
    super(removeDuplicates);
    this.m_comparator = ((comparator != null) ? comparator
        : HierarchicalComparator.HIERARCHICAL_COMPARATOR);
  }

  /**
   * Get the comparator used for sorting.
   *
   * @return the comparator used for sorting
   */
  @Override
  public IComparator getComparator() {
    return this.m_comparator;
  }

  /**
   * Set the comparator used for sorting.
   *
   * @param comparator
   *          the comparator used for sorting
   * @throws NullPointerException
   *           if and only if <code>comparator==null</code>
   */
  public void setComparator(final IComparator comparator) {
    if (comparator == null)
      throw new NullPointerException();
    this.m_comparator = comparator;
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
    int i;
    Arrays.sort(source, 0, size, this.m_comparator);
    for (i = 0; i < size; i++)
      this.output(source[i]);
  }
}

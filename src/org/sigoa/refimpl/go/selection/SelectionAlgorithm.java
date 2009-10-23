/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.selection.SelectionAlgorithm.java
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

import org.sigoa.refimpl.go.BufferedPassThroughPipe;
import org.sigoa.refimpl.go.comparators.FitnessComparator;
import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.selection.ISelectionAlgorithm;

/**
 * This is the base class of all <code>ISelectionAlgorithm</code>-implementations.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public abstract class SelectionAlgorithm<G extends Serializable, PP extends Serializable>
    extends BufferedPassThroughPipe<G, PP> implements
    ISelectionAlgorithm<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * <code>true</code> if and only if this selection algorithm is fitness
   * based, <code>false</code> if it is comparator based.
   */
  private final boolean m_fitnessBased;

  /**
   * Create a new selection algorithm instance.
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
  protected SelectionAlgorithm(final boolean fitnessBased,
      final int selectionSize) {
    super(selectionSize);
    this.m_fitnessBased = fitnessBased;
  }

  /**
   * Check whether the selection is performed based on the fitness values
   * or on the comparator.
   *
   * @return <code>true</code> if and only if this selection algorithm is
   *         fitness based, <code>false</code> if it is comparator based.
   */
  public boolean isFitnessBased() {
    return this.m_fitnessBased;
  }

  /**
   * Obtain the comparator to be used for the fitness assignment process.
   * This overriden method first checks if the selection is fitness-based.
   * If so, the <code>FitnessComparator.FITNESS_COMPARATOR</code> is
   * returned. Otherwise, it tries to query the current host. If it is run
   * inside a host environment, it returns the comparator of the host's
   * jobinfo's optimization info. Otherwise, the default comparator
   * (pareto-comparator) is returned.
   *
   * @return The comparator to be used for fitness assignment.
   */
  @Override
  protected IComparator getComparator() {
    if (this.m_fitnessBased)
      return FitnessComparator.FITNESS_COMPARATOR;
    return super.getComparator();
  }
}

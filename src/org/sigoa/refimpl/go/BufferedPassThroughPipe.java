/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.BufferedPassThroughPipe.java
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

package org.sigoa.refimpl.go;

import java.io.Serializable;

import org.sigoa.refimpl.pipe.BufferedPipe;
import org.sigoa.spec.go.IPassThroughParameters;

/**
 * This is the base class of all algorithms which act as a gate and let
 * pass through individuals based on a buffered pipe strategy.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public abstract class BufferedPassThroughPipe<G extends Serializable, PP extends Serializable>
    extends BufferedPipe<G, PP> implements IPassThroughParameters {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The pass through count.
   */
  private int m_count;

  /**
   * Create a new pass through algorithm instance.
   *
   * @param count
   *          The initial selection size.
   * @throws IllegalArgumentException
   *           if <code>count&lt;=0</code>.
   */
  protected BufferedPassThroughPipe(final int count) {
    // super();
    // if (count <= 0)
    // throw new IllegalArgumentException();
    // this.m_count = count;
    this(false, count);
  }

  /**
   * Create a new buffered pass through pipe.
   *
   * @param removeDuplicates
   *          <code>true</code> if all duplicates should be removed from
   *          the buffer before processing is done.
   * @param count
   *          The initial selection size.
   * @throws IllegalArgumentException
   *           if <code>count&lt;=0</code>.
   */
  protected BufferedPassThroughPipe(final boolean removeDuplicates,
      final int count) {
    super(removeDuplicates);
    if (count <= 0)
      throw new IllegalArgumentException();
    this.m_count = count;

  }

  /**
   * Set the count of individuals to be passed through
   *
   * @param count
   *          The count of individuals to be written to the ouput pipe per
   *          turn.
   * @throws IllegalArgumentException
   *           if <code>count &lt;= 0</code>
   */
  public void setPassThroughCount(final int count) {
    if (count <= 0)
      throw new IllegalArgumentException();
    this.m_count = count;
  }

  /**
   * Obtain the count of individuals to be passed through this pipe level.
   * This may be more or less than the individuals actually passed to the
   * pipe.
   *
   * @return The count of individuals to be written to the ouput pipe per
   *         turn.
   */
  public int getPassThroughCount() {
    return this.m_count;
  }
}

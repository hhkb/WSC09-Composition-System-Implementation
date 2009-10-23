/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-29
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.ParameterIterator.java
 * Last modification: 2007-09-29
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

package org.sigoa.refimpl.utils.testSeries;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.sfc.collections.iterators.IteratorBase;
import org.sigoa.refimpl.stoch.Randomizer;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * This class is used to iterate over different parameter settings
 * 
 * @author Thomas Weise
 */
public class ParameterIterator extends IteratorBase<Object[]> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the parameter matrix
   */
  private final Object[][] m_matrix;

  /**
   * the current matrix indexes.
   */
  private int[] m_indexes;

  /**
   * the current configuration.
   */
  private final Object[] m_config;

  /**
   * Create a new parameter iterator.
   * 
   * @param matrix
   *          the matrix of possible values (each column contains one
   *          parameter type)
   */
  public ParameterIterator(final Object[][] matrix) {
    super();

    int[] ints;
    Object[] o;
    int i;
    IRandomizer r;

    this.m_matrix = matrix;

    r = new Randomizer();

    i = matrix.length;
    this.m_indexes = ints = new int[i];
    this.m_config = o = new Object[i];

    for (--i; i >= 0; i--) {
      ints[i] = r.nextInt(matrix[i].length);
      o[i] = matrix[i][0];
    }

    this.next();
  }

  /**
   * Obtain the number of maximum possible combinations
   * 
   * @return the number of maximum possible combinations
   */
  @Override
  public int getCombinationCount() {
    int c;
    int[] indexes;

    indexes = this.m_indexes.clone();

    c = 0;
    do {
      c++;
      this.next();
    } while (!(Arrays.equals(indexes, this.m_indexes)));

    return c;
  }

  /**
   * Returns <tt>true</tt> if the iteration has more elements. (In other
   * words, returns <tt>true</tt> if <tt>next</tt> would return an
   * element rather than throwing an exception.)
   * 
   * @return <tt>true</tt> if the iterator has more elements.
   */
  public final boolean hasNext() {
    return true;
  }

  /**
   * Returns the next element in the iteration. Calling this method
   * repeatedly until the {@link #hasNext()} method returns false will
   * return each element in the underlying collection exactly once.
   * 
   * @return the next element in the iteration.
   * @exception NoSuchElementException
   *              iteration has no more elements.
   */
  public Object[] next() {
    final Object[] config;
    final Object[][] matrix;
    final int[] ints;
    final int len;
    int i, j;
    boolean b;

    ints = this.m_indexes;
    len = ints.length;
    config = this.m_config;
    matrix = this.m_matrix;

    do {
      b = true;
      for (i = (len - 1); i >= 0; i--) {
        j = ints[i];
        if (b)
          ints[i] = (++j);

        if (j >= matrix[i].length) {
          ints[i] = 0;
          j = 0;
        } else
          b = false;

        config[i] = matrix[i][j];
      }

    } while (!(this.checkConfiguration(config)));

    return config;
  }

  /**
   * Check whether the given parameter configuration is correct
   * 
   * @param config
   *          the configuration
   * @return <code>true</code> if and only if the configuration is
   *         correct, <code>false</code> otherwise
   */
  protected boolean checkConfiguration(final Object[] config) {
    return true;
  }

}

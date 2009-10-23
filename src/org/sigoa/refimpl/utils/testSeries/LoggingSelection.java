/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-17
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.LoggingSelection.java
 * Last modification: 2007-10-17
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

import org.sfc.text.TextUtils;

/**
 * this enumeration helps to select what information should be logged
 * 
 * @author Thomas Weise
 */
public final class LoggingSelection {

  /**
   * the synchronizer
   */
  private static final Object SYNC = new Object();

  /**
   * the index
   */
  private static long s_index = 0;

  /**
   * log all individuals that are built during the evolution run
   */
  public static final LoggingSelection ALL_INDIVIDUALS = //
  new LoggingSelection("allIndividuals"); //$NON-NLS-1$

  /**
   * log all objective values that occure during the evolution run
   */
  public static final LoggingSelection ALL_OBJECTIVES = //
  new LoggingSelection("allObjectives");//$NON-NLS-1$

  /**
   * log the resulting individuals
   */
  public static final LoggingSelection RESULT_INDIVIDUALS = //
  new LoggingSelection("resultIndividuals");//$NON-NLS-1$

  /**
   * log the resulting objectives
   */
  public static final LoggingSelection RESULT_OBJECTIVES = //
  new LoggingSelection("resultObjectives");//$NON-NLS-1$

  /**
   * log the success after the optimizer has finished
   */
  public static final LoggingSelection FINAL_SUCCESS = //
  new LoggingSelection("finalSuccess");//$NON-NLS-1$

  /**
   * observe the success throughout all the generations and abort the
   * optimizer if it has found a non-overfitted result
   */
  public static final LoggingSelection OBSERVE_SUCCESS = //
  new LoggingSelection("observeSuccess");//$NON-NLS-1$

  /**
   * observe the success individuals throughout all the generations and
   * abort the optimizer if it has found a non-overfitted result
   */
  public static final LoggingSelection OBSERVE_SUCCESS_INDIVIDUALS = //
  new LoggingSelection(OBSERVE_SUCCESS.getPrefix() + "Individuals");//$NON-NLS-1$

  /**
   * the flag associated with this logging selection..
   */
  final long m_flag;

  /**
   * the prefix associated with this information
   */
  private final String m_prefix;

  /**
   * Create a new logging selection
   * 
   * @param prefix
   *          the prefix associated with this logging information
   */
  public LoggingSelection(final String prefix) {
    super();
    synchronized (SYNC) {
      this.m_flag = (1L << (s_index++));
    }
    this.m_prefix = TextUtils.preprocessString(prefix);
  }

  /**
   * Obtain the prefix associated with this logging information
   * 
   * @return the prefix associated with this logging information
   */
  public final String getPrefix() {
    return this.m_prefix;
  }
}

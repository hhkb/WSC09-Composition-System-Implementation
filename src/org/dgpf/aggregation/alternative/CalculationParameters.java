/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.simulation.CalculationParameters.java
 * Last modification: 2007-03-27
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

package org.dgpf.aggregation.alternative;

import java.io.Serializable;

/**
 * This class represents the parameters needed by a calculation.
 *
 * @author Thomas Weise
 */
public class CalculationParameters implements Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the variable count
   */
  private final int m_varCnt;

  /**
   * the virtual machine count
   */
  private final int m_vmCnt;

  /**
   * the test count
   */
  private final int m_testCnt;

  /**
   * the step count
   */
  private final int m_stepCnt;

  /**
   * is the data constant or (otherwise) volatile?
   */
  private final boolean m_isConstant;

  /**
   * Create a new calculation parameter record
   *
   * @param varCnt
   *          the count of variables
   * @param vmCnt
   *          the virtual machine count
   * @param testCnt
   *          the test count
   * @param stepCnt
   *          the step count
   * @param isConstant
   *          is the data constant or (otherwise) volatile?
   */
  public CalculationParameters(final int varCnt, final int vmCnt,
      final int testCnt, final int stepCnt, final boolean isConstant) {
    super();
    this.m_varCnt = varCnt;
    this.m_vmCnt = vmCnt;
    this.m_testCnt = Math.max(Calculation.MIN_TESTS, testCnt);
    this.m_stepCnt = stepCnt;
    this.m_isConstant = isConstant;
  }

  /**
   * Create a new calculation parameter record
   *
   * @param params
   *          the parameters to copy
   */
  public CalculationParameters(final CalculationParameters params) {
    this(params.getVariableCount(), params.getVMCount(), params
        .getTestCount(), params.getStepsPerTest(), params.isConstant());
  }

  /**
   * Check whether the data is constant or (otherwise) volatile?
   *
   * @return <code>true</code> if and only if the is data constant,
   *         <code>false</code> if it is volatile?
   */
  public boolean isConstant() {
    return this.m_isConstant;
  }

  /**
   * Obtain the variable count.
   *
   * @return the variable count
   */
  public int getVariableCount() {
    return this.m_varCnt;
  }

  /**
   * Obtain the virtual machine count
   *
   * @return the virtual machine count
   */
  public int getVMCount() {
    return this.m_vmCnt;
  }

  /**
   * Obtain the test count
   *
   * @return the test count
   */
  public int getTestCount() {
    return this.m_testCnt;
  }

  /**
   * Obtain the count of steps per test
   *
   * @return the count of steps per test
   */
  public int getStepsPerTest() {
    return this.m_stepCnt;
  }
}

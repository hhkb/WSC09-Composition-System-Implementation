/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-09
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.CollisionObjective.java
 * Last modification: 2007-10-09
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

package test.org.dgpf.byzantine.agreement.eRGBP;

import org.dgpf.rbgp.base.RBGPMemory;

import test.org.dgpf.byzantine.agreement.AgreementObjective;

/**
 * An objective function for cs-protection algorithms counting the number
 * of violations.
 * 
 * @author Thomas Weise
 */
public class RBGPAgreementObjective extends AgreementObjective<RBGPMemory> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the result index
   */
  static final int RESULT_INDEX = AgreementERBGPTestSeries.SYMBOL_SET
      .getVariable(0).m_id;

  /**
   * Create the objective function
   * 
   * @param requiredSteps
   *          the steps required
   */
  public RBGPAgreementObjective(final int requiredSteps) {
    super(requiredSteps, true);
  }

  /**
   * Create the objective function
   * 
   * @param requiredSteps
   *          the steps required
   * @param usesHysteresis
   */
  public RBGPAgreementObjective(final int requiredSteps,
      final boolean usesHysteresis) {
    super(requiredSteps, usesHysteresis);
  }

  /**
   * read the result from the memory
   * 
   * @param mem
   *          the memory
   * @return the result
   */
  @Override
  protected int read(final RBGPMemory mem) {
    return mem.m_mem1[RESULT_INDEX];
  }

}

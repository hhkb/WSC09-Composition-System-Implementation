/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-24
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.IGCDResultInfo.java
 * Last modification: 2007-09-24
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

package test.org.dgpf.gcd;

import org.dgpf.vm.objectives.IVirtualMachineSimulationInformation;
import org.dgpf.vm.objectives.error.IErrorInformation;

/**
 * the gcd result info
 * 
 * @author Thomas Weise
 */
public interface IGCDResultInfo extends
    IVirtualMachineSimulationInformation, IErrorInformation {

  /**
   * obtain the result value
   * 
   * @return the result value
   */
  public abstract int getGCD();

  /**
   * obtain the result value
   * 
   * @return the result value
   */
  public abstract int getGCDResult();

  /**
   * obtain the first value
   * 
   * @return the first value
   */
  public abstract int getA();

  /**
   * obtain the second value
   * 
   * @return the second value
   */
  public abstract int getB();

}

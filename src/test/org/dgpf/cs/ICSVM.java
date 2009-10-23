/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-11
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.ICSVM.java
 * Last modification: 2008-03-11
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

package test.org.dgpf.cs;

import org.dgpf.vm.net.objectives.INetVirtualMachineSimulationInformation;

/**
 * The critical section vm interface
 * 
 * @author Thomas Weise
 */
public interface ICSVM extends INetVirtualMachineSimulationInformation {

  /**
   * Obtain the number of times this vm has been inside the critical
   * section.
   * 
   * @return the number of times this vm has been inside the critical
   *         section
   */
  public abstract int getCSTimes();
}

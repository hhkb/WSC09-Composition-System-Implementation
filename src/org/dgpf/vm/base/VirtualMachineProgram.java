/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-08-29
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.VirtualMachineProgram.java
 * Last modification: 2007-08-29
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

package org.dgpf.vm.base;

import java.io.Serializable;

import org.sfc.text.JavaTextable;

/**
 * The base class for programs for a virtual machine.
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @author Thomas Weise
 */
public abstract class VirtualMachineProgram<MT extends Serializable>
    extends JavaTextable implements Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Obtain the size of this program which is the total number of primitive
   * instructions in it.
   * 
   * @return the size of this program
   */
  public abstract int getSize();

  /**
   * Obtain the weight of this program which, different to
   * {@link #getSize()}, may incorporate some sort of complexity measure.
   * 
   * @return the weight of this program
   */
  public int getWeight() {
    return this.getSize();
  }

  /**
   * Obtain the number of useless instructions in this program.
   * 
   * @return the number of useless instructions in this program
   */
  public int getUselessCount() {
    return 0;
  }

  /**
   * Check whether this program is useless.
   * 
   * @return <code>true</code> if this program is useless,<code>false</code>
   *         otherwise
   */
  public boolean isUseless() {
    return (this.getSize() <= 2);
  }
}

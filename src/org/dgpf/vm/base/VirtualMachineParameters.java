/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-08-29
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.VirtualMachineParameters.java
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
 * This class encapsulates a set of virtual machine parameters
 * 
 * @param <MT>
 *          the virtual machine memory type, for example <code>int[]</code>
 *          or <code>double[]</code>
 * @author Thomas Weise
 */
public class VirtualMachineParameters<MT extends Serializable> extends
    JavaTextable implements Serializable, IVirtualMachineParameters<MT> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the memory size
   */
  protected final int m_memorySize;

  /**
   * Create a new virtual machine parameter set.
   * 
   * @param memorySize
   *          the memory size
   */
  @SuppressWarnings("unchecked")
  public VirtualMachineParameters(final int memorySize) {
    super();

    this.m_memorySize = Math.max(1, memorySize);
  }

  /**
   * Create a new virtual machine parameter set by copying another one.
   * 
   * @param copy
   *          the parameters to copy
   */
  public VirtualMachineParameters(final IVirtualMachineParameters<MT> copy) {
    this(copy.getMemorySize());
  }

  /**
   * Obtain the memory size defined in this parameter set.
   * 
   * @return the memory size defined in this parameter set
   */
  public final int getMemorySize() {
    return this.m_memorySize;
  }

  /**
   * Serializes the parameters of the constructor of this object.
   * 
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  @Override
  protected void javaParametersToStringBuilder(final StringBuilder sb,
      final int indent) {
    sb.append(this.m_memorySize);
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    sb.append("RBGPMemory Size   : "); //$NON-NLS-1$
    sb.append(this.m_memorySize);
  }
}

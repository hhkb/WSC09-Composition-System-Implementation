/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-13
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.HostedVirtualMachine.java
 * Last modification: 2007-09-13
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

import org.sfc.text.TextUtils;

/**
 * A hosted virtual machine.
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the program type
 * @param <HT>
 *          the host type
 * @author Thomas Weise
 */
public abstract class HostedVirtualMachine<MT extends Serializable, PT extends VirtualMachineProgram<MT>, HT extends Serializable>
    extends VirtualMachine<MT, PT> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the host
   */
  protected final HT m_host;

  /**
   * Create a new hosted virtual machine.
   * 
   * @param parameters
   *          the virtual machine parameters
   * @param host
   *          the host
   */
  protected HostedVirtualMachine(
      final IVirtualMachineParameters<MT> parameters, final HT host) {
    super(parameters);
    this.m_host = host;
  }

  /**
   * Obtain the host of this virtual machine.
   * 
   * @return the host of this virtual machine
   */
  public final HT getHost() {
    return this.m_host;
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
    super.toStringBuilder(sb);
    sb.append(TextUtils.LINE_SEPARATOR);
    TextUtils.appendClass(this.m_host.getClass(), sb);
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
    super.javaParametersToStringBuilder(sb, indent);
    sb.append(PARAMETER_SEPARATOR);
    appendJavaObject(this.m_host, sb, indent);
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-14
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.HostedVirtualMachineFactoryWrapper.java
 * Last modification: 2007-09-14
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

/**
 * This helper class transforms a {@link IHostedVirtualMachineFactory} into
 * a {@link IVirtualMachineFactory}.
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the program type
 * @param <HT>
 *          the host type
 * @author Thomas Weise
 */
public class HostedVirtualMachineFactoryWrapper<MT extends Serializable, PT extends VirtualMachineProgram<MT>, HT extends Serializable>
    implements IVirtualMachineFactory<MT, PT> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the host to be used when creating virtual machines
   */
  private final HT m_host;

  /**
   * the host-based factory to delegate to
   */
  final IHostedVirtualMachineFactory<MT, PT, HT, ?> m_factory;

  /**
   * Create a new {@link HostedVirtualMachineFactoryWrapper}.
   * 
   * @param host
   *          the host
   * @param factory
   *          the host-based factory to delegate to
   */
  public HostedVirtualMachineFactoryWrapper(final HT host,
      final IHostedVirtualMachineFactory<MT, PT, HT, ?> factory) {
    super();
    this.m_host = host;
    this.m_factory = factory;
  }

  /**
   * Create a new virtual machine with the given parameters.
   * 
   * @param parameters
   *          the parameters used for virtual machine creation
   * @return the new virtual machine
   */
  public VirtualMachine<MT, PT> createVirtualMachine(
      final VirtualMachineParameters<MT> parameters) {
    return this.m_factory.createVirtualMachine(parameters, this.m_host);
  }
}

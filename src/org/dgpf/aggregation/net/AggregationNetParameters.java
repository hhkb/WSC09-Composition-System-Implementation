/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-21
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.net.AggregationNetParameters.java
 * Last modification: 2007-12-21
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

package org.dgpf.aggregation.net;

import org.dgpf.aggregation.base.DefaultAggregationLanguage;
import org.dgpf.aggregation.base.IAggregationParameters;
import org.dgpf.vm.net.INetVirtualMachineFactory;
import org.dgpf.vm.net.VirtualMachineNetworkParameters;
import org.sigoa.refimpl.genomes.tree.reproduction.NodeFactorySet;

/**
 * The aggregation network virtual machine parameter set.
 * 
 * @author Thomas Weise
 */
public class AggregationNetParameters extends
    VirtualMachineNetworkParameters<double[]> implements
    IAggregationParameters {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the language for aggregation protocols
   */
  private final NodeFactorySet m_language;

  /**
   * Create a new virtual machine network parameter set.
   * 
   * @param language
   *          the language definition
   * @param minVMs
   *          the minimum number of virtual machines
   * @param maxVMs
   *          the maximum number of virtual machines
   * @param factory
   *          the networked virtual machine factory of this parameter
   *          setting
   * @param maxMessages
   *          the maximum messages a node is allowed to send
   * @param minDelay
   *          the minimum message delay
   * @param maxDelay
   *          the maximum message delay
   */
  public AggregationNetParameters(
      final DefaultAggregationLanguage language, final int minVMs,
      final int maxVMs,
      final INetVirtualMachineFactory<double[], ?, ?> factory,
      final int maxMessages, final int minDelay, final int maxDelay) {
    super(
        Math.max(2, language.getVectorSize()),
        minVMs,
        maxVMs,
        (factory != null) ? factory
            : DefaultAggregationNetVMFactory.DEFAULT_AGGREGATION_NET_VM_FACTORY,
        maxMessages, Math.max(2, language.getVectorSize()), minDelay,
        maxDelay);
    this.m_language = (language != null) ? language
        : new DefaultAggregationLanguage(1);
  }

  /**
   * Obtain the language used for aggregation -- the available methods and
   * expressions.
   * 
   * @return the set of allowed constructs for the aggregation
   */
  public NodeFactorySet getLanguage() {
    return this.m_language;
  }
}

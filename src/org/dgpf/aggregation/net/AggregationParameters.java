/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-23
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.aggregation.AggregationParameters.java
 * Last modification: 2007-12-23
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
import org.dgpf.vm.net.INetVirtualMachineFactory;

/**
 * The aggregation network parameters
 * 
 * @author Thomas Weise
 */
public class AggregationParameters extends AggregationNetParameters {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the aggregation function
   */
  final IAggregationFunction m_function;

  /**
   * Create a new virtual machine network parameter set.
   * 
   * @param function
   *          the aggregate function
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
  public AggregationParameters(final IAggregationFunction function,
      final DefaultAggregationLanguage language, final int minVMs,
      final int maxVMs,
      final INetVirtualMachineFactory<double[], ?, ?> factory,
      final int maxMessages, final int minDelay, final int maxDelay) {
    super(language, minVMs, maxVMs, (factory != null) ? factory
        : AggregationVMFactory.AGGREGATION_VM_FACTORY, maxMessages,
        minDelay, maxDelay);
    this.m_function = function;
  }

  /**
   * obtain the aggregation function
   * 
   * @return the aggregation function
   */
  public IAggregationFunction getFunction() {
    return this.m_function;
  }
}

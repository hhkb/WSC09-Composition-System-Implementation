/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.net.base.ClassifierParameters.java
 * Last modification: 2007-05-27
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

package org.dgpf.rbgp.net;

import java.lang.reflect.Field;

import org.dgpf.rbgp.base.ActionSet;
import org.dgpf.rbgp.base.DefaultActionSet;
import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.RBGPProgramBase;
import org.dgpf.vm.net.INetVirtualMachineFactory;
import org.sfc.text.TextUtils;
import org.sfc.utils.Classes;

/**
 * This class is a container for all parameters relevant to a symbolic
 * classifier
 * 
 * @author Thomas Weise
 */
public class RBGPNetParameters extends RBGPNetParametersBase {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the default classifier parameters
   */
  public static final RBGPNetParameters DEFAULT_PARAMETERS = new RBGPNetParameters(
      null, null, true,// actions,symbols,bufferedMemory
      8, // minVMs,
      16, // maxVMs,
      null,// factory
      512,// max messages
      1,// min delay
      20// max delay
  );

  /**
   * the actions
   */
  private final ActionSet m_actions;

  /**
   * Create a new classifier parameter set
   * 
   * @param actions
   *          the classifier actions
   * @param symbols
   *          the symbols available to the classifier
   * @param useBufferedMemory
   *          <code>true</code> if and only if buffered memory is to be
   *          used, <code>false</code> otherwise
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
  @SuppressWarnings("unchecked")
  public RBGPNetParameters(
      final ActionSet actions,
      final DefaultNetSymbolSet symbols,
      final boolean useBufferedMemory,
      final int minVMs,
      final int maxVMs,
      final INetVirtualMachineFactory<RBGPMemory, RBGPProgramBase, int[]> factory,
      final int maxMessages, final int minDelay, final int maxDelay) {
    super(symbols, useBufferedMemory, minVMs, maxVMs, factory,
        maxMessages, minDelay, maxDelay);
    this.m_actions = ((actions != null) ? actions
        : DefaultActionSet.DEFAULT_ACTION_SET);
  }

  /**
   * Obtain the actions available to the classifier
   * 
   * @return the actions available to the classifer
   */
  public final ActionSet getActions() {
    return this.m_actions;
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
    Field m;

    m = Classes.findStaticField(this.m_actions);
    if (m != null)
      TextUtils.appendStaticField(m, sb);
    else
      this.m_actions.javaToStringBuilder(sb, indent);// nonsense, but
    // whatever

    sb.append(',');

    super.javaParametersToStringBuilder(sb, indent);
  }
}

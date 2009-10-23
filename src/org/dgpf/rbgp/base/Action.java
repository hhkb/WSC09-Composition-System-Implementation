/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.base.Action.java
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

package org.dgpf.rbgp.base;

import org.dgpf.utils.FixedSetElement;
import org.dgpf.vm.base.EVirtualMachineState;
import org.dgpf.vm.base.VirtualMachine;
import org.dgpf.vm.base.VirtualMachineProgram;

/**
 * An action that can be executed by the classifier
 * 
 * @param <VMT>
 *          the virtual machine type
 * @author Thomas Weise
 */
public abstract class Action<VMT extends VirtualMachine<RBGPMemory, ? extends VirtualMachineProgram<RBGPMemory>>>
    extends FixedSetElement<ActionSet> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new action.
   * 
   * @param actionSet
   *          The action set
   */
  public Action(final ActionSet actionSet) {
    super(actionSet);

  }

  /**
   * Perform this action on a given <code>RBGPNetVM</code>
   * 
   * @param sym1
   *          the first parameter symbol
   * @param sym2
   *          the second parameter symbol
   * @param vm
   *          the virtual machine
   *          
   * @return the resulting virtual machine state
   *          
   */
  public abstract EVirtualMachineState perform(final Symbol sym1, final Symbol sym2, final VMT vm);

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @param sym1
   *          the first parameter symbol
   * @param sym2
   *          the second parameter symbol
   * @see #toString()
   */
  public void toStringBuilder(final StringBuilder sb,
      final Symbol sym1, final Symbol sym2) {
    this.toStringBuilder(sb);
  }

  /**
   * Check whether this action is useless.
   * 
   * @param sym1
   *          the first parameter symbol
   * @param sym2
   *          the second parameter symbol
   * @return <code>true</code> if this action is useless,<code>false</code>
   *         otherwise
   */
  public boolean isUseless(final Symbol sym1, final Symbol sym2) {
    return sym1.m_writeProtected;
  }

  /**
   * Check whether this action equals to an object.
   * 
   * @param o
   *          the object to compare with
   * @return <code>true</code> if and only if this action equals to the
   *         object <code>o</code>
   */
  @Override
  public final boolean equals(final Object o) {
    if (o == this)
      return true;
    return ((o != null) && (o.getClass() == this.getClass()));
  }
}

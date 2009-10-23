/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-13
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.rbgp.EnterCSAction.java
 * Last modification: 2008-03-13
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

package test.org.dgpf.cs.rbgp;

import org.dgpf.rbgp.base.Action;
import org.dgpf.rbgp.base.ActionSet;
import org.dgpf.rbgp.base.Symbol;
import org.dgpf.vm.base.EVirtualMachineState;

/**
 * the enter cs action
 * 
 * @author Thomas Weise
 */
public class EnterCSAction extends Action<RBGPCSVM> {
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
  public EnterCSAction(final ActionSet actionSet) {
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
   * @return the resulting virtual machine state
   */
  @Override
  public EVirtualMachineState perform(final Symbol sym1,
      final Symbol sym2, final RBGPCSVM vm) {
    vm.setCSFlag();
    return EVirtualMachineState.CHANGED;
  }

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
  @Override
  public void toStringBuilder(final StringBuilder sb, final Symbol sym1,
      final Symbol sym2) {
    sb.append("enterCs"); //$NON-NLS-1$
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
  @Override
  public final boolean isUseless(final Symbol sym1, final Symbol sym2) {
    return false;
  }

}

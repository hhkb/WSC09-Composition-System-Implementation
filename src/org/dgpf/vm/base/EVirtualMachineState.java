/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-21
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.EVirtualMachineState.java
 * Last modification: 2007-09-21
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

import org.sfc.text.IJavaTextable;

/**
 * This enumeration denotes the possible states of a virtual machine.
 * 
 * @author Thomas Weise
 */
public enum EVirtualMachineState implements Serializable, IJavaTextable {

  /**
   * Nothing happened in the current step. The virtual machine could, for
   * example, be in a sleep modus.
   */
  NOTHING,
  /**
   * Some of the data was changed in this step, i.e. the virtual machine is
   * currently doing some active processing.
   */
  CHANGED,
  /**
   * The virtual machine has terminated in a graceful way in this step and
   * will not process anything further.
   */
  TERMINATED,
  /**
   * An error occured in the current step. The virtual machine will
   * subsequently terminate.
   */
  ERROR;

  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Serializes this object as string to a java string builder. The string
   * appended to the string builder can be copy-and-pasted into a java file
   * and represents the constructor of this object.
   * 
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  public final void javaToStringBuilder(final StringBuilder sb,
      final int indent) {
    sb.append(this.getClass().getCanonicalName());
    sb.append('.');
    sb.append(this.name());
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  public final void toStringBuilder(final StringBuilder sb) {
    sb.append(this.name());
  }

  /**
   * Combine two virtual machine states.
   * 
   * @param state1
   *          the first state
   * @param state2
   *          the second state
   * @return the composed state
   */
  public static final EVirtualMachineState combineStates(
      final EVirtualMachineState state1, final EVirtualMachineState state2) {
    return ((state1.ordinal() > state2.ordinal()) ? state1 : state2);
  }

  /**
   * Combine two virtual machine states.
   * 
   * @param state1
   *          the first state
   * @param state2
   *          the second state
   * @return the composed state
   */
  public static final EVirtualMachineState updateState(
      final EVirtualMachineState state1, final EVirtualMachineState state2) {
    if (state2.ordinal() > 1) {
      if (state1 == EVirtualMachineState.TERMINATED)
        return state1;
      if (state2 == EVirtualMachineState.ERROR)
        return state2;
      return state1;
    }
    return ((state1.ordinal() > state2.ordinal()) ? state1 : state2);
  }
}

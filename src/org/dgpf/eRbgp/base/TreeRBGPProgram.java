/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-16
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.eRbgp.base.TreeRBGPProgram.java
 * Last modification: 2008-04-16
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

package org.dgpf.eRbgp.base;

import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.RBGPProgramBase;
import org.dgpf.vm.base.EVirtualMachineState;
import org.dgpf.vm.base.VirtualMachine;
import org.sfc.utils.Utils;

/**
 * The rbgp program.
 * 
 * @author Thomas Weise
 */
public class TreeRBGPProgram extends RBGPProgramBase {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the internal program
   */
  public final InternalProgram m_program;

  /**
   * Create a new tree rbgp program.
   * 
   * @param program
   *          the internal program
   */
  public TreeRBGPProgram(final InternalProgram program) {
    super();
    this.m_program = program;
  }

  /**
   * Obtain the size of this program which is the total number of primitive
   * instructions in it.
   * 
   * @return the size of this program
   */
  @Override
  public int getSize() {
    return this.m_program.getWeight();
  }

  /**
   * perform the classification on a vm.
   * 
   * @param vm
   *          the vm to work on
   * @return the resulting virtual machine state
   */
  @Override
  @SuppressWarnings("unchecked")
  public EVirtualMachineState perform(
      final VirtualMachine<RBGPMemory, ? extends RBGPProgramBase> vm) {

    return (this.m_program.execute((VirtualMachine) vm) != 0) ? EVirtualMachineState.CHANGED
        : EVirtualMachineState.NOTHING;
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
    this.m_program.toStringBuilder(sb);
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
    this.m_program.javaToStringBuilder(sb, indent);
  }

  /**
   * check for equality
   * 
   * @param o
   *          the object to compare with
   * @return <code>true</code> if and only if the two objects equal
   */
  @Override
  public boolean equals(final Object o) {
    if (o == this)
      return true;
    if (!(o instanceof TreeRBGPProgram))
      return false;
    return Utils
        .testEqual(this.m_program, ((TreeRBGPProgram) o).m_program);
  }

}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-15
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.base.Instruction.java
 * Last modification: 2007-12-15
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

package org.dgpf.fraglets.base;

import org.dgpf.utils.FixedSetElement;
import org.dgpf.vm.base.VirtualMachine;

/**
 * The basic fraglet instruction type
 * 
 * @param <V>
 *          the virtual machine type
 * @author Thomas Weise
 */
public abstract class Instruction<V extends VirtualMachine<FragletStore, FragletProgram>>
    extends FixedSetElement<InstructionSet> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new instruction for a instruction set.
   * 
   * @param isb
   *          the instruction set builder
   */
  public Instruction(final InstructionSet isb) {
    super(isb);
  }

  /**
   * Execute an instruction on a fraglet store.
   * 
   * @param vm
   *          the virtual machine
   * @param fraglet
   *          the invoking fraglet
   * @return <code>true</code> if the fraglet was destroyed during the
   *         execution of the instruction,<code>false</code> if it
   *         remains as-is in the fraglet store.
   */
  public abstract boolean execute(final V vm, final Fraglet fraglet);

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public abstract void toStringBuilder(final StringBuilder sb);
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-15
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.base.instructions.reactions.Match.java
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

package org.dgpf.fraglets.base.instructions.reactions;

import org.dgpf.fraglets.base.Fraglet;
import org.dgpf.fraglets.base.FragletProgram;
import org.dgpf.fraglets.base.FragletStore;
import org.dgpf.fraglets.base.Instruction;
import org.dgpf.fraglets.base.InstructionSet;
import org.dgpf.vm.base.VirtualMachine;
import org.sfc.math.Mathematics;

/**
 * The duplication instruction duplicates two following instructions.
 * <code>[match a tail1], [a tail2] => [tail1 tail2]</code>
 * <code>[matchp a tail1], [a tail2] => [matchp a tail1], [tail1 tail2]</code>
 * <code>[matchs a tail1], [a tail2] => [a tail2], [tail1 tail2]</code>
 * <code>[matchps a tail1], [a tail2] => [matchps a tail1], [a tail2], [tail1 tail2]</code>
 * 
 * @author Thomas Weise
 */
public class Match extends
    Instruction<VirtualMachine<FragletStore, FragletProgram>> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * <code>true</code> if and only if the first fraglet should be
   * removed, <code>false</code> otherwise
   */
  private final boolean m_removeFirst;

  /**
   * <code>true</code> if and only if the second fraglet should be
   * removed, <code>false</code> otherwise
   */
  private final boolean m_removeSecond;

  /**
   * Create a new dup instruction
   * 
   * @param is
   *          the instruction set
   * @param removeFirst
   *          <code>true</code> if and only if the first fraglet should
   *          be removed, <code>false</code> otherwise
   * @param removeSecond
   *          <code>true</code> if and only if the second fraglet should
   *          be removed, <code>false</code> otherwise
   */
  public Match(final InstructionSet is, final boolean removeFirst,
      final boolean removeSecond) {
    super(is);
    this.m_removeFirst = removeFirst;
    this.m_removeSecond = removeSecond;
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
  @Override
  public boolean execute(
      final VirtualMachine<FragletStore, FragletProgram> vm,
      final Fraglet fraglet) {

    Fraglet fn;
    int[] od, nd;
    int l, l2, ln;

    l = fraglet.m_len;
    if (l <= 1)
      return true;// this.m_removeFirst;

    nd = fraglet.m_code;
    fn = vm.m_memory.findFraglet(Mathematics.modulo(nd[1],
        vm.m_memory.m_instructions.size()), this.m_removeSecond);

    if (fn == null)
      return false;

    od = fn.m_code;
    l2 = fn.m_len;

    ln = ((l2 - 1) + (l - 2));
    if (ln > vm.m_memory.m_maxLength) {
      vm.m_memory.dispose(fn);
      return this.m_removeFirst;
    }

    System.arraycopy(od, 1, od, l - 2, l2 - 1);
    System.arraycopy(nd, 2, od, 0, l - 2);
    fn.m_len = ln;
    vm.m_memory.enterFraglet(fn, vm);

    return this.m_removeFirst;
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
    sb.append("match"); //$NON-NLS-1$

    if (!(this.m_removeFirst))
      sb.append('P');
    if (!(this.m_removeSecond))
      sb.append('S');
  }
}

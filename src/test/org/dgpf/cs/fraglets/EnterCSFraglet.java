/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-27
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.fraglets.EnterCSFraglet.java
 * Last modification: 2008-03-27
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

package test.org.dgpf.cs.fraglets;

import org.dgpf.fraglets.base.Fraglet;
import org.dgpf.fraglets.base.Instruction;
import org.dgpf.fraglets.base.InstructionSet;

/**
 * this fraglet allows the virtual machine to enter the critical section.
 * 
 * @author Thomas Weise
 */
public class EnterCSFraglet extends Instruction<FragletCSVM> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new max instruction
   * 
   * @param is
   *          the instruction set
   */
  public EnterCSFraglet(final InstructionSet is) {
    super(is);
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
  public boolean execute(final FragletCSVM vm, final Fraglet fraglet) {
    Fraglet fn;
    int l;

    vm.enterCS();

    l = fraglet.m_len;
    if (l <= 1)
      return true;

    fn = vm.m_memory.allocate();
    if (fn == null) {
      vm.setError();
      return true;
    }

    System.arraycopy(fraglet.m_code, 1, fn.m_code, 0, --l);
    fn.m_len = l;

    vm.m_memory.enterFraglet(fn, vm);
    return true;
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
    sb.append("enterCS"); //$NON-NLS-1$
  }

}

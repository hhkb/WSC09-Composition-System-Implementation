/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-15
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.base.instructions.Symbol.java
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

package org.dgpf.fraglets.base.instructions;

import org.dgpf.fraglets.base.Fraglet;
import org.dgpf.fraglets.base.FragletProgram;
import org.dgpf.fraglets.base.FragletStore;
import org.dgpf.fraglets.base.Instruction;
import org.dgpf.fraglets.base.InstructionSet;
import org.dgpf.vm.base.VirtualMachine;

/**
 * The symbol instruction simple defines a symbol inside the fraglet
 * 
 * @author Thomas Weise
 */
public class Symbol extends
    Instruction<VirtualMachine<FragletStore, FragletProgram>> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the symbol name
   */
  private final char[] m_name;
  
/**
 * should this symbol be persistent or not?
 */
  private final boolean m_keep;

  /**
   * Create a named symbol
   * 
   * @param is
   *          the instruction set
   * @param name
   *          the symbol's name
   *          @param keep should this symbol be persistent or not?
   */
  public Symbol(final InstructionSet is, final String name, final boolean keep) {
    super(is);
    this.m_name = name.toCharArray();
    this.m_keep = keep;
  }

  /**
   * Create an unnamed symbol
   * 
   * @param is
   *          the instruction set
   */
  public Symbol(final InstructionSet is) {
    this(is, "symbol_" + getSymbolCount(is), false); //$NON-NLS-1$
  }

  /**
   * Obtain the number of symbols in the instruction set
   * 
   * @param is
   *          the instruction set
   * @return the number of symbols in the instruction set
   */
  private static final int getSymbolCount(final InstructionSet is) {
    int i, c;
    synchronized (is) {
      c = 0;
      for (i = (is.size() - 2); i >= 0; i--) {
        if (is.get(i) instanceof Symbol)
          c++;
      }
    }
    return c;
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
    return this.m_keep;
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
    sb.append(this.m_name);
  }
}

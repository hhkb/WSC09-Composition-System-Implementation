/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-12
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.InstructionSet.java
 * Last modification: 2007-11-12
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

package org.dgpf.lgp.base;

import org.dgpf.lgp.base.instructions.arith.Add;
import org.dgpf.lgp.base.instructions.arith.And;
import org.dgpf.lgp.base.instructions.arith.Compare;
import org.dgpf.lgp.base.instructions.arith.Div;
import org.dgpf.lgp.base.instructions.arith.Exchange;
import org.dgpf.lgp.base.instructions.arith.Mod;
import org.dgpf.lgp.base.instructions.arith.Move;
import org.dgpf.lgp.base.instructions.arith.Mul;
import org.dgpf.lgp.base.instructions.arith.Not;
import org.dgpf.lgp.base.instructions.arith.Or;
import org.dgpf.lgp.base.instructions.arith.Sub;
import org.dgpf.lgp.base.instructions.arith.Xor;
import org.dgpf.utils.FixedSet;

/**
 * The instruction set
 * 
 * @author Thomas Weise
 */
public class InstructionSet extends FixedSet<Instruction<?>> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the empty instruction set.
   */
  @SuppressWarnings("unchecked")
  public static final InstructionSet EMPTY_INSTRUCTION_SET = new InstructionSet(
      false) {
    private static final long serialVersionUID = 1;

    private final Object readResolve() {
      return EMPTY_INSTRUCTION_SET;
    }

    private final Object writeReplace() {
      return EMPTY_INSTRUCTION_SET;
    }

    @Override
    protected final boolean checkRegister(final Instruction<?> element) {
      return false;
    }
  };

  /**
   * the empty instruction set.
   */
  @SuppressWarnings("unchecked")
  public static final InstructionSet DEFAULT_INSTRUCTION_SET = new InstructionSet(
      false) {
    private static final long serialVersionUID = 1;

    private final Object readResolve() {
      return DEFAULT_INSTRUCTION_SET;
    }

    private final Object writeReplace() {
      return DEFAULT_INSTRUCTION_SET;
    }
  };

  /**
   * Create a new instruction set.
   * 
   * @param canTerminate
   *          can programs terminate themselves?
   */
  public InstructionSet(final boolean canTerminate) {
    super();
    this.addDefaultInstructions(canTerminate);
  }

  /**
   * Create the internal array
   * 
   * @param len
   *          the length of the wanted array
   * @return the array
   */
  @SuppressWarnings("unchecked")
  @Override
  protected Instruction<?>[] createArray(final int len) {
    return new Instruction[len];
  }

  /**
   * Add the default instructions to this instruction set.
   * 
   * @param canTerminate
   *          can programs terminate themselves?
   */
  protected void addDefaultInstructions(final boolean canTerminate) {
    new Add(this);
    new And(this);
    new Compare(this);
    new Div(this);
    new Exchange(this);
    new Mod(this);
    new Move(this);
    new Mul(this);
    new Not(this);
    new Or(this);
    new Sub(this);
    new Xor(this);
  }
}

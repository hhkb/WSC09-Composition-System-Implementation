/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-16
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.fraglets.ElectionInstructionSet.java
 * Last modification: 2007-12-16
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

package test.org.dgpf.election.fraglets;

import org.dgpf.fraglets.base.instructions.Symbol;
import org.dgpf.fraglets.base.instructions.arith.Lt;
import org.dgpf.fraglets.base.instructions.arith.Max2;
import org.dgpf.fraglets.net.NetInstructionSet;

/**
 * The instruction set for a (simplified) election algorithm for fraglets
 * 
 * @author Thomas Weise
 */
public class ElectionInstructionSet extends NetInstructionSet {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the empty instruction set.
   */
  public static final ElectionInstructionSet DEFAULT_ELECTION_INSTRUCTION_SET = new ElectionInstructionSet() {
    private static final long serialVersionUID = 1;

    private final Object readResolve() {
      return DEFAULT_ELECTION_INSTRUCTION_SET;
    }

    private final Object writeReplace() {
      return DEFAULT_ELECTION_INSTRUCTION_SET;
    }
  };

  /**
   * the id symbol
   */
  public Symbol m_idSymbol;

  // /**
  // * the result symbol
  // */
  // public final Symbol m_resultSymbol;

  /**
   * Create a new instruction set.
   */
  protected ElectionInstructionSet() {
    super();
  }

  /**
   * Add the default instructions to this instruction set.
   */
  @Override
  protected void addDefaultInstructions() {
    super.addDefaultInstructions();
    new Max2(this);
    new Lt(this);
    this.m_idSymbol = new Symbol(this, "id", false); //$NON-NLS-1$
  }

}

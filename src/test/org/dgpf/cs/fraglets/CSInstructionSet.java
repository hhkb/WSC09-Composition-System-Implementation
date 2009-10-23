/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-27
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.fraglets.CSInstructionSet.java
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

import org.dgpf.fraglets.base.instructions.Symbol;

import test.org.dgpf.election.fraglets.ElectionInstructionSet;

/**
 * the critical section instruction set.
 * 
 * @author Thomas Weise
 */
public class CSInstructionSet extends ElectionInstructionSet {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the empty instruction set.
   */
  public static final CSInstructionSet DEFAULT_CS_INSTRUCTION_SET = new CSInstructionSet() {
    private static final long serialVersionUID = 1;

    private final Object readResolve() {
      return DEFAULT_CS_INSTRUCTION_SET;
    }

    private final Object writeReplace() {
      return DEFAULT_CS_INSTRUCTION_SET;
    }
  };

  /**
   * the id symbol
   */
  public EnterCSFraglet m_enterSymbol;

  /**
   * the id symbol
   */
  public Symbol m_leaveSymbol;

  /**
   * Create a new instruction set.
   */
  protected CSInstructionSet() {
    super();
  }

  /**
   * Add the default instructions to this instruction set.
   */
  @Override
  protected void addDefaultInstructions() {
    super.addDefaultInstructions();
    this.m_leaveSymbol = new Symbol(this, "csLeft", false); //$NON-NLS-1$
    this.m_enterSymbol = new EnterCSFraglet(this);
    new Symbol(this, "multi1", false);//$NON-NLS-1$
    new Symbol(this, "multi2", false);//$NON-NLS-1$
  }

}

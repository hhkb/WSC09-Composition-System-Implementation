/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-16
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.net.NetInstructionSet.java
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

package org.dgpf.fraglets.net;

import org.dgpf.fraglets.base.InstructionSet;
import org.dgpf.fraglets.net.instructions.Broadcast;
import org.dgpf.fraglets.net.instructions.Node;


/**
 * The network instruction set for fraglets.
 * 
 * @author Thomas Weise
 */
public class NetInstructionSet extends InstructionSet {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new instruction set.
   */
  public NetInstructionSet() {
    super();
  }

  /**
   * Add the default instructions to this instruction set.
   */
  @Override
  protected void addDefaultInstructions() {
    super.addDefaultInstructions();
    new Broadcast(this);
    new Node(this);
  }
}

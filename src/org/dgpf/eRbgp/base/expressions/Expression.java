/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-17
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.eRbgp.base.expressions.And.java
 * Last modification: 2008-04-17
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

package org.dgpf.eRbgp.base.expressions;

import org.dgpf.eRbgp.base.Construct;
import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.RBGPProgramBase;
import org.dgpf.vm.base.VirtualMachine;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * the logical and
 * 
 * @author Thomas Weise
 */
public abstract class Expression extends
    Construct<VirtualMachine<RBGPMemory, RBGPProgramBase>> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new node
   * 
   * @param children
   *          the child nodes
   */
  public Expression(final Node[] children) {
    super(children);
  }
}

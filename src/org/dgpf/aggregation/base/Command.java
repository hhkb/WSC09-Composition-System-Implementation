/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.base.Command.java
 * Last modification: 2007-12-18
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

package org.dgpf.aggregation.base;

import org.dgpf.symbolicRegression.vector.real.RealExpression;
import org.dgpf.vm.base.IVirtualMachineParameters;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * The basic command for aggregation protocols.
 * 
 * @author Thomas Weise
 */
public abstract class Command extends RealExpression {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * Create a new node.
   * 
   * @param children
   *          the child nodes
   */
  protected Command(final Node[] children) {
    super(children);
  }

  /**
   * Postprocess this command using the virtual machine parameters.
   * 
   * @param p
   *          the virtual machine parameters
   */
  public void postProcess(final IVirtualMachineParameters<double[]> p) {
    //
  }

  // int i;
  // Node n;
  //
  // for (i = (this.size() - 1); i >= 0; i--) {
  // n = this.get(i);
  // if (n instanceof Command)
  // ((Command) n).postProcess(p);
  // }
  // }

  /**
   * Postprocess this command using the virtual machine parameters.
   * 
   * @param p
   *          the virtual machine parameters
   * @param n
   *          the
   */
  public static final void postProcess(
      final IVirtualMachineParameters<double[]> p, final Node n) {
    int i;

    if (n instanceof Command)
      ((Command) n).postProcess(p);

    for (i = (n.size() - 1); i >= 0; i--) {
      postProcess(p, n.get(i));
    }
  }
}

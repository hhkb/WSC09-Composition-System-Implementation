/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-20
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.eRBGP.EnterCSAction.java
 * Last modification: 2008-04-20
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

package test.org.dgpf.cs.eRBGP;

import org.dgpf.eRbgp.base.actions.Action;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.refimpl.genomes.tree.NodeFactory;
import org.sigoa.spec.stoch.IRandomizer;

import test.org.dgpf.cs.rbgp.RBGPCSVM;

/**
 * the enter cs action
 * 
 * @author Thomas Weise
 */
public class EnterCSAction extends Action<RBGPCSVM> {

  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the globally shared instance of the send action
   */
  public static final Action<RBGPCSVM> ENTER_CS_ACTION = new EnterCSAction();

  /**
   * the globally shared send factory
   */
  public static final INodeFactory ENTER_CS_FACTORY = new NodeFactory(
      EnterCSAction.class, 0, 0) {
    /**
     * the serial version uid
     */
    private static final long serialVersionUID = 1;

    /**
     * Create a new node using the specified children.
     * 
     * @param children
     *          the children of the node to be, or <code>null</code> if
     *          no children are wanted
     * @param random
     *          the randomizer to be used for the node's creation
     * @return the new node
     */
    public Node create(final Node[] children, final IRandomizer random) {
      return ENTER_CS_ACTION;
    }
  };

  /**
   * the text
   */
  private static final char[] TEXT = (EnterCSAction.class
      .getCanonicalName() + //
  ".ENTER_CS_ACTION").toCharArray(); //$NON-NLS-1$

  /**
   * Create a new send action
   */
  EnterCSAction() {
    super(null);
  }

  /**
   * Execute this construct
   * 
   * @param vm
   *          the virtual machine
   * @return an arbitrary return value
   */
  @Override
  public int execute(final RBGPCSVM vm) {
    vm.setCSFlag();
    return -1;
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

  /**
   * Obtain the factory which deals with nodes of the same type as this
   * node.
   * 
   * @return the factory which deals with nodes of the same type as this
   *         node
   */
  @Override
  public INodeFactory getFactory() {
    return ENTER_CS_FACTORY;
  }

  /**
   * Serializes this object as string to a java string builder. The string
   * appended to the string builder can be copy-and-pasted into a java file
   * and represents the constructor of this object.
   * 
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  @Override
  public void javaToStringBuilder(final StringBuilder sb, final int indent) {
    sb.append(TEXT);
  }

}

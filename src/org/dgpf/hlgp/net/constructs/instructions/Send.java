/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.hlgp.net.constructs.instructions.Store.java
 * Last modification: 2007-03-01
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

package org.dgpf.hlgp.net.constructs.instructions;

import org.dgpf.hlgp.base.Instruction;
import org.dgpf.hlgp.base.compiler.HLGPCompiler;
import org.dgpf.lgp.base.ECondition;
import org.sigoa.refimpl.genomes.tree.INodeFactory;

/**
 * This instruction stores a value from an expression into a variable
 * 
 * @author Thomas Weise
 */
public class Send extends Instruction {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the default send instruction
   */
  public static final Instruction SEND = new Send() {
    private static final long serialVersionUID = 1;

    private final Object writeReplace() {
      return SEND;
    }

    private final Object readResolve() {
      return SEND;
    }
  };

  /**
   * Create a new send instruction
   */
  protected Send() {
    super(null);
  }

  /**
   * Transform this program into its human readable representation.
   * 
   * @param sb
   *          the string builder to write to
   * @param indent
   *          the indent
   */
  @Override
  protected void toStringBuilder(final StringBuilder sb, final int indent) {
    sb.append("send"); //$NON-NLS-1$
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
    return SendFactory.SEND_FACTORY;
  }

  /**
   * Check whether this node equals another object.
   * 
   * @param o
   *          the other object
   * @return <code>true</code> if the other object equals this node
   */
  @Override
  public boolean equals(final Object o) {
    return (this == o);
  }

  /**
   * Compile this construct.
   * 
   * @param compiler
   *          the compiler to use
   */
  @Override
  public void compile(final HLGPCompiler compiler) {
    compiler.addInstruction(org.dgpf.lgp.net.instructions.net.Send.class,
        ECondition.encode(ECondition.TRUE), 0, 0);
  }
}

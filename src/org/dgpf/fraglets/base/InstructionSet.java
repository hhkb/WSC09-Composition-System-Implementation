/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-15
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.base.InstructionSet.java
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

package org.dgpf.fraglets.base;

import org.dgpf.fraglets.base.instructions.Marker;
import org.dgpf.fraglets.base.instructions.reactions.Match;
import org.dgpf.fraglets.base.instructions.transformations.Dup;
import org.dgpf.fraglets.base.instructions.transformations.Exchg;
import org.dgpf.fraglets.base.instructions.transformations.Fork;
import org.dgpf.fraglets.base.instructions.transformations.Nop;
import org.dgpf.fraglets.base.instructions.transformations.Null;
import org.dgpf.fraglets.base.instructions.transformations.Pop2;
import org.dgpf.fraglets.base.instructions.transformations.Split;
import org.dgpf.utils.FixedSet;
import org.sfc.text.TextUtils;

/**
 * The basic instruction set class for fraglets
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
  public static final InstructionSet EMPTY_INSTRUCTION_SET = new InstructionSet() {
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
  public static final InstructionSet DEFAULT_INSTRUCTION_SET = new InstructionSet() {
    private static final long serialVersionUID = 1;

    private final Object readResolve() {
      return DEFAULT_INSTRUCTION_SET;
    }

    private final Object writeReplace() {
      return DEFAULT_INSTRUCTION_SET;
    }
  };

  /**
   * the id of the marker symbol
   */
  private int m_markerID;

  /**
   * Create a new instruction set.
   */
  public InstructionSet() {
    super();
    this.addDefaultInstructions();
    this.m_markerID = -1;
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
   */
  protected void addDefaultInstructions() {
    new Marker(this);
    new Dup(this);
    new Exchg(this);
    new Fork(this);
    new Nop(this);
    new Null(this);
    new Pop2(this);
    new Split(this);
    new Match(this, true, true);
    new Match(this, false, true);
  }

  /**
   * Obtain the id of the marker element.
   * 
   * @return the id of the marker element or 0, if no markers are available
   */
  public final int getMarkerId() {
    Instruction<?> i;

    if (this.m_markerID >= 0)
      return this.m_markerID;

    i = this.getInstance(Marker.class, 0);

    return this.m_markerID = (((i != null) ? i.m_id : 0));
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
    int i;
    for (i = 0; i < this.size(); i++) {
      if (i > 0)
        sb.append(TextUtils.LINE_SEPARATOR);
      sb.append(i);
      sb.append(" => "); //$NON-NLS-1$
      this.get(i).toStringBuilder(sb);
    }
  }
}

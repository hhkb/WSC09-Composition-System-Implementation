/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-15
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.base.FragletProgram.java
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

import java.lang.reflect.Field;
import java.util.Arrays;

import org.dgpf.vm.base.VirtualMachineProgram;
import org.sfc.text.TextUtils;
import org.sfc.utils.Classes;

/**
 * The fraglet program
 * 
 * @author Thomas Weise
 */
public class FragletProgram extends VirtualMachineProgram<FragletStore> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The empty program.
   */
  @SuppressWarnings("unchecked")
  public static final FragletProgram EMPTY_PROGRAM = new FragletProgram(
      InstructionSet.EMPTY_INSTRUCTION_SET, new int[1][0]) {
    private static final long serialVersionUID = 1;

    private final Object readResolve() {
      return EMPTY_PROGRAM;
    }

    private final Object writeReplace() {
      return EMPTY_PROGRAM;
    }
  };

  /**
   * the fraglet code
   */
  final int[][] m_code;

  /**
   * the instructions
   */
  private final InstructionSet m_instructions;

  /**
   * Create a new fraglet program
   * 
   * @param code
   *          the fraglet code
   * @param instructionSet
   *          the instruction set
   */
  public FragletProgram(final InstructionSet instructionSet,
      final int[][] code) {
    super();
    this.m_code = code;
    this.m_instructions = instructionSet;
  }

  /**
   * Obtain the total instruction count
   * 
   * @return the total instruction count
   */
  @Override
  public final int getSize() {
    return this.m_code.length;
  }

  /**
   * Obtain the weight of this program which, different to
   * {@link #getSize()}, may incorporate some sort of complexity measure.
   * 
   * @return the weight of this program
   */
  @Override
  public final int getWeight() {
    int[][] x;
    int i, c;

    x = this.m_code;
    c = 0;
    for (i = (x.length - 1); i >= 0; i--) {
      c += x[i].length;
    }

    return c;
  }

  /**
   * Check whether this program equals another object.
   * 
   * @param o
   *          the object
   * @return <code>true</code> if the specified object is the same as
   *         this one
   */
  @Override
  public final boolean equals(final Object o) {
    int[][] code1, code2;
    FragletProgram p;
    int i;

    if (o == this)
      return true;

    if (o instanceof FragletProgram) {
      p = ((FragletProgram) o);
      code1 = this.m_code;
      code2 = p.m_code;

      if (code1 == null) {
        if (code2 == null)
          return true;
        return false;
      }
      if (code2 == null)
        return false;

      i = code1.length;
      if (i != code2.length)
        return false;

      for (--i; i >= 0; i--) {
        if (!(Arrays.equals(code1[i], code2[i])))
          return false;
      }

      return true;
    }
    return false;
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
    final InstructionSet is;
    int[][] cc;
    int i;

    is = this.m_instructions;

    cc = this.m_code;
    for (i = 0; i < cc.length; i++) {
      if (i > 0)
        sb.append(TextUtils.LINE_SEPARATOR);
      fragletToStringBuilder(sb, cc[i], cc[i].length, is);
    }
  }

  /**
   * Obtain the string representation of a given fraglet.
   * 
   * @param sb
   *          the string builder
   * @param fraglet
   *          the fraglet
   * @param len
   *          the fraglet's length
   * @param is
   *          the instruction set imposed on the fraglet
   */
  public static final void fragletToStringBuilder(final StringBuilder sb,
      final int[] fraglet, final int len, final InstructionSet is) {
    int j, k;
    final int s;

    s = is.size();

    if (is != InstructionSet.EMPTY_INSTRUCTION_SET) {
      for (j = 0; j < len; j++) {
        if (j > 0)
          sb.append(':');
        k = fraglet[j];
        if (InstructionUtils.isInstruction(k))
          is.get(InstructionUtils.decodeInstruction(k, s))
              .toStringBuilder(sb);
        else
          sb.append(InstructionUtils.decodeData(k));
      }

      sb.append(" // "); //$NON-NLS-1$
    }

    j = 0;
    do {
      if (j > 0)
        sb.append(':');
      sb.append(fraglet[j]);
      j++;
    } while (j < len);
  }

  /**
   * Serializes the parameters of the constructor of this object.
   * 
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  @Override
  protected void javaParametersToStringBuilder(final StringBuilder sb,
      final int indent) {
    Field m;

    if (this.m_instructions != null) {
      m = Classes.findStaticField(this.m_instructions);

      if (m == null)
        this.m_instructions.javaToStringBuilder(sb, indent);
      else
        TextUtils.appendStaticField(m, sb);
    } else
      sb.append((Object) null);

    sb.append(", new "); //$NON-NLS-1$
    TextUtils.append(this.m_code, sb);
  }

}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-12
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.LGPProgram.java
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

import java.lang.reflect.Field;
import java.util.Arrays;

import org.dgpf.lgp.base.genome.Int2StringPostProcessor;
import org.dgpf.vm.base.VirtualMachineProgram;
import org.sfc.math.Mathematics;
import org.sfc.text.TextUtils;
import org.sfc.utils.Classes;

/**
 * The program type to be used for linear genetic programming
 * 
 * @author Thomas Weise
 */
public class LGPProgram extends VirtualMachineProgram<LGPMemory> {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * The empty program.
   */
  @SuppressWarnings("unchecked")
  public static final LGPProgram EMPTY_PROGRAM = new LGPProgram(
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
   * the default indentation
   */
  private static final char[] INDENT = (TextUtils.LINE_SEPARATOR + "  ").toCharArray();//$NON-NLS-1$

  /**
   * the procedure name
   */
  private static final char[] FUNC_CHARS = "function_".toCharArray(); //$NON-NLS-1$

  /**
   * the instruction set this program belongs to
   */
  public final InstructionSet m_instructionSet;

  /**
   * The code
   */
  private final int[][] m_code;

  /**
   * Create a new program
   * 
   * @param code
   *          the code
   * @param instructionSet
   *          the instruction set
   */
  public LGPProgram(final InstructionSet instructionSet, final int[][] code) {
    super();
    this.m_instructionSet = instructionSet;
    this.m_code = code;
  }

  /**
   * Obtain the total instruction count
   * 
   * @return the total instruction count
   */
  @Override
  public final int getSize() {
    int[][] x;
    int i, c;

    x = this.m_code;
    c = 0;
    for (i = (x.length - 1); i >= 0; i--) {
      c += (x[i].length >>> 2);
    }

    return c;
  }

  /**
   * obtain the count of procedures of this program
   * 
   * @return the count of procedures of this program
   */
  public final int getFunctionCount() {
    return this.m_code.length;
  }

  /**
   * Obtain the count of instructions of a certain function.
   * 
   * @param function
   *          the function index
   * @return the count of instructions of the certain function
   */
  public final int getInstructionCount(final int function) {
    return (this.m_code[function].length >>> 2);
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
    LGPProgram p;
    int i;

    if (o == this)
      return true;

    if (o instanceof LGPProgram) {
      p = ((LGPProgram) o);
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
   * Count the occurences of the specified instruction.
   * 
   * @param instruction
   *          the instruction
   * @return the number of its occurences
   */
  public final int countInstructionOccurences(
      final Instruction<?> instruction) {
    int[][] ccc;
    int[] cc;
    int i, j, c, l, oc;

    c = 0;
    l = this.m_instructionSet.size();
    oc = Mathematics.modulo(instruction.m_id, l);
    ccc = this.m_code;
    for (i = (ccc.length - 1); i >= 0; i--) {
      cc = ccc[i];
      for (j = (cc.length - 4); j >= 0; j -= 4) {
        if (Mathematics.modulo(cc[j], l) == oc)
          c++;
      }
    }

    return c;
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

    if (this.m_instructionSet != null) {
      m = Classes.findStaticField(this.m_instructionSet);

      if (m == null)
        this.m_instructionSet.javaToStringBuilder(sb, indent);
      else
        TextUtils.appendStaticField(m, sb);
    } else
      sb.append((Object) null);

    sb.append(", new "); //$NON-NLS-1$
    TextUtils.append(this.m_code, sb);
  }

  /**
   * Append the name of the procedure with the specified index to a string
   * builder.
   * 
   * @param index
   *          the index of the procedure
   * @param sb
   *          the string builder.
   */
  public static final void appendFunctionName(final int index,
      final StringBuilder sb) {
    sb.append(FUNC_CHARS);
    sb.append(index);
  }

  /**
   * Write this program's human readable representation to a string
   * builder.
   * 
   * @param add
   *          the string builder to write to
   */
  @Override
  @SuppressWarnings("unchecked")
  public void toStringBuilder(final StringBuilder add) {
    int[][] code;
    int[] c;
    int i, j, x, y, z, k, s;
    Instruction<?> ins;
    InstructionSet is;

    code = this.m_code;
    if (code != null) {
      is = this.m_instructionSet;
      s = is.size();

      for (i = 0; i < code.length; i++) {
        if (i > 0)
          add.append(TextUtils.LINE_SEPARATOR);
        appendFunctionName(i, add);
        add.append(':');
        c = code[i];

        x = 0;
        for (j = ((c.length >>> 2) - 1); j > 0; j /= 10) {
          x++;
        }

        for (j = 0, k = 0; k < c.length; j++, k += 4) {
          add.append(INDENT);

          if (j <= 0) {
            z = (x - 1);
          } else {
            z = x;
            for (y = j; y > 0; y /= 10) {
              z--;
            }
          }

          for (; z > 0; z--)
            add.append(' ');
          add.append(j);
          add.append(':');
          add.append(' ');

          ins = is.get(Mathematics.modulo(c[k], s));

          // ((Instruction<V>) (InstructionSet.get(c[k],
          // is.m_instructions)));
          ins.toStringBuilder(c[k + 1], c[k + 2], c[k + 3], add);
        }
      }
    }
  }

  /**
   * Obtain the code of this program. The returned array must not be
   * modified.
   * 
   * @return the code of this program
   */
  public int[][] getCode() {
    return this.m_code;
  }

  /**
   * Postprocess this program - ensure that it does not violate the given
   * parameters. <strong>Warning:</strong> This operation will most likely
   * modify the contents of this program!
   * 
   * @param parameters
   *          params
   * @return the postprocessed program
   */
  public final LGPProgram postProcess(final ILGPParameters parameters) {
    int[][] p;

    p = Int2StringPostProcessor.postProcess(this.m_code, parameters);
    if (p != this.m_code)
      return new LGPProgram(parameters.getInstructions(), p);
    return this;
  }
}
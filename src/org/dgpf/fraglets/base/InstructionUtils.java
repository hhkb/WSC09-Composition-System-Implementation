/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-16
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.base.InstructionUtils.java
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

package org.dgpf.fraglets.base;

import org.dgpf.vm.net.NetworkProvider;
import org.sfc.math.Mathematics;
import org.sfc.utils.ErrorUtils;

/**
 * Some utilities for instruction handling
 * 
 * @author Thomas Weise
 */
public final class InstructionUtils {
  /**
   * we allow for ten bits of data
   */
  public static final int DATA_BITS;

  /**
   * the instruction mask
   */
  private static final int INSTR_MASK;

  /**
   * the data mask
   */
  private static final int DATA_MASK;

  /**
   * the high bit
   */
  private static final int HIGH_BIT;

  static {
    int i, c;

    i = Mathematics.ld(NetworkProvider.MAX_ID);
    while ((1 << i) < NetworkProvider.MAX_ID)
      i++;

    i += 4;

    DATA_BITS = (i + 1);

    HIGH_BIT = (1 << i);

    c = 0;
    for (i++; i < 32; i++) {
      c |= (1 << i);
    }

    INSTR_MASK = c;
    DATA_MASK = (~c);
  }

  /**
   * Check whether the given value is an instruction/symbol or a
   * number/data.
   * 
   * @param value
   *          the value
   * @return <code>true</code> if and only if the given value represents
   *         an instruction, <code>false</code> if it represents data
   */
  public static final boolean isInstruction(final int value) {
    return ((value & INSTR_MASK) != INSTR_MASK);
  }

  /**
   * Check whether the given things are equal.
   * 
   * @param v1
   *          the first value
   * @param v2
   *          the second value
   * @param ic
   *          the instruction count
   * @return <code>true</code> if and only if the values are equal.
   */
  public static final boolean isEqual(final int v1, final int v2,
      final int ic) {
    boolean b1, b2;

    b1 = isInstruction(v1);
    b2 = isInstruction(v2);
    if (b1 != b2)
      return false;
    if (b1)
      return (decodeInstruction(v1, ic) == decodeInstruction(v2, ic));
    return decodeData(v1) == decodeData(v2);
  }

  /**
   * Decode the given instruction/symbol value.
   * 
   * @param value
   *          the value
   * @param instrCount
   *          the total number of instructions
   * @return the instruction index
   */
  public static final int decodeInstruction(final int value,
      final int instrCount) {
    return Mathematics.modulo(value, instrCount);
  }

  /**
   * Encode the given instruction/symbol value.
   * 
   * @param i
   *          the instruction to encode
   * @return the instruction index
   */
  public static final int encodeInstruction(final Instruction<?> i) {
    return i.m_id;
  }

  /**
   * Decode the given data value. Data may be at most 31 bits wide, signs
   * are preserved.
   * 
   * @param value
   *          the data value
   * @return the decoded data value
   */
  public static final int decodeData(final int value) {
    int d;

    d = (value & DATA_MASK);
    if ((d & HIGH_BIT) == 0)
      return d;
    return (d | INSTR_MASK);
  }

  /**
   * Encode the given data value. Data may be at most 31 bits wide, signs
   * are preserved.
   * 
   * @param value
   *          the data value
   * @return the encoded value.
   */
  public static final int encodeData(final int value) {
    return (value | INSTR_MASK);
  }

  /**
   * the forbidden fruit
   */
  private InstructionUtils() {
    ErrorUtils.doNotCall();
  }
}

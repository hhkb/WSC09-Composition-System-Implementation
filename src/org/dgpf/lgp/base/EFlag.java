/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-01-17
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.EFlag.java
 * Last modification: 2007-01-17
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

import org.sfc.text.ITextable;

/**
 * This enumeration contains the semantics of the flags in the virtual.
 * 
 * @author Thomas Weise
 */
public enum EFlag implements ITextable {

  /**
   * the carry flag
   */
  CARRY {
    public void toStringBuilder(final StringBuilder sb) {
      sb.append(CARRY_CHARS);
    }
  },
  /**
   * the overflow flag
   */
  OVERFLOW {
    public void toStringBuilder(final StringBuilder sb) {
      sb.append(OVERFLOW_CHARS);
    }
  },
  /**
   * the sign flag
   */
  SIGN {
    public void toStringBuilder(final StringBuilder sb) {
      sb.append(SIGN_CHARS);
    }
  },
  /**
   * the zero flag
   */
  ZERO {
    public void toStringBuilder(final StringBuilder sb) {
      sb.append(ZERO_CHARS);
    }
  };

  /**
   * the mask of this flag.
   */
  transient final int m_mask;

  /**
   * the negative mask of this flag.
   */
  transient final int m_negMask;

  /**
   * Create a new flag.
   */
  EFlag() {
    this.m_mask = (1 << this.ordinal());
    this.m_negMask = (~(this.m_mask));
  }

  /**
   * Obtain the human readable representation of this textable object.
   * 
   * @return the human readable representation of this textable object
   */
  @Override
  public String toString() {
    StringBuilder sb;
    sb = new StringBuilder();
    this.toStringBuilder(sb);
    return sb.toString();
  }

  /**
   * the carry mask
   */
  static final int CARRY_MASK = CARRY.m_mask;

  /**
   * the negative carry mask
   */
  static final int CARRY_NMASK = CARRY.m_negMask;

  /**
   * the overflow mask
   */
  static final int OVERFLOW_MASK = OVERFLOW.m_mask;

  /**
   * the negative overflow mask
   */
  static final int OVERFLOW_NMASK = OVERFLOW.m_negMask;

  /**
   * the zero mask
   */
  static final int ZERO_MASK = ZERO.m_mask;

  /**
   * the negative zero mask
   */
  static final int ZERO_NMASK = ZERO.m_negMask;

  /**
   * the sign mask
   */
  static final int SIGN_MASK = SIGN.m_mask;

  /**
   * the negative sign mask
   */
  static final int SIGN_NMASK = SIGN.m_negMask;

  /**
   * the carry char
   */
  static final char[] CARRY_CHARS = "carry".toCharArray(); //$NON-NLS-1$

  /**
   * the sign char
   */
  static final char[] SIGN_CHARS = "sign".toCharArray(); //$NON-NLS-1$

  /**
   * the zero char
   */
  static final char[] ZERO_CHARS = "zero".toCharArray(); //$NON-NLS-1$

  /**
   * the overflow char
   */
  static final char[] OVERFLOW_CHARS = "overflow".toCharArray(); //$NON-NLS-1$

  /**
   * the flags
   */
  private static final EFlag[] FLAGS = EFlag.values();

  /**
   * Decode a flag from a parameter.
   * 
   * @param param
   *          the parameter to decode
   * @return the decoded flag
   */
  public static final EFlag decode(final int param) {
    return FLAGS[param & 3];
  }

  /**
   * Encode a flag into a parameter
   * 
   * @param flag
   *          the flag
   * @return the parameter
   */
  public static final int encode(final EFlag flag) {
    return flag.ordinal();
  }

  /**
   * Compare two values.
   * 
   * @param a
   *          the first parameter to be compared
   * @param b
   *          the second parameter to be compared
   * @return the comparison result
   */
  public static final int compare(final int a, final int b) {

    int f, c;

    f = 0;
    c = (a - b);

    if (c == 0) {
      f |= EFlag.ZERO_MASK;
    } else {
      if (c < 0)
        f |= EFlag.SIGN_MASK;
    }

    if (((a > 0) && (b < 0) && (c < 0)) || ((a < 0) && (b > 0) && (c > 0)))
      f |= EFlag.OVERFLOW_MASK;

    if ((a & 0xffffffffl) < (b & 0xffffffffl)) {
      f |= EFlag.CARRY_MASK;
    }

    return f;
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-15
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.ECondition.java
 * Last modification: 2007-02-15
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
 * This enumeration enumerates conditions.
 * 
 * @author Thomas Weise
 */
public enum ECondition implements ITextable {

  /**
   * This condition is always <code>true</code>
   */
  TRUE {
    @Override
    public boolean check(final int test) {
      return true;
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(TRUE_TXT);
    }

    @Override
    public ECondition invert() {
      return FALSE;
    }
  },

  /**
   * This condition is always <code>false</code>
   */
  FALSE {
    @Override
    public boolean check(final int test) {
      return true;
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(FALSE_TXT);
    }

    @Override
    public ECondition invert() {
      return TRUE;
    }
  },

  /**
   * This condition is only <code>true</code> if the carry flag is set.
   * This condition can also be explained as "unsigned below" or "not above
   * or equal unsigned"
   */
  C {
    @Override
    public boolean check(final int test) {
      return ((test & EFlag.CARRY_MASK) != 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(CARRY_TXT);
    }

    @Override
    public ECondition invert() {
      return NC;
    }
  },

  /**
   * This condition is only <code>true</code> if the carry flag is not
   * set. This condition can also be explained as "unsigned not below" or
   * "above or equal unsigned"
   */
  NC {
    @Override
    public boolean check(final int test) {
      return ((test & EFlag.CARRY_MASK) == 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(NOT_CARRY_TXT);
    }

    @Override
    public ECondition invert() {
      return C;
    }
  },

  /**
   * This condition is only <code>true</code> if the overflow flag is set
   */
  O {
    @Override
    public boolean check(final int test) {
      return ((test & EFlag.OVERFLOW_MASK) != 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(OVERFLOW_TXT);
    }

    @Override
    public ECondition invert() {
      return NO;
    }
  },

  /**
   * This condition is only <code>true</code> if the overflow flag is not
   * set
   */
  NO {
    @Override
    public boolean check(final int test) {
      return ((test & EFlag.OVERFLOW_MASK) == 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(NOT_OVERFLOW_TXT);
    }

    @Override
    public ECondition invert() {
      return O;
    }
  },

  /**
   * This condition is only <code>true</code> if the sign flag is set
   */
  S {
    @Override
    public boolean check(final int test) {
      return ((test & EFlag.SIGN_MASK) != 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(SIGN_TXT);
    }

    @Override
    public ECondition invert() {
      return NS;
    }
  },

  /**
   * This condition is only <code>true</code> if the sign flag is not set
   */
  NS {
    @Override
    public boolean check(final int test) {
      return ((test & EFlag.SIGN_MASK) == 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(NOT_SIGN_TXT);
    }

    @Override
    public ECondition invert() {
      return S;
    }
  },

  /**
   * This condition is only <code>true</code> if the overflow flag is set
   */
  Z {
    @Override
    public boolean check(final int test) {
      return ((test & EFlag.ZERO_MASK) != 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(ZERO_TXT);
    }

    @Override
    public ECondition invert() {
      return NZ;
    }
  },

  /**
   * This condition is only <code>true</code> if the overflow flag is not
   * set
   */
  NZ {
    @Override
    public boolean check(final int test) {
      return ((test & EFlag.ZERO_MASK) == 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(NOT_ZERO_TXT);
    }

    @Override
    public ECondition invert() {
      return Z;
    }
  },

  /**
   * This condition is only <code>true</code> if the carry or the zero
   * flag is set. This condition can also be explained as "unsigned below
   * or equal" or "unsigned not above"
   */
  C_OR_Z {
    @Override
    public boolean check(final int test) {
      return ((test & CARRY_OR_ZERO_MASK) != 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(CARRY_OR_ZERO_TXT);
    }

    @Override
    public ECondition invert() {
      return NC_AND_NZ;
    }
  },

  /**
   * This condition is only <code>true</code> if the neither the carry
   * nor the zero flag is set. This condition can also be explained as
   * "unsigned above" or "unsigned not below or equal"
   */
  NC_AND_NZ {

    @Override
    public boolean check(final int test) {
      return ((test & CARRY_OR_ZERO_MASK) == 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(NOT_CARRY_AND_NOT_ZERO_TXT);
    }

    @Override
    public ECondition invert() {
      return C_OR_Z;
    }
  },

  /**
   * This condition is only <code>true</code> if the signed flag is set
   * and the overflow flag not - or vice versa. This condition can also be
   * explained as "signed less" or "signed not greater or equal"
   */
  S_XOR_O {
    @Override
    public boolean check(final int test) {
      return (((test & EFlag.SIGN_MASK) != 0) != ((test & EFlag.OVERFLOW_MASK) != 0));
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(SIGN_XOR_OVERFLOW_TXT);
    }

    @Override
    public ECondition invert() {
      return S_EXOR_O;
    }
  },

  /**
   * This condition is only <code>true</code> if the signed flag has the
   * same value as the overflow flag. This condition can also be explained
   * as "signed greater or equal" or "signed not less"
   */
  S_EXOR_O {
    @Override
    public boolean check(final int test) {
      return (((test & EFlag.SIGN_MASK) != 0) == ((test & EFlag.OVERFLOW_MASK) != 0));
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(SIGN_EXOR_OVERFLOW_TXT);
    }

    @Override
    public ECondition invert() {
      return S_XOR_O;
    }
  },

  /**
   * This condition is only <code>true</code> if the zero flag is set and
   * the sign flag is different from the overflow flag. This condition can
   * also be explained as "signed less or equal" or "signed not greater"
   */
  Z_OR_S_XOR_O {
    @Override
    public boolean check(final int test) {
      return (((test & EFlag.ZERO_MASK) != 0) || (((test & EFlag.SIGN_MASK) != 0) != ((test & EFlag.OVERFLOW_MASK) != 0)));
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(ZERO_OR_SIGN_XOR_OVERFLOW_TXT);
    }

    @Override
    public ECondition invert() {
      return N_Z_AND_S_EXOR_O;
    }
  },

  /**
   * This condition is only <code>true</code> if the zero flag is not set
   * and the sign flag is equals the overflow flag. This condition can also
   * be explained as "signed greater" or "signed not less or equal"
   */
  N_Z_AND_S_EXOR_O {
    @Override
    public boolean check(final int test) {
      return (((test & EFlag.ZERO_MASK) == 0) && (((test & EFlag.SIGN_MASK) != 0) == ((test & EFlag.OVERFLOW_MASK) != 0)));
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(NOT_ZERO_AND_SIGN_EXOR_OVERFLOW_TXT);
    }

    @Override
    public ECondition invert() {
      return Z_OR_S_XOR_O;
    }
  },

  /**
   * This condition is only <code>true</code> value is null
   */
  NULL {
    @Override
    public boolean check(final int test) {
      return (test == 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(NULL_TXT);
    }

    @Override
    public ECondition invert() {
      return N_NULL;
    }
  },

  /**
   * This condition is only <code>true</code> value is not null
   */
  N_NULL {
    @Override
    public boolean check(final int test) {
      return (test != 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(N_NULL_TXT);
    }

    @Override
    public ECondition invert() {
      return NULL;
    }
  },

  /**
   * This condition is only <code>true</code> value is >null
   */
  G_NULL {
    @Override
    public boolean check(final int test) {
      return (test > 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(G_NULL_TXT);
    }

    @Override
    public ECondition invert() {
      return LE_NULL;
    }
  },

  /**
   * This condition is only <code>true</code> value is <= null
   */
  LE_NULL {
    @Override
    public boolean check(final int test) {
      return (test <= 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(LE_NULL_TXT);
    }

    @Override
    public ECondition invert() {
      return G_NULL;
    }
  },

  /**
   * This condition is only <code>true</code> value is >=null
   */
  GE_NULL {
    @Override
    public boolean check(final int test) {
      return (test >= 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(GE_NULL_TXT);
    }

    @Override
    public ECondition invert() {
      return L_NULL;
    }
  },

  /**
   * This condition is only <code>true</code> value is <null
   */
  L_NULL {
    @Override
    public boolean check(final int test) {
      return (test < 0);
    }

    public void toStringBuilder(final StringBuilder sb) {
      sb.append(L_NULL_TXT);
    }

    @Override
    public ECondition invert() {
      return GE_NULL;
    }
  };

  /**
   * The qualified name
   */
  private static final char[] QN = (ECondition.class.getCanonicalName() + '.')
      .toCharArray();

  /**
   * Append the qualified name of this indirection type to the string
   * builder.
   * 
   * @param sb
   *          the string builder to append to
   */
  public void appendQualifiedName(final StringBuilder sb) {
    sb.append(QN);
    sb.append(this.name());
  }

  /**
   * Check this condition.
   * 
   * @param test
   *          an int-value containing flags
   * @return <code>true</code> if and only if this condition is met in
   *         the vm
   */
  public abstract boolean check(final int test);

  /**
   * Obtain the inverse condition.
   * 
   * @return the inverse condition
   */
  public abstract ECondition invert();

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
   * below or equal
   */
  public static final ECondition BELOW_OR_EQUAL = C_OR_Z;

  /**
   * not above
   */
  public static final ECondition NOT_ABOVE = C_OR_Z;

  /**
   * unsigned: not below or equal
   */
  public static final ECondition NOT_BELOW_OR_EQUAL = NC_AND_NZ;

  /**
   * unsigned: above
   */
  public static final ECondition ABOVE = NC_AND_NZ;

  /**
   * signed: less
   */
  public static final ECondition LESS = S_XOR_O;

  /**
   * signed: not greater or equal
   */
  public static final ECondition NOT_GREATER_OR_EQUAL = S_XOR_O;

  /**
   * signed: greater or equal
   */
  public static final ECondition GREATER_OR_EQUAL = S_EXOR_O;

  /**
   * signed: not less
   */
  public static final ECondition NOT_LESS = S_EXOR_O;

  /**
   * equal
   */
  public static final ECondition EQUAL = Z;

  /**
   * not equal
   */
  public static final ECondition NOT_EQUAL = NZ;

  /**
   * unsigned: above or equal
   */
  public static final ECondition ABOVE_OR_EQUAL = NC;

  /**
   * unsigned: not below
   */

  public static final ECondition NOT_BELOW = NC;

  /**
   * unsigned: not above or equal
   */
  public static final ECondition NOT_ABOVE_OR_EQUAL = C;

  /**
   * unsigned: below
   */

  public static final ECondition BELOW = C;

  /**
   * signed: less or equal
   */
  public static final ECondition LESS_OR_EQUAL = Z_OR_S_XOR_O;

  /**
   * signed: not greater
   */
  public static final ECondition NOT_GREATER = Z_OR_S_XOR_O;

  /**
   * signed: greater
   */
  public static final ECondition GREATER = N_Z_AND_S_EXOR_O;

  /**
   * signed: not less or equal
   */
  public static final ECondition NOT_LESS_OR_EQUAL = N_Z_AND_S_EXOR_O;

  /**
   * true
   */
  static final char[] TRUE_TXT = Boolean.toString(true).toCharArray();

  /**
   * true
   */
  static final char[] FALSE_TXT = Boolean.toString(false).toCharArray();

  /**
   * 0
   */
  static final char[] NULL_TXT = "==0".toCharArray(); //$NON-NLS-1$

  /**
   * !=0
   */
  static final char[] N_NULL_TXT = "!=0".toCharArray(); //$NON-NLS-1$

  /**
   * >0
   */
  static final char[] G_NULL_TXT = ">0".toCharArray(); //$NON-NLS-1$

  /**
   * <0
   */
  static final char[] L_NULL_TXT = "<0".toCharArray(); //$NON-NLS-1$

  /**
   * >=0
   */
  static final char[] GE_NULL_TXT = ">=0".toCharArray(); //$NON-NLS-1$

  /**
   * <=0
   */
  static final char[] LE_NULL_TXT = "==0".toCharArray(); //$NON-NLS-1$

  /**
   * below
   */
  static final char[] CARRY_TXT = "below".toCharArray(); //$NON-NLS-1$

  /** not below */
  static final char[] NOT_CARRY_TXT = "!below".toCharArray(); //$NON-NLS-1$

  /**
   * overflow
   */
  static final char[] OVERFLOW_TXT = "overflow".toCharArray(); //$NON-NLS-1$

  /** not overflow */
  static final char[] NOT_OVERFLOW_TXT = "!overflow".toCharArray(); //$NON-NLS-1$

  /** sign */
  static final char[] SIGN_TXT = "sign".toCharArray(); //$NON-NLS-1$

  /** not sign */
  static final char[] NOT_SIGN_TXT = "!sign".toCharArray(); //$NON-NLS-1$

  /**
   * equal
   */
  static final char[] ZERO_TXT = "equal".toCharArray(); //$NON-NLS-1$

  /**
   * not equal
   */
  static final char[] NOT_ZERO_TXT = "!equal".toCharArray(); //$NON-NLS-1$

  /**
   * not aboe
   */
  static final char[] CARRY_OR_ZERO_TXT = "!above".toCharArray(); //$NON-NLS-1$

  /**
   * above
   */
  static final char[] NOT_CARRY_AND_NOT_ZERO_TXT = "above".toCharArray(); //$NON-NLS-1$

  /**
   * less
   */
  static final char[] SIGN_XOR_OVERFLOW_TXT = "less".toCharArray(); //$NON-NLS-1$

  /**
   * not less
   */
  static final char[] SIGN_EXOR_OVERFLOW_TXT = "!less".toCharArray(); //$NON-NLS-1$

  /**
   * greater
   */
  static final char[] ZERO_OR_SIGN_XOR_OVERFLOW_TXT = "!greater".toCharArray(); //$NON-NLS-1$

  /**
   * not greater
   */
  static final char[] NOT_ZERO_AND_SIGN_EXOR_OVERFLOW_TXT = "greater".toCharArray(); //$NON-NLS-1$

  /**
   * the carry and the zero flag as a bit mask
   */
  static final int CARRY_OR_ZERO_MASK = (EFlag.CARRY_MASK | EFlag.ZERO_MASK);

  /**
   * the possible conditions
   */
  private static final ECondition[] CONDS = ECondition.values();

  /**
   * Decode a condition which is stored in an int parameter.
   * 
   * @param param
   *          the parameter
   * @return the condition
   */
  public static final ECondition decode(final int param) {
    // int e;
    // if (param < 0)
    // e = (((param % 22) + 22) % 22);
    // else if (param >= 22)
    // e = param % 22;
    // else
    // e = param;
    // return CONDS[e];
    return CONDS[param];
  }

  /**
   * Decode a condition which is stored in an int parameter.
   * 
   * @param param
   *          the parameter
   * @return the condition
   */
  public static final int reencode(final int param) {

    if (param < 0)
      return (((param % 22) + 22) % 22);
    if (param >= 22)
      return (param % 22);

    return param;
  }

  /**
   * Encode a condition into an int param
   * 
   * @param condition
   *          the condition to be encoded
   * @return the integer parameter
   */
  public static final int encode(final ECondition condition) {
    return ((condition != null) ? condition.ordinal() : TRUE.ordinal());
  }

}

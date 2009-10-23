/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.single.Rule.java
 * Last modification: 2007-05-27
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

package org.dgpf.rbgp.base;

import java.io.Serializable;

import org.sfc.text.JavaTextable;
import org.sfc.utils.Utils;

/**
 * A single rule in the classfier
 * 
 * @author Thomas Weise
 */
public class Rule extends JavaTextable implements Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * t
   */
  private static final char[] T = "(t)".toCharArray(); //$NON-NLS-1$

  /**
   * useless
   */
  private static final char[] UL = "[useless] ".toCharArray(); //$NON-NLS-1$

//  /**
//   * the parameters
//   */
//  private static final String PARAMETERS;
//
//  static {
//    StringBuilder sb;
//
//    sb = new StringBuilder();
//
//    sb.append('<');
//    sb.append(VirtualMachine.class.getCanonicalName());
//    sb.append('<');
//    sb.append(RBGPMemory.class.getCanonicalName());
//    sb.append(","); //$NON-NLS-1$    
//    sb.append(VirtualMachineProgram.class.getCanonicalName());
//    sb.append('<');
//    sb.append(RBGPMemory.class.getCanonicalName());
//    sb.append('>');
//    sb.append('>');
//    sb.append('>');
//
//    PARAMETERS = sb.toString();
//  }

  /**
   * the first comparison
   */
  public final EComparison m_c1;

  /**
   * the first symbol of the first comparison
   */
  public final Symbol m_s11;

  /**
   * the second symbol of the first comparison
   */
  public final Symbol m_s12;

  /**
   * the second comparison
   */
  public final EComparison m_c2;

  /**
   * the first symbol of the second comparison
   */
  public final Symbol m_s21;

  /**
   * the second symbol of the second comparison
   */
  public final Symbol m_s22;

  /**
   * <code>true</code> for and, <code>false</code> for or combinations.
   */
  public final boolean m_and;

  /**
   * the action
   */
  public final Action<?> m_act;

  /**
   * the first symbol of the action
   */
  public final Symbol m_sa1;

  /**
   * the second symbol of the action
   */
  public final Symbol m_sa2;

  /**
   * @param c1
   *          the first comparison
   * @param s11
   *          the first symbol of the first comparison
   * @param s12
   *          the second symbol of the first comparison
   * @param c2
   *          the second comparison
   * @param s21
   *          the first symbol of the second comparison
   * @param s22
   *          the indirection of the second symbol of the second comparison
   * @param and
   *          <code>true</code> for and, <code>false</code> for or
   *          combinations.
   * @param act
   *          the action
   * @param sa1
   *          the first symbol of the action
   * @param sa2
   *          the second symbol of the action
   */
  public Rule(final EComparison c1, final Symbol s11, final Symbol s12,
      final EComparison c2, final Symbol s21, final Symbol s22,
      final boolean and, final Action<?> act, final Symbol sa1,
      final Symbol sa2) {
    this.m_c1 = c1;
    this.m_s11 = s11;
    this.m_s12 = s12;
    this.m_c2 = c2;
    this.m_s21 = s21;
    this.m_s22 = s22;
    this.m_and = and;
    this.m_act = act;
    this.m_sa1 = sa1;
    this.m_sa2 = sa2;
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public final void toStringBuilder(final StringBuilder sb) {
    boolean b;
    Symbol s;

    if (this.isUseless()) {
      sb.append(UL);
      // return;
    }

    b = (this.m_c1.ordinal() > 1);
    if (b) {
      sb.append('(');
      s = this.m_s11;
      s.toStringBuilder(sb);
      if (!(s.m_writeProtected))
        sb.append(T);

    }
    this.m_c1.toStringBuilder(sb);
    if (b) {
      s = this.m_s12;
      s.toStringBuilder(sb);
      if (!(s.m_writeProtected))
        sb.append(T);
      sb.append(')');
    }

    if (this.m_and)
      sb.append(" and "); //$NON-NLS-1$
    else
      sb.append(" or ");//$NON-NLS-1$

    b = (this.m_c2.ordinal() > 1);
    if (b) {
      sb.append('(');
      s = this.m_s21;
      s.toStringBuilder(sb);
      if (!(s.m_writeProtected))
        sb.append(T);
    }
    this.m_c2.toStringBuilder(sb);
    if (b) {
      s = this.m_s22;
      s.toStringBuilder(sb);
      if (!(s.m_writeProtected))
        sb.append(T);
      sb.append(')');
    }

    sb.append(" ==> "); //$NON-NLS-1$
    this.m_act.toStringBuilder(sb, this.m_sa1, this.m_sa2);
  }

  // /**
  // * Here a generic configuration may be added to the text of this
  // * java-textable.
  // *
  // * @param sb
  // * the string builder
  // * @param indent
  // * an optional parameter denoting the indentation
  // */
  // @Override
  // protected void javaGenericToStringBuilder(final StringBuilder sb,
  // final int indent) {
  // sb.append(PARAMETERS);
  //  }

  /**
   * Serializes the parameters of the constructor of this object.
   * 
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  @Override
  protected final void javaParametersToStringBuilder(
      final StringBuilder sb, final int indent) {
    this.m_c1.javaToStringBuilder(sb, indent);
    sb.append(',');
    this.m_s11.javaToStringBuilder(sb, indent);
    sb.append(',');
    this.m_s12.javaToStringBuilder(sb, indent);
    sb.append(',');
    this.m_c2.javaToStringBuilder(sb, indent);
    sb.append(',');
    this.m_s21.javaToStringBuilder(sb, indent);
    sb.append(',');
    this.m_s22.javaToStringBuilder(sb, indent);
    sb.append(',');
    sb.append(this.m_and);
    sb.append(',');
    this.m_act.javaToStringBuilder(sb, indent);
    sb.append(',');
    this.m_sa1.javaToStringBuilder(sb, indent);
    sb.append(',');
    this.m_sa2.javaToStringBuilder(sb, indent);
  }

  /**
   * Check whether this rule is useless.
   * 
   * @return <code>true</code> if this rule is useless,<code>false</code>
   *         otherwise
   */
  public final boolean isUseless() {
    boolean b1, b2;

    if (this.m_act.isUseless(this.m_sa1, this.m_sa2))
      return true;
    b1 = this.m_c1.canBeMet(this.m_s11, this.m_s12);
    b2 = this.m_c2.canBeMet(this.m_s21, this.m_s22);

    return (!(((this.m_and) ? (b1 && b2) : (b1 || b2))));
  }

  /**
   * Check whether this rule equals to an object.
   * 
   * @param o
   *          the object to compare with
   * @return <code>true</code> if and only if this rule equals to the
   *         object <code>o</code>
   */
  @Override
  @SuppressWarnings("unchecked")
  public final boolean equals(final Object o) {
    Rule r;
    if (o == this)
      return true;
    if (o instanceof Rule) {
      r = ((Rule) o);
      return ((r.m_and == this.m_and)
          && Utils.testEqual(r.m_act, this.m_act)
          && Utils.testEqual(r.m_c1, this.m_c1)
          && Utils.testEqual(r.m_c2, this.m_c2)
          && Utils.testEqual(r.m_s11, this.m_s11)
          && Utils.testEqual(r.m_s12, this.m_s12)
          && Utils.testEqual(r.m_s21, this.m_s21)
          && Utils.testEqual(r.m_s22, this.m_s22)
          && Utils.testEqual(r.m_sa1, this.m_sa1) && Utils.testEqual(
          r.m_sa2, this.m_sa2));
    }
    return false;
  }

  /**
   * Obtain the weight of the rule.
   * 
   * @return the weight of the rule
   */
  public final int getWeight() {
    return ((((this.m_c1 == EComparison.TRUE) ? 2
        : ((this.m_c1 == EComparison.FALSE) ? 1 : 4))) + (((this.m_c2 == EComparison.TRUE) ? 2
        : ((this.m_c2 == EComparison.FALSE) ? 1 : 4))));

  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-04
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.expressions.EComparison.java
 * Last modification: 2007-03-04
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

package org.dgpf.hlgp.base.constructs.expressions;

import org.dgpf.lgp.base.ECondition;

/**
 * the possible comparisons
 * 
 * @author Thomas Weise
 */
public enum EComparison {
  /**
   * equal
   */
  EQ(" == ", ECondition.EQUAL), //$NON-NLS-1$
  /**
   * not equal
   */
  NEQ(" != ", ECondition.NOT_EQUAL), //$NON-NLS-1$
  /**
   * signed less
   */
  LT(" < ", ECondition.LESS), //$NON-NLS-1$
  /**
   * signed greater
   */
  GT(" > ", ECondition.GREATER), //$NON-NLS-1$
  /**
   * signed less or equal
   */
  LTE(" <= ", ECondition.LESS_OR_EQUAL), //$NON-NLS-1$
  /**
   * signed greater or equal
   */
  GTE(" >= ", ECondition.GREATER_OR_EQUAL), //$NON-NLS-1$
  /**
   * unsigned above
   */
  A(" >u ", ECondition.ABOVE), //$NON-NLS-1$
  /**
   * unsigned below
   */
  B(" <u ", ECondition.BELOW), //$NON-NLS-1$
  /**
   * unsigned above or equal
   */
  AE(" >=u ", ECondition.ABOVE_OR_EQUAL), //$NON-NLS-1$
  /**
   * unsigned below or equal
   */
  BE(" <=u ", ECondition.BELOW_OR_EQUAL); //$NON-NLS-1$
  /**
   * the label
   */
  int m_label;

  /**
   * the index
   */
  int m_index;

  /**
   * the expression text
   */
  final char[] m_txt;

  /**
   * the condition
   */
  final ECondition m_cond;

  /**
   * Create a new comparison
   * 
   * @param text
   *          the text
   * @param condition
   *          the condition relevant to the comparison
   */
  EComparison(final String text, final ECondition condition) {
    this.m_txt = text.toCharArray();
    this.m_cond = condition;
  }

  /**
   * The qualified name
   */
  private static final char[] QN = (EComparison.class.getCanonicalName() + '.')
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

}

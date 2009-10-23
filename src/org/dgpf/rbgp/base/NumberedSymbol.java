/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-09
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.base.NumberedSymbol.java
 * Last modification: 2007-10-09
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

/**
 * A symbol that can be evaluated by the classifier
 * 
 * @author Thomas Weise
 */
public class NumberedSymbol extends Symbol {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the getter procedure
   */
  private final String m_getter;

  /**
   * the index
   */
  protected final int m_index;

  /**
   * Create a new symbol.
   * 
   * @param symbolSet
   *          The symbol set
   * @param name
   *          the name
   * @param index
   *          the symbol index
   * @param initialValue
   *          the general initial value of this symbol
   * @param writeProtected
   *          <code>true</code> if and only if this symbol is write
   *          protected
   * @param copy
   *          <code>true</code> if and only if the symbol should be
   *          copied in each iteration, <code>false</code> otherwise.
   *          Write protected will always be copied.
   * @param getter
   *          the getter
   */
  protected NumberedSymbol(final SymbolSet symbolSet, final String name,
      final int index, final int initialValue,
      final boolean writeProtected, final boolean copy, final String getter) {
    super(symbolSet, name, initialValue, writeProtected, copy);
    this.m_getter = getter;
    this.m_index = index;
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
  public final void javaToStringBuilder(final StringBuilder sb,
      final int indent) {
    sb.append('(');
    this.appendJavaPrefix(sb);

    sb.append('(');
    this.getSet().javaToStringBuilder(sb, indent);
    sb.append('.');
    sb.append(this.m_getter);
    sb.append(this.m_index);
    sb.append(')');

    sb.append(')');
    sb.append(')');
  }
}

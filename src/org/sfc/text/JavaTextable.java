/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-04-02
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.JavaTextable.java
 * Last modification: 2007-04-02
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

package org.sfc.text;

import java.lang.reflect.Field;

import org.sfc.utils.Classes;

/**
 * The default implementation of the {@link IJavaTextable}-interface.
 * 
 * @author Thomas Weise
 */
public class JavaTextable extends Textable implements IJavaTextable {

  /**
   * the new string
   */
  protected static final char[] NEW_TEXT = "new ".toCharArray(); //$NON-NLS-1$

  /**
   * the new node text
   */
  private static final char[] NEW_NODES2 = "[] {".toCharArray();//$NON-NLS-1$

  /**
   * the new node text
   */
  private static final char[] NEW_NODES3 = "[0]".toCharArray();//$NON-NLS-1$

  /**
   * the parameter separator
   */
  public static final String PARAMETER_SEPARATOR = ", ";//$NON-NLS-1$

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
  public void javaToStringBuilder(final StringBuilder sb, final int indent) {
    sb.append(NEW_TEXT);
    sb.append(this.getClass().getCanonicalName());
    this.javaGenericToStringBuilder(sb, indent);
    sb.append('(');
    this.javaParametersToStringBuilder(sb, indent + 2);
    sb.append(')');
  }

  /**
   * Here a generic configuration may be added to the text of this
   * java-textable.
   * 
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  protected void javaGenericToStringBuilder(final StringBuilder sb,
      final int indent) {
    //
  }

  /**
   * Serializes the parameters of the constructor of this object.
   * 
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  protected void javaParametersToStringBuilder(final StringBuilder sb,
      final int indent) {
    //
  }

  /**
   * Obtain the java text.
   * 
   * @return the java text
   */
  public String getJavaText() {
    StringBuilder sb;

    sb = new StringBuilder();
    this.javaToStringBuilder(sb, 0);
    return sb.toString();
  }

  /**
   * Append an array of java textables to the given string builder.
   * 
   * @param ch
   *          the textables
   * @param count
   *          their number
   * @param nullOnZeroCount
   *          <code>true</code> if and only if we can use
   *          <code>null</code> if the count is zero, <code>false</code>
   *          means an array will be created anyway.
   * @param sb
   *          the string builder to append to
   * @param indent
   *          the indent
   */
  protected static final void appendJavaObjects(final Object[] ch,
      final int count, final boolean nullOnZeroCount,
      final StringBuilder sb, final int indent) {
    int i, j;

    j = (count - 1);
    if (j >= 0) {
      sb.append(TextUtils.LINE_SEPARATOR);
      TextUtils.appendSpaces(sb, indent);
      sb.append(NEW_TEXT);
      sb.append(ch.getClass().getComponentType().getCanonicalName());
      sb.append(NEW_NODES2);

      for (i = 0; i <= j; i++) {
        sb.append(TextUtils.LINE_SEPARATOR);
        TextUtils.appendSpaces(sb, indent + 2);
        appendJavaObject(ch[i], sb, indent + 4);
        // ch[i].javaToStringBuilder(sb, indent + 4);
        if (i < j) {
          sb.append(',');
          sb.append(' ');
        }
      }

      sb.append(TextUtils.LINE_SEPARATOR);
      TextUtils.appendSpaces(sb, indent);
      sb.append('}');
    } else {
      if (nullOnZeroCount)
        sb.append((Object) null);
      else {
        sb.append(NEW_TEXT);
        sb.append(ch.getClass().getComponentType().getCanonicalName());
        sb.append(NEW_NODES3);
      }

      // sb.append(NEW_NODES);
      // sb.append(NEW_NODES3);
    }
  }

  /**
   * Append the given object to the string builder.
   * 
   * @param o
   *          the object to be appended
   * @param sb
   *          the string builder to append to
   * @param indent
   *          the indentation
   */
  public static final void appendJavaObject(final Object o,
      final StringBuilder sb, final int indent) {
    Field m;

    if (o == null)
      sb.append((Object) null);
    else if (o instanceof IJavaTextable) {
      ((IJavaTextable) o).javaToStringBuilder(sb, indent);
    } else if ((m = Classes.findStaticField(o)) != null) {
      TextUtils.appendStaticField(m, sb);
    } else
      TextUtils.append(o, sb);
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-12
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.utils.FixedSetElement.java
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

package org.dgpf.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.sfc.text.IJavaTextable;
import org.sfc.utils.Classes;

/**
 * The base class of elements of fixed sets.
 * 
 * @param <FST>
 *          the fixed set type
 * @author Thomas Weise
 */
public class FixedSetElement<FST extends FixedSet<?>> extends
    org.sfc.collections.sets.FixedSetElement<FST> implements IJavaTextable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new element.
   * 
   * @param set
   *          The owning set
   */
  public FixedSetElement(final FST set) {
    super(set);
  }

  /**
   * This method allows us to append a prefix in order to handle generic
   * types.
   * 
   * @param sb
   *          the string builder
   */
  protected void appendJavaPrefix(final StringBuilder sb) {
    Class<?> c1, c2, c3;

    c1 = this.getClass();
    c3 = c1;

    do {
      c2 = c1;
      c1 = c1.getSuperclass();
    } while (c1 != FixedSetElement.class);

    
    //does the typeparameters-thing make sense?? XXX
    if ((c2 != c3) || (c2.getTypeParameters().length > 0)) {
      sb.append('(');
      sb.append(c2.getCanonicalName());
      sb.append(')');
    }
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
  public void javaToStringBuilder(final StringBuilder sb, final int indent) {
    Method mm;

    sb.append('(');
    this.appendJavaPrefix(sb);

    sb.append('(');
    mm = Classes.findGetter(this.m_set, this);
    if (mm != null) {
      if (Modifier.isStatic(mm.getModifiers())) {
        sb.append(this.m_set.getClass().getCanonicalName());
        sb.append('.');
        sb.append(mm.getName());
        sb.append('(');
        sb.append(')');
        return;
      }
    }

    this.m_set.javaToStringBuilder(sb, indent);
    if (mm != null) {
      sb.append('.');
      sb.append(mm.getName());
      sb.append('(');
      sb.append(')');
    } else {
      sb.append(".get("); //$NON-NLS-1$
      sb.append(this.m_id);
      sb.append(')');

    }

    sb.append(')');
    sb.append(')');
  }
}

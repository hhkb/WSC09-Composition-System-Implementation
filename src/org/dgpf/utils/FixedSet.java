/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-12
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.utils.FixedSet.java
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

import java.lang.reflect.Field;

import org.sfc.collections.sets.FixedSetElement;
import org.sfc.security.CallStack;
import org.sfc.security.SfcSecurityManager;
import org.sfc.text.IJavaTextable;
import org.sfc.text.TextUtils;
import org.sfc.utils.Classes;

/**
 * The base class for fixed set.
 * 
 * @param <FSET>
 *          the fixed set element type
 * @author Thomas Weise
 */
public class FixedSet<FSET extends FixedSetElement<?>> extends
    org.sfc.collections.sets.FixedSet<FSET> implements IJavaTextable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the callstack
   */
  private volatile CallStack m_stack;

  /**
   * the declaring field
   */
  private Field m_field;

  /**
   * Create a java static textable
   */
  protected FixedSet() {
    super();
    this.m_stack = SfcSecurityManager.getCurrentCallStack();
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
    CallStack c;
    int i;
    Field f;

    c = this.m_stack;
    if (c != null) {
      for (i = (c.size() - 1); i >= 0; i--) {
        f = Classes.findStaticField(this, c.get(i));
        if (f != null) {
          this.m_field = f;
          break;
        }
      }
      this.m_stack = null;
    }

    if (this.m_field != null) {
      TextUtils.appendStaticField(this.m_field, sb);
    } else {
      sb.append("new "); //$NON-NLS-1$
      sb.append(this.getClass().getCanonicalName());
      sb.append('(');
      sb.append(')');
    }
  }
}

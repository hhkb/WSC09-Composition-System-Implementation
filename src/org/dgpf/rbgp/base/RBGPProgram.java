/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.single.RBGPProgramBase.java
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

import java.util.Arrays;

import org.dgpf.vm.base.EVirtualMachineState;
import org.dgpf.vm.base.VirtualMachine;
import org.dgpf.vm.base.VirtualMachineProgram;
import org.sfc.text.TextUtils;

/**
 * A symbolic classifier
 * 
 * @author Thomas Weise
 */
public class RBGPProgram extends RBGPProgramBase {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;
//
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
//    sb.append(",? extends "); //$NON-NLS-1$    
//    sb.append(RBGPProgram.class.getCanonicalName());
//    sb.append("<?>>>");//$NON-NLS-1$    
//
//    PARAMETERS = sb.toString();
//  }

  /**
   * the rules
   */
  protected final Rule[] m_rules;

  /**
   * Create a new classifier
   * 
   * @param rules
   *          the rules
   */
  public RBGPProgram(final Rule[] rules) {
    super();
    this.m_rules = rules;
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
    Rule[] r;
    int i;

    r = this.m_rules;
    for (i = 0; i < r.length; i++) {
      if (i > 0)
        sb.append(TextUtils.LINE_SEPARATOR);
      r[i].toStringBuilder(sb);
    }
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
  protected final void javaParametersToStringBuilder(
      final StringBuilder sb, final int indent) {
    appendJavaObjects(this.m_rules, this.m_rules.length, false, sb, indent);
////     Rule[] r;
////     int i;
////    
////     sb.append("new "); //$NON-NLS-1$
////     sb.append(Rule.class.getCanonicalName());
////     sb.append("[] {");//$NON-NLS-1$
////    
////     r = this.m_rules;
////     for (i = 0; i < r.length; i++) {
////     if (i > 0) {
////     sb.append(',');
////     sb.append(TextUtils.LINE_SEPARATOR);
////     TextUtils.appendSpaces(sb, indent);
////     }
////     r[i].javaToStringBuilder(sb, indent);
////     }
////    
////     sb.append("}");//$NON-NLS-1$
  }
//
//  /**
//   * Here a generic configuration may be added to the text of this
//   * java-textable.
//   * 
//   * @param sb
//   *          the string builder
//   * @param indent
//   *          an optional parameter denoting the indentation
//   */
//  @Override
//  protected void javaGenericToStringBuilder(final StringBuilder sb,
//      final int indent) {
//    sb.append(PARAMETERS);
//  }

  /**
   * Obtain the rule count of this classifier
   * 
   * @return the number of rules of this classifier
   */
  @Override
  public final int getSize() {
    return this.m_rules.length;
  }

  /**
   * Obtain the rule weights of this classifier
   * 
   * @return the weights of the rules of this classifier
   */
  @Override
  public final int getWeight() {
    int i, c;
    Rule[] r;

    c = 0;
    r = this.m_rules;
    for (i = (r.length - 1); i >= 0; i--) {
      c += r[i].getWeight();
    }

    return c;
  }

  /**
   * Check whether this classifier is useless.
   * 
   * @return <code>true</code> if this classifier is useless,<code>false</code>
   *         otherwise
   */
  @Override
  public final boolean isUseless() {
    int i;
    Rule[] r;

    r = this.m_rules;
    for (i = (r.length - 1); i >= 0; i--) {
      if (!(r[i].isUseless()))
        return false;
    }

    return true;
  }

  /**
   * Count the number of useless rules.
   * 
   * @return the number of useless rules
   */
  @Override
  public final int getUselessCount() {
    int i;

    i = 0;
    for (Rule r : this.m_rules) {
      if (r.isUseless())
        i++;
    }

    return i;
  }

  /**
   * Check whether this classifier equals to an object.
   * 
   * @param o
   *          the object to compare with
   * @return <code>true</code> if and only if this classifier equals to
   *         the object <code>o</code>
   */
  @Override
  public final boolean equals(final Object o) {
    if (o == this)
      return true;
    if (o instanceof RBGPProgram) {
      return Arrays.equals(((RBGPProgram) o).m_rules, this.m_rules);
    }
    return false;
  }

  /**
   * perform the classification on a vm.
   * 
   * @param vm
   *          the vm to work on
   * @return the resulting virtual machine state
   */
  @Override
  @SuppressWarnings("unchecked")
  public final EVirtualMachineState perform(
      final VirtualMachine<RBGPMemory, ? extends RBGPProgramBase> vm) {
    Rule[] r;
    int i;
    Rule x;
    boolean b1, b2;
    int[] d;
    EVirtualMachineState e1, e2;

    r = this.m_rules;
    d = vm.m_memory.m_mem1;
    e1 = EVirtualMachineState.NOTHING;
    for (i = 0; i < r.length; i++) {
      x = r[i];
      b1 = x.m_c1.compare(d[x.m_s11.m_id], d[x.m_s12.m_id]);
      b2 = x.m_c2.compare(d[x.m_s21.m_id], d[x.m_s22.m_id]);

      if (x.m_and)
        b1 = (b1 && b2);
      else
        b1 = (b1 || b2);

      if (b1) {
        e2 = ((Action<VirtualMachine<RBGPMemory, ? extends VirtualMachineProgram<RBGPMemory>>>) (x.m_act))
            .perform(x.m_sa1, x.m_sa2, vm);
        if ((e2 == EVirtualMachineState.ERROR)
            || (e2 == EVirtualMachineState.TERMINATED))
          return e2;
        if (e2 == EVirtualMachineState.CHANGED)
          e1 = e2;
      }

    }

    return e1;
  }

}

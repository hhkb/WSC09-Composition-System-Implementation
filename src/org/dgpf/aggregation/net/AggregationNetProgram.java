/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.net.AggregationNetProgram.java
 * Last modification: 2007-12-18
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

package org.dgpf.aggregation.net;

import org.dgpf.aggregation.base.AggregationProgram;
import org.dgpf.aggregation.base.constructs.Program;
import org.sfc.text.JavaTextable;
import org.sfc.text.TextUtils;

/**
 * An aggregation program.
 * 
 * @author Thomas Weise
 */
public class AggregationNetProgram extends AggregationProgram {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the empty aggregation net program
   */
  public static final AggregationNetProgram EMPTY_NET_PROGRAM = new AggregationNetProgram(
      Program.EMPTY_PROGRAM, new int[0], new int[0]);

  /**
   * the outgoing message configuration
   */
  public final int[] m_out;

  /**
   * the incoming message configuration
   */
  public final int[] m_in;

  /**
   * Create a new aggregation program.
   * 
   * @param root
   *          the program's root
   * @param out
   *          the outgoing message configuration
   * @param in
   *          the incoming message configuration
   */
  public AggregationNetProgram(final Program root, final int[] out,
      final int[] in) {
    super(root);
    this.m_in = in;
    this.m_out = out;
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    sb.append("in:  "); //$NON-NLS-1$
    TextUtils.append(this.m_in, sb);
    sb.append(TextUtils.LINE_SEPARATOR);
    sb.append("out:  "); //$NON-NLS-1$ 
    TextUtils.append(this.m_out, sb);
    sb.append(TextUtils.LINE_SEPARATOR);
    super.toStringBuilder(sb);
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
  protected void javaParametersToStringBuilder(final StringBuilder sb,
      final int indent) {
    sb.append(JavaTextable.NEW_TEXT);
    TextUtils.append(this.m_out, sb);
    sb.append(',');
    sb.append(JavaTextable.NEW_TEXT);
    TextUtils.append(this.m_in, sb);
    sb.append(',');
    super.javaParametersToStringBuilder(sb, indent);
  }

  /**
   * Obtain the weight of this program which, different to
   * {@link #getSize()}, may incorporate some sort of complexity measure.
   * 
   * @return the weight of this program
   */
  @Override
  public int getWeight() {
    return (super.getWeight() + (this.m_in.length << 2));
  }
}

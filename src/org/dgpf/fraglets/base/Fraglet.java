/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-15
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.base.Fraglet.java
 * Last modification: 2007-12-15
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

package org.dgpf.fraglets.base;

import java.io.Serializable;

import org.sfc.collections.buffers.DirectBufferable;

/**
 * A single fraglet
 * 
 * @author Thomas Weise
 */
public class Fraglet extends DirectBufferable<Fraglet> implements
    Serializable {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * the fraglet code
   */
  public final int[] m_code;

  /**
   * the fraglet's length
   */
  public int m_len;

  /**
   * the number of occurences of this fraglet type
   */
  public int m_count;

  /**
   * Create a new fraglet.
   * 
   * @param size
   *          the maximum size of the fraglet
   */
  public Fraglet(final int size) {
    super();
    this.m_code = new int[size];
  }

  /**
   * Initialize this fraglet
   * 
   * @param code
   *          the fraglet code
   */
  public void init(final int[] code) {
    int l;

    this.m_len = l = Math.min(code.length, this.m_code.length);
    System.arraycopy(code, 0, this.m_code, 0, l);
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @param is
   *          the instruction set
   * @see #toString()
   */
  public void toStringBuilder(final StringBuilder sb,
      final InstructionSet is) {
    sb.append(this.m_count);
    sb.append('*');
    sb.append(' ');
    FragletProgram.fragletToStringBuilder(sb, this.m_code, this.m_len, is);
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
    this.toStringBuilder(sb, InstructionSet.EMPTY_INSTRUCTION_SET);
  }

}

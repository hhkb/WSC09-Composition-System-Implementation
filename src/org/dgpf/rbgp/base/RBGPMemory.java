/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-13
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.base.RBGPMemory.java
 * Last modification: 2007-11-13
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

import org.sfc.text.TextUtils;
import org.sfc.text.Textable;

/**
 * The memory type of rbgp programs.
 * 
 * @author Thomas Weise
 */
public class RBGPMemory extends Textable implements Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /** the first memory set */
  public int[] m_mem1;

  /** the second memory set */
  public int[] m_mem2;

  /**
   * Create a new memory set.
   * 
   * @param size
   *          the memory size
   * @param bufferedMem
   *          <code>true</code> if and only if the memory used shall be
   *          buffered
   */
  public RBGPMemory(final int size, final boolean bufferedMem) {
    super();
    this.m_mem1 = new int[size];
    this.m_mem2 = (bufferedMem ? new int[size] : this.m_mem1);
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
    TextUtils.append(this.m_mem1, sb);
    sb.append(TextUtils.LINE_SEPARATOR);
    sb.append("out: "); //$NON-NLS-1$
    TextUtils.append(this.m_mem2, sb);
    sb.append(TextUtils.LINE_SEPARATOR);
  }
}

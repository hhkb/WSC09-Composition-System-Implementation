/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.base.AggregationProgram.java
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

package org.dgpf.aggregation.base;

import org.dgpf.aggregation.base.constructs.Program;
import org.dgpf.vm.base.VirtualMachineProgram;

/**
 * An aggregation program.
 * 
 * @author Thomas Weise
 */
public class AggregationProgram extends VirtualMachineProgram<double[]> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the root command
   */
  public final Program m_root;

  /**
   * Create a new aggregation program.
   * 
   * @param root
   *          the program's root
   */
  public AggregationProgram(final Program root) {
    super();
    this.m_root = root;
  }

  /**
   * Obtain the size of this program which is the total number of primitive
   * instructions in it.
   * 
   * @return the size of this program
   */
  @Override
  public int getSize() {
    return this.m_root.getWeight();
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
    this.m_root.toStringBuilder(sb);
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
    this.m_root.javaToStringBuilder(sb, indent);
  }
}

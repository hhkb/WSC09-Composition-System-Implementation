/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-16
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.base.genetics.FragletInt2StringEmbryogeny.java
 * Last modification: 2007-12-16
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

package org.dgpf.fraglets.base.genetics;

import org.dgpf.fraglets.base.FragletProgram;
import org.dgpf.fraglets.base.InstructionSet;
import org.sigoa.refimpl.go.embryogeny.Embryogeny;

/**
 * An embryogeny instance that is able to transform <code>int[][]</code>
 * to <code>FragletProgram</code>s.
 * 
 * @author Thomas Weise
 */
public class FragletInt2StringEmbryogeny extends
    Embryogeny<int[][], FragletProgram> {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * The instruction set
   */
  private final InstructionSet m_is;

  /**
   * Create a new embryogeny for lgp programs.
   * 
   * @param is
   *          the instruction set
   */
  public FragletInt2StringEmbryogeny(final InstructionSet is) {
    super();
    this.m_is = is;
  }

  /**
   * This method is supposed to compute an instance of the phenotype from
   * an instance of the genotype. You should override it, since the default
   * method here just performs a typecast and returns the genotype.
   * 
   * @param genotype
   *          The genotype instance to breed a phenotype from.
   * @return The phenotype hatched from the genotype instance.
   * @throws NullPointerException
   *           if <code>genotype==null</code>.
   */
  @Override
  public FragletProgram hatch(final int[][] genotype) {
    return new FragletProgram(this.m_is, genotype);
  }
}

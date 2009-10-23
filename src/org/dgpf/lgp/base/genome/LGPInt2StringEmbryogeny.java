/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.genome.LGPInt2StringEmbryogeny.java
 * Last modification: 2007-11-18
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

package org.dgpf.lgp.base.genome;

import org.dgpf.lgp.base.InstructionSet;
import org.dgpf.lgp.base.LGPProgram;
import org.sigoa.refimpl.go.embryogeny.Embryogeny;

/**
 * The int[][] embryogeny for lgp programs
 * 
 * @author Thomas Weise
 */
public class LGPInt2StringEmbryogeny extends
    Embryogeny<int[][], LGPProgram> {
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
  public LGPInt2StringEmbryogeny(final InstructionSet is) {
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
  public LGPProgram hatch(final int[][] genotype) {
    return new LGPProgram(this.m_is, genotype);
  }
}

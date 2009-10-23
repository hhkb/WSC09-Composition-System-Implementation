/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-19
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.genome.Int2StringPostProcessor.java
 * Last modification: 2007-11-19
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

import org.dgpf.lgp.base.ILGPParameters;
import org.dgpf.lgp.base.InstructionSet;
import org.sfc.math.Mathematics;

/**
 * A preprocessor for the int[][] genome of lgp programs.
 * 
 * @author Thomas Weise
 */
public final class Int2StringPostProcessor {

  /**
   * Preprocess a program.
   * 
   * @param program
   *          the program
   * @param parameters
   *          the parameters
   * @return the preprocessed result
   */
  public static final int[][] postProcess(final int[][] program,
      final ILGPParameters parameters) {

    stage1(program, parameters);

    return program;
  }

  /**
   * Preprocess a program.
   * 
   * @param program
   *          the program
   * @param parameters
   *          the parameters
   */
  private static final void stage1(final int[][] program,
      final ILGPParameters parameters) {
    InstructionSet is;
    int i, j, in, l;
    int[] x;

    is = parameters.getInstructions();
    l = is.size();

    for (i = (program.length - 1); i >= 0; i--) {
      x = program[i];
      for (j = (x.length - 4); j >= 0; j -= 4) {
        x[j] = in = Mathematics.modulo(x[j], l);
        is.get(in).postProcessParameters(program, i, j, parameters);
      }
    }

  }

}

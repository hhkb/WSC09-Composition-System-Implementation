/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.bitString.redundancy.XorNetworks.java
 * Last modification: 2008-03-01
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

package org.sigoa.refimpl.genomes.bitString.redundancy;

import java.io.Serializable;

import org.sfc.math.BinaryMath;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;

/**
 * An xor-based redundant network approach
 * 
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class XorNetworks<G extends Serializable, PP extends Serializable>
    extends Redundancy<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new xor redundancy network
   * 
   * @param next
   *          the next embryogeny
   * @param inputGeneSize
   *          the input gene size
   */
  public XorNetworks(final IEmbryogeny<G, PP> next, final int inputGeneSize) {
    super(next, inputGeneSize, inputGeneSize * (inputGeneSize + 1));
  }

  /**
   * Translate the input data to output data. A gene is treated as a
   * sequence of initial values followed by xor connections
   * 
   * <pre>
   * [abcd 1110 1010 0001 0000] =&gt; [a&circ;b&circ;c, a&circ;c, d, 0]  
   * </pre>
   * 
   * @param input
   *          the input
   * @param inputSize
   *          the input size in genes
   * @param output
   *          the output
   */
  @Override
  protected void translate(final byte[] input, final int inputSize,
      final byte[] output) {
    int i, o, j, k, l;
    final int is, os;
    boolean v;

    is = this.m_inputGeneSize;
    os = this.m_outputGeneSize;
    i = is * inputSize;
    o = os * inputSize;

    for (; i > 0;) {
      l = i - is;
      for (j = os; j > 0; j--) {
        v = false;
        for (k = os; k > 0; k--) {
          if (BinaryMath.getBit(input, --i)) {
            v ^= BinaryMath.getBit(input, l + k);
          }
        }
        BinaryMath.setBit(output, --o, v);
      }
      i = l;
    }
  }
}

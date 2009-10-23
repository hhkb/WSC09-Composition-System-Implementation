/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-23
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.base.genome.RBGPBitStringEmbryogeny.java
 * Last modification: 2007-09-23
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

package org.dgpf.rbgp.base.genome;

import org.dgpf.rbgp.base.ActionSet;
import org.dgpf.rbgp.base.EComparison;
import org.dgpf.rbgp.base.RBGPProgram;
import org.dgpf.rbgp.base.Rule;
import org.dgpf.rbgp.base.SymbolSet;
import org.sfc.io.binary.BinaryInputStream;
import org.sfc.math.BinaryMath;
import org.sfc.math.Mathematics;
import org.sigoa.refimpl.genomes.bitString.embryogeny.BitStringEmbryogeny;

/**
 * The base class for bit string embryogenies
 * 
 * @author Thomas Weise
 */
public class RBGPBitStringEmbryogeny extends
    BitStringEmbryogeny<RBGPProgram> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The eight comparisons
   */
  static final EComparison[] COMPS = EComparison.values();

  /**
   * the comparison bits
   */
  static final int COMPS_BITS = BinaryMath.getBits(COMPS.length);

  /**
   * the internal action set
   */
  private final ActionSet m_actions;

  /**
   * the action bits
   */
  private final int m_actionBits;

  /**
   * the action count
   */
  private final int m_actionCnt;

  /**
   * the symbol set
   */
  private final SymbolSet m_symbols;

  /**
   * the symbol bits
   */
  private final int m_symbolBits;

  /**
   * the symbol count
   */
  private final int m_symbolCnt;

  /**
   * the total count of bits
   */
  private final int m_granularity;

  /**
   * Create a new classifier embryogeny.
   * 
   * @param actions
   *          the actions
   * @param symbols
   *          the symbols
   * @param isGrayCoded
   *          <code>true</code> if and only if this embryogeny is gray
   *          coded, <code>false</code> otherwise
   */
  public RBGPBitStringEmbryogeny(final ActionSet actions,
      final SymbolSet symbols, final boolean isGrayCoded) {
    super(isGrayCoded);
    this.m_actions = actions;
    this.m_actionCnt = actions.size();
    this.m_actionBits = BinaryMath.getBits(this.m_actionCnt);
    this.m_symbols = symbols;
    this.m_symbolCnt = symbols.size();
    this.m_symbolBits = BinaryMath.getBits(this.m_symbolCnt);

    this.m_granularity = ((this.m_symbolBits * 6) + this.m_actionBits
        + (2 * COMPS_BITS) + 1);

  }

  /**
   * Create a new classifier embryogeny.
   * 
   * @param actions
   *          the actions
   * @param symbols
   *          the symbols
   */
  public RBGPBitStringEmbryogeny(final ActionSet actions,
      final SymbolSet symbols) {
    this(actions, symbols, false);
  }

  /**
   * Obtain the granularity.
   * 
   * @return the granularity
   */
  public final int getGranularity() {
    return this.m_granularity;
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
  @SuppressWarnings("unchecked")
  public RBGPProgram hatch(final byte[] genotype) {
    BinaryInputStream bis;
    Rule[] r;
    int i;

    bis = this.acquireBitStringInputStream();
    bis.init(genotype);
    i = (bis.availableBits() / this.m_granularity);

    // if (i > 100) {
    // r = new Rule[0];
    // } else {
    r = new Rule[i];
    for (--i; i >= 0; i--) {
      r[i] = new Rule(

      COMPS[bis.readBits(COMPS_BITS)],//

          this.m_symbols.get(Mathematics.modulo(bis
              .readBits(this.m_symbolBits), this.m_symbolCnt)),//

          this.m_symbols.get(Mathematics.modulo(bis
              .readBits(this.m_symbolBits), this.m_symbolCnt)),//

          COMPS[bis.readBits(COMPS_BITS)], //

          this.m_symbols.get(Mathematics.modulo(bis
              .readBits(this.m_symbolBits), this.m_symbolCnt)),//

          this.m_symbols.get(Mathematics.modulo(bis
              .readBits(this.m_symbolBits), this.m_symbolCnt)),//

          bis.readBoolean(),//

          this.m_actions.get(Mathematics.modulo(bis
              .readBits(this.m_actionBits), this.m_actionCnt)),//

          this.m_symbols.get(Mathematics.modulo(bis
              .readBits(this.m_symbolBits), this.m_symbolCnt)),//

          this.m_symbols.get(Mathematics.modulo(bis
              .readBits(this.m_symbolBits), this.m_symbolCnt)));
    }
    // }

    this.releaseBitStringInputStream(bis);
    return new RBGPProgram(r);
  }

}

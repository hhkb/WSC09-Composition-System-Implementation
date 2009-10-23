/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.bitString.embryogeny.BitStringEmbryogeny.java
 * Last modification: 2007-05-28
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

package org.sigoa.refimpl.genomes.bitString.embryogeny;

import java.io.Serializable;

import org.sfc.io.binary.BinaryInputStream;
import org.sfc.io.binary.BinaryOutputStream;
import org.sigoa.refimpl.genomes.bitString.BinaryInputStreamBuffer;
import org.sigoa.refimpl.genomes.bitString.BinaryOutputStreamBuffer;
import org.sigoa.refimpl.go.embryogeny.Embryogeny;

/**
 * A default base class for all embryogenies that en- and decode bit
 * strings.
 * 
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class BitStringEmbryogeny<PP extends Serializable> extends
    Embryogeny<byte[], PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the bit string embryogeny input stream buffer
   */
  private final BinaryInputStreamBuffer m_bis;

  /**
   * the bit string embryogeny output stream buffer
   */
  private final BinaryOutputStreamBuffer m_bos;

  /**
   * Create a new bit string embryogeny
   * 
   * @param isGrayCoded
   *          <code>true</code> if and only if this embryogeny is gray
   *          coded, <code>false</code> otherwise
   */
  protected BitStringEmbryogeny(final boolean isGrayCoded) {
    super();
    this.m_bis = new BinaryInputStreamBuffer(isGrayCoded);
    this.m_bos = new BinaryOutputStreamBuffer(isGrayCoded);
  }

  /**
   * Create a new bit string embryogeny which is not gray-coded
   */
  protected BitStringEmbryogeny() {
    this(false);
  }

  /**
   * Acquire an input stream
   * 
   * @return the bit string input stream
   */
  protected final BinaryInputStream acquireBitStringInputStream() {
    return this.m_bis.allocate();
  }

  /**
   * Release a bit string input stream
   * 
   * @param bis
   *          the bit string input stream no longer needed
   */
  protected final void releaseBitStringInputStream(
      final BinaryInputStream bis) {
    this.m_bis.dispose(bis);
  }

  /**
   * Acquire an output stream
   * 
   * @return the bit string output stream
   */
  protected final BinaryOutputStream acquireBitStringOutputStream() {
    return this.m_bos.allocate();
  }

  /**
   * Release a bit string output stream
   * 
   * @param bos
   *          the bit string output stream no longer needed
   */
  protected final void releaseBitStringOutputStream(
      final BinaryOutputStream bos) {
    this.m_bos.dispose(bos);
  }

  /**
   * Check whether this bit string embryogeny is gray coded.
   * 
   * @return <code>true</code> if and only if this embryogeny is gray
   *         coded, <code>false</code> otherwise.
   */
  public boolean isGrayCoded() {
    return this.m_bis.isGrayCoded();
  }
}

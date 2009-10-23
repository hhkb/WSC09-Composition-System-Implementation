/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.bitString.BinaryOutputStreamBuffer.java
 * Last modification: 2007-10-22
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

package org.sigoa.refimpl.genomes.bitString;

import org.sfc.io.binary.BinaryOutputStream;
import org.sfc.io.binary.GrayCodedBinaryOutputStream;

/**
 * This class allows you to allocate and dispose reusable bit string output
 * streams.
 * 
 * @author Thomas Weise
 */
public class BinaryOutputStreamBuffer extends
    BinaryObjectBuffer<BinaryOutputStream> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new bit string output stream buffer
   * 
   * @param isGrayCoded
   *          <code>true</code> if and only if this buffer provides gray
   *          coded streams, <code>false</code> otherwise
   */
  public BinaryOutputStreamBuffer(final boolean isGrayCoded) {
    super(isGrayCoded);
  }

  /**
   * Create an instance of the heavy weight object
   * 
   * @return the instance of the heavy weight object
   */
  @Override
  protected BinaryOutputStream create() {

    if (this.m_isGrayCoded)
      return new GrayCodedBinaryOutputStream();

    return new BinaryOutputStream();
  }

}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.bitString.BinaryObjectBuffer.java
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

import org.sfc.collections.buffers.SynchronizedObjectBuffer;

/**
 * This class allows you to allocate and dispose reusable bit string
 * streams.
 * 
 * @param <TP>
 *          the type to buffer
 * @author Thomas Weise
 */
abstract class BinaryObjectBuffer<TP> extends SynchronizedObjectBuffer<TP> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * <code>true</code> if and only if this embryogeny is gray coded,
   * <code>false</code> otherwise.
   */
  final boolean m_isGrayCoded;

  /**
   * Create a new binary object buffer
   * 
   * @param isGrayCoded
   *          <code>true</code> if and only if this object buffer is gray
   *          coded, <code>false</code> otherwise
   */
  public BinaryObjectBuffer(final boolean isGrayCoded) {
    super();
    this.m_isGrayCoded = isGrayCoded;
  }

  /**
   * Returns whether this buffer is gray coded or not.
   * 
   * @return <code>true</code> if and only if this buffer is gray coded,
   *         <code>false</code> for normal encoding
   */
  public boolean isGrayCoded() {
    return this.m_isGrayCoded;
  }
}

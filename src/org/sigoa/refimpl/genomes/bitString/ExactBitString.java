/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-01-27
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.bitString.ExactBitString.java
 * Last modification: 2007-01-27
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

import java.io.Serializable;

import org.sfc.math.BinaryMath;
import org.sfc.math.Mathematics;
import org.sfc.text.JavaTextable;
import org.sfc.text.TextUtils;
import org.sfc.utils.ICloneable;

/**
 * An exact binary string genotype
 * 
 * @author Thomas Weise
 */
public class ExactBitString extends JavaTextable implements Serializable,
    ICloneable {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the empty bytes
   */
  private static final byte[] EMPTY = new byte[0];

  /**
   * the empty exact bit string
   */
  public static final ExactBitString EMPTY_EXACT_BIT_STRING = new ExactBitString(
      0, null);

  /**
   * the length in bits
   */
  final int m_length;

  /**
   * the data
   */
  final byte[] m_data;

  /**
   * Create a new exact bit string genotype.
   * 
   * @param length
   *          the length
   */
  public ExactBitString(final int length) {
    this(length, (length > 0) ? new byte[Mathematics.ceilDiv(length, 8)]
        : EMPTY);
  }

  /**
   * Create a new exact bit string genotype.
   * 
   * @param length
   *          the length
   * @param data
   *          the data
   */
  public ExactBitString(final int length, final byte[] data) {
    super();

    if ((length <= 0) || (data == null) || ((data.length << 3) < length)) {
      this.m_length = 0;
      this.m_data = EMPTY;
    } else {
      this.m_length = length;
      this.m_data = data;
    }
  }

  /**
   * Obtain the binary data
   * 
   * @return the binary data
   */
  public final byte[] getData() {
    return this.m_data;
  }

  /**
   * Obtain the length of this genotype in bits
   * 
   * @return the length of this genotype in bits
   */
  public final int getLength() {
    return this.m_length;
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
    sb.append(this.m_length);
    sb.append(", new "); //$NON-NLS-1$
    TextUtils.append(this.m_data, sb);
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
    int i, l;
    byte[] d;

    l = this.m_length;
    d = this.m_data;
    for (i = 0; i < l; i++) {
      sb.append(BinaryMath.getBit(d, i) ? '1' : '0');
    }
  }

  /**
   * Copy this object
   * 
   * @return an exact copy of this object
   */
  public ExactBitString copy() {
    return new ExactBitString(this.m_length,
        (this.m_length > 0) ? this.m_data.clone() : this.m_data);
  }

  /**
   * Copy this object
   * 
   * @return an exact copy of this object
   */
  @Override
  public Object clone() {
    return this.copy();
  }
}

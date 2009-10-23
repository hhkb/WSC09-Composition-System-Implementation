/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-20
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.bitString.embryogeny.BitStringToDoubleArrayEmbryogeny.java
 * Last modification: 2006-12-20
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

import org.sfc.io.binary.BinaryInputStream;
import org.sfc.io.binary.BinaryOutputStream;

/**
 * This embryogeny transforms bit strings into vectors of doubles.
 *
 * @author Thomas Weise
 */
public class BitStringToDoubleArrayEmbryogeny extends
    BitStringEmbryogeny<double[]> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new hatcher.
   *
   * @param isGrayCoded
   *          <code>true</code> if and only if the data in the genome is
   *          gray coded.
   */
  public BitStringToDoubleArrayEmbryogeny(final boolean isGrayCoded) {
    super(isGrayCoded);
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
  public double[] hatch(final byte[] genotype) {
    double[] b;
    int i;
    BinaryInputStream bis;

    if (genotype == null)
      throw new NullPointerException();

    bis = this.acquireBitStringInputStream();
    bis.init(genotype);

    i = (genotype.length >>> 3);
    b = new double[i];
    for (--i; i >= 0; i--) {
      b[i] = bis.readDouble();
    }

    bis.clear();

    this.releaseBitStringInputStream(bis);
    return b;
  }

  /**
   * Compute a genotype from a phenotype. This method can be used if
   * different optimizers optimize a phenotype using different genotypes
   * and want to exchange individuals.
   *
   * @param phenotype
   *          the phenotype
   * @return the genotype regressed from the phenotype
   * @throws NullPointerException
   *           if <code>phenotype==null</code>.
   * @throws UnsupportedOperationException
   *           if the operation is not supported
   */
  @Override
  public byte[] regress(final double[] phenotype) {
    BinaryOutputStream bos;
    byte[] b;
    int i;

    if (phenotype == null)
      throw new NullPointerException();

    bos = this.acquireBitStringOutputStream();
    synchronized (bos) {
      for (i = (phenotype.length - 1); i >= 0; i--) {
        bos.writeDouble(phenotype[i]);
      }

      b = bos.getOutput();
      bos.clear();
    }

    this.releaseBitStringOutputStream(bos);
    return b;
  }
}

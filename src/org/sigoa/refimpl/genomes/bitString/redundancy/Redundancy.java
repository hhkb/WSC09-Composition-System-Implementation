/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.bitString.redundancy.Redundancy.java
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

import org.sfc.math.Mathematics;
import org.sigoa.refimpl.genomes.bitString.ExactBitString;
import org.sigoa.refimpl.go.embryogeny.Embryogeny;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;

/**
 * This is the base class for all systems which introduce redundancy in
 * form of neutral networks.
 * 
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public abstract class Redundancy<G extends Serializable, PP extends Serializable>
    extends Embryogeny<G, PP> {

  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the next embryogeny level
   */
  private final IEmbryogeny<G, PP> m_next;

  /**
   * whether this class uses the <code>ExactBitString</code> genome or
   * directly <code>byte[]</code>s
   */
  private transient boolean m_exact;

  /**
   * is the exact value set?
   */
  private transient boolean m_set;

  /**
   * the output gene size;
   */
  final int m_outputGeneSize;

  /**
   * the input gene size;
   */
  final int m_inputGeneSize;

  /**
   * Create a new redundancy component.
   * 
   * @param next
   *          the next embryogeny
   * @param outputGeneSize
   *          the gene size
   * @param inputGeneSize
   *          the input gene size
   */
  protected Redundancy(final IEmbryogeny<G, PP> next,
      final int outputGeneSize, final int inputGeneSize) {
    super();
    this.m_next = next;
    this.m_outputGeneSize = Math.max(1, outputGeneSize);
    this.m_inputGeneSize = Math.max(this.m_outputGeneSize, inputGeneSize);
  }

  /**
   * Obtain the output gene size
   * 
   * @return the output gene size
   */
  public int getOutputGeneSize() {
    return this.m_outputGeneSize;
  }

  /**
   * Obtain the input gene size
   * 
   * @return the input gene size
   */
  public int getInputGeneSize() {
    return this.m_inputGeneSize;
  }

  /**
   * Translate the input data to output data.
   * 
   * @param input
   *          the input
   * @param inputSize
   *          the input size in genes
   * @param output
   *          the output
   */
  protected abstract void translate(final byte[] input,
      final int inputSize, final byte[] output);

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
  public PP hatch(final G genotype) {
    G x;
    ExactBitString g, o;
    byte[] y, z;
    int l;

    if (!(this.m_set)) {
      this.m_exact = (!(genotype instanceof byte[]));
      this.m_set = true;
    }

    if (this.m_exact) {
      o = ((ExactBitString) genotype);
      l = o.getLength() / this.m_inputGeneSize;
      g = new ExactBitString(l * this.m_outputGeneSize);
      y = o.getData();
      z = g.getData();
      x = ((G) g);
    } else {
      y = ((byte[]) genotype);
      l = (y.length << 3) / this.m_inputGeneSize;
      z = new byte[Mathematics.ceilDiv(l * this.m_outputGeneSize, 8)];
      x = ((G) z);
    }

    this.translate(y, l, z);

    return this.m_next.hatch(x);
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-07
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.hlgp.base.genetics.HLGPLGPEmbryogeny.java
 * Last modification: 2007-03-07
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

package org.dgpf.hlgp.base.genetics;

import org.dgpf.hlgp.base.HLGPProgram;
import org.dgpf.hlgp.base.compiler.HLGPCompiler;
import org.dgpf.lgp.base.ILGPParameters;
import org.dgpf.lgp.base.LGPProgram;
import org.sfc.collections.buffers.DirectBuffer;
import org.sigoa.refimpl.go.embryogeny.Embryogeny;

/**
 * The embryogeny for transforming high-level code to low-level code.
 * 
 * @author Thomas Weise
 */
public class HLGPLGPEmbryogeny extends Embryogeny<HLGPProgram, LGPProgram> {

  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the parameters
   */
  final ILGPParameters m_parameters;

  /**
   * the buffer
   */
  private final BufferClass m_buffer;

  /**
   * Create a new embryogeny.
   * 
   * @param parameters
   *          the lgp parameters
   */
  public HLGPLGPEmbryogeny(final ILGPParameters parameters) {
    super();
    this.m_parameters = parameters;
    this.m_buffer = new BufferClass();
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
  public LGPProgram hatch(final HLGPProgram genotype) {
    HLGPCompiler c;
    LGPProgram p;

    synchronized (this.m_buffer) {
      c = this.m_buffer.allocate();
    }

    genotype.compile(c);
    p = c.getProgram();

    c.clear();
    synchronized (this.m_buffer) {
      this.m_buffer.dispose(c);
    }

    return p;
  }

  /**
   * Compute a genotype from a phenotype. This method can be used if
   * different optimizers optimize a phenotype using different genotypes
   * and want to exchange individuals. Here it is possible to return
   * <code>null</code> if regression cannot be performed.
   * 
   * @param phenotype
   *          the phenotype
   * @return the genotype regressed from the phenotype or <code>null</code>
   *         if regression was not possible
   * @throws NullPointerException
   *           if <code>phenotype==null</code>.
   */
  @Override
  public HLGPProgram regress(final LGPProgram phenotype) {
    return HLGPProgram.EMPTY_PROGRAM;
  }

  /**
   * the internal compiler buffer class
   * 
   * @author Thomas Weise
   */
  private final class BufferClass extends DirectBuffer<HLGPCompiler> {
    /**
     * the serial version uid
     */
    private static final long serialVersionUID = 1;

    /**
     * the constructor
     */
    BufferClass() {
      super();
    }

    /**
     * Create a new instance of the buffered object type.
     * 
     * @return the new instance, or <code>null</code> if the creation
     *         failed somehow
     */
    @Override
    protected HLGPCompiler create() {
      return new HLGPCompiler(HLGPLGPEmbryogeny.this.m_parameters);
    }
  }
}

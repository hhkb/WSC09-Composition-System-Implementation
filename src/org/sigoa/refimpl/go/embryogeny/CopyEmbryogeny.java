/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.embryogeny.CopyEmbryogeny.java
 * Last modification: 2007-09-22
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

package org.sigoa.refimpl.go.embryogeny;

import java.io.Serializable;

import org.sigoa.spec.go.embryogeny.IEmbryogeny;

/**
 * This embryogeny can only be used if genotype and phenotype are
 * identically
 * 
 * @param <GP>
 *          The genotype.
 * @author Thomas Weise
 */
public class CopyEmbryogeny<GP extends Serializable> extends
    Embryogeny<GP, GP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared instance of the copy embryogeny
   */
  public static final IEmbryogeny<?, ?> COPY_EMBRYOGENY = new CopyEmbryogeny<Serializable>();

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
  public GP hatch(final GP genotype) {
    if (genotype == null)
      throw new NullPointerException();
    return (genotype);
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
  public GP regress(final GP phenotype) {
    if (phenotype == null)
      throw new NullPointerException();
    return (phenotype);
  }

  /**
   * read resolve
   * 
   * @return the read resolved object
   */
  private final Object readResolve() {
    if (this.getClass() == CopyEmbryogeny.class)
      return COPY_EMBRYOGENY;
    return this;
  }

  /**
   * write resolve
   * 
   * @return the write resolved object
   */
  private final Object writeReplace() {
    return this.readResolve();
  }
}

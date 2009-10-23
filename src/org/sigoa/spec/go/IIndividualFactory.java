/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.IIndividualFactory.java
 * Last modification: 2006-12-12
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

package org.sigoa.spec.go;

import java.io.Serializable;

/**
 * this interface is allows the instantiation of individuals
 *
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public interface IIndividualFactory<G extends Serializable, PP extends Serializable>
    extends Serializable {

  /**
   * This method is called in order to create a new individual.
   *
   * @param objectiveFunctionCount
   *          the count of objective functions
   * @return the new, empty individual record
   * @throws IllegalArgumentException
   *           if <code>objectiveFunctionCount&lt;=0</code>
   */
  public abstract IIndividual<G, PP> createIndividual(
      final int objectiveFunctionCount);

  /**
   * This method is called when a new individual should be created that is
   * derived from an existing one (i.e. a mutated offspring). The resulting
   * individual has the same objective function count as its parent.
   *
   * @param parent
   *          the parent individual
   * @return the new, empty individual record
   * @throws NullPointerException
   *           if <code>parent==null</code>
   * @throws IllegalArgumentException
   *           if <code>parent</code> has a invalid count of objective
   *           functions
   */
  public abstract IIndividual<G, PP> createIndividual(
      final IIndividual<G, PP> parent);

  /**
   * This method is called when a new individual should be created that is
   * derived from two existing ones (i.e. a crossover offspring). The
   * resulting individual has the same objective function count as its
   * parents.
   *
   * @param parent1
   *          the first parent individual
   * @param parent2
   *          the second parent individual
   * @return the new, empty individual record
   * @throws NullPointerException
   *           if <code>parent1==null || parent2==null</code>
   * @throws IllegalArgumentException
   *           if the parents have different or invalid numbers of
   *           objective functions
   */
  public abstract IIndividual<G, PP> createIndividual(
      final IIndividual<G, PP> parent1, final IIndividual<G, PP> parent2);

}

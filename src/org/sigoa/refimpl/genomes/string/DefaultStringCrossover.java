/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-06-17
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.string.DefaultStringCrossover.java
 * Last modification: 2008-06-17
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

package org.sigoa.refimpl.genomes.string;

import java.io.Serializable;

import org.sigoa.refimpl.go.reproduction.IterativeMultiCrossover;
import org.sigoa.spec.go.reproduction.ICrossover;

/**
 * The default string crossover class
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public class DefaultStringCrossover<G extends Serializable> extends
    IterativeMultiCrossover<G> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new multi-mutator.
   * 
   * @param editor
   *          the string editor to be used to process the genome
   * @param variableLength
   *          <code>true</code> if and only if the genome is of variable
   *          length, <code>false</code> for fixed length.
   */
  public DefaultStringCrossover(final StringEditor<G> editor,
      final boolean variableLength) {
    super(createCrossover(editor, variableLength),
        createProbs(variableLength));
  }

  /**
   * Create a new multi-crossover.
   * 
   * @param editor
   *          the string editor to be used to process the genome
   * @param variableLength
   *          <code>true</code> if and only if the genome is of variable
   *          length, <code>false</code> for fixed length.
   * @param <GG>
   *          the genotype
   * @return the crossover set
   */
  @SuppressWarnings("unchecked")
  private static final <GG extends Serializable> ICrossover<GG>[] createCrossover(
      final StringEditor<GG> editor, final boolean variableLength) {

    if (variableLength) {
      return new ICrossover[] {
          new FixedLengthStringNPointCrossover<GG>(editor),
          new VariableLengthStringNPointCrossover<GG>(editor) };
    }

    return new ICrossover[] { new FixedLengthStringNPointCrossover<GG>(
        editor) };
  }

  /**
   * Create a new multi-mutator.
   * 
   * @param variableLength
   *          <code>true</code> if and only if the genome is of variable
   *          length, <code>false</code> for fixed length.
   * @return the mutator set
   */
  private static final double[] createProbs(final boolean variableLength) {
    if (variableLength) {
      return new double[] { 3d, 1d };
    }
    return new double[] { 1d };
  }

}

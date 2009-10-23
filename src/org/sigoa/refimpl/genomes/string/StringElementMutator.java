/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.string.StringElementMutator.java
 * Last modification: 2007-11-16
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

import org.sigoa.spec.go.reproduction.IMutator;

/**
 * This mutator changes the elements of a string consecutively. In
 * otherwords, it mutates a substring.
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public abstract class StringElementMutator<G extends Serializable> extends
    StringReproductionOperator<G> implements IMutator<G> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the maximum number of elements (not genes) to be changed
   */
  final int m_maxChanges;

  /**
   * Instantiate a new string reproduction operation
   * 
   * @param editor
   *          the string editor
   * @param maxChanges
   *          the maximum number of elements (not genes) to be changed
   * @throws IllegalArgumentException
   *           if <code>maxChanges&lt;1</code>
   */
  protected StringElementMutator(final StringEditor<G> editor,
      final int maxChanges) {
    super(editor);
    if (maxChanges < 1)
      throw new IllegalArgumentException();
    this.m_maxChanges = maxChanges;
  }

  /**
   * Obtain the maximum number of changes that can be performed.
   * 
   * @return the maximum number of changes that can be performed
   */
  public int getMaxChanges() {
    return this.m_maxChanges;
  }
}

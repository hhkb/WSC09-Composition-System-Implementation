/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.string.StringReproductionOperator.java
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

import org.sigoa.refimpl.go.ImplementationBase;

/**
 * a creator able to create bit strings
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public abstract class StringReproductionOperator<G extends Serializable>
    extends ImplementationBase<G, Serializable> {

  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the string editor
   */
  final StringEditor<G> m_editor;

  /**
   * Instantiate a new string reproduction operation
   * 
   * @param editor
   *          the string editor
   */
  protected StringReproductionOperator(final StringEditor<G> editor) {
    super();
    this.m_editor = editor;
  }

  /**
   * Obtain the string editor used by this reproduction operation.
   * 
   * @return the string editor used by this reproduction operation
   */
  public StringEditor<G> getEditor() {
    return this.m_editor;
  }
}

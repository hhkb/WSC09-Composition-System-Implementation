/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.genome.plain.Int2StringLGPEditor.java
 * Last modification: 2007-11-18
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

package org.dgpf.lgp.base.genome.plain;

/**
 * The editor for two dimensional integer string genomes.
 * 
 * @author Thomas Weise
 */
public class Int2StringLGPEditor extends Int2StringEditor {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Instantiate a new string editor operation as to be used for plain
   * linear genetic programming
   * 
   * @param minLength
   *          the minimum length in genes of a genotype
   * @param maxLength
   *          the maximum length in genes of a genotype
   * @throws IllegalArgumentException
   *           if <code>minLength&lt;=0||maxLength&lt;minLength</code>
   */
  public Int2StringLGPEditor(final int minLength, final int maxLength) {
    super(minLength, maxLength, 2, 100, 4);
  }

}

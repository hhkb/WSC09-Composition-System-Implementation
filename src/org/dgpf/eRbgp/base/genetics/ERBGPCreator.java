/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : TODO
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.eRbgp.base.genetics.ERBGPCreator.java
 * Last modification: TODO
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

package org.dgpf.eRbgp.base.genetics;

import org.dgpf.eRbgp.base.InternalProgram;
import org.dgpf.eRbgp.base.TreeRBGPProgram;
import org.sigoa.refimpl.genomes.tree.reproduction.TreeCreator;
import org.sigoa.refimpl.go.reproduction.Creator;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * the tree rbgp creator
 * 
 * @author Thomas Weise
 */
public class ERBGPCreator extends Creator<TreeRBGPProgram> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the tree creator
   */
  private final TreeCreator m_creator;

  /**
   * create a new tree creator
   * 
   * @param language
   *          the language
   */
  public ERBGPCreator(final DefaultRBGPLanguage language) {
    super();
    this.m_creator = new TreeCreator(language);
  }

  /**
   * Create a single new random genotype
   * 
   * @param random
   *          The randomizer to be used.
   * @return The resulting genotype.
   * @throws NullPointerException
   *           if <code>random==null</code>.
   */
  public TreeRBGPProgram create(final IRandomizer random) {
    return new TreeRBGPProgram(
        ((InternalProgram)(this.m_creator.create(random))));
  }
}

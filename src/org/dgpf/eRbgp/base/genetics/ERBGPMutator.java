/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.eRbgp.base.genetics.ERBGPMutator.java
 * Last modification: 2008-04-18
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
import org.sigoa.refimpl.genomes.tree.reproduction.DefaultTreeMutator;
import org.sigoa.refimpl.genomes.tree.reproduction.TreeCreator;
import org.sigoa.refimpl.go.reproduction.Mutator;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * the tree rbgp mutator
 * 
 * @author Thomas Weise
 */
public class ERBGPMutator extends Mutator<TreeRBGPProgram> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the default tree mutator
   */
  private final DefaultTreeMutator m_mutator;

  /**
   * create a new tree creator
   * 
   * @param language
   *          the language
   */
  public ERBGPMutator(final DefaultRBGPLanguage language) {
    super();
    this.m_mutator = new DefaultTreeMutator(new TreeCreator(language, 4,
        0.2, null));
  }

  /**
   * Perform one single mutation.
   * 
   * @param source
   *          The source genotype.
   * @param random
   *          The randomizer to be used.
   * @return The resulting genotype.
   * @throws NullPointerException
   *           if <code>source==null||random==null</code>.
   */
  @Override
  public TreeRBGPProgram mutate(final TreeRBGPProgram source,
      final IRandomizer random) {
    InternalProgram in;

    in = ((InternalProgram) (this.m_mutator.mutate(source.m_program,
        random)));
    if ((in != null) && (in != source.m_program)) {
      return new TreeRBGPProgram(in);
    }

    return source;
  }
}

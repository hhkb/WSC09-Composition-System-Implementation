/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-15
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.tree.reproduction.DefaultTreeMutator.java
 * Last modification: 2007-12-15
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

package org.sigoa.refimpl.genomes.tree.reproduction;

import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.refimpl.go.reproduction.MultiMutator;
import org.sigoa.spec.go.reproduction.IMutator;

/**
 * The default form of the tree mutator
 * 
 * @author Thomas Weise
 */
public class DefaultTreeMutator extends MultiMutator<Node> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The default mutator for trees.
   * 
   * @param creator
   *          the creator to be internally used for creating trees
   */
  @SuppressWarnings("unchecked")
  public DefaultTreeMutator(final TreeCreator creator) {
    super(new IMutator[] {//
        SubTreeDeleteMutator.SUB_TREE_DELETE_MUTATOR,
            new SubTreeInsertMutator(creator),
            new SubTreeReplaceMutator(creator),
            SubTreeMutator.SUB_TREE_MUTATOR,
            SubTreeLiftMutator.SUB_TREE_LIFT_MUTATOR,
            new SubTreeSinkMutator(creator.m_factories) },//
        new double[] { 3, 1, 1, 1, 1, 1 });
  }

}

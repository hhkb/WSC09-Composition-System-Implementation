/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-19
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.reproduction.PostProcessingCreator.java
 * Last modification: 2007-11-19
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

package org.sigoa.refimpl.go.reproduction;

import java.io.Serializable;

import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * A creator that postprocesses the results of another creator,
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public class PostProcessingCreator<G extends Serializable> extends
    Creator<G> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the real creator
   */
  private final ICreator<G> m_creator;

  /**
   * Create a new postprocessing creator.
   * 
   * @param creator
   *          the true creator
   */
  public PostProcessingCreator(final ICreator<G> creator) {
    super();
    this.m_creator = creator;
  }

  /**
   * The postprocessing routing.
   * 
   * @param genotype
   *          the genotype
   * @return the postprocessed genotype.
   */
  protected G postprocess(final G genotype) {
    return genotype;
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
  public G create(final IRandomizer random) {
    return this.postprocess(this.m_creator.create(random));
  }
}

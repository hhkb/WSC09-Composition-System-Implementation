/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-08
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.simulation.ICompetitionSimulation.java
 * Last modification: 2007-11-08
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

package org.sigoa.spec.simulation;

import java.io.Serializable;

/**
 * A simulation class that can be used especially for competitions.
 * 
 * @param <PP>
 *          The phenotype used as input for the simulation.
 * @author Thomas Weise
 */
public interface ICompetitionSimulation<PP extends Serializable> extends
    ISimulation<PP> {
  /**
   * Begin the simulation of the specified individual.
   * 
   * @param contestant1
   *          the first contestant individual
   * @param contestant2
   *          the second contestant individual
   * @throws NullPointerException
   *           if <code>what</code> is <code>null</code>.
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  public abstract void beginIndividual(final PP contestant1,
      final PP contestant2);

  /**
   * Begin the simulation of the specified individual which is going to
   * compete with itself.
   * 
   * @param what
   *          the individual
   * @throws NullPointerException
   *           if <code>what</code> is <code>null</code>.
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  public abstract void beginIndividual(final PP what);
}

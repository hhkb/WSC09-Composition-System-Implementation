/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-05-10
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.symbolicRegression.ComputationContext.java
 * Last modification: 2008-05-10
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

package org.dgpf.symbolicRegression;

import java.io.Serializable;

import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.refimpl.simulation.Simulation;

/**
 * The computation context
 * 
 * @param <PP>
 *          the node type
 * @author Thomas Weise
 */
public class ComputationContext<PP extends Serializable> extends
    Simulation<PP> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the number of negligible terms
   */
  private int m_negligible;

  /**
   * the number of mathematical errors
   */
  private int m_errors;

  /**
   * This method is called right before the simulation begins.
   * 
   * @throws IllegalStateException
   *           if this simulation is already running or the
   *           {@link #beginIndividual(Serializable)} method has not yet
   *           been called or {@link #endIndividual()} has already been
   *           called.
   */
  @Override
  public void beginSimulation() {
    super.beginSimulation();
    this.clear();
  }

  /**
   * clear the errors an all
   */
  public void clear() {
    this.m_negligible = 0;
    this.m_errors = 0;
  }

  /**
   * increase the number of negligible terms
   * 
   * @param node
   *          the negligible node
   */
  public final void negligible(final Node node) {
    this.m_negligible += (node != null) ? node.getWeight() : 1;
  }

  /**
   * Obtain the number of negligible terms
   * 
   * @return the number of negligible terms
   */
  public double getNegligible() {
    return this.m_negligible;
  }

  /**
   * increase the number of erraneous terms
   * 
   * @param node
   *          the node detecting the error
   */
  public void setError(final Node node) {
    this.m_errors++;
  }

  /**
   * Obtain the number of erraneous terms
   * 
   * @return the number of erraneous terms
   */
  public final double getErrors() {
    return this.m_errors;
  }
}

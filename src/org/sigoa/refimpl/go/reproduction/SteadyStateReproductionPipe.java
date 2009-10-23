/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-15
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.reproduction.SteadyStateReproductionPipe.java
 * Last modification: 2007-05-15
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

import org.sigoa.refimpl.pipe.Pipe;
import org.sigoa.spec.go.reproduction.ISteadyStateReproduction;

/**
 * A pipeline that has the property of possible being steady state
 * 
 * @param <G>
 *          The genotype
 * @param <PP>
 *          The phenotype
 * @author Thomas Weise
 */
public class SteadyStateReproductionPipe<G extends Serializable, PP extends Serializable>
    extends Pipe<G, PP> implements ISteadyStateReproduction {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the steady state property
   */
  volatile boolean m_steadyState;

  /**
   * Create a new steady state pipeline.
   * 
   * @param steadyState
   *          <code>true</code> if and only if this pipeline is steady
   *          state, <code>false</code> otherwise
   */
  public SteadyStateReproductionPipe(final boolean steadyState) {
    super();
    this.m_steadyState = steadyState;
  }

  /**
   * Create a new steady state pipeline.
   */
  public SteadyStateReproductionPipe() {
    this(true);
  }

  /**
   * Returns whether this pipeline is steady state or not.
   * 
   * @return <code>true</code> if and only if this pipeline is steady
   *         state, <code>false</code> otherwise
   */
  public boolean isSteadyState() {
    return this.m_steadyState;
  }

  /**
   * Set this pipeline to be steady state.
   * 
   * @param steadyState
   *          The new steady state property: <code>true</code> if and
   *          only if this pipeline should become steady state,
   *          <code>false</code> if it should not be steady state anymore
   */
  public void setSteadyState(final boolean steadyState) {
    this.m_steadyState = steadyState;
  }
}

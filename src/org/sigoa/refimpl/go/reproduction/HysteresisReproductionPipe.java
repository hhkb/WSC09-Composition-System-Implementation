/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-06
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.reproduction.SteadyStateReproductionPipe.java
 * Last modification: 2007-09-06
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

import org.sfc.math.Mathematics;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.reproduction.IHysteresisReproduction;

/**
 * A pipeline that has the property of possible being steady state and may
 * apply hysteresis (offspring has same fitness as parent)
 * 
 * @param <G>
 *          The genotype
 * @param <PP>
 *          The phenotype
 * @author Thomas Weise
 */
public class HysteresisReproductionPipe<G extends Serializable, PP extends Serializable>
    extends SteadyStateReproductionPipe<G, PP> implements
    IHysteresisReproduction {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * <code>true</code> if and only if the offspring starts out with the
   * same fitness and objective values than its parent,<code>false</code>
   * otherwise
   */
  volatile boolean m_hystersis;

  /**
   * Create a new steady state/hysteresis pipeline.
   * 
   * @param steadyState
   *          <code>true</code> if and only if this pipeline is steady
   *          state, <code>false</code> otherwise
   */
  public HysteresisReproductionPipe(final boolean steadyState) {
    this(steadyState, false);
  }

  /**
   * Create a new steady state /hysteresis pipeline.
   * 
   * @param steadyState
   *          <code>true</code> if and only if this pipeline is steady
   *          state, <code>false</code> otherwise
   * @param hysteresis
   *          <code>true</code> if hysteresis should be applied,
   *          <code>false</code> otherwise
   */
  public HysteresisReproductionPipe(final boolean steadyState,
      final boolean hysteresis) {
    super(steadyState);
    this.m_hystersis = hysteresis;
  }

  /**
   * Create a new steady state/hysteresis pipeline.
   */
  public HysteresisReproductionPipe() {
    this(true);
  }

  /**
   * Does this pipe apply hysteresis?
   * 
   * @return <code>true</code> if hysteresis should be applied,
   *         <code>false</code> otherwise
   */
  public boolean usesHysteresis() {
    return this.m_hystersis;
  }

  /**
   * Set whether this reproduction pipe should apply hysteresis or not
   * 
   * @param hysteresis
   *          <code>true</code> if hysteresis should be applied,
   *          <code>false</code> otherwise
   */
  public void setHysteresisUsage(final boolean hysteresis) {
    this.m_hystersis = hysteresis;
  }

  /**
   * Copy the evaluation values from one individual to another
   * 
   * @param src
   *          the source
   * @param dst
   *          the destination
   * @return <code>true</code> if copying was successful,
   *         <code>false</code> otherwise
   */
  static final boolean copyEvaluation(final IIndividual<?, ?> src,
      final IIndividual<?, ?> dst) {
    double o;
    int i;

    o = src.getFitness();
    if (Mathematics.isNumber(o)) {
      dst.setFitness(src.getFitness());
    } else
      return false;

    for (i = (dst.getObjectiveValueCount() - 1); i >= 0; i--) {
      o = src.getObjectiveValue(i);
      if (!(Mathematics.isNumber(o))) {
        dst.clearEvaluation();
        return false;
      }
      dst.setObjectiveValue(i, o);
    }

    return true;
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-09
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.evaluation.EvaluationGroup.java
 * Last modification: 2007-11-09
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

package org.sigoa.refimpl.go.evaluation;

import java.io.Serializable;

import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.go.objectives.IObjectiveFunction;

/**
 * A group of objective functions that can be evaluated with the same
 * simulator.
 * 
 * @author Thomas Weise
 */
public class EvaluationGroup extends
    ImplementationBase<Serializable, Serializable> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1l;

  /**
   * the identifier
   */
  final Serializable m_id;

  /**
   * the objectives
   */
  final IObjectiveFunction<?, ?, ?, ?>[] m_objectives;

  /**
   * Create a new evaluation group
   * 
   * @param id
   *          the required simulation id
   * @param objectives
   *          the objective functions
   */
  public EvaluationGroup(final Serializable id,
      final IObjectiveFunction<?, ?, ?, ?>[] objectives) {
    super();
    this.m_id = id;
    this.m_objectives = objectives;
  }

  /**
   * Create an evaluation group by copying another one.
   * 
   * @param e
   *          the evaluation group
   */
  protected EvaluationGroup(final EvaluationGroup e) {
    this(e.m_id, e.m_objectives);
  }
}

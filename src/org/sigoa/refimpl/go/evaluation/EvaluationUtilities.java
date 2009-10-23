/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-09
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.evaluation.EvaluationUtilities.java
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
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.utils.ErrorUtils;
import org.sigoa.spec.go.objectives.IObjectiveFunction;

/**
 * Utilities for evaluation.
 * 
 * @author Thomas Weise
 */
public final class EvaluationUtilities {

  /**
   * the standard evaluation group factory
   */
  public static final IEvaluationGroupFactory DEFAULT_EVALUATION_GROUP_FACTORY = new IEvaluationGroupFactory() {
    /**
     * the serial version uid
     */
    private static final long serialVersionUID = 1l;

    /**
     * create the evaluation group
     */
    public EvaluationGroup createEvaluationGroup(final Serializable id,
        final IObjectiveFunction<?, ?, ?, ?>[] objectives) {
      return new EvaluationGroup(id, objectives);
    }

    /**
     * Resolve this comparator at deserialization.
     * 
     * @return The right comparator instance.
     */
    private final Object readResolve() {
      return DEFAULT_EVALUATION_GROUP_FACTORY;
    }

    /**
     * The write replace method.
     * 
     * @return the comparator instance to be written
     */
    private final Object writeReplace() {
      return DEFAULT_EVALUATION_GROUP_FACTORY;
    }
  };

  /**
   * Group the objective given functions to groups as large as possible
   * that all can use the same simulation.
   * 
   * @param objectives
   *          the objective functions
   * @return the groups
   */
  @SuppressWarnings("unchecked")
  public static final EvaluationGroup[] groupObjectives(
      final List<IObjectiveFunction<?, ?, ?, ?>> objectives) {
    return groupObjectives(objectives, DEFAULT_EVALUATION_GROUP_FACTORY);
  }

  /**
   * Group the objective given functions to groups as large as possible
   * that all can use the same simulation.
   * 
   * @param objectives
   *          the objective functions
   * @param factory
   *          the evaluation group factory
   * @return the groups
   */
  @SuppressWarnings("unchecked")
  public static final EvaluationGroup[] groupObjectives(
      final List<IObjectiveFunction<?, ?, ?, ?>> objectives,
      final IEvaluationGroupFactory factory) {
    Serializable[] sers;
    Serializable x;
    int l, i, j, k;
    Class<?> c1, c2;
    EvaluationGroup[] g;
    List<IObjectiveFunction<?, ?, ?, ?>>[] y;
    IObjectiveFunction<?, ?, ?, ?> o;

    l = objectives.size();
    if (l <= 0)
      return new EvaluationGroup[0];

    sers = new Serializable[l];
    y = new List[l];
    l--;
    k = 0;
    main: for (i = l; i >= 0; i--) {
      o = objectives.get(i);
      x = o.getRequiredSimulationId();
      for (j = (k - 1); j >= 0; j--) {
        if (sers[j] == x)
          continue main;
        if ((x instanceof Class) && (sers[j] instanceof Class)) {
          c1 = ((Class) x);
          c2 = ((Class) (sers[j]));

          if (c1.isAssignableFrom(c2))
            continue main;
          if (c2.isAssignableFrom(c1)) {
            sers[j] = x;
            continue main;
          }
        }
      }
      y[k] = CollectionUtils.createList();
      y[k].add(o);
      sers[k++] = x;
    }

    g = new EvaluationGroup[k];
    for (--k; k >= 0; k--) {
      g[k] = new EvaluationGroup(sers[k],// 
          y[k].toArray(new IObjectiveFunction[y[k].size()]));
    }

    return g;
  }

  /**
   * the forbidden constructor
   */
  private EvaluationUtilities() {
    ErrorUtils.doNotCall();
  }

  /**
   * the evaluation group factory
   * 
   * @author Thomas Weise
   */
  public interface IEvaluationGroupFactory extends Serializable {

    /**
     * Create a new evaluation group
     * 
     * @param id
     *          the required simulation id
     * @param objectives
     *          the objective functions
     * @return the new evaluation group
     */
    public abstract EvaluationGroup createEvaluationGroup(
        final Serializable id,
        final IObjectiveFunction<?, ?, ?, ?>[] objectives);
  }
}

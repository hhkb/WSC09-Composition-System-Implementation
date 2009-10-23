/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : TODO
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.objectives.wrapper.AutoObjectives.java
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

package org.sigoa.refimpl.go.objectives.wrapper;

import java.io.Serializable;
import java.util.Arrays;

import org.sfc.math.Mathematics;
import org.sigoa.refimpl.go.algorithms.ea.EA;
import org.sigoa.refimpl.pipe.BufferedPipe;
import org.sigoa.refimpl.pipe.Pipeline;
import org.sigoa.refimpl.stoch.StatisticInfo;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.OptimizationUtils;
import org.sigoa.spec.go.evaluation.IEvaluatorPipe;
import org.sigoa.spec.pipe.IPipe;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * This class is able to control the behavior of objective functions
 * 
 * @author Thomas Weise
 */
public class AutoObjectives extends
    BufferedPipe<Serializable, Serializable> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the default values
   */
  private final double[] m_defaults;

  /**
   * the statistic information records
   */
  private final StatisticInfo[] m_infos;

  /**
   * the booles
   */
  private final boolean[] m_booles;

  /**
   * the rotator
   */
  private int m_rotate;

  /**
   * Create a new auto objective function setter
   * 
   * @param objectives
   *          the number of objectives
   */
  @SuppressWarnings("unchecked")
  public AutoObjectives(final int objectives) {
    super();

    int i;
    StatisticInfo[] infos;

    this.m_defaults = new double[objectives];
    this.m_booles = new boolean[objectives];
    this.m_infos = infos = new StatisticInfo[objectives];
    for (i = (objectives - 1); i >= 0; i--) {
      infos[i] = new StatisticInfo();
    }
  }

  /**
   * Process the buffered population.
   * 
   * @param source
   *          The buffered source population.
   * @param size
   *          The count of individuals in the population array.
   */
  @Override
  protected void process(
      final IIndividual<Serializable, Serializable>[] source,
      final int size) {
    StatisticInfo[] f;
    IIndividual<Serializable, Serializable> ind;
    double d;
    int i, j, sze, t;
    final boolean[] use;
    final double[] def;
    StatisticInfo inf;
    IRandomizer rand;

    f = this.m_infos;
    for (j = (f.length - 1); j >= 0; j--) {
      f[j].clear();
    }

    sze = size;
    def = this.m_defaults;
    main: for (i = (sze - 1); i >= 0; i--) {
      ind = source[i];
      for (j = (f.length - 1); j >= 0; j--) {
        d = ind.getObjectiveValue(j);
        if (Mathematics.isNumber(d)) {
          def[j] = d;
        } else {
          source[j] = source[--sze];
          continue main;
        }
      }

      for (j = (f.length - 1); j >= 0; j--) {
        d = def[j];
        if (d < OptimizationUtils.WORST_NUMERIC)
          f[j].append(d);
      }
    }

    t = 0;
    use = this.m_booles;
    for (i = (f.length - 1); i >= 0; i--) {
      inf = f[i];

      Arrays.fill(use, true);
      if (Mathematics.collate(inf.getMinimum(), inf.getMaximum()) < 1e-3) {
        t++;
        use[i] = false;
      }
      d = inf.getAverage();
      if (!(Mathematics.isNumber(d))) {
        d = inf.getMinimum();
        if (!(Mathematics.isNumber(d))) {
          inf.getMaximum();
          if (!(Mathematics.isNumber(d))) {
            d = 1d;
          }
        }
      }
      def[i] = d;
    }

    i = use.length;
    if (t >= i) {
      rand = this.getRandomizer();
      if (rand == null) {
        i = Mathematics.modulo(this.m_rotate++, i);
      } else {
        i = rand.nextInt(i);
      }
      use[i] = true;
    }

    for (i = (sze - 1); i >= 0; i--) {
      ind = source[i];
      if (t != 0) {
        for (j = (def.length - 1); j >= 0; j--) {
          if (use[j])
            ind.setObjectiveValue(j, def[j]);
        }
      }
      this.output(ind);
    }
  }

  /**
   * Prepare an evolutionary algorithm with an auto objective
   * 
   * @param ea
   *          the ea
   * @param objectives
   *          the number of objectives
   */
  @SuppressWarnings("unchecked")
  public static final void prepareEA(final EA<?, ?> ea,
      final int objectives) {
    Pipeline<?, ?> p;
    int i;

    p = ea.getPipeline();
    for (i = (p.size() - 1); i >= 0; i--) {
      if (p.get(i) instanceof IEvaluatorPipe) {
        p.add(i + 1, (IPipe) (new AutoObjectives(objectives)));
      }
    }
  }
}

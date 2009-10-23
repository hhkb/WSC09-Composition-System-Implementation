/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-22
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.benchmark.GPBenchmarkNonFunctionalObjectiveFunction.java
 * Last modification: 2007-10-22
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

package test.org.dgpf.benchmark.epistasisTransform;

import java.io.Serializable;

import org.sigoa.refimpl.go.objectives.ObjectiveFunction;
import org.sigoa.refimpl.go.objectives.ObjectiveState;
import org.sigoa.spec.simulation.ISimulation;

/**
 * The non functional objective function of the genetic programming
 * benchmark.
 * 
 * @author Thomas Weise
 */
public class TObjectiveFunction
    extends
    ObjectiveFunction<int[], ObjectiveState, Serializable, ISimulation<int[]>> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /** */
  private static final int N = TransformationEvolver.N;

  /** */
  public final static int[][] MATRIX = new int[N][N];

  /**
   * bla
   * 
   * @param x
   * @param y
   * @return x
   */
  private static final int h(final int x, final int y) {
    int i, a, b;

    i = 0;
    a = x;
    b = y;
    while ((a != 0) || (b != 0)) {
      if (((a & 1) ^ (b & 1)) != 0)
        i++;
      a >>>= 1;
      b >>>= 1;
    }
    return i;
  }

  static {
    int i, j;

    for (i = (MATRIX.length - 1); i >= 0; i--) {
      for (j = (MATRIX.length - 1); j >= 0; j--) {
        MATRIX[i][j] = h(i, j);
      }
    }
  }

  /**
   * Create a new objective function.
   */
  public TObjectiveFunction() {
    super();
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    sb.append("transform"); //$NON-NLS-1$
  }

  /**
   * In this method, we evaluate the objective value of the genetic
   * programming fitness landscape.
   * 
   * @param individual
   *          The individual that should be evaluated next.
   * @param state
   *          The state record.
   * @param staticState
   *          the static state record
   * @param simulation
   *          The simulation (<code>null</code> if no simulation is
   *          required as indivicated by
   *          <code>getRequiredSimulationSteps</code>).
   * @throws NullPointerException
   *           if <code>individual==null</code> or
   *           <code>state==null</code> or if
   *           <code>simulator==null</code> but a simulation is required.
   */
  @Override
  public void endEvaluation(final int[] individual,
      final ObjectiveState state, final Serializable staticState,
      final ISimulation<int[]> simulation) {
    int i, j, d1, d2, s;

    s = 0;
    for (i = (N - 1); i > 0; i--) {
      for (j = (i - 1); j >= 0; j--) {
        d1 = individual[i];
        d2 = individual[j];
        if (d1 == d2)
          s += N;
        else {

          d2 = MATRIX[d1][d2];
          d1 = MATRIX[i][j];
          if (d1 <= 1) {

            System.out.println(i + "-" + j + //$NON-NLS-1$
                " d=" + d1 + //$NON-NLS-1$
                "  ->  " + individual[i] + //$NON-NLS-1$
                "-" + individual[j] + //$NON-NLS-1$
                " d=" + d2);//$NON-NLS-1$

            if (d1 != TransformationEvolver.K) {
              if (d2 < (TransformationEvolver.K - d1))
                s++;
            }
          }
        }
      }
    }

    state.setObjectiveValue(s);
  }
}

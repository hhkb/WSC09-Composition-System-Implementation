/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-09
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.dmc.ClassificationSimulation.java
 * Last modification: 2007-05-09
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

package test.org.sigoa.dmc.dmc07;

import java.util.Arrays;

import org.sfc.text.TextUtils;
import org.sigoa.refimpl.simulation.Simulation;
import org.sigoa.refimpl.simulation.SimulationProvider;
import org.sigoa.spec.simulation.ISimulationProvider;

/**
 * The classification simulation
 *
 * @author Thomas Weise
 */
public class ClassificationSimulation extends Simulation<ClassifierSystem> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the shared simulation provider
   */
  public static final ISimulationProvider PROVIDER = new SimulationProvider(
      ClassificationSimulation.class);

  /**
   * the classes
   */
  private static final EClasses[] CLASSES = EClasses.values();

  /** a */
  private static final int A = EClasses.A.ordinal();

  /** b */
  private static final int B = EClasses.B.ordinal();

  /** n */
  private static final int N = EClasses.N.ordinal();

  /**
   * the counter for a-errors
   */
  private final int[][] m_classifications;

  /**
   * the constructor
   */
  public ClassificationSimulation() {
    super();
    this.m_classifications = new int[EClasses.CLASS_COUNT][EClasses.CLASS_COUNT];
  }

  /**
   * This method is called right before the simulation begins.
   *
   * @param what
   *          The item to be simulated.
   * @throws NullPointerException
   *           if <code>what</code> is <code>null</code>.
   */
  @Override
  public void beginIndividual(final ClassifierSystem what) {
    super.beginIndividual(what);

    int i;
    int[][] x;

    x = this.m_classifications;
    for (i = (x.length - 1); i >= 0; i--)
      Arrays.fill(x[i], 0);

    for (i = (Datasets.DATA.length - 1); i >= 0; i--) {
      x[Datasets.CLASSES[i].ordinal()][what.classify(Datasets.DATA[i])
          .ordinal()]++;
    }
  }

  /**
   * Obtain the count of how often the specified class was classified as
   * the other specified one.
   *
   * @param real
   *          the real class
   * @param as
   *          the classification
   * @return how often elements of the class <code>real</code> were
   *         classified as class <code>as</code>
   */
  public int getClassification(final EClasses real, final EClasses as) {
    return this.m_classifications[real.ordinal()][as.ordinal()];
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
    String[][] s;
    int i, j, m, z;
    int[][] x;
    String ss;

    s = new String[EClasses.CLASS_COUNT + 1][EClasses.CLASS_COUNT + 1];
    x = this.m_classifications;
    m = 2;
    for (j = (EClasses.CLASS_COUNT - 1); j >= 0; j--) {
      s[0][j + 1] = CLASSES[j].name();
    }
    s[0][0] = "   "; //$NON-NLS-1$
    for (i = (EClasses.CLASS_COUNT - 1); i >= 0; i--) {
      s[i + 1][0] = CLASSES[i].name() + ": "; //$NON-NLS-1$
      for (j = (EClasses.CLASS_COUNT - 1); j >= 0; j--) {
        ss = String.valueOf(x[i ][j]);
        m = Math.max(m, ss.length());
        s[i + 1][j + 1] = ss;
      }
    }

    for (i = 0; i < EClasses.CLASS_COUNT+1; i++) {
      for (j = 0; j < EClasses.CLASS_COUNT+1; j++) {
        if (j > 0)
          sb.append(' ');
        ss = s[i][j];
        z = (m - ss.length());
        if (j > 0)
          TextUtils.appendSpaces(sb, z >>> 1);
        sb.append(ss);
        if (j > 0)
          TextUtils.appendSpaces(sb, z - (z >>> 1));
      }
      sb.append(TextUtils.LINE_SEPARATOR);
    }

    sb.append(TextUtils.LINE_SEPARATOR);
    sb.append("profit: "); //$NON-NLS-1$
    sb.append(this.getProfit());
  }

  /**
   * Compute the profit value.
   *
   * @return the profit
   */
  public int getProfit() {
    int[][] data;

    data = this.m_classifications;

    return (3 * data[A][A]) + (6 * data[B][B])
        - (data[N][A] + data[N][B] + data[A][B] + data[B][A]);
  }
}

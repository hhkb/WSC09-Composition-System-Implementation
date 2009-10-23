/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-30
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.simulation.LoggingCalculation.java
 * Last modification: 2007-03-30
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

package org.dgpf.aggregation.alternative;

import org.dgpf.aggregation.net.IAggregationFunction;
import org.sfc.io.TextWriter;
import org.sfc.io.writerProvider.IWriterProvider;

/**
 * A calculation that loggs its output
 *
 * @author Thomas Weise
 */
public class LoggingCalculation extends Calculation {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * A writer provider to be used to obtain the writers to print the output
   * to
   */
  private final IWriterProvider<TextWriter> m_provider;

  /**
   * A writer provider to be used to obtain the writers to print the
   * targets to
   */
  private final IWriterProvider<TextWriter> m_provider2;

  /**
   * A writer provider to be used to obtain the writers to print the inputs
   * to
   */
  private final IWriterProvider<TextWriter> m_provider3;

  /**
   * the output textwriter
   */
  private TextWriter m_out;

  /**
   * the output textwriter
   */
  private TextWriter m_out2;

  /**
   * the output textwriter
   */
  private TextWriter m_out3;

  /**
   * Create a new calculation.
   *
   * @param params
   *          the parameters
   * @param compare
   *          the aggregation function to compare with
   * @param provider
   *          a writer provider to be used to obtain the writers to print
   *          the output to
   * @param provider2
   *          a writer provider to be used to obtain the writers to print
   *          the targets to
   * @param provider3
   *          a writer provider to be used to obtain the writers to print
   *          the inputs to
   */
  public LoggingCalculation(final CalculationParameters params,
      final IAggregationFunction compare,
      final IWriterProvider<TextWriter> provider,
      final IWriterProvider<TextWriter> provider2,
      final IWriterProvider<TextWriter> provider3) {
    super(params, compare);
    this.m_provider = provider;
    this.m_provider2 = provider2;
    this.m_provider3 = provider3;
  }

  /**
   * This method is called right before the simulation begins.
   *
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  @Override
  public void beginSimulation() {
    super.beginSimulation();
    this.m_out = this.m_provider.provideWriter(Long.valueOf(this.m_index));
    this.m_out2 = this.m_provider2.provideWriter(Long.valueOf(this.m_index));
    this.m_out3 = this.m_provider3.provideWriter(Long.valueOf(this.m_index));
  }

  /**
   * This method is called when the simulation has ended.
   *
   * @throws IllegalStateException
   *           If this simulation is not yet running.
   */
  @Override
  public void endSimulation() {
    if (this.m_out != null) {
      this.m_out.release();
      this.m_out = null;
    }
    if (this.m_out2 != null) {
      this.m_out2.release();
      this.m_out2 = null;
    }
    if (this.m_out3 != null) {
      this.m_out3.release();
      this.m_out3 = null;
    }
    super.endSimulation();
  }

  /**
   * update the internal values
   *
   * @param v
   *          the current values
   * @param t
   *          the destination
   * @param variance
   *          the variance
   */
  @Override
  final void updateValues(final double[][] v, final double variance,
      final double t) {
    int i, s;
    TextWriter w, w2;
    super.updateValues(v, variance, t);

    s = this.m_stepIdx;
    w = this.m_out2;
    w.ensureNewLine();
    w.writeInt(s);
    w.writeCSVSeparator();
    w.writeDouble(t);

    w = this.m_out;
    w2 = this.m_out3;
    for (i = (v.length - 1); i >= 0; i--) {
      w.ensureNewLine();
      w.writeInt(s);
      w.writeCSVSeparator();
      w.writeDouble(v[i][1]);
      w2.ensureNewLine();
      w2.writeInt(s);
      w2.writeCSVSeparator();
      w2.writeDouble(this.m_values[this.m_index][this.m_stepIdx][i]);
    }
  }
}

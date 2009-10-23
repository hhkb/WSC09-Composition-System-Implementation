/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-02
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.postProcessing.processors.SeriesSuccessInformation.java
 * Last modification: 2007-10-02
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

package org.sigoa.refimpl.utils.testSeries.postProcessing.processors;

import org.sfc.io.TextWriter;
import org.sigoa.refimpl.utils.testSeries.postProcessing.EProcessorTypes;
import org.sigoa.refimpl.utils.testSeries.postProcessing.SeriesProcessor;

/**
 * The series-based success information gatherer
 * 
 * @author Thomas Weise
 */
public class SeriesBestObjectivePlot extends SeriesProcessor {

  /**
   * the success generation
   */
  private final int m_objective;

  /**
   * Create a new success based series processor
   * 
   * @param dir
   *          the directory
   * @param prefix
   *          the prefix
   * @param objective
   *          the objective to plot
   */
  public SeriesBestObjectivePlot(final Object dir, final String prefix,
      final int objective) {
    super(dir, prefix, EProcessorTypes.SERIES);
    this.m_objective = objective;
  }

  /**
   * begin a new run
   * 
   * @param name
   *          the name of the run
   */
  @Override
  public void beginRun(final String name) {
    TextWriter w;

    super.beginRun(name);

    w = this.getOut();
    if ((w != null) && (!(w.isNewLine()))) {
      w.newLine();
      w.newLine();
      w.newLine();
    }
  }

  /**
   * Process the given run
   * 
   * @param data
   *          the data of the run
   */
  @Override
  public void processRun(final double[][][] data) {
    double[][] gen;
    final int obj;
    int i, j;
    double x, y;
    TextWriter w;

    w = this.getOut();

    if (w != null) {

      obj = this.m_objective;
      for (i = 0; i < data.length; i++) {
        gen = data[i];
        x = Double.POSITIVE_INFINITY;
        for (j = (gen.length - 1); j >= 0; j--) {
          y = gen[j][obj];
          if (Double.compare(y, x) < 0)
            x = y;
        }

        w.ensureNewLine();
        w.writeInt(i);
        w.writeCSVSeparator();
        w.writeDouble(x);
      }
    }
  }
}

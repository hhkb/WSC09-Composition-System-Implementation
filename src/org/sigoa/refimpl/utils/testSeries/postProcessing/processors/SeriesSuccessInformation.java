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

/**
 * The series-based success information gatherer
 * 
 * @author Thomas Weise
 */
public class SeriesSuccessInformation extends SuccessBasedSeriesProcessor {

  /**
   * the success generation
   */
  private int m_successGen;

  /**
   * Create a new success based series processor
   * 
   * @param dir
   *          the directory
   * @param prefix
   *          the prefix
   * @param success
   *          the success definition
   */
  public SeriesSuccessInformation(final Object dir, final String prefix,
      final ISuccessDefinition success) {
    super(dir, prefix, EProcessorTypes.SERIES, success);
  }

  /**
   * begin a new run
   * 
   * @param name
   *          the name of the run
   */
  @Override
  public void beginRun(final String name) {
    super.beginRun(name);
    this.m_successGen = -1;
  }

  /**
   * Begin the series of the given name
   * 
   * @param name
   *          the name of the series
   * @param runCnt
   *          the number of runs to expect
   */
  @Override
  public void beginSeries(final String name, final int runCnt) {
    TextWriter w;

    super.beginSeries(name, runCnt);

    w = this.getOut();
    if (w != null) {
      w.write("run"); //$NON-NLS-1$
      w.writeCSVSeparator();
      w.write("success?");//$NON-NLS-1$
      w.writeCSVSeparator();
      w.write("success_generation");//$NON-NLS-1$
    }
  }

  /**
   * Process the given success individual.
   * 
   * @param individual
   *          the success individual to be processed
   * @param generation
   *          the generation in which this individual occured
   * @return <code>true</code> if and only if the success-based
   *         processing should be continued, <code>false</code> if that
   *         was all that we wanted to know
   */
  @Override
  protected boolean processSuccess(final double[] individual,
      final int generation) {
    this.m_successGen = generation;

    return false;
  }

  /**
   * end a run
   */
  @Override
  public void endRun() {
    TextWriter w;

    w = this.getOut();
    if (w != null) {

      w.ensureNewLine();
      w.write(this.getRunName());
      w.writeCSVSeparator();
      w.writeInt((this.m_successGen >= 0) ? 1 : 0);
      w.writeCSVSeparator();
      w.writeInt(this.m_successGen);
    }

    super.endRun();
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-02
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.postProcessing.processors.SuccessBasedSeriesProcessor.java
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

import org.sigoa.refimpl.utils.testSeries.postProcessing.EProcessorTypes;
import org.sigoa.refimpl.utils.testSeries.postProcessing.SeriesProcessor;

/**
 * this test series processor is based on successful outcomes of an
 * experiment
 * 
 * @author Thomas Weise
 */
public class SuccessBasedSeriesProcessor extends SeriesProcessor {

  /**
   * the minimum generations a success must last to be considered as a real
   * success.
   */
  private static final int MIN_GENERATIONS = 3;

  /**
   * the success definition
   */
  private final ISuccessDefinition m_def;

  /**
   * Create a new success based series processor
   * 
   * @param dir
   *          the directory
   * @param prefix
   *          the prefix
   * @param type
   *          the type of the processor
   * @param success
   *          the success definition
   */
  protected SuccessBasedSeriesProcessor(final Object dir,
      final String prefix, final EProcessorTypes type,
      final ISuccessDefinition success) {
    super(dir, prefix, type);
    this.m_def = success;
  }

  /**
   * Check whether the individual denoted by this record of objective
   * values can be regarded as a success.
   * 
   * @param individual
   *          the individual record
   * @return <code>true</code> if and only if this individual was a
   *         success
   */
  protected boolean isSuccess(final double[] individual) {
    return this.m_def.isSuccess(individual);
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
  protected boolean processSuccess(final double[] individual,
      final int generation) {
    return false;
  }

  /**
   * Process the given run
   * 
   * @param data
   *          the data of the run
   * @return the first success generation
   */
  public int findFistSuccessGeneration(final double[][][] data) {
    int i, j, sg, lg;
    double[][] d2;

    // find the first _real_ success
    sg = -1;
    lg = -1;
    outer: for (i = 0; i < data.length; i++) {
      d2 = data[i];
      for (j = (d2.length - 1); j >= 0; j--) {
        if (this.isSuccess(d2[j])) {
          if ((sg <= -1) || (i != (lg + 1))) {
            lg = sg = i;
          } else {
            lg = i;
            if ((lg - sg) >= MIN_GENERATIONS)
              return sg;
          }

          continue outer;
        }
      }
    }

    return -1;
  }

  /**
   * Process the given run
   * 
   * @param data
   *          the data of the run
   */
  @Override
  public void processRun(final double[][][] data) {
    int i, j;
    double[][] d2;
    double[] d;

    // find the first _real_ success
    i = this.findFistSuccessGeneration(data);
    if (i < 0)
      return;

    // now output all the successes, starting with the success generation
    for (; i < data.length; i++) {
      d2 = data[i];
      for (j = (d2.length - 1); j >= 0; j--) {
        d = d2[j];
        if (this.isSuccess(d)) {
          if (!(this.processSuccess(d, i)))
            return;
        }
      }
    }
  }

}

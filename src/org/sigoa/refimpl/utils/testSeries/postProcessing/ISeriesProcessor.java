/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-02
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.postProcessing.ISeriesProcessor.java
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

package org.sigoa.refimpl.utils.testSeries.postProcessing;

/**
 * This interface denotes a processor that works on a complete test series
 * 
 * @author Thomas Weise
 */
public interface ISeriesProcessor {

  /**
   * begin processing the data
   */
  public abstract void beginProcessing();

  /**
   * end processing the data
   */
  public abstract void endProcessing();

  /**
   * Begin the series of the given name
   * 
   * @param name
   *          the name of the series
   * @param runCnt
   *          the number of runs to expect
   */
  public abstract void beginSeries(final String name, final int runCnt);

  /**
   * end the current series
   */
  public abstract void endSeries();

  /**
   * begin a new run
   * 
   * @param name
   *          the name of the run
   */
  public abstract void beginRun(final String name);

  /**
   * end a run
   */
  public abstract void endRun();

  /**
   * Process the given run
   * 
   * @param data
   *          the data of the run
   */
  public abstract void processRun(final double[][][] data);

}

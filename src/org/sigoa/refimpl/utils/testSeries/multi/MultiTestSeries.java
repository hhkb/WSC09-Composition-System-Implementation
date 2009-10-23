/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.multi.MultiTestSeries.java
 * Last modification: 2008-04-22
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

package org.sigoa.refimpl.utils.testSeries.multi;

import java.io.File;

import org.sfc.io.CanonicalFile;
import org.sfc.io.IO;
import org.sfc.parallel.SfcThread;
import org.sfc.utils.Utils;
import org.sigoa.refimpl.stoch.Randomizer;
import org.sigoa.refimpl.utils.testSeries.ParameterizedTestSeries;
import org.sigoa.refimpl.utils.testSeries.TestSeries;

/**
 * A multi test series
 * 
 * @author Thomas Weise
 */
public class MultiTestSeries extends SfcThread {

  /**
   * the test series
   */
  private final ITestSeriesFactory[] m_series;

  /**
   * the base dir
   */
  private final CanonicalFile m_baseDir;

  /**
   * Create a new multi test series
   * 
   * @param series
   *          the test series
   * @param baseDir
   *          the base dir
   */
  public MultiTestSeries(final ITestSeriesFactory[] series,
      final Object baseDir) {
    super();

    this.m_series = series.clone();
    this.m_baseDir = IO.getFile(baseDir);
  }

  /**
   * Perform the work of this thread.
   */
  @Override
  protected void doRun() {
    final ITestSeriesFactory[] s;
    int i;
    ITestSeriesFactory tf;
    TestSeries t;

    s = this.m_series;
    i = new Randomizer().nextInt(s.length);
    while (this.isRunning()) {
      tf = s[i];
      i = ((i + 1) % (s.length));
      t = tf.create(new File(this.m_baseDir, tf.getName()));
      if (t instanceof ParameterizedTestSeries) {
        ((ParameterizedTestSeries) t).limitRunsToAllConfigurations();
      }
      t.start();
      t.waitFor(false);
      t.abort();
      t = null;
      tf = null;
      Utils.invokeGC();
    }
  }
}

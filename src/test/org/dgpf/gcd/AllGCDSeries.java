/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-19
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.AllGCDSeries.java
 * Last modification: 2007-11-19
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

package test.org.dgpf.gcd;

import java.io.File;

import org.sfc.io.CanonicalFile;
import org.sfc.io.IO;
import org.sfc.utils.Utils;
import org.sigoa.refimpl.stoch.Randomizer;

import test.org.dgpf.gcd.hlgp.GCDHLGPTestSeries;
import test.org.dgpf.gcd.lgp.GCDLGPTestSeries;
import test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries;

/**
 * This program performs all gcd test series.
 * 
 * @author Thomas Weise
 */
public class AllGCDSeries {

  /**
   * the base dir
   */
  public static final CanonicalFile BASE_DIR = IO.getFile("gcd"); //$NON-NLS-1$

  /**
   * the rbgp gcd series factory
   */
  public static final ISeriesFactory RBGP = new ISeriesFactory() {

    public GCDTestSeries create(final Object dir) {
      return new GCDRBGPTestSeries(dir);
    }

    /**
     * obtain the name of the series
     * 
     * @return the name of the series
     */
    public String getName() {
      return "rbgp"; //$NON-NLS-1$
    }
  };

  /**
   * the lgp gcd series factory
   */
  public static final ISeriesFactory LGP = new ISeriesFactory() {

    public GCDTestSeries create(final Object dir) {
      return new GCDLGPTestSeries(dir);
    }

    /**
     * obtain the name of the series
     * 
     * @return the name of the series
     */
    public String getName() {
      return "lgp"; //$NON-NLS-1$
    }
  };

  /**
   * the hlgp gcd series factory
   */
  public static final ISeriesFactory HLGP = new ISeriesFactory() {

    public GCDTestSeries create(final Object dir) {
      return new GCDHLGPTestSeries(dir);
    }

    /**
     * obtain the name of the series
     * 
     * @return the name of the series
     */
    public String getName() {
      return "hlgp"; //$NON-NLS-1$
    }
  };

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    ISeriesFactory[] f;
    int i;
    GCDTestSeries x;

    f = new ISeriesFactory[] { RBGP, LGP, HLGP };
    i = new Randomizer().nextInt(f.length);

    for (;;) {
      i = ((i + 1) % f.length);
      x = f[i].create(new File(BASE_DIR, f[i].getName()));

      x.start();
      x.waitFor(false);
      x = null;
      Utils.invokeGC();
    }
  }

  /**
   * the series factory
   * 
   * @author Thomas Weise
   */
  private interface ISeriesFactory {
    /**
     * create a new gcd test series
     * 
     * @param dir
     *          the directory
     * @return the new test series
     */
    abstract GCDTestSeries create(final Object dir);

    /**
     * obtain the name of the series
     * 
     * @return the name of the series
     */
    abstract String getName();
  }
}

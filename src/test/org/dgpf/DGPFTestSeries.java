/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.GCDTestSeries.java
 * Last modification: 2007-09-22
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

package test.org.dgpf;

import java.util.Iterator;

import org.sigoa.refimpl.utils.testSeries.EDataGrouping;
import org.sigoa.refimpl.utils.testSeries.ea.EATestSeries;

/**
 * The standard settings for the gcd problem
 * 
 * @author Thomas Weise
 */
public abstract class DGPFTestSeries extends EATestSeries {

  /**
   * Create a new parameterized test series based on evolutionary
   * algorithms
   * 
   * @param titles
   *          the titles of the test series parameters
   * @param iterator
   *          the parameter iterator
   * @param dir
   *          the directory to write to
   * @param grouping
   *          how the data should be grouped
   * @param maxRuns
   *          the maximum runs this test series is running
   */
  public DGPFTestSeries(final Object dir, final EDataGrouping grouping,
      final int maxRuns, final String[] titles,
      final Iterator<Object[]> iterator) {
    super(dir, grouping, maxRuns,//
        titles, iterator);

  }

  /**
   * Obtain the number of available processors.
   * 
   * @return the number of available processors.
   */
  @Override
  protected int getProcessorCount() {
    int i;
    String s;

    i = super.getProcessorCount();

    s = this.getRootDirectory().getCanonicalPath();

    if (s.contains("/home/fb16/tweise/") || //$NON-NLS-1$
        s.contains("/gpfs/home02/fb16/tweise/"))//$NON-NLS-1$
      return Math.min(1, i);
    return i;
  }
}

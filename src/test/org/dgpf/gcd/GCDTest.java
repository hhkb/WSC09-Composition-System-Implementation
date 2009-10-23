/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-29
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.GCDTest.java
 * Last modification: 2007-09-29
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

import java.io.Serializable;

import org.sigoa.refimpl.utils.testSeries.successFilters.FirstObjectiveZeroFilter;

import test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries;

/**
 * Summarize the gcd data
 * 
 * @author Thomas Weise
 */
public class GCDTest {

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  @SuppressWarnings("unchecked")
  public static void main(final String[] args) {
    FirstObjectiveZeroFilter x;
    Serializable d;

    d = new org.dgpf.rbgp.base.RBGPProgram(new org.dgpf.rbgp.base.Rule[] {
        new org.dgpf.rbgp.base.Rule(org.dgpf.rbgp.base.EComparison.TRUE,
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getZeroSymbol())),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getOneSymbol())), org.dgpf.rbgp.base.EComparison.TRUE,
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getOneSymbol())),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getVariable(0))), false,
            test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.ACTION_SET
                .getAddAction(),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getVariable(1))),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getOneSymbol()))),
        new org.dgpf.rbgp.base.Rule(
            org.dgpf.rbgp.base.EComparison.NOT_EQUAL,
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getZeroSymbol())),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getStartSymbol())), org.dgpf.rbgp.base.EComparison.FALSE,
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getVariable(0))),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getZeroSymbol())), false,
            test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.ACTION_SET
                .getMulAction(),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getVariable(1))),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getStartSymbol()))),
        new org.dgpf.rbgp.base.Rule(org.dgpf.rbgp.base.EComparison.FALSE,
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getStartSymbol())),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getStartSymbol())), org.dgpf.rbgp.base.EComparison.FALSE,
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getOneSymbol())),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getOneSymbol())), false,
            test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.ACTION_SET
                .getSetAction(),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getZeroSymbol())),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getVariable(2)))),
        new org.dgpf.rbgp.base.Rule(org.dgpf.rbgp.base.EComparison.FALSE,
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getZeroSymbol())),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getZeroSymbol())), org.dgpf.rbgp.base.EComparison.FALSE,
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getZeroSymbol())),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getZeroSymbol())), false,
            test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.ACTION_SET
                .getAddAction(),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getZeroSymbol())),
            ((test.org.dgpf.gcd.rbgp.GCDRBGPTestSeries.SYMBOL_SET
                .getZeroSymbol()))) });

    new GCDRBGPTestSeries("E:\\temp\\"); //$NON-NLS-1$
    x = GCDRBGPTestSeries.s_filter;

    System.out.println(x.isOverfitted(d));
  }
}

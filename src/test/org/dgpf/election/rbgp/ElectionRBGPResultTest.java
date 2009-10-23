/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-23
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.rbgp.ElectionRBGPTestSeries.java
 * Last modification: 2007-09-23
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

package test.org.dgpf.election.rbgp;

import java.io.Serializable;

import org.dgpf.rbgp.base.EComparison;
import org.dgpf.rbgp.net.DefaultNetActionSet;
import org.dgpf.rbgp.net.DefaultNetSymbolSet;
import org.sigoa.refimpl.utils.testSeries.ISuccessFilter;

/**
 * The test series for the election rbgp algorithm.
 * 
 * @author Thomas Weise
 */
public class ElectionRBGPResultTest {
  /**
   * the ss
   */
  private static final DefaultNetSymbolSet SS = test.org.dgpf.election.rbgp.ElectionRBGPTestSeries.SYMBOL_SET;

  /**
   * as
   */
  private static final DefaultNetActionSet AS = test.org.dgpf.election.rbgp.ElectionRBGPTestSeries.ACTION_SET;

  /** */
  @SuppressWarnings("unchecked")
  private static final Serializable S = new org.dgpf.rbgp.base.RBGPProgram(
      new org.dgpf.rbgp.base.Rule[] {
          new org.dgpf.rbgp.base.Rule(EComparison.LESS, ((SS
              .getVariable(0))), ((SS.getReceiveSymbol(0))),
              EComparison.TRUE, ((SS.getReceiveSymbol(0))), ((SS
                  .getVariable(0))), true, AS.getSetAction(), ((SS
                  .getVariable(0))), ((SS.getReceiveSymbol(0)))),
          new org.dgpf.rbgp.base.Rule(EComparison.LESS, ((SS
              .getVariable(0))), ((SS.getReceiveSymbol(0))),
              EComparison.TRUE, ((SS.getReceiveSymbol(0))), ((SS
                  .getVariable(0))), true, AS.getSetAction(), ((SS
                  .getIdSymbol())), ((SS.getReceiveSymbol(0)))),
          new org.dgpf.rbgp.base.Rule(EComparison.LESS, ((SS
              .getVariable(0))), ((SS.getReceiveSymbol(0))),
              EComparison.TRUE, ((SS.getReceiveSymbol(0))), ((SS
                  .getVariable(0))), true, AS.getSetAction(), ((SS
                  .getSendSymbol(0))), ((SS.getReceiveSymbol(0)))),
          new org.dgpf.rbgp.base.Rule(EComparison.TRUE, ((SS
              .getVariable(0))), ((SS.getReceiveSymbol(0))),
              EComparison.EQUAL, ((SS.getStartSymbol())), ((SS
                  .getOneSymbol())), true, AS.getSetAction(), ((SS
                  .getVariable(0))), ((SS.getIdSymbol()))),
          new org.dgpf.rbgp.base.Rule(EComparison.TRUE, ((SS
              .getVariable(0))), ((SS.getReceiveSymbol(0))),
              EComparison.EQUAL, ((SS.getStartSymbol())), ((SS
                  .getOneSymbol())), true, AS.getSetAction(), ((SS
                  .getSendSymbol(0))), ((SS.getIdSymbol()))),
          new org.dgpf.rbgp.base.Rule(EComparison.EQUAL, ((SS
              .getVariable(0))), ((SS.getZeroSymbol())), EComparison.LESS,
              ((SS.getVariable(0))), ((SS.getReceiveSymbol(0))), false, AS
                  .getSendAction(), ((SS.getVariable(1))), ((SS
                  .getIdSymbol()))), });

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static final void main(String[] args) {
    ISuccessFilter<Serializable> f;

    new ElectionRBGPTestSeries("e:\\temp\\q").abort(); //$NON-NLS-1$

    f = ElectionRBGPTestSeries.s_filter;
    System.out.println(S);
    System.out.println();
    System.out.println(f.isOverfitted(S));
  }
}

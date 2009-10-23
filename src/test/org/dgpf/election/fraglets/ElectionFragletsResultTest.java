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

package test.org.dgpf.election.fraglets;

import java.io.Serializable;

import org.sigoa.refimpl.utils.testSeries.ISuccessFilter;

/**
 * The test series for the election rbgp algorithm.
 * 
 * @author Thomas Weise
 */
public class ElectionFragletsResultTest {

  /** */
  @SuppressWarnings("unchecked")
  private static final Serializable S = new org.dgpf.fraglets.base.FragletProgram(
      test.org.dgpf.election.fraglets.ElectionInstructionSet.DEFAULT_ELECTION_INSTRUCTION_SET,
      new int[][] {{158912594, -1316721491, 2147128138, 913657767, 1655274428, 44827049, -866774087, -944967801}, {-965052981, -651223324, -224874349, -974544486, 249035268, -1759906707, 976760373, -1773161956, -53275376, -414530268, 725207939, 1779745191}, {-88623251, -1702931637, 780020158}, {1788165604, -1201003790, 348807712, -2103607473, 1277848293}, {-88623251, -1702931637, 780020158}, {828163188, 837114519, -1574181156, 284558442, 223938134}, {-1073820844, 804337132, 1045382473, -1841674097, -1395781155, -1660435734, -1076420172, 1891345747, -1375427090, -1660499209, 1314185199, -1042544890, -19355496}});

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static final void main(String[] args) {
    ISuccessFilter<Serializable> f;

    new ElectionFragletTestSeries("e:\\temp\\q").abort(); //$NON-NLS-1$

    f = ElectionFragletTestSeries.s_filter;
    System.out.println(S);
    System.out.println();
    System.out.println(f.isOverfitted(S));
  }
}

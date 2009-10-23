/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-18-12
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.fraglets.AlgorithmTest.java
 * Last modification: 2007-18-12
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

import org.dgpf.fraglets.base.FragletProgram;
import org.sigoa.refimpl.utils.testSeries.IndividualEvaluator;
import org.sigoa.spec.go.IIndividual;

/**
 * A test class for fraglet-based election algorithms.
 * 
 * @author Thomas Weise
 */
public class FragletAlgorithmTest {
  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    FragletProgram p;
    IIndividual<?, ?> ind;
    IndividualEvaluator i;

    p =new org.dgpf.fraglets.base.FragletProgram(test.org.dgpf.election.fraglets.ElectionInstructionSet.DEFAULT_ELECTION_INSTRUCTION_SET, new int[][] {{-599840214, 2121909804, -728499846, -356070694, 1850456022, -122443441, 2115217489, -122516369, -413104964, -1490686850, 963934696, -42590896, -377593659, -999586537}, {-1545446864, 1529584579, -321717894, -699833276, 1916003674, 2044326874, -1755733528, 1609422905, -1466411777, 211502608}, {-420620453, 734672383, 1825059333, -387398937, -62570036, -1634915476, -2133494447, -1269943248, 1006543422, 629865709, 1560283677, 1031828493, -1221836529, 496366469}, {-292927982, 15127392, 475365692, -943476215, -1376008379, 1486602870, -1030844013, 1485893522, 1997071366, 1687691710, 1776006504, -1210813579, -1308271942, 990509208, 2083089883, 282499675, 1770843988, 1498898407, -1847714737, 1776671796}, {-1901030917, -1691007208, 1284425465, -1461656865, 2003511521, -1687666962, -1016676306, -430421037, -89217788}, {-292927982, 15127392, 475365692, -943476215, -1376008379, 1486602870, -1030844013, 1485893522, 1997071366, 1687691710, 1776006504, -1210813579, -1308271942, 990509208, 2083089883, 282499675, 1770843988, 1498898407, -1847714737, 1776671796}, {2108799905, -2077386124, -44303905, 668557068, 736086089, 937069322, 186689014, 1811489190}, {-529562441}, {-481487096, -1503716314, -572255717, -675141866, 398286753, 690337121, 389324417}, {256884136, 1587122591, 1293396604, 1162804639, 412485988, 1855736219, -118537200, -857307658, 273105219, 1590111078}, {-599840214, 2121909804, -728499846, -356070694, 1850456022, -122443441, 2115217489, -122516369, -413104964, -1490686850, 963934696, -42590896, -999586537, -377593659}, {120174721, 1084613044, 1376586237, 440177643, 599134830, 1803426298, 646911392}, {-1408767849}, {439127569, 170319973, -2035216820, 1443399788, -1794013059, 1621397510, -1833746278, 402045183, 922085475, 1616386665}, {-599840214, 2121909804, -728499846, -356070694, 1850456023, -122443441, 2115217489, -122516369, -413104964, -1490686850, -42590896, -377593659, -999586537}})
 
      //new org.dgpf.fraglets.base.FragletProgram(test.org.dgpf.election.fraglets.ElectionInstructionSet.DEFAULT_ELECTION_INSTRUCTION_SET, new int[][] {{1129786154, -540333862, -543632477, 303013035, -2002318120, -1264240290, -1877199905, 2107699948, -602984673, -677458100}, {-599840214, 2121909804, -728499846, -356070694, 1850456022, -122443441, 2115217489, -122516369, -413104964, -1490686850, 963934696, -42590896, -377593659, -999586537}, {1879302851, 393461889}, {-124019808, -508647286, -1105515747, 1332630892, -1650969343, 291668390, 577290271, 1078709905, -1642970941, -1717380888}, {-2009347057, -1248868684}, {-1281144543, 135603916, 507444688, -1257102812, 974821259, -1851538058, -1449228835}, {64334467}, {-1792127917, -1154822764, -1399801271}, {238509398, -1044195445, 1836915059, -235857488, -338361290, 1611909691, 1902153540, -55334979}})
;
    i = new ElectionFragletTestSeries("E:\\temp\\").createIndividualEvaluator(); //$NON-NLS-1$

    ind = i.evaluate(null, p);
    System.out.println(ind);
    
    i.release();
  }
}

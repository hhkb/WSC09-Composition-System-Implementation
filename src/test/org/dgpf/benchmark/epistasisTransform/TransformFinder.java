/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-26
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.benchmark.epistasisTransform.TransformFinder.java
 * Last modification: 2007-11-26
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

package test.org.dgpf.benchmark.epistasisTransform;

import org.sfc.text.TextUtils;
import org.sigoa.refimpl.go.objectives.ObjectiveState;

/**
 * Mapping builder
 * 
 * @author Thomas Weise
 */
public class TransformFinder {

  /** */
  private static final int N = TransformationEvolver.N;

  /** */
  private static final int K = TransformationEvolver.K;

  /**
   * the main routine
   * 
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {
    int[] l;
    int i, j, k, r;
    boolean b;

    l = new int[N];

    
    for (i = 0; i < (N>>1); i++) {
      r = 0;
      
      for (j = 0; j < K; j++) {
        b = false;
        
        for (k = (K - 2); k >= 0; k--) {          
          b ^= ((i & (1 << ((j + k) % K))) != 0);
        }
        if (b)
          r |= (1 << j);
      }
      l[i] = r;
    }

    
    for(i = (N>>1); i <N;i++){
      l[i] = N-1-l[i-(N>>1)];
    }
    
    System.out.println(TextUtils.toString(l));

    ObjectiveState s;

    s = new ObjectiveState();
    new TObjectiveFunction().endEvaluation(l, s, null, null);
    System.out.println(s.getObjectiveValue());

    k = 0;
    for (i = l.length - 1; i > 0; i--) {
      for (j = (i - 1); j >= 0; j--) {
        if (l[i] == l[j])
          k++;
      }
    }

    System.out.println(k);
  }

}

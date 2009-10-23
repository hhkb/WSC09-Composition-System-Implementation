/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-29
 * Creator          : Thomas Weise
 * Original Filename: test.Dummy.java
 * Last modification: 2006-12-29
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

package test.org.dgpf.benchmark.tunableModel;

import org.dgpf.benchmark.TunableModel;
import org.sfc.text.TextUtils;
import org.sigoa.refimpl.stoch.Randomizer;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * the dummy test class
 * 
 * @author Stefan Niemczyk
 */
public class NeutralityBits {

  /**
   * the main routine
   * 
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {
    byte[] b1, b2;
    final int maxLen, maxBits;
    int len, neutr, i, j;
    final IRandomizer rand;
    
    maxLen = 10000;
    maxBits = (maxLen << 3);
    b1 = new byte[maxLen];
    b2 = new byte[maxLen];
    rand = new Randomizer();

    
    for( i = 0; i<1000000; ++i )
    {
      do {
        len = rand.nextInt(maxBits);
      } while (len <= 0);

      do {
        neutr = rand.nextInt(maxBits);
      } while (neutr <= 0);
      
      rand.nextBytes(b1);
      TunableModel.neutrality(b1, b2, len, neutr);
      TunableModel.neutrality(b1, b1, len, neutr);
      
      j = (len / neutr) >> 3;
      if( ((len / neutr) & 7) != 0)
        ++j;
      
      --j;
      
      for( ; j >= 0; --j )
      {
        if( b1[j] != b2[j] )
        {
          System.out.println( "error" ); //$NON-NLS-1$
          System.out.println( TextUtils.toString(b1));
          System.out.println( TextUtils.toString(b2));
        }
      }
    }
  }
}

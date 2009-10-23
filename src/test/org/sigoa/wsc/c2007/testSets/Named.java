/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-05-02 09:30:54
 * Original Filename: test.org.sigoa.wsc.c2007.testSets.data.Named.java
 * Version          : 1.0.3
 * Last modification: 2006-05-08
 *                by: Thomas Weise
 * 
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to the Free Software
 *                    Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *                    MA 02111-1307, USA or download the license under
 *                    http://www.gnu.org/copyleft/lesser.html.
 *                    
 * Warranty         : This software is provided "as is" without any
 *                    warranty; without even the implied warranty of
 *                    merchantability or fitness for a particular purpose.
 *                    See the Gnu Lesser General Public License for more
 *                    details.
 */

package test.org.sigoa.wsc.c2007.testSets;

import java.util.Random;

/**
 * This class ensures proper naming. It is the base of all parts of our
 * challenge.
 * 
 * @author Thomas Weise
 */
class Named {
  /**
   * The naming counter.
   */
  private static int s_counter = 0;

  /**
   * The internal randomize.
   */
  private static final Random R = new Random();

  /**
   * The name of the instance.
   */
  final String m_name;

  /**
   * Create a new named object.
   * 
   * @param prefix
   *          The prefix for the name to use.
   */
  Named(final String prefix) {
    super();

    StringBuilder sb;
    synchronized (Named.class) {
      s_counter += (R.nextInt(200) + 1);
      sb = new StringBuilder(Integer.toHexString(s_counter));
    }
    while (sb.length() < 10)
      sb.insert(0, '0');

    sb.insert(0, prefix);
    this.m_name = sb.toString();
  }
}

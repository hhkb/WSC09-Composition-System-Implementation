/*
 * Copyright (c) 2006 Thomas Weise
 *
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-01-07 15:26:24
 * Original Filename: examples.Test.java
 * Version          : 2.0.0
 * Last modification: 2006-04-10
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
 *
 * STRONG WARNING   : This file is an experimental test file. Anything you
 *                    perform may result in unexpected, unintend or even
 *                    catastrophical results.
 */

package test.org.sigoa.wsc.c2009.generator.test;

import test.org.sigoa.wsc.c2009.generator.Rand;

/**
 * The default skeleton of a test class. This class does nothing, it is
 * just copied when a new test class is needed.
 *
 * @author Thomas Weise
 */
public class IDTester {

  /**
   * The main routine.
   *
   * @param p_args
   *          The comment line arguments.
   */
  public static void main(String[] p_args) {
    int[] ids;
    int i, j, id;

    ids = new int[1024 * 1024*2];
    for (i = 0; i < ids.length; i++) {
      ids[i] = id = Rand.nextId();
//      System.out.println(id);
      for (j = (i - 1); j >= 0; j--) {
        if (ids[j] == id)
          System.out.println("error"); //$NON-NLS-1$
      }
    }

    System.out.println("ok"); //$NON-NLS-1$
  }
}

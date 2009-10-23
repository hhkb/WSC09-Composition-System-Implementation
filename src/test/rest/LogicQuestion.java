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

package test.rest;

/**
 * says to S and P: “I have chosen two integers x, y such that 1 < x < y
 * and x+y <= 100. In a moment, I will inform S only of S = x + y and P
 * only of p = xy.” After the announcements are made, the following
 * conversation takes place.<br/>
 * 1. P says: “I do not know the pair”. <br/>
 * 2. S says: “I knew you didn’t”. <br/>
 * 3. P says: “I now know the pair”. <br/>
 * 4. S says: “I now know too”.
 * 
 * @author Thomas Weise
 */
public class LogicQuestion {

  /**
   * @param x
   * @param y
   * @return
   */
  static boolean multisum(int x, int y) {
    int i, j;

    for (i = 2; i < 100; i++) {
      for (j = (i + 1); j < 100; j++) {
        if ((i + j) == (x + y)) {
          if (!(multiprod(i, j)))
            return false;
        }
      }
    }

    return true;
  }

  /**
   * @param y
   * @param x
   * @return
   */
  static boolean multiprod(int x, int y) {
    int i, j, k;

    k = 0;
    for (i = 2; i < 100; i++) {
      for (j = (i + 1); j < 100; j++) {
        if (((i * j) == (x * y)) && ((i + j) <= 100)) {
          if ((++k) >= 2)
            return true;
        }
      }
    }

    return false;
  }

  /**
   * @param y
   * @param x
   * @return
   */
  static boolean multiprod2(int x, int y) {
    int i, j, k;

    k = 0;
    for (i = 2; i < 100; i++) {
      for (j = (i + 1); j < 100; j++) {
        if ((i * j) == (x * y)) {
          if (multisum(i, j)) {
            if ((++k) > 2)
              return false;
          }
        }
      }
    }

    return (k == 1);
  }

  /**
   * @param x
   * @param y
   * @return
   */
  static boolean multisum2(int x, int y) {
    int i, j, k;

    k = 0;
    for (i = 2; i < 100; i++) {
      for (j = (i + 1); j < 100; j++) {
        if ((i + j) == (x + y)) {
          if (multiprod2(i, j)) {
            if ((++k) >= 2)
              return false;
          }
        }
      }
    }

    return (k == 1);
  }

  /**
   * The main routine.
   * 
   * @param p_args
   *          The comment line arguments.
   */
  public static void main(String[] p_args) {
    int i, j;

    for (i = 2; i < 100; i++) {
      for (j = (i + 1); j < 100; j++) {
        if ((i + j) <= 100) {
          if (multiprod(i, j)) {
            if (multisum(i, j)) {
              if (multiprod2(i, j)) {
                if (multisum2(i, j)) {
                  System.out.println(i + " " + j); //$NON-NLS-1$
                }
              }
            }
          }
        }
      }
    }
  }

}

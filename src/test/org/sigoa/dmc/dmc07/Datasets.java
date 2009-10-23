/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-09
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.dmc.Datasets.java
 * Last modification: 2007-05-09
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

package test.org.sigoa.dmc.dmc07;

import java.io.Reader;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.io.IO;
import org.sfc.utils.ErrorUtils;

/**
 * The datasets
 *
 * @author Thomas Weise
 */
public final class Datasets {

  /**
   * the data
   */
  public static final byte[][] DATA;

  /**
   * the classes
   */
  public static final EClasses[] CLASSES;

  static {
    List<byte[]> l;
    List<EClasses> l2;
    char[] ch;
    Reader r;
    byte[] b;
    int i;

    ch = new char[35 + 2];
    l = CollectionUtils.createList();
    l2 = CollectionUtils.createList();
    r = IO.getReader(IO.getResourceInputStream("all.csv")); //$NON-NLS-1$

    if (r != null) {

      try {

        while ((i = (r.read(ch))) == ch.length) {
          b = new byte[ClassifierSystem.DATASET_LEN];
          for (i = 0; i < ClassifierSystem.DATASET_LEN; i++) {
            b[i] = (byte) (ch[2 * i] - '0');
          }

          l.add(b);

          switch (ch[ClassifierSystem.DATASET_LEN * 2]) {
          case 'N': {
            l2.add(EClasses.N);
            break;
          }
          case 'A': {
            l2.add(EClasses.A);
            break;
          }
          default:
            l2.add(EClasses.B);
          }
        }
      } catch (Throwable t) {
        ErrorUtils.onError(t);
      } finally {
        try {
          r.close();
        } catch (Throwable t) {
          ErrorUtils.onError(t);
        }
      }

    }

    DATA = l.toArray(new byte[l.size()][]);
    CLASSES = l2.toArray(new EClasses[l.size()]);
  }

}

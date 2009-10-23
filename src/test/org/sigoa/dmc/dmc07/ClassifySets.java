/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-09
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.dmc.ClassifySets.java
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

import java.io.BufferedReader;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.io.IO;
import org.sfc.io.TextWriter;
import org.sfc.utils.ErrorUtils;

/**
 * The datasets
 *
 * @author Thomas Weise
 */
public final class ClassifySets {

  /**
   * the data
   */
  public static final byte[][] DATA;

  /**
   * the classes
   */
  public static final String[] CLASSES;

  static {
    List<byte[]> l;
    List<String> l2;
    String s;
    String[] ll;
    byte[] b;
    int i;
    BufferedReader r;

    l = CollectionUtils.createList();
    l2 = CollectionUtils.createList();
    try {
      r = new BufferedReader(IO.getReader(IO.getResourceInputStream( // "refined.csv"));//$xNON-NLS-1$
          "classify.csv"))); //$NON-NLS-1$

      while ((s = r.readLine()) != null) {
        b = new byte[ClassifierSystem.DATASET_LEN];
        ll = s.split(";"); //$NON-NLS-1$
        l2.add(ll[0]);
        for (i = 0; i < b.length; i++) {
          b[i] = Byte.parseByte(ll[i + 1]);
        }
        l.add(b);
      }

      r.close();
    } catch (Throwable t) {
      ErrorUtils.onError(t);
    }

    DATA = l.toArray(new byte[l.size()][]);
    CLASSES = l2.toArray(new String[l.size()]);
  }

  /**
   * @param c
   *          the classifier
   * @param w
   *          the textwriter
   */
  public static void classify(final ClassifierSystem c, final TextWriter w) {
    int i;
    for (i = 0; i < DATA.length; i++) {
      w.write(CLASSES[i]);
      w.write(',');
      w.write(c.classify(DATA[i]).name());
      w.newLine();
    }
  }

  /**
   * the main routine
   *
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {
    TextWriter w;
    ClassifierSystem c;

    c = new Classifier2();
//      ClassifierEmbryogeny.CLASSIFIER_EMBRYOGENY.hatch(new byte[] { -4,
//        124, 87, 29, -124, 40, -2, 27, -34, -44, 55, 31, -123, 3, -84,
//        -26, 127, 122, 112, -64, -67, -46, 108, -18, 127, -88, -26, 75,
//        -46, 113, -121, -45, 1, 30, 48, 90, 56, 15, 65, -113, -33, -88,
//        -85, 121, 56, 122, -120, 9, -68, -86, 34, -2, 1, -28, -24, 4, -46,
//        77, 44, 117, -18, 127, -88, -26, -53, -106, 127, 100, 116, 32, 5,
//        96, -104, 89, -3, 3, -56, -47, 105, -53, -1, 57, 122, -112, 2, 16,
//        12, 44, 42, -10, -45, 6, -62, 15, -119, -12, 78, -8, 9, 47, 63,
//        -56, 7, 81, -23, 11, -7, -84, -113, -119, 27, -29, -35, -46, 125,
//        68, 5, 0, 96, 0, 80, 14, -64, 56, 44, -121, 13, -6, 9, 51, 123, 1,
//        -9, 39, -94, -5, 35, 62, 64, 111, -75, 61, 63, 121, -70, 124, 79,
//        52, 33, -123, 83, -37, 103, -24, -83, -67, 99, -128, -34, 45, 7,
//        -81, -24, -2, -120, 15, 48, -12, -42, -34, 49, 64, -17, -106,
//        -125, 87, 112, 32, 12, 54, -62, -17, 53, -105, 24, 124, 95, -29,
//        -21, -89, -19, 39, -77, 97, -60, 0, 48, -58, -85, -22, -122, 2,
//        110, 53, 71, 67, -126, -80, 120, 113, 14, -31, 23 });

    w = new TextWriter(IO.getWriter("E:\\classes.txt",//$NON-NLS-1$
        "ASCII")); //$NON-NLS-1$

    classify(c, w);

    w.flush();
    w.release();
  }

}

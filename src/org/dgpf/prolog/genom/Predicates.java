/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-02-20
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.prolog.genom.Predicates.java
 * Last modification: 2008-02-20
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

package org.dgpf.prolog.genom;

import org.sfc.collections.lists.SimpleList;
import org.sfc.io.csv.CSVReader;
import org.sfc.utils.ErrorUtils;

/**
 * the internal predicate list
 * 
 * @author Thomas Weise
 */
final class Predicates {
  /**
   * the initialization file for the predicates
   */
  private static final String INI_FILE = "predicates.txt"; //$NON-NLS-1$

  /**
   * the available predicates
   */
  static String[][] PREDICATES;

  /**
   * Load the predicates
   * 
   * @param src
   *          the source file
   * @return the predicates
   */
  private static final String[][] loadPredicates(final Object src) {
    String[][] s;
    int i, j, v;
    SimpleList<SimpleList<String>> l;
    SimpleList<String> x;

    s = CSVReader.readCSVFile(src, true);
    if ((s != null) && ((i = s.length) > 0)) {
      l = new SimpleList<SimpleList<String>>(i);

      for (--i; i >= 0; i--) {
        v = 0;
        if (s[i].length > 1) {
          try {
            v = Integer.parseInt(s[i][1]);
          } catch (Throwable tt) {
            ErrorUtils.onError(tt);
          }

          j = l.m_count;
          if (v > j) {
            for (; j < v; j++) {
              l.add(new SimpleList<String>());
            }
          }

          l.get(v).add(s[i][0]);
        }
      }

      i = l.m_count;
      s = new String[i][];

      for (--i; i >= 0; i--) {
        x = l.get(i);
        System.arraycopy(x.m_data, 0, s[i] = new String[x.m_count], 0,
            x.m_count);
      }

      return s;
    }

    return new String[0][];
  }

  static {
    PREDICATES = loadPredicates(INI_FILE);
  }

}

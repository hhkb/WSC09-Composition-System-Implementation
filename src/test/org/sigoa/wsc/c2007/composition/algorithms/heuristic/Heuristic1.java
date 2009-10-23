/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-07-20
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.composition.algorithms.heuristic.Heuristic1.java
 * Last modification: 2007-07-20
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

package test.org.sigoa.wsc.c2007.composition.algorithms.heuristic;

import java.util.Comparator;

/**
 * The first heuristic function
 *
 * @author Thomas Weise
 */
public class Heuristic1 implements Comparator<Composition> {

  /**
   * the globally shared first heuristic
   */
  public static final Comparator<Composition> H1 = new Heuristic1();

  /**
   * Compare two compositions
   *
   * @param o1
   *          the first one
   * @param o2
   *          the second one
   * @return the comparison result (> 0 if o1 is better than o2)
   */
  public final int compare(final Composition o1, final Composition o2) {
    int ul1c, ul2c, el1, el2;

    ul1c = o1.m_unknown.m_count;
    ul2c = o2.m_unknown.m_count;
    if (ul1c <= 0) {
      if (ul2c <= 0) {
        return (o2.m_services.m_count - o1.m_services.m_count);
      }
      return 1;
    }
    if (ul2c <= 0)
      return -1;

    el1 = o1.m_eliminated;
    el2 = o2.m_eliminated;

    if (el1 > el2)
      return 1;
    else if (el2 > el1)
      return -1;

    if (ul1c < ul2c)
      return 1;
    else if (ul2c < ul1c)
      return -1;

    ul1c = o1.m_services.m_count;
    ul2c = o2.m_services.m_count;
    if (ul1c < ul2c)
      return 1;
    else if (ul2c < ul1c)
      return -1;

    return (o1.m_known.m_count - o2.m_known.m_count);// 0;

    // Double.compare(((double) el1) / ((double) (ul1c + 1)),
    // ((double) el2) / ((double) (ul2c + 1)));
  }

}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-06-11
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.composition.algorithms.heuristic.HeuristicDFS3.java
 * Last modification: 2007-06-11
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

import java.util.Arrays;

import test.org.sigoa.wsc.c2007.challenge.Challenge;
import test.org.sigoa.wsc.c2007.challenge.ServiceList2;
import test.org.sigoa.wsc.c2007.challenge.Solution;
import test.org.sigoa.wsc.c2007.composition.ICompositionAlgorithm;
import test.org.sigoa.wsc.c2007.composition.ICompositionAlgorithmFactory;
import test.org.sigoa.wsc.c2007.kb.ConceptList;
import test.org.sigoa.wsc.c2007.kb.Service;
import test.org.sigoa.wsc.c2007.kb.ServiceList;


/**
 * The heuristic depth first search
 *
 * @author Thomas Weise
 */
public class HeuristicDFS3 implements ICompositionAlgorithm {

  /**
   * The globally shared factory
   */
  public static final ICompositionAlgorithmFactory HEURISTIC_DFS_FACTORY3 = new ICompositionAlgorithmFactory() {
    public ICompositionAlgorithm createCompositionAlgorithm() {
      return new HeuristicDFS3();
    }
  };

  /**
   * Find service compositions.
   *
   * @param c
   *          the challenge
   */
  public final void compose(final Challenge c) {
    Composition cc;
    ServiceList2 l;

    cc = HeuristicDFS3.compose(c.getIn(), c.getOut(), c);
    if ((cc == null) || (c.solved()))
      return;
    l = Composition.complete(cc, c.getIn(), c.getOut(), c);
    if ((l == null) || (c.solved()))
      return;

    new Solution(c, l);
  }

  /**
   * Find the shortest possible composition suitable to solve the given
   * task guaranteed.
   *
   * @param known
   *          The list of known concepts.
   * @param wanted
   *          The list of wanted concepts.
   * @param clg
   *          the challenge
   * @return A composition solving that task.
   */
  public static final Composition compose(final ConceptList known,
      final ConceptList wanted, final Challenge clg) {
    Composition c;
    CompositionList cl;
    ServiceList sl;
    Service[] sll;
    Composition[] l;
    int i, j, k, maxLen;
    Service s;
    Composition perfect;

    i = wanted.m_count;
    sl = new ServiceList(i << 3);

    for (--i; i >= 0; i--) {
      wanted.m_data[i].getPromising(sl);
    }

    if (clg.solved())
      return null;

    i = sl.m_count;
    cl = new CompositionList(i);

    l = cl.m_data;
    j = 0;
    sll = sl.m_data;
    main: for (--i; i >= 0; i--) {
      s = sll[i];
      for (k = (j - 1); k >= 0; k--) {
        if (l[k].contains(s))
          continue main;
      }

      l[j++] = new Composition(s, known, wanted);
    }

    cl.m_count = j;
    maxLen = 50;

    perfect = null;
    main: for (;;) {
      if (clg.solved())
        return null;
      if (cl.m_count <= 0)
        break;
      Arrays.sort(cl.m_data, 0, cl.m_count);
      for (;;) {

        if ((--cl.m_count) <= 0)
          break main;

        c = cl.m_data[cl.m_count];
        j = c.m_services.m_count;

        if (c.isPerfect()) {
          if (maxLen >= j) {
            perfect = c;
            if ((maxLen = (j - 1)) <= 0)
              break main;
          }
          continue;
        } else if (j >= maxLen)
          continue;

        sl = c.getPromising();

        i = sl.m_count;
        if (i <= 0)
          continue;

        sll = sl.m_data;
        for (--i; i >= 0; i--) {
          s = sll[i];
          if (c.contains(s))
            continue;
          cl.add(new Composition(s, c));
        }

        break;
      }
    }

    return perfect;
  }
}

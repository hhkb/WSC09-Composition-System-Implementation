/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-06-11
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.composition.algorithms.heuristic.HeuristicDFS.java
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
public class HeuristicDFS implements ICompositionAlgorithm {

  /**
   * The globally shared factory
   */
  public static final ICompositionAlgorithmFactory HEURISTIC_DFS_FACTORY = new ICompositionAlgorithmFactory() {
    public ICompositionAlgorithm createCompositionAlgorithm() {
      return new HeuristicDFS();
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

    cc = HeuristicDFS.compose(c.getIn(), c.getOut(), c);
    if ((cc == null) || (c.solved()))
      return;
    l = Composition.complete(cc, c.getIn(), c.getOut(), c);
    if ((l == null) || (c.solved()))
      return;

    new Solution(c, l);
  }

  /**
   * Find a composition suitable to solve the given task.
   *
   * @param known
   *          The list of known concepts.
   * @param wanted
   *          The list of wanted concepts.
   * @param clg
   *          the challenge
   * @return A composition solving that task.
   */
  private static final Composition compose(final ConceptList known,
      final ConceptList wanted, final Challenge clg) {
    Composition c;
    ServiceList sl;
    int i, j, k;
    Composition[] l;
    Service s;

    i = wanted.m_count;
    sl = new ServiceList(i << 3);

    for (--i; i >= 0; i--) {
      wanted.m_data[i].getPromising(sl);
    }

    if (clg.solved())
      return null;

    i = sl.m_count;
    l = new Composition[i];
    j = 0;
    main: for (--i; i >= 0; i--) {
      s = sl.m_data[i];
      for (k = (j - 1); k >= 0; k--) {
        if (l[k].contains(s))
          continue main;
      }

      l[j++] = new Composition(s, known, wanted);
    }

    if (clg.solved())
      return null;

    Arrays.sort(l, 0, j, Heuristic1.H1);
    for (--j; j >= 0; j--) {
      c = l[j];
      if (c.isPerfect())
        return c;
      c = compose(l[j], clg, 50);
      if (c != null)
        return c;
    }

    return null;
  }

  /**
   * Find a composition suitable to solve the given task.
   *
   * @param prev
   *          The previous composition.
   * @param clg
   *          the challenge
   * @param md
   *          the remaining depth
   * @return A composition solving that task.
   */
  private static final Composition compose(final Composition prev,
      final Challenge clg, final int md) {
    Composition c;
    ServiceList sl;
    int i, j, k;
    Composition[] l;
    Service s;

    if (clg.solved())
      return null;

    sl = prev.getPromising();

    i = sl.m_count;
    if (i <= 0)
      return null;

    l = new Composition[i];
    j = 0;
    main: for (--i; i >= 0; i--) {
      s = sl.m_data[i];
      for (k = (j - 1); k >= 0; k--) {
        if (l[k].contains(s))
          continue main;
      }

      l[j++] = new Composition(s, prev);
    }

    if (clg.solved())
      return null;

    k = (md - 1);
    Arrays.sort(l, 0, j, Heuristic1.H1);
    for (--j; j >= 0; j--) {
      c = l[j];
      if (c.isPerfect())
        return c;
      if (k > 0) {
        c = compose(l[j], clg, k);
        if (c != null)
          return c;
      }
    }

    return null;
  }

}

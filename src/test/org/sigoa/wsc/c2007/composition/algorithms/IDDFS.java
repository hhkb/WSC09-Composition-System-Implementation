/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-11
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.composition.algorithms.IDDFS.java
 * Last modification: 2007-05-11
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

package test.org.sigoa.wsc.c2007.composition.algorithms;

import test.org.sigoa.wsc.c2007.challenge.Challenge;
import test.org.sigoa.wsc.c2007.challenge.ServiceList2;
import test.org.sigoa.wsc.c2007.challenge.Solution;
import test.org.sigoa.wsc.c2007.composition.Composer;
import test.org.sigoa.wsc.c2007.composition.ICompositionAlgorithm;
import test.org.sigoa.wsc.c2007.composition.ICompositionAlgorithmFactory;
import test.org.sigoa.wsc.c2007.kb.Concept;
import test.org.sigoa.wsc.c2007.kb.ConceptList;
import test.org.sigoa.wsc.c2007.kb.Service;
import test.org.sigoa.wsc.c2007.kb.ServiceList;

/**
 * The native IDDFS algorithm
 *
 * @author Thomas Weise
 */
public class IDDFS implements ICompositionAlgorithm {

  /**
   * The globally shared factory
   */
  public static final ICompositionAlgorithmFactory IDDFS_FACTORY = new ICompositionAlgorithmFactory() {
    public ICompositionAlgorithm createCompositionAlgorithm() {
      return new IDDFS();
    }
  };

  /**
   * The maximum depth.
   */
  private int m_max_depth;

  /**
   * The lowest level which has been investigated fully.
   */
  private int m_max_level;

  /**
   * The service lists.
   */
  ServiceList2 m_solution;

  /**
   * Create a new composer.
   */
  public IDDFS() {
    super();
  }

  /**
   * This method will be called whenever a solution was found.
   *
   * @param list
   *          The service list holding a solution.
   */
  private final void onSolution(final ServiceList list) {
    ServiceList2 sl;
    ServiceList[] d;
    Service[] v;
    int i, lc;

    sl = this.m_solution;
    lc = list.m_count;

    if (sl == null) {
      sl = new ServiceList2(lc);
      sl.m_count = lc;
      d = sl.m_data;
      for (i = (lc - 1); i >= 0; i--) {
        d[i] = new ServiceList(-1);
      }
      this.m_solution = sl;
    } else {
      if (lc > sl.m_count)
        return;
      d = sl.m_data;
    }

    v = list.m_data;
    for (i = (lc - 1); i >= 0; i--) {
      d[i].addIfNew(v[i]);
    }
  }

  /**
   * Find service compositions.
   *
   * @param c
   *          the challenge
   */
  public final void compose(final Challenge c) {
    ServiceList sl2;
    ConceptList known, wanted;

    this.m_solution = null;// new ServiceList2(-1);

    this.m_max_depth = 2;
    this.m_max_level = Integer.MAX_VALUE;
    sl2 = new ServiceList(-1);
    known = c.getIn();
    wanted = c.getOut();

    while (c.notSolved()) {
      sl2.clear();
      if (this.doCompose(known, wanted, sl2, 1, c)) {
        if (c.notSolved()) {
          new Solution(c, this.m_solution);
        }
        return;
      }
      this.m_max_depth++;
    }
  }

  /**
   * Find service compositions.
   *
   * @param known
   *          The set of known concepts.
   * @param wanted
   *          The wanted output parameters.
   * @param composition
   *          The current composition.
   * @param depth
   *          The current depth.
   * @param ch
   *          the challenge
   * @return <code>true</code> if and only if a solution was found.
   */
  private final boolean doCompose(final ConceptList known,
      final ConceptList wanted, final ServiceList composition,
      final int depth, final Challenge ch) {
    ServiceList sl;
    int i, j, k;
    Concept c;
    ConceptList wt;
    Service s;
    Concept[] ip;
    boolean b;

    wt = new ConceptList(wanted.m_count);
    sl = new ServiceList(-1);
    b = false;

    service_loop: for (i = (wanted.m_count - 1); i >= 0; i--) {
      if (ch.solved())
        return true;

      c = wanted.m_data[i];
      sl.clear();
      c.getPromising(sl);

      for (j = (sl.m_count - 1); j >= 0; j--) {
        s = sl.m_data[j];
        if (composition.contains(s))
          continue;

        composition.add(s);
        wt.assign(wanted);
        wt.remove(i);

        ip = s.getOutputs();
        for (k = (ip.length - 1); k >= 0; k--) {
          wt.removeSubsumptions(ip[k]);
        }

        ip = s.getInputs();
        for (k = (ip.length - 1); k >= 0; k--) {
          if (known.includesSpecializationOf(ip[k]) < 0) {
            wt.add(ip[k]);
          }
        }

        if (wt.m_count <= 0) {
          this.onSolution(composition);

          // only a single solution wanted?
          if (Composer.SINGLE)
            return true;
          b = true;

          // since all services in one level must be semantically somewhat
          // equal,
          // we only need to evaluate one level fully once. After that, it
          // is
          // sufficient to know if there is a solution or not.
          if (depth >= this.m_max_level) {
            composition.removeLast();
            break service_loop;
          }
        } else {
          if (depth < this.m_max_depth) {
            if (this.doCompose(known, wt, composition, depth + 1, ch)) {
              b = true;
              // only a single solution wanted?
              if (Composer.SINGLE)
                return true;

              // since all services in one level must be semantically
              // somewhat equal,
              // we only need to evaluate one level fully once. After that,
              // it is
              // sufficient to know if there is a solution or not.
              if (depth >= this.m_max_level) {
                composition.removeLast();
                break service_loop;
              }
            }
          }
        }

        composition.removeLast();
      }

    }

    if (b) {
      if (depth < this.m_max_level)
        this.m_max_level = depth;
    }

    return b;
  }

}

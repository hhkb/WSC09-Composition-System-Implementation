/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-06-11
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.composition.Composer.java
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

package test.org.sigoa.wsc.c2007.composition;

import test.org.sigoa.wsc.c2007.challenge.ChallengeList;
import test.org.sigoa.wsc.c2007.composition.algorithms.IDDFS;
import test.org.sigoa.wsc.c2007.composition.algorithms.heuristic.HeuristicDFS;
import test.org.sigoa.wsc.c2007.composition.algorithms.heuristic.HeuristicDFS2;

/**
 * The composer class is used to compose.
 *
 * @author Thomas Weise
 */
public final class Composer {

  /**
   * The set of available composition algorithm factories
   */
  public static final ICompositionAlgorithmFactory[] ALGORITHMS = //
  new ICompositionAlgorithmFactory[] {
      HeuristicDFS2.HEURISTIC_DFS_FACTORY2,//
      HeuristicDFS.HEURISTIC_DFS_FACTORY,//
      IDDFS.IDDFS_FACTORY //
  };

//   /**
//   * The thread cache
//   */
//   private static final WorkerThread[] CACHE = new WorkerThread[Runtime
//   .getRuntime().availableProcessors()];

   /**
     * The thread cache
     */
  private static final WorkerThread[] CACHE = new WorkerThread[Math.max(
      Runtime.getRuntime().availableProcessors(), 2)];

  /**
   * <code>true</code> if always only single configurations need to be
   * found
   */
  public static volatile boolean SINGLE = false;

  static {
    refreshCache();
  }

  /**
   * Refresh the worker cache
   */
  public static final void refreshCache() {
    int i;
    for (i = (CACHE.length - 1); i >= 0; i--) {
      CACHE[i] = new WorkerThread(ALGORITHMS[i % ALGORITHMS.length]
          .createCompositionAlgorithm());
    }
  }

  /**
   * Compose the challenges
   *
   * @param challenges
   *          The list of challenges to compose
   */
  public static final void compose(final ChallengeList challenges) {
    int i;
    for (i = (CACHE.length - 1); i >= 0; i--) {
      CACHE[i].activate(challenges);
      CACHE[i] = null;
    }
  }

}

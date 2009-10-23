/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-09
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.composition.WorkerThread.java
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

package test.org.sigoa.wsc.c2007.composition;

import test.org.sigoa.wsc.c2007.challenge.Challenge;
import test.org.sigoa.wsc.c2007.challenge.ChallengeList;

/**
 * The worker thread that uses an composition algorithm to do the work.
 *
 * @author Thomas Weise
 */
public class WorkerThread extends Thread {
  /**
   * the composition algorithm.
   */
  private final ICompositionAlgorithm m_algo;

  /**
   * the challenge list.
   */
  private ChallengeList m_cl;

  /**
   * Create a new worker thread.
   *
   * @param algorithm
   *          the composition algorithm
   */
  public WorkerThread(final ICompositionAlgorithm algorithm) {
    super();
    this.m_algo = algorithm;
  }

  /**
   * Activate this worker thread
   *
   * @param cl
   *          the challenge list
   */
  public void activate(final ChallengeList cl) {
    this.m_cl = cl;
    this.start();
  }

  /**
   * The work is done here
   */
  @Override
  public void run() {
    ChallengeList cl;
    ICompositionAlgorithm algo;
    Challenge c;

    cl = this.m_cl;
    algo = this.m_algo;

    for (;;) {
      c = cl.getChallenge();
      if (c == null)
        return;
      algo.compose(c);
      // System.out.println(System.currentTimeMillis());
    }
  }
}

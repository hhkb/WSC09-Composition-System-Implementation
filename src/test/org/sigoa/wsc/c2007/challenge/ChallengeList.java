/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-09
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.challenge.ChallengeList.java
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

package test.org.sigoa.wsc.c2007.challenge;

import org.sfc.collections.lists.SimpleList;

/**
 * the challenge list
 *
 * @author Thomas Weise
 */
public final class ChallengeList {

  /**
   * the challenges
   */
  final Challenge[] m_challenges;

  /**
   * the current index.
   */
  // private int m_idx;
  /**
   * <code>true</code> if all work is done.
   */
  private volatile boolean m_done;

  /**
   * Create a new challenge list.
   *
   * @param challenges
   *          the challenges
   */
  public ChallengeList(final SimpleList<Challenge> challenges) {
    super();
    int i;
    Challenge[] c;

    i = challenges.size();
    this.m_challenges = c = new Challenge[i];
    challenges.copyToArray(c, i);
  }

  /**
   * Obtain the number of challenges in this challenge list.
   *
   * @return the number of challenges in this challenge list
   */
  public int size() {
    return this.m_challenges.length;
  }

  /**
   * Obtain the challenge at the specified position
   *
   * @param index
   *          the index of the challenge
   * @return the challenge at the specified position
   */
  public Challenge get(final int index) {
    return this.m_challenges[index];
  }

  // /**
  // * Obtain the next challenge to process.
  // *
  // * @return the next challenge to process or <code>null</code> if all
  // * challenges have been solved
  // */
  // public Challenge getChallenge() {
  // int i, l, c;
  // Challenge[] cc;
  // Challenge d;
  //
  // cc = this.m_challenges;
  // l = cc.length;
  //
  // synchronized (this) {
  // i = this.m_idx;
  // for (c = l; c > 0; c--) {
  // d = cc[i++];
  // if (i >= l)
  // i = 0;
  // if (!(d.m_inProgress)) {
  // d.m_inProgress = true;
  // this.m_idx = i;
  // return d;
  // }
  // }
  //
  // for (c = l; c > 0; c--) {
  // d = cc[i++];
  // if (i >= l)
  // i = 0;
  // if (d.m_solution == null) {
  // d.m_inProgress = true;
  // this.m_idx = i;
  // return d;
  // }
  // }
  //
  // }
  //
  // synchronized (this.m_challenges) {
  // this.m_done = true;
  // this.m_challenges.notifyAll();
  // }
  // return null;
  // }

  /**
   * Obtain the next challenge to process.
   *
   * @return the next challenge to process or <code>null</code> if all
   *         challenges have been solved
   */
  public Challenge getChallenge() {
    int l, c;
    Challenge[] cc;
    Challenge d;

    cc = this.m_challenges;
    l = cc.length;

    synchronized (this) {
      for (c = 0; c < l; c++) {
        d = cc[c++];
        if (d.m_solution == null) {
          d.m_inProgress = true;
          return d;
        }
      }
    }

    synchronized (this.m_challenges) {
      this.m_done = true;
      this.m_challenges.notifyAll();
    }
    return null;
  }

  /**
   * Check whether all challenges are done.
   *
   * @return <code>true</code> if and only if all challenges are done
   */
  public final boolean isDone() {
    return this.m_done;
  }

  /**
   * Wait until all challenges have been reported to be solved.
   */
  public final void waitFor() {
    Challenge[] c;
    c = this.m_challenges;
    for (;;) {
      synchronized (c) {
        if (this.m_done)
          return;
        try {
          c.wait();
        } catch (Throwable tt) {
          tt.printStackTrace();
        }
      }
    }
  }
}

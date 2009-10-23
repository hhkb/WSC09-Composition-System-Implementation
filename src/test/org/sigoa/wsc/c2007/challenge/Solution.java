/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-09
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.challenge.Solution.java
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

import test.org.sigoa.wsc.c2007.kb.ServiceList;

/**
 * The solution class holds a full solution to a challenge.
 *
 * @author Thomas Weise
 */
public class Solution {
  /**
   * the challenge solved by the solution
   */
  final Challenge m_challenge;

  /**
   * the solution
   */
  final ServiceList2 m_sl;

  /**
   * Create a new solution record.
   *
   * @param challenge
   *          the challenge
   * @param services
   *          the services
   */
  public Solution(final Challenge challenge, final ServiceList2 services) {
    super();
    Solution s;

    this.m_challenge = challenge;
    this.m_sl = services;
    synchronized (challenge) {
      s = challenge.m_solution;
      if ((s == null) || (s.m_sl.size() < services.size())) {
        challenge.m_solution = this;
      }
    }

  }

  /**
   * Obtain the service list.
   *
   * @return the service list
   */
  public final ServiceList2 getServiceLists() {
    return this.m_sl;
  }

  /**
   * Obtain the challenge object.
   *
   * @return the challenge
   */
  public final Challenge getChallenge() {
    return this.m_challenge;
  }

  /**
   * Append a given composition to the solution buffer.
   *
   * @param composition
   *          the composition
   * @param dest
   *          the destination
   */
  public static final void appendComposition(
      final ServiceList composition, final ServiceList2 dest) {

    int ldest, lcomp;
    ServiceList sl;
    ldest = dest.size();
    lcomp = composition.size();
    if (lcomp <= 0)
      return;
    if (ldest > lcomp) {
      dest.clear();
      ldest = 0;
    }

    if (ldest <= 0) {
      lcomp--;
      for (/* ldest=0; */; ldest <= lcomp; ldest++) {
        sl = new ServiceList(-1);
        sl.add(composition.get(ldest));
        dest.add(sl);
      }
    } else {
      for (--lcomp; lcomp >= 0; lcomp--) {
        dest.get(lcomp).addIfNew(composition.get(lcomp));
      }
    }

  }
}

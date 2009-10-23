/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-07-20
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.checker.Solution.java
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

package test.org.sigoa.wsc.c2007.checker;

import test.org.sigoa.wsc.c2007.challenge.Challenge;
import test.org.sigoa.wsc.c2007.challenge.ServiceList2;
import test.org.sigoa.wsc.c2007.kb.ServiceList;

/**
 * The solution object
 *
 * @author Thomas Weise
 */
public class Solution {
  /**
   * the challenge this solution object belongs to
   */
  final String m_name;

  /**
   * the challenge
   */
  Challenge m_ch;

  /**
   * the services
   */
  ServiceList2 m_services;

  /**
   * the service spec index
   */
  private int m_idx;

  /**
   * begin a new solution
   *
   * @param name
   *          the solution name
   */
  public Solution(final String name) {
    super();
    this.m_name = name;
    this.m_services = new ServiceList2(-1);
    this.m_idx = -1;
  }

  /**
   * begin a new service spec
   */
  final void beginServiceSpec() {
    if (this.m_idx <= (this.m_services.m_count - 1)) {
      this.m_services.add(new ServiceList(-1));
    }
    this.m_idx++;
  }

  /**
   * end a service spec
   */
  final void endServiceSpec() {
    this.m_idx--;
  }

  /**
   * add a service of the given name
   *
   * @param name
   *          the service name
   */
  final void addService(final String name) {
    if (name != null)
      this.m_services.get(this.m_idx).add(Main.KB.getService(name));
  }

}

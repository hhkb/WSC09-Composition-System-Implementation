/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-27
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.net.protocols.http.EHttpResponse.java
 * Last modification: 2007-02-27
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

package org.sfc.net.protocols.http;

/**
 * The http response.
 *
 * @author Thomas Weise
 */
public enum EHttpResponse {
  /**
   * the ok-response
   */
  OK {
    @Override
    char[] getText() {
      return OK_TXT;
    }
  };

  /**
   * Get the response text
   *
   * @return the response text
   */
  abstract char[] getText();

  /**
   * the response text for "ok"
   */
  static final char[] OK_TXT = "200 OK".toCharArray(); //$NON-NLS-1$
}

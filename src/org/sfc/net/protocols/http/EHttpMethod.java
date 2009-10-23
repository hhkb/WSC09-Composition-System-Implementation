/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.net.protocols.http.EHttpMethod.java
 * Last modification: 2007-02-26
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
 * The http methods
 *
 * @author Thomas Weise
 */
public enum EHttpMethod {
  /** the post method */
  POST,
  /** the put method */
  PUT,
  /** get */
  GET;

  /**
   * Check if the given string represents this protocol
   *
   * @param s
   *          the method string
   * @return <code>true</code> if and only if <code>s</code> represents
   *         this protocol
   */
  public boolean isMethod(final String s) {
    return this.name().equalsIgnoreCase(s);
  }
}

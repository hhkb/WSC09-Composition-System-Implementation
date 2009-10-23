/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-05-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.net.byzantine.ByzantineType.java
 * Last modification: 2008-05-27
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

package org.dgpf.vm.net.byzantine;

/**
 * The different ways a byzantine node may behave
 * 
 * @author Thomas Weise
 */
public final class ByzantineType {

  /**
   * normal behavior
   */
  public static final int NORMAL = 0;

  /**
   * send the same errors to every node
   */
  public static final int SEND_SAME_ERRORS = 1;

  /**
   * send different errors
   */
  public static final int SEND_DIFFERENT_ERRORS = 2;

  /**
   * send seldomly messages
   */
  public static final int SELDOMLY_SEND = 4;

  /**
   * many messages get lost
   */
  public static final int SELDOMLY_RECEIVED = 8;

  /**
   * the static errors
   */
  static final int ERROR_SET = (SELDOMLY_RECEIVED << 1);
}

/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 10:19:28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.events.IErrorEvent.java
 * Version          : 1.0.0
 * Last modification: 2006-11-22
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

package org.sigoa.spec.events;

/**
 * This event is useful to transmit errors that occured somewhere in the
 * system.
 *
 * @author Thomas Weise
 */
public interface IErrorEvent extends IEvent {
  /**
   * Obtain the throwable error that occured somewhere in the system.
   *
   * @return The throwable error that occured somewhere in the system.
   */
  public abstract Throwable getError();
}

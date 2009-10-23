/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-08-28
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.parallel.simulation.IStepable.java
 * Last modification: 2007-08-28
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

package org.sfc.parallel.simulation;

/**
 * The common stepable interface. This interace is common to, for example,
 * simulations, since it allows to perform single steps of some activity.
 * 
 * @author Thomas Weise
 */
public interface IStepable {
  /**
   * Perform a step of the activity, for example a single simulation step
   */
  public abstract void step();
}

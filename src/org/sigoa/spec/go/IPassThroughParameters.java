/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-10
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.reproduction.IPassThroughParameters.java
 * Last modification: 2006-12-10
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

package org.sigoa.spec.go;

import java.io.Serializable;

/**
 * This interface allows us to determine how many individuals will be
 * passed through a pipe level.
 *
 * @author Thomas Weise
 */
public interface IPassThroughParameters extends Serializable {

  /**
   * Set the count of individuals to be passed through this pipe level.
   * This may be more or less than the individuals actually passed to the
   * pipe. This count may be interpreted as an upper or a lower border,
   * regarding the semantics of the context in which this interface is used.
   *
   * @param count
   *          The count of individuals to be written to the ouput pipe
   *          per turn.
   * @throws IllegalArgumentException
   *           if <code>count &lt;= 0</code>
   */
  public abstract void setPassThroughCount(final int count);

  /**
   * Obtain the count of individuals to be passed through this pipe level.
   * This may be more or less than the individuals actually passed to the
   * pipe.
   *
   * @return The count of individuals to be written to the ouput pipe
   *         per turn.
   */
  public abstract int getPassThroughCount();

}

/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-01
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.pipe.IPipeSource.java
 * Last modification: 2006-12-01
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

package org.sigoa.spec.pipe;

import java.io.Serializable;

/**
 * This interface is common to all objects that represent a collection of
 * individuals which might be written to a pipe.
 *
 * @param <G>
 *          The genotype
 * @param <PP>
 *          The phenotype
 * @author Thomas Weise
 */
public interface IPipeSource<G extends Serializable, PP extends Serializable>
    extends Serializable {

  /**
   * Store the contents of this object to a pipe. This method calls
   * <code>eof()</code> of the pipe.
   *
   * @param pipe
   *          The pipe to write to.
   * @throws NullPointerException
   *           if <code>pipe==null</code>.
   */
  public abstract void writeToPipe(final IPipeIn<G, PP> pipe);

}

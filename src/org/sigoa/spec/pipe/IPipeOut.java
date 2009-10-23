/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-01
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.pipe.IPipeOut.java
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
 * This interface specifies the output end of a pipe. It is possible to
 * specify destination pipes here.
 *
 * @param <G>
 *          The genotype
 * @param <PP>
 *          The phenotype
 * @author Thomas Weise
 */
public interface IPipeOut<G extends Serializable, PP extends Serializable>
    extends Serializable{

  /**
   * Set the ouput pipe.
   *
   * @param pipe
   *          The output pipe.
   */
  public abstract void setOutputPipe(
      final IPipeIn<? super G, ? super PP> pipe);

  /**
   * Obtain the output pipe.
   *
   * @return The output pipe, or <code>null</code> if none is set.
   */
  public abstract IPipeIn<? super G, ? super PP> getOutputPipe();

//  /**
//   * Set whether this pipe should call eof on the output pipe after writing
//   * its output to it or not. The default setting is <code>true</code>.
//   *
//   * @param callEOF
//   *          <code>true</code> if and only if this pipe should call
//   *          <code>eof</code> on the output pipe after finishing writing
//   *          its output to it, <code>false</code> otherwise.
//   */
//  public abstract void setCallEOF(final boolean callEOF);
//
//  /**
//   * Obtain whether this pipe calls <code>eof</code> of the output pipe
//   * after finishing its work.
//   *
//   * @return <code>true</code> if and only if this pipe is calling the
//   *         <code>eof</code>-method of the output pipe after finishing
//   *         writing its output.
//   */
//  public abstract boolean isCallingEOF();

}

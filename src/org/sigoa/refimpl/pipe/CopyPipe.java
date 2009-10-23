/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-14
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.pipe.CopyPipe.java
 * Last modification: 2006-12-14
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

package org.sigoa.refimpl.pipe;

import java.io.Serializable;

import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.pipe.IPipeIn;

/**
 * A copy pipe copies its input to two output pipes. It still has
 * whatsoever a main output pipe.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class CopyPipe<G extends Serializable, PP extends Serializable>
    extends Pipe<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the pipe to copy the input to.
   */
  private IPipeIn<G, PP> m_cpy;

  /**
   * Create a new copy pipe.
   */
  public CopyPipe() {
    super();
  }

  /**
   * Write a new individual into the pipe. This default implementation only
   * checks the possible errors and throws exceptions if needed, but in
   * principle does nothing.
   *
   * @param individual
   *          The new individual to be written.
   * @throws NullPointerException
   *           if <code>individual</code> is <code>null</code>.
   */
  @Override
  public void write(final IIndividual<G, PP> individual) {
    IPipeIn<G, PP> x;
    super.write(individual);
    x = this.m_cpy;
    if (x != null)
      x.write(individual);
  }

  /**
   * Obtain the pipe the input of this pipe is copied to.
   *
   * @return the pipe the input of this pipe is copied to
   */
  public IPipeIn<? super G, ? super PP> getCopyPipe() {
    return this.m_cpy;
  }

  /**
   * Set the pipe the input should be copied to.
   *
   * @param pipe
   *          the pipe the input of this pipe is to be copied to
   */
  @SuppressWarnings("unchecked")
  public void setCopyPipe(final IPipeIn<? super G, ? super PP> pipe) {
    this.m_cpy = ((IPipeIn<G, PP>) (pipe));
  }

  /**
   * Propagate an <code>eof</code> to the next pipe stage and to the copy
   * pipe end.
   */
  @Override
  public void eof() {
    IPipeIn<G, PP> p;

    p = this.m_cpy;
    if (p != null)
      p.eof();

    super.eof();
  }
}

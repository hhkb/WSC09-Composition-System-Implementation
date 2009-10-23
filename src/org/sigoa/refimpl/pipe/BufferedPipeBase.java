/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-14
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.pipe.BufferedPipeBase.java
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;

import org.sigoa.spec.go.IIndividual;

/**
 * An internal base class for buffered pipes.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
abstract class BufferedPipeBase<G extends Serializable, PP extends Serializable>
    extends Pipe<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The internal buffer.
   */
  transient IIndividual<G, PP>[] m_buffer;

  /**
   * The current buffer size.
   */
  transient int m_bufferSize;

  /**
   * Create a new buffered pipe base.
   */
  @SuppressWarnings("unchecked")
  BufferedPipeBase() {
    super();
    this.m_buffer = new IIndividual[1024];
  }

  /**
   * Store a list of individual into the internal buffer.
   *
   * @param individuals
   *          The list of individuals to be buffered.
   */
  protected abstract void bufferIndividuals(
      final List<IIndividual<G, PP>> individuals);

  /**
   * Tell the pipe that all individuals have been written to it. This
   * method forwards to <code>doEof</code> which is synchronized. You
   * should override <code>doEof</code> instead of this method if
   * changing the behavior. After invoking <code>doEof</code>, the
   * <code>eof</code> of the next pipe state is invoked (indirectly) by
   * this method. Since we left our own <code>synchronized</code> block
   * when doing so, this pipe level does not block itself when invoking the
   * next pipe stage.
   *
   * @see #doEof()
   */
  @Override
  public void eof() {
    this.doEof();
    super.eof();
  }

  /**
   * This method passes an array containing all the buffered individuals to
   * <code>process</code>.
   */
  protected abstract void doEof();

  /**
   * Deserialize the buffered pipe.
   *
   * @param stream
   *          The stream to deserialize from.
   * @throws IOException
   *           if io fucks up.
   * @throws ClassNotFoundException
   *           if the class could not be found.
   */
  @SuppressWarnings("unchecked")
  private final void readObject(final ObjectInputStream stream)
      throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.m_buffer = new IIndividual[1024];
  }
}

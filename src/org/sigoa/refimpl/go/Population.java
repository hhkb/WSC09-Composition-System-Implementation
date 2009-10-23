/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-30
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.Population.java
 * Last modification: 2006-11-30
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

package org.sigoa.refimpl.go;

import java.io.Serializable;

import org.sfc.collections.lists.DefaultList;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.IPopulation;
import org.sigoa.spec.pipe.IPipeIn;

/**
 * The default implementation of the <code>IPopulation</code>-interface.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class Population<G extends Serializable, PP extends Serializable>
    extends DefaultList<IIndividual<G, PP>> implements IPopulation<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new population record.
   */
  public Population() {
    super();
  }

  /**
   * Create a new population record.
   *
   * @param initialSize
   *          an initial count of slots to reserve
   */
  public Population(final int initialSize) {
    super(initialSize);
  }

  /**
   * Write a new individual into the pipe.
   *
   * @param individual
   *          The new individual to be written.
   * @throws NullPointerException
   *           if <code>individual</code> is <code>null</code>.
   */
  public synchronized void write(final IIndividual<G, PP> individual) {
    if (individual == null)
      throw new NullPointerException();
    this.add(individual);
  }

  /**
   * Tell the pipe that all individuals have been written to it. This
   * method, as implemented here, does nothing.
   */
  public void eof() {
    //
  }

  /**
   * Store the contents of this object to a pipe. This method calls
   * <code>eof()</code> of the pipe.
   *
   * @param pipe
   *          The pipe to write to.
   * @throws NullPointerException
   *           if <code>pipe==null</code>.
   */
  public  synchronized void writeToPipe(final IPipeIn<G, PP> pipe) {
    int i;

    for (i = (this.size() - 1); i >= 0; i--) {
      pipe.write(this.get(i));
    }

    pipe.eof();
  }
}

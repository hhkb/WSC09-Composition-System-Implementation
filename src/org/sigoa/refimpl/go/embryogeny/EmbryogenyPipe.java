/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-01
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.embryogeny.EmbryogenyPipe.java
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

package org.sigoa.refimpl.go.embryogeny;

import java.io.Serializable;

import org.sigoa.refimpl.pipe.Pipe;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.embryogeny.IEmbryogenyPipe;

/**
 * A simple breeder pipe implementation which forwards breeding to a single
 * breeder instance.
 *
 * @param <G>
 *          The genotype
 * @param <PP>
 *          The phenotype
 * @author Thomas Weise
 */
public class EmbryogenyPipe<G extends Serializable, PP extends Serializable>
    extends Pipe<G, PP> implements IEmbryogenyPipe<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new breeding pipe.
   */
  public EmbryogenyPipe() {
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
    PP p;
    G g;
    IEmbryogeny<G, PP> i;

    if (individual == null)
      throw new NullPointerException();

    p = individual.getPhenotype();
    g = individual.getGenotype();

    if ((p == null) || (g == null)) {
      i = this.getEmbryogeny();
      if (i != null) {
        if (g != null) {
          individual.setPhenotype(i.hatch(g));
        }
        if (p != null) {
          individual.setGenotype(i.regress(p));
        }
      }
    }

    this.output(individual);
  }
}

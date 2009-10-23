/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-01
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.reproduction.CreatorPipe.java
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

package org.sigoa.refimpl.go.reproduction;

import java.io.Serializable;

import org.sigoa.refimpl.go.PassThroughPipe;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.IIndividualFactory;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICreatorPipe;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * A simple creator pipe implementation which forwards creation to a single
 * creator instance.
 *
 * @param <G>
 *          The genotype
 * @param <PP>
 *          The phenotype
 * @author Thomas Weise
 */
public class CreatorPipe<G extends Serializable, PP extends Serializable>
    extends PassThroughPipe<G, PP> implements ICreatorPipe<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the count of individuals that passed this pipe in this turn
   */
  private int m_passed;

  /**
   * Create a new creation pipe.
   *
   * @param count
   *          The initial count of individuals that will at least pass
   *          through this pipe per turn
   * @throws IllegalArgumentException
   *           if <code>count&lt;1</code>
   */
  public CreatorPipe(final int count) {
    super(count);
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
    synchronized (this) {
      this.m_passed++;
    }
    this.output(individual);
  }

  /**
   * Propagate an <code>eof</code> to the next pipe stage if needed.
   */
  @Override
  public void eof() {
    int i, j;
    ICreator<G> c;
    IRandomizer r;
    IIndividual<G, PP> in;
    IIndividualFactory<G, PP> f;

    synchronized (this) {
      i = (this.getPassThroughCount() - this.m_passed);
      this.m_passed = 0;

      if (i > 0) {
        f = this.getIndividualFactory();
        r = this.getRandomizer();
        j = this.getObjectiveValueCount();
        c = this.getCreator();
        for (; i > 0; i--) {
          in = f.createIndividual(j);
          in.setGenotype(c.create(r));
          this.output(in);
        }
      }
    }

    super.eof();
  }
}

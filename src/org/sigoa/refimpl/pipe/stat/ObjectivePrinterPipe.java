/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.pipe.stat.ObjectivePrinterPipe.java
 * Last modification: 2008-05-20
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

package org.sigoa.refimpl.pipe.stat;

import java.io.Serializable;

import org.sfc.io.TextWriter;
import org.sfc.io.writerProvider.IWriterProvider;
import org.sigoa.refimpl.go.evaluation.Evaluator;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.evaluation.IEvaluator;
import org.sigoa.spec.go.objectives.IObjectiveFunction;

/**
 * this special pipe stores the objective values of the individuals it
 * receives to an output writer
 * 
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class ObjectivePrinterPipe<G extends Serializable, PP extends Serializable>
    extends PrinterPipe<G, PP, TextWriter> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * Create a print pipe.
   * 
   * @param provider
   *          the text writer provider to be used to obtain the output
   *          writers
   * @param flushing
   *          <code>true</code> if and only if this pipe is flushing
   *          after every output, <code>false</code> otherwise
   * @throws NullPointerException
   *           if <code>provider==null</code>
   */
  public ObjectivePrinterPipe(final IWriterProvider<TextWriter> provider,
      final boolean flushing) {
    super(provider, flushing);
  }

  /**
   * Create a print pipe.
   * 
   * @param provider
   *          the text writer provider to be used to obtain the output
   *          writers
   * @throws NullPointerException
   *           if <code>provider==null</code>
   */
  public ObjectivePrinterPipe(final IWriterProvider<TextWriter> provider) {
    super(provider, true);
  }

  /**
   * This method is called when an iteration begins.
   * 
   * @param w
   *          the text writer.
   * @param iteration
   *          the index of the iteration that just ended
   */
  @Override
  protected void onIterationBegin(final TextWriter w, final long iteration) {
    int i, j;
    IObjectiveFunction<?, ?, ?, ?>[] l;
    IEvaluator<?> ev;

    super.onIterationBegin(w, iteration);

    ev = this.getOptimizationInfo().getEvaluator();
    if (ev instanceof Evaluator) {
      l = ((Evaluator<?>) ev).getObjectiveFunctions();

      i = l.length;
      for (j = 0; j < i; j++) {
        if (j > 0)
          w.writeCSVSeparator();
        w.writeObject(l[j]);
      }
    } else {
      i = this.getEvaluator().getObjectiveValueCount();
      for (j = 0; j < i; j++) {
        if (j > 0)
          w.writeCSVSeparator();
        w.write("objective_");//$NON-NLS-1$
        w.writeInt(j);
      }
    }
  }

  /**
   * Write an individual's data to the output.
   * 
   * @param individual
   *          The new individual to be written.
   * @param dest
   *          the text writer to write to
   */
  @Override
  protected void outputIndividual(final IIndividual<G, PP> individual,
      final TextWriter dest) {

    int i, j;

    i = individual.getObjectiveValueCount();
    dest.ensureNewLine();
    for (j = 0; j < i; j++) {
      if (j > 0)
        dest.writeCSVSeparator();
      dest.writeRawDouble(individual.getObjectiveValue(j));
    }
  }

}

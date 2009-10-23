/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-05-06
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.benchmark.adrulyte.EAFactory.java
 * Last modification: 2008-05-06
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

package test.org.dgpf.benchmark.adrulyte;

import java.io.Serializable;

import org.sigoa.refimpl.go.algorithms.ea.EA;
import org.sigoa.refimpl.go.fitnessAssignment.PrevalenceFitnessAssigner2;
import org.sigoa.refimpl.go.fitnessAssignment.SumFitnessAssigner;
import org.sigoa.refimpl.go.fitnessAssignment.variety.VarietyFitnessAssigner;
import org.sigoa.refimpl.go.selection.TournamentSelectionR;
import org.sigoa.refimpl.go.selection.TruncationSelectionR;
import org.sigoa.refimpl.utils.testSeries.ea.IEAFactory;
import org.sigoa.spec.go.fitnessAssignment.IFitnessAssigner;
import org.sigoa.spec.go.selection.ISelectionAlgorithm;

/**
 * the internal ea factory for the test series
 * 
 * @author Thomas Weise
 */
public class EAFactory implements IEAFactory {

  /**
   * the fitness assignment method
   */
  int m_fitnessAssignment;

  /**
   * the selection method
   */
  int m_selection;

  /**
   * Create the evolutionary algorithm
   * 
   * @return the new evolutionary algorithm, ready to use
   */

  public EA<?, ?> createEA() {
    return new MyEA();
  }

  /**
   * the internal ea class
   * 
   * @author Thomas Weise
   */
  private class MyEA extends EA<Serializable, Serializable> {
    /**
     * The serial version uid.
     */
    private static final long serialVersionUID = 1;

    /**
     * create the ea
     */
    MyEA() {
      super();
    }

    /**
     * Create the fitness assigner to be used by this ea. This method is
     * invoked by <code>createPipeline</code>.
     * 
     * @return the fitness assigner pipe to be used by this ea
     */
    @Override
    protected IFitnessAssigner<Serializable, Serializable> createFitnessAssigner() {
      switch (EAFactory.this.m_fitnessAssignment % 3) {
      case 2:
        return new SumFitnessAssigner<Serializable, Serializable>();
      case 1:
        return new VarietyFitnessAssigner<Serializable, Serializable>();
      default:
        return new PrevalenceFitnessAssigner2<Serializable, Serializable>();
      }
    }

    /**
     * Create the selection algorithm to be used by this ea. This method is
     * invoked by <code>createPipeline</code>.
     * 
     * @return the selection algorithm to be used by this ea
     */
    @Override
    protected ISelectionAlgorithm<Serializable, Serializable> createSelectionAlgorithm() {
      switch (EAFactory.this.m_selection % 4) {
      case 3:
        return new TruncationSelectionR<Serializable, Serializable>(true,
            this.getNextPopulationSize());
      case 2:
        return new TournamentSelectionR<Serializable, Serializable>(true,
            this.getNextPopulationSize(), 10);
      case 1:
        return new TournamentSelectionR<Serializable, Serializable>(true,
            this.getNextPopulationSize(), 2);
      default:
        return new TournamentSelectionR<Serializable, Serializable>(true,
            this.getNextPopulationSize(), 5);
      }
    }
  }
}

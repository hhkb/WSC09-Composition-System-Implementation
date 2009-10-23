/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-03
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.ImplementationBase.java
 * Last modification: 2006-12-03
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

import org.sfc.text.Textable;
import org.sigoa.refimpl.go.comparators.ParetoComparator;
import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.IIndividualFactory;
import org.sigoa.spec.go.IOptimizationInfo;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.evaluation.IEvaluator;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.jobsystem.IHost;
import org.sigoa.spec.jobsystem.IJobInfo;
import org.sigoa.spec.jobsystem.JobSystemUtils;
import org.sigoa.spec.simulation.ISimulationManager;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * A base class for all global optimization objects.
 * 
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class ImplementationBase<G extends Serializable, PP extends Serializable>
    extends Textable implements Serializable {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Obtain the host this object is currently used in.
   * 
   * @return the host this object is currently used in or <code>null</code>
   *         if no host could be detected
   */
  protected IHost getHost() {
    return JobSystemUtils.getCurrentHost();
  }

  /**
   * Obtain the job info record that was passed in along with the job this
   * object is used by.
   * 
   * @return the information about the job this object is currently used by
   *         or <code>null</code> if no such information could be
   *         detected
   */
  protected IJobInfo<G, PP> getJobInfo() {
    IHost h;
    h = this.getHost();
    if (h != null)
      return h.getJobInfo();
    return null;
  }

  /**
   * Obtain the optimization information valid for the current optimization
   * process.
   * 
   * @return the optimization information or <code>null</code> if none
   *         could be detected
   */
  protected IOptimizationInfo<G, PP> getOptimizationInfo() {
    IJobInfo<G, PP> j;
    j = this.getJobInfo();
    if (j != null)
      return j.getOptimizationInfo();
    return null;
  }

  /**
   * Obtain the comparator to be used for the fitness assignment process.
   * This method first tries to query the current host. If it is run inside
   * a host environment, it returns the comparator of the host's jobinfo's
   * optimization info. Otherwise, the default comparator
   * (pareto-comparator) is returned.
   * 
   * @return The comparator to be used for fitness assignment.
   */
  protected IComparator getComparator() {
    IOptimizationInfo<G, PP> o;

    o = this.getOptimizationInfo();
    if (o != null)
      return o.getComparator();
    return ParetoComparator.PARETO_COMPARATOR;
  }

  /**
   * Obtain the randomizer assigned to this job. This will be the
   * randomizer provided by the host thread.
   * 
   * @return the randomizer
   */
  protected IRandomizer getRandomizer() {
    IHost h;

    h = this.getHost();
    if (h != null)
      return h.getRandomizer();
    return null;
  }

  /**
   * Obtain the default evaluator used in order to determine the objective
   * values of an individual.
   * 
   * @return the evaluator used in order to determine the objective values
   *         of an individual or <code>null</code> if none could be
   *         detected
   */
  protected IEvaluator<PP> getEvaluator() {
    IOptimizationInfo<G, PP> o;

    o = this.getOptimizationInfo();
    if (o != null)
      return o.getEvaluator();
    return null;
  }

  /**
   * Obtain the objective value count valid for this optimization process.
   * 
   * @return the objective value count valid for this optimization process
   *         or <code>-1</code> if it could not be determined
   */
  protected int getObjectiveValueCount() {
    IEvaluator<?> e;

    e = this.getEvaluator();
    if (e != null)
      return e.getObjectiveValueCount();
    return -1;
  }

  /**
   * Obtain the simulation manager to be used for the evaluations.
   * 
   * @return the simulation manager to be used for the evaluations or
   *         <code>null</code> if none could be detected
   */
  protected ISimulationManager getSimulationManager() {
    IHost h;

    h = this.getHost();
    if (h != null)
      return h.getSimulationManager();
    return null;
  }

  /**
   * Obtain the embryogeny set in the optimization info record.
   * 
   * @return the embryogeny set in the optimization info record or
   *         <code>null</code> if none could be detected
   */
  protected IEmbryogeny<G, PP> getEmbryogeny() {
    IOptimizationInfo<G, PP> o;

    o = this.getOptimizationInfo();
    return o.getEmbryogeny();
  }

  /**
   * Obtain the mutator set in the optimization info record.
   * 
   * @return the mutator set in the optimization info record or
   *         <code>null</code> if none could be detected
   */
  protected IMutator<G> getMutator() {
    IOptimizationInfo<G, PP> o;

    o = this.getOptimizationInfo();
    return o.getMutator();
  }

  /**
   * Obtain the creator set in the optimization info record.
   * 
   * @return the creator set in the optimization info record or
   *         <code>null</code> if none could be detected
   */
  protected ICreator<G> getCreator() {
    IOptimizationInfo<G, PP> o;

    o = this.getOptimizationInfo();
    return o.getCreator();
  }

  /**
   * Obtain the crossover set in the optimization info record.
   * 
   * @return the crossover set in the optimization info record or
   *         <code>null</code> if none could be detected
   */
  protected ICrossover<G> getCrossover() {
    IOptimizationInfo<G, PP> o;

    o = this.getOptimizationInfo();
    return o.getCrossover();
  }

  /**
   * Obtain the id of the current optimization process
   * 
   * @return the id of the current optimization process
   */
  protected Serializable getOptimizationId() {
    IHost h;
    h = this.getHost();
    return ((h != null) ? h.getOptimizationId() : null);
  }

  /**
   * Obtain the individual factory to be used in this optimization process.
   * 
   * @return the individual factory to be used in this optimization process
   */
  @SuppressWarnings("unchecked")
  protected IIndividualFactory<G, PP> getIndividualFactory() {
    IOptimizationInfo<G, PP> info;
    info = this.getOptimizationInfo();
    if (info != null)
      return info.getIndividualFactory();
    return ((IIndividualFactory<G, PP>) (IndividualFactory.DEFAULT_INDIVIDUAL_FACTORY));
  }
}

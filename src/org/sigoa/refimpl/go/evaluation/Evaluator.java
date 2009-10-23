/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.evaluation.Evaluator.java
 * Last modification: 2007-09-06
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

package org.sigoa.refimpl.go.evaluation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.math.Mathematics;
import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.go.IHysteresis;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.OptimizationUtils;
import org.sigoa.spec.go.evaluation.IEvaluator;
import org.sigoa.spec.go.objectives.IObjectiveFunction;
import org.sigoa.spec.go.objectives.IObjectiveState;
import org.sigoa.spec.simulation.ISimulation;
import org.sigoa.spec.simulation.ISimulationManager;

/**
 * The standard implementation of the <code>IEvaluator</code>-interface.
 * 
 * @param <PP>
 *          The phenotype to evaluate.
 * @author Thomas Weise
 */
public class Evaluator<PP extends Serializable> extends
    ImplementationBase<Serializable, PP> implements IEvaluator<PP>,
    IHysteresis {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The internal array of all objective functions.
   */
  private final IObjectiveFunction<PP, IObjectiveState, Serializable, ISimulation<PP>>[] m_allObjectives;

  /**
   * The internal array of objective functions.
   */
  private final IObjectiveFunction<PP, IObjectiveState, Serializable, ISimulation<PP>>[][] m_objectives;

  /**
   * the hysteresis array
   */
  private final boolean[] m_hyst;

  // /**
  // * the static state of the objective functions.
  // */
  // private final Serializable[][] m_staticData;
  //
  // /**
  // * the second static state array.
  // */
  // private final Serializable[] m_staticData2;

  /**
   * The true indices of the fitness functions.
   */
  private final int[][] m_assignment;

  /**
   * The simulator ids.
   */
  private final Serializable[] m_simulators;

  /**
   * The states list.
   */
  private volatile OFState m_states;

  /**
   * The count of evaluations per individual to be performed.
   */
  private final int m_times;

  /**
   * the re-evaluation feature
   */
  private volatile boolean m_reEvaluate;

  /**
   * the part to which old evaluations are valued, normally 0
   */
  private volatile double m_hystersis;

  /**
   * Create a new evaluator.
   * 
   * @param objectives
   *          A list of objective functions.
   * @throws NullPointerException
   *           If the objective function list or any of its items is
   *           <code>null</code>.
   */
  public Evaluator(
      final List<IObjectiveFunction<PP, ?, ?, ISimulation<PP>>> objectives) {
    this(objectives, false);
  }

  /**
   * Create a new evaluator.
   * 
   * @param objectives
   *          A list of objective functions.
   * @param reEvaluate
   *          <code>true</code> if individuals which already have a
   *          fitness assigned should be re-evaluated,<code>false</code>
   *          otherwise
   * @throws NullPointerException
   *           If the objective function list or any of its items is
   *           <code>null</code>.
   */
  public Evaluator(
      final List<IObjectiveFunction<PP, ?, ?, ISimulation<PP>>> objectives,
      final boolean reEvaluate) {
    this(objectives, 1, reEvaluate);
  }

  /**
   * Create a new evaluator.
   * 
   * @param objectives
   *          A list of objective functions.
   * @param times
   *          The count of evaluations per individual to be performed
   * @param reEvaluate
   *          <code>true</code> if individuals which already have a
   *          fitness assigned should be re-evaluated,<code>false</code>
   *          otherwise
   * @throws NullPointerException
   *           If the objective function list or any of its items is
   *           <code>null</code>.
   * @throws IllegalArgumentException
   *           If the objective function list is empty or if
   *           <code>times&lt;=0</code>.
   */
  @SuppressWarnings("unchecked")
  public Evaluator(
      final List<IObjectiveFunction<PP, ?, ?, ISimulation<PP>>> objectives,
      final int times, final boolean reEvaluate) {
    this(objectives, times, reEvaluate, 0.0d);
  }

  /**
   * Create a new evaluator.
   * 
   * @param objectives
   *          A list of objective functions.
   * @param times
   *          The count of evaluations per individual to be performed
   * @param reEvaluate
   *          <code>true</code> if individuals which already have a
   *          fitness assigned should be re-evaluated,<code>false</code>
   *          otherwise
   * @param hystersis
   *          a value <code>0&lt;= hystersis &lt; 1</code> which denotes
   *          the fraction of the old objective values, normally 0
   * @throws NullPointerException
   *           If the objective function list or any of its items is
   *           <code>null</code>.
   * @throws IllegalArgumentException
   *           If the objective function list is empty or if
   *           <code>((l &lt;= 0) || (times &lt;= 0) || (hystersis &lt; 0) || (hystersis &gt;= 1))</code>.
   */
  @SuppressWarnings("unchecked")
  public Evaluator(
      final List<IObjectiveFunction<PP, ?, ?, ISimulation<PP>>> objectives,
      final int times, final boolean reEvaluate, final double hystersis) {
    super();

    int l, i, j, k;

    List<SimSet> simLists;
    List<IObjectiveFunction<PP, IObjectiveState, Serializable, ISimulation<PP>>> ll;
    IObjectiveFunction<PP, IObjectiveState, Serializable, ISimulation<PP>>[][] oo;
    IObjectiveFunction<PP, IObjectiveState, Serializable, ISimulation<PP>>[] ooo;
    Serializable[] sims;
    int[][] assign;
    boolean[] o;
    int[] as;
    SimSet e;

    l = objectives.size();
    if ((l <= 0) || (times <= 0) || (hystersis < 0) || (hystersis >= 1))
      throw new IllegalArgumentException();

    this.m_hystersis = hystersis;
    this.m_times = times;

    ooo = new IObjectiveFunction[l];
    objectives.toArray(ooo);
    this.m_allObjectives = ooo;

    simLists = buildSimSet(((List) (objectives)));

    l = simLists.size();
    oo = new IObjectiveFunction[l][];
    assign = new int[l][];
    sims = new Serializable[l];

    for (--l; l >= 0; l--) {
      e = simLists.get(l);
      ll = ((List) (e.m_obj));
      sims[l] = e.computeId();
      i = ll.size();
      assign[l] = as = new int[i];
      ooo = new IObjectiveFunction[i];
      ll.toArray(ooo);
      oo[l] = ooo;

      for (j = (i - 1); j >= 0; j--) {
        for (k = (this.m_allObjectives.length - 1); k >= 0; k--) {
          if (ooo[j] == this.m_allObjectives[k]) {
            as[j] = k;
            break;
          }
        }
      }
    }

    this.m_assignment = assign;
    this.m_objectives = oo;
    this.m_simulators = sims;
    this.m_reEvaluate = reEvaluate;

    l = objectives.size();
    this.m_hyst = o = new boolean[l];
    for (--l; l >= 0; l--) {
      if ((this.m_allObjectives[l] instanceof IHysteresis)) {
        o[l] = ((IHysteresis) (this.m_allObjectives[l])).usesHysteresis();
      }
    }
  }

  /**
   * Obtain the hysteresis value.
   * 
   * @return the hysteresis value
   */
  public double getHysteresis() {
    return this.m_hystersis;
  }

  // not yet provided, would require synchronization
  //
  // /**
  // * Set the hysteresis value
  // *
  // * @param hysteresis
  // * the new hystersis value
  // * @throws IllegalArgumentException
  // * If the objective function list is empty or if
  // * <code>times&lt;=0</code>.
  // */
  // public void setHysteresis(final double hysteresis) {
  // if ((hysteresis < 0) || (hysteresis >= 1))
  // throw new IllegalArgumentException();
  // this.m_hystersis = hysteresis;
  // }

  /**
   * Obtain the list of objective functions applied
   * 
   * @return the list of objective functions applied
   */
  public IObjectiveFunction<PP, IObjectiveState, Serializable, ISimulation<PP>>[] getObjectiveFunctions() {
    return this.m_allObjectives.clone();
  }

  /**
   * Build the set of simulation groups.
   * 
   * @param obj
   *          the objectives
   * @return the simulation set
   */
  private static final List<SimSet> buildSimSet(
      final List<IObjectiveFunction<?, ?, Serializable, ?>> obj) {
    List<SimSet> l;
    int i, j, k;
    IObjectiveFunction<?, ?, Serializable, ?> o;
    List<Class<?>> l2;
    Serializable id;
    Class<?> c, c2;
    SimSet s;
    boolean b1, b2;

    i = obj.size();
    l = CollectionUtils.createList(i);
    main: for (--i; i >= 0; i--) {
      o = obj.get(i);
      if (o == null)
        throw new NullPointerException();
      id = o.getRequiredSimulationId();
      c = ((id instanceof Class) ? ((Class<?>) id) : null);

      for (j = (l.size() - 1); j >= 0; j--) {
        s = l.get(j);
        if (s.m_id == id) {
          s.m_obj.add(o);
          continue main;
        }
        if (c != null) {
          l2 = s.m_cls;
          if (l2 != null) {
            b1 = true;
            b2 = true;
            for (k = (l2.size() - 1); (k >= 0) && (b1 || b2); k--) {
              c2 = l2.get(k);
              b1 &= (c2.isAssignableFrom(c));
              b2 &= (c.isAssignableFrom(c2));
            }

            if (b1 || b2) {
              s.m_obj.add(o);
              s.m_cls.add(c);
              continue main;
            }
          }
        }
      }

      s = new SimSet(id, c);
      s.m_obj.add(o);
      l.add(s);
    }

    return l;
  }

  /**
   * Allocate an <code>OFState</code>-instance.
   * 
   * @return The <code>OFState</code>-instance.
   */
  private final OFState allocState() {
    OFState o;
    synchronized (this.m_objectives) {
      o = this.m_states;
      if (o != null) {
        this.m_states = o.m_next;
        return o;
      }
    }
    return new OFState(this.m_objectives, this.m_assignment, this.m_times,
        this.m_allObjectives.length);
  }

  /**
   * Dispose an <code>OFState</code>-instance.
   * 
   * @param state
   *          The <code>OFState</code>-instance.
   */
  private final void disposeState(final OFState state) {
    synchronized (this.m_objectives) {
      state.m_next = this.m_states;
      this.m_states = state;
    }
  }

  /**
   * Evaluate an individual by computing all its objective values.
   * 
   * @param individual
   *          The individual to be evaluated.
   * @throws NullPointerException
   *           if the individual or its phenotype or the host is
   *           <code>null</code> or if no simulation manager is
   *           available.
   */
  @SuppressWarnings("unchecked")
  public void evaluate(final IIndividual<?, PP> individual) {
    final PP p;
    final OFState s;
    IObjectiveFunction<PP, IObjectiveState, Serializable, ISimulation<PP>>[] funcs;
    int i;
    final ISimulation<PP>[] ss;
    Serializable[] sss;
    ISimulationManager simulationManager;
    Serializable[] sers;
    boolean hasOldFitness;

    p = individual.getPhenotype();
    if (p == null)
      throw new NullPointerException();

    hasOldFitness = (Double.compare(individual.getFitness(),
        OptimizationUtils.WORST) != 0);
    if (hasOldFitness) {
      if (!(this.m_reEvaluate)) {
        individual.clearFitness();
        return;
      }

      if (this.m_hystersis <= 0) {
        individual.clearEvaluation();
        hasOldFitness = false;
      }
    }

    if ((simulationManager = this.getSimulationManager()) == null)
      throw new NullPointerException();

    funcs = this.m_allObjectives;

    s = this.allocState();

    try {
      sers = s.m_staticData2;
      for (i = (funcs.length - 1); i >= 0; i--) {
        if (!(funcs[i].sanityCheck(p, sers[i])))
          return;
      }

      for (i = (funcs.length - 1); i >= 0; i--) {
        funcs[i].beginIndividual(p, sers[i]);
      }

      ss = ((ISimulation<PP>[]) (s.m_sims));
      sss = this.m_simulators;

      try {
        simulationManager.getSimulations(sss, ss);

        for (i = (ss.length - 1); i >= 0; i--) {
          if (ss[i] != null)
            ss[i].beginIndividual(p);
        }

        for (i = (this.m_times - 1); i >= 0; i--) {
          this.singleEvaluation(p, ss, this.m_objectives, s.m_state[i],
              s.m_staticData, s.m_temp);
        }

        for (i = (ss.length - 1); i >= 0; i--) {
          if (ss[i] != null)
            ss[i].endIndividual();
        }

      } finally {
        for (i = (ss.length - 1); i >= 0; i--) {
          if (ss[i] != null)
            simulationManager.returnSimulation(ss[i]);
          ss[i] = null;
        }
      }

      for (i = (funcs.length - 1); i >= 0; i--) {
        funcs[i].endIndividual(p, sers[i]);
      }
      this.summarize(individual, s.m_state, sers, s.m_res, hasOldFitness);

    } finally {
      this.disposeState(s);
    }

  }

  /**
   * Perform one single evaluation.
   * 
   * @param ind
   *          The individual to fill in.
   * @param states
   *          The objective function states.
   * @param sers
   *          The static states.
   * @param res
   *          The temporary result storage.
   * @param hasOldFitness
   *          <code>true</code> if hysteresis should be applied
   */
  private final void summarize(final IIndividual<?, PP> ind,
      final IObjectiveState[][][] states, final Serializable[] sers,
      final double[] res, final boolean hasOldFitness) {
    int[][] assign;
    int[] as;
    int i, j, t, v;
    IObjectiveFunction<PP, IObjectiveState, Serializable, ISimulation<PP>>[] ao;
    IObjectiveState q;
    double h, x, z;
    boolean d;
    boolean[] u;

    ao = this.m_allObjectives;
    assign = this.m_assignment;
    h = this.m_hystersis;
    u = this.m_hyst;
    d = ((h > 0d) && hasOldFitness);
    for (i = (assign.length - 1); i >= 0; i--) {
      as = assign[i];
      for (j = (as.length - 1); j >= 0; j--) {
        for (t = (states.length - 1); t >= 0; t--) {
          q = states[t][i][j];
          res[t] = q.getObjectiveValue();
          q.clear();
        }

        Arrays.sort(res);

        v = as[j];
        x = ao[v].computeObjectiveValue(res, sers[v]);
        if (d && u[v]) {
          z = ind.getObjectiveValue(v);
          if (Mathematics.isNumber(z))
            x = ((h * z) + ((1.0d - h) * x));
        }
        ind.setObjectiveValue(v, x);
      }
    }
  }

  /**
   * Does this component apply hysteresis?
   * 
   * @return <code>true</code> if hysteresis is/cab be applied by this
   *         component, <code>false</code> otherwise
   */
  public boolean usesHysteresis() {
    return (this.m_hystersis > 0.0d);
  }

  /**
   * Perform one single evaluation.
   * 
   * @param p
   *          The phenotype to be evaluated.
   * @param sims
   *          The simulations to be used.
   * @param ofs
   *          The objective functions.
   * @param states
   *          The objective function states.
   * @param tmp
   *          The temporary longs.
   * @param serD
   *          the static states
   */
  private final void singleEvaluation(
      final PP p,
      final ISimulation<PP>[] sims,
      final IObjectiveFunction<PP, IObjectiveState, Serializable, ISimulation<PP>>[][] ofs,
      final IObjectiveState[][] states, final Serializable[][] serD,
      final long[] tmp) {
    int i, j, l;
    long t, t2, k, m, om;
    IObjectiveFunction<PP, IObjectiveState, Serializable, ISimulation<PP>>[] o;
    IObjectiveState[] s;
    ISimulation<PP> x;
    Serializable[] ser;
    boolean running;

    // for all simulations
    for (i = (sims.length - 1); i >= 0; i--) {
      o = ofs[i];
      t = 0;
      s = states[i];
      x = sims[i];
      ser = serD[i];
      l = (o.length - 1);
      // if a simulation is assigned to this objective function set
      if (x != null) {
        x.beginSimulation();
        running=true;
        try {
          try {
            // find out the required simulation count and the first
            // inspections
            k = Long.MAX_VALUE;
            for (j = l; j >= 0; j--) {
              t2 = o[j].getRequiredSimulationSteps(p, s[j], ser[j]);
              if (t2 > t)
                t = t2;
              tmp[j] = m = o[j].beginEvaluation(p, s[j], ser[j], x);
              if ((m < k) && (m > 0))
                k = m;
            }

            // perform the simulation steps
            while (t > 0) {
              // no inspection steps are needed?
              if (k == Long.MAX_VALUE) {
                x.simulate(t);
                t = 0;
              } else {
                // perform at most min(t,k) steps
                if (t < k)
                  k = t;
                if (!(x.simulate(k)))
                  t = -1;
                t -= k;
                om = k;
                // perform the current inspections and check when the next
                // inspection is due
                k = Long.MAX_VALUE;
                for (j = l; j >= 0; j--) {
                  m = tmp[j] - om;
                  if (m == 0) {
                    tmp[j] = m = o[j].inspect(p, s[j], ser[j], x);
                  } else
                    tmp[j] = m;
                  if ((m > 0) && (m < k)) {
                    k = m;
                  }

                }
              }
            }

            //warning: endSimultion is now invoked before endEvaluation!
            x.endSimulation();
            running=false;
            //
          } finally {
            for (j = l; j >= 0; j--) {
              o[j].endEvaluation(p, s[j], ser[j], x);
            }
          }

        } finally {
          if (running)
            x.endSimulation();
        }
      } else {
        // if no simulation is assigned to this objective function set
        for (j = l; j >= 0; j--) {
          o[j].beginEvaluation(p, s[j], ser[j], null);
          o[j].endEvaluation(p, s[j], ser[j], null);
        }
      }
    }
  }

  /**
   * An objective function state object.
   * 
   * @author Thomas Weise
   */
  private static final class OFState {
    /**
     * The set of objective function states.
     */
    final IObjectiveState[][][] m_state;

    /**
     * A temporary variable.
     */
    final long[] m_temp;

    /**
     * A temporary array for simulations allocated.
     */
    final ISimulation<?>[] m_sims;

    /**
     * The result buffer.
     */
    final double[] m_res;

    /**
     * the static state of the objective functions.
     */
    final Serializable[][] m_staticData;

    /**
     * the static state of the objective functions.
     */
    final Serializable[] m_staticData2;

    /**
     * The next objective function state.
     */
    OFState m_next;

    /**
     * Create a new <code>OFState</code>-instance.
     * 
     * @param objectives
     *          The objectives to work for.
     * @param times
     *          The count of evaluations to be performed.
     * @param as
     *          the assignment integers
     * @param len
     *          the count of objective functions
     */
    OFState(
        final IObjectiveFunction<?, ?, Serializable, ?>[][] objectives,
        final int[][] as, final int times, int len) {
      super();

      int i, j, v, t;
      IObjectiveState[][][] oos;
      IObjectiveState[][] os;
      IObjectiveState[] o;
      IObjectiveFunction<?, ?, Serializable, ?>[] x;
      Serializable[][] ser;
      Serializable[] ser2;
      Serializable[] serd;

      this.m_res = new double[times];

      oos = new IObjectiveState[times][][];

      i = objectives.length;
      ser = new Serializable[i][];
      serd = new Serializable[len];
      for (--i; i >= 0; i--) {
        x = objectives[i];
        j = x.length;
        ser[i] = ser2 = new Serializable[j];
        for (--j; j >= 0; j--) {
          serd[as[i][j]] = ser2[j] = x[j].createStaticState();
        }
      }
      this.m_staticData = ser;
      this.m_staticData2 = serd;

      for (t = (times - 1); t >= 0; t--) {
        v = 0;
        i = objectives.length;

        os = new IObjectiveState[i][];
        for (--i; i >= 0; i--) {
          ser2 = ser[i];
          x = objectives[i];
          j = x.length;
          if (j > v)
            v = j;
          o = new IObjectiveState[j];
          for (--j; j >= 0; j--) {
            o[j] = x[j].createState(ser2[j]);
          }
          os[i] = o;
        }
        oos[t] = os;
      }

      v = 0;
      i = objectives.length;
      this.m_sims = new ISimulation[i];

      for (--i; i >= 0; i--) {
        x = objectives[i];
        j = x.length;
        if (j > v)
          v = j;
      }
      this.m_state = oos;
      this.m_temp = new long[v];
    }
  }

  /**
   * Obtain the count of the objective values which are set by this
   * evaluator.
   * 
   * @return The count of objective functions applied.
   */
  @Override
  public int getObjectiveValueCount() {
    return this.m_allObjectives.length;
  }

  /**
   * the internal class used to build simulations.
   * 
   * @author Thomas Weise
   */
  private static final class SimSet {
    /**
     * the id.
     */
    Serializable m_id;

    /**
     * the objectives
     */
    final List<IObjectiveFunction<?, ?, ?, ?>> m_obj;

    /**
     * the objectives
     */
    final List<Class<?>> m_cls;

    /**
     * Create a new simulation set.
     * 
     * @param id
     *          the id
     * @param cls
     *          the id as class class
     */
    SimSet(final Serializable id, final Class<?> cls) {
      super();
      this.m_id = id;
      if (cls != null) {
        this.m_cls = CollectionUtils.createList();
        this.m_cls.add(cls);
      } else
        this.m_cls = null;
      this.m_obj = CollectionUtils.createList();
    }

    /**
     * Obtain the id of the simulations required.
     * 
     * @return the id of the simulations required
     */
    final Serializable computeId() {
      Serializable id;
      List<Class<?>> cls;
      Class<?> c, c2;
      int i;

      id = this.m_id;
      if (id == null)
        return id;
      cls = this.m_cls;
      if (cls == null)
        return id;

      c = cls.get(0);
      for (i = (cls.size() - 1); i > 0; i--) {
        c2 = cls.get(i);
        if (c2.isAssignableFrom(c))
          c = c2;
      }

      return c;
    }

  }
}

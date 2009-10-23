/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-26
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.simulation.Calculation.java
 * Last modification: 2007-03-26
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

package org.dgpf.aggregation.alternative;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.dgpf.aggregation.net.AggregationNetProgram;
import org.dgpf.aggregation.net.IAggregationFunction;
import org.dgpf.symbolicRegression.ComputationContext;
import org.sfc.collections.CollectionUtils;
import org.sfc.math.Mathematics;
import org.sigoa.refimpl.stoch.Randomizer;
import org.sigoa.refimpl.stoch.StatisticInfo;
import org.sigoa.spec.go.algorithms.IIterationHook;

/**
 * The calculation
 * 
 * @author Thomas Weise
 */
public class Calculation extends ComputationContext<AggregationNetProgram>
    implements Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the calculations ... quick and dirty hack
   */
  static final List<Calculation> CCS = CollectionUtils.createList();

  /**
   * the randomizer of the hook.
   */
  static final Randomizer RND;
  static {
    RND = new Randomizer();
    RND.setDefaultSeed();
  }

  /**
   * the globally shared iteration hook
   */
  public static final IIterationHook HOOK = new IIterationHook() {

    private static final long serialVersionUID = 1L;

    public void beforeIteration(final long iteration) {
      Long l;
      int i;
      Calculation c;

      l = new Long(RND.nextLong());
      for (i = (CCS.size() - 1); i >= 0; i--) {
        c = CCS.get(i);
        c.initializeValues(l);
      }
    }

    public void afterIteration(final long iteration) {
      //
    }
  };

  /**
   * the maximum of powers
   */
  private static final int POWER_CNT = 11;

  /**
   * the maximum of powers
   */
  private static final int POWER_SUB = (POWER_CNT >>> 1);

  /**
   * the minimum test count
   */
  public static final int MIN_TESTS = (POWER_CNT << 1);

  /**
   * the maximum weight
   */
  private static final int MAX_WEIGHT = 30;

  /**
   * sqrt 12
   */
  private static final double SQRT_12 = Math.sqrt(12.0d);

  /**
   * sqrt 3
   */
  private static final double SQRT_3 = Math.sqrt(3.0d);

  /**
   * the variables
   */
  private double[][] m_vars;

  /**
   * the variables
   */
  private double[][] m_vars2;

  /**
   * the variable count
   */
  private final int m_varCnt;

  /**
   * the virtual machine count
   */
  final int m_vmCnt;

  /**
   * the values
   */
  final double[][][] m_values;

  /**
   * the target values
   */
  final double[][] m_targets;

  /**
   * the standard deviations
   */
  final double[][] m_variances;

  /**
   * the internal randomizer
   */
  private final Randomizer m_random;

  /**
   * the change counter
   */
  int m_cc;

  /**
   * the stepg change counter
   */
  int m_cs;

  /**
   * the number of above values
   */
  int m_above;

  /**
   * the number of below values
   */
  int m_below;

  /**
   * the index
   */
  int m_index;

  /**
   * the step index
   */
  int m_stepIdx;

  /**
   * the sqrError
   */
  double m_sqrError;

  /**
   * the sqrError
   */
  double m_error;

  /**
   * the inter-value variance.
   */
  double m_var;

  /**
   * the step counter
   */
  private final int m_stepCnt;

  /**
   * the maximum data dissemination distance
   */
  private final int m_maxDist;

  /**
   * the params
   */
  final CalculationParameters m_params;

  /**
   * compare
   */
  final IAggregationFunction m_compare;

  /**
   * Create a new calculation.
   * 
   * @param params
   *          the parameters
   * @param compare
   *          the aggregation function to compare with
   */
  public Calculation(final CalculationParameters params,
      final IAggregationFunction compare) {
    super();
    int i, j;

    this.m_params = params;
    this.m_compare = compare;
    this.m_stepCnt = params.getStepsPerTest();
    this.m_varCnt = params.getVariableCount();
    this.m_vmCnt = params.getVMCount();
    this.m_vars = new double[this.m_vmCnt][this.m_varCnt];
    this.m_vars2 = new double[this.m_vmCnt][this.m_varCnt];

    i = params.getTestCount();
    j = this.m_stepCnt;
    this.m_values = new double[i][j][this.m_vmCnt];
    this.m_variances = new double[i][j];
    this.m_targets = new double[i][j];
    this.m_random = new Randomizer();
    // initializeValues(this.m_values, this.m_targets, this.m_variances,
    // compare, params.isConstant(), new Long(1));
    CCS.add(this);

    j = 0;
    for (i = 1; i < this.m_vmCnt; i <<= 1) {
      j++;
    }

    this.m_maxDist = j;
  }

  /**
   * compute the step count
   * 
   * @param c
   *          the parameters
   * @return the step count
   */
  static final int getStepCnt(final CalculationParameters c) {
    int i;
    i = (c.getVMCount() + 2);
    return (i * i);
  }

  /**
   * Obtain the value of a variable
   * 
   * @param vm
   *          the vm
   * @param index
   *          the variable index
   * @return the variable value
   */
  public double getVariable(final int vm, final int index) {
    return this.m_vars[vm][Mathematics.modulo(index, this.m_varCnt)];
  }

  /**
   * Set the value of a variable
   * 
   * @param vm
   *          the vm
   * @param index
   *          the variable index
   * @param value
   *          the new value
   */
  public void setVariable(final int vm, final int index, final double value) {
    this.m_vars[vm][Mathematics.modulo(index, this.m_varCnt)] = value;
  }

  /**
   * Obtain the variable count.
   * 
   * @return the variable count
   */
  public int getVariableCount() {
    return this.m_varCnt;
  }

  /**
   * Obtain the virtual machine count
   * 
   * @return the virtual machine count
   */
  public int getVMCount() {
    return this.m_vmCnt;
  }

  /**
   * Perform the vm calculations
   * 
   * @param program
   *          the program
   */
  private final void step(final AggregationNetProgram program) {
    int i, si, id, l, c;
    double[][] d1, d2;
    double[] z, z2;

    d1 = this.m_vars;
    d2 = this.m_vars2;

    id = this.m_index;
    si = this.m_stepIdx;

    z = this.m_values[id][si];
    for (i = (z.length - 1); i >= 0; i--) {
      d1[i][0] = z[i];
    }

    l = d1[0].length;
    c = 0;
    for (i = (this.m_vmCnt - 1); i >= 0; i--) {
      z = d1[i];
      z2 = d2[i];
      program.m_root.compute(z, this);
      if (Double.compare(z[1], z2[1]) != 0)
        c++;
      System.arraycopy(z, 0, z2, 0, l);
    }

    this.m_cc += c;
    if (c > 0)
      this.m_cs++;

    this
        .updateValues(d1, this.m_variances[id][si], this.m_targets[id][si]);

    this.exchangeData(program, d1, d2);
    this.m_vars2 = d1;
    this.m_vars = d2;

    this.m_stepIdx++;
  }

  /**
   * update the internal values
   * 
   * @param v
   *          the current values
   * @param t
   *          the destination
   * @param variance
   *          the inter-value variance
   */
  void updateValues(final double[][] v, final double variance,
      final double t) {
    double sum, sumSqr, sqrError, error, value, divT;
    int i, a, b;

    sum = 0;
    sumSqr = 0;
    sqrError = 0;
    divT = (1.0d / variance);
    error = 0;
    a = this.m_above;
    b = this.m_below;

    for (i = (v.length - 1); i >= 0; i--) {
      value = v[i][1];
      if (value > t)
        a++;
      else if (value < t)
        b++;
      sum += value;
      sumSqr += (value * value);
      value = (value - t);
      error += Math.abs(value);
      value *= value;
      sqrError += value;

    }

    sumSqr -= ((sum * sum) / v.length);
    sumSqr *= divT;
    sqrError *= divT;
    error *= /* Math.sqrt( */divT/* ) */;

    this.m_sqrError += sqrError;
    this.m_error += error;
    this.m_var += sumSqr;
    this.m_above += a;
    this.m_below += b;
  }

  /**
   * Exchange data between the vms
   * 
   * @param program
   *          the program
   * @param src
   *          the source data
   * @param dst
   *          the destination data
   */
  private final void exchangeData(final AggregationNetProgram program,
      final double[][] src, final double[][] dst) {

    int i, m, e, j;
    final int[] in, out;
    double[] did, dod;

    m = this.m_vmCnt;

    e = (1 << (this.m_stepIdx % this.m_maxDist));
    in = program.m_in;
    out = program.m_out;

    for (i = (src.length - 1); i >= 0; i--) {
      dod = src[i];
      did = dst[(i + e) % m];

      for (j = (in.length - 1); j >= 0; j--) {
        did[in[j]] = dod[out[j]];
        // program.exchangeData(src[i], dst[(i + e) % m]);
      }
    }

    // int i, m, e;
    //
    // m = this.m_vmCnt;
    // e = ((this.m_stepIdx % (m - 1)) + 1);
    // for (i = (src.length - 1); i >= 0; i--) {
    // program.exchangeData(src[i], dst[(i + e) % m]);
    // }

  }

  /**
   * This method is called right before the simulation begins.
   * 
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  @Override
  public void beginSimulation() {
    double[][][] v;
    double[][] d1, d2;
    double[] z;
    int i;

    super.beginSimulation();
    d1 = this.m_vars;
    d2 = this.m_vars2;

    for (i = (d1.length - 1); i >= 0; i--) {
      // Arrays.fill(d1[i], 0.0d);
      Arrays.fill(d2[i], 0.0d);
    }

    // b = this.m
    i = this.m_index;
    this.m_above = 0;
    this.m_below = 0;

    v = this.m_values;
    this.m_index = (((++i) >= v.length) ? 0 : i);

    z = this.m_values[this.m_index][0];
    for (i = (z.length - 1); i >= 0; i--) {
      Arrays.fill(d1[i], 0);
      d1[i][0] = z[i];

      // Arrays.fill(d1[i], z[i]);
      this.getSimulated().m_root.init(d1[i], this);
    }

    this.m_sqrError = 0.0d;
    this.m_error = 0.0d;
    this.m_var = 0.0d;
    this.m_stepIdx = 0;
    this.m_cc = 0;
    this.m_cs = 0;
  }

  /**
   * Obtain the test count
   * 
   * @return the test count
   */
  public int getTestCount() {
    return this.m_values.length;
  }

  /**
   * Obtain the count of changes per test
   * 
   * @return the count of changes per test
   */
  public int getChangesPerTest() {
    return this.m_values[0].length;
  }

  /**
   * initialize the given value array
   * 
   * @param seed
   *          the randomizer seed
   */
  final void initializeValues(final Serializable seed) {

    int i, k, j, kkk;
    final int chg;
    double average, stddev, power, ctarget;
    boolean sign;
    double[] cvalues, ocv;
    double v, cr, s;
    StatisticInfo f;
    boolean b;
    final double[][][] values;
    final double[][] target;
    final double[][] variances;
    final IAggregationFunction compare;
    final boolean isConstant;
    Randomizer r;

    r = this.m_random;
    r.setSeed(seed);
    f = new StatisticInfo();

    values = this.m_values;
    target = this.m_targets;
    variances = this.m_variances;
    compare = this.m_compare;
    isConstant = this.m_params.isConstant();

    chg = (isConstant ? 0 : Math.max(1, values[0][0].length >> 3));
    main: for (i = (values.length - 1); i >= 0; i--) {

      sign = ((i & 1) != 0);
      power = ((i >>> 1) % POWER_CNT) - POWER_SUB;

      average = Math.pow(10, power);
      if (power >= 0)
        average = Math.rint(average);

      stddev = average * 0.1d;

      cvalues = values[i][0];
      for (k = (cvalues.length - 1); k >= 0; k--) {
        switch (r.nextInt(2)) {
        case 1: {
          v = r.nextNormal(average, stddev);
          break;
        }
        default: {
          v = average
              + ((r.nextDouble() * stddev * SQRT_12) - (stddev * SQRT_3));
          break;
        }
        }

        if (sign)
          v = -v;
        cvalues[k] = v;
        f.append(v);
      }

      ctarget = compare.computeAggregate(cvalues, cvalues.length);
      // stddev = f.getStdDev();
      if (isConstant) {
        Arrays.fill(variances[i], f.getVariance());
        Arrays.fill(values[i], cvalues);
        Arrays.fill(target[i], ctarget);
        continue main;
      }

      variances[i][0] = (stddev * stddev);
      target[i][0] = ctarget;
      stddev *= 0.3;
      for (j = 1; j < variances[i].length; j++) {
        ocv = cvalues;
        cvalues = values[i][j];
        kkk = 0;
        for (;;) {
          if (Math.abs(kkk) > 500)
            kkk = 0;
          s = stddev * Math.pow(1.1d, kkk);
          f.clear();
          b = false;
          for (k = (cvalues.length - 1); k >= 0; k--) {
            v = ocv[k];
            if (r.nextInt(chg) <= 0) {
              do {
                switch (r.nextInt(2)) {
                case 1: {
                  v = r.nextNormal((0.2 * average) + (0.8 * Math.abs(v)),
                      s);
                  break;
                }
                default: {
                  v = (0.2 * average) + (0.8 * Math.abs(v))
                      + ((r.nextDouble() * s * SQRT_12) - (s * SQRT_3));
                  break;
                }
                }

                if (sign)
                  v = -v;
              } while (v == ocv[k]);
              b = true;
            }

            f.append(v);
            cvalues[k] = v;
          }
          if (!b)
            continue;

          target[i][j] = compare.computeAggregate(cvalues, cvalues.length);
          cr = Mathematics.collate(target[i][j], target[i][j - 1]);
          if (cr < 0.001) {
            kkk++;
            continue;
          }
          if (cr > 0.01) {
            kkk--;
            continue;
          }
          break;
        }

        variances[i][j] = f.getVariance();
      }
    }

    //
    // Randomizer r;
    // int i, j, k, chg;
    // final int v2l;
    // double avg, stddev, v, ot, t, mit, mat, kkk, maxV, xxx;
    // double[][] v1;
    // double[] v2;
    // StatisticInfo f;
    // boolean sign;
    // int power, z;
    // boolean isConstant2,repeat;
    //
    // r = new Randomizer();
    // r.setDefaultSeed();
    //
    // v2l = (values[0][0].length - 1);
    // f = new StatisticInfo();
    // t = 0;
    // maxV = (0.03d * 300.0d / values[0].length);
    //
    // System.out.println("Volatility: " + maxV); //$NON-NLS-1$
    //
    // for (i = (values.length - 1); i >= 0; i--) {
    // sign = ((i & 1) != 0);
    // power = ((i >>> 1) % POWER_CNT) - POWER_SUB;
    // isConstant2 = isConstant;// (isConstant || (r.nextInt(4) <= 0));
    //
    // chg = (60 + r.nextInt(50));
    // stddev = Math.pow(10, power);
    // if (power >= 0)
    // stddev = Math.rint(stddev);
    // avg = ((r.nextDouble() + r.nextInt(10)) * stddev);
    // stddev = (r.nextDouble() * 40 * stddev);
    //
    // if (sign)
    // avg = -avg;
    // z = r.nextInt(2);
    // kkk = 0;
    // do {
    // mit = Double.POSITIVE_INFINITY;
    // mat = Double.NEGATIVE_INFINITY;
    // repeat = false;
    //
    // v1 = values[i];
    // inner: for (j = 0; j < v1.length; j++) {
    // v2 = v1[j];
    // f.clear();
    // ot = t;
    // if (j != 0) {
    // mit = Math.min(mit, t);
    // mat = Math.max(mat, t);
    // }
    // do {
    // xxx = 0;
    // do {
    // if ((xxx++) >= 100) {
    // kkk = 0;
    // xxx = 0;
    // repeat = true;
    // break inner;
    // }
    // for (k = v2l; k >= 0; k--) {
    //
    // v = ((j > 0) ? (v1[j - 1][k]) : avg);
    // if (j == 0) {
    // switch (z) {
    // case 0: {
    // v = r.nextNormal(avg, stddev);
    // break;
    // }
    // case 1: {
    // v = avg - stddev + (2 * stddev * r.nextDouble());
    // break;
    // }
    // }
    // } else {
    // v = (v1[j - 1][k]);
    // if ((!isConstant2) && ((r.nextInt(chg) <= 0))) {
    // if (z != 1)
    // v = r.nextNormal(v, stddev);
    // else
    // v = (v - stddev + (2 * stddev * r.nextDouble()));
    // }
    // }
    //
    // f.append(v2[k] = v);
    // }
    //
    // t = compare.compute(values[i][j]);
    // } while (Math.abs(t) <= 1e-60d);
    //
    // if (j == 0) {
    // stddev = f.getStdDev() * Math.pow(1.1d, kkk / 10.0d);
    // System.out.println(z + " " + kkk + //$NON-NLS-1$
    // " " + Math.pow(1.1d, kkk / 10)); //$NON-NLS-1$
    // v = 0;
    // } else {
    // if (ot == t) {
    // v = 0.0d;
    // } else {
    // v = Math.abs((ot - t) / ot);
    // }
    // }
    // } while ((j != 0) && ((v <= 0) || (v > maxV/* 0.03 */))
    // && (!isConstant2));
    //
    // target[i][j] = t;
    //
    // variances[i][j] = f.getVariance();
    // }
    //
    // if (mit == mat)
    // v = 1.0d;
    // else
    // v = Math.abs((mat - mit) / mit);
    //
    // if(!repeat){
    // if (v > 0.6d)
    // kkk--;
    // else if (!isConstant2 && (v < 0.25))
    // kkk++;}
    //
    // } while (repeat || ((!isConstant2) && ((v > 0.6) || (v < 0.25))));
    //
    // }
    System.out.println("init done"); //$NON-NLS-1$
  }

  /**
   * Perform <code>steps</code> simulation steps. This method returns
   * <code>true</code> per default.
   * 
   * @param steps
   *          The count of simulation steps to be performed.
   * @return <code>true</code> if and only if further simulating would
   *         possible change the state of the simulation,
   *         <code>false</code> if the simulation has come to a final,
   *         terminal state which cannot change anymore.
   * @throws IllegalStateException
   *           If this simulation is not yet running.
   * @throws IllegalArgumentException
   *           if <code>steps <= 0</code>.
   */
  @Override
  public boolean simulate(final long steps) {
    AggregationNetProgram p;
    p = this.getSimulated();
    for (int i = ((int) steps); i > 0; i--) {
      this.step(p);
    }
    return true;
  }

  /**
   * Obtain the square error
   * 
   * @return the square error
   */
  public double getSquareError() {
    return this.m_sqrError;
  }

  /**
   * Obtain the error
   * 
   * @return the error
   */
  public double getError() {
    return this.m_sqrError;
  }

  /**
   * Obtain the distortion value
   * 
   * @return the distortion value
   */
  public double getDistortion() {
    return this.m_var;
  }

  /**
   * Apply the weight to a value.
   * 
   * @param value
   *          the value
   * @param weight
   *          the weight
   * @return the value made worst according to the weight
   */
  public static final double applyWeight(final double value,
      final int weight) {
    double d;
    if (weight < MAX_WEIGHT)
      return value;
    d = Math.pow((weight - MAX_WEIGHT + 2), 0.33);
    if (value < 0)
      return (value / d);
    return (value * d);
  }

}

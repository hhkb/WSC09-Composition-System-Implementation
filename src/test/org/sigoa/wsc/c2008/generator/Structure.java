/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-03
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2008.generator.Structure.java
 * Last modification: 2007-03-03
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

package test.org.sigoa.wsc.c2008.generator;

import java.util.Arrays;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.xml.sax.SAXWriter;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * a sequence or parallel structure
 * 
 * @author Thomas Weise
 */
public class Structure extends IDObject<Structure> {
  /**
   * the randomizer
   */
  public static final IRandomizer RANDOM = Rand.RANDOM;

  /**
   * the sequence data
   */
  final List<Structure> m_data;

  /**
   * <code>true</code> if and only if this structure is sequential,
   * <code>false</code> if it is parallel
   */
  private EType m_type;

  /**
   * Create a new structure
   * 
   * @param type
   *          <code>true</code> if and only if this structure is
   *          sequential, <code>false</code> if it is parallel
   */
  public Structure(final EType type) {
    super();
    this.m_data = CollectionUtils.createList();
    this.m_type = (type != null) ? type : EType.FLOW;
  }

  /**
   * Serialize this object to xml
   * 
   * @param w
   *          the sax writer to write to
   */
  @Override
  public void toSAXWriter(final SAXWriter w) {
    int i;
    final int m;

    w.startElement(this.m_type.m_text);

    m = this.m_data.size();
    for (i = 0; i < m; i++) {
      this.m_data.get(i).toSAXWriter(w);
    }

    w.endElement();
    super.toSAXWriter(w);
  }

  // /**
  // * build a structure of the given type
  // *
  // * @param inputs
  // * the input services
  // * @param outputs
  // * the output services
  // * @param taxonomy
  // * the concept taxonomy
  // * @param sequential
  // * the type
  // * @param services
  // * the services
  // * @return the structure
  // */
  // public static final Structure buildStructure(final List<Concept>
  // inputs,
  // final List<Concept> outputs, final Concept taxonomy,
  // final boolean sequential, final int services) {
  //    
  // return buildStructure(inputs, outputs, taxonomy, sequential, services,
  // false, new List<Concept>());
  // }
  /**
   * build a structure of the given type
   * 
   * @param inputs
   *          the input services
   * @param outputs
   *          the output services
   * @param taxonomy
   *          the concept taxonomy
   * @param sequential
   *          the type
   * @param services
   *          the services
   * @return the structure
   */
  public static final Structure buildStructure(final List<Concept> inputs,
      final List<Concept> outputs, final Concept taxonomy,
      final int services) {

    return buildStructure(inputs, outputs,
        ((outputs.size() < 2) || (RANDOM.nextBoolean())), services,
        taxonomy);
  }

  /**
   * build the lists of allowed concepts of arbitrary services
   * 
   * @param allowedIn
   *          the list of allowed input concepts of arbitrary services
   * @param allowedOut
   *          the list of allowed output concepts of arbitrary services
   */
  void buildAllowed(final List<Concept> allowedIn,
      final List<Concept> allowedOut) {
    int i;

    for (i = (this.m_data.size() - 1); i >= 0; i--) {
      this.m_data.get(i).buildAllowed(allowedIn, allowedOut);
    }
  }

  /**
   * instantiate the services
   * 
   * @param allowedIn
   *          the available input parameters
   * @param allowedOut
   *          the allowed output parameters
   */
  void instantiate(final List<Concept> allowedIn,
      final List<Concept> allowedOut) {
    int i;

    for (i = (this.m_data.size() - 1); i >= 0; i--) {
      this.m_data.get(i).instantiate(allowedIn, allowedOut);
    }
  }

  /**
   * print all the services
   * 
   * @param l
   *          the service list
   */
  void getServices(final List<Service> l) {
    int i;

    for (i = (this.m_data.size() - 1); i >= 0; i--) {
      this.m_data.get(i).getServices(l);
    }
  }

  /**
   * build a structure of the given type
   * 
   * @param inputs
   *          the input services
   * @param outputs
   *          the output services
   * @param addIn
   *          additional inputs
   * @param addOut
   *          additional outputs
   * @param sequential
   *          the type
   * @param services
   *          the services
   * @param taxonomy
   * @return the structure
   */
  public static final Structure buildStructure(final List<Concept> inputs,
      final List<Concept> outputs, final boolean sequential,
      final int services, final Concept taxonomy) {
    int[] serv;
    int trials;

    if (services <= 0)
      return null;

    if (services <= 1)
      return buildService(inputs, outputs);

    trials = 100;
    do {
      if (sequential) {
        serv = distributeN(services);
      } else {
        serv = distributeN(services, RANDOM.nextInt(Math.min(services,
            outputs.size())) + 1);
      }

      if (serv == null)
        return null;
      if ((--trials) <= 0)
        return null;
    } while (sequential && (serv[serv.length - 1] > outputs.size()));

    if (sequential)
      return buildSequential(inputs, outputs, serv, taxonomy);

    return buildParallel(inputs, outputs, serv, taxonomy);
  }

  /**
   * distribute n
   * 
   * @param n
   *          the number of things to evenly distribute
   * @param s
   *          the fixed number
   * @return the distribution
   */
  private static final int[] distributeN(final int n, final int s) {
    double[] ds;
    int[] serv;
    int trials, i, avail, cur;
    double sum, x;

    if (n <= 1)
      return null;

    trials = 100;

    serv = new int[s];
    ds = new double[s];

    // compute how the `services' are distributed amongst
    // the s stages
    distrLoop: for (;;) {
      Arrays.fill(ds, 0);
      Arrays.fill(serv, 0);
      sum = 0d;
      for (i = (s - 1); i >= 0; i--) {
        ds[i] = x = RANDOM.nextDouble();
        sum += x;
      }
      avail = n;

      if (sum <= 0) {
        if ((trials--) < 0)
          return null;
        continue distrLoop;
      }

      sum = (1.0d / sum);

      for (i = (s - 1); i >= 0; i--) {
        serv[i] = cur = ((int) (Math.round(n * sum * ds[i])));
        avail -= cur;
        if ((cur <= 0) || (avail < 0)) {
          if ((trials--) < 0)
            return null;
          continue distrLoop;
        }
      }
      break;
    }
    serv[0] += avail;

    return serv;
  }

  /**
   * distribute n
   * 
   * @param n
   *          the number of things to evenly distribute
   * @return the distribution
   */
  private static final int[] distributeN(final int n) {
    int[] serv;
    int s, trials;
    double d;

    if (n <= 1)
      return null;

    trials = 100;

    // find the service distributions and their parameters
    for (;;) {
      // compute how many items the sequence will have (s >= 2)
      d = RANDOM.nextDouble();
      d *= d;
      s = ((n > 2) ? ((int) (d * (n - 1)) + 2) : 2);

      serv = distributeN(n, s);

      if (serv != null)
        return serv;
      if ((--trials) <= 0)
        return null;
    }

  }

  /**
   * build a structure of the given type
   * 
   * @param inputs
   *          the input services
   * @param outputs
   *          the output services
   * @param serv
   *          the service distribution
   * @param taxonomy
   * @return the structure
   */
  public static final Structure buildSequential(
      final List<Concept> inputs, final List<Concept> outputs,
      final int[] serv, final Concept taxonomy) {
    List<Concept> newIn, newOut;
    Structure resS, subS;
    int doneOut, i;
    Concept pp;

    newOut = CollectionUtils.createList();
    newIn = CollectionUtils.createList();
    resS = new Structure(EType.SEQUENCE);

    // build the actual structure
    newOut.addAll(inputs);
    doneOut = 0;
    for (i = 0; i < serv.length; i++) {
      newIn.clear();
      newIn.addAll(newOut);
      newOut.clear();

      if (i < (serv.length - 1)) {
        while ((doneOut < (outputs.size() - serv[serv.length - 1] - 1))
            && (RANDOM.nextInt(3) == 0)) {
          newOut.add(outputs.get(doneOut++));
        }

        while ((newOut.size() <= Math.max(2, serv[i]))
            || (RANDOM.nextInt(3) == 0)) {
          pp = taxonomy.takeConcept();
          if (pp == null)
            return null;
          newOut.add(pp);
        }
      } else {
        Rand.addArray(newOut, outputs, doneOut, outputs.size() - doneOut);
      }

      subS = buildStructure(newIn, newOut, false, serv[i], taxonomy);
      if (subS == null)
        return null;

      resS.m_data.add(subS);
    }

    return resS;
  }

  /**
   * build a structure of the given type
   * 
   * @param inputs
   *          the input services
   * @param outputs
   *          the output services
   * @param serv
   *          the service distribution
   * @param taxonomy
   * @return the structure
   */
  public static final Structure buildParallel(final List<Concept> inputs,
      final List<Concept> outputs, final int[] serv, final Concept taxonomy) {
    int maxC, i, j, k;
    int[] conc;
    Structure ss, t;
    List<Concept> out;

    maxC = outputs.size();
    if ((maxC < 2) || (maxC < serv.length))
      return null;

    conc = distributeN(maxC, serv.length);
    if (conc == null)
      return null;

    out = CollectionUtils.createList();
    t = new Structure(EType.PARALLEL);
    j = 0;
    for (i = (serv.length - 1); i >= 0; i--) {
      out.clear();
      k = conc[i];
      Rand.addArray(out, outputs, j, k);
      j += k;

      ss = buildStructure(inputs, out, true, serv[i], taxonomy);
      if (ss == null)
        return null;
      t.m_data.add(ss);
    }

    return t;
  }

  /**
   * build a structure of the given type
   * 
   * @param inputs
   *          the input services
   * @param outputs
   *          the output services
   * @return the structure
   */
  public static final Structure buildService(final List<Concept> inputs,
      final List<Concept> outputs) {
    Abstraction a;
    // int c;

    a = new Abstraction();

    a.m_realizations.clear();
        
    a.m_input.clear();
    a.m_input.addAll(inputs);
    a.m_realInputs = a.m_input.size();

    // c = known.m_count;
    // if (c > 0) {
    // while (Rand.RANDOM.nextBoolean()) {
    // a.m_input.addIfNew(known.m_data[Rand.RANDOM.nextInt(c)]);
    // }
    // }

    a.m_output.clear();    
    a.m_output.addAll(outputs);
    a.m_realOutputs = a.m_output.size();
    // c = known.m_count;
    // if (c > 0) {
    // while (Rand.RANDOM.nextBoolean()) {
    // a.m_input.addIfNew(known.m_data[Rand.RANDOM.nextInt(c)]);
    // }
    // }

    return a;
  }

  // /**
  // * build a structure of the given type
  // *
  // * @param inputs
  // * the input services
  // * @param outputs
  // * the output services
  // * @param taxonomy
  // * the concept taxonomy
  // * @param mustUse
  // * is the usage of all inputs required?
  // * @param sequential
  // * the type
  // * @param services
  // * the services
  // * @param accIn
  // * the accumulated input parameters
  // * @return the structure
  // */
  // public static final Structure buildStructure(final List<Concept>
  // inputs,
  // final List<Concept> outputs, final Concept taxonomy,
  // final boolean sequential, final int services, final boolean mustUse,
  // final List<Concept> accIn) {
  // Abstraction a;
  // int s, i, trials, avail, cur, doneOut;
  // double sum, x;
  // double[] ds;
  // boolean mu;
  // int[] serv;
  // List<Concept> newIn, newOut, accInN;
  // Concept pp;
  // Structure resS, subS;
  //
  // if (services <= 0)
  // return null;
  //
  // // single service
  // if ((services <= 1)
  // || ((!sequential) && (Math.min(services, outputs.m_count) <= 1))) {
  // a = new Abstraction();
  // if (mustUse) {
  // a.m_input.addSimpleList(inputs);
  // } else {
  // i = 0;
  // do {
  // do {
  // pp = inputs.m_data[RANDOM.nextInt(inputs.m_count)];
  // } while (a.m_input.contains(pp));
  // a.m_input.add(pp);
  // i++;
  // } while ((i < inputs.m_count) && RANDOM.nextBoolean());
  // }
  //
  // a.m_output.addSimpleList(outputs);
  //
  // return a;
  // }
  //
  // trials = 10000;
  //
  // newOut = new List<Concept>();
  // resS = new Structure(sequential ? EType.SEQUENCE : EType.PARALLEL);
  //
  // // create a service sequence
  // if (sequential) {
  // newIn = new List<Concept>();
  // accInN = new List<Concept>();
  // accInN.addSimpleList(accIn);
  //
  // for (;;) {
  // resS.m_data.clear();
  //
  // // find the service distributions and their parameters
  // largeLoop: for (;;) {
  // // compute how many items the sequence will have (s >= 2)
  // s = ((services > 2) ? (RANDOM.nextInt(services - 1) + 2) : 2);
  //
  // serv = new int[s];
  // ds = new double[s];
  //
  // // compute how the `services' are distributed amongst
  // // the s stages
  // distrLoop1: for (;;) {
  // Arrays.fill(ds, 0);
  // Arrays.fill(serv, 0);
  // sum = 0d;
  // for (i = (s - 1); i >= 0; i--) {
  // ds[i] = x = RANDOM.nextDouble();
  // sum += x;
  // }
  // avail = services;
  //
  // if (sum <= 0) {
  // if ((trials--) < 0)
  // return null;
  // if (RANDOM.nextBoolean())
  // continue distrLoop1;
  // continue largeLoop;
  // }
  //
  // sum = (1.0d / sum);
  //
  // for (i = (s - 1); i >= 0; i--) {
  // serv[i] = cur = ((int) (Math.round(services * sum * ds[i])));
  // avail -= cur;
  // if ((cur <= 0) || (avail < 0)) {
  // if ((trials--) < 0)
  // return null;
  // if (RANDOM.nextBoolean())
  // continue distrLoop1;
  // continue largeLoop;
  // }
  // }
  // break;
  // }
  // serv[0] += avail;
  // break;
  // }
  // // service distribution found
  //
  // newIn.clear();
  // newOut.clear();
  // accInN.clear();
  // accInN.addSimpleList(inputs);
  //
  // // build the actual structure
  // newOut.addSimpleList(inputs);
  // doneOut = 0;
  // for (i = 0; i < serv.length; i++) {
  // newIn.clear();
  // newIn.addSimpleList(newOut);
  // do {
  // pp = accInN.m_data[RANDOM.nextInt(accInN.m_count)];
  // if (!(newIn.contains(pp))) {
  // newIn.add(pp);
  // }
  // } while (RANDOM.nextBoolean());
  //
  // newOut.clear();
  // if (i < (serv.length - 1)) {
  //
  // while ((doneOut < (outputs.m_count - 1))
  // && (RANDOM.nextInt(3) == 0)) {
  // newOut.add(outputs.m_data[doneOut++]);
  // }
  //
  // while ((newOut.m_count <= 0) || (RANDOM.nextInt(3) == 0)) {
  // pp = taxonomy.takeConcept();
  // if (pp == null)
  // return null;
  // newOut.add(pp);
  // }
  // } else {
  // newOut.addArray(outputs.m_data, doneOut, outputs.m_count
  // - doneOut);
  // }
  //
  // mu = ((i <= 0) ? mustUse : true);
  // // : (resS.m_data.get(i - 1).getClass() != Abstraction.class));
  //
  // subS = buildStructure(newIn, newOut, taxonomy, false, serv[i],
  // mu, accInN);
  // if (subS == null)
  // return null;
  //
  // resS.m_data.add(subS);
  // accInN.addSimpleList(newOut);
  // }
  //
  // return resS;
  //
  // }
  //
  // }
  //
  // // /
  // // parallel!
  // // //
  //
  // for (;;) {
  // resS.m_data.clear();
  //
  // // find the service distributions and their parameters
  // largeLoop: for (;;) {
  // avail = Math.min(services, outputs.m_count);
  // if (avail <= 1)
  // return null;
  // // compute how many items the parallelization will have (s >= 2)
  // s = ((avail > 2) ? (RANDOM.nextInt(avail - 1) + 2) : 2);
  //
  // serv = new int[s];
  // ds = new double[s];
  //
  // // compute how the `services' services are distributed amongst
  // // the s parallel elements
  // distrLoop1: for (;;) {
  // Arrays.fill(ds, 0);
  // Arrays.fill(serv, 0);
  // sum = 0d;
  // for (i = (s - 1); i >= 0; i--) {
  // ds[i] = x = RANDOM.nextDouble();
  // sum += x;
  // }
  // avail = services;
  //
  // if (sum <= 0) {
  // if ((trials--) < 0)
  // return null;
  // if (RANDOM.nextBoolean())
  // continue distrLoop1;
  // continue largeLoop;
  // }
  //
  // sum = (1.0d / sum);
  //
  // for (i = (s - 1); i >= 0; i--) {
  // serv[i] = cur = ((int) (Math.round(services * sum * ds[i])));
  // avail -= cur;
  // if ((cur <= 0) || (avail < 0)) {
  // if ((trials--) < 0)
  // return null;
  // if (RANDOM.nextBoolean())
  // continue distrLoop1;
  // continue largeLoop;
  // }
  // }
  // break;
  // }
  // serv[0] += avail;
  //
  // break;
  // }
  //
  // // build the parallel structures
  // avail = 0;
  // for (i = 0; i < serv.length; i++) {
  // newOut.clear();
  // do {
  // newOut.add(outputs.m_data[avail++]);
  // } while ((avail < outputs.m_count) && //
  // ((i >= (serv.length - 1)) || //
  // ((serv.length - i) > (1 + outputs.m_count - avail) && RANDOM
  // .nextBoolean())));
  //
  // subS = buildStructure(inputs, newOut, taxonomy, true, serv[i],
  // mustUse, accIn);
  //
  // if (subS == null)
  // return null;
  // resS.m_data.add(subS);
  // }
  //
  // return resS;
  // }
  //
  // }

  /**
   * xx
   */
  static enum EType {
    /**
     * 
     */
    SEQUENCE("sequence"), //$NON-NLS-1$
    /**
     * 
     */
    PARALLEL("parallel"), //$NON-NLS-1$
    /**
     * 
     */
    FLOW("flow");//$NON-NLS-1$

    /**
     * the text
     */
    final String m_text;

    /**
     * @param s
     */
    EType(final String s) {
      this.m_text = s;
    }
  }
}

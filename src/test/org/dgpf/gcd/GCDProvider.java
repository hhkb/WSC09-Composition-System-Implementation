/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.GCDProvider.java
 * Last modification: 2007-09-22
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

package test.org.dgpf.gcd;

import java.io.File;
import java.io.Serializable;

import org.dgpf.vm.base.IHostedVirtualMachineFactory;
import org.dgpf.vm.base.VirtualMachine;
import org.dgpf.vm.base.VirtualMachineParameters;
import org.dgpf.vm.base.VirtualMachineProgram;
import org.dgpf.vm.base.VirtualMachineProvider;
import org.sfc.io.CanonicalFile;
import org.sfc.io.TextWriter;
import org.sfc.utils.Log;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * A uniform way to create sample data for ggd algorithms
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the program type
 * @author Thomas Weise
 */
public class GCDProvider<MT extends Serializable, PT extends VirtualMachineProgram<MT>>
    extends VirtualMachineProvider<MT, PT> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the first data sequence
   */
  public final int[] m_a;

  /**
   * the second data sequence
   */
  public final int[] m_b;

  /**
   * the result data sequence
   */
  public final int[] m_result;

  /**
   * the test series log
   */
  private final TextWriter m_seriesLog;

  /**
   * the old average r
   */
  private double m_r2;

  /**
   * Create a new gcd data record
   * 
   * @param parameters
   *          the virtual machine parameters
   * @param factory
   *          the virtual machine factory
   * @param clazz
   *          the class of virtual machines to be created
   * @param directory
   *          the directory to log to
   * @param scenarioCount
   *          the scenarioCount
   * @param randomizeScenarios
   *          are the scenarios randomized
   */
  public GCDProvider(
      final VirtualMachineParameters<MT> parameters,
      final IHostedVirtualMachineFactory<MT, PT, GCDProvider<MT, PT>, ?> factory,
      final Class<? extends VirtualMachine<MT, PT>> clazz,
      final CanonicalFile directory, final boolean randomizeScenarios,
      final int scenarioCount) {
    super(parameters, factory, clazz, randomizeScenarios, scenarioCount);

    File f;
    TextWriter t;

    this.m_a = new int[scenarioCount];
    this.m_b = new int[scenarioCount];
    this.m_result = new int[scenarioCount];

    f = new File(directory, "test_cases.txt"); //$NON-NLS-1$
    t = null;
    try {
      if (f.createNewFile())
        t = new TextWriter(f);
    } catch (Throwable tt) {
      Log.getLog().log(tt);
    }
    this.m_seriesLog = t;

    this.m_r2 = 1d;
  }

  /**
   * Compute a scaled random number
   * 
   * @param r
   *          the randomizer
   * @param max
   *          the maximum
   * @return the number
   */
  private static final int scaledRandom(final IRandomizer r, final int max) {
    return ((int) (Math.exp((r.nextDouble() * Math.log(max)))));
  }

  /**
   * compare two doubles
   * 
   * @param d1
   *          the first one
   * @param d2
   *          the second one
   * @return whether they are equal enough or not
   */
  private static boolean compare(final double d1, final double d2) {

    if ((d1 <= 0) || (d2 <= 0))
      return true;
    if (Double.compare(d1, d2) == 0)
      return true;

    return (Math.abs(d1 - d2) / Math.min(d1, d2) < 0.7);

  }

  /**
   * Create the scenarios
   * 
   * @param random
   *          the randomizer to be used for scenario creation
   * @param iteration
   *          the index of the iteration
   */
  @Override
  protected void createScenarios(final IRandomizer random,
      final long iteration) {
    int i, x, y, z, t, v;
    final int[] a, b, rr;
    final TextWriter tt;
    double a2, a3, b2, b3, r2, r3, s2, s3;
    double ad, bd, rd, sd;
    final int len;
    final long tx;

    a = this.m_a;
    b = this.m_b;
    rr = this.m_result;
    len = a.length;

    tx = System.currentTimeMillis();

    // first initialization of the test sets
    a2 = 0d;
    a3 = 0d;
    b2 = 0d;
    b3 = 0d;
    r2 = 0d;
    r3 = 0d;
    s3 = 0d;
    s2 = 0d;
    for (i = (len - 1); i >= 0; i--) {
      t = 7 + scaledRandom(random, 100000);

      do {
        x = t * scaledRandom(random, Integer.MAX_VALUE / t);
        y = t * scaledRandom(random, Integer.MAX_VALUE / t);
        z = gcd(x, y);
      } while ((z < t) || (z == x) || (z == y) || (x == y));

      a2 += x;
      a3 += ((long) x) * ((long) x);

      b2 += y;
      b3 += ((long) y) * ((long) y);

      r2 += z;
      r3 += ((long) z) * ((long) z);

      t = gcdSteps(x, y);
      s2 += t;
      s3 += (t * t);

      a[i] = x;
      b[i] = y;
      rr[i] = z;
    }
    a2 /= len;
    a3 /= len;
    ad = a3 - (a2 * a2);
    b2 /= len;
    b3 /= len;
    bd = b3 - (b2 * b2);

    r2 /= len;
    r3 /= len;
    rd = r3 - (r2 * r2);

    s2 /= len;
    s3 /= len;
    sd = s3 - (s2 * s2);
    v = 0;

    // refining: changing one element until all data fits
    while ((//
        ((len > 1) && //
        ((ad < 4e17) || (bd < 4e17) || (rd < 1.2e12) || (sd < 7.5d))) || //
        compare(r2, this.m_r2))//
        && ((System.currentTimeMillis() - tx) < 60000l)) {

      t = 7 + scaledRandom(random, 100000);

      do {
        x = t * scaledRandom(random, Integer.MAX_VALUE / t);
        y = t * scaledRandom(random, Integer.MAX_VALUE / t);
        z = gcd(x, y);
      } while ((z < t) || (z == x) || (z == y) || (x == y));

      i = ((v++) % len);
      a[i] = x;
      b[i] = y;
      rr[i] = z;

      for (i = (len - 1); i >= 0; i--) {

        a2 += a[i];
        a3 += ((long) a[i]) * ((long) a[i]);

        b2 += b[i];
        b3 += ((long) b[i]) * ((long) b[i]);

        r2 += rr[i];
        r3 += ((long) rr[i]) * ((long) rr[i]);

        t = gcdSteps(a[i], b[i]);
        s2 += t;
        s3 += (t * t);
      }

      a2 /= len;
      a3 /= len;
      ad = a3 - (a2 * a2);
      b2 /= len;
      b3 /= len;
      bd = b3 - (b2 * b2);

      r2 /= len;
      r3 /= len;
      rd = r3 - (r2 * r2);

      s2 /= len;
      s3 /= len;
      sd = s3 - (s2 * s2);

    }

    this.m_r2 = r2;

    tt = this.m_seriesLog;
    if (tt != null) {
      if (iteration > 0) {
        tt.ensureNewLine();
        tt.newLine();
      }
      tt.write("Iteration: "); //$NON-NLS-1$
      tt.writeLong(iteration);
      tt.ensureNewLine();
      for (i = 0; i < 50; i++)
        tt.writeChar('=');
      tt.ensureNewLine();

      for (i = 0; i < a.length; i++) {
        tt.ensureNewLine();
        tt.writeCSVSeparator();
        tt.writeInt(a[i]);
        tt.writeCSVSeparator();
        tt.writeInt(b[i]);
        tt.writeCSVSeparator();
        tt.writeInt(rr[i]);
      }
      tt.ensureNewLine();
      for (i = 0; i < 50; i++)
        tt.writeChar('-');
      tt.ensureNewLine();
      tt.write("avg:"); //$NON-NLS-1$
      tt.writeCSVSeparator();
      tt.writeDouble(a2);
      tt.writeCSVSeparator();
      tt.writeDouble(b2);
      tt.writeCSVSeparator();
      tt.writeDouble(r2);
      tt.ensureNewLine();
      for (i = 0; i < 50; i++)
        tt.writeChar('-');
      tt.ensureNewLine();
      tt.write("var:"); //$NON-NLS-1$
      tt.writeCSVSeparator();
      tt.writeDouble(a3);
      tt.writeCSVSeparator();
      tt.writeDouble(b3);
      tt.writeCSVSeparator();
      tt.writeDouble(r3);
      tt.ensureNewLine();
      tt.write("time:"); //$NON-NLS-1$
      tt.writeCSVSeparator();
      tt.writeTime(System.currentTimeMillis() - tx, true);

      tt.ensureNewLine();
      tt.write("steps avg/var:"); //$NON-NLS-1$
      tt.writeCSVSeparator();
      tt.writeDouble(s2);
      tt.writeCSVSeparator();
      tt.writeDouble(sd);

      tt.flush();
    }

  }

  /**
   * compute the gcd
   * 
   * @param a
   *          the first variable
   * @param b
   *          the second variable
   * @return the result
   */
  static final int gcd(final int a, final int b) {
    int x, y, t;

    if ((a <= 0) || (b <= 0))
      return 1;

    x = a;
    y = b;
    while (y != 0) {
      t = y;
      y = (x % y);
      x = t;
    }

    return x;
  }

  /**
   * compute the gcd
   * 
   * @param a
   *          the first variable
   * @param b
   *          the second variable
   * @return the result
   */
  private static final int gcdSteps(final int a, final int b) {
    int x, y, t, s;

    if ((a <= 0) || (b <= 0))
      return 1;

    s = 0;
    x = a;
    y = b;
    while (y != 0) {
      s++;
      t = y;
      y = (x % y);
      x = t;
    }

    return s;
  }

  // /**
  // * compute the gcd
  // *
  // * @param i
  // * the first variable
  // * @return the result
  // */
  // private static final int fibonacci(final int i) {
  // int a, b, x, t;
  //
  // if (i <= 1)
  // return 1;
  // a = 1;
  // b = 2;
  // for (x = i; x > 1; x--) {
  // t = a + b;
  // a = b;
  // b = t;
  // }
  //
  // return b;
  // }

  /**
   * finalize this object
   * 
   * @throws Throwable
   *           probably
   */
  @Override
  protected void finalize() throws Throwable {
    if (this.m_seriesLog != null)
      this.m_seriesLog.close();
    super.finalize();
  }
}

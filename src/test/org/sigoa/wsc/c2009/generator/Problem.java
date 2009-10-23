/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-02-13
 * Creator          : Thomas Weise
 * Original Filename: wsc.c2008.generator.Problem.java
 * Last modification: 2008-02-13
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

package test.org.sigoa.wsc.c2009.generator;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.io.CanonicalFile;
import org.sfc.io.IO;
import org.sfc.utils.ErrorUtils;
import org.sfc.xml.sax.SAXWriter;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The problem builder creates a problem
 *
 * @author Thomas Weise
 */
public class Problem extends XMLObject {
  /**
   * the randomizer
   */
  private static final IRandomizer RANDOM = Rand.RANDOM;

  /**
   * the inputs
   */
  static final String INPUTS = "inputs";//$NON-NLS-1$

  /**
   * the outputs
   */
  static final String OUTPUTS = "outputs"; //$NON-NLS-1$

  /**
   * the taxonomy
   */
  private final Concept m_tax;

  /**
   * the structures
   */
  private final Structure[] m_struc;

  /**
   * the input
   */
  private final List<Concept> m_in;

  /**
   * the output
   */
  private final List<Concept> m_out;

  /**
   * the list of allowed inputs
   */
  private final List<Concept> m_allowedIn;

  /**
   * the list of allowed outputs
   */
  private final List<Concept> m_allowedOut;

  /**
   * the service file
   */
  private SAXWriter m_serviceFile;

  /**
   * the structure
   */
  private final int[] m_sols;

  /**
   * Create a new problem builder
   *
   * @param tax
   *          the taxonomy
   * @param d
   *          the depth
   * @param allowed
   *          the allowed inputs
   */
  Problem(final int d[], final Concept tax, final List<Concept> allowed) {
    super();

    this.m_struc = new Structure[d.length];
    this.m_tax = tax;

    this.m_sols = d;

    this.m_in = CollectionUtils.createList();
    this.m_out = CollectionUtils.createList();

    this.m_allowedIn = CollectionUtils.createList();
    this.m_allowedIn.addAll(allowed);
    this.m_allowedOut = CollectionUtils.createList();
    this.m_allowedOut.addAll(allowed);
  }

  /**
   * build the problem
   *
   * @return <code>true</code> if everything went ok
   */
  private final boolean build() {
    int i;
    int[] sols;
    Concept tax, x;

    sols = this.m_sols;
    i = sols.length;

    tax = this.m_tax.copy();
    this.m_in.clear();
    this.m_out.clear();

    for (i = RANDOM.nextInt(10); i >= 0; i--) {
      x = tax.takeConcept();
      if (x == null)
        return false;
      this.m_in.add(x);
    }

    for (i = RANDOM.nextInt(4); i >= 0; i--) {
      x = tax.takeConcept();
      if (x == null)
        return false;
      this.m_out.add(x);
    }

    for (i = (this.m_sols.length - 1); i >= 0; i--) {

      this.m_struc[i] = Structure.buildStructure(this.m_in, this.m_out,
          tax, /* RANDOM.nextBoolean(), */sols[i]);
      if (this.m_struc[i] == null)
        return false;
    }

    return true;
  }

  /**
   * Create a new problem
   *
   * @param taxonomy
   *          the taxonomy
   * @param depth
   *          the depth
   * @return the problem
   */
  public static Problem buildProblem(final Concept taxonomy,
      final int depth[]) {
    Problem p;
    List<Concept> c;

    c = CollectionUtils.createList();
    taxonomy.instancesToList(c);

    p = new Problem(depth, taxonomy, c);
    if (p.build()) {
      p.instantiate();
      return p;
    }

    return null;
  }

  /**
   * instantiate the problem
   */
  private final void instantiate() {
    int i;

    for (i = (this.m_struc.length - 1); i >= 0; i--) {
      this.m_struc[i].buildAllowed(this.m_allowedIn, this.m_allowedOut);
    }
    for (i = (this.m_out.size() - 1); i >= 0; i--) {
      this.m_out.get(i).removeAllInstances(this.m_allowedOut);
    }

    for (i = (this.m_struc.length - 1); i >= 0; i--) {
      this.m_struc[i].instantiate(this.m_allowedIn, this.m_allowedOut);
    }
  }

  /**
   * serialize
   *
   * @param dir
   *          the directory to serialize to
   */
  public final void serialize(final Object dir) {
    CanonicalFile f;
    SAXWriter w;

    f = IO.getFile(dir);
    f.mkdirs();
    try {
      this.m_serviceFile = w = new SAXWriter(new File(f, "services.xml"));//$NON-NLS-1$
      w.startDocument();
      w.startElement("services");//$NON-NLS-1$

      w = new SAXWriter(new File(f, "problem.xml")); //$NON-NLS-1$

      this.toSAXWriter(w);

      w.release();
    } catch (Throwable t) {
      ErrorUtils.onError(t);
    }
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

    w.startDocument();
    w.startElement("problemStructure"); //$NON-NLS-1$

    w.startElement("task"); //$NON-NLS-1$
    w.startElement("provided");//$NON-NLS-1$
    for (i = (this.m_in.size() - 1); i >= 0; i--) {
      this.m_in.get(i).randomInstance().toSAXWriter(w, false);
    }
    w.endElement();

    w.startElement("wanted");//$NON-NLS-1$
    for (i = (this.m_out.size() - 1); i >= 0; i--) {
      this.m_out.get(i).randomDirectInstance().toSAXWriter(w, false);
    }
    w.endElement();
    w.endElement();

    w.startElement("solutions");//$NON-NLS-1$
    for (i = 0; i < this.m_struc.length; i++) {
      w.startElement("solution");//$NON-NLS-1$
      this.m_struc[i].toSAXWriter(w);
      w.endElement();
    }

    w.endElement();

    w.endElement();

    w.endElement();
    w.endDocument();

    super.toSAXWriter(w);
  }

  /**
   * Generate a random service
   */
  public void generateService() {
    Service v;
    int c, mc;
    Concept q;

    v = new Service();
    mc = this.m_allowedIn.size();
    c = (1 + ((int) (Math.abs(RANDOM.nextNormal(5, 2)))));
    for (; c > 0; c--) {
      q = this.m_allowedIn.get(RANDOM.nextInt(mc));
      if (!(v.m_in.contains(q)))
        v.m_in.add(q);
      else
        c++;
    }

    mc = this.m_allowedOut.size();
    c = (1 + ((int) (Math.abs(RANDOM.nextNormal(5, 2)))));
    for (; c > 0; c--) {
      q = this.m_allowedOut.get(RANDOM.nextInt(mc));
      if ((!(v.m_out.contains(q))) && (!(v.m_in.contains(q)))) {
        v.m_out.add(q);
      } else
        c++;
    }

    // if (w == null)
    // v.serialize(this.m_serviceDir);
    // else
    v.toSAXWriter(this.m_serviceFile, true);
    this.m_serviceFile.flush();
  }

  /**
   * Generate the given number of services
   *
   * @param c
   *          the number of services
   */
  public void generateServices(final int c) {
    int i, bi;
    List<Service> all;
    int[] idx;

    all = CollectionUtils.createList();
    for (i = (this.m_struc.length - 1); i >= 0; i--) {
      this.m_struc[i].getServices(all);
    }

    i = all.size();
    idx = new int[i];
    for (--i; i >= 0; i--)
      idx[i] = RANDOM.nextInt(c);
    Arrays.sort(idx);

    bi = 0;
    try {
      for (i = 0; i < c; i++) {
        this.generateService();
        while ((bi < idx.length) && (i >= idx[bi])) {
          all.get(bi++).toSAXWriter(this.m_serviceFile, true);
        }
      }

      this.m_serviceFile.endElement();
      this.m_serviceFile.endDocument();
      this.m_serviceFile.close();
    } catch (Throwable t) {
      ErrorUtils.onError(t);
    }

  }

  /**
   * @param out
   */
  public final void serializeServiceFile(final Object out) {
    SAXWriter w;
    try {
      this.m_serviceFile = w = new SAXWriter(out);
      w.startDocument();w.startElement("services");//$NON-NLS-1$
    } catch (Throwable t) {
      ErrorUtils.onError(t);
    }
  }

  /**
   * @param out
   */
  public final void serializeProblemFile(final Object out) {
    SAXWriter w;
    try {
      w = new SAXWriter(out);
      this.toSAXWriter(w);
      w.release();
    } catch (Throwable t) {
      ErrorUtils.onError(t);
    }
  }

}

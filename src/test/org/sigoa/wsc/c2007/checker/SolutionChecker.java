/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-07-20
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.checker.SolutionChecker.java
 * Last modification: 2007-07-20
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

package test.org.sigoa.wsc.c2007.checker;

import org.sfc.io.TextWriter;

import test.org.sigoa.wsc.c2007.challenge.ServiceList2;
import test.org.sigoa.wsc.c2007.composition.Composer;
import test.org.sigoa.wsc.c2007.kb.Concept;
import test.org.sigoa.wsc.c2007.kb.ConceptList;
import test.org.sigoa.wsc.c2007.kb.Service;
import test.org.sigoa.wsc.c2007.kb.ServiceList;


/**
 * the solution checker
 *
 * @author Thomas Weise
 */
public class SolutionChecker {

  /**
   * Check the given solution
   *
   * @param s
   *          the solution
   * @param w
   *          the text writer to print the output to
   */
  public static final void check(final Solution s, final TextWriter w) {
    ConceptList in, out, i2, o2;
    int i, j;
    ServiceList2 ss;
    ServiceList x;
    long t;

    w.write("==========================================================="); //$NON-NLS-1$
    w.newLine();
    w.write("Solution Checker by Steffen Bleul and Thomas Weise"); //$NON-NLS-1$
    w.newLine();
    w.write("                    University of Kassel"); //$NON-NLS-1$
    w.newLine();
    w.write("                    "); //$NON-NLS-1$
    w.newLine();
    w.writeTime(t = System.currentTimeMillis(), false);
    w.newLine();
    w.write("==========================================================="); //$NON-NLS-1$
    w.newLine();
    w.newLine();
    w.write("begin checking solution for challenge " + s.m_name); //$NON-NLS-1$
    w.newLine();
    w.newLine();

    in = s.m_ch.getIn();
    out = s.m_ch.getOut();

    w.write("known input concepts  : ");//$NON-NLS-1$
    for (i = 0; i < in.m_count; i++) {
      if (i > 0)
        w.write("                        ");//$NON-NLS-1$
      w.write(in.m_data[i].toString());
      w.newLine();

    }

    w.newLine();
    w.write("wanted output concepts: ");//$NON-NLS-1$
    for (i = 0; i < out.m_count; i++) {
      if (i > 0)
        w.write("                        ");//$NON-NLS-1$
      w.write(out.m_data[i].toString());
      w.newLine();
    }

    ss = new ServiceList2(-1);
    buildList(s.m_services, ss, new ServiceList(0));

    w.formatOn();
    w.incIndent();

    for (i = 0; i < ss.m_count; i++) {
      w.newLine();
      w.newLine();

      w.write("begin checking ");//$NON-NLS-1$
      w.writeInt(i + 1);
      w.write(". single solution"); //$NON-NLS-1$
      w.newLine();

      w.write("   ["); //$NON-NLS-1$
      x = ss.m_data[i];
      for (j = 0; j < x.m_count; j++) {
        if (j > 0)
          w.write("->"); //$NON-NLS-1$
        w.write(x.m_data[j].toString());
      }
      w.write(']');
      w.newLine();

      i2 = new ConceptList(in.m_count);
      i2.assign(in);
      o2 = new ConceptList(out.m_count);
      o2.assign(out);

      doCheck(x, i2, o2, w);

      w.ensureNewLine();
      w.write("done checking ");//$NON-NLS-1$
      w.writeInt(i + 1);
      w.write(". single solution"); //$NON-NLS-1$
      w.newLine();
    }

    w.decIndent();

    w.newLine();
    w.newLine();
    w.write("done solution for challenge " + s.m_name + //$NON-NLS-1$
        " after ");//$NON-NLS-1$
    w.writeTime(System.currentTimeMillis() - t, true);
    w.newLine();
    w.write("==========================================================="); //$NON-NLS-1$
  }

  /**
   * Check a single combo
   *
   * @param combo
   *          the combo
   * @param known
   *          the known concepts
   * @param wanted
   *          the wanted concepts
   * @param w
   *          the text writer
   */
  private static final void doCheck(final ServiceList combo,
      final ConceptList known, final ConceptList wanted, final TextWriter w) {

    int v, i, j, k;
    Service s;
    boolean b;
    Concept[] in;
    Concept ic, kc;

    w.incIndent();
    for (v = 0; v < combo.m_count; v++) {
      s = combo.m_data[v];
      w.ensureNewLine();
      w.newLine();
      w.write("executing "); //$NON-NLS-1$
      w.writeObject(s);
      w.newLine();
      b = true;

      w.writeObject(s);
      w.write(" needs the following input concepts:");//$NON-NLS-1$
      w.ensureNewLine();
      w.write("        ");//$NON-NLS-1$
      for (Concept c : s.getInputs()) {
        w.write(' ');
        w.writeObject(c);
      }
      w.ensureNewLine();
      w.writeObject(s);
      w.write(" provides the following output concepts:");//$NON-NLS-1$
      w.ensureNewLine();
      w.write("        ");//$NON-NLS-1$
      for (Concept c : s.getOutputs()) {
        w.write(' ');
        w.writeObject(c);
      }
      w.ensureNewLine();

      b = true;

      in = s.getInputs();
      for (j = 0; j < in.length; j++) {
        ic = in[j];
        checker: {
          w.ensureNewLine();
          w.newLine();
          for (k = 0; k < known.m_count; k++) {
            kc = known.m_data[k];
            if ((ic == kc) || (ic.subsumes(kc))) {

              w.write("input concept "); //$NON-NLS-1$
              w.writeObject(ic);
              w.write(" subsumes the known concept "); //$NON-NLS-1$
              w.writeObject(kc);
              w.write('.');

              if (ic != kc) {
                w.ensureNewLine();
                w.write(" since: "); //$NON-NLS-1$
                for (; kc != ic; kc = kc.getGeneralization()) {
                  w.writeObject(kc);
                  w.write("->"); //$NON-NLS-1$
                }
                w.writeObject(ic);
              }

              break checker;
            }
          }

          w.write("concept "); //$NON-NLS-1$
          w.writeObject(ic);
          w.write(" cannot be resolved. It doesn't subsume any " + //$NON-NLS-1$
              "known concept."); //$NON-NLS-1$
          b = false;
        }
      }

      w.ensureNewLine();
      w.newLine();

      if (!b) {
        w.write("execution of "); //$NON-NLS-1$
        w.writeObject(s);
        w.write(" is not possible, since at least needed input " + //$NON-NLS-1$
            "concept is not available."); //$NON-NLS-1$
        w.ensureNewLine();
        w.write("ERROR: composition is invalid.");//$NON-NLS-1$
        w.decIndent();
        return;
      }

      w.write("execution of "); //$NON-NLS-1$
      w.writeObject(s);
      w.write(" is possible, since all needed input " + //$NON-NLS-1$
          "concepts are available."); //$NON-NLS-1$

      in = s.getOutputs();
      for (i = 0; i < in.length; i++) {
        known.add(in[i]);
      }

      w.newLine();
      w.newLine();
      w.write("list of known concepts after execution of ");//$NON-NLS-1$
      w.writeObject(s);
      if (known.m_count <= 0) {
        w.write(" is empty.");//$NON-NLS-1$
      } else {
        w.write(':');
        w.newLine();
        w.write("  ");//$NON-NLS-1$
        for (j = 0; j < known.m_count; j++) {
          w.write(' ');
          w.writeObject(known.m_data[j]);
        }
      }
      w.ensureNewLine();
      w.newLine();

      b = false;
      for (i = 0; i < in.length; i++) {
        ic = in[i];
        for (j = (wanted.m_count - 1); j >= 0; j--) {
          kc = wanted.m_data[j];
          if ((ic == kc) || kc.subsumes(ic)) {
            b = true;
            w.ensureNewLine();
            w.write("wanted concept ");//$NON-NLS-1$
            w.writeObject(kc);
            w.write(" subsumes service return concept ");//$NON-NLS-1$
            w.writeObject(ic);
            w.write(" and can therefore be removed from the wanted list.");//$NON-NLS-1$
            if (ic != kc) {
              w.ensureNewLine();
              w.write("  since"); //$NON-NLS-1$
              for (; ic != kc; ic = ic.getGeneralization()) {
                w.writeObject(ic);
                w.write("->"); //$NON-NLS-1$
              }
              w.writeObject(kc);
            }
            wanted.remove(j);
          }
        }
      }

      if (b) {
        w.newLine();
        w.newLine();
        w.write("list of wanted concepts after execution of ");//$NON-NLS-1$
        w.writeObject(s);
        w.write(':');
        w.newLine();
        w.write("  ");//$NON-NLS-1$
        for (j = 0; j < wanted.m_count; j++) {
          w.write(' ');
          w.writeObject(wanted.m_data[j]);
        }
        w.ensureNewLine();
        w.newLine();
      }
    }

    if (wanted.m_count <= 0) {
      w.write("All wanted concepts have been resolved."); //$NON-NLS-1$
      w.ensureNewLine();
      w.write("This composition is valid."); //$NON-NLS-1$
    } else {
      w.write("The wanted concepts ");//$NON-NLS-1$
      for (i = 0; i < wanted.m_count; i++) {
        if (i != 0)
          w.write(", "); //$NON-NLS-1$
        w.writeObject(wanted.m_data[i]);
      }
      w.write(" have not been resolved.");//$NON-NLS-1$
      w.ensureNewLine();
      w.write("ERROR: This composition is invalid."); //$NON-NLS-1$
    }

    w.decIndent();

  }

  /**
   * build the service list
   *
   * @param ssl
   *          the solution service list
   * @param sl
   *          the solution service list
   * @param s
   *          the service list
   */
  private static final void buildList(final ServiceList2 ssl,
      final ServiceList2 sl, final ServiceList s) {
    int i, l;
    ServiceList s2, s3;
    boolean b;
    Service xs;

    l = s.m_count;
    s2 = ssl.m_data[l];
    b = ((ssl.m_count - 1) <= l);
    for (i = (s2.m_count - 1); i >= 0; i--) {
      xs = s2.m_data[i];
      if (xs == null)
        continue;
      s3 = new ServiceList(l + 1);
      System.arraycopy(s.m_data, 0, s3.m_data, 0, l);
      s3.m_data[l] = xs;
      s3.m_count = (l + 1);
      if (b)
        sl.add(s3);
      else
        buildList(ssl, sl, s3);
      if (Composer.SINGLE && (sl.m_count > 0))
        return;
    }
  }

}

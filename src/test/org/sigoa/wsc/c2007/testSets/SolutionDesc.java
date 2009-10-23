/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-05-02 09:35:26
 * Original Filename: test.org.sigoa.wsc.c2007.testSets.data.Solution.java
 * Version          : 1.0.3
 * Last modification: 2006-05-08
 *                by: Thomas Weise
 * 
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to the Free Software
 *                    Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *                    MA 02111-1307, USA or download the license under
 *                    http://www.gnu.org/copyleft/lesser.html.
 *                    
 * Warranty         : This software is provided "as is" without any
 *                    warranty; without even the implied warranty of
 *                    merchantability or fitness for a particular purpose.
 *                    See the Gnu Lesser General Public License for more
 *                    details.
 */

package test.org.sigoa.wsc.c2007.testSets;

import java.io.File;

import org.sfc.collections.lists.SimpleList;
import org.sfc.xml.sax.SAXWriter;
import org.sigoa.refimpl.stoch.Randomizer;

/**
 * This class holds the data of a solution. Solutions are created using
 * this class.
 * 
 * @author Thomas Weise
 */
final class SolutionDesc {
  /**
   * The name counter.
   */
  static int s_c = 1;

  /**
   * The name.
   */
  final String m_name;

  /**
   * The solution data.
   */
  final ServiceDesc[][] m_services;

  /**
   * The SimpleList of the wanted concepts.
   */
  final SimpleList<ConceptDesc> m_wanted;

  /**
   * The SimpleList of the known concepts.
   */
  final SimpleList<ConceptDesc> m_provided;

  /**
   * Create a new solution of the specified depth and width.
   * 
   * @param ch
   *          The challenge-desc to realize.
   */
  public SolutionDesc(final ChallengeDesc ch) {
    super();

    ServiceDesc[][] d;
    int i, j, depth, width;

    synchronized (SolutionDesc.class) {
      this.m_name = ("C" + (s_c++)); //$NON-NLS-1$
    }

    depth = ch.m_solutionDepth;
    width = ch.m_solutionWidth;

    d = new ServiceDesc[depth][width];
    for (i = (depth - 1); i >= 0; i--) {
      for (j = (width - 1); j >= 0; j--) {
        d[i][j] = new ServiceDesc();
      }
    }

    this.m_wanted = new SimpleList<ConceptDesc>(ch.m_wanted);
    this.m_provided = new SimpleList<ConceptDesc>(ch.m_provided);
    this.m_services = d;
  }

  /**
   * Store the solutions.
   * 
   * @param dest
   *          The destination.
   * @param s
   *          The solution array.
   * @param cc
   *          The challenge array.
   */
  public static final void storeSolutions(final Object dest,
      final SolutionDesc[] s, final ChallengeDesc[] cc) {
    SAXWriter ss;
    int k, i, j;
    SolutionDesc x;
    ChallengeDesc c;

    ss = new SAXWriter(dest);
    ss.startDocument();
    ss.startElement("WSChallenge"); //$NON-NLS-1$
    ss.attribute("type",//$NON-NLS-1$ 
        "solutions"); //$NON-NLS-1$

    for (k = (s.length - 1); k >= 0; k--) {
      x = s[k];
      c = cc[k];
      ss.startElement("case"); //$NON-NLS-1$
      ss.attribute("name", x.m_name); //$NON-NLS-1$

      for (i = 0; i < c.m_solutionDepth; i++) {
        ss.startElement("serviceSpec"); //$NON-NLS-1$
        ss.attribute("name", x.m_name + '.' + (i + 1)); //$NON-NLS-1$

        for (j = 0; j < c.m_solutionWidth; j++) {
          ss.startElement("service"); //$NON-NLS-1$
          ss.attribute("name", //$NON-NLS-1$ 
              x.m_services[i][j].m_name);
          ss.endElement();
        }
      }

      for (i = c.m_solutionDepth; i > 0; i--) {
        ss.endElement();
      }

      ss.endElement();
    }

    ss.endElement();
    ss.endDocument();
    ss.release();

    ss = null;
    x = null;
    c = null;
    System.gc();
    System.gc();
    System.gc();
    System.gc();
  }

  /**
   * Create the composition query.
   * 
   * @param concepts
   *          The concept SimpleList.
   * @param ch
   *          The challenge-desc to realize.
   * @param wsddir
   *          The directory to store the wsdl files into.
   * @param parameter_count
   *          The average count of in/output parameters of services.
   * @param r
   *          The randomizer to be used.
   */
  final void query(final SimpleList<ConceptDesc> concepts,
      final ChallengeDesc ch, final File wsddir,
      final int parameter_count, final Randomizer r) {
    int i, j, st, k, m, w, p, v1, v2;
    SimpleList<ConceptDesc> prov, wanted;
    ConceptDesc c;
    ServiceDesc[][] sol;
    ConceptDesc x, c2;

    prov = new SimpleList<ConceptDesc>(ch.m_provided);
    wanted = new SimpleList<ConceptDesc>(ch.m_wanted);
    sol = this.m_services;
    st = sol.length;
    w = ch.m_wanted;
    p = ch.m_provided;

    // determine the wanted outputs
    for (i = w; i > 0; i--) {
      xx: for (;;) {
        j = r.nextInt(concepts.size());
        c = concepts.get(j);
        for (v1 = wanted.size() - 1; v1 >= 0; v1--) {
          c2 = wanted.get(v1);
          if (c2.subsumes(c) || c.subsumes(c2))
            continue xx;
        }
        break xx;
      }

      wanted.add(c);
    }

    this.m_wanted.addSimpleList(wanted);

    // determine the provided inputs
    for (i = p; i > 0; i--) {
      xx2: for (;;) {
        j = r.nextInt(concepts.size());
        c = concepts.get(j);
        for (v2 = wanted.size() - 1; v2 >= 0; v2--) {
          c2 = wanted.get(v2);
          if (c2.subsumes(c))
            continue xx2;
        }
        for (v2 = prov.size() - 1; v2 >= 0; v2--) {
          c2 = prov.get(v2);
          if (c.subsumes(c2))
            continue xx2;
        }
        break xx2;
      }

      prov.add(c);
    }
    this.m_provided.addSimpleList(prov);

    // assign the wanted outputs to the services
    k = w;
    for (i = st - 1; i >= 0; i--) {
      if (k <= 0)
        break;
      j = Math.min(k, Math.max(1, r.nextInt(k)));
      if (i <= 0)
        j = k;
      k -= j;
      for (; j > 0; j--) {
        x = wanted.remove(r.nextInt(wanted.size()));
        for (m = (sol[i].length - 1); m >= 0; m--) {
          sol[i][m].m_out.add(x.randomSpec(r));
        }
      }
    }

    // assign the known inputs to the services
    k = p;
    for (i = 0; i < st; i++) {
      if (k <= 0)
        break;
      j = Math.min(k, Math.max(1, r.nextInt(k)));
      if (i >= (st - 1))
        j = k;
      k -= j;
      for (; j > 0; j--) {
        x = prov.remove(r.nextInt(prov.size()));
        for (m = (sol[i].length - 1); m >= 0; m--) {
          sol[i][m].m_in.add(x.randomGen(r));
        }
      }
    }

    // create the service dependency
    for (i = (sol.length - 1); i > 0; i--) {
      for (j = Math.max(1, r.nextInt(parameter_count
          - sol[i][0].m_in.size())); j > 0; j--) {
        x = concepts.get(r.nextInt(concepts.size()));
        for (m = (sol[i].length - 1); m >= 0; m--) {
          sol[i][m].m_in.add(x.randomGen(r));
        }
        for (m = (sol[i - 1].length - 1); m >= 0; m--) {
          sol[i - 1][m].m_out.add(x.randomSpec(r));
        }
      }
    }

    // add some random outputs
    for (i = (sol.length - 1); i >= 0; i--) {
      for (j = (sol[i].length - 1); j >= 0; j--) {
        for (m = Math.max(1, (parameter_count - sol[i][j].m_out.size())); m >= 0; m--) {
          sol[i][j].m_out.add(concepts.get(r.nextInt(concepts.size())));
        }
      }
    }

    // store the services

    for (i = (sol.length - 1); i >= 0; i--) {
      for (j = (sol[0].length - 1); j >= 0; j--) {
        sol[i][j].storeWsdl(wsddir);
      }
    }
  }

  /**
   * Store the challenges.
   * 
   * @param dest
   *          The destination.
   * @param s
   *          The solution array.
   */
  public static final void storeChallenges(final Object dest,
      final SolutionDesc[] s) {
    SAXWriter sx;
    int i, z;
    SolutionDesc x;

    // store the solution

    sx = new SAXWriter(dest);
    sx.startDocument();
    sx.startElement("WSChallenge"); //$NON-NLS-1$

    for (z = (s.length - 1); z >= 0; z--) {
      x = s[z];

      sx.startElement("CompositionRoutine"); //$NON-NLS-1$
      sx.attribute("name", x.m_name); //$NON-NLS-1$

      sx.startElement("Provided"); //$NON-NLS-1$
      for (i = (x.m_provided.size() - 1); i > 0; i--) {
        sx.write("IPO:" + x.m_provided.get(i).m_name); //$NON-NLS-1$
        sx.write(',');
      }
      sx.write(x.m_provided.get(i).m_name);
      sx.endElement();

      sx.startElement("Resultant"); //$NON-NLS-1$
      for (i = (x.m_wanted.size() - 1); i > 0; i--) {
        sx.write("IPO:" + x.m_wanted.get(i).m_name);//$NON-NLS-1$
        sx.write(',');
      }
      sx.write(x.m_wanted.get(i).m_name);
      sx.endElement();

      sx.endElement();
    }

    sx.endElement();
    sx.endDocument();
    sx.release();
    sx = null;
    x = null;
    System.gc();
    System.gc();
    System.gc();
    System.gc();
  }
}

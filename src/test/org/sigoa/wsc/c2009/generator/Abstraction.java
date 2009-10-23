/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-04
 * Creator          : Thomas Weise
 * Original Filename: wsc.c2008.generator.Abstraction.java
 * Last modification: 2008-03-04
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

import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.xml.sax.SAXWriter;

/**
 * the abstraction
 *
 * @author Thomas Weise
 */
public class Abstraction extends Structure {
  /**
   * the inputs
   */
  final List<Concept> m_input;

  /**
   * the real inputs
   */
  int m_realInputs;

  /**
   * the real outputs
   */
  int m_realOutputs;

  /**
   * the inputs
   */
  final List<Concept> m_output;

  /**
   * the realizations
   */
  final List<Service> m_realizations;

  /**
   * Create a new abstraction
   */
  public Abstraction() {
    super(EType.SEQUENCE);
    this.m_input = CollectionUtils.createList();
    this.m_output = CollectionUtils.createList();
    this.m_realizations = CollectionUtils.createList();
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

    w.startElement("serviceDesc");//$NON-NLS-1$

    w.startElement("abstraction"); //$NON-NLS-1$

    w.startElement("input");//$NON-NLS-1$
    for (i = (this.m_input.size() - 1); i >= 0; i--) {
      this.m_input.get(i).toSAXWriter(w);
    }
    w.endElement();
    w.startElement("output");//$NON-NLS-1$
    for (i = (this.m_output.size() - 1); i >= 0; i--) {
      this.m_output.get(i).toSAXWriter(w);
    }
    w.endElement();

    w.endElement();

    w.startElement("realizations"); //$NON-NLS-1$
    for (i = (this.m_realizations.size() - 1); i >= 0; i--) {
      this.m_realizations.get(i).toSAXWriter(w);
    }
    w.endElement();

    w.endElement();
  }

  /**
   * build the lists of allowed concepts of arbitrary services
   *
   * @param allowedIn
   *          the list of allowed input concepts of arbitrary services
   * @param allowedOut
   *          the list of allowed output concepts of arbitrary services
   */
  @Override
  void buildAllowed(final List<Concept> allowedIn,
      final List<Concept> allowedOut) {
    //
     this.m_output.get(RANDOM.nextInt(this.m_realOutputs))
     .removeAllInstances(allowedOut);
//    this.m_output.get(0).removeAllInstances(allowedOut);
//    this.m_output.get(this.m_output.size() - 1).removeAllInstances(
//        allowedOut);

    //
     this.m_input.get(RANDOM.nextInt(this.m_realInputs))
     .removeAllInstances(allowedOut);
    //
    // this.m_input.get(0).removeAllInstances(allowedOut);
    // this.m_input.get(this.m_input.size() - 1).removeAllInstances(
    // allowedOut);

    super.buildAllowed(allowedIn, allowedOut);
  }

  /**
   * instantiate the services
   *
   * @param allowedIn
   *          the available input parameters
   * @param allowedOut
   *          the allowed output parameters
   */
  @Override
  void instantiate(final List<Concept> allowedIn,
      final List<Concept> allowedOut) {
    Service s;
    int i;
    Concept p;

    do {
      s = new Service();

      for (i = (this.m_input.size() - 1); i >= 0; i--) {
        Rand.addIfNew(s.m_in, this.m_input.get(i).randomDirectInstance());
      }

      for (i = (this.m_output.size() - 1); i >= 0; i--) {
        Rand.addIfNew(s.m_out, this.m_output.get(i).randomInstance());
      }

      do {
        p = allowedOut.get(RANDOM.nextInt(allowedOut.size()));
        if (!(s.m_in.contains(p))) {
          Rand.addIfNew(s.m_out, p);
        }
      } while (RANDOM.nextBoolean());

      this.m_realizations.add(s);
    } while (RANDOM.nextInt(10) > 3);

    super.instantiate(allowedIn, allowedOut);
  }

  /**
   * print all the services
   *
   * @param l
   *          the service list
   */
  @Override
  void getServices(final List<Service> l) {
    l.addAll(this.m_realizations);

    super.getServices(l);
  }
}

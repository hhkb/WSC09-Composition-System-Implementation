/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : TODO
 * Creator          : Thomas Weise
 * Original Filename: wsc.c2008.generator.Service.java
 * Last modification: TODO
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
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.io.CanonicalFile;
import org.sfc.io.IO;
import org.sfc.xml.sax.SAXWriter;

/**
 * The service class
 *
 * @author Thomas Weise
 */
public class Service extends IDObject<Service> {

  /**
   * the inputs
   */
  final List<Concept> m_in;

  /**
   * the outputs
   */
  final List<Concept> m_out;

  /**
   * Create a new service
   */
  public Service() {
    super();
    this.m_in = CollectionUtils.createList();
    this.m_out = CollectionUtils.createList();
  }

  /**
   * Append this object's textual representation to a string builder.
   *
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    sb.append("serv"); //$NON-NLS-1$
    super.toStringBuilder(sb);
  }

  /**
   * Serialize this object to xml
   *
   * @param w
   *          the sax writer to write to
   */
  @Override
  public void toSAXWriter(final SAXWriter w) {
    this.toSAXWriter(w, false);
  }

  /**
   * serialize a service
   *
   * @param w
   *          the writer
   * @param deep
   *          deep?
   */
  public void toSAXWriter(final SAXWriter w, final boolean deep) {
    int i;
    w.startElement("service"); //$NON-NLS-1$
    w.attribute("name", this.toString());//$NON-NLS-1$

    if (deep) {
      w.startElement(Problem.INPUTS);
      for (i = (this.m_in.size() - 1); i >= 0; i--) {
        this.m_in.get(i).toSAXWriter(w, false);
      }
      w.endElement();

      w.startElement(Problem.OUTPUTS);
      for (i = (this.m_out.size() - 1); i >= 0; i--) {
        this.m_out.get(i).toSAXWriter(w, false);
      }
      w.endElement();
    }

    w.endElement();
  }

  /**
   * Serialize this object to the given directory
   *
   * @param dir
   *          the directory
   */
  public void serialize(final Object dir) {
    SAXWriter w;
    CanonicalFile f;

    f = IO.getFile(dir);
    try {
      w = new SAXWriter(new File(f, this.toString() + ".xml")); //$NON-NLS-1$
      try {
        w.startDocument();
        this.toSAXWriter(w, true);
        w.endDocument();
      } finally {
        w.close();
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
}

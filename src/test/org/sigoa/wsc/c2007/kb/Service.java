/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-04-21 05:54:09
 * Original Filename: test.org.sigoa.wsc.c2007.kb.Service.java
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

package test.org.sigoa.wsc.c2007.kb;

import java.util.Arrays;

/**
 * This class is used to define a service. A service is fulle described by
 * <ol>
 * <li>Its name.</li>
 * <li>The list of its input parameter concepts.</li>
 * <li>The list of its output parameter concepts.</li>
 * </ol>
 * 
 * @author Thomas Weise
 */
public final class Service {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The name of the service.
   */
  final String m_name;

  /**
   * The input concepts.
   */
  private transient Concept[] m_in;

  /**
   * The output concepts.
   */
  transient Concept[] m_out;

  /**
   * A service that is equal to this one.
   */
  transient Service m_equal;

  /**
   * The next service in the service hash.
   */
  transient Service m_next;

  /**
   * Create a new service object.
   * 
   * @param name
   *          The name of the service object.
   * @param in
   *          The input concepts required to run the service.
   * @param out
   *          The output concepts provided by the service.
   */
  public Service(final String name, final Concept[] in, final Concept[] out) {
    super();

    this.m_name = name;
    this.m_in = in;
    this.m_out = out;

    Arrays.sort(in, 0, in.length);
    Arrays.sort(out, 0, out.length);
  }

  /**
   * Obtain the service's name.
   * 
   * @return The service's name.
   */
  public final String getName() {
    return this.m_name;
  }

  /**
   * Obtain the input concepts required to run the service.
   * 
   * @return The input concepts required to run the service.
   */
  public final Concept[] getInputs() {
    return this.m_in;
  }

  /**
   * Obtain the output concepts provided by the service.
   * 
   * @return The output concepts provided by the service.
   */
  public final Concept[] getOutputs() {
    return this.m_out;
  }

  /**
   * Obtain a service that is equal to this one in all input and output
   * parameters.
   * 
   * @return A service that is equal to this one in all input and output
   *         parameters, or <code>null</code> if no such service exists.
   */
  public final Service nextEqual() {
    return this.m_equal;
  }

  /**
   * Check wether another service is equal to this one.
   * 
   * @param service
   *          The service to check for equality.
   * @return <code>true</code> if and only if the service provided is
   *         equal.
   */
  public final boolean isServiceEqual(final Service service) {
    Concept[] c1, c2;
    int i;

    c1 = this.m_in;
    c2 = service.m_in;

    i = c1.length;
    if (i != c2.length)
      return false;

    for (--i; i >= 0; i--) {
      if (c1[i] != c2[i])
        return false;
    }

    c1 = this.m_out;
    c2 = service.m_out;

    i = c1.length;
    if (i != c2.length)
      return false;

    for (--i; i >= 0; i--) {
      if (c1[i] != c2[i])
        return false;
    }

    this.m_equal = service.m_equal;
    this.m_in = service.m_in;
    this.m_out = service.m_out;
    service.m_equal = this;

    return true;
  }

  /**
   * Obtain the name of this concept.
   * 
   * @return The name of this concept.
   */
  @Override
  public final String toString() {
    return this.m_name;
    // Service c;
    // StringBuilder sb;
    //
    // c = this.m_equal;
    // if (c == null)
    // return this.m_name;
    //
    // sb = new StringBuilder();
    // sb.append('[');
    // sb.append(this.m_name);
    // do {
    // sb.append(' ');
    // sb.append(c.m_name);
    // c = c.m_equal;
    // } while (c != null);
    //
    // sb.append(']');
    // return sb.toString();
  }
}

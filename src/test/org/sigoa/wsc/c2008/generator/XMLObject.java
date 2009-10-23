/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-04
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2008.generator.XMLObject.java
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

package test.org.sigoa.wsc.c2008.generator;

import org.sfc.text.Textable;
import org.sfc.xml.sax.SAXWriter;

/**
 * this object is capable of producing xml output
 * 
 * @author Thomas Weise
 */
public class XMLObject extends Textable {

  /**
   * Serialize this object to xml
   * 
   * @param w
   *          the sax writer to write to
   */
  public void toSAXWriter(final SAXWriter w) {
    w.flush();
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-01-01
 * Creator          : Thomas Weise
 * Original Filename: test.org.sfc.xml.SAXTest.java
 * Last modification: 2007-01-01
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

package test.org.sfc.xml;

import java.io.File;

import org.sfc.xml.XML;
import org.sfc.xml.sax.SAXWriter;

/**
 * A test class for sax.
 *
 * @author Thomas Weise
 */
public class SAXTest {

  /**
   * the main routine
   *
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {
    File source, dest;
    SAXWriter w;


    source = new File("E:\\test.xml"); //$NON-NLS-1$
    dest = new File("E:\\dest.xml"); //$NON-NLS-1$

    w = new SAXWriter(dest);
    w.formatOn();
    XML.saxParse(source, w);

    w.release();
  }
}

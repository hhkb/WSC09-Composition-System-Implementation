/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-04-02
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.IJavaTextable.java
 * Last modification: 2007-04-02
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

package org.sfc.text;


/**
 * This interface is common to all objects that have a java-representation.
 *
 * @author Thomas Weise
 */
public interface IJavaTextable extends ITextable {

  /**
   * Serializes this object as string to a java string builder. The string
   * appended to the string builder can be copy-and-pasted into a java file
   * and represents the constructor of this object.
   *
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  public abstract void javaToStringBuilder(final StringBuilder sb,
      final int indent);

}

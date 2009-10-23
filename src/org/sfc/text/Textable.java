/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-02-14
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.text.Textable.java
 * Last modification: 2007-02-14
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
 * A simple default implementation of the {@link ITextable}-interface
 * 
 * @author Thomas Weise
 */
public abstract class Textable implements ITextable {

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  public void toStringBuilder(final StringBuilder sb) {
    sb.append(super.toString());
  }

  /**
   * Obtain the human readable representation of this textable object.
   * 
   * @return the human readable representation of this textable object
   */
  @Override
  public String toString() {
    StringBuilder sb;
    sb = new StringBuilder();
    this.toStringBuilder(sb);
    return sb.toString();
  }

}

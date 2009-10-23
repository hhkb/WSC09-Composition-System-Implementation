/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-06-07
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.service.IServiceCommand.java
 * Last modification: 2007-06-07
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

package test.org.sigoa.wsc.c2007.service;

import java.io.Writer;

/**
 * The service command interface
 *
 * @author Thomas Weise
 */
public abstract interface IServiceCommand {

  /**
   * The service command text.
   *
   * @return The service command text
   */
  public abstract char[] getText();


  /**
   * The query response
   *
   * @param writer
   *          the output writer
   * @param text
   *          the text
   * @param start
   *          the start
   * @param end
   *          the end
   * @param service
   *          the service
   * @throws Throwable
   *           if an error occured
   */
  public abstract void perform(final Writer writer, final char[] text,
      final int start, final int end, final WSCService service)
      throws Throwable;
}

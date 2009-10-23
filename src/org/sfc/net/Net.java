/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-02-23
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.net.Net.java
 * Last modification: 2007-02-23
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

package org.sfc.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.utils.ErrorUtils;

/**
 * This class helps you by accessing the network. For example, it holds
 * some port definitions useful for network connectivity.
 * 
 * @author Thomas Weise
 */
public final class Net {

  /**
   * The internet address of the local host.
   */
  public static final InetAddress LOCAL_HOST;

  static {
    InetAddress ia;

    try {
      ia = InetAddress.getLocalHost();
    } catch (Throwable t) {
      try {
        ia = InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 });
      } catch (Throwable t2) {
        try {
          ia = InetAddress.getByName("localhost"); //$NON-NLS-1$
        } catch (Throwable t3) {
          ia = null;
        }
      }
    }

    LOCAL_HOST = ia;
  }

  /**
   * The internal, hidden constructor.
   */
  private Net() {
    ErrorUtils.doNotCall();
  }

  /**
   * Convert a string to a valid ip address/port combination.
   * 
   * @param address
   *          The string to be converted.
   * @return The new internet address/port combination.
   * @throws Throwable
   *           If something goes wrong.
   */
  public static final InetSocketAddress stringToAddress(
      final String address) throws Throwable {
    final int j, p;
    final InetAddress a;
    String aa;

    aa = address.trim();
    j = address.lastIndexOf(':');
    p = Integer.parseInt(aa.substring(j + 1));
    a = InetAddress.getByName(aa.substring(0, j));

    return new InetSocketAddress(a, p);
  }

  /**
   * Convert a ip-address/port combination to a human readable string
   * representation.
   * 
   * @param address
   *          The internet socket address.
   * @param sb
   *          the string builder
   */
  public static final void appendAddress(
      final InetSocketAddress address, final StringBuilder sb) {
    sb.append(address.getAddress().getHostAddress());
    sb.append(':');
    sb.append(address.getPort());
  }

  /**
   * Convert a ip-address/port combination to a human readable string
   * representation.
   * 
   * @param address
   *          The internet socket address.
   * @return The string representation of the address.
   */
  public static final String addressToString(
      final InetSocketAddress address) {
    return (address.getAddress().getHostAddress() + ':' + address
        .getPort());
  }

  /**
   * Extract all possible internet socket addresses from the specified
   * range of a string array.
   * 
   * @param args
   *          The string array.
   * @param start
   *          The start index from where to search inside the string.
   * @param count
   *          The count of strings to investiage.
   * @return A list with the internet-addresses wanted.
   */
  public static final List<InetSocketAddress> extractAddresses(
      final String[] args, int start, int count) {
    final List<InetSocketAddress> l;
    int c, s;

    l = CollectionUtils.createList(count);
    c = (count - 1);
    s = (c + start);

    for (; c >= 0; c--) {
      try {
        l.add(stringToAddress(args[s--]));
      } catch (Throwable t) {
        //
      }
    }

    return l;
  }
}

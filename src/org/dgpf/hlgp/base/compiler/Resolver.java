/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-06
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.compiler.Resolver.java
 * Last modification: 2007-03-06
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

package org.dgpf.hlgp.base.compiler;

import org.sfc.math.Mathematics;
import org.sfc.utils.ErrorUtils;

/**
 * In this class we provide helper methods to resolve things properly.
 * 
 * @author Thomas Weise
 */
public final class Resolver {

  /**
   * resolve the given variable
   * 
   * @param var
   *          the variable to be resolved
   * @param count
   *          the count variables
   * @return the resolved variable
   */
  public static final int resolveVariable(final int var, final int count) {
    int i, c;

    if (count <= 0)
      return 0;
    c = ((count >>> 2) << 2);
    if (c != count)
      c += 4;

    if (count <= 19)
      i = resolveVarInt(var, c);
    else if (count <= 39)
      i = resolveVarLong(var, c);
    else if (count <= 62)
      i = resolveVarLong2(var, c);
    else
      i = Mathematics.modulo(var, c);

    i = (c - i - 1);
    if (i >= count)
      return (count - 1);
    return i;
  }

  /**
   * resolve the given variable
   * 
   * @param var
   *          the variable to be resolved
   * @param z
   *          x
   * @return the resolved variable
   */
  private static final int resolveVarLong(final int var, final int z) {
    long v, c, l, d;

    c = z;
    l = 0;
    d = 3;
    for (v = Mathematics.ceilDiv(c, 3); v > 0; v--) {
      l += d;
      d *= 3;
    }
    c = Mathematics.modulo(var, l);
    l = 0;
    while (d > 3) {
      d /= 3;
      if (c >= d) {
        c -= d;
        l += 3;
      } else {
        l += (c % 3);
        break;
      }
    }

    return (int) (l % z);
  }

  /**
   * resolve the given variable
   * 
   * @param var
   *          the variable to be resolved
   * @param z
   *          x
   * @return the resolved variable
   */
  private static final int resolveVarLong2(final int var, final int z) {
    long v, c, l, d;

    c = z;
    l = 0;
    d = 2;

    for (v = Mathematics.ceilDiv(c, 2); v > 0; v--) {
      l += d;
      d <<= 1;
    }
    c = Mathematics.modulo(var, l);
    l = 0;
    while (d > 2) {
      d >>>= 1;
      if (c >= d) {
        c -= d;
        l += 2;
      } else {
        l += (c & 1);
        break;
      }
    }

    return (int) (l % z);
  }

  /**
   * resolve the given variable
   * 
   * @param var
   *          the variable to be resolved
   * @param z
   *          x
   * @return the resolved variable
   */
  private static final int resolveVarInt(final int var, final int z) {
    int v, c, l, d;

    c = z;
    l = 0;
    d = 3;
    for (v = Mathematics.ceilDiv(c, 3); v > 0; v--) {
      l += d;
      d *= 3;
    }
    c = Mathematics.modulo(var, l);
    l = 0;
    while (d > 3) {
      d /= 3;
      if (c >= d) {
        c -= d;
        l += 3;
      } else {
        l += (c % 3);
        break;
      }
    }

    return (l % z);
  }

  /**
   * Resolve the given function.
   * 
   * @param index
   *          the function index
   * @param count
   *          the function count
   * @return the resolved function call
   */
  public static final int resolveFunction(final int index, final int count) {
    int c;

    if (count <= 0)
      return 0;
    c = ((count >>> 3) << 3);
    if (c != count)
      c += 8;

    c = Mathematics.modulo(index, c);

    return ((c < count) ? c : (count - 1));
  }

  /**
   * The forbidden constructor
   */
  private Resolver() {
    ErrorUtils.doNotCall();
  }

}

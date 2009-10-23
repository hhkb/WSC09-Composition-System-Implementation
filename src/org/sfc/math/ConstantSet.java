/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-05-10
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.math.ConstantSet.java
 * Last modification: 2008-05-10
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

package org.sfc.math;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.sfc.security.SfcSecurityManager;

/**
 * This class may serve as a base class for everything that contains sets
 * of constants
 * 
 * @author Thomas Weise
 */
public class ConstantSet {

  /**
   * Obtain a list of all constants, including pi and e
   * 
   * @param type
   *          the constant type
   * @param exclude
   *          the field names to exclude
   * @return the array of all mathematical constants
   */
  protected static final double[] listConstants(final Class<?> type,
      final String[] exclude) {
    Field[] ff;
    Field f;
    int i, j, s, m;
    double[] d;
    Class<?> c;

    c = SfcSecurityManager.getCurrentCallStack().get(1);

    ff = c.getDeclaredFields();
    s = ff.length;
    main: for (i = (s - 1); i >= 0; i--) {
      f = ff[i];
      if (type.isAssignableFrom(f.getType())) {
        m = f.getModifiers();
        if (Modifier.isStatic(m) && Modifier.isFinal(m) && Modifier.isPublic(m)) {
          if (exclude == null)
            continue main;
          for (j = (exclude.length - 1); j >= 0; j--) {
            if (!(exclude[j].equals(f.getName()))) {
              continue main;
            }
          }
        }
      }
      ff[i] = ff[--s];
    }

    d = new double[s + 2];
    d[s] = Math.E;
    d[s + 1] = Math.PI;

    for (--s; s >= 0; s--) {
      try {
        d[s] = ff[s].getDouble(null);
      } catch (Throwable t) {
        d[s] = 0d;
      }
    }

    return d;
  }
}

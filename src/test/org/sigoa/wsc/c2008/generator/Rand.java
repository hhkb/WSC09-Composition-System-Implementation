/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-02-11
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2008.generator.Rand.java
 * Last modification: 2008-02-11
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

import java.util.List;

import org.sfc.math.Mathematics;
import org.sigoa.refimpl.stoch.Randomizer;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * the id generator
 * 
 * @author Thomas Weise
 */
public class Rand {

  /**
   * the randomizer
   */
  public static final IRandomizer RANDOM = new Randomizer();

  /**
   * the id counter
   */
  private static int s_idCounter;

  static {
    s_idCounter = RANDOM.nextInt();
  }

  /**
   * increment the id counter
   * 
   * @return the new value of the id counter
   */
  private static final int incId() {
    return s_idCounter++;
  }

  /**
   * pseudo-randomize the given id
   * 
   * @param id
   *          the given
   * @return the new id
   */
  private static final int modifyId(final int id) {
    int i;

    i = Mathematics.modulo(id, Mathematics.MAX_PRIME);

    i *= 126337;
    i += 283763;
    i *= 56477;
    i -= 3;

    return Mathematics.modulo(i, Mathematics.MAX_PRIME);
  }

  /**
   * obtain the next id
   * 
   * @return the next id
   */
  public static final int nextId() {
    return modifyId(incId());
  }

  /**
   * @param o
   *          the list to be sorted
   */
  public static final void shuffle(final Object[] o) {
    int i, j, k;
    Object z;

    k = (o.length << 1);
    do {
      i = RANDOM.nextInt(o.length);
      j = RANDOM.nextInt(o.length);

      z = o[i];
      o[i] = o[j];
      o[j] = z;

      if (RANDOM.nextBoolean())
        --k;
    } while (k >= 0);
  }

  /**
   * add
   * 
   * @param l
   * @param item
   * @param <T>
   */
  public static final <T> void addIfNew(final List<T> l, final T item) {
    if (!(l.contains(item)))
      l.add(item);
  }

  /**
   * add
   * 
   * @param l
   * @param items
   * @param <T>
   */
  public static final <T> void addIfNew(final List<T> l,
      final List<T> items) {
    int i;
    if (items != null) {
      for (i = (items.size() - 1); i >= 0; i--) {
        addIfNew(l, items.get(i));
      }
    }
  }

  /**
   * remove
   * 
   * @param l
   * @param o
   * @return
   */
  public static final boolean remove(final List<?> l, final Object o) {
    boolean b;

    b = false;
    for (;;) {
      if (!(l.remove(o)))
        return b;
      b = true;
    }
  }

  /**
   * add
   * 
   * @param dest
   * @param items
   * @param start
   * @param count
   * @param <T>
   */
  public static final <T> void addArray(final List<T> dest,
      final List<T> items, int start, int count) {
    int i, j;
    if (items != null) {
      j = (start + count);
      for (i = start; i < j; i++) {
        addIfNew(dest, items.get(i));
      }
    }
  }
}

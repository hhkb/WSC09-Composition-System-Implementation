/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-07-18
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.composition.algorithms.heuristic.CompositionList.java
 * Last modification: 2007-07-18
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

package test.org.sigoa.wsc.c2007.composition.algorithms.heuristic;

import org.sfc.collections.IArrayConstructor;
import org.sfc.collections.lists.SimpleList;

/**
 * The composition list
 *
 * @author Thomas Weise
 */
public class CompositionList extends SimpleList<Composition> {
  /**
   * The composition array constructor
   */
  public static final IArrayConstructor<Composition> COMPOSITION_ARRAY_CONSTRUCTOR = new IArrayConstructor<Composition>() {
    public static final long serialVersionUID = 1;

    public Composition[] createArray(final int length) {
      return new Composition[length];
    }

    private final Object readResolve() {
      return COMPOSITION_ARRAY_CONSTRUCTOR;
    }

    private final Object writeReplace() {
      return COMPOSITION_ARRAY_CONSTRUCTOR;
    }
  };

  /**
   * Create a new composition list.
   *
   * @param initialSize
   *          The initial size. Use -1 for don't care.
   */
  public CompositionList(final int initialSize) {
    super(initialSize, COMPOSITION_ARRAY_CONSTRUCTOR);
  }

  /**
   * Create a new simple list.
   *
   * @param copy
   *          the list to copy from
   */
  @SuppressWarnings("unchecked")
  public CompositionList(final SimpleList<Composition> copy) {
    super(copy);
  }
}

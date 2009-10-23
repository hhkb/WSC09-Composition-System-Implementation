/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-25
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.base.EComparison.java
 * Last modification: 2007-05-25
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

package org.dgpf.rbgp.base;

import java.io.Serializable;

import org.sfc.text.IJavaTextable;
import org.sfc.text.ITextable;
import org.sfc.utils.Utils;

/**
 * The set of possible comparisons
 * 
 * @author Thomas Weise
 */
public enum EComparison implements IJavaTextable, ITextable, Serializable {

  /**
   * the false comparator
   */
  FALSE {
    @Override
    public final boolean compare(final int value1, final int value2) {
      return false;
    }

    public final void toStringBuilder(final StringBuilder sb) {
      sb.append("false"); //$NON-NLS-1$
    }

    @Override
    public final boolean canBeMet(final Symbol s1, final Symbol s2) {
      return false;
    }
  },
  /**
   * the true comparator
   */
  TRUE {
    @Override
    public final boolean compare(final int value1, final int value2) {
      return true;
    }

    @Override
    public final boolean canBeMet(final Symbol s1, final Symbol s2) {
      return true;
    }

    public final void toStringBuilder(final StringBuilder sb) {
      sb.append("true"); //$NON-NLS-1$
    }
  },
  /**
   * the < comparator
   */
  LESS {
    @Override
    public final boolean compare(final int value1, final int value2) {
      return (value1 < value2);
    }

    public final void toStringBuilder(final StringBuilder sb) {
      sb.append('<');
    }

    @Override
    public final boolean canBeMet(final Symbol s1, final Symbol s2) {
      return (!(Utils.testEqual(s1, s2)));
    }
  },
  /**
   * the <= comparator
   */
  LESS_EQUAL {
    @Override
    public final boolean compare(final int value1, final int value2) {
      return (value1 <= value2);
    }

    public final void toStringBuilder(final StringBuilder sb) {
      sb.append('<');
      sb.append('=');
    }

    @Override
    public final boolean canBeMet(final Symbol s1, final Symbol s2) {
      return true;
    }
  },
  /**
   * the == comparator
   */
  EQUAL {
    @Override
    public final boolean compare(final int value1, final int value2) {
      return (value1 == value2);
    }

    @Override
    public final boolean canBeMet(final Symbol s1, final Symbol s2) {
      return true;
    }

    public final void toStringBuilder(final StringBuilder sb) {
      sb.append('=');
      sb.append('=');
    }
  },
  /**
   * the >= comparator
   */
  GREATER_EQUAL {
    @Override
    public final boolean compare(final int value1, final int value2) {
      return (value1 >= value2);
    }

    @Override
    public final boolean canBeMet(final Symbol s1, final Symbol s2) {
      return true;
    }

    public final void toStringBuilder(final StringBuilder sb) {
      sb.append('>');
      sb.append('=');
    }
  },
  /**
   * the > comparator
   */
  GREATER {
    @Override
    public final boolean compare(final int value1, final int value2) {
      return (value1 > value2);
    }

    @Override
    public final boolean canBeMet(final Symbol s1, final Symbol s2) {
      return !(Utils.testEqual(s1, s2));
    }

    public final void toStringBuilder(final StringBuilder sb) {
      sb.append('>');
    }
  },
  /**
   * the != comparator
   */
  NOT_EQUAL {
    @Override
    public final boolean compare(final int value1, final int value2) {
      return (value1 != value2);
    }

    @Override
    public final boolean canBeMet(final Symbol s1, final Symbol s2) {
      return !(Utils.testEqual(s1, s2));
    }

    public final void toStringBuilder(final StringBuilder sb) {
      sb.append('!');
      sb.append('=');
    }
  },
  ;

  /**
   * Compare two values.
   * 
   * @param value1
   *          the first value
   * @param value2
   *          the second value
   * @return the comparison result
   */
  public abstract boolean compare(final int value1, final int value2);

  /**
   * Check if this comparison can become <code>true</code>.
   * 
   * @param s1
   *          the first symbol
   * @param s2
   *          the second symbol
   * @return <code>true</code> if and only if this comparison may
   *         evaluate to <code>true</code>, <code>false</code>
   *         otherwise.
   */
  public abstract boolean canBeMet(final Symbol s1, final Symbol s2);

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
  public final void javaToStringBuilder(final StringBuilder sb,
      final int indent) {
    sb.append(EComparison.class.getCanonicalName());
    sb.append('.');
    sb.append(this.name());
  }
}

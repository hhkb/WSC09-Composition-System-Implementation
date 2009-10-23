/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-26
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.objectives.ovcs.PrecisionOVC.java
 * Last modification: 2007-03-26
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

package org.sigoa.refimpl.go.objectives.ovcs;

import org.sigoa.spec.go.objectives.IObjectiveValueComputer;

/**
 * This objective value computer truncates its results.
 *
 * @author Thomas Weise
 */
abstract class PrecisionOVC implements IObjectiveValueComputer {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the precision value
   */
  final double m_precision;

  /**
   * Create a new precision objective value computer.
   *
   * @param precision
   *          the precision value.
   */
  public PrecisionOVC(final double precision) {
    super();
    this.m_precision = precision;
  }

  /**
   * Check whether this ovc equals another object
   *
   * @param o
   *          the object to compare with
   * @return <code>true</code> if and only if this ovc equals to the
   *         object <code>o</code>, <code>false</code> other wise
   */
  @Override
  public boolean equals(final Object o) {
    return ((o == this) || ((o != null)
        && (o.getClass() == this.getClass()) && (((PrecisionOVC) o).m_precision == this.m_precision)));
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-02
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.postProcessing.processors.ISuccessDefinition.java
 * Last modification: 2007-10-02
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

package org.sigoa.refimpl.utils.testSeries.postProcessing.processors;

/**
 * this interface allows us to define whether an individual record can be
 * considered as a success or not
 * 
 * @author Thomas Weise
 */
public interface ISuccessDefinition {
  /**
   * Check whether the individual denoted by this record of objective
   * values can be regarded as a success.
   * 
   * @param individual
   *          the individual record
   * @return <code>true</code> if and only if this individual was a
   *         success
   */
  public abstract boolean isSuccess(final double[] individual);

}

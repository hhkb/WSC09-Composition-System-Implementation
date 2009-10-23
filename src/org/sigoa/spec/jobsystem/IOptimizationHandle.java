/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-08
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.jobsystem.IOptimizationHandle.java
 * Last modification: 2006-12-08
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

package org.sigoa.spec.jobsystem;

import java.io.Serializable;

/**
 * A handle to a running optimization is returned by the job systems
 * <code>executeOptimization</code> method. It is basically a
 * <code>IWaitable</code>-instance equipped with a unique identifier.
 *
 * @author Thomas Weise
 */
public interface IOptimizationHandle extends IWaitable {

/**
 * Obtain the unique identifier of the running optimization process.
 * @return  the unique identifier of the running optimization process.
 */
  public  abstract  Serializable  getId();

}

/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-02
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.IOptimizer.java
 * Last modification: 2006-12-02
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

package org.sigoa.spec.go;

import java.io.Serializable;

import org.sigoa.spec.pipe.IPipeOut;

/**
 * The optimizer interface. An optimizer is represented as a job that will
 * be carried out by an executor. When it is finished or aborted, it will
 * write all the solutions it has found to the output pipe.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public interface IOptimizer<G extends Serializable, PP extends Serializable>
    extends Runnable, IPipeOut<G, PP> {//
}

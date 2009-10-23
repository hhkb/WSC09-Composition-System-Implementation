/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-29
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.IPipe.java
 * Last modification: 2006-11-29
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

package org.sigoa.spec.pipe;

import java.io.Serializable;

/**
 * This is the basic interface for all items of the reproduction pipeline.
 * Pipes are able to receive a junk of individuals piece by piece. If all
 * individuals needed are written to a pipe, its <code>eof</code> method
 * is invoked. This tells the pipe stage that a load of has been processed
 * and now a new cycle begins. Pipes in the sense of the <code>sigoa</code>
 * can be used again and again in a cycle, each time writing a load of
 * individuals to it using
 * <code>write</code< and invoking <code>eof</code> in order to tell that
 * one cylce step is completed.
 *
 * @param <G>
 *          The genotype
 * @param <PP>
 *          The phenotype
 * @author Thomas Weise
 */
public interface IPipe<G extends Serializable, PP extends Serializable>
    extends IPipeIn<G, PP>, IPipeOut<G, PP> {
  //

}

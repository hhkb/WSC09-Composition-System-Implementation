/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-30
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.IPopulation.java
 * Last modification: 2006-11-30
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
import java.util.List;

import org.sigoa.spec.pipe.IPipeIn;
import org.sigoa.spec.pipe.IPipeSource;

/**
 * The population interface.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public interface IPopulation<G extends Serializable, PP extends Serializable>
    extends IPipeIn<G, PP>, IPipeSource<G, PP>, List<IIndividual<G, PP>> {
  //
}

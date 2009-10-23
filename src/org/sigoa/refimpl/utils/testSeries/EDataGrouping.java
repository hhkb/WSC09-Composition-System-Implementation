/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-17
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.EDataGrouping.java
 * Last modification: 2007-11-17
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
 
package org.sigoa.refimpl.utils.testSeries;

/**
 * This enumeration specifies constants that define how data should be grouped.
 *
 * @author Thomas Weise
 */
public enum EDataGrouping {
/**
 * group everything together
 */
  ALL,
/**
 * group everything of a series together
 */
  SERIES,
/**
 * group everything of a run together
 */
  RUN;
  
}

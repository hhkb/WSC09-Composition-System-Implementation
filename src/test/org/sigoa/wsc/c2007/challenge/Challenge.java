/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-04-27 14:43:16
 * Original Filename: test.org.sigoa.wsc..challenge.Challenge.java
 * Version          : 1.0.3
 * Last modification: 2006-05-08
 *                by: Thomas Weise
 * 
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to the Free Software
 *                    Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *                    MA 02111-1307, USA or download the license under
 *                    http://www.gnu.org/copyleft/lesser.html.
 *                    
 * Warranty         : This software is provided "as is" without any
 *                    warranty; without even the implied warranty of
 *                    merchantability or fitness for a particular purpose.
 *                    See the Gnu Lesser General Public License for more
 *                    details.
 */

package test.org.sigoa.wsc.c2007.challenge;

import test.org.sigoa.wsc.c2007.kb.ConceptList;

/**
 * This class represents a challenge to be solved. A challenge in this
 * context is a set of known input parameters and wanted output parameters.
 * 
 * @author Thomas Weise
 */
public final class Challenge {
  /**
   * The challenge's name.
   */
  final String m_name;

  /**
   * The concept list holding the input concepts provided.
   */
  final ConceptList m_in;

  /**
   * The list with the concepts needed to derive from the inputs.
   */
  final ConceptList m_out;

  /**
   * the state of the challenge.
   */
  volatile boolean m_inProgress;

  /**
   * the solution to this challenge
   */
  volatile Solution m_solution;

  /**
   * Create a new challenge object.
   * 
   * @param name
   *          The name of the challenge.
   * @param in
   *          The concept list holding the input concepts provided.
   * @param out
   *          The list with the concepts needed to derive from the inputs.
   */
  public Challenge(final String name, final ConceptList in,
      final ConceptList out) {
    super();

    this.m_in = new ConceptList(in);
    this.m_out = new ConceptList(out);
    this.m_name = name;
  }

  /**
   * Obtain the concept list holding the input concepts provided.
   * 
   * @return The concept list holding the input concepts provided.
   */
  public final ConceptList getIn() {
    return this.m_in;
  }

  /**
   * Obtain the list with the concepts needed to derive from the inputs.
   * 
   * @return The list with the concepts needed to derive from the inputs.
   */
  public final ConceptList getOut() {
    return this.m_out;
  }

  /**
   * Obtain the name of the challenge.
   * 
   * @return The name of the challenge.
   */
  public final String getName() {
    return this.m_name;
  }

  /**
   * This method returns <code>true</code> if and only if this challenge
   * has not yet been solved.
   * 
   * @return <code>true</code> if and only if this challenge has not yet
   *         been solved
   */
  public final boolean notSolved() {
    return (this.m_solution == null);
  }

  /**
   * This method returns <code>true</code> if and only if this challenge
   * is solved.
   * 
   * @return <code>true</code> if and only if this challenge is solved
   */
  public final boolean solved() {
    return (this.m_solution != null);
  }

}

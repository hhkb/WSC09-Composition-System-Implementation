/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-05-05 04:19:26
 * Original Filename: test.org.sigoa.wsc.c2007.testSets.ChallengeDesc.java
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

package test.org.sigoa.wsc.c2007.testSets;

/**
 * Instances of this class describe one challenge.
 * 
 * @author Thomas Weise
 */
public final class ChallengeDesc {
  /**
   * The depth of the solution.
   */
  final int m_solutionDepth;

  /**
   * The service count per level of the solution.
   */
  final int m_solutionWidth;

  /**
   * The count of services to create.
   */
  int m_provided;

  /**
   * The input/output parameter count per service.
   */
  int m_wanted;

  /**
   * Create a new test.org.sigoa.wsc challenge.
   * 
   * @param p_solution_depth
   *          The depth of the solution.
   * @param p_solution_width
   *          The service count per level of the solution.
   * @param p_provided
   *          The count of parameters provided.
   * @param p_wanted
   *          The count of parameters wanted.
   */
  public ChallengeDesc(final int p_solution_depth,
      final int p_solution_width, final int p_provided, final int p_wanted) {
    super();
    this.m_solutionDepth = p_solution_depth;
    this.m_solutionWidth = p_solution_width;
    this.m_provided = p_provided;
    this.m_wanted = p_wanted;
  }
}

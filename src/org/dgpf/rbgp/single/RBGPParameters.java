/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.single.RBGPParameters.java
 * Last modification: 2007-05-27
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

package org.dgpf.rbgp.single;

import java.lang.reflect.Field;

import org.dgpf.rbgp.base.ActionSet;
import org.dgpf.rbgp.base.DefaultActionSet;
import org.dgpf.rbgp.base.SymbolSet;
import org.sfc.text.TextUtils;
import org.sfc.utils.Classes;

/**
 * This class is a container for all parameters relevant to a symbolic
 * classifier
 * 
 * @author Thomas Weise
 */
public class RBGPParameters extends RBGPParametersBase {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the default classifier parameters
   */
  public static final RBGPParameters DEFAULT_PARAMETERS = new RBGPParameters(
      null, null, true);

  /**
   * the actions
   */
  private final ActionSet m_actions;

  /**
   * Create a new classifier parameter set
   * 
   * @param actions
   *          the classifier actions
   * @param symbols
   *          the symbols available to the classifier
   * @param useBufferedMemory
   *          <code>true</code> if and only if buffered memory is to be
   *          used, <code>false</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public RBGPParameters(final ActionSet actions, final SymbolSet symbols,
      final boolean useBufferedMemory) {
    super(symbols, useBufferedMemory);
    this.m_actions = ((actions != null) ? actions
        : DefaultActionSet.DEFAULT_ACTION_SET);
  }

  /**
   * Obtain the actions available to the classifier
   * 
   * @return the actions available to the classifer
   */
  public final ActionSet getActions() {
    return this.m_actions;
  }

  /**
   * Serializes the parameters of the constructor of this object.
   * 
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  @Override
  protected void javaParametersToStringBuilder(final StringBuilder sb,
      final int indent) {
    Field m;

    m = Classes.findStaticField(this.m_actions);
    if (m != null)
      TextUtils.appendStaticField(m, sb);
    else
      this.m_actions.javaToStringBuilder(sb, indent);// nonsense, but
    // whatever

    sb.append(',');

    super.javaParametersToStringBuilder(sb, indent);
  }
}

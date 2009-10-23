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

import org.dgpf.rbgp.base.DefaultSymbolSet;
import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.SymbolSet;
import org.dgpf.vm.base.VirtualMachineParameters;
import org.sfc.text.TextUtils;
import org.sfc.utils.Classes;

/**
 * This class is a container for all parameters relevant to a symbolic
 * classifier
 * 
 * @author Thomas Weise
 */
public class RBGPParametersBase extends
    VirtualMachineParameters<RBGPMemory> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the default classifier parameters
   */
  public static final RBGPParametersBase DEFAULT_BASE_PARAMETERS = new RBGPParametersBase(
      null, true);

  /**
   * the symbols
   */
  private final SymbolSet m_symbols;

  /**
   * <code>true</code> if and only if buffered memory is to be used,
   * <code>false</code> otherwise
   */
  private final boolean m_useBufferedMemory;

  /**
   * Create a new classifier parameter set
   * 
   * @param symbols
   *          the symbols available to the classifier
   * @param useBufferedMemory
   *          <code>true</code> if and only if buffered memory is to be
   *          used, <code>false</code> otherwise
   */
  @SuppressWarnings("unchecked")
  public RBGPParametersBase(final SymbolSet symbols,
      final boolean useBufferedMemory) {
    super(((symbols != null) ? symbols
        : DefaultSymbolSet.DEFAULT_SYMBOL_SET).size());
    this.m_symbols = ((symbols != null) ? symbols
        : DefaultSymbolSet.DEFAULT_SYMBOL_SET);
    this.m_useBufferedMemory = useBufferedMemory;
  }

  /**
   * Obtain the symbols available to the classifiers
   * 
   * @return the symbols available to the classifiers
   */
  public final SymbolSet getSymbols() {
    return this.m_symbols;
  }

  /**
   * Returns whether the vms should use buffered memory or not.
   * 
   * @return <code>true</code> if the vms should use buffered memory,
   *         <code>false</code> if not
   */
  public final boolean useBufferendMemory() {
    return this.m_useBufferedMemory;
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
  protected  void javaParametersToStringBuilder(
      final StringBuilder sb, final int indent) {
    Field m;

    m = Classes.findStaticField(this.m_symbols);
    if (m != null)
      TextUtils.appendStaticField(m, sb);
    else
      this.m_symbols.toStringBuilder(sb);// nonsense, but whatever
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.base.Symbol.java
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

package org.dgpf.rbgp.base;

import org.dgpf.utils.FixedSetElement;
import org.dgpf.vm.base.VirtualMachine;
import org.dgpf.vm.base.VirtualMachineProgram;
import org.sfc.utils.Utils;

/**
 * A symbol that can be evaluated by the classifier
 * 
 * @author Thomas Weise
 */
public class Symbol extends FixedSetElement<SymbolSet> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the initial value of the symbol
   */
  private final int m_initialValue;

  /**
   * is this symbol write protected?
   */
  public final boolean m_writeProtected;

  /**
   * the symbol's name
   */
  private final String m_name;

  /**
   * <code>true</code> if and only if the symbol should be copied in each
   * iteration, <code>false</code> otherwise
   */
  public final boolean m_copy;

  /**
   * Create a new symbol.
   * 
   * @param symbolSet
   *          The symbol set
   * @param name
   *          the symbol's name
   * @param initialValue
   *          the general initial value of this symbol
   * @param writeProtected
   *          <code>true</code> if and only if this symbol is write
   *          protected
   * @param copy
   *          <code>true</code> if and only if the symbol should be
   *          copied in each iteration, <code>false</code> otherwise.
   *          Write protected will always be copied.
   */
  public Symbol(final SymbolSet symbolSet, final String name,
      final int initialValue, final boolean writeProtected,
      final boolean copy) {
    super(symbolSet);
    this.m_name = name;
    this.m_initialValue = initialValue;
    this.m_writeProtected = writeProtected;
    this.m_copy = (writeProtected | copy);
  }

  /**
   * Create a new symbol.
   * 
   * @param symbolSet
   *          The symbol set
   * @param name
   *          the symbol's name
   */
  public Symbol(final SymbolSet symbolSet, final String name) {
    this(symbolSet, name, 0, false, false);
  }

  /**
   * Obtain the initial value of this symbol
   * 
   * @param vm
   *          the virtual machine
   * @return the initial value of this symbol
   */
  public int getInitialValue(
      final VirtualMachine<RBGPMemory, ? extends VirtualMachineProgram<RBGPMemory>> vm) {
    return this.m_initialValue;
  }

  /**
   * Is this symbol write protected?
   * 
   * @return <code>true</code> if and only if this symbol is write
   *         protected
   */
  public final boolean isWriteProtected() {
    return this.m_writeProtected;
  }

  /**
   * Check whether this symbol will be copied in each iteration.
   * 
   * @return <code>true</code> if and only if the symbol should be copied
   *         in each iteration, <code>false</code> otherwise. Write
   *         protected will always be copied.
   */
  public final boolean willBeCopied() {
    return this.m_copy;
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public final void toStringBuilder(final StringBuilder sb) {
    sb.append(this.m_name);
  }

  /**
   * Check whether this symbol equals to an object.
   * 
   * @param o
   *          the object to compare with
   * @return <code>true</code> if and only if this symbol equals to the
   *         object <code>o</code>
   */
  @Override
  public final boolean equals(final Object o) {
    Symbol s;
    if (o == this)
      return true;
    if (o instanceof Symbol) {
      s = ((Symbol) o);

      return ((s.m_id == this.m_id) && (s.m_copy == this.m_copy)
          && (s.m_writeProtected == this.m_writeProtected)
          && (s.m_initialValue == this.m_initialValue) && //
      (Utils.testEqual(s.m_name, this.m_name)));
    }
    return false;
  }
  
    /**
   * This method allows us to append a prefix in order to handle generic
   * types.
   * 
   * @param sb
   *          the string builder
   */
  @Override
  protected void appendJavaPrefix(final StringBuilder sb) {
    //
  }
}

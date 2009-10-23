/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.single.RBGPVM.java
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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.dgpf.rbgp.base.DefaultSymbolSet;
import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.RBGPProgram;
import org.dgpf.rbgp.base.Symbol;
import org.dgpf.rbgp.base.SymbolSet;
import org.dgpf.vm.base.EVirtualMachineState;
import org.dgpf.vm.base.HostedVirtualMachine;
import org.sfc.text.TextUtils;
import org.sfc.utils.Classes;

/**
 * A virtual machine able to execute a symbolic classifier.
 * 
 * @param <HT>
 *          the host type
 * @author Thomas Weise
 */
public class RBGPVM<HT extends Serializable> extends
    HostedVirtualMachine<RBGPMemory, RBGPProgram, HT> implements
    Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the symbol set
   */
  protected final SymbolSet m_symbols;

  /**
   * the symbols
   */
  private final Symbol[] m_sym;

  /**
   * Create a new virtual machine with the default symbol set.
   * 
   * @param host
   *          the host
   */
  public RBGPVM(final HT host) {
    this(null, host);
  }

  /**
   * Create a new virtual machine.
   * 
   * @param params
   *          the classifier parameters needed
   * @param host
   *          the host
   */
  @SuppressWarnings("unchecked")
  public RBGPVM(final RBGPParametersBase params, final HT host) {
    super((params != null) ? params
        : RBGPParametersBase.DEFAULT_BASE_PARAMETERS, host);

    SymbolSet s;
    RBGPParametersBase p;

    p = ((params != null) ? params
        : RBGPParametersBase.DEFAULT_BASE_PARAMETERS);

    s = p.getSymbols();
    if (s == null)
      s = DefaultSymbolSet.DEFAULT_SYMBOL_SET;

    this.m_symbols = s;
    this.m_sym = s.getElements();
  }

  /**
   * Create a memory block
   * 
   * @return the vm memory
   */
  @Override
  protected RBGPMemory createMemory() {
    return new RBGPMemory(this.m_memorySize,
        ((RBGPParameters) (this.m_parameters)).useBufferendMemory());
  }

  /**
   * Obtain the symbol set.
   * 
   * @return the symbol set
   */
  public final SymbolSet getSymbols() {
    return this.m_symbols;
  }

  /**
   * Initialize this vm
   */
  @Override
  public void beginSimulation() {
    int[] is, r;
    int i;
    Symbol[] s;

    super.beginSimulation();

    is = this.m_memory.m_mem2;
    r = this.m_memory.m_mem1;

    Arrays.fill(is, 0);
    Arrays.fill(r, 0);

    s = this.m_sym;
    for (i = (is.length - 1); i >= 0; i--) {
      is[i] = s[i].getInitialValue(this);
      if (s[i].m_writeProtected)
        r[i] = is[i];
    }
  }

  /**
   * Set a symbol's value
   * 
   * @param sym
   *          the symbol
   * @param value
   *          the new value
   */
  public final void setValue(final Symbol sym, final int value) {
    this.m_memory.m_mem2[sym.m_id] = value;
    this.m_memory.m_mem1[sym.m_id] = value;
  }

  /**
   * Obtain a symbol's value.
   * 
   * @param sym
   *          the symbol
   * @return the symbol's value
   */
  public final int getValue(final Symbol sym) {
    return this.m_memory.m_mem1[sym.m_id];
  }

  /**
   * Obtain a symbol's value.
   * 
   * @param sym
   *          the symbol
   * @param indirection
   * @return the symbol's value
   */
  public final int getValue(final Symbol sym, final boolean indirection) {
    int i;
    i = this.m_memory.m_mem1[sym.m_id];
    if (indirection) {
      if ((i < 0) || (i >= this.m_memorySize))
        this.setError();
      else
        i = this.m_memory.m_mem1[i];
    }
    return i;
  }

  /**
   * Obtain a symbol's output value.
   * 
   * @param sym
   *          the symbol
   * @return the symbol's value
   */
  public final int getNextValue(final Symbol sym) {
    return this.m_memory.m_mem2[sym.m_id];
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
  protected final void javaParametersToStringBuilder(
      final StringBuilder sb, final int indent) {
    Field m;

    m = Classes.findStaticField(this.m_symbols);
    if (m != null) {
      TextUtils.appendStaticField(m, sb);
    } else
      // this.m_symbols.javaToStringBuilder(sb, indent);
      this.m_symbols.javaToStringBuilder(sb, indent);// nonsense, but what
    // ever
  }

  /**
   * Perform a single simulation step.
   * 
   * @return a new virtual machine state, see {@link EVirtualMachineState}
   */
  @Override
  protected final EVirtualMachineState doStep() {
    int[] i, z;
    Symbol[] s;
    int j;
    EVirtualMachineState x;

    i = this.m_memory.m_mem1;
    this.m_memory.m_mem1 = z = this.m_memory.m_mem2;
    this.m_memory.m_mem2 = i;
    s = this.m_sym;

    for (j = (i.length - 1); j >= 0; j--) {
      if (s[j].m_copy)
        i[j] = z[j];
      else
        i[j] = 0;
    }

    x = this.m_program.perform(this);
    if (x == EVirtualMachineState.NOTHING)
      return EVirtualMachineState.TERMINATED;
    return x;
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

    sb.append("symbols: "); //$NON-NLS-1$
    this.m_symbols.toStringBuilder(sb);
    sb.append(TextUtils.LINE_SEPARATOR);
    this.m_memory.toStringBuilder(sb);

    if (this.m_program != null) {
      sb.append(TextUtils.LINE_SEPARATOR);
      this.m_program.toStringBuilder(sb);
    }

  }
}

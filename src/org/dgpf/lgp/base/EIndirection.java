/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.EIndirection.java
 * Last modification: 2007-02-18
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

package org.dgpf.lgp.base;

import org.dgpf.vm.base.VirtualMachine;
import org.sfc.math.Mathematics;

/**
 * The indirection type
 * 
 * @author Thomas Weise
 */
public enum EIndirection {

  /**
   * The index value is a constant. It thus cannot be written.
   */
  CONSTANT {
    @Override
    public int read(final int idx,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      return idx;
    }

    @Override
    public void write(final int idx, final int val,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      vm.setError();
    }

    @Override
    public void toStringBuilder(final int idx, final boolean read,
        final StringBuilder sb) {
      if (read)
        sb.append(idx);
      else {
        sb.append(CNST);
        sb.append(idx);
        sb.append('=');
      }
    }

    /**
     * Correct the bounds of the given parameter
     * 
     * @param value
     *          the value
     * @param params
     *          the parameters
     * @return the corrected bounds
     */
    @Override
    int correctBounds(final int value, final ILGPParameters params) {
      return value;
    }
  },

  /**
   * The index value is a global memory address.
   */
  GLOBAL {
    @Override
    public int read(final int idx,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      return vm.m_memory.m_global[idx];
      //      
      // int[] m;
      //
      // m = vm.m_memory.m_global;
      //
      // if ((idx < 0) || (idx >= m.length)) {
      // vm.setError();
      // return 0;
      // }
      //
      // return m[idx];
    }

    @Override
    public void write(final int idx, final int val,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      vm.m_memory.m_global[idx] = val;
      //      
      // int[] m;
      //
      // m = vm.m_memory.m_global;
      //
      // if ((idx < 0) || (idx >= m.length)) {
      // vm.setError();
      // return;
      // }
      //
      // m[idx] = val;
    }

    /**
     * Correct the bounds of the given parameter
     * 
     * @param value
     *          the value
     * @param params
     *          the parameters
     * @return the corrected bounds
     */
    @Override
    int correctBounds(final int value, final ILGPParameters params) {
      return Mathematics.modulo(value, params.getGlobalMemorySize());
    }

    @Override
    public void toStringBuilder(final int idx, final boolean read,
        final StringBuilder sb) {
      sb.append(GLOBAL_TXT);
      sb.append(idx);
      if (read)
        sb.append(']');
      else
        sb.append(END_TXT1);
    }
  },

  /**
   * The index value is a local memory address.
   */
  LOCAL {
    @Override
    public int read(final int idx,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      return vm.m_memory.m_frame.m_local[idx];
      //      
      //      
      // int[] m;
      //
      // m = vm.m_memory.m_frame.m_local;
      //
      // if ((idx < 0) || (idx >= m.length)) {
      // vm.setError();
      // return 0;
      // }
      //
      // return m[idx];
    }

    @Override
    public void write(final int idx, final int val,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      vm.m_memory.m_frame.m_local[idx] = val;
      // int[] m;
      //
      // m = vm.m_memory.m_frame.m_local;
      //
      // if ((idx < 0) || (idx >= m.length)) {
      // vm.setError();
      // return;
      // }
      //
      // m[idx] = val;
    }

    /**
     * Correct the bounds of the given parameter
     * 
     * @param value
     *          the value
     * @param params
     *          the parameters
     * @return the corrected bounds
     */
    @Override
    int correctBounds(final int value, final ILGPParameters params) {
      return Mathematics.modulo(value, params.getLocalMemorySize());
    }

    @Override
    public void toStringBuilder(final int idx, final boolean read,
        final StringBuilder sb) {
      sb.append(LOCAL_TXT);
      sb.append(idx);
      if (read)
        sb.append(']');
      else
        sb.append(END_TXT1);
    }
  },

  /**
   * The index value is a indirect memory address which uses the value at
   * the specified address in global memory to index global memory.
   */
  GLOBAL_GLOBAL {
    @Override
    public int read(final int idx,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      int[] m;
      int k;

      m = vm.m_memory.m_global;

      if (/* (idx < 0) || (idx >= m.length) || */((k = m[idx]) < 0)
          || (k >= m.length)) {
        vm.setError();
        return 0;
      }

      return m[k];
    }

    @Override
    public void write(final int idx, final int val,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      int[] m;
      int k;

      m = vm.m_memory.m_global;

      if (/* (idx < 0) || (idx >= m.length) || */((k = m[idx]) < 0)
          || (k >= m.length)) {
        vm.setError();
        return;
      }

      m[k] = val;
    }

    /**
     * Correct the bounds of the given parameter
     * 
     * @param value
     *          the value
     * @param params
     *          the parameters
     * @return the corrected bounds
     */
    @Override
    int correctBounds(final int value, final ILGPParameters params) {
      return Mathematics.modulo(value, params.getGlobalMemorySize());
    }

    @Override
    public void toStringBuilder(final int idx, final boolean read,
        final StringBuilder sb) {
      sb.append(GLOBAL_GLOBAL_TXT);
      sb.append(idx);
      if (read) {
        sb.append(']');
        sb.append(']');
      } else
        sb.append(END_TXT2);
    }
  },

  /**
   * The index value is a indirect memory address which uses the value at
   * the specified address in local memory to index global memory.
   */
  GLOBAL_LOCAL {
    @Override
    public int read(final int idx,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      int[] m, m2;
      int k;

      m2 = vm.m_memory.m_global;
      m = vm.m_memory.m_frame.m_local;

      if (/* (idx < 0) || (idx >= m.length) || */((k = m[idx]) < 0)
          || (k >= m2.length)) {
        vm.setError();
        return 0;
      }

      return m2[k];
    }

    @Override
    public void write(final int idx, final int val,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      int[] m, m2;
      int k;

      m2 = vm.m_memory.m_global;
      m = vm.m_memory.m_frame.m_local;

      if (/* (idx < 0) || (idx >= m.length) || */((k = m[idx]) < 0)
          || (k >= m2.length)) {
        vm.setError();
        return;
      }

      m2[k] = val;
    }

    /**
     * Correct the bounds of the given parameter
     * 
     * @param value
     *          the value
     * @param params
     *          the parameters
     * @return the corrected bounds
     */
    @Override
    int correctBounds(final int value, final ILGPParameters params) {
      return Mathematics.modulo(value, params.getLocalMemorySize());
    }

    @Override
    public void toStringBuilder(final int idx, final boolean read,
        final StringBuilder sb) {
      sb.append(GLOBAL_LOCAL_TXT);
      sb.append(idx);
      if (read) {
        sb.append(']');
        sb.append(']');
      } else
        sb.append(END_TXT2);
    }
  },

  /**
   * The index value is a indirect memory address which uses the value at
   * the specified address in local memory to index local memory.
   */
  LOCAL_LOCAL {
    @Override
    public int read(final int idx,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      int[] m;
      int k;

      m = vm.m_memory.m_frame.m_local;

      if (/* (idx < 0) || (idx >= m.length) || */((k = m[idx]) < 0)
          || (k >= m.length)) {
        vm.setError();
        return 0;
      }

      return m[k];
    }

    @Override
    public void write(final int idx, final int val,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      int[] m;
      int k;

      m = vm.m_memory.m_frame.m_local;

      if (/* (idx < 0) || (idx >= m.length) || */((k = m[idx]) < 0)
          || (k >= m.length)) {
        vm.setError();
        return;
      }

      m[k] = val;
    }

    /**
     * Correct the bounds of the given parameter
     * 
     * @param value
     *          the value
     * @param params
     *          the parameters
     * @return the corrected bounds
     */
    @Override
    int correctBounds(final int value, final ILGPParameters params) {
      return Mathematics.modulo(value, params.getLocalMemorySize());
    }

    @Override
    public void toStringBuilder(final int idx, final boolean read,
        final StringBuilder sb) {
      sb.append(LOCAL_LOCAL_TXT);
      sb.append(idx);
      if (read) {
        sb.append(']');
        sb.append(']');
      } else
        sb.append(END_TXT2);
    }
  },

  /**
   * The index value is a indirect memory address which uses the value at
   * the specified address in global memory to index local memory.
   */
  LOCAL_GLOBAL {
    @Override
    public int read(final int idx,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      int[] m, m2;
      int k;

      m = vm.m_memory.m_global;
      m2 = vm.m_memory.m_frame.m_local;

      if (/* (idx < 0) || (idx >= m.length) || */((k = m[idx]) < 0)
          || (k >= m2.length)) {
        vm.setError();
        return 0;
      }

      return m2[k];
    }

    @Override
    public void write(final int idx, final int val,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      int[] m, m2;
      int k;

      m = vm.m_memory.m_global;
      m2 = vm.m_memory.m_frame.m_local;

      if (/* (idx < 0) || (idx >= m.length) || */((k = m[idx]) < 0)
          || (k >= m2.length)) {
        vm.setError();
        return;
      }

      m2[k] = val;
    }

    /**
     * Correct the bounds of the given parameter
     * 
     * @param value
     *          the value
     * @param params
     *          the parameters
     * @return the corrected bounds
     */
    @Override
    int correctBounds(final int value, final ILGPParameters params) {
      return Mathematics.modulo(value, params.getGlobalMemorySize());
    }

    @Override
    public void toStringBuilder(final int idx, final boolean read,
        final StringBuilder sb) {
      sb.append(LOCAL_GLOBAL_TXT);
      sb.append(idx);
      if (read) {
        sb.append(']');
        sb.append(']');
      } else
        sb.append(END_TXT2);
    }
  },
  /**
   * The index value is ignored and the top-of-stack is used.
   */
  STACK {
    @Override
    public int read(final int idx,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      int sp;
      LGPVMFrame f;

      f = vm.m_memory.m_frame;

      sp = (f.m_sp - 1);

      if (sp < 0) {
        vm.setError();
        return 0;
      }
      f.m_sp = sp;
      return f.m_stack[sp];
    }

    @Override
    public void write(final int idx, final int val,
        final VirtualMachine<LGPMemory, LGPProgram> vm) {
      int[] s;
      int sp;
      LGPVMFrame f;

      f = vm.m_memory.m_frame;
      s = f.m_stack;
      sp = f.m_sp;
      if (s.length <= sp) {
        vm.setError();
        return;
      }

      s[sp] = val;
      f.m_sp = (sp + 1);

    }

    @Override
    public void toStringBuilder(final int idx, final boolean read,
        final StringBuilder sb) {
      if (read)
        sb.append(POP_TXT);
      else
        sb.append(PUSH_TXT);
    }

    /**
     * Correct the bounds of the given parameter
     * 
     * @param value
     *          the value
     * @param params
     *          the parameters
     * @return the corrected bounds
     */
    @Override
    int correctBounds(final int value, final ILGPParameters params) {
      return value;
    }
  };

  /**
   * The qualified name
   */
  private static final char[] QN = (EIndirection.class.getCanonicalName() + '.')
      .toCharArray();

  /**
   * Append the qualified name of this indirection type to the string
   * builder.
   * 
   * @param sb
   *          the string builder to append to
   */
  public void appendQualifiedName(final StringBuilder sb) {
    sb.append(QN);
    sb.append(this.name());
  }

  /**
   * Read a data word according to this indirection type.
   * 
   * @param idx
   *          the index number
   * @param vm
   *          the virtual machine to read from
   * @return the value
   */
  public abstract int read(final int idx,
      final VirtualMachine<LGPMemory, LGPProgram> vm);

  /**
   * Write a data word according to this indirection type.
   * 
   * @param idx
   *          the index value
   * @param val
   *          the new value
   * @param vm
   *          the virtual machine to write to
   */
  public abstract void write(final int idx, final int val,
      final VirtualMachine<LGPMemory, LGPProgram> vm);

  /**
   * Correct the bounds of the given parameter
   * 
   * @param value
   *          the value
   * @param params
   *          the parameters
   * @return the corrected bounds
   */
  abstract int correctBounds(final int value, final ILGPParameters params);

  /**
   * Write the access string performed by this indirection operator to a
   * string builder.
   * 
   * @param idx
   *          the index value
   * @param read
   *          <code>true</code> if a read operation is used,
   *          <code>false</code> for write operations
   * @param sb
   *          the destination string builder
   */
  public abstract void toStringBuilder(final int idx, final boolean read,
      final StringBuilder sb);

  /**
   * the indirections
   */
  private static final EIndirection[] INDIRS = EIndirection.values();

  /**
   * the constant index
   */
  private static final int CNST_IDX = CONSTANT.ordinal();

  /**
   * Decode an indirection value from a parameter string.
   * 
   * @param param
   *          the parameter string
   * @return the decoded indirection value
   */
  public static final EIndirection decodeIndirection(final int param) {
    return INDIRS[param >>> 29];// works only because we have 8=2^n
  }

  /**
   * Decode an index value from a parameter string.
   * 
   * @param indir
   *          the indirection value
   * @param param
   *          the parameter string
   * @return the decoded index
   */
  public static final int decodeIndex(final EIndirection indir,
      final int param) {
    int i;
    i = (param & 0x1FFFFFFF);
    if ((indir == CONSTANT) && ((i & 0x10000000) != 0))
      i |= 0xe0000000;
    return i;
  }

  /**
   * Encode the indirection and index values.
   * 
   * @param indir
   *          the indirection value
   * @param index
   *          the index
   * @return the encoded value
   */
  public static final int encode(final EIndirection indir, final int index) {
    return ((((indir != null) ? indir.ordinal() : CNST_IDX) << 29) //
    | (index & 0x1FFFFFFF));
  }

  /**
   * Reencode the given parameter
   * 
   * @param param
   *          the parameter
   * @param params
   *          the lgp parameters
   * @return the reencoded parameter
   */
  public static final int reencode(final int param,
      final ILGPParameters params) {

    return (param & 0xe0000000)
        | (INDIRS[param >>> 29].correctBounds(param & 0x1FFFFFFF, params) & 0x1FFFFFFF);
  }

  /**
   * Read the value from the vm using the indirection and index encoded in
   * the parameter.
   * 
   * @param vm
   *          the vm to read from
   * @param param
   *          the parameter with the encoded indirection and index value
   * @return the value read
   */
  public static final int read(
      final VirtualMachine<LGPMemory, LGPProgram> vm, final int param) {
    int i, c;

    c = (param >>> 29);
    i = (param & 0x1FFFFFFF);
    if ((c == CNST_IDX) && ((i & 0x10000000) != 0))
      i |= 0xe0000000;
    return INDIRS[c].read(i, vm);
  }

  /**
   * Write a value to the virtual machine using the encoded indirection and
   * index value.
   * 
   * @param vm
   *          the vm to write to
   * @param param
   *          the parameter with the encoded indirection and index value
   * @param val
   *          the value to write
   */
  public static final void write(
      final VirtualMachine<LGPMemory, LGPProgram> vm, final int param,
      final int val) {
    INDIRS[param >>> 29].write(param & 0x1FFFFFFF, val, vm);
  }

  /**
   * the end text1.
   */
  static final char[] END_TXT1 = "] = ".toCharArray();//$NON-NLS-1$

  /**
   * the end text2.
   */
  static final char[] END_TXT2 = "]] = ".toCharArray();//$NON-NLS-1$

  /**
   * the global text.
   */
  static final char[] GLOBAL_TXT = "g[".toCharArray(); //$NON-NLS-1$

  /** the global-global text */
  static final char[] GLOBAL_GLOBAL_TXT = "g[g[".toCharArray();//$NON-NLS-1$

  /**
   * the global-local text
   */
  static final char[] GLOBAL_LOCAL_TXT = "g[l[".toCharArray();//$NON-NLS-1$

  /**
   * the local text
   */
  static final char[] LOCAL_TXT = "l[".toCharArray();//$NON-NLS-1$

  /**
   * the local-local text
   */
  static final char[] LOCAL_LOCAL_TXT = "l[l[".toCharArray();//$NON-NLS-1$

  /**
   * the local-global text
   */
  static final char[] LOCAL_GLOBAL_TXT = "l[g[".toCharArray();//$NON-NLS-1$

  /**
   * the push-txt
   */
  static final char[] PUSH_TXT = "push ".toCharArray();//$NON-NLS-1$

  /**
   * the pop text
   */
  static final char[] POP_TXT = "pop".toCharArray();//$NON-NLS-1$

  /**
   * the const string
   */
  static final char[] CNST = "cnst:".toCharArray();//$NON-NLS-1$
}

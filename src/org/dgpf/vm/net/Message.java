/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-20
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.net.Message.java
 * Last modification: 2007-09-20
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

package org.dgpf.vm.net;

import java.io.Serializable;

import org.sfc.collections.buffers.DirectBufferable;

/**
 * A message record.
 * 
 * @param <MDT>
 *          the message data
 * @author Thomas Weise
 */
public final class Message<MDT extends Serializable> extends DirectBufferable<Message<MDT>>{
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the message data
   */
  public final MDT m_data;

  /**
   * the message owner
   */
  public final NetVirtualMachine<?, ?, MDT> m_owner;


  /**
   * the message size
   */
  public int m_size;

  /**
   * the delay
   */
  int m_delay;

  /**
   * the virtual machine to send to
   */
  NetVirtualMachine<?, ?, MDT> m_to;

  /**
   * create a new message record
   * 
   * @param data
   *          the message data
   * @param owner
   *          the message owner
   */
  Message(final MDT data, final NetVirtualMachine<?, ?, MDT> owner) {
    super();
    this.m_data = data;
    this.m_owner = owner;
  }

}

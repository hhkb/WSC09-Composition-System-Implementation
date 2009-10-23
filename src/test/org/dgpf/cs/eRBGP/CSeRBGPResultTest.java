/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-23
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.rbgp.ElectionRBGPTestSeries.java
 * Last modification: 2007-09-23
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

package test.org.dgpf.cs.eRBGP;

import java.io.Serializable;

import org.sigoa.refimpl.utils.testSeries.ISuccessFilter;
import org.sigoa.refimpl.utils.testSeries.successFilters.FirstObjectiveZeroFilter;

/**
 * The test series for the election rbgp algorithm.
 * 
 * @author Thomas Weise
 */
public class CSeRBGPResultTest {

  /** */
  @SuppressWarnings("unchecked")
  private static final Serializable S = new org.dgpf.eRbgp.base.TreeRBGPProgram(new org.dgpf.eRbgp.base.InternalProgram(
      new org.sigoa.refimpl.genomes.tree.Node[] {
          new org.dgpf.eRbgp.base.Rule(
              new org.sigoa.refimpl.genomes.tree.Node[] {
                new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getReceiveSymbol(1)))), 
                test.org.dgpf.cs.eRBGP.EnterCSAction.ENTER_CS_ACTION
              }), 
          new org.dgpf.eRbgp.base.Rule(
              new org.sigoa.refimpl.genomes.tree.Node[] {
                new org.dgpf.eRbgp.base.expressions.Compare(
                    new org.sigoa.refimpl.genomes.tree.Node[] {
                      new org.dgpf.eRbgp.base.expressions.And(
                          new org.sigoa.refimpl.genomes.tree.Node[] {
                            new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getStartSymbol()))), 
                            new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getVariable(2))))
                          }), 
                      new org.dgpf.eRbgp.base.expressions.Sub(
                          new org.sigoa.refimpl.genomes.tree.Node[] {
                            new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getSendSymbol(0)))), 
                            new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getStartSymbol())))
                          })
                    }, org.dgpf.rbgp.base.EComparison.EQUAL), 
                new org.dgpf.eRbgp.base.actions.SetAction(
                    new org.sigoa.refimpl.genomes.tree.Node[] {
                      new org.dgpf.eRbgp.base.expressions.Compare(
                          new org.sigoa.refimpl.genomes.tree.Node[] {
                            new org.dgpf.eRbgp.base.expressions.And(
                                new org.sigoa.refimpl.genomes.tree.Node[] {
                                  new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getIncomingMsgSymbol()))), 
                                  new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getVariable(0))))
                                }), 
                            new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getVariable(2))))
                          }, org.dgpf.rbgp.base.EComparison.EQUAL)
                    }, ((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getReceiveSymbol(1))))
              }), 
          new org.dgpf.eRbgp.base.Rule(
              new org.sigoa.refimpl.genomes.tree.Node[] {
                new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getSendSymbol(0)))), 
                org.dgpf.eRbgp.net.SendAction.SEND_ACTION
              }), 
          new org.dgpf.eRbgp.base.Rule(
              new org.sigoa.refimpl.genomes.tree.Node[] {
                new org.dgpf.eRbgp.base.expressions.Not(
                    new org.sigoa.refimpl.genomes.tree.Node[] {
                      new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getIncomingMsgSymbol())))
                    }), 
                new org.dgpf.eRbgp.base.actions.SetAction(
                    new org.sigoa.refimpl.genomes.tree.Node[] {
                      new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getStartSymbol())))
                    }, ((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getSendSymbol(0))))
              }), 
          new org.dgpf.eRbgp.base.Rule(
              new org.sigoa.refimpl.genomes.tree.Node[] {
                new org.dgpf.eRbgp.base.expressions.Add(
                    new org.sigoa.refimpl.genomes.tree.Node[] {
                      new org.dgpf.eRbgp.base.expressions.And(
                          new org.sigoa.refimpl.genomes.tree.Node[] {
                            new org.dgpf.eRbgp.base.expressions.Mul(
                                new org.sigoa.refimpl.genomes.tree.Node[] {
                                  new org.dgpf.eRbgp.base.expressions.Not(
                                      new org.sigoa.refimpl.genomes.tree.Node[] {
                                        new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getStartSymbol())))
                                      }), 
                                  new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getReceiveSymbol(0))))
                                }), 
                            new org.dgpf.eRbgp.base.expressions.Mul(
                                new org.sigoa.refimpl.genomes.tree.Node[] {
                                  new org.dgpf.eRbgp.base.expressions.And(
                                      new org.sigoa.refimpl.genomes.tree.Node[] {
                                        new org.dgpf.eRbgp.base.expressions.Sub(
                                            new org.sigoa.refimpl.genomes.tree.Node[] {
                                              new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getSendSymbol(1)))), 
                                              new org.dgpf.eRbgp.base.expressions.Not(
                                                  new org.sigoa.refimpl.genomes.tree.Node[] {
                                                    new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getIncomingMsgSymbol())))
                                                  })
                                            }), 
                                        new org.dgpf.eRbgp.base.expressions.Mul(
                                            new org.sigoa.refimpl.genomes.tree.Node[] {
                                              new org.dgpf.eRbgp.base.expressions.And(
                                                  new org.sigoa.refimpl.genomes.tree.Node[] {
                                                    new org.dgpf.eRbgp.base.expressions.Constant(0), 
                                                    new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getVariable(2))))
                                                  }), 
                                              new org.dgpf.eRbgp.base.expressions.Constant(-2)
                                            })
                                      }), 
                                  new org.dgpf.eRbgp.base.expressions.Add(
                                      new org.sigoa.refimpl.genomes.tree.Node[] {
                                        new org.dgpf.eRbgp.base.expressions.Compare(
                                            new org.sigoa.refimpl.genomes.tree.Node[] {
                                              new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getLeaveCSSymbol()))), 
                                              new org.dgpf.eRbgp.base.expressions.Add(
                                                  new org.sigoa.refimpl.genomes.tree.Node[] {
                                                    new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getStartSymbol()))), 
                                                    new org.dgpf.eRbgp.base.expressions.Compare(
                                                        new org.sigoa.refimpl.genomes.tree.Node[] {
                                                          new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getStartSymbol()))), 
                                                          new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getVariable(2))))
                                                        }, org.dgpf.rbgp.base.EComparison.EQUAL)
                                                  })
                                            }, org.dgpf.rbgp.base.EComparison.EQUAL), 
                                        new org.dgpf.eRbgp.base.expressions.Constant(1)
                                      })
                                })
                          }), 
                      new org.dgpf.eRbgp.base.expressions.Add(
                          new org.sigoa.refimpl.genomes.tree.Node[] {
                            new org.dgpf.eRbgp.base.expressions.Sub(
                                new org.sigoa.refimpl.genomes.tree.Node[] {
                                  new org.dgpf.eRbgp.base.expressions.Or(
                                      new org.sigoa.refimpl.genomes.tree.Node[] {
                                        new org.dgpf.eRbgp.base.expressions.And(
                                            new org.sigoa.refimpl.genomes.tree.Node[] {
                                              new org.dgpf.eRbgp.base.expressions.Not(
                                                  new org.sigoa.refimpl.genomes.tree.Node[] {
                                                    new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getIncomingMsgSymbol())))
                                                  }), 
                                              new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getIncomingMsgSymbol())))
                                            }), 
                                        new org.dgpf.eRbgp.base.expressions.Constant(-1)
                                      }), 
                                  new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getReceiveSymbol(0))))
                                }), 
                            new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getStartSymbol())))
                          })
                    }), 
                new org.dgpf.eRbgp.base.actions.SetActionInd(
                    new org.sigoa.refimpl.genomes.tree.Node[] {
                      new org.dgpf.eRbgp.base.expressions.Constant(2)
                    }, ((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getVariable(2))))
              }), 
          new org.dgpf.eRbgp.base.Rule(
              new org.sigoa.refimpl.genomes.tree.Node[] {
                new org.dgpf.eRbgp.base.expressions.Or(
                    new org.sigoa.refimpl.genomes.tree.Node[] {
                      new org.dgpf.eRbgp.base.expressions.Constant(0), 
                      new org.dgpf.eRbgp.base.expressions.Or(
                          new org.sigoa.refimpl.genomes.tree.Node[] {
                            new org.dgpf.eRbgp.base.expressions.Not(
                                new org.sigoa.refimpl.genomes.tree.Node[] {
                                  new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getIncomingMsgSymbol())))
                                }), 
                            new org.dgpf.eRbgp.base.expressions.Read(((test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries.SYMBOL_SET.getStartSymbol())))
                          })
                    }), 
                org.dgpf.eRbgp.net.SendAction.SEND_ACTION
              })
        }));

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static final void main(String[] args) {
    ISuccessFilter<Serializable> f;
    CSeRBGPTestSeries x;

    x = new CSeRBGPTestSeries("e:\\temp\\q");//$NON-NLS-1$

    f = new FirstObjectiveZeroFilter(x.createIndividualEvaluator());
    x.abort();

    System.out.println(S);
    System.out.println();
    System.out.println(f.isOverfitted(S));
  }
}

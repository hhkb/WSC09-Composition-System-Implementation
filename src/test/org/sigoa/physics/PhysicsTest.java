/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-29
 * Creator          : Thomas Weise
 * Original Filename: test.Dummy.java
 * Last modification: 2006-12-29
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

package test.org.sigoa.physics;

import org.dgpf.symbolicRegression.scalar.real.RealExpression;
import org.sfc.io.TextWriter;
import org.sfc.math.Mathematics;

/**
 * the dummy test class
 * 
 * @author Thomas Weise
 */
public class PhysicsTest {
  /**
   * 
   */
  private static final RealExpression F = new org.dgpf.symbolicRegression.scalar.real.Pwr(
      new org.sigoa.refimpl.genomes.tree.Node[] {
          new org.dgpf.symbolicRegression.scalar.real.ArcTan(
              new org.sigoa.refimpl.genomes.tree.Node[] {
                new org.dgpf.symbolicRegression.scalar.real.Constant(0.915965594177219)
              }), 
          new org.dgpf.symbolicRegression.scalar.real.Add(
              new org.sigoa.refimpl.genomes.tree.Node[] {
                new org.dgpf.symbolicRegression.scalar.real.Sub(
                    new org.sigoa.refimpl.genomes.tree.Node[] {
                      new org.dgpf.symbolicRegression.scalar.real.Sub(
                          new org.sigoa.refimpl.genomes.tree.Node[] {
                            new org.dgpf.symbolicRegression.scalar.real.Sub(
                                new org.sigoa.refimpl.genomes.tree.Node[] {
                                  new org.dgpf.symbolicRegression.scalar.real.Add(
                                      new org.sigoa.refimpl.genomes.tree.Node[] {
                                        new org.dgpf.symbolicRegression.scalar.real.Tan(
                                            new org.sigoa.refimpl.genomes.tree.Node[] {
                                              new org.dgpf.symbolicRegression.scalar.real.Constant(1024.0)
                                            }), 
                                        new org.dgpf.symbolicRegression.scalar.real.Constant(1.7320508075688772)
                                      }), 
                                  org.dgpf.symbolicRegression.scalar.real.X.INSTANCE
                                }), 
                            new org.dgpf.symbolicRegression.scalar.real.Pwr(
                                new org.sigoa.refimpl.genomes.tree.Node[] {
                                  org.dgpf.symbolicRegression.scalar.real.X.INSTANCE, 
                                  new org.dgpf.symbolicRegression.scalar.real.Constant(2.23606797749979)
                                })
                          }), 
                      new org.dgpf.symbolicRegression.scalar.real.Div(
                          new org.sigoa.refimpl.genomes.tree.Node[] {
                            new org.dgpf.symbolicRegression.scalar.real.Constant(0.5772156649015329), 
                            new org.dgpf.symbolicRegression.scalar.real.Exp(
                                new org.sigoa.refimpl.genomes.tree.Node[] {
                                  new org.dgpf.symbolicRegression.scalar.real.Add(
                                      new org.sigoa.refimpl.genomes.tree.Node[] {
                                        new org.dgpf.symbolicRegression.scalar.real.ArcTan(
                                            new org.sigoa.refimpl.genomes.tree.Node[] {
                                              new org.dgpf.symbolicRegression.scalar.real.Pwr(
                                                  new org.sigoa.refimpl.genomes.tree.Node[] {
                                                    org.dgpf.symbolicRegression.scalar.real.X.INSTANCE, 
                                                    new org.dgpf.symbolicRegression.scalar.real.Constant(1.4142135623730951)
                                                  })
                                            }), 
                                        new org.dgpf.symbolicRegression.scalar.real.ArcTan(
                                            new org.sigoa.refimpl.genomes.tree.Node[] {
                                              new org.dgpf.symbolicRegression.scalar.real.Mul(
                                                  new org.sigoa.refimpl.genomes.tree.Node[] {
                                                    new org.dgpf.symbolicRegression.scalar.real.Exp(
                                                        new org.sigoa.refimpl.genomes.tree.Node[] {
                                                          new org.dgpf.symbolicRegression.scalar.real.ArcTan(
                                                              new org.sigoa.refimpl.genomes.tree.Node[] {
                                                                new org.dgpf.symbolicRegression.scalar.real.Constant(0.915965594177219)
                                                              })
                                                        }), 
                                                    new org.dgpf.symbolicRegression.scalar.real.Tan(
                                                        new org.sigoa.refimpl.genomes.tree.Node[] {
                                                          new org.dgpf.symbolicRegression.scalar.real.Constant(256.0)
                                                        })
                                                  })
                                            })
                                      })
                                })
                          })
                    }), 
                new org.dgpf.symbolicRegression.scalar.real.Constant(256.0)
              })
        });

  /**
   * the main routine
   * 
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {
    // int i;
    double d, r;
    PhysicsContext x;
    TextWriter tw;

    tw = new TextWriter("E:\\1.txt");//$NON-NLS-1$

    x = new PhysicsContext();

    for (d = -10d; d < 10d; d += 0.01d) {
      x.clear();
      r = F.compute(d, x);
      if (Mathematics.isNumber(r) && (x.getErrors() <= 0)) {
        tw.ensureNewLine();
        tw.writeDouble(d);
        tw.writeCSVSeparator();
        tw.writeDouble(r);
      }

      // System.out.println(i + ": " + //$NON-NLS-1$
      // F.compute(i, x) + " e:" + //$NON-NLS-1$
      // x.getErrors());

    }

    tw.release();
  }
}

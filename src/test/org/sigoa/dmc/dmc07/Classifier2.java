/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-29
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.dmc.Classifier2.java
 * Last modification: 2007-05-29
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

package test.org.sigoa.dmc.dmc07;

/**
 * The combined classifier
 *
 * @author Thomas Weise
 */
public class Classifier2 extends ClassifierSystem {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the first classifier
   */
  final ClassifierSystem m_c1;

  /**
   * the second classifier
   */
  final ClassifierSystem m_c2;
  /**
   * the third classifier
   */
  final ClassifierSystem m_c3;

  /**
   * Decode a genotype to a rule.
   */
  public Classifier2() {
    super(new byte[0][0], new EClasses[0]);

    this.m_c1 = ClassifierEmbryogeny.CLASSIFIER_EMBRYOGENY
        .hatch(new byte[] { -4, 124, 87, 29, -124, 40, -2, 27, -34, -44,
            55, 31, -123, 3, -84, -26, 127, 122, 112, -64, -67, -46, 108,
            -18, 127, -88, -26, 75, -46, 113, -121, -45, 1, 30, 48, 90,
            56, 15, 65, -113, -33, -88, -85, 121, 56, 122, -120, 9, -68,
            -86, 34, -2, 1, -28, -24, 4, -46, 77, 44, 117, -18, 127, -88,
            -26, -53, -106, 127, 100, 116, 32, 5, 96, -104, 89, -3, 3,
            -56, -47, 105, -53, -1, 57, 122, -112, 2, 16, 12, 44, 42, -10,
            -45, 6, -62, 15, -119, -12, 78, -8, 9, 47, 63, -56, 7, 81,
            -23, 11, -7, -84, -113, -119, 27, -29, -35, -46, 125, 68, 5,
            0, 96, 0, 80, 14, -64, 56, 44, -121, 13, -6, 9, 51, 123, 1,
            -9, 39, -94, -5, 35, 62, 64, 111, -75, 61, 63, 121, -70, 124,
            79, 52, 33, -123, 83, -37, 103, -24, -83, -67, 99, -128, -34,
            45, 7, -81, -24, -2, -120, 15, 48, -12, -42, -34, 49, 64, -17,
            -106, -125, 87, 112, 32, 12, 54, -62, -17, 53, -105, 24, 124,
            95, -29, -21, -89, -19, 39, -77, 97, -60, 0, 48, -58, -85,
            -22, -122, 2, 110, 53, 71, 67, -126, -80, 120, 113, 14, -31,
            23 });
    // new ClassifierSystem(new byte[][] {
    // { 0, 3, 3, 3, 0, 3, 3, 1, 3, 1, 1, 1, 1, 3, 1, 0, 0 },
    // { 0, 1, 0, 1, 1, 0, 3, 3, 3, 3, 1, 3, 0, 0, 3, 3, 2 },
    // { 1, 1, 3, 3, 1, 3, 0, 3, 3, 1, 0, 1, 1, 0, 2, 3, 0 },
    // { 0, 2, 1, 1, 1, 3, 0, 3, 3, 3, 3, 3, 0, 1, 3, 3, 0 },
    // { 3, 1, 0, 0, 0, 3, 1, 3, 3, 2, 2, 0, 1, 3, 0, 3, 2 },
    // { 3, 1, 3, 3, 3, 3, 3, 0, 0, 1, 1, 1, 3, 0, 3, 3, 1 },
    // { 1, 2, 0, 1, 3, 1, 0, 3, 1, 3, 1, 0, 2, 3, 0, 1, 3 },
    // { 0, 1, 3, 0, 1, 2, 2, 0, 2, 3, 1, 0, 1, 0, 2, 3, 2 },
    // { 0, 0, 0, 3, 3, 0, 0, 0, 2, 1, 0, 1, 3, 2, 0, 0, 3 },
    // { 2, 3, 2, 2, 0, 3, 1, 1, 3, 1, 2, 2, 1, 3, 1, 0, 2 },
    // { 3, 3, 0, 0, 1, 0, 0, 1, 3, 3, 0, 2, 3, 3, 1, 3, 0 },
    // { 2, 2, 0, 3, 0, 1, 2, 0, 2, 1, 3, 3, 2, 3, 3, 3, 1 },
    // { 1, 3, 1, 1, 1, 3, 0, 3, 3, 0, 0, 3, 1, 0, 1, 3, 3 },
    // { 2, 0, 2, 1, 0, 0, 0, 0, 3, 3, 2, 2, 2, 2, 2, 2, 0 },
    // { 0, 3, 3, 3, 3, 0, 0, 0, 0, 2, 0, 3, 1, 0, 1, 3, 1 },
    // { 3, 1, 3, 3, 3, 3, 3, 0, 0, 1, 1, 1, 3, 0, 3, 3, 1 },
    // { 3, 2, 1, 1, 2, 3, 3, 3, 1, 0, 1, 2, 1, 0, 1, 3, 3 },
    // { 0, 1, 2, 2, 0, 0, 0, 0, 0, 3, 0, 0, 3, 0, 3, 0, 3 },
    // { 1, 3, 3, 3, 3, 0, 0, 0, 0, 2, 0, 3, 1, 0, 1, 3, 1 },
    // { 3, 2, 1, 1, 2, 3, 3, 3, 3, 3, 0, 3, 1, 0, 1, 3, 3 },
    // { 0, 1, 2, 2, 0, 0, 0, 0, 0, 1, 0, 0, 3, 0, 0, 0, 3 },
    // { 0, 1, 1, 1, 0, 3, 2, 3, 3, 1, 2, 2, 1, 3, 0, 0, 0 },
    // { 0, 3, 3, 3, 0, 0, 1, 2, 0, 2, 0, 1, 3, 3, 2, 3, 0 },
    // { 0, 3, 3, 3, 0, 1, 0, 2, 3, 1, 1, 2, 3, 3, 1, 0, 0 },
    // { 3, 3, 1, 0, 0, 1, 0, 1, 1, 1, 2, 2, 3, 3, 2, 0, 0 },
    // { 3, 3, 1, 2, 1, 1, 3, 3, 1, 0, 3, 0, 1, 0, 3, 1, 3 },
    // { 3, 3, 1, 2, 1, 1, 0, 3, 1, 0, 0, 2, 1, 0, 3, 1, 3 },
    // { 3, 0, 2, 3, 1, 3, 1, 3, 2, 0, 1, 3, 1, 3, 3, 1, 0 },
    // { 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0 },
    // { 0, 1, 1, 2, 3, 0, 0, 0, 0, 0, 3, 0, 2, 3, 0, 0, 3 },
    // { 2, 3, 0, 0, 3, 2, 1, 0, 0, 1, 3, 3, 3, 0, 1, 0, 2 },
    // { 3, 0, 3, 2, 3, 1, 1, 0, 2, 0, 3, 1, 3, 3, 0, 2, 2 },
    // { 0, 2, 0, 1, 0, 2, 0, 3, 1, 1, 3, 3, 0, 2, 0, 2, 1 },
    // { 1, 0, 1, 3, 1, 3, 3, 3, 1, 0, 1, 0, 3, 3, 1, 0, 0 },
    // { 3, 0, 0, 1, 3, 3, 2, 1, 1, 3, 2, 3, 1, 3, 1, 0, 3 },
    // { 0, 1, 0, 0, 2, 0, 0, 1, 0, 0, 3, 2, 2, 0, 0, 1, 3 },
    // { 0, 0, 2, 2, 3, 1, 3, 1, 3, 2, 0, 3, 1, 0, 0, 3, 3 },
    // { 1, 3, 2, 1, 3, 3, 3, 0, 1, 2, 1, 2, 1, 3, 0, 0, 3 },
    // { 1, 0, 3, 0, 0, 0, 0, 0, 0, 3, 0, 2, 1, 0, 3, 3, 2 },
    // { 3, 0, 3, 1, 2, 3, 3, 3, 0, 1, 2, 2, 0, 0, 0, 0, 3 },
    // { 0, 3, 3, 1, 3, 3, 1, 1, 3, 2, 3, 3, 3, 2, 2, 3, 3 },
    // { 3, 0, 3, 1, 0, 1, 3, 0, 0, 1, 2, 0, 0, 2, 0, 0, 3 },
    // { 0, 3, 3, 2, 0, 2, 3, 0, 3, 1, 0, 2, 0, 0, 3, 3, 3 } },
    // new EClasses[] { EClasses.B, EClasses.B, EClasses.A, EClasses.A,
    // EClasses.B, EClasses.B, EClasses.B, EClasses.A, EClasses.B,
    // EClasses.A, EClasses.N, EClasses.B, EClasses.A, EClasses.N,
    // EClasses.N, EClasses.B, EClasses.A, EClasses.N, EClasses.N,
    // EClasses.A, EClasses.N, EClasses.B, EClasses.B, EClasses.B,
    // EClasses.B, EClasses.A, EClasses.A, EClasses.B, EClasses.A,
    // EClasses.N, EClasses.B, EClasses.A, EClasses.N, EClasses.A,
    // EClasses.A, EClasses.B, EClasses.N, EClasses.A, EClasses.N,
    // EClasses.N, EClasses.N, EClasses.N, EClasses.N });

    this.m_c2 = ClassifierEmbryogeny.CLASSIFIER_EMBRYOGENY.hatch(new  byte[] {95, -35, 13, -33, -32, -8, -97, -22, -29, 28, -1, 83, 125, -100, -29, 127, -86, -113, 115, -4, 79, -11, 113, -114, -1, -87, 62, -50, -15, 63, -43, -57, 57, -2, -89, -6, 56, -57, -1, 84, 31, -25, -8, -97, -22, -29, 28, -1, 83, 125, -100, -29, 127, -86, -113, 115, -4, 79, -11, 113, -114, -1, -87, 62, -50, -15, 63, -43, -57, 57, -2, -89, -6, 56, -57, -1, 84, 31, -25, -8, -97, -22, -29, 28, -1, 83, 125, -100, -29, 127, -86, -113, 115, -4, 79, -11, 113, -120, 47, 54, 38, 65, 7, -47, 68, 92, -96, -1, -89, 124, 60} );
// = new test.org.sigoa.dmc.ClassifierSystem(new byte[][] {
// { 3, 3, 1, 1, 1, 3, 1, 3, 1, 3, 0, 3, 3, 3, 1, 3, 0 },
// { 3, 1, 0, 3, 3, 3, 3, 3, 0, 1, 1, 1, 3, 3, 1, 0, 3 },
// { 3, 2, 3, 3, 0, 0, 0, 0, 0, 0, 2, 2, 3, 0, 3, 3, 3 },
// { 0, 3, 1, 3, 1, 2, 3, 0, 2, 0, 3, 3, 1, 0, 3, 3, 0 },
// { 1, 3, 3, 1, 0, 1, 3, 3, 2, 0, 0, 3, 3, 1, 1, 3, 3 },
// { 0, 0, 0, 0, 1, 3, 3, 1, 2, 3, 3, 3, 3, 3, 3, 1, 0 },
// { 0, 1, 1, 3, 3, 2, 2, 0, 3, 1, 3, 3, 3, 1, 3, 0, 0 },
// { 2, 1, 0, 1, 0, 0, 0, 1, 3, 1, 3, 0, 1, 2, 3, 3, 0 },
// { 0, 1, 1, 3, 2, 3, 2, 2, 3, 1, 3, 3, 3, 1, 3, 0, 0 },
// { 3, 1, 2, 3, 1, 1, 0, 0, 2, 2, 0, 3, 0, 3, 3, 3, 3 },
// { 1, 2, 2, 0, 1, 2, 0, 1, 1, 0, 0, 3, 2, 1, 1, 3, 2 },
// { 0, 2, 0, 0, 3, 1, 0, 2, 0, 3, 0, 3, 3, 3, 3, 3, 1 },
// { 3, 3, 0, 1, 0, 0, 1, 1, 2, 1, 0, 3, 1, 0, 1, 3, 1 },
// { 3, 2, 3, 3, 0, 3, 1, 0, 3, 2, 1, 3, 3, 3, 2, 0, 3 },
// { 3, 2, 3, 3, 0, 3, 1, 0, 3, 2, 1, 1, 3, 3, 0, 0, 3 },
// { 3, 2, 3, 3, 0, 0, 0, 0, 0, 0, 2, 2, 3, 0, 3, 3, 3 },
// { 3, 3, 3, 1, 0, 3, 2, 0, 0, 0, 1, 2, 1, 2, 0, 3, 0 },
// { 1, 0, 3, 1, 1, 3, 2, 0, 1, 1, 1, 0, 3, 3, 3, 3, 3 },
// { 1, 0, 1, 3, 2, 1, 0, 3, 0, 1, 1, 3, 1, 1, 0, 2, 0 },
// { 1, 0, 0, 0, 3, 3, 2, 3, 0, 1, 3, 1, 1, 3, 0, 2, 0 },
// { 0, 2, 3, 3, 2, 1, 2, 0, 3, 0, 3, 1, 0, 3, 2, 2, 2 },
// { 0, 1, 0, 3, 3, 1, 2, 0, 3, 2, 1, 0, 3, 0, 1, 2, 0 },
// { 1, 1, 1, 0, 0, 1, 3, 1, 3, 0, 1, 0, 1, 0, 3, 1, 2 },
// { 0, 1, 3, 3, 3, 3, 3, 3, 0, 1, 1, 1, 1, 3, 3, 2, 3 } },
// new EClasses[] { test.org.sigoa.dmc.EClasses.A, test.org.sigoa.dmc.EClasses.B,
// test.org.sigoa.dmc.EClasses.A, test.org.sigoa.dmc.EClasses.A,
// test.org.sigoa.dmc.EClasses.A, test.org.sigoa.dmc.EClasses.N,
// test.org.sigoa.dmc.EClasses.N, test.org.sigoa.dmc.EClasses.B,
// test.org.sigoa.dmc.EClasses.N, test.org.sigoa.dmc.EClasses.N,
// test.org.sigoa.dmc.EClasses.A, test.org.sigoa.dmc.EClasses.N,
// test.org.sigoa.dmc.EClasses.A, test.org.sigoa.dmc.EClasses.B,
// test.org.sigoa.dmc.EClasses.B, test.org.sigoa.dmc.EClasses.A,
// test.org.sigoa.dmc.EClasses.N, test.org.sigoa.dmc.EClasses.A,
// test.org.sigoa.dmc.EClasses.B, test.org.sigoa.dmc.EClasses.B,
// test.org.sigoa.dmc.EClasses.A, test.org.sigoa.dmc.EClasses.A,
// test.org.sigoa.dmc.EClasses.A, test.org.sigoa.dmc.EClasses.B });

    this.m_c3 = ClassifierEmbryogeny.CLASSIFIER_EMBRYOGENY.hatch(new  byte[] {69, -104, -24, -36, 1, 0, -8, 17, 92, -36, -13, 83, 79, 30, -6, 127, -86, 113, 50, 60, 112, -59, 66, 26, 7, -29, -23, -63, -96, -36, -64, 46, -71, -25, -89, -98, 60, -107, 93, -15, 1, 3, -28, -73, -25, 8, 125, -19, 63, 108, 15, -112, -33, -98, 39, 52, 60, 112, -59, 66, 2, 30, -1, 49, -112, -14, -2, 68, -40, -72, 66, 60, 95, 9, -11, -1, 84, -29, -28, -98, -104, 58, -14, 4, 60, -34, 67, -96, -128, -57, 127, 12, -76, 96, -29, 10, -68, -66, -10, 31, -74, -121, -37, 95, 7, -77, 96, 4, -96, -25, 90, 108, 15, 15, -57, 9, -81, -1, 125, 127, 92, 19, 83, 71, -98, -128, -57, 127, 12, 52, -107, -45, -45, 61, -52, 98, -8, -119, 23, -56, 83, -49, 19, -102, -54, -17, -7, 30, -4, -1, 84, -29, 5} );
 }



  /**
   * Classify a data set.
   *
   * @param data
   *          the data set
   * @return the classification
   */
  @Override
  public EClasses classify(final byte[] data) {
 int i, j;
 EClasses e1, e2;
//
 j = 0;
 for (i = (data.length - 1); i >= 0; i--) {
 j += data[i];
 }
//
// e1 = this.m_c1.classify(data);
// e2 = this.m_c2.classify(data);
//
// if ((j >= 1) && (j <= 3))
// return e1;
//
// return e2;

// if((j>=1)&&(j<=3)) return EClasses.N;
 e1 = this.m_c1.classify(data);
 e2 = this.m_c3.classify(data);
 if(e1==e2) return e1;
 if ((j >= 1) && (j <= 3)) return EClasses.N;
    return this.m_c2.classify(data);
  }
}

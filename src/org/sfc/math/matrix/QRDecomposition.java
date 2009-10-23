/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-30
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.math.matrix.QRDecomposition.java
 * Last modification: 2007-12-30
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

package org.sfc.math.matrix;

import java.io.Serializable;

/**
 * For an m-by-n matrix A with m >= n, the QR decomposition is an m-by-n
 * orthogonal matrix Q and an n-by-n upper triangular matrix R so that A =
 * Q*R. The QR decompostion always exists, even if the matrix does not have
 * full rank, so the constructor will never fail. The primary use of the QR
 * decomposition is in the least squares solution of nonsquare systems of
 * simultaneous linear equations. This will fail if is_fullrank() returns
 * false. The algorithm used here is the same as in the Jama package.
 */
public final class QRDecomposition implements Cloneable, Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /** the matrix data */
  private double m_data[];

  /** the matrix height */
  private int m_m;

  /** the matrix width */
  private int m_n;

  /** the r diagonale */
  private double m_r_diag[];

  /** true if thematrix has full rank */
  private boolean m_fullrank;

  /**
   * Performe a qr-decomposition of a matrix.
   * 
   * @param m
   *          The height of the matrix
   * @param n
   *          The width of matrix
   * @param mat
   *          The matrix data
   */
  public QRDecomposition(int m, int n, double mat[]) {
    super();

    int li, lj, lk, la, lb, lc, ld;
    double ls, lt;

    this.m_fullrank = true;
    this.m_data = new double[li = (m * n)];
    System.arraycopy(mat, 0, this.m_data, 0, li);
    this.m_r_diag = new double[n];
    this.m_m = m;
    this.m_n = n;

    la = 0;
    lc = 0;
    for (lk = 0; lk < n; lk++) {
      ls = 0;
      la = lc + lk;
      lb = la;
      for (li = lk; li < m; li++) {
        ls = Math.hypot(ls, this.m_data[lb]);
        lb += n;
      }

      if (ls != 0.0) {
        if (this.m_data[la] < 0.0)
          ls = -ls;

        lb = la;
        for (li = lk; li < m; li++) {
          this.m_data[lb] /= ls;
          lb += n;
        }

        this.m_data[la] += 1.0;

        for (lj = (lk + 1); lj < n; lj++) {
          lt = 0.0;
          lb = la;
          ld = lc + lj;
          for (li = lk; li < m; li++) {
            lt += this.m_data[lb] * this.m_data[ld];
            lb += n;
            ld += n;
          }
          lt = -lt / this.m_data[la];

          ld = lc + lj;
          lb = la;
          for (li = lk; li < m; li++) {
            this.m_data[ld] += lt * this.m_data[lb];
            ld += n;
            lb += n;
          }
        }
      }

      lc += n;

      if (ls == 0.0) {
        this.m_fullrank = false;
        this.m_r_diag[lk] = 0.0;
      } else
        this.m_r_diag[lk] = -ls;
    }
  }

  /**
   * internal constructor for cloning
   * 
   * @param data
   *          The original's this.m_data member
   * @param m
   *          The original's this.m_m member
   * @param n
   *          The original's this.m_n member
   * @param r_diag
   *          The original's this.m_r_diag member
   * @param fullrank
   *          The original's this.m_fullrank member
   */
  private QRDecomposition(double data[], int m, int n, double r_diag[],
      boolean fullrank) {
    super();
    this.m_data = new double[data.length];
    System.arraycopy(data, 0, this.m_data, 0, data.length);
    this.m_m = m;
    this.m_n = n;
    this.m_r_diag = new double[r_diag.length];
    System.arraycopy(this.m_r_diag, 0, r_diag, 0, r_diag.length);
    this.m_fullrank = fullrank;
  }

  /**
   * returns a copy of this decomposition
   * 
   * @return a copy of this QRDecomposition
   */
  public QRDecomposition copy() {
    return new QRDecomposition(this.m_data, this.m_m, this.m_n,
        this.m_r_diag, this.m_fullrank);
  }

  /**
   * the implementation of Cloneable's clone()-method
   * 
   * @return a copy of this decomposition
   */
  @Override
  public Object clone() {
    return copy();
  }

  /**
   * calculates the least squares solution of A * X = B, where the initial
   * matrix is A, mat is B and the result is to be put into result mat is a
   * mxn matrix, the matrix decomposed was this.m_mxthis.m_n, the resulting
   * matrix is then this.m_nxn.
   * 
   * @param m
   *          height of matrix B
   * @param n
   *          width of matrix B
   * @param mat
   *          the matrix B
   * @param result
   *          matrix X that minimizes the two norm of Q*R*X-B
   */
  public void solve(int m, int n, double mat[], double result[]) {
    int li, lj, lk, la, lb, lc, ld;
    double ls;
    double ltemp[];

    if (!this.m_fullrank)
      throw new ArithmeticException();

    ltemp = new double[m * n];
    System.arraycopy(mat, 0, ltemp, 0, ltemp.length);

    la = 0;
    lc = 0;
    for (lk = 0; lk < this.m_n; lk++) {

      for (lj = 0; lj < n; lj++) {
        ls = 0.0;

        lb = la;
        ld = lc + lj;
        for (li = lk; li < this.m_m; li++) {
          ls += this.m_data[lb] * ltemp[ld];
          lb += this.m_n;
          ld += n;
        }

        ls = -ls / this.m_data[la];
        lb = la;
        ld = lc + lj;
        for (li = lk; li < this.m_m; li++) {
          ltemp[ld] += ls * this.m_data[lb];
          lb += this.m_n;
          ld += n;
        }
      }

      lc += n;
      la += this.m_n + 1;
    }

    lc = this.m_n * n;
    for (lk = (this.m_n - 1); lk >= 0; lk--) {
      ld = lc;
      for (lj = (n - 1); lj >= 0; lj--) {
        ltemp[--ld] /= this.m_r_diag[lk];
      }

      lc -= n;
      la = lc;
      lb = lc + lk;
      for (li = (lk - 1); li >= 0; li--) {
        lb -= n;
        ls = this.m_data[lb];
        ld = lc + n;
        for (lj = (n - 1); lj >= 0; lj--) {
          ltemp[--la] -= ltemp[--ld] * ls;
        }
      }
    }

    lk = this.m_n;
    la = (lk - 1) * n;
    for (; lk > 0; lk--) {
      System.arraycopy(ltemp, la, result, la, n);
      la -= n;
    }
  }

  /**
   * returns if the initial matrix has full rank.
   * 
   * @return true if the initial matrix has full rank, false otherwise
   */
  public boolean is_fullrank() {
    return this.m_fullrank;
  }

  /**
   * returns the Householder vectors
   * 
   * @return lower trapezoidal matrix whose columns define the reflections
   */
  public Matrix get_h() {
    int li, la;
    double ltemp[];

    li = this.m_m;
    la = li * this.m_n;
    ltemp = new double[la];
    la -= this.m_n;

    for (; li > 0; li--) {
      System.arraycopy(this.m_data, la, ltemp, la, li);
      la -= this.m_n;
    }

    return new Matrix(this.m_m, this.m_n, ltemp);
  }

  /**
   * returns the upper triangular factor R
   * 
   * @return R
   */
  public Matrix get_r() {
    int li, la, lj;
    double ltemp[];

    ltemp = new double[this.m_n * this.m_n];

    lj = this.m_n;
    la = this.m_n * this.m_n;
    lj = 0;
    for (li = (this.m_n - 1); li >= 0; li--) {
      System.arraycopy(this.m_data, la, ltemp, la, lj++);
      ltemp[--la] = this.m_r_diag[li];
      la -= this.m_n;
    }

    return new Matrix(this.m_n, this.m_n, ltemp);
  }

  /**
   * returns the (economy-sized) orthogonal factor Q
   * 
   * @return Q
   */
  public Matrix get_q() {
    int li, lj, lk, la, lb, lc, ld, le;
    double ltemp[];
    double ls, lt;

    ltemp = new double[this.m_m * this.m_n];

    lk = this.m_n;
    lc = lk * lk;
    la = lc - lk;
    lk--;

    for (; lk >= 0; lk--) {
      ltemp[--lc] = 1.0;

      le = lc;
      for (lj = lk; lj < this.m_n; lj++) {

        if ((lt = this.m_data[lc]) != 0.0) {
          ls = 0.0;
          lb = le;
          ld = lc;
          for (li = lk; li < this.m_m; li++) {
            ls += this.m_data[ld] * ltemp[lb];
            lb += this.m_n;
            ld += this.m_n;
          }

          ls = -ls / lt;
          lb = le;
          ld = lc;
          for (li = lk; li < this.m_m; li++) {
            ltemp[lb] += ls * this.m_data[ld];
            lb += this.m_n;
            ld += this.m_n;
          }
        }

        le++;
      }

      la -= this.m_n;
      lc -= this.m_n;
    }

    return new Matrix(this.m_n, this.m_n, ltemp);
  }

}

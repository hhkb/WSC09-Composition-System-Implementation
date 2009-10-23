/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-30
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.math.matrix.CholeskyDecomposition.java
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
 * For a symmetric, positive definite matrix A, the Cholesky decomposition
 * is an lower triangular matrix L so that A = L*L'. If the matrix is not
 * symmetric or positive definite, the constructor returns a partial
 * decomposition and sets an internal flag that may be queried by the
 * is_symmetric_positive_definite() method.
 */
public final class CholeskyDecomposition implements Cloneable,
    Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /** the dimension of the matrix */
  private int m_n;

  /** the l triangular factor */
  private double[] m_l;

  /** true if the matrix was symmetric positive semi definite */
  private boolean m_isSPD;

  /**
   * constructor for cloning
   * 
   * @param m
   *          The this.m_n of the original matrix.
   * @param l
   *          The l-data of the original.
   * @param is_s_pd
   *          The this.m_is_s_pd of the original matrix.
   */
  private CholeskyDecomposition(int m, double[] l, boolean is_s_pd) {
    super();
    this.m_n = m;
    this.m_l = new double[m * m];
    System.arraycopy(l, 0, this.m_l, 0, m * m);
    this.m_isSPD = is_s_pd;
  }

  /**
   * constructor for cholesky-decomposition
   * 
   * @param mat
   *          The original matrix data.
   * @param m
   *          The height of the matrix.
   * @param n
   *          The width of the matrix.
   */
  public CholeskyDecomposition(double mat[], int m, int n) {
    int li, lj, lk, lz, la, lb, lc1, lc2;
    double ld, ls;

    this.m_n = lz = (n < m) ? n : m;
    this.m_isSPD = (m == n);
    this.m_l = new double[n * n];

    la = 0;
    for (lj = 0; lj < lz; lj++) {
      ld = 0.0;

      lb = 0;
      for (lk = 0; lk < lj; lk++) {
        ls = 0.0;

        lc1 = la;
        lc2 = lb;
        for (li = 0; li < lk; li++) {
          ls += this.m_l[lc1++] * this.m_l[lc2++];
        }

        ls = (mat[la + lk] - ls) / this.m_l[lb + lk];
        this.m_l[la + lk] = ls;
        ld += (ls * ls);
        this.m_isSPD &= (mat[lb + lj] == mat[la + lk]);

        lb += n;
      }

      ld = mat[la + lj] - ld;
      this.m_isSPD &= (ld > 0.0);
      this.m_l[la + lj] = Math.sqrt((ld > 0.0) ? ld : 0.0);

      la += n;
    }
  }

  /**
   * returns the triangular factor
   * 
   * @return L
   */
  public Matrix get_l() {
    return new Matrix(this.m_n, this.m_n, this.m_l, 0);
  }

  /**
   * returns wether the matrix is square, symmetric and positive definite
   * or not
   * 
   * @return true if A is square, symmetric and positive definite.
   */
  public boolean isSymmetricPositiveDefinite() {
    return this.m_isSPD;
  }

  /**
   * solves A * X = B and where the initial matrix is A, mat is B and the
   * result is to be put into result
   * 
   * @param m
   *          height of matrix B
   * @param n
   *          width of matrix B
   * @param mat
   *          the matrix B
   * @param result
   *          matrix X
   */
  public void solve(int m, int n, double mat[], double result[]) {
    int li, lj, lk, la, lb, lc, ld, le;
    double ls;

    if ((m != this.m_n) || (!this.m_isSPD))
      throw new ArithmeticException();

    li = m * n;
    System.arraycopy(mat, 0, result, 0, li);

    la = 0;
    for (lk = 0; lk < this.m_n; lk++) {
      lb = la + n;
      li = lk + 1;
      lc = (li * this.m_n) + lk;
      for (; li < this.m_n; li++) {
        ls = this.m_l[lc];

        ld = lb;
        le = la;
        for (lj = 0; lj < n; lj++) {
          result[ld++] -= result[le++] * ls;
        }
        lb += n;
        lc += this.m_n;
      }

      lb = la;
      ls = this.m_l[la + lk];
      for (lj = 0; lj < n; lj++) {
        result[lb++] /= ls;// this.m_l[(lk*this.m_n)+lk];
      }

      la += n;
    }

    lk = (this.m_n - 1);
    la = lk * n;
    lb = lk * this.m_n;
    for (; lk >= 0; lk--) {
      ls = this.m_l[lb + lk];
      lc = la;
      for (lj = 0; lj < n; lj++) {
        result[lc++] /= ls;// this.m_l[(lk*this.m_n)+lk];
      }

      lc = 0;
      for (li = 0; li < lk; li++) {
        ld = lc;
        le = la;
        ls = this.m_l[lb + li];
        for (lj = 0; lj < n; lj++) {
          result[ld++] -= result[le++] * ls;// this.m_l[(lk*this.m_n)+li];
        }
        lc += n;
      }
      la -= n;
      lb -= this.m_n;
    }
  }

  /**
   * creates a perfect copy of this decomposition
   * 
   * @return a copy of this decomposition
   */
  public CholeskyDecomposition copy() {
    return new CholeskyDecomposition(this.m_n, this.m_l, this.m_isSPD);
  }

  /**
   * the implementation of Cloneable.clone()
   * 
   * @return a copy of this decomposition
   */
  @Override
  public Object clone() {
    return copy();
  }

}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-30
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.math.matrix.SingularValueDecomposition.java
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

import org.sfc.math.Mathematics;

/**
 * The Singular-Value-decomposition here is inspired by the Jama package.
 * For an m-by-n matrix A with m >= n, the singular value decomposition is
 * an m-by-n orthogonal matrix U, an n-by-n diagonal matrix S, and an
 * n-by-n orthogonal matrix V so that A = U*S*V'. The singular values,
 * sigma[k] = S[k][k], are ordered so that sigma[0] >= sigma[1] >= ... >=
 * sigma[n-1]. The singular value decompostion always exists, so the
 * constructor will never fail. The matrix condition number and the
 * effective numerical rank can be computed from this decomposition.
 */
public final class SingularValueDecomposition implements Cloneable,
    Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /** the matrix height */
  private int m_m;

  /** the matrix width */
  private int m_n;

  /** the u matrix */
  private double[] m_u;

  /** the v matrix */
  private double[] m_v;

  /** the singular values */
  private double[] m_s;

  /** the matrix rank */
  private int m_rank;

  /**
   * private constructor for cloning
   * 
   * @param m
   *          The original's this.m_m member
   * @param n
   *          The original's this.m_n member
   * @param u
   *          The original's this.m_u member
   * @param v
   *          The original's this.m_v member
   * @param s
   *          The original's this.m_s member
   * @param rank
   *          The original's this.m_rank member
   */
  private SingularValueDecomposition(int m, int n, double[] u, double[] v,
      double[] s, int rank) {
    super();
    this.m_m = m;
    this.m_n = n;

    if (u != null) {
      this.m_u = new double[u.length];
      System.arraycopy(u, 0, this.m_u, 0, u.length);
    } else
      this.m_u = null;

    if (this.m_v != null) {
      this.m_v = new double[v.length];
      System.arraycopy(v, 0, this.m_v, 0, v.length);
    } else
      this.m_v = null;

    this.m_s = new double[s.length];
    System.arraycopy(s, 0, this.m_s, 0, s.length);

    this.m_rank = rank;
  }

  /**
   * this constructor creates the singular value decomposition of a matrix.
   * 
   * @param m
   *          height of the matrix
   * @param n
   *          width of the matrix
   * @param mat
   *          data of the matrix
   * @param wantu
   *          If the matrix u should be produced
   * @param wantv
   *          If the matrix v should be produced
   */
  public SingularValueDecomposition(int m, int n, double mat[],
      boolean wantu, boolean wantv) {
    super();

    int lnu, lnct, lnrt, li, lj, lk, lp, lpp, lkase, lks, lxa, lxb, lxc, lxd;
    double lt, lf, lcs, lsn, lspm1, lepm1, lsk, lek, lb, lc, lshift, lscale, lg, lsp, lya, lyb, lyc;
    double[] lmat, le, lwork;

    this.m_m = m;
    this.m_n = n;

    lnu = (m < n) ? m : n;
    lp = (m < n) ? (m + 1) : n;

    this.m_rank = -1;

    this.m_s = new double[lp];
    if (wantu)
      this.m_u = new double[m * lnu];
    else
      this.m_u = null;
    if (wantv)
      this.m_v = new double[n * n];
    else
      this.m_v = null;
    le = new double[n];
    lwork = new double[m];

    li = m * n;
    lmat = new double[li];
    System.arraycopy(mat, 0, lmat, 0, li);

    lnct = (m <= n) ? (m - 1) : n;

    lnrt = n - 2;
    if (m < lnrt)
      lnrt = m;
    else if (lnrt < 0)
      lnrt = 0;

    lxa = 0;
    for (lk = 0; lk < Math.max(lnct, lnrt); lk++) {
      if (lk < lnct) {
        lya = 0.0;
        lxb = lxa;

        for (li = lk; li < this.m_m; li++) {
          lya = Math.hypot(lya, lmat[lxb]);
          lxb += n;
        }

        if (lya != 0.0) {
          if (lmat[lxa] < 0.0) {
            lya = -lya;
          }

          lxb = lxa;
          for (li = lk; li < m; li++) {
            lmat[lxb] /= lya;
            lxb += n;
          }

          lmat[lxa] += 1.0;
        }

        this.m_s[lk] = -lya;
      }

      lxc = lxa + 1;
      for (lj = (lk + 1); lj < n; lj++) {
        if ((lk < lnct) && (this.m_s[lk] != 0.0)) {
          lt = 0.0;

          lxb = lxa;
          lxd = lxc;
          for (li = lk; li < m; li++) {
            lt += lmat[lxb] * lmat[lxd];
            lxb += n;
            lxd += n;
          }

          lt = -lt / lmat[lxa];

          lxb = lxa;
          lxd = lxc;
          for (li = lk; li < m; li++) {
            lmat[lxd] += lt * lmat[lxb];
            lxb += n;
            lxd += n;
          }
        }

        le[lj] = lmat[lxc];
        lxc++;
      }

      if (wantu && (lk < lnct)) {
        lxb = (lk * lnu) + lk;
        lxc = lxa;
        for (li = lk; li < m; li++) {
          this.m_u[lxb] = lmat[lxc];
          lxb += lnu;
          lxc += n;
        }
      }

      if (lk < lnrt) {
        lyb = le[lk + 1];
        lya = lyb;
        for (li = (lk + 2); li < n; li++) {
          lya = Math.hypot(lya, le[li]);
        }

        if (lya != 0.0) {
          if (lyb < 0.0) {
            lya = -lya;
          }

          lyb /= lya;
          for (li = (lk + 2); li < n; li++) {
            le[li] /= lya;
          }

          lyb += 1.0;
          le[lk + 1] = lyb;
        }

        le[lk] = -lya;

        if (((lk + 1) < m) && (lya != 0.0)) {
          for (li = (lk + 1); li < m; li++) {
            lwork[li] = 0.0;
          }

          lxb = lxa + n + 1;
          for (lj = (lk + 1); lj < n; lj++) {
            lyc = le[lj];
            lxc = lxb;
            for (li = (lk + 1); li < m; li++) {
              lwork[li] += lyc * lmat[lxc];
              lxc += n;
            }
            lxb++;
          }

          lxb = lxa + n + 1;
          for (lj = (lk + 1); lj < n; lj++) {
            lt = -le[lj] / lyb;
            lyc = le[lj];
            lxc = lxb;
            for (li = (lk + 1); li < m; li++) {
              lmat[lxc] += lt * lwork[li];
              lxc += n;
            }
            lxb++;
          }
        }

        if (wantv) {
          lxb = lxa + n;
          for (li = (lk + 1); li < n; li++) {
            this.m_v[lxb] = le[li];
            lxb += n;
          }
        }
      }

      lxa += n + 1;
    }

    if (lnct < n) {
      this.m_s[lnct] = lmat[(lnct * n) + lnct];
    }

    if (m < lp) {
      this.m_s[lp - 1] = 0.0;
    }

    if ((lnrt + 1) < lp) {
      le[lnrt] = lmat[(lnrt * n) + lp - 1];
    }

    le[lp - 1] = 0.0;

    if (wantu) {

      lxb = (lnct * lnu) + lnct;
      for (lj = lnct; lj < lnu; lj++) {
        lxa = lj;
        for (li = 0; li < m; li++) {
          this.m_u[lxa] = 0.0;
          lxa += lnu;
        }

        this.m_u[lxb] = 1.0;
        lxb += lnu + 1;
      }

      lk = lnct - 1;
      lxa = lk * lnu;
      for (; lk >= 0; lk--) {
        if (this.m_s[lk] != 0.0) {
          for (lj = (lk + 1); lj < lnu; lj++) {
            lt = 0.0;
            lxb = lxa;
            for (li = lk; li < m; li++) {
              lt += this.m_u[lxb + lk] * this.m_u[lxb + lj];
              lxb += lnu;
            }

            lt = -lt / this.m_u[lxa + lk];
            lxb = lxa;
            for (li = lk; li < m; li++) {
              this.m_u[lxb + lj] += lt * this.m_u[lxb + lk];
              lxb += lnu;
            }
          }

          lxb = lxa;
          for (li = lk; li < m; li++) {
            this.m_u[lxb + lk] = -this.m_u[lxb + lk];
            lxb += lnu;
          }

          this.m_u[lxa + lk] += 1.0;

          lxb = lk;
          for (li = 0; li < (lk - 1); li++) {
            this.m_u[lxb] = 0.0;
            lxb += lnu;
          }
        } else {
          lxb = lk;
          for (li = 0; li < m; li++) {
            this.m_u[lxb] = 0.0;
            lxb += lnu;
          }

          this.m_u[lxa + lk] = 1.0;
        }

        lxa -= lnu;
      }
    }

    if (wantv) {
      lxa = n * n;
      for (lk = (n - 1); lk >= 0; lk--) {
        if ((lk < lnrt) && (le[lk] != 0.0)) {
          for (lj = (lk + 1); lj < lnu; lj++) {
            lt = 0.0;
            lxb = lxa;
            for (li = (lk + 1); li < n; li++) {
              lt += this.m_v[lxb + lk] * this.m_v[lxb + lj];
              lxb += n;
            }

            lt = -lt / this.m_v[lxa + lk];

            lxb = lxa;
            for (li = (lk + 1); li < n; li++) {
              this.m_v[lxb + lj] += lt * this.m_v[lxb + lk];
              lxb += n;
            }
          }
        }

        lxb = lk;
        for (li = 0; li < n; li++) {
          this.m_v[lxb] = 0.0;
          lxb += n;
        }

        lxa -= n;
        this.m_v[lxa + lk] = 1.0;
      }
    }

    lpp = lp - 1;

    while (lp > 0) {

      for (lk = (lp - 2); lk >= 0; lk--) {
        if (Math.abs(le[lk]) <= (Mathematics.EPS * (Math.abs(this.m_s[lk]) + Math
            .abs(this.m_s[lk + 1])))) {
          le[lk] = 0.0;
          break;
        }
      }

      if (lk == (lp - 2)) {
        lkase = 4;
      } else {

        for (lks = (lp - 1); lks > lk; lks--) {
          lt = ((lks != lp) ? Math.abs(le[lks]) : 0.0)
              + ((lks != (lk + 1)) ? Math.abs(le[lks - 1]) : 0.0);

          if (Math.abs(this.m_s[lks]) <= (Mathematics.EPS * lt)) {
            this.m_s[lks] = 0.0;
            break;
          }
        }

        if (lks == lk) {
          lkase = 3;
        } else if (lks == (lp - 1)) {
          lkase = 1;
        } else {
          lkase = 2;
          lk = lks;
        }
      }

      lk++;

      switch (lkase) {

      case 1: {
        lf = le[lp - 2];
        le[lp - 2] = 0.0;

        for (lj = (lp - 2); lj >= lk; lj--) {
          lya = this.m_s[lj];
          lt = Math.hypot(lya, lf);
          lcs = lya / lt;
          lsn = lf / lt;
          this.m_s[lj] = lt;

          if (lj != lk) {
            lya = le[lj - 1];
            lf = -lsn * lya;
            le[lj - 1] = lya * lcs;
          }

          if (wantv) {
            lxa = lp - 1;
            lxb = lj;
            for (li = 0; li < n; li++) {
              lya = this.m_v[lxb];
              lyb = this.m_v[lxa];
              lt = (lcs * lya) + (lsn * lyb);
              this.m_v[lxa] = (lcs * lyb) - (lsn * lya);
              this.m_v[lxb] = lt;
              lxa += n;
              lxb += n;
            }
          }
        }

        break;
      }

      case 2: {
        lf = le[lk - 1];
        le[lk - 1] = 0.0;

        for (lj = lk; lj < lp; lj++) {
          lya = this.m_s[lj];
          lt = Math.hypot(lya, lf);
          lcs = lya / lt;
          lsn = lf / lt;

          this.m_s[lj] = lt;
          lya = le[lj];
          lf = -lsn * lya;
          le[lj] = lya * lcs;

          if (wantu) {
            lxa = lj;
            lxb = lk - 1;
            for (li = 0; li < m; li++) {
              lya = this.m_u[lxa];
              lyb = this.m_u[lxb];
              lt = (lcs * lya) + (lsn * lyb);
              this.m_u[lxb] = (lcs * lyb) - (lsn * lya);
              this.m_u[lxa] = lt;
              lxa += lnu;
              lxb += lnu;
            }
          }
        }

        break;
      }

      case 3: {
        lsp = this.m_s[lp - 1];
        lspm1 = this.m_s[lp - 2];
        lepm1 = le[lp - 2];
        lsk = this.m_s[lk];
        lek = le[lk];

        lscale = Math.max(Math.max(Math.max(Math.max(Math.abs(lsp), Math
            .abs(lspm1)), Math.abs(lsk)), Math.abs(lepm1)), Math.abs(lek));

        lsp /= lscale;
        lspm1 /= lscale;
        lepm1 /= lscale;
        lsk /= lscale;
        lek /= lscale;

        lb = (((lspm1 + lsp) * (lspm1 - lsp)) + (lepm1 * lepm1)) * 0.5;
        lc = (lsp * lepm1);
        lc *= lc;
        lshift = 0.0;

        if ((lb != 0.0) || (lc != 0.0)) {
          lshift = Math.sqrt((lb * lb) + lc);
          if (lb < 0.0)
            lshift = -lshift;
          lshift = lc / (lb + lshift);
        }

        lf = ((lsk + lsp) * (lsk - lsp)) + lshift;
        lg = lsk * lek;

        for (lj = lk; lj < (lp - 1); lj++) {
          lt = Math.hypot(lf, lg);
          lcs = lf / lt;
          lsn = lg / lt;

          if (lj != lk)
            le[lj - 1] = lt;

          lya = this.m_s[lj];
          lyb = le[lj];
          lf = (lcs * lya) + (lsn * lyb);

          le[lj] = (lcs * lyb) - (lsn * lya);

          lya = this.m_s[lj + 1];
          lg = lsn * lya;
          this.m_s[lj + 1] = lya * lcs;

          if (wantv) {
            lxa = lj;
            for (li = 0; li < n; li++) {
              lya = this.m_v[lxa];
              lyb = this.m_v[lxa + 1];

              lt = (lcs * lya) + (lsn * lyb);

              this.m_v[lxa + 1] = (lcs * lyb) - (lsn * lya);
              this.m_v[lxa] = lt;
              lxa += n;
            }
          }

          lt = Math.hypot(lf, lg);
          lcs = lf / lt;
          lsn = lg / lt;
          this.m_s[lj] = lt;

          lf = (lcs * le[lj]) + (lsn * this.m_s[lj + 1]);
          this.m_s[lj + 1] = (lcs * this.m_s[lj + 1]) - (lsn * le[lj]);
          lg = lsn * le[lj + 1];
          le[lj + 1] *= lcs;

          if (wantu && (lj < (m - 1))) {
            for (li = 0; li < m; li++) {
              lt = (lcs * this.m_u[(li * lnu) + lj])
                  + (lsn * this.m_u[(li * lnu) + lj + 1]);
              this.m_u[(li * lnu) + lj + 1] = (lcs * this.m_u[(li * lnu)
                  + lj + 1])
                  - (lsn * this.m_u[(li * lnu) + lj]);
              this.m_u[(li * lnu) + lj] = lt;
            }
          }
        }

        le[lp - 2] = lf;

        break;
      }

      default: {

        lya = this.m_s[lk];
        if (lya <= 0.0) {
          this.m_s[lk] = (lya < 0.0) ? -lya : 0.0;

          if (wantv) {
            lxa = lk;
            for (li = 0; li <= lpp; li++) {
              this.m_v[lxa] = -this.m_v[lxa];
              lxa += n;
            }
          }
        }

        while (lk < lpp) {
          lt = this.m_s[lk];
          lya = this.m_s[lk + 1];

          if (lt >= lya)
            break;

          this.m_s[lk] = lya;
          this.m_s[lk + 1] = lt;

          if (wantv && (lk < (n - 1))) {
            lxa = lk;

            for (li = 0; li < n; li++) {
              lt = this.m_v[lxa + 1];
              this.m_v[lxa + 1] = this.m_v[lxa];
              this.m_v[lxa] = lt;
              lxa += n;
            }
          }

          if (wantu && (lk < (m - 1))) {
            lxa = lk;
            for (li = 0; li < m; li++) {
              lt = this.m_u[lxa + 1];
              this.m_u[lxa + 1] = this.m_u[lxa];
              this.m_u[lxa] = lt;
              lxa += lnu;
            }
          }

          lk++;
        }

        lp--;

        break;
      }
      }
    }
  }

  /**
   * returns the matrix of the singular values
   * 
   * @return the matrix of the singular values
   */
  public Matrix getS() {
    double[] ls;
    int li, lj;

    lj = this.m_n;
    li = lj * lj;
    ls = new double[li--];

    for (--lj; lj >= 0; lj--) {
      ls[li] = this.m_s[lj];
      li -= (this.m_n + 1);
    }

    return new Matrix(this.m_n, this.m_n, ls);
  }

  /**
   * returns the matrix of the left singular vectors
   * 
   * @return the matrix of the left singular vectors
   */
  public Matrix getU() {
    return new Matrix(this.m_m, (this.m_m < this.m_n) ? (this.m_m + 1)
        : this.m_n, this.m_u);
  }

  /**
   * returns the matrix of the right singular vectors
   * 
   * @return the matrix of the right singular vectors
   */
  public Matrix getV() {
    return new Matrix(this.m_n, this.m_n, this.m_v);
  }

  /**
   * returns the intern array of the singular values
   * 
   * @return the intern array of the singular values
   */
  public double[] getSingularValues() {
    return this.m_s;
  }

  /**
   * returns the two norm
   * 
   * @return max{sigma[]}
   */
  public double norm2() {
    return this.m_s[0];
  }

  /**
   * returns the two norm condition number
   * 
   * @return max{sigma[]}/min{sigma[]}
   */
  public double conditionNumber() {
    return this.m_s[0]
        / this.m_s[(this.m_m < this.m_n) ? (this.m_m - 1) : (this.m_n - 1)];
  }

  /**
   * returns the effective numerical rank
   * 
   * @return the number of non-negligible singular values
   */
  public int rank() {
    int li;
    double ltol;

    ltol = Mathematics.EPS * ((this.m_m > this.m_n) ? this.m_m : this.m_n)
        * this.m_s[0];

    if (this.m_rank >= 0)
      return this.m_rank;

    this.m_rank = 0;
    for (li = (this.m_s.length - 1); li >= 0; li--) {
      if (this.m_s[li] > ltol)
        this.m_rank++;
    }

    return this.m_rank;
  }

  /**
   * creates a perfect copy of this decomposition
   * 
   * @return a copy of this decomposition
   */
  public SingularValueDecomposition copy() {
    return new SingularValueDecomposition(this.m_m, this.m_n, this.m_u,
        this.m_v, this.m_s, this.m_rank);
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

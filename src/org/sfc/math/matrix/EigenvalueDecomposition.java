/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-30
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.math.matrix.EigenvalueDecomposition.java
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
 * The Eigenvalue-decomposition here is inspired by the Jama package. If
 * matrix A is symmetric: For every eigenvector vi and eigenvalue di of
 * matrix A, the following statement is valid: vi * A = di * vi. The matrix
 * D is the matrix of the eigenvalues, which is diagonal. The eigenvalues
 * are placed on the diagonal. The matrix V is the matrix of the
 * eigenvectors. It is orthogonal, so that V^T = V^-1. It is possible to
 * diagonalize A by: D = V^T * A * V = V^-1 * A * V. If A is not symmetric:
 * The eigenvalue matrix D will be block-diagonal, with the real
 * eigenvalues in 1-by-1 blocks and any complex eigenvalues, a + i*b, in
 * 2-by-2 blocks, [a, b; -b, a]. The columns of V represent the
 * eigenvectors in the sense that A*V = V*D, i.e. A.mul(V) equals V.mul(D).
 * The matrix V may be badly conditioned, or even singular. The validity of
 * the equation A = V*D*inverse(V) depends upon V.condition_number().
 */
public final class EigenvalueDecomposition implements Cloneable,
    Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /** the x and y dimension of the matrix */
  private int m_l;

  /** is the matrix symmetric */
  private boolean m_symmetric;

  /** the v matrix */
  private double m_v[];

  /** real parts of the eigenvalues */
  private double m_real[];

  /** imaginary parts of the eigenvalues */
  private double m_imaginary[];

  /** transient calculation variable */
  private transient double m_r;

  /** transient calculation variable */
  private transient double m_i;

  /**
   * private constructor for cloning
   * 
   * @param l
   *          The this.m_l value of the original decomposition.
   * @param symmetric
   *          The this.m_symmetric value of the original decomposition.
   * @param v
   *          The this.m_v value of the original decomposition.
   * @param real
   *          The this.m_real value of the original decomposition.
   * @param imaginary
   *          The this.m_imaginary value of the original decomposition.
   */
  private EigenvalueDecomposition(int l, boolean symmetric, double v[],
      double real[], double imaginary[]) {
    super();
    this.m_l = l;
    this.m_symmetric = symmetric;
    this.m_v = new double[l * l];
    System.arraycopy(v, 0, this.m_v, 0, l * l);
    this.m_real = new double[l];
    System.arraycopy(real, 0, this.m_real, 0, l);
    this.m_imaginary = new double[l];
    System.arraycopy(imaginary, 0, this.m_imaginary, 0, l);
  }

  /**
   * constructor for eigenvalue-decomposition
   * 
   * @param mat
   *          the matrix data
   * @param l
   *          the matrix dimension
   */
  public EigenvalueDecomposition(double mat[], int l) {
    super();

    double ld[];
    double ls, lt;
    int li, lj, la, lb, lc, lx;

    this.m_l = l;
    this.m_real = new double[l];
    this.m_imaginary = new double[l];
    this.m_symmetric = true;

    la = l * l;
    ld = new double[la];
    la--;
    lb = la;
    lx = 0;
    main_loop: for (li = (l - 1); li >= 0; li--) {
      lc = lb--;
      la -= lx;
      ld[la] = mat[la];
      la--;
      lc -= this.m_l;
      for (lj = (li - 1); lj >= 0; lj--) {
        ld[la] = ls = mat[la];
        ld[lc] = lt = mat[lc];
        if (!(this.m_symmetric &= (lt == ls)))
          break main_loop;
        la--;
        lc -= this.m_l;
      }
      lx++;
      lb -= this.m_l;
    }

    if (la >= 0)
      System.arraycopy(mat, 0, ld, 0, la + 1);

    if (this.m_symmetric) {
      this.m_v = ld;

      triDiagonalize();
      diagonalize();
    } else {
      this.m_v = new double[l * l];
      hessenberg(ld);
      schur(ld);
      ld = null;
    }
  }

  /**
   * Symmetric Householder reduction to tridiagonal form. Yama package
   * named as source: "This is derived from the Algol procedures tred2 by
   * Bowdler, Martin, Reinsch, and Wilkinson, Handbook for Auto. Comp.,
   * Vol.ii-Linear Algebra, and the corresponding Fortran subroutine in
   * EISPACK."
   */
  private void triDiagonalize() {
    int li, lj, lk, la, lb, lc, ld, le, le2;
    double lscale, lh, lf, lg, lhh;

    li = this.m_l - 1;
    lc = li * this.m_l;
    System.arraycopy(this.m_v, lc, this.m_real, 0, this.m_l);

    for (; li > 0; li--) {
      lscale = 0.0;
      lh = 0.0;
      la = li - 1;

      for (lk = la; lk >= 0; lk--) {
        lscale += Math.abs(this.m_real[lk]);
      }

      if (lscale == 0.0) {
        this.m_imaginary[li] = this.m_real[la];

        la *= this.m_l;
        System.arraycopy(this.m_v, la, this.m_real, 0, li);
        la += this.m_l;
        lb = li;
        for (lj = 0; lj < li; lj++) {
          this.m_v[la++] = 0.0;
          this.m_v[lb] = 0.0;
          lb += this.m_l;
        }
      } else {
        for (lk = 0; lk < li; lk++) {
          lf = (this.m_real[lk] /= lscale);
          lh += lf * lf;
        }

        lf = this.m_real[la];
        lg = Math.sqrt(lh);

        if (lf > 0)
          lg = -lg;

        this.m_imaginary[li] = lscale * lg;
        lh -= lf * lg;
        this.m_real[la] = lf - lg;

        for (lj = 0; lj < li; lj++) {
          this.m_imaginary[lj] = 0.0;
        }

        lb = li;
        for (lj = 0; lj < li; lj++) {
          lf = this.m_real[lj];
          this.m_v[lb] = lf;
          lg = this.m_imaginary[lj]
              + (this.m_v[(lj * this.m_l) + lj] * lf);
          la = lb - li + this.m_l + lj;

          for (lk = (lj + 1); lk < li; lk++) {
            lg += this.m_v[la] * this.m_real[lk];
            this.m_imaginary[lk] += (this.m_v[la] * lf);
            la += this.m_l;
          }

          this.m_imaginary[lj] = lg;
          lb += this.m_l;
        }

        lf = 0.0;

        for (lj = 0; lj < li; lj++) {
          this.m_imaginary[lj] /= lh;
          lf += this.m_imaginary[lj] * this.m_real[lj];
        }

        lhh = lf / (lh + lh);

        for (lj = 0; lj < li; lj++) {
          this.m_imaginary[lj] -= lhh * this.m_real[lj];
        }

        la = 0;
        ld = lc;
        for (lj = 0; lj < li; lj++) {
          lf = this.m_real[lj];
          lg = this.m_imaginary[lj];
          lb = la++;
          for (lk = lj; lk < li; lk++) {
            this.m_v[lb] -= (lf * this.m_imaginary[lk])
                + (lg * this.m_real[lk]);
            lb += this.m_l;
          }

          la += this.m_l;
          this.m_v[ld] = 0.0;
          this.m_real[lj] = this.m_v[(ld++) - this.m_l];

        }
      }

      this.m_real[li] = lh;
      lc -= this.m_l;
    }

    la = 0;
    le = 1;
    le2 = (this.m_l - 1) * this.m_l;
    for (li = 0; li < (this.m_l - 1); li++) {
      this.m_v[le2] = this.m_v[la];
      le2++;
      this.m_v[la] = 1.0;

      lh = this.m_real[li + 1];
      if (lh != 0.0) {
        lb = le;
        for (lk = 0; lk <= li; lk++) {
          this.m_real[lk] = this.m_v[lb] / lh;
          lb += this.m_l;
        }

        for (lj = 0; lj <= li; lj++) {
          lg = 0.0;

          lc = lj;
          ld = le;
          for (lk = 0; lk <= li; lk++) {
            lg += this.m_v[ld] * this.m_v[lc];
            ld += this.m_l;
            lc += this.m_l;
          }

          lc = lj;
          for (lk = 0; lk <= li; lk++) {
            this.m_v[lc] -= lg * this.m_real[lk];
            lc += this.m_l;
          }

          lb += this.m_l + 1;
        }
      }

      ld = le;
      for (lk = 0; lk <= li; lk++) {
        this.m_v[ld] = 0.0;
        ld += this.m_l;
      }

      la += this.m_l + 1;
      le++;
    }

    le2 = (this.m_l - 1) * this.m_l;
    for (lj = 0; lj < this.m_l; lj++) {
      this.m_real[lj] = this.m_v[le2];
      this.m_v[le2++] = 0.0;
    }

    this.m_v[le2 - 1] = 1.0;
    this.m_imaginary[0] = 0.0;
  }

  /**
   * Symmetric tridiagonal QL algorithm. Yama package named as source:
   * "This is derived from the Algol procedures tql2 by Bowdler, Martin,
   * Reinsch, and Wilkinson, Handbook for Auto. Comp., Vol.ii-Linear
   * Algebra, and the corresponding Fortran subroutine in EISPACK."
   */
  private void diagonalize() {
    int li, lj, lk, ll, lm, liter, laa, lbb, lcc;
    double lf, ltst1, lg, lp, lr, lc, lc2, lc3, lel1, ls, ls2, ldl1, lh;

    System.arraycopy(this.m_imaginary, 1, this.m_imaginary, 0,
        this.m_l - 1);
    this.m_imaginary[this.m_l - 1] = 0.0;

    lf = 0.0;
    ltst1 = 0.0;
    for (ll = 0; ll < this.m_l; ll++) {
      ltst1 = Math.max(ltst1, Math.abs(this.m_real[ll])
          + Math.abs(this.m_imaginary[ll]));
      lm = ll;

      while (lm < this.m_l) {
        if (Math.abs(this.m_imaginary[lm]) <= (Mathematics.EPS * ltst1))
          break;
        lm++;
      }

      if (lm > ll) {
        liter = 0;

        do {
          liter++;
          lg = this.m_real[ll];
          lp = (this.m_real[ll + 1] - lg) / (2.0 * this.m_imaginary[ll]);
          lr = Math.hypot(lp, 1.0);

          if (lp < 0.0)
            lr = -lr;

          this.m_real[ll] = lh = this.m_imaginary[ll] / (lp + lr);
          this.m_real[ll + 1] = ldl1 = this.m_imaginary[ll] * (lp + lr);

          lh = lg - lh;

          for (li = (ll + 2); li < this.m_l; li++) {
            this.m_real[li] -= lh;
          }

          lf += lh;
          lp = this.m_real[lm];
          lc = 1.0;
          lc2 = lc;
          lc3 = lc;
          lel1 = this.m_imaginary[ll + 1];
          ls = 0.0;
          ls2 = ls;

          laa = lm;
          for (li = (laa - 1); li >= ll; li--) {
            lc3 = lc2;
            lc2 = lc;
            ls2 = ls;
            lg = lc * this.m_imaginary[li];
            lh = lc * lp;
            lr = Math.hypot(lp, this.m_imaginary[li]);
            this.m_imaginary[li + 1] = ls * lr;
            ls = this.m_imaginary[li] / lr;
            lc = lp / lr;
            lp = (lc * this.m_real[li]) - (ls * lg);
            this.m_real[li + 1] = lh
                + (ls * ((lc * lg) + (ls * this.m_real[li])));

            lbb = laa;
            lcc = laa - 1;
            for (lk = 0; lk < this.m_l; lk++) {
              lh = this.m_v[lbb];
              this.m_v[lbb] = (ls * this.m_v[lcc]) + (lc * lh);
              this.m_v[lcc] = (lc * this.m_v[lcc]) - (ls * lh);
              lbb += this.m_l;
              lcc += this.m_l;
            }

            laa--;
          }

          lp = -ls * ls2 * lc3 * lel1 * this.m_imaginary[ll] / ldl1;
          this.m_imaginary[ll] = ls * lp;
          this.m_real[ll] = lc * lp;

        } while (Math.abs(this.m_imaginary[ll]) > (Mathematics.EPS * ltst1));

      }

      this.m_real[ll] += lf;
      this.m_imaginary[ll] = 0.0;
    }

    laa = this.m_l - 1;
    for (li = 0; li < (this.m_l - 1); li++) {
      lk = li;
      lp = lc = this.m_real[li];

      for (lj = (li + 1); lj < this.m_l; lj++) {
        if ((ls = this.m_real[lj]) < lp) {
          lk = lj;
          lp = ls;
        }
      }

      if (lk != li) {
        this.m_real[lk] = lc;
        this.m_real[li] = lp;

        lbb = laa * this.m_l;
        lcc = lbb + li;
        lbb += lk;
        for (lj = laa; lj >= 0; lj--) {
          lp = this.m_v[lcc];
          this.m_v[lcc] = this.m_v[lbb];
          this.m_v[lbb] = lp;
          lcc -= this.m_l;
          lbb -= this.m_l;
        }
      }
    }
  }

  /**
   * Nonsymmetric reduction to Hessenberg form. Yama package named as
   * source: "This is derived from the Algol procedures orthes and ortran,
   * by Martin and Wilkinson, Handbook for Auto. Comp., Vol.ii-Linear
   * Algebra, and the corresponding Fortran subroutines in EISPACK."
   * 
   * @param mat
   *          The matrix data
   */
  private void hessenberg(double mat[]) {
    int lhigh, lm, li, lj, la, lb, lc;
    double lscale, lh, lg, lf;
    double lo[];

    la = this.m_l;
    lo = new double[la];

    lhigh = la - 1;

    for (lm = 1; lm < lhigh; lm++) {
      lscale = 0.0;

      lb = la + lm - 1;
      for (li = lm; li <= lhigh; li++) {
        lscale += Math.abs(mat[lb]);
        lb += this.m_l;
      }

      if (lscale != 0.0) {
        lh = 0.0;

        lb = (lhigh * this.m_l) + lm - 1;
        for (li = lhigh; li >= lm; li--) {
          lo[li] = lg = (mat[lb] / lscale);
          lb -= this.m_l;
          lh += lg * lg;
        }

        lg = Math.sqrt(lh);
        if ((lf = lo[lm]) > 0.0)
          lg = -lg;

        lh -= lf * lg;
        lo[lm] = lf - lg;

        lb = (lhigh * this.m_l) + lm;
        for (lj = lm; lj < this.m_l; lj++) {
          lf = 0.0;
          lc = lb;
          for (li = lhigh; li >= lm; li--) {
            lf += lo[li] * mat[lc];
            lc -= this.m_l;
          }

          lf /= lh;
          for (li = lm; li <= lhigh; li++) {
            lc += this.m_l;
            mat[lc] -= lf * lo[li];
          }
          lb++;
        }

        lb = 0;
        for (li = 0; li <= lhigh; li++) {
          lf = 0.0;
          lc = lb + lhigh;
          for (lj = lhigh; lj >= lm; lj--) {
            lf += lo[lj] * mat[lc];
            lc--;
          }

          lf /= lh;
          for (lj = lm; lj <= lhigh; lj++) {
            mat[++lc] -= lf * lo[lj];
          }

          lb += this.m_l;
        }

        lo[lm] *= lscale;
        mat[la + lm - 1] = lscale * lg;
      }

      la += this.m_l;
    }

    la += this.m_l - 1;
    for (li = lhigh; li >= 0; li--) {
      this.m_v[la] = 1.0;
      la -= this.m_l + 1;
    }

    lm = (lhigh - 1);
    la = lm * this.m_l;
    lb = la + lm - 1;
    for (; lm > 0; lm--) {
      if (mat[lb] != 0.0) {
        lc = lb;
        for (li = (lm + 1); li <= lhigh; li++) {
          lo[li] = mat[lc += this.m_l];
        }

        for (lj = lm; lj <= lhigh; lj++) {
          lg = 0.0;
          lc = la + lj;
          for (li = lm; li <= lhigh; li++) {
            lg += lo[li] * this.m_v[lc];
            lc += this.m_l;
          }

          lg = (lg / lo[lm]) / mat[lb];

          for (li = lhigh; li >= lm; li--) {
            lc -= this.m_l;
            this.m_v[lc] += lg * lo[li];
          }
        }
      }
      la -= this.m_l;
      lb -= this.m_l + 1;
    }
  }

  /**
   * complex scalar division
   * 
   * @param r1
   *          the real part of the 1st number
   * @param i1
   *          the imaginary part of the 1st number
   * @param r2
   *          the real part of the 2nd number
   * @param i2
   *          the imaginary part of the 2nd number
   */
  private void div(double r1, double i1, double r2, double i2) {
    double lr, ld;

    if (Math.abs(r2) > Math.abs(i2)) {
      lr = i2 / r2;
      ld = r2 + (lr * i2);
      this.m_r = (r1 + (lr * i1)) / ld;
      this.m_i = (i1 - (lr * r1)) / ld;
    } else {
      lr = r2 / i2;
      ld = i2 + (lr * r2);
      this.m_r = ((lr * r1) + i1) / ld;
      this.m_i = ((lr * i1) - r1) / ld;
    }
  }

  /**
   * Nonsymmetric reduction from Hessenberg to real Schur form. Yama
   * package named as source: "This is derived from the Algol procedure
   * hqr2, by Martin and Wilkinson, Handbook for Auto. Comp., Vol.ii-Linear
   * Algebra, and the corresponding Fortran subroutines in EISPACK."
   * 
   * @param mat
   *          The matrix data
   */
  private void schur(double mat[]) {
    int ln, lhigh, lm, li, lj, ll, liter, lk, la, lb, lc, ld, le;
    double lexshift, lp, lq, lr, ls, lt, lu, lv, lw, lx, ly, lz, lv2, lnorm, lra, lsa, lvr, lvi;
    boolean lnot_last;

    la = this.m_l;
    lhigh = ln = la - 1;
    lnorm = lexshift = lp = lq = lr = ls = lz = 0.0;

    la = (la * la) - 1;
    lb = lhigh;
    lc = 1;
    for (li = lhigh; li > 0; li--) {
      for (lj = lc++; lj >= 0; lj--) {
        lnorm += Math.abs(mat[la--]);
      }
      la -= --lb;
    }

    for (lj = lhigh; lj >= 0; lj--) {
      lnorm += Math.abs(mat[la--]);
    }

    liter = 0;
    la = ln * this.m_l;
    while (ln >= 0) {
      ll = ln;
      lb = la + ln;
      lc = lb - this.m_l - 1;
      while (ll > 0) {
        ls = Math.abs(mat[lc]) + Math.abs(mat[lb]);

        if (ls == 0.0)
          ls = lnorm;

        if (Math.abs(mat[lb - 1]) < (Mathematics.EPS * ls))
          break;

        ll--;
        lb = lc;
        lc -= this.m_l + 1;
      }

      if (ll == ln) {
        lv = mat[lb] + lexshift;
        mat[lb] = lv;
        this.m_real[ll] = lv;
        this.m_imaginary[ll] = 0.0;
        ln--;
        la -= this.m_l;
        liter = 0;
      } else if (ll == (ln - 1)) {
        lb += this.m_l + 1;
        lc += this.m_l + 1;

        lw = mat[lb - 1] * mat[lc + 1];
        lp = (mat[lc] - mat[lb]) * 0.5;
        lq = (lp * lp) + lw;
        lz = Math.sqrt(Math.abs(lq));
        lx = mat[lb] + lexshift;
        mat[lb] = lx;
        mat[lc] += lexshift;

        if (lq >= 0.0) {
          if (lp >= 0.0)
            lz += lp;
          else
            lz = lp - lz;

          lv = lx + lz;
          this.m_real[ll] = lv;

          if (lz != 0.0)
            this.m_real[ln] = lx - (lw / lz);
          else
            this.m_real[ln] = lv;

          this.m_imaginary[ll] = 0.0;
          this.m_imaginary[ln] = 0.0;

          lx = mat[--lb];
          ls = Math.abs(lx) + Math.abs(lz);
          lp = lx / ls;
          lq = lz / ls;
          lr = Math.sqrt((lp * lp) + (lq * lq));
          lp /= lr;
          lq /= lr;

          for (lj = ll; lj <= lhigh; lj++) {
            lz = mat[lc];
            lv = mat[lb];
            mat[lc++] = (lq * lz) + (lp * lv);
            mat[lb++] = (lq * lv) - (lp * lz);
          }

          lb = ll;
          for (li = 0; li <= ln; li++) {
            lz = mat[lb];
            lv = mat[lb + 1];
            mat[lb] = (lq * lz) + (lp * lv);
            mat[lb + 1] = (lq * lv) - (lp * lz);
            lb += this.m_l;
          }

          lb = ll;
          for (li = 0; li <= lhigh; li++) {
            lz = this.m_v[lb];
            lv = this.m_v[lb + 1];
            this.m_v[lb] = (lq * lz) + (lp * lv);
            this.m_v[lb + 1] = (lq * lv) - (lp * lz);
            lb += this.m_l;
          }
        } else {
          lv = lx + lp;
          this.m_real[ll] = lv;
          this.m_real[ln] = lv;
          this.m_imaginary[ll] = lz;
          this.m_imaginary[ln] = -lz;
        }

        ln -= 2;
        la -= (this.m_l << 1);
        liter = 0;
      } else {
        lb = la + ln;

        lx = mat[lb];
        ly = 0.0;
        lw = 0.0;

        if (ll < ln) {
          lc = (--lb) - this.m_l;
          ly = mat[lc];
          lw = mat[lb] * mat[lc + 1];
        }

        if (liter == 10) {
          lexshift += lx;

          lb = (this.m_l * ln) + ln;
          for (li = lb; li >= 0; li -= (this.m_l + 1)) {
            mat[li] -= lx;
          }

          ls = Math.abs(mat[--lb]) + Math.abs(mat[lb - this.m_l - 1]);

          lx = ly = 0.75 * ls;
          lw = -0.4375 * ls * ls;
        }

        if (liter == 30) {
          ls = (ly - lx) * 0.5;
          ls = (ls * ls) + lw;

          if (ls > 0.0) {
            ls = Math.sqrt(ls);
            if (ly < lx)
              ls = -ls;
            ls = lx - (lw / (((ly - lx) * 0.5) + ls));

            for (li = (this.m_l * ln) + ln; li >= 0; li -= (this.m_l + 1)) {
              mat[li] -= ls;
            }

            lexshift += ls;
            lx = ly = lw = 0.964;
          }
        }

        liter++;

        lm = ln - 2;
        lc = la - this.m_l + lm;
        lb = lc - this.m_l;
        lc++;
        while (lm >= ll) {
          lz = mat[lb];
          lr = lx - lz;
          ls = ly - lz;
          lp = (((lr * ls) - lw) / mat[lc - 1]) + mat[lb + 1];
          lq = mat[lc] - lz - lr - ls;
          lr = mat[lc + this.m_l];
          ls = Math.abs(lp) + Math.abs(lq) + Math.abs(lr);
          lp /= ls;
          lq /= ls;
          lr /= ls;

          if (lm == ll)
            break;

          if ((Math.abs(mat[lb - 1]) * (Math.abs(lq) + Math.abs(lr))) < (Mathematics.EPS * (Math
              .abs(lp) * (Math.abs(mat[lb - this.m_l - 1]) + Math.abs(lz) + Math
              .abs(mat[lc])))))
            break;

          lm--;
          lc = lb;
          lb -= this.m_l + 1;
        }

        li = lm + 2;
        lb = (li * this.m_l) + lm;
        mat[lb] = 0.0;
        li++;
        for (; li <= ln; li++) {
          lb += this.m_l + 1;
          mat[lb] = 0.0;
          mat[lb - 1] = 0.0;
        }

        lb = (lm * this.m_l) + lm - 1;
        for (lk = lm; lk < ln; lk++) {
          lnot_last = (lk != (ln - 1));
          if (lk != lm) {
            lp = mat[lb];
            lq = mat[lb + this.m_l];
            lr = (lnot_last ? mat[lb + (this.m_l << 1)] : 0.0);
            lx = Math.abs(lp) + Math.abs(lq) + Math.abs(lr);

            if (lx != 0.0) {
              lp /= lx;
              lq /= lx;
              lr /= lx;
            }
          }

          if (lx == 0.0)
            break;

          ls = Math.sqrt((lp * lp) + (lq * lq) + (lr * lr));
          if (lp < 0.0)
            ls = -ls;

          if (ls != 0.0) {
            if (lk != lm)
              mat[lb] = -(ls * lx);
            else if (ll != lm)
              mat[lb] = -mat[(lk * this.m_l) + lk - 1];

            lp += ls;
            lx = lp / ls;
            ly = lq / ls;
            lz = lr / ls;
            lq /= lp;
            lr /= lp;

            lc = lb + 1;
            ld = lc + this.m_l;
            le = ld + this.m_l;
            for (lj = lk; lj <= lhigh; lj++) {
              lv = mat[lc];
              lv2 = mat[ld];
              lp = lv + (lq * lv2);

              if (lnot_last) {
                lu = mat[le];
                lp += lr * lu;
                mat[le] = lu - (lp * lz);
                le++;
              }

              mat[lc] = lv - (lp * lx);
              mat[ld] = lv2 - (lp * ly);
              lc++;
              ld++;
            }

            li = lk + 3;
            if (ln < li) {
              li = ln;
              lc = la + lk;
            } else
              lc = (li * this.m_l) + lk;

            for (; li >= 0; li--) {
              lv = mat[lc];
              lv2 = mat[lc + 1];
              lp = (lv * lx) + (ly * lv2);

              if (lnot_last) {
                lu = mat[lc + 2];
                lp += lz * lu;
                mat[lc + 2] = lu - (lp * lr);
              }

              mat[lc] = lv - lp;
              mat[lc + 1] = lv2 - (lp * lq);
              lc -= this.m_l;
            }

            lc = lk;
            for (li = 0; li <= lhigh; li++) {
              lv = this.m_v[lc];
              lv2 = this.m_v[lc + 1];
              lp = (lx * lv) + (ly * lv2);

              if (lnot_last) {
                lu = this.m_v[lc + 2];
                lp += lz * lu;
                this.m_v[lc + 2] = lu - (lp * lr);
              }

              this.m_v[lc] = lv - lp;
              this.m_v[lc + 1] = lv2 - (lp * lq);
              lc += this.m_l;
            }
          }

          lb += this.m_l + 1;
        }
      }
    }

    if (lnorm == 0.0)
      return;

    la = (lhigh * this.m_l) + lhigh;
    for (ln = lhigh; ln >= 0; ln--) {
      lp = this.m_real[ln];
      lq = this.m_imaginary[ln];

      if (lq == 0.0) {
        ll = ln;
        mat[la] = 1.0;

        lb = la - this.m_l - 1;
        for (li = (ln - 1); li >= 0; li--) {
          lw = mat[lb] - lp;
          lr = 0.0;

          lc = lb - li + ll;
          ld = ln + (ll * this.m_l);
          for (lj = ll; lj <= ln; lj++) {
            lr += mat[lc++] * mat[ld];
            ld += this.m_l;
          }

          lv = this.m_imaginary[li];
          if (lv < 0.0) {
            lz = lw;
            ls = lr;
          } else {
            ll = li;
            lc = lb - li + ln;
            if (lv == 0.0) {
              if (lw != 0.0)
                mat[lc] = -(lr / lw);
              else
                mat[lc] = -(lr / (Mathematics.EPS * lnorm));
            } else {
              lx = mat[lb + 1];
              ly = mat[lb + this.m_l];
              lv = this.m_real[li] - lp;
              lv2 = this.m_imaginary[li];
              lq = (lv * lv) + (lv2 * lv2);
              lt = ((lx * ls) - (lz * lr)) / lq;
              mat[lc] = lt;
              if (Math.abs(lx) > Math.abs(lz))
                mat[lc + this.m_l] = ((-lr) - (lw * lt)) / lx;
              else
                mat[lc + this.m_l] = ((-ls) - (ly * lt)) / lz;
            }

            lt = Math.abs(mat[lc]);
            if (((Mathematics.EPS * lt) * lt) > 1.0) {
              for (lj = li; lj <= ln; lj++) {
                mat[lc] /= lt;
                lc += this.m_l;
              }
            }
          }
          lb -= this.m_l + 1;
        }
      } else if (lq < 0.0) {
        ll = ln - 1;
        lb = la - this.m_l;
        if (Math.abs((lv = mat[la - 1])) > Math.abs((lv2 = mat[lb]))) {
          mat[lb - 1] = lq / lv;
          mat[lb] = -(mat[la] - lp) / lv;
        } else {
          div(0.0, -lv2, mat[lb - 1] - lp, lq);
          mat[lb - 1] = this.m_r;
          mat[lb] = this.m_i;
        }

        mat[la - 1] = 0.0;
        mat[la] = 1.0;

        lb = la - (this.m_l << 1) - ln;
        for (li = (ln - 2); li >= 0; li--) {
          lra = 0.0;
          lsa = 0.0;

          lc = lb + ll;
          ld = (ll * this.m_l) + ln;
          for (lj = ll; lj <= ln; lj++) {
            lv = mat[lc++];
            lra += lv * mat[ld - 1];
            lsa += lv * mat[ld];
            ld += this.m_l;
          }

          lw = mat[lb + li] - lp;
          lc = lb + ln;
          if (this.m_imaginary[li] < 0.0) {
            lz = lw;
            lr = lra;
            ls = lsa;
          } else {
            ll = li;
            if (this.m_imaginary[li] == 0.0) {
              div(-lra, -lsa, lw, lq);
              mat[lc - 1] = this.m_r;
              mat[lc] = this.m_i;
            } else {
              lx = mat[lb + li + 1];
              ly = mat[lb + this.m_l + li];
              lv = this.m_real[li] - lp;
              lv2 = this.m_imaginary[li];
              lvr = (lv * lv) + (lv2 * lv2) - (lq * lq);
              lvi = (lv + lv) * lq;

              if ((lvr == 0.0) && (lvi == 0.0)) {
                lvr = Mathematics.EPS
                    * lnorm
                    * (Math.abs(lw) + Math.abs(lq) + Math.abs(lx)
                        + Math.abs(ly) + Math.abs(lz));
              }

              div((lx * lr) - (lz * lra) + (lq * lsa), (lx * ls)
                  - (lz * lsa) - (lq * lra), lvr, lvi);

              mat[lc - 1] = lv = this.m_r;
              mat[lc] = lv2 = this.m_i;

              if (Math.abs(lx) > (Math.abs(lz) + Math.abs(lq))) {
                mat[lc + this.m_l - 1] = ((-lra) - (lw * lv) + (lq * lv2))
                    / lx;
                mat[lc + this.m_l] = ((-lsa) - (lw * lv2) - (lq * lv))
                    / lx;
              } else {
                div((-lr) - (ly * lv), (-ls) - (ly * lv2), lz, lq);
                mat[lc + this.m_l - 1] = this.m_r;
                mat[lc + this.m_l] = this.m_i;
              }
            }

            lt = Math.max(Math.abs(mat[lc - 1]), Math.abs(mat[lc]));

            if (((Mathematics.EPS * lt) * lt) > 1.0) {
              ld = lc;

              for (lj = li; lj <= ln; lj++) {
                mat[ld - 1] /= lt;
                mat[ld] /= lt;
                ld += this.m_l;
              }
            }
          }

          lb -= this.m_l;
        }
      }

      la -= this.m_l + 1;
    }

    for (lj = lhigh; lj >= 0; lj--) {
      lb = 0;
      for (li = 0; li <= lhigh; li++) {
        lz = 0.0;
        la = lj;
        lc = lb;
        for (lk = 0; lk <= lj; lk++) {
          lz += this.m_v[lc++] * mat[la];
          la += this.m_l;
        }
        this.m_v[lc - 1] = lz;
        lb += this.m_l;
      }
    }
  }

  /**
   * returns the matrix v of the eigenvectors
   * 
   * @return the matrix of the eigenvectors
   */
  public Matrix getEigenvectors() {
    return new Matrix(this.m_l, this.m_l, this.m_v, 0);
  }

  /**
   * returns the real parts of the eigenvalues
   * 
   * @return the real parts of the eigenvalues
   */
  public double[] getRealEigenvaluesDirect() {
    return this.m_real;
  }

  /**
   * returns the imaginary parts of the eigenvalues
   * 
   * @return the imaginary parts of the eigenvalues
   */
  public double[] getImaginaryEigenvaluesDirect() {
    return this.m_imaginary;
  }

  /**
   * returns the block diagonal matrix of the eigenvalues
   * 
   * @return the block diagonal matrix of the eigenvalues
   */
  public Matrix getEigenvalues() {
    double[] ldata;
    double ld;
    int li;

    ldata = new double[this.m_l * this.m_l];
    for (li = 0; li < this.m_l; li++) {
      ldata[(li * this.m_l) + li] = this.m_real[li];
      ld = this.m_imaginary[li];
      if (ld > 0.0)
        ldata[(li * this.m_l) + li + 1] = ld;
      else if (ld < 0.0)
        ldata[(li * this.m_l) + li - 1] = ld;
    }

    return new Matrix(this.m_l, this.m_l, ldata);
  }

  /**
   * returns a copy of this decomposition
   * 
   * @return a copy of this EigenvalueDecomposition
   */
  public EigenvalueDecomposition copy() {
    return new EigenvalueDecomposition(this.m_l, this.m_symmetric,
        this.m_v, this.m_real, this.m_imaginary);
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
   * calculates the determinante as the product of the eigenvalues
   * 
   * @return det(A)
   */
  public double det() {
    double lp, lr;
    int li;

    lp = 1.0;
    for (li = (this.m_real.length - 1); li >= 0; li--) {
      lp *= lr = this.m_real[li];
      if (Math.abs(this.m_imaginary[li]) > (Mathematics.EPS * Math.abs(lr)))
        throw new ArithmeticException();
    }

    return lp;
  }

}

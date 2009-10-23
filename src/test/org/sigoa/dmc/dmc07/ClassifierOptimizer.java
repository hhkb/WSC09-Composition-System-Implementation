/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-24
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.dmc.ClassifierOptimizer.java
 * Last modification: 2007-05-24
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

import java.io.File;
import java.io.Writer;

import org.sfc.io.IO;
import org.sfc.text.TextUtils;

/**
 * The rule optimizer.
 *
 * @author Thomas Weise
 */
public final class ClassifierOptimizer {

  /**
   * This method optimizes a classifier.
   *
   * @param c
   *          the classifier to be optimized
   * @return the optimized classifier
   */
  public static final ClassifierSystem optimize(final ClassifierSystem c) {
    byte[][] rules, ru;
    EClasses[] results, re;
    int i, j, l, best, x;
    boolean b;
    ClassificationSimulation cs;
    ClassifierSystem res, f;
    byte bt;

    rules = c.getRules();
    results = c.getResults();
    cs = new ClassificationSimulation();
    cs.beginIndividual(c);
    cs.endIndividual();
    best = cs.getProfit();
    res = c;
    main: do {
      b = false;
      l = rules.length;

      for (i = (l - 1); i >= 0; i--) {
        ru = new byte[l - 1][ClassifierSystem.DATASET_LEN];
        re = new EClasses[l - 1];

        if (i > 0)
          System.arraycopy(results, 0, re, 0, i);
        System.arraycopy(results, i + 1, re, i, l - i - 1);

        for (j = (l - 1); j >= 0; j--) {
          if (j != i) {
            if (j > i)
              ru[j - 1] = rules[i];
            else
              ru[j] = rules[i];
          }
        }

        f = new ClassifierSystem(ru, re);
        cs.beginIndividual(f);
        cs.endIndividual();
        x = cs.getProfit();
        if (x >= best) {
          best = x;
          res = f;
          rules = ru;
          results = re;
          b = true;
          continue main;
        }
      }

      ru = rules.clone();
      re = results.clone();
      /*inner: */for (i = rules.length - 1; i > 0; i--) {
        for (j = (i - 1); j > 0; j--) {
          ru[i] = rules[j];
          ru[j] = rules[i];
          re[i] = results[j];
          re[j] = results[i];

          f = new ClassifierSystem(ru, results);
          cs.beginIndividual(f);
          cs.endIndividual();
          x = cs.getProfit();

          if (x > best) {
            best = x;
            res = f;
            rules = ru;
            ru = rules.clone();
            ru[i] = rules[i].clone();
            b = true;

            System.out.println(best);
            echo(f);
            rules = ru;
            results = re;
//            break inner;
          }

          ru[i] = rules[i];
          ru[j] = rules[j];
          re[i] = results[i];
          re[j] = results[j];
        }
      }

      ru = rules.clone();
      for (i = (rules.length - 1); i >= 0; i--) {
        ru[i] = ru[i].clone();

        for (j = (ClassifierSystem.DATASET_LEN - 1); j >= 0; j--) {

          for (bt = 3; bt >= 0; bt--) {
            ru[i][j] = bt;
            f = new ClassifierSystem(ru, results);
            cs.beginIndividual(f);
            cs.endIndividual();

            x = cs.getProfit();
            if (x > best) {
              best = x;
              res = f;
              rules = ru;
              ru = rules.clone();
              ru[i] = rules[i].clone();
              b = true;

              System.out.println(best);
              echo(f);
              // continue main;
            }
          }

          ru[i][j] = rules[i][j];
        }

      }
    } while (b);

    return res;
  }

  /**
   * the counter
   */
  static int s_i = 0;

  /**
   * Echo a classifier
   *
   * @param c
   *          the classifier
   */
  public static final void echo(final ClassifierSystem c) {
    File f;
    Writer w;
    for (;; s_i++) {
      f = IO.getFile("E:\\test\\input\\OPT" + s_i + //$NON-NLS-1$
          ".txt");//$NON-NLS-1$
      if (f.exists())
        continue;
      w = IO.getWriter(f);
      if (w == null)
        continue;
      try {
        w.write(' ');
        w.write(TextUtils
            .toString(ClassifierEmbryogeny.CLASSIFIER_EMBRYOGENY
                .regress(c)));
        w.write(' ');
        w.write(TextUtils.LINE_SEPARATOR);
      } catch (Throwable t) {//
      } finally {
        try {
          w.close();
          return;
        } catch (Throwable t) {//
        }
      }
      return;
    }

  }

}

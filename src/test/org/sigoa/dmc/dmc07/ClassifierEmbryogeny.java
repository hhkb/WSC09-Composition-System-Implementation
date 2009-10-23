/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-09
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.dmc.ClassifierEmbryogeny.java
 * Last modification: 2007-05-09
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
import java.io.InputStream;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.io.IO;
import org.sfc.io.binary.BinaryInputStream;
import org.sfc.io.binary.BinaryOutputStream;
import org.sigoa.refimpl.genomes.bitString.embryogeny.BitStringEmbryogeny;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;

/**
 * The embryogeny : translate bit strings of granularity 41 to
 * <code>RBGPNetVM</code>.
 *
 * @author Thomas Weise
 */
public class ClassifierEmbryogeny extends BitStringEmbryogeny<ClassifierSystem> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the classes
   */
  private static final EClasses[] CLASSES = EClasses.values();

  /**
   * The globally shared instance
   */
  public static final IEmbryogeny<byte[], ClassifierSystem> CLASSIFIER_EMBRYOGENY = new ClassifierEmbryogeny();

  /**
   * Create a new embryogeny.
   */
  protected ClassifierEmbryogeny() {
    super();
  }

  /**
   * This method is supposed to compute an instance of the phenotype from
   * an instance of the genotype. You should override it, since the default
   * method here just performs a typecast and returns the genotype.
   *
   * @param genotype
   *          The genotype instance to breed a phenotype from.
   * @return The phenotype hatched from the genotype instance.
   * @throws NullPointerException
   *           if <code>genotype==null</code>.
   */
  @Override
  public ClassifierSystem hatch(final byte[] genotype) {
    int i, j, c;
    byte[][] rules;
    byte[] rule;
    EClasses[] results;
    BinaryInputStream bis;

    if (genotype == null)
      throw new NullPointerException();

    c = ((genotype.length * 8) / ClassifierSystem.GRANULARITY);
    bis = this.acquireBitStringInputStream();
    bis.init(genotype);

    rules = new byte[c][ClassifierSystem.DATASET_LEN];
    results = new EClasses[c];

    for (i = 0; i < c; i++) {
      rule = rules[i];
      for (j = 0; j < ClassifierSystem.DATASET_LEN; j++) {
        rule[j] = (byte) (bis.readBits(2));
      }
      results[i] = (CLASSES[bis.readBits(ClassifierSystem.CLASS_BITS)
          % EClasses.CLASS_COUNT]);
    }

    this.releaseBitStringInputStream(bis);

    return new ClassifierSystem(rules, results);
  }

  /**
   * Compute a genotype from a phenotype. This method can be used if
   * different optimizers optimize a phenotype using different genotypes
   * and want to exchange individuals. Here it is possible to return
   * <code>null</code> if regression cannot be performed.
   *
   * @param phenotype
   *          the phenotype
   * @return the genotype regressed from the phenotype or <code>null</code>
   *         if regression was not possible
   * @throws NullPointerException
   *           if <code>phenotype==null</code>.
   */
  @SuppressWarnings("unchecked")
  @Override
  public byte[] regress(final ClassifierSystem phenotype) {
    if (phenotype == null)
      throw new NullPointerException();

    EClasses[] res;
    byte[][] rules;
    byte[] rule;
    int i, j;
    BinaryOutputStream bos;

    res = phenotype.getResults();
    rules = phenotype.getRules();
    bos = this.acquireBitStringOutputStream();

    for (i = 0; i < rules.length; i++) {
      rule = rules[i];
      for (j = 0; j < rule.length; j++) {
        bos.writeBits(rule[j], 2);
      }
      bos.writeBits(res[i].ordinal(), ClassifierSystem.CLASS_BITS);
    }

    this.releaseBitStringOutputStream(bos);
    return bos.getOutput();
  }

  /**
   * Extract a classifier genotype from a string.
   *
   * @param s
   *          the string
   * @return the classifier genotype
   */
  public static final byte[] genotypeFromString(final String s) {
    int i, v;
    char ch;
    BinaryOutputStream bos;
    boolean neg;

    bos = new BinaryOutputStream();
    v = 0;
    neg = false;
    for (i = 0; i < s.length(); i++) {
      ch = s.charAt(i);
      if (ch == '-')
        neg = true;
      else if ((ch >= '0') && (ch <= '9')) {
        v = ((v * 10) + (ch - '0'));
      }
      if (ch == ',') {
        if (neg) {
          v = -v;
          neg = false;
        }
        bos.writeBits(v, 8);
        v = 0;
      }
    }

    if (v != 0) {
      if (neg) {
        v = -v;
        neg = false;
      }
      bos.writeBits(v, 8);
    }

    return bos.getOutput();
  }

  /**
   * Extract a classifier genotypes from a file.
   *
   * @param source
   *          the file descripto
   * @param append
   *          the list to append to
   */
  private static final void genotypesFromFile(final Object source,
      final List<byte[]> append) {
    InputStream is;
    StringBuilder sb;
    int i, k;

    is = IO.getInputStream(source);
    if (is != null) {
      sb = new StringBuilder();
      try {

        while ((i = is.read()) >= 0) {
          sb.append(((char) i));
        }

      } catch (Throwable t) {//
      } finally {
        try {
          is.close();
        } catch (Throwable t) {//
        }
      }

      k = 0;
      for (i = sb.indexOf("byte[] {", 0); //$NON-NLS-1$
      i >= 0; i = sb.indexOf("byte[] {", k)) {//$NON-NLS-1$
        k = sb.indexOf("}", i);//$NON-NLS-1$
        if (k <= 0)
          return;
        append.add(genotypeFromString(sb.substring(i, k)));
      }
    }
  }

  /**
   * Extract the classifier genotypes from a file.
   *
   * @param source
   *          the file descriptor
   * @return the classifier genotypes
   */
  public static final byte[][] genotypesFromFile(final Object source) {
    List<byte[]> l;

    l = CollectionUtils.createList();
    genotypesFromFile(source, l);
    return transformList(l);
  }

  /**
   * Extract the classifier genotypes from a directory.
   *
   * @param dir
   *          the directory descriptor
   * @return the classifier genotypes
   */
  public static final byte[][] genotypesFromDirectory(final Object dir) {
    File f;
    List<byte[]> t;

    t = CollectionUtils.createList();
    f = IO.getFile(dir);
    genotypesFromDirectoryRec(f, t);

    return transformList(t);

  }

  /**
   * Extract the classifier genotypes from a directory.
   *
   * @param dir
   *          the directory descriptor
   * @param append
   *          the list to append to
   */
  private static final void genotypesFromDirectoryRec(final File dir,
      List<byte[]> append) {
    File[] l;
    int i;

    if (dir != null) {
      if (dir.isFile())
        genotypesFromFile(dir, append);
      else if (dir.isDirectory()) {
        l = dir.listFiles();
        for (i = (l.length - 1); i >= 0; i--) {
          genotypesFromDirectoryRec(l[i], append);
        }
      }
    }
  }

  /**
   * Transform a list of bytes to a genotype array.
   *
   * @param list
   *          the list
   * @return the genotype array
   */
  private static final byte[][] transformList(final List<byte[]> list) {
    int i, j, k;
    byte[] b1, b2;

    for (i = (list.size() - 2); i >= 0; i--) {
      b1 = list.get(i);
      inner: for (j = (list.size() - 1); j > i; j--) {
        b2 = list.get(j);

        k = b2.length;
        if (k == b1.length) {
          for (--k; k >= 0; k--) {
            if (b2[k] != b1[k])
              continue inner;
          }
          list.remove(j);
        }
      }
    }

    return list.toArray(new byte[list.size()][]);
  }
}

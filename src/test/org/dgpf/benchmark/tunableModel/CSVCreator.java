package test.org.dgpf.benchmark.tunableModel;

import org.dgpf.benchmark.TunableModel;
import org.sfc.io.TextWriter;

/**
 * @author Locu
 */
public class CSVCreator {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    int max, i, j, o, k, z;
    TunableModel tm;
    byte[] ba, ba2;
    TextWriter t;

    max = 4;
    tm = new TunableModel();
    tm.setPhenotypeLength(max);
    tm.setNeutralityDifficultyLevel(0);
    tm.setEpistasisDifficultyLevel(0);
    tm.setFunctionalObjectiveFunctionCount(1);// .setFunctionalObjectiveFunctionsCount(1);
    tm.setRuggednessDifficultyLevel(0);
   // tm.generateTestcases(1, 0, 0);

    ba = new byte[(max >>> 3) + 1];
    t = new TextWriter("d:\\Temp\\test.csv"); //$NON-NLS-1$

    for (i = 0; i < (1 << max); ++i) {
      o = i;

      for (j = 0; j < max >>> 3; ++j) {
        z = 0;
        for (k = 0; k < 8; ++k) {
          z |= ((o & 1) << k);
          o >>>= 1;
        }
        ba[j] = (byte) z;
      }
      // System.out.println( i + " " + tm.funktionalObjectiveFunktion(ba,
      // 0) ); //$NON-NLS-1$

      t.writeInt(0);
      t.writeCSVSeparator();
      t.writeInt(i);
      t.writeCSVSeparator();
      t.writeInt(tm.functionalObjectiveFunction(ba, ba.length << 3, 0));
      t.writeCSVSeparator();
      t.ensureNewLine();

      for (j = 0; j < max; ++j) {
        ba2 = ba.clone();
        ba2[j >>> 3] ^= (1 << (j & 7));
        // System.out.println( " " + j + " " +
        // tm.funktionalObjectiveFunktion(ba2, 0) ); //$NON-NLS-1$
        // //$NON-NLS-2$

        t.writeInt(j + 1);
        t.writeCSVSeparator();
        t.writeInt(i);
        t.writeCSVSeparator();
        t.writeInt(tm.functionalObjectiveFunction(ba2, ba.length << 3, 0));
        t.writeCSVSeparator();
        t.ensureNewLine();
      }

      t.newLine();
      t.newLine();
    }

    t.release();
  }
}

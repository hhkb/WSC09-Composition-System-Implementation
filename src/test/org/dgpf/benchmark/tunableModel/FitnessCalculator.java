package test.org.dgpf.benchmark.tunableModel;

import org.dgpf.benchmark.TunableModel;

/**
 * @author Ich
 */
public class FitnessCalculator {

  /**
   * Phenotype länge
   */
  private final static int PS = 80;

  /**
   * neutralitätslevel
   */
  private final static int N = 8;

  /**
   * Tunable Model
   */
  private static TunableModel tm;

  /**
   * input individuum
   */
  private final static byte[] INDIVIDUUM = new byte[] { (byte) 0xff,
      (byte) 0x00, (byte) 0xf4, (byte) 0x50, (byte) 0xf0, (byte) 0xe0,
      (byte) 0xf0, (byte) 0xe0,

      (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0,
      (byte) 0xe0, (byte) 0xf0, (byte) 0xe0,

      (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0,
      (byte) 0xe0, (byte) 0xf0, (byte) 0xe0,

      (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0,
      (byte) 0xe0, (byte) 0xf0, (byte) 0xe0,

      (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0,
      (byte) 0xe0, (byte) 0xf0, (byte) 0xe0,

      (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0,
      (byte) 0xe0, (byte) 0xf0, (byte) 0xe0,

      (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0,
      (byte) 0xe0, (byte) 0xf0, (byte) 0xe0,

      (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0,
      (byte) 0xe0, (byte) 0xf0, (byte) 0xe0,

      (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0,
      (byte) 0xe0, (byte) 0xf0, (byte) 0xe0,

      (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0,
      (byte) 0xe0, (byte) 0xf0, (byte) 0xe0,

      (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0,
      (byte) 0xe0, (byte) 0xf0, (byte) 0xe0 };

  /**
   * @param args
   */
  public static void main(String[] args) {
    tm = new TunableModel();

    tm.setNeutralityDifficultyLevel(N);
    tm.setPhenotypeLength(PS);

    System.out.println(fitness(INDIVIDUUM));
  }

  /**
   * @param individuum
   * @return sd
   */
  private static int fitness(byte[] individuum) {
    return tm.functionalObjectiveFunction(individuum,
        individuum.length << 3, 0);
  }
}

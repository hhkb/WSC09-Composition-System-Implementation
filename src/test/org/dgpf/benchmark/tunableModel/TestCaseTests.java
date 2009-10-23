package test.org.dgpf.benchmark.tunableModel;

import org.dgpf.benchmark.TunableModel;
import org.sfc.text.TextUtils;

/**
 * 
 * @author Locu
 *
 */
public class TestCaseTests {
  /**
   * 
   */
  private static TunableModel tunalbeModel;
  
  /**
   * 
   */
  private static int testSize;
  
  /**
   * 
   */
  private static int testcaseSize[];
  
  /**
   * 
   */
  private static int noiseLevels[];
  
  /**
   * 
   */
  private static int incompletenessLevels[];
  
  /**
   * 
   */
  private static int phenotypeSize[];
  
  /**
   * 
   */
  private static int noiseCount[];
  
  /**
   * 
   */
  private static int incompletenessCount[];
  
  /**
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    tunalbeModel = new TunableModel();
    generateTests();
    int i, j, f, k, n, in;
    boolean c;
    
    i = 0;
    j = 0;
    f = 0;
    k = 0;
    n = 0;
    in = 0;
    
    for( i=0; i < testSize; ++i )
    {
      tunalbeModel.setPhenotypeLength(phenotypeSize[i]);
      tunalbeModel.setTestCaseCount(testcaseSize[i]);
      tunalbeModel.setNoiseDifficultyLevel( noiseLevels[i]);
      tunalbeModel.setIncompletenessDifficultyLevel(incompletenessLevels[i]);

      
      System.out.println( "Test Nr. " + i ); //$NON-NLS-1$
      System.out.println( "Noise Level: " + tunalbeModel.getNoiseDifficultyLevel() ); //$NON-NLS-1$
      System.out.println( "Incompleteness Level: " + tunalbeModel.getIncompletenessDifficultyLevel() ); //$NON-NLS-1$

      int testcases[][] = tunalbeModel.getTestCases();
      
      c = true;
      
      if( testcases.length != testcaseSize[i] )
      {
        c = false;
        System.out.println( "Es wurden " + testcases.length + " statt " +   //$NON-NLS-1$//$NON-NLS-2$
            testcaseSize[i] + " Testcases erstellt!" ); //$NON-NLS-1$
      }
      
      for( j = 0; j < testcaseSize[i]; ++j )
      {
        if( testcases[j].length != phenotypeSize[i] )
        {
          c = false;
          System.out.println( "Die länge des Testcases ist " + testcases[j].length  //$NON-NLS-1$
              + " statt " + phenotypeSize[i] ); //$NON-NLS-1$
        }
        
        System.out.println( "Testcase: " + TextUtils.toString(testcases[j]) ); //$NON-NLS-1$
      
        
        n = 0;
        in = 0;
        
        for( k = 0; k < testcases[j].length; ++k )
        {
          if( testcases[j][k] == 2 )
            ++in;
          else if( testcases[j][k] != ((k^1) & 1) )
            ++n;
        }
      }
      
      if( in != incompletenessCount[i] )
      {
        c = false;
        System.out.println( "Die Anzahl von ungenauen Stellen ist " + in  //$NON-NLS-1$
              + " statt " + incompletenessCount[i] ); //$NON-NLS-1$
      }
      
      if( n != noiseCount[i] )
      {
        c = false;
        System.out.println( "Die Anzahl von verrauschten Stellen ist " + n  //$NON-NLS-1$
              + " statt " + noiseCount[i] ); //$NON-NLS-1$
      }
      
      System.out.println( "Correct: " + c ); //$NON-NLS-1$
      System.out.println();
      
      if( !c )
        ++f;
    }
    
    System.out.println( testSize + " Tests, " + (testSize - f) + " correct, "   //$NON-NLS-1$//$NON-NLS-2$
        + f + " incorrect"); //$NON-NLS-1$
  }
  
  /**
   * 
   */
  private static void generateTests(){
    testSize = 6;
    
    testcaseSize = new int[testSize];
    noiseLevels = new int[testSize];
    incompletenessLevels = new int[testSize];
    phenotypeSize = new int[testSize];
    noiseCount = new int[testSize];
    incompletenessCount = new int[testSize];
    
    testcaseSize[0] = 5;
    noiseLevels[0] = 0;
    incompletenessLevels[0] = 0;
    phenotypeSize[0] = 64;
    noiseCount[0] = 0;
    incompletenessCount[0] = 0;
    
    testcaseSize[1] = 5;
    noiseLevels[1] = 1;
    incompletenessLevels[1] = 1;
    phenotypeSize[1] = 64;
    noiseCount[1] = 3;
    incompletenessCount[1] = 3;
    
    testcaseSize[2] = 50;
    noiseLevels[2] = 10;
    incompletenessLevels[2] = 10;
    phenotypeSize[2] = 160;
    noiseCount[2] = 80;
    incompletenessCount[2] = 80;
    
    testcaseSize[3] = 50;
    noiseLevels[3] = 10;
    incompletenessLevels[3] = 0;
    phenotypeSize[3] = 160;
    noiseCount[3] = 80;
    incompletenessCount[3] = 0;
    
    testcaseSize[4] = 50;
    noiseLevels[4] = 0;
    incompletenessLevels[4] = 10;
    phenotypeSize[4] = 160;
    noiseCount[4] = 0;
    incompletenessCount[4] = 80;
    
    testcaseSize[5] = 50;
    noiseLevels[5] = 3;
    incompletenessLevels[5] = 7;
    phenotypeSize[5] = 160;
    noiseCount[5] = 24;
    incompletenessCount[5] = 56;
  }
}

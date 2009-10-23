package test.org.dgpf.benchmark.tunableModel;

import org.dgpf.benchmark.TunableModel;
import org.sfc.text.TextUtils;

/**
 * 
 * @author Locu
 *
 */
public class EpistasisTests {
 /**
   * 
   */
  private static TunableModel tunalbeModel;
  
  /**
   * 
   */
  private static byte[][] testDatas;
  
  /**
   * 
   */
  private static byte[][] results;
  
  /**
   *
   */
  private static int[] epistasisLevel;
  
  /**
   * 
   */
  private static int[] bitsize;
  
  /**
   * 
   */
  private static int testSize;
  
  /**
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    tunalbeModel = new TunableModel();
    generateTests();
    byte[] result;
    int size, i, j, f;
    boolean c;
    
    i = 0;
    j = 0;
    f = 0;
    
    for( i=0; i < testSize; ++i )
    {
      tunalbeModel.setInternalEpistasisDifficultyLevel(epistasisLevel[i]);
      tunalbeModel.recalculateInternalValues();
      
      System.out.println( "Test Nr. " + i ); //$NON-NLS-1$
      System.out.println( "Epistasis Level: " + tunalbeModel.getInternalEpistasisDifficultyLevel() ); //$NON-NLS-1$
      System.out.println( "Input: " + TextUtils.toString(testDatas[i]) ); //$NON-NLS-1$
      
      size = bitsize[i] >> 3;
    
      if( (bitsize[i] & 7) != 0 )
        ++size;
      
      result = new byte[ size ];
      TunableModel.epistasis(testDatas[i], result, bitsize[i], tunalbeModel.getEpistasisMapping(), epistasisLevel[i] );
      
      System.out.println( "Output: " + TextUtils.toString(result) ); //$NON-NLS-1$
      
      c = true;
      for( j = 0; j < size; ++j ){
        if( result[j] != results[i][j] )
        {
          c = false;
          break;
        }
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
    testSize = 8;
    testDatas = new byte[testSize][4];
    results = new byte[testSize][4];
    epistasisLevel = new int[testSize];
    bitsize = new int[testSize];
    
    testDatas[0] = new byte[]{ (byte) 0xf0, (byte) 0xf0, (byte) 0xf0, (byte) 0xf0};
    results[0] = new byte[]{ (byte) 0xa0, (byte) 0xa0, (byte) 0xa0, (byte) 0xa0 };
    epistasisLevel[0] = 2;
    bitsize[0] = 32;
    
    testDatas[1] = new byte[]{ (byte) 0xf0, (byte) 0xf0, (byte) 0xf0, (byte) 0xf0};
    results[1] = new byte[]{ (byte) 0xa0, (byte) 0x91, (byte) 0x3e, (byte) 0xa0};
    epistasisLevel[1] = 3;
    bitsize[1] = 32;
    
    testDatas[2] = new byte[]{ (byte) 0xf0, (byte) 0xf0, (byte) 0xf0, (byte) 0xf0};
    results[2] = new byte[]{ (byte) 0xa0, (byte) 0x01 };
    epistasisLevel[2] = 3;
    bitsize[2] = 10;
    
    testDatas[3] = new byte[]{ (byte) 0xf0, (byte) 0xf0, (byte) 0xf0, (byte) 0xf0};
    results[3] = new byte[]{ (byte) 0xa0, (byte) 0x11 };
    epistasisLevel[3] = 3;
    bitsize[3] = 13;
     
    testDatas[4] = new byte[]{ (byte) 0x55, (byte) 0x55, (byte) 0x55};
    results[4] = new byte[]{ (byte) 0x9a, (byte) 0xa6, (byte) 0x69 };
    epistasisLevel[4] = 3;
    bitsize[4] = 24;
   
    testDatas[5] = new byte[]{ (byte) 0x00 };
    results[5] = new byte[]{ (byte) 0x00 };
    epistasisLevel[5] = 41;
    bitsize[5] = 8;
    
    testDatas[6] = new byte[]{85, 85, 85, 85, -87, -86, -86, -86, 106, 85};
    results[6] = new byte[]{ (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55 };
    epistasisLevel[6] = 35;
    bitsize[6] = 80;
    
    testDatas[7] = new byte[]{-43};
    results[7] = new byte[]{ (byte) 0x55};
    epistasisLevel[7] = 8;
    bitsize[7] = 8;
  }
}

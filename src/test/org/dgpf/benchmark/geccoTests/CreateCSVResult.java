package test.org.dgpf.benchmark.geccoTests;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.sfc.io.TextWriter;

/**
 * 
 * @author Locu
 *
 */
public class CreateCSVResult {

  /**
   * @param args 
   * 
   */
  public static void main(String[] args)
  {
    String path = "d:\\temp\\geccoEpistasisLevelTest1";//$NON-NLS-1$
    String fileName = "geccoEpistasisLevelTest1.csv";//$NON-NLS-1$
    File dir = new File(path); 
    TextWriter t;    
    File[] tests = dir.listFiles();
    File[] runs, help;
    Element e;
    int count, runNumber, n, avg, all, i, j, k;
    ArrayList<Element> allElements = new ArrayList<Element>();
    
    for( i=0; i < tests.length; ++i )
    {
      e = new Element();
      if( tests[i].isDirectory() )
      {
        String name = tests[i].getName();
        n = Integer.valueOf( name.substring(name.indexOf('=')+1) ).intValue();
        
        e.n = n;
        runs = tests[i].listFiles();
        avg = 0;
        all = 0;

        for( j = 0; j < runs.length; ++j )
        {
          if( runs[j].isDirectory() )
          {
            help = runs[j].listFiles();
            for( k=0; k< help.length; ++k )
            {
              if( help[k].isDirectory() )
              {
                runNumber = Integer.valueOf( runs[j].getName() ).intValue();
    
                if( runNumber > 0 && runNumber < 101 )
                {
                  count = help[k].listFiles().length;     
    
                  if( count < e.min )
                    e.min = count;
                  
                  if( count > e.max && count < 1000)
                    e.max = count;
                  
                  
                  if( count < 1000 )
                  {
                    avg += count;
                    ++all;
                  }
                  else
                    ++e.notResolve;
                }
                break;
              }
            }
          }
        }
        
        e.avg = (double)avg/(double)all;
        
        if( e.n != 0 )
          ++e.n;
        
        allElements.add(e);
      }
    }
    
    Collections.sort( allElements );
    
    t = new TextWriter( path + "\\" + fileName ); //$NON-NLS-1$
    for( i=0; i < allElements.size(); ++i)
    {
      e = allElements.get(i);
      t.writeInt( e.n );
      t.writeCSVSeparator();
      t.writeDouble( e.avg );
      t.ensureNewLine();
    }
    
    t.newLine();
    t.newLine();
    
    for( i=0; i < allElements.size(); ++i)
    {
      e = allElements.get(i);
      t.writeInt( e.n );
      t.writeCSVSeparator();
      t.writeInt( e.min );
      t.ensureNewLine();
    }
    
    t.newLine();
    t.newLine();
    
    for( i=0; i < allElements.size(); ++i)
    {
      e = allElements.get(i);
      t.writeInt( e.n );
      t.writeCSVSeparator();
      t.writeInt( e.max );
      t.ensureNewLine();
    }
    
    t.newLine();
    t.newLine();
    
    for( i=0; i < allElements.size(); ++i)
    {
      e = allElements.get(i);
      t.writeInt( e.n );
      t.writeCSVSeparator();
      t.writeInt( e.notResolve );
      t.ensureNewLine();
    }
    
    t.release();
  }
  
  /**
   * @author Locu
   *
   */
  private static class Element implements Comparable<Element>{
    /**
     * 
     */
    public int n;
    /**
     * 
     */
    public double avg;
    /**
     * 
     */
    public int min = 100000;
    /**
     * 
     */
    public int max = 0;
    /**
     * 
     */
    public int notResolve = 0;
    
    /**
     * 
     */
    public Element(){
//
      }

    /**
     * 
     */
    @Override
    public int compareTo(Element o) {
      if( o.n > this.n )
        return -1;

      return 1;
    }
  }
}

/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-06-06
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.service.Main.java
 * Last modification: 2007-06-06
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

package test.org.sigoa.wsc.c2007.service;

import org.sfc.net.server.Server;
import org.sfc.utils.Classes;

import test.org.sigoa.wsc.c2007.challenge.Challenge;
import test.org.sigoa.wsc.c2007.challenge.ChallengeList;
import test.org.sigoa.wsc.c2007.challenge.ChallengeReader;
import test.org.sigoa.wsc.c2007.challenge.ServiceList2;
import test.org.sigoa.wsc.c2007.challenge.Solution;
import test.org.sigoa.wsc.c2007.challenge.SolutionWriter;
import test.org.sigoa.wsc.c2007.composition.Composer;
import test.org.sigoa.wsc.c2007.composition.ICompositionAlgorithm;
import test.org.sigoa.wsc.c2007.composition.ICompositionAlgorithmFactory;
import test.org.sigoa.wsc.c2007.composition.WorkerThread;
import test.org.sigoa.wsc.c2007.composition.algorithms.IDDFS;
import test.org.sigoa.wsc.c2007.composition.algorithms.heuristic.Composition;
import test.org.sigoa.wsc.c2007.composition.algorithms.heuristic.Heuristic1;
import test.org.sigoa.wsc.c2007.composition.algorithms.heuristic.HeuristicDFS;
import test.org.sigoa.wsc.c2007.composition.algorithms.heuristic.HeuristicDFS2;
import test.org.sigoa.wsc.c2007.composition.algorithms.heuristic.HeuristicDFS3;
import test.org.sigoa.wsc.c2007.kb.Concept;
import test.org.sigoa.wsc.c2007.kb.ConceptList;
import test.org.sigoa.wsc.c2007.kb.KnowledgeBase;
import test.org.sigoa.wsc.c2007.kb.Service;
import test.org.sigoa.wsc.c2007.kb.ServiceList;
import test.org.sigoa.wsc.c2007.kb.WSDLReader;
import test.org.sigoa.wsc.c2007.kb.XSDReader;


/**
 * The main class for the dmc challenge.
 *
 * @author Thomas Weise
 */
public class Main {

  /**
   * the default port
   */
  private static final int DEFAULT_PORT = 8080;

  /**
   * the main program called at startup
   *
   * @param args
   *          the command line arguments
   */
  public static void main(final String[] args) {
    int port, i;
    String s;
    Server serv;

    System.out.println("Web Service Composition Challenge 2007");//$NON-NLS-1$
    System.out.println("Steffen Bleul and Thomas Weise");//$NON-NLS-1$
    System.out.println("Distributed Systems Group");//$NON-NLS-1$
    System.out.println("University of Kassel, Germany");//$NON-NLS-1$
    System.out.println();
    System.out.println("Usage: java -jar -Xmx1024m -Xms1024m wscKassel.jar [port] [lowmem] [single] [check]");//$NON-NLS-1$
    System.out.println("-> Xmx,Xms: uses 1 GB of RAM");//$NON-NLS-1$
    System.out.println("-> [port]   : optional port for service to listen at");//$NON-NLS-1$
    System.out.println("              per default 8080");//$NON-NLS-1$
    System.out.println("-> [lowmem] : run with low memory consumption");//$NON-NLS-1$
    System.out.println("-> [single] : always return a single solution only");//$NON-NLS-1$
    System.out.println("-> [check]  : check given solutions");//$NON-NLS-1$
    System.out.println();


    port = DEFAULT_PORT;
    if ((args != null) && (args.length > 0)) {
      try {
        port = Integer.parseInt(args[0]);
      } catch (Throwable t) {
        System.out.println("invalid port, using default, using 8080."); //$NON-NLS-1$
      }

      for(i = (args.length-1); i >= 0; i--) {
        s = args[i].toLowerCase();
        if("lowmem".equals(s)) {//$NON-NLS-1$
          KnowledgeBase.LOW_MEM = true;
          System.out.println("lowmen-option is on");//$NON-NLS-1$
        }
        if("single".equals(s)) {//$NON-NLS-1$
          Composer.SINGLE = true;
          System.out.println("single-option is on");//$NON-NLS-1$
        }
      }

       for(i = (args.length-1); i >= 0; i--) {
        s = args[i].toLowerCase();
        if("check".equals(s)){ //$NON-NLS-1$
          test.org.sigoa.wsc.c2007.checker.Main.main(args);
          return;
        }
      }
    }


    if(KnowledgeBase.LOW_MEM)  WSCService.doGC();
    else initialize();

    //circumvention of the SPONTANEOUS RULE CHANGES #+*@@@°!!
    KnowledgeBase.LOW_MEM = true;

    System.out.println("starting service at port " + port); //$NON-NLS-1$

    serv = new Server(null, port, new WSCService(new IServiceCommand[] {
        new SetWaitTime(), new SetBootstrap(), new PerformQuery() }));
    serv.start();

    System.out.println("service is running.");//$NON-NLS-1$
  }

  /**
   * Initialize the program
   */
   static final void initialize()
  {
    long l;
    byte[] b;

    System.out.print("initializing..."); //$NON-NLS-1$
    Classes.loadClasses(new Class<?>[]{
        org.sfc.net.server.Server.class,//
        Concept.class,//
        ConceptList.class,//
        KnowledgeBase.class,//
        Service.class,//
        ServiceList.class,//
        WSDLReader.class,//
        XSDReader.class,//
        Challenge.class,//
        ChallengeList.class,//
        ChallengeReader.class,//
        Solution.class,//
        SolutionWriter.class,//
        ICompositionAlgorithm.class,//
        Heuristic1.class,//
        HeuristicDFS.class,
        HeuristicDFS2.class,
        HeuristicDFS3.class,
        WorkerThread.class,//
        Composer.class,//
        ICompositionAlgorithm.class,//
        ICompositionAlgorithmFactory.class,//
        IDDFS.class,//
        Composition.class,//
        HeuristicDFS.class,//
        ServiceList2.class//
    });

    System.out.print("classes done..."); //$NON-NLS-1$

    WSCService.doGC();

    l=1;
    try {
      for(; l <= Integer.MAX_VALUE; l<<=1) {
        b    = new byte[(int)l];
        b[0] = 1;
        b    =null;
        WSCService.doGC();
      }
    } catch(Throwable t) {
      System.out.print("memory available >= " + (l>>>21)+ //$NON-NLS-1$
      " MiB..."); //$NON-NLS-1$
    }

    WSCService.doGC();

    System.out.println("done."); //$NON-NLS-1$
    System.out.println();
  }
}

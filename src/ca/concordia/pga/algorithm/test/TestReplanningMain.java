package ca.concordia.pga.algorithm.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import ca.concordia.pga.algorithm.PGAlgorithm;
import ca.concordia.pga.algorithm.RefinementAlgorithm;
import ca.concordia.pga.algorithm.utils.CombinationHelper;
import ca.concordia.pga.algorithm.utils.DocumentParser;
import ca.concordia.pga.algorithm.utils.IndexBuilder;
import ca.concordia.pga.algorithm.utils.SolutionGenerator;
import ca.concordia.pga.models.*;
import de.vs.unikassel.generator.converter.bpel_creator.BPEL_Creator;

/**
 * This class is for testing and experiment purpose
 * 
 * @author Ludeng Zhao(Eric)
 * 
 */
public class TestReplanningMain {

	// change the Prefix URL according your environment
//	static final String PREFIX_URL = "/Users/ericzhao/Desktop/WSC2009_Testsets/Testset01/";
	static final String PREFIX_URL = "/Users/ericzhao/Desktop/WSC08_Dataset/Testset01/";
	static final String TAXONOMY_URL = PREFIX_URL + "Taxonomy.owl";
	static final String SERVICES_URL = PREFIX_URL + "Services.wsdl";
	// static final String WSLA_URL = PREFIX_URL +
	// "Servicelevelagreements.wsla";
	static final String CHALLENGE_URL = PREFIX_URL + "Challenge.wsdl";


	/**
	 * @param args
	 * @throws DocumentException
	 */
	public static void main(String[] args) {

		PlanningGraph pg = new PlanningGraph();

		Map<String, Concept> conceptMap = new HashMap<String, Concept>();
		Map<String, Thing> thingMap = new HashMap<String, Thing>();
		Map<String, Service> serviceMap = new HashMap<String, Service>();
		Map<String, Param> paramMap = new HashMap<String, Param>();

		Set<Service> invokedServiceSet = new HashSet<Service>();
		Set<Service> currInvokableServiceSet = new HashSet<Service>();
		Set<Service> currNonInvokableServiceSet = new HashSet<Service>();
		Set<Concept> knownConceptSet = new HashSet<Concept>(); 
		
		Set<Concept> givenConceptSet = new HashSet<Concept>();
		Set<Concept> goalConceptSet = new HashSet<Concept>();

		Date initStart = new Date();
		try {
			DocumentParser.parseTaxonomyDocument(conceptMap, thingMap, TAXONOMY_URL);
			DocumentParser.parseServicesDocument(serviceMap, paramMap, conceptMap, thingMap,
					SERVICES_URL);
			// parseWSLADocument(serviceMap, WSLA_URL);
		} catch (DocumentException e) {

			e.printStackTrace();
		}

		IndexBuilder.buildInvertedIndex(conceptMap, serviceMap);
		Date initEnd = new Date();

		System.out.println("Initializing Time "
				+ (initEnd.getTime() - initStart.getTime()));

		System.out.println("Concepts size " + conceptMap.size());
		System.out.println("Things size " + thingMap.size());
		System.out.println("Param size " + paramMap.size());
		System.out.println("Services size " + serviceMap.size());



		/**
		 * begin executing algorithm
		 */

		try {
			DocumentParser.parseChallengeDocument(paramMap, conceptMap, thingMap, pg,
					CHALLENGE_URL);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		System.out.println();
		System.out.println("Given Concepts: ");
		for (Concept c : pg.getPLevel(0)) {
			givenConceptSet.add(c);
			System.out.print(c + " | ");
		}
		System.out.println();
		System.out.println("Goal Concepts: ");
		for (Concept c : pg.getGoalSet()) {
			goalConceptSet.add(c);
			System.out.print(c + " | ");
		}
		System.out.println();

		/**
		 * PG Algorithm Implementation
		 */
		Date compStart = new Date(); // start composition checkpoint
		
		boolean goalFound = PGAlgorithm.generatePG(knownConceptSet,currInvokableServiceSet,
			currNonInvokableServiceSet,invokedServiceSet,
			pg);
		
		Date compEnd = new Date(); // end composition checkpoint

		/**
		 * Print out the composition result
		 */
		if (goalFound) {
			/**
			 * printout PG status (before pruning)
			 */
			System.out.println("\n=========Goal Found=========");
			System.out.println("PG Composition Time: "
					+ (compEnd.getTime() - compStart.getTime()) + "ms");
			System.out.println("Execution Length: "
					+ (pg.getALevels().size() - 1));
			System.out.println("Services Invoked: " + invokedServiceSet.size());
			System.out.println("=============================");

			
			/**
			 * do backward search to remove redundancy (pruning PG)
			 */
			Vector<Integer> routesCounters = RefinementAlgorithm.refineSolution(pg);
			Date refineEnd = new Date(); //refinement end checkpoint
			
			/**
			 * printout backward search status
			 */
			System.out.println();
			System.out.println("===================================");
			System.out.println("===========After Pruning===========");
			System.out.println("===================================");

			int invokedServiceCount = 0;
			for(int i=1; i<pg.getALevels().size(); i++){
				System.out.println("\n*********Action Level " + i
						+ " (alternative routes:" 
						+ routesCounters.get(routesCounters.size() - i) + ") *******");
				for (Service s : pg.getALevel(i)) {
					System.out.println(s);
					invokedServiceCount++;
				}
			}
			System.out.println("\n=================Status=================");
			System.out.println("Total(including PG) Composition Time: "
					+ (refineEnd.getTime() - compStart.getTime()) + "ms");
			System.out.println("Execution Length: "
					+ (pg.getALevels().size() - 1));
			System.out.println("Services Invoked: " + invokedServiceCount);			
			System.out.println("==================End===================");

			/**
			 * generate solution file
			 */
			try {
				SolutionGenerator.generateSolution(pg);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("\n=========Goal @NOT@ Found=========");
		}

		/**
		 * remove n% services from serviceMap
		 */
		int originalServiceMapSize;
		int changedServiceMapSize;
		float changedRate;
		
		originalServiceMapSize = serviceMap.size();
		Set<String> removedServiceKeySet = new HashSet<String>();
//		for(String key : serviceMap.keySet()){
//			if(Math.random() <= 0.20){
//				removedServiceKeySet.add(key);
//			}
//		}
		Set<Service> removedServiceSet = new HashSet<Service>();
		
		removedServiceSet.add(serviceMap.get("serv18541048"));
		removedServiceSet.add(serviceMap.get("serv1612205357"));
		removedServiceSet.add(serviceMap.get("serv919521571"));
//
		serviceMap.remove("serv18541048");
		serviceMap.remove("serv1612205357");
		serviceMap.remove("serv919521571");

		for(String key : removedServiceKeySet){
			removedServiceSet.add(serviceMap.get(key));
			serviceMap.remove(key);
		}
		changedServiceMapSize = serviceMap.size();
		changedRate = (float)(originalServiceMapSize-changedServiceMapSize)/(float)originalServiceMapSize*100;
		System.out.println();
		System.out.println("======================================");
		System.out.println("===========Removing Services==========");				
		System.out.println("======================================");		
		System.out.println("Original ServiceMap size: " + originalServiceMapSize);
		System.out.println("Updated ServiceMap size: " + changedServiceMapSize);
		System.out.println("Changed rate: " + changedRate +"%");
		
		System.out.println("======================================");
		System.out.println("===========Replanning Start===========");				
		System.out.println("======================================");	

		Date replanningStart = new Date(); // start replanning checkpoint

		/**
		 * reset inverted index
		 */
		for(String key : conceptMap.keySet()){
			Concept concept = conceptMap.get(key);
			concept.resetServiceIndex();
		}
		IndexBuilder.buildInvertedIndex(conceptMap, serviceMap);
		
		/**
		 * reset PG
		 */
		pg = new PlanningGraph();
		pg.addPLevel(givenConceptSet);
		pg.addALevel(new HashSet<Service>());
		pg.setGoalSet(goalConceptSet);
		
		System.out.println();
		System.out.println("Given Concepts: ");
		for (Concept c : pg.getPLevel(0)) {
			System.out.print(c + " | ");
		}
		System.out.println();
		System.out.println("Goal Concepts: ");
		for (Concept c : pg.getGoalSet()) {
			System.out.print(c + " | ");
		}
		System.out.println();

		/**
		 * reset params
		 */
		invokedServiceSet = new HashSet<Service>();
		currInvokableServiceSet = new HashSet<Service>();
		currNonInvokableServiceSet = new HashSet<Service>();
		knownConceptSet = new HashSet<Concept>(); 
		
		/**
		 * PG Algorithm Implementation
		 */
		goalFound = PGAlgorithm.generatePG(knownConceptSet,currInvokableServiceSet,
			currNonInvokableServiceSet,invokedServiceSet,
			pg);
		
		Date planningEnd = new Date(); // end composition checkpoint

		/**
		 * Print out the composition result
		 */
		if (goalFound) {

			/**
			 * printout PG status (before pruning)
			 */
			System.out.println("\n=========Goal Found=========");
			System.out.println("PG Composition Time: "
					+ (planningEnd.getTime() - replanningStart.getTime()) + "ms");
			System.out.println("Execution Length: "
					+ (pg.getALevels().size() - 1));
			System.out.println("Services Invoked: " + invokedServiceSet.size());
			System.out.println("=============================");

			
			/**
			 * do backward search to remove redundancy (pruning PG)
			 */
			Vector<Integer> routesCounters = RefinementAlgorithm.refineSolution(pg);
			Date refineEnd = new Date(); //refinement end checkpoint
			
			/**
			 * printout backward search status
			 */
			System.out.println();
			System.out.println("===================================");
			System.out.println("===========After Pruning===========");
			System.out.println("===================================");

			int invokedServiceCount = 0;
			for(int i=1; i<pg.getALevels().size(); i++){
				System.out.println("\n*********Action Level " + i
						+ " (alternative routes:" 
						+ routesCounters.get(routesCounters.size() - i) + ") *******");
				for (Service s : pg.getALevel(i)) {
					System.out.println(s);
					invokedServiceCount++;
				}
			}
			System.out.println("\n=================Status=================");
			System.out.println("Total(including PG) Composition Time: "
					+ (refineEnd.getTime() - replanningStart.getTime()) + "ms");
			System.out.println("Execution Length: "
					+ (pg.getALevels().size() - 1));
			System.out.println("Services Invoked: " + invokedServiceCount);			
			System.out.println("==================End===================");

			/**
			 * generate solution file
			 */
//			try {
//				generateSolution(pg);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}

		} else {
			System.out.println("\n=========Goal @NOT@ Found=========");
		}
		System.out.println("======================================");
		System.out.println("===========Replanning End=============");				
		System.out.println("======================================");	
	}
}

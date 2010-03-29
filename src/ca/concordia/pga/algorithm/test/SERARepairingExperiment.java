package ca.concordia.pga.algorithm.test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.dom4j.DocumentException;

import ca.concordia.pga.algorithm.PGAlgorithm;
import ca.concordia.pga.algorithm.BackwardSearchAlgorithm;
import ca.concordia.pga.algorithm.RemovalAlgorithm;
import ca.concordia.pga.algorithm.RepairAlgorithm;
import ca.concordia.pga.algorithm.utils.DocumentParser;
import ca.concordia.pga.algorithm.utils.IndexBuilder;
import ca.concordia.pga.algorithm.utils.PGValidator;
import ca.concordia.pga.algorithm.utils.PlanStabilityEvaluator;
import ca.concordia.pga.algorithm.utils.RepairingEvaluator;
import ca.concordia.pga.models.*;

/**
 * This class is for testing and experiment purpose
 * 
 * @author Ludeng Zhao(Eric)
 * 
 */
public class SERARepairingExperiment {

	// change the Prefix URL according your environment
	//static final String PREFIX_URL = "/Users/ericzhao/Desktop/WSC2009_Testsets/Testset07/";
	static final String PREFIX_URL = "/Users/ericzhao/Desktop/WSC/WSC08_Dataset/Testset12/";
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
		pg.getGivenConceptSet().addAll(pg.getPLevel(0));
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

		boolean goalFound = PGAlgorithm.generatePG(knownConceptSet,
				currInvokableServiceSet, currNonInvokableServiceSet,
				invokedServiceSet, pg);

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
			 * validate PG
			 */
			PGValidator.comboValidate(pg, serviceMap, conceptMap, thingMap, paramMap, givenConceptSet);
			

			/**
			 * prune PG
			 */
			BackwardSearchAlgorithm.extractSolution(pg);

			/**
			 * reserve old PG status
			 */
			PlanningGraph oldpg = pg.clone();
			
			/**
			 * remove services from serviceMap
			 */
			int originalServiceMapSize;
			int changedServiceMapSize;
			float changedRate;
			originalServiceMapSize = serviceMap.size();
			Set<String> removedServiceKeySet = new HashSet<String>();
			Set<Service> removedServiceSet = new HashSet<Service>();
			for (String key : serviceMap.keySet()) {
				if (Math.random() <= 0.18) {
					removedServiceKeySet.add(key);
				}
			}
			removedServiceKeySet.remove("serv1939094802");
			removedServiceKeySet.remove("serv208204533");
			removedServiceKeySet.remove("serv1663004376");
//			removedServiceKeySet.remove("serv807605227");


//			candidates.add(serviceMap.get("serv1222560119"));//cause failed
//			candidates.add(serviceMap.get("serv1915243905"));//cause failed
//			candidates.add(serviceMap.get("serv529876295"));
//			candidates.add(serviceMap.get("serv668740761"));
//			candidates.add(serviceMap.get("serv2123540604"));
//			candidates.add(serviceMap.get("serv45489208"));
//			candidates.add(serviceMap.get("serv877037460"));
//			candidates.add(serviceMap.get("serv114921441"));//cause failed
//			candidates.add(serviceMap.get("serv807605227"));//cause failed

			for (String key : removedServiceKeySet) {
				removedServiceSet.add(serviceMap.get(key));
				serviceMap.remove(key);
			}
			

			/**
			 * print out status after removal
			 */
			changedServiceMapSize = serviceMap.size();
			changedRate = (float) (originalServiceMapSize - changedServiceMapSize)
					/ (float) originalServiceMapSize * 100;
			
			System.out.println();
			System.out.println("======================================");
			System.out.println("===========Removing Services==========");
			System.out.println("======================================");
			System.out.println("Original ServiceMap size: "
					+ originalServiceMapSize);
			System.out.println("Updated ServiceMap size: " + changedServiceMapSize);
			System.out.println("Removed Service Size: " + removedServiceSet.size());
			System.out.println("Changed rate: " + changedRate + "%");
			System.out.println("Removed Services: ");
			for(Service s : removedServiceSet){
				System.out.println(s + " | ");
			}
			System.out.println("======================================");
			System.out.println("===========Repairing Start===========");
			System.out.println("======================================");

			
			/**
			 * mark concept that in initial PLevel as given
			 */
			for(Concept c : pg.getPLevel(0)){
				c.setRin(true);
			}
			
			/**
			 * compute services in PG
			 */
			Set<Service> servicesInPG = new HashSet<Service>();
			for(Set<Service> aLevel : pg.getALevels()){
				for(Service s : aLevel){
					servicesInPG.add(s);
				}
			}
			
			/**
			 * compute producedby/usedby for each concept in PG
			 */
			int currentLevel = 0;
			do{
				for(Concept c : pg.getPLevel(currentLevel)){
					//producedby
					for(Service s : pg.getALevel(currentLevel)){
						if(s.getOutputConceptSet().contains(c)){
							c.getProducedByServices().add(s);
						}
					}	
					
					//usedby
					if(currentLevel != pg.getPLevels().size()-1){
						c.getUsedByServices().addAll(c.getServicesIndex());
						c.getUsedByServices().retainAll(servicesInPG);			
					}
				}
				currentLevel++;
			}while(currentLevel < pg.getPLevels().size());
			
			/**
			 * rebuild inverted index
			 */
			for (String key : conceptMap.keySet()) {
				Concept concept = conceptMap.get(key);
				concept.resetServiceIndex();
			}
			IndexBuilder.buildInvertedIndex(conceptMap, serviceMap);
			
			
			/**
			 * compute concepts in PG
			 */
			Set<Concept> conceptsInPG = new HashSet<Concept>();
			conceptsInPG.addAll(pg.getPLevel(0));
			currentLevel = 1;
			do{
				for(Service s : pg.getALevel(currentLevel)){
					conceptsInPG.addAll(s.getOutputConceptSet());
				}
				currentLevel++;
			}while(currentLevel < pg.getALevels().size());
			
			for(String key : conceptMap.keySet()){
				Concept concept = conceptMap.get(key);
				concept.getOriginServiceSet().clear();
			}

			
			Date removalStart = new Date();
			/**
			 * remove services in PG
			 */
			Set<Concept> bp = RemovalAlgorithm.removeServcesFromPG(pg, removedServiceSet);
			
			System.out.println("BP: " + bp.size());
			for(Concept c : bp){
				System.out.print(c + " | ");
			}
			
						
			/**
			 * validate PG
			 */
			boolean pgvalid = PGValidator.comboValidate(pg, serviceMap, conceptMap, thingMap, paramMap, givenConceptSet);
			
			if(pgvalid){
				/**
				 * do backward search to remove redundancy (pruning PG)
				 */
				Date refineStart = new Date();
				for(String key : conceptMap.keySet()){
					Concept concept = conceptMap.get(key);
					concept.getOriginServiceSet().clear();
				}
				Vector<Integer> routesCounters = BackwardSearchAlgorithm.extractSolution(pg);
				Date refineEnd = new Date(); // refinement end checkpoint

				/**
				 * printout backward search status
				 */
				System.out.println();
				System.out.println("===================================");
				System.out.println("===========After Pruning===========");
				System.out.println("===================================");

				int invokedServiceCount = 0;
				for (int i = 1; i < pg.getALevels().size(); i++) {
					System.out.println("\n*********Action Level " + i
							+ " (alternative routes:"
							+ routesCounters.get(routesCounters.size() - i)
							+ ") *******");
					for (Service s : pg.getALevel(i)) {
						System.out.println(s);
						invokedServiceCount++;
					}
				}
				System.out.println("\n=================Status=================");
				System.out.println("Total(including PG) Composition Time: "
						+ (refineEnd.getTime() - removalStart.getTime()) + "ms");
				System.out.println("Execution Length: "
						+ (pg.getALevels().size() - 1));
				System.out.println("Services Invoked: " + invokedServiceCount);
				System.out.println("==================End===================");		
			}else { //if PG is not valid or Goal is not satisfied
				
				/**
				 * remove all actions in the PG
				 */
//				PlanningGraph oldpg = pg.clone();
//				pg = new PlanningGraph();
//				pg.getGivenConceptSet().addAll(oldpg.getGivenConceptSet());
//				pg.getGoalSet().addAll(oldpg.getGoalSet());
//				pg.addALevel(oldpg.getALevel(0));
//				pg.addPLevel(oldpg.getPLevel(0));
//				pg.addALevel(new HashSet<Service>());
//				pg.addPLevel(new HashSet<Concept>());
//				
//				System.out.println("\npg has " + (pg.getALevels().size()-1) + " action levels\n");
				
				Date repairStart = new Date();
				if(RepairAlgorithm.repairSERA(pg, serviceMap, conceptMap, thingMap, paramMap)){
					System.out.println("Repair Succeed!");

					System.out.println();
					System.out.println("===================================");
					System.out.println("===========Repair Result===========");
					System.out.println("=========(Before Refinement)=======");					
					System.out.println("===================================");
					int invokedServiceCount = 0;
					for (int i = 1; i < pg.getALevels().size(); i++) {
						System.out.println("\n*********Action Level " + i);
						for (Service s : pg.getALevel(i)) {
							System.out.println(s);
							invokedServiceCount++;
						}
					}
					Date repairEnd = new Date(); // refine end checkpoint
					System.out.println("\n=================Status=================");
					System.out.println("Repair Time: "
							+ (repairEnd.getTime() - repairStart.getTime()) + "ms");
					System.out.println("Execution Length: "
							+ (pg.getALevels().size() - 1));
					System.out.println("Services Invoked: " + invokedServiceCount);
					System.out.println("==================End===================");	

					/**
					 * do backward search to remove redundancy (pruning PG)
					 */
					for(String key : conceptMap.keySet()){
						Concept concept = conceptMap.get(key);
						concept.getOriginServiceSet().clear();
					}
					Vector<Integer> routesCounters = BackwardSearchAlgorithm.extractSolution(pg);
					Date refineEnd = new Date(); // refine end checkpoint

					
					/**
					 * printout backward search status
					 */
					System.out.println();
					System.out.println("===================================");
					System.out.println("===========Repair Result===========");
					System.out.println("=========(After Refinement)========");					
					System.out.println("===================================");

					invokedServiceCount = 0;
					for (int i = 1; i < pg.getALevels().size(); i++) {
						System.out.println("\n*********Action Level " + i
								+ " (alternative routes:"
								+ routesCounters.get(routesCounters.size() - i)
								+ ") *******");
						for (Service s : pg.getALevel(i)) {
							System.out.println(s);
							invokedServiceCount++;
						}
					}
					
					/**
					 * compute plan distance
					 */
					int planDistance;
					planDistance = PlanStabilityEvaluator.evaluate(oldpg, pg);
					
					System.out.println("\n=================Status=================");
					System.out.println("Total(Repair + Refinement) Composition Time: "
							+ (refineEnd.getTime() - repairStart.getTime()) + "ms");
					System.out.println("Execution Length: "
							+ (pg.getALevels().size() - 1));
					System.out.println("Services Invoked: " + invokedServiceCount);
					System.out.println("Plan Distance: " + planDistance);					
					System.out.println("==================End===================");	
					
					PGValidator.comboValidate(pg, serviceMap, conceptMap, thingMap, paramMap, givenConceptSet);
				}else{
					Date repairFailed = new Date();
					System.out.println("Repair Failed!");
					System.out.println("Repair Failed Time: "
							+ (repairFailed.getTime() - repairStart.getTime()) + "ms");					
				}
					
			}


		} else {
			System.out.println("\n=========Goal @NOT@ Found=========");
		}

	}

}

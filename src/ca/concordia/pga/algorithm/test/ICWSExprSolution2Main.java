package ca.concordia.pga.algorithm.test;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.dom4j.DocumentException;

import ca.concordia.pga.algorithm.PGAlgorithm;
import ca.concordia.pga.algorithm.BackwardSearchAlgorithm;
import ca.concordia.pga.algorithm.RepairAlgorithm;
import ca.concordia.pga.algorithm.utils.DocumentParser;
import ca.concordia.pga.algorithm.utils.IndexBuilder;
import ca.concordia.pga.algorithm.utils.PGValidator;
import ca.concordia.pga.algorithm.utils.PlanStabilityEvaluator;
import ca.concordia.pga.models.Concept;
import ca.concordia.pga.models.Param;
import ca.concordia.pga.models.PlanningGraph;
import ca.concordia.pga.models.Service;
import ca.concordia.pga.models.Thing;

public class ICWSExprSolution2Main {
	
	// change the Prefix URL according your environment
	static final String PREFIX_URL = "/Users/ericzhao/Desktop/WSC/WSC08_Dataset/Testset01/";
	static final String TAXONOMY_URL = PREFIX_URL + "Taxonomy.owl";
	static final String SERVICES_URL = PREFIX_URL + "Services.wsdl";
	// static final String WSLA_URL = PREFIX_URL + "Servicelevelagreements.wsla";
	static final String CHALLENGE_URL = PREFIX_URL + "Challenge.wsdl";
	
	
	private static void calculateCommonOutputs(PlanningGraph leanSolutionPG){
		
		int currentLevel = leanSolutionPG.getALevels().size() - 1;
		do{
			if(currentLevel == leanSolutionPG.getALevels().size() - 1){
				for(Service s : leanSolutionPG.getALevel(currentLevel)){
					s.getCommonOutputs().addAll(s.getOutputConceptSet());
					s.getCommonOutputs().retainAll(leanSolutionPG.getGoalSet());
				}
			}else{
				Set<Concept> commonOutputs = new HashSet<Concept>();
				Set<Service> allServicesAtHigherLevel = leanSolutionPG.getAllServciesAtHigerLevel(currentLevel);
				for(Service s : allServicesAtHigherLevel){
					commonOutputs.addAll(s.getInputConceptSet());
				}
				commonOutputs.addAll(leanSolutionPG.getGoalSet());
				for(Service s : leanSolutionPG.getALevel(currentLevel)){

					s.getCommonOutputs().addAll(s.getOutputConceptSet());
					s.getCommonOutputs().retainAll(commonOutputs);
				}
			}
			
			currentLevel--;
		}while(currentLevel > 0);
		
	}
	
	/**
	 * return the action level of given s
	 * @param s
	 * @param pg
	 * @return
	 */
	private static Set<Service> getServiceLevel(Service s, PlanningGraph pg){
		
		for(Set<Service> aLevel : pg.getALevels()){
			if(aLevel.contains(s)){
				return aLevel;
			}
		}
		return null;
	}
	
	/**
	 * calculate equivalent service set for given services 
	 * @param services
	 * @param pg
	 */
	private static void calculateBackups(Set<Service> services, PlanningGraph pg){
		for(Service s : services){
			Set<Service> aLevel = getServiceLevel(s,pg);
			for(Service candidate : aLevel){
				Set<Concept> outputs = new HashSet<Concept>();
				outputs.addAll(s.getOutputConceptSet());
				outputs.retainAll(candidate.getOutputConceptSet());
				if(s.getInputConceptSet().equals(candidate.getInputConceptSet())
					& candidate.getOutputConceptSet().containsAll(s.getCommonOutputs())
					& !s.equals(candidate)){
					s.getBackupServiceSet().add(candidate);
				}
			}	
			System.out.println("service " + s + "'s backups:");
			for(Service b : s.getBackupServiceSet()){
				System.out.println(b);
			}
			System.out.println("---------------------------------------\n");
		}
	}

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

		Map<String, Service> serviceMapReserved = new HashMap<String, Service>();

		Set<Concept> givenConceptSet = new HashSet<Concept>();
		Set<Concept> goalConceptSet = new HashSet<Concept>();
		
//		Set<PlanningGraph> leanSolutions = new HashSet<PlanningGraph>();
		List<PlanningGraph> leanSolutions = new LinkedList<PlanningGraph>();
		
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
		 * begin parsing process
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
		
		System.out.println("============================================");
		System.out.println("============ ICWS Repairing Expr ===========");				
		System.out.println("============================================");
		
		boolean goalFound = false;
		
		serviceMapReserved.putAll(serviceMap);
		
		do {
			
			Set<Service> invokedServiceSet = new HashSet<Service>();
			Set<Service> currInvokableServiceSet = new HashSet<Service>();
			Set<Service> currNonInvokableServiceSet = new HashSet<Service>();
			Set<Concept> knownConceptSet = new HashSet<Concept>(); 
			
			Date compStart = new Date(); // start composition checkpoint
			goalFound = PGAlgorithm.generatePG(knownConceptSet,
					currInvokableServiceSet, currNonInvokableServiceSet,
					invokedServiceSet, pg);
			Date compEnd = new Date(); // end composition checkpoint

			if(!goalFound){
				break;
			}
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
			 * reserve full PG status
			 */
			PlanningGraph fullPG = pg.clone();
			
			/**
			 * prune PG
			 */
			Vector<Integer> routesCounters = BackwardSearchAlgorithm.extractSolution(pg);
			/**
			 * printout backward search status
			 */
			Date refineEnd = new Date(); //refinement end checkpoint

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
			 * reserve lean solution status
			 */
			PlanningGraph leanSolutionPG = pg.clone();
			leanSolutions.add(leanSolutionPG);
			
			/**
			 * mark solution services in PG
			 */
			for(Service s : leanSolutionPG.getAllServices()){
				s.setSolutionService(true);
			}
			
			/**
			 * calculate backups for each solution services
			 */
			calculateCommonOutputs(leanSolutionPG);
			calculateBackups(leanSolutionPG.getAllServices(),fullPG);
			
			
			/**
			 * get a list of removed services
			 */
			Set<Service> removedServices = leanSolutionPG.getAllServicesAndTheirBackups();
			
			int oldServiceMapSize = serviceMap.size();
			for(Service s : removedServices){
				serviceMap.remove(s.toString());
			}
			System.out.println("services remvoed from map: " + (oldServiceMapSize-serviceMap.size()));
			

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
			pg.addPLevel(new HashSet<Concept>());
			pg.getPLevel(0).addAll(fullPG.getGivenConceptSet());
			pg.addALevel(new HashSet<Service>());
			pg.getGoalSet().addAll(fullPG.getGoalSet());
			pg.getGivenConceptSet().addAll(fullPG.getGivenConceptSet());
			
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
			 * if all backup services in any removed services' backup list are removed, 
			 * then add all solution services as well as their backups
			 * to removed services list 
			 */
			
		}while (goalFound);
		
		serviceMap.clear();
		serviceMap.putAll(serviceMapReserved);
		
		System.out.println();
		System.out.println("***************************************************");
		System.out.println("Number of lean solutions indexed: " + leanSolutions.size());
		System.out.println("***************************************************");
		
		/**
		 * exclude solution 1, rebuild PG
		 */
			
		Set<Service> invokedServiceSet = new HashSet<Service>();
		Set<Service> currInvokableServiceSet = new HashSet<Service>();
		Set<Service> currNonInvokableServiceSet = new HashSet<Service>();
		Set<Concept> knownConceptSet = new HashSet<Concept>(); 
		
		/**
		 * take out solution 1
		 */
		currNonInvokableServiceSet.addAll(leanSolutions.get(0).getAllServicesAndTheirBackups());
		for(Service s : currNonInvokableServiceSet){
			serviceMap.remove(s.toString());
		}
		TestSERARepairingMain.takeOutServices.addAll(leanSolutions.get(0).getAllServicesAndTheirBackups());
		TestPlanningMain.takeOutServices.addAll(leanSolutions.get(0).getAllServicesAndTheirBackups());
		TestPlanningMain.oldpg = leanSolutions.get(1).clone();
		/**
		 * reset inverted index
		 */
		for(String key : conceptMap.keySet()){
			Concept concept = conceptMap.get(key);
			concept.resetServiceIndex();
		}
		IndexBuilder.buildInvertedIndex(conceptMap, serviceMap);
		
		pg = new PlanningGraph();
		pg.addPLevel(new HashSet<Concept>());
		pg.getPLevel(0).addAll(leanSolutions.get(0).getGivenConceptSet());
		pg.addALevel(new HashSet<Service>());
		pg.getGoalSet().addAll(leanSolutions.get(0).getGoalSet());
		pg.getGivenConceptSet().addAll(leanSolutions.get(0).getGivenConceptSet());
		
		goalFound = PGAlgorithm.generatePG(knownConceptSet,
				currInvokableServiceSet, currNonInvokableServiceSet,
				invokedServiceSet, pg);
		
		/**
		 * Sending Removal Query
		 */
		
		
		Set<Service> candidates = new HashSet<Service>();
		Set<String> removedServiceKeySet = new HashSet<String>();
		Set<Service> removedServices = new HashSet<Service>();

//		c(serviceMap.get("serv1056747493"));
//		candidates.add(serviceMap.get("serv1126179726"));
//		candidates.add(serviceMap.get("serv1195611959"));//cause failed
//		candidates.add(serviceMap.get("serv502928173"));
//		candidates.add(serviceMap.get("serv2096592482"));//cause failed
//		candidates.add(serviceMap.get("serv18541048"));
//		candidates.add(serviceMap.get("serv87973281"));
//		candidates.add(serviceMap.get("serv850089338"));//cause failed
//		candidates.add(serviceMap.get("serv1612205357"));
//		candidates.add(serviceMap.get("serv919521571"));
		
		
		candidates.add(serviceMap.get("serv21818098")); 
		candidates.add(serviceMap.get("serv160682564"));
		candidates.add(serviceMap.get("serv230114797"));
		candidates.add(serviceMap.get("serv438411496"));
		candidates.add(serviceMap.get("serv1962643572"));
		candidates.add(serviceMap.get("serv2101508038"));
		candidates.add(serviceMap.get("serv785572661"));
		candidates.add(serviceMap.get("serv231753341"));
		candidates.add(serviceMap.get("serv1755985417"));
		candidates.add(serviceMap.get("serv1063301593"));	
		candidates.add(serviceMap.get("serv1202166059"));
		candidates.add(serviceMap.get("serv1271598292"));
		candidates.add(serviceMap.get("serv1341030525"));
		candidates.add(serviceMap.get("serv25095148"));	
		candidates.add(serviceMap.get("serv787211205"));
		candidates.add(serviceMap.get("serv1339392019"));//break
		candidates.add(serviceMap.get("serv1823779106"));//break
		candidates.add(serviceMap.get("serv299547030"));//break
		candidates.add(serviceMap.get("serv992230854"));//break
		candidates.add(serviceMap.get("serv922798621"));//break
		candidates.add(serviceMap.get("serv91250331"));//break
		candidates.add(serviceMap.get("serv1476617941"));//break
		candidates.add(serviceMap.get("serv1408824252"));//break
		candidates.add(serviceMap.get("serv1132733826"));//break
		candidates.add(serviceMap.get("serv1617120951"));//break
		candidates.add(serviceMap.get("serv648346739"));//break	
		candidates.add(serviceMap.get("serv2103146582"));//break
		candidates.add(serviceMap.get("serv1549327224"));//break
		candidates.remove(null);


		do{
			for(Service s : candidates){
				if (Math.random() <= 0.05) {
					removedServiceKeySet.add(s.getName());
					break;
				}		
			}
		}while(removedServiceKeySet.size() < 28);

//		removedServiceKeySet.clear();

		for (String key : removedServiceKeySet) {
			removedServices.add(serviceMap.get(key));
			serviceMap.remove(key);
		}
		
//		removedServices.addAll(leanSolutions.get(0).getAllServicesAndTheirBackups());

		TestSERARepairingMain.commonRemovedServices.addAll(removedServices);
		TestPlanningMain.commonRemovedServices.addAll(removedServices);
		
//		removedServices.add(serviceMap.get("serv157405514"));//break solution 1
//		removedServices.add(serviceMap.get("serv1612205357"));
//		removedServices.add(serviceMap.get("serv2103146582"));//break solution 2
//		removedServices.add(serviceMap.get("serv1197250503"));//break solution 3
//		removedServices.add(serviceMap.get("serv1889934289"));
//		removedServices.add(serviceMap.get("serv1762539517"));//break solution 4
//		removedServices.add(serviceMap.get("serv929352721"));

		
		System.out.println("=================================");
		System.out.println("============ Removing ===========");				
		System.out.println("=================================");
		System.out.println("Removed Service: (" + removedServices.size() +")");
		for(Service s : removedServices){
			serviceMap.remove(s.toString());
			System.out.println(s);
		}
		
		/**
		 * reset inverted index
		 */
		for(String key : conceptMap.keySet()){
			Concept concept = conceptMap.get(key);
			concept.resetServiceIndex();
		}
		IndexBuilder.buildInvertedIndex(conceptMap, serviceMap);
		
		/**
		 * preserve all service as well as their backups in the lean solution 
		 */
//		Set<Service> leanSolutionServices = leanSolutions.get(1).getAllServicesAndTheirBackups();
//		leanSolutionServices.add(serviceMap.get("serv368979263"));
		
		Set<Service> leanSolutionServices = new HashSet<Service>();
		leanSolutionServices = leanSolutions.get(1).getAllServicesAndTheirBackups();
		leanSolutionServices.add(serviceMap.get("serv714501922"));
		leanSolutionServices.add(serviceMap.get("serv21818098"));
		leanSolutionServices.add(serviceMap.get("serv1476617941"));
		leanSolutionServices.add(serviceMap.get("serv783934155"));
		leanSolutionServices.add(serviceMap.get("serv91250331"));
		leanSolutionServices.add(serviceMap.get("serv1546050174"));
		leanSolutionServices.add(serviceMap.get("serv853366388"));
		leanSolutionServices.add(serviceMap.get("serv160682564"));
		leanSolutionServices.add(serviceMap.get("serv1615482407"));
		leanSolutionServices.add(serviceMap.get("serv922798621"));
		leanSolutionServices.add(serviceMap.get("serv230114797"));
		leanSolutionServices.add(serviceMap.get("serv1684914640"));
		leanSolutionServices.add(serviceMap.get("serv992230854"));
		leanSolutionServices.add(serviceMap.get("serv299547030"));
		leanSolutionServices.add(serviceMap.get("serv1754346873"));
		leanSolutionServices.add(serviceMap.get("serv1061663087"));
		leanSolutionServices.add(serviceMap.get("serv368979263"));
		leanSolutionServices.add(serviceMap.get("serv1823779106"));
		leanSolutionServices.add(serviceMap.get("serv1131095320"));
		leanSolutionServices.add(serviceMap.get("serv438411496"));
		leanSolutionServices.add(serviceMap.get("serv1893211339"));
		leanSolutionServices.add(serviceMap.get("serv1200527553"));
		leanSolutionServices.add(serviceMap.get("serv507843729"));
		leanSolutionServices.add(serviceMap.get("serv1962643572"));
		leanSolutionServices.add(serviceMap.get("serv1269959786"));
		leanSolutionServices.add(serviceMap.get("serv577275962"));
		leanSolutionServices.add(serviceMap.get("serv2032075805"));
		leanSolutionServices.add(serviceMap.get("serv1339392019"));
		leanSolutionServices.add(serviceMap.get("serv646708195"));
		leanSolutionServices.add(serviceMap.get("serv2101508038"));
		leanSolutionServices.add(serviceMap.get("serv1408824252"));
		leanSolutionServices.add(serviceMap.get("serv716140428"));
		leanSolutionServices.add(serviceMap.get("serv23456642"));
		leanSolutionServices.add(serviceMap.get("serv1478256485"));
		leanSolutionServices.add(serviceMap.get("serv785572661"));
		leanSolutionServices.add(serviceMap.get("serv92888875"));
		leanSolutionServices.add(serviceMap.get("serv1547688718"));
		leanSolutionServices.add(serviceMap.get("serv855004894"));
		leanSolutionServices.add(serviceMap.get("serv162321108"));
		leanSolutionServices.add(serviceMap.get("serv1617120951"));
		leanSolutionServices.add(serviceMap.get("serv924437127"));
		leanSolutionServices.add(serviceMap.get("serv231753341"));
		leanSolutionServices.add(serviceMap.get("serv1686553184"));
		leanSolutionServices.add(serviceMap.get("serv993869360"));
		leanSolutionServices.add(serviceMap.get("serv301185574"));
		leanSolutionServices.add(serviceMap.get("serv1755985417"));
		leanSolutionServices.add(serviceMap.get("serv1063301593"));	
		leanSolutionServices.add(serviceMap.get("serv370617807"));	
		leanSolutionServices.add(serviceMap.get("serv1825417650"));	
		leanSolutionServices.add(serviceMap.get("serv1132733826"));	
		leanSolutionServices.add(serviceMap.get("serv440050040"));	
		leanSolutionServices.add(serviceMap.get("serv1894849883"));	
		leanSolutionServices.add(serviceMap.get("serv1202166059"));	
		leanSolutionServices.add(serviceMap.get("serv509482273"));	
		leanSolutionServices.add(serviceMap.get("serv1964282116"));	
		leanSolutionServices.add(serviceMap.get("serv1271598292"));	
		leanSolutionServices.add(serviceMap.get("serv578914506"));	
		leanSolutionServices.add(serviceMap.get("serv2033714349"));	
		leanSolutionServices.add(serviceMap.get("serv1341030525"));	
		leanSolutionServices.add(serviceMap.get("serv648346739"));	
		leanSolutionServices.add(serviceMap.get("serv2103146582"));	
		leanSolutionServices.add(serviceMap.get("serv1410462758"));	
		leanSolutionServices.add(serviceMap.get("serv717778972"));	
		leanSolutionServices.add(serviceMap.get("serv25095148"));	
		leanSolutionServices.add(serviceMap.get("serv1479894991"));	
		leanSolutionServices.add(serviceMap.get("serv787211205"));	
		leanSolutionServices.add(serviceMap.get("serv94527381"));	
		leanSolutionServices.add(serviceMap.get("serv1549327224"));	
		leanSolutionServices.remove(null);
		leanSolutionServices.add(serviceMap.get("serv707947822"));	
		leanSolutionServices.add(serviceMap.get("serv2025521705"));	


		/**
		 * checking if solution break
		 */
		Date fixStart = new Date();
		PlanningGraph fixedPG = null;
		PlanningGraph g = leanSolutions.get(1).clone();
		Set<Service> brokenServices = new HashSet<Service>();
		brokenServices.addAll(g.getAllServices());
		brokenServices.retainAll(removedServices);
		boolean fixed = true;
		for(Service s : brokenServices){
			Set<Service> backups = s.getBackupServiceSet();
			backups.removeAll(removedServices);
			if(!backups.isEmpty()){
				Service backup = backups.iterator().next();
				backup.getBackupServiceSet().addAll(s.getBackupServiceSet());
				backup.getBackupServiceSet().remove(backup);
				g.replaceService(s, backup);
				
			}else{
				fixed = false;
				System.out.println("service " + s + " doesn't has available backups,\njump to next solution");
				break;
			}
		}
		if(fixed){
			fixedPG = g;
		}
	
		Date fixEnd = new Date();

		if(fixedPG != null){
			System.out.println("=================================");
			System.out.println("============ Fixed PG ===========");				
			System.out.println("=================================");
			int currentLevel = 1;
			while(currentLevel < fixedPG.getALevels().size()){
				Set<Service> aLevel = fixedPG.getALevel(currentLevel);
				System.out.println("=========== Action Level " + currentLevel + " ===========");
				for(Service s : aLevel){
					System.out.println(s);
				}
				System.out.println();
				
				currentLevel++;
			}
			
			/**
			 * printout PG status (before pruning)
			 */
			System.out.println("\n=========Goal Found=========");
			System.out.println("PG Repairing Time: "
					+ (fixEnd.getTime() - fixStart.getTime()) + "ms");
			System.out.println("Execution Length: "
					+ (fixedPG.getALevels().size() - 1));
			System.out.println("Services Invoked: " + fixedPG.getAllServices().size());
			System.out.println("=============================");
		}else{
			System.out.println("No backup found, going to repairing mode!");
			
			PlanningGraph oldpg = leanSolutions.get(1).clone();
			
			leanSolutionServices.addAll(removedServices);
			pg.removeServices(leanSolutionServices);
			for(Service s : leanSolutionServices){
				serviceMap.remove(s.toString());
				System.out.println(s + " removed!");
			}
			
			/**
			 * reset inverted index
			 */
			for(String key : conceptMap.keySet()){
				Concept concept = conceptMap.get(key);
				concept.resetServiceIndex();
			}
			IndexBuilder.buildInvertedIndex(conceptMap, serviceMap);
			
			pg.regeneratePLevels();

			
			/**
			 * validate PG
			 */
			PGValidator.comboValidate(pg, serviceMap, conceptMap, thingMap, paramMap, givenConceptSet);
			
			Date repairStart = new Date();
			
//			if(RepairAlgorithm.repair(pg, serviceMap, conceptMap, thingMap, paramMap)){
			if(RepairAlgorithm.repairICWS(pg, serviceMap, conceptMap, thingMap, paramMap)){
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
		
//		System.out.println("===========================================");
//		System.out.println("============ ICWS Repairing Expr ===========");				
//		System.out.println("===========================================");
//		TestICWSRepairingAlgorithmMain(args);
//				
		
		System.out.println("===========================================");
		System.out.println("============ SERA Repairing Expr ===========");				
		System.out.println("===========================================");
		TestSERARepairingMain.main(args);
		
		System.out.println("========================================");
		System.out.println("============ Replanning Expr ===========");				
		System.out.println("========================================");
		TestPlanningMain.main(args);
		
		
		

	}//end main method
}//end class

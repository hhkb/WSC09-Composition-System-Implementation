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

import ca.concordia.pga.algorithm.utils.CombinationHelper;
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
	static final String PREFIX_URL = "/Users/ericzhao/Desktop/WSC2009_Testsets/Testset02/";
//	static final String PREFIX_URL = "/Users/ericzhao/Desktop/WSC08_Dataset/Testset01/";
	static final String TAXONOMY_URL = PREFIX_URL + "Taxonomy.owl";
	static final String SERVICES_URL = PREFIX_URL + "Services.wsdl";
	// static final String WSLA_URL = PREFIX_URL +
	// "Servicelevelagreements.wsla";
	static final String CHALLENGE_URL = PREFIX_URL + "Challenge.wsdl";

	/**
	 * Parse WSLA document from given URL
	 * 
	 * @param serviceMap
	 * @param url
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	private static void parseWSLADocument(Map<String, Service> serviceMap,
			String url) throws DocumentException {
		File wslaFile = new File(url);
		SAXReader reader = new SAXReader();
		Document document = reader.read(wslaFile);
		Element wslaRoot = document.getRootElement();
		Element obligationsRoot = wslaRoot.element("Obligations");

		int count = 0;

		for (Iterator i = obligationsRoot.elementIterator(); i.hasNext();) {
			Element serviceLevelObjective = (Element) i.next();
			String nameStr = serviceLevelObjective.attribute("name").getText();

			if (nameStr.contains("ServiceLevelObjectiveResponsetimeserv")) {
				String servName = nameStr.replaceAll(
						"ServiceLevelObjectiveResponsetime", "");
				Service s = serviceMap.get(servName);
				if (s != null) {
					count++;
					s.setResponseTime(new Integer(serviceLevelObjective
							.element("Expression").element("Predicate")
							.element("Value").getText()));
				}
			} else if (nameStr.contains("ServiceLevelObjectiveThroughputserv")) {
				String servName = nameStr.replaceAll(
						"ServiceLevelObjectiveThroughput", "");
				Service s = serviceMap.get(servName);
				if (s != null) {
					s.setThroughput(new Integer(serviceLevelObjective.element(
							"Expression").element("Predicate").element("Value")
							.getText()));
				}

			}

		}

		System.out.println("Modified WSLA Services: " + count);
	}

	/**
	 * Parse taxonomy document from given URL
	 * 
	 * @param conceptMap
	 * @param thingMap
	 * @param url
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	private static void parseTaxonomyDocument(Map<String, Concept> conceptMap,
			Map<String, Thing> thingMap, String url) throws DocumentException {
		File taxonomyFile = new File(url);
		SAXReader reader = new SAXReader();
		Document document = reader.read(taxonomyFile);
		Element taxonomyRoot = document.getRootElement();

		/**
		 * loop through semantic elements to check taxonomy
		 */
		for (Iterator i = taxonomyRoot.elementIterator(); i.hasNext();) {
			Element el = (Element) i.next();
			if (el.getName().equals("Class")) {
				Concept concept = new Concept(el.attribute("ID").getText());
				if (el.element("subClassOf") != null) {
					concept.setDirectParantName(el.element("subClassOf")
							.attribute("resource").getText()
							.replaceAll("#", ""));

				} else {
					concept.setRoot(true);
				}
				conceptMap.put(concept.getName(), concept);

			} else if (el.getName().equals("Thing")) {
				Thing thing = new Thing(el.attribute("ID").getText());

				thing.setType(el.element("type").attribute("resource")
						.getText().replaceAll("#", ""));

				thingMap.put(thing.getName(), thing);
			}
		}

		/**
		 * build indexing for concept
		 */
		for (String key : conceptMap.keySet()) {
			Concept concept = conceptMap.get(key);
			Concept varConcept = conceptMap.get(key);
			do {
				concept.addConceptToParentIndex(varConcept);
				varConcept.addConceptToChildrenIndex(concept);
				if (varConcept.isRoot()) {
					varConcept = null;
				} else {
					varConcept = conceptMap.get(varConcept
							.getDirectParantName());
				}

			} while (varConcept != null);

		}

	}

	/**
	 * Parse services document from given URL
	 * 
	 * @param serviceMap
	 * @param paramMap
	 * @param conceptMap
	 * @param thingMap
	 * @param url
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	private static void parseServicesDocument(Map<String, Service> serviceMap,
			Map<String, Param> paramMap, Map<String, Concept> conceptMap,
			Map<String, Thing> thingMap, String url) throws DocumentException {

		File ServicesFile = new File(url);
		SAXReader reader = new SAXReader();
		Document document = reader.read(ServicesFile);
		Element servicesRoot = document.getRootElement();
		Element semRoot = servicesRoot.element("semExtension");

		/**
		 * loop through semantic elements
		 */
		Service service = null;

		for (Iterator i = semRoot.elementIterator(); i.hasNext();) {
			Element semMsgExtEl = (Element) i.next();
			if (semMsgExtEl.getName().equals("semMessageExt")) {
				boolean isRequestParam;
				if (semMsgExtEl.attribute("id").getText().contains(
						"RequestMessage")) {
					service = new Service(semMsgExtEl.attribute("id").getText()
							.replaceAll("RequestMessage", ""));
					isRequestParam = true;
				} else {
					isRequestParam = false;
				}

				for (Iterator j = semMsgExtEl.elementIterator(); j.hasNext();) {
					Element semExtEl = (Element) j.next();
					if (semExtEl.getName().equals("semExt")) {
						Param param = new Param(semExtEl.attribute("id")
								.getText());
						Thing thing = thingMap.get(semExtEl.element(
								"ontologyRef").getText().replaceAll(
								"http://www.ws-challenge.org/wsc08.owl#", ""));

						param.setThing(thing);
						paramMap.put(param.getName(), param);
						if (isRequestParam) {
							service.addInputParam(param);
							service.addInputConcept(conceptMap.get(thing
									.getType()));
						} else {
							service.addOutputParam(param);
							for (Concept c : conceptMap.get(thing.getType())
									.getParentConceptsIndex()) {
								service.addOutputConcept(c);
							}
						}
					}
				}
				if (semMsgExtEl.attribute("id").getText().contains(
						"ResponseMessage")) {
					serviceMap.put(service.getName(), service);
				}

			}
		}
	}

	/**
	 * Build inverted indexing table: concept -> all services who accept the
	 * concept
	 * 
	 * @param conceptMap
	 * @param serviceMap
	 */
	private static void buildInvertedIndex(Map<String, Concept> conceptMap,
			Map<String, Service> serviceMap) {
		for (String serviceKey : serviceMap.keySet()) {
			Service service = serviceMap.get(serviceKey);
			for (Param param : service.getInputParamSet()) {
				Concept concept = conceptMap.get(param.getThing().getType());
				for (Concept childrenConcept : concept
						.getChildrenConceptsIndex()) {
					childrenConcept.addServiceToIndex(service);
				}
			}
		}
	}

	/**
	 * Parse the challenge String given by client. Also convert the I/O params
	 * to concepts
	 * 
	 * @param paramMap
	 * @param conceptMap
	 * @param thingMap
	 * @param pg
	 * @param url
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	private static void parseChallengeDocument(Map<String, Param> paramMap,
			Map<String, Concept> conceptMap, Map<String, Thing> thingMap,
			PlanningGraph pg, String url) throws DocumentException {

		Set<Concept> initPLevel = new HashSet<Concept>();
		Set<Concept> goalSet = new HashSet<Concept>();

		File ServicesFile = new File(url);
		SAXReader reader = new SAXReader();
		Document document = reader.read(ServicesFile);
		Element servicesRoot = document.getRootElement();
		Element semRoot = servicesRoot.element("semExtension");

		for (Iterator i = semRoot.elementIterator(); i.hasNext();) {
			Element semMsgExtEl = (Element) i.next();
			if (semMsgExtEl.getName().equals("semMessageExt")) {
				boolean isRequestParam;
				if (semMsgExtEl.attribute("id").getText().contains(
						"RequestMessage")) {
					isRequestParam = true;
				} else {
					isRequestParam = false;
				}

				for (Iterator j = semMsgExtEl.elementIterator(); j.hasNext();) {
					Element semExtEl = (Element) j.next();
					if (semExtEl.getName().equals("semExt")) {
						Param param = new Param(semExtEl.attribute("id")
								.getText());
						Thing thing = thingMap.get(semExtEl.element(
								"ontologyRef").getText().replaceAll(
								"http://www.ws-challenge.org/wsc08.owl#", ""));

						param.setThing(thing);
						paramMap.put(param.getName(), param);
						if (isRequestParam) {
							for (Concept c : conceptMap.get(thing.getType())
									.getParentConceptsIndex()) {
								initPLevel.add(c);
							}
						} else {
							goalSet.add(conceptMap.get(thing.getType()));
						}
					}
				}
				if (semMsgExtEl.attribute("id").getText().contains(
						"ResponseMessage")) {
					pg.addPLevel(initPLevel);
					pg.setGoalSet(goalSet);
				}

			}
		}
		pg.addALevel(new HashSet<Service>());

	}

	/**
	 * Generate BPEL file from the solution
	 * 
	 * @param pg
	 * @throws IOException
	 */
	private static void generateSolution(PlanningGraph pg) throws IOException {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("problemStructure");
		Element solutions = root.addElement("solutions");
		Element solution = solutions.addElement("solution");
		Element sequenceRoot = solution.addElement("sequence");
		for (int i = 1; i < pg.getALevels().size(); i++) {
			Set<Service> actionLevel = pg.getALevel(i);
			Element parallel = sequenceRoot.addElement("parallel");
			for (Service s : actionLevel) {
				Element serviceDesc = parallel.addElement("serviceDesc");
				Element abstraction = serviceDesc.addElement("abstraction");
				Element realizations = serviceDesc.addElement("realizations");
				Element input = abstraction.addElement("input");
				Element output = abstraction.addElement("output");
				Element service = realizations.addElement("service");
				service.addAttribute("name", s.getName());
				for (Concept c : s.getInputConceptSet()) {
					input.addElement("concept").addAttribute("name",
							c.getName());
				}
				for (Concept c : s.getOutputConceptSet()) {
					output.addElement("concept").addAttribute("name",
							c.getName());
				}
			}
		}

		/**
		 * write problem.xml to a file
		 */
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new FileWriter(
				"/Users/ericzhao/Desktop/problem.xml"), format);
		writer.write(document);
		writer.close();

		/**
		 * call BPEL creator to convert problem.xml to BPEL and save it to a
		 * file
		 */
		BPEL_Creator bpelCreator = new BPEL_Creator(
				"/Users/ericzhao/Desktop/problem.xml");
		bpelCreator.createBPELDocument();
		bpelCreator.saveBPELDocument("/Users/ericzhao/Desktop/Solution.bpel");

	}

	/**
	 * compute  removable service set from the given goal concept set
	 * @param conceptSet
	 * @return
	 */
	private static Set<Service> getRemovableServiceSet(Set<Concept> conceptSet){
		Set<Service> removableServiceSet = new HashSet<Service>();
		Set<Service> unRemovableServiceSet = new HashSet<Service>();
		/**
		 * get removable service
		 */
		for (Concept c : conceptSet) {
			if (c.getOriginServiceSet().size() > 1) {
				removableServiceSet.addAll(c.getOriginServiceSet());
			} else if (c.getOriginServiceSet().size() == 1) {
				unRemovableServiceSet.addAll(c.getOriginServiceSet());
			}
		}
		
		removableServiceSet.removeAll(unRemovableServiceSet);

		return removableServiceSet;
	}

	/**
	 * Implementation for computing all alternative routes that can produce
	 * given concepts
	 * @param conceptSet
	 * @param routes
	 */
	@SuppressWarnings("unchecked")
	private static void computeRoutes(Set<Concept> conceptSet,
			Set<Set<Service>> routes) {

		List<List<Service>> combinations;
		
		Set<Service> removableServiceSet = getRemovableServiceSet(conceptSet);
	
		combinations = CombinationHelper.findCombinations(removableServiceSet);
		combinations.remove(new ArrayList<Service>());

		if(removableServiceSet.size() == 0){
			Set<Service> minimumServiceSet = new HashSet<Service>();
			for (Concept c : conceptSet) {
				minimumServiceSet.addAll(c.getOriginServiceSet());
			}
			routes.add(minimumServiceSet);
		}
		for(List<Service> combinationList : combinations){
			
			boolean valid = true;
			boolean atom = false;
			
			Set<Service> combination = new HashSet<Service>();
			combination.addAll(combinationList);
			/**
			 * clone conceptSet to currStatus
			 */
			Set<Concept> currStatus = new HashSet<Concept>();
			for (Concept c : conceptSet) {
				Concept concept = new Concept(c.getName());
				concept.getOriginServiceSet().addAll(
						c.getOriginServiceSet());
				currStatus.add(concept);
			}
			/**
			 * remove removable services from the clone
			 */
			for (Concept c : currStatus) {
				c.getOriginServiceSet().removeAll(combination);
//				for(Service s : combinationList){
//					c.getOriginServiceSet().remove(s);
//				}
				if(c.getOriginServiceSet().size() == 0){
					valid = false;
					break;
				}
			}
			if(getRemovableServiceSet(currStatus).size() == 0){
				atom = true;
			}

			if(valid & atom){
				Set<Service> minimumServiceSet = new HashSet<Service>();
				for (Concept c : currStatus) {
					minimumServiceSet.addAll(c.getOriginServiceSet());
				}
				routes.add(minimumServiceSet);
			}
		}

	}
	
	/**
	 * Backward search based on PG to prune redundant web services
	 * @param pg
	 * @return routeCouters
	 */
	private static Vector<Integer> refineSolution(PlanningGraph pg) {

		int currLevel = pg.getALevels().size() - 1;

		Set<Service> minimumServiceSet; 
		Set<Concept> subGoalSet = pg.getGoalSet();
		Set<Concept> leftGoalSet = new HashSet<Concept>();
		Set<Set<Service>> routes = new HashSet<Set<Service>>();
		Vector<Integer> routeCounters = new Vector<Integer>(); //debug purpose, will be remove
		Map<Integer, Set<Service>> solutionMap = new HashMap<Integer, Set<Service>>();
		do {
			/**
			 * compute services that each concept is origin from
			 */
			Set<Service> actionSet = pg.getALevel(currLevel);
			subGoalSet.addAll(leftGoalSet);
			leftGoalSet.clear();
			for (Concept g : subGoalSet) {
				for (Service s : actionSet) {
					if (s.getOutputConceptSet().contains(g)) {
						g.addServiceToOrigin(s);
					}
				}
				/**
				 * check if the goal can be produced by current action level
				 */
				if(g.getOriginServiceSet().size() == 0){
					leftGoalSet.add(g);
				}
			}
			/**
			 * defer the goals that cannot be produced in current action level
			 */
			subGoalSet.removeAll(leftGoalSet);

			/**
			 * compute all alternative routes that current level has  
			 */
//			computeRoutes_Old(subGoalSet, routes);
			computeRoutes(subGoalSet, routes);
			
			/**
			 * get the route with minimum web services to invoke
			 */
			minimumServiceSet = new HashSet<Service>();
			Iterator<Set<Service>> itr = routes.iterator();
			while(itr.hasNext()){
				Set<Service> candidate = itr.next();
				if(minimumServiceSet.size() == 0){
					minimumServiceSet = candidate;
				}else if(minimumServiceSet.size() > candidate.size()){
					minimumServiceSet = candidate;
				}
			}
			
			/**
			 * overwrite selected route into pg's ALevel
			 * (temperate implementation! in order to keep pg's information)
			 */
			pg.setALevel(currLevel, minimumServiceSet);
			solutionMap.put(currLevel, minimumServiceSet);
			
			/**
			 * get the inputs of invoked web services as subGoals
			 */
			subGoalSet.clear();
			for (Service s : minimumServiceSet) {
				subGoalSet.addAll(s.getInputConceptSet());
			}
			/**
			 * reset routes
			 */
			routeCounters.add(routes.size());
			routes.clear();
			currLevel--;

		} while (currLevel > 0);
		
		/**
		 * debug checking if solution is valid
		 */
		if(leftGoalSet.size() != 0){
			System.out.println("**Solution is NOT VALID!");
		}
		return routeCounters; //temperate for debug, will be removed!
	}

	
	private static boolean generatePG(Set<Concept> knownConceptSet, Set<Service> currInvokableServiceSet,
			Set<Service> currNonInvokableServiceSet, Set<Service> invokedServiceSet,
			PlanningGraph pg){
		int currentLevel = 0;
		do {
			/**
			 * point knownConceptSet to pg's current PLevel
			 */
			knownConceptSet = pg.getPLevel(currentLevel);
			currInvokableServiceSet = new HashSet<Service>();
			currNonInvokableServiceSet = new HashSet<Service>();
			Set<Concept> pLevel = new HashSet<Concept>();
			/**
			 * fetch all possible candidates
			 */
			for (Concept c : pg.getPLevel(currentLevel)) {
				currInvokableServiceSet.addAll(c.getServicesIndex());
			}
			/**
			 * remove those who have already been invoked
			 */
			currInvokableServiceSet.removeAll(invokedServiceSet);
			/**
			 * remove those whose invocation condition have not been satisfied
			 */
			for (Service s : currInvokableServiceSet) {
				if (!pg.getPLevel(currentLevel).containsAll(
						s.getInputConceptSet())) {
					currNonInvokableServiceSet.add(s);
				}
			}
			currInvokableServiceSet.removeAll(currNonInvokableServiceSet);
			if (currInvokableServiceSet.size() <= 0) {
				break;
			}
			/**
			 * invoke the services
			 */
			invokedServiceSet.addAll(currInvokableServiceSet);
			pg.addALevel(currInvokableServiceSet);
			/**
			 * generate PLevel
			 */
			for (Service s : currInvokableServiceSet) {
				knownConceptSet.addAll(s.getOutputConceptSet());
			}
			pLevel.addAll(knownConceptSet);
			pg.addPLevel(pLevel);
			/**
			 * increase the level and print out newly invoked services
			 */
			currentLevel++;
			System.out.println("\n*********Action Level " + currentLevel
					+ " *******");
			for (Service s : pg.getALevel(currentLevel)) {
				System.out.print(s + "|");
			}
			System.out.println();

		} while (!knownConceptSet.containsAll(pg.getGoalSet())
				& !currInvokableServiceSet.isEmpty());

		return knownConceptSet.containsAll(pg.getGoalSet());
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

		Set<Service> invokedServiceSet = new HashSet<Service>();
		Set<Service> currInvokableServiceSet = new HashSet<Service>();
		Set<Service> currNonInvokableServiceSet = new HashSet<Service>();
		Set<Concept> knownConceptSet = null; // shortcut to pg's current PLevel
		
		Set<Concept> givenConceptSet = new HashSet<Concept>();
		Set<Concept> goalConceptSet = new HashSet<Concept>();

		Date initStart = new Date();
		try {
			parseTaxonomyDocument(conceptMap, thingMap, TAXONOMY_URL);
			parseServicesDocument(serviceMap, paramMap, conceptMap, thingMap,
					SERVICES_URL);
			// parseWSLADocument(serviceMap, WSLA_URL);
		} catch (DocumentException e) {

			e.printStackTrace();
		}

		buildInvertedIndex(conceptMap, serviceMap);
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
			parseChallengeDocument(paramMap, conceptMap, thingMap, pg,
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
		
		boolean goalFound = generatePG(knownConceptSet,currInvokableServiceSet,
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
			Vector<Integer> routesCounters = refineSolution(pg);
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
				generateSolution(pg);
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
		for(String key : serviceMap.keySet()){
			if(Math.random() <= 0.25){
				removedServiceKeySet.add(key);
			}
		}
		for(String key : removedServiceKeySet){
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
		buildInvertedIndex(conceptMap, serviceMap);
		
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
		knownConceptSet = null; // shortcut to pg's current PLevel
		
		/**
		 * PG Algorithm Implementation
		 */
		
		goalFound = generatePG(knownConceptSet,currInvokableServiceSet,
			currNonInvokableServiceSet,invokedServiceSet,
			pg);
		
		Date replanningEnd = new Date(); // end composition checkpoint

		/**
		 * Print out the composition result
		 */
		if (goalFound) {

			/**
			 * printout PG status (before pruning)
			 */
			System.out.println("\n=========Goal Found=========");
			System.out.println("PG Composition Time: "
					+ (replanningEnd.getTime() - replanningStart.getTime()) + "ms");
			System.out.println("Execution Length: "
					+ (pg.getALevels().size() - 1));
			System.out.println("Services Invoked: " + invokedServiceSet.size());
			System.out.println("=============================");

			
			/**
			 * do backward search to remove redundancy (pruning PG)
			 */
			Vector<Integer> routesCounters = refineSolution(pg);
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

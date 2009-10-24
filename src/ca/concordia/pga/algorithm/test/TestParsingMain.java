package ca.concordia.pga.algorithm.test;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import ca.concordia.pga.models.*;

/**
 * 
 * @author Ludeng Zhao(Eric)
 * 
 */
public class TestParsingMain {

	//change the Prefix URL according your environment
	static final String PREFIX_URL = "/Users/ericzhao/Desktop/WSC08_Dataset/Testset01/";
	static final String TAXONOMY_URL = PREFIX_URL + "Taxonomy.owl";
	static final String SERVICES_URL = PREFIX_URL + "Services.wsdl";
	static final String CHALLENGE_URL = PREFIX_URL + "Challenge.wsdl";

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
								service.addInputConcept(conceptMap.get(thing.getType()));
							
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

	private static void buildInvertedIndexing(Map<String, Concept> conceptMap,
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
		Date start = new Date();
		try {
			parseTaxonomyDocument(conceptMap, thingMap, TAXONOMY_URL);
			parseServicesDocument(serviceMap, paramMap, conceptMap, thingMap, SERVICES_URL);
		} catch (DocumentException e) {

			e.printStackTrace();
		}

		buildInvertedIndexing(conceptMap, serviceMap);
		Date end = new Date();
		
		System.out.println("Initializing Time " + (end.getTime() - start.getTime()));

		System.out.println("Concepts size " + conceptMap.size());
		System.out.println("Things size " + thingMap.size());
		System.out.println("Param size " + paramMap.size());
		System.out.println("Services size " + serviceMap.size());

//		System.out.println("**********************************");
//		System.out.println("********Inverted Indexing*********");
//		System.out.println("**********************************");
//
//		for (String key : conceptMap.keySet()) {
//			Concept concept = conceptMap.get(key);
//			if (concept.getServicesIndex().size() > 0) {
//				System.out.print(concept.getName() + " ===> ");
//				for (Service s : concept.getServicesIndex()) {
//					System.out.print(s.getName() + " | ");
//				}
//				System.out.println("");
//
//			}
//		}
		
//		int equalService = 0;
//		for(String key : serviceMap.keySet()){
//			Service s = serviceMap.get(key);
//			for(String key2 : serviceMap.keySet()){
//				Service s2 = serviceMap.get(key2);
//				if(!s.equals(s2) & s.getInputConceptSet().equals(s2.getInputConceptSet())
//						& s.getOutputConceptSet().equals(s2.getOutputConceptSet())){
//					s.addEquivalentService(s2);
//					equalService++;
//				}
//			}
//		}
//		System.out.println("equivalent service number: " + equalService);


		/**
		 * begin the algorithm implementation
		 */
		
		try {
			parseChallengeDocument(paramMap, conceptMap, thingMap, pg, CHALLENGE_URL);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		System.out.println();
		System.out.println("Given Concepts: ");
		for(Concept c : pg.getPLevel(0)){
			System.out.print(c + " | ");
		}
		System.out.println();
		System.out.println("Goal Concepts: ");
		for(Concept c : pg.getGoalSet()){
			System.out.print(c + " | ");
		}
		
		System.out.println();
		Service service = serviceMap.get("serv1056747493");
		for(Concept c : pg.getPLevel(0)){
			if(service.getInputConceptSet().contains(c))
				System.out.print(c + " | ");
		}

		
		Set<Service> invokableSet1 = new HashSet<Service>();
		Set<Service> invokableSet2 = new HashSet<Service>();
		Set<Service> notInvokableSet2 = new HashSet<Service>();
		Date t1 = new Date();
		for(int i=0; i<100000; i++){
			invokableSet1.clear();
			for(String key : serviceMap.keySet()){
				Service s = serviceMap.get(key);
				if(pg.getPLevel(0).containsAll(s.getInputConceptSet())){
					invokableSet1.add(s);
				}
			}
				
		}
		Date t2 = new Date();

		Date t3 = new Date();
		for(int i=0; i<100000; i++){
			invokableSet2.clear();
			notInvokableSet2.clear();
			for(Concept c : pg.getPLevel(0)){
				invokableSet2.addAll(c.getServicesIndex());
			}
			
			for(Service s : invokableSet2){
				if(!pg.getPLevel(0).containsAll(s.getInputConceptSet())){
					notInvokableSet2.add(s);
				}
			}
			invokableSet2.removeAll(notInvokableSet2);		
		}
		Date t4 = new Date();
		
		
		System.out.println("\n\nfirst timer: " + (t2.getTime() - t1.getTime()));
		System.out.println("\n\nsecond timer: " + (t4.getTime() - t3.getTime()));
	
	}
}

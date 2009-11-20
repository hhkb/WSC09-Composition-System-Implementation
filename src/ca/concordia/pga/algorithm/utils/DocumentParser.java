package ca.concordia.pga.algorithm.utils;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import ca.concordia.pga.models.Concept;
import ca.concordia.pga.models.Param;
import ca.concordia.pga.models.PlanningGraph;
import ca.concordia.pga.models.Service;
import ca.concordia.pga.models.Thing;

public class DocumentParser {

	/**
	 * Parse WSLA document from given URL
	 * 
	 * @param serviceMap
	 * @param url
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	public static void parseWSLADocument(Map<String, Service> serviceMap,
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
	public static void parseTaxonomyDocument(Map<String, Concept> conceptMap,
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
	public static void parseServicesDocument(Map<String, Service> serviceMap,
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
	public static void parseChallengeDocument(Map<String, Param> paramMap,
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


}

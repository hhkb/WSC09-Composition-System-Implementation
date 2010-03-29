package ca.concordia.pga.algorithm.test;



import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import ca.concordia.pga.models.*;

/**
 * This class is for testing and experiment purpose
 * 
 * @author Ludeng Zhao(Eric)
 * 
 */
public class TestSolutionXMLMain {

	// change the Prefix URL according your environment
	static final String PREFIX_URL = "/Users/ericzhao/Desktop/WSC/WSC08_Dataset/Testset01/";
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
		 * 				<service name="serv1056747493"/>
						<service name="serv364063707"/>
						<service name="serv1818863550"/>
		 */
		
		Set<Concept> inputs = new HashSet<Concept>();
		Set<Concept> outputs = new HashSet<Concept>();
		Set<Concept> eInputs = new HashSet<Concept>();
		Set<Concept> eOutputs = new HashSet<Concept>();
		Set<Param> inputParams = new HashSet<Param>();
		Set<Param> outputParams = new HashSet<Param>();
		Set<Service> services = new HashSet<Service>();
		Set<Concept> knownConceptSet = new HashSet<Concept>(); 
		
		Concept c1 = conceptMap.get("con1556905685");
		Concept c2 = conceptMap.get("con1660132741");
		Service s1 = serviceMap.get("serv1823779106");
		/**
		 * step1
		 */
		services.add(serviceMap.get("serv1056747493"));
		services.add(serviceMap.get("serv364063707"));
		services.add(serviceMap.get("serv1818863550"));

		for(Service s : services){
			for(Concept c : s.getInputConceptSet()){
				inputs.add(c);
			}
			for(Concept c : s.getOutputConceptSet()){
				outputs.add(c);
			}
			for(Param p : s.getInputParamSet()){
				inputParams.add(p);
			}
			for(Param p : s.getOutputParamSet()){
				outputParams.add(p);
			}
		}
		knownConceptSet.addAll(givenConceptSet);
		knownConceptSet.addAll(outputs);
		
//		/**
//		 * step2
//		 */
//		services.clear();
//		inputs.clear();
//		outputs.clear();
//		eInputs.clear();
//		eOutputs.clear();
//		inputParams.clear();
//		outputParams.clear();
//		
//		services.add(serviceMap.get("serv1126179726"));
////		services.add(serviceMap.get("serv433495940"));
////		services.add(serviceMap.get("serv1888295783"));
//		
//		eInputs.add(conceptMap.get("con279680725"));
//		eInputs.add(conceptMap.get("con615372196"));
//		eInputs.add(conceptMap.get("con619673607"));
//		eInputs.add(conceptMap.get("con1387319403"));
//		
//		eOutputs.add(conceptMap.get("con395605841"));
//		eOutputs.add(conceptMap.get("con467904946"));
//
//		for(Service s : services){
//			for(Concept c : s.getInputConceptSet()){
//				inputs.add(c);
//			}
//			for(Concept c : s.getOutputConceptSet()){
//				outputs.add(c);
//			}
//			for(Param p : s.getInputParamSet()){
//				inputParams.add(p);
//			}
//			for(Param p : s.getOutputParamSet()){
//				outputParams.add(p);
//			}
//		}
		
		System.out.println("\nInputs:" + inputs.size());
		for(Concept c : inputs){
			System.out.println(c);
		}
		
		System.out.println("\nOutputs:" + outputs.size());
		for(Concept c : outputs){
			System.out.println(c);
		}
		
		outputs.removeAll(knownConceptSet);
		System.out.println("\nNew Outputs:" + outputs.size());
		for(Concept c : outputs){
			System.out.println(c);
		}
		
		System.out.println("\nInputParams:" + inputParams.size());
		for(Param p : inputParams){
			System.out.println(p);
		}
		
		System.out.println("\nOutputParams:" + outputParams.size());
		for(Param p : outputParams){
			System.out.println(p);
		}
		
		outputs.clear();
		System.out.println("\nnewOutputs:" + outputs.size());
		for(Param p : outputParams){
			Thing t = p.getThing();
			Concept c = conceptMap.get(t.getType());
			if(!knownConceptSet.contains(c)){
				outputs.add(c);
				if(eOutputs.contains(c)){
					System.out.println(c + "*");		
				}else{
					System.out.println(c);		
				}
			}
		}
		
	}

}

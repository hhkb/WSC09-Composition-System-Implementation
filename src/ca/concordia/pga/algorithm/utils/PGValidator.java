package ca.concordia.pga.algorithm.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ca.concordia.pga.models.Concept;
import ca.concordia.pga.models.Param;
import ca.concordia.pga.models.PlanningGraph;
import ca.concordia.pga.models.Service;
import ca.concordia.pga.models.Thing;

public class PGValidator {
	
	public static void debugService(PlanningGraph pg, Map<String, Service> serviceMap,
			Map<String, Concept> conceptMap, Map<String, Thing> thingMap,
			Map<String, Param> paramMap, String serviceName){
		
		Service service = serviceMap.get(serviceName);
		System.out.println("Service " + serviceName);
		System.out.println("Input: ");
		for(Concept c : service.getInputConceptSet()){
			System.out.print(c + " | ");
		}
		System.out.println("Output: ");
		for(Concept c : service.getOutputConceptSet()){
			System.out.print(c + " | ");
		}
		System.out.println();

	}
	
	public static boolean debug(PlanningGraph pg, Map<String, Service> serviceMap,
			Map<String, Concept> conceptMap, Map<String, Thing> thingMap,
			Map<String, Param> paramMap){

		Concept con = conceptMap.get("con835752772");
		System.out.println("Provided by: ");
		for(Service sp : con.getProducedByServices()){
			System.out.print(sp + " | ");
		}
		System.out.println("\n");
		
//		int currentLevel = 1;
//		do{
//			for(Service s : pg.getALevel(currentLevel)){
//				if(s.getOutputConceptSet().contains(con)){
//					System.out.println(con + " Found in Level " + currentLevel);
//					System.out.println("Provided by: ");
//					for(Service sp : con.getProducedByServices()){
//						System.out.print(sp + " | ");
//					}
//					System.out.println("\n");
//					return true;
//				}
//			}
//			currentLevel++;
//		}while(currentLevel < pg.getALevels().size());
		return false;	

	}

	public static boolean validate(PlanningGraph pg, Map<String, Service> serviceMap,
			Map<String, Concept> conceptMap, Map<String, Thing> thingMap,
			Map<String, Param> paramMap){
		

		int currentLevel = 1;
		Set<Concept> knownConceptSet = new HashSet<Concept>();
		System.out.println();
		do{
			/**
			 * check if each action can be invoked
			 */
			knownConceptSet.addAll(pg.getPLevel(currentLevel-1));

			for(Service s : pg.getALevel(currentLevel)){
				if(!pg.getPLevel(currentLevel-1).containsAll(s.getInputConceptSet())){
					System.out.println("Service (" + s + ") cannot be invoked");
					return false;
				}
				knownConceptSet.addAll(s.getOutputConceptSet());
			}

			
			/**
			 * check if there is invalid concept
			 */
			if(!pg.getPLevel(currentLevel).equals(knownConceptSet)){
				System.out.println("\nPG PLevel " + currentLevel + ": " + pg.getPLevel(currentLevel).size());
				for(Concept c : pg.getPLevel(currentLevel)){
					System.out.print(c + " ");
				}
				System.out.println("\nShould be " + ": " + knownConceptSet.size());			
				for(Concept c : knownConceptSet){
					System.out.print(c + " ");
				}
				
				Set<Concept> diffs = new HashSet<Concept>();
				if(pg.getPLevel(currentLevel).size() > knownConceptSet.size()){
					diffs.addAll(pg.getPLevel(currentLevel));
					diffs.removeAll(knownConceptSet);
				}else{
					diffs.addAll(knownConceptSet);
					diffs.removeAll(pg.getPLevel(currentLevel));
				}

				System.out.println("\nDiffs " + ": " + diffs.size());			
				for(Concept c : diffs){
					System.out.print(c + " ");
				}
				System.out.println("\n");
				
				return false;
			}			
			currentLevel++;
			
		}while(currentLevel < pg.getALevels().size());

		
		return true;
	}
	
	public static boolean validateGivenConcepts(PlanningGraph pg, 
			Set<Concept> givenConceptSet){
		if(pg.getPLevel(0).equals(givenConceptSet)){
			return true;
		}
		return false;
	}
	
	public static boolean validateGoalConcepts(PlanningGraph pg, 
			Set<Concept> goalConceptSet){
		if(pg.getPLevel(pg.getALevels().size()-1).containsAll(goalConceptSet)){
			return true;
		}
		return false;
	}
	
	public static boolean hasEmptyLevel(PlanningGraph pg){
		int currentLevel = 1;
		while(currentLevel < pg.getALevels().size()){
			if(pg.getALevel(currentLevel).size() == 0){
				System.out.println("Level " + currentLevel);
				return true;
			}	
			currentLevel++;
		}

		return false;
	}
	
	public static boolean comboValidate(PlanningGraph pg, Map<String, Service> serviceMap,
			Map<String, Concept> conceptMap, Map<String, Thing> thingMap,
			Map<String, Param> paramMap, Set<Concept> givenConceptSet){
	
		boolean isValid = true;
		System.out.println("");
		if(PGValidator.validate(pg, serviceMap, conceptMap, thingMap, paramMap)){
			System.out.println("PG is VALID!");
		}else{
			System.out.println("PG is NOT VALID!");
			isValid = false;
		}
		
		if(PGValidator.validateGivenConcepts(pg, givenConceptSet)){
			System.out.println("GivenConcepts is VALID!");
		}else{
			System.out.println("GivenConcepts is NOT VALID!");
			isValid = false;
		}
		
		if(PGValidator.validateGoalConcepts(pg, pg.getGoalSet())){
			System.out.println("GoalConcepts is VALID!");
		}else{
			System.out.println("GoalConcepts is NOT VALID!");
			isValid = false;
		}
		
		if(PGValidator.hasEmptyLevel(pg)){
			System.out.println("PG has empty level!");
			isValid = false;
		}else{
			System.out.println("PG doesn't have empty level!");
		}
		return isValid;
	}
	
}

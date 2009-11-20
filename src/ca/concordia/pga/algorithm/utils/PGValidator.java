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
	
	public static boolean debug(PlanningGraph pg, Map<String, Service> serviceMap,
			Map<String, Concept> conceptMap, Map<String, Thing> thingMap,
			Map<String, Param> paramMap){
//		con144093309
		Concept con = new Concept("con144093309");
		int currentLevel = 1;
		do{
			for(Service s : pg.getALevel(currentLevel)){
				if(s.getOutputConceptSet().contains(con)){
					System.out.println("Found in Level " + currentLevel);
					return true;
				}
			}
			currentLevel++;
		}while(currentLevel < pg.getALevels().size());
		return false;	

	}

	public static boolean validate(PlanningGraph pg, Map<String, Service> serviceMap,
			Map<String, Concept> conceptMap, Map<String, Thing> thingMap,
			Map<String, Param> paramMap){
		

		int currentLevel = 1;
		Set<Concept> knownConceptSet = new HashSet<Concept>();
		do{
			/**
			 * check if each action can be invoked
			 */
			knownConceptSet.addAll(pg.getPLevel(currentLevel-1));

			for(Service s : pg.getALevel(currentLevel)){
				if(!pg.getPLevel(currentLevel-1).containsAll(s.getInputConceptSet())){
					return false;
				}
				knownConceptSet.addAll(s.getOutputConceptSet());
			}
//
//			System.out.println("\nPG PLevel: ");
//			for(Concept c : pg.getPLevel(currentLevel)){
//				System.out.print(c + " ");
//			}
//			System.out.println("\nShould be: ");			
//			for(Concept c : knownConceptSet){
//				System.out.print(c + " ");
//			}
//			System.out.println();
			
			/**
			 * check if there is invalid concept
			 */
			if(!pg.getPLevel(currentLevel).equals(knownConceptSet)){
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
}

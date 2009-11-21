package ca.concordia.pga.algorithm;

import java.util.HashSet;
import java.util.Set;

import ca.concordia.pga.models.Concept;
import ca.concordia.pga.models.PlanningGraph;
import ca.concordia.pga.models.Service;

/**
 * 
 * @author Ludeng Zhao(Eric)
 *
 */
public class RemovalAlgorithm {

	public static Set<Concept> removeServcesFromPG(PlanningGraph pg,
			Set<Service> removedServiceSet){
		Set<Concept> brokenPreConditionSet = new HashSet<Concept>();
		Set<Concept> conceptsInPG = getConceptsInPG(pg);
		Set<Concept> removableConceptSet = new HashSet<Concept>();
		
		int currentLevel = 1;

		while(currentLevel < pg.getALevels().size()){
			Set<Service> aLevel = pg.getALevel(currentLevel);
			Set<Service> currentRemovedServiceSet = new HashSet<Service>();
			currentRemovedServiceSet.addAll(aLevel);
			currentRemovedServiceSet.retainAll(removedServiceSet);
			aLevel.removeAll(removedServiceSet);
			
			for(Concept c : conceptsInPG){
				c.getUsedByServices().removeAll(removedServiceSet);
//				c.getProducedByServices().removeAll(removedServiceSet);
			}
			
			for(Service s : currentRemovedServiceSet){
				for(Concept c : s.getOutputConceptSet()){
					c.getProducedByServices().remove(s);

					if(c.getProducedByServices().size() == 0 & !c.isRin()){
						removableConceptSet.add(c);
						if(c.getUsedByServices().size() != 0 | c.isGoal()){
							brokenPreConditionSet.add(c);
						}
					}
				}
			}
			
			currentLevel++;
		}
		
		for(Concept c : conceptsInPG){
			if(c.getProducedByServices().size() == 0 & !c.isRin()){
				removableConceptSet.add(c);
			}
		}
		
		currentLevel = 1;
		while(currentLevel < pg.getPLevels().size()){
			pg.getPLevel(currentLevel).removeAll(removableConceptSet);
			currentLevel++;
		}
		
		return brokenPreConditionSet;

	}
	
	public static void openForwardRemoval(){
		
	}
	
	private static Set<Concept> getConceptsInPG(PlanningGraph pg){
		
		Set<Concept> conceptsInPG = new HashSet<Concept>();
		int currentLevel = 0;
		do{
			conceptsInPG.addAll(pg.getPLevel(currentLevel));
			currentLevel++;
		}while(currentLevel < pg.getALevels().size());
		
		for(Concept c : pg.getPLevel(0)){
			c.setRin(true);
		}
		
		for(Concept c : pg.getPLevel(pg.getPLevels().size()-1)){
			if(pg.getGoalSet().contains(c)){
				c.setGoal(true);
			}
		}
		
		return conceptsInPG;
	}
}

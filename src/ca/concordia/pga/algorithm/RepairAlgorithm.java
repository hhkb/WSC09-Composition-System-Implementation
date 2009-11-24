package ca.concordia.pga.algorithm;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.concordia.pga.algorithm.utils.RepairingEvaluator;
import ca.concordia.pga.models.Concept;
import ca.concordia.pga.models.Param;
import ca.concordia.pga.models.PlanningGraph;
import ca.concordia.pga.models.Service;
import ca.concordia.pga.models.Thing;

/**
 * 
 * @author Ludeng Zhao(Eric)
 *
 */
public class RepairAlgorithm {
	/**
	 * define comparator for sorting services according heuristic scores
	 */
	private static Comparator<Service> serviceScoreComparator = new Comparator<Service>() {
		public int compare(Service s1, Service s2) {
			return s2.getScore().compareTo(s1.getScore());
		}
	};
	
	public static boolean repair(PlanningGraph pg,
			Map<String, Service> serviceMap, Map<String, Concept> conceptMap,
			Map<String, Thing> thingMap, Map<String, Param> paramMap) {

		Set<Concept> subGoalSet = new HashSet<Concept>();
		Set<Service> candidates = new HashSet<Service>();
		
		/**
		 * compute subGoalSet
		 */
		subGoalSet = getSubGoalSet(pg);
		
		int currentLevel = pg.getPLevels().size() - 1;
		while(currentLevel > 0 & subGoalSet.size() != 0){
			
			Set<Service> aLevel = pg.getALevel(currentLevel);
			Set<Concept> pLevel = pg.getPLevel(currentLevel);
			List<Service> sortedCandidates = new LinkedList<Service>();
			Set<Concept> currentSubGoalSet = new HashSet<Concept>();
			
			do{
				/**
				 * 1. compute currentSubGoalSet which contains all broken preconditions 
				 * and unstatisfied goals that need to be statisfied 
				 * right in current PLevel (level n). 
				 * Initially, this set will contains all unstatisfied goals only.
				 */
				
				/**
				 * compute broken preconditions in current level
				 */
				if(currentLevel == pg.getPLevels().size()-1){
					/**
					 * initially currentSubGoalSet only contains broken goals
					 */
					currentSubGoalSet.clear();
					currentSubGoalSet.addAll(pg.getGoalSet());
					currentSubGoalSet.removeAll(pg.getPLevel(pg.getPLevels().size()-1));
				
				}else{
					
					/**
					 * compute currentSubGoalSet
					 */
					currentSubGoalSet.clear();
					for(Service s : pg.getALevel(currentLevel+1)){
						currentSubGoalSet.addAll(s.getInputConceptSet());
					}
					currentSubGoalSet.removeAll(pLevel);
				}
				
				/**
				 * skip to next level if no subgoals need to be satisfied for current level 
				 */
				if(currentSubGoalSet.size() == 0){
					break;
				}
					
				/**
				 * 2. Select all services that not already in current 
				 * ALevel and could produces at least one subgoal 
				 * in currentSubGoalSet as candidates. 
				 * compute their heuristic score based on heuristic function. 
				 * Sort from highest to lowest.
				 */
				
				/**
				 * compute candidate services (services not in current ALevel)
				 */
				candidates.clear();
				for(String key : serviceMap.keySet()){
					candidates.add(serviceMap.get(key));
				}
				candidates.removeAll(pg.getALevel(currentLevel));
				Set<Service> removableCandidates = new HashSet<Service>();
				for(Service s : candidates){
					Set<Concept> outputs = new HashSet<Concept>();
					outputs.addAll(s.getOutputConceptSet());
					outputs.retainAll(currentSubGoalSet);
					if(outputs.size() == 0){
						removableCandidates.add(s);
					}
				}
				candidates.removeAll(removableCandidates);
				
				/**
				 * compute heuristic score for each of candidate service
				 */
				sortedCandidates.clear();
				for(Service s : candidates){
					int score = RepairingEvaluator.evaluate(currentSubGoalSet, pg.getPLevel(currentLevel-1), s);
					s.setScore(score);
					sortedCandidates.add(s);
				}
				Collections.sort(sortedCandidates, serviceScoreComparator);
				
				/**
				 * 3. Insert first service in candidate list to current ALevel. 
				 * Add its outputs to PLevel(n). 
				 * Add unstatisfied inputs (if any) to PLevel(n-1). 
				 * Check if PLevel(n) still has unstatisfied subgoals. 
				 * if yes, repeat step 1 unless candidates list become empty 
				 * then return unrepairable.
				 */
				if(sortedCandidates.size() == 0){
					return false;
				}
				Service candidate = sortedCandidates.get(0);
				sortedCandidates.remove(0);
				aLevel.add(candidate);
				pLevel.addAll(candidate.getOutputConceptSet());
				pg.getPLevel(currentLevel-1).addAll(candidate.getInputConceptSet());
				System.out.println("CurrentSubGoalSet size: " + currentSubGoalSet.size());

				currentSubGoalSet.removeAll(pg.getPLevel(currentLevel));
				
				System.out.println("Added: " + candidate + "(" + candidate.getScore() + ")" + " at: " + currentLevel);
				System.out.println("sortedCandidates size: " + sortedCandidates.size());
				System.out.println("CurrentSubGoalSet size: " + currentSubGoalSet.size());
				
			}while(currentSubGoalSet.size() != 0 & sortedCandidates.size() > 0);
			
			/**
			 * if currentSubGoalSet cannot be satisfied, return unrepairable
			 */
			if(currentSubGoalSet.size() != 0){
				return false;
			}
			
			/**
			 * 4. compute subGoalSet which contains all broken preconditions 
			 * and unstatisfied goals based on current PG status, if empty return PG
			 */
			
			/**
			 * compute subGoalSet based on current PG status
			 */
			subGoalSet = getSubGoalSet(pg);
			if(subGoalSet.size() == 0){
				return true;
			}
			
			System.out.println("subGoalSet size: " + subGoalSet.size());
			/**
			 * 5. decrese level count by 1
			 */
			currentLevel--;
			
			/**
			 * 6. if level == 0, 
			 * insert a new PLevel which contains 
			 * only the given concepts to Level 0 
			 * (all current Plevel number increased by 1), 
			 * increse level count by 1.
			 */
			if(currentLevel == 0){
				pg.insertPLevel(0, new HashSet<Concept>());
				pg.getPLevel(0).addAll(pg.getGivenConceptSet());
				pg.insertALevel(0, new HashSet<Service>());
				currentLevel++;
			}
			
			
		}
		
		
		return true;
	}
	
	
	/**
	 * get subGoalSet which contains bp and unsatisfied goals base on given PG status
	 * @param pg
	 * @return
	 */
	private static Set<Concept> getSubGoalSet(PlanningGraph pg){
		
		Set<Concept> subGoalSet = new HashSet<Concept>();
		
		int currentLevel = 1;
		while(currentLevel < pg.getALevels().size()){
			for(Service s : pg.getALevel(currentLevel)){
				subGoalSet.addAll(s.getInputConceptSet());
			}
			subGoalSet.removeAll(pg.getPLevel(currentLevel-1));			
			currentLevel++;
		}
		
		subGoalSet.addAll(pg.getGoalSet());
		subGoalSet.removeAll(pg.getPLevel(pg.getPLevels().size()-1));
		
		
		return subGoalSet;
	}
}

package ca.concordia.pga.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import ca.concordia.pga.algorithm.utils.CombinationHelper;
import ca.concordia.pga.models.Concept;
import ca.concordia.pga.models.PlanningGraph;
import ca.concordia.pga.models.Service;

/**
 * 
 * @author Ludeng Zhao(Eric)
 *
 */
public class RefinementAlgorithm {
	/**
	 * compute removable service set from the given goal concept set
	 * 
	 * @param conceptSet
	 * @return
	 */
	private static Set<Service> getRemovableServiceSet(Set<Concept> conceptSet) {
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
	 * 
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

		if (removableServiceSet.size() == 0) {
			Set<Service> minimumServiceSet = new HashSet<Service>();
			for (Concept c : conceptSet) {
				minimumServiceSet.addAll(c.getOriginServiceSet());
			}
			routes.add(minimumServiceSet);
		}
		for (List<Service> combinationList : combinations) {

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
				concept.getOriginServiceSet().addAll(c.getOriginServiceSet());
				currStatus.add(concept);
			}
			/**
			 * remove removable services from the clone
			 */
			for (Concept c : currStatus) {
				c.getOriginServiceSet().removeAll(combination);
				// for(Service s : combinationList){
				// c.getOriginServiceSet().remove(s);
				// }
				if (c.getOriginServiceSet().size() == 0) {
					valid = false;
					break;
				}
			}
			if (getRemovableServiceSet(currStatus).size() == 0) {
				atom = true;
			}

			if (valid & atom) {
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
	 * 
	 * @param pg
	 * @return routeCouters
	 */
	public static Vector<Integer> refineSolution(PlanningGraph pg) {

		int currLevel = pg.getALevels().size() - 1;

		Set<Service> minimumServiceSet;
		Set<Concept> subGoalSet = new HashSet<Concept>();
		subGoalSet.addAll(pg.getGoalSet());
		
		Set<Concept> leftGoalSet = new HashSet<Concept>();
		Set<Set<Service>> routes = new HashSet<Set<Service>>();
		Vector<Integer> routeCounters = new Vector<Integer>(); // debug purpose,
																// will be
																// remove
		Map<Integer, Set<Service>> solutionMap = new HashMap<Integer, Set<Service>>();
		
		do {
			/**
			 * compute services that each concept is origin from
			 */
			Set<Service> actionSet = pg.getALevel(currLevel);
			subGoalSet.addAll(leftGoalSet);
			subGoalSet.removeAll(pg.getGivenConceptSet());
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
				if (g.getOriginServiceSet().size() == 0) {
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
			// computeRoutes_Old(subGoalSet, routes);
			computeRoutes(subGoalSet, routes);

			/**
			 * get the route with minimum web services to invoke
			 */
			minimumServiceSet = new HashSet<Service>();
			Iterator<Set<Service>> itr = routes.iterator();
			while (itr.hasNext()) {
				Set<Service> candidate = itr.next();
				if (minimumServiceSet.size() == 0) {
					minimumServiceSet = candidate;
				} else if (minimumServiceSet.size() > candidate.size()) {
					minimumServiceSet = candidate;
				}
			}

			/**
			 * overwrite selected route into pg's ALevel (temperate
			 * implementation! in order to keep pg's information)
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
		 * remove invalid concepts after pruning services
		 */
		int currentLevel = 1;
		do{
			Set<Concept> knownConceptSet = new HashSet<Concept>();
			knownConceptSet.addAll(pg.getPLevel(currentLevel-1));
			for(Service s : pg.getALevel(currentLevel)){
				knownConceptSet.addAll(s.getOutputConceptSet());
			}
			pg.getPLevel(currentLevel).clear();
			pg.getPLevel(currentLevel).addAll(knownConceptSet);
			currentLevel++;
		}while(currentLevel < pg.getALevels().size());
		
		/**
		 * debug checking if solution is valid
		 */
		if (leftGoalSet.size() != 0) {
			System.out.println("Solution is NOT VALID!");
		}
		
//		removeEmptyLevels(pg);
		
		return routeCounters; // temperate for debug, will be removed!
	}
	
	/**
	 * remove empty levels from given pg
	 * @param pg
	 */
	private static void removeEmptyLevels(PlanningGraph pg){
	
		/**
		 * remove empty levels
		 */
		int currentLevel = 1;			
		while(currentLevel < pg.getALevels().size()){
			if(pg.getALevel(currentLevel).size()==0){
				pg.removedLevel(currentLevel);
			}else{
				currentLevel++;
			}
		}
		

		
		
	}

}

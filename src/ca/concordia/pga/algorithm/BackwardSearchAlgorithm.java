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
 * Backward Search Algorithm for Extracting a Solution from the Given Planning Graph
 * @author Ludeng Zhao(Eric)
 *
 */
public class BackwardSearchAlgorithm {
	/**
	 * Compute removable service set from the given subgoal concept set.
	 * 
	 * @param conceptSet
	 * @return removable service set
	 */
	private static Set<Service> getRemovableServiceSet(Set<Concept> conceptSet) {
		Set<Service> removableServiceSet = new HashSet<Service>();
		Set<Service> unRemovableServiceSet = new HashSet<Service>();
		/**
		 * get removable service. For each of the subgoals, if it can be produced by
		 * more than one service, it is a removable service.
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
	 * Implementation for computing all alternative routes (hitting sets) that can produce
	 * given concepts
	 * 
	 * @param subGoalSet
	 * @param routes
	 */
	private static void computeRoutes(Set<Concept> subGoalSet,
			Set<Set<Service>> routes) {

		List<List<Service>> combinations;

		/**
		 * calculate removable service set
		 */
		Set<Service> removableServiceSet = getRemovableServiceSet(subGoalSet);

		/**
		 * calculate all combinations for the removable service set
		 */
		combinations = CombinationHelper.findCombinations(removableServiceSet);
		/**
		 * remove empty set
		 */
		combinations.remove(new ArrayList<Service>());

		/**
		 * if no services can be removed, smallest set is the set of services in current action level
		 */
		if (removableServiceSet.size() == 0) {
			Set<Service> route = new HashSet<Service>();
			for (Concept c : subGoalSet) {
				route.addAll(c.getOriginServiceSet());
			}
			routes.add(route);
		}else{

			/**
			 * calculate all routes
			 */
			for (List<Service> combinationList : combinations) {

				boolean valid = true; //each subgoal can still be provided by at least one service
				boolean nonRemovable = false; //no removable services existed

				Set<Service> combination = new HashSet<Service>();
				combination.addAll(combinationList);
				/**
				 * clone subGoalSet to currStatus
				 */
				Set<Concept> currStatus = new HashSet<Concept>();
				for (Concept c : subGoalSet) {
					Concept concept = new Concept(c.getName());
					concept.getOriginServiceSet().addAll(c.getOriginServiceSet());
					currStatus.add(concept);
				}
				/**
				 * remove removable services from the clone,
				 * test if it is still valid after the removal
				 */
				for (Concept c : currStatus) {
					c.getOriginServiceSet().removeAll(combination);
					if (c.getOriginServiceSet().size() == 0) {
						valid = false;
						break;
					}
				}
				/**
				 * calculate if it is removable
				 */
				if (getRemovableServiceSet(currStatus).size() == 0) {
					nonRemovable = true;
				}
				
				/**
				 * if both valid and nonRemovalble are true, 
				 * calculate the route and add it as a candidate
				 */
				if (valid & nonRemovable) {
					Set<Service> route = new HashSet<Service>();
					for (Concept c : currStatus) {
						route.addAll(c.getOriginServiceSet());
					}
					routes.add(route);
				}
			}
		}

	}

	/**
	 * Backward search for extracting a solution
	 * 
	 * @param pg
	 * @return routeCouters
	 */
	public static Vector<Integer> extractSolution(PlanningGraph pg) {

		int currLevel = pg.getALevels().size() - 1;

		Set<Service> smallestServiceSet;
		Set<Concept> subGoalSet = new HashSet<Concept>();
		subGoalSet.addAll(pg.getGoalSet());
		
		Set<Concept> deferredGoalSet = new HashSet<Concept>(); //subgoals not need to be satisfied at current level
		Set<Set<Service>> routes = new HashSet<Set<Service>>(); //all alternative routers (hitting sets) for current level
		Vector<Integer> routeCounters = new Vector<Integer>(); // debug purpose,

		Map<Integer, Set<Service>> solutionMap = new HashMap<Integer, Set<Service>>(); // record the route (smallest set) chosen at each level
		
		do {

			Set<Service> actionSet = pg.getALevel(currLevel); //create shortcut to current PG action level
			subGoalSet.addAll(deferredGoalSet); //put deferred subgoals into current subGoalSet
			deferredGoalSet.clear(); //empty the deferredGoalSet
			subGoalSet.removeAll(pg.getGivenConceptSet()); //removed subgoals that are initially known
			/**
			 * compute services from which each subgoal is originated 
			 */
			for (Concept g : subGoalSet) {
				for (Service s : actionSet) {
					if (s.getOutputConceptSet().contains(g)) {
						g.addServiceToOrigin(s);
					}
				}
				/**
				 * check if the goal can be produced by current action level
				 * if not, put it into deferredGoalSet
				 */
				if (g.getOriginServiceSet().size() == 0) {
					deferredGoalSet.add(g);
				}
			}
			/**
			 * defer the subgoals that cannot be produced in current action level
			 */
			subGoalSet.removeAll(deferredGoalSet);

			/**
			 * compute all alternative routes (hitting sets) that current level has
			 */
			computeRoutes(subGoalSet, routes);

			/**
			 * get the route with minimal web services as the smallestServiceSet
			 */
			smallestServiceSet = new HashSet<Service>();
			Iterator<Set<Service>> itr = routes.iterator();
			while (itr.hasNext()) {
				Set<Service> candidate = itr.next();
				if (smallestServiceSet.size() == 0) {
					smallestServiceSet = candidate;
				} else if (smallestServiceSet.size() > candidate.size()) {
					smallestServiceSet = candidate;
				}
			}

			/**
			 * replace current pg's ALevel with selected route into 
			 */
			pg.setALevel(currLevel, smallestServiceSet);
			/**
			 * write solution 
			 */
			solutionMap.put(currLevel, smallestServiceSet);

			/**
			 * get the inputs of newly added web services as subGoals
			 */
			subGoalSet.clear();
			for (Service s : smallestServiceSet) {
				subGoalSet.addAll(s.getInputConceptSet());
			}
			/**
			 * reset routes
			 */
			routeCounters.add(routes.size());
			routes.clear();
			
			/**
			 * decrease level counter
			 */
			currLevel--;

		} while (currLevel > 0);

		/**
		 * remove invalid concepts after pruning services
		 */
		pg.regeneratePLevels();
//		int currentLevel = 1;
//		do{
//			Set<Concept> knownConceptSet = new HashSet<Concept>();
//			knownConceptSet.addAll(pg.getPLevel(currentLevel-1));
//			for(Service s : pg.getALevel(currentLevel)){
//				knownConceptSet.addAll(s.getOutputConceptSet());
//			}
//			pg.getPLevel(currentLevel).clear();
//			pg.getPLevel(currentLevel).addAll(knownConceptSet);
//			currentLevel++;
//		}while(currentLevel < pg.getALevels().size());
		
		/**
		 * Debugging purpose, checking if solution is valid
		 */
		if (deferredGoalSet.size() != 0) {
			System.out.println("Solution is NOT VALID!");
		}
		
		
//		removeEmptyLevels(pg); //not happen in WSC dataset
		
		return routeCounters; 
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

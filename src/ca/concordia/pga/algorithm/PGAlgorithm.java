package ca.concordia.pga.algorithm;

import java.util.HashSet;
import java.util.Set;

import ca.concordia.pga.models.Concept;
import ca.concordia.pga.models.PlanningGraph;
import ca.concordia.pga.models.Service;

/**
 * Planning Algorithm for Composing a Planning Graph
 * @author Ludeng Zhao(Eric)
 *
 */
public class PGAlgorithm {

	public static boolean generatePG(Set<Concept> knownConceptSet,
			Set<Service> currInvokableServiceSet,
			Set<Service> currNonInvokableServiceSet,
			Set<Service> invokedServiceSet, PlanningGraph pg) {
		int currentLevel = 0;
		do {
			/**
			 * point knownConceptSet to pg's current PLevel
			 */
			knownConceptSet.addAll(pg.getPLevel(currentLevel));
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
			/**
			 * if there is no invokable services, return false
			 */
			if (currInvokableServiceSet.size() <= 0) {
				break;
			}
			/**
			 * generate ALevel
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
			 * increase the level 
			 */
			currentLevel++;
			
			/**
			 * print out newly added services
			 */
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

}

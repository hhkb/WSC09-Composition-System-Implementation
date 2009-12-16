package ca.concordia.pga.algorithm.utils;

import java.util.HashSet;
import java.util.Set;

import ca.concordia.pga.models.Concept;
import ca.concordia.pga.models.Service;

public class RepairingEvaluator {

	/**
	 * Calculate the repairing heuristic score for one action
	 * @param g
	 * @param p
	 * @param a
	 * @return repairing heuristic score
	 */
	public static int evaluate(Set<Concept> g, Set<Concept> p, Service a){
		
		int score = 0;
		Set<Concept> t = new HashSet<Concept>();

		/**
		 * calculate g join aOut
		 */
		t.addAll(g);
		t.retainAll(a.getOutputConceptSet());
		score += t.size() * 10;
		
		/**
		 * calculate p join aIn
		 */
		t.clear();
		t.addAll(p);
		t.retainAll(a.getInputConceptSet());
//		score += t.size();
		
		/**
		 * calculate aIn not in p
		 */
		t.clear();
		t.addAll(a.getInputConceptSet());
		t.removeAll(p);
		score -= t.size();
		
		return score;
	}
}

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


	public static boolean repairICWS(PlanningGraph pg,
			Map<String, Service> serviceMap, Map<String, Concept> conceptMap,
			Map<String, Thing> thingMap, Map<String, Param> paramMap) {

		Set<Concept> subGoalSet = new HashSet<Concept>();
//		Set<Service> candidates = new HashSet<Service>();
		
		/**
		 * remove empty levels
		 */
		removeEmptyLevels(pg);

		/**
		 * compute subGoalSet
		 */
		subGoalSet = getSubGoalSet(pg);

		int currentLevel = pg.getPLevels().size() - 1;
		while (currentLevel > 0 & subGoalSet.size() != 0) {
			
			Set<Service> aLevel = pg.getALevel(currentLevel);
			Set<Concept> pLevel = pg.getPLevel(currentLevel);
			List<Service> sortedCandidates = new LinkedList<Service>();
			Set<Concept> currentSubGoalSet = new HashSet<Concept>();
			
			do {
				/**
				 * 1. compute currentSubGoalSet which contains all broken
				 * preconditions and unsatisfied goals that need to be
				 * satisfied right in current PLevel (level n). Initially, this
				 * set will contains all unsatisfied goals only.
				 */

				/**
				 * compute broken preconditions in current level
				 */
				if (currentLevel == pg.getPLevels().size() - 1) {
					/**
					 * initially currentSubGoalSet only contains broken goals
					 */
					currentSubGoalSet.clear();
					currentSubGoalSet.addAll(pg.getGoalSet());
					currentSubGoalSet.removeAll(pg.getPLevel(pg.getPLevels()
							.size() - 1));

				} else {

					/**
					 * compute currentSubGoalSet
					 */
					currentSubGoalSet.clear();
					for (Service s : pg.getALevel(currentLevel + 1)) {
						currentSubGoalSet.addAll(s.getInputConceptSet());
					}
					currentSubGoalSet.removeAll(pLevel);
				}

				/**
				 * skip to next level if no subgoals need to be satisfied for
				 * current level
				 */
				if (currentSubGoalSet.size() == 0) {
					break;
				}

				/**
				 * 2. Select all services that not already in current ALevel and
				 * could produces at least one subgoal in currentSubGoalSet as
				 * candidates. compute their heuristic score based on heuristic
				 * function. Sort from highest to lowest.
				 */

				sortedCandidates = getCandiateSet(pg, serviceMap, currentLevel,
						currentSubGoalSet);

				/**
				 * 3. Insert first service in candidate list to current ALevel.
				 * Add its outputs to PLevel(n). Check if PLevel(n) still has
				 * unsatisfied subgoals. if yes, repeat step 1 unless candidates
				 * list become empty then return not reparable.
				 */
				if (sortedCandidates.size() == 0) {
					return false;
				}
				Service candidate = sortedCandidates.get(0);
				sortedCandidates.remove(0);

				/**
				 * if precondition of newly insert service is not satisfied by
				 * P(n-1), insert a new level
				 */
				if (!pg.getPLevel(currentLevel - 1).containsAll(
						candidate.getInputConceptSet())) {
					int newLevel = createAndInsertLevel(pg, currentLevel);
					currentLevel = newLevel;
					currentSubGoalSet.clear();
					System.out.println("*** Create and insert new level at: "
							+ newLevel);
					System.out.println("Jump In");

					do {
						/**
						 * compute currentSubGoalSet
						 */
						currentSubGoalSet.clear();
						if(currentLevel == pg.getALevels().size()-1){
							currentSubGoalSet.addAll(pg.getGoalSet());
						}else{
							for (Service s : pg.getALevel(currentLevel + 1)) {
								currentSubGoalSet.addAll(s.getInputConceptSet());
							}
						}

						currentSubGoalSet.removeAll(pg.getPLevel(currentLevel));

						sortedCandidates = getCandiateSet(pg, serviceMap,
								currentLevel, currentSubGoalSet);

						if (sortedCandidates.size() == 0) {
							return false;
						}
						Service newLevelCandidate = sortedCandidates.get(0);
						sortedCandidates.remove(0);

						pg.getALevel(currentLevel).add(newLevelCandidate);
						pg.getPLevel(currentLevel).addAll(
								newLevelCandidate.getOutputConceptSet());

						System.out.println("CurrentSubGoalSet size: "
								+ currentSubGoalSet.size());

						currentSubGoalSet.removeAll(pg.getPLevel(currentLevel));

						System.out.println("Added: " + newLevelCandidate + "("
								+ newLevelCandidate.getScore() + ")" + " at: "
								+ currentLevel);
						System.out.println("sortedCandidates size: "
								+ sortedCandidates.size());
					} while (currentSubGoalSet.size() > 0);

					System.out.println("Jump Out");

					break;
				} else {
					aLevel.add(candidate);
					pLevel.addAll(candidate.getOutputConceptSet());
					// pg.getPLevel(currentLevel-1).addAll(candidate.getInputConceptSet());
					System.out.println("CurrentSubGoalSet size: "
							+ currentSubGoalSet.size());

					currentSubGoalSet.removeAll(pg.getPLevel(currentLevel));

					System.out.println("Added: " + candidate + "("
							+ candidate.getScore() + ")" + " at: "
							+ currentLevel);
					System.out.println("sortedCandidates size: "
							+ sortedCandidates.size());

				}

			} while (currentSubGoalSet.size() != 0
					& sortedCandidates.size() > 0);

			/**
			 * if currentSubGoalSet cannot be satisfied, return non-reparable
			 */
			if (currentSubGoalSet.size() != 0) {
				return false;
			}

			/**
			 * 4. compute subGoalSet which contains all broken preconditions and
			 * unsatisfied goals based on current PG status, if empty return PG
			 */

			/**
			 * compute subGoalSet based on current PG status
			 */
			subGoalSet = getSubGoalSet(pg);
			if (subGoalSet.size() == 0) {
				
				/**
				 * Upon success: Start from level 1 while level < levels.size() 1.
				 * select each service in current ALevel, removed all its duplicate in
				 * higher ALevel. 2. Increase level count by 1
				 */

				currentLevel = 1;
				while (currentLevel < pg.getALevels().size()) {
					for (Service s : pg.getALevel(currentLevel)) {
						int higherLevel = currentLevel + 1;
						while (higherLevel < pg.getALevels().size()) {
							Set<Service> duplicates = new HashSet<Service>();
							for (Service hs : pg.getALevel(higherLevel)) {
								if (s.equals(hs)) {
									duplicates.add(hs);
								}
							}
							if (pg.getALevel(higherLevel).removeAll(duplicates)) {
								System.out.println("duplicates removed!");
							}
							higherLevel++;
						}
					}
					currentLevel++;
				}
				
				return true;
			}

			System.out.println("subGoalSet size: " + subGoalSet.size());
			/**
			 * 5. decrease level count by 1
			 */
			currentLevel--;


		}


		return true;
	}

	public static boolean repairForward(PlanningGraph pg,
			Map<String, Service> serviceMap, Map<String, Concept> conceptMap,
			Map<String, Thing> thingMap, Map<String, Param> paramMap) {

		Set<Concept> subGoalSet = new HashSet<Concept>();
		Set<Service> candidates = new HashSet<Service>();

		/**
		 * compute subGoalSet
		 */
		subGoalSet = getSubGoalSet(pg);

		int currentLevel = 1;
		while (subGoalSet.size() != 0) {

			Set<Service> aLevel = pg.getALevel(currentLevel);
			Set<Concept> pLevel = pg.getPLevel(currentLevel);
			List<Service> sortedCandidates = new LinkedList<Service>();
			Set<Concept> currentSubGoalSet = new HashSet<Concept>();

			System.out.println("\ncurrent level " + currentLevel);
			do {
				/**
				 * 1. compute currentSubGoalSet and subGoalSet
				 */

				/**
				 * compute broken preconditions in current level
				 */
				if (currentLevel == pg.getPLevels().size() - 1) {
					/**
					 * if last level, currentSubGoalSet only contains broken
					 * goals
					 */
					currentSubGoalSet.clear();
					currentSubGoalSet.addAll(pg.getGoalSet());
					currentSubGoalSet.removeAll(pg.getPLevel(pg.getPLevels()
							.size() - 1));

				} else {

					/**
					 * compute currentSubGoalSet
					 */
					currentSubGoalSet.clear();
					for (Service s : pg.getALevel(currentLevel + 1)) {
						currentSubGoalSet.addAll(s.getInputConceptSet());
					}
					currentSubGoalSet.removeAll(pLevel);
				}

				/**
				 * skip to next level if no subgoals need to be satisfied for
				 * current level
				 */
				// if(currentSubGoalSet.size() == 0){
				// break;
				// }

				/**
				 * 2. Select all services that not already in current ALevel and
				 * could produces at least one subgoal in subGoalSet as
				 * candidates. compute their heuristic score based on heuristic
				 * function. Sort from highest to lowest.
				 */

				/**
				 * compute candidate services (services not in current ALevel)
				 */
				candidates.clear();
				// for(String key : serviceMap.keySet()){
				// candidates.add(serviceMap.get(key));
				// }

				for (Concept c : pg.getPLevel(currentLevel - 1)) {
					candidates.addAll(c.getServicesIndex());
				}
				candidates.removeAll(pg.getALevel(currentLevel));
				Set<Service> removableCandidates = new HashSet<Service>();

				for (Service s : candidates) {
					Set<Concept> outputs = new HashSet<Concept>();
					outputs.addAll(s.getOutputConceptSet());
					outputs.retainAll(subGoalSet);
					if (outputs.size() == 0) {
						removableCandidates.add(s);
					} else {
						for (Concept c : outputs) {
							System.out.println(s + "satisfied " + c);

						}
					}
				}
				candidates.removeAll(removableCandidates);
				System.out.println("candiates size: " + candidates.size());

				/**
				 * compute heuristic score for each of candidate service
				 */
				sortedCandidates.clear();
				for (Service s : candidates) {
					int score = RepairingEvaluator.evaluateForward(subGoalSet,
							pg.getPLevel(currentLevel), s);
					// int score =
					// RepairingEvaluator.evaluate(currentSubGoalSet,
					// pg.getPLevel(currentLevel-1), s);
					s.setScore(score);
					sortedCandidates.add(s);
				}
				Collections.sort(sortedCandidates, serviceScoreComparator);

				/**
				 * 3. Insert first service in candidate list to current ALevel.
				 * Add its outputs to PLevel(n). Check if PLevel(n) still has
				 * unstatisfied subgoals. if yes, repeat step 1 unless
				 * candidates list become empty then return unrepairable.
				 */

				if (sortedCandidates.size() == 0 & currentSubGoalSet.size() > 0) {
					return false;
				} else if (sortedCandidates.size() == 0
						& currentSubGoalSet.size() == 0) {
					System.out.println("skip current level");
					break;
				}

				Service candidate = sortedCandidates.get(0);
				sortedCandidates.remove(0);
				aLevel.add(candidate);
				addCandidateOuputs(pg, currentLevel, candidate);
				// pLevel.addAll(candidate.getOutputConceptSet());
				// pg.getPLevel(currentLevel-1).addAll(candidate.getInputConceptSet());
				System.out.println("CurrentSubGoalSet size: "
						+ currentSubGoalSet.size());
				System.out.println("SubGoalSet size(before): "
						+ subGoalSet.size());
				subGoalSet = getSubGoalSet(pg);

				currentSubGoalSet.removeAll(pg.getPLevel(currentLevel));

				System.out.println("Added: " + candidate + "("
						+ candidate.getScore() + ")" + " at: " + currentLevel);
				System.out.println("SubGoalSet size(after): "
						+ subGoalSet.size());

				// System.out.println("CurrentSubGoalSet size: " +
				// currentSubGoalSet.size());

			} while (true);

			/**
			 * if currentSubGoalSet cannot be satisfied, return unrepairable
			 */
			if (currentSubGoalSet.size() != 0) {
				return false;
			}

			/**
			 * 4. compute subGoalSet which contains all broken preconditions and
			 * unsatisfied goals based on current PG status, if empty return PG
			 */

			/**
			 * compute subGoalSet based on current PG status
			 */
			subGoalSet = getSubGoalSet(pg);
			if (subGoalSet.size() == 0) {
				return true;
			}

			System.out.println("subGoalSet size: " + subGoalSet.size());
			/**
			 * 5. increase level count by 1
			 */
			currentLevel++;

			/**
			 * 6. if level = size-1, append a new PLevel which is contains all
			 * concepts in last PLevel to the PG.
			 */
			if (currentLevel == pg.getALevels().size() - 1) {
				Set<Concept> newPLevel = new HashSet<Concept>();
				newPLevel.addAll(pg.getPLevel(currentLevel));
				pg.getPLevels().add(newPLevel);
				pg.getALevels().add(new HashSet<Service>());
			}

		}

		/**
		 * Upon success: Start from level 1 while level < levels.size() 1.
		 * select each service in current ALevel, removed all its duplicate in
		 * higher ALevel. 2. increse level count by 1
		 */

		currentLevel = 1;
		while (currentLevel < pg.getALevels().size()) {
			for (Service s : pg.getALevel(currentLevel)) {
				int higherLevel = currentLevel + 1;
				while (higherLevel < pg.getALevels().size()) {
					Set<Service> duplicates = new HashSet<Service>();
					for (Service hs : pg.getALevel(higherLevel)) {
						if (s.equals(hs)) {
							duplicates.add(hs);
						}
					}
					if (pg.getALevel(higherLevel).removeAll(duplicates)) {
						System.out.println("duplicates removed!");
					}
					higherLevel++;
				}
			}
			currentLevel++;
		}
		return true;
	}

	/**
	 * repair given PG using backward approach
	 * 
	 * @param pg
	 * @param serviceMap
	 * @param conceptMap
	 * @param thingMap
	 * @param paramMap
	 * @return
	 */
	public static boolean repairSERA(PlanningGraph pg,
			Map<String, Service> serviceMap, Map<String, Concept> conceptMap,
			Map<String, Thing> thingMap, Map<String, Param> paramMap) {

		Set<Concept> subGoalSet = new HashSet<Concept>();
		Set<Service> candidates = new HashSet<Service>();

		/**
		 * compute subGoalSet
		 */
		subGoalSet = getSubGoalSet(pg);

		int currentLevel = pg.getPLevels().size() - 1;
		while (currentLevel > 0 & subGoalSet.size() != 0) {

			Set<Service> aLevel = pg.getALevel(currentLevel);
			Set<Concept> pLevel = pg.getPLevel(currentLevel);
			List<Service> sortedCandidates = new LinkedList<Service>();
			Set<Concept> currentSubGoalSet = new HashSet<Concept>();

			do {
				/**
				 * 1. compute currentSubGoalSet which contains all broken
				 * preconditions and unsatisfied goals that need to be
				 * satisfied right in current PLevel (level n). Initially, this
				 * set will contains all unsatisfied goals only.
				 */

				/**
				 * compute broken preconditions in current level
				 */
				if (currentLevel == pg.getPLevels().size() - 1) {
					/**
					 * initially currentSubGoalSet only contains broken goals
					 */
					currentSubGoalSet.clear();
					currentSubGoalSet.addAll(pg.getGoalSet());
					currentSubGoalSet.removeAll(pg.getPLevel(pg.getPLevels()
							.size() - 1));

				} else {

					/**
					 * compute currentSubGoalSet
					 */
					currentSubGoalSet.clear();
					for (Service s : pg.getALevel(currentLevel + 1)) {
						currentSubGoalSet.addAll(s.getInputConceptSet());
					}
					currentSubGoalSet.removeAll(pLevel);
				}

				/**
				 * skip to next level if no subgoals need to be satisfied for
				 * current level
				 */
				if (currentSubGoalSet.size() == 0) {
					break;
				}

				/**
				 * 2. Select all services that not already in current ALevel and
				 * could produces at least one subgoal in currentSubGoalSet as
				 * candidates. compute their heuristic score based on heuristic
				 * function. Sort from highest to lowest.
				 */

				/**
				 * compute candidate services (services not in current ALevel)
				 */
				candidates.clear();
				for (String key : serviceMap.keySet()) {
					candidates.add(serviceMap.get(key));
				}
				candidates.removeAll(pg.getALevel(currentLevel));
				Set<Service> removableCandidates = new HashSet<Service>();
				for (Service s : candidates) {
					Set<Concept> outputs = new HashSet<Concept>();
					outputs.addAll(s.getOutputConceptSet());
					outputs.retainAll(currentSubGoalSet);
					if (outputs.size() == 0) {
						removableCandidates.add(s);
					}
				}
				candidates.removeAll(removableCandidates);

				/**
				 * compute heuristic score for each of candidate service
				 */
				sortedCandidates.clear();
				for (Service s : candidates) {
					int score = RepairingEvaluator.evaluate(currentSubGoalSet,
							pg.getPLevel(currentLevel - 1), s);
					s.setScore(score);
					sortedCandidates.add(s);
				}
				Collections.sort(sortedCandidates, serviceScoreComparator);

				/**
				 * 3. Insert first service in candidate list to current ALevel.
				 * Add its outputs to PLevel(n). Check if PLevel(n) still has
				 * unsatisfied subgoals. if yes, repeat step 1 unless
				 * candidates list become empty then return non-reparable.
				 */
				if (sortedCandidates.size() == 0) {
					return false;
				}
				Service candidate = sortedCandidates.get(0);
				sortedCandidates.remove(0);
				aLevel.add(candidate);
				pLevel.addAll(candidate.getOutputConceptSet());
				// pg.getPLevel(currentLevel-1).addAll(candidate.getInputConceptSet());
				System.out.println("CurrentSubGoalSet size: "
						+ currentSubGoalSet.size());

				currentSubGoalSet.removeAll(pg.getPLevel(currentLevel));

				System.out.println("Added: " + candidate + "("
						+ candidate.getScore() + ")" + " at: " + currentLevel);
				System.out.println("sortedCandidates size: "
						+ sortedCandidates.size());

			} while (currentSubGoalSet.size() != 0
					& sortedCandidates.size() > 0);

			/**
			 * if currentSubGoalSet cannot be satisfied, return non-repairable
			 */
			if (currentSubGoalSet.size() != 0) {
				return false;
			}

			/**
			 * 4. compute subGoalSet which contains all broken preconditions and
			 * unsatisfied goals based on current PG status, if empty return PG
			 */

			/**
			 * compute subGoalSet based on current PG status
			 */
			subGoalSet = getSubGoalSet(pg);
			if (subGoalSet.size() == 0) {
				return true;
			}

			System.out.println("subGoalSet size: " + subGoalSet.size());
			/**
			 * 5. decrease level count by 1
			 */
			currentLevel--;

			/**
			 * 6. if level == 0, insert a new PLevel which contains only the
			 * given concepts to Level 0 (all current PLevel number increased by
			 * 1), increase level count by 1.
			 */
			if (currentLevel == 0) {
				pg.insertPLevel(0, new HashSet<Concept>());
				pg.getPLevel(0).addAll(pg.getGivenConceptSet());
				pg.insertALevel(0, new HashSet<Service>());
				currentLevel++;
			}

		}

		/**
		 * Upon success: Start from level 1 while level < levels.size() 1.
		 * select each service in current ALevel, removed all its duplicate in
		 * higher ALevel. 2. Increase level count by 1
		 */

		currentLevel = 1;
		while (currentLevel < pg.getALevels().size()) {
			for (Service s : pg.getALevel(currentLevel)) {
				int higherLevel = currentLevel + 1;
				while (higherLevel < pg.getALevels().size()) {
					Set<Service> duplicates = new HashSet<Service>();
					for (Service hs : pg.getALevel(higherLevel)) {
						if (s.equals(hs)) {
							duplicates.add(hs);
						}
					}
					if (pg.getALevel(higherLevel).removeAll(duplicates)) {
						System.out.println("duplicates removed!");
					}
					higherLevel++;
				}
			}
			currentLevel++;
		}

		return true;
	}

	/**
	 * used in forward repairing. Add the outputs of a web service to current
	 * PLevel as well as all PLevel above it
	 * 
	 * @param pg
	 * @param currentLevel
	 * @param candidate
	 */
	private static void addCandidateOuputs(PlanningGraph pg, int currentLevel,
			Service candidate) {
		while (currentLevel < pg.getALevels().size()) {
			pg.getPLevel(currentLevel).addAll(candidate.getOutputConceptSet());
			currentLevel++;
		}
	}

	/**
	 * create and insert a new level after the given level
	 * 
	 * @param pg
	 * @param currentLevel
	 * @return
	 */
	private static int createAndInsertLevel(PlanningGraph pg, int currentLevel) {

		int insertedLevel = currentLevel;

		// Set<Service> currentALevel = pg.getALevel(currentLevel);
		Set<Concept> currentPLevel = pg.getPLevel(currentLevel);
		Set<Service> newALevel = new HashSet<Service>();
		Set<Concept> newPLevel = new HashSet<Concept>();

		newPLevel.addAll(currentPLevel);

		if (currentLevel == pg.getALevels().size()) {
			pg.getALevels().add(newALevel);
			pg.getPLevels().add(newPLevel);
		} else {
			pg.getALevels().add(currentLevel + 1, newALevel);
			pg.getPLevels().add(currentLevel + 1, newPLevel);
		}

		return insertedLevel + 1;
	}

	/**
	 * get subGoalSet which contains bp and unsatisfied goals base on given PG
	 * status
	 * 
	 * @param pg
	 * @return
	 */
	private static Set<Concept> getSubGoalSet(PlanningGraph pg) {

		Set<Concept> subGoalSet = new HashSet<Concept>();
		Set<Concept> tempSet = new HashSet<Concept>();
		
		int currentLevel = 1;
		while (currentLevel < pg.getALevels().size()) {
			tempSet.clear();
			for (Service s : pg.getALevel(currentLevel)) {
				tempSet.addAll(s.getInputConceptSet());
			}
			tempSet.removeAll(pg.getPLevel(currentLevel - 1));
			subGoalSet.addAll(tempSet);
			currentLevel++;
		}

		subGoalSet.addAll(pg.getGoalSet());
		subGoalSet.removeAll(pg.getPLevel(pg.getPLevels().size() - 1));

		return subGoalSet;
	}

	private static List<Service> getCandiateSet(PlanningGraph pg,
			Map<String, Service> serviceMap, int currentLevel,
			Set<Concept> currentSubGoalSet) {

		Set<Service> candidates = new HashSet<Service>();
		List<Service> sortedCandidates = new LinkedList<Service>();
		/**
		 * compute candidate services (services not in current ALevel)
		 */
		candidates.clear();
		for (String key : serviceMap.keySet()) {
			candidates.add(serviceMap.get(key));
		}
		candidates.removeAll(pg.getALevel(currentLevel));
		Set<Service> removableCandidates = new HashSet<Service>();
		for (Service s : candidates) {
			Set<Concept> outputs = new HashSet<Concept>();
			outputs.addAll(s.getOutputConceptSet());
			outputs.retainAll(currentSubGoalSet);
			if (outputs.size() == 0) {
				removableCandidates.add(s);
			}
		}
		candidates.removeAll(removableCandidates);

		/**
		 * compute heuristic score for each of candidate service
		 */
		sortedCandidates.clear();
		for (Service s : candidates) {
			int score = RepairingEvaluator.evaluate(currentSubGoalSet, pg
					.getPLevel(currentLevel - 1), s);
			s.setScore(score);
			sortedCandidates.add(s);
		}
		Collections.sort(sortedCandidates, serviceScoreComparator);

		return sortedCandidates;
	}


	/**
	 * remove empty levels from the given PG
	 * @param pg
	 */
	private static void removeEmptyLevels(PlanningGraph pg){
	
		/**
		 * remove empty levels
		 */
		int currentLevel = 2;			
		while(currentLevel < pg.getALevels().size()){
			if(pg.getALevel(currentLevel).size()==0){
				pg.removedLevel(currentLevel);
			}else{
				currentLevel++;
			}
		}
		
		/**
		 * if all pg's action levels have been removed
		 */
		if(pg.getALevels().size()==1){
			Set<Service> newALevel = new HashSet<Service>();
			Set<Concept> newPLevel = new HashSet<Concept>();
			newPLevel.addAll(pg.getPLevel(0));
			pg.getALevels().add(newALevel);
			pg.getPLevels().add(newPLevel);
		}
		
		/**
		 * re-generate pLevels
		 */
		Set<Concept> knownConcepts = new HashSet<Concept>();
		knownConcepts.addAll(pg.getPLevel(0));
		currentLevel = 1;
		while(currentLevel < pg.getPLevels().size()){
			for(Service s : pg.getALevel(currentLevel)){
				knownConcepts.addAll(s.getOutputConceptSet());
			}
			pg.getPLevel(currentLevel).clear();
			pg.getPLevel(currentLevel).addAll(knownConcepts);
			currentLevel++;
		}
		
		
	}
	
	
}

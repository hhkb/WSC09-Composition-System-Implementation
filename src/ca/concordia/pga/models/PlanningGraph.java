package ca.concordia.pga.models;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * The class represents a planning graph
 * 
 * @author Ludeng Zhao(Eric)
 * 
 */
public class PlanningGraph {

	private Vector<Set<Concept>> PLevels;
	private Vector<Set<Service>> ALevels;
	private Set<Concept> goalSet;
	private Set<Concept> givenConceptSet;

	public PlanningGraph() {
		PLevels = new Vector<Set<Concept>>();
		ALevels = new Vector<Set<Service>>();
		goalSet = new HashSet<Concept>();
		givenConceptSet = new HashSet<Concept>();
	}

	public Vector<Set<Concept>> getPLevels() {
		return PLevels;
	}

	public Vector<Set<Service>> getALevels() {
		return ALevels;
	}

	public Set<Concept> getPLevel(int index) {
		return this.PLevels.get(index);
	}

	public Set<Service> getALevel(int index) {
		return this.ALevels.get(index);
	}
	
	public void setALevel(int index, Set<Service> ALevel){
		this.ALevels.set(index, ALevel);
	}

	public void addPLevel(Set<Concept> level) {
		this.PLevels.add(level);
	}

	public void addALevel(Set<Service> level) {
		this.ALevels.add(level);
	}

	public void removeLastPLevel() {
		int size = this.PLevels.size();
		if (size > 0) {
			this.PLevels.remove(size - 1);
		}
	}

	public void removeLastALevel() {
		int size = this.ALevels.size();
		if (size > 0) {
			this.ALevels.remove(size - 1);
		}
	}

	public Set<Concept> getGoalSet() {
		return goalSet;
	}

	public void setGoalSet(Set<Concept> goalSet) {
		this.goalSet = goalSet;
	}
	
	public Set<Concept> getGivenConceptSet() {
		return givenConceptSet;
	}

	public void setGivenConceptSet(Set<Concept> givenConceptSet) {
		this.givenConceptSet = givenConceptSet;
	}

	public void insertALevel(int level, Set<Service> aLevel){
		this.ALevels.add(level, aLevel);
	}
	
	public void insertPLevel(int level, Set<Concept> pLevel){
		this.PLevels.add(level, pLevel);
	}
	
	@Override
	public PlanningGraph clone(){
		PlanningGraph pg = new PlanningGraph();
		for(Set<Service> aLevel : this.getALevels()){
			Set<Service> newALevel = new HashSet<Service>();
			newALevel.addAll(aLevel);
			pg.addALevel(newALevel);
		}
		for(Set<Concept> pLevel : this.getPLevels()){
			Set<Concept> newPLevel = new HashSet<Concept>();
			newPLevel.addAll(pLevel);
			pg.addPLevel(newPLevel);
		}
		pg.getGoalSet().addAll(this.getGoalSet());
		pg.getGivenConceptSet().addAll(this.getGivenConceptSet());
		
		return pg;
	}
	
	public void removedLevel(int level){
		this.ALevels.remove(level);
		this.PLevels.remove(level);
	}
	
	/**
	 * 
	 * @return all services in the PG
	 */
	public Set<Service> getAllServices(){
		Set<Service> services = new HashSet<Service>();
		for(Set<Service> aLevel : this.getALevels()){
			services.addAll(aLevel);
		}
		return services;
	}

	/**
	 * 
	 * @return all services in the PG as well as their backups
	 */
	public Set<Service> getAllServicesAndTheirBackups(){
		Set<Service> services = new HashSet<Service>();
		for(Set<Service> aLevel : this.getALevels()){
			services.addAll(aLevel);
			for(Service s : aLevel){
				services.addAll(s.getBackupServiceSet());
			}
		}
		return services;	
		
	}
	
	/**
	 * regenerate all pLevel according to current action level status
	 */
	public void regeneratePLevels(){
		Set<Concept> knownConcepts = new HashSet<Concept>();
		knownConcepts.addAll(this.getPLevel(0));
		
		for(int i=1; i < this.getALevels().size(); i++){
			for(Service s : this.getALevel(i)){
				knownConcepts.addAll(s.getOutputConceptSet());
			}
			this.getPLevel(i).clear();
			this.getPLevel(i).addAll(knownConcepts);
		}
	}
	
	/**
	 * replace PG service by given service
	 * @param original
	 * @param replacement
	 * @return true if replaced, otherwise false
	 */
	public boolean replaceService(Service original, Service replacement){
		
		for(Set<Service> aLevel : this.getALevels()){
			if(aLevel.contains(original)){
				aLevel.add(replacement);
				aLevel.remove(original);
				this.regeneratePLevels();
				return true;
			}
		}
		
		return false;
	}
}

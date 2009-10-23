package ca.concordia.pga.models;

import java.util.Set;
import java.util.Vector;
/**
 * The class represents a planning graph
 * @author Ludeng Zhao(Eric)
 *
 */
public class PlanningGraph {
	
	private Vector<Set<Concept>> PLevels;
	private Vector<Set<Service>> ALevels;
	private Set<Concept> goalSet;
	
	public PlanningGraph(){
		PLevels = new Vector<Set<Concept>>();
		ALevels = new Vector<Set<Service>>();
	}

	public Set<Concept> getPLevel(int index){
		return this.PLevels.get(index);
	}
	
	public Set<Service> getALevel(int index){
		return this.ALevels.get(index);
	}
	
	public void addPLevel(Set<Concept> level){
		this.PLevels.add(level);
	}
	
	public void addALevel(Set<Service> level){
		this.ALevels.add(level);
	}
	
	public void removeLastPLevel(){
		int size = this.PLevels.size();
		if(size > 0){
			this.PLevels.remove(size - 1);	
		}
	}
	
	public void removeLastALevel(){
		int size = this.ALevels.size();
		if(size > 0){
			this.ALevels.remove(size - 1);	
		}
	}

	public Set<Concept> getGoalSet() {
		return goalSet;
	}

	public void setGoalSet(Set<Concept> goalSet) {
		this.goalSet = goalSet;
	}
	
	

}

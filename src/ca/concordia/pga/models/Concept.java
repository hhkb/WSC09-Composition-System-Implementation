package ca.concordia.pga.models;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents semantic concept in taxonomy document
 * 
 * @author Ludeng Zhao(Eric)
 * 
 */
public class Concept extends UniNameObject {

	private boolean root;
	private String directParantName;
	private Set<Service> servicesIndex;
	private Set<Service> originServiceSet;
	private Set<Concept> parentConceptsIndex;
	private Set<Concept> childrenConceptsIndex;
	private Set<Service> producedByServices;
	private Set<Service> usedByServices;
	private boolean rin;
	
	public Concept(String name) {
		super(name);
		this.root = false;
		this.servicesIndex = new HashSet<Service>();
		this.originServiceSet = new HashSet<Service>();
		this.parentConceptsIndex = new HashSet<Concept>();
		this.childrenConceptsIndex = new HashSet<Concept>();
		this.producedByServices = new HashSet<Service>();
		this.usedByServices = new HashSet<Service>();
		this.rin = false;
	}

	public String getDirectParantName() {
		return directParantName;
	}

	public void setDirectParantName(String directParantName) {
		this.directParantName = directParantName;
	}

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

	public Set<Service> getServicesIndex() {
		return servicesIndex;
	}

	public void addServiceToIndex(Service service) {
		this.servicesIndex.add(service);
	}

	public void addConceptToParentIndex(Concept concept) {
		this.parentConceptsIndex.add(concept);
	}

	public void removeConceptFromParentIndex(Concept concept) {
		this.parentConceptsIndex.remove(concept);
	}

	public void addConceptToChildrenIndex(Concept concept) {
		this.childrenConceptsIndex.add(concept);
	}

	public void removeConceptFromChildrenIndex(Concept concept) {
		this.childrenConceptsIndex.remove(concept);
	}

	public Set<Concept> getParentConceptsIndex() {
		return parentConceptsIndex;
	}

	public Set<Concept> getChildrenConceptsIndex() {
		return childrenConceptsIndex;
	}

	public void addServiceToOrigin(Service service){
		this.originServiceSet.add(service);
	}
	
	public Set<Service> getOriginServiceSet() {
		return originServiceSet;
	}

	public void resetServiceIndex(){
		this.servicesIndex = new HashSet<Service>();
		this.originServiceSet = new HashSet<Service>();
	}

	public void addProducedByService(Service service){
		this.producedByServices.add(service);
	}
	
	public Set<Service> getProducedByServices() {
		return producedByServices;
	}

	public void addUsedByService(Service service){
		this.usedByServices.add(service);
	}
	
	public Set<Service> getUsedByServices() {
		return usedByServices;
	}

	public boolean isRin() {
		return rin;
	}

	public void setRin(boolean rin) {
		this.rin = rin;
	}
	
	
	
}

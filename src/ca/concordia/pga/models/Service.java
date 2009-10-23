package ca.concordia.pga.models;

import java.util.HashSet;
import java.util.Set;
/**
 * The class represents a web service
 * @author Ludeng Zhao(Eric)
 *
 */
public class Service extends UniNameObject {

	private Set<Param> inputParamSet;
	private Set<Param> outputParamSet;
	private Set<Concept> inputConceptSet;
	private Set<Concept> outputConceptSet;
	private Set<Service> equivalentServiceSet;

	public Service(String name) {
		super(name);
		inputParamSet = new HashSet<Param>();
		outputParamSet = new HashSet<Param>();
		inputConceptSet = new HashSet<Concept>();
		outputConceptSet = new HashSet<Concept>();
	}

	public void addInputParam(Param param){
		this.inputParamSet.add(param);
	}
	
	public Set<Param> getInputParamSet() {
		return inputParamSet;
	}

	public void addOutputParam(Param param){
		this.outputParamSet.add(param);
	}
	
	public Set<Param> getOutputParamSet() {
		return outputParamSet;
	}

	public void addInputConcept(Concept concept){
		this.inputConceptSet.add(concept);
	}
	
	public Set<Concept> getInputConceptSet() {
		return inputConceptSet;
	}
	
	public void addOutputConcept(Concept concept){
		this.outputConceptSet.add(concept);
	}

	public Set<Concept> getOutputConceptSet() {
		return outputConceptSet;
	}

	public void addEquivalentService(Service service){
		this.equivalentServiceSet.add(service);
	}
	
	public Set<Service> getEquivalentServiceSet() {
		return equivalentServiceSet;
	}

	
}

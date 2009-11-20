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
	private int responseTime;
	private int throughput;
	private Integer score;

	public Service(String name) {
		super(name);
		inputParamSet = new HashSet<Param>();
		outputParamSet = new HashSet<Param>();
		inputConceptSet = new HashSet<Concept>();
		outputConceptSet = new HashSet<Concept>();
		score = 0;
	}
	
	public int compareTo(Service s) {
		Integer v1 = score;
		Integer v2 = s.score;
	
		return v1.compareTo(v2);
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

	public int getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}

	public int getThroughput() {
		return throughput;
	}

	public void setThroughput(int throughput) {
		this.throughput = throughput;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	
}

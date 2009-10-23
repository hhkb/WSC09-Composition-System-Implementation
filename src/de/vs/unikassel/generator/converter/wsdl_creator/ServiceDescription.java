package de.vs.unikassel.generator.converter.wsdl_creator;

import java.util.Vector;

/**
 * Represents a service.
 * @author Marc Kirchhoff
 *
 */
public class ServiceDescription {
	
	/**
	 * The name of the service.
	 */
	private String name;
	
	/**
	 * The input-instances/concepts of the service.
	 */
	private Vector<String> inputs;
		
	/**
	 * The output- instances/concepts of the service.
	 */
	private Vector<String> outputs;
	
	public ServiceDescription() {
		this.name = null;
		this.inputs = new Vector<String>();
		this.outputs = new Vector<String>();
	}

	/**
	 * @return the inputs
	 */
	public Vector<String> getInputs() {
		return inputs;
	}

	/**
	 * @param inputs the inputs to set
	 */
	public void setInputs(Vector<String> inputs) {
		this.inputs = inputs;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the outputs
	 */
	public Vector<String> getOutputs() {
		return outputs;
	}

	/**
	 * @param outputs the outputs to set
	 */
	public void setOutputs(Vector<String> outputs) {
		this.outputs = outputs;
	}
}

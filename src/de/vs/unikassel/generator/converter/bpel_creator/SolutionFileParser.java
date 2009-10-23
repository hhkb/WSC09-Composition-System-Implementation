package de.vs.unikassel.generator.converter.bpel_creator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

interface SolutionElement {
	
}

/**
 * An abstract service-describtion.
 * @author Marc Kirchhoff
 *
 */
class ServiceDesc implements SolutionElement {
	
	/**
	 * Contains the input-concepts.
	 */
	private Vector<String> inputs;
	
	/**
	 * Contains the output-concepts.
	 */
	private Vector<String> outputs;
	
	/**
	 * The services which realize the abstract service.
	 */
	private Vector<String> services;
		
	/**
	 * Creates an empty service-description.
	 */
	public ServiceDesc() {
		this.inputs = new Vector<String>();
		this.outputs = new Vector<String>();
		this.services = new Vector<String>();
	}

	/**
	 * Returns the input-concepts.
	 * @return The input-concepts.
	 */
	public Vector<String> getInputs() {
		return inputs;
	}

	/**
	 * Sets the input-concepts.
	 * @param inputs The input-concepts.
	 */
	public void setInputs(Vector<String> inputs) {
		this.inputs = inputs;
	}

	/**
	 * Returns the output-concepts.
	 * @return The output-concepts.
	 */
	public Vector<String> getOutputs() {
		return outputs;
	}

	/**
	 * Sets the output-concepts.
	 * @param outputs The outputs-concepts.
	 */
	public void setOutputs(Vector<String> outputs) {
		this.outputs = outputs;
	}

	/**
	 * Returns the names of the services.
	 * @return
	 */
	public Vector<String> getServices() {
		return services;
	}

	/**
	 * Sets the names of the services.
	 * @param services The names of the services.
	 */
	public void setServices(Vector<String> services) {
		this.services = services;
	}
}

/**
 * Base-class of all structure-elements (<sequence>, <parallel>, <solution>).
 * @author Marc Kirchhoff
 */
abstract class StructureElement implements SolutionElement {
	
	/**
	 * The structure-elements (<sequence>, <parallel>) that this sequence-elements contains.
	 */
	protected Vector<SolutionElement> solutionElements;
		
	/**
	 * Creates a new structure-element.
	 */
	public StructureElement() {
		this.solutionElements = null;
	}
	
	/**
	 * Returns the structure-elements.
	 * @return The structure-elements.
	 */
	public Vector<SolutionElement> getSolutionElements() {
		return this.solutionElements;
	}
	
	/**
	 * Set the structure-elements.
	 * @param solutionElements The structure-elements.
	 */
	public void setSolutionElements(Vector<SolutionElement> solutionElements) {
		this.solutionElements = solutionElements;		
	}
}

/**
 * Represents an <sequence>-element.
 * @author Marc Kirchhoff
 */
class Sequence extends StructureElement {
		
	/**
	 * Creates a new sequence-element.
	 */
	public Sequence() {
		super();
	}
}

/**
 * Represents an <parallel>-element.
 * @author Marc Kirchhoff
 */
class Parallel extends StructureElement {
	
	/**
	 * Creates a new parallel-element.
	 */
	public Parallel() {
		super();
	}
}

/**
 * Represents an <solution>-element.
 * @author Marc Kirchhoff
 */
class Solution extends StructureElement{
	
	/**
	 * Constructs an empty solution.
	 */
	public Solution() {
		super();
	}
}

/**
 * This class is used to parse solution-files.
 * @author Marc Kirchhoff
 *
 */
public class SolutionFileParser implements Runnable{
		
	/**
	 * The input-stream that contains the solution-file.
	 */
	private InputStream solutionFileInputStream;
	
	/**
	 * The basis-solutions.
	 */
	private Vector<Solution> solutions;
	
	/**
	 * This enum is used during the parsing of the service-description.
	 */
	private enum ParameterType { INPUT, OUTPUT, NONE };
	
	/**
	 * Initializes some class-members.
	 */
	private SolutionFileParser() {
		this.solutions = new Vector<Solution>();
	}
	
	/**
	 * Creates a new "SolutionFileParser" with the given solution-file.
	 * @param solutionFilePath
	 */
	public SolutionFileParser(String solutionFilePath) {
		this();		
		
		try {
			this.solutionFileInputStream = new FileInputStream(solutionFilePath);
		} catch (FileNotFoundException exception) {
			this.solutionFileInputStream = null;
			System.err.println("SolutionFileParser: The file "+solutionFilePath+" doesn't exist.");
			exception.printStackTrace();		
		}
	}
	
	/**
	 * Creates a new "SolutionFileParser" with the given solution-document.
	 * @param solutionDocument The solution-document.
	 */
	public SolutionFileParser(InputStream solutionDocument) {
		this();
		
		this.solutionFileInputStream = solutionDocument;
	}
	
	/**
	 * Parses the solution-file.
	 */
	public void parseSolutionFile() {
		if(this.solutionFileInputStream == null) {
			System.err.println("SolutionFileParser: Parsing-failed. Invalid input-stream.");
			return;
		}
		
		// TODO Validate solution-file.
		
		try {
			
			// Create an xml-parser.
			XMLInputFactory factory = XMLInputFactory.newInstance(); 
			XMLStreamReader parser = factory.createXMLStreamReader(this.solutionFileInputStream);
			
			// Parse the solution-file-elements.
			Solution currentSolution = null;
			ServiceDesc currentServiceDesc = null;
			ParameterType currentParameterType = ParameterType.NONE;
			Vector<StructureElement> structureElements = null;
			StructureElement currentStructureElement = null;
			
			
			while (parser.hasNext())  {
				int event = parser.next();
				
				switch(event) {				
								    	
					case XMLStreamConstants.START_ELEMENT: {
						String elementName = parser.getLocalName();
						
						if(elementName.equals("solution")) {
														
							currentSolution = new Solution();
							currentStructureElement = currentSolution;
							structureElements = new Vector<StructureElement>();
							structureElements.add(currentSolution);
						}
						else if(elementName.equals("sequence")) {
							Sequence sequence = new Sequence();
							if(currentStructureElement.getSolutionElements() == null) {
								currentStructureElement.setSolutionElements(new Vector<SolutionElement>());
							}
							
							currentStructureElement.getSolutionElements().add(sequence);
							currentStructureElement = sequence;
							structureElements.add(sequence);
						}
						else if(elementName.equals("parallel")) {
							Parallel parallel = new Parallel();							
							if(currentStructureElement.getSolutionElements() == null) {
								currentStructureElement.setSolutionElements(new Vector<SolutionElement>());
							}
							
							currentStructureElement.getSolutionElements().add(parallel);
							currentStructureElement = parallel;
							structureElements.add(parallel);
						}
						else if(elementName.equals("serviceDesc")) {
							currentServiceDesc = new ServiceDesc();
							if(currentStructureElement.getSolutionElements() == null) {
								currentStructureElement.setSolutionElements(new Vector<SolutionElement>());
							}
							
							currentStructureElement.getSolutionElements().add(currentServiceDesc);
						}
						else if(elementName.equals("input")) {
							currentParameterType = ParameterType.INPUT;
						}
						else if(elementName.equals("output")) {
							currentParameterType = ParameterType.OUTPUT;
						}
						else if(elementName.equals("service")) {
							String serviceName = parser.getAttributeValue(null, "name");
							currentServiceDesc.getServices().add(serviceName);
						}
						else if(elementName.equals("concept")) {
				    		// Get the name of the instancec.
				    		String conceptName = parser.getAttributeValue(null, "name");
				    		
				    		// Put the instance in the corresponding vector.
				    		switch(currentParameterType) {
				    			case INPUT: {
				    				currentServiceDesc.getInputs().add(conceptName);				    				
				    				break;
				    			}
				    			case OUTPUT: {
				    				currentServiceDesc.getOutputs().add(conceptName);
				    				break;
				    			}
				    			case NONE: {
						    		// Do nothing. Maybe it is an concept-element within the task-element.
				    				break;
				    			}
				    		}				    		
				    	}
						
						break;
					}
					case XMLStreamConstants.END_ELEMENT: {
						
						String elementName = parser.getLocalName();
						
						if(elementName.equals("solution")) {
							this.solutions.add(currentSolution);
							currentSolution = null;
						}
						else if(elementName.equals("sequence") || elementName.equals("parallel")) {
							structureElements.remove(structureElements.size()-1);
							currentStructureElement = structureElements.get(structureElements.size()-1);							
						}
						
						break;
					}
					default: {
						break;
					}
				}
			}
				
		} catch (Exception exception) {
			System.err.println("SolutionFileParser: An error occurred during the parsing of the solution-file.");
			exception.printStackTrace();
		}
	}
	
	/**
	 * Calculates the services that must be removed to get an unsolvable problem.
	 * @return The services that must be removed.
	 */
	public Vector<String> calculateUnsolvableSolution() {
		Vector<String> services = new Vector<String>();
		
		for(Solution solution : this.solutions) {
			// Calculate the size of the complex-element.			
			Random random = new Random();
			int solutionElementIndex = random.nextInt(solution.getSolutionElements().size());
			processSolutionElement(solution.getSolutionElements().get(solutionElementIndex), services);			
		}
		
		return services;
	}
	
	/**
	 * 
	 * @param solutionElement
	 * @param services
	 */
	private void processSolutionElement(SolutionElement solutionElement, Vector<String> services) {
		if(solutionElement instanceof StructureElement) {
			StructureElement structureElement = (StructureElement)solutionElement;			
			
			Vector<ServiceDesc> serviceDescs = new Vector<ServiceDesc>();
			for(SolutionElement solutionElement2 : structureElement.getSolutionElements()) {
				if(solutionElement2 instanceof ServiceDesc) {
					serviceDescs.add((ServiceDesc)solutionElement2);
				}
			}
			
			if(serviceDescs.size() == 0) {
				Random random = new Random();
				int solutionElementIndex = random.nextInt(structureElement.getSolutionElements().size());
				processSolutionElement(structureElement.getSolutionElements().get(solutionElementIndex), services);
			}
			else {
				Random random = new Random();
				int serviceDescIndex = random.nextInt(serviceDescs.size());
				services.addAll((Vector<String>)serviceDescs.get(serviceDescIndex).getServices().clone());
				return;
			}
		}
	}

	/**
	 * Starts the parsing.
	 */
	@Override
	public void run() {
		parseSolutionFile();		
	}

	/**
	 * Returns the solutions.
	 * @return The solutions.
	 */
	public Vector<Solution> getSolutions() {
		return solutions;
	}
}

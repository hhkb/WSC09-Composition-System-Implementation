package de.vs.unikassel.solution_checker;

import java.io.InputStream;
import java.util.Vector;
import java.util.logging.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * A marker-interface. 
 * @author Marc Kirchhoff
 *
 */
interface SolutionElement {}

/**
 * Base-class of all structure-elements (<sequence>, <flow>, <switch>).
 * @author Marc Kirchhoff
 */
abstract class StructureElement implements SolutionElement {
	
	/**
	 * The structure-elements (<sequence>, <flow>, <switch>, <invoke>) that this structure-elements contains.
	 */
	protected Vector<SolutionElement> solutionElements;
		
	/**
	 * Creates a new structure-element.
	 */
	public StructureElement() {
		this.solutionElements = new Vector<SolutionElement>();
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
 * Represents a service-invocation.
 * @author Marc Kirchhoff
 *
 */
class ServiceInvocation implements SolutionElement{
	
	/**
	 * The name of the service.
	 */
	private String serviceName;
	
	/**
	 * Constructs a new <code>ServiceInvocation</code> with the specified service.
	 * @param serviceName The name of the 
	 */
	public ServiceInvocation(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Returns the name of the service.
	 * @return The name of the service.
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Sets the name of the service.
	 * @param serviceName The name of the service.
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}

/**
 * Represent an <switch>-element.
 * @author Marc Kirchhoff
 *
 */
class Alternatives extends StructureElement {
	
	/**
	 * Constructs a new <code>Alternatives</code>-element.
	 */
	public Alternatives() {
		super();
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
 * Represents an <flow>-element.
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
 * Represents a solution.
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
 * This class can be used to parse BPEL-solution-files.
 * @author Marc Kirchhoff
 *
 */
public class BPELSolutionParser implements Runnable{
	
	/**
	 * The logger.
	 */
	private static final Logger logger = Logger.getLogger(BPELSolutionParser.class.getName());
	
	/**
	 * The input-stream that contains the bpel-solution-document.
	 */
	private InputStream bpelSolutionDocumentIS;
	
	/**
	 * The solutions that were parsed.
	 */
	private Vector<Solution> solutions;

	/**
	 * Constructs a new <code>BPELSolutionParser</code> with the specified input-stream.
	 * @param bpelSolutionDocumentIS The inptu-stream that contains the bpel-solution-document.
	 */
	public BPELSolutionParser(InputStream bpelSolutionDocumentIS) {
		this.bpelSolutionDocumentIS = bpelSolutionDocumentIS;
		this.solutions = new Vector<Solution>();
	}
	
	/**
	 * Parses the bpel-solution-file.
	 * @throws XMLStreamException
	 */
	public void parseSolutionFile()
	throws XMLStreamException {
		
		// Error checking.
		if(this.bpelSolutionDocumentIS == null) {
			String errorMessage = "The BPEL-Solution-document input-stream is null.";
			BPELSolutionParser.logger.severe(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
				
		// Create an xml-parser.
		XMLInputFactory factory = XMLInputFactory.newInstance(); 
		XMLStreamReader parser = factory.createXMLStreamReader(this.bpelSolutionDocumentIS);
		
		Vector<StructureElement> structureElements = null;
		StructureElement currentStructureElement = null;
		
		// Parse the solution-file.
		while (parser.hasNext())  {
			int event = parser.next();
			
			switch(event) {				
							    	
				case XMLStreamConstants.START_ELEMENT: {
					String elementName = parser.getLocalName();
										
					if(elementName.equals("case")) {
						
						String caseName = parser.getAttributeValue(null, "name");
						if(caseName.startsWith("Alternative-Solution")) {
							Solution solution = new Solution();
							currentStructureElement = solution;
							structureElements = new Vector<StructureElement>();
							structureElements.add(solution);
						}						
					}
					else if(elementName.equals("sequence")) {
						String sequenceName = parser.getAttributeValue(null, "name");
						
						if(sequenceName == null || !sequenceName.equals("main")) {
							Sequence sequence = new Sequence();
							currentStructureElement.getSolutionElements().add(sequence);
							currentStructureElement = sequence;
							structureElements.add(sequence);
						}
					}
					else if(elementName.equals("flow")) {
						Parallel parallel = new Parallel();												
						currentStructureElement.getSolutionElements().add(parallel);
						currentStructureElement = parallel;
						structureElements.add(parallel);
					}
					else if(elementName.equals("switch")) {
						String switchName = parser.getAttributeValue(null, "name");
						if(switchName.startsWith("Alternative-Services")) {
							Alternatives alternatives = new Alternatives();
							currentStructureElement.getSolutionElements().add(alternatives);
							currentStructureElement = alternatives;
							structureElements.add(alternatives);
						}
					}
					else if(elementName.equals("invoke")) {
						String serviceName = parser.getAttributeValue(null, "name");
						ServiceInvocation serviceInvocation = new ServiceInvocation(serviceName);
						currentStructureElement.getSolutionElements().add(serviceInvocation);
					}
					else if(!elementName.equals("process") && !elementName.equals("receive")){
						String errorMessage = "The solution-file contains an invalid element: "+elementName;
						BPELSolutionParser.logger.severe(errorMessage);
						throw new XMLStreamException(errorMessage);
					}
					
					break;
				}
				case XMLStreamConstants.END_ELEMENT: {
					String elementName = parser.getLocalName();
					
					if(elementName.equals("sequence") || elementName.equals("flow")) {
						if(structureElements.size() > 0) {
							structureElements.remove(structureElements.size()-1);
							currentStructureElement = structureElements.get(structureElements.size()-1);
						}
					}
					else if(elementName.equals("case")) {						
						if(structureElements.size() == 1 && structureElements.get(structureElements.size()-1) instanceof Solution) {
							this.solutions.add((Solution)structureElements.get(structureElements.size()-1));
							structureElements.clear();
						}
					}
					else if(elementName.equals("switch")) {
						if(structureElements.size() > 0) {
							StructureElement structureElement = structureElements.get(structureElements.size()-1);
							if(structureElement instanceof Alternatives) {
								structureElements.remove(structureElements.size()-1);
								currentStructureElement = structureElements.get(structureElements.size()-1);
							}
						}						
					}
				
					break;
				}
				default: {
					break;
				}
			}
		}
	}
	

	/**
	 * Starts the parsing.
	 */
	@Override
	public void run() {
		
		try {
			parseSolutionFile();
		}
		catch(XMLStreamException streamException) {
			BPELSolutionParser.logger.severe("An error occurred during the parsing of the solution-file");
			streamException.printStackTrace();
		}
	}

	/**
	 * Returns the solutions.
	 * @return The solutions.
	 */
	public Vector<Solution> getSolutions() {
		return solutions;
	}
}

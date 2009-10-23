package de.vs.unikassel.generator.converter.bpel_creator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.transform.stream.StreamSource;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import de.vs.unikassel.generator.converter.*;
import de.vs.unikassel.generator.converter.owl_creator.OWL_Creator;

/**
 * Creates a BPEL-document from a solution-file.
 * @author Marc Kirchhoff
 *
 */
public class BPEL_Creator implements Runnable {
	
	/**
	 * The parser that contains the solution-file.
	 */
	private SolutionFileParser solutionFileParser;
	
	/**
	 * The BPEL-document.
	 */
	private Document bpelDocument;
	
	/**
	 * The name of the process.
	 */
	public static String processName = "WSC08";
		
	/**
	 * The target-namespace of the process.
	 */
	public static String targetNamespace = "http://www.ws-challenge.org/WSC08CompositionSolution/";
	
	/**
	 * Initializes some class-members.
	 */
	private BPEL_Creator() {
		this.bpelDocument = null;
	}
	
	/**
	 * Creates a new "BPEL_Creator" with the given solution-file.
	 * @param solutionFilePath The path of the solution-file.
	 */
	public BPEL_Creator(String solutionFilePath) {
		this();
		this.solutionFileParser = new SolutionFileParser(solutionFilePath);		
	}
	
	public BPEL_Creator(InputStream solutionDocument) {
		this();
		this.solutionFileParser = new SolutionFileParser(solutionDocument);		
	}
	
	/**
	 * Creates an empty BPEL-document.
	 */
	private Element createBasicBPELStructure() {
		
		this.bpelDocument = new Document();
		
		// Create <bpel:process>-element.
		Element processElement = new Element("process", Namespaces.bpelNamespace);
		processElement.addNamespaceDeclaration(Namespaces.bpelNamespace);
		processElement.addNamespaceDeclaration(Namespaces.serviceNamespace);
		processElement.setAttribute("name",BPEL_Creator.processName);
		processElement.setAttribute("targetNamespace",BPEL_Creator.targetNamespace);		
		
		this.bpelDocument.setRootElement(processElement);
		
		// Create main-<bpel:sequence>-element and add it to the <bpel:process>-element..
		Element mainSequenceElement = new Element("sequence", Namespaces.bpelNamespace);
		mainSequenceElement.setAttribute("name", "main");
		processElement.addContent(mainSequenceElement);		
		
		// Create an <bpel:receive>-element and add it to the <bpel:sequence>-element.
		// Starting BPEL invocation (Input: Challenge Query)
		Element receiveElement = new Element("receive", Namespaces.bpelNamespace);
		receiveElement.setAttribute("name", "receiveQuery");
		receiveElement.setAttribute("portType", "solutionProcess");
		receiveElement.setAttribute("variable", "query");
		mainSequenceElement.addContent(receiveElement);
		
		return mainSequenceElement;
	}
	
	/**
	 * Processes an <sequence>-element.
	 * @param sequence The <sequence>-element.
	 * @param parentElement The current-element of the bpel-document.
	 */
	private void processSequenceElement(Sequence sequence, Element parentElement) {
		Element sequenceElement = new Element("sequence", Namespaces.bpelNamespace);
		parentElement.addContent(sequenceElement);
		
		for(SolutionElement solutionElement : sequence.getSolutionElements()) {
			processSolutionElement(solutionElement, sequenceElement);
		}
	}
	
	/**
	 * Processes a <parallel>-element.
	 * @param parallel The <parallel>-element.
	 * @param parentElement The current-element of the bpel-document.
	 */
	private void processParallelElement(Parallel parallel, Element parentElement) {
		Element flowElement = new Element("flow", Namespaces.bpelNamespace);
		parentElement.addContent(flowElement);
		
		for(SolutionElement solutionElement : parallel.getSolutionElements()) {
			processSolutionElement(solutionElement, flowElement);
		}
	}
	
	/**
	 * Processes a structure-element (<parallel> or <sequence>)
	 * @param structureElement The structure-element (<parallel> or <sequence>)
	 * @param parentElement The current element of the bpel-document.
	 */
	private void processStructureElement(StructureElement structureElement, Element parentElement) {
		if(structureElement instanceof Sequence) {
			processSequenceElement((Sequence)structureElement, parentElement);
		}
		else if(structureElement instanceof Parallel) {
			processParallelElement((Parallel)structureElement, parentElement);
		}
	}
	
	/**
	 * Processes a solution-element.
	 * @param solutionElement
	 * @param parentElement
	 */
	private void processSolutionElement(SolutionElement solutionElement, Element parentElement) {
		if(solutionElement instanceof StructureElement) {
			processStructureElement((StructureElement)solutionElement, parentElement);
		}
		else if(solutionElement instanceof ServiceDesc) {
			processServiceDesc((ServiceDesc)solutionElement, parentElement);
		}
	}
	
	/**
	 * Processes an <solution>-element.
	 * @param solution The solution-element.
	 */
	private void processSolution(Solution solution, Element switchElement, int solutionCount) {
		// Create an <bpel:case>-element and add it to the switch-element.
		Element caseElement = new Element("case", Namespaces.bpelNamespace);
		caseElement.setAttribute("name", "Alternative-Solution"+solutionCount);
		switchElement.addContent(caseElement);		
		
		for(SolutionElement solutionElement : solution.getSolutionElements()) {
			processSolutionElement(solutionElement, caseElement);
		}	
	}
	
	/**
	 * Process a service-describtion (<serviceDesc>).
	 * @param parentElement The parent-element.
	 * @param serviceDesc The service-describtion.
	 */
	private void processServiceDesc(ServiceDesc serviceDesc, Element parentElement) {
		
		if(serviceDesc.getServices().size() > 1) {
			// Create an <bpel:switch>-element.
			Element switchElement = new Element("switch", Namespaces.bpelNamespace);
			switchElement.setAttribute("name", "Alternative-Services");
			parentElement.addContent(switchElement);
			
			// Create an <bpel:case>-element for every service-alternative.
			for(String service : serviceDesc.getServices()) {
				
				// Create an <bpel:case>-element and add it to the <bpel:switch>-element.
				Element caseElement = new Element("case", Namespaces.bpelNamespace);
				caseElement.setAttribute("name", "Execute-"+service+"Service");
				switchElement.addContent(caseElement);
				
				// Create an <bpel:sequence>-element and add it to the <bpel:case>-element.
				Element altServSequenceElement = new Element("sequence", Namespaces.bpelNamespace);
				caseElement.addContent(altServSequenceElement);
				
				// Create an <bpel:invoke>-element and add it to the <bpel:sequence>-element.
				Element invokeElement = new Element("invoke", Namespaces.bpelNamespace);
				invokeElement.setAttribute("name", Namespaces.serviceNamespace.getPrefix()+":"+service+"Service");
				invokeElement.setAttribute("portType", Namespaces.serviceNamespace.getPrefix()+":"+service+"PortType");
				invokeElement.setAttribute("operation", Namespaces.serviceNamespace.getPrefix()+":"+service+"Operation");
				altServSequenceElement.addContent(invokeElement);
			}
		}
		else if(serviceDesc.getServices().size() == 1){
			// Create an <bpel:invoke>-element and add it to the <bpel:sequence>-element.
			String service = serviceDesc.getServices().get(0);
			Element invokeElement = new Element("invoke", Namespaces.bpelNamespace);
			invokeElement.setAttribute("name", Namespaces.serviceNamespace.getPrefix()+":"+service+"Service");
			invokeElement.setAttribute("portType", Namespaces.serviceNamespace.getPrefix()+":"+service+"PortType");
			invokeElement.setAttribute("operation", Namespaces.serviceNamespace.getPrefix()+":"+service+"Operation");
			parentElement.addContent(invokeElement);
		}		
	}
	
	/**
	 * Creates the BPEL-document.
	 */
	public void createBPELDocument() {
		
		this.solutionFileParser.parseSolutionFile();
		
		Element mainSequenceElement = createBasicBPELStructure();
		
		// Create a Switch/Case operator for alternative solutions.
		// Create an <bpel:switch>-element and add it to the <bpel:sequence>-element.
		Element switchElement = new Element("switch", Namespaces.bpelNamespace);
		switchElement.setAttribute("name", "SolutionAlternatives");
		mainSequenceElement.addContent(switchElement);
		
		Vector<Solution> solutions = this.solutionFileParser.getSolutions();
		for(int i = 0; i< solutions.size(); ++i) {
			processSolution(solutions.get(i), switchElement, i);
		}
	}
	
	/**
	 * Saves the BPEL-document.
	 * @param fileName The absolute path of the file.
	 * @return "true" if the saving was successful otherwise "false".
	 */
	public boolean saveBPELDocument(String fileName) {
		if(this.bpelDocument == null) {
			System.err.println("BPEL_Creator: The BPEL-file wasn't created until now.");
			return false;
		}
		
		File newBPELFile = new File(fileName);
		return saveBPELDocument(newBPELFile);
	}
	
	/**
	 * Saves the BPEL-document.
	 * @param fileName The path of the file.
	 * @return "true" if the saving was successful otherwise "false".
	 */
	public boolean saveBPELDocument(File file) {
		if(this.bpelDocument == null) {
			System.err.println("BPEL_Creator: The BPEL-file wasn't created until now.");
			return false;
		}
		
		System.out.println("BPEL_Creator: Saving BPEL-document at "+file.getAbsolutePath());
		
		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(this.toString());
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException exception) {
			System.err.println("BPEL_Creator: An error occurred during the writing of the BPEL-file.");
			exception.printStackTrace();
			return false;
		}
		
		System.out.println("BPEL_Creator: BPEL-document successfully saved at "+file.getAbsolutePath());
		
		return true;
	}
	
	/**
	 * Returns the BPEL-document in a pretty-format.
	 * @return The BPEL-document in a pretty-format or null if the WSDL-file wasn't created until now.
	 */
	@Override
	public String toString() {
		if(this.bpelDocument != null) {
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			return outputter.outputString(this.bpelDocument);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Starts the creation of the BPEL-document.
	 */
	@Override
	public void run() {
		createBPELDocument();		
	}
	
	/**
	 * Prints the commandline-manual.
	 */
	public static void printManual() {
		System.out.println();
		System.out.println("Comamand Line BPEL_Creator");
		System.out.println("---------------------------");
		System.out.println("Usage:");
		System.out.println("BPEL_Creator -file filename -o targfilename");
		System.out.println();
		System.out.println("file: File which will be tranformed to BPEL.");
		System.out.println("targetfilename: Output-file containing the BPEL.");
		System.out.println("---------------------------");
		System.out.println("Parameters:");
		System.out.println("-?: Printing this manual.");
		System.out.println("-file: The file which will be transformed to BPEL.");
		System.out.println("-o: Output-file containing the BPEL.");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length == 0 || args[0].equals("-?")) {
			BPEL_Creator.printManual();
			System.exit(0);
		}
		
		String fileName = null;
		String targetFileName = null;
		
		for(int i = 0; i < args.length; ++i) {
			
			if(args[i].equals("-?")) {
				BPEL_Creator.printManual();
				System.exit(0);
			}
			else if(args[i].equals("-file")) {
				if(i+1 > args.length) {
					System.out.println("Parameter-value for option -file not found.");
					System.exit(1);
				}
				
				fileName = args[i+1];
			}
			else if(args[i].equals("-o")) {
				if(i+1 > args.length) {
					System.out.println("Parameter-value for option -o not found");
					System.exit(1);
				}
				
				targetFileName = args[i+1];
			}			
		}
		
		if(fileName == null) {
			System.out.println("You must enter a filename");
			System.exit(1);
		}
		
		if(targetFileName == null) {
			System.out.println("You must enter a filename for the output.");
			System.exit(1);
		}	
		
		BPEL_Creator bpelCreator = new BPEL_Creator(fileName);
		bpelCreator.createBPELDocument();
		
		System.out.println("BPEL_Creator: Generated BPEL-document:");
		System.out.println(bpelCreator.toString());
		
		System.out.println("BPEL_Creator: Saving BPEL-document.");
		bpelCreator.saveBPELDocument(targetFileName);

	}
}

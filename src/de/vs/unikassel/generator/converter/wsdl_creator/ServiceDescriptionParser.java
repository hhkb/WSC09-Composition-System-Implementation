package de.vs.unikassel.generator.converter.wsdl_creator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Vector;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;


/**
 * This class is used to parse service-description-files.
 * @author Marc Kirchhoff
 *
 */
public class ServiceDescriptionParser implements Runnable, Parser{
	
	/**
	 * Contains the services.
	 */
	private Vector<ServiceDescription> services;
		
	/**
	 * The input-stream that contains the service-description-file.
	 */
	private InputStream serviceDescriptionFileInputStream;
	
	/**
	 * This enum is used during the parsing of the service-description.
	 */
	private enum ParameterType { INPUT, OUTPUT, NONE };
	
	/**
	 * Sets some class-members.
	 */
	private ServiceDescriptionParser() {
		this.services = new Vector<ServiceDescription>();
	}
	
	/**
	 * Creates a new ServiceDescriptionParser.
	 * @param serviceDescriptionFilePath The path of the file that descripes the service.
	 */
	public ServiceDescriptionParser(String serviceDescriptionFilePath) {		
		this();
		
		try {
			this.serviceDescriptionFileInputStream = new FileInputStream(serviceDescriptionFilePath);
		} catch (FileNotFoundException exception) {
			this.serviceDescriptionFileInputStream = null;
			System.err.println("ServiceDescriptionFileParser: The file "+serviceDescriptionFilePath+" doesn't exist.");
			exception.printStackTrace();
			return;
		}
	}
	
	/**
	 * Creates a new ServiceDescriptionParser.
	 * @param serviceDescriptionDocument The service-description-document.
	 */
	public ServiceDescriptionParser(InputStream serviceDescriptionDocument) {
		this();
		this.serviceDescriptionFileInputStream = serviceDescriptionDocument;
	}
	
	/**
	 * Parses the service-description-file.
	 */
	public void parse(){
		
		if(this.serviceDescriptionFileInputStream == null) {
			System.err.println("ServiceDescriptionParser: Parsing-failed. Invalid input-stream.");
			return;
		}
		
		// TODO Validate service-description-file.
		
		try {
			
			// Create an xml-parser.
			XMLInputFactory factory = XMLInputFactory.newInstance(); 
			XMLStreamReader parser = factory.createXMLStreamReader(this.serviceDescriptionFileInputStream);
			
			// Parse the service-description-file-elements.
			String currentElementName = null;						
			ParameterType currentParameterType = ParameterType.NONE;
			ServiceDescription serviceDescription = null;
			
			while (parser.hasNext())  {
				int event = parser.next();
				
				switch(event) {				
								    	
					case XMLStreamConstants.START_ELEMENT: {
						// Get the name of the current name.
						currentElementName = parser.getLocalName();
												
						if(currentElementName.equals("service")) {
							
							// Get the name of the service.
							String serviceName = parser.getAttributeValue(null, "name");
							serviceDescription = new ServiceDescription();
							serviceDescription.setName(serviceName);
						}
						else if(currentElementName.equals("inputs")) {
				    		currentParameterType = ParameterType.INPUT;
				    	}
				    	else if(currentElementName.equals("outputs")) {
				    		currentParameterType = ParameterType.OUTPUT;
				    	}
				    	else if(currentElementName.equals("instance")) {
				    		// Get the name of the instancec.
				    		String instanceName = parser.getAttributeValue(null, "name");
				    		
				    		// Put the instance in the corresponding vector.
				    		switch(currentParameterType) {
				    			case INPUT: {				    				
				    				serviceDescription.getInputs().add(instanceName);
				    				break;
				    			}
				    			case OUTPUT: {
				    				serviceDescription.getOutputs().add(instanceName);
				    				break;
				    			}
				    			case NONE: {
						    		int currentLine = parser.getLocation().getLineNumber();
						    		int currentColumn = parser.getLocation().getColumnNumber();
						    		System.err.println("ServiceDescriptionParser: An error occurred during the parsing at line "+currentLine+", column: "+currentColumn+".");
						    		System.err.println("ServiceDescriptionParser: Name of the invalid-element: "+currentElementName);
				    				break;
				    			}
				    		}				    		
				    	}
												
						break;
					}									  
					case XMLStreamConstants.END_ELEMENT: { 				    	
					    	
				    	currentElementName = parser.getLocalName();
				    	
				    	// Put the instances in the corresponding hashtable.
				    	if(currentElementName.equals("service")) {
				    		this.services.add(serviceDescription);
				    		serviceDescription = null;
				    	}
					    	
					    break;					
					}
					default: {
						break;	
					}
				}		
			}
		} catch (Exception exception) {
			System.err.println("ServiceDescriptionParser: An error occurred during the parsing of the service-description-file.");
			exception.printStackTrace();
			return;
		}			
		
	}

	/**
	 * Starts the parsing. 	 
	 */
	@Override
	public void run() {
		parse();		
	}

	/**
	 * @return the services
	 */
	public Vector<ServiceDescription> getServiceDescriptions() {
		return services;
	}
}

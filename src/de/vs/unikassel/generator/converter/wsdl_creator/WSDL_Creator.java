package de.vs.unikassel.generator.converter.wsdl_creator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Random;
import java.util.TreeSet;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import de.vs.unikassel.generator.converter.Namespaces;
import de.vs.unikassel.generator.converter.wsla_creator.WSLASingleton;
import de.vs.unikassel.generator.converter.wsla_creator.WSLATemplate;

/**
 * The parser-interface that is used by the WSDL_Creator.
 * @author Marc Kirchhoff
 */
interface Parser {
	
	/**
	 * Returns the service-descriptions as a result of the parsing.
	 * @return The service-descriptions.
	 */
	public Vector<ServiceDescription> getServiceDescriptions();
	
	/**
	 * Starts the parsing.
	 */
	public void parse();
	
	/**
	 * Starts the parsing within an extra thread.
	 */
	public void run();
}

/**
 * Creates a WSDL-document from a service-description-file.
 * @author Marc Kirchhoff
 *
 */
public class WSDL_Creator implements Runnable {
	
	/**
	 * A parser that contains the service-description-file.
	 */
	private Parser parser;
	
	/**
	 * The wsdl-document.
	 */
	private Document wsdlDocument;
	
	/**
	 * The <wsdl:definitions>-element of the wsdl-document.
	 */
	private Element definitionsElement;
	
	/**
	 * The <wsdl:types>-element.
	 */
	private Element typesElement;
	
	/**
	 * The <mece:semExtension>-element.
	 */
	private Element semExtensionElement;
	
	/**
	 * This variable is used to create unique-names.
	 */
	private int countComplexElements;
	
	private Hashtable<String, String> elementNames;
	
	private TreeSet<String> allElementNames;
	
	private Random elementNamesGenerator;
	
	/**
	 * Creates a new WSDL_Creator with the given service-description-file.
	 * @param parser The parser.
	 */
	public WSDL_Creator(Parser parser) {
		this.parser = parser;
		this.wsdlDocument = null;
		this.definitionsElement = null;
		this.typesElement = null;
		this.countComplexElements = 0;
		this.semExtensionElement = null;
		this.elementNames = new Hashtable<String, String>();
		this.allElementNames = new TreeSet<String>();
		this.elementNamesGenerator = new Random();
	}
	
	/**
	 * Creates an empty wsdl-document.
	 */
	private void createBasicWSDLStructure() {
		this.wsdlDocument = new Document();
			
		// Create the <wsdl:definitions>-element.
		Element definitionsElement = new Element("definitions",Namespaces.wsdlNamespace);
		definitionsElement.addNamespaceDeclaration(Namespaces.wsdlNamespace);
		definitionsElement.addNamespaceDeclaration(Namespaces.soapNamespace);
		definitionsElement.addNamespaceDeclaration(Namespaces.httpNamespace);
		definitionsElement.addNamespaceDeclaration(Namespaces.xsdNamespace);
		definitionsElement.addNamespaceDeclaration(Namespaces.soapencNamespace);
		definitionsElement.addNamespaceDeclaration(Namespaces.mimeNamespace);	
		definitionsElement.addNamespaceDeclaration(Namespaces.serviceNamespace);
		definitionsElement.setAttribute("targetNamespace", Namespaces.serviceNamespace.getURI());
		
		this.wsdlDocument.setRootElement(definitionsElement);
		this.definitionsElement = definitionsElement;
		
		// Create the <wsdl:types>-element.
		this.typesElement = new Element("types", Namespaces.wsdlNamespace);
		
		// Create the <xsd:schema>-element and add it to the <wsdl:types>-element.
		Element schemaElement = new Element("schema", Namespaces.xsdNamespace);
		//schemaElement.addNamespaceDeclaration(Namespaces.meceNamespace);
		schemaElement.setAttribute("targetNamespace",Namespaces.serviceNamespace.getURI());
		this.typesElement.addContent(schemaElement);
		
		// Create the <mece:semExtension>-element and add it to the <wsdl:definitions>-element.
		this.semExtensionElement = new Element("semExtension", Namespaces.meceNamespace);		
	}
	
	/**
	 * Creates the <wsdl:service>-element.
	 * @param serviceName The name of the service.
	 */
	private void createServiceElement(ServiceDescription service) {
		
		// Create the <wsdl:service>-element and add it to the <wsdl:definitions>-element.
		Element serviceElement = new Element("service", Namespaces.wsdlNamespace);		
		serviceElement.setAttribute("name", service.getName()+"Service");
		this.definitionsElement.addContent(serviceElement);
		
		// Create the <wsdl:port>-element and add it to the <wsdl:service>-element.
		Element portElement = new Element("port", Namespaces.wsdlNamespace);		
		portElement.setAttribute("binding", Namespaces.serviceNamespace.getPrefix()+":"+service.getName()+"SOAP");
		portElement.setAttribute("name", service.getName()+"Port");
		serviceElement.addContent(portElement);
		
		// Create the <soap:address>-element and add it to the <wsdl:port>-element..
		Element soapAddressElement = new Element("address", Namespaces.soapNamespace);		
		soapAddressElement.setAttribute("location", "http://www.unknownexamplehost.ukn/");
		portElement.addContent(soapAddressElement);
	}
	
	/**
	 * Creates the <wsdl:binding>-element.
	 * @param serviceName The name of the service.
	 */
	private void createBindingElement(ServiceDescription service) {
		// Create the <wsdl:binding>-element and add it to the <wsdl:definitions>-element.
		Element bindingElement = new Element("binding", Namespaces.wsdlNamespace);		
		bindingElement.setAttribute("name",service.getName()+"SOAP");
		bindingElement.setAttribute("type", Namespaces.serviceNamespace.getPrefix()+":"+service.getName()+"PortType");
		this.definitionsElement.addContent(bindingElement);
		
		// Create the <soap:binding>-element and add it to the <wsdl:binding>-element.
		Element soapBindingElement = new Element("binding", Namespaces.soapNamespace);
		soapBindingElement.setAttribute("style","rpc");
		soapBindingElement.setAttribute("transport", "http://schemas.xmlsoap.org/soap/http");
		bindingElement.addContent(soapBindingElement);
		
		// Create the <wsdl:operation>-element and add it to the <wsdl:binding>-element.
		Element operationElement = new Element("operation", Namespaces.wsdlNamespace);
		operationElement.setAttribute("name", service.getName()+"Operation");
		bindingElement.addContent(operationElement);
		
		// Create the <soap:operation>-element and add it to the <wsdl:operation>-element.
		Element soapOperationElement = new Element("operation", Namespaces.soapNamespace);
		soapOperationElement.setAttribute("soapAction","http://www.ws-challenge.org/"+service.getName());
		operationElement.addContent(soapOperationElement);
		
		// Create the <wsdl:input>-element and add it to the <wsdl:operation>-element.
		Element inputElement = new Element("input", Namespaces.wsdlNamespace);
		operationElement.addContent(inputElement);
		
		// Create the <soap:body>-element and add it to the <wsdl:input>-element.
		Element soapBodyInputElement = new Element("body", Namespaces.soapNamespace);
		soapBodyInputElement.setAttribute("use", "literal");
		inputElement.addContent(soapBodyInputElement);
		
		// Create the <wsdl:output>-element and add it to the <wsdl:operation>-element.
		Element outputElement = new Element("output", Namespaces.wsdlNamespace);
		operationElement.addContent(outputElement);
		
		// Create the <soap:body>-element and add it to the <wsdl:input>-element.
		Element soapBodyOutputElement = new Element("body", Namespaces.soapNamespace);
		soapBodyOutputElement.setAttribute("use", "literal");
		outputElement.addContent(soapBodyOutputElement);	
	}
	
	/**
	 * Creates the <wsdl:portType>-element.
	 * @param serviceName The name of the service.
	 */
	private void createPortTypeElement(ServiceDescription service) {
		// Create the <wsdl:portType>-element and add it to the <wsdl:definitions>-element.
		Element portTypeElement = new Element("portType", Namespaces.wsdlNamespace);
		portTypeElement.setAttribute("name", service.getName()+"PortType");
		this.definitionsElement.addContent(portTypeElement);
		
		// Create the <wsdl:operation>-element and add it to the <wsdl:portType>-element.
		Element operationElement = new Element("operation", Namespaces.wsdlNamespace);
		operationElement.setAttribute("name", service.getName()+"Operation");
		portTypeElement.addContent(operationElement);
		
		// Create the <wsdl:input>-element and add it to the <wsdl:operation>-element.
		Element inputElement = new Element("input", Namespaces.wsdlNamespace);
		inputElement.setAttribute("message", Namespaces.serviceNamespace.getPrefix()+":"+service.getName()+"RequestMessage");
		operationElement.addContent(inputElement);
		
		// Create the <wsdl:output>-element and add it to the <wsdl:operation>-element.
		Element outputElement = new Element("output", Namespaces.wsdlNamespace);
		outputElement.setAttribute("message", Namespaces.serviceNamespace.getPrefix()+":"+service.getName()+"ResponseMessage");
		operationElement.addContent(outputElement);
	}
	
	/**
	 * Creates a simple xsd-element with the given name. The type of the element is xsd:string.
	 * Furthermore the element contains an "id"-attribute from the MECE-namespace.
	 * @param elementName The name of the element.
	 * @return The element.
	 */
	private Element createSimpleXSDElement(String elementName) {
		Element xsdElement = new Element("element", Namespaces.xsdNamespace);
		xsdElement.setAttribute("name", elementName);
		xsdElement.setAttribute("type", Namespaces.xsdNamespace.getPrefix()+":string");
		//xsdElement.setAttribute("id", elementName, Namespaces.meceNamespace);
		
		return xsdElement;
	}
	
	/**
	 * Creates the request- and response-<wsdl:message>-elements and the corresponding xsd-elements in the types-section.
	 * @param service The service-description.
	 */
	private void createMessageAndXSDElements(ServiceDescription service) {
		// Create the request-message.
		createMessageElement(service, true);
		
		// Create the response-message.
		createMessageElement(service, false);
	}
	
		
	/**
	 * Creates an <wsdl:message>-element for the input-instances of the service.
	 * @param service The service-description.
	 * @param requestMessage If this value if "true" this method creates the request-message otherwise the response-message.
	 */
	private void createMessageElement(ServiceDescription service, boolean requestMessage) {
		
		String messageElementName = null;
		Vector<String> instances = null;
	
		if(requestMessage) {
			messageElementName = service.getName()+"RequestMessage";
			instances = (Vector<String>)service.getInputs().clone();
		}
		else {
			messageElementName = service.getName()+"ResponseMessage";
			instances = (Vector<String>)service.getOutputs().clone();
		}
		
		// Create the <wsdl:message>-element and add it to the <wsdl:definitions>-element.
		Element wsdlMessageElement = new Element("message", Namespaces.wsdlNamespace);
		wsdlMessageElement.setAttribute("name", messageElementName);
		this.definitionsElement.addContent(wsdlMessageElement);		
		
		// Get the xsd-schema-element
		Element schemaElement = this.typesElement.getChild("schema", Namespaces.xsdNamespace);
		
		createNestedElements(schemaElement, wsdlMessageElement, instances, true);		
	}
	
	/**
	 * 
	 * @param parentElement
	 * @param wsdlMessageElement
	 * @param instances
	 * @param part
	 */
	private void createNestedElements(Element parentElement, Element wsdlMessageElement, Vector<String> instances, boolean part) {
		
		if(instances.size() == 0) {
			return;
		}
				
		// Create a complex-element?
		if(instances.size() > 1 && Math.random() <= 0.5) {		

			String name = "ComplexElement"+this.countComplexElements;
			++this.countComplexElements;
			
			if(part) {
			// Create the <wsdl:part>-element and add it to the <wsdl:message>-element.
				Element partElement = new Element("part", Namespaces.wsdlNamespace);
				partElement.setAttribute("element", Namespaces.serviceNamespace.getPrefix()+":"+name);
				partElement.setAttribute("name", name+"Part");				
				wsdlMessageElement.addContent(partElement);
			}

			// Create a complex-element and add it to the <xsd:schema>-element.
			Element complexXSDElement = new Element("element", Namespaces.xsdNamespace);
			complexXSDElement.setAttribute("name",name);
			//complexXSDElement.setAttribute("minOccurs", "0");
			//complexXSDElement.setAttribute("maxOccurs", "unbounded");
			parentElement.addContent(complexXSDElement);
						
			Element complexTypeElement = new Element("complexType", Namespaces.xsdNamespace);
			complexXSDElement.addContent(complexTypeElement);
			
			Element sequenceElement = new Element("sequence", Namespaces.xsdNamespace);
			complexTypeElement.addContent(sequenceElement);
			
			// Calculate the size of the complex-element.			
			Random random = new Random();
			int complexElementSize = random.nextInt(instances.size());
			if(complexElementSize == 0) {
				++complexElementSize;
			}
			
			int remainingElements = instances.size() - complexElementSize;
			if(remainingElements == 0) {
				++remainingElements;
			}
			// Create the simple elements and add they to the complex-element.
			for(int i = 0; i < remainingElements; ++i) {
				//String elementName = instances.get(i);
				String elementName = createUniqueElementName(instances.get(i));
				
				Element simpleXSDElement = createSimpleXSDElement(elementName);
				parentElement.addContent(simpleXSDElement);	
				
				if(part) {
					// Create the corresponding <wsdl:part>-element.
					Element partElement = new Element("part", Namespaces.wsdlNamespace);
					partElement.setAttribute("element", Namespaces.serviceNamespace.getPrefix()+":"+elementName);
					partElement.setAttribute("name", elementName+"Part");					
					wsdlMessageElement.addContent(partElement);
				}
			}			
			
			for(int i = 0; i < remainingElements; ++i) {				
				instances.remove(0);
			}
			
			createNestedElements(sequenceElement, wsdlMessageElement, instances, false);			
		}
		else {
			// Add the remaining-instances to the <xsd:schema>-element.
			for(int i = 0; i < instances.size(); ++i) {
				//String elementName = instances.get(i);
				String elementName = createUniqueElementName(instances.get(i));
				Element simpleXSDElement = createSimpleXSDElement(elementName);
				parentElement.addContent(simpleXSDElement);
				
				if(part) {
					// Create the corresponding <wsdl:part>-element.
					Element partElement = new Element("part", Namespaces.wsdlNamespace);
					partElement.setAttribute("element", Namespaces.serviceNamespace.getPrefix()+":"+elementName);
					partElement.setAttribute("name", elementName+"Part");					
					wsdlMessageElement.addContent(partElement);
				}
			}
		}
	}
	
	private String createUniqueElementName(String instanceName) {
		String elementName = null;
				
		do {		
			elementName = String.valueOf(this.elementNamesGenerator.nextInt(Integer.MAX_VALUE));			
		}
		while(this.allElementNames.contains(elementName));
		
		this.allElementNames.add(elementName);	
		this.elementNames.put(instanceName, elementName);
		
		return elementName;
	}
	
	/**
	 * 
	 * @param service
	 */
	private void createSemanticExtensions(ServiceDescription service) {
						
		// Create a <mece:semMessageExt>-element for the request-message.
		Element requestSemMessageExtElement = new Element("semMessageExt", Namespaces.meceNamespace);
		requestSemMessageExtElement.setAttribute("id",service.getName()+"RequestMessage");
		this.semExtensionElement.addContent(requestSemMessageExtElement);		
		
		// Create an <mece:semExt>-element for every-instance.
		for(String inputInstance : service.getInputs()) {
			Element semExtElement = new Element("semExt", Namespaces.meceNamespace);
			semExtElement.setAttribute("id", this.elementNames.get(inputInstance));
			requestSemMessageExtElement.addContent(semExtElement);
			
			Element ontologyRefElement = new Element("ontologyRef", Namespaces.meceNamespace);
			ontologyRefElement.setText("http://www.ws-challenge.org/wsc08.owl#"+inputInstance);
			semExtElement.addContent(ontologyRefElement);
		}
		
		// Create a <mece:semMessageExt>-element for the response-message.
		Element responseSemMessageExtElement = new Element("semMessageExt", Namespaces.meceNamespace);		
		responseSemMessageExtElement.setAttribute("id",service.getName()+"ResponseMessage");
		this.semExtensionElement.addContent(responseSemMessageExtElement);
		
		// Create an <mece:semExt>-element for every-instance.
		for(String outputInstance : service.getOutputs()) {
			Element semExtElement = new Element("semExt", Namespaces.meceNamespace);
			semExtElement.setAttribute("id", this.elementNames.get(outputInstance));
			responseSemMessageExtElement.addContent(semExtElement);
			
			Element ontologyRefElement = new Element("ontologyRef", Namespaces.meceNamespace);
			ontologyRefElement.setText("http://www.ws-challenge.org/wsc08.owl#"+outputInstance);
			semExtElement.addContent(ontologyRefElement);
		}
	}
	
	/**
	 * Creates the WSDL-document.
	 */
	public void createWSDLDocument() {
		// Parse the service-description-file.
		this.parser.parse();
		
		createBasicWSDLStructure();
		
		Random random = new Random();
		
		for(ServiceDescription service : this.parser.getServiceDescriptions()) {
			this.elementNames.clear();
			createServiceElement(service);
			createBindingElement(service);
			createPortTypeElement(service);
			createMessageAndXSDElements(service);
			createSemanticExtensions(service);
			
			WSLATemplate serviceqos = new WSLATemplate(service.getName()+"Service");
			
			serviceqos.setBindingname(service.getName()+"SOAP");
			serviceqos.setOperationname(service.getName()+"Operation");
			
			serviceqos.setResponsetimeurl("http://192.168.143.245:7438/ResponseTime");
			serviceqos.setThroughputurl("http://192.168.143.245:7438/Throughput");
			serviceqos.setServicedefinitionname("ServiceDefinition"+service.getName());
			serviceqos.setSlaparameternameresponsetime("SLAParameterResponsetime"+service.getName());
			serviceqos.setSlaparameternamethroughput("SLAParameterThroughput"+service.getName());
			serviceqos.setSloparameternameresponsetime("ServiceLevelObjectiveResponsetime"+service.getName());
			serviceqos.setSloparameternamethroughput("ServiceLevelObjectiveThroughput"+service.getName());
			
			int responsetime = random.nextInt(50)+1;
			int throughput = random.nextInt(20)+1;
			
			responsetime = responsetime * 10;
			throughput = throughput * 1000;
			
			serviceqos.setResponsetime("" + responsetime);
			serviceqos.setThroughput("" + throughput);
			
			WSLASingleton.repository.add(serviceqos);
		}
		
		// Add the <wsdl:types>-element to the <wsdl:definitions>-element.
		this.definitionsElement.addContent(this.typesElement);
		
		this.definitionsElement.addContent(this.semExtensionElement);
	}
	
	/**
	 * Saves the WSDL-document.
	 * @param fileName The absolute path of the file.
	 * @return "true" if the saving was successfull otherwise "false".
	 */
	public boolean saveWSDLFile(String fileName) {
		if(this.wsdlDocument == null) {
			System.err.println("WSDL_Creator: The WSDL-file wasn't created until now.");
			return false;
		}
		
		File newWSDLFile = new File(fileName);
		
		return saveWSDLFile(newWSDLFile);
	}
	
	/**
	 * Saves the WSDL-file.
	 * @param file The file.
	 * @return "true" if the saving was successfull otherwise "false".
	 */
	public boolean saveWSDLFile(File file) {
		
		if(this.wsdlDocument == null) {
			System.err.println("WSDL_Creator: The WSDL-file wasn't created until now.");
			return false;
		}
		
		System.out.println("WSDL_Creator: Saving WSDL-document at "+file.getAbsolutePath());
		
		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(this.toString());
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException exception) {
			System.err.println("WSDL_Creator: An error occurred during the writing of the WSDL-file.");
			exception.printStackTrace();
			return false;
		}
		
		System.out.println("WSDL_Creator: WSDL-document successfully saved at "+file.getAbsolutePath());
		
		return true;		
	}
	
	/**
	 * Returns the WSDL-file in a pretty-format.
	 * @return The WSDL-file in a pretty-format or null if the WSDL-file wasn't created until now.
	 */
	@Override
	public String toString() {
		if(this.wsdlDocument != null) {
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			return outputter.outputString(this.wsdlDocument);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Starts the creation of the WSDL-document.
	 */
	@Override
	public void run() {
		createWSDLDocument();		
	}
}

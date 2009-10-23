package de.vs.unikassel.generator.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




/**
 * A simple test class to check if there is a semantic extension for every element.
 * @author Marc Kirchhoff
 *
 */
public class ElementChecker {
	
	private static final Namespace xsdNamespace = Namespace.getNamespace("http://www.w3.org/2001/XMLSchema");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Set<String> elementNames = new HashSet<String>();
		Set<String> semanticExtensions = new HashSet<String>();
		
		try {
			//String servicesDefinitionFilePath = "C:\\lala\\Services.wsdl";
			String servicesDefinitionFilePath = "C:\\lala\\WSCChallengeSets2008\\WSCChallengeSets\\ChallengeSet09\\Services.wsdl";
			//String servicesDefinitionFilePath = "C:\\WSC08TestSet\\WSC Site\\Testset\\Testset-30.06.2008\\Testset02\\Services.wsdl";
			
			// Create a dom tree.
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		    DocumentBuilder builder = factory.newDocumentBuilder(); 
		    Document document = builder.parse( new File(servicesDefinitionFilePath) ); 
		    		    
		    // Get the names of all simple elements of the xsd-schema definition..
		    NodeList elementNodes = document.getElementsByTagName("xs:element");
		    
		    int counter = 0;
		    int length = elementNodes.getLength();
		    while(true) {
		    	
		    	if(counter == length) {
		    		break;
		    	}		    	
		    	
		    	Node elementNode = elementNodes.item(counter);		    	
		    			    	
		    	if(elementNode instanceof Element) {   		
		    		
		    		Element element = (Element)elementNode;	    		
		    		String nameAttribute = element.getAttribute("name");
		    		
		    		if(!nameAttribute.startsWith("ComplexElement")) {		    			
		    			elementNames.add(nameAttribute);
		    		}	    		
		    	}
	    		
	    		++counter;
		    }
		    
		    
		 // Get the semantic extension nodes.
		    NodeList semanticExtNodes = document.getElementsByTagName("mece:semExt");
		    
		    counter = 0;
		    length = semanticExtNodes.getLength();
		    while(true) {
		    	
		    	if(counter == semanticExtNodes.getLength()) {
		    		break;
		    	}
		    	
		    	Node semanticExtNode = semanticExtNodes.item(counter);
		    	
		    	if(semanticExtNode instanceof Element) {
		    		
		    		Element semanticExtElement = (Element)semanticExtNode;
		    		String idAttribute = semanticExtElement.getAttribute("id");
		    		
		    		semanticExtensions.add(idAttribute);
		    	}
		    	
		    	++counter;
		    }
		    
		    
		    System.out.println();
		    System.out.println("Number of element-names: "+elementNames.size());
		    System.out.println("Number of semantic extensions: "+semanticExtensions.size());
		    	
		    // Search all element that don't have a semantic extension.
		    List<String> missingSemanticExtensions = new Vector<String>();
		    for(String elementName : elementNames) {
		    	if(!semanticExtensions.contains(elementName)) {
		    		missingSemanticExtensions.add(elementName);
		    	}
		    }
		    
		    if(missingSemanticExtensions.size() > 0) {
		    	System.out.println("Missing semantic extensions: ");
		    	for(String missingSemanticExtension : missingSemanticExtensions) {
		    		System.out.println(missingSemanticExtension);
		    	}		    	
		    }
		    else {
		    	System.out.println("No semantic extensions missing.");
		    }
		    
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}		
	}
}

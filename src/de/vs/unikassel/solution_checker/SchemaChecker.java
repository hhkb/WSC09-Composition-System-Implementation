package de.vs.unikassel.solution_checker;

import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class SchemaChecker {
	
	public SchemaChecker() {		
	}
	
	/**
	 * Returns the internal WSC'09 BPEL Schema
	 * 
	 * @return XSD Schema Object
	 */
	@SuppressWarnings("static-access")
	public static Schema getBPELSchema() {
		// create a SchemaFactory capable of understanding WXS schemas
	    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

	    // load a WXS schema, represented by a Schema instance
	    URL stream = SchemaChecker.class.getClassLoader().getSystemResource("de/vs/unikassel/solution_checker/bpel.xsd");
	    Source schemaFile = null;
	    
		try {
			schemaFile = new StreamSource(stream.openStream());
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	    Schema schema = null;
	    
		try {
			schema = factory.newSchema(schemaFile);
		} catch (SAXException e1) {			
			e1.printStackTrace();
		}
		
		return schema;
	}		
	
	/**
	 * Validate the DOM tree. Output is printed to the console.
	 * 
	 * @param doc The DOM tree to be checked.
	 * @param schema The validation schema.
	 */
	public boolean checkSchema(Document doc, Schema schema) {
		// create a Validator instance, which can be used to validate an instance document
	    javax.xml.validation.Validator val = schema.newValidator();
	    
    	System.out.println("[SchemaChecker:] Checking BPEL schema...");
	    
	    // validate the DOM tree
	    try {
	        val.validate(new DOMSource(doc));
	        	        
	        System.out.println("[SchemaChecker:] Checking BPEL schema... successfull");	        
	    } catch (SAXException e) {
	    	System.out.println("");
	    	System.out.println(e.toString());
	    	System.out.println("");
			
			System.out.println("[SchemaChecker:] Checking BPEL schema... failed");
			return false;
		} catch (IOException e) {	
			System.out.println("");
			System.out.println(e.toString());
			System.out.println("");
			
			System.out.println("[SchemaChecker:] Checking BPEL schema... failed");
			return false;
		}
		
		System.out.println("[SchemaChecker:] Checking BPEL schema... done");
		return true;
	}
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {		
		// parse an XML document into a DOM tree
		
	    DocumentBuilder parser = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    Document document = null;
	    
		try {
			URL stream = SchemaChecker.class.getClassLoader().getSystemResource("de/vs/unikassel/solution_checker/Solution.bpel");
			document = parser.parse(stream.openStream());
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    // load a WXS schema, represented by a Schema instance
	    //Source schemaFile = new StreamSource(new File("bpel.xsd"));
	    Schema schema = SchemaChecker.getBPELSchema();		

	    SchemaChecker checker = new SchemaChecker();
	    checker.checkSchema(document, schema);
	}
}

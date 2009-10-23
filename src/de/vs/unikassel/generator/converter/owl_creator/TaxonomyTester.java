package de.vs.unikassel.generator.converter.owl_creator;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TaxonomyTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File ("taxonomy.xml");
		File file2 = new File ("Taxonomy.owl");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder dbuilder = null;
		Document doc = null;
		
        try {
            factory.setNamespaceAware(true);
            dbuilder = factory.newDocumentBuilder();

            doc = dbuilder.parse(file);
        } catch (Exception e) {        	
        }
        
        NodeList instances = doc.getElementsByTagName("instance");
        
        System.out.println("Instances: " + instances.getLength());
        
        int length = instances.getLength();
        
        String[] inames = new String[length];
                
        for (int i=0;i < length;i++) {
        	Element elem = (Element) instances.item(i);
        	
        	inames[i] = elem.getAttribute("name");
        }        	
        
        NodeList concepts = doc.getElementsByTagName("concept");
        
        System.out.println("Concepts: " + concepts.getLength());
        
        length = concepts.getLength();
        
        String[] cnames = new String[length];
                
        for (int i=0;i < length;i++) {
        	Element elem = (Element) concepts.item(i);
        	
        	cnames[i] = elem.getAttribute("name");
        }               
				
		Document doc2 = null;
		
        try {
            factory.setNamespaceAware(true);
            dbuilder = factory.newDocumentBuilder();

            doc2 = dbuilder.parse(file2);
        } catch (Exception e) {        	
        }
        
        NodeList owlinstances = doc2.getElementsByTagNameNS("http://www.w3.org/2002/07/owl#","Thing");
        
        //System.out.println(XMLUtils.DocumentToString(doc2));
        
        System.out.println("Owl-Instances: " + owlinstances.getLength());
        
        length = owlinstances.getLength();
        
        String[] owlinames = new String[length];
                
        for (int i=0;i < length;i++) {
        	Element elem = (Element) owlinstances.item(i);
        	
        	owlinames[i] = elem.getAttributeNS("http://www.w3.org/1999/02/22-rdf-syntax-ns#","ID");
        }       
        
        NodeList owlconcepts = doc2.getElementsByTagNameNS("http://www.w3.org/2002/07/owl#","Class");
        
        //System.out.println(XMLUtils.DocumentToString(doc2));
        
        System.out.println("Owl-Concepts: " + owlconcepts.getLength());
        
        length = owlconcepts.getLength();
        
        String[] owlcnames = new String[length];
                
        for (int i=0;i < length;i++) {
        	Element elem = (Element) owlconcepts.item(i);
        	
        	owlcnames[i] = elem.getAttributeNS("http://www.w3.org/1999/02/22-rdf-syntax-ns#","ID");
        }       
        
        for (int i=0;i < inames.length;i++) {
        	boolean found = false;
        	
        	for (int j=0;j < owlinames.length;j++) {
        		if (owlinames[j].compareTo(inames[i]) == 0) {
        			found = true;
        			break;
        		}
        	}
        	
        	if (found == false) {
        		System.out.println("Instance: " + inames[i] + " not found in OWL-File!");
        	}
        }
        
        for (int i=0;i < cnames.length;i++) {
        	boolean found = false;
        	
        	for (int j=0;j < owlcnames.length;j++) {
        		if (owlcnames[j].compareTo(cnames[i]) == 0) {
        			found = true;
        			break;
        		}
        	}
        	
        	if (found == false) {
        		System.out.println("Concept: " + cnames[i] + " not found in OWL-File!");
        	}
        }
        
        System.out.println("End");
	}

}

package de.vs.unikassel.generator.converter.wsla_creator;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WSLARepository {
	
	public static final String template = "de/vs/unikassel/generator/converter/wsla_creator/WSLATemplate.wsla";
	
	protected List<WSLATemplate> wsla = null;
	
	public WSLARepository() {
		this.wsla = new LinkedList<WSLATemplate>();
	}
	
	public WSLARepository(int size) {
		this.wsla = new ArrayList<WSLATemplate>(size);
	}

	public void add(int index, WSLATemplate element) {
		wsla.add(index, element);
	}

	public boolean add(WSLATemplate e) {
		return wsla.add(e);
	}

	public WSLATemplate get(int index) {
		return wsla.get(index);
	}

	public int size() {
		return wsla.size();
	}
	
	public static Document getWSLATemplate() {
		InputStream is = WSLATemplate.class.getClassLoader().getResourceAsStream(WSLARepository.template);
		return XMLUtils.DocumentFromStream(is);
	}
	
	public Document fillTemplate() {
		Document doc = WSLARepository.getWSLATemplate();
		
		NodeList elems = doc.getElementsByTagName("SLA");
		
		Element rootelement = (Element) elems.item(0);
		
		//Write ServiceDefinitions
		for(WSLATemplate template:this.wsla) {
			Document servicedefinition = template.fillServiceDefinition();
			
			NodeList childs = servicedefinition.getChildNodes();
			
			Node inode = childs.item(0);
			Node importednode = doc.importNode(inode,true);
			
			rootelement.appendChild(importednode);
		}
		
		Element obligationelement = doc.createElement("Obligations");
		
		rootelement.appendChild(obligationelement);
		
		//Write Obligations
		for(WSLATemplate template:this.wsla) {
			Document obligations = template.fillObligations();
			
			NodeList childs = obligations.getElementsByTagName("ServiceLevelObjective");
			
			for (int i = 0;i < childs.getLength();i++) {
				Node inode = childs.item(i);
				Node importednode = doc.importNode(inode,true);
				
				obligationelement.appendChild(importednode);
			}
		}
		
		return doc;
	}
	
	
	
	public static void main(String args[]) {
		System.out.println("start");
		
		WSLARepository repository = new WSLARepository(10000);
		
		for (int i=0;i < 1;i++) {
			WSLATemplate test = new WSLATemplate("Service"+i);
			
			test.setServicedefinitionname("ServiceDefinitionService"+i);
			test.setSlaparameternameresponsetime("SLAResponsetimeService"+i);
			test.setSlaparameternamethroughput("SLAThroughputService"+i);
			test.setSloparameternameresponsetime("SLOParameterService"+i);
			test.setSloparameternamethroughput("SLOParameterService"+i);
			test.setBindingname("WSCServiceBindingService"+i);
			test.setOperationname("WSCServiceOperationnameService"+i);
			test.setThroughput("1000");
			test.setResponsetime("200");
			
			repository.add(test);
		}
		
		//System.out.println(XMLUtils.DocumentToString(repository.fillTemplate()));
		
		XMLUtils.DocumentToFile(repository.fillTemplate(),new File("test.wsla"));
		
		System.out.println("ended");
	}
}

package de.vs.unikassel.generator.converter.wsla_creator;

import java.io.InputStream;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WSLATemplate {
	
	public static final String servicedefinition = "de/vs/unikassel/generator/converter/wsla_creator/ServiceDefinitionTemplate.wsla";
	
	public static final String obligations = "de/vs/unikassel/generator/converter/wsla_creator/ObligationsTemplate.wsla";
	
	protected String servicename;
	
	protected String operationname;
	
	protected String servicedefinitionname;
	
	protected String bindingname;
	
	protected String responsetime;
	
	protected String slaparameternameresponsetime;
	
	protected String sloparameternameresponsetime;
	
	protected String slaparameternamethroughput;
	
	protected String sloparameternamethroughput;
	
	protected String throughput;
	
	protected String responsetimeurl;
	
	protected String throughputurl;
	
	public WSLATemplate(String servicename) {
		this.servicename = servicename;
		this.responsetimeurl = "http://192.168.143.245:7438/ResponseTime";
		this.throughputurl = "http://192.168.143.245:7438/Throughput";
	}
	
	public String getOperationname() {
		return operationname;
	}

	public void setOperationname(String operationname) {
		this.operationname = operationname;
	}

	public String getBindingname() {
		return bindingname;
	}

	public void setBindingname(String bindingname) {
		this.bindingname = bindingname;
	}

	public String getResponsetime() {
		return responsetime;
	}

	public void setResponsetime(String responsetime) {
		this.responsetime = responsetime;
	}

	public String getThroughput() {
		return throughput;
	}

	public void setThroughput(String throughput) {
		this.throughput = throughput;
	}

	public String getResponsetimeurl() {
		return responsetimeurl;
	}

	public void setResponsetimeurl(String responsetimeurl) {
		this.responsetimeurl = responsetimeurl;
	}

	public String getThroughputurl() {
		return throughputurl;
	}

	public void setThroughputurl(String throughputurl) {
		this.throughputurl = throughputurl;
	}
	
	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	
	public String getServicedefinitionname() {
		return servicedefinitionname;
	}

	public void setServicedefinitionname(String servicedefinitionname) {
		this.servicedefinitionname = servicedefinitionname;
	}
	
	public String getSlaparameternameresponsetime() {
		return slaparameternameresponsetime;
	}

	public void setSlaparameternameresponsetime(String slaparameternameresponsetime) {
		this.slaparameternameresponsetime = slaparameternameresponsetime;
	}

	public String getSlaparameternamethroughput() {
		return slaparameternamethroughput;
	}

	public void setSlaparameternamethroughput(String slaparameternamethroughput) {
		this.slaparameternamethroughput = slaparameternamethroughput;
	}
	
	public String getSloparameternameresponsetime() {
		return sloparameternameresponsetime;
	}

	public void setSloparameternameresponsetime(String sloparameternameresponsetime) {
		this.sloparameternameresponsetime = sloparameternameresponsetime;
	}

	public String getSloparameternamethroughput() {
		return sloparameternamethroughput;
	}

	public void setSloparameternamethroughput(String sloparameternamethroughput) {
		this.sloparameternamethroughput = sloparameternamethroughput;
	}
	
	public Document fillServiceDefinition() {
		Document doc = WSLATemplate.getServiceDefinition();
		
		//writing SoapBindingName
		NodeList elems = doc.getElementsByTagName("SOAPBindingName");
		
		Element elem = (Element) elems.item(0);
		
		NodeList childs = elem.getChildNodes();
		
		Node node = childs.item(0);
		
		//System.out.println(elem.getLocalName());
		//System.out.println(node.getNodeValue());
		node.setNodeValue(this.getBindingname());
		
		//writing Servicedefinitionname
		elems = doc.getElementsByTagName("ServiceDefinition");
		
		elem = (Element) elems.item(0);
		
		Attr nameattribute = elem.getAttributeNode("name");
		
		nameattribute.setNodeValue(this.getServicedefinitionname());
		
		//writing SOAPOperationName
		elems = doc.getElementsByTagName("SOAPOperationName");
		
		elem = (Element) elems.item(0);
		
		childs = elem.getChildNodes();
		
		node = childs.item(0);
		
		//System.out.println(elem.getLocalName());
		//System.out.println(node.getNodeValue());
		node.setNodeValue(this.getOperationname());
		
		//writing measurement urls
		elems = doc.getElementsByTagName("SourceURI");
		
		Element responsetimeurl = (Element) elems.item(0);
		Element throughputurl = (Element) elems.item(1);
		
		childs = responsetimeurl.getChildNodes();
		
		Node responsetimeurlvalue = childs.item(0);
		
		//System.out.println(responsetimeurl.getLocalName());
		//System.out.println(responsetimeurlvalue.getNodeValue());
		responsetimeurlvalue.setNodeValue(this.getResponsetimeurl());
		
		childs = throughputurl.getChildNodes();
		
		Node throughputurlvalue = childs.item(0);
		
		//System.out.println(throughputurl.getLocalName());
		//System.out.println(throughputurlvalue.getNodeValue());
		throughputurlvalue.setNodeValue(this.getThroughputurl());
		
		//Set Slaparameternames (1) Response Time (2) Throughput
		elems = doc.getElementsByTagName("SLAParameter");
		
		elem = (Element) elems.item(0);
		
		nameattribute = elem.getAttributeNode("name");
		
		nameattribute.setNodeValue(this.getSlaparameternameresponsetime());
		
		elem = (Element) elems.item(1);
		
		nameattribute = elem.getAttributeNode("name");
		
		nameattribute.setNodeValue(this.getSlaparameternamethroughput());
		
		return doc;
	}
	
	public void extractServiceDefinition(final Document doc) {
		//extracting SoapBindingName
		NodeList elems = doc.getElementsByTagName("SOAPBindingName");
		
		Element elem = (Element) elems.item(0);
		
		NodeList childs = elem.getChildNodes();
		
		Node node = childs.item(0);
		
		this.bindingname = node.getNodeValue();
		//System.out.println(elem.getLocalName());
		//System.out.println(node.getNodeValue());
		
		//extracting Servicedefinitionname
		elems = doc.getElementsByTagName("ServiceDefinition");
		
		elem = (Element) elems.item(0);
		
		Attr nameattribute = elem.getAttributeNode("name");
		
		this.setServicedefinitionname(nameattribute.getNodeValue());
		
		//extracting SOAPOperationName
		elems = doc.getElementsByTagName("SOAPOperationName");
		
		elem = (Element) elems.item(0);
		
		childs = elem.getChildNodes();
		
		node = childs.item(0);
		
		this.operationname = node.getNodeValue();
		//System.out.println(elem.getLocalName());
		//System.out.println(node.getNodeValue());
		
		//extracting measurement urls
		elems = doc.getElementsByTagName("SourceURI");
		
		Element responsetimeurl = (Element) elems.item(0);
		Element throughputurl = (Element) elems.item(1);
		
		childs = responsetimeurl.getChildNodes();
		
		Node responsetimeurlvalue = childs.item(0);
		
		this.setResponsetimeurl(responsetimeurlvalue.getNodeValue());
		//System.out.println(responsetimeurl.getLocalName());
		//System.out.println(responsetimeurlvalue.getNodeValue());
		
		childs = throughputurl.getChildNodes();
		
		Node throughputurlvalue = childs.item(0);
		
		this.setThroughputurl(throughputurlvalue.getNodeValue());
		//System.out.println(throughputurl.getLocalName());
		//System.out.println(throughputurlvalue.getNodeValue());
		
		//Set Slaparameternames (1) Response Time (2) Throughput
		elems = doc.getElementsByTagName("SLAParameter");
		
		elem = (Element) elems.item(0);
		
		nameattribute = elem.getAttributeNode("name");
		
		this.setSlaparameternameresponsetime(nameattribute.getNodeValue());
		
		elem = (Element) elems.item(1);
		
		nameattribute = elem.getAttributeNode("name");
		
		this.setSlaparameternamethroughput(nameattribute.getNodeValue());
	}
	
	public Document fillObligations() {
		Document doc = WSLATemplate.getObligations();
		//writing values
		NodeList elems = doc.getElementsByTagName("Value");
		
		Element responsetime = (Element) elems.item(0);
		Element throughput = (Element) elems.item(1);
		
		NodeList childs = responsetime.getChildNodes();
		
		Node responsetimevalue = childs.item(0);
		
		//System.out.println(responsetime.getLocalName());
		//System.out.println(responsetimevalue.getNodeValue());
		responsetimevalue.setNodeValue(this.getResponsetime());
		
		childs = throughput.getChildNodes();
		
		Node throughputvalue = childs.item(0);
		
		//System.out.println(throughput.getLocalName());
		//System.out.println(throughputvalue.getNodeValue());
		throughputvalue.setNodeValue(this.getThroughput());
		
		//Set Sloparameternames (1) Response Time (2) Throughput
		elems = doc.getElementsByTagName("ServiceLevelObjective");
		
		Element elem = (Element) elems.item(0);
		
		Attr nameattribute = elem.getAttributeNode("name");
		
		nameattribute.setNodeValue(this.getSloparameternameresponsetime());
		
		elem = (Element) elems.item(1);
		
		nameattribute = elem.getAttributeNode("name");
		
		nameattribute.setNodeValue(this.getSloparameternamethroughput());
		
		//Set Slaparameternames (1) Response Time (2) Throughput
		elems = doc.getElementsByTagName("SLAParameter");
		
		responsetime = (Element) elems.item(0);
		throughput = (Element) elems.item(1);
		
		childs = responsetime.getChildNodes();
		
		responsetimevalue = childs.item(0);
		
		//System.out.println(responsetime.getLocalName());
		//System.out.println(responsetimevalue.getNodeValue());
		responsetimevalue.setNodeValue(this.getSlaparameternameresponsetime());
		
		childs = throughput.getChildNodes();
		
		throughputvalue = childs.item(0);
		
		//System.out.println(throughput.getLocalName());
		//System.out.println(throughputvalue.getNodeValue());
		throughputvalue.setNodeValue(this.getSlaparameternamethroughput());
		
		return doc;
	}
	
	public void extractObligations(final Document doc) {
		//extracting values
		NodeList elems = doc.getElementsByTagName("Value");
		
		Element responsetime = (Element) elems.item(0);
		Element throughput = (Element) elems.item(1);
		
		NodeList childs = responsetime.getChildNodes();
		
		Node responsetimevalue = childs.item(0);
		
		this.responsetime = responsetimevalue.getNodeValue();
		//System.out.println(responsetime.getLocalName());
		//System.out.println(responsetimevalue.getNodeValue());
		
		childs = throughput.getChildNodes();
		
		Node throughputvalue = childs.item(0);
		
		this.throughput = throughputvalue.getNodeValue();
		//System.out.println(throughput.getLocalName());
		//System.out.println(throughputvalue.getNodeValue());
		
		//Extract Sloparameternames (1) Response Time (2) Throughput
		elems = doc.getElementsByTagName("ServiceLevelObjective");
		
		Element elem = (Element) elems.item(0);
		
		Attr nameattribute = elem.getAttributeNode("name");
		
		this.setSloparameternameresponsetime(nameattribute.getNodeValue());
		
		elem = (Element) elems.item(1);
		
		nameattribute = elem.getAttributeNode("name");
		
		this.setSloparameternamethroughput(nameattribute.getNodeValue());
	}
	
	public static Document getServiceDefinition() {
		InputStream is = WSLATemplate.class.getClassLoader().getResourceAsStream(WSLATemplate.servicedefinition);
		return XMLUtils.DocumentFromStream(is);
	}
	
	public static Document getObligations() {
		InputStream is = WSLATemplate.class.getClassLoader().getResourceAsStream(WSLATemplate.obligations);
		return XMLUtils.DocumentFromStream(is);
	}
	
	public static void main(String args[]) {
		System.out.println("start");
		
		WSLATemplate test = new WSLATemplate("ServiceA123456");
		
		test.setServicedefinitionname("ServiceDefinitionServiceA123456");
		test.setSlaparameternameresponsetime("SLAResponsetimeServiceA123456");
		test.setSlaparameternamethroughput("SLAThroughputServiceA123456");
		test.setSloparameternameresponsetime("SLOParameterServiceA123456");
		test.setSloparameternamethroughput("SLOParameterServiceA123456");
		test.setBindingname("WSCServiceBindingServiceA123456");
		test.setOperationname("WSCServiceOperationnameServiceA123456");
		test.setThroughput("1000");
		test.setResponsetime("200");
		
		System.out.println(XMLUtils.DocumentToString(test.fillServiceDefinition()));
		
		test.extractServiceDefinition(test.fillServiceDefinition());
		
		System.out.println(XMLUtils.DocumentToString(test.fillObligations()));
		
		test.extractObligations(test.fillObligations());
		
		System.out.println("ended");
	}

	

	

	
}

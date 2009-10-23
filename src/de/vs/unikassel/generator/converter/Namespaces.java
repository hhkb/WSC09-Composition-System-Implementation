package de.vs.unikassel.generator.converter;

import org.jdom.Namespace;

public interface Namespaces {
	public Namespace wsdlNamespace = Namespace.getNamespace("","http://schemas.xmlsoap.org/wsdl/");
	public Namespace soapNamespace = Namespace.getNamespace("soap","http://schemas.xmlsoap.org/wsdl/soap/");
	public Namespace httpNamespace = Namespace.getNamespace("http","http://schemas.xmlsoap.org/wsdl/http/");
	public Namespace xsdNamespace = Namespace.getNamespace("xs","http://www.w3.org/2001/XMLSchema");
	public Namespace soapencNamespace = Namespace.getNamespace("soapenc","http://schemas.xmlsoap.org/soap/encoding/");
	public Namespace mimeNamespace = Namespace.getNamespace("mime","http://schemas.xmlsoap.org/wsdl/mime/");
	public Namespace meceNamespace = Namespace.getNamespace("mece", "http://www.vs.uni-kassel.de/mece");
	public Namespace serviceNamespace = Namespace.getNamespace("service","http://www.ws-challenge.org/WSC08Services/");
	public Namespace bpelNamespace = Namespace.getNamespace("bpel","http://schemas.xmlsoap.org/ws/2003/03/business-process/");
}

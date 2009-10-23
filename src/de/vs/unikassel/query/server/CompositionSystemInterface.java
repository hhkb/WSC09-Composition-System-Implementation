package de.vs.unikassel.query.server;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.WebParam;;

/**
 * This web-service-interface must be implemented by each Composition-System.
 * @author Marc Kirchhoff
 *
 */
@WebService(name = "CompositionSystem")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class CompositionSystemInterface {
	
	/**
	 * This method is called by the WSC-Client to initialize the Composition-System.
	 * @param wsdlServiceDescriptionsURL The URL of the WSDL-Service-Descriptions-document.
	 * @param owlTaxonomyURL The URL of the OWL-taxonomy-document.	
	 */
	@WebMethod
	@Oneway
	public void initialize( 
			@WebParam(name = "wsdlServiceDescriptionsURL") String wsdlServiceDescriptionsURL, 
			@WebParam(name = "owlTaxonomyURL") String owlTaxonomyURL,
			@WebParam(name = "wslaSLAgreementsURL") String wslaSLAgreementsURL) {
		
	}
	
	/**
	 * This method is called by the WSC-Client to start the query.
	 * @param wsdlQuery The WSDL-Query-document as a string.
	 * @param callbackURL The callback-URL of the WSC-Client.
	 */
	@WebMethod
	@Oneway
	public void startQuery(
			@WebParam(name = "wsdlQuery") String wsdlQuery, 
			@WebParam(name = "callbackURL") String callbackURL) {
		
	}
	
	/**
	 * This method is called by the WSC-Client to stop the composition.
	 * @return "true" if Composition-System stopped successfully otherwise "false".
	 */
	@WebMethod
	public boolean stopComposition() {
		return false;
	}
}

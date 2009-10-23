package de.vs.unikassel.query.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import de.vs.unikassel.query.client.callback_interface.stub.CallbackService;
import de.vs.unikassel.query.client.callback_interface.stub.ClientCallbackInterfaceService;

/**
 * This is test-implementation of a Composition-System.
 * @author Marc Kirchhoff
 *
 */
@WebService(name = "CompositionSystem")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class CompositionSystemImpl extends CompositionSystemInterface {
	
	/**
	 * Indicates if the composition was stopped.
	 */
	private boolean stopped;
		
	/**
	 * Creates a new Composition-System.
	 */
	public CompositionSystemImpl() {
		// Do nothing.
	}
	
	/**
	 * Reads the file and returns the content of the file as a string.
	 * @param fileURL The URL of the file.
	 * @return The content of the file.
	 */
	private void readFile(String fileURL) {
		
		try {
			URL source = new URL(fileURL);
			BufferedReader bufferedFileReader = new BufferedReader(new InputStreamReader(source.openStream()));			
			String line = null;		
		
			while((line = bufferedFileReader.readLine()) != null) {
				System.out.println(line);
			}
			
			bufferedFileReader.close();			
			
		} catch (Exception exception) {			
			exception.printStackTrace();			
		}	
	}

	/**
	 * This method is called by the WSC-Client to initialize the Composition-System.
	 * @param wsdlServiceDescriptionsURL The URL of the WSDL-Service-Descriptions-document.
	 * @param owlTaxonomyURL The URL of the OWL-taxonomy-document.	
	 */
	@Override
	@WebMethod
	@Oneway
	public void initialize(@WebParam(name = "wsdlServiceDescriptionsURL") String wsdlServiceDescriptionsURL, 
			@WebParam(name = "owlTaxonomyURL") String owlTaxonomyURL,
			@WebParam(name = "wslaSLAgreementsURL") String wslaSLAgreementsURL) {
		
		// Print the given documents.
		System.out.println("WSDL-Service-Descriptions:");
		readFile(wsdlServiceDescriptionsURL);
		
		System.out.println();
		System.out.println("OWL-Taxonomy:");
		readFile(owlTaxonomyURL);		
		
		System.out.println();
		System.out.println("WSLA-Agreements:");
		readFile(wslaSLAgreementsURL);		
	}

	/**
	 * This method is called by the WSC-Client to start the query.
	 * @param wsdlQuery The WSDL-Query-document.
	 * @param callbackURL The callback-URL of the WSC-Client.
	 */
	@Override
	@WebMethod
	@Oneway
	public void startQuery(@WebParam(name = "wsdlQuery") String wsdlQuery, 
			@WebParam(name = "callbackURL") String callbackURL) {
		
		this.stopped = false;
		
		System.out.println("WSDL-Query:");
		System.out.println(wsdlQuery);
		
		System.out.println("Callback-URL: "+callbackURL);
		
		// Do some calculations and create the BPEL-document.
		try {
			Thread.sleep(1000);
		} catch (InterruptedException exception) {			
			exception.printStackTrace();
		}
		
		// Read a test-bpel-document.
		String testBPELResultDocumentPath = "de/vs/unikassel/query/server/TestSolution.bpel";
		BufferedReader bpelDocumentReader = new BufferedReader(new InputStreamReader(CompositionSystemImpl.class.getClassLoader().getResourceAsStream(testBPELResultDocumentPath)));
		StringBuffer bpelDocument = new StringBuffer();
		String line = null;
		
		try {
			while((line = bpelDocumentReader.readLine()) != null) {
				bpelDocument.append(line);
				bpelDocument.append("\n");
			}
		} catch (IOException exception) {			
			exception.printStackTrace();
			return;
		}	
		
		// Call the callback-interface.
		if(!this.stopped) {
			try {
				CallbackService callbackService = new ClientCallbackInterfaceService().getCallbackServicePort();
				setURL(callbackURL,callbackService);
				callbackService.result(bpelDocument.toString());
			}
			catch(Exception exception) {
				exception.printStackTrace();
			}
		}		
	}

	/**
	 * This method is called by the WSC-Client to stop the composition.
	 * @return "true" if Composition-System stopped successfully otherwise "false".
	 */
	@Override
	@WebMethod
	public boolean stopComposition() {
		System.out.println("Stop composition");
		this.stopped = true;
		
		return true;
	}
	
	/**
	 * A Web Service stub is instantiated with the default URL of the 
	 * related WSDL document. This is a convenient method to change the target URL of the
	 * Web Service stub.
	 * 
	 * @param url The URL of the Web Service.
	 * @param JaxWS The JAX WS stub instance.
	 * @return The actualized Web Service stub instance.
	 */
	public static Object setURL(String url,Object JaxWS) {
		return ((BindingProvider)JaxWS).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,url);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Start the service.
		String serviceURL = "http://localhost:8080/CompositionSystem";
		CompositionSystemImpl compositionSystemImpl = new CompositionSystemImpl();
				
		Endpoint serviceEndpoint = Endpoint.publish(serviceURL, compositionSystemImpl);
		System.out.println("Service started at "+serviceURL);
		
		System.out.println("Press any key to stop the service");
		
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(System.in);
			inputStreamReader.read();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		
		serviceEndpoint.stop();
		System.out.println("Service stopped.");
	}
}

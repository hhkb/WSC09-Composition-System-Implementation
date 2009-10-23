package de.vs.unikassel.query.client.callback_interface;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import de.vs.unikassel.query.client.gui.ResultDialog;
import de.vs.unikassel.query.client.gui.WSCClient;

/**
 * This is the callback-interface that can be used by a Composition-System to send the result to the client-application.
 * @author Marc Kirchhoff
 *
 */
@WebService(name = "CallbackService")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class ClientCallbackInterface extends CallBack {
	
	
	public ClientCallbackInterface() {		
	}
	
	/**
	 * Creates a new callback-service that can be used by the Composition-System to send the result.
	 * @param progressDialog
	 */
	public ClientCallbackInterface(WSCClient wscClient) {
		this.setWscClient(wscClient);
		this.setProgressDialog(null);
		this.setWaitingForResult(true);
		this.setStartTimeLastQuery(-1);
		this.setEndTimeLastQuery(-1);		
	}
	
	/**
	 * This method can be called by the Composition-System to send the result.
	 * @param bpelDocument The result-BPEL-document.
	 */
	@WebMethod	 
	public void result(@WebParam(name = "bpelDocument") String bpelDocument) {
		
		this.setEndTimeLastQuery((new Date()).getTime());
		this.setWaitingForResult(false);		
		
		System.out.println("Document: " + bpelDocument);
		
		String debugText = this.getProgressDialog().getSystemOutTextArea2().getTextbox().getText();
		
		ResultDialog resultDialog = new ResultDialog(this.getWscClient(), bpelDocument, debugText, getDurationLastQuery());
		
		// Close the progress-dialog.
		this.getProgressDialog().dispose();
		
		resultDialog.setVisible(true);	
	}

	
}

package de.vs.unikassel.query.client.gui.listener;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import org.sfc.io.loaders.StreamLoader;

import de.vs.unikassel.query.client.callback_interface.ClientCallbackInterface;
import de.vs.unikassel.query.client.gui.ProgressDialog;
import de.vs.unikassel.query.client.gui.WSCClient;
import de.vs.unikassel.query.server.stub.CompositionSystem;
import de.vs.unikassel.query.server.stub.CompositionSystemInterfaceService;

/**
 * Handles the buttons of the "WSCClient".
 * @author Marc Kirchhoff
 *
 */
public class WSCClientButtonListener implements ActionListener{
	
	/**
	 * The "WSCClient"-frame.
	 */
	private WSCClient wscClient;
	
	/**
	 * The endpoint for the callback-service.
	 */
	private Endpoint callbackService;
	
	/**
	 * The URL of the callback-service.
	 */
	private String callbackServiceURL;
	
	/**
	 * The QName of the Composition-System.
	 */
	public static QName compositionSystemServiceName = new QName("http://server.query.unikassel.vs.de/", "CompositionSystemImplService");
	
	/**
	 * The path of the info-file.
	 */
	private static String INFO_FILE_PATH = "de/vs/unikassel/query/client/gui/listener/infos.txt";
	
	/**
	 * The start-time of a query.
	 */
	private long startTime;
	
	/**
	 * The file chooser remembers his last directory.
	 */
	private File lastdir = null;
	
	/**
	 * Creates a new listener with the given "WSCClient".
	 * @param wscClient The "WSCClient"-frame.
	 */
	public WSCClientButtonListener(WSCClient wscClient) {
		this.wscClient = wscClient;
		this.callbackService = null;
		this.callbackServiceURL = null;
		this.startTime = -1;
	}
	
	/**
	 * Starts a file-chooser and returns the URL of the selected file.
	 * @param fileExtension The extension of the files the file-chooser should display.
	 * @param description The description of the file-chooser.
	 * @return The URL of the file as a String.
	 */
	private String browseFile(final String fileExtension, final String description) {
		// Create a file-chooser.
		JFileChooser fileChooser=new JFileChooser();
		
		// Restrict the selection to files.
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		// Set the file-filter.
		fileChooser.setFileFilter(new FileFilter() {	
			
			@Override
			public boolean accept(File file) {
				if(file.isDirectory() || file.getName().endsWith(fileExtension)) {
					return true;
				}
				else {
					return false;
				}
			}

			@Override
			public String getDescription() {
				return description;					
			}
		});
		
		//Check for last directory
		if (this.lastdir != null) {			
			fileChooser.setCurrentDirectory(this.lastdir);
		}
		
		// Display the file-chooser.
		int option = fileChooser.showOpenDialog(this.wscClient);
		
		
		// Check the return-status.
		if(option == JFileChooser.APPROVE_OPTION) {
			String fileURL = null;
			
			// Remember directory
			this.lastdir = new File(fileChooser.getSelectedFile().getParent());			
			
			// Convert the path of the file into an URL.
			try {
				fileURL = fileChooser.getSelectedFile().toURI().toURL().toExternalForm();				
			} catch (MalformedURLException exception) {				
				exception.printStackTrace();
				JOptionPane.showMessageDialog(this.wscClient, "An error occurred during the creation of file file-URL!\n See debug-window for more informations.","Incorrect Path",JOptionPane.ERROR_MESSAGE);
				return null;
			}
			
			return fileURL;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Handles the "Browse-OWL-Taxonomy"-button.
	 * This function starts a file-chooser.
	 */
	private void browseOWLTaxonomyButton() {
		String owlFileURL = browseFile("owl", "OWL-Files and Folders");
		if(owlFileURL != null) {
			this.wscClient.getOwlTaxonomyURLjTextField().setText(owlFileURL);
		}
	}
	
	/**
	 * Handles the "Browse-WSDL-Service-Descriptions"-button.
	 * This function starts a file-chooser.
	 */
	private void browseWSDLServiceDescriptionsButton() {
		String wsdlServiceDescriptionsFileURL = browseFile("wsdl", "WSDL-Files and Folders");
		if(wsdlServiceDescriptionsFileURL != null) {
			this.wscClient.getWsdlServiceDescriptionsURLjTextField().setText(wsdlServiceDescriptionsFileURL);
		}
	}
	
	/**
	 * Handles the "Browse WSLA"-button.
	 * This function starts a file chooser.
	 */
	private void browseWSLAButton() {
		String wslaFileURL = browseFile("wsla", "WSLA-Files and Folders");
		if(wslaFileURL != null) {
			this.wscClient.getWslaURLjTextField().setText(wslaFileURL);
		}
	}
	
	/**
	 * Handles the "Browse-WSDL-Service-Descriptions"-button.
	 * This function starts a file-chooser.
	 */
	private void browseWSDLQueryButton() {
		String wsdlQueryFileURL = browseFile("wsdl", "WSDL-Files and Folders");
		if(wsdlQueryFileURL != null) {
			this.wscClient.getWsdlQueryjTextField().setText(wsdlQueryFileURL);
		}
	}
	
	/**
	 * Handles the "Initialize"-button.
	 * Reads the OWL-taxonomy-file and the Service-Descrptions-WSDL-file and send them to the composition-system.
	 */
	private void initializeButton() {
		
		// Read the URL of the Composition-System.
		String compositionSystemURL = this.wscClient.getCompositionSystemURLjTextField().getText().trim();
		
		if(compositionSystemURL == null || compositionSystemURL.length() == 0) {
			JOptionPane.showMessageDialog(this.wscClient, "Please enter the URL of the Composition-System!","Missing Composition-System-URL",JOptionPane.ERROR_MESSAGE);
			this.wscClient.getCompositionSystemURLjTextField().requestFocus();
			return;
		}
		
		// Read the URL of the OWL-Taxonomy-file.
		String owlTaxonomyURL = this.wscClient.getOwlTaxonomyURLjTextField().getText().trim();
		
		if(owlTaxonomyURL == null || owlTaxonomyURL.length() == 0) {
			JOptionPane.showMessageDialog(this.wscClient, "Please enter the URL of the OWL-Taxonomy!","Missing OWL-Taxonomy",JOptionPane.ERROR_MESSAGE);
			this.wscClient.getOwlTaxonomyURLjTextField().requestFocus();
			return;
		}				
		
		try {
			new URL(owlTaxonomyURL);
		} catch (Exception exception) {			
			System.err.println("[WSCClientButtonListener]: Incorrect OWL-Taxonomy-URL");
			exception.printStackTrace();
			JOptionPane.showMessageDialog(this.wscClient, "The initialization went wrong. \n See Debug-Box for more informations.","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}				
		
		// Read the URL of the WSDL-Service-Description-file.
		String wsdlServiceDescriptionsURL = this.wscClient.getWsdlServiceDescriptionsURLjTextField().getText().trim();
		
		if(wsdlServiceDescriptionsURL == null || wsdlServiceDescriptionsURL.length() == 0) {
			JOptionPane.showMessageDialog(this.wscClient, "Please enter the URL of the WSDL-Service-Descriptions!","Missing WSDL-Service-Descriptions",JOptionPane.ERROR_MESSAGE);
			this.wscClient.getOwlTaxonomyURLjTextField().requestFocus();
			return;
		}				
		
		try {
			new URL(wsdlServiceDescriptionsURL);
		} catch (Exception exception) {
			System.err.println("[WSCClientButtonListener]: Incorrect WSDL-Service-Descriptions-URL");
			exception.printStackTrace();
			JOptionPane.showMessageDialog(this.wscClient, "The initialization went wrong. \n See Debug-Box for more informations.","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}				
		
		// Read the URL of the WSLA-Agreements-Description-file.
		String wslaAgreementsURL = this.wscClient.getWslaURLjTextField().getText().trim();
		
		if(wslaAgreementsURL == null || wslaAgreementsURL.length() == 0) {
			JOptionPane.showMessageDialog(this.wscClient, "Please enter the URL of the WSLA-Agreement-Descriptions!","Missing WSLA-Agreements-Descriptions",JOptionPane.ERROR_MESSAGE);
			this.wscClient.getWslaURLjTextField().requestFocus();
			return;
		}
		
		try {
			new URL(wslaAgreementsURL);
		} catch (Exception exception) {
			System.err.println("[WSCClientButtonListener]: Incorrect WSLA-Agreement-Descriptions-URL");
			exception.printStackTrace();
			JOptionPane.showMessageDialog(this.wscClient, "The initialization went wrong. \n See Debug-Box for more informations.","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}				
		
		System.out.println("[WSCClientButtonListener]: Call Composition-System.");
		
		// Call the service.			
		CompositionSystem stub = new CompositionSystemInterfaceService().getCompositionSystemPort();
		WSCClientButtonListener.setURL(compositionSystemURL,stub);
		
		stub.initialize(wsdlServiceDescriptionsURL, owlTaxonomyURL, wslaAgreementsURL);
		
		JOptionPane.showMessageDialog(this.wscClient, "The initialization started.","Initialization started",JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Handles the "Start"-button.
	 * Sends the Query-WSDL-document to the Composition-System.
	 */
	public void sendQueryButton() {
		
		// Read the URL of the Composition-System.
		String compositionSystemURL = this.wscClient.getCompositionSystemURLjTextField().getText().trim();
		
		if(compositionSystemURL == null || compositionSystemURL.length() == 0) {
			JOptionPane.showMessageDialog(this.wscClient, "Please enter the URL of the Composition-System!","Missing Composition-System-URL",JOptionPane.ERROR_MESSAGE);
			this.wscClient.getCompositionSystemURLjTextField().requestFocus();
			return;
		}
		
		// Read the WSDL-Query-file-URL.
		String wsdlQueryURL = this.wscClient.getWsdlQueryjTextField().getText().trim();
		
		if(wsdlQueryURL == null || wsdlQueryURL.length() == 0) {
			JOptionPane.showMessageDialog(this.wscClient, "Please enter the URL of the WSDL-Query!","Missing WSDL-Query-File",JOptionPane.ERROR_MESSAGE);
			this.wscClient.getWsdlQueryjTextField().requestFocus();
			return;
		}				
		
		URL wsdlQueryFile = null;
		
		try {
			wsdlQueryFile = new URL(wsdlQueryURL);
		} catch (Exception exception) {
			System.err.println("[WSCClientButtonListener]: Incorrect WSDL-Query-URL");
			exception.printStackTrace();
			JOptionPane.showMessageDialog(this.wscClient, "The starting went wrong. \n See Debug-Box for more informations.","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}				
		
		// Is the callback-service published?
		if(this.callbackService == null || !this.callbackService.isPublished() || this.callbackServiceURL == null) {
			String callbackURL = this.wscClient.getCallbackURLjTextField().getText().trim();
			if(callbackURL != null && !(callbackURL.length() == 0)) {
				int result = JOptionPane.showConfirmDialog(this.wscClient, "Start the Callback-Service at "+callbackURL+"?","Start Callback-Service", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(result == JOptionPane.YES_OPTION) {
					startCallbackService(callbackURL);
				}
				else {
					JOptionPane.showMessageDialog(this.wscClient, "The Callback-Service must be started to send the Query!","Callback-Service isn't published",JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			else {
				JOptionPane.showMessageDialog(this.wscClient, "Please enter the Callback-URL and start the Callback-Service first!","Missing Callback-URL",JOptionPane.ERROR_MESSAGE);
				this.wscClient.getCallbackURLjTextField().requestFocus();
				return;
			}
		}
		
		URL compositionSystemWSDLURL = null;
		
		try {
			compositionSystemWSDLURL = new URL(compositionSystemURL + "?wsdl");
		} catch (MalformedURLException exception) {			
			System.err.println("[WSCClientButtonListener]: Incorrect Composition-System-WSDL-URL.");
			exception.printStackTrace();
			JOptionPane.showMessageDialog(this.wscClient, "The starting went wrong. \n See Debug-Box for more informations.","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Read the WSDL-Query-file.
		String wsdlQueryFileContent = readFile(wsdlQueryFile);		
		
		if(wsdlQueryFileContent == null) {
			JOptionPane.showMessageDialog(this.wscClient, "An error occurred during the reading of the Query-file. \n See Debug-Box for more informations.","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		System.out.println("[WSCClientButtonListener]: Send query to the Composition-System.");
		
		// Send the query to the Composition-System.
		CompositionSystem stub = new CompositionSystemInterfaceService().getCompositionSystemPort();
		this.startTime = (new Date()).getTime();
		WSCClientButtonListener.setURL(compositionSystemURL,stub);
		
		try {
			stub.startQuery(wsdlQueryFileContent, this.callbackServiceURL);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[WSCClientButtonListener]: Could not invoke composition system server.");
			return;
		}
		
		// Set the starting-time.
		ClientCallbackInterface callbackInterface = (ClientCallbackInterface)this.callbackService.getImplementor();
		callbackInterface.setStartTimeLastQuery(this.startTime);
		callbackInterface.setWaitingForResult(true);
		
		// Show the progress-dialog.
		ProgressDialog progressDialog = new ProgressDialog(this.wscClient, compositionSystemWSDLURL);
		progressDialog.setClientCallbackInterface(callbackInterface);
		callbackInterface.setProgressDialog(progressDialog);
		progressDialog.setVisible(true);
	}
	
	/**
	 * Reads a file and returns the content of the file as a string.
	 * @param file The file.
	 * @return The content of the file as a string or "null" if an error occurred during the reading of the file.
	 */
	private String readFile(URL file) {
		
		System.out.println("[WSCClientButtonListener]: Start reading file at " + file.toExternalForm());
		
		try {
			InputStream stream = file.openStream();
			StreamLoader s;
            
	        s = new StreamLoader();
	        s.load(stream);        
	        
	        System.out.println("[WSCClientButtonListener]: The reading of the file at "+ file.toExternalForm() +" concluded successfully.");
	        
	        byte[] data = new byte[s.getLength()];
	        
	        System.arraycopy(s.getData(), 0, data, 0, s.getLength());
	        
	        return new String(data);
		} catch (Exception exception) {
			System.err.println("[WSCClientButtonListener]: An error occurred during the reading of file.");
			System.err.println("[WSCClientButtonListener]: URL: " + file.toExternalForm());
			exception.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Handles the "Info"-button.
	 * Displays some informations about us.
	 */
	private void infoButton() {
		// Read info-file.
		BufferedReader infoFileReader = new BufferedReader(new InputStreamReader(WSCClientButtonListener.class.getClassLoader().getResourceAsStream(WSCClientButtonListener.INFO_FILE_PATH)));
		StringBuffer infoFileText = new StringBuffer();
		String line = null;
		
		try {
			while((line = infoFileReader.readLine()) != null) {
				infoFileText.append(line);
				infoFileText.append("\n");
			}
		} catch (IOException exception) {
			System.err.println("[WSCClientButtonListener]: An error occurred during the reading of the info-file at "+WSCClientButtonListener.INFO_FILE_PATH);
			exception.printStackTrace();
			return;
		}
		
		JOptionPane.showMessageDialog(this.wscClient, infoFileText.toString(),"Web Service Challenge 2008 Client-GUI",JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * This method handles the "WSDL"-button of the "Query"-frame.
	 * It displays the WSDL-document of the callback-interface in the default-browser.
	 */
	private void showWSDLCallbackButton() {
		try {
			if(this.callbackService != null && this.callbackService.isPublished() && this.callbackServiceURL != null) {
				Desktop.getDesktop().browse(new URI(this.callbackServiceURL+"?wsdl"));
			}
			
		} catch (Exception exception) {
			System.err.println("[WSCClientButtonListener]: The WSDL-document of the callback-interface couldn't be displayed.");
			exception.printStackTrace();
			JOptionPane.showMessageDialog(this.wscClient, "The WSDL-document of the callback-interface couldn't be displayed. \n See Debug-Box for more informations.","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	/**
	 * Starts the callback-service with the given URL.
	 * @param callbackURL The URL of the callback-service.
	 */
	private void startCallbackService(String callbackURL) {
		// Start the callback-service.	
		ClientCallbackInterface callbackInterface = new ClientCallbackInterface(this.wscClient);					
		this.callbackService = Endpoint.publish(callbackURL, callbackInterface);
		this.callbackServiceURL = callbackURL;
		
		this.wscClient.getStartStopjButton().setText("Stop");
		this.wscClient.getCallbackWSDLjButton().setEnabled(true);
		
		System.out.println("[WSCClientButtonListener]: Callback-Service startet at "+callbackURL);
	}
	
	/**
	 * Stops the callback-service.
	 */
	private void stopCallbackService() {
		this.callbackService.stop();
		this.callbackService = null;
		this.callbackServiceURL = null;
		
		this.wscClient.getStartStopjButton().setText("Start");
		this.wscClient.getCallbackWSDLjButton().setEnabled(false);
		
		System.out.println("[WSCClientButtonListener]: Callback-Service stopped.");
	}
	
	/**
	 * This method handles the "Start"-button.
	 * It starts and stops the callback-service.
	 */
	private void startStopButton() {
		
		if(this.callbackService == null || !this.callbackService.isPublished()) {
			// Read the callback-URL.
			String callbackURL = this.wscClient.getCallbackURLjTextField().getText().trim();
			
			if(callbackURL == null || callbackURL.length() == 0) {
				JOptionPane.showMessageDialog(this.wscClient, "Please enter the Callback-URL!","Missing Callback-URL",JOptionPane.ERROR_MESSAGE);
				this.wscClient.getCallbackURLjTextField().requestFocus();
				return;
			}
						
			startCallbackService(callbackURL);				
		}
		else {
			stopCallbackService();
		}		
	}
	
	/**
	 * Handles the "Quit"-button.
	 * Exits the program.
	 */
	private void quitButton() {
		System.exit(0);
	}

	/**
	 * This method is called when a button is pressed on the "WSCClient"-frame.
	 */
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		
		if(actionEvent.getActionCommand().equals("Browse OWL Taxonomy")) {			
			browseOWLTaxonomyButton();
		}
		else if(actionEvent.getActionCommand().equals("Browse WSDL Service Descriptions")) {			
			browseWSDLServiceDescriptionsButton();
		}
		else if(actionEvent.getActionCommand().equals("Browse WSLA")) {
			browseWSLAButton();
		}
		else if(actionEvent.getActionCommand().equals("Initialize")) {			
			initializeButton();
		}
		else if(actionEvent.getActionCommand().equals("Browse WSDL Query")) {			
			browseWSDLQueryButton();
		}
		else if(actionEvent.getActionCommand().equals("WSDL Callback")) {			
			showWSDLCallbackButton();
		}
		else if(actionEvent.getActionCommand().equals("Start-Stop-CallbackService")) {
			startStopButton();
		}
		else if(actionEvent.getActionCommand().equals("Send Query")) {			
			sendQueryButton();
		}
		else if(actionEvent.getActionCommand().equals("Info")) {
			infoButton();
		}
		else if(actionEvent.getActionCommand().equals("Quit")) {			
			quitButton();
		}
	}
	
	public static Object setURL(String url,Object JaxWS) {
		return ((BindingProvider)JaxWS).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,url);
	}
}

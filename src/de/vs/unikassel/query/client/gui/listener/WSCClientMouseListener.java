package de.vs.unikassel.query.client.gui.listener;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import de.vs.unikassel.query.client.gui.WSCClient;

/**
 * This class handles the mouse-events of the "WSCClient".
 * @author Marc Kirchhoff
 *
 */
public class WSCClientMouseListener extends MouseAdapter{
	
	/**
	 * The "WSCClient"-frame.
	 */
	private WSCClient wscClient;
	
	/**
	 * The corresponding "(?)"-label.
	 */
	private JLabel helpLabel;
	
	/**
	 * The info-file-pathes.
	 */
	public static String compositionSystemURLInfoFilePath = "de/vs/unikassel/query/client/gui/helpfiles/CompositionSystemURL.txt";	
	public static String owlTaxonomyInfoFilePath = "de/vs/unikassel/query/client/gui/helpfiles/OWLTaxonomy.txt";
	public static String wsdlServiceDescriptionsInfoFilePath = "de/vs/unikassel/query/client/gui/helpfiles/WSDLServiceDescriptions.txt";
	public static String wslaInfoFilePath = "de/vs/unikassel/query/client/gui/helpfiles/WSLA.txt";
	public static String wsdlQueryInfoFilePath = "de/vs/unikassel/query/client/gui/helpfiles/WSDLQuery.txt";
	public static String callbackURLInfoFilePath = "de/vs/unikassel/query/client/gui/helpfiles/CallbackURL.txt";
	public static String timeMeasurementInfoFilePath = "de/vs/unikassel/query/client/gui/helpfiles/TimeMeasurement.txt";
	public static String challengeScoreInfoFilePath = "de/vs/unikassel/query/client/gui/helpfiles/ChallengeScore.txt";
		
	/**
	 * Creates a new listener with the given generator-gui-frame and help-label.
	 * @param wscClient The "WSCClient"-frame.
	 * @param helpLabel The corresponding "(?)"-label.
	 */
	public WSCClientMouseListener(WSCClient wscClient, JLabel helpLabel) {
		this.wscClient = wscClient;
		this.helpLabel = helpLabel;
	}	
	
	/**
	 * Reads the given file and returns the content of the file as a string.
	 * @param infoFilePath The path of the file.
	 * @return The content of the file as a string.
	 */
	private String readInfoFile(String infoFilePath) {
		
		// Read info-file.
		BufferedReader infoFileReader = new BufferedReader(new InputStreamReader(WSCClientMouseListener.class.getClassLoader().getResourceAsStream(infoFilePath)));
		StringBuffer infoFileText = new StringBuffer();
		String line = null;
		
		try {
			while((line = infoFileReader.readLine()) != null) {
				infoFileText.append(line);
				infoFileText.append("\n");
			}
		} catch (IOException exception) {
			System.err.println("WSCClientMouseListener: An error occurred during the reading of the info-file at "+infoFilePath);
			exception.printStackTrace();
			JOptionPane.showMessageDialog(this.wscClient, "An error occurred.\n See Debug-window for more informations.","Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		return infoFileText.toString();
	}
	
	/**
	 * Shows the corresponding info-dialog.
	 */
	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
		
		String titleText = null;
		String infoFileText = null;	
		
		if(helpLabel.getName().equals("compositionSystemURLHelp")) {
			titleText = "Composition-System-URL";
			infoFileText = readInfoFile(WSCClientMouseListener.compositionSystemURLInfoFilePath);	
		}
		else if(helpLabel.getName().equals("owlTaxonomyHelp")) {
			titleText = "OWL-Taxonomy";
			infoFileText = readInfoFile(WSCClientMouseListener.owlTaxonomyInfoFilePath);			
		}
		else if(helpLabel.getName().equals("wsdlServiceDescriptionsHelp")) {
			titleText = "WSDL-Service-Descriptions";
			infoFileText = readInfoFile(WSCClientMouseListener.wsdlServiceDescriptionsInfoFilePath);			
		}
		else if(helpLabel.getName().equals("wslaHelp")) {
			titleText = "WSLA";
			infoFileText = readInfoFile(WSCClientMouseListener.wslaInfoFilePath);
		}
		else if(helpLabel.getName().equals("wsdlQueryHelp")) {
			titleText = "WSDL-Query";
			infoFileText = readInfoFile(WSCClientMouseListener.wsdlQueryInfoFilePath);			
		}
		else if(helpLabel.getName().equals("callbackURLHelp")) {
			titleText = "Callback-URL";
			infoFileText = readInfoFile(WSCClientMouseListener.callbackURLInfoFilePath);			
		}
		else if(helpLabel.getName().equals("timeMeasurementHelp")) {
			titleText = "Time-Measurement";
			infoFileText = readInfoFile(WSCClientMouseListener.timeMeasurementInfoFilePath);			
		}
		else if(helpLabel.getName().equals("challengeScoreHelp")) {
			titleText = "Challenge-Score";
			infoFileText = readInfoFile(WSCClientMouseListener.challengeScoreInfoFilePath);			
		}
		else {
			System.err.println("[WSCClientMouseListener]: Incorrect label-name.");
			return;
		}
		
		if(titleText == null || infoFileText == null) {
			JOptionPane.showMessageDialog(this.wscClient, "An error occurred.\n See Debug-window for more informations.","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		JOptionPane.showMessageDialog(this.wscClient, infoFileText,titleText,JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Increases the sizes of the font and sets another cursor.
	 */
	@Override
	public void mouseEntered(MouseEvent mouseEvent) {
		this.helpLabel.setFont(this.helpLabel.getFont().deriveFont(this.helpLabel.getFont().getSize2D()+2f));
		this.helpLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	/**
	 * Decreases the size of the font and sets the default-cursor.
	 */
	@Override
	public void mouseExited(MouseEvent mouseEvent) {
		this.helpLabel.setFont(this.helpLabel.getFont().deriveFont(this.helpLabel.getFont().getSize2D()-2f));
		this.helpLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
}

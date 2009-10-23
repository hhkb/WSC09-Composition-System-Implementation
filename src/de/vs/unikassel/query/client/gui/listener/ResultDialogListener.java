package de.vs.unikassel.query.client.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;

import javax.swing.JOptionPane;

import de.vs.unikassel.GuiComponents.SAXTreeViewer;
import de.vs.unikassel.query.client.gui.ResultDialog;
import de.vs.unikassel.query.client.gui.WSCClient;

/**
 * This class handles the button-events of the "ResultDialog".
 * @author Marc Kirchhoff
 *
 */
public class ResultDialogListener implements ActionListener{
	
	/**
	 * The Result-Dialog.
	 */
	private ResultDialog resultDialog;
	
	/**
	 * Creates a new listener with the given Result-Dialog.
	 * @param resultDialog The Result-Dialog.
	 */
	public ResultDialogListener(ResultDialog resultDialog) {
		this.resultDialog = resultDialog;
	}
	
	/**
	 * Starts a new request.
	 */
	private void startAgainButton() {
		this.resultDialog.dispose();
		this.resultDialog.getWscClient().getButtonListener().sendQueryButton();		
	}
	
	/**
	 * Displays the result-bpel-document.
	 */
	private void showBPELButton() {
		// Display the result-BPEL-document.
		try {			
			SAXTreeViewer treeViewer = new SAXTreeViewer(this.resultDialog.getBpelDocument());
			treeViewer.init("Result-BPEL-Document", new StringReader(this.resultDialog.getBpelDocument()));			
			
		} catch (Exception exception) {
			System.err.println("[ClientCallbackInterface]: An error occurred during the processing of the BPEL-document.");
			exception.printStackTrace();
			JOptionPane.showMessageDialog(this.resultDialog, "An error occurred during the processing of the BPEL-document. \n See Debug-Box for more informations.","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Updates the "Time-Measurement"- and "ChallengeScore"-textfield of the WSCClient and closes the Result-Dialog.
	 */
	private void closeButton() {
		WSCClient wscClient = this.resultDialog.getWscClient();
		wscClient.getTimeMeasurementjTextField().setText(this.resultDialog.getTimeMeasurementjTextField().getText());
		wscClient.getChallengeScorejTextField().setText(this.resultDialog.getChallengeScorejTextField().getText());
		this.resultDialog.dispose();
	}

	/**
	 * This method is called when the user presses a button.
	 */
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if(actionEvent.getActionCommand().equals("Start Again")) {
			startAgainButton();
		}
		else if(actionEvent.getActionCommand().equals("Show BPEL")) {
			showBPELButton();
		}
		else if(actionEvent.getActionCommand().equals("Close")) {
			closeButton();
		}
	}
}

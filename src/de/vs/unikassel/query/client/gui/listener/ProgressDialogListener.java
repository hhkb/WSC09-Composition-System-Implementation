package de.vs.unikassel.query.client.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import de.vs.unikassel.query.client.gui.ProgressDialog;
import de.vs.unikassel.query.server.stub.CompositionSystem;
import de.vs.unikassel.query.server.stub.CompositionSystemInterfaceService;

/**
 * Handles the events of the "ProgressDialog".
 * @author Marc Kirchhoff
 *
 */
public class ProgressDialogListener implements ActionListener{
	
	/**
	 * The "ProgressDialog".
	 */
	private ProgressDialog progressDialog;
		
	/**
	 * Creates a new Listener with the given progress-dialog.
	 * @param progressDialog The progress-dialog.
	 */
	public ProgressDialogListener(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}
	
	/**
	 * Sends a stop-signal to the Composition-System
	 */
	private void stopComposition() {
		
		System.out.println("[ProgressDialogListener]: Stop composition.");
		
		// Send a stop-signal to the Composition-System.
		CompositionSystem stub = new CompositionSystemInterfaceService().getCompositionSystemPort();
		WSCClientButtonListener.setURL(this.progressDialog.getCompositionSystemServiceURL().toExternalForm(), stub);
		boolean stopped = stub.stopComposition();
		
		this.progressDialog.getClientCallbackInterface().setWaitingForResult(false);
		
		if(stopped) {
			System.out.println("[ProgressDialogListener]: Composition-System stopped.");
			this.progressDialog.getCreationjProgressBar().setIndeterminate(false);
			this.progressDialog.getCreationjProgressBar().setValue(100);
			JOptionPane.showMessageDialog(this.progressDialog, "Composition-System stopped.","Stopped",JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			System.out.println("[ProgressDialogListener]: The stopping of the Composition-System failed.");
			JOptionPane.showMessageDialog(this.progressDialog, "The stopping of the Composition-System failed.","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Handles the "Stop"-button.
	 */
	private void stopButton() {
		if(this.progressDialog.getClientCallbackInterface().isWaitingForResult()) {
			stopComposition();
		}					
	}
	
	/**
	 * Handles the "Cancel"-button.
	 */
	private void closeButton() {
		if(this.progressDialog.getClientCallbackInterface().isWaitingForResult()) {
			int result = JOptionPane.showConfirmDialog(this.progressDialog, "The Composition-System is still running. \n Stop composition?","Composition-System is running", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if(result != JOptionPane.YES_OPTION) {				
				return;				
			}
			
			try {
				stopComposition();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		this.progressDialog.dispose();
	}

	/**
	 * Handles the buttons of the progress-dialog.
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		if(actionEvent.getActionCommand().equals(this.progressDialog.getStopjButton().getActionCommand())) {
			stopButton();
		}
		else if(actionEvent.getActionCommand().equals(this.progressDialog.getClosejButton().getActionCommand())) {
			closeButton();
		}
	}
}

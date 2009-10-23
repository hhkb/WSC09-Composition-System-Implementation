package de.vs.unikassel.generator.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import de.vs.unikassel.generator.gui.ProgressDialog;

/**
 * This class handles the action-events of the Progress-Dialog.
 * @author Marc Kirchhoff
 *
 */
public class ProgressDialogListener implements ActionListener{
	
	/**
	 * The dialog.
	 */
	private ProgressDialog progressDialog;
	
	/**
	 * Creates a new Listener with the given Dialog.
	 * @param progressDialog The dialog.
	 */
	public ProgressDialogListener(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	/**
	 * This method is called when the user presses a button on the Prograss-Dialog.
	 * @param actionEvent The event-object.
	 */
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if(actionEvent.getActionCommand().equals("Stop")) {
			this.progressDialog.getTaskGeneratorThread().stop();
			this.progressDialog.getStopjButton().setEnabled(false);
			this.progressDialog.getCreationjProgressBar().setIndeterminate(false);
			return;
		}
		else if(actionEvent.getActionCommand().equals("Close")) {
			if(this.progressDialog.getTaskGeneratorThread().isAlive()) {
				int result = JOptionPane.showConfirmDialog(this.progressDialog, "Stop the generation of the test-files?","Generation in Progress", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(result == JOptionPane.YES_OPTION) {
					if(this.progressDialog.getTaskGeneratorThread().isAlive()) {
						this.progressDialog.getTaskGeneratorThread().stop();
					}
					this.progressDialog.dispose();					
				}
				else {
					return;
				}
			}
			
			this.progressDialog.dispose();
		}
	}
}

package de.vs.unikassel.query.client.gui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import de.vs.unikassel.logger.SystemOutTextArea;
import de.vs.unikassel.query.client.gui.listener.ResultDialogListener;

/**
 * This progress-dialog is shown until the Composition-System has sent the result-document.
 * @author Marc Kirchhoff
 *
 */
public class ResultDialog extends JDialog { 

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JButton startAgainjButton = null;
	
	private SystemOutTextArea systemOutTextArea = null;

	private JButton closejButton = null;

	private JButton showBPELjButton = null;

	private JLabel timeMeasurementjLabel = null;

	private JLabel ChallengeScorejLabel = null;

	private JTextField timeMeasurementjTextField = null;

	private JTextField challengeScorejTextField = null;	
	
	private String debugText = null;
	
	private long timeMeasurement;
	
	private String bpelDocument;
	
	private ResultDialogListener resultDialogListener;
	
	private WSCClient wscClient;

	public ResultDialog(Frame owner, String bpelDocument, String debugText, long timeMeasurement) {
		super(owner);
		this.wscClient = (WSCClient)owner;
		this.bpelDocument = bpelDocument;
		this.debugText = debugText;
		this.timeMeasurement = timeMeasurement;
		this.resultDialogListener = new ResultDialogListener(this);
		initialize();		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(438, 289);
		this.setTitle("Result");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setUndecorated(false);
		this.setResizable(false);
		this.setModal(false);		
		this.setContentPane(getJContentPane());
		this.setLocationRelativeTo(null);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			ChallengeScorejLabel = new JLabel();
			ChallengeScorejLabel.setBounds(new Rectangle(12, 172, 112, 27));
			ChallengeScorejLabel.setText("Challenge Score:");
			timeMeasurementjLabel = new JLabel();
			timeMeasurementjLabel.setBounds(new Rectangle(12, 132, 125, 27));
			timeMeasurementjLabel.setText("Time Measurement:");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);			
			jContentPane.add(getStartAgainjButton(), null);
			jContentPane.add(getSystemOutTextArea2(), null);
			jContentPane.add(getClosejButton(), null);
			jContentPane.add(getShowBPELjButton(), null);
			jContentPane.add(timeMeasurementjLabel, null);
			jContentPane.add(ChallengeScorejLabel, null);
			jContentPane.add(getTimeMeasurementjTextField(), null);
			jContentPane.add(getChallengeScorejTextField(), null);
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes startAgainjButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getStartAgainjButton() {
		if (startAgainjButton == null) {
			startAgainjButton = new JButton();
			startAgainjButton.setBounds(new Rectangle(12, 217, 113, 33));
			startAgainjButton.setText("Start Again");
			startAgainjButton.addActionListener(this.resultDialogListener);
			}
		return startAgainjButton;
	}

	/**
	 * This method initializes systemOutTextArea	
	 * 	
	 * @return de.unikassel.vs.logger.SystemOutTextArea	
	 */
	private SystemOutTextArea getSystemOutTextArea2() {
		if (systemOutTextArea == null) {
			systemOutTextArea = new SystemOutTextArea(new JTextArea());
			systemOutTextArea.setBounds(new Rectangle(1, 0, 431, 105));
			
			if (this.bpelDocument != null)
				systemOutTextArea.getTextbox().setText(this.debugText + '\n' + this.bpelDocument);
			else
				systemOutTextArea.getTextbox().setText(this.debugText);
		}
		return systemOutTextArea;
	}

	/**
	 * This method initializes closejButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getClosejButton() {
		if (closejButton == null) {
			closejButton = new JButton();
			closejButton.setBounds(new Rectangle(311, 217, 113, 33));
			closejButton.setText("Close");
			closejButton.addActionListener(this.resultDialogListener);
		}
		return closejButton;
	}

	/**
	 * This method initializes showBPELjButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getShowBPELjButton() {
		if (showBPELjButton == null) {
			showBPELjButton = new JButton();
			showBPELjButton.setBounds(new Rectangle(161, 217, 113, 33));
			showBPELjButton.setText("Show BPEL");
			showBPELjButton.addActionListener(this.resultDialogListener);
		}
		return showBPELjButton;
	}

	/**
	 * This method initializes timeMeasurementjTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getTimeMeasurementjTextField() {
		if (timeMeasurementjTextField == null) {
			timeMeasurementjTextField = new JTextField();
			timeMeasurementjTextField.setBounds(new Rectangle(161, 132, 153, 27));
			timeMeasurementjTextField.setEnabled(false);
			timeMeasurementjTextField.setForeground(Color.white);
			timeMeasurementjTextField.setText(String.valueOf(this.timeMeasurement));
		}
		return timeMeasurementjTextField;
	}

	/**
	 * This method initializes challengeScorejTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getChallengeScorejTextField() {
		if (challengeScorejTextField == null) {
			challengeScorejTextField = new JTextField();
			challengeScorejTextField.setBounds(new Rectangle(161, 172, 153, 27));
			challengeScorejTextField.setEditable(false);
		}
		return challengeScorejTextField;
	}

	public String getBpelDocument() {
		return bpelDocument;
	}

	public void setBpelDocument(String bpelDocument) {
		this.bpelDocument = bpelDocument;
	}

	/**
	 * @return the wscClient
	 */
	public WSCClient getWscClient() {
		return wscClient;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

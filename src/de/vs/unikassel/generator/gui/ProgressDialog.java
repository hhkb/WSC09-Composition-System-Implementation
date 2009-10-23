package de.vs.unikassel.generator.gui;

import java.awt.Frame;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import de.vs.unikassel.generator.gui.listener.ProgressDialogListener;
import de.vs.unikassel.logger.SystemOutTextArea;

import java.awt.Dimension;

public class ProgressDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JProgressBar creationjProgressBar = null;

	private JButton stopjButton = null;
	
	private SystemOutTextArea systemout = null;
	
	private Thread taskGeneratorThread;

	private SystemOutTextArea systemOutTextArea = null;

	private JButton closejButton = null;
	
	public ProgressDialog(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * @param owner
	 */
	public ProgressDialog(Frame owner, Thread taskGeneratorThread) {
		super(owner);
		initialize();
		this.taskGeneratorThread = taskGeneratorThread;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(322, 240);
		this.setTitle("Generation in Progress");
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setUndecorated(false);
		this.setResizable(false);
		this.setModal(true);		
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
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getCreationjProgressBar(), null);
			jContentPane.add(getStopjButton(), null);
			jContentPane.add(getSystemOutTextArea2(), null);
			jContentPane.add(getClosejButton(), null);
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes creationjProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	public JProgressBar getCreationjProgressBar() {
		if (creationjProgressBar == null) {
			creationjProgressBar = new JProgressBar();
			creationjProgressBar.setBounds(new Rectangle(-2, 0, 318, 39));
			creationjProgressBar.setIndeterminate(true);			
		}
		return creationjProgressBar;
	}

	/**
	 * This method initializes stopjButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getStopjButton() {
		if (stopjButton == null) {
			stopjButton = new JButton();
			stopjButton.setBounds(new Rectangle(23, 166, 113, 33));
			stopjButton.setText("Stop");
			stopjButton.addActionListener(new ProgressDialogListener(this));
		}
		return stopjButton;
	}

	/**
	 * @return the taskGeneratorThread
	 */
	public Thread getTaskGeneratorThread() {
		return taskGeneratorThread;
	}

	/**
	 * This method initializes systemOutTextArea	
	 * 	
	 * @return de.unikassel.vs.logger.SystemOutTextArea	
	 */
	private SystemOutTextArea getSystemOutTextArea2() {
		if (systemOutTextArea == null) {
			systemOutTextArea = new SystemOutTextArea(new JTextArea());
			systemOutTextArea.setBounds(new Rectangle(0, 40, 317, 114));
		}
		return systemOutTextArea;
	}

	/**
	 * This method initializes closejButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getClosejButton() {
		if (closejButton == null) {
			closejButton = new JButton();
			closejButton.setBounds(new Rectangle(173, 166, 113, 33));
			closejButton.setText("Close");
			closejButton.addActionListener(new ProgressDialogListener(this));
		}
		return closejButton;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

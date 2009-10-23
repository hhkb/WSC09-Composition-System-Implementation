package de.vs.unikassel.query.client.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import de.vs.unikassel.logger.SystemOutTextArea;
import de.vs.unikassel.query.client.gui.listener.WSCClientButtonListener;
import de.vs.unikassel.query.client.gui.listener.WSCClientMouseListener;

/**
 * This GUI can be used to send a request to a composition-system. 
 * @author Marc Kirchhoff
 *
 */
public class WSCClient extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JLabel compositionSystemURLjLabel = null;

	private JTextField compositionSystemURLjTextField = null;

	private JLabel owlTaxonomyjLabel = null;

	private JTextField owlTaxonomyURLjTextField = null;

	private JLabel wsdlServiceDescriptionsjLabel = null;

	private JTextField wsdlServiceDescriptionsURLjTextField = null;

	private JButton browseWSDLServicesjButton = null;

	private JButton owlTaxonomyBrowsejButton = null;

	private JButton initializejButton = null;

	private JLabel wsdlQueryjLabel = null;

	private JTextField wsdlQueryjTextField = null;

	private JButton browseWSDLQueryjButton = null;

	private JButton startStopjButton = null;

	private JLabel compositionSystemURLHelpjLabel = null;

	private JLabel compositionSystemURLColonjLabel = null;

	private JPanel wsdlQueryjPanel = null;

	private JLabel wsdlQueryHelpjLabel = null;

	private JLabel wsdlQueryColonjLabel = null;

	private JPanel webServiceClientjPanel = null;

	private JPanel semanticServicesjPanel = null;

	private JLabel owlTaxonomyHelpjLabel = null;

	private JLabel owlTaxonomyColonjLabel = null;

	private JLabel wsdlServiceDescriptionsHelpjLabel = null;

	private JLabel wsdlServiceDescriptionsColonjLabel = null;

	private JPanel debugjPanel = null;

	private JLabel timeMeasurementjLabel = null;

	private JLabel timeMeasurementHelpjLabel = null;

	private JLabel timeMeasurementColonjLabel = null;

	private JTextField timeMeasurementjTextField = null;

	private JLabel challengeScorejLabel = null;

	private JLabel challengeScoreHelpjLabel = null;

	private JLabel challengeScoreColonjLabel = null;

	private JTextField challengeScorejTextField = null;

	private JButton quitjButton = null;

	private JLabel callbackURLjLabel = null;

	private JLabel callbackURLHelpjLabel = null;

	private JTextField callbackURLjTextField = null;

	private JButton callbackWSDLjButton = null;

	private SystemOutTextArea debugSystemOutTextArea = null;

	private JPanel compositionSystemURLjPanel = null;

	private JLabel callbackURLColonjLabel = null;

	private JButton sendQueryjButton = null;
	
	private WSCClientButtonListener buttonListener;

	private JButton infojButton = null;

	private JLabel wslajLabel = null;

	private JLabel wslaHelpjLabel = null;

	private JLabel wslaColonjLabel = null;

	private JTextField wslaURLjTextField = null;

	private JButton browseWSLAjButton = null;

	/**
	 * This method initializes compositionSystemURLjTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getCompositionSystemURLjTextField() {
		if (compositionSystemURLjTextField == null) {
			compositionSystemURLjTextField = new JTextField();
			compositionSystemURLjTextField.setBounds(new Rectangle(208, 20, 235, 27));
			compositionSystemURLjTextField.setText("http://localhost:8080/CompositionSystem");
		}
		return compositionSystemURLjTextField;
	}

	/**
	 * This method initializes owlTaxonomyURLjTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getOwlTaxonomyURLjTextField() {
		if (owlTaxonomyURLjTextField == null) {
			owlTaxonomyURLjTextField = new JTextField();
			owlTaxonomyURLjTextField.setBounds(new Rectangle(208, 20, 235, 27));
		}
		return owlTaxonomyURLjTextField;
	}

	/**
	 * This method initializes wsdlServiceDescriptionsURLjTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getWsdlServiceDescriptionsURLjTextField() {
		if (wsdlServiceDescriptionsURLjTextField == null) {
			wsdlServiceDescriptionsURLjTextField = new JTextField();
			wsdlServiceDescriptionsURLjTextField.setBounds(new Rectangle(208, 105, 235, 27));
		}
		return wsdlServiceDescriptionsURLjTextField;
	}

	/**
	 * This method initializes browseWSDLServicesjButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBrowseWSDLServicesjButton() {
		if (browseWSDLServicesjButton == null) {
			browseWSDLServicesjButton = new JButton();
			browseWSDLServicesjButton.setText("Browse");
			browseWSDLServicesjButton.setActionCommand("Browse WSDL Service Descriptions");
			browseWSDLServicesjButton.setBounds(new Rectangle(208, 140, 100, 27));
			browseWSDLServicesjButton.addActionListener(this.buttonListener);
		}
		return browseWSDLServicesjButton;
	}

	/**
	 * This method initializes owlTaxonomyBrowsejButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOwlTaxonomyBrowsejButton() {
		if (owlTaxonomyBrowsejButton == null) {
			owlTaxonomyBrowsejButton = new JButton();
			owlTaxonomyBrowsejButton.setText("Browse");
			owlTaxonomyBrowsejButton.setActionCommand("Browse OWL Taxonomy");
			owlTaxonomyBrowsejButton.setBounds(new Rectangle(208, 55, 100, 27));
			owlTaxonomyBrowsejButton.addActionListener(this.buttonListener);
		}
		return owlTaxonomyBrowsejButton;
	}

	/**
	 * This method initializes initializejButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getInitializejButton() {
		if (initializejButton == null) {
			initializejButton = new JButton();
			initializejButton.setText("Initialize");
			initializejButton.setActionCommand("Initialize");
			initializejButton.setBounds(new Rectangle(343, 226, 100, 27));
			initializejButton.addActionListener(this.buttonListener);
		}
		return initializejButton;
	}

	/**
	 * This method initializes wsdlQueryjTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getWsdlQueryjTextField() {
		if (wsdlQueryjTextField == null) {
			wsdlQueryjTextField = new JTextField();
			wsdlQueryjTextField.setBounds(new Rectangle(208, 20, 235, 27));
		}
		return wsdlQueryjTextField;
	}

	/**
	 * This method initializes browseWSDLQueryjButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBrowseWSDLQueryjButton() {
		if (browseWSDLQueryjButton == null) {
			browseWSDLQueryjButton = new JButton();
			browseWSDLQueryjButton.setText("Browse");
			browseWSDLQueryjButton.setActionCommand("Browse WSDL Query");
			browseWSDLQueryjButton.setBounds(new Rectangle(208, 55, 100, 27));
			browseWSDLQueryjButton.addActionListener(this.buttonListener);
		}
		return browseWSDLQueryjButton;
	}

	/**
	 * This method initializes startStopjButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getStartStopjButton() {
		if (startStopjButton == null) {
			startStopjButton = new JButton();
			startStopjButton.setText("Start");
			startStopjButton.setActionCommand("Start-Stop-CallbackService");
			startStopjButton.setBounds(new Rectangle(208, 140, 100, 27));
			startStopjButton.addActionListener(this.buttonListener);
		}
		return startStopjButton;
	}

	/**
	 * This method initializes wsdlQueryjPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getWsdlQueryjPanel() {
		if (wsdlQueryjPanel == null) {
			callbackURLColonjLabel = new JLabel();
			callbackURLColonjLabel.setBounds(new Rectangle(110, 105, 13, 27));
			callbackURLColonjLabel.setText(":");
			callbackURLHelpjLabel = new JLabel();
			callbackURLHelpjLabel.setBounds(new Rectangle(91, 105, 20, 27));
			callbackURLHelpjLabel.setForeground(new Color(0, 0, 140));
			callbackURLHelpjLabel.setName("callbackURLHelp");
			callbackURLHelpjLabel.setText("(?)");
			callbackURLHelpjLabel.addMouseListener(new WSCClientMouseListener(this, callbackURLHelpjLabel));
			callbackURLjLabel = new JLabel();
			callbackURLjLabel.setBounds(new Rectangle(10, 105, 80, 27));
			callbackURLjLabel.setText("Callback URL");
			wsdlQueryColonjLabel = new JLabel();
			wsdlQueryColonjLabel.setBounds(new Rectangle(110, 20, 16, 27));
			wsdlQueryColonjLabel.setText(":");
			wsdlQueryHelpjLabel = new JLabel();
			wsdlQueryHelpjLabel.setBounds(new Rectangle(90, 20, 20, 27));
			wsdlQueryHelpjLabel.setForeground(new Color(0, 0, 140));
			wsdlQueryHelpjLabel.setName("wsdlQueryHelp");
			wsdlQueryHelpjLabel.setText("(?)");
			wsdlQueryHelpjLabel.addMouseListener(new WSCClientMouseListener(this, wsdlQueryHelpjLabel));
			wsdlQueryjPanel = new JPanel();
			wsdlQueryjPanel.setLayout(null);
			wsdlQueryjPanel.setBorder(BorderFactory.createTitledBorder(null, "Query", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			wsdlQueryjPanel.setBounds(new Rectangle(9, 369, 458, 221));
			wsdlQueryjPanel.add(getStartStopjButton(), null);
			wsdlQueryjPanel.add(getBrowseWSDLQueryjButton(), null);
			wsdlQueryjPanel.add(getWsdlQueryjTextField(), null);
			wsdlQueryjPanel.add(wsdlQueryjLabel, null);
			wsdlQueryjPanel.add(wsdlQueryHelpjLabel, null);
			wsdlQueryjPanel.add(wsdlQueryColonjLabel, null);
			wsdlQueryjPanel.add(callbackURLjLabel, null);
			wsdlQueryjPanel.add(callbackURLHelpjLabel, null);
			wsdlQueryjPanel.add(getCallbackURLjTextField(), null);
			wsdlQueryjPanel.add(getCallbackWSDLjButton(), null);
			wsdlQueryjPanel.add(callbackURLColonjLabel, null);
			wsdlQueryjPanel.add(getSendQueryjButton(), null);
		}
		return wsdlQueryjPanel;
	}

	/**
	 * This method initializes webServiceClientjPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getWebServiceClientjPanel() {
		if (webServiceClientjPanel == null) {
			webServiceClientjPanel = new JPanel();
			webServiceClientjPanel.setLayout(null);
			webServiceClientjPanel.setBounds(new Rectangle(5, 4, 476, 857));
			webServiceClientjPanel.setBorder(BorderFactory.createTitledBorder(null, "WSC\'09 Web service client", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			webServiceClientjPanel.add(getWsdlQueryjPanel(), null);
			webServiceClientjPanel.add(getSemanticServicesjPanel(), null);
			webServiceClientjPanel.add(getDebugjPanel(), null);
			webServiceClientjPanel.add(getQuitjButton(), null);
			webServiceClientjPanel.add(getCompositionSystemURLjPanel(), null);
			webServiceClientjPanel.add(getInfojButton(), null);
		}
		return webServiceClientjPanel;
	}

	/**
	 * This method initializes semanticServicesjPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSemanticServicesjPanel() {
		if (semanticServicesjPanel == null) {
			wslaColonjLabel = new JLabel();
			wslaColonjLabel.setBounds(new Rectangle(69, 191, 13, 27));
			wslaColonjLabel.setText(":");
			wslaHelpjLabel = new JLabel();
			wslaHelpjLabel.setBounds(new Rectangle(48, 191, 21, 27));
			wslaHelpjLabel.setForeground(new Color(0, 0, 140));
			wslaHelpjLabel.setName("wslaHelp");
			wslaHelpjLabel.setText("(?)");
			wslaHelpjLabel.addMouseListener(new WSCClientMouseListener(this, wslaHelpjLabel));
			wslajLabel = new JLabel();
			wslajLabel.setBounds(new Rectangle(9, 191, 39, 27));
			wslajLabel.setText("WSLA");
			wsdlServiceDescriptionsColonjLabel = new JLabel();
			wsdlServiceDescriptionsColonjLabel.setBounds(new Rectangle(190, 105, 12, 27));
			wsdlServiceDescriptionsColonjLabel.setText(":");
			wsdlServiceDescriptionsHelpjLabel = new JLabel();
			wsdlServiceDescriptionsHelpjLabel.setBounds(new Rectangle(170, 105, 21, 27));
			wsdlServiceDescriptionsHelpjLabel.setForeground(new Color(0, 0, 140));
			wsdlServiceDescriptionsHelpjLabel.setName("wsdlServiceDescriptionsHelp");
			wsdlServiceDescriptionsHelpjLabel.setText("(?)");
			wsdlServiceDescriptionsHelpjLabel.addMouseListener(new WSCClientMouseListener(this, wsdlServiceDescriptionsHelpjLabel));
			owlTaxonomyColonjLabel = new JLabel();
			owlTaxonomyColonjLabel.setBounds(new Rectangle(128, 20, 10, 27));
			owlTaxonomyColonjLabel.setText(":");
			owlTaxonomyHelpjLabel = new JLabel();
			owlTaxonomyHelpjLabel.setBounds(new Rectangle(108, 20, 19, 27));
			owlTaxonomyHelpjLabel.setForeground(new Color(0, 0, 140));
			owlTaxonomyHelpjLabel.setName("owlTaxonomyHelp");
			owlTaxonomyHelpjLabel.setText("(?)");
			owlTaxonomyHelpjLabel.addMouseListener(new WSCClientMouseListener(this, owlTaxonomyHelpjLabel));
			semanticServicesjPanel = new JPanel();
			semanticServicesjPanel.setLayout(null);
			semanticServicesjPanel.setBounds(new Rectangle(8, 92, 458, 272));
			semanticServicesjPanel.setBorder(BorderFactory.createTitledBorder(null, "Initialize", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			semanticServicesjPanel.add(owlTaxonomyjLabel, null);
			semanticServicesjPanel.add(getOwlTaxonomyURLjTextField(), null);
			semanticServicesjPanel.add(getOwlTaxonomyBrowsejButton(), null);
			semanticServicesjPanel.add(wsdlServiceDescriptionsjLabel, null);
			semanticServicesjPanel.add(getWsdlServiceDescriptionsURLjTextField(), null);
			semanticServicesjPanel.add(getBrowseWSDLServicesjButton(), null);
			semanticServicesjPanel.add(getInitializejButton(), null);
			semanticServicesjPanel.add(owlTaxonomyHelpjLabel, null);
			semanticServicesjPanel.add(owlTaxonomyColonjLabel, null);
			semanticServicesjPanel.add(wsdlServiceDescriptionsHelpjLabel, null);
			semanticServicesjPanel.add(wsdlServiceDescriptionsColonjLabel, null);
			semanticServicesjPanel.add(wslajLabel, null);
			semanticServicesjPanel.add(wslaHelpjLabel, null);
			semanticServicesjPanel.add(wslaColonjLabel, null);
			semanticServicesjPanel.add(getWslaURLjTextField(), null);
			semanticServicesjPanel.add(getBrowseWSLAjButton(), null);
		}
		return semanticServicesjPanel;
	}

	/**
	 * This method initializes debugjPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDebugjPanel() {
		if (debugjPanel == null) {
			challengeScoreColonjLabel = new JLabel();
			challengeScoreColonjLabel.setBounds(new Rectangle(130, 64, 18, 27));
			challengeScoreColonjLabel.setText(":");
			challengeScoreHelpjLabel = new JLabel();
			challengeScoreHelpjLabel.setBounds(new Rectangle(110, 64, 20, 27));
			challengeScoreHelpjLabel.setForeground(new Color(0, 0, 140));
			challengeScoreHelpjLabel.setName("challengeScoreHelp");
			challengeScoreHelpjLabel.setText("(?)");
			challengeScoreHelpjLabel.addMouseListener(new WSCClientMouseListener(this, challengeScoreHelpjLabel));
			challengeScorejLabel = new JLabel();
			challengeScorejLabel.setBounds(new Rectangle(10, 64, 101, 27));
			challengeScorejLabel.setText("Challenge Score");
			timeMeasurementColonjLabel = new JLabel();
			timeMeasurementColonjLabel.setBounds(new Rectangle(150, 20, 15, 27));
			timeMeasurementColonjLabel.setText(":");
			timeMeasurementHelpjLabel = new JLabel();
			timeMeasurementHelpjLabel.setBounds(new Rectangle(127, 20, 21, 27));
			timeMeasurementHelpjLabel.setForeground(new Color(0, 0, 140));
			timeMeasurementHelpjLabel.setName("timeMeasurementHelp");
			timeMeasurementHelpjLabel.setText("(?)");
			timeMeasurementHelpjLabel.addMouseListener(new WSCClientMouseListener(this, timeMeasurementHelpjLabel));
			timeMeasurementjLabel = new JLabel();
			timeMeasurementjLabel.setText("Time Measurement");
			timeMeasurementjLabel.setBounds(new Rectangle(10, 20, 117, 27));
			debugjPanel = new JPanel();
			debugjPanel.setLayout(null);
			debugjPanel.setBounds(new Rectangle(9, 596, 458, 208));
			debugjPanel.setBorder(BorderFactory.createTitledBorder(null, "Debug", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			debugjPanel.add(timeMeasurementjLabel, null);
			debugjPanel.add(timeMeasurementHelpjLabel, null);
			debugjPanel.add(timeMeasurementColonjLabel, null);
			debugjPanel.add(getTimeMeasurementjTextField(), null);
			debugjPanel.add(challengeScorejLabel, null);
			debugjPanel.add(challengeScoreHelpjLabel, null);
			debugjPanel.add(challengeScoreColonjLabel, null);
			debugjPanel.add(getChallengeScorejTextField(), null);
			debugjPanel.add(getDebugSystemOutTextArea(), null);
		}
		return debugjPanel;
	}

	/**
	 * This method initializes timeMeasurementjTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getTimeMeasurementjTextField() {
		if (timeMeasurementjTextField == null) {
			timeMeasurementjTextField = new JTextField();
			timeMeasurementjTextField.setBounds(new Rectangle(208, 20, 96, 27));
			timeMeasurementjTextField.setEditable(false);
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
			challengeScorejTextField.setBounds(new Rectangle(208, 64, 96, 27));
			challengeScorejTextField.setEditable(false);
		}
		return challengeScorejTextField;
	}

	/**
	 * This method initializes quitjButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getQuitjButton() {
		if (quitjButton == null) {
			quitjButton = new JButton();
			quitjButton.setBounds(new Rectangle(9, 813, 119, 29));
			quitjButton.setActionCommand("Quit");
			quitjButton.setText("Quit");
			quitjButton.addActionListener(this.buttonListener);
		}
		return quitjButton;
	}

	/**
	 * This method initializes callbackURLjTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getCallbackURLjTextField() {
		if (callbackURLjTextField == null) {
			callbackURLjTextField = new JTextField();
			callbackURLjTextField.setBounds(new Rectangle(208, 105, 235, 27));
			callbackURLjTextField.setText("http://localhost:7000/Callback");
		}
		return callbackURLjTextField;
	}

	/**
	 * This method initializes callbackWSDLjButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getCallbackWSDLjButton() {
		if (callbackWSDLjButton == null) {
			callbackWSDLjButton = new JButton();
			callbackWSDLjButton.setBounds(new Rectangle(341, 141, 100, 27));
			callbackWSDLjButton.setActionCommand("WSDL Callback");
			callbackWSDLjButton.setText("WSDL");
			callbackWSDLjButton.addActionListener(this.buttonListener);
			callbackWSDLjButton.setEnabled(false);
		}
		return callbackWSDLjButton;
	}

	/**
	 * This method initializes debugSystemOutTextArea	
	 * 	
	 * @return de.unikassel.vs.logger.SystemOutTextArea	
	 */
	private SystemOutTextArea getDebugSystemOutTextArea() {
		if (debugSystemOutTextArea == null) {
			debugSystemOutTextArea = new SystemOutTextArea(new JTextArea());
			debugSystemOutTextArea.setBounds(new Rectangle(10, 109, 432, 85));
		}
		return debugSystemOutTextArea;
	}

	/**
	 * This method initializes compositionSystemURLjPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCompositionSystemURLjPanel() {
		if (compositionSystemURLjPanel == null) {
			compositionSystemURLjPanel = new JPanel();
			compositionSystemURLjPanel.setLayout(null);
			compositionSystemURLjPanel.setBounds(new Rectangle(8, 26, 458, 58));
			compositionSystemURLjPanel.setBorder(BorderFactory.createTitledBorder(null, "URL", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			compositionSystemURLjPanel.add(compositionSystemURLjLabel, null);
			compositionSystemURLjPanel.add(compositionSystemURLHelpjLabel, null);
			compositionSystemURLjPanel.add(compositionSystemURLColonjLabel, null);
			compositionSystemURLjPanel.add(getCompositionSystemURLjTextField(), null);
		}
		return compositionSystemURLjPanel;
	}

	/**
	 * This method initializes sendQueryjButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSendQueryjButton() {
		if (sendQueryjButton == null) {
			sendQueryjButton = new JButton();
			sendQueryjButton.setBounds(new Rectangle(208, 178, 100, 27));
			sendQueryjButton.setText("Send Query");
			sendQueryjButton.addActionListener(this.buttonListener);
		}
		return sendQueryjButton;
	}

	/**
	 * This method initializes infojButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getInfojButton() {
		if (infojButton == null) {
			infojButton = new JButton();
			infojButton.setBounds(new Rectangle(361, 813, 106, 29));
			infojButton.setText("Info");
			infojButton.addActionListener(this.buttonListener);
		}
		return infojButton;
	}

	/**
	 * This method initializes wslaURLjTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getWslaURLjTextField() {
		if (wslaURLjTextField == null) {
			wslaURLjTextField = new JTextField();
			wslaURLjTextField.setBounds(new Rectangle(208, 191, 235, 27));
		}
		return wslaURLjTextField;
	}

	/**
	 * This method initializes browseWSLAjButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBrowseWSLAjButton() {
		if (browseWSLAjButton == null) {
			browseWSLAjButton = new JButton();
			browseWSLAjButton.setBounds(new Rectangle(208, 226, 100, 27));
			browseWSLAjButton.setActionCommand("Browse WSLA");
			browseWSLAjButton.setText("Browse");
			browseWSLAjButton.addActionListener(this.buttonListener);
		}
		return browseWSLAjButton;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				WSCClient thisClass = new WSCClient();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public WSCClient() {
		super();
		this.buttonListener = new WSCClientButtonListener(this);
		initialize();		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(494, 901);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("WSC'09");
		this.setLocationRelativeTo(null);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			compositionSystemURLColonjLabel = new JLabel();
			compositionSystemURLColonjLabel.setText(":");
			compositionSystemURLColonjLabel.setBounds(new Rectangle(181, 20, 12, 27));
			compositionSystemURLHelpjLabel = new JLabel();
			compositionSystemURLHelpjLabel.setForeground(new Color(0, 0, 140));
			compositionSystemURLHelpjLabel.setName("compositionSystemURLHelp");
			compositionSystemURLHelpjLabel.setBounds(new Rectangle(158, 20, 20, 27));
			compositionSystemURLHelpjLabel.setText("(?)");
			compositionSystemURLHelpjLabel.addMouseListener(new WSCClientMouseListener(this, compositionSystemURLHelpjLabel));
			wsdlQueryjLabel = new JLabel();
			wsdlQueryjLabel.setText("WSDL Query");
			wsdlQueryjLabel.setBounds(new Rectangle(10, 20, 81, 27));
			wsdlServiceDescriptionsjLabel = new JLabel();
			wsdlServiceDescriptionsjLabel.setText("WSDL Service Descriptions");
			wsdlServiceDescriptionsjLabel.setBounds(new Rectangle(10, 105, 162, 27));
			owlTaxonomyjLabel = new JLabel();
			owlTaxonomyjLabel.setText("OWL Taxonomy");
			owlTaxonomyjLabel.setBounds(new Rectangle(10, 20, 97, 27));
			compositionSystemURLjLabel = new JLabel();
			compositionSystemURLjLabel.setText("Composition System URL");			
			compositionSystemURLjLabel.setBounds(new Rectangle(10, 20, 147, 27));
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getWebServiceClientjPanel(), null);
		}
		return jContentPane;
	}

	/**
	 * @return the buttonListener
	 */
	public WSCClientButtonListener getButtonListener() {
		return buttonListener;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

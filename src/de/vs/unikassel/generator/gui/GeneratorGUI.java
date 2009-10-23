package de.vs.unikassel.generator.gui;

import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.border.TitledBorder;

import de.vs.unikassel.generator.gui.listener.GeneratorGUIListener;
import de.vs.unikassel.generator.gui.listener.GeneratorGUIMouseListener;

import java.awt.Font;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import java.awt.Dimension;

/**
 * This GUI can be used to generate test-cases.
 * @author Marc Kirchhoff
 *
 */
public class GeneratorGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel inputDatajPanel = null;

	private JLabel numberOfConceptsjLabel = null;

	private JLabel numberOfServicesjLabel = null;

	private JTextField numberOfConceptsjTextField = null;

	private JTextField numberOfServicesjTextField = null;

	private JPanel outputDatajPanel = null;

	private JLabel bpelFileNamejLabel = null;

	private JTextField bpelFileNamejTextField = null;

	private JLabel taskWSDLFileNamejLabel = null;

	private JTextField taskWSDLFileNamejTextField = null;

	private JLabel servicesWSDLFileNamejLabel = null;

	private JTextField servicesWSDLFileNamejTextField = null;

	private JLabel outputFolderjLabel = null;

	private JTextField outputFolderjTextField = null;

	private JButton browseOutputFolderjButton = null;

	private JLabel solutionsjLabel = null;

	private JList solutionsjList = null;

	private JButton addSolutionjButton = null;

	private JButton removeSolutionjButton = null;

	private JLabel solutionDepthjLabel = null;

	private JTextField solutionDepthjTextField = null;

	private JButton startjButton = null;

	private JLabel intermediateFilesjLabel = null;

	private JCheckBox intermediateFilesjCheckBox = null;

	private JLabel owlFileNamejLabel = null;

	private JTextField owlFileNamejTextField = null;

	private JScrollPane solutionsListjScrollPane = null;

	private JLabel solvablejLabel = null;

	private JCheckBox solvablejCheckBox = null;

	private JButton infojButton = null;

	private JLabel numberOfConceptsHelpjLabel = null;

	private JLabel numberOfConceptsColonjLabel = null;

	private JLabel numberOfServicesHelpjLabel = null;

	private JLabel numberOfServicesColonjLabel = null;

	private JLabel solvableHelpjLabel = null;

	private JLabel solvableColonjLabel = null;

	private JLabel solutionsHelpjLabel = null;

	private JLabel solutionsColonjLabel = null;

	private JLabel solutionDepthHelpjLabel = null;

	private JLabel solutionDepthColonjLabel = null;

	private JLabel wslaFileNamejLabel = null;

	private JTextField wslaFileNamejTextField = null;

	/**
	 * This method initializes inputDatajPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getInputDatajPanel() {
		if (inputDatajPanel == null) {
			solutionDepthColonjLabel = new JLabel();
			solutionDepthColonjLabel.setBounds(new Rectangle(143, 219, 18, 27));
			solutionDepthColonjLabel.setText(":");
			solutionDepthHelpjLabel = new JLabel();
			solutionDepthHelpjLabel.setBounds(new Rectangle(125, 219, 22, 27));
			solutionDepthHelpjLabel.setForeground(new Color(0, 0, 140));
			solutionDepthHelpjLabel.setName("solutionDepthHelp");
			solutionDepthHelpjLabel.setText("(?)");
			solutionDepthHelpjLabel.addMouseListener(new GeneratorGUIMouseListener(this, solutionDepthHelpjLabel));
			solutionsColonjLabel = new JLabel();
			solutionsColonjLabel.setBounds(new Rectangle(93, 141, 17, 27));
			solutionsColonjLabel.setText(":");
			solutionsHelpjLabel = new JLabel();
			solutionsHelpjLabel.setBounds(new Rectangle(75, 141, 22, 27));
			solutionsHelpjLabel.setForeground(new Color(0, 0, 140));
			solutionsHelpjLabel.setName("solutionsHelp");
			solutionsHelpjLabel.setText("(?)");
			solutionsHelpjLabel.addMouseListener(new GeneratorGUIMouseListener(this, solutionsHelpjLabel));
			solvableColonjLabel = new JLabel();
			solvableColonjLabel.setBounds(new Rectangle(85, 110, 22, 27));
			solvableColonjLabel.setText(":");
			solvableHelpjLabel = new JLabel();
			solvableHelpjLabel.setBounds(new Rectangle(66, 110, 19, 27));
			solvableHelpjLabel.setForeground(new Color(0, 0, 140));
			solvableHelpjLabel.setName("solvableHelp");
			solvableHelpjLabel.setText("(?)");
			solvableHelpjLabel.addMouseListener(new GeneratorGUIMouseListener(this, solvableHelpjLabel));
			numberOfServicesColonjLabel = new JLabel();
			numberOfServicesColonjLabel.setBounds(new Rectangle(151, 71, 15, 27));
			numberOfServicesColonjLabel.setText(":");
			numberOfServicesHelpjLabel = new JLabel();
			numberOfServicesHelpjLabel.setBounds(new Rectangle(132, 71, 20, 27));
			numberOfServicesHelpjLabel.setForeground(new Color(0, 0, 140));
			numberOfServicesHelpjLabel.setName("numberOfServicesHelp");
			numberOfServicesHelpjLabel.setText("(?)");
			numberOfServicesHelpjLabel.addMouseListener(new GeneratorGUIMouseListener(this, numberOfServicesHelpjLabel));
			numberOfConceptsColonjLabel = new JLabel();
			numberOfConceptsColonjLabel.setBounds(new Rectangle(154, 29, 21, 27));
			numberOfConceptsColonjLabel.setText(":");
			numberOfConceptsHelpjLabel = new JLabel();
			numberOfConceptsHelpjLabel.setBounds(new Rectangle(135, 29, 19, 27));
			numberOfConceptsHelpjLabel.setForeground(new Color(0, 0, 140));
			numberOfConceptsHelpjLabel.setName("numberOfConceptsHelp");
			numberOfConceptsHelpjLabel.setText("(?)");
			numberOfConceptsHelpjLabel.addMouseListener(new GeneratorGUIMouseListener(this, numberOfConceptsHelpjLabel));
			solvablejLabel = new JLabel();
			solvablejLabel.setBounds(new Rectangle(15, 110, 53, 27));
			solvablejLabel.setText("Solvable");
			solutionDepthjLabel = new JLabel();
			solutionDepthjLabel.setBounds(new Rectangle(38, 219, 88, 27));
			solutionDepthjLabel.setText("Solution-Depth");
			solutionsjLabel = new JLabel();
			solutionsjLabel.setBounds(new Rectangle(15, 141, 61, 27));
			solutionsjLabel.setText("Solutions");			
			numberOfServicesjLabel = new JLabel();
			numberOfServicesjLabel.setBounds(new Rectangle(15, 71, 117, 27));
			numberOfServicesjLabel.setText("Number of Services");
			numberOfConceptsjLabel = new JLabel();
			numberOfConceptsjLabel.setText("Number of Concepts");
			numberOfConceptsjLabel.setBounds(new Rectangle(15, 29, 119, 27));
			inputDatajPanel = new JPanel();
			inputDatajPanel.setLayout(null);
			inputDatajPanel.setBounds(new Rectangle(9, 8, 329, 319));
			inputDatajPanel.setBorder(BorderFactory.createTitledBorder(null, "Input-Informations", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			inputDatajPanel.add(numberOfConceptsjLabel, null);
			inputDatajPanel.add(numberOfServicesjLabel, null);
			inputDatajPanel.add(getNumberOfConceptsjTextField(), null);
			inputDatajPanel.add(getNumberOfServicesjTextField(), null);
			inputDatajPanel.add(solutionsjLabel, null);			
			inputDatajPanel.add(getAddSolutionjButton(), null);
			inputDatajPanel.add(getRemoveSolutionjButton(), null);
			inputDatajPanel.add(solutionDepthjLabel, null);
			inputDatajPanel.add(getSolutionDepthjTextField(), null);
			inputDatajPanel.add(getSolutionsListjScrollPane(), null);
			inputDatajPanel.add(solvablejLabel, null);
			inputDatajPanel.add(getSolvablejCheckBox(), null);
			inputDatajPanel.add(numberOfConceptsHelpjLabel, null);
			inputDatajPanel.add(numberOfConceptsColonjLabel, null);
			inputDatajPanel.add(numberOfServicesHelpjLabel, null);
			inputDatajPanel.add(numberOfServicesColonjLabel, null);
			inputDatajPanel.add(solvableHelpjLabel, null);
			inputDatajPanel.add(solvableColonjLabel, null);
			inputDatajPanel.add(solutionsHelpjLabel, null);
			inputDatajPanel.add(solutionsColonjLabel, null);
			inputDatajPanel.add(solutionDepthHelpjLabel, null);
			inputDatajPanel.add(solutionDepthColonjLabel, null);
		}
		return inputDatajPanel;
	}

	/**
	 * This method initializes numberOfConceptsjTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getNumberOfConceptsjTextField() {
		if (numberOfConceptsjTextField == null) {
			numberOfConceptsjTextField = new JTextField();
			numberOfConceptsjTextField.setBounds(new Rectangle(171, 29, 132, 27));
			numberOfConceptsjTextField.setText("10000");
		}
		return numberOfConceptsjTextField;
	}

	/**
	 * This method initializes numberOfServicesjTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getNumberOfServicesjTextField() {
		if (numberOfServicesjTextField == null) {
			numberOfServicesjTextField = new JTextField();
			numberOfServicesjTextField.setBounds(new Rectangle(171, 71, 132, 27));
			numberOfServicesjTextField.setText("4000");
		}
		return numberOfServicesjTextField;
	}

	/**
	 * This method initializes outputDatajPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getOutputDatajPanel() {
		if (outputDatajPanel == null) {
			wslaFileNamejLabel = new JLabel();
			wslaFileNamejLabel.setBounds(new Rectangle(14, 204, 156, 26));
			wslaFileNamejLabel.setText("WSLA-File-Name:");
			owlFileNamejLabel = new JLabel();
			owlFileNamejLabel.setBounds(new Rectangle(14, 75, 115, 26));
			owlFileNamejLabel.setText("OWL-File-Name:");
			intermediateFilesjLabel = new JLabel();
			intermediateFilesjLabel.setBounds(new Rectangle(14, 318, 175, 24));
			intermediateFilesjLabel.setText("Generate Intermediate-Files:");
			outputFolderjLabel = new JLabel();
			outputFolderjLabel.setBounds(new Rectangle(14, 246, 89, 26));
			outputFolderjLabel.setText("Output-Folder:");
			servicesWSDLFileNamejLabel = new JLabel();
			servicesWSDLFileNamejLabel.setBounds(new Rectangle(14, 165, 156, 26));
			servicesWSDLFileNamejLabel.setText("Services-WSDL-File-Name:");
			taskWSDLFileNamejLabel = new JLabel();
			taskWSDLFileNamejLabel.setBounds(new Rectangle(14, 118, 156, 26));
			taskWSDLFileNamejLabel.setText("Task-WSDL-File-Name:");
			bpelFileNamejLabel = new JLabel();
			bpelFileNamejLabel.setBounds(new Rectangle(14, 35, 108, 26));
			bpelFileNamejLabel.setText("BPEL-File-Name:");
			outputDatajPanel = new JPanel();
			outputDatajPanel.setLayout(null);
			outputDatajPanel.setBounds(new Rectangle(8, 352, 329, 363));
			outputDatajPanel.setBorder(BorderFactory.createTitledBorder(null, "Output-Informations", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			outputDatajPanel.add(bpelFileNamejLabel, null);
			outputDatajPanel.add(getBpelFileNamejTextField(), null);
			outputDatajPanel.add(taskWSDLFileNamejLabel, null);
			outputDatajPanel.add(getTaskWSDLFileNamejTextField(), null);
			outputDatajPanel.add(servicesWSDLFileNamejLabel, null);
			outputDatajPanel.add(getServicesWSDLFileNamejTextField(), null);
			outputDatajPanel.add(outputFolderjLabel, null);
			outputDatajPanel.add(getOutputFolderjTextField(), null);
			outputDatajPanel.add(getBrowseOutputFolderjButton(), null);
			outputDatajPanel.add(intermediateFilesjLabel, null);
			outputDatajPanel.add(getIntermediateFilesjCheckBox(), null);
			outputDatajPanel.add(owlFileNamejLabel, null);
			outputDatajPanel.add(getOwlFileNamejTextField(), null);
			outputDatajPanel.add(wslaFileNamejLabel, null);
			outputDatajPanel.add(getWslaFileNamejTextField(), null);
		}
		return outputDatajPanel;
	}

	/**
	 * This method initializes bpelFileNamejTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getBpelFileNamejTextField() {
		if (bpelFileNamejTextField == null) {
			bpelFileNamejTextField = new JTextField();
			bpelFileNamejTextField.setBounds(new Rectangle(176, 35, 132, 26));
			bpelFileNamejTextField.setText("Solution");
		}
		return bpelFileNamejTextField;
	}

	/**
	 * This method initializes taskWSDLFileNamejTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getTaskWSDLFileNamejTextField() {
		if (taskWSDLFileNamejTextField == null) {
			taskWSDLFileNamejTextField = new JTextField();
			taskWSDLFileNamejTextField.setBounds(new Rectangle(176, 118, 132, 26));
			taskWSDLFileNamejTextField.setText("Challenge");
		}
		return taskWSDLFileNamejTextField;
	}

	/**
	 * This method initializes servicesWSDLFileNamejTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getServicesWSDLFileNamejTextField() {
		if (servicesWSDLFileNamejTextField == null) {
			servicesWSDLFileNamejTextField = new JTextField();
			servicesWSDLFileNamejTextField.setBounds(new Rectangle(176, 165, 132, 26));
			servicesWSDLFileNamejTextField.setText("Services");
		}
		return servicesWSDLFileNamejTextField;
	}

	/**
	 * This method initializes outputFolderjTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getOutputFolderjTextField() {
		if (outputFolderjTextField == null) {
			outputFolderjTextField = new JTextField();
			outputFolderjTextField.setBounds(new Rectangle(112, 246, 196, 26));
		}
		return outputFolderjTextField;
	}

	/**
	 * This method initializes browseOutputFolderjButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBrowseOutputFolderjButton() {
		if (browseOutputFolderjButton == null) {
			browseOutputFolderjButton = new JButton();
			browseOutputFolderjButton.setBounds(new Rectangle(229, 280, 79, 26));
			browseOutputFolderjButton.setActionCommand("Browse Output Folder");
			browseOutputFolderjButton.setText("Browse");
			browseOutputFolderjButton.addActionListener(new GeneratorGUIListener(this));
		}
		return browseOutputFolderjButton;
	}

	/**
	 * This method initializes solutionsjList	
	 * 	
	 * @return javax.swing.JList	
	 */
	public JList getSolutionsjList() {
		if (solutionsjList == null) {
			DefaultListModel defaultListModel = new DefaultListModel();
			// Add some default-values.
			defaultListModel.addElement(10);
			//defaultListModel.addElement(20);
			//defaultListModel.addElement(30);
			//defaultListModel.addElement(40);
			solutionsjList = new JList(defaultListModel);			
			solutionsjList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			//((DefaultListCellRenderer)solutionsjList.getCellRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		}
		return solutionsjList;
	}

	/**
	 * This method initializes addSolutionjButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getAddSolutionjButton() {
		if (addSolutionjButton == null) {
			addSolutionjButton = new JButton();
			addSolutionjButton.setBounds(new Rectangle(38, 259, 85, 29));
			addSolutionjButton.setActionCommand("Add Solution");
			addSolutionjButton.setText("Add");
			addSolutionjButton.addActionListener(new GeneratorGUIListener(this));
		}
		return addSolutionjButton;
	}

	/**
	 * This method initializes removeSolutionjButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getRemoveSolutionjButton() {
		if (removeSolutionjButton == null) {
			removeSolutionjButton = new JButton();
			removeSolutionjButton.setBounds(new Rectangle(206, 260, 97, 28));
			removeSolutionjButton.setActionCommand("Remove Solution");
			removeSolutionjButton.setText("Remove");
			removeSolutionjButton.addActionListener(new GeneratorGUIListener(this));
		}
		return removeSolutionjButton;
	}

	/**
	 * This method initializes solutionDepthjTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getSolutionDepthjTextField() {
		if (solutionDepthjTextField == null) {
			solutionDepthjTextField = new JTextField();
			solutionDepthjTextField.setBounds(new Rectangle(171, 219, 132, 27));
		}
		return solutionDepthjTextField;
	}

	/**
	 * This method initializes startjButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getStartjButton() {
		if (startjButton == null) {
			startjButton = new JButton();
			startjButton.setBounds(new Rectangle(8, 732, 122, 28));
			startjButton.setText("Start");
			startjButton.addActionListener(new GeneratorGUIListener(this));
		}
		return startjButton;
	}

	/**
	 * This method initializes intermediateFilesjCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	public JCheckBox getIntermediateFilesjCheckBox() {
		if (intermediateFilesjCheckBox == null) {
			intermediateFilesjCheckBox = new JCheckBox();
			intermediateFilesjCheckBox.setBounds(new Rectangle(187, 318, 33, 22));
		}
		return intermediateFilesjCheckBox;
	}

	/**
	 * This method initializes owlFileNamejTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getOwlFileNamejTextField() {
		if (owlFileNamejTextField == null) {
			owlFileNamejTextField = new JTextField();
			owlFileNamejTextField.setBounds(new Rectangle(176, 75, 132, 26));
			owlFileNamejTextField.setText("Taxonomy");
			owlFileNamejTextField.setToolTipText("");
		}
		return owlFileNamejTextField;
	}

	/**
	 * This method initializes solutionsListjScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSolutionsListjScrollPane() {
		if (solutionsListjScrollPane == null) {
			solutionsListjScrollPane = new JScrollPane();
			solutionsListjScrollPane.setBounds(new Rectangle(170, 141, 133, 64));
			solutionsListjScrollPane.setViewportView(getSolutionsjList());
		}
		return solutionsListjScrollPane;
	}

	/**
	 * This method initializes solvablejCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	public JCheckBox getSolvablejCheckBox() {
		if (solvablejCheckBox == null) {
			solvablejCheckBox = new JCheckBox();
			solvablejCheckBox.setBounds(new Rectangle(167, 111, 17, 18));
			solvablejCheckBox.setSelected(true);
			solvablejCheckBox.setName("solvableCheckBox");
			solvablejCheckBox.addItemListener(new GeneratorGUIListener(this));
		}
		return solvablejCheckBox;
	}

	/**
	 * This method initializes infojButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getInfojButton() {
		if (infojButton == null) {
			infojButton = new JButton();
			infojButton.setBounds(new Rectangle(262, 732, 74, 28));
			infojButton.setText("Info");
			infojButton.addActionListener(new GeneratorGUIListener(this));
		}
		return infojButton;
	}

	/**
	 * This method initializes wslaFileNamejTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getWslaFileNamejTextField() {
		if (wslaFileNamejTextField == null) {
			wslaFileNamejTextField = new JTextField();
			wslaFileNamejTextField.setBounds(new Rectangle(176, 205, 132, 26));
			wslaFileNamejTextField.setText("Servicelevelagreements");
		}
		return wslaFileNamejTextField;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GeneratorGUI thisClass = new GeneratorGUI();
				
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
				
				if (Runtime.getRuntime().maxMemory() < 524288000) {					
					thisClass.warningPopup();
				}
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public GeneratorGUI() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(352, 801);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("Generator");
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
			jContentPane.add(getInputDatajPanel(), null);
			jContentPane.add(getOutputDatajPanel(), null);
			jContentPane.add(getStartjButton(), null);
			jContentPane.add(getInfojButton(), null);
		}
		return jContentPane;
	}

	/**
	 * @return the solutionDepthjLabel
	 */
	public JLabel getSolutionDepthjLabel() {
		return solutionDepthjLabel;
	}

	/**
	 * @return the solutionsjLabel
	 */
	public JLabel getSolutionsjLabel() {
		return solutionsjLabel;
	}

	/**
	 * @return the solutionDepthColonjLabel
	 */
	public JLabel getSolutionDepthColonjLabel() {
		return solutionDepthColonjLabel;
	}

	/**
	 * @return the solutionDepthHelpjLabel
	 */
	public JLabel getSolutionDepthHelpjLabel() {
		return solutionDepthHelpjLabel;
	}

	/**
	 * @return the solutionsColonjLabel
	 */
	public JLabel getSolutionsColonjLabel() {
		return solutionsColonjLabel;
	}

	/**
	 * @return the solutionsHelpjLabel
	 */
	public JLabel getSolutionsHelpjLabel() {
		return solutionsHelpjLabel;
	}
	
	/**
	 * Handles the "Warning"-popup.
	 * Displays some informations about us.
	 */
	public void warningPopup() {
		// Read info-file.
		BufferedReader infoFileReader = new BufferedReader(new InputStreamReader(GeneratorGUIListener.class.getClassLoader().getResourceAsStream(GeneratorGUIListener.warningFilePath)));
		StringBuffer infoFileText = new StringBuffer();
		String line = null;
		
		try {
			while((line = infoFileReader.readLine()) != null) {
				infoFileText.append(line);
				infoFileText.append("\n");
			}
		} catch (IOException exception) {
			System.err.println("GeneratorGUIListener: An error occurred during the reading of the info-file at "+GeneratorGUIListener.warningFilePath);
			exception.printStackTrace();
			return;
		}
		
		JOptionPane.showMessageDialog(this, infoFileText.toString(),"Low memory warning!",JOptionPane.INFORMATION_MESSAGE);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

package de.vs.unikassel.generator.gui.listener;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import de.vs.unikassel.generator.gui.GeneratorGUI;

/**
 * Handels the mouse-events of the GeneratorGUI.
 * @author Marc Kirchhoff
 *
 */
public class GeneratorGUIMouseListener extends MouseAdapter{
	
	/**
	 * The "GeneratorGUI"-frame.
	 */
	private GeneratorGUI generatorGUI;
	
	/**
	 * The corresponding "(?)"-label.
	 */
	private JLabel helpLabel;
	
	/**
	 * The info-file-pathes.
	 */
	public static String numberOfConceptsInfoFilePath = "de/vs/unikassel/generator/gui/listener/helpfiles/NumberOfConcepts.txt";
	public static String numberOfServicesInfoFilePath = "de/vs/unikassel/generator/gui/listener/helpfiles/NumberOfServices.txt";
	public static String solutionDepthInfoFilePath = "de/vs/unikassel/generator/gui/listener/helpfiles/SolutionDepth.txt";
	public static String solutionsInfoFilePath = "de/vs/unikassel/generator/gui/listener/helpfiles/Solutions.txt";
	public static String solvableFilePath = "de/vs/unikassel/generator/gui/listener/helpfiles/Solvable.txt";
	
	/**
	 * Creates a new listener with the given generator-gui-frame and help-label.
	 * @param generatorGUI The "GeneratorGUI"-frame.
	 * @param helpLabel The corresponding "(?)"-label.
	 */
	public GeneratorGUIMouseListener(GeneratorGUI generatorGUI, JLabel helpLabel) {
		this.generatorGUI = generatorGUI;
		this.helpLabel = helpLabel;
	}
	
	/**
	 * Reads the given file and returns the content of the file as a string.
	 * @param infoFilePath The path of the file.
	 * @return The content of the file as a string.
	 */
	private String readInfoFile(String infoFilePath) {
		
		// Read info-file.
		BufferedReader infoFileReader = new BufferedReader(new InputStreamReader(GeneratorGUIListener.class.getClassLoader().getResourceAsStream(infoFilePath)));
		StringBuffer infoFileText = new StringBuffer();
		String line = null;
		
		try {
			while((line = infoFileReader.readLine()) != null) {
				infoFileText.append(line);
				infoFileText.append("\n");
			}
		} catch (IOException exception) {
			System.err.println("GeneratorGUIMouseListener: An error occurred during the reading of the info-file at "+infoFilePath);
			exception.printStackTrace();
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
				
		if(helpLabel.getName().equals("numberOfConceptsHelp")) {
			titleText = "Number of Concepts";
			infoFileText = readInfoFile(GeneratorGUIMouseListener.numberOfConceptsInfoFilePath);			
		}
		else if(helpLabel.getName().equals("numberOfServicesHelp")) {
			titleText = "Number of Services";
			infoFileText = readInfoFile(GeneratorGUIMouseListener.numberOfServicesInfoFilePath);			
		}
		else if(helpLabel.getName().equals("solvableHelp")) {
			titleText = "Solvable";
			infoFileText = readInfoFile(GeneratorGUIMouseListener.solvableFilePath);			
		}
		else if(helpLabel.getName().equals("solutionsHelp")) {
			titleText = "Solutions";
			infoFileText = readInfoFile(GeneratorGUIMouseListener.solutionsInfoFilePath);			
		}
		else if(helpLabel.getName().equals("solutionDepthHelp")) {
			titleText = "Solution-Depth";
			infoFileText = readInfoFile(GeneratorGUIMouseListener.solutionDepthInfoFilePath);			
		}
		else{
			System.err.println("GeneratorGUIMouseListener: Incorrect label-name.");
			return;
		}	
		
		JOptionPane.showMessageDialog(this.generatorGUI, infoFileText,titleText,JOptionPane.INFORMATION_MESSAGE);
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

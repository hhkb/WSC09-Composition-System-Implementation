package de.vs.unikassel.generator.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.sfc.utils.Utils;

import test.org.sigoa.wsc.c2008.generator.Concept;
import test.org.sigoa.wsc.c2008.generator.Problem;
import test.org.sigoa.wsc.c2008.generator.Rand;
import de.vs.unikassel.generator.converter.bpel_creator.BPEL_Creator;
import de.vs.unikassel.generator.converter.owl_creator.OWL_Creator;
import de.vs.unikassel.generator.converter.wsdl_creator.ServiceConverter;
import de.vs.unikassel.generator.converter.wsdl_creator.TaskConverter;
import de.vs.unikassel.generator.converter.wsla_creator.WSLASingleton;
import de.vs.unikassel.generator.gui.GeneratorGUI;
import de.vs.unikassel.generator.gui.ProgressDialog;

/**
 * A class to create some test-files.
 * @author Marc Kirchhoff
 *
 */
class TaskGenerator implements Runnable {

	/**
	 * The number of concepts the taxonomy should contain.
	 */
	private int numberOfConcepts;
	
	/**
	 * Should the problem be solvable.
	 */
	private boolean solvableProblem;
	
	/**
	 * The depths of the solutions.
	 */
	private int[] solutionDepths;
	
	/**
	 * The number of services the generator should create.
	 */
	private int numberOfServices;
	
	/**
	 * The output-folder that should contain the output-files.
	 */
	private File outputFolder;
	
	/**
	 * The name of the BPEL-file that the generator should create.
	 */
	private String bpelFileName;
	
	/**
	 * The name of the Services-WSDL-File that the generator should create.
	 */
	private String serviceWSDLFileName;
	
	/**
	 * The name of the Task-WSDL-file that the generator should create.
	 */
	private String taskWSDLFileName;
	
	/**
	 * The name of the QoS-WSLA-file that the generator should create.
	 */
	private String WSLAFileName;
	
	/**
	 * The name of the OWL-File that the generator should create.
	 */
	private String owlFileName;
	
	/**
	 * Create the intermediate-files?
	 */
	private boolean generateIntermediateFiles;
	
	/**
	 * The dialog that displays the progress-panel.
	 */
	private ProgressDialog progressDialog;
		
	/**
	 * Creates a TaskGenerator.
	 * @param numberOfConcepts The number of concepts the taxonomy should contain.
	 * @param solvableProblem Should the problem be solvable.
	 * @param solutionDepths The depths of the solutions.
	 * @param numberOfServices The number of services the generator should create.
	 * @param outputFolder The output-folder that should contain the output-files.
	 * @param bpelFileName The name of the BPEL-file that the generator should create.
	 * @param serviceWSDLFileName The name of the Services-WSDL-File that the generator should create.
	 * @param taskWSDLFileName The name of the Task-WSDL-file that the generator should create.
	 * @param owlFileName The name of the OWL-File that the generator should create.
	 * @param generateIntermediateFiles The dialog that displays the progress-panel.
	 */
	public TaskGenerator(int numberOfConcepts, boolean solvableProblem, int[] solutionDepths, int numberOfServices,
			File outputFolder, String bpelFileName, String serviceWSDLFileName, String taskWSDLFileName, String owlFileName, String wslafilename, boolean generateIntermediateFiles) {
		this.numberOfConcepts = numberOfConcepts;
		this.solvableProblem = solvableProblem;
		this.solutionDepths = solutionDepths;
		this.numberOfServices = numberOfServices;
		this.outputFolder = outputFolder;
		this.bpelFileName = bpelFileName;
		this.serviceWSDLFileName = serviceWSDLFileName;
		this.taskWSDLFileName = taskWSDLFileName;
		this.owlFileName = owlFileName;
		this.WSLAFileName = wslafilename;
		this.generateIntermediateFiles = generateIntermediateFiles;
	}
	
	/**
	 * Removes the given services from a service-description-file.
	 * @param servicesInputStream The service-description-file.
	 * @param services The names of the services.
	 * @return The service-description-document without the given services.
	 */
	private ByteArrayOutputStream removeServices(InputStream servicesInputStream, Vector<String> services) {

		
		try {
			Document doc = new SAXBuilder().build( servicesInputStream );
			
			// Remove the given service-elements.
			List<Element> serviceElements = doc.getRootElement().getChildren("service");
			for(Element element : serviceElements) {
				if(services.contains(element.getAttributeValue("name"))) {
					doc.getRootElement().removeContent(element);
					break;
				}
			}
			
			ByteArrayOutputStream wsdl = new ByteArrayOutputStream();
			XMLOutputter out = new XMLOutputter( Format.getPrettyFormat() ); 
			out.output( doc, wsdl );
			
			return wsdl;
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Removes all services from the service-description-file that provide at least one of the specified output-instances.
	 * @param servicesInputStream The service-description-file.
	 * @param outputs The output-instance.
	 * @return The service-description-document without the services.
	 */
	private ByteArrayOutputStream removeAllServicesWithSpecifiedOutput(InputStream servicesInputStream, Vector<String> outputs) {
		
		try {
			Document doc = new SAXBuilder().build( servicesInputStream );
			
			// Remove the given service-elements.
			List<Element> serviceElements = doc.getRootElement().getChildren("service");
			Vector<Element> removeServicesElements = new Vector<Element>();
			
			for(Element serviceElement : serviceElements) {
				Element outputElement = serviceElement.getChild("outputs");
				List<Element> outputInstanceElements = outputElement.getChildren("instance");
				
				for(Element outputInstanceElement : outputInstanceElements) {
					
					if(outputs.contains(outputInstanceElement.getAttributeValue("name"))) {
						//doc.getRootElement().removeContent(serviceElement);
						removeServicesElements.add(serviceElement);
						break;
					}
				}
			}
			
			for(Element serviceElement : removeServicesElements) {
				doc.getRootElement().removeContent(serviceElement);
			}
			
			ByteArrayOutputStream wsdl = new ByteArrayOutputStream();
			XMLOutputter out = new XMLOutputter( Format.getPrettyFormat() ); 
			out.output( doc, wsdl );
			
			return wsdl;
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Creates a new concept and a new instance and adds it to the taxonomy below the first concept.
	 * @param taxonomyInputStream The taxonomy-file.
	 * @param newInstanceName This parameter will contain the name of the new generated instance after the execution.
	 * @return The taxonomy-file with the new concept and new instance. 
	 */
	private ByteArrayOutputStream createNewConceptAndInstance(InputStream taxonomyInputStream, StringBuffer newInstanceName) {
				
		try {
			Document doc = new SAXBuilder().build( taxonomyInputStream );
			
			// Get the first concept-element.
			Element firstConceptElement = doc.getRootElement().getChild("concept");
			
			// Create the new concept-element.
			String conceptName = "con"+Rand.nextId();
			String instanceName = "inst"+Rand.nextId();
			newInstanceName.append(instanceName);
			
			Element conceptElement = new Element("concept");
			conceptElement.setAttribute("name", conceptName);
			
			Element instanceElement = new Element("instance");
			instanceElement.setAttribute("name", instanceName);
			
			conceptElement.addContent(instanceElement);
			
			// Add the new concept to the first concept-element of the taxonomy.
			firstConceptElement.addContent(conceptElement);
			
			ByteArrayOutputStream taxonomy = new ByteArrayOutputStream();
			XMLOutputter out = new XMLOutputter( Format.getPrettyFormat() ); 
			out.output( doc, taxonomy );
			return taxonomy;
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return null;
	}
	
	private ByteArrayOutputStream addNewInputInstance(InputStream servicesInputStream, Vector<String> outputConcepts, String newInstanceName) {
		try {
			Document doc = new SAXBuilder().build( servicesInputStream );
			
			// Get all service-elements.
			List<Element> serviceElements = doc.getRootElement().getChildren("service");
			
			for(Element serviceElement : serviceElements) {
				Element outputElement = serviceElement.getChild("outputs");
				List<Element> outputInstanceElements = outputElement.getChildren("instance");
				
				// Check if this service provides one of the specified output-elements.
				boolean found = false;
				for(Element outputInstanceElement : outputInstanceElements) {					
					if(outputConcepts.contains(outputInstanceElement.getAttributeValue("name"))) {
						found = true;
						break;
					}
				}
				
				// Add the new instance to the input-elements.
				if(found) {					
					// Create the new input-element.
					Element newInputElement = new Element("instance");
					newInputElement.setAttribute("name", newInstanceName);
					
					// Add the new input-element to the input-instances.
					Element inputsElement = serviceElement.getChild("inputs");
					inputsElement.addContent(newInputElement);					
				}				
			}
			
			ByteArrayOutputStream serviceDescription = new ByteArrayOutputStream();
			XMLOutputter out = new XMLOutputter( Format.getPrettyFormat() ); 
			out.output( doc, serviceDescription );
			
			return serviceDescription;
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Collects all sub-concepts of the specified concept.
	 * @param concept The concept that sub-concepts should be added to the specified vector.
	 * @param allConcepts Contains all concepts after the execution of this method.
	 */
	private void getAllConcepts(Concept concept, Vector<String> allConcepts) {
		if(concept.getM_children() == null) {
			return;
		}
		
		for(int i = 0; i < concept.getM_children().size(); ++i )  {
			Concept currentConcept = concept.getM_children().get(i);
			if(currentConcept == null) {
				continue;
			}
			
			// Add the name of the concept to the vector.
			StringBuilder conceptName = new StringBuilder();
			currentConcept.toStringBuilder(conceptName);
			allConcepts.add(conceptName.toString());
			
			this.getAllConcepts(currentConcept, allConcepts);
		}
	}
		
	/**
	 * Creates a new task.
	 */
	private void createTask() {
		// Create the intermediate-files.
		Concept concepts = null;		
		Problem problem = null;
		
		do {
			concepts = null;	
			problem = null;
			
			Utils.invokeGC();
			concepts = Concept.createConceptTree(this.numberOfConcepts);
			problem =  Problem.buildProblem(concepts, this.solvableProblem ? this.solutionDepths : new int[] { 1 });
			System.out.println("Please wait...");
		} while (problem == null);
		
		ByteArrayOutputStream conceptsByteArrayOutputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream problemArrayOutputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream servicesArrayOutputStream = new ByteArrayOutputStream();			
		
		concepts.serialize(conceptsByteArrayOutputStream);		
		problem.serializeProblemFile(problemArrayOutputStream);
		problem.serializeServiceFile(servicesArrayOutputStream);
		problem.generateServices(this.numberOfServices);
		
		//ByteArrayInputStream conceptsByteArrayInputStream = new ByteArrayInputStream(conceptsByteArrayOutputStream.toByteArray());
		//ByteArrayInputStream problemByteArrayInputStream = new ByteArrayInputStream(problemArrayOutputStream.toByteArray());		
		
		if(!this.solvableProblem) {
			
			StringBuffer newInstanceName = new StringBuffer();
			conceptsByteArrayOutputStream = this.createNewConceptAndInstance(new ByteArrayInputStream(conceptsByteArrayOutputStream.toByteArray()), newInstanceName);
			
			Concept oneNecessaryOutputConcept = problem.getM_out().get(0);
			Vector<String> allSubConccepts = new Vector<String>();
			this.getAllConcepts(oneNecessaryOutputConcept, allSubConccepts);
			
			servicesArrayOutputStream = this.addNewInputInstance(new ByteArrayInputStream(servicesArrayOutputStream.toByteArray()), allSubConccepts, newInstanceName.toString());
			
		}	
				
		// Create the service-wsdl-document.
		ServiceConverter serviceConverter = new ServiceConverter(new ByteArrayInputStream(servicesArrayOutputStream.toByteArray()));
		Thread serviceConverterThread = new Thread(serviceConverter);
		serviceConverterThread.start();
		
		// Create the wsdl-request-document.
		TaskConverter taskConverter = new TaskConverter(new ByteArrayInputStream(problemArrayOutputStream.toByteArray()));
		Thread taskConverterThread = new Thread(taskConverter);
		taskConverterThread.start();
					
		// Create the owl-document.			
		OWL_Creator owlCreator = new OWL_Creator(new ByteArrayInputStream(conceptsByteArrayOutputStream.toByteArray()));
		Thread owlCreatorThread = new Thread(owlCreator);
		owlCreatorThread.start();
		
		// Wait until all threads are stopped.
		try {
			if(this.solvableProblem) {
				// Create the bpel-document.
				BPEL_Creator bpelCreator = new BPEL_Creator(new ByteArrayInputStream(problemArrayOutputStream.toByteArray()));
				Thread bpelCreatorThread = new Thread(bpelCreator);
				bpelCreatorThread.start();
				bpelCreatorThread.join();
				bpelCreator.saveBPELDocument(new File(this.outputFolder, this.bpelFileName));
			}
			
			serviceConverterThread.join();
			taskConverterThread.join();
			owlCreatorThread.join();
			
		} catch (InterruptedException exception) {
			System.err.println("GeneratorGUI: An error occurred during creation of the documents.");
			exception.printStackTrace();
			return;
		}
		
		// Save the created documents.
		serviceConverter.saveWSDLFile(new File(this.outputFolder,this.serviceWSDLFileName));
		taskConverter.saveWSDLFile(new File(this.outputFolder, this.taskWSDLFileName));		
		owlCreator.saveOWLFile(new File(this.outputFolder, this.owlFileName));
		WSLASingleton.saveWSLAFile(new File(this.outputFolder,this.WSLAFileName));
		
		// Save Intermediate-Files?
		if(generateIntermediateFiles) {
			File taxonomyFile = new File(this.outputFolder, "taxonomy.xml");
			File problemFile = new File(this.outputFolder, "problem.xml");
			File servicesFile = new File(this.outputFolder, "services.xml");
							
			try {
				FileWriter taxonomyFileWriter = new FileWriter(taxonomyFile);
				FileWriter problemFileWriter = new FileWriter(problemFile);
				FileWriter servicesFileWriter = new FileWriter(servicesFile);
				
				taxonomyFileWriter.write(conceptsByteArrayOutputStream.toString());
				problemFileWriter.write(problemArrayOutputStream.toString());
				servicesFileWriter.write(servicesArrayOutputStream.toString());
				
				taxonomyFileWriter.flush();
				problemFileWriter.flush();
				servicesFileWriter.flush();
				
				taxonomyFileWriter.close();
				problemFileWriter.close();
				servicesFileWriter.close();
				
			} catch (IOException exception) {
				System.err.println("GeneratorGUI: An error occurred during saving of the intermediate-files.");
				exception.printStackTrace();
				return;
			}
		}		
	}
	
	/**
	 * Starts the creation of the new task.
	 */
	@Override
	public void run() {
		
		// Create the new task.
		createTask();
		
		// Disable the stop-button and the progress-bar.		
		this.progressDialog.getStopjButton().setEnabled(false);
		this.progressDialog.getCreationjProgressBar().setIndeterminate(false);
	}

	/**
	 * @param progressDialog the progressDialog to set
	 */
	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}	
}

/**
 * The listener of the GeneratorGUI-frame.
 * @author Marc Kirchhoff
 */
public class GeneratorGUIListener implements ActionListener, ItemListener{
	
	/**
	 * The GeneratorGUI-frame.
	 */
	private GeneratorGUI generatorGUI;
	
	/**
	 * The path of the info-file which contains some contact-informations.
	 */
	public static String infoFilePath = "de/vs/unikassel/generator/gui/listener/infos.txt";
	
	/**
	 * The path of the warning-file which contains some contact-informations.
	 */
	public static String warningFilePath = "de/vs/unikassel/generator/gui/listener/warning.txt";
	
	/**
	 * Creates a new Listener with the given GeneratorGUI-frame.
	 * @param generatorGUI The GeneratorGUI-frame.
	 */
	public GeneratorGUIListener(GeneratorGUI generatorGUI) {
		this.generatorGUI = generatorGUI;
	}
	
	/**
	 * Handles the "Add Solution"-Button.
	 */
	private void addSolution() {
		// Get the solution-depth.
		String solutionDepthS = this.generatorGUI.getSolutionDepthjTextField().getText();
		this.generatorGUI.getSolutionDepthjTextField().setText("");
		
		// Do some error-checking.
		if(solutionDepthS == null || solutionDepthS.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the solution-depth!","Incorrect Solution-Depth",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getSolutionDepthjTextField().requestFocus();
			return;
		}
		
		// Convert the solution-depth into an integer.
		int solutionDepth = -1;
		try {
			solutionDepth = Integer.parseInt(solutionDepthS);
		}
		catch(NumberFormatException numberFormatException) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The solution-depth must be a number!","Incorrect Solution-Depth",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getSolutionDepthjTextField().requestFocus();
			return;
		}
		
		if(solutionDepth <= 0) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The solution-depth must be greater than 0!","Incorrect Solution-Depth",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getSolutionDepthjTextField().requestFocus();
			return;
		}
		
		// Add the solution to the list.
		((DefaultListModel)this.generatorGUI.getSolutionsjList().getModel()).addElement(solutionDepth);
	}
	
	/**
	 * Handles the "Remove Solution"-button.
	 */
	private void removeSolution() {
		// Get the selected index.
		int selectedIndex = this.generatorGUI.getSolutionsjList().getSelectedIndex();
		if(selectedIndex == -1) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please select a solution!","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		((DefaultListModel)this.generatorGUI.getSolutionsjList().getModel()).remove(selectedIndex);
	}
	
	/**
	 * Handles the "Browse Output Folder"-button.
	 */
	private void browseOutputFolder() {
		// Create a file-chooser.
		JFileChooser fileChooser=new JFileChooser();
		
		// Restrict the selection to directories.
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		// Set the file-filter. Only directories are shown.
		fileChooser.setFileFilter(new FileFilter() {	
			
			@Override
			public boolean accept(File file) {
				if(file.isDirectory()) {
					return true;
				}
				else {
					return false;
				}
			}

			@Override
			public String getDescription() {
				return "Folders";					
			}
		});
		
		// Display the file-chooser.
		int option = fileChooser.showOpenDialog(this.generatorGUI);
		
		// Check the return-status.
		if(option == JFileChooser.APPROVE_OPTION) {
			String pathOutputFolder = fileChooser.getSelectedFile().getAbsolutePath();
			this.generatorGUI.getOutputFolderjTextField().setText(pathOutputFolder);
		}
	}
	
	/**
	 * Handles the "Start"-button.
	 */
	private void startButton() {
		// Read the number of concepts.
		String numberOfConceptsS = this.generatorGUI.getNumberOfConceptsjTextField().getText();
		
		if(numberOfConceptsS == null || numberOfConceptsS.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the number of concepts!","Missing Number of Concepts",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getNumberOfConceptsjTextField().requestFocus();
			return;
		}
		
		// Convert number of concepts.
		int numberOfConcepts = -1;
		try {
			numberOfConcepts = Integer.parseInt(numberOfConceptsS);
		}
		catch(NumberFormatException numberFormatException) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The \"Number-Of-Concepts\" must be a number!","Incorrect Number-Of-Concepts",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getNumberOfConceptsjTextField().requestFocus();
			return;
		}
		
		if(numberOfConcepts <= 0) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The \"Number-Of-Concepts\" must be greater than 0!","Incorrect Number-Of-Concepts",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getNumberOfConceptsjTextField().requestFocus();
			return;
		}
		
		// Read the number of services.
		String numberOfServicesS = this.generatorGUI.getNumberOfServicesjTextField().getText();
		
		if(numberOfServicesS == null || numberOfServicesS.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the number of services!","Missing Number of Services",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getNumberOfServicesjTextField().requestFocus();
			return;
		}
		
		
		
		// Convert number of services.
		int numberOfServices = -1;
		try {
			numberOfServices = Integer.parseInt(numberOfServicesS);
		}
		catch(NumberFormatException numberFormatException) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The \"Number-Of-Services\" must be a number!","Incorrect Number-Of-Services",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getNumberOfServicesjTextField().requestFocus();
			return;
		}
		
		if(numberOfServices <= 0) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The \"Number-Of-Services\" must be greater than 0!","Incorrect Number-Of-Services",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getNumberOfServicesjTextField().requestFocus();
			return;
		}
		
		
		
		// Read the solvable-problem-checkbox.
		boolean solvableProblem = this.generatorGUI.getSolvablejCheckBox().isSelected();
		
		// Read the solutions-list.
		int[] solutionDepths = null;			
		if(solvableProblem) {
			
			// Get the solution-list-elements.
			Object[] solutionDepthsO = ((DefaultListModel)this.generatorGUI.getSolutionsjList().getModel()).toArray();
			
			if(solutionDepthsO == null || solutionDepthsO.length < 1) {
				JOptionPane.showMessageDialog(this.generatorGUI, "Please enter a solution!","Missing Solution",JOptionPane.ERROR_MESSAGE);
				this.generatorGUI.getSolutionDepthjTextField().requestFocus();
				return;
			}
			
			solutionDepths = new int[solutionDepthsO.length];
			int completeSolutionDepth = 0;
			
			// Convert the values to integer.
			for(int i = 0;i < solutionDepthsO.length; ++i) {
				solutionDepths[i] = (Integer)solutionDepthsO[i];
				completeSolutionDepth += solutionDepths[i];
			}
			
			// Calculate the min number of concepts.
			int minNumberOfConcepts = completeSolutionDepth * completeSolutionDepth;			
			if(numberOfConcepts < minNumberOfConcepts) {
				int result = JOptionPane.showConfirmDialog(this.generatorGUI, "The \"Number-Of-Concepts\" should be at least "+minNumberOfConcepts+"!\n Ignore?","Incorrect Number-Of-Concepts", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
				if(result != JOptionPane.YES_OPTION) {
					this.generatorGUI.getNumberOfConceptsjTextField().setText(String.valueOf(minNumberOfConcepts));
					this.generatorGUI.getNumberOfConceptsjTextField().requestFocus();
					return;
				}
			}
		}
		
		
		
		// Read the output-folder-path.
		String outputFolderPath = this.generatorGUI.getOutputFolderjTextField().getText();
		
		if(outputFolderPath == null || outputFolderPath.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the path of the output-folder!","Incorrect Output-Folder",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getOutputFolderjTextField().requestFocus();
			return;
		}
		
		File outputFolder = new File(outputFolderPath);
		if(!outputFolder.exists()) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The output-folder doesn't exist!","Incorrect Output-Folder",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getOutputFolderjTextField().requestFocus();
			return;
		}
		
		if(!outputFolder.isDirectory()) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The output-folder must be a directory!","Incorrect Output-Folder",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getOutputFolderjTextField().requestFocus();
			return;
		}
					
		
		
		// Read the name of the bpel-file.
		String bpelFileName = this.generatorGUI.getBpelFileNamejTextField().getText();
		
		if(bpelFileName == null || bpelFileName.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the name of the BPEL-file!","Missing BPEL-File-Name",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getBpelFileNamejTextField().requestFocus();
			return;
		}
		
		// Correct file-extension.
		if(!bpelFileName.endsWith(".bpel")) {
			bpelFileName = bpelFileName+".bpel";
		}			
		
		// Check if the file already exists.
		if(containsFile(outputFolder, bpelFileName)) {
			int result = JOptionPane.showConfirmDialog(this.generatorGUI, "The file "+bpelFileName+" already exists!\n Overwrite?","File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if(result != JOptionPane.YES_OPTION) {
				this.generatorGUI.getBpelFileNamejTextField().requestFocus();
				return;
			}
		}
		
		
		
		// Read the owl-file-name.
		String owlFileName = this.generatorGUI.getOwlFileNamejTextField().getText();
		
		if(owlFileName == null || owlFileName.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the name of the OWL-file!","Missing OWL-File-Name",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getOwlFileNamejTextField().requestFocus();
			return;
		}
		
		// Correct file-extension.
		if(!owlFileName.endsWith(".owl")) {
			owlFileName = owlFileName+".owl";
		}			
		
		// Check if the file already exists.
		if(containsFile(outputFolder, owlFileName)) {
			int result = JOptionPane.showConfirmDialog(this.generatorGUI, "The file "+owlFileName+" already exists!\n Overwrite?","File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if(result != JOptionPane.YES_OPTION) {
				this.generatorGUI.getOwlFileNamejTextField().requestFocus();
				return;
			}
		}
		
		// Read name of the task-wsdl-file.
		String taskWSDLFileName = this.generatorGUI.getTaskWSDLFileNamejTextField().getText();
		
		if(taskWSDLFileName == null || taskWSDLFileName.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the name of the Task-WSDL-file!","Missing Task-WSDL-File-Name",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getTaskWSDLFileNamejTextField().requestFocus();
			return;
		}
		
		// Correct file-extension.
		if(!taskWSDLFileName.endsWith(".wsdl")) {
			taskWSDLFileName = taskWSDLFileName+".wsdl";
		}
		
		// Check if the file already exists.
		if(containsFile(outputFolder, taskWSDLFileName)) {
			int result = JOptionPane.showConfirmDialog(this.generatorGUI, "The file "+taskWSDLFileName+" already exists!\n Overwrite?","File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if(result != JOptionPane.YES_OPTION) {
				this.generatorGUI.getTaskWSDLFileNamejTextField().requestFocus();
				return;
			}
		}	
		
		// Read name of the task-wsla-file.
		String WSLAFileName = this.generatorGUI.getWslaFileNamejTextField().getText();
		
		if(WSLAFileName == null || WSLAFileName.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the name of the Task-WSLA-file!","Missing Task-WSLA-File-Name",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getWslaFileNamejTextField().requestFocus();
			return;
		}
		
		// Correct file-extension.
		if(!WSLAFileName.endsWith(".wsla")) {
			WSLAFileName = WSLAFileName+".wsla";
		}
		
		// Check if the file already exists.
		if(containsFile(outputFolder, WSLAFileName)) {
			int result = JOptionPane.showConfirmDialog(this.generatorGUI, "The file "+WSLAFileName+" already exists!\n Overwrite?","File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if(result != JOptionPane.YES_OPTION) {
				this.generatorGUI.getWslaFileNamejTextField().requestFocus();
				return;
			}
		}	
		
		// Read the name of the services-wsdl-file.
		String serviceWSDLFileName = this.generatorGUI.getServicesWSDLFileNamejTextField().getText();
		
		if(serviceWSDLFileName == null || serviceWSDLFileName.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the name of the Services-WSDL-file!","Missing Services-WSDL-File-Name",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getServicesWSDLFileNamejTextField().requestFocus();
			return;
		}
		
		// Correct file-extension.
		if(!serviceWSDLFileName.endsWith(".wsdl")) {
			serviceWSDLFileName = serviceWSDLFileName+".wsdl";
		}
		
		// Check if the file already exists.
		if(containsFile(outputFolder, serviceWSDLFileName)) {
			int result = JOptionPane.showConfirmDialog(this.generatorGUI, "The file "+serviceWSDLFileName+" already exists!\n Overwrite?","File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if(result != JOptionPane.YES_OPTION) {
				this.generatorGUI.getServicesWSDLFileNamejTextField().requestFocus();
				return;
			}
		}			
		
		// Get the intermediate-files-checkbox.
		boolean generateIntermediateFiles = this.generatorGUI.getIntermediateFilesjCheckBox().isSelected();
		
		// Create the "TaskGeneratorThread".
		// This thread creates the files.
		TaskGenerator taskGenerator = new TaskGenerator(numberOfConcepts, solvableProblem, solutionDepths, numberOfServices,
				outputFolder, bpelFileName, serviceWSDLFileName, taskWSDLFileName, owlFileName, WSLAFileName, generateIntermediateFiles);
		
		Thread taskGeneratorThread = new Thread(taskGenerator);
		
		// Create a progress-dialog which shows an progress-bar with an cancel-button to stop the creation.
		ProgressDialog progressDialog = new ProgressDialog(this.generatorGUI, taskGeneratorThread);
		taskGenerator.setProgressDialog(progressDialog);
		
		taskGeneratorThread.start();
		
		progressDialog.setVisible(true);		
	}
	
	/**
	 * Handles the "Info"-button.
	 * Displays some informations about us.
	 */
	private void infoButton() {
		// Read info-file.
		BufferedReader infoFileReader = new BufferedReader(new InputStreamReader(GeneratorGUIListener.class.getClassLoader().getResourceAsStream(GeneratorGUIListener.infoFilePath)));
		StringBuffer infoFileText = new StringBuffer();
		String line = null;
		
		try {
			while((line = infoFileReader.readLine()) != null) {
				infoFileText.append(line);
				infoFileText.append("\n");
			}
		} catch (IOException exception) {
			System.err.println("GeneratorGUIListener: An error occurred during the reading of the info-file at "+GeneratorGUIListener.infoFilePath);
			exception.printStackTrace();
			return;
		}
		
		JOptionPane.showMessageDialog(this.generatorGUI, infoFileText.toString(),"Web Service Challenge 2008 Test Generator",JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * This method is called when a button is pressed on the GeneratorGUI-frame.
	 */
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		
		// Adds a new solutions to the solution-list.
		if(actionEvent.getActionCommand().equals("Add Solution")) {			
			addSolution();
		}
		// Remove a solution from the list.
		else if(actionEvent.getActionCommand().equals("Remove Solution")) {
			removeSolution();
		}
		// Display a file-chooser.		
		else if(actionEvent.getActionCommand().equals("Browse Output Folder")) {
			browseOutputFolder();			
		}
		// Start the creation of the documents.
		else if(actionEvent.getActionCommand().equals("Start")) {
			startButton();
			System.gc();
		}
		else if(actionEvent.getActionCommand().equals("Info")) {
			infoButton();
		}
	}
	
	/**
	 * Removes the given services from a service-description-file.
	 * @param servicesInputStream The service-description-file.
	 * @param services The names of the services.
	 * @return
	 */
	private ByteArrayOutputStream removeServices(InputStream servicesInputStream, Vector<String> services) {

		
		try {
			Document doc = new SAXBuilder().build( servicesInputStream );
			
			// Remove the given service-elements.
			List<Element> serviceElements = doc.getRootElement().getChildren("service");
			for(Element element : serviceElements) {
				if(services.contains(element.getAttributeValue("name"))) {
					doc.getRootElement().removeContent(element);
					break;
				}
			}
			
			ByteArrayOutputStream wsdl = new ByteArrayOutputStream();
			XMLOutputter out = new XMLOutputter( Format.getPrettyFormat() ); 
			out.output( doc, wsdl );
			
			return wsdl;
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Returns "true" if the directory contains the given file.
	 * @param directory The directory.
	 * @param fileName The name of the file.
	 * @return "true" if the directory contains the given file otherwise "false".
	 */
	private boolean containsFile(File directory, String fileName) {
		if(!directory.isDirectory()) {
			return false;
		}
		
		File[] files = directory.listFiles();
		for(File file : files) {
			if(file.getName().equals(fileName)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * This method is called when the user changes the state of the "solvable"-Checkbox.
	 * @param itemEvent The event that is thrown.
	 */
	@Override
	public void itemStateChanged(ItemEvent itemEvent) {
		if(itemEvent.getItem() instanceof JCheckBox) {
			JCheckBox checkBox = (JCheckBox)itemEvent.getItem();
			
			// Chance the state of the solution-components.
			if(checkBox.getName().equals("solvableCheckBox")) {
				if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
					this.generatorGUI.getSolutionsjLabel().setEnabled(true);
					this.generatorGUI.getSolutionsjList().setEnabled(true);					
					this.generatorGUI.getSolutionsHelpjLabel().setEnabled(true);
					this.generatorGUI.getSolutionsColonjLabel().setEnabled(true);					
					this.generatorGUI.getSolutionDepthjLabel().setEnabled(true);
					this.generatorGUI.getSolutionDepthjTextField().setEnabled(true);
					this.generatorGUI.getSolutionDepthHelpjLabel().setEnabled(true);
					this.generatorGUI.getSolutionDepthColonjLabel().setEnabled(true);
					this.generatorGUI.getAddSolutionjButton().setEnabled(true);
					this.generatorGUI.getRemoveSolutionjButton().setEnabled(true);
				}
				else {
					this.generatorGUI.getSolutionsjLabel().setEnabled(false);
					this.generatorGUI.getSolutionsjList().setEnabled(false);
					this.generatorGUI.getSolutionsHelpjLabel().setEnabled(false);
					this.generatorGUI.getSolutionsColonjLabel().setEnabled(false);
					this.generatorGUI.getSolutionDepthjLabel().setEnabled(false);
					this.generatorGUI.getSolutionDepthHelpjLabel().setEnabled(false);
					this.generatorGUI.getSolutionDepthColonjLabel().setEnabled(false);
					this.generatorGUI.getSolutionDepthjTextField().setEnabled(false);
					this.generatorGUI.getAddSolutionjButton().setEnabled(false);
					this.generatorGUI.getRemoveSolutionjButton().setEnabled(false);
				}
			}
		}
	}
}

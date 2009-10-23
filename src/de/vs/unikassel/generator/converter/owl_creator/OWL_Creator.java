package de.vs.unikassel.generator.converter.owl_creator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMResult;



/**
 * Creates an OWL-document from a taxonomy-description-file.
 * @author Marc Kirchhoff
 *
 */
public class OWL_Creator implements Runnable{
	
	/**
	 * The taxonomy-description-document.
	 */	
	private StreamSource taxonomySource;
	
	/**
	 * The OWL-document.
	 */
	private Document owlDocument;
	
	/**
	 * The path of the xslt-tansformation-file.
	 */
	private static String owlXSLTFilePath = "de/vs/unikassel/generator/converter/owl_creator/OWL.xslt";
	//private static InputStream owlXSLTFilePath = OWL_Creator.class.getClassLoader().getResourceAsStream("de/vs/unikassel/generator/converter/owl_creator/OWL.xslt");
	
	/**
	 * Sets some class-members.
	 */
	private OWL_Creator() {
		this.owlDocument = null;
	}
	
	/**
	 * Creates a new OWL_Creator with the given taxonomy-description-file.
	 * @param taxonomyDescriptionFilePath The path of the taxonomy-description-file.
	 */
	public OWL_Creator(String taxonomyDescriptionFilePath) {		
		this();
					
		File taxonomyDescriptionFile = new File(taxonomyDescriptionFilePath);
		if(taxonomyDescriptionFile.exists()) {
			this.taxonomySource = new StreamSource(taxonomyDescriptionFile);
		}
		else {
			System.err.println("OWL_Creator: The file "+taxonomyDescriptionFilePath+" doesn't exist.");
			this.taxonomySource = null;
		}
	}
	
	/**
	 * Creates a new OWL_Creator with the given taxonomy-description-document.
	 * @param taxonomyDescriptionDocument The taxonomy-description-document.
	 */
	public OWL_Creator(InputStream taxonomyDescriptionDocument) {
		this();
		
		this.taxonomySource = new StreamSource(taxonomyDescriptionDocument);
	}
	
	/**
	 * Creates the OWL-document.
	 */
	public void createOWLDocument() {
		
		System.out.println("OWL_Creator: Creation started.");
		
		if(this.taxonomySource == null) {
			System.err.println("OWL_Creator: An error occurred during the creation of the OWL-document.");
			System.err.println("OWL_Creator: The taxonomy-document is invalid.");
			return;
		}
		
		try {
			// Transform the taxonomy-description-document.
			JDOMResult transformationResult = new JDOMResult();
			Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(OWL_Creator.class.getClassLoader().getResourceAsStream(OWL_Creator.owlXSLTFilePath)) );
			transformer.transform(this.taxonomySource, transformationResult);
			this.owlDocument = transformationResult.getDocument();
		} catch (Exception exception) {
			System.err.println("OWL_Creator: An error occurred during the creation of the OWL-document.");
			exception.printStackTrace();
			return;
		}
		
		System.out.println("OWL_Creator: Creation ended.");		
	}
	
	/**
	 * Returns the OWL-file in a pretty-format.
	 * @return The OWL-file in a pretty-format or null if the OWL-file wasn't created until now.
	 */
	@Override
	public String toString() {
		if(this.owlDocument != null) {
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			return outputter.outputString(this.owlDocument);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Saves the OWL-file.
	 * @param fileName The absolute path of the OWL-file.
	 * @return "true" if the saving was successful otherwise "false".
	 */
	public boolean saveOWLFile(String fileName) {
		if(this.owlDocument == null) {
			System.err.println("OWL_Creator: The OWL-file wasn't created until now.");
			return false;
		}
		
		File newOWLFile = new File(fileName);
		return saveOWLFile(newOWLFile);
	}
	
	/**
	 * Saves the OWL-file.
	 * @param fileName The path of the file.
	 * @return "true" if the saving was successfull otherwise "false".
	 */
	public boolean saveOWLFile(File file) {
		if(this.owlDocument == null) {
			System.err.println("OWL_Creator: The OWL-file wasn't created until now.");
			return false;
		}		
		
		System.out.println("OWL_Creator: Saving OWL-document at "+file.getAbsolutePath());
		
		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(this.toString());
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException exception) {
			System.err.println("OWL_Creator: An error occurred during the writing of the OWL-file.");
			exception.printStackTrace();
			return false;
		}
		
		System.out.println("OWL_Creator: OWL-document successfully saved at "+file.getAbsolutePath());
		
		return true;
	}
	
	/**
	 * Starts the creation of the OWL-document.
	 */
	@Override
	public void run() {
		createOWLDocument();		
	}
	
	/**
	 * Prints the commandline-manual.
	 */
	public static void printManual() {
		System.out.println();
		System.out.println("Comamand Line OWL_Creator");
		System.out.println("---------------------------");
		System.out.println("Usage:");
		System.out.println("OWL_Creator -file filename -o targfilename");
		System.out.println();
		System.out.println("file: File which will be tranformed to OWL.");
		System.out.println("targetfilename: Output-file containing the OWL.");
		System.out.println("---------------------------");
		System.out.println("Parameters:");
		System.out.println("-?: Printing this manual.");
		System.out.println("-file: The file which will be transformed to OWL.");
		System.out.println("-o: Output-file containing the OWL.");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length == 0 || args[0].equals("-?")) {
			OWL_Creator.printManual();
			System.exit(0);
		}
		
		String fileName = null;
		String targetFileName = null;
		
		for(int i = 0; i < args.length; ++i) {
			
			if(args[i].equals("-?")) {
				OWL_Creator.printManual();
				System.exit(0);
			}
			else if(args[i].equals("-file")) {
				if(i+1 > args.length) {
					System.out.println("Parameter-value for option -file not found.");
					System.exit(1);
				}
				
				fileName = args[i+1];
			}
			else if(args[i].equals("-o")) {
				if(i+1 > args.length) {
					System.out.println("Parameter-value for option -o not found");
					System.exit(1);
				}
				
				targetFileName = args[i+1];
			}			
		}
		
		if(fileName == null) {
			System.out.println("You must enter a filename");
			System.exit(1);
		}
		
		if(targetFileName == null) {
			System.out.println("You must enter a filename for the output.");
			System.exit(1);
		}	
		
		OWL_Creator owlCreator = new OWL_Creator(fileName);
		owlCreator.createOWLDocument();
		
		System.out.println("OWL_Creator: Generated OWL-document:");
		System.out.println(owlCreator.toString());
		
		System.out.println("OWL_Creator: Saving OWL-document.");
		owlCreator.saveOWLFile(targetFileName);
	}
}

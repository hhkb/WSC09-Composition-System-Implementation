package de.vs.unikassel.generator.converter.wsdl_creator;

import java.io.File;
import java.io.InputStream;

/**
 * Converts a service-description-file into a WSDL-document.
 * @author Marc Kirchhoff
 */
public class ServiceConverter implements Runnable{
		
	/**
	 * The WSDL_Creator that creates the WSDL-document.
	 */
	private WSDL_Creator wsdlCreator;
	
	/**
	 * Creates a new ServiceConverter with the given service-description-file.
	 * @param serviceDescriptionFilePath The path of the service-description-file.
	 */
	public ServiceConverter(String serviceDescriptionFilePath) {		
		ServiceDescriptionParser parser = new ServiceDescriptionParser(serviceDescriptionFilePath);
		this.wsdlCreator = new WSDL_Creator(parser);
	}
	
	/**
	 * Creates a new ServiceConverter with the given service-description-document.
	 * @param serviceDescriptionDocument
	 */
	public ServiceConverter(InputStream serviceDescriptionDocument) {
		ServiceDescriptionParser parser = new ServiceDescriptionParser(serviceDescriptionDocument);
		this.wsdlCreator = new WSDL_Creator(parser);
	}
	
	/**
	 * Starts the conversion.
	 * @return The wsdl-document in a pretty format.
	 */
	public String convert() {		
		this.wsdlCreator.createWSDLDocument();		
		return this.wsdlCreator.toString();
	}
	
	/**
	 * Saves the wsdl-file.
	 * @param filePath The path of the file.
	 * @return "true" if the saving was successful otherwise "false".
	 */
	public boolean saveWSDLFile(String filePath) {
		if(this.wsdlCreator.toString() == null) {
			convert();
		}
		
		return this.wsdlCreator.saveWSDLFile(filePath);		
	}
	
	/**
	 * Saves the wsdl-file.
	 * @param file The file.
	 * @return "true" is the saving was successful otherwise "false".
	 */
	public boolean saveWSDLFile(File file) {
		if(this.wsdlCreator.toString() == null) {
			convert();
		}
		
		return this.wsdlCreator.saveWSDLFile(file);	
	}

	/**
	 * Starts the conversion in an extra thread.
	 */
	@Override
	public void run() {
		convert();		
	}
	
	/**
	 * Returns the wsdl-document in a pretty format or "null" if the wsdl-document wasn't created until now.
	 * @return The wsdl-document in a pretty format or "null" if the wsdl-document wasn't created until now.
	 */
	@Override
	public String toString() {
		if(this.wsdlCreator == null) {
			return null;
		}
		else {
			return this.wsdlCreator.toString();
		}
	}
		
	/**
	 * Prints the commandline-manual.
	 */
	public static void printManual() {
		System.out.println();
		System.out.println("Comamand Line ServiceConverter");
		System.out.println("---------------------------");
		System.out.println("Usage:");
		System.out.println("ServiceConverter -file filename -o targfilename");
		System.out.println();
		System.out.println("file: File which will be tranformed to WSDL.");
		System.out.println("targetfilename: Output-file containing the WSDL.");
		System.out.println("---------------------------");
		System.out.println("Parameters:");
		System.out.println("-?: Printing this manual.");
		System.out.println("-file: The file which will be transformed to WSDL.");
		System.out.println("-o: Output-file containing the WSDL.");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length == 0 || args[0].equals("-?")) {
			ServiceConverter.printManual();
			System.exit(0);
		}
		
		String fileName = null;
		String targetFileName = null;
		
		for(int i = 0; i < args.length; ++i) {
			
			if(args[i].equals("-?")) {
				ServiceConverter.printManual();
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
		
		System.out.println("ServiceConverter: Conversion started.");
		
		ServiceConverter serviceConverter = new ServiceConverter(fileName);
		serviceConverter.convert();		
		
		System.out.println("ServiceConverter: Generated WSDL-document:");
		System.out.println(serviceConverter.toString());
		
		System.out.println("ServiceConverter: Saving WSDL-document.");
		serviceConverter.saveWSDLFile(targetFileName);
	}
}

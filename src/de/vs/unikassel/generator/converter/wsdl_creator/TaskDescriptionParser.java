package de.vs.unikassel.generator.converter.wsdl_creator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Vector;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;


/**
 * This class is used to parse task-description-files.
 * @author Marc Kirchhoff
 *
 */
public class TaskDescriptionParser implements Runnable, Parser{
	
	/**
	 * The tasks of the task-description-file.
	 */
	private Vector<ServiceDescription> tasks;
	
	/**
	 * The input-stream that contains the service-description-file.
	 */
	private InputStream taskDescriptionFileInputStream;
	
	/**
	 * This enum is used during the parsing of the service-description.
	 */
	private enum ParameterType { PROVIDED, WANTED, NONE };
	
	/**
	 * Initializes some class-members.
	 */
	private TaskDescriptionParser() {
		this.tasks = new Vector<ServiceDescription>();
	}
	
	/**
	 * Creates a new TaskDescriptionParser.
	 * @param serviceDescriptionFilePath The path of the file that descripes the task.
	 */
	public TaskDescriptionParser(String taskDescriptionFilePath) {
		this();		
		
		try {
			this.taskDescriptionFileInputStream = new FileInputStream(taskDescriptionFilePath);
		} catch (FileNotFoundException exception) {
			this.taskDescriptionFileInputStream = null;
			System.err.println("TaskDescriptionFileParser: The file "+taskDescriptionFilePath+" doesn't exist.");
			exception.printStackTrace();		
		}
	}
	
	/**
	 * Creates a new ServiceDescriptionParser.
	 * @param taskDescriptionDocument The task-description-document.
	 */
	public TaskDescriptionParser(InputStream taskDescriptionDocument) {
		this();
		this.taskDescriptionFileInputStream = taskDescriptionDocument;
	}
	
	/**
	 * Parses the task-description-file.
	 */
	public void parse() {
		if(this.taskDescriptionFileInputStream == null) {
			System.err.println("TaskDescriptionFileParser: Parsing-failed. Invalid input-stream.");
			return;
		}
		
		// TODO Validate task-description-file.
		
		try {
			// Create an xml-parser.
			XMLInputFactory factory = XMLInputFactory.newInstance(); 
			XMLStreamReader parser = factory.createXMLStreamReader(this.taskDescriptionFileInputStream);
			
			// Parse the task-description-file-elements.
			ServiceDescription task = null;
			String currentElementName = null;
			ParameterType parameterType = ParameterType.NONE;
			int countTasks = 0;
			
			while (parser.hasNext())  {
				int event = parser.next();
				
				switch(event) {				
								    	
					case XMLStreamConstants.START_ELEMENT: {
						// Get the name of the current name.
						currentElementName = parser.getLocalName();
						
						if(currentElementName.equals("task")) {
							task = new ServiceDescription();
							task.setName("Task"+countTasks);
						}
						else if(currentElementName.equals("provided")) {
							parameterType = ParameterType.PROVIDED;
						}
						else if(currentElementName.equals("wanted")) {
							parameterType = ParameterType.WANTED;
						}
						else if(currentElementName.equals("instance")) {
							if(task == null) {
								continue;
							}
							
				    		// Get the name of the instancec.
				    		String conceptName = parser.getAttributeValue(null, "name");
				    		
				    		// Put the instance in the corresponding vector.
				    		switch(parameterType) {
				    			case PROVIDED: {				    				
				    				task.getInputs().add(conceptName);
				    				break;
				    			}
				    			case WANTED: {
				    				task.getOutputs().add(conceptName);
				    				break;
				    			}
				    			case NONE: {
						    		int currentLine = parser.getLocation().getLineNumber();
						    		int currentColumn = parser.getLocation().getColumnNumber();
						    		System.err.println("TaskDescriptionParser: An error occurred during the parsing at line "+currentLine+", column: "+currentColumn+".");
						    		System.err.println("TaskDescriptionParser: Name of the invalid-element: "+currentElementName);
				    				break;
				    			}
				    		}				    		
				    	}
						
						break;
					}
					case XMLStreamConstants.END_ELEMENT: { 				    	
				    	
				    	currentElementName = parser.getLocalName();
				    	
				    	// Put the instances in the corresponding hashtable.
				    	if(currentElementName.equals("task")) {
				    		this.tasks.add(task);
				    		task = null;
				    	}
					    	
					    break;					
					}
					default: {
						break;
					}
				}
			}
		}
		catch(Exception exception) {
			System.err.println("TaskDescriptionFileParser: An error occurred during the parsing of the task-description-file.");
			exception.printStackTrace();
			return;
		}
	}

	/**
	 * Starts the parsing.
	 */
	public void run() {
		parse();		
	}

	/**
	 * @return the tasks
	 */
	public Vector<ServiceDescription> getServiceDescriptions() {
		return tasks;
	}
}

package de.vs.unikassel.generator.converter.wsdl_creator.tests;

import de.vs.unikassel.generator.converter.wsdl_creator.TaskConverter;

/**
 * Simple de.vs.unikassel.generator.test-class which tests the "TaskConverter"-class.
 * @author Marc Kirchhoff
 *
 */
public class TestTaskConverter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TaskConverter taskConverter = new TaskConverter("de\\vs\\unikassel\\Testsets\\Testset01\\Raw\\problem.xml");
		System.out.println(taskConverter.convert());
		taskConverter.saveWSDLFile("de\\vs\\unikassel\\Testsets\\Testset01\\Converted\\challenge.wsdl");
	}

}

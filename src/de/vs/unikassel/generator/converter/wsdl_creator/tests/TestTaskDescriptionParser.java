package de.vs.unikassel.generator.converter.wsdl_creator.tests;

import de.vs.unikassel.generator.converter.wsdl_creator.TaskDescriptionParser;

/**
 * Simple de.vs.unikassel.generator.test-class which tests the "TaskDescriptionParser"-class.
 * @author Marc Kirchhoff
 *
 */
public class TestTaskDescriptionParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TaskDescriptionParser descriptionParser = new TaskDescriptionParser("de\\vs\\unikassel\\converter\\test_files\\solution_files\\problem.xml");
		descriptionParser.parse();
	}

}

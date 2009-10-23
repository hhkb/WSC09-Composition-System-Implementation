package de.vs.unikassel.generator.converter.owl_creator.tests;

import de.vs.unikassel.generator.converter.owl_creator.OWL_Creator;

/**
 * Simple de.vs.unikassel.generator.test-class which tests the "OWL_Creator"-class.
 * @author Marc Kirchhoff
 *
 */
public class TestOWL_Creator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OWL_Creator owlCreator = new OWL_Creator("de\\vs\\unikassel\\converter\\test_files\\taxonomy_files\\taxonomy.xml");
		owlCreator.createOWLDocument();
		owlCreator.saveOWLFile("WSTest.owl");
		System.out.println(owlCreator.toString());
	}

}

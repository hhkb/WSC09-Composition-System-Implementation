package de.vs.unikassel.generator.converter.owl_creator.tests;

import de.vs.unikassel.generator.converter.owl_creator.OWL_Creator;

public class Testset01OWL_Creator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OWL_Creator owlCreator = new OWL_Creator("de\\vs\\unikassel\\Testsets\\Testset01\\Raw\\taxonomy.xml");
		owlCreator.createOWLDocument();
		owlCreator.saveOWLFile("de\\vs\\unikassel\\Testsets\\Testset01\\Converted\\taxonomy.owl");		
	}

}

package de.vs.unikassel.generator.converter.wsdl_creator.tests;

import de.vs.unikassel.generator.converter.wsdl_creator.ServiceDescriptionParser;

/**
 * Simple de.vs.unikassel.generator.test-class which tests the "ServiceDescriptionParser"-class.
 * @author Marc Kirchhoff
 *
 */
public class TestServiceDescriptionParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServiceDescriptionParser serviceDescriptionParser = new ServiceDescriptionParser("de\\vs\\unikassel\\converter\\test_files\\service_files\\services.xml");
		serviceDescriptionParser.parse();
	}

}

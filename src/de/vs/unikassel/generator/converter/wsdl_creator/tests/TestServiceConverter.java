package de.vs.unikassel.generator.converter.wsdl_creator.tests;

import de.vs.unikassel.generator.converter.wsdl_creator.ServiceConverter;

/**
 * Simple de.vs.unikassel.generator.test-class which tests the "ServiceConverter"-class.
 * @author Marc Kirchhoff
 *
 */
public class TestServiceConverter {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServiceConverter creator = new ServiceConverter("de\\vs\\unikassel\\converter\\test_files\\service_files\\serv11613652.xml");
		System.out.println(creator.convert());
		creator.saveWSDLFile("WSChallenge.wsdl");		
	}
}

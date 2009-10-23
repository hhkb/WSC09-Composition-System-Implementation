package de.vs.unikassel.generator.converter.wsdl_creator.tests;

import de.vs.unikassel.generator.converter.wsdl_creator.ServiceConverter;
import de.vs.unikassel.generator.converter.wsdl_creator.WSDL_Creator;

public class Testset01WSDL_Creator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServiceConverter creator = new ServiceConverter("de\\vs\\unikassel\\Testsets\\Testset01\\Raw\\services.xml");
		//System.out.println(creator.convert());
		creator.convert();
		creator.saveWSDLFile("de\\vs\\unikassel\\Testsets\\Testset01\\Converted\\services.wsdl");		
	}

}

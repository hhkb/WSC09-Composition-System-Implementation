package de.vs.unikassel.generator.converter.bpel_creator.tests;

import de.vs.unikassel.generator.converter.bpel_creator.BPEL_Creator;

public class Testset01BPEL_Creator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BPEL_Creator bpelCreator = new BPEL_Creator("de\\vs\\unikassel\\Testsets\\Testset01\\Raw\\problem.xml");
		bpelCreator.createBPELDocument();
		bpelCreator.saveBPELDocument("de\\vs\\unikassel\\Testsets\\Testset01\\Converted\\problem.bpel");
		//System.out.println(bpelCreator.toString());
	}

}

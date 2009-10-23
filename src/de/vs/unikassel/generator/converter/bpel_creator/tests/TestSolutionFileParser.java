package de.vs.unikassel.generator.converter.bpel_creator.tests;

import de.vs.unikassel.generator.converter.bpel_creator.SolutionFileParser;

/**
 * A simple de.vs.unikassel.generator.test-class which tests the "SolutionFileParser"-class.
 * @author Marc Kirchhoff
 *
 */
public class TestSolutionFileParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		SolutionFileParser solutionFileParser = new SolutionFileParser("C:/WSC08TestSet/Testsets/15/problem.xml");
		solutionFileParser.parseSolutionFile();		
	}
}

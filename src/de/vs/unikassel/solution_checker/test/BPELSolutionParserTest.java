package de.vs.unikassel.solution_checker.test;

import java.io.FileInputStream;

import de.vs.unikassel.solution_checker.BPELSolutionParser;

/**
 * A simple test-class to test the <code>BPELSolutionParser</code>.
 * @author Marc Kirchhoff
 *
 */
public class BPELSolutionParserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {			
			String bpelSolutionFilePath = "C:/test/1/Solution.bpel";
			FileInputStream fileInputStream = new FileInputStream(bpelSolutionFilePath);
			
			// Parse the bpel-solution-file.
			BPELSolutionParser solutionParser = new BPELSolutionParser(fileInputStream);
			solutionParser.parseSolutionFile();
			
			System.out.println("The parsing has stopped.");
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
	}
}

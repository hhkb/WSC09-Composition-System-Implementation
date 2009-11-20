package ca.concordia.pga.algorithm.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import ca.concordia.pga.models.Concept;
import ca.concordia.pga.models.PlanningGraph;
import ca.concordia.pga.models.Service;
import de.vs.unikassel.generator.converter.bpel_creator.BPEL_Creator;

public class SolutionGenerator {

	/**
	 * Generate BPEL file from the solution
	 * 
	 * @param pg
	 * @throws IOException
	 */
	public static void generateSolution(PlanningGraph pg) throws IOException {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("problemStructure");
		Element solutions = root.addElement("solutions");
		Element solution = solutions.addElement("solution");
		Element sequenceRoot = solution.addElement("sequence");
		for (int i = 1; i < pg.getALevels().size(); i++) {
			Set<Service> actionLevel = pg.getALevel(i);
			Element parallel = sequenceRoot.addElement("parallel");
			for (Service s : actionLevel) {
				Element serviceDesc = parallel.addElement("serviceDesc");
				Element abstraction = serviceDesc.addElement("abstraction");
				Element realizations = serviceDesc.addElement("realizations");
				Element input = abstraction.addElement("input");
				Element output = abstraction.addElement("output");
				Element service = realizations.addElement("service");
				service.addAttribute("name", s.getName());
				for (Concept c : s.getInputConceptSet()) {
					input.addElement("concept").addAttribute("name",
							c.getName());
				}
				for (Concept c : s.getOutputConceptSet()) {
					output.addElement("concept").addAttribute("name",
							c.getName());
				}
			}
		}

		/**
		 * write problem.xml to a file
		 */
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new FileWriter(
				"/Users/ericzhao/Desktop/problem.xml"), format);
		writer.write(document);
		writer.close();

		/**
		 * call BPEL creator to convert problem.xml to BPEL and save it to a
		 * file
		 */
		BPEL_Creator bpelCreator = new BPEL_Creator(
				"/Users/ericzhao/Desktop/problem.xml");
		bpelCreator.createBPELDocument();
		bpelCreator.saveBPELDocument("/Users/ericzhao/Desktop/Solution.bpel");

	}

}

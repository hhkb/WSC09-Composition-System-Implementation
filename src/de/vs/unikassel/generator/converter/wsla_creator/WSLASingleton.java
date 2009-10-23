package de.vs.unikassel.generator.converter.wsla_creator;

import java.io.File;

public class WSLASingleton {
	
	public static WSLARepository repository = new WSLARepository();
	
	public static void saveWSLAFile(String filepath) {
		XMLUtils.DocumentToFile(repository.fillTemplate(),new File(filepath));
	}
	
	public static void saveWSLAFile(File file) {
		XMLUtils.DocumentToFile(repository.fillTemplate(),file);
	}
}

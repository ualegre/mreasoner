package edu.casetools.mreasoner.configs;

import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.casetools.mreasoner.compiler.MSpecificationLoader;
import edu.casetools.mreasoner.configs.compiler.ConfigsReader;
import edu.casetools.mreasoner.configs.compiler.ParseException;
import edu.casetools.mreasoner.configs.data.MConfigs;


public class MConfigsLoader extends MSpecificationLoader{

	
	public MConfigs readConfigs(String configsFileName) {	
		ConfigsReader reader;
		try {
			reader = new ConfigsReader(new FileReader(configsFileName));
			return reader.readConfigs();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
		

}

package edu.casetools.icase.mreasoner.configs;

import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.casetools.icase.mreasoner.compiler.MSpecificationLoader;
import edu.casetools.icase.mreasoner.configs.compiler.ConfigsReader;
import edu.casetools.icase.mreasoner.configs.compiler.ParseException;
import edu.casetools.icase.mreasoner.configs.data.MConfigs;


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
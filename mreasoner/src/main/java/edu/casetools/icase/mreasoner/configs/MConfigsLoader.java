package edu.casetools.icase.mreasoner.configs;

import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.casetools.icase.mreasoner.compiler.MSpecificationLoader;
import edu.casetools.icase.mreasoner.configs.compiler.ConfigsReader;
import edu.casetools.icase.mreasoner.configs.compiler.ParseException;
import edu.casetools.icase.mreasoner.configs.data.MConfigs;
import edu.casetools.icase.mreasoner.vera.sensors.ssh.configs.SSHConfigs;
import edu.casetools.icase.mreasoner.vera.sensors.ssh.configs.compiler.SSHConfigsReader;



public class MConfigsLoader extends MSpecificationLoader{

	
	public MConfigs readConfigs(String configsFileName) {	
		ConfigsReader reader;
		try {
			MConfigs configs;
			reader = new ConfigsReader(new FileReader(configsFileName));
			configs = reader.readConfigs();
			configs = readSSHConfigs(configs);
			return configs;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (edu.casetools.icase.mreasoner.vera.sensors.ssh.configs.compiler.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new MConfigs();

	}

	private MConfigs readSSHConfigs(MConfigs configs) throws FileNotFoundException, edu.casetools.icase.mreasoner.vera.sensors.ssh.configs.compiler.ParseException {
		if(!configs.getTimeConfigs().isSimulation() && !configs.getFilesConfigs().getSshConfigsFilePath().equals("null") && !configs.getFilesConfigs().getSshConfigsFilePath().isEmpty()){
				SSHConfigsReader sshReader = new SSHConfigsReader(new FileReader(configs.getFilesConfigs().getSshConfigsFilePath()));
				configs.setSshConfigs(sshReader.readConfigs());
				return configs;	
		} 
		configs.setSshConfigs(new SSHConfigs());
		return configs;
	}
	
	public SSHConfigs readSSHConfigs(String filePath) {
		if(!filePath.equals("null") && !filePath.isEmpty()){
				try {
					SSHConfigsReader sshReader = new SSHConfigsReader(new FileReader(filePath));
					return sshReader.readConfigs();
				} catch (edu.casetools.icase.mreasoner.vera.sensors.ssh.configs.compiler.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		} 
		return new SSHConfigs();
	}
		

}

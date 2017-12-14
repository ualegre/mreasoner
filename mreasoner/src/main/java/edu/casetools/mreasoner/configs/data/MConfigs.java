package edu.casetools.mreasoner.configs.data;

import edu.casetools.mreasoner.configs.data.db.MDBConfigs;
import edu.casetools.mreasoner.configs.data.files.FilesConfigs;
import edu.casetools.icase.mreasoner.core.elements.time.conf.TimeConfigs;

public class MConfigs {

	private MDBConfigs   dbConfigs;
	private TimeConfigs  timeConfigs;
	private FilesConfigs filesConfigs;
	private String  	 jarConfigs;

	private boolean 	useStratification;
	private boolean		useMaxExecutionTime;
	
	public MConfigs(){
		this.setDBConfigs(new MDBConfigs());
		this.setFilesConfigs(new FilesConfigs());
		this.setTimeConfigs(new TimeConfigs());
		jarConfigs   = "Please write the jar execution commands separated by commas:";
		jarConfigs   = jarConfigs + "\n Eg.: \"java -jar c:/a.jar,java -jar c:/b.jar\"";
		
		this.useStratification(true);
	
	}
	
	public void setDBConfigs(MDBConfigs dbConfigs){
		this.dbConfigs = dbConfigs;
	}
	public MDBConfigs getDBConfigs(){
		return dbConfigs;
	}


	public boolean useStratification() {
		return useStratification;
	}

	public void useStratification(boolean useStratification) {
		this.useStratification = useStratification;
	}

	public String parseConfigs(){
		String result = "";
		//General Info
		
		result = result+"<USE_STRATIFICATION> \n";
		result = result+useStratification()+"\n";
		result = result+"</USE_STRATIFICATION> \n";
	
		// Time Info
		result = result+timeConfigs.parseConfigs();
	
		result = result+"<USE_MAX_EXECUTION_TIME> \n";
		result = result+useMaxExecutionTime+"\n";
		result = result+"</USE_MAX_EXECUTION_TIME> \n";
		
		//Files Path Info
		result = result+filesConfigs.parseConfigs();
		
		//Database Info
		result = result+dbConfigs.parseConfigs();
		
		//Jar configs
		result = result+"<EXTERNAL_JARS> \n";
		result = result+"\""+jarConfigs+"\""+"\n";
		result = result+"</EXTERNAL_JARS> \n";

		
		return result;	
	}

	public String getJarConfigs() {
		return jarConfigs;
	}

	public void setJarConfigs(String jarConfigs) {
		this.jarConfigs = jarConfigs;
	}

	public boolean useMaxExecutionTime() {
		return useMaxExecutionTime;
	}

	public void setUseMaxExecutionTime(boolean useMaxExecutionTime) {
		this.useMaxExecutionTime = useMaxExecutionTime;
	}

	public TimeConfigs getTimeConfigs() {
		return timeConfigs;
	}

	public void setTimeConfigs(TimeConfigs timeConfigs) {
		this.timeConfigs = timeConfigs;
	}

	public FilesConfigs getFilesConfigs() {
		return filesConfigs;
	}

	public void setFilesConfigs(FilesConfigs filesConfigs) {
		this.filesConfigs = filesConfigs;
	}	
	
}

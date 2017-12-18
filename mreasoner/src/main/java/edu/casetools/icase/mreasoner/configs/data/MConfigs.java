package edu.casetools.icase.mreasoner.configs.data;

import edu.casetools.icase.mreasoner.configs.data.db.MDBConfigs;
import edu.casetools.icase.mreasoner.configs.data.files.FilesConfigs;
import edu.casetools.icase.mreasoner.core.elements.time.conf.TimeConfigs;
import edu.casetools.icase.mreasoner.vera.sensors.ssh.configs.SSHConfigs;

public class MConfigs {

	private MDBConfigs   dbConfigs;
	private TimeConfigs  timeConfigs;
	private FilesConfigs filesConfigs;
	private SSHConfigs   sshConfigs;

	private boolean 	useStratification;
	private boolean		useMaxExecutionTime;
	
	public MConfigs(){
		this.setDBConfigs(new MDBConfigs());
		this.setFilesConfigs(new FilesConfigs());
		this.setTimeConfigs(new TimeConfigs());
		this.setSshConfigs(new SSHConfigs());		
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
		
		//SSH Info
		result = result+sshConfigs.parseConfigs();
		
		return result;	
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

	public SSHConfigs getSshConfigs() {
		return sshConfigs;
	}

	public void setSshConfigs(SSHConfigs sshConfigs) {
		this.sshConfigs = sshConfigs;
	}	
	
}

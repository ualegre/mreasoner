package edu.casetools.icase.mreasoner.configs.data.files;

import java.io.File;

public class FilesConfigs {

	private String 		systemSpecificationFilePath;
	private String		LFPUBSOutputFilePath;
	private String 		sessionFilePath;
	private String 		resultsFilePath;
	private String 		sshConfigsFilePath;
	
	public FilesConfigs(){
		setSessionFilePath("");
		setSystemSpecificationFilePath("");
		setLFPUBSOutputFilePath("");
		setResultsFilePath("");
	}
	
	public String getSystemSpecificationFilePath() {
		return systemSpecificationFilePath;
	}

	public void setSystemSpecificationFilePath(String systemSpecificationFilePath) {
		this.systemSpecificationFilePath = systemSpecificationFilePath;
	}
	
	public boolean existsSystemDeclarationFile(){
		return new File(""+systemSpecificationFilePath).exists();
	}	
	
	public String getSessionFilePath() {
		return sessionFilePath;
	}

	public void setSessionFilePath(String configsFilePath) {
		this.sessionFilePath = configsFilePath;
	}
	
	public boolean existsConfigsFilePath(){
		return new File(""+sessionFilePath).exists();
	}

	public String getResultsFilePath() {
		return resultsFilePath;
	}

	public void setResultsFilePath(String resultsFilePath) {
		this.resultsFilePath = resultsFilePath;
	}

	public boolean existsLogFilePath(){
		return new File(""+resultsFilePath).exists();
	}
	
	public String getLFPUBSOutputFilePath() {
		return LFPUBSOutputFilePath;
	}

	public void setLFPUBSOutputFilePath(String lFPUBSOutputFilePath) {
		LFPUBSOutputFilePath = lFPUBSOutputFilePath;
	}
	
	public boolean existsLFPUBSOutputFile(){
		return new File(""+LFPUBSOutputFilePath).exists();
	}
	
	public String getSshConfigsFilePath() {
		return sshConfigsFilePath;
	}

	public void setSshConfigsFilePath(String sshConfigsFilePath) {
		this.sshConfigsFilePath = sshConfigsFilePath;
	}
	
	public String parseConfigs(){
		String result = "";
		

		//Files Path Info
		result = result+"<SYSTEM_SPECIFICATION_FILE_PATH> \n";
		result = result+getSystemSpecificationFilePath()+"\n";
		result = result+"</SYSTEM_SPECIFICATION_FILE_PATH> \n";
		
		result = result+"<RESULTS_FILE_PATH> \n";
		result = result+this.getResultsFilePath()+"\n";
		result = result+"</RESULTS_FILE_PATH> \n";
		
		result = result+"<LFPUBS_OUTPUT_FILE_PATH> \n";
		result = result+getLFPUBSOutputFilePath()+"\n";
		result = result+"</LFPUBS_OUTPUT_FILE_PATH> \n";
		
		result = result+"<SESSION_FILE_PATH> \n";
		result = result+this.getSessionFilePath()+"\n";
		result = result+"</SESSION_FILE_PATH> \n";
		
		result = result+"<SSH_CONFIGS_FILE_PATH> \n";
		result = result+this.getSshConfigsFilePath()+"\n";
		result = result+"</SSH_CONFIGS_FILE_PATH> \n";
		
		return result;	
	}






	
}

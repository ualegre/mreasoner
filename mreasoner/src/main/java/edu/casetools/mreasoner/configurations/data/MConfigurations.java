package edu.casetools.mreasoner.configurations.data;

import java.io.File;

public class MConfigurations {

	public enum EXECUTION_MODE {SIMULATION_ITERATION,SIMULATION_REAL_TIME,REAL_ENVIRONMENT};
	private long 		executionTime,timeUnit;
	private MDBConfigs  dbConfigs;
	private String  	jarConfigs;
	private String 		systemSpecificationFilePath;
	private String		LFPUBSOutputFilePath;
	private String 		sessionFilePath;
	private String 		resultsFilePath;
	private boolean 	useFixedIterationTime;
	private boolean		useMaxExecutionTime;


//	private boolean 	relative_isIteration;
//	private boolean 	absolute_isIteration;
	private boolean 	useStratification;
	private EXECUTION_MODE 		executionMode;
	
	public MConfigurations(){
		dbConfigs = new MDBConfigs();
		jarConfigs = "Please write the jar execution commands separated by commas:";
		jarConfigs = jarConfigs + "\n Eg.: \"java -jar c:/a.jar,java -jar c:/b.jar\"";
		this.executionMode = EXECUTION_MODE.SIMULATION_ITERATION;
		this.useFixedIterationTime = true;
		this.setTimeUnit(0);
		this.setExecutionTime(0);
		//this.setRelativeTimeValue(0);
		this.dbConfigs.setDbType("PostgreSQL");
		this.dbConfigs.setDriver("<Driver>");
		this.dbConfigs.setDbName("<name>");
		this.dbConfigs.setTable("<tablename>");
		this.dbConfigs.setIp("localhost");
		this.dbConfigs.setPort("<port>");
		this.dbConfigs.setUser("<user>");
		this.dbConfigs.setPassword("<password>");
		this.useStratification(true);
		this.setExecutionMode(EXECUTION_MODE.SIMULATION_ITERATION);
	
	}
	
	public long getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}
	public long getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(long timeUnit) {
		this.timeUnit = timeUnit;
	}
	public void setDBConfigs(MDBConfigs dbConfigs){
		this.dbConfigs = dbConfigs;
	}
	public MDBConfigs getDBConfigs(){
		return dbConfigs;
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
	
	public EXECUTION_MODE getExecutionMode() {
		return executionMode;
	}
	
	public boolean getTimeIsGivenInIterations(){
		boolean simulation = false;
		switch(this.executionMode){
				case REAL_ENVIRONMENT:
					simulation = false;
					break;
				case SIMULATION_ITERATION:
					simulation = true;
					break;
				case SIMULATION_REAL_TIME:
					simulation = false;
					break;
				default:
			break;
		}
		return simulation;
	}
	
	public boolean getSimulateEvents(){
		boolean simulation = false;
		switch(this.executionMode){
				case REAL_ENVIRONMENT:
					simulation = false;
					break;
				case SIMULATION_ITERATION:
					simulation = true;
					break;
				case SIMULATION_REAL_TIME:
					simulation = true;
					break;
				default:
			break;
		}
		return simulation;
	}
	

	public void setExecutionMode(EXECUTION_MODE executionMode) {
		this.executionMode = executionMode;
	}

	public boolean useStratification() {
		return useStratification;
	}

	public void useStratification(boolean useStratification) {
		this.useStratification = useStratification;
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

	public String getConfigsInString(){
		String result = "";
		
		//General Info
		result = result+"<EXECUTION_MODE>  \n";
		result = result+ getExecutionMode()+"\n";
		result = result+"</EXECUTION_MODE> \n";
		
		result = result+"<USE_STRATIFICATION> \n";
		result = result+useStratification()+"\n";
		result = result+"</USE_STRATIFICATION> \n";
		
		result = result+"<USE_FIXED_ITERATION_TIME> \n";
		result = result+isFixedIterationTime()+"\n";
		result = result+"</USE_FIXED_ITERATION_TIME> \n";
		
		result = result+"<FIXED_ITERATION_TIME> \n";
		result = result+getTimeUnit()+"\n";
		result = result+"</FIXED_ITERATION_TIME> \n";

		result = result+"<USE_MAX_EXECUTION_TIME> \n";
		result = result+useMaxExecutionTime()+"\n";
		result = result+"</USE_MAX_EXECUTION_TIME> \n";
		
		result = result+"<EXECUTION_TIME> \n";
		result = result+getExecutionTime()+"\n";
		result = result+"</EXECUTION_TIME> \n";	
		
//		result = result+"<RELATIVE_TIME_IS_ITERATION> \n";
//		result = result+getRelativeTimeType()+"\n";
//		result = result+"</RELATIVE_TIME_IS_ITERATION> \n";
//		
//		result = result+"<ABSOLUTE_TIME_IS_ITERATION> \n";
//		result = result+getAbsoluteTimeType()+"\n";
//		result = result+"</ABSOLUTE_TIME_IS_ITERATION> \n";
		
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
		
		//Database Info
		result = result+"<DATABASE_TYPE> \n";
		result = result+dbConfigs.getDbType()+"\n";
		result = result+"</DATABASE_TYPE> \n";
		
		result = result+"<DATABASE_DRIVER> \n";
		result = result+dbConfigs.getDriver()+"\n";
		result = result+"</DATABASE_DRIVER> \n";
		
		
		result = result+"<DATABASE_IP> \n";
		result = result+dbConfigs.getIp()+"\n";
		result = result+"</DATABASE_IP> \n";
		
		result = result+"<DATABASE_PORT> \n";
		result = result+dbConfigs.getPort()+"\n";
		result = result+"</DATABASE_PORT> \n";
		
		result = result+"<DATABASE_USER> \n";
		result = result+dbConfigs.getUser()+"\n";
		result = result+"</DATABASE_USER> \n";
		
		result = result+"<DATABASE_PASSWORD> \n";
		result = result+dbConfigs.getPassword()+"\n";
		result = result+"</DATABASE_PASSWORD> \n";
		
		result = result+"<DATABASE_NAME> \n";
		result = result+dbConfigs.getDbName()+"\n";
		result = result+"</DATABASE_NAME> \n";
		
		//Jar configs
		result = result+"<EXTERNAL_JARS> \n";
		result = result+"\""+jarConfigs+"\""+"\n";
		result = result+"</EXTERNAL_JARS> \n";

		
		return result;	
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
	
	public String getJarConfigs() {
		return jarConfigs;
	}

	public void setJarConfigs(String jarConfigs) {
		this.jarConfigs = jarConfigs;
	}
	
	public boolean isFixedIterationTime() {
		return useFixedIterationTime;
	}

	public void setUseFixedIterationTime(boolean useFixedIterationTime) {
		this.useFixedIterationTime = useFixedIterationTime;
	}

	public boolean useMaxExecutionTime() {
		return useMaxExecutionTime;
	}

	public void setUseMaxExecutionTime(boolean useMaxExecutionTime) {
		this.useMaxExecutionTime = useMaxExecutionTime;
	}
	
	
}

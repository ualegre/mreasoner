package edu.casetools.mreasoner.io;

import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.casetools.mreasoner.core.MSpecification;
import edu.casetools.mreasoner.core.configs.MConfigs;
import edu.casetools.mreasoner.core.configs.MConfigs.EXECUTION_MODE;
import edu.casetools.mreasoner.io.compiler.configs.ConfigsReader;
import edu.casetools.mreasoner.io.compiler.configs.ParseException;
import edu.casetools.mreasoner.io.compiler.iterations.MCompiler_Iteration;
import edu.casetools.mreasoner.io.compiler.realtime.MCompiler;

public class MSpecificationLoader {

	
	public MConfigs readConfigs(String configsFileName) throws ParseException,FileNotFoundException{	
		ConfigsReader reader = new ConfigsReader(new FileReader(configsFileName));
		return reader.readConfigs();
	}
	
	public MSpecification readSystemSpecifications_RealTime(String configsFileName) throws FileNotFoundException, edu.casetools.mreasoner.io.compiler.realtime.ParseException{
		MCompiler reader = new MCompiler(new FileReader(configsFileName));
		return reader.readSystemSpecifications();
	}
	
	public MSpecification readSystemSpecifications_Iteration(String configsFileName) throws FileNotFoundException, edu.casetools.mreasoner.io.compiler.iterations.ParseException{
		MCompiler_Iteration reader = new MCompiler_Iteration(new FileReader(configsFileName));
		return reader.readSystemSpecifications();
	}
	
	public MSpecification getMSpecification(String configsFileName) throws FileNotFoundException, ParseException, edu.casetools.mreasoner.io.compiler.iterations.ParseException, edu.casetools.mreasoner.io.compiler.realtime.ParseException{
		MSpecification minput = new MSpecification();
		
		MConfigs configs = readConfigs(configsFileName);
		
		if(configs.getExecutionMode().equals(EXECUTION_MODE.SIMULATION_ITERATION)){
			minput = readSystemSpecifications_Iteration( configs.getSystemSpecificationFilePath() );
		}else if(configs.getExecutionMode().equals(EXECUTION_MODE.SIMULATION_REAL_TIME) || configs.getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT)  ){
			minput =  readSystemSpecifications_RealTime( configs.getSystemSpecificationFilePath() );
		}
		
		minput.setSystemConfigs( configs );

		return minput;
	}
	

}

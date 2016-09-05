package edu.casetools.mreasoner.core;

import java.sql.Timestamp;

import edu.casetools.mreasoner.configurations.data.MConfigurations;
import edu.casetools.mreasoner.core.elements.MInputData;
import edu.casetools.mreasoner.core.elements.MRules;
import edu.casetools.mreasoner.core.elements.MStatus;
import edu.casetools.mreasoner.core.elements.time.Time;
import edu.casetools.mreasoner.core.engine.MReasonerSemaphore;
import edu.casetools.mreasoner.core.engine.RuleStratificator;
import edu.casetools.mreasoner.database.MDatabase;

public class MReasoner extends Thread{

	public static MStatus        	   systemStatus;
	public static MReasonerSemaphore 	   semaphore;
	
	private MInputData			  	   	   systemInput;
	private MRules			 	   	   systemRules;		
	private MDatabase				       database;
	
	private boolean running;
	private boolean simulateEvents;
	private boolean hasMaxExecutionTime;
	private boolean stratify;
	
	public MReasoner( MInputData systemInput, MConfigurations systemConfigs){

		this.systemInput       = systemInput;		
		this.systemRules       = systemInput.getSystemRules();
		
		initializeTime( systemConfigs );

		simulateEvents = systemConfigs.getSimulateEvents();
		hasMaxExecutionTime       = systemConfigs.useMaxExecutionTime();
		semaphore   = new MReasonerSemaphore(  simulateEvents );	
	//	database    = new Database         (     systemConfigs,systemStatus    );
		

		running 	   = true;
		
		stratify = systemConfigs.useStratification();

	}

	private void initializeTime(MConfigurations configs){
		Time time              = new Time  ( configs );
		//time.setMaxExecutionTime(configs.getExecutionTime());
		systemStatus      = systemInput.getSystemStatus();
		systemStatus.setTime(time);
	}
	
	private void printHeader(){
		System.out.println("**********************************************************");
		System.out.println("*************FORWARD REASONING ALGORITHM******************");
		System.out.println("**********************************************************");
		systemRules.showSystemRules();
		systemStatus.showStates();
	//	logger.writeHeader( systemStatus );	
	}
	
	public void MTPLInitialization(){
		if(stratify) stratify();
		printHeader();
		System.out.println("INITIALIZATION AT t = 0");
		systemStatus.getTime().start();
	//	systemStatus.getTime().getSystemRealTime();
		assertSameTimeRules();
		assertNextTimeRules();
		nextIteration();
		semaphore.inputPut();
		System.out.println("END OF INITIALIZATION AT t = 0\n");

	}
	

	
	public void run(){
		this.MTPLInitialization();
		System.out.println("SIMULATE EVENTS "+simulateEvents);
		if(hasMaxExecutionTime)
			while(systemStatus.getTime().simulationTime()&&running) iteration();
		else while(running) iteration();
		
		terminate();

	}
	
	private void iteration(){
	    System.out.println("\nITERATION: "+systemStatus.getTime().getIteration() +" - "+new Timestamp(systemStatus.getTime().getSystemRealTime())+"\n");

			   semaphore.reasonerTake();
					readEvents();
					assertSameTimeRules();	
					assertNextTimeRules();	
					nextIteration();
				semaphore.inputPut();
	}
	

	private void readEvents(){
		systemStatus = database.findLatestEvents(systemStatus);
	
	}
	
	private void stratify(){
		RuleStratificator ruleStratificator = new RuleStratificator(systemRules);
		System.out.println("______________________________________");
		System.out.println("Statifying..");
		systemRules.setSameTimeRules( ruleStratificator.stratify( systemInput.getIndependentStates()) );
		System.out.println("______________________________________");
		System.out.println("Same Time rules succesfully stratified\n");
	}
	
	public void assertSameTimeRules(){	
		for( int i=0; i < systemRules.getSameTimeRules().size(); i++ ){
				systemStatus = systemRules.getSameTimeRules().get(i).assertRule(systemStatus,database);
		}
	}
	
	public void assertNextTimeRules(){	
		for( int i=0; i < systemRules.getNextTimeRules().size(); i++ ){
				systemStatus = systemRules.getNextTimeRules().get(i).assertRule(systemStatus,database);
		}
	}
	
	public void terminate(){
	//	logger.close();
		database.disconnect();
		running = false;
		System.out.println("REASONER THREAD FINISHED");
	}
	
	
	private void nextIteration(){
		database.writeLog(systemStatus);
		while(!systemStatus.getTime().endOfTimeUnit());
		systemStatus.getTime().nextTime();	
		System.out.println("");
		
	}
	


}

package edu.casetools.mreasoner;

import java.sql.Timestamp;

import edu.casetools.mreasoner.core.MSpecification;
import edu.casetools.mreasoner.core.configs.MConfigurations;
import edu.casetools.mreasoner.core.data.MRules;
import edu.casetools.mreasoner.core.data.MStatus;
import edu.casetools.mreasoner.core.data.time.Time;
import edu.casetools.mreasoner.database.MDatabase;
import edu.casetools.mreasoner.utils.Launcher;
import edu.casetools.mreasoner.utils.MSemaphore;
import edu.casetools.mreasoner.utils.RuleStratificator;

public class MReasoner extends Thread {

	public  static MStatus        	   systemStatus;
	public  static MSemaphore  semaphore;	
	private 	   MSpecification	   systemInput;
	private 	   MRules			   systemRules;		
	private 	   MDatabase		   database;
	
	private boolean running;
	private boolean hasMaxExecutionTime;
	
	public MReasoner( MSpecification systemInput ){
		running 	   		= true;
		this.systemInput    = systemInput;		
		this.systemRules    = systemInput.getSystemRules();
		hasMaxExecutionTime = systemInput.getSystemConfigs().useMaxExecutionTime();		
		semaphore   		= new MSemaphore( systemInput.getSystemConfigs().getSimulateEvents() );	
		initTime( systemInput.getSystemConfigs() );
		database    		= new MDatabase         ( systemInput.getSystemConfigs(), systemStatus );
	}

	private void initTime(MConfigurations configs){
		Time time              = new Time  ( configs );
		systemStatus      	   = systemInput.getSystemStatus();
		systemStatus.setTime(time);
	}
	
	private void printHeader(){
		System.out.println("**********************************************************");
		System.out.println("*************FORWARD REASONING ALGORITHM******************");
		System.out.println("**********************************************************");
		systemRules.showSystemRules();
		systemStatus.showStates();
	}
	
	public void initReasoner(){
		if(systemInput.getSystemConfigs().useStratification()) stratify();
		printHeader();
		System.out.println("INITIALIZATION AT t = 0");
		systemStatus.getTime().start();
		System.out.println(systemStatus.getTime().getSystemRealTime());
		assertSameTimeRules();
		assertNextTimeRules();
		nextIteration();
		semaphore.inputPut();
		System.out.println("END OF INITIALIZATION AT t = 0\n");

	}
	

	
	public void run(){
		this.initReasoner();
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
	
	private void nextIteration(){
		database.writeLog(systemStatus);
		while(!systemStatus.getTime().endOfTimeUnit());
		systemStatus.getTime().nextTime();	
		System.out.println("");
		
	}
	
	public void terminate(){
		running = false;
		semaphore.reasonerTake();
			database.disconnect();
		semaphore.inputPut();
	}
	
	private void stratify(){
		RuleStratificator ruleStratificator = new RuleStratificator(systemRules);
		System.out.println("______________________________________");
		System.out.println("Statifying..");
		systemRules.setSameTimeRules( ruleStratificator.stratify( systemInput.getIndependentStates()) );
		System.out.println("______________________________________");
		System.out.println("Same Time rules succesfully stratified\n");
	}
	
	public static void main(String[] args) {
    	
        String 				 configsFileName = args[0];
        Launcher	 launcher = new Launcher();
        
		try {
			launcher.readMSpecification(configsFileName);
			launcher.start();
			launcher.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}

}

package edu.casetools.mreasoner.utils;

import java.io.FileNotFoundException;
import java.util.Vector;

import edu.casetools.mreasoner.MReasoner;
import edu.casetools.mreasoner.core.MSpecification;
import edu.casetools.mreasoner.core.configs.MConfigurations;
import edu.casetools.mreasoner.core.configs.MConfigurations.EXECUTION_MODE;
import edu.casetools.mreasoner.io.MSpecificationLoader;
import edu.casetools.mreasoner.io.compiler.configs.ParseException;
import edu.casetools.mreasoner.utils.modules.LibraryThread;
import edu.casetools.mreasoner.utils.simulator.EventSimulator;


public class Launcher extends Thread {

	MSpecificationLoader 	   	  minputLoader;
	MSpecification	      		  minput;
	MReasoner 		  	  		  mtpl;
//	MVeraLogReader		  		  eventReader;
	EventSimulator 	   		      inputSimulator;
	Vector<LibraryThread> 		  externalLibraries;
	
	public Launcher(){	
		minputLoader 	  = new MSpecificationLoader();
		externalLibraries = new Vector<LibraryThread>();
	}

	
	public boolean readMSpecification(String configFileName) {
		try {
			
			minput = minputLoader.getMSpecification(configFileName);
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (edu.casetools.mreasoner.io.compiler.realtime.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (edu.casetools.mreasoner.io.compiler.iterations.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return true;
	}
	
	
	public void run(){

		if(minput != null){
			mtpl = new MReasoner(minput);
			MConfigurations configs = minput.getSystemConfigs();

			if(configs.getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT)){
				deployModules(configs);
			} else{
		    	inputSimulator    = new EventSimulator( minput.getEventsHistory(), minput.getSystemConfigs() );
				inputSimulator.start();
			}
			
			//if(modulesInitialised()){
				System.out.println("\nSTARTING REASONER..\n");
				mtpl.run();	
				if(!configs.getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT))
					waitForThreadEnd();
			//}	

		} else  System.out.println("SSH Connection error");


	}
	

//	private boolean modulesInitialised(){
//		if(minput.getSystemConfigs().getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT)){
//			return eventReader.checkConnection();
//		}else{
//			return true;
//		}
//	}


	private void deployModules(MConfigurations configs) {
		System.out.println("\nDEPLOYING ACTUATOR MANAGER..");
		launchActuatorManager();
		System.out.println("COMPLETE\n");		
		
		System.out.println("\nDEPLOYING SENSOR READER..\n");
//		launchSensorManager(configs);
		System.out.println("COMPLETE\n");
	}

	public void launchActuatorManager(){
		if(minput != null){
			String[] commands = minput.getSystemConfigs().getJarConfigs().split(",");
	
			for(int i=0;i<commands.length;i++){
				LibraryThread thread = new LibraryThread(commands[i]);
				externalLibraries.add(thread);
				thread.start();
			}
		}
		sleepWrapper(500);
	}

//	private void launchSensorManager(MConfigurations configs) {
//		eventReader = new MVeraLogReader(configs,false);
//		eventReader.start();
//		
//		while(!eventReader.getSshClient().isInitializationFinished()){
//			sleepWrapper(1);
//		}
//		
//		startSSHConnection();
//	}
	
	public void terminate(){
		try {
		
			mtpl.terminate();
			mtpl.join();
			if(minput.getSystemConfigs().getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT))
				terminateDeployment();
			else
				waitForThreadEnd();	
			

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	private void terminateDeployment() throws InterruptedException {
		
//		eventReader.stop();
//		while(!eventReader.getSshClient().isFinalizationFinished()){
//			sleepWrapper(1);
//		}
		if(externalLibraries != null){
			for(LibraryThread library : externalLibraries){
				library.interrupt();
				library.join();
			}
		}
	}
	
	public void waitForThreadEnd() {
		try {
			mtpl.join();
			inputSimulator.terminate();
			inputSimulator.join();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	
	private void sleepWrapper(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public void startSSHConnection() {
//	
//		Scanner in = new Scanner(System.in);
//		in.nextInt();
//		eventReader.stop();
//		in.close();
//		
//	}
	
	
}

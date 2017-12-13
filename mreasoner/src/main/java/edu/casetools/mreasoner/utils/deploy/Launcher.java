package edu.casetools.mreasoner.utils.deploy;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import edu.casetools.mreasoner.MReasoner;
import edu.casetools.mreasoner.core.MSpecification;
import edu.casetools.mreasoner.core.configs.MConfigs;
import edu.casetools.mreasoner.core.configs.MConfigs.EXECUTION_MODE;
import edu.casetools.mreasoner.io.MSpecificationLoader;
import edu.casetools.mreasoner.io.compiler.configs.ParseException;
import edu.casetools.mreasoner.utils.deploy.actuators.MActuatorManager;
import edu.casetools.mreasoner.utils.deploy.sensors.MVeraLogReader;
import edu.casetools.mreasoner.utils.deploy.simulation.EventSimulator;
import edu.casetools.mreasoner.vera.actuators.device.Actuator;


public class Launcher extends Thread {

	MSpecificationLoader 	   	  minputLoader;
	MSpecification	      		  minput;
	MReasoner 		  	  		  mtpl;
	MVeraLogReader		  		  sensorManager;
	EventSimulator 	   		      inputSimulator;
//	Vector<LibraryThread> 		  externalLibraries;
	MActuatorManager			  actuatorManager;
	Vector<Actuator> 			  actuators;  
	String 					  	  sshConfigsFileName;
	
	public Launcher(Vector<Actuator> actuators, String sshConfigsFileName){	
		minputLoader 	     	 = new MSpecificationLoader();
//		externalLibraries    	 = new Vector<LibraryThread>();
		this.actuators 		 	 = actuators;
		this.sshConfigsFileName  = sshConfigsFileName;
	}
	
	public MConfigs readMConfigurations(String configFileName){
		try {
			return  minputLoader.readConfigs(configFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
			MConfigs configs = minput.getSystemConfigs();

			if(configs.getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT)){
				deployModules(configs);
			} else{
		    	inputSimulator    = new EventSimulator( minput.getEventsHistory(), minput.getSystemConfigs() );
				inputSimulator.start();
			}
			
			if(isDeployed()){
				System.out.println("\nSTARTING REASONER..\n");
				mtpl.run();	
				if(!configs.getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT))
					waitForThreadEnd();
			}	

		} else  System.out.println("SSH Connection error");


	}
	

	private boolean isDeployed(){
		if(minput.getSystemConfigs().getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT)){
			return sensorManager.checkConnection();
		}else{
			return true;
		}
	}


	private void deployModules(MConfigs mConfigs) {
		System.out.println("\nDEPLOYING ACTUATOR MANAGER..");
		launchActuatorManager(mConfigs);
		System.out.println("COMPLETE\n");		
		
		System.out.println("\nDEPLOYING SENSOR READER..\n");
		launchSensorManager(mConfigs);
		System.out.println("COMPLETE\n");
	}

	public void launchActuatorManager(MConfigs mConfigs){
//		if(minput != null){
//			String[] commands = minput.getSystemConfigs().getJarConfigs().split(",");
//	
//			for(int i=0;i<commands.length;i++){
//				LibraryThread thread = new LibraryThread(commands[i]);
//				externalLibraries.add(thread);
//				thread.start();
//			}
//		}
//		sleepWrapper(500);
		actuatorManager = new MActuatorManager(mConfigs.getDBConfigs(), actuators);
		actuatorManager.start();
	}

	private void launchSensorManager(MConfigs mConfigs) {
		sensorManager = new MVeraLogReader(mConfigs, sshConfigsFileName);
		sensorManager.start();
		
		while(!sensorManager.getSshClient().isInitializationFinished()){
			sleepWrapper(1);
		}
		
		startSSHConnection();
	}
	
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
		
		sensorManager.stop();
		while(!sensorManager.getSshClient().isFinalizationFinished()){
			sleepWrapper(1);
		}
		if(actuatorManager != null){
			actuatorManager.terminate();
			actuatorManager.interrupt();
			actuatorManager.join();
		}
//		if(externalLibraries != null){
//			for(LibraryThread library : externalLibraries){
//				library.interrupt();
//				library.join();
//			}
//		}
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
	
	public void startSSHConnection() {
	
		Scanner in = new Scanner(System.in);
		in.nextInt();
		sensorManager.stop();
		in.close();
		
	}
	
//	public static void main(String[] args) {
//    	
//        String   configsFileName = args[0];
//        
//		SSHConfigs sshConfigs = SSHConfigs.getInstance();
//		sshConfigs.setPassword("smarthouse123");
//		sshConfigs.setUsername("root");
//		sshConfigs.setSilence(true);
//		sshConfigs.setHostname("10.12.102.156");
//    	
//    	Vector<Actuator> actuators = new Vector<>();
//		LampConfigs lampOnConfigs = new LampConfigs("lampOn");
//		LampActuator lampOn = new LampActuator(lampOnConfigs);
//		actuators.add(lampOn);
//		
//		MActuatorManager actuatorManager = new MActuatorManager(null, actuators);
//        
//        
//        Launcher launcher 		 = new Launcher();
//        
//		try {
//			launcher.readMSpecification(configsFileName);
//			launcher.start();
//			launcher.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
	
	
}

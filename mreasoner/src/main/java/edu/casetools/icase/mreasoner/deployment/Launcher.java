package edu.casetools.icase.mreasoner.deployment;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import edu.casetools.icase.mreasoner.MReasoner;
import edu.casetools.icase.mreasoner.configs.MConfigsLoader;
import edu.casetools.icase.mreasoner.configs.data.MConfigs;
import edu.casetools.icase.mreasoner.core.MSpecification;
import edu.casetools.icase.mreasoner.core.elements.time.conf.TimeConfigs.EXECUTION_MODE;
import edu.casetools.icase.mreasoner.deployment.actuators.MActuatorManager;
import edu.casetools.icase.mreasoner.deployment.sensors.MVeraLogReader;
import edu.casetools.icase.mreasoner.deployment.simulation.EventSimulator;
import edu.casetools.icase.mreasoner.vera.actuators.device.Actuator;


public class Launcher extends Thread {

	MConfigsLoader 	   	  		  minputLoader;
	MConfigs					  mconfigs;
	MSpecification	      		  minput;
	MReasoner 		  	  		  mtpl;
	MVeraLogReader		  		  sensorManager;
	EventSimulator 	   		      inputSimulator;
//	Vector<LibraryThread> 		  externalLibraries;
	MActuatorManager			  actuatorManager;
	Vector<Actuator> 			  actuators;  
	Thread						  mreasonerThread;
	
	public Launcher(Vector<Actuator> actuators){	
		minputLoader 	     	 = new MConfigsLoader();
//		externalLibraries    	 = new Vector<LibraryThread>();
		this.actuators 		 	 = actuators;
	}
	
	public MConfigs readMSpecification(MConfigs configs) {
		try {
			if(mconfigs != null)
				minput = minputLoader.getMSpecification(
						mconfigs.getTimeConfigs().getExecutionMode(), 
						mconfigs.getFilesConfigs().getSystemSpecificationFilePath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (edu.casetools.icase.mreasoner.compiler.realtime.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (edu.casetools.icase.mreasoner.compiler.iterations.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return mconfigs;
	}	
	
	public MConfigs readMConfigs(String configFileName) {
			mconfigs = minputLoader.readConfigs(configFileName);
			
		return mconfigs;
	}	
	
	public void run(){

		if(minput != null){
			mtpl = new MReasoner(minput, mconfigs);
			mreasonerThread = new Thread(mtpl);

			if(mconfigs.getTimeConfigs().getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT)){
				deployModules(mconfigs);
			} else{
		    	inputSimulator    = new EventSimulator( minput.getEventsHistory(), mconfigs );
				inputSimulator.start();
			}
			
			if(isDeployed()){
				System.out.println("\nSTARTING REASONER..\n");
				mreasonerThread.run();	
				if(!mconfigs.getTimeConfigs().getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT))
					try {
						waitForThreadEnd();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}	

		} else  System.out.println("SSH Connection error");


	}
	

	private boolean isDeployed(){
		if(mconfigs.getTimeConfigs().getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT)){
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
		sensorManager = new MVeraLogReader(mConfigs);
		sensorManager.start();
		
		while(!sensorManager.getSshClient().isInitializationFinished()){
			sleepWrapper(1);
		}
		
	}
	
	public void terminate() {


			try {
				mtpl.terminate();

				if(mconfigs.getTimeConfigs().getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT))
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
		waitForThreadEnd();	
	}
	
	public void waitForThreadEnd()  throws InterruptedException {
			mreasonerThread.join();
			inputSimulator.terminate();
			inputSimulator.join();
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

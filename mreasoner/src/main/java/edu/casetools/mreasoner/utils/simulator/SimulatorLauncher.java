package edu.casetools.mreasoner.utils.simulator;

import java.io.FileNotFoundException;

import edu.casetools.mreasoner.MReasoner;
import edu.casetools.mreasoner.core.MSpecification;
import edu.casetools.mreasoner.io.MSpecificationLoader;
import edu.casetools.mreasoner.io.compiler.configs.ParseException;


public class SimulatorLauncher {

	MSpecification 	       	   minput;
	MSpecificationLoader 	   minputLoader;
	EventSimulator 	   		   inputSimulator;
	MReasoner 		   		   mtpl;
	
	public SimulatorLauncher(){
		minputLoader = new MSpecificationLoader();
	}
	
	public boolean loadMInputData(String configFileName) {
		try {
			
			minput 			= minputLoader.getMSpecification(configFileName);
			
			mtpl 			= new MReasoner     ( minput );
        	inputSimulator  = new EventSimulator( minput.getEventsHistory(), minput.getSystemConfigs() );
        	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (edu.casetools.mreasoner.io.compiler.iterations.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (edu.casetools.mreasoner.io.compiler.realtime.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
		
	public void launchMReasoner(){	
			startSimulation();
			waitForThreadEnd();
			
	}

	private void startSimulation() {
		inputSimulator.start();
		mtpl.start();
	}

	public void stopSimulation() {
		mtpl.terminate();
		waitForThreadEnd();
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
	
	
}

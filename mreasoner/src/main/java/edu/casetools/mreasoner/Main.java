package edu.casetools.mreasoner;

import edu.casetools.mreasoner.utils.simulator.SimulatorLauncher;

public class Main {

	public static void main(String[] args) {
		        	
            String 				 configsFileName = args[0];
            SimulatorLauncher	 launcher = new SimulatorLauncher();
            
			launcher.loadMInputData(configsFileName);
			launcher.launchMReasoner();

	}

}

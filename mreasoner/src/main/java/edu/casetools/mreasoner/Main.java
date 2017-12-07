package edu.casetools.mreasoner;

import edu.casetools.mreasoner.utils.Launcher;

public class Main {

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

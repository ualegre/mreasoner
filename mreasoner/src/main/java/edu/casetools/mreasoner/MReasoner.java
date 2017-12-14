package edu.casetools.mreasoner;

import edu.casetools.mreasoner.configs.data.MConfigs;
import edu.casetools.mreasoner.core.MSpecification;
import edu.casetools.mreasoner.core.data.MStatus;
import edu.casetools.mreasoner.database.MDatabase;
import edu.casetools.mreasoner.deployment.Launcher;

public class  MReasoner extends  AbstractMReasoner  {

	public  static MStatus        	   systemStatus;
	public MReasoner( MSpecification systemInput, MConfigs mconfigs){
		super(systemInput, mconfigs.getTimeConfigs(), mconfigs.useStratification(), mconfigs.useMaxExecutionTime());
		this.database    		= new MDatabase ( mconfigs, systemStatus );
	}

	
	public static void main(String[] args) {
    	
        String 				 configsFileName = args[0];
        Launcher	 launcher = new Launcher(null, null);
        if(configsFileName == null) {
        	System.out.println("Please provide the configuration file when executing the command:");
        	System.out.println("\t java -jar mreasoner.jar path/to/configs/your_configurations.txt");
        } else {

    			MConfigs configs = launcher.readMConfigs(configsFileName);
    			if(configs == null) System.out.println("Error reading the configuration file.");
    			else {
        			switch(configs.getTimeConfigs().getExecutionMode()){
	        			case SIMULATION_ITERATION:
	        			case SIMULATION_REAL_TIME:
	        	    		try {
		            			launcher.readMSpecification(configs);
		            			launcher.start();
		            			launcher.join();
	        	    		} catch (InterruptedException e) {
	        	    			// TODO Auto-generated catch block
	        	    			e.printStackTrace();
	        	    		}
	        			break;
	        			case REAL_ENVIRONMENT:
	        				System.out.println("The real environment execution is not yet allowed for the command line.");
	        			break;
	        			default:
	        				System.out.println("Error reading the execution mode of the configuration file.");
	        			break;	
        			}

    			}


        }


}

}

package edu.casetools.mreasoner;

import java.util.Vector;

import edu.casetools.icase.mreasoner.AbstractMReasoner;
import edu.casetools.mreasoner.configs.data.MConfigs;
import edu.casetools.icase.mreasoner.core.MSpecification;
import edu.casetools.mreasoner.database.MDatabase;
import edu.casetools.mreasoner.deployment.Launcher;
import edu.casetools.mreasoner.vera.actuators.device.Actuator;

public class  MReasoner extends  AbstractMReasoner  {


	public MReasoner( MSpecification systemInput, MConfigs mconfigs){
		super(systemInput, mconfigs.getTimeConfigs(), mconfigs.useStratification(), mconfigs.useMaxExecutionTime());
		this.database    		= new MDatabase ( mconfigs, systemStatus );
	}

	
	public static void main(String[] args) {
    	
        String 	configsFileName = args[0];
        Vector<Actuator> actuators = new Vector<>();
        
        //Add your own actuators here
        //
        
        Launcher	 launcher = new Launcher(actuators);
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

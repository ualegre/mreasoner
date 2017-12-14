package edu.casetools.mreasoner.deployment.actuators;

import java.util.Vector;

import edu.casetools.mreasoner.configs.data.db.MDBConfigs;
import edu.casetools.mreasoner.database.core.MDBImplementations.DB_IMPLEMENTATION;
import edu.casetools.mreasoner.database.core.operations.DatabaseOperations;
import edu.casetools.mreasoner.database.core.operations.DatabaseOperationsFactory;
import edu.casetools.icase.mreasoner.vera.actuators.data.Action;
import edu.casetools.icase.mreasoner.vera.actuators.data.ActuatorConfigs;
import edu.casetools.icase.mreasoner.vera.actuators.device.Actuator;




public class MActuatorManager extends Thread{

	private boolean running;
	private DatabaseOperations databaseOperations;	
	protected Vector<Actuator> actuators;
	
	public MActuatorManager(MDBConfigs configs, Vector<Actuator> actuators){	
		this.actuators = actuators;
		databaseOperations = DatabaseOperationsFactory.getDatabaseOperations(DB_IMPLEMENTATION.POSTGRESQL, configs);
		running = true;
		
	}

	public void run(){
		while (running)
		{
			for(int i=0;i<actuators.size();i++){
				Action action = readAction(actuators.get(i).getConfigs());				
				actuators.get(i).performAction(action);
			}
		}
	}
	
	private Action readAction(ActuatorConfigs actuatorConfigs){
		Action action  = new Action();
		String state   = actuatorConfigs.getState();

		String device  = databaseOperations.getDevice(state);
		boolean status = databaseOperations.getStatus(state);
		
		if(device != null){
			action.setDevice(device);
			action.setValue(status);
		}
		
		return action;
	}

	public void terminate(){
		running = false;
	}
	
}

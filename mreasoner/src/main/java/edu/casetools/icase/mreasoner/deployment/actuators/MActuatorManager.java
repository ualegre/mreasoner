package edu.casetools.icase.mreasoner.deployment.actuators;

import java.util.Vector;

import edu.casetools.icase.mreasoner.configs.data.db.MDBConfigs;
import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperations;
import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperationsFactory;
import edu.casetools.icase.mreasoner.vera.actuators.AbstractActuatorManager;
import edu.casetools.icase.mreasoner.vera.actuators.device.Actuator;

public class MActuatorManager extends AbstractActuatorManager{

	private DatabaseOperations databaseOperations;	
	
	public MActuatorManager(MDBConfigs configs, Vector<Actuator> actuators){	
		super(actuators);
		databaseOperations = DatabaseOperationsFactory.getDatabaseOperations(configs);	
	}

	@Override
	protected String getDevice(String stateName) {
		return databaseOperations.getDevice(stateName);
	}

	@Override
	protected boolean getStatus(String stateName) {
		return databaseOperations.getStatus(stateName);
	}
	
}

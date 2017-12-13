package edu.casetools.mreasoner.utils.deploy.sensors;


import edu.casetools.mreasoner.core.configs.MConfigs;
import edu.casetools.mreasoner.database.core.MDBImplementations.DB_IMPLEMENTATION;
import edu.casetools.mreasoner.database.core.connection.DBConnection.STATUS;
import edu.casetools.mreasoner.database.core.operations.DatabaseOperations;
import edu.casetools.mreasoner.database.core.operations.DatabaseOperationsFactory;
import edu.casetools.mreasoner.vera.sensors.AbstractSensorManager;
import edu.casetools.mreasoner.vera.sensors.ssh.configs.SSHConfigs;



public class MVeraLogReader extends AbstractSensorManager {

	DatabaseOperations databaseOperations;
	SSHConfigs sshConfigs;
	
	public MVeraLogReader(MConfigs configs, String configsFilename){
		super(configsFilename);
		databaseOperations = DatabaseOperationsFactory.getDatabaseOperations(DB_IMPLEMENTATION.POSTGRESQL, configs.getDBConfigs());
		if(databaseOperations.getDBConnection().checkConnection() == STATUS.CONNECTED){
			this.registerObserver(new MDataManager(databaseOperations));
		}else{
			System.out.println("ERROR CONECTING TO THE DATABASE");
		}	
	}
	
	}




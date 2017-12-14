package edu.casetools.mreasoner.deployment.sensors;

import edu.casetools.mreasoner.configs.data.MConfigs;
import edu.casetools.mreasoner.database.core.MDBImplementations.DB_IMPLEMENTATION;
import edu.casetools.mreasoner.database.core.connection.DBConnection.STATUS;
import edu.casetools.mreasoner.database.core.operations.DatabaseOperations;
import edu.casetools.mreasoner.database.core.operations.DatabaseOperationsFactory;
import edu.casetools.icase.mreasoner.vera.sensors.AbstractSensorManager;
import edu.casetools.icase.mreasoner.vera.sensors.ssh.configs.SSHConfigs;



public class MVeraLogReader extends AbstractSensorManager {

	DatabaseOperations databaseOperations;
	SSHConfigs sshConfigs;
	
	public MVeraLogReader(MConfigs configs){
		super(configs.getFilesConfigs().getSshConfigsFilePath());
		databaseOperations = DatabaseOperationsFactory.getDatabaseOperations(DB_IMPLEMENTATION.POSTGRESQL, configs.getDBConfigs());
		if(databaseOperations.getDBConnection().checkConnection() == STATUS.CONNECTED){
			this.registerObserver(new MDataManager(databaseOperations));
		}else{
			System.out.println("ERROR CONECTING TO THE DATABASE");
		}	
	}
	
	}




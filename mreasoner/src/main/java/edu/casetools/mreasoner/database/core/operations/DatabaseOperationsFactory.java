package edu.casetools.mreasoner.database.core.operations;

import edu.casetools.mreasoner.database.core.implementations.PostgreSQL_DatabaseOperations;
import edu.casetools.mreasoner.input.configurations.MDBConfigs;
import edu.casetools.mreasoner.input.configurations.MDBTypes;

public class DatabaseOperationsFactory {


	public static DatabaseOperations getDatabaseOperations(
		MDBTypes.DB_IMPLEMENTATION implementation, MDBConfigs configs) {
		DatabaseOperations reasonerDb = null;
			switch(implementation){
				case MYSQL:
					break;
				case POSTGRESQL:
					reasonerDb = new PostgreSQL_DatabaseOperations(configs);
					break;
				default:
					break;
				
			}
		return reasonerDb;
	}


}

package edu.casetools.mreasoner.database.core.operations;

import edu.casetools.mreasoner.core.configs.MDBConfigs;
import edu.casetools.mreasoner.database.core.MDBImplementations;
import edu.casetools.mreasoner.database.core.implementations.PostgreSQL_DatabaseOperations;

public class DatabaseOperationsFactory {


	public static DatabaseOperations getDatabaseOperations(
		MDBImplementations.DB_IMPLEMENTATION implementation, MDBConfigs configs) {
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

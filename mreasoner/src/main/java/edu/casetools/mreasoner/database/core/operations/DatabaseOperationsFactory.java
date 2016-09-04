package edu.casetools.mreasoner.database.core.operations;

import edu.casetools.mreasoner.database.core.implementations.PostgreSQL_DatabaseOperations;
import edu.casetools.mreasoner.input.configurations.databases.DBConfigs;
import edu.casetools.mreasoner.input.configurations.databases.DBTypes;

public class DatabaseOperationsFactory {


	public static DatabaseOperations getDatabaseOperations(
		DBTypes.DB_IMPLEMENTATION implementation, DBConfigs configs) {
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

package edu.casetools.icase.mreasoner.database.core.operations;


import edu.casetools.icase.mreasoner.configs.data.db.MDBConfigs;
import edu.casetools.icase.mreasoner.database.core.implementations.MySQL_DatabaseOperations;
import edu.casetools.icase.mreasoner.database.core.implementations.PostgreSQL_DatabaseOperations;


public class DatabaseOperationsFactory {


	public static DatabaseOperations getDatabaseOperations(MDBConfigs configs) {
		DatabaseOperations reasonerDb = null;
			switch(configs.getDBImplementation()){
				case MYSQL:
					reasonerDb = new MySQL_DatabaseOperations(configs);
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

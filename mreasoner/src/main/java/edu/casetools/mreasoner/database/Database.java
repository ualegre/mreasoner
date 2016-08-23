package edu.casetools.mreasoner.database;

import edu.casetools.mreasoner.core.SystemStatus;
import edu.casetools.mreasoner.core.elements.states.State;
import edu.casetools.mreasoner.core.elements.time.TemporalOperator;
import edu.casetools.mreasoner.core.elements.time.Time;
import edu.casetools.mreasoner.database.Tables.EventsTable;
import edu.casetools.mreasoner.database.Tables.InternalEventsTable;
import edu.casetools.mreasoner.database.Tables.ResultsTable;
import edu.casetools.mreasoner.database.TemporalOperatorChecker.TOC;
import edu.casetools.mreasoner.input.configurations.Configurations;

public class Database {

	
	//DatabaseOperations      databaseOperations;
	
	EventsTable				eventsTable;
	InternalEventsTable     internalEventsTable;
	ResultsTable			resultsTable;

	TOC             		temporalOperatorChecker;

	
	public Database( Configurations systemConfigs, SystemStatus systemStatus ){
	
//		databaseOperations         = DatabaseOperationsFactory.getDatabaseOperations(
//				DBTypes.DB_IMPLEMENTATION.POSTGRESQL,
//				systemConfigs.getDBConfigs());
//		
//		eventsTable 			  = new EventsTable			( databaseOperations );
//		internalEventsTable 	  = new InternalEventsTable ( databaseOperations );
//		resultsTable			  = new ResultsTable		( databaseOperations, systemStatus );
//		temporalOperatorChecker   = new TOC					( databaseOperations, systemConfigs.getTimeIsGivenInIterations() );

		
	}
	
	
	public SystemStatus findLatestEvents(SystemStatus systemStatus){
		
		systemStatus = internalEventsTable.findLatestEvents(systemStatus);
		systemStatus = eventsTable.findLatestEvents(systemStatus);
		return systemStatus;
	}
	
	public boolean checkTemporalOperator(TemporalOperator TOp,Time time){
		return temporalOperatorChecker.checkTemporalOperator(TOp, time);
	}
	
	public void insertInernalEvent(State consequence,Time time){
		internalEventsTable.insertNextTimeRuleEffect(consequence,time);
	}
	
	public void writeLog(SystemStatus systemStatus){
		resultsTable.write(systemStatus);
	}
	
	public void disconnect(){
		//	databaseOperations.disconnect();

	}
	
//	private EF_TYPE getType(boolean simulation){
//		if(simulation) return EF_TYPE.SIMULATION;
//		else           return EF_TYPE.REAL_TIME;
//	}
	
	
	
}

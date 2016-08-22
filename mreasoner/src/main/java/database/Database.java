package database;

import database.Tables.EventsTable;
import database.Tables.InternalEventsTable;
import database.Tables.ResultsTable;
import database.TemporalOperatorChecker.TOC;
import input.configurations.Configurations;
import main.SystemStatus;
import main.elements.states.State;
import main.elements.time.TemporalOperator;
import main.elements.time.Time;

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

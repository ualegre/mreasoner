package edu.casetools.mreasoner.database;


import edu.casetools.mreasoner.configurations.data.MConfigurations;
import edu.casetools.mreasoner.configurations.data.MDBTypes;
import edu.casetools.mreasoner.core.elements.MStatus;
import edu.casetools.mreasoner.core.elements.states.State;
import edu.casetools.mreasoner.core.elements.time.TemporalOperator;
import edu.casetools.mreasoner.core.elements.time.Time;
import edu.casetools.mreasoner.database.core.operations.DatabaseOperations;
import edu.casetools.mreasoner.database.core.operations.DatabaseOperationsFactory;
import edu.casetools.mreasoner.database.tables.EventsTable;
import edu.casetools.mreasoner.database.tables.InternalEventsTable;
import edu.casetools.mreasoner.database.tables.ResultsTable;
import edu.casetools.mreasoner.database.temporaloperator.TOC;


public class MDatabase {

	
	DatabaseOperations      databaseOperations;
	
	EventsTable				eventsTable;
	InternalEventsTable     internalEventsTable;
	ResultsTable			resultsTable;

	TOC             		temporalOperatorChecker;

	
	public MDatabase( MConfigurations systemConfigs, MStatus systemStatus ){
	
		databaseOperations         = DatabaseOperationsFactory.getDatabaseOperations( 
				MDBTypes.DB_IMPLEMENTATION.MYSQL,
				systemConfigs.getDBConfigs());
		
		eventsTable 			  = new EventsTable			( databaseOperations );
		internalEventsTable 	  = new InternalEventsTable ( databaseOperations );
		resultsTable			  = new ResultsTable		( databaseOperations, systemStatus );
		temporalOperatorChecker   = new TOC					( databaseOperations, systemConfigs.getTimeIsGivenInIterations() );

		
	}
	
	
	public MStatus findLatestEvents(MStatus systemStatus){
		
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
	
	public void writeLog(MStatus systemStatus){
		resultsTable.write(systemStatus);
	}
	
	public void disconnect(){
			databaseOperations.disconnect();

	}
	
//	private EF_TYPE getType(boolean simulation){
//		if(simulation) return EF_TYPE.SIMULATION;
//		else           return EF_TYPE.REAL_TIME;
//	}
	
	
	
}

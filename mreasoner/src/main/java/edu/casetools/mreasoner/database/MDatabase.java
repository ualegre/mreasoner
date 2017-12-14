package edu.casetools.mreasoner.database;


import edu.casetools.icase.mreasoner.database.MDBInterface;
import edu.casetools.mreasoner.configs.data.MConfigs;
import edu.casetools.icase.mreasoner.core.elements.MStatus;
import edu.casetools.icase.mreasoner.core.elements.states.State;
import edu.casetools.icase.mreasoner.core.elements.time.Time;
import edu.casetools.icase.mreasoner.core.elements.time.top.TemporalOperator;
import edu.casetools.mreasoner.database.core.MDBImplementations;
import edu.casetools.mreasoner.database.core.operations.DatabaseOperations;
import edu.casetools.mreasoner.database.core.operations.DatabaseOperationsFactory;
import edu.casetools.mreasoner.database.tables.EventsTable;
import edu.casetools.mreasoner.database.tables.InternalEventsTable;
import edu.casetools.mreasoner.database.tables.ResultsTable;
import edu.casetools.mreasoner.database.temporaloperator.TOC;


public class MDatabase implements MDBInterface{

	
	DatabaseOperations      databaseOperations;
	
	EventsTable				eventsTable;
	InternalEventsTable     internalEventsTable;
	ResultsTable			resultsTable;
	TOC             		temporalOperatorChecker;

	
	public MDatabase( MConfigs systemConfigs, MStatus systemStatus ){
	
		databaseOperations         = DatabaseOperationsFactory.getDatabaseOperations( 
				MDBImplementations.DB_IMPLEMENTATION.POSTGRESQL,
				systemConfigs.getDBConfigs());
		
		eventsTable 			  = new EventsTable			( databaseOperations );
		internalEventsTable 	  = new InternalEventsTable ( databaseOperations );
		resultsTable			  = new ResultsTable		( databaseOperations, systemStatus );
		temporalOperatorChecker   = new TOC					( databaseOperations, systemConfigs.getTimeConfigs().isSimulation() );

		
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
	
}

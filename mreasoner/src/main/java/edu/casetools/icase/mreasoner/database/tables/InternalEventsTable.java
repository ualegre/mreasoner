package edu.casetools.icase.mreasoner.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;

import edu.casetools.icase.mreasoner.core.elements.MStatus;
import edu.casetools.icase.mreasoner.core.elements.states.State;
import edu.casetools.icase.mreasoner.core.elements.time.Time;
import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperations;




public class InternalEventsTable {

//	private long      		lastId;
	private DatabaseOperations reasonerDatabase;
	
	public InternalEventsTable(DatabaseOperations reasonerDatabase){
		this.reasonerDatabase = reasonerDatabase;
	//	this.lastId     = 0;
		this.
		reasonerDatabase.eraseInternalEventsTable();
		reasonerDatabase.createInternalEventsTable();
		
	}
	
	public void insertNextTimeRuleEffect(State consequence,Time time){
		
		reasonerDatabase.insertInternalEvent(""+time.getIteration(),consequence.getName(), 
											 ""+consequence.getStatus(),
											 time.getDateFromRealTimeMillis(),
											 time.getDayTimeFromRealTimeMillis(),
											 ""+time.getSystemRealTime());
		
	}
	
	
	

	
	public MStatus findLatestEvents(MStatus systemStatus){
		ResultSet result;

	    try {
			
			result = reasonerDatabase.findLatestInternalEvents();
		
			if(result != null){
				while(result.next()){
					systemStatus.occurs(result.getString("state"), result.getBoolean("value"),true);
			//		lastId = result.getInt("id");
					reasonerDatabase.logInternalEvent(""+result.getInt("id"), 
														 result.getString("iteration"),
														 result.getString("state"),
														 ""+result.getBoolean("value"), 
														 result.getString("date"), 
														 result.getString("time"));
				}
				result.close();
			}else System.out.println("RESULT = NULL");

		} catch (SQLException e1) {
		e1.printStackTrace();
		}
		
		return systemStatus;
		
	}
}

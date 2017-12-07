package edu.casetools.mreasoner.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;

import edu.casetools.mreasoner.core.data.MStatus;
import edu.casetools.mreasoner.core.data.states.State;
import edu.casetools.mreasoner.core.data.time.Time;
import edu.casetools.mreasoner.database.core.operations.DatabaseOperations;




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
			}else System.out.println("RESULT = NULL");
			result.close();
		} catch (SQLException e1) {
		e1.printStackTrace();
		}
		
		return systemStatus;
		
	}
}

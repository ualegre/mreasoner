package edu.casetools.mreasoner.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;

import edu.casetools.mreasoner.core.data.MStatus;
import edu.casetools.mreasoner.database.core.operations.DatabaseOperations;



public class EventsTable {

	private DatabaseOperations reasonerDatabase;
	
	//private long      lastId;

	
	public EventsTable(DatabaseOperations reasonerDatabase) {
		this.reasonerDatabase = reasonerDatabase;
		//lastId         		  = 0;
		reasonerDatabase.eraseEventsTable();
		reasonerDatabase.createEventsTable();
	}
	
	public MStatus findLatestEvents(MStatus systemStatus){
		ResultSet result;
		
	    try {
			
			result = reasonerDatabase.findLatestEvents();
			if(result != null){
				while(result.next()){
					
					systemStatus.occurs(result.getString("state"), result.getBoolean("value"),true);
					
					reasonerDatabase.logExternalEvent(result.getString("id"), result.getString("iteration"),result.getString("state"),
													  result.getString("value"), result.getString("date_old"),
													  result.getString("time_old"));
					//lastId = result.getInt("id");
				}
			}else System.out.println("RESULT = NULL");
			result.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return systemStatus;
		
	}

}

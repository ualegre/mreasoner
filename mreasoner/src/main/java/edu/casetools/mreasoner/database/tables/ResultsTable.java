package edu.casetools.mreasoner.database.tables;

import java.util.Vector;

import edu.casetools.mreasoner.core.SystemStatus;
import edu.casetools.mreasoner.database.core.operations.DatabaseOperations;



public class ResultsTable {

	private DatabaseOperations reasonerDatabase;
	
	public ResultsTable(DatabaseOperations reasonerDatabase, SystemStatus systemStatus){
		
		this.reasonerDatabase = reasonerDatabase;
		reasonerDatabase.dropResultsTable();
		createTable(systemStatus);
	}
	

	
	private void createTable(SystemStatus systemStatus){
		Vector<String> states = new Vector<String>();
		for(int i = 0;i<systemStatus.getSystemStatus().size();i++){
			states.add(systemStatus.getSystemStatus().get(i).getName());
		}
		reasonerDatabase.createResultsTable(states);
	}

	
	public void write(SystemStatus systemStatus){
		Vector<Boolean> status = new Vector<Boolean>();
		
		for(int i = 0;i<systemStatus.getSystemStatus().size();i++){
			status.add(systemStatus.getSystemStatus().get(i).getStatus());
		}
		reasonerDatabase.insertResult(""+systemStatus.getTime().getIteration(),
				""+systemStatus.getTime().getSystemRealTimeLastUnit(), status);
				
		
	}
	
	
}

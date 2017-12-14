package edu.casetools.icase.mreasoner.database.tables;

import java.util.Vector;

import edu.casetools.icase.mreasoner.core.elements.MStatus;
import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperations;



public class ResultsTable {

	private DatabaseOperations reasonerDatabase;
	
	public ResultsTable(DatabaseOperations reasonerDatabase, MStatus systemStatus){
		
		this.reasonerDatabase = reasonerDatabase;
		reasonerDatabase.dropResultsTable();
		createTable(systemStatus);
	}
	

	
	private void createTable(MStatus systemStatus){
		Vector<String> states = new Vector<String>();
		for(int i = 0;i<systemStatus.getSystemStatus().size();i++){
			states.add(systemStatus.getSystemStatus().get(i).getName());
		}
		reasonerDatabase.createResultsTable(states);
	}

	
	public void write(MStatus systemStatus){
		Vector<Boolean> status = new Vector<Boolean>();
		
		for(int i = 0;i<systemStatus.getSystemStatus().size();i++){
			status.add(systemStatus.getSystemStatus().get(i).getStatus());
		}
		reasonerDatabase.insertResult(""+systemStatus.getTime().getIteration(),
				""+systemStatus.getTime().getSystemRealTimeLastUnit(), status);
				
		
	}
	
	
}

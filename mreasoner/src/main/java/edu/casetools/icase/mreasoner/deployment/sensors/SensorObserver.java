package edu.casetools.icase.mreasoner.deployment.sensors;

import edu.casetools.icase.mreasoner.MReasoner;
import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperations;
import edu.casetools.icase.mreasoner.vera.sensors.core.VeraLogDataObserver;
import edu.casetools.icase.mreasoner.vera.sensors.core.data.VeraData;
import edu.casetools.icase.mreasoner.vera.sensors.core.data.VeraEvent;
import edu.casetools.icase.mreasoner.vera.sensors.core.data.VeraVariable;

public abstract class SensorObserver extends VeraLogDataObserver {

	protected DatabaseOperations   databaseOperations;
	protected MReasoner 		   mreasoner;
	protected Sensor 			   sensor;
	
	public SensorObserver(){
		
	}
	
	public SensorObserver(DatabaseOperations databaseOperations, MReasoner mreasoner){
		this.databaseOperations = databaseOperations;
		this.mreasoner  		= mreasoner;
		
	}
	
	public void initDataManager(DatabaseOperations databaseOperations, MReasoner mreasoner){
		this.databaseOperations = databaseOperations;
		this.mreasoner  		= mreasoner;
	}
	
	
	@Override
	protected void handleVeraEvent(VeraData data) {
		VeraEvent event = data.getEvent();
	 	String iteration = String.valueOf(mreasoner.getCurrentStatus().getTime().getIteration());
		 if(event != null){	 
			 if(event.getDevice() == sensor.getDeviceId()){
				 for(String stateName : sensor.getStates()){
					 boolean result = applyCustomModellingRules(stateName, iteration, Boolean.toString(event.getStatus()));
					 databaseOperations.insertEvent(stateName, Boolean.toString(result), iteration, event.getDate(), event.getTime());
			     	 printEvent(event, stateName);
				 }
			 }


		  }
		
	}
	
	@Override
	protected void handleVeraVariable(VeraData data) {
		VeraVariable variable = data.getVariable();
		String iteration = String.valueOf(mreasoner.getCurrentStatus().getTime().getIteration());
		 if(!variable.isEmpty()){	
			 if(variable.getVariable().contentEquals(VeraVariable.VAR_TRIPPED)||(variable.getVariable().contentEquals(VeraVariable.VAR_STATUS))){
				 
				 if(variable.getDeviceId() == sensor.getDeviceId()){
					 for(String stateName : sensor.getStates()){
						 boolean result = applyCustomModellingRules(stateName, iteration, variable.getNewValue());
						 databaseOperations.insertEvent(stateName, Boolean.toString(result), iteration, variable.getDate(), variable.getTime());
				     	 printVariable(variable, stateName);
					 }
				 }


			 }
		 }	
	}
	
	
	protected abstract boolean applyCustomModellingRules(String stateName, String iteration, String value);
	
	public Sensor getSensor(){
		return this.sensor;
	}
	
	public void setSensor(Sensor sensor){
		this.sensor = sensor;
	}
	

	protected void printVariableWarning(VeraVariable variable) {
		System.out.println("WARNING! Device id:"+variable.getDeviceId()+
				 " not recogniced in database table "+
				 databaseOperations.getDBConnection().getDBConfigs().getTable()+
				 "_events");
	}

	protected void printVariable(VeraVariable variable, String state) {
		System.out.println("\n VERA LOG READER: Variable Detection: "+state+ " - "
		 +variable.getNewValue()+" - "+ variable.getDate()+" "+ variable.getTime());
	}
	
	protected void printEventWarning(VeraEvent event) {
		System.out.println("WARNING! Event Reading Failed. Device id:"
				 +event.getDevice()+
				 " not recogniced in database table "+
				 databaseOperations.getDBConnection().getDBConfigs().getTable()+
				 "_events");
	}

	protected void printEvent(VeraEvent event, String state) {
		System.out.println("\n VERA LOG READER: Event Detection: "+state+ " - "
		 +event.getStatus()+" - "+ event.getDate()+" "+ event.getTime());
	}

}

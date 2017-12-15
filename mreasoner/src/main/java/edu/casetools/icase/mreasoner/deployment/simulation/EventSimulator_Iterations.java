package edu.casetools.icase.mreasoner.deployment.simulation;

import java.util.Vector;

import edu.casetools.icase.mreasoner.AbstractMReasoner;
import edu.casetools.icase.mreasoner.configs.data.MConfigs;
import edu.casetools.icase.mreasoner.core.elements.events.Event;
import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperations;
import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperationsFactory;



public class EventSimulator_Iterations extends Thread{
	
	Vector<Event>     		  eventHistory;
	DatabaseOperations 	      connection;
	boolean                   running;
	boolean 				  isRealTime;
	
	public EventSimulator_Iterations(Vector<Event> eventHistory, MConfigs configs){
		this.eventHistory = eventHistory;
		this.connection = 
				DatabaseOperationsFactory.getDatabaseOperations( configs.getDBConfigs() );
		this.isRealTime = !configs.getTimeConfigs().isSimulation();
		running = true;
	
	}
	public void setEventsHistory( Vector<Event> eventHistory){
		this.eventHistory = eventHistory;

	}
	
	public void setDatabase(DatabaseOperations connection){
		this.connection		  = connection;
	}
	
	public Vector<Event> getEventsAt_Simulation(){
		Vector<Event> events = new Vector<Event>();

			for(int i = 0; i < eventHistory.size(); i++){
			//	System.out.println("ITERATION!: "+MTPL.MTPL.systemStatus.getTime().getIteration());
				if(eventHistory.get(i).getTime() == AbstractMReasoner.systemStatus.getTime().getIteration()){
					events.add(eventHistory.get(i));
				//	System.out.println("EVENTO:"+eventHistory.get(i).getName());
					eventHistory.removeElementAt(i);
					i--;
				}
			}

		return events;
	}
	
	public Vector<Event> getEventsAt_Real(){
		Vector<Event> events = new Vector<Event>();
		for(int i = 0; i < eventHistory.size(); i++){
			if(eventHistory.get(i).getTime() >= AbstractMReasoner.systemStatus.getTime().getSystemRealTime()){
				events.add(eventHistory.get(i));
				eventHistory.removeElementAt(i);
				i--;
			}
		}
		return  events;
	}
	
	public void insertEventsAt() {
		Vector<Event> events = null;
	//	if(time.isSimulation()){
		AbstractMReasoner.semaphore.inputTake();
				if(!isRealTime) events = getEventsAt_Simulation();
				else events = getEventsAt_Real();
				for(int i=0;i<events.size();i++){
					
					connection.insertEvent(events.get(i).getName(),""+events.get(i).getStatus(),
							""+ AbstractMReasoner.systemStatus.getTime().getIteration(),
							AbstractMReasoner.systemStatus.getTime().getDateFromRealTimeMillis(),
							AbstractMReasoner.systemStatus.getTime().getDayTimeFromRealTimeMillis());
					//connection.insertEvent(events.get(i));
				}
	 AbstractMReasoner.semaphore.reasonerPut();
	//	}else{
	//		getEventsAt_RealTime(time.g)
	//	}
		
	}
	
	@Override
	public void run() {
		while(running){		
			insertEventsAt();
		}
		// TODO Auto-generated method stub
	}
	
	public void terminate() {
		
		running = false;
		AbstractMReasoner.semaphore.inputPut();
		
	}
}

package edu.casetools.mreasoner.input;

import java.util.Vector;

import edu.casetools.mreasoner.core.SystemStatus;
import edu.casetools.mreasoner.core.elements.events.Event;
import edu.casetools.mreasoner.core.elements.states.State;
import edu.casetools.mreasoner.core.rules.SystemRules;
import edu.casetools.mreasoner.input.configurations.Configurations;


public class InputData {
	Vector<String>		 declaredStates;
	Vector<State> 		 independentStates;
	Vector<Event> 		 eventsHistory;
	SystemRules 		 systemRules;
	SystemStatus 		 systemStatus;
	Configurations		 		 systemConfigs;

	public InputData(){
    	independentStates = new Vector<State>();
    	eventsHistory     = new Vector<Event>();
    	systemRules 	  = new SystemRules();
    	systemStatus      = new SystemStatus();
    }

	public Vector<State> getIndependentStates() {
		return independentStates;
	}

	public void setIndependentStates(Vector<State> independentStates) {
		this.independentStates = independentStates;
	}

	public SystemRules getSystemRules() {
		return systemRules;
	}

	public void setSystemRules(SystemRules systemRules) {
		this.systemRules = systemRules;
	}

	public SystemStatus getSystemStatus() {
		return systemStatus;
	}

	public void setSystemStatus(SystemStatus systemStatus) {
		this.systemStatus = systemStatus;
	}

	public Vector<Event> getEventsHistory() {
		return eventsHistory;
	}

	public void setEventsHistory(Vector<Event> eventsHistory) {
		this.eventsHistory = eventsHistory;
	}
	
	public Configurations getSystemConfigs() {
		return systemConfigs;
	}

	public void setSystemConfigs(Configurations systemConfigs) {
		this.systemConfigs = systemConfigs;
	}
	
	public void setDeclaredStates(Vector<String> declaredStates){
		this.declaredStates = declaredStates;
	}
	
	public Vector<String> getDeclaredStates(){
		return declaredStates;
	}

	public void printInputData(){
		String negation = "";
		System.out.println("******    INITIAL STATUS  ******");
		System.out.println("AT t = 0 :");
		for(int i=0;i<systemStatus.getSystemStatus().size();i++)
		{
			if(systemStatus.getSystemStatus().get(i).getStatus()) negation = "";
			else negation = "!";
			System.out.println(i+") "+negation+systemStatus.getSystemStatus().get(i).getName() );
		}
		System.out.println("********************************\n");
		System.out.println("****** INDEPENDENT STATES ******");
		for(int i=0;i<independentStates.size();i++)
		{
			if(independentStates.get(i).getStatus()) negation = "";
			else negation = "!";
			System.out.println(i+") "+negation+independentStates.get(i).getName() );
		}
		System.out.println("********************************\n");
		System.out.println("******       EVENTS       ******");
		for(int i=0;i<eventsHistory.size();i++)
		{
			eventsHistory.get(i).print();
		}
		System.out.println("********************************\n");
		System.out.println("********************************\n");
		System.out.println("******  SAME TIME RULES   ******");
		for(int i=0;i<systemRules.getSameTimeRules().size();i++)
		{
			for(int j=0;j<systemRules.getSameTimeRules().get(i).getAntecedents().size();j++){
					systemRules.getSameTimeRules().get(i).getAntecedents().get(j).print();
			}
			for(int j=0;j<systemRules.getSameTimeRules().get(i).getTemporalOperators().size();j++){
				systemRules.getSameTimeRules().get(i).getTemporalOperators().get(j).print();
			}
			for(int j=0;j<systemRules.getSameTimeRules().get(i).getInternalStates().size();j++){
				systemRules.getSameTimeRules().get(i).getInternalStates().get(j).print();
			}
			System.out.print(" => ");
			systemRules.getSameTimeRules().get(i).getConsequence().print();
			System.out.println("");
		
		}
		System.out.println("********************************\n");
		System.out.println("******  NEXT TIME RULES   ******");
		for(int i=0;i<systemRules.getNextTimeRules().size();i++)
		{
			for(int j=0;j<systemRules.getNextTimeRules().get(i).getAntecedents().size();j++){
					systemRules.getNextTimeRules().get(i).getAntecedents().get(j).print();
			}
			for(int j=0;j<systemRules.getNextTimeRules().get(i).getTemporalOperators().size();j++){
				systemRules.getNextTimeRules().get(i).getTemporalOperators().get(j).print();
			}
			for(int j=0;j<systemRules.getNextTimeRules().get(i).getInternalStates().size();j++){
				systemRules.getNextTimeRules().get(i).getInternalStates().get(j).print();
			}
			System.out.print(" => ");
			systemRules.getNextTimeRules().get(i).getConsequence().print();
			System.out.println("");
		
		}
		System.out.println("********************************\n");
		
	}
	
		
	
}

package main;

import java.util.Vector;

import main.elements.states.State;
import main.elements.time.Time;

public class SystemStatus {

	private Vector<State> systemStatus;
	private Time		  time;

	
	public SystemStatus(){
		systemStatus = new Vector<State>();
	}
	
	public Vector<State> getSystemStatus() {
		return systemStatus;
	}
	
	public void setTime(Time time){
		this.time = time;
	}
	
	public Time getTime(){
		return this.time;
	}
	
	
	public void showStates(){
		System.out.println("----SYSTEM STATES----");
		for(int i=0;i<systemStatus.size();i++){
			System.out.println("State: "+systemStatus.get(i).getName()+" - "+systemStatus.get(i).getStatus());
		}
		System.out.println("----SYSTEM STATES----");
		System.out.println("");
	}
	
	public void occurs( String state, boolean status,boolean external ){
		String negation = "";
		for(int i=0;i<systemStatus.size();i++){
			if( systemStatus.get(i).getName().equalsIgnoreCase(state) ){
				if(systemStatus.get(i).getStatus()!=status){
					systemStatus.get(i).setStatus(status);
					if(!status) negation = "!";
					if(!external) System.out.println("\t Occurs( "+negation+state+" ),"+time.getIteration()+")");
					else System.out.println("\t Occurs( ingr( "+negation+state+" ) ),"+time.getIteration()+")\n");
				}
			}
		}
	}
}

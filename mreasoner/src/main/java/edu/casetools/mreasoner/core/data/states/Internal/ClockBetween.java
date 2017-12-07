package edu.casetools.mreasoner.core.data.states.Internal;

import edu.casetools.mreasoner.core.data.states.DefaultState;
import edu.casetools.mreasoner.core.data.time.Time;
import edu.casetools.mreasoner.core.data.time.absolute.TimeOfDay;


public class ClockBetween extends DefaultState{
	TimeOfDay since;
	TimeOfDay until;
	
	public ClockBetween(){
		name = "ClockBetween";
	}
	
	public TimeOfDay getSince() {
		return since;
	}

	public void setSince(TimeOfDay since) {
		this.since = since;
	}


	public TimeOfDay getUntil() {
		return until;
	}


	public void setUntil(TimeOfDay until) {
		this.until = until;
	}

	@Override
	public boolean assertState(Time actualTime) {
		long actualTimeOfDay = actualTime.getTimeOfDayInSeconds();
		boolean result = false;
		
		if((actualTimeOfDay >= since.getTimeOfDayInSeconds()) && (actualTimeOfDay < until.getTimeOfDayInSeconds()))
			result = true;

		if(!this.status){
			return !result;
		}else return result;
	}

	@Override
	public void print() {
		System.out.print(name+"("+since.getHour()+":"+since.getMinute()+":"+since.getSecond()+"-"+since.getHour()+":"+since.getMinute()+":"+since.getSecond()+")");
		
	}

}

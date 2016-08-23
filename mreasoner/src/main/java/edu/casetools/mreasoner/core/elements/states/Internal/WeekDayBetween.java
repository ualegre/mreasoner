package edu.casetools.mreasoner.core.elements.states.Internal;

import java.util.Calendar;

import edu.casetools.mreasoner.core.elements.states.DefaultState;
import edu.casetools.mreasoner.core.elements.time.Time;

public class WeekDayBetween extends DefaultState{

	private int since,until;
	
	public WeekDayBetween(){
		since = 1;
		until = 1;
		name = "WeekDayBetween";
	}

	public int getSince() {
		return since;
	}

	public void setSince(int since) {
		this.since = since;
	}


	public int getUntil() {
		return until;
	}


	public void setUntil(int until) {
		this.until = until;
	}
	

	@Override
	public boolean assertState(Time actualTime) {
		
		boolean result = false;
		int actualWeekDay = actualTime.getDate(Calendar.DAY_OF_WEEK);
		System.out.println("ACTUAL "+actualWeekDay+ "  since "+since+" until "+until);
		if( (actualWeekDay >= since)&&(actualWeekDay < until ) ) {
			result = true;
		}
		if(!this.status) return !result;
		else return result;

	}


	@Override
	public void print() {
		System.out.print(name+"("+since+"-"+until+")");
		
	}

}

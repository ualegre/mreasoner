package edu.casetools.mreasoner.core.data.states.Internal;


import edu.casetools.mreasoner.core.data.states.DefaultState;
import edu.casetools.mreasoner.core.data.time.Time;
import edu.casetools.mreasoner.core.data.time.absolute.TimeOfDay;

public class ClockAt extends DefaultState{
	TimeOfDay time;
	
	public ClockAt(){
		super();
		name = "ClockAt";
		time = new TimeOfDay();

	}
	
	public TimeOfDay getTime() {
		return time;
	}

	public void setTime(TimeOfDay time) {
		this.time = time;
	}
	
	@Override
	public boolean assertState(Time actualTime){
		
//		TimeOfDay auxiliarTime = new TimeOfDay();
//		Calendar c = new GregorianCalendar();
//		c.setTimeInMillis(actualTime.getSystemRealTimeLastUnit());
//		
//		auxiliarTime.setValuesIntoCalendar(c);
		boolean result = false;
	//	System.out.println("ACTUAL: "+actualTime.getTimeOfDayInSeconds()+" FIXED: "+time.getTimeOfDayInSeconds());
		if(actualTime.getTimeOfDayInSeconds() ==  time.getTimeOfDayInSeconds())result =  true;
		if(!this.status) return !result;
		else return result;

		
	}

	@Override
	public void print() {
		System.out.print(name+"("+time.getHour()+":"+time.getMinute()+":"+time.getSecond()+")");
		
	}
	
}

package edu.casetools.mreasoner.core.data.events;

import java.util.Calendar;

import edu.casetools.mreasoner.core.data.time.absolute.Date;
import edu.casetools.mreasoner.core.data.time.absolute.TimeOfDay;


public class Event {

	private String name;
	private boolean status;
	private long time;
	private TimeOfDay timeOfDay;
	private Date      date;
	
	public Event(){
		name = null;
		time = -1;
		timeOfDay = null;
		date = null;
		status = false;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String stateId) {
		this.name = stateId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	

	public TimeOfDay getTimeOfDay() {
		return timeOfDay;
	}

	public void setTimeOfDay(TimeOfDay timeOfDay) {
		this.timeOfDay = timeOfDay;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Calendar setValuesIntoCalendar(Calendar calendar){
		
		if(timeOfDay != null)
			calendar = timeOfDay.setValuesIntoCalendar_LocalAM_PM(calendar);
		
		if(date != null)
			calendar = date.setValuesIntoCalendar(calendar);
		
		return calendar;
	}

	public void print() {
		String sign ="";
		if(!status)sign ="!";
		if((timeOfDay == null) && (date ==null))
			System.out.println("EVENT: ("+sign+""+name+","+time+")");
		else{
			System.out.print("EVENT: ("+sign+""+name+",");
			if(timeOfDay != null) 
				timeOfDay.print();
			if(date != null)
				date.print();
			System.out.print("\n");
		}
	
		
	}
	
	
	
	

}

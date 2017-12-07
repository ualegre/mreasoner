package edu.casetools.mreasoner.core.data.states.Internal;

import java.util.Calendar;

import edu.casetools.mreasoner.core.data.states.DefaultState;
import edu.casetools.mreasoner.core.data.time.Time;
import edu.casetools.mreasoner.core.data.time.absolute.Date;

public class CalendarAt extends DefaultState{
	Date data;
	
	public CalendarAt(){
		name = "CalendarAt";
		data = new Date();
	}
	
	public Date getDate() {
		return data;
	}

	public void setDate(Date data) {
		this.data = data;
	}

	@Override
	public boolean assertState(Time actualTime) {
		boolean result = false;
		// TODO Auto-generated method stub
		
		if(  isSameYear(actualTime.getDate(Calendar.YEAR)) || (data.isEmptyYear()) ){
			if( isSameMonth(actualTime.getDate(Calendar.MONTH)) || (data.isEmptyMonth())){
				if( isSameDayOfMonth(actualTime.getDate(Calendar.DAY_OF_MONTH)) || (data.isEmptyDay_of_month())){
					result = true;
				}
			}
		}
	
	
		if(!this.status) result = !result;
		System.out.println("RESULT: "+result);
		return result;
	}
	
	private boolean isSameYear(int year){
		if(data.getYear() == year) return true;
		else return false;
	}
	
	private boolean isSameMonth(int month){
		if(data.getMonth() == month) return true;
		else return false;
	}
	
	private boolean isSameDayOfMonth(int dayOfMonth){
		if(data.getDay_of_month() == dayOfMonth) return true;
		else return false;
	}

	@Override
	public void print() {
		System.out.print(name+"("+data.getDay_of_month()+"/"+data.getMonth()+"/"+data.getYear()+")");
		
	}
	
	

}

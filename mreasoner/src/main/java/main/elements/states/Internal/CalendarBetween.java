package main.elements.states.Internal;

import java.util.Calendar;

import main.elements.states.DefaultState;
import main.elements.time.Time;
import main.elements.time.absolute.Date;

public class CalendarBetween extends DefaultState {
	Date since,until;
	
	public CalendarBetween(){
		name = "CalendarBetween";
		since = new Date();
		until = new Date();
	}
	
	
	public Date getSince() {
		return since;
	}


	public void setSince(Date since) {
		this.since = since;
	}


	public Date getUntil() {
		return until;
	}


	public void setUntil(Date until) {
		this.until = until;
	}


	@Override
	public boolean assertState(Time actualTime) {
		boolean result = false;
		
		if(checkYears(actualTime.getDate(Calendar.YEAR))){
			System.out.println("YEAR");
			if(checkMonths(actualTime.getDate(Calendar.MONTH))){
				System.out.println("MONTH");
				if(checkDayOfMonth(actualTime.getDate(Calendar.DAY_OF_MONTH))){
					System.out.println("DIA DEL MES");
					result = true;
				}
			}
		}
		
//		System.out.println("NOW: "+actualTime.getDate(Calendar.DAY_OF_MONTH)+"\\"+(actualTime.getDate(Calendar.MONTH)+1)+"\\"+actualTime.getDate(Calendar.YEAR));
//		System.out.println("SINCE: "+since.getDay_of_month()+"\\"+since.getMonth()+"\\"+since.getYear());
//		System.out.println("UNTIL: "+until.getDay_of_month()+"\\"+until.getMonth()+"\\"+until.getYear());
//		
		if(!this.status) return !result;
		else return result;
	}
	
	private boolean checkYears(int actualYear){
		boolean result = false;
		if( (actualYear >= since.getYear())&&(actualYear <= until.getYear())){
			result = true;
		}else if(since.isEmptyYear() || until.isEmptyYear()){
			result = true;
		}
		return result;
	}
	
	private boolean checkMonths(int actualMonths){
		boolean result = false;
		if( (actualMonths >= since.getMonth())&&(actualMonths <= until.getMonth())){
			result = true;
		}else if(since.isEmptyMonth() || until.isEmptyMonth()){
			result = true;
		}
		return result;
	}
	
	private boolean checkDayOfMonth(int actualDayOfMonth){
		boolean result = false;
		if( (actualDayOfMonth >= since.getDay_of_month())&&(actualDayOfMonth <= until.getDay_of_month())){
			result = true;
		}else if(since.isEmptyDay_of_month() || until.isEmptyDay_of_month()){
			result = true;
		}
		return result;
	}


	@Override
	public void print() {
		System.out.print(name+"("+since.getDay_of_month()+"/"+since.getMonth()+"/"+since.getYear()+"-"+until.getDay_of_month()+"/"+until.getMonth()+"/"+until.getYear()+")");
		
	}

}

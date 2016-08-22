package database.TemporalOperatorChecker;


import java.sql.SQLException;

import main.elements.time.TemporalOperator;
import main.elements.time.Time;
import main.elements.time.TemporalOperator.TOP_TYPE;

public class TOC {
	

//	private DatabaseOperations  databaseOperations;
	private long since,until;
	private boolean simulatedTime;
	
	public TOC(){//DatabaseOperations databaseOperations,boolean simulatedTime){
	//	operation = Operation_Factory.getOperation( databaseOperations.getDBConnection().getDBConfigs().getTable(), type);
		this.simulatedTime = simulatedTime;
		if(simulatedTime) System.out.println(" - - - THE TIME IS GIVEN IN ITERATIONS - - -");
		else  			  System.out.println(" - - - THE TIME IS GIVEN IN REAL TIME - - -");
		//this.databaseOperations = databaseOperations;
	}
	
	public void adjustToPresent(TemporalOperator TOp, Time time){

		if(!simulatedTime){
			long realTime 	   = time.getSystemRealTimeLastUnit() - TOp.getSinceValue().getSimulation_value();
			long  realTimeComp = realTime - time.getBaseTimeMillis();
			//since = databaseOperations.getIterationFromRealTime(""+realTime);
			
			if(realTimeComp >= 0 )  {	
				until = time.getIteration();
			}else{
				since = -1;
			}

		}else{
			since =  time.getIteration() - TOp.getSinceValue().getSimulation_value() ;
			if( (since >= 0) && (time.getIteration() >= 0) ) 
			{
				until = time.getIteration();
			}else{
				since = -1;
			}
		}
		
	}
	
	public void adjustToAbsolute(TemporalOperator TOp, Time time){
		
		if(simulatedTime){
			if(TOp.getUntilValue().isNotFuture(time)){
				since = TOp.getSinceValue().getSimulation_value();
				until = TOp.getUntilValue().getSimulation_value();

			}else{
				until = -1;
			}
			
		}else{
			long realTime = 0;
			if(TOp.getUntilValue().isNotFuture(time)){
				
				realTime = TOp.getSinceValue().getTimeMillis(time);
			//	since = databaseOperations.getIterationFromRealTime(""+realTime);
				
				realTime = TOp.getUntilValue().getTimeMillis(time);
			//	until = databaseOperations.getIterationFromRealTime(""+realTime);
				
			}else{
				until = -1;
			}
		}
		
	}
	
	public boolean checkTemporalOperator(TemporalOperator TOp,Time time){
		boolean result = false;
		//System.out.println("CHECK TEMPORAL OPERATOR");
		try {
			switch(TOp.getType()){
			
				case STRONG_RELATIVE:  result = this.checkStrongRelative  (TOp, time);	
					 break;
				case WEAK_RELATIVE:    result = this.checkWeakRelative    (TOp, time);
					 break;
				case STRONG_ABSOLUTE: result = this.checkStrongAbsolute (TOp, time);
					 break;
				case WEAK_ABSOLUTE:   result = this.checkWeakAbsolute   (TOp, time);
					 break;
				default:
					break;
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	boolean checkStrongRelative(TemporalOperator TOp, Time time) throws SQLException{
		boolean  isTrueInBetween = false; 
		
		adjustToPresent(TOp, time);
		
		if( (since != -1) && isSameValueDuringWholeInterval(TOp)){
			isTrueInBetween = this.isEqualToFirstIntervalElement(TOp);
		}
		printTemporalOperator(TOp);
		//System.out.println(isTrueInBetween);
		return isTrueInBetween;
	}
	
	boolean checkWeakRelative(TemporalOperator TOp, Time time) throws SQLException{
		boolean  isTrueInBetween = false; 
		
		adjustToPresent(TOp, time);
		
		if( (since != -1) && isAtLeastOneValueDuringInterval(TOp) ){
			isTrueInBetween = true;
		}
		printTemporalOperator(TOp);
		//System.out.println(isTrueInBetween);
		return isTrueInBetween;
	}
	
	boolean checkStrongAbsolute(TemporalOperator TOp, Time time) throws SQLException{
		boolean  isTrueInBetween = false; 
		//System.out.println("STRONG ABSOLUTE");

		adjustToAbsolute(TOp,time);
		if( (until != -1) && isSameValueDuringWholeInterval(TOp)){
			isTrueInBetween = this.isEqualToFirstIntervalElement(TOp);
		}
		printTemporalOperator(TOp);
		//System.out.println(isTrueInBetween);
		return isTrueInBetween;
	}
	
	boolean checkWeakAbsolute(TemporalOperator TOp, Time time) throws SQLException{
		boolean  isTrueInBetween = false; 
		adjustToAbsolute(TOp,time);
		if( (until != -1) && isAtLeastOneValueDuringInterval(TOp)){
			isTrueInBetween = true;
		}
		printTemporalOperator(TOp);
		//System.out.println(isTrueInBetween);
		return isTrueInBetween;
	}
	
	private boolean isAtLeastOneValueDuringInterval(TemporalOperator TOp){

		boolean result = true;
		
			if(TOp.getType() == TOP_TYPE.STRONG_RELATIVE || TOp.getType() == TOP_TYPE.WEAK_RELATIVE ){
		//	System.out.println("SINCE"+since+" UNTIL: "+until);
				//result = databaseOperations.getAtLeastOneValueQueryPresent( ""+since, ""+until, TOp.getName(), ""+TOp.getStatus());
			}else{
				//result = databaseOperations.getAtLeastOneValueQueryAbsolute( ""+since, ""+until, TOp.getName(), ""+TOp.getStatus());
			}
		
		return result;
	}
	
	private boolean isSameValueDuringWholeInterval(TemporalOperator TOp){
	    boolean result = true ;
		if(TOp.getType() == TOP_TYPE.STRONG_RELATIVE || TOp.getType() == TOP_TYPE.WEAK_RELATIVE){
			//result = databaseOperations.getSameValueQueryPresent(""+since,""+until,TOp.getName(),""+TOp.getStatus());
		}else{
			//result = databaseOperations.getSameValueQueryAbsolute(""+since,""+until,TOp.getName(),""+TOp.getStatus());
		}
		
		//System.out.println("SAME VALUE DURING WHOLE INTERVAL "+result);
		return result;
		

	}
	
	private boolean isEqualToFirstIntervalElement(TemporalOperator TOp) throws SQLException{
		boolean result = true;
		if(TOp.getType() == TOP_TYPE.STRONG_RELATIVE || TOp.getType() == TOP_TYPE.WEAK_RELATIVE ){
			//result = databaseOperations.getEqualToFirstQueryPresent(""+since,TOp.getName(),""+TOp.getStatus());
		}else{
			//result = databaseOperations.getEqualToFirstQueryAbsolute(""+since,TOp.getName(),""+TOp.getStatus());
		}
		//System.out.println("IS EQUAL TO FIRST INTERVAL ELEMENT "+result+"SINCE "+since);
		return result;
		

	}
	
	public void printTemporalOperator(TemporalOperator TOp){
	//	String negative = "";
	//	if(!TOp.getStatus()) negative = "¬";
		
	//	TOp.print();
//		switch(TOp.getType()){
//		case STRONG_ABSOLUTE:
//			System.out.print("\t [-]"+negative+TOp.getName()+" - "+"FROM [ "+since+" , "+until+" ] => ");
//			break;
//		case STRONG_RELATIVE:
//			System.out.print("\t p[-]"+negative+TOp.getName()+" - "+"FROM [ "+since+" , "+until+" ] => ");
//			break;
//		case WEAK_ABSOLUTE:
//			System.out.print("\t <->"+negative+TOp.getName()+" - "+"FROM [ "+since+" , "+until+" ] => ");
//			break;
//		case WEAK_RELATIVE:
//			System.out.print("\t p<->"+negative+TOp.getName()+" - "+"FROM [ "+since+" , "+until+" ] => ");
//			break;
//		default:
//			break;
//		
//		}
	}
	
}

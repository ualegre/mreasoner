package edu.casetools.mreasoner.core.elements.rules;



import edu.casetools.mreasoner.core.SystemStatus;
import edu.casetools.mreasoner.database.Database;

public class NextTimeRule extends SameTimeRule{

	public NextTimeRule(){
		super();
	}
	
	public SystemStatus assertRule(SystemStatus systemStatus,Database database){
		boolean result = true;

		result = checkAntecedents(systemStatus);
		
//		if(result) result = checkPastBoundedAntecedents(systemStatus,database);
//		if(result) database.insertInernalEvent(this.consequence);
//		if(result != lastResult) printRuleChange();
		
		/****************/
		if(result) result = checkPastBoundedAntecedents(systemStatus,database);
		if(!result)firstTime = true;
		if(result && firstTime){
		 database.insertInernalEvent(this.consequence,systemStatus.getTime());
		 printRuleChange();	
		 firstTime = false;
		}
		/****************/		

		
	//	lastResult = result;
		return systemStatus;

	}
	
	/**********************************************************************************************************************************************
	 *  THESE ARE ONLY PRINTING FUNCTIONS
	 ********************************************************************************************************************************************/	
	private void printRuleChange(){
		String negation = "";
		if(!consequence.getStatus())negation = "!";
		System.out.println("\t NEXT TIME RULE: "+negation+this.consequence.getName()+" WILL BE TRIGGERED ON THE NEXT STEP");
	}
	
}

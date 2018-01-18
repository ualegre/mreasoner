package edu.casetools.icase.mreasoner;

import edu.casetools.icase.mreasoner.deployment.sensors.SensorObserver;

public class BatteryObserver extends SensorObserver{


	@Override
	protected boolean applyCustomModellingRules(String stateName, String iteration, String value) {
		int intValue = Integer.valueOf(value);
		boolean result = false;
		
		if(stateName.equals("BatteryLow")){
			result = applyBatteryLowModellingRule(intValue);
		} else if(stateName.equals("BatteryMed")){
			result = applyBatteryMedModellingRule(intValue);
		} else if(stateName.equals("BatteryHigh")){
			result = applyBatteryHighModellingRule(intValue);
		} 
		return result;
	}

	private boolean applyBatteryHighModellingRule(int intValue) {
		if(intValue >= 70) return true;
		else return false;
	}

	private boolean applyBatteryMedModellingRule(int intValue) {
		if(intValue < 70 && intValue > 30) return true;
		else return false;
	}

	private boolean applyBatteryLowModellingRule(int intValue) {
		if(intValue <= 30) return true;
		else return false;
	}
	
	

}

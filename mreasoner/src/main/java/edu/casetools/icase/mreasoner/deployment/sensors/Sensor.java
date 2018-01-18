package edu.casetools.icase.mreasoner.deployment.sensors;

public class Sensor {

	private String deviceId, name, minValue, maxValue, state;
	private boolean isBoolean;
	
	public Sensor(){
		setDeviceId("");
		setName("");
		setMinValue("");
		setMaxValue("");
		setState("");
		setBoolean(true);
	}
	
	public Sensor(String name, String minValue, String maxValue, boolean isBoolean, String state, String deviceId){
		this.setDeviceId(deviceId);
		this.setName(name);
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.isBoolean = isBoolean;
		this.setState(state);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public boolean isBoolean() {
		return isBoolean;
	}

	public void setBoolean(boolean isBoolean) {
		this.isBoolean = isBoolean;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	
}

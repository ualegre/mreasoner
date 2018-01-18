package edu.casetools.icase.mreasoner.deployment.sensors;

import java.util.Vector;

public class Sensor {

	private String deviceId, name, model, location, dataType, minValue, maxValue;
	private Vector<String> states;
	private boolean isBoolean;
	
	public Sensor(){
		this.setDeviceId("");
		this.setName("");
		this.setModel("");
		this.setLocation("");
		this.setDataType("");
		this.setMinValue("");
		this.setMaxValue("");
		this.setBoolean(true);
		this.setStates(new Vector<String>());
	}
	
	public Sensor(String id, String name, String model, String location, String dataType, String minValue, String maxValue, boolean isBoolean, Vector<String> states){
		this.setDeviceId(id);
		this.setName(name);
		this.setModel(model);
		this.setLocation(location);
		this.setDataType(dataType);
		this.setMinValue(minValue);
		this.setMaxValue(maxValue);
		this.setBoolean(isBoolean);
		this.setStates(states);
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
	
	public void setStates(String stateName) {
		this.states.add(stateName);
	}

	public void addState(String stateName) {
		this.states.add(stateName);
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Vector<String> getStates() {
		return states;
	}

	public void setStates(Vector<String> states) {
		this.states = states;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	
}

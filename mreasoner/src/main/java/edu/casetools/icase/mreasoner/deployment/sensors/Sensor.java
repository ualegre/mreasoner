package edu.casetools.icase.mreasoner.deployment.sensors;

import java.util.Vector;

public class Sensor {

	private String deviceId, name, model, location, minValue, maxValue;
	private Vector<String> states;
	private boolean isBoolean;
	
	public Sensor(){
		setStates(new Vector<String>());
		setDeviceId("");
		setName("");
		setModel("");
		setLocation("");
		setMinValue("");
		setMaxValue("");
		setBoolean(true);
	}
	
	public Sensor(String name, String minValue, String maxValue, boolean isBoolean, String state, String deviceId){
		setStates(new Vector<String>());
		this.setDeviceId(deviceId);
		this.setName(name);
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.isBoolean = isBoolean;
		
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
	
	
}

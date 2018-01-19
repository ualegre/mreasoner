package edu.casetools.icase.mreasoner.deployment.realenvironment;

import java.util.Vector;

import edu.casetools.icase.mreasoner.deployment.sensors.SensorObserver;
import edu.casetools.icase.mreasoner.vera.actuators.device.Actuator;

public abstract class AbstractDeploymentModule {

	protected Vector<Actuator> actuators;
	protected Vector<SensorObserver> sensorObservers;
	
	public AbstractDeploymentModule(){
		
		setActuators(new Vector<>());
		initialiseActuators();
		setSensorObservers(new Vector<>());
		initialiseSensorObservers();
		
	}

	public Vector<Actuator> getActuators() {
		return actuators;
	}

	public void setActuators(Vector<Actuator> actuators) {
		this.actuators = actuators;
	}

	public Vector<SensorObserver> getSensorObservers() {
		return sensorObservers;
	}

	public void setSensorObservers(Vector<SensorObserver> sensorObservers) {
		this.sensorObservers = sensorObservers;
	}
	
	protected abstract void initialiseSensorObservers();
	protected abstract void initialiseActuators();

	
}

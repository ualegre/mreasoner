package edu.casetools.mreasoner.database.core.operations;

import java.sql.ResultSet;
import java.util.Vector;

import edu.casetools.mreasoner.configurations.data.MDBConfigs;
import edu.casetools.mreasoner.database.core.connection.DBConnection;
import edu.casetools.mreasoner.database.core.connection.DBConnection.STATUS;




public abstract class DatabaseOperations {
	
		protected DBConnection dbConnection;
		
		public DatabaseOperations(MDBConfigs configs){
			this.dbConnection = new DBConnection(configs);
			connect();
		}
		
		public DBConnection getDBConnection(){
			return dbConnection;
		}

		public STATUS checkConnection(){
			return dbConnection.checkConnection();
		}

		public abstract void connect();
		public abstract void reconnect(MDBConfigs configs);
		public abstract void disconnect();
		
		public abstract boolean createDatabase(String dbName);
		public abstract void dropDatabase(String dbName);
		public abstract boolean databaseExists(String dbName);
		
		public abstract void createEventsTable();
		public abstract void eraseEventsTable();
		public abstract void insertEvent(String stateName,String status,String iteration,String date,String time);
		public abstract ResultSet findLatestEvents();
		public abstract ResultSet getEventsTableContent(boolean simulation);
		public abstract void logExternalEvent(String id,String iteration,String stateName,String status,String data,String time);
		public abstract void logInternalEvent(String id,String iteration,String stateName,String status,String data,String time);
		
		public abstract void createInternalEventsTable();
		public abstract void eraseInternalEventsTable();
		public abstract void insertInternalEvent(String iteration, String stateName, String status, String date, String time,String time_millis);
		public abstract ResultSet findLatestInternalEvents();
		public abstract ResultSet getInternalEventsTableContent(boolean simulation);
		
		public abstract void createResultsTable(Vector<String> states);
		public abstract void eraseResultsTable();
		public abstract void dropResultsTable();
		public abstract void insertResult(String iteration, String time, Vector<Boolean> status);
		public abstract ResultSet getResultsTableContent();
		
		public abstract void   	  createSensorTable();
		public abstract void   	  eraseSensorTable();
		public abstract void 	  newSensorTableRelation(String device,String implementation,String state);
		public abstract void 	  removeSensorTableRelation(String device,String implementation, String state);
		public abstract ResultSet getSensorTableContent();
		
		public abstract void   	  createActuatorTable();
		public abstract void   	  eraseActuatorTable();
		public abstract void 	  newActuatorTableRelation(String device,String implementation,String state);
		public abstract void 	  removeActuatorTableRelation(String device,String implementation, String state);
		public abstract ResultSet getActuatorTableContent();
		
		public abstract void   	  createSensorImplementationTable();
		public abstract void   	  eraseSensorImplementationTable();
		public abstract void 	  newSensorImplementation(String name,String maxValue,String minValue,String isOnOff);
		public abstract void 	  removeSensorImplementation(String name,String maxValue,String minValue,String isOnOff);
		public abstract ResultSet getSensorImplementationTableContent();
		
		public abstract void   	  createActuatorImplementationTable();
		public abstract void   	  eraseActuatorImplementationTable();
		public abstract void 	  newActuatorImplementation(String name,String maxValue,String minValue,String isOnOff);
		public abstract void 	  removeActuatorImplementation(String name,String maxValue,String minValue,String isOnOff);
		public abstract ResultSet getActuatorImplementationTableContent();
		
		public abstract String    getState(String device);
		public abstract String    getDevice(String state);
		public abstract boolean   getStatus(String state);
		
//		public abstract ResultSet findLatestInternalEvents(long lastId);
		public abstract boolean getAtLeastOneValueQueryPresent  ( String since, String until, String state, String status );
		public abstract boolean getAtLeastOneValueQueryAbsolute ( String since, String until, String state, String status );
		public abstract boolean getSameValueQueryPresent		( String since, String until, String state, String status );
		public abstract boolean getSameValueQueryAbsolute		( String since, String until, String state, String status );
		public abstract boolean getEqualToFirstQueryPresent		( String since,  String state, String status);
		public abstract boolean getEqualToFirstQueryAbsolute	( String since, String state, String status);

		public abstract long getIterationFromRealTime(String string);

		
}

package edu.casetools.icase.mreasoner.database.core.implementations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

//import java.sql.SQLException;

import edu.casetools.icase.mreasoner.configs.data.db.MDBConfigs;
import edu.casetools.icase.mreasoner.database.core.connection.DBConnection;
import edu.casetools.icase.mreasoner.database.core.connection.DBConnection.STATUS;
import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperations;


public class MySQL_DatabaseOperations extends DatabaseOperations {
	String resultsQuery;
	MDBConfigs configs;
	private ResultSet rs;
	
	public MySQL_DatabaseOperations(MDBConfigs configs) {
		super(configs);
		this.configs = configs;
		resultsQuery = "";
		// TODO Auto-generated constructor stub
	}

	@Override
	public void connect() {
		try {
			dbConnection.connect();
			dbConnection.setConnection(STATUS.CONNECTED);
		} catch (ClassNotFoundException e) {
			dbConnection.setConnection(STATUS.ERROR);
		//	e.printStackTrace();
		} catch (SQLException e) {
			dbConnection.setConnection(STATUS.ERROR);
		//	e.printStackTrace();
		}
	}

	@Override
	public void reconnect(MDBConfigs configs) {
		dbConnection = new DBConnection(configs);
		connect();
	}
	@Override
	public void disconnect() {		
		try {
			dbConnection.disconnect();
			dbConnection.setConnection(STATUS.DISCONNECTED);
		} catch (SQLException e) {
			dbConnection.setConnection(STATUS.ERROR);
		//	e.printStackTrace();
		}
		
	}

	@Override
	public void createEventsTable() {
		
		String incommingEventsTableQuery = "CREATE TABLE IF NOT EXISTS external_events ("
				 +" id serial PRIMARY KEY,"+
				  "state varchar(50) NOT NULL,"+
				  "value boolean NOT NULL,"+
				  "iteration bigint NOT NULL,"+
				  "date_old date NOT NULL,"+
				  "time_old time NOT NULL );";
		String eventLogTableQuery = "CREATE TABLE IF NOT EXISTS execution_events ("
				 +" id serial PRIMARY KEY,"+
				 " is_internal boolean NOT NULL,"+
				  "state varchar(50) NOT NULL,"+
				  "value boolean NOT NULL,"+
				  "iteration bigint NOT NULL,"+
				  "date_old date NOT NULL,"+
				  "time_old time NOT NULL );";
		
		try {
			dbConnection.executeUpdate(incommingEventsTableQuery);
			dbConnection.executeUpdate(eventLogTableQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void eraseEventsTable() {
		try {
			String incommingEventsTableQuery = "DELETE FROM external_events";
			String eventLogTableQuery = "DELETE FROM execution_events";

			dbConnection.executeUpdate(incommingEventsTableQuery);
			dbConnection.executeUpdate(eventLogTableQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void insertEvent(String stateName, String status, String iteration, String date, String time) {
		String query;
		
	    try {

			query = "INSERT INTO external_events (state, value,iteration, date_old, time_old)"
					+"VALUES( '"+stateName+"', "+status+", '"+iteration+"','"+date+"', '"+time+"')";
			dbConnection.executeUpdate(query);
    	
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	@Override
	public ResultSet findLatestEvents() {
	
		ResultSet result = null;
	
		String query;
		
	    try {
			query = "SELECT id,iteration,state,value,date_old,time_old FROM external_events";
			//query = "select * from measure where idDevice=12 order by time desc limit 1";
			//query = "select * from measure where time IN (SELECT MAX(time) from measure where idDevice=12)";
			result =  dbConnection.executeQueryOpenStatement(query);
	
		} catch (SQLException e1) {
		e1.printStackTrace();
		}
		return result;	
	}
	
	@Override
	public void logExternalEvent(String id, String iteration, String stateName, String status,
			String data, String time) {
	    try {
			String query = "INSERT INTO execution_events (is_internal,state, value, iteration,date_old, time_old)"
					+"VALUES(false, '"+stateName+"', '"+status+"','"+iteration+"','"+data+"', '"+time+"')";

			dbConnection.executeUpdate(query);
			
			query = "DELETE FROM external_events where id = "+id;
			
			dbConnection.executeUpdate(query);
			
    	
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	

	

	@Override
	public void createInternalEventsTable() {
	
		String query = "CREATE TABLE IF NOT EXISTS internal_events ("
				 +" id serial PRIMARY KEY,"+
				 " iteration bigint NOT NULL,"+
				  "state varchar(50) NOT NULL,"+
				  "value boolean NOT NULL,"+
				  "date  date NOT NULL,"+
				  "time  time NOT NULL,"+
				  "time_millis  bigint NOT NULL );";

				try {
					dbConnection.executeUpdate(query);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
	}

	@Override
	public void eraseInternalEventsTable() {
		String query;
		try {
			query = "DELETE FROM internal_events";
			dbConnection.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
	}

	@Override
	public void insertInternalEvent(String iteration, String stateName, String status, String date, String time, String time_millis) {
		String query;
		
	    try {
	    	query = "INSERT INTO  internal_events (iteration, state, value, date, time, time_millis) VALUES"+
	    			"('"+iteration+"','"+stateName+"', '"+status+"', '"+date+"', '"+time+"', '"+time_millis+"');";
	    	dbConnection.executeUpdate(query);
		
		} catch (SQLException e1) {
		e1.printStackTrace();
		}
		
	}
	
	@Override
	public void logInternalEvent(String id, String iteration, String stateName, String status,
			String data, String time) {
	    try {

			String query = "INSERT INTO execution_events (is_internal, state, value,iteration, date_old, time_old)"
					+"VALUES( 't','"+stateName+"', "+status+",'"+iteration+"','"+data+"', '"+time+"')";
			
			dbConnection.executeUpdate(query);
			
			query = "DELETE FROM internal_events where id = "+id;
			
			dbConnection.executeUpdate(query);
			
    	
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	@Override
	public ResultSet findLatestInternalEvents() {
		ResultSet result = null;
		String 	  query;

	    try {
	    	//Borrar el where id..
//			query = "SELECT id,state,value FROM \"internal_events\" WHERE id > '"+lastId+"'";		
//			result = dbConnection.executeQueryOpenStatement(query);
			query = "SELECT id,iteration,state,value,date,time FROM internal_events";//WHERE id > '"+lastId+"'";		
			result = dbConnection.executeQueryOpenStatement(query);
		
		} catch (SQLException e1) {
		e1.printStackTrace();
		}
		
		return result;
	}

	@Override
	public void createResultsTable(Vector<String> states) {
	
		String query ="";

		query = "CREATE TABLE IF NOT EXISTS results ("+
				"iteration bigint PRIMARY KEY,"+
				"system_time_millis bigint";	
					
		for(int i=0;i<states.size();i++){
			query = query + ","+states.get(i)+" boolean NOT NULL";
		}
		query = query + ")";
		
		createInsertResultQuery(states);
		
		try {
			dbConnection.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void createInsertResultQuery(Vector<String> states){
		resultsQuery = "INSERT INTO results (iteration,system_time_millis";
		for(int i=0;i<states.size();i++){
			resultsQuery = resultsQuery + ","+states.get(i)+"";
		}
		resultsQuery = resultsQuery + ") VALUES (";
	}

	@Override
	public void eraseResultsTable() {
	
		try {
			String query = "DELETE FROM results";
			dbConnection.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void dropResultsTable() {
	
		try {
			String query = "DROP TABLE IF EXISTS results";
			dbConnection.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void insertResult(String iteration,String time,Vector<Boolean> status) {

		String statusQuery = resultsQuery;
		
	    try {
		    statusQuery = statusQuery + "'"+iteration+"','"+time+"'";
	    	
	    	for(int i=0;i<status.size();i++){
	    		
	    		statusQuery = statusQuery + ","+status.get(i);
	    	}

	    	statusQuery = statusQuery + ");";	    	
			dbConnection.executeUpdate(statusQuery);
		
		} catch (SQLException e1) {
		e1.printStackTrace();
		}		
	}
	public ResultSet getDeviceMappingTableContent() {
		ResultSet resultSet = null;
		String query = null;

			try {
				query = "select * from device_mapping";
				//query = "select * from measure";
				resultSet = dbConnection.executeQueryOpenStatement(query);
		
	//		} catch (PSQLException e) {
	//		//	dbConnection.setConnection(-1);
			} catch (SQLException e) {
			//	dbConnection.setConnection(-2);
			}
		
		return resultSet;
	}

	public void newDeviceMappingTableRelation(String device, String state) {
		String query = null;
		try {
			query = "insert into device_mapping (device,implementation,state) values (DEFAULT, '"+device+"','"+state+"')";
			this.dbConnection.executeUpdate(query);
//		} catch (PSQLException e) {
//		//	dbConnection.setConnection(-1);

		} catch (SQLException e) {
			//dbConnection.setConnection(-2);
		}
		
	}

	public void removeDeviceMappingTableRelation(String device, String state) {
		String query = null;

		try {
			query = "DELETE FROM device_mapping where device ='"+
					device+"'and state ='"+state+"'";
			
			this.dbConnection.executeUpdate(query);
//		} catch (PSQLException e) {
		//	dbConnection.setConnection(-1);
		} catch (SQLException e) {
			e.printStackTrace();
		//	dbConnection.setConnection(-2);
		}
		
	}

	public ResultSet getEventsTableContent(boolean iteration) {
		String query = null;
		ResultSet resultSet = null;
		try {
			query = "SELECT iteration, state, value";
			if(!iteration) query = query + ", date_old, time_old";
			query = query + " FROM execution_events WHERE is_internal = 'f' ";
			//query = query + " FROM execution_events WHERE is_internal = 0 ";
			
			resultSet = dbConnection.executeQueryOpenStatement(query);
//		} catch (PSQLException e) {
		//	dbConnection.setConnection(-1);
		} catch (SQLException e) {
		//	dbConnection.setConnection(-2);
		}
		return resultSet;
	}

	public ResultSet getInternalEventsTableContent(boolean iteration) {
		String query = null;
		ResultSet resultSet = null;
		try {
			query = "SELECT iteration, state, value";
			if(!iteration) query = query + ", date_old, time_old";
			query = query + " FROM execution_events WHERE is_internal = 't' ";
			
			resultSet = this.dbConnection.executeQueryOpenStatement(query);
//		}catch (PSQLException e) {
		//	dbConnection.setConnection(-1);
		}  catch (SQLException e) {
		//	dbConnection.setConnection(-2);
		}
		return resultSet;
	}
	
	

	public ResultSet getResultsTableContent() {
		String 	  query     = null;
		ResultSet resultSet = null;
		try {
			query = "select * from results";
			
			resultSet = dbConnection.executeQueryOpenStatement(query);
//		} catch (PSQLException e) {
		//	dbConnection.setConnection(-1);
		} catch (SQLException e) {
		//	dbConnection.setConnection(-2);
		}
		return resultSet;
	}
	
	@Override
	public void createDeviceMappingTable() {
		createDevicesTable();
		String query ="";

		 query = "CREATE TABLE IF NOT EXISTS device_mapping ("
				 			+"id varchar(50) PRIMARY KEY,"
							+"device varchar(50),"+
							"implementation varchar(50) references devices(name),"+
							"state varchar(50) NOT NULL);";
		 ///System.out.println(query);
	 	   
		try {
			dbConnection.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	
	public boolean databaseExists(String dbName){
		boolean result = false;		try {
			Connection mysqlDBConnection = DriverManager.getConnection(configs.getConnectionString()+"mysql", configs.getUser(), configs.getPassword());
			Statement stmt = null;
			String query = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '"+dbName.toLowerCase()+"'";
	       //String query = "SELECT count(*) from pg_catalog.pg_database where datname = '"+dbName.toLowerCase()+"'"; //comment

			//String query = "SELECT 1 FROM pg_database WHERE datname = '"+dbName+"'";
			  rs = null;
			   if(mysqlDBConnection != null){
				   stmt = mysqlDBConnection.createStatement();
				   if(stmt != null){
					   rs = stmt.executeQuery(query); //comment					   
				   }
			   }
			   
			   if(rs.next()){
				   result = true;
			   }else{
				   result = false;
			   }
//			 if(rs.next()){
//				 if(rs.getInt("count") > 0) result = true;
//				 else return result = false;
//			 }
//			 else result = false;
			 
			 stmt.close();
			 rs.close();
			 mysqlDBConnection.close();
		 } catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private boolean createDBIfDoesNotExist(String dbName){
//		boolean result = false;
		try {
			Connection mysqlDBConnection = DriverManager.getConnection( configs.getConnectionString()+"mysql", configs.getUser(), configs.getPassword());
			Statement stmt = null;
			 String query = "CREATE DATABASE "+dbName.toLowerCase()+"";
			//String query = "SELECT 1 FROM pg_database WHERE datname = '"+dbName+"'";
			  rs = null;
			   if(mysqlDBConnection != null){
				   stmt = mysqlDBConnection.createStatement();
				   if(stmt != null){
					   stmt.executeUpdate(query);

				   }
			   }
			 
			 
			 stmt.close();
			 mysqlDBConnection.close();
		 } catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean getAtLeastOneValueQuery(String query){
		//System.out.println(query+"2");
		ResultSet resultSet;
		boolean result = false;
		   // String query  = operation.getAtLeastOneValueQuery(TOp);
			//	System.out.println(query);		
			try {
				resultSet = dbConnection.executeQueryOpenStatement(query);
				if( (resultSet != null) && (resultSet.next()) ){
					result = true;
				}
				dbConnection.closeStatement();
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//result = databaseOperations.isAtLeastOneValueDuringWholeInterval( TOp.getName(), ""+TOp.getStatus());


		return result;
	}
	
	@Override
	public boolean getAtLeastOneValueQueryPresent(String since, String until,
			String state, String status) {
		String query = "SELECT "+state+" FROM results WHERE iteration >= '"+since+
	            "' and iteration < '"+until+"' and "+
	            state+" = '"+status+"'";

		return getAtLeastOneValueQuery(query);
	}



	@Override
	public boolean getAtLeastOneValueQueryAbsolute(String since, String until,
			String state, String status) {
		String query = "SELECT "+state+" FROM results WHERE iteration >= '"+since+
	            "' and iteration <= '"+until+"' and "+
	            state+" = '"+status+"'";

	           
		return getAtLeastOneValueQuery(query);
	}

	private boolean getSameValueQuery(String query){
		boolean sameValue = true;
		ResultSet result;
		//System.out.println(query+"2");
			try {
				result = dbConnection.executeQueryOpenStatement(query);
				if(result.next()){
					sameValue = false;
				}
				result.close();
				dbConnection.closeStatement();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return sameValue;
	}
	
	@Override
	public boolean getSameValueQueryPresent(String since, String until,
			String state, String status) {
		String query = "SELECT "+state+" FROM results WHERE iteration >= '"+since+"' and "+
		   	      "iteration < '"+until+"' and "+state+
		   	      " = '"+!Boolean.parseBoolean(status)+"'";
		
		return getSameValueQuery(query);
	}

	@Override
	public boolean getSameValueQueryAbsolute(String since, String until,
			String state, String status) {
		String query = "SELECT "+state+" FROM results WHERE system_time_millis >= '"+since+"' and "+
		   	      "system_time_millis <= '"+until+"' and "+state+
		   	      " = '"+!Boolean.parseBoolean(status)+"'";
		//System.out.println(query);
		return getSameValueQuery(query);
	}

	private boolean getEqualToFirstQuery(String query,String state,String status){
		ResultSet result;
		//System.out.println(query);
		try {
			result = dbConnection.executeQueryOpenStatement(query);
			
			if(result.next()){
				boolean b = result.getBoolean(state);
				if(Boolean.parseBoolean(status) == b){
					return true;
				}
			}
//			try {
//				Thread.sleep(52000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			result.close();
			dbConnection.closeStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public boolean getEqualToFirstQueryPresent(String since, String state,String status) {
		String query = "SELECT "+state+" FROM results WHERE  iteration = '"+since+"'";
		return getEqualToFirstQuery(query,state,status);
	}

	@Override
	public boolean getEqualToFirstQueryAbsolute(String since, String state, String status) {
		String query = "SELECT "+state+" FROM results WHERE iteration = (SELECT MIN(iteration) FROM results WHERE iteration >= '"+since+"')" ;
		return getEqualToFirstQuery(query,state,status);
	}

	@Override
	public String getDevice(String state) {
		String query,result = null;
		ResultSet resultSet;
		//System.out.println(state);
		
		query = "SELECT device FROM device_mapping WHERE state = '"+state+"'";
		
		//System.out.println(query);
		try {
			resultSet = dbConnection.executeQueryOpenStatement(query);
			if(resultSet.next()){
				result = resultSet.getString("device");
				resultSet.close();
				dbConnection.closeStatement();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result == null) System.out.println("WARNING: Device related with '"+state+"' in table 'device_mapping' was not found.");
		return result;
	}
	
	@Override
	public String getState(String device) {
		String query,result = null;
		ResultSet resultSet;
		//query = "SELECT state FROM device_mapping WHERE device = '"+device+"'";
		query = "SELECT state FROM data_storage.measure WHERE device = '"+device+"'";
		
		try {
			resultSet = dbConnection.executeQueryOpenStatement(query);
			if(resultSet.next()){
				result = resultSet.getString("state");
			}
			resultSet.close();
			dbConnection.closeStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public boolean getStatus(String state) {
		String query;
		ResultSet resultSet;
		boolean result = false;
		query = "SELECT "+state+" FROM sensors.results WHERE iteration = (SELECT MAX(iteration) from sensors.results)";
		try {
			resultSet = dbConnection.executeQueryOpenStatement(query);
			if(resultSet.next()){
				result = resultSet.getBoolean(state);
			}
			resultSet.close();
			dbConnection.closeStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void createDevicesTable() {
			String query = "CREATE TABLE IF NOT EXISTS devices ("
					 + "name varchar(50) PRIMARY KEY,"+
					  "max_value varchar(50),"+
					  "min_value varchar(50),"+
					  "has_boolean_values boolean NOT NULL);";
			//System.out.println(query);
			
			try {
				dbConnection.executeUpdate(query);
	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	@Override
	public void newDevicesTableRelation(String id, String name, String model, String location, String dataType, String maxValue,String minValue,String isBoolean) {
		
		String query;
		
	    try {

			query = "INSERT INTO devices (vera_id, name, model, location, data_type, max_value, min_value, has_boolean_values)"
					+"VALUES( '"+id+"', '"+name+"', '"+model+"', '"+location+"', "+dataType+"', "+maxValue+",'"+minValue+"', '"+isBoolean+"')";
			
			dbConnection.executeUpdate(query);
    	
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	@Override
	public void removeSensorImplementation(String name, String maxValue,
			String minValue, String isOnOff) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ResultSet getDevicesTableContent() {
		ResultSet resultSet = null;
		String query = null;

			try {
				query = "select * from devices";
				resultSet = dbConnection.executeQueryOpenStatement(query);
		
	//		} catch (PSQLException e) {
	//		//	dbConnection.setConnection(-1);
			} catch (SQLException e) {
			//	dbConnection.setConnection(-2);
			}
		
		return resultSet;

	}

	@Override
	public void dropDatabase(String dbName) {
		try {
			Connection mysqlDBConnection = DriverManager.getConnection( configs.getConnectionString()+"mysql", configs.getUser(), configs.getPassword());
			Statement stmt = null;
			 String query = "DROP DATABASE "+dbName.toLowerCase()+"";
//			 String session = "SELECT pg_terminate_backend(pg_stat_activity.pid)"+
//					 		  "FROM pg_stat_activity "+
//					 		  "WHERE pg_stat_activity.datname = '"+dbName.toLowerCase()+"'"+
//					 		  "AND pid <> pg_backend_pid()";

			//String query = "SELECT 1 FROM pg_database WHERE datname = '"+dbName+"'";
			  rs = null;
			   if(mysqlDBConnection != null){
				   stmt = mysqlDBConnection.createStatement();
				   if(stmt != null){
					  //rs =  stmt.executeQuery(session);
					   stmt.executeUpdate(query);

				   }
			   }
			 
			 if(stmt!=null)stmt.close();
			 if(rs!=null)rs.close();
			 mysqlDBConnection.close();
		 } catch (SQLException e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public void eraseDeviceMappingTable() {
		try {
			String query = "DELETE FROM device_mapping";
			dbConnection.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void eraseDevicesTable() {
		try {
			String query = "DELETE FROM devices";
			dbConnection.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public long getIterationFromRealTime(String string) {
		String query;
		ResultSet resultSet;
		long result = 0;
		query = "SELECT MIN(iteration) FROM results WHERE system_time_millis >= "+string;
	//	System.out.println(query);
		try {
			resultSet = dbConnection.executeQueryOpenStatement(query);
			if(resultSet.next()){
				result = resultSet.getLong(1);
				//System.out.println("ITERATION DETECTED: "+result );
			}
			resultSet.close();
			dbConnection.closeStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean createDatabase(String dbName) {
		return this.createDBIfDoesNotExist(dbName);
	}

	@Override
	public void createDataTypesTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eraseDataTypesTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertDataTypes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDataTypeId(String dataType) {
		return dataType;
		// TODO Auto-generated method stub
		
	}

	

	}


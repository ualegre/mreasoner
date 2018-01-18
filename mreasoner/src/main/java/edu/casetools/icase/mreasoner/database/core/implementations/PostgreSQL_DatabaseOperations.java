package edu.casetools.icase.mreasoner.database.core.implementations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import org.postgresql.util.PSQLException;

import edu.casetools.icase.mreasoner.configs.data.db.MDBConfigs;
import edu.casetools.icase.mreasoner.database.core.connection.DBConnection;
import edu.casetools.icase.mreasoner.database.core.connection.DBConnection.STATUS;
import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperations;

public class PostgreSQL_DatabaseOperations extends DatabaseOperations{
	String resultsQuery;
	MDBConfigs configs;
	private ResultSet rs;
	
	public PostgreSQL_DatabaseOperations(MDBConfigs configs){
		super(configs);
		this.configs = configs;
		resultsQuery = "";
		//System.out.println("SOLO IMPLEMENTADO PARA SIMULATION = FALSE (PostgreSQL_DatabaseOperations.java)");

	}
	
	@Override
	public void connect() {
		try {
			dbConnection.connect();
			dbConnection.setConnection(STATUS.CONNECTED);
		} catch (ClassNotFoundException e) {
			dbConnection.setConnection(STATUS.ERROR);
		//	e.printStackTrace();
		} catch (PSQLException e) {	
			dbConnection.setConnection(STATUS.ERROR);
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
		}catch (PSQLException e) {		
			dbConnection.setConnection(STATUS.ERROR);
		} catch (SQLException e) {
			dbConnection.setConnection(STATUS.ERROR);
		//	e.printStackTrace();
		}
		
	}

	@Override
	public void createEventsTable() {
		
		String incommingEventsTableQuery = "CREATE TABLE IF NOT EXISTS \"external_events\" ("
				 +" \"id\" serial PRIMARY KEY,"+
				  "\"state\" varchar(50) NOT NULL,"+
				  "\"value\" boolean NOT NULL,"+
				  "\"iteration\" bigint NOT NULL,"+
				  "\"date_old\" date NOT NULL,"+
				  "\"time_old\" time NOT NULL );";
		String eventLogTableQuery = "CREATE TABLE IF NOT EXISTS \"events_log\" ("
				 +" \"id\" serial PRIMARY KEY,"+
				 " \"is_internal\" boolean NOT NULL,"+
				  "\"state\" varchar(50) NOT NULL,"+
				  "\"value\" boolean NOT NULL,"+
				  "\"iteration\" bigint NOT NULL,"+
				  "\"date_old\" date NOT NULL,"+
				  "\"time_old\" time NOT NULL );";
		
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
			String incommingEventsTableQuery = "DELETE FROM \"external_events\"";
			String eventLogTableQuery = "DELETE FROM \"events_log\"";

			dbConnection.executeUpdate(incommingEventsTableQuery);
			dbConnection.executeUpdate(eventLogTableQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void insertEvent(String stateName, String status, String iteration, String date, String time) {
		String query = "INSERT INTO \"external_events\" (state, value,iteration, date_old, time_old)"
					+"VALUES( '"+stateName+"', '"+status+"', '"+iteration+"','"+date+"', '"+time+"')";
			
		this.executeUpdate(query);
   
	}

	@Override
	public ResultSet findLatestEvents() {
		String query= "SELECT id,iteration,state,value,date_old,time_old FROM \"external_events\"";
		return  this.executeQueryOpenStatement(query);

	}
	
	@Override
	public void logExternalEvent(String id, String iteration, String stateName, String status,
			String data, String time) {

			String query = "INSERT INTO \"events_log\" (is_internal,state, value, iteration,date_old, time_old)"
					+"VALUES('f', '"+stateName+"', '"+status+"','"+iteration+"','"+data+"', '"+time+"')";

			this.executeUpdate(query);
			
			query = "DELETE FROM \"external_events\" where id = "+id;
			
			this.executeUpdate(query);
		
	}
	

	

	@Override
	public void createInternalEventsTable() {
	
		String query = "CREATE TABLE IF NOT EXISTS \"internal_events\" ("
				 +" \"id\" serial PRIMARY KEY,"+
				 " \"iteration\" bigint NOT NULL,"+
				  "\"state\" varchar(50) NOT NULL,"+
				  "\"value\" boolean NOT NULL,"+
				  "\"date\"  date NOT NULL,"+
				  "\"time\"  time NOT NULL,"+
				  "\"time_millis\"  bigint NOT NULL );";

		executeUpdate(query);
		
	}

	@Override
	public void eraseInternalEventsTable() {
			this.executeUpdate("DELETE FROM \"internal_events\"");
		
	}

	@Override
	public void insertInternalEvent(String iteration, String stateName, String status, String date, String time, String time_millis) {
		String 	query = "INSERT INTO  \"internal_events\" (\"iteration\","+
						" \"state\", \"value\", \"date\", \"time\", \"time_millis\") VALUES"+
						"('"+iteration+"','"+stateName+"', '"+status+"', '"+date+"', '"+
						time+"', '"+time_millis+"');";
	    this.executeUpdate(query);

	}
	
	@Override
	public void logInternalEvent(String id, String iteration, String stateName, String status,
			String data, String time) {

			String query = "INSERT INTO \"events_log\" (is_internal, state, value,iteration, date_old, time_old)"
					+"VALUES( 't','"+stateName+"', "+status+",'"+iteration+"','"+data+"', '"+time+"')";
			
			this.executeUpdate(query);
			
			query = "DELETE FROM \"internal_events\" where id = "+id;
			
			this.executeUpdate(query);
			
		
	}

	@Override
	public ResultSet findLatestInternalEvents() {
		String 	query = "SELECT id,iteration,state,value,date,time FROM \"internal_events\"";
	    return executeQueryOpenStatement(query);
	}


	@Override
	public void createResultsTable(Vector<String> states) {
	
		String query ="";

		query = "CREATE TABLE IF NOT EXISTS \"results\" ("+
				"\"iteration\" bigint PRIMARY KEY,"+
				"\"system_time_millis\" bigint";	
					
		for(int i=0;i<states.size();i++){
			query = query + ",\""+states.get(i)+"\" boolean NOT NULL";
		}
		query = query + ")";
		
		createInsertResultQuery(states);
		
		executeUpdate(query);
		
	}
	
	private void createInsertResultQuery(Vector<String> states){
		resultsQuery = "INSERT INTO \"results\" (\"iteration\",\"system_time_millis\"";
		for(int i=0;i<states.size();i++){
			resultsQuery = resultsQuery + ",\""+states.get(i)+"\"";
		}
		resultsQuery = resultsQuery + ") VALUES (";
	}

	@Override
	public void eraseResultsTable() {
		this.executeUpdate("DELETE FROM \"results\"");
	}
	
	@Override
	public void dropResultsTable() {
		String query = "DROP TABLE IF EXISTS \"results\"";
		this.executeUpdate(query);
	}

	@Override
	public void insertResult(String iteration,String time,Vector<Boolean> status) {

		String statusQuery = resultsQuery;
		    statusQuery = statusQuery + "'"+iteration+"','"+time+"'";
	    	
	    	for(int i=0;i<status.size();i++){
	    		
	    		statusQuery = statusQuery + ",'"+status.get(i)+"'";
	    	}

	    	statusQuery = statusQuery + ");";
			this.executeUpdate(statusQuery);
			
	}
	public ResultSet getDeviceMappingTableContent() {
		return this.executeQueryOpenStatement("select * from \"device_mapping\"");
	}

	public void newSensorTableRelation(String device,String implementation,String state) {
		String 	query = "insert into \"device_mapping\" (device,implementation"+
						",state) values ('"+device+"','"+implementation+"','"+state+"')";
		this.executeUpdate(query);
		
	}

	public void removeSensorTableRelation(String device,String implementation, String state) {
		String 	query = "DELETE FROM \"device_mapping\" where device ='"+
				device+"' and implementation ='"+implementation+"' and state ='"+state+"'";
			
		this.executeUpdate(query);
	
	}

	public ResultSet getEventsTableContent(boolean iteration) {
		String 	query = "SELECT iteration, state, value";
			
		if(!iteration) 
			query = query + ", date_old, time_old";
		
		query = query + " FROM \"events_log\" WHERE \"is_internal\" = 'f' ";
			
		return this.executeQueryOpenStatement(query);

	}

	public ResultSet getInternalEventsTableContent(boolean iteration) {
		String query = "SELECT iteration, state, value";
		
		if(!iteration) 
			query = query + ", date_old, time_old";
		query = query + " FROM \"events_log\" WHERE \"is_internal\" = 't' ";
			
		return this.executeQueryOpenStatement(query);
	}
	
	public ResultSet getResultsTableContent() {
		return this.executeQueryOpenStatement("select * from \"results\"");
	}
	
	@Override
	public void createDeviceMappingTable() {
		createDevicesTable();
		String query = "CREATE TABLE IF NOT EXISTS \"device_mapping\" ("+
				  "\"id\" SERIAL PRIMARY KEY,"+	
				  "\"device\" references \"devices\"(veraId),"+
		 		  "\"state\" varchar(50) NOT NULL"
		 		  + ");";
	 	   
		executeUpdate(query);
	}

	@Override
	public boolean createDatabase(String dbName) {
		try {
			Connection postgresDBConnection = DriverManager.getConnection( configs.getConnectionString()+"postgres", configs.getUser(), configs.getPassword());
			Statement stmt = null;
			 String query = "CREATE DATABASE "+dbName.toLowerCase()+"";
			  rs = null;
			   if(postgresDBConnection != null){
				   stmt = postgresDBConnection.createStatement();
				   if(stmt != null){
					   stmt.executeUpdate(query);

				   }
			   }
			 
			 
			 stmt.close();
			 postgresDBConnection.close();
		 } catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean databaseExists(String dbName){
		boolean result = false;
		try {
			Connection postgresDBConnection = DriverManager.getConnection( configs.getConnectionString()+"postgres", configs.getUser(), configs.getPassword());
			Statement stmt = null;
			 String query = "SELECT count(*) from pg_catalog.pg_database where datname = '"+dbName.toLowerCase()+"'";
			//String query = "SELECT 1 FROM pg_database WHERE datname = '"+dbName+"'";
			  rs = null;
			   if(postgresDBConnection != null){
				   stmt = postgresDBConnection.createStatement();
				   if(stmt != null){
					   rs = stmt.executeQuery(query);

				   }
			   }
			 if(rs.next()){
				 if(rs.getInt("count") > 0) result = true;
				 else return result = false;
			 }
			 else result = false;
			 
			 stmt.close();
			 rs.close();
			 postgresDBConnection.close();
		 } catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	private boolean getAtLeastOneValueQuery(String query){
		//System.out.println(query+"2");
		ResultSet resultSet;
		boolean result = false;
		resultSet = this.executeQueryOpenStatement(query);
		if( (resultSet != null) && next(resultSet) ){
			result = true;
		}
		close(resultSet);

		return result;
	}
	
	@Override
	public boolean getAtLeastOneValueQueryPresent(String since, String until,
			String state, String status) {
		String query = "SELECT \""+state+"\" FROM \"results\" WHERE iteration >= '"+since+
	            "' and iteration < '"+until+"' and \""+
	            state+"\" = '"+status+"'";

		return getAtLeastOneValueQuery(query);
	}



	@Override
	public boolean getAtLeastOneValueQueryAbsolute(String since, String until,
			String state, String status) {
		String query = "SELECT \""+state+"\" FROM \"results\" WHERE iteration >= '"+since+
	            "' and iteration <= '"+until+"' and \""+
	            state+"\" = '"+status+"'";
		
		return getAtLeastOneValueQuery(query);
	}

	private boolean getSameValueQuery(String query){
		boolean sameValue = true;
		ResultSet result = this.executeQueryOpenStatement(query);
		
		if(next(result)){
			sameValue = false;
		}
		this.close(result);

		return sameValue;
	}
	
	@Override
	public boolean getSameValueQueryPresent(String since, String until,
			String state, String status) {
		String query = "SELECT \""+state+"\" FROM \"results\" WHERE iteration >= '"+since+"' and "+
		   	      "iteration < '"+until+"' and \""+state+
		   	      "\" = '"+!Boolean.parseBoolean(status)+"'";
		
		return getSameValueQuery(query);
	}

	@Override
	public boolean getSameValueQueryAbsolute(String since, String until,
			String state, String status) {
		String query = "SELECT \""+state+"\" FROM \"results\" WHERE system_time_millis >= '"+since+"' and "+
		   	      "system_time_millis <= '"+until+"' and \""+state+
		   	      "\" = '"+!Boolean.parseBoolean(status)+"'";

		return getSameValueQuery(query);
	}

	private boolean getEqualToFirstQuery(String query,String state,String status){
		ResultSet result = this.executeQueryOpenStatement(query);
		try {			
				if(next(result)){
					boolean b;
	
						b = result.getBoolean(state);
					if(Boolean.parseBoolean(status) == b){
						return true;
					}
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		this.close(result);
		
		return false;
	}
	
	@Override
	public boolean getEqualToFirstQueryPresent(String since, String state,String status) {
		String query = "SELECT \""+state+"\" FROM \"results\" WHERE  iteration = '"+since+"'";
		return getEqualToFirstQuery(query,state,status);
	}

	@Override
	public boolean getEqualToFirstQueryAbsolute(String since, String state, String status) {
		String query = "SELECT \""+state+"\" FROM \"results\" WHERE iteration = (SELECT MIN(iteration) FROM \"results\" WHERE iteration >= '"+since+"')" ;
		return getEqualToFirstQuery(query,state,status);
	}

	@Override
	public String getDevice(String state) {
		String result = null;
		String query = "SELECT device FROM \"device_mapping\" WHERE state = '"+state+"'";
		ResultSet resultSet = this.executeQueryOpenStatement(query);
		
		try {
			if(next(resultSet)){
				result = resultSet.getString("device");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.close(resultSet);
		
		if(result == null) 
			System.out.println("WARNING: Device related with '"+state+"' in table 'device_mapping' was not found.");
		
		return result;
	}
	
	@Override
	public String getState(String device) {
		String query,result = null;
		ResultSet resultSet;
		query = "SELECT state FROM \"device_mapping\" WHERE device = '"+device+"'";
	
		try {
			resultSet = dbConnection.executeQueryOpenStatement(query);
			if(next(resultSet)){
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
		query = "SELECT \""+state+"\" FROM \"results\" WHERE iteration = (SELECT MAX(\"iteration\") from \"results\");";
		
		try {
			resultSet = dbConnection.executeQueryOpenStatement(query);
			if(next(resultSet)){
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
			String query = "CREATE TABLE IF NOT EXISTS \"devices\" ("+
					  "\"veraId\" varchar(50) PRIMARY KEY,"+
					  "\"dataType\" varchar(50) FOREIGN KEY,"+
					  "\"name\" varchar(50),"+
					  "\"max_value\" varchar(50),"+
					  "\"min_value\" varchar(50),"+
					  "\"has_boolean_values\" boolean NOT NULL);";
			
			executeUpdate(query);
		
	}

	@Override
	public void newDevicesRelation(String name, String maxValue,
			String minValue, String isOnOff) {
		
		String query = "INSERT INTO \"devices\" (name, max_value, min_value, has_boolean_values)"
				+"VALUES( '"+name+"', "+maxValue+",'"+minValue+"', '"+isOnOff+"')";
		
		this.executeUpdate(query);
    	
		
	}

	@Override
	public void removeSensorImplementation(String name, String maxValue,
			String minValue, String isOnOff) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ResultSet getDevicesTableContent() {
		return  this.executeQueryOpenStatement("select * from \"devices\"");
	}

	@Override
	public void dropDatabase(String dbName) {
		try {
			Connection postgresDBConnection = DriverManager.getConnection( configs.getConnectionString()+"postgres", configs.getUser(), configs.getPassword());
			Statement stmt = null;
			 String query = "DROP DATABASE \""+dbName.toLowerCase()+"\"";
			 String session = "SELECT pg_terminate_backend(pg_stat_activity.pid)"+
					 		  "FROM pg_stat_activity "+
					 		  "WHERE pg_stat_activity.datname = '"+dbName.toLowerCase()+"'"+
					 		  "AND pid <> pg_backend_pid()";

			//String query = "SELECT 1 FROM pg_database WHERE datname = '"+dbName+"'";
			  rs = null;
			   if(postgresDBConnection != null){
				   stmt = postgresDBConnection.createStatement();
				   if(stmt != null){
					  rs =  stmt.executeQuery(session);
					   stmt.executeUpdate(query);

				   }
			   }
			 
			 if(stmt!=null)stmt.close();
			 if(rs!=null)rs.close();
			 postgresDBConnection.close();
		 } catch (SQLException e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public void eraseDeviceMappingTable() {
		this.executeUpdate("DELETE FROM \"device_mapping\"");
	}

	@Override
	public void eraseDevicesTable() {
		this.executeUpdate("DELETE FROM \"devices\"");
	}

	@Override
	public long getIterationFromRealTime(String string) {
		long result = 0;
		String query = "SELECT MIN(\"iteration\") FROM results WHERE system_time_millis >= "+string;
		ResultSet resultSet = this.executeQueryOpenStatement(query);

		try {
			if(next(resultSet)){
				result = resultSet.getLong(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.close(resultSet);
		
		return result;
	}

	@Override
	public void createDataTypesTable() {
		String query = "CREATE TABLE IF NOT EXISTS \"data_types\" ("+
				  "\"id\" SERIAL PRIMARY KEY,"+
				  "\"name\" varchar(50) FOREIGN KEY);";
		
		executeUpdate(query);
		insertDataTypes();
	}
	
	@Override
	public void insertDataTypes() {		
		String query = "";
		for(String dataType : dataTypes){
			query = "INSERT INTO \"data_types\" VALUES(DEFAULT, '"+dataType+"');";
			executeUpdate(query);
		}
		
	}

	@Override
	public void eraseDataTypesTable() {
		this.executeUpdate("DELETE FROM \"data_types\"");
	}

	@Override
	public String getDataTypeId(String dataType) {
		String query = "SELECT \"id\" FROM data_types WHERE \"name\" = '"+dataType+"'";
		int result = 0;
		ResultSet resultSet = this.executeQueryOpenStatement(query);

		try {
			if(next(resultSet)){
				result = resultSet.getInt(0);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.close(resultSet);
		
		return Integer.toString(result);
	}
	
	private void executeUpdate(String query) {
		try {
			dbConnection.executeUpdate(query);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ResultSet executeQueryOpenStatement(String query) {
		ResultSet result = null;
		try {
			result = dbConnection.executeQueryOpenStatement(query);
		
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return result;
	}
	
	private boolean next(ResultSet resultSet) {
		try {
			return resultSet.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private void close(ResultSet resultSet) {
		try {
			resultSet.close();
			dbConnection.closeStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}

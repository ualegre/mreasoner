package edu.casetools.icase.mreasoner.database.core.connection;

import java.sql.*;

import edu.casetools.icase.mreasoner.configs.data.db.MDBConfigs;

public class DBConnection {

   private Connection conn = null;
   private Statement stmt = null;
   MDBConfigs configs;
   private STATUS connectionStatus;
   public enum STATUS {CONNECTED,DISCONNECTED,ERROR};
   
   public DBConnection(MDBConfigs dbConfigs){
	   
	   connectionStatus = STATUS.DISCONNECTED;
	   this.configs = dbConfigs;
	
   }
   
   public void connect() throws SQLException, ClassNotFoundException{
	   
		Class.forName(configs.getDriver());
		conn = DriverManager.getConnection( configs.getConnectionStringWithDB(), configs.getUser(), configs.getPassword());
	    stmt = conn.createStatement();
	   
   }
   
   public STATUS checkConnection(){
	   return connectionStatus;
   }
   
   public void setConnection(STATUS connectionStatus){
	   this.connectionStatus = connectionStatus;
   }
      
   public void executeUpdate(String query) throws SQLException{
	   if(conn != null){
		   stmt = conn.createStatement();
		   if(stmt != null){
			   stmt.executeUpdate(query);
			   stmt.close();
		   }
	   }
   }
   
   public ResultSet executeQuery(String query) throws SQLException{
	   ResultSet resultSet = null;
	   if(conn != null){
		   stmt = conn.createStatement();
		   if(stmt != null){
			   resultSet = stmt.executeQuery(query);
			   stmt.close();
		   }
	   }
	   return resultSet;
	}
   
   public ResultSet executeQueryOpenStatement(String query) throws SQLException{
	  
	   ResultSet resultSet = null;
	   if(conn != null){
	   stmt = conn.createStatement();
	   resultSet = stmt.executeQuery(query);
	   }else{
		   System.out.println("WARNING CONNECTION = NULL");
	   }
	   return resultSet;
	}
   
  public void closeStatement() throws SQLException{
	   try { stmt.close(); } catch (Exception e) { /* ignored */ };   
   }
   
   public void disconnect() throws SQLException{
	   try { conn.close(); } catch (Exception e) { /* ignored */ };   
   }
   
   public MDBConfigs getDBConfigs(){	   
	   return this.configs;
	   
   }

public boolean isConnected() {
	if(connectionStatus == STATUS.CONNECTED) return true;
	else return false;
}
   
   
}
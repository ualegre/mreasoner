package edu.casetools.icase.mreasoner.configs.data.db;

import edu.casetools.icase.mreasoner.database.core.MDBImplementations.DB_IMPLEMENTATION;

public class MDBConfigs {

	private String dbType,driver,dbName,ip,port,user,password,table;

	public MDBConfigs(){
		setDbType("PostgreSQL");
		setDriver("<Driver>");
		setDbName("<name>");
		setTable("<tablename>");
		setIp("localhost");
		setPort("<port>");
		setUser("<user>");
		setPassword("<password>");
	}
	   
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	public String getConnectionString() {
		return "jdbc:"+this.getDbType().toLowerCase()+"://"+this.getIp()+":"+this.getPort()+"/";
	}
	
	public String getConnectionStringWithDB() {
		return "jdbc:"+this.getDbType().toLowerCase()+"://"+this.getIp()+":"+this.getPort()+"/"+this.getDbName();
	}
	
	public DB_IMPLEMENTATION getDBImplementation(){
		if(dbType.equalsIgnoreCase("PostgreSQL"))
			return DB_IMPLEMENTATION.POSTGRESQL;
		else if (dbType.equalsIgnoreCase("MySQL"))
			return DB_IMPLEMENTATION.MYSQL;
		else return DB_IMPLEMENTATION.POSTGRESQL;
		
	}
	
	public String parseConfigs(){
		String result = "";
				
		//Database Info
		result = result+"<DATABASE_TYPE> \n";
		result = result+getDbType()+"\n";
		result = result+"</DATABASE_TYPE> \n";
		
		result = result+"<DATABASE_DRIVER> \n";
		result = result+getDriver()+"\n";
		result = result+"</DATABASE_DRIVER> \n";
		
		
		result = result+"<DATABASE_IP> \n";
		result = result+getIp()+"\n";
		result = result+"</DATABASE_IP> \n";
		
		result = result+"<DATABASE_PORT> \n";
		result = result+getPort()+"\n";
		result = result+"</DATABASE_PORT> \n";
		
		result = result+"<DATABASE_USER> \n";
		result = result+getUser()+"\n";
		result = result+"</DATABASE_USER> \n";
		
		result = result+"<DATABASE_PASSWORD> \n";
		result = result+getPassword()+"\n";
		result = result+"</DATABASE_PASSWORD> \n";
		
		result = result+"<DATABASE_NAME> \n";
		result = result+getDbName()+"\n";
		result = result+"</DATABASE_NAME> \n";
		

		return result;	
	}

	   
}

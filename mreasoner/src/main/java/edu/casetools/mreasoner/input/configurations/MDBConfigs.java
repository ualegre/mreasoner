package edu.casetools.mreasoner.input.configurations;

public class MDBConfigs {

	private String dbType,driver,dbName,ip,port,user,password,table;

	   
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
	   
	   
	   
}

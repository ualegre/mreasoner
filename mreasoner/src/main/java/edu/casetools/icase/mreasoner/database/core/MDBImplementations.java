package edu.casetools.icase.mreasoner.database.core;

import java.util.Arrays;

public class MDBImplementations {

	/***************************************************
	 * 
	 * @author Unai
	 * To add a new database just add another name to the DB_IMPLEMENTATION
	 * E.g.: 	public static enum DB_IMPLEMENTATION {POSTGRESQL,MYSQL,MY_OWN_DATABASE}; 
	 * 
	 */
	public static enum DB_IMPLEMENTATION {POSTGRESQL,MYSQL}; 
	public static enum DATA_TYPES {BOOLEAN,BYTE,CHAR,SHORT,INTEGER,LONG,FLOAT,DOUBLE}; 
	
	public static String[] getDBImplementationNames() {
	    return Arrays.toString(DB_IMPLEMENTATION.values()).replaceAll("^.|.$", "").split(", ");
	}
	
	public static String[] getDataTypeNames() {
	    return Arrays.toString(DATA_TYPES.values()).replaceAll("^.|.$", "").split(", ");
	}
	
}

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
	
	public static String[] getNames() {
	    return Arrays.toString(DB_IMPLEMENTATION.values()).replaceAll("^.|.$", "").split(", ");
	}
	
}

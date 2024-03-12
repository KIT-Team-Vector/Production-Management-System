package edu.kit.pms.im.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface to get mysql connections via jdbc, 
 */
public interface ConnectionProvider {
	
	/**
	 * Used to get a connection to communicate with a mysql database
	 * 
	 * @return connection
	 * @throws SQLException if connection could not be created
	 */
	Connection getConnection() throws SQLException;

}

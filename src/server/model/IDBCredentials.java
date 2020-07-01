package server.model;
/**
 * Provides the database credentials for the program.
 * @author Jason Paul
 * @version 1.0
 * @since Nov 15, 2019
 */
public interface IDBCredentials {
	
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/client_db?useSSL=false";

	//  Database credentials
	static final String USERNAME = "root";
	static final String PASSWORD = "MjAy9228*ql";

}

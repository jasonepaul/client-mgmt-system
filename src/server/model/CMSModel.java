package server.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Provides fields and methods for working with customer data, conducting 
 * database queries and updates, and generating results. A database table is 
 * created if it doesn't already exist. 
 * @author Jason Paul
 * @version 1.0
 * @since Nov 15, 2019
 */
public class CMSModel implements IDBCredentials{
	
	private Connection conn;
	private PreparedStatement pStat;
	private String tableName = "customers";
	private String dataFile = "customers.txt";
	private static int id = 0;
	private ArrayList<Customer> customerSearchResult;
	
	/**
	 * Constructs a new Model, first getting a database connection and then 
	 * checking if the table exists, creating a new one if it doesn't.
	 */
	public CMSModel() {
		customerSearchResult = new ArrayList<Customer>();
		getConnection();
		checkTableStatus();
	}

	/**
	 * Gets the database connection.
	 */
	private void getConnection() {
		try {
			Class.forName(JDBC_DRIVER);
			conn =  DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("Connected to: " + DB_URL + "\n");
		}
		catch(SQLException e) { e.printStackTrace(); }
		catch(Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * Checks whether the customer database table exists. If it doesn't, it calls 
	 * methods to create a new table and fill the table with the contents of 
	 * the csv file.
	 */
	private void checkTableStatus() {
		try {
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, "customers", null);
			if(! (tables.next())){
				createTable();
				fillTable();
			}
			else {
				id = getNextAvailableId(); // set id to num of table items.
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the database table for the customers.
	 */
	private void createTable() {
		String table = "CREATE TABLE " + tableName + "(" +
				     "id INT(4) NOT NULL, " +
				     "firstName VARCHAR(20) NOT NULL, " + 
				     "lastName VARCHAR(20) NOT NULL, " + 
				     "address VARCHAR(50) NOT NULL, " + 
				     "postalCode CHAR(7) NOT NULL, " + 
				     "phoneNumber CHAR(12) NOT NULL, " + 
				     "customerType CHAR(1) NOT NULL, " + 
				     "PRIMARY KEY ( id ))";
		try{
			pStat = conn.prepareStatement(table);
			pStat.executeUpdate();
			pStat.close();
			System.out.println("Created Table " + tableName);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes the customer database table.
	 */
	public void removeTable()
	{
		String table = "DROP TABLE " + tableName;
		try{
			pStat = conn.prepareStatement(table);
			pStat.executeUpdate();
			pStat.close();
			System.out.println("Removed Table " + tableName);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Fills the customer database table with the contents of the csv file.
	 */
	private void fillTable()
	{
		try{
			Scanner sc = new Scanner(new FileReader(dataFile));
			while(sc.hasNext()) {
				String customerInfo[] = sc.nextLine().split(";");
				addItem(new Customer( newId(), customerInfo[0], customerInfo[1], 
						customerInfo[2], customerInfo[3], customerInfo[4], 
						customerInfo[5].charAt(0)));
			}
			sc.close();
			System.out.println("Table " + tableName + 
					" filled with data from file " + dataFile);
		}
		catch(FileNotFoundException e)
		{
			System.err.println("File " + dataFile + " Not Found!");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Modifies a customer in the database table, or if the customer is new, calls
	 * a method to add the new customer.
	 * @param c The customer to add to the database table.
	 */
	public void addCustomer(Customer c) {
		String sql = "SELECT * FROM " + tableName + " WHERE ID=?";
		try {
			pStat = conn.prepareStatement(sql);
			pStat.setInt(1, c.getId());
			ResultSet res = pStat.executeQuery();
			if(res.next()) {
				sql = "UPDATE " + tableName + " SET firstName = ?, " +
					"lastName = ?, address = ?, postalCode = ?, " +
					"phoneNumber = ?, customerType = ? WHERE id = ?";
				pStat = conn.prepareStatement(sql);
				pStat.setString(1, c.getFirstName());
				pStat.setString(2, c.getLastName());
				pStat.setString(3, c.getAddress());
				pStat.setString(4, c.getPostalCode());
				pStat.setString(5, c.getPhoneNumber());
				pStat.setString(6, String.valueOf(c.getCustomerType()));
				pStat.setInt(7, c.getId());
				pStat.executeUpdate();
				pStat.close();
			}
			else {
				addItem(c);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes a customer from the database, and calls a method to remove the customer 
	 * from the search results.
	 * @param id The ID of the customer to delete.
	 */
	public void deleteCustomer(int id) {
		String sql = "DELETE FROM " + tableName + " WHERE ID=?";
		try {
			pStat = conn.prepareStatement(sql);
			pStat.setInt(1, id);
			pStat.executeUpdate();
			pStat.close();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		removeFromSearchResult(id);
		
	}
	
	/**
	 * Removes a customer from the search results.
	 * @param id The ID of the customer to remove.
	 */
	private void removeFromSearchResult(int id) {
		for(int i = 0; i < customerSearchResult.size(); i++) {
			if (customerSearchResult.get(i).getId() == id) {
				customerSearchResult.remove(i);
			}
		}
	}

	/**
	 * Adds a customer to the database.
	 * @param c The customer to add to the database.
	 */
	private void addItem(Customer c)
	{
		String sql = "INSERT INTO " + tableName +
				" VALUES (?, ?, ?, ?, ?, ?, ?)";
		try{
			pStat = conn.prepareStatement(sql);
			pStat.setInt(1, id);
			pStat.setString(2, c.getFirstName());
			pStat.setString(3, c.getLastName());
			pStat.setString(4, c.getAddress());
			pStat.setString(5, c.getPostalCode());
			pStat.setString(6, c.getPhoneNumber());
			pStat.setString(7, String.valueOf(c.getCustomerType()));
			pStat.executeUpdate();
			pStat.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Searches for a customer based on the customer ID.
	 * @param id The customer ID.
	 * @return The customer.
	 */
	public Customer searchCustomer(int id)
	{
		String sql = "SELECT * FROM " + tableName + " WHERE ID=?";
		ResultSet customers;
		try {
			pStat = conn.prepareStatement(sql);
			pStat.setInt(1, id);
			customers = pStat.executeQuery();
			customerSearchResult.clear();
			if (customers.next()) {
				customerSearchResult.add(new Customer(customers.getInt("id"),
								customers.getString("firstName"), 
								customers.getString("lastName"), 
								customers.getString("address"), 
								customers.getString("postalCode"), 
								customers.getString("phoneNumber"), 
								customers.getString("customerType").charAt(0)));
			}
			pStat.close();
		} catch (SQLException e) { e.printStackTrace(); }
		
		return null;
	}
	
	/**
	 * Searches for customers based on last name.
	 * @param lName Customer's last name.
	 * @return The customer.
	 */
	public Customer searchCustomer(String lName)
	{
		String sql = "SELECT * FROM " + tableName + " WHERE lastName=?";
		ResultSet customers;
		try {
			pStat = conn.prepareStatement(sql);
			pStat.setString(1, lName);
			customers = pStat.executeQuery();
			customerSearchResult.clear();
			while (customers.next()) {
				customerSearchResult.add(new Customer(customers.getInt("id"),
								customers.getString("firstName"), 
								customers.getString("lastName"), 
								customers.getString("address"), 
								customers.getString("postalCode"), 
								customers.getString("phoneNumber"), 
								customers.getString("customerType").charAt(0)));
			}
			pStat.close();
		} catch (SQLException e) { e.printStackTrace(); }
		
		return null;
	}
	
	/**
	 * Searches for customers based on the customer type.
	 * @param cType Customer type, either residential (R) or commercial (C).
	 * @return The customer.
	 */
	public Customer searchCustomer(char cType)
	{
		String sql = "SELECT * FROM " + tableName + " WHERE customerType=?";
		ResultSet customers;
		try {
			pStat = conn.prepareStatement(sql);
			pStat.setString(1, String.valueOf(cType));
			customers = pStat.executeQuery();
			customerSearchResult.clear();
			while (customers.next()) {
				customerSearchResult.add(new Customer(customers.getInt("id"),
								customers.getString("firstName"), 
								customers.getString("lastName"), 
								customers.getString("address"), 
								customers.getString("postalCode"), 
								customers.getString("phoneNumber"), 
								customers.getString("customerType").charAt(0)));
			}
			pStat.close();
		} catch (SQLException e) { e.printStackTrace(); }
		
		return null;
	}
	
	/**
	 * Generates a customer ID.
	 * @return the incremented customer ID.
	 */
	private static int newId() {
		return ++id;
	}
	
	/**
	 * Wrapper method that provides the next available customer ID for a new customer.
	 * @return The customer ID.
	 */
	public int getNextAvailId() {
		return getNextAvailableId();
	}
	
	/**
	 * Provides the next available customer ID for a new customer.
	 * @return The customer ID.
	 */
	private int getNextAvailableId() {
		int id = 0;
		String sql = "SELECT id FROM " + tableName;
		try {
			pStat = conn.prepareStatement(sql);
			ResultSet res = pStat.executeQuery();
			int i = 0;
			while(res.next()) {
				i++;
				if(res.getInt("id") != i) {
					id = i;
					CMSModel.id = id;
					return id;
				}
			}
			pStat.close();
			res.close();
			id = i + 1;
			CMSModel.id = id;
			return id;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	// ********* GETTERS AND SETTERS ***************
	
	/**
	 * @return The array list of customer search results.
	 */
	public ArrayList<Customer> getCustomerSearchResult() {
		return customerSearchResult;
	}
	
}

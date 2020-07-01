package server.model;

import java.io.Serializable;

/**
 * Provides fields, getters and setters, and toString methods for a client.
 * @author Jason Paul
 * @version 1.0
 * @since Nov 15, 2018
 *
 */
public class Customer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String firstName;
	private String lastName;
	private String address;
	private String postalCode;
	private String phoneNumber;
	private char customerType;
	
	/**
	 * Constructs a customer, with all fields set with the constructor arguments.
	 * @param id The customer's ID.
	 * @param firstName The customer's first name.
	 * @param lastName The customer's last name.
	 * @param address The customer's address.
	 * @param postalCode The customer's postal code.
	 * @param phoneNumber The customer's phone number.
	 * @param customerType The type of customer (residential (R), or commercial (C)).
	 */
	public Customer(int id, String firstName, String lastName, String address, 
			String postalCode, String phoneNumber, char customerType) {
		this.id = id;
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setAddress(address);
		this.setPostalCode(postalCode);
		this.setPhoneNumber(phoneNumber);
		this.setCustomerType(customerType);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public char getCustomerType() {
		return customerType;
	}

	public void setCustomerType(char customerType) {
		this.customerType = customerType;
	}

	@Override
	public String toString()
	{
		String customer = this.id + " " + this.firstName + " " + 
						this.lastName + " " + 
						this.address + " " + this.postalCode + " " + 
						this.phoneNumber + " " + this.customerType;
		return customer;
	}
	
	/**
	 * Provides a shortened string representation of the customer.
	 * @return The customer as a string.
	 */
	public String toShortString()
	{
		String customer = this.id + " " + this.firstName + " " + 
						this.lastName + " " + this.customerType;
		return customer;
	}
	
}

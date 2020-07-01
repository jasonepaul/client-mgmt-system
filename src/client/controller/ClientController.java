package client.controller;

import java.io.IOException;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import client.view.CMSView;
import server.model.Customer;

/**
 * Serves as the controller for communication between the view controller and 
 * the server.
 * @author Jason Paul
 * @version 1.0
 * @since Feb 13, 2020
 */
public class ClientController{
	
	/**
	 * The view (GUI).
	 */
	private Socket socket;
	private ObjectOutputStream objectOut;
	private ObjectInputStream objectIn;
	
	/**
	 * Constructs a ClientController and sets the server name and port number for
	 * a socket connection.
	 * @param serverName Name of the server.
	 * @param portNumber Port number of the server.
	 * @param theView The client GUI class
	 */
	public ClientController (String serverName, int portNumber) {
		
		try {
			socket = new Socket (serverName, portNumber);
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			objectIn = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Customer> searchCustomer(int id) {
		ArrayList<Customer> customerSearchResult = null;
		try {
			objectOut.writeObject("searchid:"+id);
			customerSearchResult = (ArrayList<Customer>) objectIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return customerSearchResult;
	}
	
	public ArrayList<Customer> searchCustomer(String lName) {
		ArrayList<Customer> customerSearchResult = null;
		try {
			objectOut.writeObject("searchlname:"+lName);
			customerSearchResult = (ArrayList<Customer>) objectIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return customerSearchResult;
	}
	
	public ArrayList<Customer> searchCustomerByType(char cType) {
		ArrayList<Customer> customerSearchResult = null;
		try {
			objectOut.writeObject("searchtype:"+cType);
			customerSearchResult = (ArrayList<Customer>) objectIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return customerSearchResult;
	}
	
	public Customer getCustomerSearchResult(int index) {
		Customer customer = null;
		try {
			objectOut.writeObject("getcustomer:"+index);
			customer = (Customer) objectIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return customer;
	}
	
	public void addCustomer(Customer c) {
		try {
			objectOut.writeObject(c);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public ArrayList<Customer> deleteCustomer(int idAsInt) {
		ArrayList<Customer> customerSearchResult = null;
		try {
			objectOut.writeObject("deletecustomer:"+idAsInt);
			customerSearchResult = (ArrayList<Customer>) objectIn.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return customerSearchResult;
	}
	
	public int getNewId() {
		int id = 0;
		try {
			objectOut.writeObject("getnewid");
			id = objectIn.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * Entry point for the client.
	 * @param args not used.
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		ClientController clientCtr = new ClientController ("localhost", 9090);
		CMSView theView = new CMSView("Customer Management Screen");
		CMSController cmsCtr = new CMSController(theView, clientCtr);
	}
}

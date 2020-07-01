package server.controller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

import server.model.*;

/**
 * Provides a server side model controller for the customer manager system, 
 * which receives requests from the client controller and send back requested data.
 * @author Jason Paul
 * @version 1.0
 * @since Feb 8, 2020
 */
public class ModelController implements Runnable{

	private CMSModel model;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;

	/**
	 * Assigns object input and output streams and a server model based on the 
	 * supplied arguments. 
	 * @param objectIn 
	 * @param objectOut
	 * @param aModel
	 */
	public ModelController(ObjectInputStream objectIn, ObjectOutputStream objectOut, 
			CMSModel aModel) {
		this.objectIn = objectIn;
		this.objectOut = objectOut;
		this.model = aModel;
	}
		
	/**
	 * Runs a thread for the connected client.
	 */
	@Override
    public  void run() {
		
		Object inFromClient = null;
		
			while(true) {
			
				try {
					inFromClient = objectIn.readObject();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}  catch (SocketException se) {
					break;
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				if(inFromClient instanceof String) {
					String stringFromClient = (String) inFromClient;
								
					if(stringFromClient.startsWith("searchid:")) {
						searchForId(stringFromClient);
					}
					
					if(stringFromClient.startsWith("searchlname:")) {
						searchForLastName(stringFromClient);
					}
					
					if(stringFromClient.startsWith("searchtype:")) {
						searchForType(stringFromClient);
					}
					
					if(stringFromClient.startsWith("getcustomer:")) {
						getCustomer(stringFromClient);
					}
					
					if(stringFromClient.startsWith("deletecustomer:")) {
						deleteCustomer(stringFromClient);
					}
					
					if(stringFromClient.startsWith("getnewid")) {
						getNewID();
					}
				}
				if(inFromClient instanceof Customer) {
					Customer customer = (Customer) inFromClient;
					model.addCustomer(customer);
				}
			}
        try {
			objectIn.close();
			objectOut.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves a unique ID from the model
	 */
	private void getNewID() {
		try {
			objectOut.writeInt(model.getNextAvailId());
			objectOut.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * deletes a customer from the DB and searchList
	 * @param stringFromClient the delete command that includes customer ID to 
	 * delete.
	 */
	private void deleteCustomer(String stringFromClient) {
		int index = Integer.parseInt(stringFromClient.substring(15));
		try {
			model.deleteCustomer(index);
			objectOut.writeObject(model.getCustomerSearchResult());
			objectOut.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves a customer from the searchList
	 * @param stringFromClient the command to retrieve the customer with index.
	 */
	private void getCustomer(String stringFromClient) {
		int index = Integer.parseInt(stringFromClient.substring(12));
		try {
			objectOut.writeObject(model.getCustomerSearchResult().get(index));
			objectOut.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * searches for all customers of a given type.  
	 * @param stringFromClient the command with the customer type to search for.
	 */
	private void searchForType(String stringFromClient) {
		char cType = stringFromClient.charAt(11);
		model.searchCustomer(cType);
		try {
			objectOut.writeObject(model.getCustomerSearchResult());
			objectOut.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * searches for all customers of a given last name.  
	 * @param stringFromClient the command with the customer last name to search for.
	 */
	private void searchForLastName(String stringFromClient) {
		String lname = stringFromClient.substring(12).trim();
		model.searchCustomer(lname);
		try {
			objectOut.writeObject(model.getCustomerSearchResult());
			objectOut.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * searches for customer of a given ID.  
	 * @param stringFromClient the command with the customer ID to search for.
	 */
	private void searchForId(String stringFromClient) {
		int id = Integer.parseInt(stringFromClient.substring(9, 10));
		model.searchCustomer(id);
		try {
			objectOut.writeObject(model.getCustomerSearchResult());
			objectOut.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CMSModel getModel() {
		return model;
	}

	public void setModel(CMSModel model) {
		this.model = model;
	}
}
